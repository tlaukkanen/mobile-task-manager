/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.model;

import com.substanceofcode.data.Serializable;

/**
 *
 * @author Tommi Laukkanen
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
