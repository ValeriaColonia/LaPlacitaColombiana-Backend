package com.laplacitacolombiana.springboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 150, message = "La dirección no debe exceder 150 caracteres")
    private String direccion;

    @NotNull(message = "La cantidad de productos es obligatorio")
    @DecimalMin(value = "0", inclusive = true, message = "La cantidad no puede ser negativo")
    private Integer cantidad;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal no puede ser negativo")
    private BigDecimal subtotal;

    @NotNull(message = "El descuento es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento no puede ser negativo")
    private BigDecimal descuento;

    @NotNull(message = "Los impuestos son obligatorios")
    @DecimalMin(value = "0.0", inclusive = true, message = "Los impuestos no pueden ser negativos")
    private BigDecimal impuestos;

    @NotNull(message = "El valor del domicilio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El domicilio no puede ser negativo")
    private BigDecimal domicilio;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El total no puede ser negativo")
    private BigDecimal total;

    @Column(name = "fecha_registro", columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El ID de transacción es obligatorio")
    private String idTransaccion;

    @NotBlank(message = "El estado del pago es obligatorio")
    private String estadoPago;

    @NotBlank(message = "La respuesta JSON es obligatoria")
    @Lob
    private String responseJson;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DetalleVenta> detalles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getCantidad() { return cantidad; }

    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(BigDecimal domicilio) {
        this.domicilio = domicilio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getEstadoPago() { return estadoPago; }

    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}


