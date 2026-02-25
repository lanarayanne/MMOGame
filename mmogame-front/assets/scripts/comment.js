async function toggleComments(postId) {
    const section = document.getElementById(`comments-${postId}`);

    if (section.style.display === "none") {
        section.style.display = "block";
        await loadComments(postId);
    } else {
        section.style.display = "none";
    }
}

window.toggleComments = toggleComments;

async function loadComments(postId) {

    const container = document.getElementById(`comments-list-${postId}`);

    const res = await getCommentsByPost(postId);

    if (res.ok) {
        const comments = await res.json();

        if (comments.length === 0) {
            container.innerHTML = "<p>Sem comentários ainda.</p>";
            return;
        }

        container.innerHTML = comments.map(c => {

            const dateObj = new Date(c.date);
            const formatted =
                `${dateObj.toLocaleDateString("pt-BR")} ` +
                `${dateObj.toLocaleTimeString("pt-BR", { hour: '2-digit', minute: '2-digit' })}`;

            const authorPhoto = c.photoContent
                ? `data:image/${c.photoExtension};base64,${c.photoContent}`
                : "assets/img/default-avatar.jpg";

            return `
                <div class="comment-item">
                    <img src="${authorPhoto}" class="mini-avatar" alt="Foto de ${c.name}">
                    <div class="post-body">

                        <div class="post-author">
                            <strong>${c.name}</strong> 
                            <span>@${c.uniqueName}</span>
                        </div>

                        <div class="post-content">
                            ${c.text}
                        </div>

                        <div class="post-date">
                            Postado em ${formatted}
                        </div>
                    </div>
                </div>
            `;

        }).join("");
    }
}


async function sendComment(postId) {

    const input = document.getElementById(`comment-input-${postId}`);
    const text = input.value.trim();

    if (!text) {
        alert("Digite um comentário.");
        return;
    }

    const characterId = sessionStorage.getItem("activeCharacterId");

    const res = await createComment(
        postId,
        parseInt(characterId),
        text
    );

    if (res.ok) {
        input.value = "";
        await loadComments(postId);
    }
}

window.sendComment = sendComment;

