function submitForm() {
            // Replace these URLs with the ones you want to use
            var postUrl = "http://localhost:8080/insertoffer";
            var successUrl = "http://localhost:8080/insertcar";
            var problemUrl = "http://localhost:8080/failed";

            // Get form data
            var from = document.getElementById("from").value;
            var to = document.getElementById("to").value;
            var price = document.getElementById("price").value;

            const params={
                          fromDate: from,
                          toDate: to,
                          pricePerDay: price
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