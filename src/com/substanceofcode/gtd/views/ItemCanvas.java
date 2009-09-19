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
import com.substanceofcode.gtd.model.Item;
import com.substanceofcode.gtd.model.TodoItem;
import com.substanceofcode.infrastructure.Device;
import com.substanceofcode.utils.StringUtil;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class ItemCanvas extends Canvas {

    private String[] titleLines;
    private String[] bodyLines;
    private Item item;
    private static final String[] priorities = {"None", "Low", "Normal", "High"};

    private int topLine;
    private boolean isBottomVisible;

    /** Fonts */
    private final Font titleFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    private final Font normalFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);

    /**
     * Creates a new instance of NewsItemCanvas
     * @param controller Controller for the reader module.
     */
    public ItemCanvas(Item item) {
        this.setFullScreenMode(true);
        setItem(item);
    }

    public void setItem(Item item) {
        if(item==null) {
            topLine = 0;
            isBottomVisible = false;
            return;
        }
        String[] originalTitleLines = {item.getName()};
        titleLines = StringUtil.formatMessage(originalTitleLines, getWidth()-8, titleFont);
        String description = item.getNote();
        if(description!=null) {
            description = StringUtil.replace(item.getNote(), "\r\n", "\n");
            String[] originalDescriptionLines = StringUtil.split(description, "\n");
            bodyLines = StringUtil.formatMessage(originalDescriptionLines, getWidth()-8, normalFont);
        } else {
            bodyLines = new String[0];
        }
        this.item = item;
        topLine = 0;
        isBottomVisible = false;
    }

    /** Paint canvas */
    protected void paint(Graphics g) {
        drawTitle(g);
        drawBody(g);
        drawPriority(g);
        drawSoftKeys(g);
    }

    private void drawTitle(Graphics g) {
        if(topLine>0) {
            return;
        }
        g.setColor(Theme.BG_COLOR-0x111111);
        int titleEndRow = (titleLines.length-topLine+1) * titleFont.getHeight()+2;
        g.fillRect(0, 0, getWidth(), titleEndRow);

        g.setColor(0xBBBBBB);
        g.drawLine(0, titleEndRow, getWidth(), titleEndRow);

        g.setColor(0x222222);
        g.setFont(titleFont);

        /** Draw item title */
        g.setColor(0x222222);
        int titleRow = 0;
        for(int line=topLine; line<titleLines.length; line++) {
            titleRow++;
            g.drawString(titleLines[line], getWidth()/2, titleRow*titleFont.getHeight(), Graphics.BOTTOM|Graphics.HCENTER);
        }
    }

    private void drawBody(Graphics g) {
        if(bodyLines==null) {
            return;
        }
        isBottomVisible = false;
        g.setColor(Theme.BG_COLOR);
        /** Top row starts after feed title + item title */
        int topRow = (titleLines.length) - topLine;
        if(topLine>0 || topRow<0) {
            topRow = 0;
        }
        int contentStartLine = topRow * titleFont.getHeight()+2;
        if(contentStartLine==2) {
            contentStartLine = 0;
        }
        g.fillRect(0, contentStartLine, getWidth(), getHeight());

        g.setColor(0x000000);
        g.setFont(normalFont);
        int row = 0;
        int rowOffset = topRow * titleFont.getHeight()+4;
        for(int line=topLine; line<bodyLines.length; line++) {
            g.drawString(bodyLines[line], 3, rowOffset + row*normalFont.getHeight(), Graphics.TOP|Graphics.LEFT);
            row++;
            if(row*normalFont.getHeight() + rowOffset>getHeight()) {
                return;
            }
        }
        row++;
        /** Check whatever we have reached the bottom of the screen */
        if(row*normalFont.getHeight() + rowOffset + normalFont.getHeight()>getHeight()) {
            isBottomVisible = false;
        }
    }

    private void drawPriority(Graphics g) {
        /** Draw soft menu labels */
        int loc = Graphics.BOTTOM|Graphics.LEFT;
        g.setColor(Theme.BG_COLOR);
        g.fillRect(0, getHeight()-normalFont.getHeight()*2, getWidth(), normalFont.getHeight());
        g.setColor(0xaaaaaa);
        g.drawLine(0, getHeight()-normalFont.getHeight()*2, getWidth(), getHeight()-normalFont.getHeight()*2);
        g.setFont(normalFont);
        g.setColor(0x333333);
        g.drawString("Priority", 0, getHeight()-normalFont.getHeight(), loc);
        if(item!=null) {
            String priority = getPriorityString();
            if(priority!=null) {
                g.drawString(priority, getWidth()-normalFont.stringWidth(priority), getHeight()-normalFont.getHeight(), loc);
            }
        }
    }

    private String getPriorityString() {
        int priority = 0;
        if(item.hasChildren()==false) {
            TodoItem todo = (TodoItem)item;
            priority = todo.getPriority();
        }
        if(priority>=0 && priority<priorities.length) {
            return priorities[priority];
        }
        return "Unknown";
    }

    /** Draw soft key indicators */
    private void drawSoftKeys(Graphics g) {
        /** Draw soft menu labels */
        int loc = Graphics.BOTTOM|Graphics.LEFT;
        g.setColor(0xdddddd);
        g.fillRect(0, getHeight()-normalFont.getHeight(), getWidth(), normalFont.getHeight());
        g.setColor(0x666666);
        g.drawLine(0, getHeight()-normalFont.getHeight(), getWidth(), getHeight()-normalFont.getHeight());
        g.setFont(normalFont);
        g.setColor(0x000000);
        g.drawString("Back", 0, getHeight(), loc);
        if(item!=null) {
            g.drawString("Edit note", getWidth()-normalFont.stringWidth("Edit note"), getHeight(), loc);
        }
    }

    protected void keyPressed(int keyCode) {
        handleUpAndDown(keyCode);
        handleSoftKeys(keyCode);
    }

    protected void keyRepeated(int keyCode) {
        handleUpAndDown(keyCode);
    }

    private void handleSoftKeys(int keyCode) {
        String keyName = this.getKeyName(keyCode);
        /** Handle soft keys */
        if( keyName.indexOf("SOFT")>=0 && keyName.indexOf("1")>0 ||
            (Device.isNokia() && keyCode==-6) ||
            keyCode == ItemCanvas.KEY_STAR) {
            /** Left soft key pressed */
            Controller.getInstance().showMainList();
        }
        if( (keyName.indexOf("SOFT")>=0 && keyName.indexOf("2")>0) ||
            (Device.isNokia() && keyCode==-7) ||
            keyCode == ItemCanvas.KEY_POUND) {
            /** Right soft key pressed */
            if(item!=null) {
                Controller.getInstance().showNoteForm(item);
            }
        }
    }

    private void handleUpAndDown(int keyCode) {
        int gameCode = getGameAction(keyCode);
        if( gameCode == Canvas.UP) {
            topLine--;
            if(topLine<0) {
                topLine = 0;
            }
            repaint();
        }
        if( gameCode == Canvas.DOWN) {
            if(isBottomVisible==false) {
                topLine++;
                if(topLine>bodyLines.length-1) {
                    topLine=bodyLines.length-1;
                }
            }
            repaint();
        }
        if( gameCode == Canvas.LEFT ) {
            if(item.hasChildren()==false) {
                TodoItem todo = (TodoItem)item;
                int priority = todo.getPriority();
                if(priority>0) {
                    priority--;
                }
                todo.setPriority(priority);
            }
            repaint();
        }
        if( gameCode == Canvas.RIGHT ) {
            if(item.hasChildren()==false) {
                TodoItem todo = (TodoItem)item;
                int priority = todo.getPriority();
                if(priority<3) {
                    priority++;
                }
                todo.setPriority(priority);
            }
            repaint();
        }
        if( gameCode == ItemCanvas.FIRE ) {
            Controller.getInstance().showMainList();
        }
    }

}
