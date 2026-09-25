import { fireEvent, render, screen, waitFor } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import Account from './Account'
import { getAccount, logout } from './accountService'

vi.mock('./accountService', () => ({
  getAccount: vi.fn(),
  logout: vi.fn()
}))

describe('Account', () => {
  it('should display the account information', async () => {
    vi.mocked(getAccount).mockResolvedValue({
      id: 1,
      name: 'Jaime',
      email: 'usuario@email.com',
      role: 'USER'
    })

    render(<Account />)

    await waitFor(() => {
      expect(screen.getByText('Jaime')).toBeInTheDocument()
    })

    expect(screen.getByText('usuario@email.com')).toBeInTheDocument()
    expect(screen.getByText('USER')).toBeInTheDocument()
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
    vi.mocked(getAccount).mockResolvedValue({
      id: 1,
      name: 'Jaime',
      email: 'usuario@email.com',
      role: 'USER'
    })

    vi.mocked(logout).mockResolvedValue(undefined)

    render(<Account />)

    await waitFor(() => {
      expect(screen.getByText('Jaime')).toBeInTheDocument()
    })

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
    vi.mocked(getAccount).mockResolvedValue({
      id: 1,
      name: 'Jaime',
      email: 'usuario@email.com',
      role: 'USER'
    })

    vi.mocked(logout).mockRejectedValue(
      new Error('No se pudo cerrar sesión')
    )

    render(<Account />)

    await waitFor(() => {
      expect(screen.getByText('Jaime')).toBeInTheDocument()
    })

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