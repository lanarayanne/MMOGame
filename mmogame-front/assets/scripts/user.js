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