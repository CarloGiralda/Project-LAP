const userDropDown = document.getElementById('userDropDown')
const reservationForm = document.getElementById('reservationForm')
let errorMessage = document.getElementById('error');
let errorModal = document.getElementById('error-modal');
document.addEventListener("DOMContentLoaded", function () {

// Check if the user is authenticated based on the presence of the HttpOnly cookie
    const isAuthenticated = document.cookie.includes('jwtToken')
    const usernameDisplay = document.getElementById('usernameDisplay')
    const overlay = document.getElementById('overlay');
    console.log("user is authenticated:", isAuthenticated)

    if (!isAuthenticated){
        window.location.href = "http://localhost:8081/login"
    } else {
        // Get the current date in the format YYYY-MM-DD
        // Set the value of the date input to the current date
        const currentDate = new Date().toISOString().split( 'T' )

        const day = currentDate[0]

        document.getElementById('fromDate').min = day;
        document.getElementById('fromDate').value = day;
        document.getElementById('toDate').min = day;

        // read name from session storage and show to the user
        usernameDisplay.textContent = sessionStorage.getItem("username")

        // show user dropdown
        userDropDown.style.display = 'block'

        const path = window.location.href.split( '/' );

        console.log("setting username")
        document.getElementById( "username" ).value = sessionStorage.getItem( "username" );
        document.getElementById( "cid" ).value = path[path.length - 1];
    }

    function showOverlay() {
        overlay.classList.remove('d-none');
    }

    reservationForm.addEventListener( 'submit', (event) => {

        event.preventDefault(); // Prevent the default form submission
        console.log( "starting reservation..." )
        // show overlay on button click
        showOverlay()

        // Get form field values
        const username = document.getElementById( "username" ).value;
        const carCode = document.getElementById( "cid" ).value;
        const fromDate = document.getElementById( "fromDate" ).value;
        const toDate = document.getElementById( "toDate" ).value;
        const fromTime = document.getElementById( "fromH" ).value;
        const toTime = document.getElementById( "toH" ).value;
        const description = document.getElementById('descriptionInput').value;

        // Prepare form data
        const formData = new FormData();
        formData.append('username', username);
        formData.append('cid', carCode);
        formData.append('fromDate', fromDate);
        formData.append('toDate', toDate);
        formData.append('fromH', fromTime);
        formData.append('toH', toTime);
        formData.append('description', description);


        submitFormInSequence( formData ).then( r => {})
    });
});

async function submitFormInSequence (data) {

    const token = getCookie( "jwtToken" );
    const reservationUrl = "http://localhost:8080/reservation/book"

    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Specify the request type (GET or POST), URL, and whether it should be asynchronous
    xhr.open( "POST", reservationUrl, true );

    // Set the request header and authentication header
    xhr.setRequestHeader( "Content-Type", "application/json" );
    xhr.setRequestHeader( 'Authorization', 'Bearer ' + token )
    console.log("authorization header:" + "Bearer " + token)

    const today = new Date();

    // format date
    const yyyy = today.getFullYear();
    let mm = today.getMonth() + 1; // Months start at 0!
    let dd = today.getDate();

    if (dd < 10) dd = '0' + dd;
    if (mm < 10) mm = '0' + mm;

    const formattedToday = dd + '-' + mm + '-' + yyyy;

    // format fromdate
    const split_from_date =  data.get('fromDate').split("-");
    const fromdate = split_from_date[2] + '-' + split_from_date[1] + '-' + split_from_date[0];

    // format todate
    const split_to_date =  data.get('toDate').split("-");
    const todate = split_to_date[2] + '-' + split_to_date[1] + '-' + split_to_date[0];

    // Define the JSON data to be sent
    const jsonData = {
        cid: data.get('cid'),
        madeDate: formattedToday,
        fromDay: fromdate,
        toDay: todate,
        fromHour: data.get('fromH'),
        toHour: data.get('toH'),
        username: data.get('username'),
        flag: formattedToday,
        description: data.get('description')
        // Add more key-value pairs as needed
    };
    console.log(jsonData);

    // Convert the JSON data to a string
    const jsonDataString = JSON.stringify( jsonData );
    console.log( jsonDataString );

    console.log("sending authorization header with token " + token)
    // Set up a callback function to handle the response
    xhr.onreadystatechange = async function () {

        // if car has been inserted correctly then starts creating PayPal payment
        if (xhr.readyState === 4 && xhr.status === 201) {
            console.log( "creating payment" )
            createPayment()
        } else {
            showErrorModal("Retry later, an error has occurred")
        }
    };
    // Send the JSON data as the request payload
    xhr.send(jsonDataString);

}

// check if the dates are consistent
document.getElementById("fromDate").addEventListener('change', event => {
    checkDates();
})

