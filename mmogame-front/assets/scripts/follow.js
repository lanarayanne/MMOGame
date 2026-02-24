async function initFollowPage() {

    const urlParams = new URLSearchParams(window.location.search);
    const type = urlParams.get("type");
    const profileIdFromUrl = urlParams.get("id");

    const myCharacterId = sessionStorage.getItem("activeCharacterId");

    const targetCharacterId = profileIdFromUrl || myCharacterId;

    if (!targetCharacterId || !type) {
        window.location.href = "home.html";
        return;
    }

    const title = document.getElementById("page-title");
    const container = document.getElementById("follow-list");

    let response;

    try {

        if (type === "followers") {
            title.innerText = profileIdFromUrl ? "Seguidores" : "Meus Seguidores";
            response = await getFollowers(targetCharacterId);

        } else if (type === "following") {
            title.innerText = profileIdFromUrl ? "Seguindo" : "Estou Seguindo";
            response = await getFollowing(targetCharacterId);

        } else {
            window.location.href = "home.html";
            return;
        }

        if (response.ok) {
            const list = await response.json();

            if (list.length === 0) {
                container.innerHTML = "<p>Nenhum resultado encontrado.</p>";
                return;
            }

            const html = list.map(character => {

                const photo = character.photoContent
                    ? `data:image/${character.photoExtension};base64,${character.photoContent}`
                    : "assets/img/default-avatar.png";

                return `
                    <div class="follow-card" onclick="goToProfile(${character.id})">
                        <img src="${photo}" class="follow-photo">
                        <div>
                            <h3>${character.name}</h3>
                            <span>@${character.uniqueName}</span>
                        </div>
                    </div>
                `;
            }).join("");

            container.innerHTML = html;
        }

    } catch (err) {
        console.error("Erro ao carregar lista:", err);
    }
}

function goToProfile(id) {
    window.location.href = `profile.html?id=${id}`;
}