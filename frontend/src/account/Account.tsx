import { useEffect, useState } from 'react'
import {
  getAccount,
  logout,
  type Account as AccountData
} from './accountService'

function Account() {
  const [account, setAccount] = useState<AccountData | null>(null)
  const [error, setError] = useState('')
  const [isOpen, setIsOpen] = useState(false)

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
    <div className="account-menu">
      <button
        type="button"
        className="account-trigger"
        aria-label="Abrir cuenta"
        aria-expanded={isOpen}
        onClick={() => setIsOpen((current) => !current)}
      >
        {account.name}
      </button>

      {isOpen && (
        <div className="account-dropdown">
          <p className="account-dropdown-name">
            {account.name}
          </p>

          <p className="account-dropdown-email">
            {account.email}
          </p>

          <button
            type="button"
            className="btn danger block"
            onClick={handleLogout}
          >
            Cerrar sesión
          </button>

          {error && (
            <p className="form-error">
              {error}
            </p>
          )}
        </div>
      )}
    </div>
  )
}

export default Account