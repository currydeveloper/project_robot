package com.athena.robot.subprocess;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.concurrent.TimeUnit;

public class SetDesktopImage {
    private static final Logger log= LogManager.getLogger(SetDesktopImage.class);
     void showWallpapers(String wallpaperDownloadedFolder, int wallpaperDuration){
        Path wallPath= Paths.get(wallpaperDownloadedFolder);
        int SPI_SETDESKWALLPAPER = 0x14; // 20 in decimal
        int SPIF_UPDATEINIFILE = 0x01;
        int SPIF_SENDWININICHANGE = 0x02;
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(wallPath)) {
            for (Path entry:stream) {
                log.debug("File details are::"+entry.getFileName());
                log.debug("File Path is "+entry.toString());
                boolean result = MyUser32.INSTANCE.SystemParametersInfoA(
                        SPI_SETDESKWALLPAPER, 0, entry.toString(),
                        SPIF_UPDATEINIFILE | SPIF_SENDWININICHANGE );
                log.debug("Refresh desktop result: " + result);
                TimeUnit.MINUTES.sleep(wallpaperDuration);
            }
        }
        catch (Exception e){
        log.error("Got the error in setting Wallpaper as "+e);
        }
    }
    private interface MyUser32 extends StdCallLibrary
    {
        MyUser32 INSTANCE =
                Native.load("user32", MyUser32.class);
        boolean SystemParametersInfoA(int uiAction, int uiParam,
                                      String fnm, int fWinIni);
    }

}
