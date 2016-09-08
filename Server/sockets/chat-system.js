var User = require('../models/user');
var Message = require('../models/message');
var Geolocation = require('../utils/geolocation');
var ConnectedClients = require('./connected-clients');

module.exports = function(io) {

    // Authentification
	io.use(function(socket, next) {
		var userID = socket.handshake.query.user_id;
		var socketID = socket.id;

        console.log("user received:", userID);

		User.findOne({ _id: userID }, function(err, users) {
			if (users && users.length !== 0) {

                console.log(userID);

                ConnectedClients.addClient(userID, socketID);
                next();
            }
		});
	});

    // Chat system
	io.on('connection', function(socket) {

        socket.on('chat.message', function(msg, latitude, longitude, discussionId) {
            var formattedLat = Geolocation.formatCoordinate(latitude);
            var formattedLng = Geolocation.formatCoordinate(longitude);

            var userID = ConnectedClients.getClientBySocketID(socket.id).userID;
            var date = new Date().getTime();

            console.log("message received", msg);

            User.findOne({ _id: userID }, function(err, user) {
                var senderUsername = user.username;

                if (discussionId === undefined) {
                    io.emit("chat.message", senderUsername, userID, msg);
                }
                else {
                    io.emit("chat.message." + discussionId, senderUsername, userID, msg);
                }

                new Message({
                    user: userID,
                    discussion: discussionId,
                    text: msg,
                    latitude: formattedLat,
                    longitude: formattedLng,
                    date: date
                }).save();
            });
        });

        socket.on('disconnect', function() {
            console.log('Got disconnect!');

            var userID = ConnectedClients.getClientBySocketID(socket.id).userID;
            ConnectedClients.removeClient(userID);
        });
	});
};