async function updateCarDetails() {
    var token = getCookie("jwtToken");
    // take the id of the car from the url
    var url = window.location.href;
    var path = url.split('/');
    var id = path[path.length - 1];

    var postUrl = "http://localhost:8080/carinsert/modify/" + id;
/*
    var successUrl = "http://localhost:8081/success";
    var problemUrl = "http://localhost:8081/failed";
*/

    var from = document.getElementById('from').value;
    var fd;
    if (from === "") {
        fd = null;
    } else {
        // conversion from date to epoch
        fd = new Date(from);
        fd = fd.getTime();
    }
    var to = document.getElementById('to').value;
    // conversion from date to epoch
    var td;
    if (to === "") {
        td = null;
    } else {
        td = new Date(to);
        td = td.getTime();
    }
    var pph = document.getElementById('price').value;
    if (pph === "") {
        pph = null;
    }
    var zone = document.getElementById('zone').value;
    if (zone === "") {
        zone = null;
    } else {
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
    var platenum = document.getElementById('platenum').value;
    if (platenum === "") {
        platenum = null;
    }
    var model = document.getElementById('model').value;
    if (model === "") {
        model = null;
    }
    var year = document.getElementById('year').value;
    if (year === "") {
        year = null;
    }
    var pl = document.getElementById('PollutionLevel').value;
    if (pl === "") {
        pl = null;
    }
    var fuel = document.getElementById('Fuel').value;
    if (fuel === "") {
        fuel = null;
    }
    var brand = document.getElementById('Brand').value;
    if (brand === "") {
        brand = null;
    }
    var passengers = document.getElementById('passengers').value;
    if (passengers === "") {
        passengers = null;
    }
    var classification = document.getElementById('classification').value;
    if (classification === "") {
        classification = null;
    }
    var carDoors = document.getElementById('CarDoors').value;
    if (carDoors === "") {
        carDoors = null;
    }
    var transmission = document.getElementById('Transmission').value;
    if (transmission === "") {
        transmission = null;
    }
    var engine = document.getElementById('Engine').value;
    if (engine === "") {
        engine = null;
    }
    var andr = document.getElementById('android').checked;
    var app = document.getElementById('apple').checked;
    var sas = document.getElementById('startAndStop').checked;
    var ns = document.getElementById('navigationsystem').checked;
    var pa = document.getElementById('parkingassistant').checked;
    var usb = document.getElementById('usb').checked;
    var cc = document.getElementById('cruisecontrol').checked;
    var pc = document.getElementById('parkingcamera').checked;
    var sa = document.getElementById('surroundaudio').checked;
    var bt = document.getElementById('bluetooth').checked;
    var radio = document.getElementById('radio').checked;
    var dis = document.getElementById('display').checked;
    var ac = document.getElementById('ac').checked;
    var cd = document.getElementById('cd').checked;
    var desc = document.getElementById('desc').value;

    const params = {
        car: {
            plateNum: platenum,
            year: year,
            pollutionLevel: pl,
            fuel: fuel,
            brand: brand,
            passengers: passengers,
            model: model,
            classification: classification,
            carDoorNumber: carDoors,
            transmission: transmission,
            engine: engine,
        },
        offer: {
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
            USBPorts: usb,
            CDPlayer: cd,
            radioAMFM: radio,
            cruiseControl: cc,
            parkingCamera: pc,
            surroundAudio: sa,
            description: desc
        }
    };

    var xhr = new XMLHttpRequest();

    xhr.open("POST", postUrl, true);

    xhr.setRequestHeader('Content-type', 'application/json')
    xhr.setRequestHeader('Authorization', "Bearer " + token)

    xhr.onload = function () {
        if (xhr.status === 200) {
            // Redirect to the success page
            showSuccessMessage();
            //window.location.href = successUrl;
        } else {
            showErrorMessage(xhr.status)
            //window.location.href = problemUrl;
        }
    };

    xhr.send(JSON.stringify(params));
}

function showSuccessMessage() {
    let successMessage = document.querySelector(".alert-info");
    let errorMessage = document.querySelector(".alert-danger");
    //hideOverlay(); // Hide the overlay when the registration is successful
    successMessage.style.display = "block";
    errorMessage.style.display = "none";
}

function showErrorMessage(message) {
    let successMessage = document.querySelector(".alert-info");
    let errorMessage = document.querySelector(".alert-danger");
    //hideOverlay(); // Hide the overlay when an error occurs
    successMessage.style.display = "none";
    errorMessage.style.display = "block";
    errorMessage.textContent = message;
}