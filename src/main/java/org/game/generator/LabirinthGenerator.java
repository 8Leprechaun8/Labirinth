package org.game.generator;

import org.game.entity.Hero;
import org.game.entity.Landscape;
import org.game.entity.LandscapeType;
import org.game.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.lang.Math.floor;

@Component
public class LabirinthGenerator {

    public List<List<Integer>> createBattlefield(int m, int n, int xFirst, int yFirst) {
        int[][] walls = new int[m][n];
        int[][] wasOnFieldOrCurrent = new int[m][n];
        for(int i = 0; i < m; ++i) {
            for(int j = 0; j < n; ++j) {
                if(i % 2 == 0 && j % 2 == 0) {
                    walls[i][j] = 1;
                }
            }
        }
        wasOnFieldOrCurrent[0][0] = -1;
        int[] point = new int[3]; point[0] = 0; point[1] = 0; point[2] = -1;
        Stack<int[]> stack = new Stack();
        stack.push(point);
        while(!stack.empty()) {
            int[] point1 = stack.pop();
            point1[2] = 1;
            List<int[]> list = new ArrayList<>();
            if(point1[0] - 2 >= 0 && wasOnFieldOrCurrent[point1[0] - 2][point1[1]] == 0) {
                int[] point2 = new int[3]; point2[0] = point1[0] - 2; point2[1] = point1[1]; point2[2] = 0;
                list.add(point2);
            }
            if(point1[0] + 2 < m && wasOnFieldOrCurrent[point1[0] + 2][point1[1]] == 0) {
                int[] point2 = new int[3]; point2[0] = point1[0] + 2; point2[1] = point1[1]; point2[2] = 0;
                list.add(point2);
            }
            if(point1[1] - 2 >= 0 && wasOnFieldOrCurrent[point1[0]][point1[1] - 2] == 0) {
                int[] point2 = new int[3]; point2[0] = point1[0]; point2[1] = point1[1] - 2; point2[2] = 0;
                list.add(point2);
            }
            if(point1[1] + 2 < n && wasOnFieldOrCurrent[point1[0]][point1[1] + 2] == 0) {
                int[] point2 = new int[3]; point2[0] = point1[0]; point2[1] = point1[1] + 2; point2[2] = 0;
                list.add(point2);
            }
            if (!list.isEmpty()) {
                stack.push(point1);
                double sluchChislo = Math.random() * list.size();
                int intSluch = (int) floor(sluchChislo);
                int[] point2 = list.get(intSluch);
                walls[(point1[0] + point2[0]) / 2][(point1[1] + point2[1]) / 2] = 1;
                point2[2] = -1;
                wasOnFieldOrCurrent[point2[0]][point2[1]] = -1;
                stack.push(point2);
            }
        }
        walls[xFirst][yFirst] = 1;
        if(xFirst - 1 >= 0) {
            walls[xFirst - 1][yFirst] = 1;
        }
        if(xFirst + 1 < m) {
            walls[xFirst + 1][yFirst] = 1;
        }
        if(yFirst - 1 >= 0) {
            walls[xFirst][yFirst - 1] = 1;
        }
        if(yFirst + 1 < n) {
            walls[xFirst][yFirst + 1] = 1;
        }
        for(int i = 0; i < m; ++i) {
            for(int j = 0; j < n; ++j) {
                if (walls[i][j] == 0) {
                    double chislo = Math.random();
                    if (chislo < 0.1) {
                        walls[i][j] = 3;
                    } else if (chislo < 0.2) {
                        walls[i][j] = 4;
                    } else if (chislo < 0.3) {
                        walls[i][j] = 9;
                    } else {
                        walls[i][j] = 7;
                    }
                }
            }
        }

        List<List<Integer>> result = convertArrayToList(walls);
        return result;
    }

