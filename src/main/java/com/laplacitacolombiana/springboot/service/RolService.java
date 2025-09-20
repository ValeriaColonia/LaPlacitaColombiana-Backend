package com.laplacitacolombiana.springboot.service;

import com.laplacitacolombiana.springboot.model.Rol;
import com.laplacitacolombiana.springboot.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Optional<Rol> findById(Long id) {
        return rolRepository.findById(id);
    }
    public Optional<Rol> findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    public Rol save(Rol rol) { return rolRepository.save(rol); }

    public void delete(Long id) {
        rolRepository.deleteById(id);
    }

}

