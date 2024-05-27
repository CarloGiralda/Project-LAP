document.addEventListener("DOMContentLoaded", function () {
    // Check if the user is authenticated based on the presence of the HttpOnly cookie
    const isAuthenticated = document.cookie.includes('jwtToken')
    const usernameDisplay = document.getElementById('usernameDisplay')
    const userDropDown = document.getElementById('userDropDown')
    const userSettingsForm = document.getElementById("userSettingsForm")

    if (!isAuthenticated){
        window.location.href = "http://localhost:8081/login"
    }

    else {
        // TODO load data if they are the same on click you don't send to backend
        if(userSettingsForm){
            getUserSettings()
            getBalance()
        }

        console.log("user is authenticated:", isAuthenticated)

        if (isAuthenticated && usernameDisplay) {

            // read name from session storage and show to the user
            usernameDisplay.textContent = sessionStorage.getItem("username")

            // show user dropdown
            userDropDown.style.display = 'block'
        }
    }



});

function getUserSettings(){
    const url = "http://localhost:8080/auth/getUser"
    const token = getCookie("jwtToken");

    fetch(url, {
        method: 'GET',
        headers: {
            'username': sessionStorage.getItem("username")
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log(data)

            document.getElementById("firstName").value = data.firstName
            document.getElementById("lastName").value = data.lastName
            document.getElementById("email").value = data.email
            //document.getElementById("contact").value = data.contact

        })
        .catch(error => console.log(error));

}


function getBalance(){
    const url = "http://localhost:8080/payment/getBalance"
    const token = getCookie("jwtToken");

    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json()
            } else {
                return null;
            }

        })
        .then(data => {
            if (data !== null){
                document.getElementById("balance").value = data + " coin"
                return 0;
            } else {
              showErrorMessage("Cannot get your balance because service is not available, retry later!")
            }


        })
        .catch(error => console.log(error));
}


document.getElementById("userSettingsForm").addEventListener("submit", (event) => {
    event.preventDefault()
    const postUrl = "http://localhost:8080/email/userSettings/modify";

    // TODO upload with new data
    const formData = new FormData(document.getElementById("userSettingsForm"))
    const data = Object.fromEntries(formData)
    const token = getCookie("jwtToken");

    console.log("sending data", data)

    fetch(postUrl, {
        method: 'POST',
        headers: {
            'Authorization': "Bearer " + token,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then( res =>  {
            if (res.ok) {
                showSuccessMessage();
            } else {
                res.clone().text().then(errorText => {
                    console.error("Error response from server:", errorText);
                    showErrorMessage("internal server error");
                });
            }
            return res.text();
        }).catch(error => console.log(error));

})


function showSuccessMessage() {
    let successMessage = document.querySelector(".alert-info");
    let errorMessage = document.querySelector(".alert-danger");
    //hideOverlay(); // Hide the overlay when the registration is successful
    successMessage.style.display = "block";
    errorMessage.style.display = "none";
}

function showErrorMessage(message) {
    let successMessage = document.querySelector(".alert-info");
    let errorMessage = document.querySelector(".alert-danger");
    //hideOverlay(); // Hide the overlay when an error occurs
    successMessage.style.display = "none";
    errorMessage.style.display = "block";
    errorMessage.textContent = message;
}