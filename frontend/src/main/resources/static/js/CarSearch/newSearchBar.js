document.addEventListener('DOMContentLoaded', () => {

    const isAuthenticated = document.cookie.includes('jwtToken')
    if (!isAuthenticated){
        window.location.href = "http://localhost:8081/login"
    }

    document.getElementById('search-button').addEventListener("click", async () => {

        showOverlay()
        const token = getCookie( "jwtToken" );
        // Replace these URLs with the ones you want to use
        const postUrl = "http://localhost:8080/carsearch/searchbar";


        // Get form data
        // checkboxes (false value means that the value was not selected, so turn it to null)
        let dis = document.getElementById( "display" ).checked;
        if (dis === false) {
            dis = null;
        }
        let andr = document.getElementById( "android" ).checked;
        if (andr === false) {
            andr = null;
        }
        let app = document.getElementById( "apple" ).checked;
        if (app === false) {
            app = null;
        }
        let ac = document.getElementById( "airconditioning" ).checked;
        if (ac === false) {
            ac = null;
        }
        let sas = document.getElementById( "startandstop" ).checked;
        if (sas === false) {
            sas = null;
        }
        let ns = document.getElementById( "navigationsystem" ).checked;
        if (ns === false) {
            ns = null;
        }
        let pa = document.getElementById( "parkingassistant" ).checked;
        if (pa === false) {
            pa = null;
        }
        let bt = document.getElementById( "bluetooth" ).checked;
        if (bt === false) {
            bt = null;
        }
        let usb = document.getElementById( "usb" ).checked;
        if (usb === false) {
            usb = null;
        }
        let cd = document.getElementById( "cd" ).checked;
        if (cd === false) {
            cd = null;
        }
        let radio = document.getElementById( "radio" ).checked;
        if (radio === false) {
            radio = null;
        }
        let cc = document.getElementById( "cruisecontrol" ).checked;
        if (cc === false) {
            cc = null;
        }
        let pc = document.getElementById( "parkingcamera" ).checked;
        if (pc === false) {
            pc = null;
        }
        let sa = document.getElementById( "surroundaudio" ).checked;
        if (sa === false) {
            sa = null;
        }
        let avail = document.getElementById( "available" ).checked;
        if (avail === false) {
            avail = null;
        }
        // multi-option (if the empty value is selected, it means that it was not selected at all, so set it to null)
        let pl = document.getElementById( "pollutionlevel" ).value;
        if (pl === "empty") {
            pl = null;
        }
        let fuel = document.getElementById( "fuel" ).value;
        if (fuel === "empty") {
            fuel = null;
        }
        let trans = document.getElementById( "transmission" ).value;
        if (trans === "empty") {
            trans = null;
        }
        let eng = document.getElementById( "engine" ).value;
        if (eng === "empty") {
            eng = null;
        }
        let clas = document.getElementById( "classification" ).value;
        if (clas === "empty") {
            clas = null;
        }
        // set the brand and the model to the search value (the split is done by the microservice)
        const brand = document.getElementById( 'search-input' ).value;
        const mod = document.getElementById( 'search-input' ).value;
        // set to null if they are empty strings (it means that their value was not set)
        let year = document.getElementById( "year" ).value;
        if (year === '') {
            year = null;
        }
        let pass = document.getElementById( "passengers" ).value;
        if (pass === '') {
            pass = null;
        }
        let cdn = document.getElementById( "cardoornumber" ).value;
        if (cdn === '') {
            cdn = null;
        }
        let pph = document.getElementById( "priceperhour" ).value;
        if (pph === '') {
            pph = null;
        }

        // conversion from date to epoch
        const fromdate = document.getElementById( "fromdate" ).value;
        let fd = new Date( fromdate );
        fd = fd.getTime();

        // conversion from date to epoch
        const todate = document.getElementById( "todate" ).value;
        let td = new Date( todate );
        td = td.getTime();

        // zone section
        let zone;
        if (document.getElementById("zone").value === '') {
            zone = null;
        } else {
            zone = document.getElementById("zone").value;

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
                    zone = String(body["lat"]) + "/" + String(body["lon"]);
                });
        }

        const params = {
            car: {
                year: year,
                pollutionLevel: pl,
                fuel: fuel,
                brand: brand,
                passengers: pass,
                classification: clas,
                carDoorNumber: cdn,
                transmission: trans,
                engine: eng,
                model: mod
            },
            offer: {
                available: avail,
                fromDate: fd,
                toDate: td,
                pricePerHour: pph,
                zoneLocation: zone
            },
            utilities: {
                display: dis,
                android: andr,
                apple: app,
                airConditioning: ac,
                startAndStop: sas,
                navigationSystem: ns,
                parkingAssistant: pa,
                bluetooth: bt,
                usbPorts: usb,
                cdPlayer: cd,
                radioAMFM: radio,
                cruiseControl: cc,
                parkingCamera: pc,
                surroundAudio: sa
            }
        };

        fetch(postUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(params)
        })
            .then(function (response) {
                return response.json();
            })
            .then(function (body) {
                clearButtons()
                addButtons(body)
                hideOverlay()
            })
    });
})

function addButtons(body) {
    const rowsAdded = body.length;
    const cardContainer = document.getElementById( "list" );

    for (let x = 0; x < rowsAdded; x++) {
        const card = document.createElement( "div" );
        card.className = "card p-2 mb-3";

        const cardBody = document.createElement( "div" );
        cardBody.className = "card-body";

        const cardTitle = document.createElement( "h5" );
        cardTitle.className = "card-title";
        cardTitle.textContent = body[x]["brand"] + " " + body[x]["model"];

        const cardText = document.createElement( "p" );
        cardText.className = "card-text";
        cardText.textContent = body[x]["pricePerHour"] + " coins/hour";

        const cardImage = document.createElement("img")
        if (body[x]["image"] != null){
            // convert the byte array to blob
            const blob = new Blob([new Uint8Array(body[x]["image"])], { type: 'image/jpeg' });

            cardImage.className = "card-img-top"
            cardImage.alt = "car image"
            cardImage.src = URL.createObjectURL( blob )

        }


        cardBody.appendChild(cardTitle);
        cardBody.appendChild(cardText);

        const btn = document.createElement( "button" );
        btn.setAttribute("id", "btn_" + x);
        btn.setAttribute("class", "btn btn-danger btn-block m-3");
        btn.textContent = "View Details";

        // Set custom data attribute to store the index 'x'
        btn.customField = body[x]["cid"];

        btn.addEventListener('click', function changePage(event) {
            window.location.href = "http://localhost:8081/carsearch/" + event.currentTarget.customField;
        }, false);

        card.appendChild(cardImage)
        card.appendChild(cardBody);
        card.appendChild(btn);
        cardContainer.append(card)
    }


}

function clearButtons() {
    var cont = document.getElementById("list");
    cont.innerHTML = "";
}

function showOverlay() {
    let overlay = document.getElementById("overlay");
    overlay.classList.remove("d-none");
}

function hideOverlay() {
    let overlay = document.getElementById("overlay");
    overlay.classList.add("d-none");
}
