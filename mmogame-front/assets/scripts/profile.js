const urlParams = new URLSearchParams(window.location.search);
const targetIdFromUrl = urlParams.get("id");
const myCharacterId = sessionStorage.getItem("activeCharacterId");

let profileId;
let isMyProfile;

async function initProfile() {

    if (!myCharacterId) {
        window.location.href = "home.html";
        return;
    }

    if (!targetIdFromUrl) {
        profileId = myCharacterId;
        isMyProfile = true;
    } else {
        profileId = targetIdFromUrl;
        isMyProfile = (profileId === myCharacterId);
    }

    await loadProfileData();
    await Promise.all([
        loadProfilePosts(),
        loadFollowStats()
    ]);

    setupProfileMode();
}

async function loadProfileData() {
    try {
        const res = await getCharacterProfile(profileId);

        if (res.ok) {
            const data = await res.json();

            document.getElementById("profile-name").innerText = data.character.name;
            document.getElementById("profile-unique").innerText =
                `@${data.character.uniqueName}`;

            if (data.content) {
                document.getElementById("profile-photo").src =
                    `data:image/${data.extension};base64,${data.content}`;
            }
        }
    } catch (err) {
        console.error(err);
    }
}

async function loadFollowStats() {
    try {
        const resFollowers = await getFollowers(profileId);
        const resFollowing = await getFollowing(profileId);

        if (resFollowers.ok) {
            const followers = await resFollowers.json();
            document.getElementById("count-seguidores").innerText =
                followers.length;

            // Só verifica follow se NÃO for meu perfil
            if (!isMyProfile) {
                const isFollowing = followers.some(f =>
                    f.id === parseInt(myCharacterId)
                );

                const btnFollow = document.getElementById("profile-action-btn");

                if (isFollowing) {
                    btnFollow.innerText = "Já Seguindo";
                    btnFollow.disabled = true;
                } else {
                    btnFollow.innerText = "Seguir";
                    btnFollow.disabled = false;
                }
            }
        }

        if (resFollowing.ok) {
            const following = await resFollowing.json();
            document.getElementById("count-seguindo").innerText =
                following.length;
        }

    } catch (err) {
        console.error(err);
    }
}

async function loadProfilePosts() {

    const container = document.getElementById("profile-posts");

    try {
        const res = await getMyProfilePosts(profileId);

        if (res.ok) {
            const posts = await res.json();

            if (posts.length === 0) {
                container.innerHTML = "<p>Nenhuma publicação ainda.</p>";
                return;
            }

            const postsHTML = posts.map(post => {

                const authorPhoto = post.photoContent
                    ? `data:image/${post.photoExtension};base64,${post.photoContent}`
                    : "assets/img/default-avatar.png";

                return createPostHTML(
                    post,
                    post.name,
                    `@${post.uniqueName}`,
                    authorPhoto
                );

            }).join("");

            container.innerHTML = postsHTML;

            posts.forEach(post => loadLikes(post.id));
        }

    } catch (err) {
        console.error(err);
    }
}

function setupProfileMode() {

    const btnFollow = document.getElementById("profile-action-btn");
    const createPostBox = document.getElementById("create-post-box");

    if (isMyProfile) {
        if (btnFollow) btnFollow.style.display = "none";

        if (createPostBox) createPostBox.style.display = "block";
    } else {
        if (createPostBox) createPostBox.style.display = "none";

        if (btnFollow) btnFollow.style.display = "inline-block";
    }
}

document.getElementById("profile-action-btn")?.addEventListener("click", async () => {

    if (isMyProfile) return;

    const res = await followCharacter(
        parseInt(myCharacterId),
        parseInt(profileId)
    );

    if (res.ok) {
        loadFollowStats();
    }
});

document.getElementById("followers-link")?.addEventListener("click", () => {
    window.location.href = `follow.html?type=followers&id=${profileId}`;
});

document.getElementById("following-link")?.addEventListener("click", () => {
    window.location.href = `follow.html?type=following&id=${profileId}`;
});

async function createPost() {

    const textarea = document.getElementById("post-text");
    const text = textarea.value.trim();

    if (!text) {
        alert("Digite algo para postar.");
        return;
    }

    const res = await createNewPost(
        text,
        parseInt(myCharacterId)
    );

    if (res.ok) {
        textarea.value = "";
        await loadProfilePosts();
    }
}

window.createPost = createPost;