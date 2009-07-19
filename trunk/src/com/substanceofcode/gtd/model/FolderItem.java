/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author tommi
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
