let registrationEndpoint = "http://localhost:8080/auth/register";
let registrationForm = document.getElementById("registrationForm");
let overlay = document.getElementById("overlay");
let successMessage = document.querySelector(".alert-info");
let errorMessage = document.querySelector(".alert-danger");

function showOverlay() {
    overlay.classList.remove("d-none");
}

function hideOverlay() {
    overlay.classList.add("d-none");
}

function showSuccessMessage() {
    hideOverlay(); // Hide the overlay when the registration is successful
    successMessage.style.display = "block";
    errorMessage.style.display = "none";
}

function showErrorMessage(message) {
    hideOverlay(); // Hide the overlay when an error occurs
    successMessage.style.display = "none";
    errorMessage.style.display = "block";
    errorMessage.textContent = message;
}

registrationForm.addEventListener("submit", (event) => {
    event.preventDefault();

    showOverlay(); // Show the overlay when the registration process starts

    const formData = new FormData(registrationForm);
    const data = Object.fromEntries(formData);

    fetch(registrationEndpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(handleResponse)
        .then(data => console.log(data))
        .catch(error => console.log(error));
});

function handleResponse(res) {
    if (res.ok) {
        showSuccessMessage();
    } else {
        res.clone().text().then(errorText => {
            console.error("Error response from server:", errorText);
            showErrorMessage(errorText);
        });
    }
    return res.text();
}
