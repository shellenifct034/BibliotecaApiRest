package com.biblioteca.servicio;
import com.biblioteca.excepciones.LibroDuplicadoException;
import com.biblioteca.excepciones.LibroNoEncontradoException;
import com.biblioteca.modelo.Libro;
import com.biblioteca.repositorio.LibroRepository;
import com.biblioteca.repositorio.PrestamoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class ServicioLibroImplTest {
    @Mock
    private LibroRepository libroRepository;
    @Mock
    private PrestamoRepository prestamoRepository;
    @InjectMocks
    private ServicioLibroImpl servicioLibro;
    @Test
    void agregarLibro_guarda_libro_si_isbn_no_existe() throws LibroDuplicadoException {
        Libro libro = new Libro("123", "El Quijote", "Cervantes", 1605);
        given(libroRepository.existsByIsbn("123")).willReturn(false);
        servicioLibro.agregarLibro(libro);
        assertThat(libro.isPrestado()).isFalse();
    }
    @Test
    void agregarLibro_lanza_excepcion_si_isbn_duplicado() {
        Libro libro = new Libro("123", "El Quijote", "Cervantes", 1605);
        given(libroRepository.existsByIsbn("123")).willReturn(true);
        assertThatThrownBy(() -> servicioLibro.agregarLibro(libro))
                .isInstanceOf(LibroDuplicadoException.class)
                .hasMessageContaining("Ya existe un libro con ese ISBN");
    }
    @Test
    void buscarPorIsbn_devuelve_libro_si_existe() throws LibroNoEncontradoException {
        Libro libro = new Libro("123", "El Quijote", "Cervantes", 1605);
        given(libroRepository.findByIsbn("123")).willReturn(Optional.of(libro));
        Libro resultado = servicioLibro.buscarPorIsbn("123");
        assertThat(resultado.getTitulo()).isEqualTo("El Quijote");
    }
    @Test
    void buscarPorIsbn_lanza_excepcion_si_no_existe() {
        given(libroRepository.findByIsbn("999")).willReturn(Optional.empty());
        assertThatThrownBy(() -> servicioLibro.buscarPorIsbn("999"))
                .isInstanceOf(LibroNoEncontradoException.class)
                .hasMessageContaining("999");
    }
}
