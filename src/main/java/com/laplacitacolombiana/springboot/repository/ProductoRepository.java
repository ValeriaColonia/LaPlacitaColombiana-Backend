package com.laplacitacolombiana.springboot.repository;

import com.laplacitacolombiana.springboot.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Page<Producto> findByCategoria_NombreIgnoreCaseAndEstadoAndStockGreaterThan(
            String categoriaNombre, Producto.EstadoProducto estado, int stockMin, Pageable pageable);

    Page<Producto> findByEstadoAndStockGreaterThan(Producto.EstadoProducto estado, int stockMin, Pageable pageable);
}
