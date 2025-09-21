package com.laplacitacolombiana.springboot.service;

import com.laplacitacolombiana.springboot.model.Usuario;
import com.laplacitacolombiana.springboot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registerUser(Usuario usuario) {
        // Validar campos obligatorios
        if (usuario.getEmail() == null || usuario.getPassword() == null ||
                usuario.getNombre() == null || usuario.getApellido() == null ||
                usuario.getTelefono() == null || usuario.getRol() == null)  {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        // Verificar si el usuario ya existe
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new RuntimeException("El correo ya existe");
        }

        Usuario newUser = new Usuario();
        newUser.setEmail(usuario.getEmail());
        newUser.setPassword(passwordEncoder.encode(usuario.getPassword()));
        newUser.setNombre(usuario.getNombre());
        newUser.setApellido(usuario.getApellido());
        newUser.setTelefono(usuario.getTelefono());
        newUser.setRol(usuario.getRol());

        return usuarioRepository.save(newUser);
    }

    // Métdo de carga de usuario implementado desde UserDetailsService
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // Crear autoridades basadas en el rol
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRol().getNombre().toUpperCase()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
}
