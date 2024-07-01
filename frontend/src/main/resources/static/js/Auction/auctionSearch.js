// INIT
let peer = null;
let isReadyAuction = false;
let peersIds = null;
let auction;
let carId;
let myPeerId;
let bid;

const token = getCookie("jwtToken");
const serverUrl = 'http://localhost:8080/auction'; // Set your server URL here
const romeOffset = 2 * 60 * 60 * 1000; // Rome is UTC+2

document.addEventListener('DOMContentLoaded', () => {
    const serverUrl = 'http://localhost:8080/auction'; // Set your server URL here
    const searchUrl = serverUrl + "/search";

    document.getElementById('bidButton').addEventListener('click', () => {
        const bid = document.getElementById('bid').value; // fetch from user
        document.getElementById('yourValue').textContent = 'Your bid: ' + bid;
        document.getElementById('bidLabel').style.display = 'none'
        document.getElementById('bid').style.display = 'none';
        document.getElementById('bidButton').style.display = 'none';
        document.getElementById('status').textContent = 'Waiting for final result of auction...';
        const raftNode = new RaftNode(peer, myPeerId, peersIds, bid);
        console.log('RaftNode initialized:', raftNode);
    });

    fetch(searchUrl, {
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
            addButtons(body)
        })
        .catch(_ => {
            document.getElementById('status').textContent = 'Failed to fetch auctions...';
        });
})


