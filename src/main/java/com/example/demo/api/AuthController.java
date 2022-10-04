package com.example.demo.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.model.User;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> loginHandler(@RequestBody User user) {
        ResponseEntity<Map<String, Object>> response =
            new ResponseEntity<>(HttpStatus.OK);
        HashMap<String, Object> responseMap = new HashMap<>();
        if(user.getUsername().equals("jperez") &&
            user.getPassword().equals("123456")) {
            Date currentTime = new Date();
            Date expirationTime = new Date(currentTime.getTime() + (5 * 60 * 1000));
            Date refreshExpTime = new Date(currentTime.getTime() + (8 * 60 * 1000));
            String token = JWT.create()
                            .withExpiresAt(expirationTime)
                            .withIssuer("ucb")
                            .withClaim("username", user.getUsername())
                            .withIssuedAt(currentTime)
                            .sign(Algorithm.HMAC256("secret"));
            String refreshToken = JWT.create()
                            .withExpiresAt(refreshExpTime)
                            .withIssuer("ucb")
                            .withIssuedAt(currentTime)
                            .sign(Algorithm.HMAC256("secret"));
            responseMap.put("token", token);
            responseMap.put("refreshToken", refreshToken);
            response = new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            responseMap.put("message", "Credenciales incorrectas");
            response = new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
        }
        return response;
    }
}
