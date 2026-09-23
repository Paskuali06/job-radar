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
        status: 'PENDING'
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

  it('should update the offer status', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true
    })

    vi.stubGlobal('fetch', fetchMock)

    await updateOfferStatus(1, 'APPLIED')

    expect(fetchMock).toHaveBeenCalledWith('/job-offers/1/status', {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        status: 'APPLIED'
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
})