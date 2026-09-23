export type JobOfferStatus =
  | 'PENDIENTE'
  | 'SOLICITADA'
  | 'RECHAZADA'

export type JobOffer = {
  id: number
  company: string
  title: string
  location: string | null
  workMode: string | null
  status: JobOfferStatus
}

export type JobOfferFilters = {
  company?: string
  location?: string
  workMode?: string
  status?: JobOfferStatus
  search?: string
  sortBy?: string
  sortDirection?: string
  page?: number
  size?: number
}

export type UpdateJobOfferData = {
  company: string
  title: string
  location: string | null
  workMode: string | null
  url: string
  description: string | null
}

export const getOffers = async (
  filters: JobOfferFilters = {}
): Promise<JobOffer[]> => {
  const params = new URLSearchParams()

  if (filters.company) {
    params.set('company', filters.company)
  }

  if (filters.location) {
    params.set('location', filters.location)
  }

  if (filters.workMode) {
    params.set('workMode', filters.workMode)
  }

  if (filters.status) {
    params.set('status', filters.status)
  }

  if (filters.search) {
    params.set('search', filters.search)
  }

  if (filters.sortBy) {
    params.set('sortBy', filters.sortBy)
  }

  if (filters.sortDirection) {
    params.set('sortDirection', filters.sortDirection)
  }

  if (filters.page !== undefined) {
    params.set('page', filters.page.toString())
  }

  if (filters.size !== undefined) {
    params.set('size', filters.size.toString())
  }

  const query = params.toString()
  const url = query ? `/job-offers?${query}` : '/job-offers'

  const response = await fetch(url)

  if (!response.ok) {
    throw new Error('No se pudieron obtener las ofertas')
  }

  return response.json()
}

export const updateOfferStatus = async (
  id: number,
  status: JobOfferStatus
): Promise<void> => {
  const response = await fetch(`/job-offers/${id}/status`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      status
    })
  })

  if (!response.ok) {
    throw new Error('No se pudo actualizar el estado de la oferta')
  }
}

export const deleteOffer = async (id: number): Promise<void> => {
  const response = await fetch(`/job-offers/${id}`, {
    method: 'DELETE'
  })

  if (!response.ok) {
    throw new Error('No se pudo eliminar la oferta')
  }
}

export const updateOffer = async (
  id: number,
  data: UpdateJobOfferData
): Promise<void> => {
  const response = await fetch(`/job-offers/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  })

  if (!response.ok) {
    throw new Error('No se pudo actualizar la oferta')
  }
}