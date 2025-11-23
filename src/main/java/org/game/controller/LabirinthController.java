package org.game.controller;

import org.game.dto.GameStructure;
import org.game.dto.JwtAuthenticationRequestDto;
import org.game.generator.LabirinthGenerator;
import org.game.service.LabirinthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LabirinthController {

    @Autowired
    private LabirinthService labirinthService;

    @GetMapping(value = "/labirinth")
    public GameStructure getGameStructure(@RequestHeader("Authorization") String authToken) {
        System.out.println(authToken);
        return labirinthService.getGameStructure("w");
    }

    @GetMapping(value = "/labirinth/battle")
    public GameStructure getGameStructureRest(@RequestHeader("Authorization") String authToken) {
        System.out.println(authToken);
        return labirinthService.getGameStructure("w");
    }

    @PostMapping(value = "/labirinth")
    public GameStructure generateGameStructure(@RequestHeader("Authorization") String authToken) {
        System.out.println(authToken);
        return labirinthService.generateGameStructure("w");
    }

    private GameStructure createGameStructure() {
        GameStructure gameStructure = new GameStructure();
//        List<List<Integer>> battlefield = createBattleField();
//        List<List<Integer>> battlefield = labirinthGenerator.createBattlefield(10, 20, 1, 1);
//        gameStructure.setStructure(battlefield);
        return gameStructure;
    }

    private List<List<Integer>> createBattleField() {
        return List.of(
                List.of(1,2,2,3,3,4,1,1,1,7,1,1,1,1,1,1,1,1,1,1),
                List.of(1,1,1,7,7,7,7,7,7,7,1,1,1,1,1,1,1,1,1,1),
                List.of(7,1,1,4,4,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
                List.of(7,7,1,1,1,7,1,1,7,1,1,1,1,1,1,1,1,1,1,1),
                List.of(4,4,4,4,1,1,4,3,3,3,1,1,1,1,2,2,2,2,2,1),
                List.of(7,7,7,7,3,3,7,7,7,7,1,1,1,1,1,1,1,1,1,1),
                List.of(7,1,1,1,1,1,7,7,7,7,1,1,1,1,1,1,1,1,1,1),
                List.of(7,7,1,7,7,1,7,7,1,7,1,1,1,1,6,1,1,1,1,1),
                List.of(7,1,1,1,7,1,7,7,1,7,1,1,1,1,1,1,1,1,1,1),
                List.of(1,1,7,1,7,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
        );
    }
}
