package com.opc.jobradar.domain.port.out;

import com.opc.jobradar.domain.model.JobOfferStatusHistory;

/**
 * Puerto de salida para persistir el historial de estados de una oferta.
 */
public interface JobOfferStatusHistoryRepository {

    /**
     * Guarda un registro del historial de estados.
     *
     * @param history registro que se quiere guardar
     * @return registro guardado
     */
    JobOfferStatusHistory save(JobOfferStatusHistory history);
}