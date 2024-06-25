let notificationWSUrl = 'http://localhost:8080/sockjs?user='
let notificationCount = 0

document.addEventListener("DOMContentLoaded", function () {
    // Check if the user is authenticated based on the presence of the HttpOnly cookie
    const isAuthenticated = document.cookie.includes('jwtToken')
    const signupLink = document.getElementById('signupLink')
    const loginLink = document.getElementById('loginLink')
    const usernameDisplay = document.getElementById('usernameDisplay')
    const userDropDown = document.getElementById('userDropDown')
    const chatLink = document.getElementById('chatLink')
    const insert = document.getElementById('carInsertion')
    const bell = document.getElementById('bellLink')
    const notificationDropDown = document.getElementById('NotificationList')
    const notificationBell = document.getElementById('notificationBell')
    const auctionLink = document.getElementById('auction')
    const searchLink = document.getElementById('search')

    console.log("user is authenticated:", isAuthenticated)

    if (isAuthenticated) {

        // read name from session storage and show to the user
        usernameDisplay.textContent = sessionStorage.getItem("username")

        // show user dropdown
        chatLink.style.display = 'block'
        insert.style.display = 'block'
        userDropDown.style.display = 'block'
        bell.style.display = 'block'
        auctionLink.style.display = 'block'
        searchLink.style.display = 'block'

        signupLink.style.display = 'none'
        loginLink.style.display = 'none'

        // you can now start a ws connection for notification
        wsNotificationConnect()

    } else {

        chatLink.style.display = 'none'
        insert.style.display = 'none'
        userDropDown.style.display = 'none'
        bell.style.display = 'none'
        auctionLink.style.display = 'none'
        searchLink.style.display = 'none'

        signupLink.style.display = 'block'
        loginLink.style.display = 'block'
    }

    // Reset notification count when user click on it
    bell.addEventListener("click", function () {

        if (notificationCount > 0) {
            notificationCount = 0
            notificationBell.textContent = ""
            const noNotificationItem = document.getElementById('0')

            if (noNotificationItem){
                notificationDropDown.removeChild(noNotificationItem)
            }

        }
        const liElements = notificationDropDown.getElementsByTagName('li').length
        if(liElements === 0){
            addLiItem("No notification","#","NO_NOTIFICATION")
        }
    })

    function wsNotificationConnect() {

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
});




