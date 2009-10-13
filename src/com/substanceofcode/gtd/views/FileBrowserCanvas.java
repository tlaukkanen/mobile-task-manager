/*
 * FileBrowserCanvas.java
 *
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
import com.substanceofcode.gtd.model.FileSelect;
import com.substanceofcode.infrastructure.Device;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com)  Laukkanen
 */
public class FileBrowserCanvas extends Canvas  {

    private int width, height;
    private Menu fileMenu;
    private String folder;
    private Vector rootFolders;
    private FileSelect fileSelect;
    private String status;
    private String lastFolder;

    public FileBrowserCanvas(FileSelect select) {
        setFullScreenMode(true);
        width = getWidth();
        height = getHeight();
        folder = "";
        lastFolder = "";
        status = "Please wait";
        this.fileSelect = select;
        fileMenu = new Menu(null, width, height);
        fileMenu.alignLeft(true);
        rootFolders = new Vector();
    }

    public void loadRoots() {
        Enumeration roots = FileSystemRegistry.listRoots();
        rootFolders.removeAllElements();
        while(roots.hasMoreElements()) {
            String root = (String)roots.nextElement();
            rootFolders.addElement(root);
        }
        MenuItem[]  folders = new MenuItem[ rootFolders.size() ];
        for(int i=0; i<rootFolders.size(); i++) {
            String fldr = (String)rootFolders.elementAt(i);
            folders[i] = new MenuItem(fldr, null, null, new MenuAction() {
                public void activate() {
                    selectFileEntry();
                }
            } );
        }
        fileMenu.setItems(folders);
        fileMenu.setTitle("Select device");
        fileMenu.activate();
        status = "Roots: " + folders.length;
        repaint();
    }

    public void resetToLastFolder() {
        folder = lastFolder;
    }

    public void showRoots() {
        status = "Loading roots";
        repaint();
        new Thread() {
            public void run() {
                super.run();
                loadRoots();
                status = "Roots loaded";
                repaint();
            }
        }.start();
    }

    protected void paint(Graphics g) {
        width = getWidth();
        height = getHeight();

        /** Background */
        g.setColor(Theme.BG_COLOR);
        g.fillRect(0, 0, width, height);

        g.setColor(0x000000);
        //g.drawString(status, 0, getHeight()/2, Graphics.BASELINE|Graphics.LEFT);

        /** Draw the file directory */
        fileMenu.draw(g);
    }

    protected void keyRepeated(int keyCode) {
        int gameAction = getGameAction(keyCode);
        switch(gameAction) {
            case(Canvas.UP):
                fileMenu.selectPrevious();
                repaint();
                break;
            case(Canvas.DOWN):
                fileMenu.selectNext();
                repaint();
                break;
            default:
                repaint();
                break;
        }
    }
    
    protected void keyPressed(int keyCode) {
        int gameAction = getGameAction(keyCode);
        switch(gameAction) {
            case(Canvas.UP):
                fileMenu.selectPrevious();
                repaint();
                return;
            case(Canvas.DOWN):
                fileMenu.selectNext();
                repaint();
                return;
            case(Canvas.FIRE):
                selectFileEntry();
                return;
            default:
                repaint();
                break;
        }
        String keyName = this.getKeyName(keyCode);
        if( (keyName.indexOf("SOFT")>=0 && keyName.indexOf("1")>0) ||
            (Device.isNokia() && keyCode==-6) ||
            keyCode == Canvas.KEY_STAR ||
            keyCode == Canvas.KEY_NUM0 ||
            keyCode == ' ') {
            /** Left soft key pressed */
            Controller.getInstance().showMainList();
        } else if( (keyName.indexOf("SOFT")>=0 && keyName.indexOf("2")>0) ||
            (Device.isNokia() && keyCode==-7) ||
            keyCode == Canvas.KEY_POUND ||
            keyCode == Canvas.KEY_NUM0 ||
            keyCode == ' ') {
            /** Right soft key pressed */
            Controller.getInstance().showMainList();
        }
    }

    private void browseToDirectory(FileConnection fc) throws IOException {
        Enumeration items = fc.list();
        rootFolders.removeAllElements();
        while(items.hasMoreElements()) {
            String path = (String)items.nextElement();
            rootFolders.addElement(path);
        }
        MenuItem[] folders = new MenuItem[ rootFolders.size()+1 ];
        folders[0] = new MenuItem("..", null, null,null);
        for(int i=0; i<rootFolders.size(); i++) {
            folders[i+1] = new MenuItem((String)rootFolders.elementAt(i), null, null, null);

        }
        fileMenu.setItems(folders);
        fileMenu.setTitle("Select file");
    }

    private void selectFileEntry() {
        try {
            String label = fileMenu.getSelectedItem().getLabel();
            if (label == null) {
                return;
            }
            if(label.equals("..")) {
                final int slashIndex = folder.lastIndexOf('/', folder.length() - 2);
                if(slashIndex == -1) {
                    folder = null;
                } else {
                    folder = folder.substring(0, slashIndex);
                    if(!folder.endsWith("/")) {
                        folder += "/";
                    }
                }
            } else if(folder.endsWith(label)==false) {
                // TODO: Check if folder is already a file path
                folder += label;
            }
            FileConnection fc = (FileConnection) Connector.open("file:///" + folder, Connector.READ);
            if(fc.isDirectory()) {
                /** move to directory */
                lastFolder = folder;
                browseToDirectory(fc);
                repaint();
            } else {
                /** Select file */
                this.fileSelect.select(fc.getURL());
                return;
            }
        } catch (IOException ex) {
            Controller.getInstance().showError("Error: " + ex.toString()
                    + " " + ex.getMessage());
        }
    }

}
