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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
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

    /**
     * Handle touch screen press
     * @param x coordinate
     * @param y coordinate
     */
    protected void pointerPressed(int x, int y) {
        menu.selectWithPointer(x, y, true);
        repaint();
    }

    /**
     * Handle touch screen drag
     * @param x coordinate
     * @param y coordinate
     */
    protected void pointerDragged(int x, int y) {
        menu.selectWithPointer(x, y, false);
        repaint();
    }

    /**
     * Handle touch screen release
     * @param x coordinate
     * @param y coordinate
     */
    protected void pointerReleased(int x, int y) {
        menu.selectWithPointer(x, y, false);
        menu.activateSelected();
    }

    
}
