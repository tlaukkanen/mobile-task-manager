/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.views;

import javax.microedition.lcdui.Image;

/**
 *
 * @author tommi
 */
public class MenuItem {

    String label;
    Image icon;
    Image[] statusIcons;
    MenuAction action;
    Object tag;

    public MenuItem(String label, Image icon, Image[] statuses, MenuAction action) {
        this.label = label;
        this.icon = icon;
        this.statusIcons = statuses;
        this.action = action;
    }

    public void activate() {
        action.activate();
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getLabel() {
        return label;
    }

    public Image getIcon() {
        return icon;
    }

    public Image[] getStatusIcons() {
        return statusIcons;
    }

    public MenuAction getAction() {
        return action;
    }

}
