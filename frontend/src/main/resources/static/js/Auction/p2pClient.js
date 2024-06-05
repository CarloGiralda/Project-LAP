
const peer = new Peer();

document.addEventListener('DOMContentLoaded', () => {
    const peer = new Peer();

    peer.on('open', function(id) {
        document.getElementById('peer-id').value = id;
        document.getElementById('status').textContent = 'Waiting for connection...';
    });

    document.getElementById('generate-peer-id').addEventListener('click', function() {
        document.getElementById('peer-id').value = peer.id;
    });

    document.getElementById('connect-button').addEventListener('click', function() {
        const connectPeerId = document.getElementById('connect-peer-id').value;
        const conn = peer.connect(connectPeerId);

        conn.on('open', function() {
            document.getElementById('status').textContent = 'Connected to ' + connectPeerId;
            conn.send('Hello from ' + peer.id);
        });

        conn.on('data', function(data) {
            console.log('Received', data);
            document.getElementById('status').textContent = 'Received: ' + data;
        });
    });

    peer.on('connection', function(conn) {
        conn.on('open', function() {
            document.getElementById('status').textContent = 'Connected to ' + conn.peer;
            conn.on('data', function(data) {
                console.log('Received', data);
                document.getElementById('status').textContent = 'Received: ' + data;
            });
        });
    });
});