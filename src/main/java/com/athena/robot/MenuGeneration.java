package com.athena.robot;
import com.athena.robot.subprocess.PresetInitializer;
import com.athena.robot.subprocess.SettingChanger;
import com.athena.robot.subprocess.WallpaperChanger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MenuGeneration {
    private static final Logger log=LogManager.getLogger(MenuGeneration.class);
    public void generateMenu() {
        log.trace("Starting the menu Generation");
        try{
            log.trace("Starting the try block");
            System.out.println("Select from the following options\n" +
                    " 1. Initialize the Presets\n" +
                    " 2. Edit the presets\n" +
                    " 3. Change the Wallpapers\n"+
                    " 4. Exit\n" +
                    "Enter the value here:::");
            Scanner scanner=new Scanner(System.in);
            String input=scanner.next();
            log.debug("Got the operation as "+input);
            switch (input){
                case "1":
                    log.debug("Initializing the BoomBox");
                    log.info("Calling the Initialize subprocess");
                    PresetInitializer presetInitializer=new PresetInitializer();
                    presetInitializer.initialize();
                    break;
                case "2":
                    log.debug("Loading the existing presets");
                    log.info("calling the setting changer class");
                    SettingChanger settingChanger=new SettingChanger();
                    settingChanger.change();
                    break;
                case "3":
                    log.debug("Downloading Fresh Wallpapers for the day");
                    log.info("Calling the wallpaper changer class");
                    WallpaperChanger wallpaperChanger=new WallpaperChanger();
                    wallpaperChanger.initialize();
                    break;
                case "4":
                    log.debug("Done with the iteration");
                    break;
                default:
                    log.debug("Choose one of the Specific Options");
                    generateMenu();
                    break;
            }
        }catch (Exception e){
            log.error("Caught Exception in Menu Generation \t"+e);
        }
    }
}
