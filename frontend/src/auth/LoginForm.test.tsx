import { fireEvent, render, screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import LoginForm from './LoginForm'

describe('LoginForm', () => {
  it('should render the login form', () => {
    render(<LoginForm onLogin={vi.fn()} />)

    expect(screen.getByLabelText('Email')).toBeInTheDocument()
    expect(screen.getByLabelText('Contraseña')).toBeInTheDocument()
    expect(
      screen.getByRole('button', { name: 'Iniciar sesión' })
    ).toBeInTheDocument()
  })

  it('should submit the email and password', async () => {
    const onLogin = vi.fn()

    render(<LoginForm onLogin={onLogin} />)

    fireEvent.change(screen.getByLabelText('Email'), {
      target: { value: 'usuario@email.com' }
    })

    fireEvent.change(screen.getByLabelText('Contraseña'), {
      target: { value: 'password123' }
    })

    fireEvent.click(
      screen.getByRole('button', { name: 'Iniciar sesión' })
    )

    expect(onLogin).toHaveBeenCalledWith(
      'usuario@email.com',
      'password123'
    )
  })
})