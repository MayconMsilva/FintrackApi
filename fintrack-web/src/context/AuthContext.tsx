import { createContext, useContext, useState, ReactNode } from 'react'

interface AuthContextData {
  token: string | null
  nome: string | null
  login: (token: string, nome: string) => void
  logout: () => void
  isAuthenticated: boolean
}

const AuthContext = createContext<AuthContextData>({} as AuthContextData)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(
    localStorage.getItem('token')
  )
  const [nome, setNome] = useState<string | null>(
    localStorage.getItem('nome')
  )

  function login(token: string, nome: string) {
    localStorage.setItem('token', token)
    localStorage.setItem('nome', nome)
    setToken(token)
    setNome(nome)
  }

  function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('nome')
    setToken(null)
    setNome(null)
  }

  return (
    <AuthContext.Provider value={{
      token,
      nome,
      login,
      logout,
      isAuthenticated: !!token
    }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}