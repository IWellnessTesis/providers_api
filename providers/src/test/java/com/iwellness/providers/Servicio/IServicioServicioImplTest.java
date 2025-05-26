package com.iwellness.providers.Servicio;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
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

import com.iwellness.providers.Clientes.PreferenciaFeignClient;
import com.iwellness.providers.Clientes.ProveedorFeignClient;
import com.iwellness.providers.DTO.ProveedorDTO;
import com.iwellness.providers.Entidad.Servicio;
import com.iwellness.providers.Repositorio.IServicioRepositorio;
import com.iwellness.providers.Servicio.Servicio.IServicioServicioImpl;



public class IServicioServicioImplTest {

    @InjectMocks
    private IServicioServicioImpl servicioServicio;

    @Mock
    private IServicioRepositorio servicioRepositorio;

    @Mock
    private ProveedorFeignClient proveedorFeignClient;

    @Mock
    private PreferenciaFeignClient preferenciaFeignClient;

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
        // Crear un servicio con idProveedor válido
        Servicio servicio = new Servicio();
        servicio.set_idProveedor(1L);

        // Mockear el repositorio para guardar el servicio
        when(servicioRepositorio.save(servicio)).thenReturn(servicio);

        // Mockear la respuesta del cliente Feign para que devuelva un proveedor válido
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        // Rellenar los campos necesarios del proveedorDTO si aplica
        when(proveedorFeignClient.obtenerProveedor(1L)).thenReturn(proveedorDTO);

        // Ejecutar el método a testear
        Servicio result = servicioServicio.Guardar(servicio);

        // Verificaciones
        assertNotNull(result);
        verify(servicioRepositorio, times(1)).save(servicio);
        verify(proveedorFeignClient, times(1)).obtenerProveedor(1L);
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

        // Para el método void elimintarPreferenciasPorServicio, mockear para que no falle
        doNothing().when(preferenciaFeignClient).elimintarPreferenciasPorServicio(id);

        servicioServicio.Eliminar(id);

        verify(servicioRepositorio, times(1)).deleteById(id);
        verify(preferenciaFeignClient, times(1)).elimintarPreferenciasPorServicio(id);
    }
}