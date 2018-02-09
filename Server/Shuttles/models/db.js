var mysql = require('mysql');
var forEach = require('async-foreach').forEach;
var async = require('async');
var HashMap = require('hashmap');

var pool = mysql.createPool({
    connectionLimit: 10,
    host: 'localhost',
    user: 'root',
    password: '1234',
    database: 'shuttlesdb'
});


exports.test = function (data, callback) {
    pool.getConnection(function (err, connection) {
        if (err) {
            console.error('not connected');
        }
        console.log('connected!!');
        callback(data);
    })
}


exports.getCoffee = function (data, callback) {
    pool.getConnection(function (err, connection) {
        var obj=[];
        if (err) { console.error('not connected'); }
        var getCoffeeInfoSql = 'SELECT coffee_id,name,picture_url FROM coffee';
        var getCoffeeStateSql = 'SELECT name from coffee_state where coffee_id = ?';
        var getCoffeePriceSql = 'SELECT price from coffee_size where coffee_id = ?';

        connection.query(getCoffeeInfoSql, function (err, coffee) {
            if (err) throw err;
            var coffee_id;
            forEach(coffee,function(item,index,arr){
                
                coffee_id = coffee[index].coffee_id;
                
                connection.query(getCoffeeStateSql, coffee_id, function (error, state) {

                    connection.query(getCoffeePriceSql, coffee_id, function (error, price) {
                
                        console.log('coffee id, state, price',
                        coffee[index].coffee_id, state[0].name, price[0].price,coffee[index].name,coffee[index].picture_url);
                    
                        var coffeeObject = {"coffee_id" : coffee[index].coffee_id,
                                             "name" : coffee[index].name,
                                            "price": price[0].price,
                                            "picture_url":coffee[index].picture_url,
                                            "state":state[0].name};
                            
                        console.log(coffeeObject);
                         obj.push(coffeeObject);

                         if(obj.length==coffee.length){
                            callback(obj);
                        }

                    })
                })
            })
        })
    })
}


exports.getCoffeeDetail = function (data, callback) {

    pool.getConnection(function (err, connection) {
        if (err) { console.error('not connected'); }
        var getCoffeeInfoSql = 'SELECT coffee_id,name,picture_url,description FROM coffee where coffee_id = ?';
        var getCoffeePriceSql = 'SELECT price from coffee_size where coffee_id = ?';
        var getCoffeeOptionSql = 'SELECT option_id,name,price from coffee_option where coffee_id = ?';
        connection.query(getCoffeeInfoSql,data, function (err, coffee) {
            var obj = [];
            if (err) throw err;
                    connection.query(getCoffeePriceSql, data, function (error, price) {
                
                        connection.query(getCoffeeOptionSql,data,function(error,option){
                          
                            var optionArr = [];
                    
                            Object.keys(option).forEach(function(key){
                                var row = option[key];
                    
                                var optionDetail = {
                                    'option_id' : row.option_id,
                                    'option_name' : row.name,
                                    'price': row.price
                                }

                                optionArr.push(optionDetail);
                            });
                        
                            var coffeeObject = {"coffee_id" : data,
                                                 "name" : coffee[0].name,
                                                "price": price[0].price,
                                                "picture_url":coffee[0].picture_url,
                                                "description":coffee[0].description,
                                                "option":optionArr};
                    
                            console.log('coffeeObject',coffeeObject);
                            obj.push(coffeeObject)
                            callback(obj);                    
                        })
                    })
                })
            })
}


