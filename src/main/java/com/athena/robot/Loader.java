package com.athena.robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Loader {
    private static final Logger log=LogManager.getLogger(Loader.class);
    public static void main(String[] args) {
        log.trace("Starting the Image Generation");
        AsciiImage asciiImage=new AsciiImage();
        asciiImage.generator();
        log.trace("Done with the generation");
        MenuGeneration menuGeneration=new MenuGeneration();
        menuGeneration.generateMenu();
    }
}
