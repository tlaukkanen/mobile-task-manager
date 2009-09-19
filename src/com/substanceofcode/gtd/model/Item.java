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

import com.substanceofcode.data.Serializable;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com)  Laukkanen
 */
public abstract class Item implements Serializable {

    private int status;
    private String name;
    private String note;
    private final static int STATUS_FAVORITE = 1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        if(note==null) {
            return "";
        } else {
            return note;
        }
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isFavorite() {
        if((status & STATUS_FAVORITE)==STATUS_FAVORITE) {
            return true;
        } else {
            return false;
        }
    }

    public void toggleFavorite() {
        status ^= STATUS_FAVORITE;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public abstract boolean hasChildren();

}
