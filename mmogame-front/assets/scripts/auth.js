document.addEventListener("DOMContentLoaded", () => {
    // Seletores de Login
    const loginForm = document.getElementById("form-login");

    // Seletores de Registro/Modal
    const registerForm = document.getElementById("form-register");
    const registerModal = document.getElementById("register-modal");
    const openRegisterBtn = document.getElementById("open-register");
    const closeModalBtn = document.querySelector(".close-modal");

    // --- CONTROLE DO MODAL ---
    if (openRegisterBtn) {
        openRegisterBtn.onclick = (e) => {
            e.preventDefault();
            registerModal.style.display = "flex";
        };
    }

    if (closeModalBtn) {
        closeModalBtn.onclick = () => {
            registerModal.style.display = "none";
        };
    }

    // Fecha o modal ao clicar fora dele
    window.onclick = (event) => {
        if (event.target === registerModal) {
            registerModal.style.display = "none";
        }
    };

    // --- LÓGICA DE LOGIN ---
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const payload = {
                email: loginForm.login.value, // mapeia name="login" para email
                password: loginForm.password.value
            };

            try {
                const response = await fetch(ENDPOINTS.LOGIN, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload)
                });

                if (response.ok) {
                    const token = await response.text();
                    sessionStorage.setItem("token", token);
                    window.location.href = "index.html";
                } else {
                    alert("Usuário ou senha inválidos.");
                }
            } catch (err) {
                alert("Erro ao conectar com o servidor.");
            }
        });
    }

    // --- LÓGICA DE REGISTRO ---
    if (registerForm) {
        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            // Criando o objeto conforme o seu UserDTO (name, email, password)
            const payload = {
                name: registerForm.name.value,
                email: registerForm.email.value,
                password: registerForm.password.value
            };

            try {
                const response = await fetch(ENDPOINTS.REGISTER, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload)
                });

                if (response.ok) {
                    alert("Conta criada com sucesso! Agora você já pode entrar.");
                    registerForm.reset(); // Limpa os campos
                    registerModal.style.display = "none"; // Fecha o modal
                } else {
                    // Aqui você pode tratar se o e-mail já existe
                    alert("Erro ao cadastrar. Verifique os dados ou tente outro e-mail.");
                }
            } catch (err) {
                console.error("Erro no cadastro:", err);
                alert("Não foi possível realizar o cadastro agora.");
            }
        });
    }
});
