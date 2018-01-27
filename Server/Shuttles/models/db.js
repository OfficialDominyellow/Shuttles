var mysql = require('mysql');

var pool = mysql.createPool({
    connectionLimit : 10,
    host : 'localhost',
    user : 'root',
    password : '1234',
    database : 'shuttlesdb'
});


exports.test = function(data,callback){
    pool.getConnection(function(err,connection){
        if(err){
            console.error('not connected');
        }
        console.log('connected!!');
        callback(data);
    })
}

//module.exports = pool;