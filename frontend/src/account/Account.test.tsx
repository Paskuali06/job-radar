import {
  fireEvent,
  render,
  screen,
  waitFor
} from '@testing-library/react'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import Account from './Account'
import {
  getAccount,
  logout
} from './accountService'

vi.mock('./accountService', () => ({
  getAccount: vi.fn(),
  logout: vi.fn()
}))

describe('Account', () => {
  beforeEach(() => {
    vi.resetAllMocks()

    vi.mocked(getAccount).mockResolvedValue({
      id: 1,
      name: 'Jaime',
      email: 'usuario@email.com',
      role: 'USER'
    })

    vi.mocked(logout).mockResolvedValue(undefined)
  })

  it('should display the account information', async () => {
    render(<Account />)

    await waitFor(() => {
      expect(
        screen.getByRole('button', {
          name: 'Abrir cuenta'
        })
      ).toBeInTheDocument()
    })

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Abrir cuenta'
      })
    )

    expect(
      screen.getAllByText('Jaime')
    ).toHaveLength(2)

    expect(
      screen.getByText('usuario@email.com')
    ).toBeInTheDocument()
  })

  it('should show an error when loading the account fails', async () => {
    vi.mocked(getAccount).mockRejectedValue(
      new Error('No se pudo obtener la cuenta')
    )

    render(<Account />)

    await waitFor(() => {
      expect(
        screen.getByText('No se pudo obtener la cuenta')
      ).toBeInTheDocument()
    })
  })

  it('should logout the user when clicking the logout button', async () => {
    render(<Account />)

    await waitFor(() => {
      expect(
        screen.getByRole('button', {
          name: 'Abrir cuenta'
        })
      ).toBeInTheDocument()
    })

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Abrir cuenta'
      })
    )

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Cerrar sesión'
      })
    )

    await waitFor(() => {
      expect(logout).toHaveBeenCalled()
    })
  })

  it('should show an error when logout fails', async () => {
    vi.mocked(logout).mockRejectedValue(
      new Error('No se pudo cerrar sesión')
    )

    render(<Account />)

    await waitFor(() => {
      expect(
        screen.getByRole('button', {
          name: 'Abrir cuenta'
        })
      ).toBeInTheDocument()
    })

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Abrir cuenta'
      })
    )

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Cerrar sesión'
      })
    )

    await waitFor(() => {
      expect(
        screen.getByText('No se pudo cerrar sesión')
      ).toBeInTheDocument()
    })
  })
})