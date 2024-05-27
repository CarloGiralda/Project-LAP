function chatRequest() {
    var token = getCookie("jwtToken")
    var username = sessionStorage.getItem("username")
    var url = "http://localhost:8080/chat?sender=" + username;

    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        }
    }).then(function(response) {
        return response.json();
    }).then(function(body) {
            console.log(body.length)
            for (let x = 0; x < body.length; x++) {
                sessionStorage.setItem("Receiver: " + x, body[x]["receiver"]);
            }
            sessionStorage.setItem("NumberOfReceivers", body.length)
            addChats()
    })
}

function addChats() {
    var rowsAdded = sessionStorage.getItem("NumberOfReceivers");
    var username = sessionStorage.getItem("username")
    var cont = document.getElementById("list");

    for(var x= 0; x < rowsAdded; x++) {
        var receiver = sessionStorage.getItem("Receiver: " + x)

        const link = "http://localhost:8081/chat/m?sender=" + username + "&receiver=" + receiver
        const newLi = document.createElement('li');
        const newLink = document.createElement('a');

        newLink.href = link

        const textElem = document.createElement('b')
        textElem.innerText = receiver

        newLink.appendChild(textElem)
        newLi.appendChild(newLink)
        cont.appendChild(newLi)
    }
}