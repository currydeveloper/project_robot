package com.athena.robot;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {
        System.out.println("Setting the windows desktop wallpaper from the code");
        Path wallPath= Paths.get("C:\\Users\\Ksaira\\Pictures\\Pics");
        int SPI_SETDESKWALLPAPER = 0x14; // 20 in decimal
        int SPIF_UPDATEINIFILE = 0x01;
        int SPIF_SENDWININICHANGE = 0x02;
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(wallPath);
            for (Path entry:stream) {
                System.out.println("File details are::"+entry.getFileName());
                System.out.println("File Path is "+entry.toString());
                 boolean result = MyUser32.INSTANCE.SystemParametersInfoA(
                    SPI_SETDESKWALLPAPER, 0, entry.toString(),
                    SPIF_UPDATEINIFILE | SPIF_SENDWININICHANGE );
            System.out.println("Refresh desktop result: " + result);
                TimeUnit.SECONDS.sleep(120);
       }

//
        }
        catch (Exception e){

        }
    }

    private interface MyUser32 extends StdCallLibrary
    {
        MyUser32 INSTANCE =
                (MyUser32) Native.load("user32", MyUser32.class);
        boolean SystemParametersInfoA(int uiAction, int uiParam,
                                      String fnm, int fWinIni);
    }

}
