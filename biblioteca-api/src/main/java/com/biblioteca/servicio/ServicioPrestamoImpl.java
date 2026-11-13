package com.biblioteca.servicio;


import com.biblioteca.excepciones.LibroNoEncontradoException;
import com.biblioteca.excepciones.LibroPrestadoException;
import com.biblioteca.excepciones.PrestamoNoEncontradoException;
import com.biblioteca.excepciones.UsuarioInactivoException;
import com.biblioteca.excepciones.UsuarioNoEncontradoException;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.repositorio.PrestamoRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;


@Service
public class ServicioPrestamoImpl implements ServicioPrestamo {


    private final PrestamoRepository prestamoRepository;
    private final ServicioLibro servicioLibro;
    private final ServicioUsuario servicioUsuario;


    public ServicioPrestamoImpl(PrestamoRepository prestamoRepository,
                                ServicioLibro servicioLibro,
                                ServicioUsuario servicioUsuario) {
        this.prestamoRepository = prestamoRepository;
        this.servicioLibro = servicioLibro;
        this.servicioUsuario = servicioUsuario;
    }


    @Override
    public void prestarLibro(String isbn, String idUsuario)
            throws LibroNoEncontradoException,
            UsuarioNoEncontradoException,
            LibroPrestadoException,
            UsuarioInactivoException {


        Libro libro = servicioLibro.buscarPorIsbn(isbn);
        if (libro.isPrestado()) {
            throw new LibroPrestadoException("El libro ya está prestado.");
        }


        Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
        if (!usuario.isActivo()) {
            throw new UsuarioInactivoException("El usuario no está activo.");
        }


        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucionPrevista = fechaPrestamo.plusDays(15);
        Prestamo prestamo = new Prestamo(libro, usuario, fechaPrestamo, fechaDevolucionPrevista);
        prestamoRepository.save(prestamo);


        servicioLibro.marcarPrestado(isbn);
    }


    @Override
    public void devolverLibro(String isbn)
            throws LibroNoEncontradoException, PrestamoNoEncontradoException {


        Prestamo prestamo = prestamoRepository.findByLibroIsbnAndFechaDevolucionRealIsNull(isbn)
                .orElseThrow(() -> new PrestamoNoEncontradoException("No hay un préstamo activo para ese libro."));


        prestamo.setFechaDevolucionReal(LocalDate.now());
        prestamoRepository.save(prestamo);


        servicioLibro.marcarDevuelto(isbn);
    }


    @Override
    public List<Prestamo> listarPrestamosActivos() {
        return prestamoRepository.findByFechaDevolucionRealIsNull();
    }
}
