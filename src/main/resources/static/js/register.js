document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const data = {
        fullName: document.getElementById('fullName').value.trim(),
        email: document.getElementById('email').value.trim(),
        password: document.getElementById('password').value.trim(),
        role: document.getElementById('role').value
    };

    // Validation
    if (!data.fullName || !data.email || !data.password) {
        document.getElementById('registerMessage').innerHTML =
            `<span class="text-danger">All fields are required!</span>`;
        return;
    }

    try {
        const res = await fetch('http://localhost:8080/api/users/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        // Backend returns plain text, not JSON → use res.text()
        const message = await res.text();

        if (res.ok) {
            document.getElementById('registerMessage').innerHTML =
                `<span class="text-success">${message}</span>`;
            document.getElementById('registerForm').reset();
        } else {
            document.getElementById('registerMessage').innerHTML =
                `<span class="text-danger">${message}</span>`;
        }
    } catch (err) {
        document.getElementById('registerMessage').innerHTML =
            `<span class="text-danger">Server Error! Try again later.</span>`;
    }
});
