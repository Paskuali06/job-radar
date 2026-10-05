import { useEffect, useState } from 'react'
import {
  getAuthenticatedUser,
  login,
  type AuthenticatedUser
} from './auth/authService'
import LoginForm from './auth/LoginForm'
import Account from './account/Account'
import Dashboard from './dashboard/Dashboard'
import OfferFilters from './offers/OfferFilters'
import {
  getOffers,
  type JobOffer,
  type JobOfferFilters
} from './offers/offerService'
import OfferList from './offers/OfferList'

function App() {
  const [user, setUser] = useState<AuthenticatedUser | null>(null)
  const [checkingSession, setCheckingSession] = useState(true)
  const [offers, setOffers] = useState<JobOffer[]>([])
  const [error, setError] = useState(false)
  const [loginError, setLoginError] = useState(false)

  useEffect(() => {
    getAuthenticatedUser()
      .then(setUser)
      .finally(() => setCheckingSession(false))
  }, [])

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
    if (user) {
      loadOffers()
    }
  }, [user])

  const handleLogin = async (
    email: string,
    password: string
  ) => {
    try {
      setLoginError(false)

      await login(email, password)

      const authenticatedUser =
        await getAuthenticatedUser()

      if (!authenticatedUser) {
        throw new Error('Sesión no disponible')
      }

      setUser(authenticatedUser)
    } catch {
      setLoginError(true)
    }
  }

  if (checkingSession) {
    return (
      <div className="auth">
        <div className="auth-panel">
          <p className="loading">
            Comprobando sesión...
          </p>
        </div>
      </div>
    )
  }

  if (!user) {
    return (
      <div className="auth">
        <section className="auth-scope">
          <div className="scope">
            <svg
              className="scope-svg"
              viewBox="0 0 500 500"
              aria-hidden="true"
            >
              <circle
                className="ring"
                cx="250"
                cy="250"
                r="220"
              />

              <circle
                className="ring"
                cx="250"
                cy="250"
                r="165"
              />

              <circle
                className="ring"
                cx="250"
                cy="250"
                r="110"
              />

              <circle
                className="ring"
                cx="250"
                cy="250"
                r="55"
              />

              <circle
                className="halo pending"
                cx="330"
                cy="165"
                r="12"
              />

              <circle
                className="blip pending"
                cx="330"
                cy="165"
                r="5"
              />

              <circle
                className="halo pending"
                cx="170"
                cy="315"
                r="12"
              />

              <circle
                className="blip pending"
                cx="170"
                cy="315"
                r="5"
              />

              <circle
                className="blip applied"
                cx="365"
                cy="280"
                r="5"
              />

              <circle
                className="blip rejected"
                cx="125"
                cy="180"
                r="5"
              />
            </svg>

            <div className="sweep" />
          </div>
        </section>

        <section className="auth-panel">
          <div>
            <span className="brand-name">
              JOB-RADAR
            </span>

            <h1 className="auth-title">
              Encuentra tu próxima oportunidad.
            </h1>

            <p className="auth-lead">
              Gestiona y sigue tus oportunidades
              profesionales desde un único lugar.
            </p>
          </div>

          <LoginForm onLogin={handleLogin} />

          {loginError && (
            <p className="form-error">
              No se pudo iniciar sesión
            </p>
          )}
        </section>
      </div>
    )
  }

  return (
    <div className="app-shell">
      <header className="topbar">
        <div className="brand">
          <span className="brand-name">
            JOB-RADAR
          </span>
        </div>

        <Account />
      </header>

      <main className="page">
        <section className="section">
          <div className="section-header">
            <div>
              <h2 className="section-title">
                Dashboard
              </h2>

              <p className="section-subtitle">
                Resumen de tus ofertas de empleo
              </p>
            </div>
          </div>

          <Dashboard />
        </section>

        <section className="section">
          <div className="section-header">
            <div>
              <h2 className="section-title">
                Ofertas
              </h2>

              <p className="section-subtitle">
                Busca y gestiona tus oportunidades
              </p>
            </div>
          </div>

          <OfferFilters onFilter={loadOffers} />

          {error ? (
            <p className="error-message">
              No se pudieron cargar las ofertas
            </p>
          ) : (
            <OfferList offers={offers} />
          )}
        </section>
      </main>
    </div>
  )
}

export default App