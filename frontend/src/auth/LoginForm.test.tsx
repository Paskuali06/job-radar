import {
  fireEvent,
  render,
  screen,
  waitFor
} from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import LoginForm from './LoginForm'
import { register } from './authService'

vi.mock('./authService', () => ({
  register: vi.fn()
}))

describe('LoginForm', () => {
  it('should render the login form', () => {
    render(<LoginForm onLogin={vi.fn()} />)

    expect(screen.getByLabelText('Email')).toBeInTheDocument()
    expect(screen.getByLabelText('Contraseña')).toBeInTheDocument()

    expect(
      screen.getByRole('button', {
        name: 'Iniciar sesión'
      })
    ).toBeInTheDocument()
  })

  it('should submit the email and password', async () => {
    const onLogin = vi.fn()

    render(<LoginForm onLogin={onLogin} />)

    fireEvent.change(screen.getByLabelText('Email'), {
      target: {
        value: 'usuario@email.com'
      }
    })

    fireEvent.change(screen.getByLabelText('Contraseña'), {
      target: {
        value: 'password123'
      }
    })

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Iniciar sesión'
      })
    )

    await waitFor(() => {
      expect(onLogin).toHaveBeenCalledWith(
        'usuario@email.com',
        'password123'
      )
    })
  })

  it('should create an account successfully', async () => {
    vi.mocked(register).mockResolvedValue(undefined)

    render(<LoginForm onLogin={vi.fn()} />)

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Crear cuenta'
      })
    )

    fireEvent.change(screen.getByLabelText('Nombre'), {
      target: {
        value: 'Jaime'
      }
    })

    fireEvent.change(screen.getByLabelText('Email'), {
      target: {
        value: 'jaime@email.com'
      }
    })

    fireEvent.change(screen.getByLabelText('Contraseña'), {
      target: {
        value: 'password123'
      }
    })

    fireEvent.click(
      screen.getAllByRole('button', {
        name: 'Crear cuenta'
      })[1]
    )

    await waitFor(() => {
      expect(register).toHaveBeenCalledWith(
        'Jaime',
        'jaime@email.com',
        'password123'
      )
    })

    expect(
      screen.getByText('Cuenta creada con éxito')
    ).toBeInTheDocument()
  })

  it('should show an error when account creation fails', async () => {
    vi.mocked(register).mockRejectedValue(
      new Error('No se pudo crear la cuenta')
    )

    render(<LoginForm onLogin={vi.fn()} />)

    fireEvent.click(
      screen.getByRole('button', {
        name: 'Crear cuenta'
      })
    )

    fireEvent.change(screen.getByLabelText('Nombre'), {
      target: {
        value: 'Jaime'
      }
    })

    fireEvent.change(screen.getByLabelText('Email'), {
      target: {
        value: 'jaime@email.com'
      }
    })

    fireEvent.change(screen.getByLabelText('Contraseña'), {
      target: {
        value: 'password123'
      }
    })

    fireEvent.click(
      screen.getAllByRole('button', {
        name: 'Crear cuenta'
      })[1]
    )

    await waitFor(() => {
      expect(
        screen.getByText('No se pudo crear la cuenta')
      ).toBeInTheDocument()
    })
  })
})