function addButtons(body) {
    const rowsAdded = body.length;
    const cardContainer = document.getElementById( "list" );

    for (let x = 0; x < rowsAdded; x++) {
        const card = document.createElement( "div" );
        card.className = "card p-2 mb-3";

        // Create a div for positioning the image
        const imageWrapper = document.createElement("div");
        imageWrapper.className = "image-wrapper";

        const cardBody = document.createElement( "div" );
        cardBody.className = "card-body";

        const cardTitle = document.createElement( "h5" );
        cardTitle.className = "card-title";
        cardTitle.textContent = "Auction id: " + body[x]["auctionId"];

        const cardText = document.createElement( "p" );
        cardText.className = "card-text";
        cardText.textContent = "Date: " + body[x]["startDate"]
        const cardImage = document.createElement("img");
        cardImage.className = "card-img-top";
        cardImage.alt = "Car Image"; // Alternative text for the image

        fetch("http://localhost:8080/carsearch/getCarModelBrandImage/" + body[x]["cid"], {
            method: 'GET',
            headers: {
                'Authorization': "Bearer " + token
            },
        }).then(function (response) {
            return response.json();
        }).then(function (body) {
            if (body["image"] !== null) {
                const image = new Blob([new Uint8Array(body["image"])], {type: 'image/jpeg'});
                const name = body["brand"] + " model " + body["model"];

                cardText.textContent += "\nCar: " + name;
                cardImage.src = URL.createObjectURL(image); // Assuming 'imageUrl' is the key in 'body' object
            }
        })

        imageWrapper.appendChild(cardImage);

        cardBody.appendChild(cardTitle);
        cardBody.appendChild(cardText);
        cardBody.appendChild(imageWrapper);

        const btn = document.createElement( "button" );
        btn.setAttribute("id", "btn_" + x);
        btn.setAttribute("class", "btn btn-danger btn-block m-3");
        btn.textContent = "Subscribe";

        // Set custom data attribute to store the index 'x'
        btn.customField = body[x]["auctionId"];
        btn.customField2 = body[x]["cid"];

        btn.addEventListener('click', function subscribe(event) {
            auction = event.currentTarget.customField;
            carId = event.currentTarget.customField2;
            peer = new Peer();
            peer.on('open', function (peerId) {
                myPeerId = peerId
                subscribeToServer(peerId, auction);
                clearButtons()
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
            startTimer(data);
        });
}

function startTimer(endTimeToConvert) {
    const timerElement = document.getElementById( 'timer' );
    timerElement.style.display = 'flex';
    const endTime = new Date( endTimeToConvert );

    function formatTime(time) {
        const hours = Math.floor((time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((time % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((time % (1000 * 60)) / 1000);
        return `${hours}h: ${minutes}m: ${seconds}s`;
    }

    function updateTimer() {
        const now = new Date()
        const nowUTC = new Date(now.toISOString());
        const remainingTime = endTime - nowUTC - romeOffset

        if (remainingTime <= 0) {
            clearInterval(interval);
            timerElement.style.display = 'none';
            onTimerEnd();
        } else {
            timerElement.textContent = formatTime(remainingTime);
        }
    }

    // Initialize the timer display immediately
    updateTimer();

    // Update the timer every second
    const interval = setInterval(updateTimer, 1000);
}

function onTimerEnd() {
    fetchPeerIdsFromServer();
}

function fetchPeerIdsFromServer() {
    // Assuming serverUrl is defined elsewhere in your code
    fetch(`${serverUrl}/get_peer_list?auctionId=${auction}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': "Bearer " + token
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data)
            peersIds = data
            isReadyAuction = true
            document.getElementById('bidLabel').style.display = 'block'
            document.getElementById('bid').style.display = 'block'
            document.getElementById('bidButton').style.display = 'block'

        })
        .catch(error => {
            console.error('Error fetching peer IDs:', error);
            document.getElementById('status').textContent = 'Failed to fetch PeersIds...';
        });
}

// STRUCTURES

const states = {
    FOLLOWER: 'FOLLOWER',
    CANDIDATE: 'CANDIDATE',
    LEADER: 'LEADER'
};

const types = {
    REQUEST_VOTE: 'RequestVote',
    REQUEST_VOTE_ACK: 'RequestVoteAck',
    HEARTBEAT: 'Heartbeat',
    HEARTBEAT_ACK: 'HeartbeatAck',
    COMMIT: 'Commit',
    COMMIT_ACK: 'CommitAck',
};

class RaftNode {
    constructor(myPeer,myId, peersIds, bid) {
        this.myPeer = myPeer;
        this.id = myId;
        this.bid = bid; // original bid to check if you win the auction for simplicity the bid value is identifier of the user
        this.peersIds = peersIds; // peers ids fetched from microservice
        this.peers = {}; // stores the P2P connection
        this.currentTerm = 0; // --> not used here
        this.votedFor = null;
        this.committed = false;
        this.log = [bid];
        this.acks = 0;
        this.finalAcks = 0;
        this.state = states.FOLLOWER;
        this.votes = {};
        this.totalNodes = this.peersIds.length;
        this.connectToPeers();
        this.finalValue = null;
    }

    // CONNECTION LAYER

    connectToPeers() {
        for (const peer of this.peersIds) {
            if (peer !== this.id) {
                const conn = this.myPeer.connect(peer);
                conn.on('open', () => {
                    this.peers[peer] = conn;
                    console.log(`Connected to peer ${peer}`);
                });
                conn.on('data', (data) => {
                    // Handle incoming data from peer
                    console.log(`Received data from ${conn.peer}:`, data);
                    const parsedData = JSON.parse(data);

                    // Access the messageType and other fields
                    const messageType = parsedData.messageType;

                    // Implement Raft consensus algorithm based on message type
                    switch (messageType) {
                        case 'RequestVote':
                            this.onReceiveVote(parsedData);
                            break;
                        case 'RequestVoteAck':
                            this.onReceiveVoteResponse(parsedData);
                            break;
                        case 'Heartbeat':
                            this.onReceiveHeartbeat(parsedData);
                            break;
                        case 'HeartbeatAck':
                            this.onReceiveHeartbeatResponse(parsedData);
                            break;
                        case 'Commit':
                            this.onReceiveHeartbeatCommit(parsedData);
                            break;
                        case 'CommitAck':
                            this.onReceiveHeartbeatCommitResponse(parsedData);
                            break;
                        // Add more cases for other message types
                        default:
                            console.log('Unknown message type:', messageType);
                    }
                });
                conn.on('close', () => {
                    console.log(`Connection from peer ${conn.peer} closed`);
                    delete this.peers[conn.peer]; // Remove the connection from peers
                });
                conn.on('error', (err) => {
                    console.error(`Error with connection from peer ${conn.peer}:`, err);
                    delete this.peers[conn.peer]; // Remove the connection from peers
                });
            }
        }

        this.myPeer.on('connection', (conn) => {
            this.peers[conn.peer] = conn;
            conn.on('data', (data) => {
                // Handle incoming data from peer
                console.log(`Received data from ${conn.peer}:`, data);
                const parsedData = JSON.parse(data);

                // Access the messageType and other fields
                const messageType = parsedData.messageType;

                // Implement Raft consensus algorithm based on message type
                switch (messageType) {
                    case 'RequestVote':
                        this.onReceiveVote(parsedData);
                        break;
                    case 'RequestVoteAck':
                        this.onReceiveVoteResponse(parsedData);
                        break;
                    case 'Heartbeat':
                        this.onReceiveHeartbeat(parsedData);
                        break;
                    case 'HeartbeatAck':
                        this.onReceiveHeartbeatResponse(parsedData);
                        break;
                    case 'Commit':
                        this.onReceiveHeartbeatCommit(parsedData);
                        break;
                    case 'CommitAck':
                        this.onReceiveHeartbeatCommitResponse(parsedData);
                        break;
                    // Add more cases for other message types
                    default:
                        console.log('Unknown message type:', messageType);
                }
            });
            conn.on('close', () => {
                console.log(`Connection from peer ${conn.peer} closed`);
                delete this.peers[conn.peer]; // Remove the connection from peers
            });
            conn.on('error', (err) => {
                console.error(`Error with connection from peer ${conn.peer}:`, err);
                delete this.peers[conn.peer]; // Remove the connection from peers
            });
        });

        this.startInitTimeout();
    }


    send(peer, message) {
        // Send message to a peer
        if(peer !== this.id){
            const conn = this.peers[peer];
            conn.send(message);
        }
    }

    // INIT --> to sync sockets connections
    startInitTimeout() {
        const minDelay = 10000; // 10 seconds in milliseconds
        const maxDelay = 20000; // 15 seconds in milliseconds
        const randomDelay = Math.random() * (maxDelay - minDelay) + minDelay;

        setTimeout(() => {
            console.log('After 10 to 15 seconds');
            this.startElectionTimeout();
        }, randomDelay);
    }


    // ELECTION PART

    startElectionTimeout() {
        clearTimeout(this.electionTimer);
        this.electionTimer = setTimeout(() => {
            if (this.state === states.FOLLOWER) {
                this.becomeCandidate();
            }
        }, this.getRandomElectionTimeout());
    }

    stopElectionTimeout() {
        clearTimeout(this.electionTimer);
    }

    getRandomElectionTimeout() {
        return Math.random() * 150 + 150; // Randomize election timeout for each node
    }

    becomeCandidate() {
        this.state = states.CANDIDATE;
        this.currentTerm++;
        this.votedFor = this.id;
        this.startElectionTimeout();
        this.requestVotes();
    }

    becomeLeader() {
        this.state = states.LEADER;
        this.leader = this.id;
        this.stopElectionTimeout();
        this.startHeartbeat();
    }

    requestVotes() {
        this.votes[this.id] = 1; // vote for myself
        this.votedFor = this.id;
        for (const peer of this.peersIds) {
            const raftMessage = {
                sender: this.id,
                messageType: types.REQUEST_VOTE,
                term: this.currentTerm,
                entries: []
            };
            const jsonMessage = JSON.stringify(raftMessage);
            this.send(peer,jsonMessage)
        }
    }

    onReceiveVote(message) {
        const peer = message.sender;
        if (this.votedFor == null) {
            this.votedFor = peer;
            this.stopElectionTimeout();
            this.startElectionTimeout();
            const raftMessage = {
                sender: this.id,
                messageType: types.REQUEST_VOTE_ACK,
                entries: []
            };
            const jsonMessage = JSON.stringify(raftMessage);
            this.send(peer,jsonMessage);
        }
    }

    onReceiveVoteResponse() {
        this.votes[this.id]++;
        if (this.votes[this.id] > this.totalNodes / 2) {
            // Received the majority of votes, become leader
            this.becomeLeader();
        }
    }

    // LOG PART

    startHeartbeat() {
        if (this.committed === true){
            return true;
        }
        for (const peer of this.peersIds) {
            const raftMessage = {
                sender: this.id,
                messageType: types.HEARTBEAT,
                term: this.currentTerm,
                entries: this.log // here send all the log but can be sent only the min entry
            };
            const jsonMessage = JSON.stringify(raftMessage);
            this.send(peer,jsonMessage);
        }
        // Schedule the next heartbeat
        setTimeout(() => {
            this.startHeartbeat();
        }, 100);
    }

    onReceiveHeartbeat(message) {
        if (this.committed === true){
            return true;
        }
        const peer = message.sender;
        const entries = message.entries;
        this.leader = peer;
        this.log = [...new Set([...this.log,...entries])];
        if (this.state !== states.FOLLOWER) {
            // If not already a follower, become one because there is already a leader
            this.state = states.FOLLOWER;
            this.stopElectionTimeout();
            this.startElectionTimeout();
        }
        const raftMessage = {
            sender: this.id,
            messageType: types.HEARTBEAT_ACK,
            term: this.currentTerm,
            entries: this.log
        };
        const jsonMessage = JSON.stringify(raftMessage);
        this.send(peer,jsonMessage);
    }

    onReceiveHeartbeatResponse(message) {
        if (this.committed === true){
            return true;
        }
        const entries = message.entries;
        this.log = [...new Set([...this.log, ...entries])];
        this.acks++;
        if (this.acks > this.totalNodes / 2) {

            this.finalValue = Math.max(...this.log);

            for (const peer of this.peersIds) {
                // committed value
                const raftMessage = {
                    sender: this.id,
                    messageType: types.COMMIT,
                    term: this.currentTerm,
                    value: this.finalValue
                };

                const jsonMessage = JSON.stringify(raftMessage);
                this.send(peer,jsonMessage);
            }
        }
    }

    onReceiveHeartbeatCommit(message) {
        if (this.committed === true){
            return true;
        }
        const peer = message.sender;
        const value = message.value;

        // committed value
        const raftMessage = {
            sender: this.id,
            messageType: types.COMMIT_ACK
        };
        this.finalValue = value;
        const jsonMessage = JSON.stringify(raftMessage);
        this.send(peer,jsonMessage);
        this.commitFunction()
    }

    onReceiveHeartbeatCommitResponse() {
        this.finalAcks++;
        if (this.finalAcks > this.totalNodes / 2) {
            this.commitFunction()
        }
    }

    commitFunction() {
        this.committed = true;
        document.getElementById('value').style.display = 'block';
        if (Number(this.finalValue) === Number(this.bid)) {

            // 1 - delete auction from available ones
            this.deleteSubscription()

            // 2 - book a reservation
            this.bookCar()

            // 3 - send the payment
            const price = this.finalValue
            this.sendPayment(price)

            const message = 'You won the auction!\n' +
                'Congratulations!\n' +
                'Check your reservations for more details';
            this.showFinalMessage(true, message)
        } else {
            const message = "You lost the auction!"
            this.showFinalMessage(false, message)
        }
    }

    // TODO test error field and the value field set to 'none'

    deleteSubscription(){

        const serverUrl = 'http://localhost:8080/auction/delete_auction?auctionId=' + auction;

        fetch(serverUrl, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': "Bearer " + token
            },
        })
            .catch(function () {
                document.getElementById('error').textContent = "You won the auction, but something has gone wrong";
                // make the value field not visible
                // one way to be sure that, in case of an error, the successful payment feedback is not visible
                document.getElementById('value').style.display = 'none';
            })
    }

    bookCar(){

        const auctionRequest = {
            cid: carId // Replace with the actual `cid` value
        }

        fetch( "http://localhost:8080/reservation/bookForAuction", {
            method: 'POST',
            headers: {
                'Content-Type': "application/json",
                'Authorization': "Bearer " + token
            },
            body: JSON.stringify(auctionRequest)
        }).then(function (response) {
            return response.json();
        }).then(function (body) {
            // TODO body should be equal to the bid returned by the booking service
            bid = body;
        }).catch(function (error) {
            document.getElementById('error').textContent = "You won the auction, but the booking for the car has encountered some problems";
            // make the value field not visible
            // one way to be sure that, in case of an error, the successful payment feedback is not visible
            document.getElementById('value').style.display = 'none';
        })
    }

    sendPayment(price){
        const payer_username = sessionStorage.getItem("username");
        fetch("http://localhost:8080/carsearch/getRenterUsername?id=" + carId, {
            method: 'GET',
            headers: {
                'Authorization': "Bearer " + token
            }
        })
            .then(function (response) {
                return response.json();
            })
            .then(function (body) {
                const beneficiary_username = body["renter"];

                const data = {
                    senderUsername: payer_username,
                    receiverUsername: beneficiary_username,
                    price: Number(price),
                };

                // extract the username from the jwt token
                fetch( "http://localhost:8080/payment/createTransaction", {
                    method: 'POST',
                    headers: {
                        'Content-Type': "application/json",
                        'Authorization': "Bearer " + token
                    },
                    body: JSON.stringify( data )
                })
                    .catch(function () {
                        // give visual feedback to the user about the error
                        document.getElementById('error').textContent = "You won the auction, but the payment for the car has encountered some problems";
                        // make the value field not visible
                        // one way to be sure that, in case of an error, the successful payment feedback is not visible
                        document.getElementById('value').style.display = 'none';

                        // TODO check if the booking is deleted
                        fetch("http://localhost:8080/reservation/deleteBooking/" + bid, {
                            method: 'DELETE',
                            headers: {
                                'Authorization': "Bearer " + token
                            }
                        })
                    })
            })
    }

    showFinalMessage(win, message){
        if (win) {
            document.getElementById('status').style.display = "none"
            document.getElementById('value').textContent = message
            document.getElementById('value').style.color = 'green'
        } else {
            document.getElementById('status').style.display = "none"
            document.getElementById('value').textContent = message
            document.getElementById('value').style.color = 'red';
        }
    }
}
