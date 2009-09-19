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
            fc = (FileConnection)Connector.open("file:///" + path, Connector.WRITE);
            fc.create();
            DataOutputStream dos = fc.openDataOutputStream();
            dos.writeUTF("Action;Done;Folder;Favorite;Details\r\n");
            writeItem(root, "Main", dos);
            dos.flush();
            dos.close();
            fc.close();
        } catch(Exception ex) {
            Controller.getInstance().showError(ex);
        }
    }

    private void writeItem(Item item, String folder, DataOutputStream dos) throws IOException {
        boolean done = false;
        if(item.hasChildren()==false) {
            TodoItem todo = (TodoItem)item;
            done = todo.isDone();
        }
        dos.writeUTF(item.getName() + ";" + (done ? "X":"") + ";" + folder + ";"+ (item.isFavorite() ? "X" : "") + ";" + item.getNote() + "\r\n");
        if(item.hasChildren()) {
            FolderItem folderItem = (FolderItem)item;
            Vector items = folderItem.getItems();
            Enumeration en = items.elements();
            while(en.hasMoreElements()) {
                Item it = (Item)en.nextElement();
                writeItem(it, item.getName(), dos);
            }
        }
    }

}
