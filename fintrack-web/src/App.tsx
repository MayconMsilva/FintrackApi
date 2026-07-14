import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import Login from './pages/Login'
import Cadastro from './pages/Cadastro'
import Dashboard from './pages/Dashboard'
import Contas from './pages/Contas'
import Transacoes from './pages/Transacoes'
import Relatorio from './pages/Relatorio'
import PrivateRoute from './components/PrivateRoute'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<Login />} />
          <Route path="/cadastro" element={<Cadastro />} />
          <Route path="/dashboard" element={
            <PrivateRoute><Dashboard /></PrivateRoute>
          } />
          <Route path="/contas" element={
            <PrivateRoute><Contas /></PrivateRoute>
          } />
          <Route path="/transacoes" element={
            <PrivateRoute><Transacoes /></PrivateRoute>
          } />
          <Route path="/relatorio" element={
            <PrivateRoute><Relatorio /></PrivateRoute>
          } />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App