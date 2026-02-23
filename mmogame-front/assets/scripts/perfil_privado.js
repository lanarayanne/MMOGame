const myCharacterId = sessionStorage.getItem("activeCharacterId");

async function initPrivateProfile() {
    if (!myCharacterId) {
        window.location.href = 'home.html';
        return;
    }

    await loadMyData();
    await loadMyPosts();
    await loadMyStats();
}

// 1. Carrega dados do personagem e foto (@GetMapping("/personagem/perfil/{id}"))
async function loadMyData() {
    try {
        const res = await fetch(`http://localhost:8080/personagem/perfil/${myCharacterId}`, {
            headers: getAuthHeader()
        });
        
        if (res.ok) {
            const data = await res.json();
            document.getElementById("my-name").innerText = data.character.name;
            document.getElementById("my-unique").innerText = `@${data.character.uniqueName}`;
            
            if (data.content) {
                document.getElementById("my-photo").src = `data:image/${data.extension};base64,${data.content}`;
            }
        }
    } catch (err) { console.error("Erro ao carregar dados:", err); }
}

// 2. Carrega estatísticas de follow
async function loadMyStats() {
    try {
        const resFollowers = await fetch(`http://localhost:8080/personagem/${myCharacterId}/seguidores`, { headers: getAuthHeader() });
        const resFollowing = await fetch(`http://localhost:8080/personagem/${myCharacterId}/seguindo`, { headers: getAuthHeader() });

        if (resFollowers.ok) {
            const followers = await resFollowers.json();
            document.getElementById("count-seguidores").innerText = followers.length;
        }
        if (resFollowing.ok) {
            const following = await resFollowing.json();
            document.getElementById("count-seguindo").innerText = following.length;
        }
    } catch (err) { console.error(err); }
}

// 3. Cria um novo post (@PostMapping("/post/novo"))
async function createMyPost() {
    const textArea = document.getElementById("post-text");
    const text = textArea.value;
    if (!text) return;

    const payload = {
        text: text,
        characterId: parseInt(myCharacterId)
    };

    const res = await fetch("http://localhost:8080/post/novo", {
        method: "POST",
        headers: getAuthHeader(),
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        textArea.value = "";
        alert("Postado com sucesso!");
        loadMyPosts(); // Atualiza a lista
    }
}


// 4. Carrega apenas os MEUS posts (@GetMapping("/post/{id}/perfil"))
// 4. Carrega apenas os MEUS posts (@GetMapping("/post/{id}/perfil"))
// 4. Carrega apenas os MEUS posts
async function loadMyPosts() {
    const container = document.getElementById("my-posts");
    try {
        const res = await fetch(`http://localhost:8080/post/${myCharacterId}/perfil`, {
            headers: getAuthHeader()
        });

        const photoUrl = document.getElementById("my-photo").src;
        const myName = document.getElementById("my-name").innerText;
        const myUnique = document.getElementById("my-unique").innerText;

        if (res.ok) {
            const posts = await res.json();
            container.innerHTML = posts.length ? "" : "<p>Você ainda não fez nenhuma publicação.</p>";

            posts.forEach(post => {
                const dateObj = new Date(post.date);
                const dateFormated = `${dateObj.toLocaleDateString("pt-BR")} às ${dateObj.toLocaleTimeString("pt-BR", {hour: '2-digit', minute:'2-digit'})}`;
                
                container.innerHTML += `
                    <div class="post-card">
                        
                        <img src="${photoUrl}" class="mini-avatar" alt="Foto de ${myName}">
                        
                        <div class="post-body">
                            
                            <div class="post-author">
                                <strong>${myName}</strong> 
                                <span>${myUnique}</span>
                            </div>
                            
                            <div class="post-content">
                                ${post.text}
                            </div>
                            
                            <div class="post-date">
                                Postado em ${dateFormated}
                            </div>
                            
                            <div class="post-actions">
                                <button class="btn-action">❤️ Curtir</button>
                                <button class="btn-action">💬 Comentar</button>
                            </div>
                            
                        </div>
                    </div>
                `;
            });
        }
    } catch (err) { console.error("Erro ao carregar meus posts:", err); }
}

function logout() {
    sessionStorage.clear();
    window.location.href = "login.html";
}