exports.getOrderList = function(user_id,callback){
    pool.getConnection(function(err,connection){
        var getOrdersList = "SELECT * from orders where user_id = ?";
        var getCoffeeOrders = "SELECT * from coffee_orders where order_id = ?";
        var getCoffeeName = "SELECT name from coffee where coffee_id = ?";
       
        var getFoodOrders = "SELECT * from food_orders where order_id = ?";
        var getFoodName = "SELECT name from food where coffee_id = ?";

        var obj =[];
        var coffee_name,order_id,coffee_id,coffee_count;
        var food_name,food_id,food_count;
        var Coffee = [];
        var Food = [];

        async.waterfall([
            function(callback){
                connection.query(getOrdersList,user_id,function(error,orderList){
                    forEach(orderList,function(item,index,arr){
                        var coffee_hashMap = new HashMap();
                        order_id = orderList[index].order_id;      
            
                        connection.query(getCoffeeOrders,order_id,function(err,coffeeOrders){
                            
                            forEach(coffeeOrders,function(item,index,arr){
                                coffee_count = coffeeOrders[index].count;
                                coffee_id = coffeeOrders[index].coffee_id;
        
                                connection.query(getCoffeeName,coffee_id,function(err,coffeeName){
                                    coffee_name = coffeeName[0].name;
                                    var CoffeeTemp = {
                                        "name" : coffee_name,
                                        "count" : coffee_count
                                    };
                                    Coffee.push(CoffeeTemp);                                   
                                })
                            })

                            if(Coffee.length==coffeeOrders.length){
                                coffee_hashMap.set(order_id,Coffee); 
                            }
                            if(coffee_hashMap.length == orderList.length){
                                callback(null,coffee_hashMap);
                            }
                        })
                    })
                })   
        },
        function(coffee_hashMap,callback){
                connection.query(getOrdersList,user_id,function(error,orderList){
                        forEach(orderList,function(item,index,arr){
                            var food_hashMap = new HashMap();
                            order_id = orderList[index].order_id;      
                
                            connection.query(getFoodOrders,order_id,function(err,foodOrders){
                    
                                forEach(foodOrders,function(item,index,arr){
                                    food_count = foodOrders[index].count;
                                    food_id = foodOrders[index].food_id;
            
                                    connection.query(getFoodName,food_id,function(err,foodName){
                                        food_name = foodName[0].name;
                                        var FoodTemp = {
                                            "name" :  food_name,
                                            "count" : food_count
                                        };
                                        Food.push(FoodTemp);     
                                    })
                                })
                                if(Food.length==foodOrders.length){
                                    food_hashMap.set(order_id,Food); 
                                }
                                if(food_hashMap.length == orderList.length){
                                    callback(null,coffee_hashMap,food_hashMap);
                                }
                            })
                        
                        })
                    })   
        },
        function(coffee_hashMap,food_hashMap,callback){
            connection.query(getOrdersList,user_id,function(error,orderList){
                forEach(orderList,function(item,index,arr){
                    order_id = orderList[index].order_id;      
                    var objCoffeeTemp = coffee_hashMap.get(order_id);
                    var objFoodTemp = food_hashMap.get(order_id);                    
                    var objTemp = {
                        "order_id" : orderList[index].order_id,
                        "order_price" : orderList[index].price,
                        "order_state" : orderList[index].state,
                        "coffee" : objCoffeeTemp,
                        "food" : objFoodTemp

                    }

                    obj.push(objTemp);

                    if(obj.length == orderList.length)
                    callback(null,obj);
                })
            }) 
        }
       ],function(err,result){
            callback(result);
        })
    });
};



