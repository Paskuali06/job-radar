import {
  render,
  screen,
  waitFor
} from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import Dashboard from './Dashboard'
import { getDashboard } from './dashboardService'

vi.mock('./dashboardService', () => ({
  getDashboard: vi.fn()
}))

describe('Dashboard', () => {
  it('should render the dashboard metrics', async () => {
    vi.mocked(getDashboard).mockResolvedValue({
      totalOffers: 10,
      pendingOffers: 6,
      appliedOffers: 3,
      rejectedOffers: 1
    })

    render(<Dashboard />)

    await waitFor(() => {
      expect(screen.getByText('10')).toBeInTheDocument()
    })

    expect(screen.getAllByText('6')).toHaveLength(2)
    expect(screen.getAllByText('3')).toHaveLength(2)
    expect(screen.getAllByText('1')).toHaveLength(2)
  })

  it('should render zero metrics', async () => {
    vi.mocked(getDashboard).mockResolvedValue({
      totalOffers: 0,
      pendingOffers: 0,
      appliedOffers: 0,
      rejectedOffers: 0
    })

    render(<Dashboard />)

    await waitFor(() => {
      expect(screen.getAllByText('0')).toHaveLength(7)
    })
  })

  it('should show an error when loading the dashboard fails', async () => {
    vi.mocked(getDashboard).mockRejectedValue(
      new Error(
        'No se pudieron obtener los datos del dashboard'
      )
    )

    render(<Dashboard />)

    await waitFor(() => {
      expect(
        screen.getByText(
          'No se pudieron cargar los datos del dashboard'
        )
      ).toBeInTheDocument()
    })
  })
})