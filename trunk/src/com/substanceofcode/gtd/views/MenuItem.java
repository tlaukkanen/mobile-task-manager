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

import javax.microedition.lcdui.Image;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
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
