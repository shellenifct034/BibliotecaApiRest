package com.biblioteca.controlador;
import com.biblioteca.excepciones.UsuarioConPrestamosException;
import com.biblioteca.excepciones.UsuarioDuplicadoException;
import com.biblioteca.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.servicio.ServicioUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Operaciones sobre los usuarios de la biblioteca")
public class UsuarioController {
    private final ServicioUsuario servicioUsuario;
    public UsuarioController(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }
    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(servicioUsuario.listarTodos());
    }
    @GetMapping("/{idUsuario}")
    @Operation(summary = "Buscar un usuario por su ID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable String idUsuario) throws UsuarioNoEncontradoException {
        return ResponseEntity.ok(servicioUsuario.buscarPorId(idUsuario));
    }
    @GetMapping("/buscar")
    @Operation(summary = "Buscar usuarios por nombre (parcial, sin distinguir mayúsculas)")
    public ResponseEntity<List<Usuario>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(servicioUsuario.buscarPorNombre(nombre));
    }
    @PostMapping
    @Operation(summary = "Agregar un nuevo usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese ID")
    public ResponseEntity<Usuario> agregarUsuario(@Valid @RequestBody Usuario usuario) throws UsuarioDuplicadoException {
        servicioUsuario.agregarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
    @PutMapping("/{idUsuario}")
    @Operation(summary = "Actualizar un usuario existente")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable String idUsuario, @RequestBody Usuario usuario)
            throws UsuarioNoEncontradoException {
        servicioUsuario.actualizarUsuario(idUsuario, usuario);
        return ResponseEntity.ok(servicioUsuario.buscarPorId(idUsuario));
    }
    @DeleteMapping("/{idUsuario}")
    @Operation(summary = "Eliminar un usuario")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado")
    @ApiResponse(responseCode = "409", description = "El usuario tiene historial de préstamos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String idUsuario)
            throws UsuarioNoEncontradoException, UsuarioConPrestamosException {
        servicioUsuario.eliminarUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{idUsuario}/activar")
    @Operation(summary = "Activar un usuario")
    @ApiResponse(responseCode = "200", description = "Usuario activado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<Usuario> activarUsuario(@PathVariable String idUsuario) throws UsuarioNoEncontradoException {
        servicioUsuario.activarUsuario(idUsuario);
        return ResponseEntity.ok(servicioUsuario.buscarPorId(idUsuario));
    }
    @PutMapping("/{idUsuario}/desactivar")
    @Operation(summary = "Desactivar un usuario")
    @ApiResponse(responseCode = "200", description = "Usuario desactivado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable String idUsuario) throws UsuarioNoEncontradoException {
        servicioUsuario.desactivarUsuario(idUsuario);
        return ResponseEntity.ok(servicioUsuario.buscarPorId(idUsuario));
    }
}
