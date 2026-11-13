package com.biblioteca.repositorio;

import com.biblioteca.modelo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    Optional<Prestamo> findByLibroIsbnAndFechaDevolucionRealIsNull(String isbn);
    List<Prestamo> findByFechaDevolucionRealIsNull();
    boolean existsByLibroIsbn(String isbn);
    boolean existsByUsuarioIdUsuario(String idUsuario);
}
