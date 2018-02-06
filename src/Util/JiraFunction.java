package Util;

import javax.security.sasl.AuthenticationException;

import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

import Configuration.Constant;
import TestRunner.TestCaseRunner;

public class JiraFunction {
	static String logmessage;
	static String auth;
	static String DefectID = "Blank";

	public void JiraLogin() throws AuthenticationException {

		auth = new String(Base64.encode(Constant.JiraUserName + ":" + Constant.JiraPassword));

		// System.out.println(auth);

		Client client = Client.create();
		WebResource webResource = client.resource(Constant.JiraBaseURL);
		// System.out.println(Constant.JiraBaseURL);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").get(ClientResponse.class);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
		// String response1 = response.getEntity(String.class);

		// System.out.println("My Response is " + statusCode);
	}

	private static String PostData(String auth, String url, String data) throws Exception {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").post(ClientResponse.class, data);

		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
		// System.out.println("statusCode is: " + statusCode);
		String returnstring;
		if (statusCode == 201) {
			returnstring = response.getEntity(String.class);
		} else {
			returnstring = null;
			;
		}
		return returnstring;
	}

	public static String Createdefect() throws Exception {
		DefectID = null;
		JiraFunction jersyClientJira = new JiraFunction();

		try {
			jersyClientJira.JiraLogin();
			// jersyClientJira.invokePostMethod(auth,"http://localhost;8080/projects/TEST",""
			// );

			// String createIssueData =
			// "{\"fields\":{\"project\":{\"key\":\"TEST\"},\"summary\":\"REST Test\",\"issuetype\":{\"name\":\"Task\"}}}";
			// String createIssueData =
			// "{\"fields\":{\"project\":{\"key\":\"MBJIR\"},\"summary\":\"REST Test\",\"issuetype\":{\"name\":\"Bug\"}}}";
			String createIssueData = JsonBuilder();
			// System.out.println(createIssueData );
			// System.exit(-1);

			String ResponseJson = PostData(auth, Constant.JiraBaseURL + "/rest/api/2/issue/", createIssueData);

			if (ResponseJson == null) {
			} else {
				JSONObject obj = new JSONObject(ResponseJson);
				obj.getString("self");
				DefectID = obj.getString("key");
				obj.getInt("id");

				logmessage = "Defect:" + DefectID + " has been raised in Jira for"
						+ ExcelUtils.getCellData(Constant.testcaserownum, Constant.Testcaseid) + ": "
						+ ExcelUtils.getCellData(Constant.testcaserownum, Constant.TestStepID) + "; "
						+ ExcelUtils.getCellData(Constant.testcaserownum, Constant.TeststepDescription)
						+ " Please refer " + Constant.JiraBaseURL + "/Browse/" + DefectID + " For more detail"
						+ "; Action: Drop Down Select";
			}
			// System.out.println(ResponseJson);
			// int statusCode = response.getStatus();

			// JsonObjectParser issueObj = new JSONObjectProvider(issue);
			// String newKey = issueObj.getString("key");
			// System.out.println("Key:"+newKey);

		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Constant.JiraBaseURL + "/Browse/" + DefectID;
	}

	public static String JsonBuilder() throws Exception {

		String jsonString = new JSONObject()
				.put("fields", new JSONObject().put("project", new JSONObject().put("key", Constant.JiraProjectName))
						.put("summary", "Automation Failed at "
								+ ExcelUtils.getCellData(TestCaseRunner.testScriptIndex, Constant.Testcaseid)
								+ ExcelUtils.getCellData(TestCaseRunner.testScriptIndex, Constant.TestStepID) + ": "
								+ ExcelUtils.getCellData(TestCaseRunner.testScriptIndex, Constant.TeststepDescription))
				.put("description", SteptoReproduce()).put("issuetype", new JSONObject().put("name", "Bug")))
				.toString();

		// System.out.println(jsonString);
		return jsonString;
	}

	public static String SteptoReproduce() throws Exception {

		String StepstoReproduce = " ";
		int i = 1, j = 1;
		// System.out.println(TestCaseRunner.iRow);
		while (i < TestCaseRunner.testScriptIndex) {
			if (ExcelUtils.getCellData(i, Constant.Action).contains("ThreadWait")) {

			} else {
				StepstoReproduce = StepstoReproduce + "\r\n" + "Step" + j + ": "
						+ ExcelUtils.getCellData(i, Constant.TeststepDescription);
				j++;
			}
			i++;
			// System.out.println(i+StepstoReproduce);
		}
		StepstoReproduce = StepstoReproduce + "\r\n\r\n" + "Actual result: " + "Automation Failed at "
				+ ExcelUtils.getCellData(TestCaseRunner.testScriptIndex, Constant.Testcaseid)
				+ ExcelUtils.getCellData(TestCaseRunner.testScriptIndex, Constant.TestStepID) + ": "
				+ ExcelUtils.getCellData(TestCaseRunner.testScriptIndex, Constant.TeststepDescription);
		StepstoReproduce = StepstoReproduce + "\r\n\r\n" + "Expected result: " + "Following step "
				+ ExcelUtils.getCellData(TestCaseRunner.testScriptIndex, Constant.TestStepID) + ": "
				+ ExcelUtils.getCellData(TestCaseRunner.testScriptIndex, Constant.TeststepDescription)
				+ " Should passed";
		// System.out.println(StepstoReproduce);
		return StepstoReproduce;
	}
}
