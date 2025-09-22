package com.laplacitacolombiana.springboot.controller;

import com.laplacitacolombiana.springboot.JwtUtil;
import com.laplacitacolombiana.springboot.dto.UsuarioDto;
import com.laplacitacolombiana.springboot.model.Usuario;
import com.laplacitacolombiana.springboot.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@Valid @RequestBody Usuario usuario) {
//        usuarioService.registerUser(usuario);
//        return ResponseEntity.ok("Usuario registrado con éxito");
//    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        // Continue with registration logic
        usuarioService.registerUser(usuario);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/loginConDTO")
    public ResponseEntity<?> loginConDTO(@RequestBody UsuarioDto usuarioDto) {
        try {
            UserDetails userDetails = usuarioService.loadUserByUsername(usuarioDto.getEmail());
            if (passwordEncoder.matches(usuarioDto.getPassword(), userDetails.getPassword())) {
                String token = jwtUtil.generateToken(userDetails.getUsername());

                // Buscar usuario en BD para devolver info adicional
                Usuario usuario = usuarioService.findByEmail(usuarioDto.getEmail());

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("usuario", usuario.getNombre());
                response.put("id", usuario.getId());
                response.put("rolID", usuario.getRol().getId());

                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(401).body("Credenciales inválidas");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }


    @GetMapping("/resource")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getProtectedResource() {
        return ResponseEntity.ok("Este es un recurso protegido!");
    }

}

