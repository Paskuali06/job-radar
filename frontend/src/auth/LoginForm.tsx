import { useState } from 'react'
import type { FormEvent } from 'react'
import { register } from './authService'

type LoginFormProps = {
  onLogin: (email: string, password: string) => Promise<void>
}

function LoginForm({ onLogin }: LoginFormProps) {
  const [mode, setMode] = useState<'login' | 'register'>('login')
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [registerError, setRegisterError] = useState('')
  const [registerSuccess, setRegisterSuccess] = useState(false)

  const handleSubmit = async (
    event: FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault()

    if (mode === 'login') {
      await onLogin(email, password)
      return
    }

    try {
      setRegisterError('')
      setRegisterSuccess(false)

      await register(name, email, password)

      setMode('login')
      setName('')
      setPassword('')
      setRegisterSuccess(true)
    } catch {
      setRegisterSuccess(false)
      setRegisterError('No se pudo crear la cuenta')
    }
  }

  const changeMode = (nextMode: 'login' | 'register') => {
    setMode(nextMode)
    setRegisterError('')
    setRegisterSuccess(false)
  }

  return (
    <>
      <div className="tabs">
        <button
          type="button"
          className="tab"
          aria-label="Cambiar a modo iniciar sesión"
          aria-selected={mode === 'login'}
          onClick={() => changeMode('login')}
        >
          Iniciar sesión
        </button>

        <button
          type="button"
          className="tab"
          aria-selected={mode === 'register'}
          onClick={() => changeMode('register')}
        >
          Crear cuenta
        </button>
      </div>

      {registerSuccess && (
        <p className="form-success">
          Cuenta creada con éxito
        </p>
      )}

      <form className="form" onSubmit={handleSubmit}>
        {mode === 'register' && (
          <div className="field">
            <label className="field-label" htmlFor="name">
              Nombre
            </label>

            <input
              id="name"
              type="text"
              value={name}
              onChange={(event) => setName(event.target.value)}
              required
            />
          </div>
        )}

        <div className="field">
          <label className="field-label" htmlFor="email">
            Email
          </label>

          <input
            id="email"
            type="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
        </div>

        <div className="field">
          <label className="field-label" htmlFor="password">
            Contraseña
          </label>

          <input
            id="password"
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
        </div>

        {registerError && (
          <p className="form-error">
            {registerError}
          </p>
        )}

        <button type="submit" className="btn primary block">
          {mode === 'login'
            ? 'Iniciar sesión'
            : 'Crear cuenta'}
        </button>
      </form>
    </>
  )
}

export default LoginForm