package com.qa.tests;

import java.io.IOException;
import java.util.HashMap;

import com.qa.parameter.Users;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qa.base.TestBase;
import com.qa.restclient.RestClient;
import com.qa.util.TestUtil;


public class GetApiTest extends TestBase{
    final static Logger Log = Logger.getLogger(GetApiTest.class);
    TestBase testBase;
    String host;
    String url;
    RestClient restClient;
    CloseableHttpResponse closeableHttpResponse;

    @BeforeClass
    public void setUp() {
        testBase = new TestBase();
        host = prop.getProperty("HOST");
        Log.info("测试服务器地址为："+ host.toString());
        url = host + "/api/users";
        Log.info("当前测试接口的完整地址为："+url.toString());
    }

    @Test
    public void getAPITest() throws ClientProtocolException, IOException {
        Log.info("开始执行用例...");
        restClient = new RestClient();
        closeableHttpResponse = restClient.get(url);
        int statusCode = restClient.getStatusCode(closeableHttpResponse);
        Assert.assertEquals(statusCode,RESPONSE_STATUS_CODE_200, "response status code is not 200");
        JSONObject responseJson = restClient.getResponseJson(closeableHttpResponse);
        String s = TestUtil.getValueByJPath(responseJson,"data[0]/first_name");
        Log.info("执行JSON解析，解析的内容是 " + s);
        Log.info("接口内容响应断言");
        Assert.assertEquals(s, "George","first name is not George");
        Log.info("用例执行结束...");
    }

    @Test
    public void postAPITest() throws ClientProtocolException, IOException {
        Log.info("开始执行用例...");
        restClient = new RestClient();
        HashMap<String,String> headermap = new HashMap<String,String>();
        headermap.put("Content-Type", "application/json");
        Users user = new Users("Nicole","HR");
        String userJsonString = JSON.toJSONString(user);
        closeableHttpResponse = restClient.post(url,userJsonString,headermap);
        int statusCode = restClient.getStatusCode(closeableHttpResponse);
        Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_201,"status code is not 201");
        JSONObject responseJson = restClient.getResponseJson(closeableHttpResponse);
        String name = TestUtil.getValueByJPath(responseJson, "name");
        String job = TestUtil.getValueByJPath(responseJson, "job");
        Log.info("执行JSON解析，解析的内容是 " + name + " " + job);
        Log.info("接口内容响应断言");
        Assert.assertEquals(name, "Nicole","name is not same");
        Assert.assertEquals(job, "HR","job is not same");
        Log.info("用例执行结束...");
    }
}
