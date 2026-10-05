export type AuthenticatedUser = {
  id: number
  name: string
  email: string
  role: string
}

export async function login(
  email: string,
  password: string
): Promise<void> {
  const response = await fetch('/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: new URLSearchParams({
      email,
      password
    }),
    credentials: 'include'
  })

  if (!response.ok) {
    throw new Error('No se pudo iniciar sesión')
  }
}

export async function register(
  name: string,
  email: string,
  password: string
): Promise<void> {
  const response = await fetch('/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      name,
      email,
      password
    }),
    credentials: 'include'
  })

  if (!response.ok) {
    throw new Error('No se pudo crear la cuenta')
  }
}

export async function getAuthenticatedUser(): Promise<AuthenticatedUser | null> {
  const response = await fetch('/auth/me', {
    credentials: 'include'
  })

  if (!response.ok) {
    return null
  }

  return response.json()
}

export async function logout(): Promise<void> {
  const response = await fetch('/auth/logout', {
    method: 'POST',
    credentials: 'include'
  })

  if (!response.ok) {
    throw new Error('No se pudo cerrar sesión')
  }
}