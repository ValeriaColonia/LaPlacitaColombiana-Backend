package com.laplacitacolombiana.springboot.service;

import com.laplacitacolombiana.springboot.model.Categoria;
import com.laplacitacolombiana.springboot.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() { return categoriaRepository.findAll(); }

    public Categoria save(Categoria categoria) { return categoriaRepository.save(categoria); }

    public Optional<Categoria> findById(Long id) { return categoriaRepository.findById(id); }

    public void delete(Long id) { categoriaRepository.deleteById(id); }

    public Optional<Categoria> findByNombre(String nombre) { return categoriaRepository.findByNombre(nombre); }
}

