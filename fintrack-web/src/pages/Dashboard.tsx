import { useEffect, useState } from 'react'
import Navbar from '../components/Navbar'
import api from '../services/api'

interface Conta {
  id: number
  nome: string
  saldo: number
  tipoConta: string
}

export default function Dashboard() {
  const [contas, setContas] = useState<Conta[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/api/contas')
      .then((res) => setContas(res.data))
      .finally(() => setLoading(false))
  }, [])

  const saldoTotal = contas.reduce((acc, conta) => acc + conta.saldo, 0)

  return (
    <div>
      <Navbar />

      <div style={styles.container}>
        <h2 style={styles.titulo}>Dashboard</h2>

        <div style={styles.cardTotal}>
          <p style={styles.cardLabel}>Saldo Total</p>
          <p style={styles.cardValor}>
            {saldoTotal.toLocaleString('pt-BR', {
              style: 'currency',
              currency: 'BRL',
            })}
          </p>
        </div>

        <h3 style={styles.subtitulo}>Suas Contas</h3>

        {loading ? (
          <p>Carregando...</p>
        ) : contas.length === 0 ? (
          <p style={styles.vazio}>Nenhuma conta cadastrada ainda.</p>
        ) : (
          <div style={styles.grid}>
            {contas.map((conta) => (
              <div key={conta.id} style={styles.card}>
                <p style={styles.contaNome}>{conta.nome}</p>
                <p style={styles.contaTipo}>{conta.tipoConta}</p>
                <p style={styles.contaSaldo}>
                  {conta.saldo.toLocaleString('pt-BR', {
                    style: 'currency',
                    currency: 'BRL',
                  })}
                </p>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

const styles: Record<string, React.CSSProperties> = {
  container: {
    padding: '2rem',
    maxWidth: '1100px',
    margin: '0 auto',
  },
  titulo: {
    fontSize: '1.8rem',
    marginBottom: '1.5rem',
    color: '#1a1a2e',
  },
  cardTotal: {
    backgroundColor: '#4f46e5',
    color: '#fff',
    padding: '2rem',
    borderRadius: '12px',
    marginBottom: '2rem',
  },
  cardLabel: {
    fontSize: '1rem',
    opacity: 0.85,
    marginBottom: '0.5rem',
  },
  cardValor: {
    fontSize: '2.5rem',
    fontWeight: '700',
  },
  subtitulo: {
    fontSize: '1.2rem',
    marginBottom: '1rem',
    color: '#444',
  },
  grid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
    gap: '1rem',
  },
  card: {
    backgroundColor: '#fff',
    padding: '1.5rem',
    borderRadius: '12px',
    boxShadow: '0 2px 10px rgba(0,0,0,0.08)',
  },
  contaNome: {
    fontWeight: '700',
    fontSize: '1.1rem',
    marginBottom: '0.3rem',
  },
  contaTipo: {
    fontSize: '0.85rem',
    color: '#888',
    marginBottom: '0.8rem',
  },
  contaSaldo: {
    fontSize: '1.4rem',
    fontWeight: '700',
    color: '#4f46e5',
  },
  vazio: {
    color: '#888',
    fontStyle: 'italic',
  },
}