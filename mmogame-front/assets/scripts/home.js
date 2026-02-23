async function initHome() {
    const token = sessionStorage.getItem("token");
    if (!token) window.location.href = "login.html";

    sessionStorage.removeItem("activeCharacterId"); 

    await loadUserInfo();
    await loadCharacters();
    setupModal();
    await setupGameSearch(); 
}

// 1. Carrega nome do usuário logado
async function loadUserInfo() {
    try {
        const response = await fetch("http://localhost:8080/user", {
            headers: getAuthHeader()
        });
        if (response.ok) {
            const user = await response.json();
            document.getElementById("display-user-name").innerText = user.name;
        }
    } catch (err) { console.error(err); }
}

// 2. Carrega lista de personagens e dispara a busca de fotos
async function loadCharacters() {
    const grid = document.getElementById("character-grid");
    try {
        const response = await fetch("http://localhost:8080/personagem/lista", {
            headers: getAuthHeader()
        });

        if (response.ok) {
            const characters = await response.json();
            grid.innerHTML = "";

            if (characters.length === 0) {
                grid.innerHTML = "<p>Você ainda não tem personagens.</p>";
                return;
            }

            characters.forEach(c => {
                const card = document.createElement("div");
                card.className = "character-card";
                card.onclick = () => {
                    sessionStorage.setItem("activeCharacterId", c.id);
                    window.location.href = `feed.html?id=${c.id}`;
                };

                // Aqui criamos o ID único para a imagem
                card.innerHTML = `
                    <img src="assets/img/default-avatar.png" class="character-photo" id="photo-${c.id}">
                    <h3>${c.name}</h3>
                    <span>${c.uniqueName}</span>
                `;
                grid.appendChild(card);
                
                // Chamamos a função que vai buscar a foto real no banco
                loadCharacterPhoto(c.id);
            });
        }
    } catch (err) { grid.innerHTML = "<p>Erro ao carregar personagens.</p>"; }
}

// 3. Busca a foto do personagem e atualiza o SRC da imagem
async function loadCharacterPhoto(id) {
    try {
        const res = await fetch(`http://localhost:8080/personagem/perfil/${id}`, {
            headers: getAuthHeader()
        });
        
        if (res.ok) {
            const photo = await res.json();
            
            // DEBUG: Abra o console (F12) e veja se os campos aparecem aqui
            console.log(`Dados da foto do personagem ${id}:`, photo);

            const imgElement = document.getElementById(`photo-${id}`);
            
            if (imgElement && photo.content) {
                // Se o seu content já vier como String Base64 do Java (padrão do Jackson para byte[]), 
                // o código abaixo funcionará.
                imgElement.src = `data:image/${photo.extension};base64,${photo.content}`;
            }
        }
    } catch (e) {
        console.error("Erro ao carregar foto:", e);
    }
}

// --- Lógica de Busca de Jogos ---
async function setupGameSearch() {
    const datalist = document.getElementById("game-options");
    const gameSearchInput = document.getElementById("game-search");
    const hiddenGameId = document.getElementById("selected-game-id");

    try {
        const response = await fetch("http://localhost:8080/api/game/list", {
            headers: getAuthHeader()
        });

        if (response.ok) {
            const games = await response.json();
            window.availableGames = games; 

            datalist.innerHTML = ""; 
            games.forEach(game => {
                const option = document.createElement("option");
                option.value = game.title; 
                datalist.appendChild(option);
            });
        }
    } catch (err) { console.error("Erro ao carregar jogos:", err); }

    gameSearchInput.addEventListener("input", function() {
        const selectedGame = window.availableGames?.find(g => g.title === this.value);
        hiddenGameId.value = selectedGame ? selectedGame.id : "";
    });
}

// 4. Lógica de Criação (Modal e Envio)
function setupModal() {
    const modal = document.getElementById("modal-create");
    const btn = document.getElementById("btn-open-create");
    const close = document.querySelector(".close-modal");
    const form = document.getElementById("form-create-character");

    btn.onclick = () => modal.style.display = "flex";
    close.onclick = () => modal.style.display = "none";

    form.onsubmit = async (e) => {
        e.preventDefault();
        
        const gameIdValue = document.getElementById("selected-game-id").value;
        const photoFile = document.getElementById("char-photo").files[0];

        if (!gameIdValue) {
            alert("Por favor, selecione um jogo válido da lista.");
            return;
        }

        const payload = {
            name: form.name.value,
            uniqueName: form.uniqueName.value,
            gameId: parseInt(gameIdValue)
        };

        try {
            const res = await fetch("http://localhost:8080/personagem/novo", {
                method: "POST",
                headers: getAuthHeader(),
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                const characterCriado = await res.json();

                if (photoFile && characterCriado.id) {
                    await uploadPhoto(photoFile, characterCriado.id);
                }

                alert("Personagem criado!");
                location.reload();
            } else {
                alert("Erro ao criar personagem.");
            }
        } catch (err) { console.error(err); }
    };
}

async function uploadPhoto(file, characterId) {
    const formData = new FormData();
    formData.append("file", file);

    try {
        await fetch(`http://localhost:8080/personagem/foto/${characterId}`, {
            method: "PATCH",
            headers: {
                "Authorization": `Bearer ${sessionStorage.getItem("token")}`
            },
            body: formData
        });
    } catch (e) { console.error("Erro no upload", e); }
}

function logout() {
    sessionStorage.removeItem("token");
    window.location.href = "login.html";
}