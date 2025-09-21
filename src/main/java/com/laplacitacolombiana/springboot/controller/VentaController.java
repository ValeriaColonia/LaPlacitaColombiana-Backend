package com.laplacitacolombiana.springboot.controller;

import com.laplacitacolombiana.springboot.dto.VentaDTO;
import com.laplacitacolombiana.springboot.model.Venta;
import com.laplacitacolombiana.springboot.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> getAll() {
        return ventaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Venta> getById(@PathVariable Long id) {
        return ventaService.findById(id);
    }

    @GetMapping("/factura/{idTransaccion}")
    public ResponseEntity<?> mostrarFacturaPorTxn(@PathVariable String idTransaccion) {
        try {
            Venta factura = ventaService.findByTransactionId(idTransaccion);
            if (factura == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\":\"Factura no encontrada\"}");
            }
            return ResponseEntity.ok(factura);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace(); // visible en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al procesar la venta\"}");
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> create(@RequestBody @Valid VentaDTO venta) {
        try {
            Venta factura = ventaService.save(venta);
            // Retornamos solo el id en un JSON
            Map<String, Object> response = new HashMap<>();
            response.put("ventaId", factura.getId());
            return ResponseEntity.ok(factura);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // para ver el error en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la venta: " + e.getMessage());
        }

    }


//    @PutMapping("/editar/{id}")
//    public Venta update(@PathVariable Long id, @RequestBody @Valid Venta venta) {
//        venta.setId(id);
//        return ventaService.save(venta);
//    }

    @DeleteMapping("/borrar/{id}")
    public void delete(@PathVariable Long id) {
        ventaService.delete(id);
    }
}

