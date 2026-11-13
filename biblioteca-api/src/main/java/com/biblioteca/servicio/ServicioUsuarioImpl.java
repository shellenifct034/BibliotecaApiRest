package com.biblioteca.servicio;
import com.biblioteca.excepciones.UsuarioConPrestamosException;
import com.biblioteca.excepciones.UsuarioDuplicadoException;
import com.biblioteca.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.repositorio.PrestamoRepository;
import com.biblioteca.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ServicioUsuarioImpl implements ServicioUsuario {
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;
    public ServicioUsuarioImpl(UsuarioRepository usuarioRepository, PrestamoRepository prestamoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }
    @Override
    public void agregarUsuario(Usuario usuario) throws UsuarioDuplicadoException {
        if (usuarioRepository.existsByIdUsuario(usuario.getIdUsuario())) {
            throw new UsuarioDuplicadoException("Ya existe un usuario con ese ID.");
        }
        usuarioRepository.save(usuario);
    }
    @Override
    public void actualizarUsuario(String idUsuario, Usuario usuario) throws UsuarioNoEncontradoException {
        Usuario existente = usuarioRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + idUsuario + " no encontrado."));
        existente.setNombre(usuario.getNombre());
        existente.setDireccion(usuario.getDireccion());
        existente.setTelefono(usuario.getTelefono());
        usuarioRepository.save(existente);
    }
    @Override
    public void eliminarUsuario(String idUsuario) throws UsuarioNoEncontradoException, UsuarioConPrestamosException {
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + idUsuario + " no encontrado."));
        if (prestamoRepository.existsByUsuarioIdUsuario(idUsuario)) {
            throw new UsuarioConPrestamosException("No se puede eliminar un usuario con historial de préstamos.");
        }
        usuarioRepository.delete(usuario);
    }
    @Override
    public Usuario buscarPorId(String idUsuario) throws UsuarioNoEncontradoException {
        return usuarioRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + idUsuario + " no encontrado."));
    }
    @Override
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }
    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    @Override
    public void activarUsuario(String idUsuario) throws UsuarioNoEncontradoException {
        Usuario usuario = buscarPorId(idUsuario);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }
    @Override
    public void desactivarUsuario(String idUsuario) throws UsuarioNoEncontradoException {
        Usuario usuario = buscarPorId(idUsuario);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
}
