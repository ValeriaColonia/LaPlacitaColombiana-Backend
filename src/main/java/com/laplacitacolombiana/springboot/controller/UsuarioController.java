package com.laplacitacolombiana.springboot.controller;

import com.laplacitacolombiana.springboot.dto.UpdateUsuarioDTO;
import com.laplacitacolombiana.springboot.model.Rol;
import com.laplacitacolombiana.springboot.model.Usuario;
import com.laplacitacolombiana.springboot.repository.RolRepository;
import com.laplacitacolombiana.springboot.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.findById(id);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
            return ResponseEntity.ok(usuarioOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody @Valid Usuario usuario) {
        try {
            Usuario newUser = usuarioService.registerUser(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping("/editar/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateUsuarioDTO dto) {
        Usuario existing = usuarioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (dto.getNombre() != null) existing.setNombre(dto.getNombre());
        if (dto.getApellido() != null) existing.setApellido(dto.getApellido());
        if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
        if (dto.getTelefono() != null) existing.setTelefono(dto.getTelefono());
        if (dto.getCiudad() != null) existing.setCiudad(dto.getCiudad());
        if (dto.getDepartamento() != null) existing.setDepartamento(dto.getDepartamento());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getRolId() != null) {
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            existing.setRol(rol);
        }

        return ResponseEntity.ok(usuarioService.save(existing));
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



