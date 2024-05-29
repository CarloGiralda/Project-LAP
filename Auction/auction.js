// STRUCTURES

const states = {
  FOLLOWER: 'FOLLOWER',
  CANDIDATE: 'CANDIDATE',
  LEADER: 'LEADER'
};

const types = {
    APPEND_ENTRIES: 'AppendEntries',
    APPEND_ENTRIES_ACK: 'AppendEntriesAck',
    REQUEST_VOTE: 'RequestVote',
    REQUEST_VOTE_ACK: 'RequestVoteAck',
    HEARTBEAT: 'Heartbeat',
    HEARTBEAT_ACK: 'HeartbeatAck',
    COMMIT: 'Commit',
    COMMIT_ACK: 'CommitAck',
    // Add more message types as needed
};


class RaftNode {
  constructor(id, peers) {
    this.id = id;
    this.peers = peers;
    this.currentTerm = 0;
    this.votedFor = null;
    this.log = [];
    this.state = states.FOLLOWER;
    this.leaderId = null;
    this.votes = {};
    this.totalNodes = this.peers.length + 1;


    for (const peer of this.peers) {
      this.nextIndex[peer] = 1;
      this.matchIndex[peer] = 0;
    }

    this.connectToPeers();
    this.startElectionTimeout();
  }

  // CONNECTION PART

      connectToPeers() {
        for (const peer of this.peers) {
          const conn = new Peer();
          conn.on('open', () => {
            console.log(`Connected to peer ${peer}`);
            this.sockets[peer] = conn;
            this.setupSocketListeners(conn);
          });
          conn.on('error', (error) => {
            console.error(`Error connecting to peer ${peer}:`, error);
          });
        }
      }

    setupSocketListeners(conn) {
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
    }

    conn.on('close', () => {
      // Handle peer disconnection
      console.log(`Peer ${conn.peer} disconnected`);
    });

    conn.on('error', (error) => {
      // Handle socket error
      console.error(`Socket error with peer ${conn.peer}:`, error);
    });
  }

   send(peer, message) {
      // Send message to a peer
      const socket = this.sockets[peer];
      if (socket) {
        socket.send(message);
      } else {
        console.error(`Socket not found for peer ${peer}`);
      }
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
    this.votes[id]= 1; // Vote for self
    for (const peer of this.peers) {
      // Simulated RequestVote RPC to all other nodes

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

  onReceiveVote(peer, term) {
      if (term < this.currentTerm) {
        return false;
      }
      if (this.votedFor === null) {
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
    // Send AppendEntries RPCs (heartbeats) to all followers
    for (const peer of this.peers) {
      this.sendHeartbeat(peer);
    }
    // Schedule the next heartbeat
    setTimeout(() => {
      this.startHeartbeat();
    }, 100); // Adjust the interval as needed
  }

  sendHeartbeat(peer) {
    // Simulated AppendEntries RPC
    // Update follower's log, commitIndex, etc.
    const prevLogIndex = this.nextIndex[peer] - 1;
    const prevLogTerm = (prevLogIndex > 0) ? this.log[prevLogIndex - 1].term : 0;
    const entries = this.log.slice(this.nextIndex[peer] - 1);
    const leaderCommit = this.commitIndex;
    const success = this.sendAppendEntries(peer, this.currentTerm, prevLogIndex, prevLogTerm, entries, leaderCommit);

    const raftMessage = {
            sender: this.id,
            messageType: types.HEARTBEAT,
            data: {
              term: this.currentTerm,
              entries: entries
            }
          };

   const jsonMessage = JSON.stringify(raftMessage);
   this.send(peer,jsonMessage);

    if (success) {
      this.matchIndex[peer] = this.nextIndex[peer] + entries.length - 1;
      this.nextIndex[peer] += entries.length;
      this.updateCommitIndex();
    }
  }

    onReceiveHeartbeat(peer, term) {
        if (term < this.currentTerm) {
          return false;
        }
         this.leaderId = leaderId;
      if (this.state !== states.FOLLOWER) {
        // If not already a follower, become one
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

  onReceiveHeartbeatResponse(peer, term) {
        if (term < this.currentTerm) {
           return false;
         }
       //TODO  if message entry not null
        this.log[0]['ack'].append(peer)
       const val =this.log[0]['ack'].length
       const value = this.log[0]['command']

       if (val > totalNodes / 2) {

        for (const peer of this.peers) {
            // Simulated RequestVote RPC to all other nodes

            // committed value
                      const raftMessage = {
                                 sender: this.id,
                                 messageType: types.COMMIT,
                                 data: {
                                   term: this.currentTerm,
                                   value: value
                                 }
                               };

                        const jsonMessage = JSON.stringify(raftMessage);
                        this.send(peer,jsonMessage);
          }

       }

    }

      onReceiveHeartbeatCommitResponse(peer, term) {
         // TODO send to user the value

        }

  sendAppendEntries(peer, term, prevLogIndex, prevLogTerm, entries, leaderCommit,command) {

  // TODO when sending this? auction value bid
   // Append entry to leader's log
        const entry = {
          term: this.currentTerm,
          command: command,
          ack:[]// only me
        };
        this.log.push(entry);


    for (const peer of this.peers) {
       // Simulated RequestVote RPC to all other nodes

     const raftMessage = {
                         sender: this.id,
                         messageType: types.APPEND_ENTRIES,
                         data: {
                           term: this.currentTerm,
                           entries: entries
                         },
                         prevLogIndex: prevLogIndex,
                         prevLogTerm:prevLogTerm,
                         leaderCommit:leaderCommit
               };

                const jsonMessage = JSON.stringify(raftMessage);
                this.send(peer,jsonMessage);
     }
  }

    onReceiveAppendEntries(command) {
      if (this.state !== states.LEADER) {
        // Only leader can accept client requests
        console.log(`Node ${this.id} is not the leader. Cannot accept client request.`);
        return;
      }

      // Append entry to leader's log
      const entry = {
        term: this.currentTerm,
        command: command
      };
      this.log.push(entry);

      // Send log entry to followers on next heartbeat
        this.sendHeartbeat(peer);

    }





}

