/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.menuactions;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.model.FolderItem;
import com.substanceofcode.gtd.model.Item;
import com.substanceofcode.gtd.views.MenuAction;
import java.util.Vector;

/**
 *
 * @author tommi
 */
public class SortByNameAction implements MenuAction {

    FolderItem folder;

    public SortByNameAction(FolderItem folder) {
        this.folder = folder;
    }

    public void activate() {
        bubbleSort(folder.getItems());
        Controller.getInstance().showMainList();
    }

    private static void bubbleSort(Vector items) {
        boolean unsorted = true;
        System.out.println("Sorting...");
        while(unsorted) {
            unsorted = false;
            for(int i=items.size()-1; i>0; i--) {
                System.out.println("Compare...");
                Item item1 = (Item)items.elementAt(i);
                Item item2 = (Item)items.elementAt(i-1);
                if(item1.getName().compareTo(item2.getName())<0) {
                    System.out.println("Change...");
                    items.setElementAt(item1, i-1);
                    items.setElementAt(item2, i);
                    unsorted = true;
                }
            }
        }
    }

}
