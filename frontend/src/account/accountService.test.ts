import { describe, expect, it, vi } from 'vitest'
import {
  getAccount,
  logout
} from './accountService'

describe('accountService', () => {
  it('should get the authenticated user account', async () => {
    const account = {
      id: 1,
      name: 'Jaime',
      email: 'jaime@test.com',
      role: 'USER'
    }

    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue(account)
    })

    vi.stubGlobal('fetch', fetchMock)

    const result = await getAccount()

    expect(fetchMock).toHaveBeenCalledWith('/auth/me')
    expect(result).toEqual(account)
  })

  it('should throw when getting the account fails', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: false
    })

    vi.stubGlobal('fetch', fetchMock)

    await expect(getAccount()).rejects.toThrow(
      'No se pudo obtener la cuenta'
    )
  })

  it('should logout the authenticated user', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true
    })

    vi.stubGlobal('fetch', fetchMock)

    await logout()

    expect(fetchMock).toHaveBeenCalledWith('/auth/logout', {
      method: 'POST'
    })
  })

  it('should throw when logout fails', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: false
    })

    vi.stubGlobal('fetch', fetchMock)

    await expect(logout()).rejects.toThrow(
      'No se pudo cerrar sesión'
    )
  })
})