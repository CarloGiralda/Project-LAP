const BookingPreviewDTO = {
    bid: '',
    cid: '',
    fromDay: '',
    toDay: '',
    toHour:'',
    madeDate: ''
}

function getReservations(token){
    const bookingUrl = "http://localhost:8080/reservation/listBookingsPreview"

    fetch(bookingUrl, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        },
    })
        .then(response => {

            const reservationMessage = document.getElementById("reservationsError")
            if (response.status === 404) { // not found code

                reservationMessage.style.display = "block"
                reservationMessage.innerHTML += "Your list is empty, start searching a car."

                console.log("no reservations")
                //throw new Error("no reservation");
                return null
            }
            else if(response.status === 503) { // not available

                reservationMessage.style.display = "block"
                reservationMessage.innerHTML += "Service not available retry later"

                console.log("service not available, retry later")
                return null
            }
            else {
                return response.json();
            }

        })
        .then(response => {
            // for each of these car we have to perform a request to get car preview

            if (response !== null && response.length > 0){

                document.getElementById("reservationsSlider").style.display = "block"

                const cardContainer = document.getElementById('carouselReservations');

                for (let i = 0; i < response.length; i++) {

                    const bookingPreview = Object.create(BookingPreviewDTO)
                    BookingPreviewDTO.bid = response[i].bid
                    BookingPreviewDTO.cid = response[i].cid
                    BookingPreviewDTO.fromDay = response[i].fromDay
                    BookingPreviewDTO.toDay = response[i].toDay
                    BookingPreviewDTO.toHour = response[i].toHour
                    BookingPreviewDTO.madeDate = response[i].madeDate

                    const extendParam = "?bid="+BookingPreviewDTO.bid + "&cid="+BookingPreviewDTO.cid

                    const card = createCard(
                        'Booking code: ' + BookingPreviewDTO.bid.toString(),
                        [
                            'Car id: ' + BookingPreviewDTO.cid.toString(),
                            'Pick-up date: ' + BookingPreviewDTO.fromDay,
                            'Drop-off date: ' + BookingPreviewDTO.toDay + " " + BookingPreviewDTO.toHour,
                            'Made date: ' + BookingPreviewDTO.madeDate
                        ],
                        ['http://localhost:8081/carsearch/'+ BookingPreviewDTO.cid.toString(),
                            'http://localhost:8081/extend' + extendParam]
                    );

                    cardContainer.appendChild(card);

                }


            }




        })
        .catch(error => console.log(error));

}