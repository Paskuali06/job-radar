import { useEffect, useState } from 'react'
import {
  getDashboard,
  type DashboardMetrics
} from './dashboardService'

function Dashboard() {
  const [metrics, setMetrics] = useState<DashboardMetrics | null>(null)
  const [error, setError] = useState(false)

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const result = await getDashboard()
        setMetrics(result)
      } catch {
        setError(true)
      }
    }

    loadDashboard()
  }, [])

  if (error) {
    return <p>No se pudieron cargar los datos del dashboard</p>
  }

  if (!metrics) {
    return <p>Cargando dashboard...</p>
  }

  return (
    <section>
      <h2>Dashboard</h2>

      <article>
        <h3>Total de ofertas</h3>
        <p>{metrics.totalOffers}</p>
      </article>

      <article>
        <h3>Pendientes</h3>
        <p>{metrics.pendingOffers}</p>
      </article>

      <article>
        <h3>Solicitadas</h3>
        <p>{metrics.appliedOffers}</p>
      </article>

      <article>
        <h3>Rechazadas</h3>
        <p>{metrics.rejectedOffers}</p>
      </article>
    </section>
  )
}

export default Dashboard