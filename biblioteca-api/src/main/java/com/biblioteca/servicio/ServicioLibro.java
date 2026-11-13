package com.biblioteca.servicio;
import com.biblioteca.excepciones.LibroDuplicadoException;
import com.biblioteca.excepciones.LibroNoEncontradoException;
import com.biblioteca.excepciones.LibroPrestadoException;
import com.biblioteca.modelo.Libro;
import java.util.List;
public interface ServicioLibro {
    void agregarLibro(Libro libro) throws LibroDuplicadoException;
    void actualizarLibro(String isbn, Libro libro) throws LibroNoEncontradoException;
    void eliminarLibro(String isbn) throws LibroNoEncontradoException, LibroPrestadoException;
    Libro buscarPorIsbn(String isbn) throws LibroNoEncontradoException;
    List<Libro> buscarPorTitulo(String titulo);
    List<Libro> listarTodos();
    void marcarPrestado(String isbn) throws LibroNoEncontradoException, LibroPrestadoException;
    void marcarDevuelto(String isbn) throws LibroNoEncontradoException;
}
