var mongoose = require('mongoose');
var ObjectId = mongoose.Schema.ObjectId;
var Number = mongoose.Schema.Types.Number;
var Schema = mongoose.Schema;

var messageSchema = new Schema({
	user: { type: ObjectId, ref: 'users' },
	discussion: { type: ObjectId, ref: 'discussions' },
	text: String,
	latitude: Number,
	longitude: Number,
	date: Date
});

module.exports = mongoose.model('messages', messageSchema);