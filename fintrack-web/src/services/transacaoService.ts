import api from './api'

export interface Transacao {
  id: number
  valor: number
  tipoTransacao: string
  descricao: string
  contaId: number
  nomeConta: string
  dataTransacao: string
}

export interface TransacaoRequest {
  valor: number
  tipoTransacao: string
  descricao: string
  contaOrigemId: number
  contaDestinoId?: number
}

export async function listarTransacoes(): Promise<Transacao[]> {
  const response = await api.get('/api/transacoes')
  return response.data
}

export async function criarTransacao(data: TransacaoRequest): Promise<Transacao> {
  const response = await api.post('/api/transacoes', data)
  return response.data
}

export async function cancelarTransacao(id: number): Promise<void> {
  await api.delete(`/api/transacoes/${id}`)
}