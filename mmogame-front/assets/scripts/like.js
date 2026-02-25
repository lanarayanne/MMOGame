async function likePost(postId) {
    const characterIdStr = sessionStorage.getItem("activeCharacterId");
    const characterId = parseInt(characterIdStr);

    if (!characterId) {
        console.error("Nenhum personagem ativo!");
        return;
    }

    try {
        // Verifica se já curtiu
        const checkRes = await fetch(`${API_BASE}/like/check/${postId}/${characterId}`, {
            headers: getAuthHeader()
        });
        const alreadyLiked = await checkRes.json();

        let res;

        if (alreadyLiked) {
            // Descurtir
            res = await fetch(`${API_BASE}/like/${characterId}/${postId}`, {
                method: "DELETE",
                headers: getAuthHeader()
            });
        } else {
            // Curtir
            res = await fetch(`${API_BASE}/like/novo`, {
                method: "POST",
                headers: getAuthHeader(),
                body: JSON.stringify({ postId, characterId })
            });
        }

        if (res.ok) {
            await loadLikes(postId);
        }

    } catch (err) {
        console.error("Erro ao curtir/descurtir:", err);
    }
}

async function loadLikes(postId) {
    const characterId = parseInt(sessionStorage.getItem("activeCharacterId"));

    try {
        const res = await fetch(`${API_BASE}/like/post/${postId}`, { headers: getAuthHeader() });
        if (!res.ok) return;

        const likes = await res.json();

        const countElement = document.getElementById(`like-count-${postId}`);
        const button = document.getElementById(`like-btn-${postId}`);

        if (countElement) countElement.innerText = likes.length;

        // Define se o usuário curtiu
        const myLike = likes.find(l => l.characterId === characterId);

        if (myLike) {
            button.classList.add("liked");
        } else {
            button.classList.remove("liked");
        }

    } catch (err) {
        console.error("Erro ao carregar likes:", err);
    }
}