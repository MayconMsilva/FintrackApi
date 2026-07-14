import { useEffect, useState } from 'react'
import Navbar from '../components/Navbar'
import type { Transacao, TransacaoRequest } from '../services/transacaoService'
import { listarTransacoes, criarTransacao, cancelarTransacao } from '../services/transacaoService'
import { listarContas } from '../services/contaService'
import type { Conta } from '../services/contaService'

const TIPOS = ['RECEITA', 'DESPESA', 'TRANSFERENCIA']

export default function Transacoes() {
  const [transacoes, setTransacoes] = useState<Transacao[]>([])
  const [contas, setContas] = useState<Conta[]>([])
  const [loading, setLoading] = useState(true)
  const [valor, setValor] = useState('')
  const [tipo, setTipo] = useState('RECEITA')
  const [descricao, setDescricao] = useState('')
  const [contaOrigemId, setContaOrigemId] = useState('')
  const [contaDestinoId, setContaDestinoId] = useState('')
  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  useEffect(() => {
    carregar()
  }, [])

  async function carregar() {
    setLoading(true)
    const [t, c] = await Promise.all([listarTransacoes(), listarContas()])
    setTransacoes(t)
    setContas(c)
    setLoading(false)
  }

  async function handleCriar(e: React.FormEvent) {
    e.preventDefault()
    setErro('')
    setSucesso('')

    try {
      const data: TransacaoRequest = {
        valor: parseFloat(valor),
        tipoTransacao: tipo,
        descricao,
        contaOrigemId: parseInt(contaOrigemId),
        ...(tipo === 'TRANSFERENCIA' && { contaDestinoId: parseInt(contaDestinoId) }),
      }

      await criarTransacao(data)
      setSucesso('Transação criada com sucesso!')
      setValor('')
      setDescricao('')
      carregar()
    } catch (err: any) {
      setErro(err.response?.data?.mensagem || 'Erro ao criar transação.')
    }
  }

  async function handleCancelar(id: number) {
    if (!confirm('Deseja cancelar essa transação?')) return

    try {
      await cancelarTransacao(id)
      setSucesso('Transação cancelada!')
      carregar()
    } catch {
      setErro('Erro ao cancelar transação.')
    }
  }

  function corTipo(tipo: string) {
    if (tipo === 'RECEITA') return '#38a169'
    if (tipo === 'DESPESA') return '#e53e3e'
    return '#4f46e5'
  }

  return (
    <div>
      <Navbar />
      <div style={styles.container}>
        <h2 style={styles.titulo}>Transações</h2>

        {/* Formulário */}
        <div style={styles.card}>
          <h3 style={styles.subtitulo}>Nova Transação</h3>
          <form onSubmit={handleCriar} style={styles.form}>

            <select
              value={tipo}
              onChange={(e) => setTipo(e.target.value)}
              style={styles.input}
            >
              {TIPOS.map((t) => (
                <option key={t} value={t}>{t}</option>
              ))}
            </select>

            <input
              type="number"
              placeholder="Valor"
              value={valor}
              onChange={(e) => setValor(e.target.value)}
              style={styles.input}
              step="0.01"
              min="0.01"
              required
            />

            <input
              type="text"
              placeholder="Descrição"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              style={styles.input}
            />

            <select
              value={contaOrigemId}
              onChange={(e) => setContaOrigemId(e.target.value)}
              style={styles.input}
              required
            >
              <option value="">Conta origem</option>
              {contas.map((c) => (
                <option key={c.id} value={c.id}>{c.nome}</option>
              ))}
            </select>

            {tipo === 'TRANSFERENCIA' && (
              <select
                value={contaDestinoId}
                onChange={(e) => setContaDestinoId(e.target.value)}
                style={styles.input}
                required
              >
                <option value="">Conta destino</option>
                {contas.map((c) => (
                  <option key={c.id} value={c.id}>{c.nome}</option>
                ))}
              </select>
            )}

            <button type="submit" style={styles.button}>
              Criar Transação
            </button>
          </form>

          {erro && <p style={styles.erro}>{erro}</p>}
          {sucesso && <p style={styles.sucesso}>{sucesso}</p>}
        </div>

        {/* Lista */}
        <h3 style={styles.subtitulo}>Histórico</h3>

        {loading ? (
          <p>Carregando...</p>
        ) : transacoes.length === 0 ? (
          <p style={styles.vazio}>Nenhuma transação registrada.</p>
        ) : (
          <div style={styles.lista}>
            {transacoes.map((t) => (
              <div key={t.id} style={styles.transacaoCard}>
                <div style={styles.transacaoInfo}>
                  <span
                    style={{
                      ...styles.badge,
                      backgroundColor: corTipo(t.tipoTransacao) + '20',
                      color: corTipo(t.tipoTransacao),
                    }}
                  >
                    {t.tipoTransacao}
                  </span>
                  <p style={styles.transacaoDesc}>
                    {t.descricao || 'Sem descrição'}
                  </p>
                  <p style={styles.transacaoConta}>{t.nomeConta}</p>
                  <p style={styles.transacaoData}>
                    {new Date(t.dataTransacao).toLocaleDateString('pt-BR')}
                  </p>
                </div>

                <div style={styles.transacaoDireita}>
                  <p style={{
                    ...styles.transacaoValor,
                    color: corTipo(t.tipoTransacao),
                  }}>
                    {t.tipoTransacao === 'DESPESA' ? '- ' : '+ '}
                    {t.valor.toLocaleString('pt-BR', {
                      style: 'currency',
                      currency: 'BRL',
                    })}
                  </p>
                  <button
                    onClick={() => handleCancelar(t.id)}
                    style={styles.cancelar}
                  >
                    Cancelar
                  </button>
                </div>
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
    minWidth: '160px',
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
  lista: {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.75rem',
  },
  transacaoCard: {
    backgroundColor: '#fff',
    padding: '1.25rem 1.5rem',
    borderRadius: '12px',
    boxShadow: '0 2px 10px rgba(0,0,0,0.06)',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  transacaoInfo: {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.25rem',
  },
  badge: {
    display: 'inline-block',
    padding: '0.2rem 0.6rem',
    borderRadius: '20px',
    fontSize: '0.75rem',
    fontWeight: '700',
    width: 'fit-content',
    marginBottom: '0.25rem',
  },
  transacaoDesc: {
    fontWeight: '600',
    fontSize: '1rem',
  },
  transacaoConta: {
    fontSize: '0.85rem',
    color: '#888',
  },
  transacaoData: {
    fontSize: '0.8rem',
    color: '#aaa',
  },
  transacaoDireita: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'flex-end',
    gap: '0.5rem',
  },
  transacaoValor: {
    fontSize: '1.3rem',
    fontWeight: '700',
  },
  cancelar: {
    backgroundColor: '#fee2e2',
    color: '#e53e3e',
    border: 'none',
    padding: '0.4rem 0.8rem',
    borderRadius: '8px',
    cursor: 'pointer',
    fontSize: '0.85rem',
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