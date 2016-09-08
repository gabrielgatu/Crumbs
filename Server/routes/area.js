var express = require('express');
var User = require('../models/user');
var Geolocation = require('../utils/geolocation');
var ConnectedClients = require('../sockets/connected-clients');

var router = express.Router();

router.get('/users', function(req, res) {
    var latitude = Geolocation.formatCoordinate(req.query.latitude);
    var longitude = Geolocation.formatCoordinate(req.query.longitude);
    var count = req.query.count;

    var connectedUsersIds = ConnectedClients.getClients().map(function(client) {
        return client.userID;
    });

    console.log(connectedUsersIds)

    //User.find({ latitude: latitude, longitude: longitude }, function(err, users) {
    User.find({  }, function(err, users) {
        if (users) {

            var usersOnline = users.filter(function(user) {
                return connectedUsersIds.some(function(id) {
                   return id == user._id;
                });
            });

            if (count === "true") {
                res.send(usersOnline.length + "");
            } else {
                res.send(users);
            }
        }
        else {
            res.send(err);
        }
    });
});

module.exports = router;