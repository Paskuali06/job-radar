package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.JobOffer;
import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para {@link CreateJobOfferService}.
 *
 * Comprueba el comportamiento del caso de uso encargado de crear
 * nuevas ofertas de empleo.
 *
 * Se prueban dos escenarios principales:
 * - Creación de una oferta cuya URL todavía no existe.
 * - Rechazo de una oferta cuya URL ya está registrada.
 *
 * El repositorio se simula mediante Mockito para comprobar exclusivamente
 * la lógica del servicio sin necesidad de utilizar PostgreSQL.
 */
@ExtendWith(MockitoExtension.class)
class CreateJobOfferServiceTest {

    /**
     * Mock del puerto de persistencia utilizado por el servicio.
     *
     * Al tratarse de un test unitario, no utilizamos el repositorio real.
     * Mockito permite controlar las respuestas del repositorio y verificar
     * las operaciones que realiza el servicio sobre él.
     */
    @Mock
    private JobOfferRepository jobOfferRepository;

    /**
     * Instancia real del servicio que estamos sometiendo a prueba.
     *
     * Mockito inyecta automáticamente el mock de {@link JobOfferRepository}
     * en el constructor del servicio.
     */
    @InjectMocks
    private CreateJobOfferService createJobOfferService;

    /**
     * CT-001 - Debe crear una oferta cuando su URL no existe.
     *
     * Escenario:
     * - La URL de la oferta no existe en el sistema.
     * - El servicio debe comprobar que la URL está disponible.
     * - La oferta debe guardarse.
     * - El servicio debe devolver la oferta guardada.
     */
    @Test
    void shouldCreateJobOfferWhenUrlDoesNotExist() {

        // Arrange:
        // Creamos una oferta mínima para este caso de prueba.
        JobOffer jobOffer = new JobOffer();
        jobOffer.setUrl("https://empresa.com/jobs/123");

        // Indicamos a Mockito que la URL todavía no existe.
        when(jobOfferRepository.existsByUrl(jobOffer.getUrl()))
                .thenReturn(false);

        // Simulamos la operación de guardado del repositorio.
        when(jobOfferRepository.save(jobOffer))
                .thenReturn(jobOffer);

        // Act:
        // Ejecutamos el caso de uso que estamos probando.
        JobOffer result = createJobOfferService.create(jobOffer);

        // Assert:
        // Comprobamos que el servicio devuelve una oferta.
        assertNotNull(result);

        // Comprobamos que la oferta devuelta es la que se ha guardado.
        assertEquals(jobOffer, result);

        // Verificamos que el servicio comprobó si la URL ya existía.
        verify(jobOfferRepository)
                .existsByUrl(jobOffer.getUrl());

        // Verificamos que la oferta se guardó porque la URL no existía.
        verify(jobOfferRepository)
                .save(jobOffer);
    }

    /**
     * CT-002 - Debe rechazar una oferta cuando su URL ya existe.
     *
     * Este comportamiento constituye una de las primeras barreras
     * contra ofertas duplicadas en Job Radar.
     *
     * Si la URL ya está registrada:
     * - No se debe guardar una segunda oferta.
     * - El servicio debe lanzar una {@link IllegalArgumentException}.
     * - La operación de guardado no debe ejecutarse.
     */
    @Test
    void shouldRejectJobOfferWhenUrlAlreadyExists() {

        // Arrange:
        // Creamos una oferta con una URL que simularemos como existente.
        JobOffer jobOffer = new JobOffer();
        jobOffer.setUrl("https://empresa.com/jobs/123");

        // Indicamos a Mockito que la URL ya está registrada.
        when(jobOfferRepository.existsByUrl(jobOffer.getUrl()))
                .thenReturn(true);

        // Act:
        // Ejecutamos el servicio y esperamos que rechace la oferta
        // lanzando una IllegalArgumentException.
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createJobOfferService.create(jobOffer)
        );

        // Assert:
        // Comprobamos que la excepción contiene el mensaje que realmente
        // devuelve el servicio.
        assertEquals(
                "Job offer with the same URL already exists.",
                exception.getMessage()
        );

