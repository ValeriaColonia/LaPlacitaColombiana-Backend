package com.laplacitacolombiana.springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Prueba home");
    }

    @GetMapping("/catalogo")
    public ResponseEntity<String> catalogo() {
        return ResponseEntity.ok("catalogo");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("login");
    }
}