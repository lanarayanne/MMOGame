const activeCharId = sessionStorage.getItem("activeCharacterId");

async function initFeed() {
    if (!activeCharId) {
        window.location.href = "home.html";
        return;
    }

    await Promise.all([
        loadActiveCharacterSidebar(),
        loadTimeline(),
        setupSearch()
    ]);
}

async function loadActiveCharacterSidebar() {
    try {
        const res = await getCharacterProfile(activeCharId);

        if (res.ok) {
            const data = await res.json();

            document.getElementById("side-char-name").innerText = data.character.name;
            document.getElementById("side-char-unique").innerText = `@${data.character.uniqueName}`;

            if (data.content) {
                const photoUrl = `data:image/${data.extension};base64,${data.content}`;
                document.getElementById("side-char-photo").src = photoUrl;

                const creatorImg = document.getElementById("creator-photo");
                if (creatorImg) creatorImg.src = photoUrl;
            }
        }
    } catch (err) {
        console.error("Erro sidebar:", err);
    }
}

async function loadTimeline() {
    const container = document.getElementById("timeline");
    try {
        const res = await getTimelinePosts(activeCharId);

        if (res.ok) {
            const posts = await res.json();

            if (posts.length === 0) {
                container.innerHTML = "<p class='empty-msg'>Nenhum post para exibir. Siga alguns jogadores!</p>";
                return;
            }

            const postsHTML = posts.map(post => {

                const authorName = post.name;
                const authorUnique = `@${post.uniqueName}`;

                const authorPhoto = post.photoContent
                    ? `data:image/${post.photoExtension};base64,${post.photoContent}`
                    : "assets/img/default-avatar.jpg";

                loadLikes(post.id)

                return createPostHTML(post, authorName, authorUnique, authorPhoto);
            }).join("");

            container.innerHTML = postsHTML;
        }
    } catch (e) {
        console.error("Erro timeline:", e);
    }
}

async function createPost() {
    const textEl = document.getElementById("post-text");
    if (!textEl.value) return;

    try {
        const res = await createNewPost(textEl.value, parseInt(activeCharId));

        if (res.ok) {
            textEl.value = "";
            loadTimeline();
        } else {
            alert("Erro ao criar post.");
        }
    } catch (err) {
        console.error("Erro ao criar post:", err);
    }
}

function setupSearch() {
    const input = document.getElementById("search-input");
    const results = document.getElementById("search-results");

    input.oninput = async () => {
        if (input.value.length < 2) {
            results.innerHTML = "";
            return;
        }

        try {
            const res = await searchCharactersByGame(activeCharId);

            if (res.ok) {
                const list = await res.json();
                results.innerHTML = "";

                const filteredList = list.filter(c =>
                    c.name.toLowerCase().includes(input.value.toLowerCase())
                );

                filteredList.forEach(c => {
                    const item = document.createElement("div");
                    item.className = "search-item";
                    item.innerHTML = `<strong>${c.name}</strong> <span>@${c.uniqueName}</span>`;
                    item.onclick = () => window.location.href = `profile.html?id=${c.id}`;
                    results.appendChild(item);
                });
            }
        } catch (err) {
            console.error("Erro na busca:", err);
        }
    };
}


function goToMyProfile() {
    window.location.href = `profile.html?id=${activeCharId}`;
}
