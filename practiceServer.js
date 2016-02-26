var http = require("http");
var mysql = require("mysql");
var url = require("url");
var qs = require("querystring");

var server = http.createServer();
var connection = mysql.createConnection({
	host : "localhost",
	user : "root",
	password : "Barkley20",
	database : "ebimayoPractice"
});

connection.connect();
server.on("request", response);
server.listen(10000);

console.log("server started");

function response(req, res){

	if(req.method == "POST"){
		var body = "";

		req.on("data",function(data){
			body += data;
		});

		req.on("end",function(){
			var post = qs.parse(body);

			if(post.purpose == 1){
				var sql = "UPDATE Circle SET comment='" + post.comment + "' WHERE circle_id=" + post.circle_id;
				console.log(sql);
				var sql_query = connection.query(sql);
				sql_query
					.on("error", function(err){
						console.log("err is: ", err);
					})
					.on("result", function(rows){
						console.log("result is: ",rows);
					})
					.on("end", function(){
						res.writeHead(200, {"Content-type": "text/plain"});
						res.write("ok");
						res.end();
						console.log("comment posted");
					});
			}
			else if(post.purpose == 2){
					var sql = "INSERT INTO event VALUES (" + post.circle_id + ", " + post.month + ", " + post.day + ", " + post.start_hour + ", " + post.start_min + ", " + post.finish_hour + ", " + post.finish_min + ", " + post.event_name + ", " + post.event_place + ", '" + post.comment + "')";
					console.log(sql);
					sql_query = connection.query(sql);
					sql_query
						.on("error", function(err){
						  console.log("err is: ", err);
						})
					  .on("result", function(rows){
							console.log("result is: ",rows);
						})
						.on("end", function(){
							res.writeHead(200, {"Content-type": "text/plain"});
							res.write("ok");
							res.end();
							console.log("comment posted");
						});
			}
			else if(post.purpose == 3){
				var sql = "UPDATE event SET month=" + post.month + ", day=" + post.day + ", start_hour=" + post.start_hour + ", start_min=" + post.start_min + ", finish_hour=" + post.finish_hour + ", finish_min=" + post.finish_min + ", event_name=" + post.event_name + ", event_place=" + post.event_place + ", comment='" + post.comment + "' WHERE event_id=" + post.event_id;
				console.log(sql);
				sql_query = connection.query(sql);
				sql_query
					.on("error", function(err){
					  console.log("err is: ", err);
					})
				  .on("result", function(rows){
						console.log("result is: ",rows);
					})
					.on("end", function(){
						res.writeHead(200, {"Content-type": "text/plain"});
						res.write("ok");
						res.end();
						console.log("comment posted");
					});
			}
			else if(post.purpose == 4){
				var sql = "DELETE FROM event WHERE event_id=" + post.event_id;
				console.log(sql);

				var sql_query = connection.query(sql);
				sql_query
					.on("error", function(err){
						console.log("err is: ", err);
					})
					.on("result", function(rows){
						console.log("result is: ", rows);
					})
					.on("end", function(){
						res.writeHead(200, {"Content-type": "text/plain"});
						res.write("ok");
						res.end();
						console.log("event deleted");
					});
			}
			else if(post.purpose == 5){
				var sql = "UPDATE image SET imgdata=" + post.imgdata + " WHERE circle_id=" + post.circle_id;
				console.log(sql);
				var sql_query = connection.query(sql);
				sql_query
					.on("error", function(err){
						console.log("err is: ", err);
					})
					.on("result", function(rows){
						console.log("result is: ", rows);
					})
					.on("end", function(){
						res.writeHead(200, {"Content-type": "text/plain"});
						res.write("ok");
						res.end();
						console.log("img updated");
					});
			}
		});
	}

	else if(req.method == "GET"){
		var url_parts = url.parse(req.url, true);
		var url_query = url_parts.query;
		var purpose = url_query.purpose;

		if(purpose == 1){
			var sql = "SELECT * FROM event WHERE circle_id=" + url_query.circle_id;
			res.writeHead(200, {"Content-type": "text/plain"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
				})
				.on("result", function(rows){
					res.write(""+rows.
				
		}
		else if(purpose == 2){
			var sql = "SELECT circle_id, circle_name FROM Circle";
			var category = url_query.category;
			var place = url_query.place;
			if(category + place > 0){
				sql = sql + " WHERE";
				if(category != 0){
					sql = sql + " category=" + category;
					if(place != 0){
						sql = sql + " AND place="+ place;
					}
				}else if(place != 0){
					sql = sql + " place=" + place;
				}
			}

			res.writeHead(200, {"Content-type": "text/plain"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
					})
				.on("result", function(rows){
					res.write(""+rows.circle_id);
					res.write(rows.circle_name);
					console.log(rows);
					})
				.on("end", function(){
					console.log("finish");
					res.end();
					});		
		}
		else if(purpose == 3){
			var sql = "SELECT * FROM Circle WHERE circle_id=" + url_query.circle_id;

			res.writeHead(200, {"Content-type": "text/plain"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
					})
				.on("result", function(rows){
					res.write(""+rows.circle_id);
					res.write(rows.circle_name);
					res.write(""+rows.category);
					res.write(""+rows.place);
					res.write(rows.comment);
					console.log(rows);
					})
				.on("end", function(){
					console.log("finish");
					res.end();
				});
		}
		else if(purpose == 4){
			var sql = "SELECT imgdata FROM image WHERE circle_id=" + url_query.circle_id;

			res.writeHead(200, {"Content-type": "image/jpeg"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
				})
				.on("result", function(rows){
					res.write(rows.imgdata);
					console.log(rows);
					console.log("imgdata sent");
				})
				.on("end", function(){
					console.log("finish");
					res.end();
				});
		}
	}
}
