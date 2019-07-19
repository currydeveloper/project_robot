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
            log.trace("Starting the iteration for the sub ::"+subName);
            if(!subName.startsWith("r/")){
                subName="r/"+subName;
            }
            String redditTempUrl=redditBaseURL+subName+limiter;
            log.trace("Setup the URL as the "+redditTempUrl);
            boolean wallpaperDownloadSuccess=buildConnectionToUrl(redditTempUrl,wallPaperFolderPath);
            if (!wallpaperDownloadSuccess){
                log.error("There is a error occurred in the download check logs for more information");
            }
        }
    }

    private boolean buildConnectionToUrl(String redditTempUrl, String wallPaperFolderPath) {
           log.trace("Starting the method for the connection building for intial scraping");
        boolean isProcessSuccess=false;
        HttpURLConnection redditTempConnection=null;
        try {
            log.trace("Starting the try block");
            URL redditTemporaryUrl=new URL(redditTempUrl);
             redditTempConnection= (HttpURLConnection) redditTemporaryUrl.openConnection();
            String redditUserAgent="project_robot 1.0 by /u/MisspelledPantheon";
            redditTempConnection.addRequestProperty("User-Agent",redditUserAgent);
            redditTempConnection.connect();
            log.trace("Setup the connection for the rule");
            InputStreamReader urlStream=new InputStreamReader((InputStream)redditTempConnection.getContent());
            JSONParser jsonParser=new JSONParser();
            JSONObject redditTempDataObject=(JSONObject) jsonParser.parse(urlStream);
            log.trace("got the initial JSON Dump as "+redditTempDataObject.toString());
            //TODO Add a condition to check the number of url obtained matches to request and fix it.
            log.debug("calling the Clean method to clean the data");
            JSONObject cleanedURLJsonObject=CleanJsonData(redditTempDataObject);
            //The below method will use the folder and download the images there.
            log.debug("calling the image download method by the passing the wallpaper path and cleanedJsonobject "+cleanedURLJsonObject);
            downloadImagesToFolder(cleanedURLJsonObject,wallPaperFolderPath);
            isProcessSuccess=true;
        }catch (Exception e){
            log.error("Error occured in building connection "+e);
        }finally {
            log.trace("disconnecting the connection");
            redditTempConnection.disconnect();
        }

        return isProcessSuccess;
    }

    private static void downloadImagesToFolder(JSONObject cleanedURLJsonObject, String wallPaperFolderPath) {
        //Iterating is done here other saveImageToFolder will take care of the downloads and saving it to folder
        log.trace("Starting the download image to folder method");
        for (Object key:cleanedURLJsonObject.keySet()) {
            log.trace("Starting the for loop for JSON object iteration");
            String tempImageURL= (String) cleanedURLJsonObject.get(key);
            String name=(String) key;
            log.trace("Calling the Image saving method with name passed as "+name+" and URL as "+tempImageURL+ " folder as "+wallPaperFolderPath);
            saveImageToFolder(name,tempImageURL,wallPaperFolderPath);
        }
    }

    private static void saveImageToFolder(String name, String tempImageURL, String wallPaperFolderPath) {
           log.trace("Starting the method for the saving the image file.");
            Image temImage=null;
            try {
                log.trace("Starting the file saving try block");
                URL tempDownloadUrl=new URL(tempImageURL);
                temImage=ImageIO.read(tempDownloadUrl);
                BufferedImage bufferedImage= (BufferedImage) temImage;
                log.info("before name modification "+name);
                name=name.replaceAll("[^A-Za-z0-9]","");
                log.info("After name modification cleanup"+name);
                String newImagePath=wallPaperFolderPath+"\\"+name+".jpg";
                File wallPaperImageFile=new File(newImagePath);
                ImageIO.write(bufferedImage,"jpg",wallPaperImageFile);

            }catch (Exception e){
                log.error("Error Occured in the download"+e);
            }
    }

    /**
     * @param jsonObject
     * @description The Method will clean the reddit JSON Object Passed and will return a clean JSON with the name and URL of the image.
     * @return JSONObject
     */
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
