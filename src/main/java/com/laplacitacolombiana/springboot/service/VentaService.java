package com.laplacitacolombiana.springboot.service;

import com.laplacitacolombiana.springboot.dto.VentaDTO;
import com.laplacitacolombiana.springboot.model.DetalleVenta;
import com.laplacitacolombiana.springboot.model.Producto;
import com.laplacitacolombiana.springboot.model.Usuario;
import com.laplacitacolombiana.springboot.model.Venta;
import com.laplacitacolombiana.springboot.repository.ProductoRepository;
import com.laplacitacolombiana.springboot.repository.UsuarioRepository;
import com.laplacitacolombiana.springboot.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    public List<Venta> findAll() { return ventaRepository.findAll(); }

    public Venta save(VentaDTO dto) {
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");
        }

        Venta venta = new Venta();
        venta.setIdTransaccion(dto.getIdTransaccion());
        venta.setEstadoPago(dto.getEstadoPago());
        venta.setFecha(dto.getFecha());
        venta.setCiudad(dto.getCiudad());
        venta.setDireccion(dto.getDireccion());
        venta.setSubtotal(dto.getSubtotal());
        venta.setDomicilio(dto.getDomicilio());
        venta.setDescuento(BigDecimal.valueOf(0));
        venta.setImpuestos(BigDecimal.valueOf(0));
        venta.setTotal(dto.getSubtotal().add(dto.getDomicilio()));
        venta.setMetodoPago("PAYPAL");
        venta.setResponseJson(dto.getDetails());

        Usuario usuario = usuarioRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        venta.setUsuario(usuario);

        List<DetalleVenta> detalles = dto.getProductos().stream().map(p -> {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setCantidad(p.getCantidad());
            detalle.setPrecioUnitario(p.getPrecioUnitario());
            detalle.setSubtotal(p.getCantidad() * p.getPrecioUnitario());

            Producto producto = productoRepository.findById(p.getProductoID())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            detalle.setProducto(producto);
            detalle.setVenta(venta);
            return detalle;
        }).collect(Collectors.toList());

        venta.setDetalles(detalles);
        return ventaRepository.save(venta);
    }

    public Optional<Venta> findById(Long id) { return ventaRepository.findById(id); }

    public Venta findByTransactionId(String transactionId) {
        return ventaRepository.findByIdTransaccion(transactionId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada para txn " + transactionId));
    }

    public void delete(Long id) { ventaRepository.deleteById(id); }
}

