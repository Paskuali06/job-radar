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

    if (company) {
      filters.company = company
    }

    if (location) {
      filters.location = location
    }

    if (workMode) {
      filters.workMode = workMode
    }

    if (status) {
      filters.status = status as JobOfferStatus
    }

    if (search) {
      filters.search = search
    }

    if (sortBy) {
      filters.sortBy = sortBy
    }

    if (sortDirection) {
      filters.sortDirection = sortDirection
    }

    if (page !== 0) {
      filters.page = page
    }

    if (size !== 10) {
      filters.size = size
    }

    onFilter(filters)
  }

  return (
    <form onSubmit={handleSubmit}>
      <label htmlFor="company">Empresa</label>
      <input
        id="company"
        value={company}
        onChange={(event) => setCompany(event.target.value)}
      />

      <label htmlFor="location">Ubicación</label>
      <input
        id="location"
        value={location}
        onChange={(event) => setLocation(event.target.value)}
      />

      <label htmlFor="workMode">Modalidad</label>
      <select
        id="workMode"
        value={workMode}
        onChange={(event) => setWorkMode(event.target.value)}
      >
        <option value="">Todas</option>
        <option value="REMOTE">Remoto</option>
        <option value="HYBRID">Híbrido</option>
        <option value="ONSITE">Presencial</option>
      </select>

      <label htmlFor="status">Estado</label>
      <select
        id="status"
        value={status}
        onChange={(event) => setStatus(event.target.value)}
      >
        <option value="">Todos</option>
        <option value="PENDIENTE">Pendiente</option>
        <option value="SOLICITADA">Solicitada</option>
        <option value="RECHAZADA">Rechazada</option>
      </select>

      <label htmlFor="search">Buscar</label>
      <input
        id="search"
        value={search}
        onChange={(event) => setSearch(event.target.value)}
      />

      <label htmlFor="sortBy">Ordenar por</label>
      <select
        id="sortBy"
        value={sortBy}
        onChange={(event) => setSortBy(event.target.value)}
      >
        <option value="">Sin ordenar</option>
        <option value="createdAt">Fecha de creación</option>
        <option value="updatedAt">Fecha de actualización</option>
        <option value="publishedAt">Fecha de publicación</option>
      </select>

      <label htmlFor="sortDirection">Dirección</label>
      <select
        id="sortDirection"
        value={sortDirection}
        onChange={(event) => setSortDirection(event.target.value)}
      >
        <option value="">Predeterminada</option>
        <option value="asc">Ascendente</option>
        <option value="desc">Descendente</option>
      </select>

      <label htmlFor="size">Ofertas por página</label>
      <select
        id="size"
        value={size}
        onChange={(event) => setSize(Number(event.target.value))}
      >
        <option value={5}>5</option>
        <option value={10}>10</option>
        <option value={20}>20</option>
      </select>

      <label htmlFor="page">Página</label>
      <input
        id="page"
        type="number"
        min="0"
        value={page}
        onChange={(event) => setPage(Number(event.target.value))}
      />

      <button type="submit">Filtrar</button>
    </form>
  )
}

export default OfferFilters