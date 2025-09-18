package com.laplacitacolombiana.springboot.controller;
import com.laplacitacolombiana.springboot.model.Proveedor;
import com.laplacitacolombiana.springboot.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {
    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<Proveedor> getAll() {
        return proveedorService.findAll();
    }

    @GetMapping("{id}")
    public Optional<Proveedor> getById(@PathVariable Long id) {
        return proveedorService.findById(id);
    }

    @PostMapping("/crear")
    public Proveedor create(@RequestBody Proveedor proveedor) {
        return proveedorService.save(proveedor);
    }

    @PutMapping("/editar/{id}")
    public Proveedor update(@PathVariable Long id, @Valid @RequestBody Proveedor proveedor) {
        proveedor.setId(id);
        return proveedorService.save(proveedor);
    }

    @DeleteMapping("/borrar/{id}")
    public void delete(@PathVariable Long id) {
        proveedorService.delete(id);
    }
}

