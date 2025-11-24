package org.game.controller;

import org.game.dto.GameStructure;
import org.game.feignclient.AuthFeignClient;
import org.game.service.LabirinthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LabirinthController {

    @Autowired
    private AuthFeignClient authFeignClient;

    @Autowired
    private LabirinthService labirinthService;

    @GetMapping(value = "/labirinth")
    public GameStructure getGameStructure(@RequestHeader("Authorization") String authToken) {
        String username = authFeignClient.getUsername(authToken);
        return labirinthService.getGameStructure(username);
    }

    @GetMapping(value = "/labirinth/battle")
    public GameStructure getGameStructureRest(@RequestHeader("Authorization") String authToken) {
        String username = authFeignClient.getUsername(authToken);
        return labirinthService.getGameStructure(username);
    }

    @PostMapping(value = "/labirinth")
    public GameStructure generateGameStructure(@RequestHeader("Authorization") String authToken) {
        String username = authFeignClient.getUsername(authToken);
        return labirinthService.generateGameStructure(username);
    }
}
