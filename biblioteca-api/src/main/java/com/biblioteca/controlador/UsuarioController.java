package com.biblioteca.controlador;
import com.biblioteca.excepciones.UsuarioConPrestamosException;
import com.biblioteca.excepciones.UsuarioDuplicadoException;
import com.biblioteca.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.servicio.ServicioUsuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    private final ServicioUsuario servicioUsuario;
    public UsuarioController(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(servicioUsuario.listarTodos());
    }
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable String idUsuario) throws UsuarioNoEncontradoException {
        return ResponseEntity.ok(servicioUsuario.buscarPorId(idUsuario));
    }
    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(servicioUsuario.buscarPorNombre(nombre));
    }
    @PostMapping
    public ResponseEntity<Usuario> agregarUsuario(@Valid @RequestBody Usuario usuario) throws UsuarioDuplicadoException {
        servicioUsuario.agregarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
    @PutMapping("/{idUsuario}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable String idUsuario, @RequestBody Usuario usuario)
            throws UsuarioNoEncontradoException {
        servicioUsuario.actualizarUsuario(idUsuario, usuario);
        return ResponseEntity.ok(servicioUsuario.buscarPorId(idUsuario));
    }
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String idUsuario)
            throws UsuarioNoEncontradoException, UsuarioConPrestamosException {
        servicioUsuario.eliminarUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{idUsuario}/activar")
    public ResponseEntity<Usuario> activarUsuario(@PathVariable String idUsuario) throws UsuarioNoEncontradoException {
        servicioUsuario.activarUsuario(idUsuario);
        return ResponseEntity.ok(servicioUsuario.buscarPorId(idUsuario));
    }
    @PutMapping("/{idUsuario}/desactivar")
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable String idUsuario) throws UsuarioNoEncontradoException {
        servicioUsuario.desactivarUsuario(idUsuario);
        return ResponseEntity.ok(servicioUsuario.buscarPorId(idUsuario));
    }
}
