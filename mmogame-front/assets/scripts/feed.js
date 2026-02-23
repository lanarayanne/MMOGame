// Centraliza a fonte do ID: sempre usar o do sessionStorage para o personagem logado
const activeCharId = sessionStorage.getItem("activeCharacterId");

async function initFeed() {
    if (!activeCharId) {
        window.location.href = "home.html";
        return;
    }
    // Carrega os dados simultaneamente para ser mais rápido
    await Promise.all([
        loadActiveCharacterSidebar(),
        loadTimeline(),
        setupSearch()
    ]);
}

// Carrega as informações na barra lateral esquerda
async function loadActiveCharacterSidebar() {
    try {
        const res = await fetch(`http://localhost:8080/personagem/perfil/${activeCharId}`, {
            headers: getAuthHeader()
        });
        if (res.ok) {
            const data = await res.json();

            // Preenche nome e @
            document.getElementById("side-char-name").innerText = data.character.name;
            document.getElementById("side-char-unique").innerText = `@${data.character.uniqueName}`;

            if (data.content) {
                const photoUrl = `data:image/${data.extension};base64,${data.content}`;
                // Atualiza a foto da sidebar e a foto pequena do criador de post
                document.getElementById("side-char-photo").src = photoUrl;

                const creatorImg = document.getElementById("creator-photo");
                if (creatorImg) creatorImg.src = photoUrl;
            }
        }
    } catch (err) { console.error("Erro sidebar:", err); }
}

// Carrega os posts (Feed Global ou de Seguidores)
async function loadTimeline() {
    const container = document.getElementById("timeline");
    try {
        const res = await fetch(`http://localhost:8080/post/${activeCharId}`, {
            headers: getAuthHeader()
        });

        if (res.ok) {
            const posts = await res.json();
            container.innerHTML = posts.length ? "" : "<p class='empty-msg'>Nenhum post para exibir. Siga alguns jogadores!</p>";

            posts.forEach(post => {
                const postEl = document.createElement("div");
                postEl.className = "post-card";
                postEl.innerHTML = `
                    <div class="post-header">
                        <strong>Personagem #${post.characterId}</strong>
                        <span class="post-date">${new Date(post.date).toLocaleDateString()}</span>
                    </div>
                    <div class="post-content">${post.text}</div>
                    <div class="post-actions">
                        <button class="btn-action" onclick="likePost(${post.id})">❤️ Curtir</button>
                    </div>
                `;
                container.appendChild(postEl);
            });
        }
    } catch (e) { console.error("Erro timeline:", e); }
}

// Função de criar post
async function createPost() {
    const textEl = document.getElementById("post-text");
    if (!textEl.value) return;

    const payload = { text: textEl.value, characterId: parseInt(activeCharId) };

    const res = await fetch("http://localhost:8080/post/novo", {
        method: "POST",
        headers: getAuthHeader(),
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        textEl.value = "";
        loadTimeline();
    }
}

// Pesquisa dinâmica
function setupSearch() {
    const input = document.getElementById("search-input");
    const results = document.getElementById("search-results");

    input.oninput = async () => {
        if (input.value.length < 2) {
            results.innerHTML = "";
            return;
        }

        // Endpoint que retorna personagens do mesmo jogo
        const res = await fetch(`http://localhost:8080/personagem/${activeCharId}`, {
            headers: getAuthHeader()
        });

        if (res.ok) {
            const list = await res.json();
            results.innerHTML = "";

            list.filter(c => c.name.toLowerCase().includes(input.value.toLowerCase()))
                .forEach(c => {
                    const item = document.createElement("div");
                    item.className = "search-item";
                    item.innerHTML = `<strong>${c.name}</strong> <span>@${c.uniqueName}</span>`;
                    item.onclick = () => window.location.href = `perfil_publico.html?id=${c.id}`;
                    results.appendChild(item);
                });
        }
    };
}

function logout() {
    sessionStorage.clear();
    window.location.href = "login.html";
}

function goToMyProfile() {
    window.location.href = `perfil_privado.html?id=${activeCharId}`;
}

// Carrega as informações na barra lateral esquerda e na navbar
async function loadActiveCharacterSidebar() {
    try {
        const res = await fetch(`http://localhost:8080/personagem/perfil/${activeCharId}`, {
            headers: getAuthHeader()
        });
        if (res.ok) {
            const data = await res.json();

            // Atualiza o nome na Navbar
            document.getElementById("display-user-name").innerText = data.character.name;

            // Preenche nome e @ na sidebar
            document.getElementById("side-char-name").innerText = data.character.name;
            document.getElementById("side-char-unique").innerText = `@${data.character.uniqueName}`;

            if (data.content) {
                const photoUrl = `data:image/${data.extension};base64,${data.content}`;
                // Atualiza a foto da sidebar e a foto pequena do criador de post
                document.getElementById("side-char-photo").src = photoUrl;

                const creatorImg = document.getElementById("creator-photo");
                if (creatorImg) creatorImg.src = photoUrl;
            }
        }
    } catch (err) { console.error("Erro sidebar:", err); }
}