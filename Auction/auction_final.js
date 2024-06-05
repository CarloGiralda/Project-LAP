// INIT
document.addEventListener('DOMContentLoaded', () => {
      const ids = ['node1'];
      const peersIds = ['node2', 'node3'];// fetch from server
      const bid = '3';// fetch from user
      const raftNode = new RaftNode(ids, peersIds, bid);
      console.log('RaftNode initialized:', raftNode);
});

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
    // Add more message types as needed
};

class RaftNode {
  constructor(ids, peersIds, bid) {
    this.id = ids[0];
    this.myPeer = null ;
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
    this.initPeer();
    this.connectToPeers();
    this.startElectionTimeout();
   }

  // CONNECTION LAYER
  initPeer() {
        this.myPeer = new Peer();
        this.myPeer.on('open', function(id) {
        document.getElementById('peer-id').value = id;
        document.getElementById('status').textContent = 'Waiting for connection...';
        });
        this.myPeer.on('close', function() {
        document.getElementById('status').textContent = 'Connection closed...';
        });
   }
     
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
        
                // Parse the incoming message
                const { messageType, messageData } = data;
        
                // Implement Raft consensus algorithm based on message type
                switch (messageType) {
                  case 'AppendEntries':
                    // TODO
                    break;
                  case 'AppendEntriesAck':
                    // TODO
                    break;
                  case 'RequestVote':
                    // TODO
                    break;
                  case 'RequestVoteAck':
                    // TODO
                    break;
                  case 'Heartbeat':
                      // TODO
                      break;
                  case 'HeartBeatAck':
                      // TODO
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

  onReceiveVoteResponse(peer, term) {
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
                      entries: this.log
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

        for (const peer of this.peers) {
            // committed value
          const raftMessage = {
                     sender: this.id,
                     messageType: types.COMMIT,
                     data: {
                       term: this.currentTerm,
                       value: Math.min(...this.log)
                     }
           };

          const jsonMessage = JSON.stringify(raftMessage);
          this.send(peer,jsonMessage);
        }
     }
    }

  onReceiveHeartbeatCommitResponse(message) {
    console.log('Final Value', data);
    document.getElementById('result').textContent = data;
  }
}

