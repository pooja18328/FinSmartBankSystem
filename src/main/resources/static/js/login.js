document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const data = {
        email: document.getElementById('loginEmail').value.trim(),
        password: document.getElementById('loginPassword').value.trim()
    };

    const messageDiv = document.getElementById('loginMessage');

    // Validation
    if (!data.email || !data.password) {
        messageDiv.innerHTML = `<span class="text-danger">All fields are required!</span>`;
        return;
    }

    try {
        const res = await fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        const message = await res.text();

        if (res.ok && message.includes("Login Successful")) {
            messageDiv.innerHTML = `<span class="text-success fw-bold">${message}</span>`;
            document.getElementById('loginForm').reset();

            // ✅ Wait 2 seconds, then redirect based on role
            setTimeout(() => {
                // Extract role from message safely
                let role = null;
                const roleMatch = message.match(/Role:\s*(\w+)/i);
                if (roleMatch && roleMatch[1]) {
                    role = roleMatch[1].toLowerCase();
                }

                if (role === 'admin') {
                    window.location.href = "/admin-dashboard"; // Controller mapping
                } else {
                    window.location.href = "/services"; // Controller mapping
                }
            }, 2000);
        } else {
            messageDiv.innerHTML = `<span class="text-danger fw-bold">${message}</span>`;
        }
    } catch (err) {
        messageDiv.innerHTML = `<span class="text-danger fw-bold">Server Error! Try again later.</span>`;
    }
});
