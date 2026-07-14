import api from './api'

export interface Conta {
  id: number
  nome: string
  saldo: number
  tipoConta: string
  dataCriacao: string
}

export interface ContaRequest {
  nome: string
  tipoConta: string
}

export async function listarContas(): Promise<Conta[]> {
  const response = await api.get('/api/contas')
  return response.data
}

export async function criarConta(data: ContaRequest): Promise<Conta> {
  const response = await api.post('/api/contas', data)
  return response.data
}

export async function deletarConta(id: number): Promise<void> {
  await api.delete(`/api/contas/${id}`)
}