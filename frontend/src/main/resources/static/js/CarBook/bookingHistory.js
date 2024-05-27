document.addEventListener("DOMContentLoaded", function () {


    const isAuthenticated = document.cookie.includes('jwtToken')

    if (!isAuthenticated){
        window.location.href = "http://localhost:8081/login"
    }

    else {
        const token = getCookie( "jwtToken" );
        const bookingUrl = "http://localhost:8080/reservation/listBookingsHistory"

        fetch( bookingUrl, {
            method: 'GET',
            headers: {
                'Authorization': "Bearer " + token
            },
        } ).then( response  => {
            if (response.status === 404) { // not found code
                const notFound = document.getElementById("notFound")
                notFound.style.display = "block"
                console.log("no reservation history")
                return null
            }
            if(response.status === 503) { // not available
                const notFound = document.getElementById("notFound")
                notFound.value = "service not available, retry later"
                notFound.style.display = "block"
                console.log("service not available, retry later")
                return null
            }
            if (response.status === 200){
                return response.json();
            }

        })
            .then( response => {
                showHistory(response);
            })
    }




});



document.getElementById("cards").addEventListener("click", function(event) {
    if (event.target && event.target.tagName === "BUTTON") {
        // If a button is clicked, extract the index and booking information from the button's data attributes
        const index = event.target.getAttribute("data-index");
        const bookingInfo = event.target.getAttribute("data-booking");

        console.log("Clicked booking index:", index);

        // Call the fillExtendForm function with the extracted parameters
        fillExtendAndRatingForm( bookingInfo);
    }
});

function showHistory(bookings) {
    if (bookings !== null && bookings.length > 0){
        console.log(bookings)
        document.getElementById("cards").innerText = "";

        for (let i = 0; i < bookings.length; i++) {
            // the button should be disabled if the booking has been expired within 10 minutes
            if (expired(bookings[i].toDay,bookings[i].toHour)){
                document.getElementById("cards").innerHTML += `
                <div class="col">
                    <div class="card p-2 ps-4 pe-4">
                        <div class="container bg-body-secondary">
                            <h4 class="text-capitalize fw-light text-center mt-2">Booking id: ${bookings[i].bid}</h4>
                        </div>
                        
                        <div class="card-body">
                            <p class="lead text-body-secondary mt-2" style="font-size: 20px;">
                                <i class="bi bi-person-badge"></i> Car id: <i> ${bookings[i].cid} </i> </p>
                        
                            <p class="lead text-body-secondary mt-2" style="font-size: 20px;">
                                <i class="bi bi-calendar"></i> To day: <i>${bookings[i].toDay} ${bookings[i].toHour} </i></p>
                            
                  
                            <div style="display: flex; gap: 12px;">
                                <button type="button" onclick="window.location.href='http://localhost:8081/carsearch/${bookings[i].cid}'" class="btn btn-danger mt-sm-4 form-control" style="flex: 2;">Car page</button>
                                <button type="button" class="btn btn-danger mt-sm-4 form-control" data-index="${i}" data-booking="${JSON.stringify(bookings[i]).replace(/"/g, '&quot;')}" data-bs-toggle="modal" data-bs-target="#ratingModal" style="flex: 2;">Give your rate <i class="bi bi-star"></i></button>
                            </div> 
                        </div>
                                               
                    </div>
                </div>
            `;
            } else {
                document.getElementById("cards").innerHTML += `
                <div class="col">
                    <div class="card p-3 ps-5 pe-5">
                        <div class="container bg-body-secondary">
                            <h4 class="text-capitalize fw-light text-center mt-2">Booking id: ${bookings[i].bid}</h4>
                        </div>
                        <div class="card-body">
                            <p class="lead text-body-secondary mt-2" style="font-size: 20px;">
                                <i class="bi bi-person-badge"></i> Car id: <i> ${bookings[i].cid} </i> </p>
                        
                            <p class="lead text-body-secondary mt-2" style="font-size: 20px;">
                                <i class="bi bi-calendar"></i> To day: <i>${bookings[i].toDay} ${bookings[i].toHour} </i></p>
                  
                            <div style="display: flex; gap: 12px;">
                                <button type="button" onclick="window.location.href='http://localhost:8081/carsearch/${bookings[i].cid}'" class="btn btn-secondary mt-sm-4 form-control" style="flex: 2;">Car page</button>
                                <button type="button" class="btn btn-primary mt-sm-4 form-control" data-index="${i}" data-booking="${JSON.stringify(bookings[i]).replace(/"/g, '&quot;')}" data-bs-toggle="modal" data-bs-target="#extendModal" style="flex: 2;">Extend rent</button>
                            </div>
                        </div>
                                       
                    </div>
                </div>
            `;
            }



        }
    }

}



function fillExtendAndRatingForm(booking) {
    // Parse the JSON string into an object
    booking = JSON.parse(booking);

    // Access the properties of the booking object and fill the modal with the data
    document.getElementById("bid").value = booking.bid;
    document.getElementById("user").value = sessionStorage.getItem("username");
    document.getElementById("cid").value = booking.cid;

    // set cid of car to rate
    document.getElementById("ratingCid").value = booking.cid

    const currentDate = new Date()
    document.getElementById("date").value = currentDate.toISOString().split('T')[0];


}



function expired(inputDate, inputHour) {

    // Convert inputDate and inputHour to a JavaScript Date object
    const inputDateTime = parseDateTime(inputDate,inputHour)

    // Calculate the current date and time
    const currentDateTime = new Date();

    // Calculate the difference in milliseconds
    const timeDifference = currentDateTime - inputDateTime;

    // Check if the time difference is greater than or equal to 10 minutes (in milliseconds)
    const tenMinutesInMillis = 10 * 60 * 1000;

    // return time differences
    return timeDifference >= tenMinutesInMillis;
}


function parseDateTime(dateString, timeString) {
    // Parse date string
    const dateParts = dateString.split( "-" );
    const year = parseInt( dateParts[2], 10 );
    const month = parseInt( dateParts[1], 10 ) - 1; // Months are zero-indexed
    const day = parseInt( dateParts[0], 10 );

    // Parse time string
    const timeParts = timeString.split( ":" );
    const hours = parseInt( timeParts[0], 10 );
    const minutes = parseInt( timeParts[1], 10 );

    // Create a new Date object with the parsed values
    return new Date( year, month, day, hours, minutes );
}