package com.biblioteca.repositorio;


import com.biblioteca.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByIdUsuario(String idUsuario);


    boolean existsByIdUsuario(String idUsuario);


    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
}
