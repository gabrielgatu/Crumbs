var mongoose = require('mongoose');
var ObjectId = mongoose.Schema.ObjectId;
var Number = mongoose.Schema.Types.Number;
var Schema = mongoose.Schema;

var discussionSchema = new Schema({
    user: { type: ObjectId, ref: 'users' },
    title: String,
    text: String,
    latitude: Number,
    longitude: Number,
    date: String
});

module.exports = mongoose.model('discussions', discussionSchema);