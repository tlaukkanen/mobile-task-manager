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

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class TodoItem extends Item {

    /** Is this item done or not */
    private boolean done;
    /** Priority of item */
    private int priority;
    private static final int PRIORITY_MAX = 3;
    
    public TodoItem(String name) {
        setName(name);
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean hasChildren() {
        return false;
    }

    public String getMimeType() {
        return "application/octet-stream";
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeInt(2); // Version
        dos.writeUTF(getName());
        dos.writeUTF(getNote());
        dos.writeInt(getStatus());
        dos.writeBoolean(done);
        dos.writeInt(priority);
    }

    public void unserialize(DataInputStream dis) throws IOException {
        int version = dis.readInt();
        if(version==1) {
            setName(dis.readUTF());
            setNote(dis.readUTF());
            setStatus(dis.readInt());
            setDone(dis.readBoolean());
        } else if(version==2) {
            setName(dis.readUTF());
            setNote(dis.readUTF());
            setStatus(dis.readInt());
            setDone(dis.readBoolean());
            setPriority(dis.readInt());
        }
    }

    public void stepPriority() {
        priority++;
        if(priority>PRIORITY_MAX) {
            priority = 0;
        }
    }

}
