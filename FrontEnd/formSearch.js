function submitForm() {
            // Replace these URLs with the ones you want to use
            var postUrl = "http://localhost:8080/carsearch/searchbar";
            var successUrl = "http://localhost:9000/carsearch/listcars";
            var problemUrl = "http://localhost:8080/failed";

            // Get form data
            var assis = document.getElementById("assistant").value;
            var ac = document.getElementById("airconditioning").value;
            var sas = document.getElementById("startandstop").value;
            var ns = document.getElementById("navigationsystem").value;
            var pa = document.getElementById("parkingassistant").value;
            var bt = document.getElementById("bluetooth").value;
            var usb = document.getElementById("usb").value;
            var cd = document.getElementById("cd").value;
            var radio = document.getElementById("radio").value;
            var cc = document.getElementById("cruisecontrol").value;
            var pc = document.getElementById("parkingcamera").value;
            var sa = document.getElementById("surroundaudio").value;
            var year = document.getElementById("year").value;
            var pl = document.getElementById("pollutionlevel").value;
            var fuel = document.getElementById("fuel").value;
            var brand = document.getElementById("brand").value;
            var pass = document.getElementById("passengers").value;
            var size = document.getElementById("size").value;
            var cdn = document.getElementById("cardoornumber").value;
            var pos = document.getElementById("position").value;
            var trans = document.getElementById("transmission").value;
            var eng = document.getElementById("engine").value;
            var mod = document.getElementById("model").value;
            var avail = document.getElementById("available").value;
            var fd = document.getElementById("fromdate").value;
            var td = document.getElementById("todate").value;
            var ppd = document.getElementById("priceperday").value;

            const params={
                    car: {
                          year: year,
                          pollutionLevel: pl,
                          fuel: fuel,
                          brand: brand,
                          passengers: pass,
                          size: size,
                          carDoorNumber: cdn,
                          position: pos,
                          transmission: trans,
                          engine: eng,
                          model: mod
                    },
                    offer: {
                        available: avail,
                        fromDate: fd,
                        toDate: td,
                        pricePerDay:ppd
                    },
                    utilities: {
                        assistant: assis,
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
                        surroundAudio: sa
                    }
            };

            fetch(postUrl, {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/json',
                    'Origin': 'http://localhost:9000/carsearch/searchbar'
                },
                body: JSON.stringify(params)
                })
            .then(response => response.json())
            .then(function(json) {
                sessionStorage.setItem(json, "response");
            })
            .then(successUrl => {
                window.location.href = successUrl
            });
}