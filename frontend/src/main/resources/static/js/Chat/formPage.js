// Function to perform the GET request
document.addEventListener("DOMContentLoaded", function () {
    // Check if the user is authenticated based on the presence of the HttpOnly cookie
    const isAuthenticated = document.cookie.includes('jwtToken')
    const usernameDisplay = document.getElementById('usernameDisplay')
    const userDropDown = document.getElementById('userDropDown')

    console.log("user is authenticated:", isAuthenticated)

    if (isAuthenticated && usernameDisplay) {

        // read name from session storage and show to the user
        usernameDisplay.textContent = sessionStorage.getItem("username")

        // show user dropdown
        userDropDown.style.display = 'block'
    }

});

  function performGetRequest(receiverValue) {
      var senderValue = document.querySelector('#sender').textContent;
      console.log(receiverValue);
      window.location.assign('/chat/m?sender='+senderValue+'&receiver='+receiverValue);
  }
