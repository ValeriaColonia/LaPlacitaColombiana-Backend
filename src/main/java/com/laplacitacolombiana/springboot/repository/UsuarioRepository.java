package com.laplacitacolombiana.springboot.repository;

import com.laplacitacolombiana.springboot.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}



