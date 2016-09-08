var express = require('express');
var Friendship = require('../models/friendship');
var User = require('../models/user');

var router = express.Router();

router.get('/find', function(req, res) {
    var user = req.query.user;
    var allFriendships = [];

    Friendship.find({ user: user }, function(err, friends1) {
        Friendship.find({ friend: user }, function(err, friends2) {

            allFriendships = allFriendships.concat(friends1);
            allFriendships = allFriendships.concat(friends2);

            var allFriendsIds = allFriendships.map(function(friendship) {
                if (friendship.user == user) return friendship.friend;
                else return friendship.user
            });

            User.find({ _id: { $in: allFriendsIds } }, function(err, users) {
                res.send(users);
            });
        });
    });
});

router.get('exists', function(req, res) {
    var user = req.query.user;
    var friend = req.query.friend;

    Friendship.find({ user: user, friend: friend }, function(err, friend1) {
        Friendship.find({ user: friend, friend: user }, function(err, friend2) {
            if (friend1 || friend2) res.send(200);
            else res.send(400);
        });
    })
});

router.post('/create', function(req, res) {
    var user = req.query.user;
    var friend = req.query.friend;
    var date = new Date().getTime();

    new Friendship({
        user: user,
        friend: friend,
        date: date
    }).save(function(err, friend) {
        res.send(friend || err);
    });
});

module.exports = router;