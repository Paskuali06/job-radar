import { describe, expect, it, vi } from 'vitest'
import { login } from './authService'

describe('authService', () => {
  it('should send login credentials to the backend', async () => {
    const fetchMock = vi.fn().mockResolvedValue({
      ok: true
    })

    vi.stubGlobal('fetch', fetchMock)

    await login('usuario@email.com', 'password123')

    expect(fetchMock).toHaveBeenCalledWith(
      '/login',
      expect.objectContaining({
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          email: 'usuario@email.com',
          password: 'password123'
        })
      })
    )
  })
})