import { useState } from 'react'
import {
  deleteOffer,
  updateOffer,
  updateOfferStatus,
  type JobOffer
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
    Record<number, string>
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
    await updateOfferStatus(id, 'APPLIED')

    setUpdatedStatuses((currentStatuses) => ({
      ...currentStatuses,
      [id]: 'APPLIED'
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
    return <p>No hay ofertas</p>
  }

  const visibleOffers = offers.filter(
    (offer) => !deletedOffers.includes(offer.id)
  )

  if (visibleOffers.length === 0) {
    return <p>No hay ofertas</p>
  }

  return (
    <section>
      {visibleOffers.map((offer) => {
        const status = updatedStatuses[offer.id] ?? offer.status

        if (editingOfferId === offer.id) {
          return (
            <article key={offer.id}>
              <label htmlFor={`company-${offer.id}`}>Empresa</label>
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

              <label htmlFor={`title-${offer.id}`}>Título</label>
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

              <label htmlFor={`location-${offer.id}`}>Ubicación</label>
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

              <label htmlFor={`workMode-${offer.id}`}>Modalidad</label>
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

              <label htmlFor={`url-${offer.id}`}>URL</label>
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

              <label htmlFor={`description-${offer.id}`}>
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

              <button type="button" onClick={handleSaveEdit}>
                Guardar cambios
              </button>
            </article>
          )
        }

        return (
          <article key={offer.id}>
            <h2>{offer.title}</h2>
            <p>{offer.company}</p>
            <p>{offer.location}</p>
            <p>{offer.workMode}</p>
            <p>{status}</p>

            {status !== 'APPLIED' && (
              <button
                type="button"
                onClick={() => handleApply(offer.id)}
              >
                Marcar como aplicada
              </button>
            )}

            <button
              type="button"
              onClick={() => handleEdit(offer)}
            >
              Editar oferta
            </button>

            <button
              type="button"
              onClick={() => handleDelete(offer.id)}
            >
              Eliminar oferta
            </button>
          </article>
        )
      })}
    </section>
  )
}

export default OfferList