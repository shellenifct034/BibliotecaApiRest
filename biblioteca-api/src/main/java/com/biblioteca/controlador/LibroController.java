package com.biblioteca.controlador;
import com.biblioteca.excepciones.LibroDuplicadoException;
import com.biblioteca.excepciones.LibroNoEncontradoException;
import com.biblioteca.excepciones.LibroPrestadoException;
import com.biblioteca.modelo.Libro;
import com.biblioteca.servicio.ServicioLibro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/libros")
@Tag(name = "Libros", description = "Operaciones sobre el catálogo de libros")
public class LibroController {
    private final ServicioLibro servicioLibro;
    public LibroController(ServicioLibro servicioLibro) {
        this.servicioLibro = servicioLibro;
    }
    @GetMapping
    @Operation(summary = "Listar todos los libros")
    public ResponseEntity<List<Libro>> listarTodos() {
        return ResponseEntity.ok(servicioLibro.listarTodos());
    }
    @GetMapping("/{isbn}")
    @Operation(summary = "Buscar un libro por ISBN")
    @ApiResponse(responseCode = "200", description = "Libro encontrado")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<Libro> buscarPorIsbn(@PathVariable String isbn) throws LibroNoEncontradoException {
        return ResponseEntity.ok(servicioLibro.buscarPorIsbn(isbn));
    }
    @GetMapping("/buscar")
    @Operation(summary = "Buscar libros por título (parcial, sin distinguir mayúsculas)")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(servicioLibro.buscarPorTitulo(titulo));
    }
    @PostMapping
    @Operation(summary = "Agregar un nuevo libro")
    @ApiResponse(responseCode = "201", description = "Libro creado")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "409", description = "Ya existe un libro con ese ISBN")
    public ResponseEntity<Libro> agregarLibro(@Valid @RequestBody Libro libro) throws LibroDuplicadoException {
        servicioLibro.agregarLibro(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libro);
    }
    @PutMapping("/{isbn}")
    @Operation(summary = "Actualizar un libro existente")
    @ApiResponse(responseCode = "200", description = "Libro actualizado")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable String isbn, @RequestBody Libro libro)
            throws LibroNoEncontradoException {
        servicioLibro.actualizarLibro(isbn, libro);
        return ResponseEntity.ok(servicioLibro.buscarPorIsbn(isbn));
    }
    @DeleteMapping("/{isbn}")
    @Operation(summary = "Eliminar un libro")
    @ApiResponse(responseCode = "204", description = "Libro eliminado")
    @ApiResponse(responseCode = "409", description = "El libro está prestado o tiene historial de préstamos")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<Void> eliminarLibro(@PathVariable String isbn)
            throws LibroNoEncontradoException, LibroPrestadoException {
        servicioLibro.eliminarLibro(isbn);
        return ResponseEntity.noContent().build();
    }
}
