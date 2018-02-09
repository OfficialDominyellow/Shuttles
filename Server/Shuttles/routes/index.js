var express = require('express');
var router = express.Router();
var db = require('../models/db');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/test',function(req,res,next){
  var obj = {"name" : "americano", "url" : "https://s3.ap-northeast-2.amazonaws.com/shuttles/coffee/jimin.png"}
  res.json(obj);
});

router.get('/testdb',function(req,res,next){
  var obj = {"db":"connected"};
  db.getCoffee(obj,function(success){
    console.log('success : ',success);
    res.json(success);
  })
})

router.post('/testpost',function(req,res,next){
  console.log(req.body);

  var obj = {"process" : 1,
             "result" : "ok"};

  res.json(obj);
})


module.exports = router;
