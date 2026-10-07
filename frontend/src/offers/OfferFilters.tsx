import { useState } from 'react'
import type {
  JobOfferFilters,
  JobOfferStatus
} from './offerService'

type OfferFiltersProps = {
  onFilter: (filters: JobOfferFilters) => void
}

function OfferFilters({ onFilter }: OfferFiltersProps) {
  const [company, setCompany] = useState('')
  const [location, setLocation] = useState('')
  const [workMode, setWorkMode] = useState('')
  const [status, setStatus] = useState('')
  const [search, setSearch] = useState('')
  const [sortBy, setSortBy] = useState('')
  const [sortDirection, setSortDirection] = useState('')
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault()

    const filters: JobOfferFilters = {}

    if (company) filters.company = company
    if (location) filters.location = location
    if (workMode) filters.workMode = workMode
    if (status) filters.status = status as JobOfferStatus
    if (search) filters.search = search
    if (sortBy) filters.sortBy = sortBy
    if (sortDirection) filters.sortDirection = sortDirection
    if (page !== 0) filters.page = page
    if (size !== 10) filters.size = size

    onFilter(filters)
  }

  return (
    <form className="filters" onSubmit={handleSubmit}>
      <div className="filters-header">
        <div>
          <h3 className="filters-title">
            Buscar ofertas
          </h3>

          <p className="filters-subtitle">
            Filtra tus oportunidades por empresa, ubicación,
            modalidad o estado.
          </p>
        </div>
      </div>

      <div className="filters-main">
        <div className="field">
          <label
            className="field-label"
            htmlFor="search"
          >
            Buscar
          </label>

          <input
            id="search"
            placeholder="Título o palabra clave"
            value={search}
            onChange={(event) =>
              setSearch(event.target.value)
            }
          />
        </div>

        <div className="field">
          <label
            className="field-label"
            htmlFor="company"
          >
            Empresa
          </label>

          <input
            id="company"
            placeholder="Ej. Google"
            value={company}
            onChange={(event) =>
              setCompany(event.target.value)
            }
          />
        </div>

        <div className="field">
          <label
            className="field-label"
            htmlFor="location"
          >
            Ubicación
          </label>

          <input
            id="location"
            placeholder="Ej. Madrid"
            value={location}
            onChange={(event) =>
              setLocation(event.target.value)
            }
          />
        </div>

        <div className="field">
          <label
            className="field-label"
            htmlFor="workMode"
          >
            Modalidad
          </label>

          <select
            id="workMode"
            value={workMode}
            onChange={(event) =>
              setWorkMode(event.target.value)
            }
          >
            <option value="">Todas</option>
            <option value="REMOTE">Remoto</option>
            <option value="HYBRID">Híbrido</option>
            <option value="ONSITE">Presencial</option>
          </select>
        </div>

        <div className="field">
          <label
            className="field-label"
            htmlFor="status"
          >
            Estado
          </label>

          <select
            id="status"
            value={status}
            onChange={(event) =>
              setStatus(event.target.value)
            }
          >
            <option value="">Todos</option>
            <option value="PENDIENTE">Pendiente</option>
            <option value="SOLICITADA">Solicitada</option>
            <option value="RECHAZADA">Rechazada</option>
          </select>
        </div>
      </div>

      <details className="filters-advanced">
        <summary>
          Más opciones
        </summary>

        <div className="filters-advanced-grid">
          <div className="field">
            <label
              className="field-label"
              htmlFor="sortBy"
            >
              Ordenar por
            </label>

            <select
              id="sortBy"
              value={sortBy}
              onChange={(event) =>
                setSortBy(event.target.value)
              }
            >
              <option value="">Sin ordenar</option>
              <option value="createdAt">
                Fecha de creación
              </option>
              <option value="updatedAt">
                Fecha de actualización
              </option>
              <option value="publishedAt">
                Fecha de publicación
              </option>
            </select>
          </div>

          <div className="field">
            <label
              className="field-label"
              htmlFor="sortDirection"
            >
              Dirección
            </label>

            <select
              id="sortDirection"
              value={sortDirection}
              onChange={(event) =>
                setSortDirection(event.target.value)
              }
            >
              <option value="">
                Predeterminada
              </option>
              <option value="asc">
                Ascendente
              </option>
              <option value="desc">
                Descendente
              </option>
            </select>
          </div>

          <div className="field">
            <label
              className="field-label"
              htmlFor="size"
            >
              Ofertas por página
            </label>

            <select
              id="size"
              value={size}
              onChange={(event) =>
                setSize(Number(event.target.value))
              }
            >
              <option value={5}>5</option>
              <option value={10}>10</option>
              <option value={20}>20</option>
            </select>
          </div>

          <div className="field">
            <label
              className="field-label"
              htmlFor="page"
            >
              Página
            </label>

            <input
              id="page"
              type="number"
              min="0"
              value={page}
              onChange={(event) =>
                setPage(Number(event.target.value))
              }
            />
          </div>
        </div>
      </details>

      <div className="filter-actions">
        <button
          type="submit"
          className="btn primary"
        >
          Filtrar
        </button>
      </div>
    </form>
  )
}

export default OfferFilters