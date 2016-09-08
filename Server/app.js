var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(__dirname + '/public/favicon.ico'));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

///////////////// SETUP //////////////////////

mongoose.connect('mongodb://127.0.0.1/buzz');

//app.use(bodyParser({limit: '50mb'}));

///////////////// ROUTES ////////////////////

var userRoute = require('./routes/user');
var discussionRoute = require('./routes/discussion');
var messageRoute = require('./routes/message');
var areaRoute = require('./routes/area');
var friendshipRoute = require('./routes/friendship');
var imageRoute = require('./routes/image');

app.use('/api/user', userRoute);
app.use('/api/discussion', discussionRoute);
app.use('/api/message', messageRoute);
app.use('/api/area', areaRoute);
app.use('/api/friendship', friendshipRoute);
app.use('/api/image', imageRoute);

/////////////// ERROR HANDLERS /////////////

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});


module.exports = app;
