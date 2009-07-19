/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.views;

import com.substanceofcode.gtd.menuactions.AddFolderAction;
import com.substanceofcode.gtd.menuactions.BackupAction;
import com.substanceofcode.gtd.menuactions.DeleteSelectedAction;
import com.substanceofcode.gtd.menuactions.ExitAction;
import com.substanceofcode.gtd.menuactions.PurgeTasksAction;
import com.substanceofcode.gtd.menuactions.ShowAboutAction;
import com.substanceofcode.gtd.menuactions.ShowTasksAction;

/**
 *
 * @author tommi
 */
public class ActionsMenu extends MenuCanvas {

    public ActionsMenu() {
        super( "Menu",
                new MenuItem[]{
            new MenuItem("Purge completed", null, null, new PurgeTasksAction()),
            new MenuItem("New folder", null, null, new AddFolderAction()),
            new MenuItem("Delete selected", null, null, new DeleteSelectedAction()),
            new MenuItem("Backup to CSV file", null, null, new BackupAction()),
            new MenuItem("About", null,null, new ShowAboutAction()),
            new MenuItem("Quit", null, null, new ExitAction()),
            new MenuItem("Cancel", null, null, new ShowTasksAction())
        });
    }

}
