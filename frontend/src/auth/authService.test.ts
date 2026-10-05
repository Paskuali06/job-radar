import { describe, expect, it, vi } from 'vitest'
import { login } from './authService'

describe('authService', () => {
  it('should send login credentials to the backend', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true
    })

    vi.stubGlobal('fetch', fetchMock)

    await login('usuario@email.com', 'password123')

    expect(fetchMock).toHaveBeenCalledWith('/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({
        email: 'usuario@email.com',
        password: 'password123'
      }),
      credentials: 'include'
    })
  })

  it('should throw when login fails', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: false
    })

    vi.stubGlobal('fetch', fetchMock)

    await expect(
      login('usuario@email.com', 'password123')
    ).rejects.toThrow('No se pudo iniciar sesión')
  })
})