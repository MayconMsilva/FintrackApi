import api from './api'

interface LoginRequest {
  email: string
  senha: string
}

interface LoginResponse {
  token: string
  nome: string
  email: string
}

interface CadastroRequest {
  nome: string
  email: string
  senha: string
}

export async function login(data: LoginRequest): Promise<LoginResponse> {
  const response = await api.post('/api/auth/login', data)
  return response.data
}

export async function cadastrar(data: CadastroRequest): Promise<void> {
  await api.post('/api/usuarios', data)
}