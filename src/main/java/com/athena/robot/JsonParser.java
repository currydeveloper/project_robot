package com.athena.robot;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class JsonParser {
    public static void main(String[] args) {
    String redURL="https://www.reddit.com/r/pics/top.json?limit=100&t=year";
    try{
    URL tempURL=new URL(redURL);
        InputStream urlStream=tempURL.openStream();
        JSONParser jsonParser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonParser.parse(new InputStreamReader(urlStream));
        System.out.println("Printing JSON object"+jsonObject.toString());
    }catch (Exception e){
        System.out.println("Error occured in "+e);
    }
    }

}
