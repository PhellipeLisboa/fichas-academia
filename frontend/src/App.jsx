import { BrowserRouter, Routes, Route } from 'react-router-dom';
import PublicWorkoutPlan from './pages/PublicWorkoutPlan';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/public/:publicCode" element={<PublicWorkoutPlan />} />
        
        <Route path="/" element={
          <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-500 to-purple-600">
            <div className="text-center text-white">
              <h1 className="text-5xl font-bold mb-4">
                Workout Planner
              </h1>
              <p className="text-xl">
                Sistema de Gerenciamento de Fichas de Treino
              </p>
              <p className="mt-4 text-sm opacity-75">
                Escaneie o QR Code da sua ficha para visualizar
              </p>
            </div>
          </div>
        } />
      </Routes>
    </BrowserRouter>
  );
}

export default App;