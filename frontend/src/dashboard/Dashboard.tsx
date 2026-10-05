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
    return (
      <p className="error-message">
        No se pudieron cargar los datos del dashboard
      </p>
    )
  }

  if (!metrics) {
    return <p className="loading">Cargando dashboard...</p>
  }

  const total = metrics.totalOffers

  const pendingPercentage =
    total > 0 ? (metrics.pendingOffers / total) * 100 : 0

  const appliedPercentage =
    total > 0 ? (metrics.appliedOffers / total) * 100 : 0

  const rejectedPercentage =
    total > 0 ? (metrics.rejectedOffers / total) * 100 : 0

  return (
    <div className="dashboard-content">
      <div className="dashboard-grid">
        <article className="dashboard-card">
          <h3>Total de ofertas</h3>
          <p className="dashboard-value">
            {metrics.totalOffers}
          </p>
        </article>

        <article className="dashboard-card pending">
          <h3>Pendientes</h3>
          <p className="dashboard-value">
            {metrics.pendingOffers}
          </p>
        </article>

        <article className="dashboard-card applied">
          <h3>Solicitadas</h3>
          <p className="dashboard-value">
            {metrics.appliedOffers}
          </p>
        </article>

        <article className="dashboard-card rejected">
          <h3>Rechazadas</h3>
          <p className="dashboard-value">
            {metrics.rejectedOffers}
          </p>
        </article>
      </div>

      <div className="status-summary">
        <div className="status-summary-header">
          <div>
            <h3>Estado de tus ofertas</h3>
            <p>
              Distribución actual de tus oportunidades
            </p>
          </div>
        </div>

        <div className="status-bar">
          <span
            className="status-bar-pending"
            style={{
              width: `${pendingPercentage}%`
            }}
          />

          <span
            className="status-bar-applied"
            style={{
              width: `${appliedPercentage}%`
            }}
          />

          <span
            className="status-bar-rejected"
            style={{
              width: `${rejectedPercentage}%`
            }}
          />
        </div>

        <div className="status-legend">
          <div className="status-item">
            <span className="status-dot pending" />
            <span>Pendientes</span>
            <strong>{metrics.pendingOffers}</strong>
          </div>

          <div className="status-item">
            <span className="status-dot applied" />
            <span>Solicitadas</span>
            <strong>{metrics.appliedOffers}</strong>
          </div>

          <div className="status-item">
            <span className="status-dot rejected" />
            <span>Rechazadas</span>
            <strong>{metrics.rejectedOffers}</strong>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Dashboard