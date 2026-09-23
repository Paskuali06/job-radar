import { render, screen, waitFor } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import App from './App'
import { getOffers } from './offers/offerService'

vi.mock('./offers/offerService', () => ({
  getOffers: vi.fn()
}))

describe('App', () => {
  it('should render the application name', () => {
    vi.mocked(getOffers).mockResolvedValue([])

    render(<App />)

    expect(screen.getByText('Job-Radar')).toBeInTheDocument()
  })

  it('should load and display the job offers', async () => {
    vi.mocked(getOffers).mockResolvedValue([
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDING'
      }
    ])

    render(<App />)

    await waitFor(() => {
      expect(screen.getByText('Java Developer')).toBeInTheDocument()
    })

    expect(getOffers).toHaveBeenCalled()
  })

  it('should show an error when loading offers fails', async () => {
    vi.mocked(getOffers).mockRejectedValue(
      new Error('No se pudieron obtener las ofertas')
    )

    render(<App />)

    await waitFor(() => {
      expect(
        screen.getByText('No se pudieron cargar las ofertas')
      ).toBeInTheDocument()
    })
  })
})