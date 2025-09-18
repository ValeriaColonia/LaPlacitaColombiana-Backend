package com.laplacitacolombiana.springboot.controller;

import com.laplacitacolombiana.springboot.model.Categoria;
import com.laplacitacolombiana.springboot.model.Producto;
import com.laplacitacolombiana.springboot.model.Proveedor;
import com.laplacitacolombiana.springboot.service.CategoriaService;
import com.laplacitacolombiana.springboot.service.ProductoService;
import com.laplacitacolombiana.springboot.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProveedorService proveedorService;

    // Obtener todos los productos
    @GetMapping
    public List<Producto> getAll() {
        return productoService.findAll();
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear producto
    @PostMapping("/crear")
    public ResponseEntity<?> guardarProducto(
            @RequestParam("nombre") String nombre,
            @RequestParam("categoria") Long categoriaId,
            @RequestParam("proveedor") Long proveedorId,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("stock") Integer stock,
            @RequestParam("presentacion") Integer presentacion,
            @RequestParam("unidadMedida") Producto.UnidadMedida unidadMedida,
            @RequestParam("imagen") MultipartFile imagen) {

        try {
            Categoria categoria = categoriaService.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            Proveedor proveedor = proveedorService.findById(proveedorId)
                    .orElseThrow(() -> new RuntimeException("PRoveedor no encontrada"));

            // Generar nombre único
            String fileName = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();

            // Carpeta donde guardar (ejemplo: local o EC2)
            Path uploadPath = Paths.get("uploads/productos/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Guardar archivo físico
            Files.copy(imagen.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            // crear producto
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setProveedor(proveedor);
            producto.setCategoria(categoria);
            producto.setPrecio(precio);
            producto.setDescripcion(descripcion);
            producto.setStock(stock);
            producto.setPresentacion(presentacion);
            producto.setUnidadMedida(unidadMedida);
            producto.setImagen("/img/productos/" + fileName); // solo guardamos el nombre

            productoService.save(producto);

            return ResponseEntity.ok(producto);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el producto");
        }
    }

    // Editar producto
    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editar(@PathVariable Long id, @RequestBody Producto p) {
        Optional<Producto> existente = productoService.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto no encontrado");

        } else {
            existente.get().setNombre(p.getNombre());
            existente.get().setPrecio(p.getPrecio());
            existente.get().setDescripcion(p.getDescripcion());
            existente.get().setStock(p.getStock());
            existente.get().setPresentacion(p.getPresentacion());
            existente.get().setUnidadMedida(p.getUnidadMedida());
            existente.get().setImagen(p.getImagen());
            existente.get().setFechaRegistro(p.getFechaRegistro());
            existente.get().setEstado(p.getEstado());
            existente.get().setCategoria(p.getCategoria());
            existente.get().setProveedor(p.getProveedor());

            productoService.save(existente.orElse(null));
            return ResponseEntity.ok("El producto se editó correctamente");
        }
    }

    // Eliminar producto
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }
}


