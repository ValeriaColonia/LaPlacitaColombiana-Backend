package com.laplacitacolombiana.springboot.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VentaDTO {
    private String idTransaccion;
    private String estadoPago;
    private Long userId;
    private LocalDateTime fecha;
    private String ciudad;
    private String direccion;
    private BigDecimal domicilio;
    private BigDecimal subtotal;
    private Integer cantidad;
    private String details;
    private List<DetalleDTO> productos;

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getCiudad() { return ciudad; }

    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDireccion() { return direccion; }

    public void setDireccion(String direccion) { this.direccion = direccion; }

    public BigDecimal getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(BigDecimal domicilio) {
        this.domicilio = domicilio;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getCantidad() { return cantidad; }

    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public List<DetalleDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<DetalleDTO> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "VentaDTO{" +
                "userId=" + userId +
                '}';
    }

}