    public List<Hero> createEnemies(List<List<Integer>> battlefield, User user) {
        List<Hero> heroes = new ArrayList<>();
        int[][] array = convertListToArray(battlefield);
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[0].length; ++j) {
                if (array[i][j] == 1 && i > 2 && j > 2) {
                    double chislo = Math.random();
                    if (chislo < 0.1) {
                        Hero hero = createHero(j*48, i*48, user, false);
                        if (j - 1 >= 0 &&
                                (
                                        array[i][j - 1] == 1 ||
                                        array[i][j - 1] == 2 ||
                                        array[i][j - 1] == 5 ||
                                        array[i][j - 1] == 6
                                )) {
                            hero.setLeft(true);
                        } else if (j + 1 <= 19 &&
                                (
                                        array[i][j + 1] == 1 ||
                                        array[i][j + 1] == 2 ||
                                        array[i][j + 1] == 5 ||
                                        array[i][j + 1] == 6
                                )) {
                            hero.setRight(true);
                        } else if (i - 1 >= 0 &&
                                (
                                        array[i - 1][j] == 1 ||
                                        array[i - 1][j] == 2 ||
                                        array[i - 1][j] == 5 ||
                                        array[i - 1][j] == 6
                                )) {
                            hero.setUp(true);
                        } else if (i + 1 <= 9 &&
                                (
                                        array[i + 1][j] == 1 ||
                                        array[i + 1][j] == 2 ||
                                        array[i + 1][j] == 5 ||
                                        array[i + 1][j] == 6
                                )) {
                            hero.setDown(true);
                        }
                        heroes.add(hero);
                    }
                }
            }
        }
        return heroes;
    }

    public Hero createHero(int xFirst, int yFirst, User user, boolean player) {
        Hero hero = new Hero();
        hero.setActive(true);
        hero.setCadr(0);
        hero.setUp(false);
        hero.setDown(false);
        hero.setLeft(false);
        hero.setRight(false);
        hero.setPlayer(player);
        hero.setX(xFirst);
        hero.setY(yFirst);
        hero.setUser(user);
        return hero;
    }

    public List<List<Integer>> convertArrayToList(int[][] array) {
        List<List<Integer>> result = new ArrayList<>();
        for(int i = 0; i < array.length; ++i) {
            List listRow = new ArrayList();
            for(int j = 0; j < array[0].length; ++j) {
                listRow.add(array[i][j]);
                System.out.println(array[i][j]);
            }
            result.add(listRow);
        }
        return result;
    }

    public int[][] convertListToArray(List<List<Integer>> list) {
        int[][] result = new int[list.size()][list.get(0).size()];
        for(int i = 0; i < list.size(); ++i) {
            List listRow = list.get(i);
            for(int j = 0; j < list.get(0).size(); ++j) {
                result[i][j] = (int) listRow.get(j);
            }
        }
        return result;
    }

    public Landscape createLandscapeFromNumber(int i, int j, Integer number) {
        Landscape landscape = new Landscape();
        landscape.setI(i);
        landscape.setJ(j);
        if(number == 1) {
            landscape.setType(LandscapeType.GREENGRASS);
        }
        if(number == 2) {
            landscape.setType(LandscapeType.BLACKGRASS);
        }
        if(number == 3) {
            landscape.setType(LandscapeType.ROCK);
        }
        if(number == 4) {
            landscape.setType(LandscapeType.WATER);
        }
        if(number == 5) {
            landscape.setType(LandscapeType.EXPLOSION);
        }
        if(number == 6) {
            landscape.setType(LandscapeType.BOMB);
            landscape.setSizeForBomb(3);
            landscape.setTimerForBomb(8);
        }
        if(number == 7) {
            landscape.setType(LandscapeType.WALL);
        }
        if(number == 9) {
            landscape.setType(LandscapeType.TREE);
        }
        return landscape;
    }

    public Integer createNumberFromLandscape(Landscape landscape) {
        return landscape.getType().getValue();
    };

}
