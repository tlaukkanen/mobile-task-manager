/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class SortByDoneAction implements MenuAction {

    FolderItem folder;

    public SortByDoneAction(FolderItem folder) {
        this.folder = folder;
    }

    public void activate() {
        doneSort(folder.getItems());
        Controller.getInstance().showMainList();
    }

    private static void doneSort(Vector items) {
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
                   todoItem1.isDone()==false &&
                   todoItem2.isDone()==true) {
                    System.out.println("Change...");
                    items.setElementAt(item1, i-1);
                    items.setElementAt(item2, i);
                    unsorted = true;
                }
            }
        }
    }
}
