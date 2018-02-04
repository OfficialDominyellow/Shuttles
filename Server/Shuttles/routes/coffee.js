var express = require('express');
var router = express.Router();
var db = require('../models/db');

router.get('/', function(req, res, next) {
    var coffee =[ {
        "coffee_id" : 1,
        "name" : "americano",
        "price" : 3500,
        "picture_url" : "https://s3.ap-northeast-2.amazonaws.com/shuttles/coffee/americano.jpg",
        "state" : "coffee"    
    },{
        "coffee_id" : 2,
        "name" : "latte",
        "price" : 3000,
        "picture_url" : "https://s3.ap-northeast-2.amazonaws.com/shuttles/coffee/latte.jpg",
        "state" : "latte"    
    }
];
    res.json(coffee);
});
  
router.get('/:coffee_id', function(req, res, next) {
    console.log("coffee_id : ",req.params.coffee_id);

    var coffee ={
        "coffee_id" : 1,
        "name" : "americano",
        "price" : 3500,
        "picture_url" : "https://s3.ap-northeast-2.amazonaws.com/shuttles/coffee/americano.jpg",
        "description" : "맛나요",
        "option": [{"option_name" : "샷추가","price" : 500},
    {"option_name":"망고 더","price":200}]    
    };
    res.json(coffee);
  });
    


module.exports = router;