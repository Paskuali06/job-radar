export type JobOffer = {
  id: number
  company: string
  title: string
  location: string | null
  workMode: string | null
  status: string
}

export type UpdateJobOfferData = {
  company: string
  title: string
  location: string | null
  workMode: string | null
  url: string
  description: string | null
}

export const getOffers = async (): Promise<JobOffer[]> => {
  const response = await fetch('/job-offers')

  if (!response.ok) {
    throw new Error('No se pudieron obtener las ofertas')
  }

  return response.json()
}

export const updateOfferStatus = async (
  id: number,
  status: string
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