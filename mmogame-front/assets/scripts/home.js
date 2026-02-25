async function initHome() {
    const token = sessionStorage.getItem("token");
    if (!token) {
        window.location.href = "login.html";
        return;
    }

    sessionStorage.removeItem("activeCharacterId");

    await loadUserInfo();
    await loadCharacters();
    setupModal();
    await setupGameSearch();
}

async function loadUserInfo() {
    try {
        const response = await getUserInfo();
        if (response.ok) {
            const user = await response.json();
            document.getElementById("display-user-name").innerText = user.name;
        }
    } catch (err) {
        console.error("Erro ao carregar usuário:", err);
    }
}

async function loadCharacters() {
    const grid = document.getElementById("character-grid");
    try {
        const response = await getCharacterList();

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

                card.innerHTML = `
                    <img src="assets/img/default-avatar.jpg" class="character-photo" id="photo-${c.id}">
                    <h3>${c.name}</h3>
                    <span>@${c.uniqueName}</span>
                    <p>${c.gameName}</p>
                `;
                grid.appendChild(card);

                loadCharacterPhoto(c.id);
            });
        }
    } catch (err) {
        grid.innerHTML = "<p>Erro ao carregar personagens.</p>";
    }
}

async function setupGameSearch() {
    const datalist = document.getElementById("game-options");
    const gameSearchInput = document.getElementById("game-search");
    const hiddenGameId = document.getElementById("selected-game-id");

    try {
        const response = await getGameList();

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
    } catch (err) {
        console.error("Erro ao carregar jogos:", err);
    }

    gameSearchInput.addEventListener("input", function () {
        const selectedGame = window.availableGames?.find(g => g.title === this.value);
        hiddenGameId.value = selectedGame ? selectedGame.id : "";
    });
}

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
            const res = await createCharacter(payload);

            if (res.ok) {
                const characterCriado = await res.json();

                if (photoFile && characterCriado.id) {
                    await uploadPhoto(photoFile, characterCriado.id);
                }

                alert("Personagem criado!"); //TODO: apagar alerts
                location.reload();
            } else {
                alert("Erro ao criar personagem.");
            }
        } catch (err) {
            console.error(err);
        }
    };
}

async function uploadPhoto(file, characterId) {
    try {
        await uploadCharacterPhoto(file, characterId);
    } catch (e) {
        console.error("Erro no upload", e);
    }
}
