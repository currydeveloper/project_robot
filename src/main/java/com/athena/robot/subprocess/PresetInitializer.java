package com.athena.robot.subprocess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class PresetInitializer {
    private static final Logger log=LogManager.getLogger(PresetInitializer.class);
    public void initialize() {
        log.trace("Starting the Initialize Process with the OS detection");
        String os=checkSourceSystem();
        log.debug("This is the source System "+os);
        log.trace("Asking for folder path");
        String folderPath=checkPathForFolder();
        log.trace("Starting the folder creation at the path with today date");
        String wallpaperFolderPath=createFolderPath(folderPath);
        log.debug("New Folder has been Created at the location"+wallpaperFolderPath);
    }

    private String createFolderPath(String folderPath) {
        log.trace("Starting the folder creation");
        String wallPaperCreatePath=null;
        try{
            log.trace("Starting the try block");
            if(null==folderPath||folderPath.isEmpty()) {
                log.trace("Folder path is empty so calling the checkPathFolder for the method");
                wallPaperCreatePath = checkPathForFolder();
            }
            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMyyyy");
            log.trace("Adding the wallpaper at the  end to generate new folder");
            wallPaperCreatePath=folderPath+"\\Wallpapers_"+simpleDateFormat.format(date);
            Path newPath=Paths.get(wallPaperCreatePath);
            if(newPath.toFile().exists()){
                log.debug("Folder is already Created");
            }else{
                log.trace("Creating the new folder for wallpapers");
                Files.createDirectory(newPath);
            }
            log.trace("New Path"+wallPaperCreatePath);
        }catch (Exception e){
            log.error("error occurred in the wallpaper Folder creation"+e);
        }
        return wallPaperCreatePath;
    }

    private String checkPathForFolder() {
        Scanner scanner=new Scanner(System.in);
        log.debug("Enter the Location for the Folder :");
        String path;
        path = scanner.nextLine();
        log.debug("path before"+path);
        Path filePath= Paths.get(path);
        if(!filePath.toFile().isDirectory()){
            log.debug("Please enter a path for directory");
            checkPathForFolder();
        }else{
            log.debug("Entered path is a directory"+filePath.toFile().isDirectory());
        }
        scanner.close();
        return path;
    }

    private String checkSourceSystem(){
       log.trace("Starting the source System check");
        String source=System.getProperty("os.name").toLowerCase();
        log.trace("Source system is out "+source);
        if(source.contains("win")){
            source="Windows";
        }else if(source.contains("nix")||source.contains("nux")){
            source="Unix";
        }else  if(source.contains("mac")){
            source="Mac OS X";
        }
        return source;
    }
}
