package org.game.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "auth", url = "http://localhost:8086")
public interface AuthFeignClient {

    @GetMapping("/rest/user")
    String getUsername(@RequestHeader("Authorization") String token);
}
