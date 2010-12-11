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

import com.substanceofcode.gtd.menuactions.AddFolderAction;
import com.substanceofcode.gtd.menuactions.BackupAction;
import com.substanceofcode.gtd.menuactions.DeleteSelectedAction;
import com.substanceofcode.gtd.menuactions.CutSelectedAction;
import com.substanceofcode.gtd.menuactions.PasteAction;
import com.substanceofcode.gtd.menuactions.ExitAction;
import com.substanceofcode.gtd.menuactions.ImportAction;
import com.substanceofcode.gtd.menuactions.PurgeTasksAction;
import com.substanceofcode.gtd.menuactions.ShowAboutAction;
import com.substanceofcode.gtd.menuactions.ShowSortMenuAction;
import com.substanceofcode.gtd.menuactions.ShowTasksAction;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class ActionsMenu extends MenuCanvas {

    public ActionsMenu() {
        super( "Menu",
                new MenuItem[]{
            new MenuItem("Purge completed", null, null, new PurgeTasksAction()),
            new MenuItem("New folder", null, null, new AddFolderAction()),
            new MenuItem("Delete selected", null, null, new DeleteSelectedAction()),
            new MenuItem("Cut selected", null, null, new CutSelectedAction()),
            new MenuItem("Paste item", null, null, new PasteAction()),
            new MenuItem("Sort", null, null, new ShowSortMenuAction()),
            new MenuItem("Export to file", null, null, new BackupAction()),
            new MenuItem("Import from file", null, null, new ImportAction()),
            new MenuItem("About", null,null, new ShowAboutAction()),
            new MenuItem("Quit", null, null, new ExitAction()),
            new MenuItem("Cancel", null, null, new ShowTasksAction())
        });
        this.menu.alignLeft(true);
    }

}
