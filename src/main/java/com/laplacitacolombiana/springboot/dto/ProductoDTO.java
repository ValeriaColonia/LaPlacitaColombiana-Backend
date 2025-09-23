package com.laplacitacolombiana.springboot.dto;

import com.laplacitacolombiana.springboot.model.Producto;
import java.math.BigDecimal;

public record ProductoDTO(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        String imagen,
        String categoria,
        String proveedor,
        Integer presentacion,
        String unidadMedida,  // "GR","KG","ML","L"
        Integer stock,
        String estado         // "DISPONIBLE"/"NODISPONIBLE"
) {
    public static ProductoDTO of(Producto p){
        return new ProductoDTO(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getImagen(),
                p.getCategoria()!=null ? p.getCategoria().getNombre() : null,
                p.getProveedor()!=null ? p.getProveedor().getRazonSocial() : null,
                p.getPresentacion(),
                p.getUnidadMedida()!=null ? p.getUnidadMedida().name() : null,
                p.getStock(),
                p.getEstado()!=null ? p.getEstado().name() : null
        );
    }
}