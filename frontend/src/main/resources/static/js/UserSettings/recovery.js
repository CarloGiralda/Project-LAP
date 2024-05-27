// Replace these URLs with the ones you want to use
var emailRecoveryUrl = "http://localhost:8080/email/recovery";

let overlay = document.getElementById("overlay")
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

function showErrorMessage() {
    hideOverlay(); // Hide the overlay when an error occurs
    successMessage.style.display = "none";
    errorMessage.style.display = "block";
}

function submitForm() {

    showOverlay(); // Show the overlay when the registration process starts

    // Get form data
    const email = document.getElementById("email").value;

    console.log("recovery email: ",email);

    const url = emailRecoveryUrl + '?email=' + email;

    // extract the username from the jwt token
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            // valid response
            if (response.status === 200) {
                showSuccessMessage()
            } else {
                showErrorMessage()
            }
        })
}

