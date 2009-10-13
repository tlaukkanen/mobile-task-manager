/*
 * WaitCanvas.java
 *
 * Copyright (C) 2005-2008 Tommi Laukkanen
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

package com.substanceofcode.gtd.views;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.tasks.AbstractTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class WaitCanvas extends Canvas implements Runnable {
    
    private String waitText;
    private AbstractTask task;
    private Font statusFont;
    private Thread thread;
    
    private final Font titleFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
     
        
    public void setWaitText(String text) {
        waitText = text;
    }
    
    /** Creates a new instance of WaitCanvas 
     * @param controller    Application controller.
     * @param task          Task to be executed.
     */
    public WaitCanvas(AbstractTask task) {
        this.setFullScreenMode(true);
        this.waitText = "Please wait...";
        this.task = task;
        statusFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        thread = new Thread(this);
        thread.start();   
    }
    
    protected void paint(Graphics g) {
        // Get dimensions
        int height = getHeight();
        int width = getWidth();
        
        // Clear the background to white
        g.setColor( Theme.BG_COLOR );
        g.fillRect( 0, 0, width, height );
        
        int titleY = height/2;
        int textWidth = titleFont.stringWidth(waitText);
        g.setColor(0x000000);
        g.setFont(statusFont);
        g.drawString(waitText, getWidth()/2-textWidth/2, titleY, Graphics.BASELINE|Graphics.LEFT);
        
    }

    public void run() {
        try {
            this.repaint();
            Thread.sleep(500);
            this.repaint();
            Thread.yield();
            task.execute();
            while(Controller.getInstance().getCurrentDisplay() == this) {
                Thread.sleep(500);
                waitText += ".";
                this.repaint();
            }
        } catch(Exception ex) {
            Controller.getInstance().showError("Error while running task " + ex.getMessage());
        }
    }
    
}
