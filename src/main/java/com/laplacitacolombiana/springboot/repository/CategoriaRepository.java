package com.laplacitacolombiana.springboot.repository;

import com.laplacitacolombiana.springboot.model.Categoria;
import com.laplacitacolombiana.springboot.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
}

