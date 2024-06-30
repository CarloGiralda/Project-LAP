function getInsertedCar() {
    const getUrl = "http://localhost:8080/carinsert/listcars"
    const token = getCookie( "jwtToken" );


    fetch(getUrl, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        }
    })
        .then(function(response) {
            const insertedMessage = document.getElementById("insertedError")

            if (response.status === 404){
                insertedMessage.style.display = "block"
                insertedMessage.innerHTML += "You have no machines entered. Start filling your catalog and allow others to rent them"
                console.log("no inserted car")
                return null
            }
            else if (response.status === 503){
                insertedMessage.style.display = "block"
                insertedMessage.innerHTML += "Service not available retry later"
                console.log("service not available")
                return null
            }
            else {
                return response.json();
            }

        })
        .then(function(response) {

            if (response != null && response.length > 0) {

                document.getElementById( 'insertedSlider' ).style.display = "block";
                const cardContainer = document.getElementById('carouselInserted')



                for(let x= 0; x < response.length; x++) {

                    let content
                    if (String(response[x]["offer"]["pricePerHour"]) === "-1"){
                        content = ["AUCTION CAR"]
                    } else {
                        content = ["Price: " + String(response[x]["offer"]["pricePerHour"]) + " coins/hour"]
                    }

                    const card = createCard(
                        String(response[x]["car"]["brand"]) + ' ' + String(response[x]["car"]["model"]),
                        content,
                        ["http://localhost:8081/carinsert/modify/" + response[x]["car"]["cid"]],
                        response[x]["car"]["image"]
                    )
                    cardContainer.appendChild(card);

                }
            }



        }).catch(error => console.log(error));
}