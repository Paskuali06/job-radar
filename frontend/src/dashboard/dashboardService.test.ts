import { describe, expect, it, vi } from 'vitest'
import { getDashboard } from './dashboardService'

describe('dashboardService', () => {
  it('should get the dashboard metrics', async () => {
    const metrics = {
      totalOffers: 10,
      pendingOffers: 6,
      appliedOffers: 3,
      rejectedOffers: 1
    }

    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue(metrics)
    })

    vi.stubGlobal('fetch', fetchMock)

    const result = await getDashboard()

    expect(fetchMock).toHaveBeenCalledWith('/dashboard')
    expect(result).toEqual(metrics)
  })

  it('should throw when the dashboard request fails', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: false
    })

    vi.stubGlobal('fetch', fetchMock)

    await expect(getDashboard()).rejects.toThrow(
      'No se pudieron obtener los datos del dashboard'
    )
  })
})
