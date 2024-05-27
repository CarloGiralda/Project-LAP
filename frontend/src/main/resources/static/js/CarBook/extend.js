document.addEventListener("DOMContentLoaded", function () {
    // Check if the user is authenticated based on the presence of the HttpOnly cookie
    const isAuthenticated = document.cookie.includes('jwtToken')

    if (!isAuthenticated){
        window.location.href = "http://localhost:8081/login"
    }

    else {

        // Get the current date in the format YYYY-MM-DD
        // Set the value of the date input to the current date
        const currentDate = new Date().toISOString().split( 'T' )

        const day = currentDate[0]
        const hour = currentDate[1].substring(0,5)

        document.getElementById('to').min = day;
        document.getElementById('to').value = day;

        const urlParams = new URLSearchParams(window.location.search)
        const bid = urlParams.get("bid")
        const cid = urlParams.get("cid")
        if (bid && cid){
            document.getElementById("bid").value = bid;
            document.getElementById("user").value = sessionStorage.getItem("username");
            document.getElementById("cid").value = cid;

        }
    }

});


document.getElementById('myForm').addEventListener('submit', function (event) {
        event.preventDefault(); // Prevent the default form submission

        // Here you can add logic for form submission and checking if something went wrong
        submitFormInSequence();

});


function submitFormInSequence() {
    const bid = document.getElementById('bid').value
    const cid = document.getElementById('cid').value;
    let toDay = document.getElementById('to').value;
    let toHour = document.getElementById('toHour').value

    // format date with new data
    const formattedDate = toDay.split("-")
    toDay = formattedDate[2] + "-" + formattedDate[1] + "-" + formattedDate[0]

    const token = getCookie("jwtToken");
    //document.getElementById("username").value = sessionStorage.getItem("username");

    // Create a new XMLHttpRequest object
    const xhr = new XMLHttpRequest();

    // Specify the request type (GET or POST), URL, and whether it should be asynchronous
    xhr.open("POST", "http://localhost:8080/reservation/extendBooking", true);

    xhr.setRequestHeader( "Content-Type", "application/json" );
    xhr.setRequestHeader('Authorization', 'Bearer ' + token)

    const jsonData = {
        bid: bid,
        cid: cid,
        toDay: toDay,
        toHour: toHour
    }
    console.log(jsonData)

    const jsonDataString = JSON.stringify(jsonData);

    // Set up a callback function to handle the response
    xhr.onreadystatechange = function () {
      // Check if the request is complete (readyState 4) and if the status is OK (status 200)
        if (xhr.status === 200) {

            // Show the success message
            const successMessage = document.getElementById('successMessage');
            successMessage.style.display = 'block';


            setTimeout(function () {
                successMessage.style.display = 'none';
            }, 20000);

        } else {
            const errorMessage = document.getElementById('errorMessage');
            errorMessage.style.display = 'block';


            setTimeout(function () {
                errorMessage.style.display = 'none';
            }, 20000);
        }


    };

    // Send the JSON data as the request payload
    xhr.send(jsonDataString);


}



function getParameterByName(name) {
    name = name.replace(/[\[\]]/g, "\\$&");
    const regex = new RegExp( "[?&]" + name + "(=([^&#]*)|&|#|$)" ),
        results = regex.exec( window.location.search );
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}