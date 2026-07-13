import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { nome, logout } = useAuth()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate('/login')
  }

  return (
    <nav style={styles.nav}>
      <span style={styles.logo}>💸 FinTrack</span>

      <div style={styles.links}>
        <button onClick={() => navigate('/dashboard')} style={styles.link}>
          Dashboard
        </button>
        <button onClick={() => navigate('/contas')} style={styles.link}>
          Contas
        </button>
        <button onClick={() => navigate('/transacoes')} style={styles.link}>
          Transações
        </button>
      </div>

      <div style={styles.user}>
        <span style={styles.nome}>Olá, {nome}</span>
        <button onClick={handleLogout} style={styles.logout}>
          Sair
        </button>
      </div>
    </nav>
  )
}

const styles: Record<string, React.CSSProperties> = {
  nav: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: '1rem 2rem',
    backgroundColor: '#4f46e5',
    color: '#fff',
  },
  logo: {
    fontSize: '1.4rem',
    fontWeight: '700',
  },
  links: {
    display: 'flex',
    gap: '1rem',
  },
  link: {
    background: 'none',
    border: 'none',
    color: '#fff',
    fontSize: '1rem',
    cursor: 'pointer',
    padding: '0.4rem 0.8rem',
    borderRadius: '6px',
  },
  user: {
    display: 'flex',
    alignItems: 'center',
    gap: '1rem',
  },
  nome: {
    fontSize: '0.9rem',
  },
  logout: {
    background: 'rgba(255,255,255,0.2)',
    border: 'none',
    color: '#fff',
    padding: '0.4rem 0.8rem',
    borderRadius: '6px',
    cursor: 'pointer',
    fontSize: '0.9rem',
  },
}