document.getElementById("fromH").addEventListener('change', event => {
    checkDates();
})

document.getElementById("toDate").addEventListener('change', event => {
    checkDates();
})

document.getElementById("toH").addEventListener('change', event => {
    checkDates();
})

function checkDates() {
    var from = document.getElementById("fromDate").value;
    var ftime = document.getElementById("fromH").value;
    var to = document.getElementById("toDate").value;
    var ttime = document.getElementById("toH").value;
    // conversion from date to epoch
    var fd = new Date(from + "T" + ftime);
    fd = fd.getTime();
    // conversion from date to epoch
    var td = new Date(to + "T" + ttime);
    td = td.getTime();

    const currentDate = new Date();
    currentD = currentDate.getTime();

    if (td - fd <= 0 || fd - currentDate <= 0) {
        showErrorMessage("Invalid Date");
        document.getElementById("openSubmitModal").disabled = true;
    } else {
        hideErrorMessage();
        document.getElementById("openSubmitModal").disabled = false;
    }
}

function showErrorModal(message) {
    errorModal.style.display = "block";
    errorModal.textContent = message;
    overlay.classList.add('d-none');
}

function hideErrorModal() {
    errorModal.style.display = "none";
}

function showErrorMessage(message) {
    errorMessage.style.display = "block";
    errorMessage.textContent = message;
}
function hideErrorMessage() {
    errorMessage.style.display = "none";
}

// set price per hour
document.getElementById("openSubmitModal").addEventListener("click", async (event) => {
    console.log("modal opening")

    const price = await getPrice(document.getElementById("cid").value);
    const balance = await getBalance();

    // compute time difference
    const fromDate = new Date(document.getElementById("fromDate").value);
    const toDate = new Date(document.getElementById("toDate").value);
    const fromTime = document.getElementById("fromH").value;
    const toTime = document.getElementById("toH").value;

    // Extract hours and minutes from time inputs
    const [fromHours, fromMinutes] = fromTime.split(':').map(Number);
    const [toHours, toMinutes] = toTime.split(':').map(Number);

    // Set the time values in the date objects
    fromDate.setHours(fromHours, fromMinutes, 0, 0);
    toDate.setHours(toHours, toMinutes, 0, 0);

    // Calculate the time difference in milliseconds
    const timeDifference = toDate - fromDate;

    // Convert milliseconds to hours
    const hoursDifference = timeDifference / (1000 * 60 * 60);

    if (price !== null) {
        const totalCost = (Number(hoursDifference) * Number(price)).toFixed(2);
        console.log("Time Difference in Hours:", hoursDifference);
        console.log("Total Cost:", totalCost);
        document.getElementById("price").innerHTML = totalCost;
        document.getElementById("balance").innerHTML = balance;
        if (Number(totalCost) > Number(balance)) {
            showErrorModal("You are too poor!");
            document.getElementById("submit").disabled = true;
        } else {
            hideErrorModal();
            document.getElementById("submit").disabled = false;
        }
    }

})

// Create payment
function createPayment() {

    var token = getCookie("jwtToken");

    // Example usage
    const payer_username = sessionStorage.getItem("username");
    const cid = document.getElementById("cid").value;

    fetch("http://localhost:8080/carsearch/getRenterUsername?id=" + cid, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        }
    })
        .then(function (response) {
            return response.json();
        })
        .then(function (body) {
            const beneficiary_username = body["renter"];

            const price = document.getElementById("price").innerHTML;

            const data = {
                senderUsername: payer_username,
                receiverUsername: beneficiary_username,
                price: Number(price),
            };

            const token = getCookie("jwtToken");

            // extract the username from the jwt token
            fetch("http://localhost:8080/payment/createTransaction", {
                method: 'POST',
                headers: {
                    'Content-Type': "application/json",
                    'Authorization': "Bearer " + token
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (response.status === 200) {
                        window.location.href = "http://localhost:8081/inProgress";
                    } else {
                        window.location.href = "http://localhost:8081/failed";
                    }
                })
        })
}

async function getBalance(){
    const url = "http://localhost:8080/payment/getBalance"
    const token = getCookie("jwtToken");

    return await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return null;
            }

        })
        .then(data => {
            if (data !== null){
                console.log("balance: ", data);
                return data;
            } else {
                showErrorMessage("Cannot get your balance because service is not available, retry later!")
            }


        })
        .catch(error => console.log(error));
}

async function getPrice(carId){

    const searchUrl = "http://localhost:8080/carsearch/getPriceById/" + carId;

    return await fetch(searchUrl, {
        method: 'GET',
    })
        .then(response => {
            return response.json();
        })
        .then(data => {
            console.log("price for car: ", data);
            return data;
        })
}



