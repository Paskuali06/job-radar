import { fireEvent, render, screen, waitFor } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import OfferList from './OfferList'
import {
  deleteOffer,
  updateOffer,
  updateOfferStatus,
  type JobOffer
} from './offerService'

vi.mock('./offerService', () => ({
  updateOfferStatus: vi.fn(),
  deleteOffer: vi.fn(),
  updateOffer: vi.fn()
}))

describe('OfferList', () => {
  it('should render the job offers', () => {
    const offers: JobOffer[] = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        url: 'https://example.com/job-1',
        status: 'PENDIENTE'
      },
      {
        id: 2,
        company: 'Empresa B',
        title: 'Spring Boot Developer',
        location: 'Barcelona',
        workMode: 'HYBRID',
        url: 'https://example.com/job-2',
        status: 'SOLICITADA'
      }
    ]

    render(<OfferList offers={offers} />)

    expect(screen.getByText('Java Developer')).toBeInTheDocument()
    expect(screen.getByText('Empresa A')).toBeInTheDocument()
    expect(screen.getByText('Madrid')).toBeInTheDocument()
    expect(screen.getByText('REMOTE')).toBeInTheDocument()
    expect(screen.getByText('PENDIENTE')).toBeInTheDocument()

    expect(screen.getByText('Spring Boot Developer')).toBeInTheDocument()
    expect(screen.getByText('Empresa B')).toBeInTheDocument()
    expect(screen.getByText('Barcelona')).toBeInTheDocument()
    expect(screen.getByText('HYBRID')).toBeInTheDocument()
    expect(screen.getByText('SOLICITADA')).toBeInTheDocument()

    const offerLinks = screen.getAllByRole('link', {
      name: 'Ver oferta'
    })

    expect(offerLinks[0]).toHaveAttribute(
      'href',
      'https://example.com/job-1'
    )
  })

  it('should show a message when there are no job offers', () => {
    render(<OfferList offers={[]} />)

    expect(screen.getByText('No hay ofertas')).toBeInTheDocument()
  })

  it('should update the offer status when changing the state', async () => {
    vi.mocked(updateOfferStatus).mockResolvedValue(undefined)

    const offers: JobOffer[] = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDIENTE'
      }
    ]

    render(<OfferList offers={offers} />)

    fireEvent.change(
      screen.getByLabelText('Estado'),
      {
        target: {
          value: 'SOLICITADA'
        }
      }
    )

    await waitFor(() => {
      expect(updateOfferStatus).toHaveBeenCalledWith(
        1,
        'SOLICITADA'
      )
    })
  })

  it('should show the updated status after changing the state', async () => {
    vi.mocked(updateOfferStatus).mockResolvedValue(undefined)

    const offers: JobOffer[] = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDIENTE'
      }
    ]

    render(<OfferList offers={offers} />)

    expect(
      screen.getByText('PENDIENTE')
    ).toBeInTheDocument()

    fireEvent.change(
      screen.getByLabelText('Estado'),
      {
        target: {
          value: 'SOLICITADA'
        }
      }
    )

    await waitFor(() => {
      expect(
        screen.getByText('SOLICITADA')
      ).toBeInTheDocument()
    })

    expect(
      screen.queryByText('PENDIENTE')
    ).not.toBeInTheDocument()
  })

  it('should change the offer status to RECHAZADA', async () => {
    vi.mocked(updateOfferStatus).mockResolvedValue(undefined)

    const offers: JobOffer[] = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDIENTE'
      }
    ]

    render(<OfferList offers={offers} />)

    fireEvent.change(
      screen.getByLabelText('Estado'),
      {
        target: {
          value: 'RECHAZADA'
        }
      }
    )

    await waitFor(() => {
      expect(updateOfferStatus).toHaveBeenCalledWith(
        1,
        'RECHAZADA'
      )
    })
  })

  it('should change the offer status to PENDIENTE', async () => {
    vi.mocked(updateOfferStatus).mockResolvedValue(undefined)

    const offers: JobOffer[] = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'SOLICITADA'
      }
    ]

    render(<OfferList offers={offers} />)

    fireEvent.change(
      screen.getByLabelText('Estado'),
      {
        target: {
          value: 'PENDIENTE'
        }
      }
    )

    await waitFor(() => {
      expect(updateOfferStatus).toHaveBeenCalledWith(
        1,
        'PENDIENTE'
      )
    })
  })

  it('should delete the offer when clicking the delete button', async () => {
    vi.mocked(deleteOffer).mockResolvedValue(undefined)

    const offers: JobOffer[] = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        status: 'PENDIENTE'
      }
    ]

    render(<OfferList offers={offers} />)

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Eliminar oferta'
      })
    )

    await waitFor(() => {
      expect(deleteOffer).toHaveBeenCalledWith(1)
    })
  })

  it('should update the offer when saving the edit form', async () => {
    vi.mocked(updateOffer).mockResolvedValue(undefined)

    const offers: JobOffer[] = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        url: 'https://example.com/job-1',
        status: 'PENDIENTE'
      }
    ]

    render(<OfferList offers={offers} />)

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Editar oferta'
      })
    )

    fireEvent.change(screen.getByLabelText('Empresa'), {
      target: {
        value: 'Empresa Nueva'
      }
    })

    fireEvent.change(screen.getByLabelText('Título'), {
      target: {
        value: 'Senior Java Developer'
      }
    })

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Guardar cambios'
      })
    )

    await waitFor(() => {
      expect(updateOffer).toHaveBeenCalledWith(1, {
        company: 'Empresa Nueva',
        title: 'Senior Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        url: 'https://example.com/job-1',
        description: ''
      })
    })
  })

  it('should show the existing URL when editing an offer', () => {
    const offers: JobOffer[] = [
      {
        id: 1,
        company: 'Empresa A',
        title: 'Java Developer',
        location: 'Madrid',
        workMode: 'REMOTE',
        url: 'https://example.com/job-1',
        status: 'PENDIENTE'
      }
    ]

    render(<OfferList offers={offers} />)

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Editar oferta'
      })
    )

    expect(
      screen.getByLabelText('URL')
    ).toHaveValue(
      'https://example.com/job-1'
    )
  })
})