package com.athena.robot;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonParser {
    public static void main(String[] args) {
    String redURL="https://www.reddit.com/r/pics/top.json?limit=150&t=year";
    try{
    URL tempURL=new URL(redURL);
        HttpURLConnection connection= (HttpURLConnection) tempURL.openConnection();
        String redditUserAgent="project_robot 1.0 by /u/MisspelledPantheon";
        connection.addRequestProperty("User-Agent",redditUserAgent);
        connection.connect();
        InputStreamReader urlStream=new InputStreamReader((InputStream)connection.getContent());
        JSONParser jsonParser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonParser.parse(urlStream);
        System.out.println("Printing JSON object:::\n"+jsonObject.toString());
    }catch (Exception e){
        System.out.println("Error occured in "+e);
    }
    }

}
