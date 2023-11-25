function submitForm() {
            // Replace these URLs with the ones you want to use
            var postUrl = "http://localhost:8080/insertcar";
            var successUrl = "http://localhost:8080/insertutilities";
            var problemUrl = "http://localhost:8080/failed";

            // Get form data
            var platenum = document.getElementById("platenum").value;
            var model = document.getElementById("model").value;
            var year = document.getElementById("year").value;
            var pl = document.getElementById("PollutionLevel").value;
            var fuel = document.getElementById("Fuel").value;
            var brand = document.getElementById("Brand").value;
            var pass = document.getElementById("Passenger").value;
            var size = document.getElementById("Size").value;
            var carD = document.getElementById("CarDoors").value;
            var pos = document.getElementById("pos").value;
            var ins = document.getElementById("ins").value;
            var tra = document.getElementById("Transmission").value;
            var engine = document.getElementById("Engine").value;

            const params = {
                plateNum: platenum,
                model: model,
                year: year,
                pollutionLevel: pl,
                fuel: fuel,
                brand: brand,
                passengers: pass,
                size: size,
                carDoorNumber: carD,
                position: pos,
                insurance: ins,
                transmission: tra,
                engine: engine
            };

            // Create a new XMLHttpRequest object
            var xhr = new XMLHttpRequest();

            // Set up the request
            xhr.open("POST", postUrl, true);

            xhr.setRequestHeader('Content-type', 'application/json')
            //xhr.setRequestHeader('Access-Control-Allow-Origin',"*")

            // Define what happens on successful data submission
            xhr.onload = function () {
                if (200 === 200) {
                    // Redirect to the success page
                    window.location.href = successUrl;
                }else{
                    window.location.href= problemUrl;
                }
            };

            // Send the form data
            xhr.send(JSON.stringify(params)); // Make sure to stringify
}