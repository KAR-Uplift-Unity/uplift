document.addEventListener('DOMContentLoaded', function() {
    const username = document.getElementById('username');
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const passwordMismatch = document.getElementById('passwordMismatch');
    const usernameMismatch = document.getElementById('usernameMismatch');


    function validateEmail(inputEmail) {
        const pattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        return pattern.test(inputEmail);
    }

    function validatePassword(inputPassword) {
        const pattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
        return pattern.test(inputPassword);
    }

    function checkPasswordMatch() {
        if (password.value === confirmPassword.value) {
            confirmPassword.style.border = '1px solid #00917B';
            passwordMismatch.textContent = '';
        } else {
            confirmPassword.style.border = '1px solid #FF8962';
            passwordMismatch.textContent = 'Passwords do not match';
        }
    }

    function checkUsername() {
        if (username.value.length < 4) {
            username.style.border = '1px solid #FF8962';
            usernameMismatch.textContent = 'Username should be at least 4 characters long';

        } else {
            username.style.border = '1px solid #00917B';
            usernameMismatch.textContent = '';
        }
    }

    function checkEmail() {
        if (!validateEmail(email.value)) {
            email.style.border = '1px solid #FF8962';
        } else {
            email.style.border = '1px solid #00917B';
        }
    }

    function checkPasswordSpecs() {
        if (!validatePassword(password.value)) {
            password.style.border = '1px solid #FF8962';
            passwordMismatch.textContent = 'Password should have at least 8 characters, 1 uppercase, 1 lowercase and 1 number';
        } else {
            password.style.border = '1px solid #00917B';
            passwordMismatch.textContent = '';
        }
    }

    username.addEventListener('keyup', checkUsername);
    email.addEventListener('keyup', checkEmail);
    password.addEventListener('keyup', checkPasswordSpecs);
    confirmPassword.addEventListener('keyup', checkPasswordMatch);
});

