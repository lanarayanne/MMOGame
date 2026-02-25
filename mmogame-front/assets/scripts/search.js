function initSearch() {
    loadGames();
    loadInitialCharacters();
    setupForm();
}


async function loadInitialCharacters() {
    try {
        const characterId = sessionStorage.getItem("activeCharacterId");

        if (!characterId) {
            console.warn("CharacterId não encontrado no sessionStorage.");
            return;
        }

        const response = await searchCharactersByGame(characterId);

        if (!response.ok) {
            throw new Error("Erro ao buscar personagens iniciais");
        }

        const data = await response.json();
        renderCharacters(data);

    } catch (error) {
        console.error(error);
    }
}


async function loadGames() {
    try {
        const response = await getGameList();

        if (!response.ok) {
            throw new Error("Erro ao buscar jogos");
        }

        const games = await response.json();
        cachedGames = games;

        const datalist = document.getElementById("game-options");
        datalist.innerHTML = "";

        games.forEach(game => {
            const option = document.createElement("option");
            option.value = game.title;
            datalist.appendChild(option);
        });

    } catch (error) {
        console.error(error);
    }
}

// function setupGameSelection(games) {
//     const input = document.getElementById("game-search");
//     const hiddenInput = document.getElementById("selected-game-id");

//     input.addEventListener("input", function () {
//         const selected = games.find(g => g.name === input.value);

//         hiddenInput.value = selected ? selected.id : "";
//     });
// }


let cachedGames = [];

function setupForm() {
    const form = document.getElementById("form-search-game");

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        const inputValue = document.getElementById("game-search").value.trim();

        const selectedGame = cachedGames.find(g =>
            g.title.toLowerCase() === inputValue.toLowerCase()
        );

        if (!selectedGame) {
            alert("Selecione um jogo válido da lista.");
            return;
        }

        await searchByGameId(selectedGame.id);
    });
}

async function searchByGameId(gameId) {
    try {
        const response = await fetch(`${ENDPOINTS.PERSONAGEM}/jogo/${gameId}`, {
            headers: getAuthHeader()
        });

        if (!response.ok) {
            throw new Error("Erro ao buscar personagens por jogo");
        }

        const data = await response.json();
        renderCharacters(data);

    } catch (error) {
        console.error(error);
    }
}


function renderCharacters(characters) {
    const grid = document.getElementById("character-grid");
    grid.innerHTML = "";

    if (!characters || characters.length === 0) {
        grid.innerHTML = "<p>Nenhum personagem encontrado.</p>";
        return;
    }

    characters.forEach(c => {
        const card = document.createElement("div");
        card.className = "character-card";

        card.onclick = () => {
            window.location.href = `profile.html?id=${c.id}`;
        };

        card.innerHTML = `
            <img src="assets/img/default-avatar.jpg" 
                class="character-photo" 
                id="photo-${c.id}">
            <h3>${c.name}</h3>
            <span>@${c.uniqueName}</span>
            <p>${c.gameName || c.game?.name || c.game?.title || ""}</p>
        `;

        grid.appendChild(card);

        loadCharacterPhoto(c.id);
    });
}

