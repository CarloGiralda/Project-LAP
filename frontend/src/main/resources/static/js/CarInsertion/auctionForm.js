document.addEventListener("DOMContentLoaded", async function () {

    // Check if the user is authenticated based on the presence of the HttpOnly cookie
    const isAuthenticated = document.cookie.includes('jwtToken')
    console.log("user is authenticated:", isAuthenticated)

    if (!isAuthenticated){
        window.location.href = "http://localhost:8081/login"
    }
    else {

        // Get the current date in the format YYYY-MM-DD
        // Set the value of the date input to the current date
        const currentDate = new Date().toISOString().split( 'T' )

        const day = currentDate[0]
        const hour = currentDate[1].substring( 0, 5 )

        document.getElementById( 'date' ).min = day;
        document.getElementById( 'date' ).value = day;
        document.getElementById( 'time' ).min = hour;
    }
})

async function submitForm() {
    var token = getCookie("jwtToken");

    // Replace these URLs with the ones you want to use
    var postUrl = "http://localhost:8080/carinsert/insertauction";
    var successUrl = "http://localhost:8081/carinsert/insertcar";
    var problemUrl = "http://localhost:8081/failed";

    var date = document.getElementById("date").value;
    var time = document.getElementById("time").value;
    const dateTimeString = date + "T" + time + ":00Z";
    var username = sessionStorage.getItem("username");

    const params = {
        startDate: dateTimeString,
        cid: 0,
        peerId: username
    };

    var xhr = new XMLHttpRequest();

    xhr.open("POST", postUrl, true);

    xhr.setRequestHeader('Content-type', 'application/json')
    xhr.setRequestHeader('Authorization', "Bearer " + token)

    xhr.onload = function () {
        if (xhr.status === 200) {
            // Redirect to the success page
            window.location.href = successUrl;
        } else {
            window.location.href = problemUrl;
        }
    };

    xhr.send(JSON.stringify(params));
}