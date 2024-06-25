let notificationWSUrl = 'http://localhost:8080/sockjs?user='
let notificationCount = 0

const notificationDropDown = document.getElementById('NotificationList')
const notificationBell = document.getElementById('notificationBell')
const bellLink = document.getElementById("bellLink")

// Reset notification count when user click on it
bellLink.addEventListener("click", function () {

    if (notificationCount > 0) {
        notificationCount = 0
        notificationBell.textContent = ""
        const noNotificationItem = document.getElementById('0')

        if (noNotificationItem){
            notificationDropDown.removeChild(noNotificationItem)
        }
    }
    const liElements = notificationDropDown.getElementsByTagName('li').length
    if (liElements === 0) {
        addLiItem("No notification","#","NO_NOTIFICATION")
    }
})

document.addEventListener("DOMContentLoaded", function () {
    // Check if the user is authenticated based on the presence of the HttpOnly cookie
    const isAuthenticated = document.cookie.includes('jwtToken')
    const signupLink = document.getElementById('signupLink')
    const loginLink = document.getElementById('loginLink')
    const searchLink = document.getElementById('search')
    const auctionLink = document.getElementById('auction')
    const usernameDisplay = document.getElementById('usernameDisplay')
    const userDropDown = document.getElementById('userDropDown')
    const chatLink = document.getElementById('chatLink')
    const insert = document.getElementById('carInsertion')
    const bell = document.getElementById('bellLink')
    const carouselItems = document.getElementById("itemsCarousels")
    const carousel = document.getElementById("myCarousel")
    const servicesDescription = document.getElementById("featured-3")

    console.log("user is authenticated:", isAuthenticated)

    if (isAuthenticated && sessionStorage.getItem("username")) {
        // read name from session storage and show to the user
        usernameDisplay.textContent = sessionStorage.getItem("username")

        // hide home page and services description
        carousel.style.display = 'none'
        servicesDescription.style.display = 'none'

        // show user dropdown
        chatLink.style.display = 'block'
        insert.style.display = 'block'
        userDropDown.style.display = 'block'
        bell.style.display = 'block'
        searchLink.style.display = 'block'
        carouselItems.style.display = 'block'
        auctionLink.style.display = 'block'

        signupLink.style.display = 'none'
        loginLink.style.display = 'none'

        const token = getCookie("jwtToken");
        Promise.all([
            getSubscribedCars(token),
            getReservations(token),
            getInsertedCar(),
            getZoneSubscriptions(),
            new Promise(resolve => wsNotificationConnect(resolve))
        ]).then(() => {
            console.log("All asynchronous functions executed successfully.");
        }).catch(error => {
            console.error("Error in one of the asynchronous functions:", error);
        });
    } else {
        chatLink.style.display = 'none'
        insert.style.display = 'none'
        userDropDown.style.display = 'none'
        bell.style.display = 'none'
        carouselItems.style.display = 'none'
        auctionLink.style.display = 'none'

        signupLink.style.display = 'block'
        loginLink.style.display = 'block'
    }
});

function wsNotificationConnect() {
    const token = getCookie("jwtToken");

    const username = sessionStorage.getItem("username");
    const socket = new SockJS(notificationWSUrl + encodeURIComponent(username));
    const stompClient = Stomp.over(socket);

    // connect to the stomp endpoint
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        //updateNotificationDisplay();
        stompClient.subscribe('/user/topic/car-notification', function (message) {
            let response = JSON.parse(message.body)
            //console.log(response.carId, response.carName)
            showMessage(response);
        });
    });
}

function showMessage(message) {
    if (notificationDropDown && notificationBell) {
        notificationCount += 1
        notificationBell.textContent = notificationCount

        addLiItem(message.carName + " now available", message.carUrl)
    }
}

function addLiItem(content, link, type){
    const newLi = document.createElement('li');
    const newLink = document.createElement('a');

    newLink.className = 'dropdown-item bold-text'
    newLink.href = link

    if (type === 'NO_NOTIFICATION') {
        // first item is the only one with the id so that you can remove it
        newLi.id = '0'
    }

    const textElem = document.createElement('b')
    textElem.innerText = content

    newLink.appendChild(textElem)
    newLi.appendChild(newLink)
    notificationDropDown.appendChild(newLi)
}


document.getElementById("carouselSubscriptions").addEventListener("click", function(event) {
    if (event.target.tagName.toLowerCase() === 'a' && event.target.hasAttribute('data-index')) {

        const token = getCookie("jwtToken");

        // take car index
        const carId = event.target.getAttribute('data-index');
        console.log(carId);

        const carBookUrl =  "http://localhost:8080/subscription/deleteSub/" + carId;

        fetch(carBookUrl, {
            method: 'DELETE',
            headers: {
                'Authorization': "Bearer " + token
            },
        }).then(  response => {

            if (response.ok){
                // Show the success message
                const successMessage = document.getElementById('successSubDelete');
                successMessage.style.display = 'block';


                setTimeout(function () {
                    successMessage.style.display = 'none';
                }, 10000);

                // reload to delete the old card
                window.location.reload()

                console.log("Subscription removed")
            }
            else if (response.status === 404){

                const errorMessage = document.getElementById('errorSubDelete');
                errorMessage.style.display = 'block';

                setTimeout(function () {
                    errorMessage.style.display = 'none';
                    errorMessage.value = response.json()
                }, 10000);

                console.log("Cannot remove the car")

            } else if (response.status === 503){
                console.log("service not available for removing subscription")
            }

        }).catch(error => console.log(error));

    }
});

document.getElementById("carouselZone").addEventListener("click", function(event) {
    if (event.target.tagName.toLowerCase() === 'a' && event.target.hasAttribute('data-index')) {

        const token = getCookie("jwtToken");

        // take car index
        const zoneId = event.target.getAttribute('data-index');
        console.log("ZONE ID: ",zoneId);

        const zoneUrl =  "http://localhost:8080/area/deleteZoneSub/" + zoneId;

        fetch(zoneUrl, {
            method: 'DELETE',
            headers: {
                'Authorization': "Bearer " + token
            },
        }).then(  response => {

            if (response.ok){
                // Show the success message
                const successMessage = document.getElementById('successZoneSubDelete');
                successMessage.style.display = 'block';


                setTimeout(function () {
                    successMessage.style.display = 'none';
                }, 10000);

                // reload to delete the old card
                window.location.reload()

                console.log("Subscription removed")
            }
            else if (response.status === 404){

                const errorMessage = document.getElementById('errorZoneSubDelete');
                errorMessage.style.display = 'block';

                setTimeout(function () {
                    errorMessage.style.display = 'none';
                    errorMessage.value = response.json()
                }, 10000);

                console.log("Cannot remove the car")

            } else if (response.status === 503){
                console.log("service not available for removing subscription")
            }

        }).catch(error => console.log(error));
    }
});




