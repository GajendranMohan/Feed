<!DOCTYPE html>
<html>
<head>
<title>base</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body style="background-color: #F2F1EF">
	<div style="font-size: 30px; font-family: Cursive; text-align: center">
		<h1 style="color: #52B3D9">
			<Strong> Express your thoughts</strong>
		</h1>

	</div>
	
	<div id="userupdate" style="margin-top: 75px; height: 500px"
		class="col-md-3">
		<button id="fetchAllFeeds" style="margin-left: 40px"> All Feeds</button><br><br>
		<ul></ul>
		
	</div>
	<div style="margin-top: 75px" class="col-md-9">
		<textarea id="textarea" style="width: 650px; resize: none"
			placeholder="Enter The Message Here..." autofocus></textarea>
		<button id="update-btn" style="position: absolute; font-size: 26px">Update</button>
	</div>
	<br>
	<br>

	<p id="replacableTag" style="color:red;margin-top:100px;margin-left:395px">All User Feeds :</p>
	<div id="display"
		style="margin-left: 395px; width: 750px; height: 400px; padding: 10px; border: 5px dashed gray; overflow: scroll">

	</div>
	<div>
		<%
			String userName = request.getParameter("nme");
		%>
		<span id="spanid1"><%=userName%></span>
		<%
			String userMail = request.getParameter("mail");
		%>
		<%
			String userMailCopy = userMail;
			char[] tempVariable = userMailCopy.toCharArray();
			char[] userMailCharArray = new char[tempVariable.length - 2];
			for (int i = 0; i <= userMailCharArray.length - 2; i++) {
				userMailCharArray[i] = tempVariable[i];
			}
			char[] userMailCharArray1 = new char[userMailCharArray.length - 1];
			for (int i = 0; i < userMailCharArray.length - 2; i++) {
				userMailCharArray1[i] = userMailCharArray[i];
			}
		%>
		<span id="spanid2"><%=userMailCharArray1%> </span>
	</div>
	
	<script>

	
	var rex = /^[a-zA-Z0-9]/;
	
