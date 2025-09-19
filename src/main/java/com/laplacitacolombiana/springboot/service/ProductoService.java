package com.laplacitacolombiana.springboot.service;

import com.laplacitacolombiana.springboot.dto.ProductoDTO;
import com.laplacitacolombiana.springboot.model.Producto;
import com.laplacitacolombiana.springboot.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    public List<Producto> findAll() { return productoRepository.findAll(); }
    public Producto save(Producto producto) { return productoRepository.save(producto); }
    public Optional<Producto> findById(Long id) { return productoRepository.findById(id); }
    public void delete(Long id) { productoRepository.deleteById(id); }

    // === NUEVOS: catálogo paginado/filtrado y en DTO ===
    public Page<ProductoDTO> listarCatalogo(String categoria, Pageable pageable) {
        Page<Producto> page;
        if (categoria != null && !categoria.isBlank() && !"todos".equalsIgnoreCase(categoria)) {
            page = productoRepository.findByCategoria_NombreIgnoreCaseAndEstadoAndStockGreaterThan(
                    categoria, Producto.EstadoProducto.DISPONIBLE, 0, pageable);
        } else {
            page = productoRepository.findByEstadoAndStockGreaterThan(
                    Producto.EstadoProducto.DISPONIBLE, 0, pageable);
        }
        return page.map(ProductoDTO::of);
    }

    public Optional<ProductoDTO> detalleDTO(Long id){
        return productoRepository.findById(id).map(ProductoDTO::of);
    }
}

