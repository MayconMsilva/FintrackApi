import { useEffect, useState } from 'react'
import Navbar from '../components/Navbar'
import type { Conta, ContaRequest } from '../services/contaService'
import { listarContas, criarConta, deletarConta } from '../services/contaService'

const TIPOS_CONTA = ['CORRENTE', 'POUPANCA', 'INVESTIMENTO']

export default function Contas() {
  const [contas, setContas] = useState<Conta[]>([])
  const [loading, setLoading] = useState(true)
  const [nome, setNome] = useState('')
  const [tipoConta, setTipoConta] = useState('CORRENTE')
  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  useEffect(() => {
    carregar()
  }, [])

  async function carregar() {
    setLoading(true)
    const data = await listarContas()
    setContas(data)
    setLoading(false)
  }

  async function handleCriar(e: React.FormEvent) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    try {
      const data: ContaRequest = { nome, tipoConta }
      await criarConta(data)
      setSucesso('Conta criada com sucesso!')
      setNome('')
      setTipoConta('CORRENTE')
      carregar()
    } catch {
      setErro('Erro ao criar conta. Tente novamente.')
    }
  }

  async function handleDeletar(id: number) {
    if (!confirm('Deseja deletar essa conta?')) return

    try {
      await deletarConta(id)
      setSucesso('Conta deletada com sucesso!')
      carregar()
    } catch {
      setErro('Erro ao deletar conta.')
    }
  }

  return (
    <div>
      <Navbar />
      <div style={styles.container}>
        <h2 style={styles.titulo}>Contas</h2>

        {/* Formulário */}
        <div style={styles.card}>
          <h3 style={styles.subtitulo}>Nova Conta</h3>
          <form onSubmit={handleCriar} style={styles.form}>
            <input
              type="text"
              placeholder="Nome da conta"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              style={styles.input}
              required
            />
            <select
              value={tipoConta}
              onChange={(e) => setTipoConta(e.target.value)}
              style={styles.input}
            >
              {TIPOS_CONTA.map((tipo) => (
                <option key={tipo} value={tipo}>{tipo}</option>
              ))}
            </select>
            <button type="submit" style={styles.button}>
              Criar Conta
            </button>
          </form>

          {erro && <p style={styles.erro}>{erro}</p>}
          {sucesso && <p style={styles.sucesso}>{sucesso}</p>}
        </div>

        {/* Lista */}
        <h3 style={styles.subtitulo}>Suas Contas</h3>

        {loading ? (
          <p>Carregando...</p>
        ) : contas.length === 0 ? (
          <p style={styles.vazio}>Nenhuma conta cadastrada.</p>
        ) : (
          <div style={styles.grid}>
            {contas.map((conta) => (
              <div key={conta.id} style={styles.contaCard}>
                <div>
                  <p style={styles.contaNome}>{conta.nome}</p>
                  <p style={styles.contaTipo}>{conta.tipoConta}</p>
                  <p style={styles.contaSaldo}>
                    {conta.saldo.toLocaleString('pt-BR', {
                      style: 'currency',
                      currency: 'BRL',
                    })}
                  </p>
                </div>
                <button
                  onClick={() => handleDeletar(conta.id)}
                  style={styles.deletar}
                >
                  Deletar
                </button>
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
  subtitulo: {
    fontSize: '1.1rem',
    marginBottom: '1rem',
    color: '#444',
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
  },
  input: {
    padding: '0.75rem',
    borderRadius: '8px',
    border: '1px solid #ddd',
    fontSize: '1rem',
    flex: 1,
    minWidth: '180px',
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
  },
  grid: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))',
    gap: '1rem',
  },
  contaCard: {
    backgroundColor: '#fff',
    padding: '1.5rem',
    borderRadius: '12px',
    boxShadow: '0 2px 10px rgba(0,0,0,0.08)',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  contaNome: {
    fontWeight: '700',
    fontSize: '1.1rem',
  },
  contaTipo: {
    fontSize: '0.85rem',
    color: '#888',
    margin: '0.3rem 0',
  },
  contaSaldo: {
    fontSize: '1.3rem',
    fontWeight: '700',
    color: '#4f46e5',
  },
  deletar: {
    backgroundColor: '#fee2e2',
    color: '#e53e3e',
    border: 'none',
    padding: '0.5rem 1rem',
    borderRadius: '8px',
    cursor: 'pointer',
    fontWeight: '600',
  },
  erro: {
    color: '#e53e3e',
    marginTop: '0.75rem',
  },
  sucesso: {
    color: '#38a169',
    marginTop: '0.75rem',
  },
  vazio: {
    color: '#888',
    fontStyle: 'italic',
  },
}