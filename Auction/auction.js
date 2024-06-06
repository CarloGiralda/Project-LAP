// INIT
var peer = null;
var isReadyAuction = false;
var isSubscribed = false;
var peersIds = null;
var serverUrl = 'YOUR_SERVER_URL_HERE'; // Set your server URL here

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('subButton').addEventListener('click', () => {
        peer = new Peer();
        peer.on('open', function(id) {
            document.getElementById('myPeer').textContent = id;
            document.getElementById('status').textContent = 'Waiting for connection...';
            fetchPeerIdsFromServer();
        });
        peer.on('close', function() {
            document.getElementById('status').textContent = 'Connection closed...';
        });
    });

    document.getElementById('bidButton').addEventListener('click', () => {
        const bid = document.getElementById('bid').textContent; // fetch from user
        const myPeerId = document.getElementById('myPeer').textContent;
        const raftNode = new RaftNode(peer, myPeerId, peersIds, bid);
        console.log('RaftNode initialized:', raftNode);
    });
    checkAuction();
    checkSubscription();
});

function startTimer(duration) {
    var progressBar = document.getElementById('progressBar');
    var startTime = Date.now();
    var endTime = startTime + duration * 1000;
    var interval = setInterval(() => {
        var remainingTime = endTime - Date.now();
        var percentage = Math.max(0, (remainingTime / (duration * 1000)) * 100);
        progressBar.style.width = percentage + '%';
        progressBar.textContent = Math.ceil(percentage) + '%';

        if (remainingTime <= 0) {
            clearInterval(interval);
            progressBar.style.width = '0%';
            progressBar.textContent = '0%';
            onTimerEnd();
        }
    }, 100);
}

function onTimerEnd() {
    alert('Timer has reached zero!');
    isReadyAuction = true;
    checkAuction();
}

// TODO only one function needed
function fetchPeerIdsFromServer() {
    fetch(serverUrl + '/subscribe', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ peerId: peer.id })
    })
    .then(response => response.json())
    .then(data => {
        peersIds = data.peers;
        isSubscribed = true;
        checkSubscription();
    })
    .catch(error => {
        console.error('Error fetching peer IDs:', error);
        document.getElementById('status').value = 'Failed to subscribe...';
    });
}

function checkSubscription() {
    if (isSubscribed) {
        document.getElementById('bid-form').style.display = 'block';
    } else {
        document.getElementById('bid-form').style.display = 'none';
    }
}

function checkAuction() {
    if (isReadyAuction) {
        document.getElementById('id-form').style.display = 'block';
    } else {
        document.getElementById('id-form').style.display = 'none';
    }
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
    APPEND: 'Append',
};

class RaftNode {
  constructor(myPeer,ids, peersIds, bid) {
    this.id = ids[0];
    this.myPeer = myPeer ;
    this.peersIds = peersIds;// peers ids fetched from microservice
    this.peers = null;// stores the P2P connection
    this.currentTerm = 0;
    this.votedFor = null;
    this.log = [bid];
    this.acks = 0;
    this.state = states.FOLLOWER;
    this.leaderId = null;
    this.votes = {};
    this.totalNodes = this.peers.length + 1;
    this.connectToPeers();
    this.startElectionTimeout();
    this.finalValue = null;
   }

  // CONNECTION LAYER
     
  connectToPeers() {
        for (const peer of this.peers) {
            const conn = myPeer.connect(peer);
            conn.on('open', () => {
            console.log(`Connected to peer ${peer}`);
            this.peers[peer] = conn;
          });
            conn.on('error', (error) => {
            console.error(`Error connecting to peer ${peer}:`, error);
          });
            conn.on('open', function() {
               console.log('Connection with ' + peer + 'opened');
        });
            conn.on('data', (data) => {
                // Handle incoming data from peer
                console.log(`Received data from ${conn.peer}:`, data);
        
                // Implement Raft consensus algorithm based on message type
                switch (data['messageType']) {
                  case 'RequestVote': // ADDED TO THE PROTOCOL TO BROADCAST VALUES FOR BIDS
                    const entries = message['entries'];
                    this.log = [...new Set([...this.log,...entries])];
                    break;
                  case 'RequestVote':
                    onReceiveVote(data);
                    break;
                  case 'RequestVoteAck':
                    onReceiveVoteResponse(data);
                    break;
                  case 'Heartbeat':
                      onReceiveHeartbeat(data);
                      break;
                  case 'HeartBeatAck':
                      onReceiveHeartbeatResponse(data);
                      break;
                  case 'Commit':
                      onReceiveHeartbeatCommit(data);
                      break;
                  case 'CommitAck':
                      onReceiveHeartbeatCommitResponse(data);
                      break;
        
                  // Add more cases for other message types
                  default:
                    console.log('Unknown message type:', messageType);
                }
            });
            conn.on('close', () => {
                  // Handle peer disconnection
                  console.log(`Peer ${conn.peer} disconnected`);
                  this.peers[peer] = null;

        });
        }
    }
        
