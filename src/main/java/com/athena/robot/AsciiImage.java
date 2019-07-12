package com.athena.robot;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AsciiImage {
    public void generator(){
        int width=50;
        int height=15;
        BufferedImage bufferedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics graphics=bufferedImage.getGraphics();
        graphics.setFont(new Font("SansSerif",Font.BOLD,10));
        Graphics2D graphics2D=(Graphics2D) graphics;
        graphics2D.setColor(Color.BLUE);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString("Boom Box",0,10);
        for (int i = 0; i <height ; i++) {
            StringBuilder sb=new StringBuilder();
            for (int j = 0; j <width ; j++) {
                sb.append(bufferedImage.getRGB(j,i)==-16777216?" ":"*");
            }
            if (sb.toString().trim().isEmpty()){
                continue;
            }
            System.out.println(sb);
        }
    }
}
