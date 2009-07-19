/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.views;

import com.substanceofcode.gtd.controllers.Controller;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author tommi
 */
public class BackupPathTextBox extends TextBox implements CommandListener {

    private Command okCommand = new Command("OK", Command.OK, 1);
    private Command cancelCommand = new Command("Cancel", Command.CANCEL, 2);

    public BackupPathTextBox() {
        super("Export path", "e:/gtd-backup.csv", 128, TextField.ANY);
        addCommand(okCommand);
        addCommand(cancelCommand);
        setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable disp) {
        if(cmd==okCommand) {
            String path = getString();
            Controller.getInstance().backup(path);
            Controller.getInstance().showMainList();
        }
        if(cmd==cancelCommand) {
            Controller.getInstance().showMainList();
        }
    }

}
