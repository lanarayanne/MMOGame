document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("form-login");
    const registerForm = document.getElementById("form-register");
    const registerModal = document.getElementById("register-modal");
    const openRegisterBtn = document.getElementById("open-register");
    const closeModalBtn = document.querySelector(".close-modal");

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

    window.onclick = (event) => {
        if (event.target === registerModal) {
            registerModal.style.display = "none";
        }
    };

    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const email = loginForm.login.value; 
            const password = loginForm.password.value;

            try {
                const response = await loginUser(email, password);

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

    if (registerForm) {
        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const name = registerForm.name.value;
            const email = registerForm.email.value;
            const password = registerForm.password.value;

            try {
                const response = await registerUser(name, email, password);

                if (response.ok) {
                    alert("Conta criada com sucesso! Agora você já pode entrar.");
                    registerForm.reset(); 
                    registerModal.style.display = "none"; 
                } else {
                    alert("Erro ao cadastrar. Verifique os dados ou tente outro e-mail.");
                }
            } catch (err) {
                console.error("Erro no cadastro:", err);
                alert("Não foi possível realizar o cadastro agora.");
            }
        });
    }
});