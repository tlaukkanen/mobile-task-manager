/*
 * Menu.java
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

package com.substanceofcode.gtd.views;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Menu implementation that will render an overlay menu.
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com)  Laukkanen
 */
public class Menu {

    private MenuItem[] items;
    private int screenWidth;
    private int screenHeight;
    private int top;
    private int height;
    private int selectedIndex;
    private boolean active;
    private String title;
    private int rowHeight;
    private boolean alignLeft;
    private boolean showIcon;
    private int rowsPerScreen;
    private int padding = 11;

    private static final Font TITLE_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    private static final Font LABEL_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    private static final int BACK_COLOR = 0xdddddd;
    private static final int SELECTED_COLOR = 0xaaaaaa;
    private static final int FONT_COLOR = 0x000000;
    
    /** Create new Menu instance 
     * @param labels        
     * @param screenWidth 
     * @param screenHeight 
     */
    public Menu(MenuItem[] items, int screenWidth, int screenHeight) {
        this.items = items;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        selectedIndex = 0;
        active = false;
        title = "Menu";
        alignLeft = false;
        showIcon = false;
        rowHeight = LABEL_FONT.getHeight();
        calculateSize();
    }

    public void showIcon(boolean show) {
        this.showIcon = show;
    }

    public void alignLeft(boolean align) {
        this.alignLeft = align;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    
    /** 
     * Draw menu
     * @param g Application graphics.
     */
    public void draw(Graphics g) {
        if(active==false) {
            return;
        }

        if(alignLeft==true) {
            top = padding;
            if(selectedIndex>(rowsPerScreen-3)) {
                top -= (selectedIndex-(rowsPerScreen-3))*rowHeight;
            }
        }

        /** Fill title background */
        g.setColor(BACK_COLOR);
        g.fillRect(padding-1, top, screenWidth-(padding*2-2), LABEL_FONT.getHeight()+2);// height+4);
        
        /** Draw title */
        g.setColor(FONT_COLOR);
        g.setFont(TITLE_FONT);

        g.drawString(
            title,
            screenWidth/2 - LABEL_FONT.stringWidth(title)/2,
            top + LABEL_FONT.getHeight(),
            Graphics.LEFT|Graphics.BOTTOM);
        g.setFont(LABEL_FONT);

        /** Draw items */
        g.setColor(FONT_COLOR);
        int col = (alignLeft ? padding+2 : 0 );
        int picRow = 0;
        if(showIcon) {
            col += 16;
            if(rowHeight>16) {
                picRow = (rowHeight-16)/2;
            }
        }
        for(int menuIndex=0; menuIndex<items.length; menuIndex++) {
            if(menuIndex==selectedIndex) {
                g.setColor(SELECTED_COLOR);
                g.fillRect(padding, top+(menuIndex+1)*LABEL_FONT.getHeight(), screenWidth-padding*2, LABEL_FONT.getHeight());
                g.setColor(FONT_COLOR);
            }
            String label = items[ menuIndex ].getLabel();
            int labelWidth = LABEL_FONT.stringWidth(label);
            if(alignLeft==false) {
                col = screenWidth/2 - labelWidth/2;
            }
            if(label!=null) {
                g.drawString(label, col, top + (menuIndex+2)*LABEL_FONT.getHeight(), Graphics.LEFT|Graphics.BOTTOM);
            }

            if(showIcon) {
                Image logo = items[menuIndex].getIcon();
                if(logo!=null) {
                    g.drawImage(logo, padding+2, top + (menuIndex+1)*rowHeight + picRow, Graphics.LEFT|Graphics.TOP);
                }
                Image[] statusIcons = items[menuIndex].getStatusIcons();
                if(statusIcons!=null) {
                    int rightPadding=0;
                    for(int i=0; i<statusIcons.length; i++) {
                        Image img = statusIcons[i];
                        g.drawImage(img, this.screenWidth-18-rightPadding, top + (menuIndex+1)*rowHeight + picRow, Graphics.LEFT|Graphics.TOP);
                        rightPadding+=16;
                    }
                }
            }

        }
    }
    
    public void selectNext() {
        selectedIndex++;
        if(selectedIndex>items.length-1) {
            selectedIndex = 0;
        }
    }
    
    public void selectPrevious() {
        selectedIndex--;
        if(selectedIndex<0) {
            selectedIndex = items.length-1;
        }
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }

    MenuItem getSelectedItem() {
        if(items==null) {
            return null;
        }
        return items[selectedIndex];
    }

    boolean isActive() {
        return active;
    }
    
    public void activate() {
        active = true;
        selectedIndex = 0;
    }

    public void activateSelected() {
        System.out.println("Activating selected");
        int selected = getSelectedIndex();
        System.out.println("Selected index " + selected);
        System.out.println("Items " + items.length);
        if(items!=null && items.length>selected && items[ selected ]!=null) {
            System.out.println("Activate()");
            items[ selected ].activate();
        }
    }
    
    public void deactivate() {
        active = false;
    }

    /**
     * Set labels for the menu.
     * @param labels
     */
    void setItems(MenuItem[] items) {
        this.items = items;
        calculateSize();
        if(selectedIndex>items.length-1) {
            selectedIndex = 0;
        }
    }

    void setItem(int index, MenuItem item) {
        items[index] = item;
    }

    void setTitle(String title) {
        this.title = title;
    }

    private void calculateSize() {
        rowsPerScreen = screenHeight/rowHeight;
        if(items!=null) {
            this.height = (items.length+1) * rowHeight;
            this.top = screenHeight/2 - height/2;
        }
    }

    int getCount() {
        return items.length;
    }

    void resize(int width, int height) {
        screenHeight = height;
        screenWidth = width;
        calculateSize();
    }

    /**
     * Select menu item with touch screen
     * @param x coordinate
     * @param y coordinate
     */
    public void selectWithPointer(int x, int y, boolean isPress) {
        int canvasY = y - top;
        int pointerIndex = canvasY / rowHeight - 1;
        selectedIndex = pointerIndex;
    }
    
}
