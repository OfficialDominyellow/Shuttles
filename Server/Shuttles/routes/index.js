var express = require('express');
var router = express.Router();
var db = require('../models/db');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/test',function(req,res,next){
  var obj = {"result" : "ok"}
  res.json(obj);
});

router.get('/testdb',function(req,res,next){
  var obj = {"db":"connected"};
  db.test(obj,function(success){
    res.json(obj);
  })
})

module.exports = router;
