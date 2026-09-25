export type Account = {
  id: number
  name: string
  email: string
  role: string
}

export async function getAccount(): Promise<Account> {
  const response = await fetch('/auth/me')

  if (!response.ok) {
    throw new Error('No se pudo obtener la cuenta')
  }

  return response.json()
}

export async function logout(): Promise<void> {
  const response = await fetch('/auth/logout', {
    method: 'POST'
  })

  if (!response.ok) {
    throw new Error('No se pudo cerrar sesión')
  }
}