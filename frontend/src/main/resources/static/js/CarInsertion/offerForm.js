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

        document.getElementById( 'from' ).min = day;
        document.getElementById( 'from' ).value = day;
        document.getElementById( 'to' ).min = day;
    }
})

        async function submitForm() {
    var token = getCookie("jwtToken");

    // Replace these URLs with the ones you want to use
    var postUrl = "http://localhost:8080/carinsert/insertoffer";
    var successUrl = "http://localhost:8081/carinsert/insertcar";
    var problemUrl = "http://localhost:8081/failed";

    // Get form data
    var from = document.getElementById("from").value;
    // conversion from date to epoch
    var fd = new Date(from);
    fd = fd.getTime();
    var to = document.getElementById("to").value;
    // conversion from date to epoch
    var td = new Date(to);
    td = td.getTime();
    var price = document.getElementById("price").value;
    var username = sessionStorage.getItem("username");
    var zone = document.getElementById("zone").value;

    // convert name into coordinates
    var getUrl = "http://localhost:8080/area/searchLocation?query=" + zone;

    await fetch(getUrl, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        },
    })
        .then(function (response) {
            return response.json();
        })
        .then(function (body) {
            let much = Math.random() * 0.0001;
            let which = Math.floor(Math.random() * 4);
            if (which === 1) {
                zone = String(Number(body["lat"]) + Number(much)) + "/" + String(body["lon"]);
            } else if (which === 2) {
                zone = String(body["lat"]) + "/" + String(Number(body["lon"]) + Number(much));
            } else if (which === 3) {
                zone = String(Number(body["lat"]) - Number(much)) + "/" + String(body["lon"]);
            } else {
                zone = String(body["lat"]) + "/" + String(Number(body["lon"]) - Number(much));
            }
        });

    const params = {
        fromDate: fd,
        toDate: td,
        pricePerHour: price,
        renterUsername: username,
        zoneLocation: zone
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