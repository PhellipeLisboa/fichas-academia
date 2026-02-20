import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { workoutPlanApi } from '../services/api';

function PublicWorkoutPlan() {
  const { publicCode } = useParams();
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
          <div className="animate-spin rounded-full h-14 w-14 border-4 border-gray-800 border-t-transparent mx-auto"></div>
          <p className="mt-4 text-gray-600">Carregando sua ficha...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
        <div className="text-center">
          <div className="w-20 h-20 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg className="w-10 h-10 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </div>
          <p className="text-gray-900 text-xl font-semibold mb-2">Ficha não encontrada</p>
          <p className="text-gray-500">Verifique o QR code e tente novamente</p>
        </div>
      </div>
    );
  }

  if (selectedWorkout) {
    return <WorkoutDetail workout={selectedWorkout} workoutPlan={workoutPlan} onBack={() => setSelectedWorkout(null)} />;
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-8">
      
      {/*Header*/}
<div className="bg-white border-b border-gray-200">
  <div className="max-w-2xl mx-auto px-4 py-6">

    <div className="flex items-center justify-center mb-6">
      <img 
        src="/logo.png" 
        alt="Logo Academia" 
        className="h-16 md:h-20 w-auto"
        onError={(e) => {
          e.target.style.display = 'none';
        }}
      />
    </div>

    <div className="flex items-start justify-between mb-4 gap-4">
      <div className="flex-1 min-w-0">
        <h1 className="text-2xl md:text-3xl font-bold text-gray-900">Ficha de Treino</h1>
        <p className="text-sm md:text-base text-gray-500 mt-1">
          Planilha {workoutPlan.sheetNumber.toString().padStart(2, '0')}
        </p>
      </div>
      
      <div className="text-right flex-shrink-0">
        <p className="text-xs text-gray-500 mt-2 mb-1">Reavaliação</p>
        <p className="text-sm md:text-base font-semibold text-red-600">
          {new Date(workoutPlan.reassessmentDate).toLocaleDateString('pt-BR', { day: '2-digit', month: 'short' })}
        </p>
      </div>
    </div>

    <div className="flex items-center flex-wrap gap-x-3 gap-y-2 text-sm md:text-base text-gray-600">
      <div className="flex items-center gap-2">
        <div className="w-8 h-8 md:w-9 md:h-9 bg-gray-100 rounded-full flex items-center justify-center flex-shrink-0">
          <svg className="w-4 h-4 md:w-5 md:h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
        </div>
        <span className="font-medium">Aluno: {workoutPlan.memberName}</span>
      </div>
      
      <span className="text-gray-300">•</span>
      
      <span className="font-medium">Prof. {workoutPlan.professionalName}</span>
    </div>

  </div>
</div>

      <div className="max-w-2xl mx-auto px-4">
        
        {workoutPlan.notes && (
          <div className="mt-4 bg-amber-50 rounded-xl border border-amber-200 p-4">
            <div className="flex items-start space-x-3">
              <svg className="w-5 h-5 md:w-6 md:h-6 text-amber-600 mt-0.5 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
              </svg>
              <div className="flex-1 min-w-0">
                <p className="text-xs md:text-sm font-semibold text-amber-900 uppercase tracking-wide mb-1">Observações</p>
                <p className="text-sm md:text-base text-amber-900 leading-relaxed">{workoutPlan.notes}</p>
              </div>
            </div>
          </div>
        )}

        <div className="mt-6">
          <h2 className="text-lg md:text-xl font-bold text-gray-900 mb-3 px-1">Seus Treinos</h2>
          
          <div className="space-y-3">
            {workoutPlan.workouts && workoutPlan.workouts.map((workout) => {
              const totalExercises = workout.blocks?.reduce((total, block) => 
                total + (block.items?.length || 0), 0) || 0;

              return (
                <button
                  key={workout.id}
                  onClick={() => setSelectedWorkout(workout)}
                  className="w-full bg-white rounded-xl border border-gray-200 p-4 md:p-5 hover:border-gray-300 hover:shadow-md transition-all duration-200 text-left group"
                >
                  <div className="flex items-center">
                    <div className="w-12 h-12 md:w-14 md:h-14 bg-gray-900 rounded-xl flex items-center justify-center flex-shrink-0 group-hover:bg-gray-800 transition-colors">
                      <span className="text-white text-xl md:text-2xl font-black">{workout.name}</span>
                    </div>
                    
                    <div className="flex-1 ml-4 md:ml-5 min-w-0">
                      <h3 className="font-semibold text-gray-900 text-base md:text-lg truncate">
                        Treino {workout.name}
                      </h3>
                      <p className="text-sm md:text-base text-gray-500 mt-0.5">
                        {totalExercises} exercício{totalExercises !== 1 && 's'}
                      </p>
                    </div>

                    <svg className="w-5 h-5 md:w-6 md:h-6 text-gray-400 group-hover:text-gray-600 transition-colors flex-shrink-0 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                    </svg>
                  </div>
                </button>
              );
            })}
          </div>
        </div>

      </div>
    </div>
  );
}

