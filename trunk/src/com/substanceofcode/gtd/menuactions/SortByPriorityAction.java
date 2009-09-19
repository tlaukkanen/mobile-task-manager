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

package com.substanceofcode.gtd.menuactions;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.model.FolderItem;
import com.substanceofcode.gtd.model.Item;
import com.substanceofcode.gtd.model.TodoItem;
import com.substanceofcode.gtd.views.MenuAction;
import java.util.Vector;

/**
 *
 * @author tommi
 */
public class SortByPriorityAction implements MenuAction {

    FolderItem folder;

    public SortByPriorityAction(FolderItem folder) {
        this.folder = folder;
    }

    public void activate() {
        prioritySort(folder.getItems());
        Controller.getInstance().showMainList();
    }

    private static void prioritySort(Vector items) {
        boolean unsorted = true;
        System.out.println("Sorting...");
        while(unsorted) {
            unsorted = false;
            for(int i=items.size()-1; i>0; i--) {
                System.out.println("Compare...");
                Item item1 = (Item)items.elementAt(i);
                TodoItem todoItem1 = null;
                if(item1.hasChildren()==false) {
                    todoItem1 = (TodoItem)item1;
                }
                Item item2 = (Item)items.elementAt(i-1);
                TodoItem todoItem2 = null;
                if(item2.hasChildren()==false) {
                    todoItem2 = (TodoItem)item2;
                }
                if(todoItem1!=null &&
                   todoItem2!=null &&
                   todoItem1.getPriority() > todoItem2.getPriority()) {
                    System.out.println("Change...");
                    items.setElementAt(item1, i-1);
                    items.setElementAt(item2, i);
                    unsorted = true;
                } else if(todoItem1==null && todoItem2!=null) {
                    items.setElementAt(item1, i-1);
                    items.setElementAt(item2, i);
                    unsorted = true;
                }
            }
        }
    }

}
