package com.athena.robot.subprocess;
import com.sun.deploy.util.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Scanner;

public class PresetInitializer {
    private static final Logger log=LogManager.getLogger(PresetInitializer.class);
    public void initialize() {
        log.trace("Starting the Initialize Process with the OS detection");
        String os=checkSourceSystem();
        log.debug("This is the source System "+os);
        log.trace("Asking for folder path");
        String folderPath=checkPathForFolder();
    }

    private String checkPathForFolder() {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter the Location for the Folder :");
        String path=scanner.next();
        File file=new File(path);
        if(!file.exists()||file.isFile()){
            System.out.println("Please enter a path for directory");
            checkPathForFolder();
        }else{
            log.debug("Entered path is a directory"+file.isDirectory());
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
