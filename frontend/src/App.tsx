import { useEffect, useState } from 'react'
import LoginForm from './auth/LoginForm'
import { getOffers, type JobOffer } from './offers/offerService'
import OfferList from './offers/OfferList'

function App() {
  const [offers, setOffers] = useState<JobOffer[]>([])
  const [error, setError] = useState(false)

  useEffect(() => {
    const loadOffers = async () => {
      try {
        const result = await getOffers()
        setOffers(result)
      } catch {
        setError(true)
      }
    }

    loadOffers()
  }, [])

  const handleLogin = (email: string, password: string) => {
    console.log(email, password)
  }

  return (
    <main>
      <h1>Job-Radar</h1>

      <LoginForm onLogin={handleLogin} />

      {error ? (
        <p>No se pudieron cargar las ofertas</p>
      ) : (
        <OfferList offers={offers} />
      )}
    </main>
  )
}

export default App