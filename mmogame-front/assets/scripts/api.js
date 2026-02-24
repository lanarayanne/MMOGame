const API_BASE = "http://localhost:8080";

const ENDPOINTS = {
    LOGIN: `${API_BASE}/auth/login`,
    REGISTER: `${API_BASE}/auth`,
    USER_INFO: `${API_BASE}/user`,
    CHANGE_PASSWORD: `${API_BASE}/user/password`,
    PERFIL: `${API_BASE}/personagem/perfil`,
    TIMELINE: `${API_BASE}/post`,
    PERSONAGEM: `${API_BASE}/personagem`,
    GAME: `${API_BASE}/api/game`,
    COMMENT: `${API_BASE}/comentario`
};

const getAuthHeader = () => ({
    "Authorization": `Bearer ${sessionStorage.getItem("token")}`,
    "Content-Type": "application/json",
    "Accept": "application/json"
});

// Requisição de login
async function loginUser(email, password) {
    return await fetch(ENDPOINTS.LOGIN, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });
}

// Requisição de registro
async function registerUser(name, email, password) {
    return await fetch(ENDPOINTS.REGISTER, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, password })
    });
}

// Busca os dados do perfil do personagem
async function getCharacterProfile(characterId) {
    return await fetch(`${ENDPOINTS.PERFIL}/${characterId}`, {
        headers: getAuthHeader()
    });
}

// Busca os posts da timeline
async function getTimelinePosts(characterId) {
    return await fetch(`${ENDPOINTS.TIMELINE}/${characterId}`, {
        headers: getAuthHeader()
    });
}

// Cria um novo post
async function createNewPost(text, characterId) {
    return await fetch(`${ENDPOINTS.TIMELINE}/novo`, {
        method: "POST",
        headers: getAuthHeader(),
        body: JSON.stringify({ text, characterId })
    });
}

// Busca personagens para a pesquisa
async function searchCharactersByGame(characterId) {
    return await fetch(`${ENDPOINTS.PERSONAGEM}/${characterId}`, {
        headers: getAuthHeader()
    });
}

// Envia a ação de curtir um post para o servidor
async function likeTimelinePost(postId, characterId) {
    return await fetch(`${ENDPOINTS.TIMELINE}/${postId}/like`, {
        method: "POST",
        headers: getAuthHeader(),
        body: JSON.stringify({ characterId })
    });
}

// --- HOME ---

// Busca os dados do usuário logado
async function getUserInfo() {
    return await fetch(ENDPOINTS.USER_INFO, {
        headers: getAuthHeader()
    });
}

// Busca a lista de personagens do usuário
async function getCharacterList() {
    return await fetch(`${ENDPOINTS.PERSONAGEM}/lista`, {
        headers: getAuthHeader()
    });
}

// Busca a lista de jogos disponíveis
async function getGameList() {
    return await fetch(`${ENDPOINTS.GAME}/list`, {
        headers: getAuthHeader()
    });
}

// Cria um novo personagem
async function createCharacter(payload) {
    return await fetch(`${ENDPOINTS.PERSONAGEM}/novo`, {
        method: "POST",
        headers: getAuthHeader(),
        body: JSON.stringify(payload)
    });
}

// Faz o upload da foto do personagem
async function uploadCharacterPhoto(file, characterId) {
    const formData = new FormData();
    formData.append("file", file);

    return await fetch(`${ENDPOINTS.PERSONAGEM}/foto/${characterId}`, {
        method: "PATCH",
        headers: {
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: formData
    });
}

// --- PERFIL  ---

// Busca a lista de seguidores
async function getFollowers(characterId) {
    return await fetch(`${ENDPOINTS.PERSONAGEM}/${characterId}/seguidores`, {
        headers: getAuthHeader()
    });
}

// Busca a lista de quem o personagem segue
async function getFollowing(characterId) {
    return await fetch(`${ENDPOINTS.PERSONAGEM}/${characterId}/seguindo`, {
        headers: getAuthHeader()
    });
}

// Busca os posts específicos do perfil do personagem
async function getMyProfilePosts(characterId) {
    return await fetch(`${ENDPOINTS.TIMELINE}/${characterId}/perfil`, {
        headers: getAuthHeader()
    });
}

// -- POST -- //TODO: arrumar a variável

function likePostRequest(postId, characterId) {
    return fetch("http://localhost:8080/like/novo", {
        method: "POST",
        headers: getAuthHeader(),
        body: JSON.stringify({
            postId,
            characterId
        })
    });
}

function getLikesByPost(postId) {
    return fetch(`http://localhost:8080/like/post/${postId}`, {
        headers: getAuthHeader()
    });
}

function getLikesByPost(postId) {
    return fetch(`http://localhost:8080/like/post/${postId}`, {
        headers: getAuthHeader()
    });
}

function unlikePostRequest(likeId) {
    return fetch(`http://localhost:8080/like/${likeId}`, {
        method: "DELETE",
        headers: getAuthHeader()
    });
}

// Buscar comentários de um post
async function getCommentsByPost(postId) {
    return await fetch(`${ENDPOINTS.COMMENT}/post/${postId}`, {
        headers: getAuthHeader()
    });
}

// Criar novo comentário
async function createComment(postId, characterId, text) {
    return await fetch(`${ENDPOINTS.COMMENT}/novo`, {
        method: "POST",
        headers: getAuthHeader(),
        body: JSON.stringify({
            postId,
            characterId,
            text
        })
    });
}
