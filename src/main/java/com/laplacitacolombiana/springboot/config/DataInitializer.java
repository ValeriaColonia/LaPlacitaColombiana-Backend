package com.laplacitacolombiana.springboot.config;

import com.laplacitacolombiana.springboot.model.Rol;
import com.laplacitacolombiana.springboot.repository.RolRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RolRepository rolRepository;

    public DataInitializer(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @PostConstruct
    public void initRoles() {
        crearRolSiNoExiste("ADMIN");
        crearRolSiNoExiste("CLIENTE");
    }

    private void crearRolSiNoExiste(String nombreRol) {
        if (rolRepository.findByNombre(nombreRol).isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre(nombreRol);
            rolRepository.save(rol);
            System.out.println("Rol creado: " + nombreRol);
        }
    }
}