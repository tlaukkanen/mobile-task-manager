package com.substanceofcode.gtd.views;

/*
 * SplashCanvas.java
 *
 * Copyright (C) 2005-2009 Tommi Laukkanen
 * http://www.substanceofcode.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.substanceofcode.data.FileIOException;
import com.substanceofcode.data.FileSystem;
import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.utils.ImageUtil;
import com.substanceofcode.utils.Log;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * SplashCanvas
 * 
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com)  Laukkanen (tlaukkanen at gmail dot com)
 */
public class SplashCanvas extends Canvas implements Runnable {

    private Thread waitThread;
    private Image logoImage;
    
    /** 
     * Creates a new instance of SplashCanvas
     * @param controller 
     */
    public SplashCanvas() {
        this.setFullScreenMode(true);
        
        logoImage = ImageUtil.loadImage("/images/logo.png");
        
        waitThread = new Thread(this);
        waitThread.run();
    }

    protected void paint(Graphics g) {
        g.setColor(Theme.BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.drawImage(logoImage, getWidth()/2, getHeight()/2, Graphics.HCENTER|Graphics.VCENTER);
        
        g.setColor(0xCCCCCC);
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL );
        g.setFont( font );
        int fontHeight = font.getHeight();
        String copyright = "© 2009 Tommi Laukkanen";
        int copyWidth = font.stringWidth(copyright);
        g.drawString(copyright, getWidth()/2 - copyWidth/2, getHeight()-fontHeight-2, Graphics.LEFT|Graphics.BOTTOM);
        String urlLink = "www.substanceofcode.com";
        int urlWidth = font.stringWidth(urlLink);
        g.drawString(urlLink, getWidth()/2 - urlWidth/2, getHeight()-2, Graphics.LEFT|Graphics.BOTTOM);

    }
    
    /** 
     * Handle key presses.
     * @param keyCode 
     */
    protected void keyPressed(int keyCode) {
        if(keyCode==Canvas.KEY_NUM9) {
            try {
                System.out.println("Formatting...");
                FileSystem.getFileSystem().formatFileSystem();
            } catch (FileIOException ex) {
                ex.printStackTrace();
            }
        } else {
            showNextView();
        }
    }
    
    private void showNextView() {
        Controller.getInstance().showMainList();
    }

    public void run() {
        try {
            Thread.sleep(4000);
            if(this==Controller.getInstance().getCurrentDisplay()) {
                showNextView();
            }
        }catch(Exception ex) {
            Log.error("Error in splash screen: " + ex.getMessage());
        }
    }

}
