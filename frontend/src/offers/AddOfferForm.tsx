import { useState } from 'react'
import {
  createOffer,
  type CreateJobOfferData
} from './offerService'

type AddOfferFormProps = {
  onSaved: () => void
  onCancel: () => void
}

function AddOfferForm({
  onSaved,
  onCancel
}: AddOfferFormProps) {
  const [company, setCompany] = useState('')
  const [title, setTitle] = useState('')
  const [url, setUrl] = useState('')
  const [location, setLocation] = useState('')
  const [workMode, setWorkMode] = useState('')
  const [error, setError] = useState(false)
  const [saving, setSaving] = useState(false)

  const handleSubmit = async (
    event: React.FormEvent
  ) => {
    event.preventDefault()

    try {
      setError(false)
      setSaving(true)

      const data: CreateJobOfferData = {
        company,
        title,
        url,
        location: location || null,
        workMode: workMode || null
      }

      await createOffer(data)

      onSaved()
    } catch {
      setError(true)
    } finally {
      setSaving(false)
    }
  }

  return (
    <form
      className="add-offer-form"
      onSubmit={handleSubmit}
    >
      <div className="field">
        <label
          className="field-label"
          htmlFor="add-company"
        >
          Empresa
        </label>

        <input
          id="add-company"
          value={company}
          onChange={(event) =>
            setCompany(event.target.value)
          }
          required
        />
      </div>

      <div className="field">
        <label
          className="field-label"
          htmlFor="add-title"
        >
          Título
        </label>

        <input
          id="add-title"
          value={title}
          onChange={(event) =>
            setTitle(event.target.value)
          }
          required
        />
      </div>

      <div className="field">
        <label
          className="field-label"
          htmlFor="add-url"
        >
          URL
        </label>

        <input
          id="add-url"
          type="url"
          value={url}
          onChange={(event) =>
            setUrl(event.target.value)
          }
          placeholder="https://..."
          required
        />
      </div>

      <div className="field">
        <label
          className="field-label"
          htmlFor="add-location"
        >
          Ubicación
        </label>

        <input
          id="add-location"
          value={location}
          onChange={(event) =>
            setLocation(event.target.value)
          }
        />
      </div>

      <div className="field">
        <label
          className="field-label"
          htmlFor="add-work-mode"
        >
          Modalidad
        </label>

        <select
          id="add-work-mode"
          value={workMode}
          onChange={(event) =>
            setWorkMode(event.target.value)
          }
        >
          <option value="">Seleccionar</option>
          <option value="REMOTE">Remoto</option>
          <option value="HYBRID">Híbrido</option>
          <option value="ONSITE">Presencial</option>
        </select>
      </div>

      {error && (
        <p className="form-error">
          No se pudo guardar la oferta
        </p>
      )}

      <div className="offer-actions">
        <button
          type="button"
          className="btn secondary"
          onClick={onCancel}
          disabled={saving}
        >
          Cancelar
        </button>

        <button
          type="submit"
          className="btn primary"
          disabled={saving}
        >
          {saving ? 'Guardando...' : 'Guardar oferta'}
        </button>
      </div>
    </form>
  )
}

export default AddOfferForm