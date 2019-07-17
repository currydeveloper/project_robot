package com.athena.robot;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        System.out.println("Setting the windows desktop wallpaper from the code");
        Path wallPath= Paths.get("C:\\Users\\Ksaira\\Pictures\\Pics");
        try {
//            DirectoryStream<Path> stream = Files.newDirectoryStream(wallPath);
//            for (Path entry:stream) {
//                System.out.println("File details are::"+entry.getFileName());
//                System.out.println("File Path is "+entry.toString());
//                boolean result=SPI.INSTANCE.SystemParametersInfoA(
//                  SPI.SPI_SETDESKWALLPAPER,0,entry.toString(),SPI.SPIF_UPDATEINFILE|SPI.SPIF_SENDWININICHANGE
//                );
//                System.out.println("Result is "+result);
//            }
            String fileName="C:\\Users\\Ksaira\\Pictures\\Pics\\alch.jpg";
            int SPI_SETDESKWALLPAPER = 0x14; // 20 in decimal
            int SPIF_UPDATEINIFILE = 0x01;
            int SPIF_SENDWININICHANGE = 0x02;
            boolean result = MyUser32.INSTANCE.SystemParametersInfoA(
                    SPI_SETDESKWALLPAPER, 0, fileName,
                    SPIF_UPDATEINIFILE | SPIF_SENDWININICHANGE );
            System.out.println("Refresh desktop result: " + result);
        }
        catch (Exception e){

        }
    }
    public interface SPI extends StdCallLibrary{
        int SPI_SETDESKWALLPAPER=20;
        int SPIF_UPDATEINFILE=0x01;
        int SPIF_SENDWININICHANGE=0x02;
        SPI INSTANCE=Native.load("user32",SPI.class,new HashMap<String,Object>(){{
            put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
            put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
        }
        });
        boolean SystemParametersInfoA(int uiAction, int uiParam,
                                      String fnm, int fWinIni);

    }
    private interface MyUser32 extends StdCallLibrary
    {
        MyUser32 INSTANCE =
                (MyUser32) Native.load("user32", MyUser32.class);
        boolean SystemParametersInfoA(int uiAction, int uiParam,
                                      String fnm, int fWinIni);
    }

}
