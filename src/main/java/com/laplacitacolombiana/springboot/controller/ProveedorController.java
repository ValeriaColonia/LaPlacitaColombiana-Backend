package com.laplacitacolombiana.springboot.controller;
import com.laplacitacolombiana.springboot.model.Producto;
import com.laplacitacolombiana.springboot.model.Proveedor;
import com.laplacitacolombiana.springboot.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

//    @DeleteMapping("/borrar/{id}")
//    public void delete(@PathVariable Long id) {
//        proveedorService.delete(id);
//    }



    //Eliminar cambiar estado a no disponible
    @PatchMapping("/borrar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Optional<Proveedor> existente = proveedorService.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Proveedor no encontrado");

        } else {
            existente.get().setEstado(Proveedor.EstadoProveedor.NODISPONIBLE);

            proveedorService.save(existente.get());
            return ResponseEntity.ok("El productor se editó correctamente");
        }
    }

}

