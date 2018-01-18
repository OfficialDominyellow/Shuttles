var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/test',function(req,res,next){
  var obj = {"result" : "ok"}
  res.json(obj);
});

module.exports = router;
