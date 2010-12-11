/*
 * Copyright (C) 2008-2009 Tommi Laukkanen
 *                    2010 MaximAL
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
import com.substanceofcode.gtd.menuactions.OpenFolderAction;
import com.substanceofcode.gtd.menuactions.ToggleTaskAction;
import com.substanceofcode.gtd.model.FolderItem;
import com.substanceofcode.gtd.model.Item;
import com.substanceofcode.gtd.model.TodoItem;
import com.substanceofcode.infrastructure.Device;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class TaskMenu extends MenuCanvas {

    private static final Font MENU_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private FolderItem currentFolder;

    public TaskMenu() {
        super("Main", null);
        menu.alignLeft(true);
        menu.setPadding(0);
        menu.showIcon(true);
    }

    protected void paint(Graphics g) {
        try {
        super.paint(g);

        /** Draw soft menu labels */
        int loc = Graphics.BOTTOM|Graphics.LEFT;
        g.setColor(0xdddddd);
        g.fillRect(0, getHeight()-MENU_FONT.getHeight(), getWidth(), MENU_FONT.getHeight());
        g.setColor(0x666666);
        g.drawLine(0, getHeight()-MENU_FONT.getHeight(), getWidth(), getHeight()-MENU_FONT.getHeight());
        g.setFont(MENU_FONT);
        g.setColor(0x000000);
        g.drawString("Menu", 0, getHeight(), loc);
        g.drawString("Add", getWidth()-MENU_FONT.stringWidth("Add"), getHeight(), loc);
        } catch(Exception ex) {
            Controller.getInstance().showError(ex);
        }
    }

    protected void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        int gameAction = this.getGameAction(keyCode);
        String keyName = this.getKeyName(keyCode);
        if( keyName.indexOf("SOFT")>=0 && keyName.indexOf("1")>0 ||
            (Device.isNokia() && keyCode==-6) ||
            keyCode == Canvas.KEY_STAR) {
            /** Left soft key pressed */
            Controller.getInstance().showActionsMenu();
        } else if( ((keyName.indexOf("SOFT")>=0 && keyName.indexOf("2")>0) ||
            (Device.isNokia() && keyCode==-7) ||
            keyCode == Canvas.KEY_POUND) ) {
            /** Right soft key pressed */
            //controller.sendMessage(order);
            Controller.getInstance().showItem("");
        } else if( gameAction==Canvas.LEFT) {
            /** Move item upwards */
            int index = menu.getSelectedIndex();
            int fix=0;
            if(currentFolder!=null && currentFolder.getParentFolder()!=null) {
                fix=-1;
            }
            if(index+fix>0) {
                menu.selectPrevious();
                Controller.getInstance().moveItemUp( index );
            }
        } else if( gameAction==Canvas.RIGHT) {
            /** Move item downwards */
            int index = menu.getSelectedIndex();
            if(index+1<menu.getCount()) {
                menu.selectNext();
                Controller.getInstance().moveItemDown( index );
            }
        } else if( keyCode==Canvas.KEY_NUM1) {
            Item item = (Item)menu.getSelectedItem().getTag();
            Controller.getInstance().showItemDetails(item);
        } else if( keyCode==Canvas.KEY_NUM3) {
            int index = menu.getSelectedIndex();
            int fix=0;
            if(currentFolder!=null && currentFolder.getParentFolder()!=null) {
                fix=-1;
            }
            Controller.getInstance().edit(index+fix);
        } else if( keyCode==Canvas.KEY_NUM7) {
            MenuItem selectedMenuItem = menu.getSelectedItem();
            if(selectedMenuItem!=null && selectedMenuItem.getTag()!=null) {
                Item item = (Item) selectedMenuItem.getTag();
                item.toggleFavorite();
            }
            Controller.getInstance().showMainList();
        } else if( keyCode==Canvas.KEY_NUM9) {
            Item item = (Item)menu.getSelectedItem().getTag();
            Controller.getInstance().showNoteForm(item);
        } else if( keyCode==Canvas.KEY_NUM0) {
            Item item = (Item)menu.getSelectedItem().getTag();
            if(item.hasChildren()==false) {
                TodoItem todo = (TodoItem)item;
                todo.stepPriority();
                Controller.getInstance().showMainList();
            }
        }
    }

    public void setFolder(FolderItem folder) {
        currentFolder = folder;
        FolderItem parentFolder = folder.getParentFolder();
        this.setTitle(folder.getName());
        Vector items = folder.getItems();
        int menuItemCount = items.size();
        MenuItem[] menuItems = null;
        int startIndex = 0;
        if(parentFolder!=null) {
            menuItemCount++;
            menuItems = new MenuItem[ menuItemCount ];
            MenuItem backItem = new MenuItem("Back", ImageRepository.getBackArrow(), null, new OpenFolderAction(parentFolder));
            menuItems[0] = backItem;
            startIndex++;
        } else {
            menuItems = new MenuItem[ menuItemCount ];
        }

        for(int i=0; i<items.size(); i++) {
            Item element = (Item)items.elementAt(i);
            if(element.hasChildren()) {
                /** Folder */
                FolderItem item = (FolderItem) element;
                item.setParent(folder);
                Image[] images = getStatusImages(item);
                MenuItem menuItem = new MenuItem( item.getName(), ImageRepository.getFolder(), images, new OpenFolderAction(item));
                menuItem.setTag(item);
                menuItems[i + startIndex] = menuItem;
            } else {
                TodoItem item = (TodoItem) element;
                Image icon = (item.isDone() ? ImageRepository.getTick() : ImageRepository.getBullet() );
                Image[] images = getStatusImages(item);
                MenuItem menuItem = new MenuItem( item.getName(), icon, images, new ToggleTaskAction(item, menu));
                menuItem.setTag(item);
                menuItems[i + startIndex] = menuItem;
            }
        }
        menu.setItems(menuItems);
        repaint();
    }

    private Image[] getStatusImages(Item item) {
        Vector imagesVector = new Vector();
        if(item.hasChildren()==false) {
            TodoItem todo = (TodoItem)item;
            int priority = todo.getPriority();
            if(priority>0) {
                imagesVector.addElement( getPriorityImage(priority) );
            }
        }
        if(item.isFavorite()) {
            imagesVector.addElement(ImageRepository.getStar());
        }
        if(item.getNote().length()>0) {
            imagesVector.addElement(ImageRepository.getNote());
        }
        Image[] images = new Image[ imagesVector.size() ];
        for(int imgIndex=0; imgIndex<imagesVector.size(); imgIndex++) {
            images[imgIndex] = (Image) imagesVector.elementAt(imgIndex);
        }
        return images;
    }

    private Image getPriorityImage(int priority) {
        switch(priority) {
            case(1):
                return ImageRepository.getGreenFlag();
            case(2):
                return ImageRepository.getYellowFlag();
            case(3):
                return ImageRepository.getRedFlag();
            default:
                return null;
        }
    }

    /**
     * Add specified item after selected.
     * @param item specified item
     */
    public void addItemAfter(Item item) {
        int index = menu.getSelectedIndex();
        Controller.getInstance().addItemAfter(item, index);
    }

    public void deleteSelected() {
        int index = menu.getSelectedIndex();
        Controller.getInstance().deleteSelectedItem( index );
        Controller.getInstance().showMainList();
    }

    /**
     * Cut selected item.
     */
    public void cutSelected() {
        int index = menu.getSelectedIndex();
        Controller.getInstance().cutSelectedItem( index );
        Controller.getInstance().showMainList();
    }

    /**
     * Paste item from clipboard
     */
    public void pasteItem() {
        int index = menu.getSelectedIndex();
        Controller.getInstance().pasteItem( index );
        Controller.getInstance().showMainList();
    }

}
