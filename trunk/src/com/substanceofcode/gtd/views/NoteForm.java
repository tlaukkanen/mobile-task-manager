/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.views;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.model.Item;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author tommi
 */
public class NoteForm extends TextBox implements CommandListener {

    Command okCommand;
    Command cancelCommand;
    Item editable;

    public NoteForm(Item item) {
        super("Note", item.getNote(), 512, TextField.ANY);
        editable = item;
        addCommands();
    }

    private void addCommands() {
        okCommand = new Command("Ok", Command.OK, 1);
        this.addCommand(okCommand);
        cancelCommand = new Command("Cancel", Command.CANCEL, 2);
        this.addCommand(cancelCommand);
        this.setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable disp) {
        if(cmd==okCommand) {
            String text = getString();
            editable.setNote(text);
            Controller.getInstance().showItemDetails(editable);
        } else if(cmd==cancelCommand) {
            Controller.getInstance().showMainList();
        }
    }

}
