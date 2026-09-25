import { useEffect, useState } from 'react'
import { login } from './auth/authService'
import LoginForm from './auth/LoginForm'
import Dashboard from './dashboard/Dashboard'
import OfferFilters from './offers/OfferFilters'
import {
  getOffers,
  type JobOffer,
  type JobOfferFilters
} from './offers/offerService'
import OfferList from './offers/OfferList'

function App() {
  const [offers, setOffers] = useState<JobOffer[]>([])
  const [error, setError] = useState(false)
  const [loginError, setLoginError] = useState(false)

  const loadOffers = async (filters: JobOfferFilters = {}) => {
    try {
      setError(false)

      const result = await getOffers(filters)

      setOffers(result)
    } catch {
      setError(true)
    }
  }

  useEffect(() => {
    loadOffers()
  }, [])

  const handleLogin = async (
    email: string,
    password: string
  ) => {
    try {
      setLoginError(false)

      await login(email, password)
    } catch {
      setLoginError(true)
    }
  }

  return (
    <main>
      <h1>Job-Radar</h1>

      <LoginForm onLogin={handleLogin} />

      {loginError && (
        <p>No se pudo iniciar sesión</p>
      )}

      <Dashboard />

      <OfferFilters onFilter={loadOffers} />

      {error ? (
        <p>No se pudieron cargar las ofertas</p>
      ) : (
        <OfferList offers={offers} />
      )}
    </main>
  )
}

export default App

