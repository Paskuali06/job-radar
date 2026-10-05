import {
  fireEvent,
  render,
  screen,
  waitFor
} from '@testing-library/react'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import App from './App'
import {
  getAuthenticatedUser,
  login
} from './auth/authService'
import { getOffers } from './offers/offerService'
import { getDashboard } from './dashboard/dashboardService'

vi.mock('./auth/authService', () => ({
  login: vi.fn(),
  getAuthenticatedUser: vi.fn()
}))

vi.mock('./offers/offerService', () => ({
  getOffers: vi.fn()
}))

vi.mock('./dashboard/dashboardService', () => ({
  getDashboard: vi.fn()
}))

describe('App', () => {
  beforeEach(() => {
    vi.resetAllMocks()

    vi.mocked(getAuthenticatedUser).mockResolvedValue({
      id: 1,
      name: 'Usuario',
      email: 'usuario@email.com',
      role: 'USER'
    })

    vi.mocked(getOffers).mockResolvedValue([])

    vi.mocked(getDashboard).mockResolvedValue({
      totalOffers: 0,
      pendingOffers: 0,
      appliedOffers: 0,
      rejectedOffers: 0
    })

    vi.mocked(login).mockResolvedValue(undefined)
  })

  it('should render the application name', async () => {
    render(<App />)

    await waitFor(() => {
      expect(getAuthenticatedUser).toHaveBeenCalled()
    })

    expect(screen.getByText('JOB-RADAR')).toBeInTheDocument()

    await waitFor(() => {
      expect(getOffers).toHaveBeenCalledWith({})
      expect(getDashboard).toHaveBeenCalled()
    })
  })

  it('should load and display the job offers', async () => {
    vi.mocked(getOffers).mockResolvedValue([
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDIENTE'
      }
    ])

    vi.mocked(getDashboard).mockResolvedValue({
      totalOffers: 1,
      pendingOffers: 1,
      appliedOffers: 0,
      rejectedOffers: 0
    })

    render(<App />)

    await waitFor(() => {
      expect(
        screen.getByText('Java Developer')
      ).toBeInTheDocument()
    })

    expect(getOffers).toHaveBeenCalledWith({})
    expect(getDashboard).toHaveBeenCalled()
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

  it('should reload offers when a filter is applied', async () => {
    vi.mocked(getOffers)
      .mockResolvedValueOnce([
        {
          id: 1,
          company: 'Empresa A',
          title: 'Java Developer',
          location: 'Madrid',
          workMode: 'REMOTE',
          status: 'PENDIENTE'
        }
      ])
      .mockResolvedValueOnce([])

    vi.mocked(getDashboard).mockResolvedValue({
      totalOffers: 1,
      pendingOffers: 1,
      appliedOffers: 0,
      rejectedOffers: 0
    })

    render(<App />)

    await waitFor(() => {
      expect(
        screen.getByText('Java Developer')
      ).toBeInTheDocument()
    })

    fireEvent.change(screen.getByLabelText('Empresa'), {
      target: {
        value: 'Empresa A'
      }
    })

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Filtrar'
      })
    )

    await waitFor(() => {
      expect(getOffers).toHaveBeenLastCalledWith({
        company: 'Empresa A'
      })
    })
  })

  it('should login the user when submitting the login form', async () => {
    vi.mocked(getAuthenticatedUser).mockResolvedValue(null)

    render(<App />)

    await waitFor(() => {
      expect(getAuthenticatedUser).toHaveBeenCalled()
    })

    fireEvent.change(screen.getByLabelText('Email'), {
      target: {
        value: 'usuario@email.com'
      }
    })

    fireEvent.change(screen.getByLabelText('Contraseña'), {
      target: {
        value: 'password123'
      }
    })

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Iniciar sesión'
      })
    )

    await waitFor(() => {
      expect(login).toHaveBeenCalledWith(
        'usuario@email.com',
        'password123'
      )
    })
  })

  it('should show an error when login fails', async () => {
    vi.mocked(getAuthenticatedUser).mockResolvedValue(null)

    vi.mocked(login).mockRejectedValue(
      new Error('No se pudo iniciar sesión')
    )

    render(<App />)

    await waitFor(() => {
      expect(getAuthenticatedUser).toHaveBeenCalled()
    })

    fireEvent.change(screen.getByLabelText('Email'), {
      target: {
        value: 'usuario@email.com'
      }
    })

    fireEvent.change(screen.getByLabelText('Contraseña'), {
      target: {
        value: 'password123'
      }
    })

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Iniciar sesión'
      })
    )

    await waitFor(() => {
      expect(
        screen.getByText('No se pudo iniciar sesión')
      ).toBeInTheDocument()
    })
  })
})