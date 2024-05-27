function getZoneSubscriptions() {
    const getUrl = "http://localhost:8080/area/getSubscribedZone"
    const token = getCookie( "jwtToken" );

    fetch(getUrl, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        }
    })
        .then(function(response) {
            const zoneMessage = document.getElementById("zoneSubsError")

            if (response.status === 404){
                zoneMessage.style.display = "block"
                zoneMessage.innerHTML += "You have no machines entered. Start filling your catalog and allow others to rent them"
                console.log("no inserted car")
                return null
            }
            else if (response.status === 503){
                zoneMessage.style.display = "block"
                zoneMessage.innerHTML += "Service not available retry later"
                console.log("service not available")
                return null
            }
            else {
                return response.json();
            }

        })
        .then(function(response) {

            if (response != null && response.length > 0) {

                document.getElementById( 'zoneSlider' ).style.display = "block";
                const cardContainer = document.getElementById('carouselZone')


                console.log("ZONE RESPONSE: ",response)

                for(let x= 0; x < response.length; x++) {
                    const card = createCard(
                        String(response[x]["locationName"] + " " + String(response[x]["radius"]/1000) + " km"),
                        ["Zone id: " + response[x]["id"]],
                        ["deleteZoneSub/" + response[x]["id"]]
                    )
                    cardContainer.appendChild(card);

                }
            }



        }).catch(error => console.log(error));
}