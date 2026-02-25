function createPostHTML(post, authorName, authorUnique, authorPhoto) {
    const dateObj = new Date(post.date);
    const dateFormated =
        `${dateObj.toLocaleDateString("pt-BR")} às ` +
        `${dateObj.toLocaleTimeString("pt-BR", { hour: '2-digit', minute: '2-digit' })}`;

    return `
        <div class="post-card">

            <img src="${authorPhoto}" class="mini-avatar" alt="Foto de ${authorName}">

            <div class="post-body">

                <div class="post-author">
                    <strong>${authorName}</strong> 
                    <span>${authorUnique}</span>
                </div>

                <div class="post-content">
                    ${post.text}
                </div>

                <div class="post-date">
                    Postado em ${dateFormated}
                </div>

                <div class="post-actions">
                    <button 
                        class="btn-action" 
                        id="like-btn-${post.id}" 
                        onclick="likePost(${post.id})"
                    >
                        ❤️ Curtir
                    </button>
                    <span id="like-count-${post.id}">0</span>

                    <button class="btn-action" onclick="toggleComments(${post.id})">
                        💬 Comentar
                    </button>
                </div>

                <!-- Área de Comentários -->
                <div class="comments-section" id="comments-${post.id}" style="display:none;">
                    
                    <div class="comments-list" id="comments-list-${post.id}"></div>

                    <div class="comment-form">
                        <input 
                            type="text" 
                            id="comment-input-${post.id}" 
                            placeholder="Escreva um comentário..."
                        />
                        <button id="btn-comment" onclick="sendComment(${post.id})">
                            Enviar
                        </button>
                    </div>

                </div>

            </div>
        </div>
    `;
}

function logout() {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("characterId");
    window.location.href = "index.html";
}

async function loadCharacterPhoto(id) {
    
    try {
        const res = await getCharacterProfile(id);

        if (res.ok) {
            const photo = await res.json();
            const imgElement = document.getElementById(`photo-${id}`);

            if (imgElement && photo.content) {
                imgElement.src = `data:image/${photo.extension};base64,${photo.content}`;
            }
        }
    } catch (e) {
        console.error("Erro ao carregar foto:", e);
    }
}