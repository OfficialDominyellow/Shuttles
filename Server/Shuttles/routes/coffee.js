var express = require('express');
var router = express.Router();
var db = require('../models/db');

// show coffee list
router.get('/', function(req, res, next) {
    db.getCoffee(obj,function(success){
        res.json(success);
      })
});
  

// show coffee detail
router.get('/:coffee_id', function(req, res, next) {
    console.log("coffee_id : ",req.params.coffee_id);
    db.getCoffeeDetail(req.params.coffee_id,function(success){
        res.json(success);
    })
  });
    

router.post('/order', function(req, res, next) {
    console.log("coffee_id : ",req.body);

    var obj = {"success" : "ok"};

    res.json(coffee);
  });
  


  
module.exports = router;