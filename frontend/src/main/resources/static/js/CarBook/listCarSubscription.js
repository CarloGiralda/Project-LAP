const CarPreviewDTO = {
    brand: '',
    model: '',
    engine: '',
    year: '',
    image: ''
};

function getSubscribedCars(token) {
    const carBookUrl = "http://localhost:8080/subscription/getCarsForUser/" + sessionStorage.getItem("username")
    const carSearchUrl = "http://localhost:8080/carsearch/getCarPreviewById/"
    const subscriptionMessage = document.getElementById("subscriptionsError")
    fetch(carBookUrl, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        },
    })
        .then(response => {

            if (response.status === 404) { // not found

                subscriptionMessage.style.display = "block"
                subscriptionMessage.innerHTML += "You are not subscribed for a car. Start searching using the buttons below"
                console.log("no subscribed car")
                return null
            }
            else if(response.status === 503) { // not available

                subscriptionMessage.style.display = "block"
                subscriptionMessage.innerHTML += "Service not available retry later"
                console.log("service not available, retry later")

                return null
            }
            else {
                return response.json()
            }
        })
        .then(response => {
            // for each of these car we have to perform a request to get car preview


            if (response != null && response.length > 0) {

                document.getElementById("subscriptionsSlider").style.display = "block"
                const cardContainer = document.getElementById('carouselSubscriptions');

                for (let i = 0; i < response.length; i++) {


                    let carId = response[i]
                    fetch( carSearchUrl + carId, {
                        method: 'GET'
                    }).then(  response => {

                        if (response.status === 200){
                            return response.json()
                        } else {
                            return response.text().then(errorMessage => {
                                throw new Error(errorMessage);
                            });
                        }

                    } )
                        .then(response => {

                            const carPreview = Object.create(CarPreviewDTO)
                            carPreview.brand = response.brand
                            carPreview.model = response.model
                            carPreview.engine = response.engine
                            carPreview.year = response.year



                            const card = createCard(
                                carPreview.brand + ' ' + carPreview.model,
                                ['Engine: ' + carPreview.engine, 'Year: ' + carPreview.year],
                                ['http://localhost:8081/carsearch/' + carId, "deleteSub/" + carId],
                                response.image
                            );



                            cardContainer.appendChild(card);


                        }).catch(error => {
                            document.getElementById("subscriptionsSlider").style.display = "none"
                            console.log(error)
                    } );
                }
            }
        })
        .catch(error => console.log(error));
}