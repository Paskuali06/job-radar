import { useState } from 'react'
import {
  deleteOffer,
  updateOffer,
  updateOfferStatus,
  type JobOffer,
  type JobOfferStatus
} from './offerService'

type OfferListProps = {
  offers: JobOffer[]
}

type EditFormData = {
  company: string
  title: string
  location: string | null
  workMode: string | null
  url: string
  description: string | null
}

function OfferList({ offers }: OfferListProps) {
  const [updatedStatuses, setUpdatedStatuses] = useState<
    Record<number, JobOfferStatus>
  >({})

  const [deletedOffers, setDeletedOffers] = useState<number[]>([])

  const [editingOfferId, setEditingOfferId] = useState<number | null>(null)

  const [editForm, setEditForm] = useState<EditFormData>({
    company: '',
    title: '',
    location: '',
    workMode: '',
    url: '',
    description: ''
  })

  const handleApply = async (id: number) => {
    await updateOfferStatus(id, 'SOLICITADA')

    setUpdatedStatuses((currentStatuses) => ({
      ...currentStatuses,
      [id]: 'SOLICITADA'
    }))
  }

  const handleDelete = async (id: number) => {
    await deleteOffer(id)

    setDeletedOffers((currentDeletedOffers) => [
      ...currentDeletedOffers,
      id
    ])
  }

  const handleEdit = (offer: JobOffer) => {
    setEditingOfferId(offer.id)

    setEditForm({
      company: offer.company,
      title: offer.title,
      location: offer.location ?? '',
      workMode: offer.workMode ?? '',
      url: '',
      description: ''
    })
  }

  const handleSaveEdit = async () => {
    if (editingOfferId === null) {
      return
    }

    await updateOffer(editingOfferId, editForm)

    setEditingOfferId(null)
  }

  if (offers.length === 0) {
    return <p className="empty-state">No hay ofertas</p>
  }

  const visibleOffers = offers.filter(
    (offer) => !deletedOffers.includes(offer.id)
  )

  if (visibleOffers.length === 0) {
    return <p className="empty-state">No hay ofertas</p>
  }

  return (
    <section className="offers-list">
      {visibleOffers.map((offer) => {
        const status = updatedStatuses[offer.id] ?? offer.status

        if (editingOfferId === offer.id) {
          return (
            <article
              key={offer.id}
              className="offer-card offer-edit-card"
            >
              <div className="offer-card-header">
                <div>
                  <p className="offer-company">
                    Editando oferta
                  </p>

                  <h3 className="offer-title">
                    {offer.title}
                  </h3>
                </div>
              </div>

              <div className="offer-edit-grid">
                <div className="field">
                  <label
                    className="field-label"
                    htmlFor={`company-${offer.id}`}
                  >
                    Empresa
                  </label>

                  <input
                    id={`company-${offer.id}`}
                    value={editForm.company}
                    onChange={(event) =>
                      setEditForm({
                        ...editForm,
                        company: event.target.value
                      })
                    }
                  />
                </div>

                <div className="field">
                  <label
                    className="field-label"
                    htmlFor={`title-${offer.id}`}
                  >
                    Título
                  </label>

                  <input
                    id={`title-${offer.id}`}
                    value={editForm.title}
                    onChange={(event) =>
                      setEditForm({
                        ...editForm,
                        title: event.target.value
                      })
                    }
                  />
                </div>

                <div className="field">
                  <label
                    className="field-label"
                    htmlFor={`location-${offer.id}`}
                  >
                    Ubicación
                  </label>

                  <input
                    id={`location-${offer.id}`}
                    value={editForm.location ?? ''}
                    onChange={(event) =>
                      setEditForm({
                        ...editForm,
                        location: event.target.value
                      })
                    }
                  />
                </div>

                <div className="field">
                  <label
                    className="field-label"
                    htmlFor={`workMode-${offer.id}`}
                  >
                    Modalidad
                  </label>

                  <input
                    id={`workMode-${offer.id}`}
                    value={editForm.workMode ?? ''}
                    onChange={(event) =>
                      setEditForm({
                        ...editForm,
                        workMode: event.target.value
                      })
                    }
                  />
                </div>

                <div className="field">
                  <label
                    className="field-label"
                    htmlFor={`url-${offer.id}`}
                  >
                    URL
                  </label>

                  <input
                    id={`url-${offer.id}`}
                    value={editForm.url}
                    onChange={(event) =>
                      setEditForm({
                        ...editForm,
                        url: event.target.value
                      })
                    }
                  />
                </div>

                <div className="field">
                  <label
                    className="field-label"
                    htmlFor={`description-${offer.id}`}
                  >
                    Descripción
                  </label>

                  <textarea
                    id={`description-${offer.id}`}
                    value={editForm.description ?? ''}
                    onChange={(event) =>
                      setEditForm({
                        ...editForm,
                        description: event.target.value
                      })
                    }
                  />
                </div>
              </div>

              <div className="offer-actions">
                <button
                  type="button"
                  className="btn primary"
                  onClick={handleSaveEdit}
                >
                  Guardar cambios
                </button>
              </div>
            </article>
          )
        }

        return (
          <article
            key={offer.id}
            className="offer-card"
          >
            <div className="offer-card-header">
              <div>
                <p className="offer-company">
                  {offer.company}
                </p>

                <h3 className="offer-title">
                  {offer.title}
                </h3>
              </div>

              <span
                className={`offer-status ${status.toLowerCase()}`}
              >
                {status}
              </span>
            </div>

            <div className="offer-meta">
              {offer.location && (
                <span>{offer.location}</span>
              )}

              {offer.workMode && (
                <span>{offer.workMode}</span>
              )}
            </div>

            <div className="offer-actions">
              {status !== 'SOLICITADA' && (
                <button
                  type="button"
                  className="btn primary"
                  onClick={() => handleApply(offer.id)}
                >
                  Marcar como aplicada
                </button>
              )}

              <button
                type="button"
                className="btn secondary"
                onClick={() => handleEdit(offer)}
              >
                Editar oferta
              </button>

              <button
                type="button"
                className="btn danger"
                onClick={() => handleDelete(offer.id)}
              >
                Eliminar oferta
              </button>
            </div>
          </article>
        )
      })}
    </section>
  )
}

export default OfferList