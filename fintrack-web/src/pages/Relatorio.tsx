import { useEffect, useState } from 'react'
import Navbar from '../components/Navbar'
import { listarContas } from '../services/contaService'
import type { Conta } from '../services/contaService'
import api from '../services/api'

interface Resumo {
  totalReceitas: number
  totalDespesas: number
  saldoPeriodo: number
  quantidadeTransacoes: number
}

export default function Relatorio() {
  const [contas, setContas] = useState<Conta[]>([])
  const [contaId, setContaId] = useState('')
  const [dataInicio, setDataInicio] = useState('')
  const [dataFim, setDataFim] = useState('')
  const [resumo, setResumo] = useState<Resumo | null>(null)
  const [erro, setErro] = useState('')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    listarContas().then(setContas)
  }, [])

  async function handleBuscar(e: React.FormEvent) {
    e.preventDefault()
    setErro('')
    setResumo(null)
    setLoading(true)

    try {
      const response = await api.get(`/api/transacoes/resumo/${contaId}`, {
        params: { dataInicio, dataFim },
      })
      setResumo(response.data)
    } catch {
      setErro('Erro ao buscar relatório. Verifique os dados.')
    } finally {
      setLoading(false)
    }
  }

  function formatarMoeda(valor: number) {
    return valor.toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    })
  }

  return (
    <div>
      <Navbar />
      <div style={styles.container}>
        <h2 style={styles.titulo}>Relatório por Período</h2>

        {/* Formulário */}
        <div style={styles.card}>
          <form onSubmit={handleBuscar} style={styles.form}>
            <div style={styles.field}>
              <label style={styles.label}>Conta</label>
              <select
                value={contaId}
                onChange={(e) => setContaId(e.target.value)}
                style={styles.input}
                required
              >
                <option value="">Selecione uma conta</option>
                {contas.map((c) => (
                  <option key={c.id} value={c.id}>{c.nome}</option>
                ))}
              </select>
            </div>

            <div style={styles.field}>
              <label style={styles.label}>Data Início</label>
              <input
                type="date"
                value={dataInicio}
                onChange={(e) => setDataInicio(e.target.value)}
                style={styles.input}
                required
              />
            </div>

            <div style={styles.field}>
              <label style={styles.label}>Data Fim</label>
              <input
                type="date"
                value={dataFim}
                onChange={(e) => setDataFim(e.target.value)}
                style={styles.input}
                required
              />
            </div>

            <button
              type="submit"
              style={styles.button}
              disabled={loading}
            >
              {loading ? 'Buscando...' : 'Gerar Relatório'}
            </button>
          </form>

          {erro && <p style={styles.erro}>{erro}</p>}
        </div>

        {/* Resultado */}
        {resumo && (
          <div style={styles.resultado}>
            <div style={{ ...styles.metricaCard, borderLeft: '4px solid #38a169' }}>
              <p style={styles.metricaLabel}>Total Receitas</p>
              <p style={{ ...styles.metricaValor, color: '#38a169' }}>
                {formatarMoeda(resumo.totalReceitas)}
              </p>
            </div>

            <div style={{ ...styles.metricaCard, borderLeft: '4px solid #e53e3e' }}>
              <p style={styles.metricaLabel}>Total Despesas</p>
              <p style={{ ...styles.metricaValor, color: '#e53e3e' }}>
                {formatarMoeda(resumo.totalDespesas)}
              </p>
            </div>

            <div style={{
              ...styles.metricaCard,
              borderLeft: `4px solid ${resumo.saldoPeriodo >= 0 ? '#4f46e5' : '#e53e3e'}`
            }}>
              <p style={styles.metricaLabel}>Saldo do Período</p>
              <p style={{
                ...styles.metricaValor,
                color: resumo.saldoPeriodo >= 0 ? '#4f46e5' : '#e53e3e'
              }}>
                {formatarMoeda(resumo.saldoPeriodo)}
              </p>
            </div>

            <div style={{ ...styles.metricaCard, borderLeft: '4px solid #888' }}>
              <p style={styles.metricaLabel}>Transações</p>
              <p style={{ ...styles.metricaValor, color: '#444' }}>
                {resumo.quantidadeTransacoes}
              </p>
            </div>
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
  card: {
    backgroundColor: '#fff',
    padding: '1.5rem',
    borderRadius: '12px',
    boxShadow: '0 2px 10px rgba(0,0,0,0.08)',
    marginBottom: '2rem',
  },
  form: {
    display: 'flex',
    gap: '1rem',
    flexWrap: 'wrap',
    alignItems: 'flex-end',
  },
  field: {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.4rem',
    flex: 1,
    minWidth: '180px',
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
  },
  button: {
    padding: '0.75rem 1.5rem',
    backgroundColor: '#4f46e5',
    color: '#fff',
    border: 'none',
    borderRadius: '8px',
    fontSize: '1rem',
    cursor: 'pointer',
    fontWeight: '600',
    height: 'fit-content',
  },
  resultado: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
    gap: '1rem',
  },
  metricaCard: {
    backgroundColor: '#fff',
    padding: '1.5rem',
    borderRadius: '12px',
    boxShadow: '0 2px 10px rgba(0,0,0,0.08)',
  },
  metricaLabel: {
    fontSize: '0.9rem',
    color: '#888',
    marginBottom: '0.5rem',
  },
  metricaValor: {
    fontSize: '1.8rem',
    fontWeight: '700',
  },
  erro: {
    color: '#e53e3e',
    marginTop: '0.75rem',
  },
}