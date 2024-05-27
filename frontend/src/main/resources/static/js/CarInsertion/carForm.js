function submitForm() {
    var token = getCookie("jwtToken");

    // Replace these URLs with the ones you want to use
    var postUrl = "http://localhost:8080/carinsert/insertcar";
    var successUrl = "http://localhost:8081/carinsert/insertutilities";
    var problemUrl = "http://localhost:8081/failed";

    var form = document.getElementById("myForm");
    var formData = new FormData(form);

    var xhr = new XMLHttpRequest();

    xhr.open("POST", postUrl, true);

    xhr.setRequestHeader('Authorization', "Bearer " + token)

    xhr.onload = function () {
        if (xhr.status === 200) {
            window.location.href = successUrl;
        } else {
            window.location.href = problemUrl;
        }
    };

    xhr.send(formData);
}