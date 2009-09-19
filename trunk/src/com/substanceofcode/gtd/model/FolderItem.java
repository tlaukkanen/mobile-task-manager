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

package com.substanceofcode.gtd.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class FolderItem extends Item {

    transient FolderItem parentFolder;
    Vector items;

    public FolderItem(String title, FolderItem parent) {
        setName(title);
        parentFolder = parent;
        items = new Vector();
    }

    public FolderItem getParentFolder() {
        return parentFolder;
    }

    public Vector getItems() {
        return items;
    }

    public void setItems(Vector items) {
        this.items = items;
    }

    public boolean hasChildren() {
        return true;
    }

    public void addItem(Item item) {
        items.addElement(item);
    }

    public String getMimeType() {
        return "text/plain";
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeInt(1); // Version
        dos.writeUTF(getName());
        dos.writeUTF(getNote());
        dos.writeInt(getStatus());
        if(parentFolder==null) {
            dos.writeBoolean(false);
        } else {
            dos.writeBoolean(true);
            //parentFolder.serialize(dos);
        }

        if(items!=null && items.isEmpty()==false) {
            dos.writeBoolean(true);
            dos.writeInt(items.size());
            Enumeration it = items.elements();
            while(it.hasMoreElements()) {
                Item item = (Item)it.nextElement();
                if(item.hasChildren()) {
                    dos.writeBoolean(true);
                } else {
                    dos.writeBoolean(false);
                }
                item.serialize(dos);
            }
        } else {
            dos.writeBoolean(false);
        }
    }

    public void unserialize(DataInputStream dis) throws IOException {
        int version = dis.readInt(); // Version
        if(version==1) {
            setName( dis.readUTF() );
            setNote( dis.readUTF() );
            setStatus( dis.readInt() );
            boolean hasParent = dis.readBoolean();
            if(hasParent) {
                //parentFolder.unserialize(dis);
            }
            boolean hasItems = dis.readBoolean();
            if(hasItems==true) {
                int itemCount = dis.readInt();
                items = new Vector();
                for(int i=0; i<itemCount; i++) {
                    boolean isFolder = dis.readBoolean();
                    Item item;
                    if(isFolder) {
                        item = new FolderItem("", null);
                        item.unserialize(dis);
                    } else {
                        item = new TodoItem("");
                        item.unserialize(dis);
                    }
                    items.addElement(item);
                }
            }
        }
    }

    public void setParent(FolderItem folder) {
        this.parentFolder = folder;
    }

}
