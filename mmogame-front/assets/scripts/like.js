async function likePost(postId) {
    const characterId = sessionStorage.getItem("activeCharacterId");
    const button = document.getElementById(`like-btn-${postId}`);
    const likeId = button.dataset.likeId;

    try {

        let res;

        if (likeId) {
            res = await unlikePostRequest(likeId);
        } else {
            res = await likePostRequest(postId, parseInt(characterId));
        }

        if (res.ok) {
            await loadLikes(postId);
        }

    } catch (err) {
        console.error("Erro:", err);
    }
}

async function loadLikes(postId) {
    const characterId = sessionStorage.getItem("activeCharacterId");

    try {
        const res = await getLikesByPost(postId);

        if (res.ok) {
            const likes = await res.json();

            const countElement = document.getElementById(`like-count-${postId}`);
            if (countElement) {
                countElement.innerText = likes.length;
            }

            const myLike = likes.find(l => l.characterId == characterId);

            const button = document.getElementById(`like-btn-${postId}`);

            if (myLike) {
                button.dataset.likeId = myLike.id;
                button.classList.add("liked");
            } else {
                button.dataset.likeId = "";
                button.classList.remove("liked");
            }
        }

    } catch (err) {
        console.error("Erro ao carregar likes:", err);
    }
}

