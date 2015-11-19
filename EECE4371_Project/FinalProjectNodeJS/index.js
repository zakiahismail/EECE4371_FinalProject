/**
 * Created by PC admin on 11/15/2015.
 */

var express=require('express'),
    bodyParser=require('body-parser');
   // $=require('jquery')(require("jsdom").jsdom().parentWindow);

var app=express();
app.use(express.static('public'));
app.use(bodyParser.urlencoded({extended: true}));

var server = app.listen(8080, function () {
    console.log('Example app listening on ' + server.address().port);
});

app.get('/m/test', function(req,res){
    console.log(req.url);
    var latParam=getUrlParameter("lat",req.url);
    var longParam=getUrlParameter("long",req.url);
    var maxParam=getUrlParameter("max",req.url);

    var response = "Parameters recieved are lat = "+ latParam +" long = " + longParam + " max = " + maxParam;
    console.log(response);
    res.status(200).send(response);
})

app.get('/test', function(req,res){
    console.log(req.url);
    var latParam=getUrlParameter("lat",req.url);
    var longParam=getUrlParameter("long",req.url);
    var maxParam=getUrlParameter("maxdist",req.url);

    var response = "Parameters recieved are lat = "+ latParam +" long = " + longParam + " max = " + maxParam;
    console.log(response);
    res.status(200).send(response);
})

var getUrlParameter = function getUrlParameter(sParam,url) {
    var sPageURL = url.split('?').pop().substring(0),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    console.log(sPageURL)

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[0] === undefined ? true : sParameterName[1];
        }
    }
};

/*
$("#testSubmit").submit(function(){
    $.ajax({
        url:"/test",
        data:$("form").serialize(),
        type: "GET",
        dataType:"json",
        success: function(result){
            $("#result").append(result);
        },
        error:function(error){
            $('#result').append("error");
        }
    })
});
*/