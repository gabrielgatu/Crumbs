var express = require('express');
var User = require('../models/user');
var Discussion = require('../models/discussion');
var Geolocation = require('../utils/geolocation');

var router = express.Router();

router.get('/find', function(req, res) {
    var latitude = Geolocation.formatCoordinate(req.query.latitude);
    var longitude = Geolocation.formatCoordinate(req.query.longitude);

    Discussion.find({ latitude: latitude, longitude: longitude }, function(err, _discussions) {
        Discussion.populate(_discussions, {path: 'user'}, function(err, discussions) {
            res.send(discussions);
        });
    });
});

router.get('/find/:id', function(req, res) {
    var id = req.params.id;

    Discussion.findOne({ _id: id }, function(err, _discussion) {
         Discussion.populate(_discussion, {path: 'user'}, function(err, discussion) {
             res.send(discussion);
        });
    });
});

router.post('/create', function(req, res) {
    var userID = req.body.user_id;
    var title = req.body.title.charAt(0).toUpperCase() + req.body.title.slice(1);
    var text = req.body.text.charAt(0).toUpperCase() + req.body.text.slice(1);
    var latitude = Geolocation.formatCoordinate(req.body.latitude);
    var longitude = Geolocation.formatCoordinate(req.body.longitude);
    var date = new Date().getTime();

    User.findById(userID, function(err, user) {
        new Discussion({
            user: user._id,
            title: title,
            text: text,
            latitude: latitude,
            longitude: longitude,
            date: date
        }).save();
    });

    res.send(200);
});

module.exports = router;