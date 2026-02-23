async function loadUserData() {
    const response = await fetch(ENDPOINTS.USER_INFO, {
        method: "GET",
        headers: getAuthHeader()
    });

    if (response.ok) {
        const user = await response.json();
        // No seu backend UserDTO tem 'name' e 'email'
        document.getElementById("userName").innerText = user.name;
    }
}

async function updatePassword(oldPassword, newPassword) {
    const response = await fetch(ENDPOINTS.CHANGE_PASSWORD, {
        method: "PATCH",
        headers: getAuthHeader(),
        body: JSON.stringify({ oldPassword, newPassword })
    });

    if (response.ok) {
        alert("Senha alterada!");
    }
}