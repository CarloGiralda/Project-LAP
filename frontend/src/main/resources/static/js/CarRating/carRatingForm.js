document.getElementById('ratingForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent the default form submission

    // Here you can add logic for form submission and checking if something went wrong
    const car_id = document.getElementById("ratingCid").value

    // Replace these URLs with the ones you want to use
    const postUrl = "http://localhost:8080/carrating/create";

    // Get form data
    const ev = sessionStorage.getItem( "rating" ).replace( /"/gi, '' );
    const token = getCookie( "jwtToken" );
    const date = document.getElementById( "date" ).value;
    const desc = document.getElementById( "desc" ).value;

    const params={
        stars: ev,
        date: date,
        description: desc,
        madeByUser: token,
        ratingOnCar: car_id
    };

    var xhr = new XMLHttpRequest();

    xhr.open("POST", postUrl, true);

    xhr.setRequestHeader('Content-type', 'application/json')
    xhr.setRequestHeader('Authorization', "Bearer " + token)

    xhr.onload = function () {
        if (xhr.status === 200) {
            // Show the success message
            const successMessage = document.getElementById('successMessageRating');
            successMessage.style.display = 'block';


            setTimeout(function () {
                successMessage.style.display = 'none';
            }, 10000);
        }
        else if (xhr.status === 409){
            const errorMessage = document.getElementById('errorMessageRating');
            errorMessage.style.display = 'block';
            errorMessage.textContent = 'You have already reviewed this rental!'
            setTimeout(function () {
                errorMessage.style.display = 'none';
            }, 10000);

        }
        else {

            const errorMessage = document.getElementById('errorMessageRating');
            errorMessage.style.display = 'block';

            errorMessage.textContent = 'Oops! Something went wrong. Please try again.'
            setTimeout(function () {
                errorMessage.style.display = 'none';
            }, 10000);
        }
    };

    xhr.send(JSON.stringify(params));

});



