let isLoginMode = true;
const authForm = document.getElementById('authForm');
const formTitle = document.getElementById('formTitle');
const roleSelection = document.getElementById('roleSelection');
const submitBtn = document.getElementById('submitBtn');
const toggleAuthMode = document.getElementById('toggleAuthMode');
const authAlert = document.getElementById('authAlert');

toggleAuthMode.addEventListener('click', (e) => {
    e.preventDefault();
    isLoginMode = !isLoginMode;
    formTitle.innerText = isLoginMode ? "Authentication" : "New account registration";
    submitBtn.innerText = isLoginMode ? "Conectare" : "Creează Cont";
    toggleAuthMode.innerText = isLoginMode ? "Don't have an account? Register" : "Already have an account? Log in";
    roleSelection.classList.toggle('hidden', isLoginMode);
    authAlert.classList.add('hidden');
});

authForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    authAlert.classList.add('hidden');

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const role = document.getElementById('role').value;

    const url = isLoginMode ? '/api/auth/login' : '/api/auth/register';
    const payload = isLoginMode ? {email, password} : {email, password, role};

    try {
        const response = await axios.post(url, payload);
        if (isLoginMode) {
            localStorage.setItem('role', response.data.role);
            localStorage.setItem('email', response.data.email);
            window.location.href = './dashboard.html';
        } else {
            alert('Account created! Please login.');
            toggleAuthMode.click();
        }
    } catch (error) {
        authAlert.classList.remove('hidden');
        authAlert.innerText = error.response?.data?.message || "Authentication failed. Please try again.";
    }
});