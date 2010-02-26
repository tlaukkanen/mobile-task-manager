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

package com.substanceofcode.gtd.tasks;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.model.FolderItem;
import com.substanceofcode.gtd.model.Item;
import com.substanceofcode.gtd.model.TodoItem;
import com.substanceofcode.utils.StringUtil;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

/**
 * CSV file exporter. CSV file format:
 * name;
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class BackupTask extends AbstractTask {

    FolderItem root;
    String path;

    public BackupTask(FolderItem root, String path) {
        this.root = root;
        this.path = path;
    }

    public void doTask() {
        FileConnection fc;
        try{
            fc = (FileConnection)Connector.open("file:///" + path, Connector.READ_WRITE);
            if(fc.exists()) {
                Controller.getInstance().showError("File already exists. Please use different filename.");
                return;
            }
            fc.create();
            DataOutputStream dos = fc.openDataOutputStream();
            writeAscii(dos,"Action;Done;Folder;Favorite;Details\r\n");
            writeItem(root, "", dos);
            dos.flush();
            dos.close();
            fc.close();
        } catch(Exception ex) {
            Controller.getInstance().showError(ex);
            return;
        }
        Controller.getInstance().showMainList();
    }

    private void writeItem(Item item, String folder, DataOutputStream dos) throws IOException {
        boolean done = false;
        if(item.hasChildren()==false) {
            TodoItem todo = (TodoItem)item;
            done = todo.isDone();
            String folderValue = folder;
            if(folder.equals("Main")) {
                folderValue = "";
            }

            String note = item.getNote();
            note = StringUtil.replace(note, "\r", "");
            note = StringUtil.replace(note, "\n", "");

            writeAscii(dos, item.getName() + ";" + 
                    (done ? "X":"") + ";" +
                    folderValue + ";"+
                    (item.isFavorite() ? "X" : "") + ";" +
                    note + "\r\n");
        } else {
            FolderItem folderItem = (FolderItem)item;
            Vector items = folderItem.getItems();
            Enumeration en = items.elements();
            while(en.hasMoreElements()) {
                Item it = (Item)en.nextElement();
                writeItem(it, item.getName(), dos);
            }
        }
    }

    private void writeAscii(DataOutputStream dos, String line) throws IOException {
        char c;
        for(int i=0; i<line.length(); i++) {
            c = line.charAt(i);
            dos.writeByte((int)c);
        }
    }

}
