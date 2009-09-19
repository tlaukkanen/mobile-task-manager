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

package com.substanceofcode.gtd.views;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.model.FolderItem;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class FolderTextBox extends TextBox implements CommandListener {

    Command okCommand;
    Command cancelCommand;
    FolderItem editable;

    public FolderTextBox(FolderItem folder) {
        super("Folder", folder.getName(), 128, TextField.ANY);
        editable = folder;
        addCommands();
    }

    public FolderTextBox(String text) {
        super("Folder", text, 128, TextField.ANY);
        addCommands();
    }

    private void addCommands() {
        okCommand = new Command("Ok", Command.OK, 1);
        this.addCommand(okCommand);
        cancelCommand = new Command("Cancel", Command.CANCEL, 2);
        this.addCommand(cancelCommand);
        this.setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable arg1) {
        if(cmd==okCommand) {
            String text = getString();
            if(editable!=null) {
                editable.setName(text);
            } else {
                Controller.getInstance().addFolder(text);
            }
            Controller.getInstance().showMainList();
        } else if(cmd==cancelCommand) {
            Controller.getInstance().showMainList();
        }
    }

}
