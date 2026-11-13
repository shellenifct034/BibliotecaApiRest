package com.biblioteca.controlador;
import com.biblioteca.excepciones.LibroNoEncontradoException;
import com.biblioteca.excepciones.LibroPrestadoException;
import com.biblioteca.excepciones.PrestamoNoEncontradoException;
import com.biblioteca.excepciones.UsuarioInactivoException;
import com.biblioteca.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.servicio.ServicioPrestamo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {
    private final ServicioPrestamo servicioPrestamo;
    public PrestamoController(ServicioPrestamo servicioPrestamo) {
        this.servicioPrestamo = servicioPrestamo;
    }
    @GetMapping
    public ResponseEntity<List<Prestamo>> listarPrestamosActivos() {
        return ResponseEntity.ok(servicioPrestamo.listarPrestamosActivos());
    }
    @PostMapping
    public ResponseEntity<String> prestarLibro(@RequestParam String isbn, @RequestParam String idUsuario)
            throws LibroNoEncontradoException, UsuarioNoEncontradoException,
            LibroPrestadoException, UsuarioInactivoException {
        servicioPrestamo.prestarLibro(isbn, idUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Préstamo registrado correctamente.");
    }
    @PutMapping("/devolver/{isbn}")
    public ResponseEntity<String> devolverLibro(@PathVariable String isbn)
            throws LibroNoEncontradoException, PrestamoNoEncontradoException {
        servicioPrestamo.devolverLibro(isbn);
        return ResponseEntity.ok("Devolución registrada correctamente.");
    }
}
