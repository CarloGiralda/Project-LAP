// INIT
var peer = null;
var isReadyAuction = false;
var peersIds = null;
var token = getCookie("jwtToken");
var serverUrl = 'http://localhost:8080/auction'; // Set your server URL here
var auction;
var timestamp;
var myPeerId;

document.addEventListener('DOMContentLoaded', () => {
    var serverUrl = 'http://localhost:8080/auction'; // Set your server URL here
    var searchUrl = serverUrl + "/search"

    document.getElementById('bidButton').addEventListener('click', () => {
        const bid = document.getElementById('bid').value; // fetch from user
        document.getElementById('yourValue').textContent = 'Your bid: ' + bid;
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
        .catch(error => {
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

        // TODO test this part
        const carInfo = getCarInfo( body[x]["cid"] );

        // Create an image element
        const cardImage = document.createElement("img");
        cardImage.className = "card-img-top";
        cardImage.src = URL.createObjectURL(carInfo["image"]); // Assuming 'imageUrl' is the key in 'body' object
        cardImage.alt = "Car Image"; // Alternative text for the image

        imageWrapper.appendChild(cardImage);

        const cardBody = document.createElement( "div" );
        cardBody.className = "card-body";

        const cardTitle = document.createElement( "h5" );
        cardTitle.className = "card-title";
        cardTitle.textContent = "Auction id: " + body[x]["auctionId"];

        const cardText = document.createElement( "p" );
        cardText.className = "card-text";
        cardText.textContent = "Date: " + body[x]["startDate"] + "\nCar: " + body["car"]["brand"] + " " + body["car"]["model"];

        cardBody.appendChild(cardTitle);
        cardBody.appendChild(cardText);
        cardBody.appendChild(imageWrapper);

        const btn = document.createElement( "button" );
        btn.setAttribute("id", "btn_" + x);
        btn.setAttribute("class", "btn btn-danger btn-block m-3");
        btn.textContent = "Subscribe";

        // Set custom data attribute to store the index 'x'
        btn.customField = body[x]["auctionId"];

        btn.addEventListener('click', function subscribe(event) {
            auction = event.currentTarget.customField;
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

async function getCarInfo(id){
    var getUrl = "http://localhost:8080/carsearch/getCarById/" + id;

    return await fetch( getUrl, {
        method: 'GET',
        headers: {
            'Authorization': "Bearer " + token
        },
    } ).then( function (response) {
        return response.json();
    } ).then( async function (body) {
        if (body["car"]["image"] !== null) {
            const blob = new Blob( [new Uint8Array( body["car"]["image"] )], {type: 'image/jpeg'} );
            const image = document.getElementById( "image" );
            image.src = URL.createObjectURL( blob )
            console.log( "car image not null" )

            return {
                "name": body["car"]["brand"] + " " + body["car"]["model"],
                "image": blob
            }
        }
    } )

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
            timestamp = data;
            startTimer(timestamp);
        });
}

function startTimer(endTimeToConvert) {
    var timerElement = document.getElementById('timer');
    timerElement.style.display = 'flex';
    var endTime = new Date(endTimeToConvert).getTime();

    function formatTime(seconds) {
        const h = String(Math.floor(seconds / 3600)).padStart(2, '0');
        const m = String(Math.floor((seconds % 3600) / 60)).padStart(2, '0');
        const s = String(seconds % 60).padStart(2, '0');
        return `${h}:${m}:${s}`;
    }

    function updateTimer() {
        var now = new Date();
        var romeOffset = 2 * 60 * 60 * 1000; // Rome is UTC+2
        var romeTime = now.getTime() + romeOffset;
        var remainingTime = Math.floor((endTime - romeTime) / 1000); // Remaining time in seconds

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
    var interval = setInterval(updateTimer, 1000);
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
            console.log(data);
            peersIds = data;
            isReadyAuction = true;
            document.getElementById('bid').style.display = 'block';
            document.getElementById('bidButton').style.display = 'block';

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
        if (this.finalValue == this.bid) {
            document.getElementById('value').textContent = 'You win the auction the price is :' + this.finalValue;
            // TODO Here code to confirm booking

        } else {
            document.getElementById('value').textContent = 'You lost the auction the price is :' + this.finalValue + '\nLeader :' + this.leader;
        }
    }
}
