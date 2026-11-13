package com.biblioteca.servicio;
import com.biblioteca.excepciones.UsuarioConPrestamosException;
import com.biblioteca.excepciones.UsuarioDuplicadoException;
import com.biblioteca.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.modelo.Usuario;
import java.util.List;
public interface ServicioUsuario {
    void agregarUsuario(Usuario usuario) throws UsuarioDuplicadoException;
    void actualizarUsuario(String idUsuario, Usuario usuario) throws UsuarioNoEncontradoException;
    void eliminarUsuario(String idUsuario) throws UsuarioNoEncontradoException, UsuarioConPrestamosException;
    Usuario buscarPorId(String idUsuario) throws UsuarioNoEncontradoException;
    List<Usuario> buscarPorNombre(String nombre);
    List<Usuario> listarTodos();
    void activarUsuario(String idUsuario) throws UsuarioNoEncontradoException;
    void desactivarUsuario(String idUsuario) throws UsuarioNoEncontradoException;
}
