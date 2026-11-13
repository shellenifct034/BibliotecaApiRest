package com.biblioteca.controlador;
import com.biblioteca.excepciones.LibroNoEncontradoException;
import com.biblioteca.excepciones.LibroPrestadoException;
import com.biblioteca.excepciones.PrestamoNoEncontradoException;
import com.biblioteca.excepciones.UsuarioInactivoException;
import com.biblioteca.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.servicio.ServicioPrestamo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/prestamos")
@Tag(name = "Préstamos", description = "Gestión de préstamos de libros")
public class PrestamoController {
    private final ServicioPrestamo servicioPrestamo;
    public PrestamoController(ServicioPrestamo servicioPrestamo) {
        this.servicioPrestamo = servicioPrestamo;
    }
    @GetMapping
    @Operation(summary = "Listar préstamos activos")
    public ResponseEntity<List<Prestamo>> listarPrestamosActivos() {
        return ResponseEntity.ok(servicioPrestamo.listarPrestamosActivos());
    }
    @PostMapping
    @Operation(summary = "Prestar un libro a un usuario")
    @ApiResponse(responseCode = "201", description = "Préstamo registrado")
    @ApiResponse(responseCode = "404", description = "Libro o usuario no encontrado")
    @ApiResponse(responseCode = "409", description = "Libro ya prestado o usuario inactivo")
    public ResponseEntity<String> prestarLibro(@RequestParam String isbn, @RequestParam String idUsuario)
            throws LibroNoEncontradoException, UsuarioNoEncontradoException,
            LibroPrestadoException, UsuarioInactivoException {
        servicioPrestamo.prestarLibro(isbn, idUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Préstamo registrado correctamente.");
    }
    @PutMapping("/devolver/{isbn}")
    @Operation(summary = "Devolver un libro")
    @ApiResponse(responseCode = "200", description = "Devolución registrada")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado o sin préstamo activo")
    public ResponseEntity<String> devolverLibro(@PathVariable String isbn)
            throws LibroNoEncontradoException, PrestamoNoEncontradoException {
        servicioPrestamo.devolverLibro(isbn);
        return ResponseEntity.ok("Devolución registrada correctamente.");
    }
}
