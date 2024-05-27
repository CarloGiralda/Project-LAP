function fillForm() {
    var url = window.location.href;
    var path = url.split('/');
    var id = path[path.length - 1];

    var token = getCookie("jwtToken");
    var getUrl = "http://localhost:8080/carsearch/getCarById/" + id;

    fetch(getUrl, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        },
    }).then(function(response) {
        return response.json();
    }).then(async function (body) {
        // set general details for renting the car
        var td = document.getElementById("to");
        var fd = document.getElementById("from");
        console.log(new Date(body["offer"]["toDate"]))
        td.value = new Date(body["offer"]["toDate"]).toISOString().substring(0, 10);
        fd.value = new Date(body["offer"]["fromDate"]).toISOString().substring(0, 10);
        var price = document.getElementById("price");
        var zone = document.getElementById("zone");
        var platenum = document.getElementById("platenum");
        var model = document.getElementById("model");
        var year = document.getElementById("year");
        var pl = document.getElementById("PollutionLevel");
        var fuel = document.getElementById("Fuel");
        var brand = document.getElementById("Brand");
        var pass = document.getElementById("passengers");
        var clas = document.getElementById("classification");
        var cdn = document.getElementById("CarDoors");
        var trans = document.getElementById("Transmission");
        var engine = document.getElementById("Engine");

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

        price.value = body["offer"]["pricePerHour"];
        model.value = body["car"]["model"];
        zone.value = actualName;
        platenum.value = body["car"]["plateNum"];
        year.value = body["car"]["year"];
        pl.value = body["car"]["pollutionLevel"];
        fuel.value = body["car"]["fuel"];
        brand.value = body["car"]["brand"];
        pass.value = body["car"]["passengers"];
        clas.value = body["car"]["classification"];
        cdn.value = body["car"]["carDoorNumber"];
        trans.value = body["car"]["transmission"];
        engine.value = body["car"]["engine"];
        // set all other details
        var dis = document.getElementById("display");
        var and = document.getElementById("android");
        var app = document.getElementById("apple");
        var ac = document.getElementById("ac");
        var sas = document.getElementById("startAndStop");
        var ns = document.getElementById("navigationsystem");
        var pa = document.getElementById("parkingassistant");
        var bt = document.getElementById("bluetooth");
        var usb = document.getElementById("usb");
        var cd = document.getElementById("cd");
        var radio = document.getElementById("radio");
        var cc = document.getElementById("cruisecontrol");
        var pc = document.getElementById("parkingcamera");
        var sa = document.getElementById("surroundaudio");
        and.checked = body["utilities"]["android"];
        app.checked = body["utilities"]["apple"];
        dis.checked = body["utilities"]["display"];
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

        // set the description of the car
        var desc = document.getElementById("desc");
        desc.value = body["utilities"]["description"];
    })
}