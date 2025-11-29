package org.game.service.impl;

import org.game.dto.ActionData;
import org.game.dto.GameStructure;
import org.game.dto.HeroDto;
import org.game.entity.Hero;
import org.game.entity.Landscape;
import org.game.entity.LandscapeType;
import org.game.entity.User;
import org.game.generator.LabirinthGenerator;
import org.game.repository.HeroRepository;
import org.game.repository.LandscapeRepository;
import org.game.repository.UserRepository;
import org.game.service.LabirinthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LabirinthServiceImpl implements LabirinthService {

    private final static int DELTA = 48;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LandscapeRepository landscapeRepository;

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private LabirinthGenerator labirinthGenerator;

    @Override
    @Transactional
    public GameStructure generateGameStructure(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            return null;
        }
        User user = optionalUser.get();
        UUID userId = user.getId();
        landscapeRepository.deleteByUserId(userId);
        heroRepository.deleteByUserId(userId);

        List<List<Integer>> battlefield = labirinthGenerator.createBattlefield(10, 20, 1, 1);
        int[][] array = labirinthGenerator.convertListToArray(battlefield);
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[0].length; ++j) {
                Landscape landscape = labirinthGenerator.createLandscapeFromNumber(i, j, array[i][j]);
                landscape.setUser(user);
                landscapeRepository.save(landscape);
            }
        }
        List<Hero> enemies = labirinthGenerator.createEnemies(battlefield, user);
        Hero hero = labirinthGenerator.createHero(48, 48, user, true);
        heroRepository.save(hero);
        heroRepository.saveAll(enemies);
        GameStructure gameStructure = new GameStructure();
        gameStructure.setStructure(battlefield);
        gameStructure.setEnemies(convertHeroListToHeroDtoList(enemies));
        gameStructure.setPlayer(convertHeroToHeroDto(hero));
        return gameStructure;
    }

    private HeroDto convertHeroToHeroDto(Hero hero) {
        if (hero == null) {
            return null;
        }
        HeroDto heroDto = new HeroDto();
        heroDto.setX(hero.getX());
        heroDto.setY(hero.getY());
        heroDto.setCadr(hero.getCadr());
        return heroDto;
    }

    private List<HeroDto> convertHeroListToHeroDtoList(List<Hero> heroList) {
        List<HeroDto> heroDtoList = new ArrayList<>();
        for (Hero hero : heroList) {
            heroDtoList.add(convertHeroToHeroDto(hero));
        }
        return heroDtoList;
    }

    @Override
    @Transactional
    public GameStructure getGameStructure(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            return null;
        }
        User user = optionalUser.get();
        UUID userId = user.getId();

        //Преобразование игрового поля
        modifyBattleField(userId);

        //Преобразование врагов
        modifyEnemies(userId);

        //Преобразование игрока
        modifyPlayer(userId);

        int[][] array = new int[10][20];
        List<Landscape> landscapes = landscapeRepository.findAllByUserId(userId);
        for (Landscape l : landscapes) {
            array[l.getI()][l.getJ()] = l.getType().getValue();
        }
        List<List<Integer>> battlefield = labirinthGenerator.convertArrayToList(array);
        java.util.Optional<Hero> heroOptional = heroRepository.getByUserIdAndPlayerTrue(userId);
        List<Hero> enemies = heroRepository.getByUserIdAndPlayerFalse(userId);
        GameStructure gameStructure = new GameStructure();
        gameStructure.setStructure(battlefield);
        gameStructure.setPlayer(convertHeroToHeroDto(heroOptional.isPresent() ? heroOptional.get() : null));
        gameStructure.setEnemies(convertHeroListToHeroDtoList(enemies));
        return gameStructure;
    }

    @Override
    @Transactional
    public void setAction(ActionData data) {
        String username = data.getUsername();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            return;
        }
        User user = optionalUser.get();
        UUID userId = user.getId();

        Optional<Hero> heroOptional = heroRepository.getByUserIdAndPlayerTrue(userId);
        if (!heroOptional.isPresent()) {
            return;
        }
        Hero hero = heroOptional.get();

        int[][] array = new int[10][20];
        List<Landscape> landscapes = landscapeRepository.findAllByUserId(userId);
        for (Landscape l : landscapes) {
            array[l.getI()][l.getJ()] = l.getType().getValue();
        }

        int i = (int) Math.floor(hero.getY() / 48.0);
        int j = (int) Math.floor(hero.getX() / 48.0);

        switch(data.getAction()) {
            case "down":
                if (i + 1 <= 9 && (array[i + 1][j] == 1 || array[i + 1][j] == 2)) {
                    hero.setY(hero.getY() + DELTA);
                    heroRepository.save(hero);
                }
                break;
            case "up":
                if (i - 1 >= 0 && (array[i - 1][j] == 1 || array[i - 1][j] == 2)) {
                    hero.setY(hero.getY() - DELTA);
                    heroRepository.save(hero);
                }
                break;
            case "right":
                if (j + 1 <= 19 && (array[i][j + 1] == 1 || array[i][j + 1] == 2)) {
                    hero.setX(hero.getX() + DELTA);
                    heroRepository.save(hero);
                }
                break;
            case "left":
                if (j - 1 >= 0 && (array[i][j - 1] == 1 || array[i][j - 1] == 2)) {
                    hero.setX(hero.getX() - DELTA);
                    heroRepository.save(hero);
                }
                break;
            case "space":
                Optional<Landscape> optionalLandscape = landscapeRepository.findByUserIdAndIAndJ(userId, i, j);
                if (optionalLandscape.isPresent()) {
                    Landscape landscape = optionalLandscape.get();
                    landscape.setType(LandscapeType.BOMB);
                    landscapeRepository.save(landscape);
                }
                break;
        }
    }

    private void modifyBattleField(UUID userId) {
        int[][] array = new int[10][20];
        List<Landscape> landscapes = landscapeRepository.findAllByUserId(userId);
        for (Landscape l : landscapes) {
            array[l.getI()][l.getJ()] = l.getType().getValue();
        }
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[0].length; ++j) {
                if (array[i][j] == 6) {
                    Optional<Landscape> optionalLandscape = landscapeRepository.findByUserIdAndIAndJ(userId, i, j);
                    if (optionalLandscape.isPresent()) {
                        Landscape landscape = optionalLandscape.get();
                        if (landscape.getTimerForBomb() != null && landscape.getTimerForBomb() > 0) {
                            landscape.setTimerForBomb(landscape.getTimerForBomb() - 1);
                            landscapeRepository.save(landscape);
                        } else if (landscape.getTimerForBomb() != null && landscape.getTimerForBomb() == 0) {
                            //Взрыв сделать
                            landscape.setType(LandscapeType.EXPLOSION);
                            landscapeRepository.save(landscape);
                            for (int k = 1; k < landscape.getSizeForBomb(); ++k) {
                                //Взрыв вверх
                                if (i - k >= 0) {
                                    Optional<Landscape> ls = landscapeRepository.findByUserIdAndIAndJ(userId, i - k, j);
                                    if (ls.isPresent()) {
                                        if (
                                                LandscapeType.BOMB.equals(ls.get().getType()) ||
                                                LandscapeType.GREENGRASS.equals(ls.get().getType()) ||
                                                LandscapeType.BLACKGRASS.equals(ls.get().getType())
                                        ) {
                                            ls.get().setType(LandscapeType.EXPLOSION);
                                            landscapeRepository.save(ls.get());
                                        } else if (LandscapeType.WALL.equals(ls.get().getType())) {
                                            ls.get().setType(LandscapeType.EXPLOSION);
                                            landscapeRepository.save(ls.get());
                                            break;
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }
                            for (int k = 1; k < landscape.getSizeForBomb(); ++k) {
                                //Взрыв вниз
                                if (i + k <= 9) {
                                    Optional<Landscape> ls = landscapeRepository.findByUserIdAndIAndJ(userId, i + k, j);
                                    if (ls.isPresent()) {
                                        if (
                                                LandscapeType.BOMB.equals(ls.get().getType()) ||
                                                LandscapeType.GREENGRASS.equals(ls.get().getType()) ||
                                                LandscapeType.BLACKGRASS.equals(ls.get().getType())
                                        ) {
                                            ls.get().setType(LandscapeType.EXPLOSION);
                                            landscapeRepository.save(ls.get());
                                        } if (LandscapeType.WALL.equals(ls.get().getType())) {
                                            ls.get().setType(LandscapeType.EXPLOSION);
                                            landscapeRepository.save(ls.get());
                                            break;
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }
                            for (int k = 1; k < landscape.getSizeForBomb(); ++k) {
                                //Взрыв влево
                                if (j - k >= 0) {
                                    Optional<Landscape> ls = landscapeRepository.findByUserIdAndIAndJ(userId, i, j - k);
                                    if (ls.isPresent()) {
                                        if (
                                                LandscapeType.BOMB.equals(ls.get().getType()) ||
                                                LandscapeType.GREENGRASS.equals(ls.get().getType()) ||
                                                LandscapeType.BLACKGRASS.equals(ls.get().getType())
                                        ) {
                                            ls.get().setType(LandscapeType.EXPLOSION);
                                            landscapeRepository.save(ls.get());
                                        } else if (LandscapeType.WALL.equals(ls.get().getType())) {
                                            ls.get().setType(LandscapeType.EXPLOSION);
                                            landscapeRepository.save(ls.get());
                                            break;
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }
                            for (int k = 1; k < landscape.getSizeForBomb(); ++k) {
                                //Взрыв вверх
                                if (j + k <= 19) {
                                    Optional<Landscape> ls = landscapeRepository.findByUserIdAndIAndJ(userId, i, j + k);
                                    if (ls.isPresent()) {
                                        if (
                                                LandscapeType.BOMB.equals(ls.get().getType()) ||
                                                LandscapeType.GREENGRASS.equals(ls.get().getType()) ||
                                                LandscapeType.BLACKGRASS.equals(ls.get().getType())
                                        ) {
                                            ls.get().setType(LandscapeType.EXPLOSION);
                                            landscapeRepository.save(ls.get());
                                        } else if (LandscapeType.WALL.equals(ls.get().getType())) {
                                            ls.get().setType(LandscapeType.EXPLOSION);
                                            landscapeRepository.save(ls.get());
                                            break;
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (array[i][j] == 5) {
                    //Взрыв убрать
                    Optional<Landscape> ls = landscapeRepository.findByUserIdAndIAndJ(userId, i, j);
                    if (ls.isPresent()) {
                        ls.get().setType(LandscapeType.BLACKGRASS);
                        landscapeRepository.save(ls.get());
                    }
                }
            }
        }
    }

    private void modifyPlayer(UUID userId) {
        int[][] array = new int[10][20];
        List<Landscape> landscapes = landscapeRepository.findAllByUserId(userId);
        for (Landscape l : landscapes) {
            array[l.getI()][l.getJ()] = l.getType().getValue();
        }
        Optional<Hero> heroOptional = heroRepository.getByUserIdAndPlayerTrue(userId);
        if (heroOptional.isPresent()) {
            Hero hero = heroOptional.get();
            int iHero = (int) Math.round(hero.getY() / 48.0);
            int jHero = (int) Math.round(hero.getX() / 48.0);
            List<Hero> enemies = heroRepository.getByUserIdAndPlayerFalse(userId);
            for (int k = 0; k < enemies.size(); ++k) {
                Hero enemy = enemies.get(k);
                int i = (int) Math.round(enemy.getY() / 48.0);
                int j = (int) Math.round(enemy.getX() / 48.0);
                if (iHero == i && jHero == j) {
                    heroRepository.delete(hero);
                    break;
                }
            }
            if (array[iHero][jHero] == 5) {
                heroRepository.delete(hero);
            }
        }
    }

    private void modifyEnemies(UUID userId) {
        int[][] array = new int[10][20];
        List<Landscape> landscapes = landscapeRepository.findAllByUserId(userId);
        for (Landscape l : landscapes) {
            array[l.getI()][l.getJ()] = l.getType().getValue();
        }
        List<Hero> enemies = heroRepository.getByUserIdAndPlayerFalse(userId);
        for (int k = 0; k < enemies.size(); ++k) {
            Hero enemy = enemies.get(k);
            int i = (int) Math.round(enemy.getY() / 48.0);
            int j = (int) Math.round(enemy.getX() / 48.0);

            if (enemy.getLeft()) {
                if (j - 1 >= 0 &&
                        (
                                array[i][j - 1] == 1 ||
                                        array[i][j - 1] == 2 ||
                                        array[i][j - 1] == 5 ||
                                        array[i][j - 1] == 6
                        )) {
                    enemy.setX(enemy.getX() - DELTA);
                } else {
                    enemy.setLeft(false);
                    enemy.setRight(true);
                }
            } else if (enemy.getRight()) {
                if (j + 1 <= 19 &&
                        (
                                array[i][j + 1] == 1 ||
                                        array[i][j + 1] == 2 ||
                                        array[i][j + 1] == 5 ||
                                        array[i][j + 1] == 6
                        )) {
                    enemy.setX(enemy.getX() + DELTA);
                } else {
                    enemy.setLeft(true);
                    enemy.setRight(false);
                }
            } else if (enemy.getUp()) {
                if (i - 1 >= 0 &&
                        (
                                array[i - 1][j] == 1 ||
                                        array[i - 1][j] == 2 ||
                                        array[i - 1][j] == 5 ||
                                        array[i - 1][j] == 6
                        )) {
                    enemy.setY(enemy.getY() - DELTA);
                } else {
                    enemy.setUp(false);
                    enemy.setDown(true);
                }
            } else if (enemy.getDown()) {
                if (i + 1 <= 9 &&
                        (
                                array[i + 1][j] == 1 ||
                                        array[i + 1][j] == 2 ||
                                        array[i + 1][j] == 5 ||
                                        array[i + 1][j] == 6
                        )) {
                    enemy.setY(enemy.getY() + DELTA);
                } else {
                    enemy.setDown(false);
                    enemy.setUp(true);
                }
            }
            if (array[i][j] == 5) {
                heroRepository.delete(enemy);
            } else {
                heroRepository.save(enemy);
            }
        }
    }


}
