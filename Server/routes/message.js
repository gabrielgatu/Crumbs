var express = require('express');
var Message = require('../models/message');
var Geolocation = require('../utils/geolocation');

var router = express.Router();

router.get('/find', function(req, res) {
    var latitude = Geolocation.formatCoordinate(req.query.latitude);
    var longitude = Geolocation.formatCoordinate(req.query.longitude);
    var discussionId = req.query.discussion_id || null;

    var query = {
        discussion: discussionId
    };

    if (! discussionId) {
        query.latitude = latitude;
        query.longitude = longitude;
    }

    Message
        .find(query)
        .limit(100)
        .sort('-date')
        .exec(function(err, _messages) {
            if (err) {
                res.send(err)
            } else {
                Message.populate(_messages, {path: 'user'}, function (err, messages) {
                    var messagesOrderedByDate = messages.reverse();
                    var simpleJsonMessages = createSimpleJsonMessage(messagesOrderedByDate);
                    res.send(simpleJsonMessages);
                });
            }
        });
});

function createSimpleJsonMessage(messages) {
    return messages.map(function(message) {
        return {
            _id: message._id,
            user_id: message.user._id,
            user_name: message.user.username,
            message: message.text,
            latitude: message.latitude,
            longitude: message.longitude,
            date: message.date
        };
    });
}

module.exports = router;