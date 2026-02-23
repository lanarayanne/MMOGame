const API_BASE = "http://localhost:8080";

const ENDPOINTS = {
    LOGIN: `${API_BASE}/auth/login`,
    REGISTER: `${API_BASE}/auth`,
    USER_INFO: `${API_BASE}/user`,
    CHANGE_PASSWORD: `${API_BASE}/user/password`
};

// Função utilitária para pegar o token fácil
const getAuthHeader = () => ({
    "Authorization": `Bearer ${sessionStorage.getItem("token")}`,
    "Content-Type": "application/json",
    "Accept": "application/json"
});