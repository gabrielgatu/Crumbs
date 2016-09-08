var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var userSchema = new Schema({
	username: String,
	password: String,
	email: String,
	latitude: Number,
	longitude: Number,
	image: String
});

module.exports = mongoose.model('users', userSchema);