import { fireEvent, render, screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import OfferFilters from './OfferFilters'

describe('OfferFilters', () => {
  it('should submit the selected filters', () => {
    const onFilter = vi.fn()

    render(<OfferFilters onFilter={onFilter} />)

    fireEvent.change(screen.getByLabelText('Empresa'), {
      target: { value: 'Empresa A' }
    })

    fireEvent.change(screen.getByLabelText('Ubicación'), {
      target: { value: 'Madrid' }
    })

    fireEvent.change(screen.getByLabelText('Modalidad'), {
      target: { value: 'REMOTE' }
    })

    fireEvent.change(screen.getByLabelText('Estado'), {
      target: { value: 'PENDIENTE' }
    })

    fireEvent.change(screen.getByLabelText('Buscar'), {
      target: { value: 'Java' }
    })

    fireEvent.click(
      screen.getByRole('button', { name: 'Filtrar' })
    )

    expect(onFilter).toHaveBeenCalledWith({
      company: 'Empresa A',
      location: 'Madrid',
      workMode: 'REMOTE',
      status: 'PENDIENTE',
      search: 'Java'
    })
  })

  it('should submit empty filters', () => {
    const onFilter = vi.fn()

    render(<OfferFilters onFilter={onFilter} />)

    fireEvent.click(
      screen.getByRole('button', { name: 'Filtrar' })
    )

    expect(onFilter).toHaveBeenCalledWith({})
  })
  it('should submit sorting filters', () => {
  const onFilter = vi.fn()

  render(<OfferFilters onFilter={onFilter} />)

  fireEvent.change(screen.getByLabelText('Ordenar por'), {
    target: { value: 'createdAt' }
  })

  fireEvent.change(screen.getByLabelText('Dirección'), {
    target: { value: 'desc' }
  })

  fireEvent.click(
    screen.getByRole('button', { name: 'Filtrar' })
  )

  expect(onFilter).toHaveBeenCalledWith({
    sortBy: 'createdAt',
    sortDirection: 'desc'
  })
})
it('should submit pagination filters', () => {
  const onFilter = vi.fn()

  render(<OfferFilters onFilter={onFilter} />)

  fireEvent.change(screen.getByLabelText('Ofertas por página'), {
    target: { value: '20' }
  })

  fireEvent.change(screen.getByLabelText('Página'), {
    target: { value: '2' }
  })

  fireEvent.click(
    screen.getByRole('button', { name: 'Filtrar' })
  )

  expect(onFilter).toHaveBeenCalledWith({
    page: 2,
    size: 20
  })
})
})