export type DashboardMetrics = {
  totalOffers: number
  pendingOffers: number
  appliedOffers: number
  rejectedOffers: number
}

export const getDashboard = async (): Promise<DashboardMetrics> => {
  const response = await fetch('/dashboard')

  if (!response.ok) {
    throw new Error('No se pudieron obtener los datos del dashboard')
  }

  return response.json()
}