exports.getOrderDetail = function(order_id,callback){
    pool.getConnection(function(err,connection){
        var getOrderbyOrderId = "SELECT * from orders where order_id = ?";

        var getCoffeeOrders = "SELECT * from coffee_orders where order_id = ?";
        var getCoffeeName = "SELECT name from coffee where coffee_id = ?";
        var getCoffeeOption = "SELECT * from coffeeOption_orders = ?";
        var getCoffeeOptionName = "SELECT name from coffee_option where option_id = ?";

        var getFoodOrders = "SELECT * from food_orders where order_id = ?";
        var getFoodName = "SELECT name from food where coffee_id = ?";
        var getFoodOption = "SELECT * from foodOption_orders = ?";
        var getfoodOptionName = "SELECT name from food_option where option_id = ?";


        var obj =[];
    
        var coffee_name,coffee_id,coffee_ordersId,coffee_count,coffee_price,coffee_optionId,coffee_optionName;
        var CoffeeOptions=[];
        var Coffee = [];

        var food_name,food_id,food_ordersId,food_count,food_price,food_optionId,food_optionName;
        var FoodOptions=[];
        var Food = [];
    

        async.waterfall([
            function(callback){
                
                        connection.query(getCoffeeOrders,order_id,function(err,coffeeOrders){
                            
                            forEach(coffeeOrders,function(item,index,arr){
                                coffee_count = coffeeOrders[index].count;
                                coffee_id = coffeeOrders[index].coffee_id;
                                coffee_price= coffeeOrders[index].price
                                coffee_ordersId = coffeeOrders[index].coffee_ordersId;

                                connection.query(getCoffeeName,coffee_id,function(err,coffeeName){
                                    coffee_name = coffeeName[0].name;

                                    connection.query(getCoffeeOption,coffee_ordersId,function(err,coffeeOptionList){
                                        
                                        forEach(coffeeOptionList,function(item,index,arr){
                                            coffee_optionId = coffeeOptionList[index].option_id;

                                            connection.query(getCoffeeOptionName,coffee_optionId,function(err,optionName){
                                                coffee_optionName = optionName.name;

                                                var CoffeeOptionsTemp = {
                                                    "name" : coffee_optionName,
                                                }
                                                CoffeeOptions.push(CoffeeOptionsTemp);
                                            })
                                            if(CoffeeOptions.length==coffeeOptionList.length){

                                                var CoffeeTemp = {
                                                    "name" : coffee_name,
                                                    "count" : coffee_count,
                                                    "price" : coffee_price,
                                                    "option" : CoffeeOptions
                                                };
                                                Coffee.push(CoffeeTemp);
                                            }
                                        })
                                    })
                                                             
                                })
                                if(Coffee.length == coffeeOrders.length)
                                    callback(null,Coffee); 
                            })
                        })
                   
                 
        },
        function(Coffee,callback){       

                            connection.query(getFoodOrders,order_id,function(err,foodOrders){
                    
                                forEach(foodOrders,function(item,index,arr){
                                    food_count = foodOrders[index].count;
                                    food_id = foodOrders[index].food_id;
                                    food_price= foodOrders[index].price
                                    food_ordersId = foodOrders[index].food_ordersId;
                                    
                                    connection.query(getFoodName,food_id,function(err,foodName){

                                     food_name = foodName[0].name;

                                    connection.query(getFoodOption,food_ordersId,function(err,foodOptionList){
                                        
                                        forEach(foodOptionList,function(item,index,arr){
                                            food_optionId = foodOptionList[index].option_id;

                                            connection.query(getfoodOptionName,food_optionId,function(err,optionName){
                                                food_optionName = optionName.name;

                                                var foodOptionsTemp = {
                                                    "name" : food_optionName,
                                                }
                                                foodOptions.push(foodOptionsTemp);
                                            })
                                            if(foodOptions.length==foodOptionList.length){

                                                var FoodTemp = {
                                                    "name" : food_name,
                                                    "count" : food_count,
                                                    "price" : food_price,
                                                    "option" : foodOptions
                                                };
                                                Food.push(FoodTemp);
                                            }
                                        })
                                    })
                                    })
                                    if(Food.length == foodOrders.length)
                                    callback(null,Coffee,Food); 
                                })
                            })
        },
        function(Coffee,Food,callback){
            
            pool.getConnection(getOrderbyOrderId,order_id,function(err,orderList){
                var objTemp = {
                    "order_id" : order_id,
                    "order_price" : orderList[0].price,
                    "order_state" : orderList[0].state,
                    "order_date" : orderList[0].date,
                    "order_address" : orderList[0].address,
                    "coffee" : Coffee,
                    "food" : Food
                }
                obj.push(objTemp);
                callback(null,obj);
            })    
        }

       ],function(err,result){
            callback(result);
        })
    });
};



exports.insertOrder = function (data, callback) {
    pool.getConnection(function(err,connection){
        



    });
}





// select name from coffee_state where coffee_id = 1;

// select price from coffee_size where coffee_id = 1;

//module.exports = pool;