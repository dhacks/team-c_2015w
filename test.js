var mysql = require("mysql");

var connection = mysql.createConnection({
	host : "localhost",
	user : "root",
	password : "Barkley20",
	database : "ebimayoPractice"
	});

var sql = "INSERT INTO Circle VALUES (5, '踊る', 3, 1, '一緒に踊りましょう')";

connection.connect();

var query = connection.query(sql);
console.log(sql);
query
	  .on('error', function(err) {
			console.log('err is: ', err );
			})
		.on('result', function(rows) {
			console.log('The res is: ', rows );
			 })
		.on('end', function() {
			console.log('end');
			connection.destroy();
			});
console.log("last");
