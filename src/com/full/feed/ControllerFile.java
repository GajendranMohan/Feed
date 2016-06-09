package com.full.feed;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@SuppressWarnings("unused")
@Controller
public class ControllerFile {

	@RequestMapping(value = "/login_google")
	public ModelAndView go() {
		return new ModelAndView(
				"redirect:https://accounts.google.com/o/oauth2/auth?redirect_uri=http://1-dot-feedsystem-1334.appspot.com/get_authz_code&response_type=code&client_id=10260336902-1d6k11lto0ng1qlt64ujdpefreiv6ldp.apps.googleusercontent.com&approval_prompt=force&scope=email&access_type=online");
	}

	@RequestMapping(value = "/get_authz_code")
	public ModelAndView get_authorization_code(@RequestParam String code, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		// code for getting authorization_code

		System.out.println("successfully came back...............");
		String auth_code = code;
		System.out.println(auth_code);

		// Code for getting access token from the authorization_code.....

		URL url = new URL("https://www.googleapis.com/oauth2/v3/token?"
				+ "client_id=10260336902-1d6k11lto0ng1qlt64ujdpefreiv6ldp.apps.googleusercontent.com"
				+ "&client_secret=XKZUtW4qzzeBWVq9kd58YzSy&" + "redirect_uri=http://1-dot-feedsystem-1334.appspot.com/get_authz_code&"
				+ "grant_type=authorization_code&" + "code=" + auth_code);
		HttpURLConnection connect = (HttpURLConnection) url.openConnection();
		connect.setRequestMethod("POST");
		connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connect.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
		String inputLine;
		String response = "";
		while ((inputLine = in.readLine()) != null) {
			response += inputLine;
		}
		in.close();
		System.out.println(response.toString());
		JSONParser parser = new JSONParser();
		JSONObject json_access_token = null;
		try {
			json_access_token = (JSONObject) parser.parse(response);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		// String access_token="";
		String access_token = (String) json_access_token.get("access_token");
		System.out.println("Access token =" + access_token);
		System.out.println("access token caught");

		// code for getting user details by sending access token.......

		URL obj1 = new URL("https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + access_token);
		HttpURLConnection conn = (HttpURLConnection) obj1.openConnection();
		BufferedReader in1 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine1;
		String responsee = "";
		while ((inputLine1 = in1.readLine()) != null) {
			responsee += inputLine1;
		}
		in1.close();
		System.out.println(responsee.toString());
		JSONObject json_user_details = null;
		try {
			json_user_details = (JSONObject) parser.parse(responsee);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}

		// code for sending accessed user details to display.jsp

		String userEmail = (String) json_user_details.get("email");
		String userName = (String) json_user_details.get("name");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (userEmail != null) {
			Query q = pm.newQuery(LoginTable.class);
			q.setFilter(" email == '" + userEmail + "'");
			@SuppressWarnings("unchecked")
			List<LoginTable> loginTableData = (List<LoginTable>) q.execute();
			if (!(loginTableData.isEmpty())) {
				System.out.println("to prevent from null");
			} else {
				LoginTable objPojo = new LoginTable();
				objPojo.setEmail(userEmail);
				objPojo.setUname(userName);

				pm.makePersistent(objPojo);
			}
		}
		
		return new ModelAndView(
				"viewPage.jsp?nme=" + json_user_details.get("name") + "&mail=" + json_user_details.get("email"));

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchUpdates")
	@ResponseBody
	public String fetchFromUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		javax.jdo.Query q = pm.newQuery(UpdateContents.class);
		q.setOrdering("date desc");

		String returningVariable = "";
		
			List<UpdateContents> updateContentsData = (List<UpdateContents>) q.execute();				
			Gson obj = new Gson();
					returningVariable = obj.toJson(updateContentsData);
		 
		return returningVariable;
	}

	@RequestMapping(value = "/fetchFromLoginTable", method = { RequestMethod.GET })
	@ResponseBody
	public String fetchFromLoginTable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(LoginTable.class);
		@SuppressWarnings("unchecked")
		List<LoginTable> LoginTableData = (List<LoginTable>) q.execute();
	
		Gson obj = new Gson();
		return obj.toJson(LoginTableData);

	}

	@RequestMapping(value = "/storingUpdate", method = { RequestMethod.GET })
	@ResponseBody
	public String toPersistUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String message = req.getParameter("passingData");
		String mail = req.getParameter("passingMail").trim();
		long uniqueId = 0;
		int likeCount=Integer.parseInt(req.getParameter("passingLike"));
		Date date = new Date();
		long milliSecondTime= date.getTime();
		long count;
		Random randomNumber =new Random();
		for (count=0;count<1;count++){
			 uniqueId=randomNumber.nextInt(123456);
		}
		if (message != "") {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			UpdateContents content = new UpdateContents();
			content.setMessage(message);
			content.setDate(milliSecondTime);
			content.setMail(mail);
			content.setLike(likeCount);
			content.setId(uniqueId);
			pm.makePersistent(content);
		}
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(LoginTable.class);
		@SuppressWarnings("unchecked")
		List<LoginTable> LoginTableData = (List<LoginTable>) q.execute();
	
		Gson obj = new Gson();
		return obj.toJson(LoginTableData);


	}

	@RequestMapping(value = "/likeUpdate", method = { RequestMethod.GET })
	@ResponseBody
	public String toPersistLikeUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String mailId = req.getParameter("passingMail");
		int userId=Integer.parseInt(req.getParameter("passingId"));
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(UpdateContents.class);
		q.setFilter("id == nameParameter");
		q.declareParameters("int nameParameter");

			List<UpdateContents> UpdateContentsData = (List<UpdateContents>) q.execute(userId);
				UpdateContents likeData=UpdateContentsData.get(0);
				List<String> listOfLikedPeople = likeData.getLikedPeople();
				System.out.println(listOfLikedPeople);
				if(listOfLikedPeople.size()==0){
					listOfLikedPeople.add(mailId);
					likeData.setLikedPeople(listOfLikedPeople);
					likeData.setLike((likeData.getLike())+1);
					pm.makePersistent(likeData);
				}else{
					if(listOfLikedPeople.contains(mailId)){
						
					}else{
						listOfLikedPeople.add(mailId);
					likeData.setLikedPeople(listOfLikedPeople);
					likeData.setLike((likeData.getLike())+1);
					pm.makePersistent(likeData);
					}
				}
		
		
		Gson obj = new Gson();
		String returningVariable = obj.toJson(likeData.getLike());
		return returningVariable;
		
	}

	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchOnlyRespectiveText", method = { RequestMethod.GET })
	@ResponseBody
	public String fetchRespectiveText(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		String comparingId = req.getParameter("passingFetchedId");
		System.out.println(comparingId);
		
		Query q = pm.newQuery(UpdateContents.class);
		String returningVariable = "";
		q.setFilter("userMail == '" + comparingId + "'");
		List<UpdateContents> updateContentsData = (List<UpdateContents>) q.execute();				
		Gson obj = new Gson();
				returningVariable = obj.toJson(updateContentsData);
	 
	return returningVariable;
	}
}
