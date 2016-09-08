var express = require('express');
var bcrypt = require('bcrypt-nodejs');
var Geolocation = require('../utils/geolocation');
var User = require('../models/user');
var Image = require('../models/image');
var Message = require('../models/message');
var Friendship = require('../models/friendship');

var router = express.Router();

router.post('/updatelocation', function(req, res) {
  var id = req.body.user;
  var latitude = Geolocation.formatCoordinate(req.body.latitude);
  var longitude = Geolocation.formatCoordinate(req.body.longitude);

  User.findById(id, function(err, user) {
    if (user) {

      user.latitude = latitude;
      user.longitude = longitude;

      user.save(function(err, _user) {
        if (err) { res.send(400); }
        else { res.send(200); }
      });

    } else {
      res.send(400);
    }
  });
});

router.post('/updateimage', function(req, res) {
  var id = req.body.user;
  var image = req.body.image;

  User.findById(id, function(err, user) {
    if (user) {

      user.image = image;

      user.save(function(err, _user) {
        if (err) { res.send(400); }
        else { res.send(200); }
      });

    } else {
      res.send(400);
    }
  });
});

router.post('/updateusername', function(req, res) {
  var id = req.body.user;
  var username = req.body.username;

  User.findById(id, function(err, user) {
    if (user) {

      user.username = username;

      user.save(function(err, _user) {
        if (err) { res.send(400); }
        else { res.send(200); }
      });

    } else {
      res.send(400);
    }
  });
});

router.post('/updateemail', function(req, res) {
  var id = req.body.user;
  var email = req.body.email;

  User.findById(id, function(err, user) {
    if (user) {

      user.email = email;

      user.save(function(err, _user) {
        if (err) { res.send(400); }
        else { res.send(200); }
      });

    } else {
      res.send(400);
    }
  });
});

// Receive id of the user @return user
// Receive username of the user @return users
router.get('/find', function(req, res) {
  var id = req.query.id;
  var username = req.query.username;

  if (id) {
    User.findById(id, function(err, user) {
      res.send(user);
    });
  }

  else if (username) {
    User.find({ username: username }, function(err, users) {
      res.send(users);
    });
  }

  else {
    res.sendStatus(400);
  }
});

router.get('/profile', function(req, res) {
  var id = req.query.user;

  User.findById(id, function(err, user) {
    if (user) {

      Image.find({ user: id }, function(err, images) {
        if (images) {
          Message.find({ user: id }, function(err, messages) {

              res.send({
                _id: user._id,
                image: user.image,
                username: user.username,
                numImages: images.length,
                numFriends: 0,
                numMessages: messages.length
              });
          });
        }
      });

    } else {
      res.send(400);
    }
  });
});

router.get('/auth', function(req, res) {
  var username = req.query.username;
  var password = req.query.password;

  console.log(username, password);

  User.findOne({ username: username }, function(err, user) {

    if (user && user.length !== 0) {
      if (bcrypt.compareSync(password, user.password)) {
        res.send(user);
      }
      else {
        res.sendStatus(400);
      }
    }

    else {
      res.sendStatus(400);
    }
  });
});

router.post('/new', function(req, res) {
  var username = req.body.username;
  var password = req.body.password;
  var email = req.body.email;

  var passwordEncrypted = bcrypt.hashSync(password);

  User.findOne({ username: username, password: password }, function(err, user) {

    if (user && user.length !== 0) {
      res.sendStatus(400);
    }

    else {
      new User({
        username: username,
        password: passwordEncrypted,
        email: email,
        latitude: 0,
        longitude: 0
      }).save(function(err, user) {

        if (err) res.send(400);
        else res.send(200);

      });
    }
  });
});

module.exports = router;