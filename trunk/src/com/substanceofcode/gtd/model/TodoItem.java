/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author tommi
 */
public class TodoItem extends Item {

    private boolean done;
    
    public TodoItem(String name) {
        setName(name);
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean hasChildren() {
        return false;
    }

    public String getMimeType() {
        return "application/octet-stream";
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeInt(1); // Version
        dos.writeUTF(getName());
        dos.writeUTF(getNote());
        dos.writeInt(getStatus());
        dos.writeBoolean(done);
    }

    public void unserialize(DataInputStream dis) throws IOException {
        int version = dis.readInt();
        if(version==1) {
            setName(dis.readUTF());
            setNote(dis.readUTF());
            setStatus(dis.readInt());
            setDone(dis.readBoolean());
        }
    }

}
