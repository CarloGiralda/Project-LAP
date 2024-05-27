let getJwtEndPoint = API_ENDPOINTS.authJwtToken;
let extractUsernameFromJwtEndpoint = API_ENDPOINTS.extractUsernameJwtToken;


let loginForm = document.getElementById("loginForm");
let overlay = document.getElementById("overlay");
let errorMessage = document.querySelector(".alert-danger");

function showOverlay() {
    overlay.classList.remove("d-none");
}

function hideOverlay() {
    overlay.classList.add("d-none");
}

function showErrorMessage(message) {
    hideOverlay(); // Hide the overlay when an error occurs
    errorMessage.style.display = "block";
    errorMessage.textContent = message;
}

loginForm.addEventListener("submit", (event) => {
    event.preventDefault();

    showOverlay(); // Show the overlay when the registration process starts


    const formData = new FormData(loginForm);
    const data = Object.fromEntries(formData);
    console.log(data)

    // Get the jwt token given the user
    fetch(getJwtEndPoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {

        if (response.status === 500) {
            throw new Error("invalid data")
        }
        else if (response.status === 503){
            throw new Error("service not available, retry later")
        }
        return response.json()
    })
        .then(response => handleResponse(response))
        .catch(error => {
            showErrorMessage(error)
            console.log("error: ", error)
        });

});

function handleResponse(res) {

    const jwtToken = res.jwt;
    console.log("jwt token: ", res.jwt);

    // set the jwt token as a session cookie
    document.cookie = "jwtToken=" + jwtToken


    const url = extractUsernameFromJwtEndpoint + '?token=' + jwtToken;

    // extract the username from the jwt token
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.json())
        .then(response => {
            const usernameFromJwt = response.username;
            console.log("extracted username: ", usernameFromJwt);

            // store username in local storage
            sessionStorage.setItem('username', usernameFromJwt);

            // redirect to homepage html
            window.location.href = '/';

        })
        .catch(error => console.log(error));
}


// on login page if logout is true then clean session storage and remove jwt token
document.addEventListener("DOMContentLoaded", function () {

    const urlParams = new URLSearchParams(window.location.search)
    const loggedOut = urlParams.get("loggedOut") === "true";
    if (loggedOut) {
        console.log(loggedOut, typeof(loggedOut))

        // clean session storage
        sessionStorage.removeItem("username")

        if (document.cookie.includes('jwtToken')) {
            // set to an old date to allow expiration
            document.cookie = "jwtToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        }


    }

});






