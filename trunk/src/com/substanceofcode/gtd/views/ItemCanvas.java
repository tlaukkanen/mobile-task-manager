/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.views;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.model.Item;
import com.substanceofcode.infrastructure.Device;
import com.substanceofcode.utils.StringUtil;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author tommi
 */
public class ItemCanvas extends Canvas {

    private String[] titleLines;
    private String[] bodyLines;
    private Item item;

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
        if( gameCode == this.UP) {
            topLine--;
            if(topLine<0) {
                topLine = 0;
            }
            repaint();
        }
        if( gameCode == this.DOWN) {
            if(isBottomVisible==false) {
                topLine++;
                if(topLine>bodyLines.length-1) {
                    topLine=bodyLines.length-1;
                }
            }
            repaint();
        }
  /*      if( gameCode == this.LEFT ) {
            if(showActionBar) {
                actionBar.selectPreviousAction();
                repaint();
            } else {
                controller.showItem(itemIndex-1);
            }
        }
        if( gameCode == this.RIGHT ) {
            if(showActionBar) {
                actionBar.selectNextAction();
                repaint();
            } else {
                controller.showItem(itemIndex+1);
            }
        }
   */     if( gameCode == ItemCanvas.FIRE ) {
            Controller.getInstance().showMainList();
        }
    }

}
