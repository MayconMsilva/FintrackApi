import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { login as loginService } from '../services/authService'

export default function Login() {
  const [email, setEmail] = useState('')
  const [senha, setSenha] = useState('')
  const [erro, setErro] = useState('')
  const [loading, setLoading] = useState(false)

  const { login } = useAuth()
  const navigate = useNavigate()

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setErro('')
    setLoading(true)

    try {
      const response = await loginService({ email, senha })
      login(response.token, response.nome)
      navigate('/dashboard')
    } catch {
      setErro('Email ou senha inválidos')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h1 style={styles.title}>💸 FinTrack</h1>
        <p style={styles.subtitle}>Gestão financeira pessoal</p>

        <form onSubmit={handleSubmit} style={styles.form}>
          <div style={styles.field}>
            <label style={styles.label}>Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              style={styles.input}
              placeholder="seu@email.com"
              required
            />
          </div>

          <div style={styles.field}>
            <label style={styles.label}>Senha</label>
            <input
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              style={styles.input}
              placeholder="••••••"
              required
            />
          </div>

          {erro && <p style={styles.erro}>{erro}</p>}

          <button
            type="submit"
            style={styles.button}
            disabled={loading}
          >
            {loading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        <p style={styles.link}>
          Não tem conta?{' '}
          <Link to="/cadastro" style={styles.linkText}>
            Cadastre-se
          </Link>
        </p>
      </div>
    </div>
  )
}

const styles: Record<string, React.CSSProperties> = {
  container: {
    minHeight: '100vh',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#f0f2f5',
  },
  card: {
    backgroundColor: '#fff',
    padding: '2rem',
    borderRadius: '12px',
    boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
    width: '100%',
    maxWidth: '400px',
  },
  title: {
    fontSize: '2rem',
    textAlign: 'center',
    color: '#1a1a2e',
    marginBottom: '0.25rem',
  },
  subtitle: {
    textAlign: 'center',
    color: '#666',
    marginBottom: '2rem',
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: '1rem',
  },
  field: {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.4rem',
  },
  label: {
    fontSize: '0.9rem',
    fontWeight: '600',
    color: '#444',
  },
  input: {
    padding: '0.75rem',
    borderRadius: '8px',
    border: '1px solid #ddd',
    fontSize: '1rem',
    outline: 'none',
  },
  button: {
    padding: '0.85rem',
    backgroundColor: '#4f46e5',
    color: '#fff',
    border: 'none',
    borderRadius: '8px',
    fontSize: '1rem',
    fontWeight: '600',
    cursor: 'pointer',
    marginTop: '0.5rem',
  },
  erro: {
    color: '#e53e3e',
    fontSize: '0.875rem',
    textAlign: 'center',
  },
  link: {
    textAlign: 'center',
    marginTop: '1.5rem',
    color: '#666',
    fontSize: '0.9rem',
  },
  linkText: {
    color: '#4f46e5',
    fontWeight: '600',
    textDecoration: 'none',
  },
}