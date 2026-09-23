import { fireEvent, render, screen, waitFor } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import OfferList from './OfferList'
import { deleteOffer, updateOffer, updateOfferStatus } from './offerService'

vi.mock('./offerService', () => ({
  updateOfferStatus: vi.fn(),
  deleteOffer: vi.fn(),
  updateOffer: vi.fn()
}))

describe('OfferList', () => {
  it('should render the job offers', () => {
    const offers = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDING'
      },
      {
        id: 2,
        company: 'Empresa B',
        title: 'Spring Boot Developer',
        location: 'Barcelona',
        workMode: 'HYBRID',
        status: 'APPLIED'
      }
    ]

    render(<OfferList offers={offers} />)

    expect(screen.getByText('Java Developer')).toBeInTheDocument()
    expect(screen.getByText('Empresa A')).toBeInTheDocument()
    expect(screen.getByText('Madrid')).toBeInTheDocument()
    expect(screen.getByText('REMOTE')).toBeInTheDocument()
    expect(screen.getByText('PENDING')).toBeInTheDocument()

    expect(screen.getByText('Spring Boot Developer')).toBeInTheDocument()
    expect(screen.getByText('Empresa B')).toBeInTheDocument()
    expect(screen.getByText('Barcelona')).toBeInTheDocument()
    expect(screen.getByText('HYBRID')).toBeInTheDocument()
    expect(screen.getByText('APPLIED')).toBeInTheDocument()
  })

  it('should show a message when there are no job offers', () => {
    render(<OfferList offers={[]} />)

    expect(screen.getByText('No hay ofertas')).toBeInTheDocument()
  })

  it('should update the offer status when clicking the button', async () => {
    vi.mocked(updateOfferStatus).mockResolvedValue(undefined)

    const offers = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDING'
      }
    ]

    render(<OfferList offers={offers} />)

    fireEvent.click(
      screen.getByRole('button', { name: 'Marcar como aplicada' })
    )

    await waitFor(() => {
      expect(updateOfferStatus).toHaveBeenCalledWith(1, 'APPLIED')
    })
  })

  it('should show the updated status after applying an offer', async () => {
    vi.mocked(updateOfferStatus).mockResolvedValue(undefined)

    const offers = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDING'
      }
    ]

    render(<OfferList offers={offers} />)

    expect(screen.getByText('PENDING')).toBeInTheDocument()

    fireEvent.click(
      screen.getByRole('button', { name: 'Marcar como aplicada' })
    )

    await waitFor(() => {
      expect(screen.getByText('APPLIED')).toBeInTheDocument()
    })

    expect(screen.queryByText('PENDING')).not.toBeInTheDocument()
  })

  it('should delete the offer when clicking the delete button', async () => {
    vi.mocked(deleteOffer).mockResolvedValue(undefined)

    const offers = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDING'
      }
    ]

    render(<OfferList offers={offers} />)

    fireEvent.click(
      screen.getByRole('button', { name: 'Eliminar oferta' })
    )

    await waitFor(() => {
      expect(deleteOffer).toHaveBeenCalledWith(1)
    })
  })
  it('should update the offer when saving the edit form', async () => {
  vi.mocked(updateOffer).mockResolvedValue(undefined)

  const offers = [
    {
      id: 1,
      company: 'Empresa A',
      title: 'Java Developer',
      location: 'Madrid',
      workMode: 'REMOTE',
      status: 'PENDING'
    }
  ]

  render(<OfferList offers={offers} />)

  fireEvent.click(
    screen.getByRole('button', { name: 'Editar oferta' })
  )

  fireEvent.change(screen.getByLabelText('Empresa'), {
    target: { value: 'Empresa Nueva' }
  })

  fireEvent.change(screen.getByLabelText('Título'), {
    target: { value: 'Senior Java Developer' }
  })

  fireEvent.click(
    screen.getByRole('button', { name: 'Guardar cambios' })
  )

  await waitFor(() => {
    expect(updateOffer).toHaveBeenCalledWith(1, {
      company: 'Empresa Nueva',
      title: 'Senior Java Developer',
      location: 'Madrid',
      workMode: 'REMOTE',
      url: '',
      description: ''
    })
  })
})
})