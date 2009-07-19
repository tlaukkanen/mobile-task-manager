/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.menuactions;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.model.FolderItem;
import com.substanceofcode.gtd.views.MenuAction;

/**
 *
 * @author tommi
 */
public class OpenFolderAction implements MenuAction {

    FolderItem folder;

    public OpenFolderAction(FolderItem folder) {
        this.folder = folder;
    }

    public void activate() {
        Controller controller = Controller.getInstance();
        controller.setCurrentFolder(folder);
        controller.showMainList();
    }
}
