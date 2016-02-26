var http = require("http");
var mysql = require("mysql");
var url = require("url");
var qs = require("querystring");

var server = http.createServer();
var connection = mysql.createConnection({
	host : "localhost",
	user : "root",
	password : "Barkley20",
	database : "ebimayo"
});

connection.connect();
server.on("request", response);
server.listen(10000);
console.log("server started");

function response(req, res){
	console.log("something came");
	if(req.method == "GET"){
		var url_parts = url.parse(req.url, true);
		var url_query = url_parts.query;

		var purpose = url_query.purpose;
		if(purpose == 1){
			var sql = "SELECT * FROM event WHERE circle_id=" + url_query.circle_id;
			console.log(sql);
			res.writeHead(200, {"Content-type": "text/plain"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
				})
				.on("result", function(rows){
					res.write("" + rows.month + '\n');
					res.write("" + rows.day + '\n');
					res.write(rows.start_time + '\n');
					res.write(rows.event_place + '\n');
					res.write(rows.event_name + '\n');
					res.write(rows.comment + '\n');
					console.log(rows);
				})
				.on("end", function(){
					res.end();
					console.log("event info sent");
				});
		}
		else if(purpose == 2){
			var sql = "SELECT circle_id, circle_name, category, school FROM circle";
			var category = url_query.category;
			var school = url_query.school;
			if(category + school > 0){
				sql = sql + " WHERE";
				if(category != 0){
					sql = sql + " category=" + category;
					if(school != 0){
						sql = sql + " AND school=" + school;
					}	
				}else if(school != 0){
					sql = sql + " school=" + school;
				}
			}
			console.log(sql);	
			res.writeHead(200, {"Content-type": "text/plain"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
				})
				.on("result", function(rows){
					res.write("" + rows.circle_id + '\n');
					res.write(rows.circle_name + '\n');
					res.write("" + rows.category + '\n');
					res.write("" + rows.school + '\n');
					console.log(rows);
				})
				.on("end", function(){
					res.end();
					console.log("search result sent");
				});
		}
		else if(purpose == 3){
			var sql = "SELECT * FROM circle WHERE circle_id=" + url_query.circle_id;
			console.log(sql);
			res.writeHead(200, {"Content-type": "text/plain"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
				})
				.on("result", function(rows){
					res.write(rows.circle_name + '\n');
					res.write("" + rows.category + '\n');
					res.write("" + rows.school + '\n');
					res.write(rows.place + '\n');
					res.write(rows.activity_date + '\n');
					res.write(rows.start_time + '\n');
					res.write(rows.end_time + '\n');
					res.write("" + rows.member_num + '\n');
					res.write(rows.mail_address + '\n');
					res.write(rows.comment + '\n');
					console.log(rows);
				})
				.on("end", function(){
					res.end();
					console.log("circle info sent");
				});
		}
		else if(purpose == 4){
			var sql = "SELECT imgdata FROM image WHERE circle_id=" + url_query.circle_id;
			console.log(sql);
			res.writeHead(200, {"Content-type": "image/png"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
				})
				.on("result", function(rows){
					res.write(rows.imgdata);
					console.log(rows);
				})
				.on("end", function(){
					res.end();
					console.log("imgdata sent");
				});
		}
		else if(purpose == 5){
			var sql = "SELECT circle_id, login_password FROM security WHERE login_id='" + url_query.login_id + "'";
			console.log(sql);
			res.writeHead(200, {"Content-type": "text/plain"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
				})
				.on("result", function(rows){
					res.write(rows.login_password + '\n');
					res.write(rows.circle_id + '\n');
				})
				.on("end", function(){
					res.end();
					console.log("password sent");
				});
		}
		else if(purpose == 6){
			var sql = "SELECT circle_name, category, school FROM circle WHERE circle_id=" + url_query.circle_id;
			console.log(sql);
			res.writeHead(200, {"Content-type": "text/plain"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
				})
				.on("result", function(rows){
					console.log(rows);
					res.write(rows.circle_name + '\n');
					res.write("" + rows.category + '\n');
					res.write("" + rows.school + '\n');
				})
				.on("end", function(){
					res.end();
					console.log("favorite circle sent");
				});
		}
		else if(purpose == 7){
			var sql = "SELECT * FROM event WHERE circle_id=" + url_query.circle_id;
			console.log(sql);
			res.writeHead(200, {"Content-type": "text/plain"});
			var sql_query = connection.query(sql);
			sql_query
				.on("error", function(err){
					console.log("err is: ", err);
				})
				.on("result", function(rows){
					console.log(rows);
					res.write("" + rows.event_id + '\n');
					res.write("" + rows.month + '\n');
					res.write("" + rows.day + '\n');
					res.write(rows.start_time + '\n');
					res.write(rows.event_place + '\n');
					res.write(rows.event_name + '\n');
					res.write(rows.comment + '\n');
				})
				.on("end", function(){
					res.end();
					console.log("event for circle sent");
				});
		}
	}
	else if(req.method == "POST"){
		var body = "";

		req.on("data", function(data){
			body += data;
		});

		req.on("end", function(){
			var post = qs.parse(body);
			var purpose = post.purpose;

			if(purpose == 1){
				var sql = "UPDATE circle SET school=" + post.school + ", place='" + post.place + "', activity_date='" + post.activity_date + "', start_time='" + post.start_time + "', end_time='" + post.end_time + "', comment='" + post.comment + "' WHERE circle_id=" + post.circle_id;
				console.log(sql);
				sql_query = connection.query(sql);
				sql_query
					.on("error", function(err){
						console.log("err is: ", err);
					})
					.on("result", function(rows){
						console.log(rows);
					})
					.on("end", function(){
						res.writeHead(200, {"Content-type": "text/plain"});
						res.write("ok");
						res.end();
						console.log("circle page updated");
					});
			}
			else if(purpose == 2){
				var sql = "INSERT INTO event VALUES (" + post.circle_id + ", " + post.month + ", " + post.day + ", '" + post.start_time + "', '" + post.event_place + "', '" + post.event_name + "', '" + post.comment + "'); SELECT LAST_INSERT_ID();";
				console.log(sql);
				res.writeHead(200, {"Content-type": "text/plain"});
				sql_query = connection.query(sql);
				sql_query
					.on("error", function(err){
						console.log("err is: ", err);
					})
					.on("result", function(rows){
						res.write("" + rows.event_id);
						console.log(rows);
					})
					.on("end", function(){
						res.end();
						console.log("circle page renewed");
					});
			}
			else if(purpose == 3){
				var sql = "UPDATE event SET month=" + post.month + ", day=" + post.day + ", start_time='" + post.start_time + "', event_place='" + post.event_place + "', event_name='" + post.event_name + "', comment='" + post.comment + "' WHERE event_id=" + post.event_id;
				console.log(sql);
				sql_query = connection.query(sql);
				sql_query
					.on("error", function(err){
						console.log("err is: ", err);
					})
					.on("result", function(rows){
						console.log(rows);
					})
					.on("end", function(){
						res.writeHead(200, {"Content-type": "text/plain"});
						res.write("ok");
						res.end();
						console.log("event updated");
					});
			}
			else if(purpose == 4){
				var sql = "DELETE FROM event WHERE event_id=" + post.event_id;
				console.log(sql);
				var sql_query = connection.query(sql);
				sql_query
					.on("error", function(err){
						console.log("err is: ", err);
					})
					.on("result", function(rows){
						console.log(rows);
					})
					.on("end", function(){
						res.writeHead(200, {"Content-type": "text/plain"});
						res.write("ok");
						res.end();
						console.log("event deleted");
					});
			}
			else if(purpose == 5){
				console.log(post.imgdata + "fin");
				var tmp = post.imgdata;
				var str = tmp.toString();
				console.log(str + "!!!");
				var cha = str.replace(/ /g, '+');
				console.log(cha + "yeaaaah");
				var sss = new Buffer(cha, 'base64');
				console.log(sss + "brison");
				var ssss = "0x" + sss.toString('hex');
				console.log(ssss + "ian");
				var sql = "UPDATE image SET imgdata=" + ssss + " WHERE circle_id=" + post.circle_id;
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
}
