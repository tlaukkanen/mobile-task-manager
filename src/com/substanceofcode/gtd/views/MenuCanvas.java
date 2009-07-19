/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.views;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author tommi
 */
public abstract class MenuCanvas extends Canvas {

    Menu menu;
    int screenWidth;

    public MenuCanvas(
            String title,
            MenuItem[] items) {
        screenWidth = getWidth();
        menu = new Menu(items, getWidth(), getHeight());
        menu.setTitle( title );
        menu.activate();
        this.setFullScreenMode(true);
    }

    protected void paint(Graphics g) {
        /** Identify screen rotation */
        if(screenWidth!=getWidth()) {
            menu.resize(getWidth(),getHeight());
            screenWidth = getWidth();
        }

        g.setColor(Theme.BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0x000000);
        menu.draw(g);
    }

    protected void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        int gameAction = this.getGameAction(keyCode);
        if(gameAction==Canvas.UP) {
            menu.selectPrevious();
            repaint();
        } else if(gameAction==Canvas.DOWN) {
            menu.selectNext();
            repaint();
        } else if(gameAction==Canvas.FIRE) {
            menu.activateSelected();
        }
    }

    public void setTitle(String title) {
        super.setTitle(title);
        menu.setTitle(title);
    }

    
}