        // Verificamos que el servicio comprobó previamente la existencia
        // de la URL.
        verify(jobOfferRepository)
                .existsByUrl(jobOffer.getUrl());

        // Verificamos que nunca se ejecutó save(), ya que la oferta
        // ya estaba registrada.
        verify(jobOfferRepository, never())
                .save(any(JobOffer.class));
    }

    /**
 * CT-003 - Debe rechazar una oferta cuando no tiene URL.
 *
 * La URL es necesaria para identificar de forma única una oferta
 * y evitar duplicados dentro de Job Radar.
 *
 * Si la oferta no contiene una URL:
 * - El servicio debe rechazar la operación.
 * - Debe lanzar una {@link IllegalArgumentException}.
 * - No debe consultar el repositorio.
 * - No debe intentar guardar la oferta.
 */
@Test
void shouldRejectJobOfferWhenUrlIsNull() {

    // Arrange:
    // Creamos una oferta sin establecer su URL.
    JobOffer jobOffer = new JobOffer();

    // Act:
    // Intentamos crear la oferta y esperamos que el servicio
    // rechace la operación.
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createJobOfferService.create(jobOffer)
    );

    // Assert:
    // Comprobamos que el servicio devuelve el mensaje esperado.
    assertEquals(
            "Job offer URL cannot be null or blank.",
            exception.getMessage()
    );

    // Verificamos que el repositorio no se consulta porque
    // la oferta ya es inválida antes de comprobar duplicados.
    verifyNoInteractions(jobOfferRepository);
}

/**
 * CT-004 - Debe rechazar una oferta cuando su URL está vacía o contiene
 * únicamente espacios.
 *
 * Una URL sin contenido válido no puede utilizarse para identificar
 * una oferta ni para comprobar posibles duplicados.
 *
 * En todos los casos inválidos el repositorio no debe ser consultado.
 */
@ParameterizedTest
@ValueSource(strings = {"", " ", "    "})
void shouldRejectJobOfferWhenUrlIsBlank(String url) {

    // Arrange:
    // Creamos una oferta utilizando la URL recibida como parámetro.
    JobOffer jobOffer = new JobOffer();
    jobOffer.setUrl(url);

    // Act:
    // Intentamos crear la oferta y esperamos que sea rechazada.
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createJobOfferService.create(jobOffer)
    );

    // Assert:
    // Comprobamos que se devuelve el mensaje esperado.
    assertEquals(
            "Job offer URL cannot be null or blank.",
            exception.getMessage()
    );

    // La validación debe producirse antes de interactuar con el repositorio.
    verifyNoInteractions(jobOfferRepository);
}
/**
 * CT-005 - Debe devolver la oferta proporcionada por el repositorio
 * después de guardar correctamente una oferta válida.
 *
 * El servicio no debe modificar ni sustituir el resultado obtenido
 * durante la operación de persistencia.
 */
@Test
void shouldReturnSavedJobOffer() {

    // Arrange:
    // Creamos la oferta que queremos guardar.
    JobOffer jobOffer = new JobOffer();
    jobOffer.setUrl("https://empresa.com/jobs/456");

    // Creamos la instancia que simularemos como resultado
    // de la operación de persistencia.
    JobOffer savedJobOffer = new JobOffer();
    savedJobOffer.setUrl("https://empresa.com/jobs/456");

    // Indicamos que la URL todavía no existe.
    when(jobOfferRepository.existsByUrl(jobOffer.getUrl()))
            .thenReturn(false);

    // Simulamos que el repositorio devuelve la oferta guardada.
    when(jobOfferRepository.save(jobOffer))
            .thenReturn(savedJobOffer);

    // Act:
    // Ejecutamos el caso de uso.
    JobOffer result = createJobOfferService.create(jobOffer);

    // Assert:
    // Comprobamos que el servicio devuelve exactamente
    // la instancia proporcionada por el repositorio.
    assertSame(savedJobOffer, result);

    // Verificamos que se ejecutó la operación de guardado
    // utilizando la oferta correcta.
    verify(jobOfferRepository)
            .save(jobOffer);
}
}