function submitForm() {
            // Replace these URLs with the ones you want to use
            var postUrl = "http://localhost:8080/insertutilities";
            var successUrl = "http://localhost:8080/insertSuccess";
            var problemUrl = "http://localhost:8080/failed";

            // Get form data
            var andr = document.getElementById("android").value;
            var apple = document.getElementById("apple").value;
            var assistant;
            if (andr == "Android") {
                as = "Android";
            } else {
                as = "Apple";
            }
            var sst = document.getElementById("startAndStop").checked;
            var nav = document.getElementById("navigationsystem").checked;
            var parkA = document.getElementById("parkingassistant").checked;
            var parkC = document.getElementById("parkingcamera").checked;
            var cc = document.getElementById("cruisecontrol").checked;
            var usb = document.getElementById("usb").checked;
            var su = document.getElementById("surroundaudio").checked;
            var bt = document.getElementById("bluetooth").checked;
            var radio = document.getElementById("radio").checked;
            var dis = document.getElementById("display").checked;
            var ac = document.getElementById("ac").checked;
            var cd = document.getElementById("cd").checked;
            var desc = document.getElementById("desc").value;
            const params={
                            assistant: as,
                            startAndStop: sst,
                            navigationSystem: nav,
                            parkingAssistant: parkA,
                            parkingCamera: parkC,
                            cruiseControl: cc,
                            USBPorts: usb,
                            SurroundAudio: su,
                            bluetooth: bt,
                            radioAMFM: radio,
                            display: dis,
                            airConditioning: ac,
                            CDPlayer: cd,
                            description: desc
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