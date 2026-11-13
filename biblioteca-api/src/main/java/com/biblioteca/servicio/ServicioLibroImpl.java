package com.biblioteca.servicio;
import com.biblioteca.excepciones.LibroDuplicadoException;
import com.biblioteca.excepciones.LibroNoEncontradoException;
import com.biblioteca.excepciones.LibroPrestadoException;
import com.biblioteca.modelo.Libro;
import com.biblioteca.repositorio.LibroRepository;
import com.biblioteca.repositorio.PrestamoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ServicioLibroImpl implements ServicioLibro {
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;
    public ServicioLibroImpl(LibroRepository libroRepository, PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }
    @Override
    public void agregarLibro(Libro libro) throws LibroDuplicadoException {
        if (libroRepository.existsByIsbn(libro.getIsbn())) {
            throw new LibroDuplicadoException("Ya existe un libro con ese ISBN.");
        }
        libroRepository.save(libro);
    }
    @Override
    public void actualizarLibro(String isbn, Libro libro) throws LibroNoEncontradoException {
        Libro existente = libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException("Libro con ISBN " + isbn + " no encontrado."));
        existente.setTitulo(libro.getTitulo());
        existente.setAutor(libro.getAutor());
        existente.setAnioPublicacion(libro.getAnioPublicacion());
        libroRepository.save(existente);
    }
    @Override
    public void eliminarLibro(String isbn) throws LibroNoEncontradoException, LibroPrestadoException {
        Libro libro = libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException("Libro con ISBN " + isbn + " no encontrado."));
        if (libro.isPrestado()) {
            throw new LibroPrestadoException("No se puede eliminar un libro prestado.");
        }
        if (prestamoRepository.existsByLibroIsbn(isbn)) {
            throw new LibroPrestadoException("No se puede eliminar un libro con historial de préstamos.");
        }
        libroRepository.delete(libro);
    }
    @Override
    public Libro buscarPorIsbn(String isbn) throws LibroNoEncontradoException {
        return libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException("Libro con ISBN " + isbn + " no encontrado."));
    }
    @Override
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }
    @Override
    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }
    @Override
    public void marcarPrestado(String isbn) throws LibroNoEncontradoException, LibroPrestadoException {
        Libro libro = buscarPorIsbn(isbn);
        if (libro.isPrestado()) {
            throw new LibroPrestadoException("El libro ya está prestado.");
        }
        libro.setPrestado(true);
        libroRepository.save(libro);
    }
    @Override
    public void marcarDevuelto(String isbn) throws LibroNoEncontradoException {
        Libro libro = buscarPorIsbn(isbn);
        libro.setPrestado(false);
        libroRepository.save(libro);
    }
}
