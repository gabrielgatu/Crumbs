var mongoose = require('mongoose');
var ObjectId = mongoose.Schema.ObjectId;
var Number = mongoose.Schema.Types.Number;
var Schema = mongoose.Schema;

var imageSchema = new Schema({
    user: { type: ObjectId, ref: 'users' },
    latitude: Number,
    longitude: Number,
    image: String,
    date: String
});

module.exports = mongoose.model('images', imageSchema);