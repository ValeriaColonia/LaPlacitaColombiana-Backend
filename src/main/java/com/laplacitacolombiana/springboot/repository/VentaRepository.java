package com.laplacitacolombiana.springboot.repository;

import com.laplacitacolombiana.springboot.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    Optional<Venta> findByIdTransaccion(String idTransaccion);
}