  send(peer, message) {
      // Send message to a peer
      const conn = this.peers[peer];
      conn.send(message);
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
      this.leaderId = this.id;
      this.stopElectionTimeout();
      this.startHeartbeat();
    }

  requestVotes() {
    this.votes[id]= 1; // vote for myself
    for (const peer of this.peers) {
          const raftMessage = {
            sender: this.id,
            messageType: types.REQUEST_VOTE,
            data: {
              term: this.currentTerm,
              entries: []
            }
          };
          const jsonMessage = JSON.stringify(raftMessage);
          this.send(peer,jsonMessage)
        }
  }

  onReceiveVote(message) {
      const peer = message['sender'];
      const term = message['term'];
      if (term < this.currentTerm) {
        return false;
      }
      if (this.votedFor == null) {
        this.votedFor = peer;
        this.votes[peer]++;
        if (this.votes[peer] > totalNodes / 2) {
          // Received majority of votes, become leader
          this.leader=peer;
        }
        this.stopElectionTimeout();
        this.startElectionTimeout();
        const raftMessage = {
                    sender: this.id,
                    messageType: types.REQUEST_VOTE_ACK,
                    data: {
                      term: this.currentTerm,
                      entries: []
                    }
        };
       const jsonMessage = JSON.stringify(raftMessage);
       this.send(peer,jsonMessage);
      }
    }

  onReceiveVoteResponse(message) {
    if (term < this.currentTerm) {
        return false;
    }
    this.votes[this.id]++;
    if (this.votes[this.id] > totalNodes / 2) {
        // Received majority of votes, become leader
        this.becomeLeader();
    }
  }

  // LOG PART
  
  startHeartbeat() {
    for (const peer of this.peers) {
        const raftMessage = {
            sender: this.id,
            messageType: types.HEARTBEAT,
            data: {
              term: this.currentTerm,
              entries: this.log // here send all the log but can be sent only the min entry
            }
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
      const peer = message['sender'];
      const term = message['term'];
      const entries = message['entries'];
      if (term < this.currentTerm) {
        return false;
      }
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
                    data: {
                      term: this.currentTerm,
                      entries: []
                    }
      };
      const jsonMessage = JSON.stringify(raftMessage);
      this.send(peer,jsonMessage);
    }

  onReceiveHeartbeatResponse(message) {
      const peer = message['sender'];
      const term = message['term'];
      const entries = message['entries'];
      if (term < this.currentTerm) {
           return false;
       }
      this.acks++;

      if (this.acks > totalNodes / 2) {

        this.finalValue = Math.min(...this.log),

        for (const peer of this.peers) {
            // committed value
          const raftMessage = {
                     sender: this.id,
                     messageType: types.COMMIT,
                     data: {
                       term: this.currentTerm,
                       value: this.finalValue
                     }
           };

          const jsonMessage = JSON.stringify(raftMessage);
          this.send(peer,jsonMessage);
        }
     }
    }

  onReceiveHeartbeatCommit(message) {
      const peer = message['sender'];
      const term = message['term'];
      const value = message['value'];
      if (term < this.currentTerm) {
           return false;
       }
      // committed value
      const raftMessage = {
          sender: this.id,
          messageType: types.COMMIT_ACK,
          data: {
              term: this.currentTerm,
          }
      };
      this.finalValue = value;
      const jsonMessage = JSON.stringify(raftMessage);
      this.send(peer,jsonMessage);
      console.log('Final Value', this.finalValue);
      document.getElementById('value').textContent = this.finalValue;
  }

  onReceiveHeartbeatCommitResponse(message) {
    console.log('Final Value', this.finalValue);
    document.getElementById('value').textContent = this.finalValue;
  }
}

