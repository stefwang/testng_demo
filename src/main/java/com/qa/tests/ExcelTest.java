package com.qa.tests;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qa.base.TestBase;
import com.qa.parameter.Users;
import com.qa.restclient.RestClient;
import com.qa.util.TestUtil;
import org.apache.log4j.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

import static com.qa.util.TestUtil.dtt;

public class ExcelTest extends TestBase {

    final static Logger Log = Logger.getLogger(GetApiTest.class);
    TestBase testBase;
    RestClient restClient;
    CloseableHttpResponse closeableHttpResponse;
    String host;
    String testCaseExcel;
    HashMap<String ,String> Header = new HashMap<String, String>();

    @BeforeClass
    public void setUp(){
        testBase = new TestBase();
        restClient = new RestClient();
        Header.put("Content-Type","application/json");
        host = prop.getProperty("HOST");
        testCaseExcel=prop.getProperty("testCaseData");
    }

    @DataProvider(name = "post")
    public Object[][] post() throws IOException {
        return dtt(testCaseExcel,0);
    }

    @DataProvider(name = "get")
    public Object[][] get() throws IOException{
        //get类型接口
        return dtt(testCaseExcel,1);
    }

    @Test(dataProvider = "post")
    public void postApi(String url,String name, String job) throws Exception {
        Log.info("开始执行用例...");
        restClient = new RestClient();
        Log.info("url: " + host + url);
        Users user = new Users(name, job);
        String userJsonString = JSON.toJSONString(user);
        closeableHttpResponse = restClient.post(host+url,userJsonString,Header);
        int statusCode = restClient.getStatusCode(closeableHttpResponse);
        Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_201, "response status code is not 201");
        JSONObject responseJson = restClient.getResponseJson(closeableHttpResponse);
        String n = TestUtil.getValueByJPath(responseJson, "name");
        String j = TestUtil.getValueByJPath(responseJson, "job");
        Log.info("执行JSON解析，解析的内容是 " + n + " " + j);
        Log.info("接口内容响应断言");
        Assert.assertEquals(n, "Nicole","name is not same");
        Assert.assertEquals(j, "HR","job is not same");
        Log.info("用例执行结束...");
    }

    @Test(dataProvider = "get")
    public void getApi(String url) throws Exception {
        Log.info("开始执行用例...");
        restClient = new RestClient();
        Log.info("url: " + host + url);
        closeableHttpResponse = restClient.get(host + url);
        int statusCode = restClient.getStatusCode(closeableHttpResponse);
        Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "response status code is not 200");
        JSONObject responseJson = restClient.getResponseJson(closeableHttpResponse);
        String s = TestUtil.getValueByJPath(responseJson, "data[0]/first_name");
        Log.info("执行JSON解析，解析的内容是 " + s);
        Assert.assertEquals(s, "George", "first name is not George");
        Log.info("用例执行结束...");
    }
}
