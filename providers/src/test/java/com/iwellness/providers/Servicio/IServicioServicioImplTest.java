package com.iwellness.providers.Servicio;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.iwellness.providers.Entidad.Servicio;
import com.iwellness.providers.Repositorio.IServicioRepositorio;



public class IServicioServicioImplTest {

    @InjectMocks
    private IServicioServicioImpl servicioServicio;

    @Mock
    private IServicioRepositorio servicioRepositorio;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuscarTodos() {
        Servicio servicio1 = new Servicio();
        Servicio servicio2 = new Servicio();
        List<Servicio> servicios = Arrays.asList(servicio1, servicio2);

        when(servicioRepositorio.findAll()).thenReturn(servicios);

        List<Servicio> result = servicioServicio.BuscarTodos();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(servicioRepositorio, times(1)).findAll();
    }

    @Test
    public void testBuscarPorId() {
        Servicio servicio = new Servicio();
        when(servicioRepositorio.findById(1L)).thenReturn(Optional.of(servicio));

        Servicio result = servicioServicio.BuscarPorId(1L);

        assertNotNull(result);
        verify(servicioRepositorio, times(1)).findById(1L);
    }
    @Test
    public void testBuscarPorId_NoExistente() {
        when(servicioRepositorio.findById(1L)).thenReturn(Optional.empty());
    
        try {
            servicioServicio.BuscarPorId(1L);
        } catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }
    
        verify(servicioRepositorio, times(1)).findById(1L);
    }
    
    @Test
    public void testGuardar() {
        Servicio servicio = new Servicio();
        when(servicioRepositorio.save(servicio)).thenReturn(servicio);

        Servicio result = servicioServicio.Guardar(servicio);

        assertNotNull(result);
        verify(servicioRepositorio, times(1)).save(servicio);
    }

    @Test
    public void testActualizar() {
        Servicio servicio = new Servicio();
        when(servicioRepositorio.save(servicio)).thenReturn(servicio);

        Servicio result = servicioServicio.Actualizar(servicio);

        assertNotNull(result);
        verify(servicioRepositorio, times(1)).save(servicio);
    }

    @Test
    public void testEliminar() {
        Long id = 1L;

        servicioServicio.Eliminar(id);

        verify(servicioRepositorio, times(1)).deleteById(id);
    }
}