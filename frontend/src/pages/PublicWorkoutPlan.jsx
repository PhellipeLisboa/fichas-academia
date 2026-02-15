import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { workoutPlanApi } from '../services/api';

function PublicWorkoutPlan() {
  const { publicCode } = useParams();
  const navigate = useNavigate();
  const [workoutPlan, setWorkoutPlan] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedWorkout, setSelectedWorkout] = useState(null);

  useEffect(() => {
    const fetchWorkoutPlan = async () => {
      try {
        setLoading(true);
        const response = await workoutPlanApi.getByPublicCode(publicCode);
        setWorkoutPlan(response.data);
        setError(null);
      } catch (err) {
        console.error('Erro ao buscar ficha:', err);
        setError('Ficha não encontrada');
      } finally {
        setLoading(false);
      }
    };

    fetchWorkoutPlan();
  }, [publicCode]);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-4 border-red-500 border-t-transparent mx-auto"></div>
          <p className="mt-4 text-gray-600">Carregando...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
        <div className="bg-white rounded-lg shadow-lg p-8 text-center max-w-md">
          <p className="text-red-600 text-xl font-bold mb-2">Ficha não encontrada</p>
          <p className="text-gray-600 text-sm">Verifique o código QR</p>
        </div>
      </div>
    );
  }

  if (selectedWorkout) {
    return <WorkoutDetail workout={selectedWorkout} onBack={() => setSelectedWorkout(null)} />;
  }

  return (
    <div className="min-h-screen bg-gray-50">
      
      <div className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-4xl mx-auto px-4 py-6">
          <h1 className="text-2xl font-bold text-gray-900 mb-1">Ficha de Treino Personalizado</h1>
          <p className="text-sm text-gray-500">Planilha {workoutPlan.sheetNumber.toString().padStart(2, '0')}</p>
        </div>
      </div>

      <div className="max-w-4xl mx-auto px-4 py-6">

        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-5 mb-6">
          <div className="grid grid-cols-2 gap-4 text-sm">
            <div>
              <p className="text-gray-500 mb-1">Aluno</p>
              <p className="font-semibold text-gray-900">{workoutPlan.memberName}</p>
            </div>
            <div>
              <p className="text-gray-500 mb-1">Professor</p>
              <p className="font-semibold text-gray-900">{workoutPlan.professionalName}</p>
            </div>
            <div>
              <p className="text-gray-500 mb-1">Início</p>
              <p className="font-medium text-gray-700">
                {new Date(workoutPlan.startDate).toLocaleDateString('pt-BR')}
              </p>
            </div>
            <div>
              <p className="text-gray-500 mb-1">Revisão</p>
              <p className="font-medium text-gray-700">
                {new Date(workoutPlan.reviewDate).toLocaleDateString('pt-BR')}
              </p>
            </div>
            <div>
              <p className="text-gray-500 mb-1">Reavaliação</p>
              <p className="font-medium text-red-600">
                {new Date(workoutPlan.reassessmentDate).toLocaleDateString('pt-BR')}
              </p>
            </div>
            <div>
              <p className="text-gray-500 mb-1">Carga</p>
              <p className="font-semibold text-gray-900">{workoutPlan.intensity}</p>
            </div>
          </div>
        </div>

        {workoutPlan.notes && (
          <div className="bg-amber-50 rounded-lg border border-amber-200 p-5 mb-6">
            <p className="text-xs uppercase tracking-wide text-amber-800 font-semibold mb-2">Observações</p>
            <p className="text-sm text-gray-700 leading-relaxed">{workoutPlan.notes}</p>
          </div>
        )}

        <h2 className="text-lg font-bold text-gray-900 mb-4">Rotinas de Treinos</h2>

        <div className="space-y-3">
          {workoutPlan.workouts && workoutPlan.workouts.map((workout) => {
            const totalExercises = workout.blocks?.reduce((total, block) => 
              total + (block.items?.length || 0), 0) || 0;

            return (
              <button
                key={workout.id}
                onClick={() => setSelectedWorkout(workout)}
                className="w-full bg-white rounded-lg shadow-sm border border-gray-200 p-5 hover:shadow-md hover:border-gray-300 transition-all duration-200 text-left"
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center space-x-4">

                    <div className="w-14 h-14 bg-gradient-to-br from-gray-700 to-gray-900 rounded-xl flex items-center justify-center shadow-md">
                      <span className="text-white text-2xl font-black">{workout.name}</span>
                    </div>

                    <div>
                      <h3 className="font-bold text-gray-900 text-lg mb-1">
                        Treino {workout.name}
                      </h3>
                      <p className="text-sm text-gray-500">
                        {totalExercises} {totalExercises === 1 ? 'exercício' : 'exercícios'}
                      </p>
                    </div>
                  </div>

                  <svg className="w-6 h-6 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                  </svg>
                </div>
              </button>
            );
          })}
        </div>

      </div>
    </div>
  );
}

function WorkoutDetail({ workout, onBack }) {
  return (
    <div className="min-h-screen bg-gray-50">

      <div className="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-10">
        <div className="max-w-4xl mx-auto px-4 py-4">
          <div className="flex items-center space-x-4">

            <button
              onClick={onBack}
              className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <svg className="w-6 h-6 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
              </svg>
            </button>
            
            <div>
              <h1 className="text-xl font-bold text-gray-900">Treino {workout.name}</h1>
              <p className="text-xs text-gray-500">
                {workout.blocks?.reduce((total, block) => total + (block.items?.length || 0), 0)} exercícios
              </p>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-4xl mx-auto px-4 py-6">
        <div className="space-y-3">
          {workout.blocks && workout.blocks.map((block) => (
            block.items && block.items.map((item, index) => (
              <div
                key={item.id}
                className="bg-white rounded-lg shadow-sm border border-gray-200 p-4 hover:shadow-md transition-shadow"
              >
                <div className="flex items-start justify-between">
                  
                  <div className="flex-1">
                    <h3 className="font-semibold text-gray-900 text-base mb-2">
                      {item.exerciseName}
                    </h3>
                    
                    <div className="flex items-center space-x-4 text-sm">
                      <div className="flex items-center space-x-1">
                        <span className="font-bold text-gray-900">Séries:</span>
                        <span className="text-gray-900">{item.sets}</span>
                      </div>
                      <div className="flex items-center space-x-1">
                        <span className="font-bold text-gray-900">Reps:</span>
                        <span className="text-gray-900">{item.reps}</span>
                      </div>
                    
                      {item.machineNumber && (
                        <div className="flex items-center space-x-1">
                          <span className="font-bold text-gray-900">Máquina:</span>
                        <span className="text-gray-900">{item.machineNumber}</span>
                        </div>
                      )}
                    
                    </div>
                  </div>

                  <button className="ml-4 p-2 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                  </button>
                </div>
              </div>
            ))
          ))}
        </div>
      </div>

    </div>
  );
}

export default PublicWorkoutPlan;