function WorkoutDetail({ workout, workoutPlan, onBack }) {
  const [selectedExercise, setSelectedExercise] = useState(null);

  const allExercises = workout.blocks?.flatMap(block => block.items || []) || [];

  return (
    <div className="min-h-screen bg-gray-50">
      
      <div className="bg-white border-b border-gray-200 sticky top-0 z-20">
        <div className="max-w-2xl mx-auto px-4 py-4">
          <div className="flex items-center space-x-3">
            <button
              onClick={onBack}
              className="w-9 h-9 md:w-10 md:h-10 flex items-center justify-center rounded-xl hover:bg-gray-100 transition-colors flex-shrink-0"
            >
              <svg className="w-5 h-5 md:w-6 md:h-6 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
              </svg>
            </button>
            
            <div className="flex-1 min-w-0">
              <h1 className="text-lg md:text-xl font-bold text-gray-900 truncate">Treino {workout.name}</h1>
              <p className="text-xs md:text-sm text-gray-500">
                {allExercises.length} exercício{allExercises.length !== 1 && 's'} • 
                Intervalo: {workoutPlan.restSeconds}"
              </p>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-2xl mx-auto px-4 py-4">
        <div className="space-y-3">
          {allExercises.map((item, index) => (
            <div
              key={item.id}
              className="bg-white rounded-xl border border-gray-200 p-4"
            >
              <div className="flex items-start gap-3">
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-2 flex-wrap">
                    <span className="inline-flex items-center justify-center px-2.5 py-1 bg-red-50 text-red-600 text-sm md:text-base font-bold rounded-lg flex-shrink-0">
                      {(index + 1).toString().padStart(2, '0')}
                    </span>
                    <h3 className="font-semibold text-gray-900 text-base md:text-lg break-words">
                      {item.exerciseName}
                    </h3>
                  </div>
                  
                  <div className="flex flex-wrap items-center gap-x-3 gap-y-1 ml-0.5">
                    <div className="flex items-center gap-1.5">
                      <span className="text-xs md:text-sm font-bold text-gray-900">Séries:</span>
                      <span className="text-sm md:text-base font-medium text-gray-700">{item.sets}</span>
                    </div>
                    <div className="w-1 h-1 bg-gray-300 rounded-full hidden sm:block"></div>
                    <div className="flex items-center gap-1.5">
                      <span className="text-xs md:text-sm font-bold text-gray-900">Reps:</span>
                      <span className="text-sm md:text-base font-medium text-gray-700">{item.reps}</span>
                    </div>
                    {item.machineNumber && (
                      <>
                        <div className="w-1 h-1 bg-gray-300 rounded-full hidden sm:block"></div>
                        <div className="flex items-center gap-1.5">
                          <span className="text-xs md:text-sm font-bold text-gray-900">Máquina:</span>
                          <span className="text-sm md:text-base font-medium text-gray-700">{item.machineNumber}</span>
                        </div>
                      </>
                    )}
                  </div>
                </div>

                <button 
                  onClick={() => setSelectedExercise(item)}
                  className="w-9 h-9 md:w-10 md:h-10 flex items-center justify-center rounded-xl bg-blue-50 text-blue-600 hover:bg-blue-100 hover:text-blue-700 transition-all duration-200 flex-shrink-0 shadow-sm"
                >
                  <svg className="w-5 h-5 md:w-6 md:h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {selectedExercise && (
        <ExerciseModal 
          exercise={selectedExercise} 
          onClose={() => setSelectedExercise(null)} 
        />
      )}

    </div>
  );
}

function ExerciseModal({ exercise, onClose }) {
  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center p-4">

      <div 
        className="absolute inset-0 bg-black/50 backdrop-blur-sm"
        onClick={onClose}
      ></div>
      
      <div className="relative bg-white rounded-t-2xl sm:rounded-2xl w-full sm:max-w-lg max-h-[90vh] overflow-y-auto shadow-2xl animate-slide-up">
        
        <div className="sticky top-0 bg-white border-b border-gray-200 px-4 md:px-6 py-4 md:py-5 flex items-center justify-between gap-3">
          <h3 className="font-bold text-gray-900 text-lg md:text-xl flex-1 min-w-0 break-words">{exercise.exerciseName}</h3>
          <button 
            onClick={onClose}
            className="w-9 h-9 md:w-10 md:h-10 flex items-center justify-center rounded-xl hover:bg-gray-100 transition-colors flex-shrink-0"
          >
            <svg className="w-5 h-5 md:w-6 md:h-6 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <div className="p-4 md:p-6">
          <div className="flex items-center justify-around gap-4 mb-6 p-4 md:p-5 bg-gray-50 rounded-xl">
            <div className="text-center">
              <p className="text-xs md:text-sm text-gray-500 mb-1">Séries</p>
              <p className="text-2xl md:text-3xl font-bold text-gray-900">{exercise.sets}</p>
            </div>
            <div className="w-px h-10 md:h-12 bg-gray-300"></div>
            <div className="text-center">
              <p className="text-xs md:text-sm text-gray-500 mb-1">Repetições</p>
              <p className="text-2xl md:text-3xl font-bold text-gray-900">{exercise.reps}</p>
            </div>
            {exercise.machineNumber && (
              <>
                <div className="w-px h-10 md:h-12 bg-gray-300"></div>
                <div className="text-center">
                  <p className="text-xs md:text-sm text-gray-500 mb-1">Máquina</p>
                  <p className="text-2xl md:text-3xl font-bold text-gray-900">{exercise.machineNumber}</p>
                </div>
              </>
            )}
          </div>

          <div className="bg-gray-900 rounded-xl aspect-video flex items-center justify-center mb-6">
            <div className="text-center">
              <svg className="w-16 h-16 md:w-20 md:h-20 text-gray-600 mx-auto mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <p className="text-gray-500 text-sm md:text-base">Vídeo em breve</p>
            </div>
          </div>

          <div>
            <h4 className="font-semibold text-gray-900 text-base md:text-lg mb-3">Instruções</h4>
            <p className="text-sm md:text-base text-gray-600 leading-relaxed">
              Informações detalhadas sobre a execução do exercício serão exibidas aqui.
            </p>
          </div>
        </div>

      </div>
    </div>
  );
}

export default PublicWorkoutPlan;
