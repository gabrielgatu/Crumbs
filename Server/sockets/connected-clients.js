module.exports = (function() {

    var connectedClients = [];

    return {

        addClient: function(userID, socketID) {
            connectedClients.push({
                userID: userID,
                socketID: socketID
            });
        },

        removeClient: function(userID) {
            var clientIndex = -1;
            for (var i = 0, length = connectedClients.length; i < length; i++) {
                if (connectedClients[i].userID === userID) {
                    clientIndex = i;
                }
            }

            if (clientIndex != -1) {
                connectedClients.splice(clientIndex, 1);
            }
        },

        exists: function(userID) {
            var exists = false;

            return connectedClients.forEach(function(client) {
                if (client.userID === userID) { exists = true; }
            });

            return exists;
        },

        getClientByUserKey: function(userID) {
            var clientFound = null;

            connectedClients.forEach(function(client) {
                if (client.userID === userID) { clientFound = client; }
            });

            return clientFound;
        },

        getClientBySocketID: function(socketID) {
            var clientFound = null;

            connectedClients.forEach(function(client) {
                if (client.socketID === socketID) { clientFound = client; }
            });

            return clientFound;
        },

        getClients: function() {
            return connectedClients;
        }
    }
})();