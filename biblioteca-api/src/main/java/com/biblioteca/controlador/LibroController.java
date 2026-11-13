package com.biblioteca.controlador;
import com.biblioteca.excepciones.LibroDuplicadoException;
import com.biblioteca.excepciones.LibroNoEncontradoException;
import com.biblioteca.excepciones.LibroPrestadoException;
import com.biblioteca.modelo.Libro;
import com.biblioteca.servicio.ServicioLibro;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {
    private final ServicioLibro servicioLibro;
    public LibroController(ServicioLibro servicioLibro) {
        this.servicioLibro = servicioLibro;
    }
    @GetMapping
    public ResponseEntity<List<Libro>> listarTodos() {
        return ResponseEntity.ok(servicioLibro.listarTodos());
    }
    @GetMapping("/{isbn}")
    public ResponseEntity<Libro> buscarPorIsbn(@PathVariable String isbn) throws LibroNoEncontradoException {
        return ResponseEntity.ok(servicioLibro.buscarPorIsbn(isbn));
    }
    @GetMapping("/buscar")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(servicioLibro.buscarPorTitulo(titulo));
    }
    @PostMapping
    public ResponseEntity<Libro> agregarLibro(@Valid @RequestBody Libro libro) throws LibroDuplicadoException {
        servicioLibro.agregarLibro(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libro);
    }
    @PutMapping("/{isbn}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable String isbn, @RequestBody Libro libro)
            throws LibroNoEncontradoException {
        servicioLibro.actualizarLibro(isbn, libro);
        return ResponseEntity.ok(servicioLibro.buscarPorIsbn(isbn));
    }
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable String isbn)
            throws LibroNoEncontradoException, LibroPrestadoException {
        servicioLibro.eliminarLibro(isbn);
        return ResponseEntity.noContent().build();
    }
}
