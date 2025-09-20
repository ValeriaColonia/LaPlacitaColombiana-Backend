package com.laplacitacolombiana.springboot.config;

import com.laplacitacolombiana.springboot.model.Categoria;
import com.laplacitacolombiana.springboot.model.Rol;
import com.laplacitacolombiana.springboot.model.Usuario;
import com.laplacitacolombiana.springboot.repository.CategoriaRepository;
import com.laplacitacolombiana.springboot.repository.RolRepository;
import com.laplacitacolombiana.springboot.repository.UsuarioRepository;
import com.laplacitacolombiana.springboot.service.CategoriaService;
import com.laplacitacolombiana.springboot.service.RolService;
import com.laplacitacolombiana.springboot.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer {

    private final RolService rolService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    public DataInitializer(RolService rolService, CategoriaService categoriaService,
                           UsuarioService usuarioService) {
        this.rolService = rolService;
        this.categoriaService = categoriaService;
        this.usuarioService = usuarioService;
    }

    @PostConstruct
    public void initRoles() {
        crearRolSiNoExiste("ADMIN");
        crearRolSiNoExiste("CLIENTE");
    }

    @PostConstruct
    public void initCategorias() {
        crearCategoriaSiNoExiste("Cafe", "Productos de cafe");
        crearCategoriaSiNoExiste("Cacao", "Productos de cacao");
        crearCategoriaSiNoExiste("Cerveza", "Productos de cerveza");
    }

    @PostConstruct
    public void initUsuarioAdmin() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellido("Perez");
        usuario.setEmail("juanperez@mail.com");
        usuario.setPassword("123456");
        usuario.setTelefono("3012314545");
        usuario.setCiudad("Bogotá");
        usuario.setDepartamento("Cundinamarca");

        Optional<Rol> rol = rolService.findByNombre("ADMIN");
        usuario.setRol(rol.get());
        crearAdminSiNoExiste(usuario);
    }

    private void crearRolSiNoExiste(String nombreRol) {
        if (rolService.findByNombre(nombreRol).isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            rolService.save(rol);
            System.out.println("Rol creado: " + nombreRol);
        }
    }

    private void crearCategoriaSiNoExiste(String nombreCategoria, String descCategoria) {
        if (categoriaService.findByNombre(nombreCategoria).isEmpty()) {
            Categoria categoria = new Categoria();
            categoria.setNombre(nombreCategoria);
            categoria.setDescripcion(descCategoria);
            categoriaService.save((categoria));
        }
    }

    private void crearAdminSiNoExiste(Usuario usuario) {
        usuarioService.registerUser(usuario);
    }
}