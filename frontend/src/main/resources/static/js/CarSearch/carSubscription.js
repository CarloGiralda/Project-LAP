document.addEventListener('DOMContentLoaded', () => {
    const notifyButton = document.getElementById( 'notifyButton' );
    const successModal = document.getElementById('successModal');
    const buttonModal = document.getElementById('buttonSuccessModal')
    const isAuthenticated = document.cookie.includes('jwtToken')


    if (!isAuthenticated){
        window.location.href = "http://localhost:8081/login"
    }


    notifyButton.addEventListener('click', function() {

        const url = window.location.href;
        const path = url.split( '/' );
        const id = path[path.length - 1];

        const token = getCookie( "jwtToken" );

        const subscriptionUrl = "http://localhost:8080/subscription/car/" + id;

        fetch( subscriptionUrl, {
            method: 'POST',
            headers: {
                'Authorization': "Bearer " + token
            },
        }).then( response => {
            if (!response.ok) {
                throw new Error( 'Network response was not ok' );
            }
            else {
                // Open the success modal
                buttonModal.click()
            }

        }).catch(error => {


            buttonModal.click()
            // set the error message and open the error modal
            document.getElementById("descriptionModalLabel").value = "Something went wrong, retry later!"
            console.error('Error:', error);
        });

    });


    // Assuming you have a modal close button with an ID 'closeSuccessModal' in your HTML
    const closeSuccessModal = document.getElementById('closeSuccessModal');

    // Close the modal when the close button is clicked
    closeSuccessModal.addEventListener('click', function () {
        successModal.style.display = 'none';
    });
})