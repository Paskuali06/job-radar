import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import AddOfferForm from './AddOfferForm'
import { createOffer } from './offerService'

vi.mock('./offerService', () => ({
  createOffer: vi.fn()
}))

describe('AddOfferForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should render the fields needed to create an offer', () => {
    render(
      <AddOfferForm
        onSaved={vi.fn()}
        onCancel={vi.fn()}
      />
    )

    expect(screen.getByLabelText('Empresa')).toBeInTheDocument()
    expect(screen.getByLabelText('Título')).toBeInTheDocument()
    expect(screen.getByLabelText('URL')).toBeInTheDocument()
    expect(screen.getByLabelText('Ubicación')).toBeInTheDocument()
    expect(screen.getByLabelText('Modalidad')).toBeInTheDocument()
  })

  it('should create an offer with the entered data', async () => {
    vi.mocked(createOffer).mockResolvedValue({
      id: 1,
      company: 'Google',
      title: 'Java Developer',
      location: 'Madrid',
      workMode: 'HYBRID',
      url: 'https://example.com/job',
      status: 'PENDIENTE'
    })

    const onSaved = vi.fn()

    render(
      <AddOfferForm
        onSaved={onSaved}
        onCancel={vi.fn()}
      />
    )

    fireEvent.change(screen.getByLabelText('Empresa'), {
      target: { value: 'Google' }
    })

    fireEvent.change(screen.getByLabelText('Título'), {
      target: { value: 'Java Developer' }
    })

    fireEvent.change(screen.getByLabelText('URL'), {
      target: { value: 'https://example.com/job' }
    })

    fireEvent.change(screen.getByLabelText('Ubicación'), {
      target: { value: 'Madrid' }
    })

    fireEvent.change(screen.getByLabelText('Modalidad'), {
      target: { value: 'HYBRID' }
    })

    fireEvent.click(
      screen.getByRole('button', { name: 'Guardar oferta' })
    )

    await waitFor(() => {
      expect(createOffer).toHaveBeenCalledWith({
        company: 'Google',
        title: 'Java Developer',
        url: 'https://example.com/job',
        location: 'Madrid',
        workMode: 'HYBRID'
      })
    })

    expect(onSaved).toHaveBeenCalled()
  })

  it('should cancel the form', () => {
    const onCancel = vi.fn()

    render(
      <AddOfferForm
        onSaved={vi.fn()}
        onCancel={onCancel}
      />
    )

    fireEvent.click(
      screen.getByRole('button', { name: 'Cancelar' })
    )

    expect(onCancel).toHaveBeenCalled()
  })
})