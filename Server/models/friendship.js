var mongoose = require('mongoose');
var ObjectId = mongoose.Schema.ObjectId;
var Schema = mongoose.Schema;

var friendshipSchema = new Schema({
    user: { type: ObjectId, ref: 'users' },
    friend: { type: ObjectId, ref: 'users' },
    date: String
});

module.exports = mongoose.model('friendships', friendshipSchema);