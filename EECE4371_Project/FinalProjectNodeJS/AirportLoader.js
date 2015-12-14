// JS Script to load airports into database

// Import required libraries and airport data
var mongoose = require("mongoose"),
    Schema = mongoose.Schema,
    airports = require('airport-codes/airports.json');

// Connect to mongoose database
mongoose.connect('mongodb://localhost:27017/hasnuli');

// Define Mongoose Airport Schema
var AirportSchema = new Schema({
    'type': String,
    'pos': {type: [Number], index: '2dsphere', required: true}
})

// Define Mongoose Airport Model
var Airport = mongoose.model('Airport', AirportSchema);

// Load Data into Mongoose Database
for (var i = 0; i < airports.length; ++i) {
    var tempAirport = new Airport({
        'type': 'Point',
        'pos': [airports[i].longitude, airports[i].latitude]
    })
    tempAirport.save(function (err) {
        if (err) {
            console.log(err);
        }
    })
}

// Log when finish
console.log('Done');
