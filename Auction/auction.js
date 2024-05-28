// STRUCTURES

const states = {
  FOLLOWER: 'FOLLOWER',
  CANDIDATE: 'CANDIDATE',
  LEADER: 'LEADER'
};

// Define a global variable for message structure
const RaftMessage = {
  types: {
    APPEND_ENTRIES: 'AppendEntries',
    APPEND_ENTRIES: 'AppendEntriesAck',
    REQUEST_VOTE: 'RequestVote',
    REQUEST_VOTE_RESPONSE: 'RequestVoteAck',
    APPEND_ENTRIES: 'Heartbeat',
    APPEND_ENTRIES: 'HeartbeatAck',
    // Add more message types as needed
  },
  create: function(type, term, leaderId, prevLogIndex, prevLogTerm, entries, leaderCommit) {
    return {
      type: type,
      term: term,
      leaderId: leaderId,
      prevLogIndex: prevLogIndex,
      prevLogTerm: prevLogTerm,
      entries: entries,
      leaderCommit: leaderCommit
    };
  }
};

class RaftNode {
  constructor(id, peers) {
    this.id = id;
    this.peers = peers;
    this.sockets = {};
    this.currentTerm = 0;
    this.votedFor = null;
    this.log = [];
    this.state = states.FOLLOWER;
    this.leaderId = null;
    this.commitIndex = 0;
    this.lastApplied = 0;
    this.nextIndex = {};
    this.matchIndex = {};
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

   sendMessage(peer, message) {
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

      // TODO is dummy
      const message = RaftMessage.create(
        RaftMessage.types.APPEND_ENTRIES,
        2,
        'node1',
        5,
        1,
        [],
        5
      );
      this.send(peer,message)
      const voteGranted = this.onReceiveVoteResponse(peer, this.currentTerm);
      if (voteGranted) {
        votesReceived++;
        if (votesReceived > totalNodes / 2) {
          // Received majority of votes, become leader
          this.becomeLeader();
          return;
        }
      }
    }
  }

  onReceiveVoteResponse(peer, term) {
    if (term < this.currentTerm) {
      return false;
    }
    if (this.votedFor === null || this.votedFor === peer) {
      this.votedFor = peer;
      this.votes[id]++;
      if (votesReceived > totalNodes / 2) {
        // Received majority of votes, become leader
        this.becomeLeader();
      }
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

    if (success) {
      this.matchIndex[peer] = this.nextIndex[peer] + entries.length - 1;
      this.nextIndex[peer] += entries.length;
      this.updateCommitIndex();
    }
  }

  sendAppendEntries(peer, term, prevLogIndex, prevLogTerm, entries, leaderCommit) {
    // Simulated AppendEntries RPC
    // Here you would send RPC to peer and handle response
    // For simplicity, let's assume the request always succeeds
    const socket = this.sockets[peer];
    if (socket) {
      socket.send({
        term,
        prevLogIndex,
        prevLogTerm,
        entries,
        leaderCommit
      });
      return true;
    } else {
      return false;
    }
  }

  updateCommitIndex() {
    const sortedMatchIndexes = Object.values(this.matchIndex).sort((a, b) => a - b);
    const majorityMatchIndex = sortedMatchIndexes[Math.floor(sortedMatchIndexes.length / 2)];
    if (majorityMatchIndex > this.commitIndex && this.log[majorityMatchIndex - 1].term === this.currentTerm) {
      this.commitIndex = majorityMatchIndex;
      this.applyLogEntries();
    }
  }

  applyLogEntries() {
    for (let i = this.lastApplied + 1; i <= this.commitIndex; i++) {
      // Apply log entries to state machine
      // For simplicity, let's just log them for now
      console.log(`Node ${this.id} applies log entry:`, this.log[i - 1].command);
      this.lastApplied = i;
    }
  }

  handleAppendEntries(command) {
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
    for (const peer of this.peers) {
      this.sendHeartbeat(peer);
    }
  }

  receiveHeartbeat(leaderId) {
    this.leaderId = leaderId;
    if (this.state !== states.FOLLOWER) {
      // If not already a follower, become one
      this.state = states.FOLLOWER;
      this.startElectionTimeout();
    }
  }
}

