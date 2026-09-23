export async function login(
  email: string,
  password: string
): Promise<void> {
  const response = await fetch('/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      email,
      password
    })
  })

  if (!response.ok) {
    throw new Error('Error al iniciar sesión')
  }
}