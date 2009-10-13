/*
 * ImportTask
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

package com.substanceofcode.gtd.tasks;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.model.FolderItem;
import com.substanceofcode.gtd.model.TodoItem;
import com.substanceofcode.utils.StringUtil;
import java.io.DataInputStream;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

/**
 * ImportTask handles the file importing to current folder.
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class ImportTask extends AbstractTask {

    String path;
    FolderItem root;
    Hashtable subFolders;
    
    public ImportTask(FolderItem root, String path) {
        this.path = path;
        this.root = root;
        this.subFolders = new Hashtable();
    }

    /**
     * Execute file importing.
     */
    public void doTask() {
        FileConnection fc = null;
        try{
            fc = (FileConnection)Connector.open(path, Connector.READ);
            DataInputStream dis = fc.openDataInputStream();
            int size = (int)fc.fileSize();
            byte[] data = new byte[size];
            dis.readFully(data);
            //dis.close();
            //fc.close();
            dis = null;
            fc = null;

            String text = new String(data);
            String separator;
            if(text.indexOf("\n")>=0) {
                separator = "\n";
            } else {
                separator = "\r";
            }

            String[] lines = StringUtil.split(text, separator);
            for(int i=0; i<lines.length; i++) {
                handleLine(lines[i]);
            }

            /*TodoItem item = new TodoItem("debug " + lines.length);
            item.setNote(text);
            root.addItem(item);*/

        } catch(Exception ex) {
            Controller.getInstance().showError("File: " + path + " Exception: " + ex.getMessage());
            return;
        }
        Controller.getInstance().showMainList();
    }

    /**
     * Handle one line in imported file. Both plain text and CSV lines are
     * processed.
     * @param line in format Action;Done;Folder;Favorite;Details
     */
    private void handleLine(String line) {
        if(line==null || line.length()==0 || line.startsWith("Action")) {
            return;
        }
        line = StringUtil.replace(line, "\n", "");
        line = StringUtil.replace(line, "\r", "");
        String[] cols = StringUtil.split(line, ";");
        if(cols==null || cols.length<1) {
            /*TodoItem item = new TodoItem("no cols");
            root.addItem(item);*/
            return;
        }
        if(cols.length==1) {
            TodoItem item = new TodoItem(line);
            root.addItem(item);
        } else {
            FolderItem subfolder = null;
            String name = (cols[0]==null?"":cols[0]);
            TodoItem item = new TodoItem(name);
            if(cols.length>1) {
                // Done
                if(cols[1].length()>0) {
                    item.setDone(true);
                }
            }
            if(cols.length>2) {
                // Subfolder
                String subfolderName = cols[2];
                if(subfolderName!=null && subfolderName.length()>0) {
                    if(subFolders.containsKey(subfolderName)) {
                        subfolder = (FolderItem)subFolders.get(subfolderName);
                    } else {
                        subfolder = new FolderItem(subfolderName, root);
                        subFolders.put(subfolderName, subfolder);
                        root.addItem(subfolder);
                    }
                }
            }
            if(cols.length>3) {
                // Favorite
                if(cols[3].length()>0) {
                    item.toggleFavorite();
                }
            }
            if(cols.length>4) {
                // Note
                String note = cols[4];
                item.setNote(note);
            }
            if(subfolder==null) {
                root.addItem(item);
            } else {
                subfolder.addItem(item);
            }
        }
    }

}
