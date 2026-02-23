const urlParams = new URLSearchParams(window.location.search);
const targetCharacterId = urlParams.get('id'); // O personagem que estou visitando
const myCharacterId = sessionStorage.getItem("activeCharacterId"); // Você deve salvar isso ao entrar no feed

async function initPublicProfile() {
    if (!targetCharacterId) {
        window.location.href = 'home.html';
        return;
    }

    await loadTargetData();
    await loadTargetPosts();
    await checkFollowStatus();
}

// 1. Carrega dados básicos e foto (@GetMapping("/personagem/perfil/{id}"))
async function loadTargetData() {
    const res = await fetch(`http://localhost:8080/personagem/perfil/${targetCharacterId}`, {
        headers: getAuthHeader()
    });
    
    if (res.ok) {
        const data = await res.json();
        document.getElementById("target-name").innerText = data.character.name;
        document.getElementById("target-unique").innerText = `@${data.character.uniqueName}`;
        
        if (data.content) {
            document.getElementById("target-photo").src = `data:image/${data.extension};base64,${data.content}`;
        }

        // Carregar contadores (@GetMapping("/{id}/seguidores" e "/seguindo"))
        updateCounters();
    }
}

async function updateCounters() {
    const resFollowers = await fetch(`http://localhost:8080/personagem/${targetCharacterId}/seguidores`, { headers: getAuthHeader() });
    const resFollowing = await fetch(`http://localhost:8080/personagem/${targetCharacterId}/seguindo`, { headers: getAuthHeader() });

    if (resFollowers.ok) {
        const followers = await resFollowers.json();
        document.getElementById("count-seguidores").innerText = followers.length;
    }
}

// 2. Carrega posts específicos deste personagem (@GetMapping("/post/{id}/perfil"))
async function loadTargetPosts() {
    const container = document.getElementById("target-posts");
    const res = await fetch(`http://localhost:8080/post/${targetCharacterId}/perfil`, {
        headers: getAuthHeader()
    });

    if (res.ok) {
        const posts = await res.json();
        container.innerHTML = posts.length ? "" : "<p>Este personagem ainda não postou nada.</p>";

        posts.forEach(post => {
            container.innerHTML += `
                <div class="post-card">
                    <div class="post-content">${post.text}</div>
                    <div class="post-actions">
                        <button class="btn-action">❤️ Curtir</button>
                    </div>
                </div>
            `;
        });
    }
}

// 3. Lógica de Seguir (@PostMapping("/personagem/seguir"))
document.getElementById("btn-follow").onclick = async function() {
    const payload = {
        followerId: parseInt(myCharacterId),
        followingId: parseInt(targetCharacterId)
    };

    const res = await fetch("http://localhost:8080/personagem/seguir", {
        method: "POST",
        headers: getAuthHeader(),
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        alert("Agora você segue este personagem!");
        location.reload();
    }
};

// DICA: Para saber se você já segue, você teria que varrer a lista de seguidores do alvo 
// comparando com o seu myCharacterId.