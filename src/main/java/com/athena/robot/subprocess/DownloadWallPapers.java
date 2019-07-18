package com.athena.robot.subprocess;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import javax.imageio.*;

public class DownloadWallPapers {
    private static final Logger log= LogManager.getLogger(DownloadWallPapers.class);
       public void downloadWallPapers(int wallpapersPerSub, List<String> subNames,String duration,String wallPaperFolderPath){
        String redditBaseURL="https://www.reddit.com/";
        String limiter="/top.json?limit="+wallpapersPerSub+"&t="+duration;
        for (String subName:
            subNames ) {
            //condition to add r/ infront of subrreddit name if doesnt have it
            if(!subName.startsWith("r/")){
                subName="r/"+subName;
            }
            String redditTempUrl=redditBaseURL+subName+limiter;
            System.out.println("Temp URL is "+redditTempUrl);
            boolean wallpaperDownloadSuccess=buildConnectionToUrl(redditTempUrl,wallPaperFolderPath);
            if (!wallpaperDownloadSuccess){
                System.err.println("There is a error occurred in the download check logs for more information");
            }
        }
    }

    private boolean buildConnectionToUrl(String redditTempUrl, String wallPaperFolderPath) {
        boolean isProcessSuccess=false;
        HttpURLConnection redditTempConnection=null;
        try {
            URL redditTemporaryUrl=new URL(redditTempUrl);
             redditTempConnection= (HttpURLConnection) redditTemporaryUrl.openConnection();
            String redditUserAgent="project_robot 1.0 by /u/MisspelledPantheon";
            redditTempConnection.addRequestProperty("User-Agent",redditUserAgent);
            redditTempConnection.connect();
            InputStreamReader urlStream=new InputStreamReader((InputStream)redditTempConnection.getContent());
            JSONParser jsonParser=new JSONParser();
            JSONObject redditTempDataObject=(JSONObject) jsonParser.parse(urlStream);
            JSONObject cleanedURLJsonObject=CleanJsonData(redditTempDataObject);
            //The below method will use the folder and download the images there.
            downloadImagesToFolder(cleanedURLJsonObject,wallPaperFolderPath);
            isProcessSuccess=true;
        }catch (Exception e){
            System.err.println("Error occured in building connection "+e);
        }finally {
            redditTempConnection.disconnect();
        }

        return isProcessSuccess;
    }

    private static void downloadImagesToFolder(JSONObject cleanedURLJsonObject, String wallPaperFolderPath) {
        //Iterating is done here other saveImageToFolder will take care of the downloads and saving it to folder
        for (Object key:cleanedURLJsonObject.keySet()) {
            String tempImageURL= (String) cleanedURLJsonObject.get(key);
            String name=(String) key;
            saveImageToFolder(name,tempImageURL,wallPaperFolderPath);
        }
    }

    private static void saveImageToFolder(String name, String tempImageURL, String wallPaperFolderPath) {
            Image temImage=null;
            try {
                URL tempDownloadUrl=new URL(tempImageURL);
                temImage=ImageIO.read(tempDownloadUrl);
                BufferedImage bufferedImage= (BufferedImage) temImage;
                name=name.replaceAll("[^A-Za-z0-9]","");
                String newImagePath=wallPaperFolderPath+"\\"+name+".jpg";
                File wallPaperImageFile=new File(newImagePath);
                ImageIO.write(bufferedImage,"jpg",wallPaperImageFile);

            }catch (Exception e){

            }
    }

    private static JSONObject CleanJsonData(JSONObject jsonObject) {
        JSONObject cleanedJSONData=new JSONObject();
       jsonObject=(JSONObject)jsonObject.get("data");
        JSONArray childrenArray=(JSONArray) jsonObject.get("children");
        Iterator it=childrenArray.iterator();
        while(it.hasNext()){
            new JSONObject();
            JSONObject tempChildJSONDataObject = (JSONObject) it.next();
            tempChildJSONDataObject=(JSONObject) tempChildJSONDataObject.get("data");
            String name=(String)tempChildJSONDataObject.get("permalink");
            name=name.replace("/r/pics/comments/","");
            String sourceURL=(String) tempChildJSONDataObject.get("url");
            cleanedJSONData.put(name,sourceURL);
        }
        System.out.println("Cleaned JSONData \n"+cleanedJSONData.toString());
        return cleanedJSONData;
    }

}