<!-- simultaneous ajax call for fetching the data as well as the logged in user details on document loading -->

	$(document).ready(function() {
		$('[data-toggle="tooltip"]').tooltip();
		
		$.ajax({
			type : "GET",
			url : "/fetchUpdates",
			success : function(text) {
				
					 var splitingTheResponse = text.split(",");
					var concatenatingVariable = "";
					parsingResponse=$.parseJSON(splitingTheResponse);
					for (i = 0; i < parsingResponse.length; i++) {
						
						var istDate = (new Date(parsingResponse[i].date)).toUTCString();	
						var date = new Date(istDate);
						var newIstDate = date.toString();
						newIstDate=newIstDate.split(' ').slice(0, 5).join(' ');
						concatenatingVariable += "<div><div>" + parsingResponse[i].message + "</div> <div>"+parsingResponse[i].userMail +"   "+ newIstDate +"</div><div id='{{id}}'><button class='btn btn-default likeButton' data-toggle='tooltip' data-placement='left' title="+parsingResponse[i].likedPeople+">Like</button><span class='likeSpan'>"+ parsingResponse[i].like +"</span></div></div><p></p>";
						concatenatingVariable=concatenatingVariable.replace("{{id}}",parsingResponse[i].id);
					}
					
					$("#display").html(concatenatingVariable);

				$.ajax({
					type : "GET",
					url : "/fetchFromLoginTable",
					success : function(response) {
						console.log(response);
						console.log($.parseJSON(response));
						 response=$.parseJSON(response);
 						for(var i=0;i<response.length;i++){
 							console.log(response[i].name +" "+ i);
 						response[i].name;
 						response[i].email;
 						//console.log(resp[i])
 						$("#userupdate ul").append('<li class="userMailClick" style="display:block;cursor:pointer">'+response[i].email+'<br><br></li>' );
 						
 						}
 					    $("#spanid1").hide();
 						$("#spanid2").hide();
						
					}

				});
			}

		});
		
		<!-- Script for counting the number of likes for particular feed  -->
		
		$(document).on("click",".likeButton",function(){
			var test;
			var passingMail=$("#spanid2").text();
			var x	= $(this).closest("div").attr("id");
				
				$.ajax({
					type : "GET",
					url : "http://localhost:8080/likeUpdate?passingId="+x+"&passingMail="+passingMail,	
					success : function(text) {
						text=$.parseJSON(text);
						$("#"+x+ " > button").attr("title",text.likedPeople);
						$("#"+x+ " > span").html(text.like);
						
										}


	});
		});	

		
		<!-- update buuton to store the entered data in to the datastore-->

		$("#update-btn").click(function() {
			var passingData=$("#textarea").val();
			var passingMail=$("#spanid2").text();
			var myLength = $("#textarea").val().length;
			console.log(myLength);
			if(rex.test(passingData) && myLength > 4){
			$.ajax({
				type : "GET",
				url : "http://localhost:8080/storingUpdate?passingData="+passingData+"&passingMail="+passingMail+"&passingLike="+0,	
				success : function(text) {
					$.ajax({
							type : "GET",
						url : "/fetchUpdates",
						success : function(text) {
							console.log(text);
								 var splitingTheResponse = text.split(",");
								var concatenatingVariable = "";
								parsingResponse=$.parseJSON(splitingTheResponse);
								for (i = 0; i < parsingResponse.length; i++) {
									
									var istDate = (new Date(parsingResponse[i].date)).toUTCString();	
									var date = new Date(istDate);
									var newIstDate = date.toString();
									newIstDate=newIstDate.split(' ').slice(0, 5).join(' ');
									concatenatingVariable += "<div><div>" + parsingResponse[i].message + "</div> <div>"+parsingResponse[i].userMail +"   "+ newIstDate + "</div><div id='{{id}}'><button class='btn btn-default likeButton'data-toggle='tooltip' data-placement='left' title="+parsingResponse[i].likedPeople+">Like</button><span class='likeSpan'>"+ parsingResponse[i].like +"</span></div></div><p></p>";
									concatenatingVariable=concatenatingVariable.replace("{{id}}",parsingResponse[i].id);
								}
								$("#display").html(concatenatingVariable);						
						}

});
				}

			});
			var prevMsg = $("#display").html();

			$("#display").html($("#textarea").val() + "<br>" + prevMsg);
			$("#textarea").val("");
			}else{
				alert("Plese check the input\n \nThe Length of the text should be greater than 5\n \nThe text should not start with the special character ");
				$("#textarea").val("");
			}
			

			
		});
		
<!-- clicking the logged in user to fetch there feeds alone-->
		
		$(document).on("click",".userMailClick",function() {
			var fetchId =$(this).text();
			var displayingVariable ="";
			$.ajax({
				type :"GET",
				url :"http://localhost:8080/fetchOnlyRespectiveText?passingFetchedId="+fetchId,
				success : function(response){	
					parsingResponse=$.parseJSON(response);
					if(parsingResponse.length == 0){
						$("#replacableTag").html("User Feed :")
						$("#display").html("OOPS...! User has not entered any feeds");
					}else{
					for (i = 0; i < parsingResponse.length; i++) {
						$("#replacableTag").html(" Respective User Feed :")
						var istDate = (new Date(parsingResponse[i].date)).toUTCString();	
						var date = new Date(istDate);
						var newIstDate = date.toString();
						newIstDate=newIstDate.split(' ').slice(0, 5).join(' ');
						displayingVariable +="<div>"+ parsingResponse[i].message + " :- " +newIstDate +"</div><div id='{{id}}'><button class='btn btn-default likeButton'data-toggle='tooltip' data-placement='left' title="+parsingResponse[i].likedPeople+">Like</button><span class='likeSpan'>"+ parsingResponse[i].like +"</span></div><p></p>";
						displayingVariable=displayingVariable.replace("{{id}}",parsingResponse[i].id);
					}
					
					$("#display").html(displayingVariable);
					}

				}
			}); 
        });
		
		<!-- button click part to display all the feeds entered by the user-->
		$("#fetchAllFeeds").click(function() {
			$.ajax({
				type : "GET",
				url : "/fetchUpdates",
				success : function(text) {
					$("#replacableTag").html("All User Feeds :")
						 var splitingTheResponse = text.split(",");
						var concatenatingVariable = "";
						parsingResponse=$.parseJSON(splitingTheResponse);
						for (i = 0; i < parsingResponse.length; i++) {
							var istDate = (new Date(parsingResponse[i].date)).toUTCString();	
							var date = new Date(istDate);
							var newIstDate = date.toString();
							newIstDate=newIstDate.split(' ').slice(0, 5).join(' ');
							concatenatingVariable +="<div><div>" + parsingResponse[i].message + "</div> <div>"+parsingResponse[i].userMail +"   "+ newIstDate +"</div><div id='{{id}}'><button class='btn btn-default likeButton'data-toggle='tooltip' data-placement='left' title="+parsingResponse[i].likedPeople+">Like</button><span class='likeSpan'>"+ parsingResponse[i].like +"</span></div></div><p></p>";
							concatenatingVariable=concatenatingVariable.replace("{{id}}",parsingResponse[i].id);
						}
						$("#display").html(concatenatingVariable);
				}
			});
		});
		
		setInterval(function(){
			$.ajax({
				type : "GET",
				url : "/fetchFromLoginTable",
				success : function(response) {
					var originalResponse=response;
					 response=$.parseJSON(originalResponse);
					
					 
					 if(response.length  != $(".userMailClick").length){
						for(var i=0;i<response.length;i++){
							console.log(response[i].name +" "+ i);
						response[i].name;
						response[i].email;
						var comparingResponse=response[i].email;
					if	(!(originalResponse==comparingResponse)){
						$("#userupdate ul").html("");
						$("#userupdate ul").append('<li class="userMailClick" style="display:block">'+response[i].email+'</li>' );
					}
						}
					 }
					    $("#spanid1").hide();
						$("#spanid2").hide();
					
				}

			});
			}, 30000)
			
		<!-- auto refresh for fetching the message ,it will incoke only after 3 minutes-->	
			setInterval(function(){
				$.ajax({
					type : "GET",
					url : "/fetchUpdates",
					success : function(text) {
						
							 var splitingTheResponse = text.split(",");
							var concatenatingVariable = "";
							parsingResponse=$.parseJSON(splitingTheResponse);
							for (i = 0; i < parsingResponse.length; i++) {
								
								var istDate = (new Date(parsingResponse[i].date)).toUTCString();	
								var date = new Date(istDate);
								var newIstDate = date.toString();
								newIstDate=newIstDate.split(' ').slice(0, 5).join(' ');
								concatenatingVariable += "<div><div>" + parsingResponse[i].message + "</div> <div>"+parsingResponse[i].userMail +"   "+ newIstDate +"</div><div id='{{id}}'><button class='btn btn-default likeButton'data-toggle='tooltip' data-placement='left' title="+parsingResponse[i].likedPeople+">Like</button><span class='likeSpan'>"+ parsingResponse[i].like +"</span></div></div><p></p>";;
								concatenatingVariable=concatenatingVariable.replace("{{id}}",parsingResponse[i].id);
							}
							$("#display").html(concatenatingVariable);
					}
				});
}, 180000)
		

	});
</script>

</body>
</html>