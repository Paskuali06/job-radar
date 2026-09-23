import { describe, expect, it, vi } from 'vitest'
import {
  deleteOffer,
  getOffers,
  updateOffer,
  updateOfferStatus
} from './offerService'

describe('offerService', () => {
  it('should get the authenticated user offers', async () => {
    const offers = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDIENTE'
      }
    ]

    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue(offers)
    })

    vi.stubGlobal('fetch', fetchMock)

    const result = await getOffers()

    expect(fetchMock).toHaveBeenCalledWith('/job-offers')
    expect(result).toEqual(offers)
  })

  it('should get offers filtered by company', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue([])
    })

    vi.stubGlobal('fetch', fetchMock)

    await getOffers({
      company: 'Empresa A'
    })

    expect(fetchMock).toHaveBeenCalledWith(
      '/job-offers?company=Empresa+A'
    )
  })

  it('should get offers with all filters', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue([])
    })

    vi.stubGlobal('fetch', fetchMock)

    await getOffers({
      company: 'Empresa A',
      location: 'Madrid',
      workMode: 'REMOTE',
      status: 'PENDIENTE',
      search: 'Java',
      sortBy: 'createdAt',
      sortDirection: 'desc',
      page: 0,
      size: 10
    })

    expect(fetchMock).toHaveBeenCalledWith(
      '/job-offers?company=Empresa+A&location=Madrid&workMode=REMOTE&status=PENDIENTE&search=Java&sortBy=createdAt&sortDirection=desc&page=0&size=10'
    )
  })

  it('should update the offer status', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true
    })

    vi.stubGlobal('fetch', fetchMock)

    await updateOfferStatus(1, 'SOLICITADA')

    expect(fetchMock).toHaveBeenCalledWith('/job-offers/1/status', {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        status: 'SOLICITADA'
      })
    })
  })

  it('should delete an offer', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true
    })

    vi.stubGlobal('fetch', fetchMock)

    await deleteOffer(1)

    expect(fetchMock).toHaveBeenCalledWith('/job-offers/1', {
      method: 'DELETE'
    })
  })

  it('should update an offer', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true
    })

    vi.stubGlobal('fetch', fetchMock)

    await updateOffer(1, {
      company: 'Empresa Nueva',
      title: 'Senior Java Developer',
      location: 'Madrid',
      workMode: 'HYBRID',
      url: 'https://empresa.com/oferta',
      description: 'Nueva descripción'
    })

    expect(fetchMock).toHaveBeenCalledWith('/job-offers/1', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        company: 'Empresa Nueva',
        title: 'Senior Java Developer',
        location: 'Madrid',
        workMode: 'HYBRID',
        url: 'https://empresa.com/oferta',
        description: 'Nueva descripción'
      })
    })
  })

  it('should send filters, sorting and pagination to the backend', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue([])
    })

    vi.stubGlobal('fetch', fetchMock)

    await getOffers({
      company: 'Empresa A',
      location: 'Madrid',
      workMode: 'REMOTE',
      status: 'PENDIENTE',
      search: 'Java',
      sortBy: 'createdAt',
      sortDirection: 'desc',
      page: 2,
      size: 20
    })

    expect(fetchMock).toHaveBeenCalledWith(
      '/job-offers?company=Empresa+A&location=Madrid&workMode=REMOTE&status=PENDIENTE&search=Java&sortBy=createdAt&sortDirection=desc&page=2&size=20'
    )
  })
})