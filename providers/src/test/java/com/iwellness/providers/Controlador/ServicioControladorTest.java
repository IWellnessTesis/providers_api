package com.iwellness.providers.Controlador;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwellness.providers.Entidad.Servicio;
import com.iwellness.providers.Servicio.Servicio.IServicioServicio;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ServicioControladorTest {

    private MockMvc mockMvc;

    @Mock
    private IServicioServicio servicioServicio;

    @InjectMocks
    private ServicioControlador servicioControlador;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(servicioControlador).build();
    }

    // GET /api/servicio/all
    @Test
    public void testBuscarTodos_Success() throws Exception {
        List<Servicio> servicios = Arrays.asList(new Servicio(), new Servicio());
        when(servicioServicio.BuscarTodos()).thenReturn(servicios);

        mockMvc.perform(get("/api/servicio/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // GET /api/servicio/search/{id} - Éxito
    @Test
    public void testBuscarPorId_Success() throws Exception {
        Servicio servicio = new Servicio();
        when(servicioServicio.BuscarPorId(1L)).thenReturn(servicio);

        mockMvc.perform(get("/api/servicio/search/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // GET /api/servicio/search/{id} - No encontrado
    @Test
    public void testBuscarPorId_NotFound() throws Exception {
        when(servicioServicio.BuscarPorId(1L))
            .thenThrow(new NoSuchElementException("Servicio no encontrado con ID: 1"));

        mockMvc.perform(get("/api/servicio/search/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // POST /api/servicio/save/{servicio} - Éxito
    @Test
    public void testGuardar_Success() throws Exception {
        Servicio servicio = new Servicio();
        when(servicioServicio.Guardar(any(Servicio.class))).thenReturn(servicio);

        String servicioJson = objectMapper.writeValueAsString(servicio);

        // Se utiliza un valor "dummy" en la URL para completar la ruta
        mockMvc.perform(post("/api/servicio/save/dummy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(servicioJson))
                .andExpect(status().isOk());
    }

    // POST /api/servicio/save/{servicio} - Error al guardar
    @Test
    public void testGuardar_BadRequest() throws Exception {
        when(servicioServicio.Guardar(any(Servicio.class)))
            .thenThrow(new RuntimeException("Error al guardar el servicio"));

        Servicio servicio = new Servicio();
        String servicioJson = objectMapper.writeValueAsString(servicio);

        mockMvc.perform(post("/api/servicio/save/dummy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(servicioJson))
                .andExpect(status().isBadRequest());
    }

    // PUT /api/servicio/update/{servicio} - Éxito
    @Test
    public void testActualizar_Success() throws Exception {
        Servicio servicio = new Servicio();
        when(servicioServicio.Actualizar(any(Servicio.class))).thenReturn(servicio);

        String servicioJson = objectMapper.writeValueAsString(servicio);

        mockMvc.perform(put("/api/servicio/update/dummy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(servicioJson))
                .andExpect(status().isOk());
    }

    // PUT /api/servicio/update/{servicio} - Error al actualizar
    @Test
    public void testActualizar_BadRequest() throws Exception {
        when(servicioServicio.Actualizar(any(Servicio.class)))
            .thenThrow(new RuntimeException("Error al actualizar el servicio"));

        Servicio servicio = new Servicio();
        String servicioJson = objectMapper.writeValueAsString(servicio);

        mockMvc.perform(put("/api/servicio/update/dummy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(servicioJson))
                .andExpect(status().isBadRequest());
    }

    // DELETE /api/servicio/delete/{id} - Éxito
    @Test
    public void testEliminar_Success() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/servicio/delete/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(servicioServicio).Eliminar(id);
    }

    // DELETE /api/servicio/delete/{id} - No encontrado
    @Test
    public void testEliminar_NotFound() throws Exception {
        Long id = 1L;

        doThrow(new RuntimeException("No se encontró la entidad con ID: " + id))
            .when(servicioServicio).Eliminar(id);

        mockMvc.perform(delete("/api/servicio/delete/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
