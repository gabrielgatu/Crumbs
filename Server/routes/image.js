var express = require('express');
var Geolocation = require('../utils/geolocation');
var Image = require('../models/image');

var router = express.Router();

router.get('/find', function(req, res) {
    var user = req.query.user;
    var latitude = Geolocation.formatCoordinate(req.query.latitude);
    var longitude = Geolocation.formatCoordinate(req.query.longitude);

    var query = {};

    if (user) {
        query.user = user;
    } else {
        query.latitude = latitude;
        query.longitude = longitude;
    }

    Image.find(query, function(err, images) {
        if (err) { res.sendStatus(400); }
        else { res.send(images) }
    });
});

router.post('/create', function(req, res) {
    var user = req.body.user;
    var latitude = Geolocation.formatCoordinate(req.body.latitude);
    var longitude = Geolocation.formatCoordinate(req.body.longitude);
    var image = req.body.image;
    var date = new Date().getTime();

    new Image({
        user: user,
        latitude: latitude,
        longitude: longitude,
        image: image,
        date: date
    }).save(function(err, image) {
        if (err) { console.log(err); res.sendStatus(400); }
        else { res.send(image); }
    })
});

module.exports = router;