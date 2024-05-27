
function submitForm() {
    var token = getCookie("jwtToken");

    // Replace these URLs with the ones you want to use
    var postUrl = "http://localhost:8080/carinsert/insertutilities";
    var successUrl = "http://localhost:8081/success";
    var problemUrl = "http://localhost:8081/failed";

    // Get form data
    var andr = document.getElementById("android").checked;
    var app = document.getElementById("apple").checked;
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
        android: andr,
        apple: app,
        startAndStop: sst,
        navigationSystem: nav,
        parkingAssistant: parkA,
        parkingCamera: parkC,
        cruiseControl: cc,
        usbPorts: usb,
        surroundAudio: su,
        bluetooth: bt,
        radioAMFM: radio,
        display: dis,
        airConditioning: ac,
        cdPlayer: cd,
        description: desc
    };

    var xhr = new XMLHttpRequest();

    xhr.open("POST", postUrl, true);

    xhr.setRequestHeader('Content-type', 'application/json')
    xhr.setRequestHeader('Authorization', "Bearer " + token)

    xhr.onload = function () {
        if (xhr.status === 200) {
            // Redirect to the success page
            window.location.href = successUrl;
        } else {
            window.location.href = problemUrl;
        }
    };

    xhr.send(JSON.stringify(params));
}