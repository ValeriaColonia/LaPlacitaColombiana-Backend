package com.laplacitacolombiana.springboot.controller;

import com.laplacitacolombiana.springboot.dto.ProductoDTO;
import com.laplacitacolombiana.springboot.model.Categoria;
import com.laplacitacolombiana.springboot.model.Producto;
import com.laplacitacolombiana.springboot.model.Proveedor;
import com.laplacitacolombiana.springboot.service.CategoriaService;
import com.laplacitacolombiana.springboot.service.ProductoService;
import com.laplacitacolombiana.springboot.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@CrossOrigin(origins = "http://127.0.0.1:5501")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> editar(@PathVariable Long id,
                                         @RequestParam("nombre") String nombre,
                                         @RequestParam("categoria") Long categoriaId,
                                         @RequestParam("proveedor") Long proveedorId,
                                         @RequestParam("precio") BigDecimal precio,
                                         @RequestParam("descripcion") String descripcion,
                                         @RequestParam("stock") Integer stock,
                                         @RequestParam("presentacion") Integer presentacion,
                                         @RequestParam("unidadMedida") Producto.UnidadMedida unidadMedida,
                                         @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                         @RequestParam("estado") Producto.EstadoProducto estado) {

        // Buscar producto existente
        Optional<Producto> existente = productoService.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto no encontrado");
        }

        // Validar entidades relacionadas
        Optional<Categoria> categoria = categoriaService.findById(categoriaId);
        if (categoria.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Categoría no encontrada");
        }

        Optional<Proveedor> proveedor = proveedorService.findById(proveedorId);
        if (proveedor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Proveedor no encontrado");
        }

        Producto producto = existente.get();

        // Actualizar campos básicos
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setPresentacion(presentacion);
        producto.setUnidadMedida(unidadMedida);
        producto.setEstado(estado);
        producto.setCategoria(categoria.get());
        producto.setProveedor(proveedor.get());
        // fechaRegistro se mantiene automáticamente

        // Procesar imagen si se proporciona
        try {
            if (imagen != null && !imagen.isEmpty()) {
                // Validaciones básicas
                String contentType = imagen.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("El archivo debe ser una imagen");
                }

                String fileName = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/productos/");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(imagen.getInputStream(), uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);

                producto.setImagen("/img/productos/" + fileName);
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la imagen: " + e.getMessage());
        }

        productoService.save(producto);
        return ResponseEntity.ok("El producto se editó correctamente");
    }

//    // Eliminar producto
//    @DeleteMapping("/borrar/{id}")
//    public ResponseEntity<String> eliminar(@PathVariable Long id) {
//        productoService.delete(id);
//        return ResponseEntity.ok("Producto eliminado correctamente");
//    }

    //Eliminar cambiar estado a no disponible
    @PatchMapping("/borrar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<Producto> existente = productoService.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto no encontrado");

        } else {
            existente.get().setEstado(Producto.EstadoProducto.NODISPONIBLE);

            productoService.save(existente.get());
            return ResponseEntity.ok("El producto se editó correctamente");
        }
    }

    // Catálogo público (DTO)
    @GetMapping("/catalogo")
    public Page<ProductoDTO> catalogo(
            @RequestParam(required = false) String categoria,
            @PageableDefault(size = 12, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        // usa los métodos nuevos del service (listarCatalogo)
        return productoService.listarCatalogo(categoria, pageable);
    }

    // Detalle DTO (opcional, para página de detalle)
    @GetMapping("/catalogo/{id}")
    public ResponseEntity<ProductoDTO> detalleDTO(@PathVariable Long id) {
        return productoService.detalleDTO(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


