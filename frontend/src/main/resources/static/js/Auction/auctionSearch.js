function onLoad() {
    var token = getCookie("jwtToken");
    var serverUrl = 'http://localhost:8080/auction'; // Set your server URL here

    fetch(`${serverUrl}/search`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': "Bearer " + token
        },
    })
        .then(function (response) {
            return response.json();
        })
        .then(body => {
            clearButtons()
            addButtons()
        })
        .catch(error => {
            console.error('Error fetching peer IDs:', error);
            document.getElementById('status').textContent = 'Failed to fetch PeersIds...';
        });
}

function addButtons(body) {
    const rowsAdded = body.length;
    const cardContainer = document.getElementById( "list" );

    for (let x = 0; x < rowsAdded; x++) {
        const card = document.createElement( "div" );
        card.className = "card p-2 mb-3";

        const cardBody = document.createElement( "div" );
        cardBody.className = "card-body";

        const cardTitle = document.createElement( "h5" );
        cardTitle.className = "card-title";
        cardTitle.textContent = body[x]["auctionId"];

        const cardText = document.createElement( "p" );
        cardText.className = "card-text";
        cardText.textContent = "Date: " + body[x]["startDate"] + "\nCarId: " + body[x]["cid"];

        cardBody.appendChild(cardTitle);
        cardBody.appendChild(cardText);

        const btn = document.createElement( "button" );
        btn.setAttribute("id", "btn_" + x);
        btn.setAttribute("class", "btn btn-danger btn-block m-3");
        btn.textContent = "Subscribe";

        // Set custom data attribute to store the index 'x'
        btn.customField = body[x]["auctionId"];

        btn.addEventListener('click', function subscribe(event) {
            peer = new Peer();
            peer.on('open', function (id) {
                document.getElementById('status').textContent = 'Waiting for bid input...';
                subscribeToServer(peerId, event.currentTarget.customField);
                document.getElementById('subButton').style.display = 'none';
            });
            peer.on('close', function () {
                document.getElementById('status').textContent = 'Connection closed...';
            });
        }, false);

        card.appendChild(cardBody);
        card.appendChild(btn);
        cardContainer.append(card)
    }
}

function clearButtons() {
    var cont = document.getElementById("list");
    cont.innerHTML = "";
}

function subscribeToServer(peerId, auctionId) {
    fetch(serverUrl + '/subscribe', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': "Bearer " + token
        },
        body: JSON.stringify({
            peerId: peerId,
            auctionId: auctionId
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Subscription successful:', data);
            // Assuming data contains a timestamp or datetime string for the auction end time
            sessionStorage.setItem("timestamp", data);
            window.location.href = "http://localhost:8081/auction";
        })
        .catch(error => {
            console.error('Error subscribing to auction:', error);
            document.getElementById('status').textContent = 'Failed to subscribe...';
        });
}