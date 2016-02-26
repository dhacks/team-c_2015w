var http = require("http");
var mysql = require("mysql");

var server = http.createServer();
var connection = mysql.createConnection({
	host : "localhost",
	user : "root",
	password : "Barkley20",
	database : "PracticeForDHacks"
	});

server.on("request", response);
server.listen(8880);
connection.connect();

console.log("server started");

function response(req, res){
	var text = "";
  var getListSql = "SELECT * FROM text";
	var postListSql = "INSERT INTO text VALUES ('";
	
	console.log("something coming");
	if(req.method == "POST"){
		console.log("it was POST");
		addList();
		console.log("text is added");
	}else if(req.method == "GET"){
		console.log("it was GET");
		giveList();
		console.log("list is sent");
	}else{
		console.log("it was nothing");
	}

	function giveList(){
		res.writeHead(200, {"Content-type": "text/plain"});
		var query = connection.query(getListSql);
		query
			.on("error", function(err){
				console.log("err is: ", err);
			})
			.on("result", function(rows){
				res.write(rows.comment);
				console.log(rows.comment);
			})
			.on("end", function(){
				console.log("finish");
				res.end();
			});
	}

	function addList(){
		req.on("data", getData);
		req.on("end", addData);
	}

	function getData(chunk){
		text += chunk;
		console.log("chunk added");
	}

	function addData(){
		postListSql = postListSql + text + "')";
		console.log(postListSql);
		var query = connection.query(postListSql);
		query
			.on("error", function(err){
				console.log("err is: ", err);
				})
			.on("result", function(rows){
				console.log("The res is: ",rows);
				})
			.on("end", function(){
				console.log("finish");
				res.write("data properly added");
				res.end();
				});
		console.log("data added");
	}
}
	
