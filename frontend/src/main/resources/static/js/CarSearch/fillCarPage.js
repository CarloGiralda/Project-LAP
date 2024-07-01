var isAuction = false;

document.addEventListener('DOMContentLoaded', () => {
    const isAuthenticated = document.cookie.includes( 'jwtToken' )
    if (!isAuthenticated){
        window.location.href = "http://localhost:8081/login"
    }
})

function computeStars(body) {
    var nor = body.length;
    var sum = 0;
    for (let i = 0; i < nor; i++) {
        sum += body[i]["stars"];
    }
    var res = sum / nor;
    if (isNaN(res)) {
        return 0;
    }
    return Math.floor(res);
}

function fillPage() {
    showOverlay()

    // take the id of the car from the url
    var url = window.location.href;
    var path = url.split('/');
    var id = path[path.length - 1];

    var token = getCookie("jwtToken");
    var getUrl = "http://localhost:8080/carsearch/getCarById/" + id;
    var getUrlRating = "http://localhost:8080/carrating/" + id;

    var username = sessionStorage.getItem("username");
    var receiver;

    fetch(getUrlRating, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        },
    })
        .then(function(response) {
            // NOT_FOUND means no value found
            if (response.status === 404) {
                return [];
            }
            // SERVICE_NOT_AVAILABLE
            if (response.status === 503) {
                return null;
            }
            return response.json();
        })
        .then(function(bodys) {

            const noReviews = document.getElementById("noReviews");
            if (bodys === null) {
                noReviews.value = "Unable to load ratings for this car, retry later";
                noReviews.style.display = "block"
            } else {
                const revAvg = document.getElementById("revAvg")
                const revNum = document.getElementById("revNum")

                revAvg.style.display = "block"
                revNum.style.display = "block"

                // set the evaluation of the car
                revAvg.innerHTML += computeStars(bodys).toString()  + "/5";
                revNum.innerHTML += bodys.length
            }

            fetch(getUrl, {
                method: 'GET',
                headers: {
                    'Authorization': "Bearer " + token
                },
            }).then(function(response) {
                return response.json();
            }).then(async function (body) {
                console.log(body);

                var pricePerHour = Number(body["offer"]["pricePerHour"]);
                if (pricePerHour === -1) {
                    isAuction = true;
                }

                if (body["car"]["image"] !== null) {
                    const blob = new Blob([new Uint8Array(body["car"]["image"])], { type: 'image/jpeg' });
                    const image = document.getElementById("image");
                    image.src = URL.createObjectURL( blob )
                    console.log("car image not null")
                }

                const rentButton = document.getElementById("rent");
                const nowAv = document.getElementById("nowAvailable")

                if (isAuction) {
                    document.getElementById("notifyButton").style.display = "none";
                    rentButton.style.display = "none";
                    nowAv.textContent = "No"
                } else if (!body["offer"]["available"]) {
                    rentButton.disabled = true
                    nowAv.textContent = "No"
                } else {
                    nowAv.textContent = "Yes"
                }
                // set the name of the car
                var brandmodel = document.getElementById("brandmodel");
                brandmodel.innerHTML = body["car"]["brand"] + " " + body["car"]["model"];
                // set the description of the car
                var desc = document.getElementById("desc");
                desc.innerHTML = body["utilities"]["description"];
                // set zone of car
                var zone = document.getElementById("zone");
                // convert coordinates into name
                var getUrl = "http://localhost:8080/area/searchName?query=" + body["offer"]["zoneLocation"];
                let actualName;

                await fetch(getUrl, {
                    method: 'GET',
                    headers: {
                        'Authorization': "Bearer " + token
                    },
                })
                    .then(function (response) {
                        return response.json();
                    })
                    .then(function (body2) {
                        actualName = body2["city"];
                    });
                zone.innerHTML += actualName;
                // set the dates for renting the car
                var td = document.getElementById("td");
                var fd = document.getElementById("fd");
                var todate = new Date(body["offer"]["toDate"]);
                var fromdate = new Date(body["offer"]["fromDate"]);
                td.innerHTML += todate;
                fd.innerHTML += fromdate;
                // set the rent price of the car
                var price = document.getElementById("price");
                if (isAuction) {
                    price.innerHTML += "Car reserved for auction";
                } else {
                    price.innerHTML += pricePerHour + " coins";
                }
                // set renter name
                var renter = document.getElementById("renter");
                renter.innerHTML += body["offer"]["renterUsername"];
                // set all other details
                var year = document.getElementById("year");
                var pl = document.getElementById("pl");
                var fuel = document.getElementById("fuel");
                var pass = document.getElementById("pass");
                var clas = document.getElementById("classification");
                var cdn = document.getElementById("cdn");
                var trans = document.getElementById("trans");
                var engine = document.getElementById("engine");

                var dis = document.getElementById("display");
                dis.disabled = true;
                var and = document.getElementById("android");
                and.disabled = true;
                var app = document.getElementById("apple");
                app.disabled = true;
                var ac = document.getElementById("ac");
                ac.disabled = true;
                var sas = document.getElementById("startAndStop");
                sas.disabled = true;
                var ns = document.getElementById("navigationsystem");
                ns.disabled = true;
                var pa = document.getElementById("parkingassistant");
                pa.disabled = true;
                var bt = document.getElementById("bluetooth");
                bt.disabled = true;
                var usb = document.getElementById("usb");
                usb.disabled = true;
                var cd = document.getElementById("cd");
                cd.disabled = true;
                var radio = document.getElementById("radio");
                radio.disabled = true;
                var cc = document.getElementById("cruisecontrol");
                cc.disabled = true;
                var pc = document.getElementById("parkingcamera");
                pc.disabled = true;
                var sa = document.getElementById("surroundaudio");
                sa.disabled = true;

                year.innerHTML += body["car"]["year"];
                pl.innerHTML += body["car"]["pollutionLevel"];
                fuel.innerHTML += body["car"]["fuel"];
                pass.innerHTML += body["car"]["passengers"];
                clas.innerHTML += body["car"]["classification"];
                cdn.innerHTML += body["car"]["carDoorNumber"];
                trans.innerHTML += body["car"]["transmission"];
                engine.innerHTML += body["car"]["engine"];

                dis.checked = body["utilities"]["display"];
                and.checked = body["utilities"]["android"];
                app.checked = body["utilities"]["android"];
                ac.checked = body["utilities"]["airConditioning"];
                sas.checked = body["utilities"]["startAndStop"];
                ns.checked = body["utilities"]["navigationSystem"];
                pa.checked = body["utilities"]["parkingAssistant"];
                bt.checked = body["utilities"]["bluetooth"];
                usb.checked = body["utilities"]["usbPorts"];
                cd.checked = body["utilities"]["cdPlayer"];
                radio.checked = body["utilities"]["radioAMFM"];
                cc.checked = body["utilities"]["cruiseControl"];
                pc.checked = body["utilities"]["parkingCamera"];
                sa.checked = body["utilities"]["surroundAudio"];

                receiver = body["offer"]["renterUsername"];
            })

            var rent = document.getElementById("rent")
            var chat = document.getElementById("chat")
            rent.addEventListener('click', function changePage(event) {
                window.location.href = "http://localhost:8081/book/" + id;
            })
            chat.addEventListener('click', function changePage(event) {
                window.location.href = "http://localhost:8081/chat/m?sender=" + username + "&receiver=" + receiver;
            })

            // populate modal window
            const modal = document.getElementById("modalBodyReviews");
            const nor = bodys.length;
            for (let i = 0; i < nor; i++) {
                const table = document.createElement("table");
                const tbody = document.createElement("tbody");

                const row_one = document.createElement("tr");
                const row_two = document.createElement("tr");
                const row_three = document.createElement("tr");

                /*const user = document.createTextNode(String(bodys[i]["madeByUser"]));
                row_one.appendChild(user);*/

                const stars_date = document.createTextNode(String(bodys[i]["stars"]) + "★     " + String(bodys[i]["date"]));
                row_two.appendChild(stars_date);

                const desc = document.createTextNode(String(bodys[i]["description"]));
                row_three.appendChild(desc);

                tbody.appendChild(row_one);
                tbody.appendChild(row_two);
                tbody.appendChild(row_three);
                table.appendChild(tbody);
                modal.appendChild(table);
                const hr = document.createElement("hr");
                modal.appendChild(hr);
            }
            hideOverlay()
        })
}




function showOverlay() {
    let overlay = document.getElementById("overlayLoader");
    overlay.classList.remove("d-none");
}

function hideOverlay() {
    let overlay = document.getElementById("overlayLoader");
    overlay.classList.add("d-none");
}