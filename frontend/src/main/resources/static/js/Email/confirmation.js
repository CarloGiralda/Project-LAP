function submitForm() {
    // Replace these URLs with the ones you want to use
    var postUrl = "http://localhost:8081/api/email/sendEmail";
    var successUrl = "http://localhost:8081/pages/?type=Email/attendConfirmingEmail";
    var problemUrl = "http://localhost:8081/pages/?type=Problem/problem";

    // Get form data
    var formData = document.getElementById("email").value;
    const params={
                    email: formData
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