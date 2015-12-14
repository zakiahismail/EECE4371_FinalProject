// JS Server Script

// Import required libraries
var express = require('express'),
    bodyParser = require('body-parser'),
    mongoose = require('mongoose'),
    Schema = mongoose.Schema;

// Construct server pipeline
var app = express();
app.use(express.static('public'));
app.use(bodyParser.urlencoded({extended: true}));

// Connect to MongoDB server (Might require changing in database name and port number)
mongoose.connect('mongodb://localhost:27017/hasnuli');

// Define Mongoose Schema for Airport
var AirportSchema = new Schema({
    'pos': {type: [Number], index: '2dsphere', required: true}
})

// Define Airport model for Airport
var Airport = mongoose.model('Airport', AirportSchema);

// Log when server is ready to receive requests
var server = app.listen(8080, function () {
    console.log('Server listening on ' + server.address().port);
});

// Process HTTP Request from Android Application
app.get('/m/test', function (req, res) {

    // Retrieve parameters from URL
    console.log(req.url);
    var latParam = req.query.lat
    var longParam = req.query.long
    var maxParam = req.query.max;

    // Send back dummy latLng, since Mongoose query is not successful
    var response = '36.1676,-86.7727'
    res.status(200).send(response);

    // Code for Mongoose query
/*    var airport = Airport.find({
        'pos': {
            $near: {
                $geometry: {
                    type: 'Point',
                    coordinates: [longParam, latParam]
                },
                $maxDistance: maxDistance
            }
        }
    })

    // If no airport found
    if (!airport) {
        var response = 'No Airport Found Close By'
        console.log(response)
        res.status(417).send(response);
    }

    // If airport is found
    else {
        console.log(airport);
        var response = 'lat=' + airport[0]['pos'][1] + 'long=' + airport[1]['pos'][0]
        console.status(200).send(response);
    }*/
})

// Process HTTP request from browser webpage
app.get('/map', function (req, res) {

    // Retrieve parameters from URL
    console.log(req.url);
    var latParam = req.query.lat;
    var longParam = req.query.long;
    var maxDistance = req.query.maxdist;

    // Send back dummy latLng, since Mongoose query is not successful
    var response = 36.1676 + ',' + -86.7727
    res.status(200).send(response);

    // Code for Mongoose query
/*    var airport = Airport.find({
        'pos': {
            $near: {
                $geometry: {
                    type: 'Point',
                    coordinates: [longParam, latParam]
                },
                $maxDistance: maxDistance
            }
        }
    })

    // If no airport is found
    if (!airport) {
        var response = 'No Airport Found Close By'
        console.log(response)
        res.status(417).send(response);
    }

    // If airport is found
    else {
        console.log(airport);
        var response = 'lat=' + airport[0]['pos'][1] + 'long=' + airport[1]['pos'][0]
        console.status(200).send(response);
    }*/
})
