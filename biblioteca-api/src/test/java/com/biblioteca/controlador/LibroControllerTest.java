package com.biblioteca.controlador;
import com.biblioteca.excepciones.LibroNoEncontradoException;
import com.biblioteca.modelo.Libro;
import com.biblioteca.servicio.ServicioLibro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(LibroController.class)
class LibroControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ServicioLibro servicioLibro;
    @Test
    void listarTodos_devuelve_lista_de_libros() throws Exception {
        Libro libro = new Libro("123", "El Quijote", "Cervantes", 1605);
        given(servicioLibro.listarTodos()).willReturn(List.of(libro));
        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("El Quijote"));
    }
    @Test
    void buscarPorIsbn_devuelve_404_si_no_existe() throws Exception {
        given(servicioLibro.buscarPorIsbn("999"))
                .willThrow(new LibroNoEncontradoException("Libro con ISBN 999 no encontrado."));
        mockMvc.perform(get("/api/v1/libros/999"))
                .andExpect(status().isNotFound());
    }
    @Test
    void agregarLibro_devuelve_400_si_titulo_vacio() throws Exception {
        String jsonInvalido = """
               {
                 "isbn": "123",
                 "titulo": "",
                 "autor": "Autor",
                 "anioPublicacion": 1605
               }
               """;
        mockMvc.perform(post("/api/v1/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.titulo").value("El título es obligatorio"));
    }
}
