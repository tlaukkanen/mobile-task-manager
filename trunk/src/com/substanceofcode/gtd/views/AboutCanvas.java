/*
 * Copyright (C) 2008-2009 Tommi Laukkanen
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
import com.substanceofcode.utils.StringUtil;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class AboutCanvas extends Canvas {

    /** Fonts */
    private final Font titleFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    private final Font normalFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);

    String[] bodyLines;

    public AboutCanvas() {
        setFullScreenMode(true);
        String[] lines = {
            "Mobile Task Manager",
            "Copyright 2009 Tommi Laukkanen http://www.substanceofcode.com",
            "",
            "Icons from Mark James from famfamfam.com",
            "Splash screen icon from Max Brown from orangeyear.com"};
        bodyLines = StringUtil.formatMessage(lines, getWidth()-8, normalFont);
    }

    private void drawTitle(Graphics g) {
        g.setColor(Theme.BG_COLOR-0x111111);
        int titleEndRow = (1) * titleFont.getHeight()+2;
        g.fillRect(0, 0, getWidth(), titleEndRow);

        g.setColor(0x666666);
        g.drawLine(0, titleEndRow, getWidth(), titleEndRow);

        g.setColor(0x222222);
        g.setFont(titleFont);

        /** Draw item title */
        g.setColor(0x222222);
        g.drawString("About", getWidth()/2, 1*titleFont.getHeight(), Graphics.BOTTOM|Graphics.HCENTER);
    }

    private void drawBody(Graphics g) {
        g.setColor(Theme.BG_COLOR);
        g.fillRect(0, titleFont.getHeight()+2, getWidth(), getHeight());

        g.setColor(0x000000);
        g.setFont(normalFont);
        int row = 0;
        int rowOffset = titleFont.getHeight()+4;
        for(int line=0; line<bodyLines.length; line++) {
            g.drawString(bodyLines[line], 3, rowOffset + row*normalFont.getHeight(), Graphics.TOP|Graphics.LEFT);
            row++;
        }
    }

    protected void paint(Graphics g) {
        try {
            drawTitle(g);
            drawBody(g);
        }catch(Exception ex) {
            Controller.getInstance().showError(ex);
        }
    }

    protected void keyPressed(int arg0) {
        Controller.getInstance().showMainList();
    }



}
