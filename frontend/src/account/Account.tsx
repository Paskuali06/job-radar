import { useEffect, useState } from 'react'
import {
  getAccount,
  logout,
  type Account as AccountData
} from './accountService'

function Account() {
  const [account, setAccount] = useState<AccountData | null>(null)
  const [error, setError] = useState('')

  const loadAccount = async () => {
    try {
      setError('')

      const result = await getAccount()

      setAccount(result)
    } catch {
      setError('No se pudo obtener la cuenta')
    }
  }

  useEffect(() => {
    loadAccount()
  }, [])

  const handleLogout = async () => {
    try {
      setError('')

      await logout()
    } catch {
      setError('No se pudo cerrar sesión')
    }
  }

  if (error && !account) {
    return <p>{error}</p>
  }

  if (!account) {
    return <p>Cargando cuenta...</p>
  }

  return (
    <section>
      <h2>Cuenta</h2>

      <p>{account.name}</p>
      <p>{account.email}</p>
      <p>{account.role}</p>

      <button
        type="button"
        onClick={handleLogout}
      >
        Cerrar sesión
      </button>

      {error && <p>{error}</p>}
    </section>
  )
}

export default Account