import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

export const workoutPlanApi = {
    getByPublicCode: (publicCode) => {
        return api.get(`/workout-plans/public/${publicCode}`);
    },

    getById: (id) => {
        return api.get(`/wokout-plans/${id}`);
    },

    getAll: () => {
        return api.get(`/workout-plans`);
    }
};

export default api;