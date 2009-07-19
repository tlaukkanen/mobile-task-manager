/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.menuactions;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.model.TodoItem;
import com.substanceofcode.gtd.views.Menu;
import com.substanceofcode.gtd.views.MenuAction;

/**
 *
 * @author tommi
 */
public class ToggleTaskAction implements MenuAction {

    static int clickCount = 0;
    TodoItem item;
    Menu menu;

    public ToggleTaskAction(TodoItem item, Menu menu) {
        this.item = item;
        this.menu = menu;
    }

    public void activate(){
        item.setDone( !item.isDone() );
        menu.selectNext();
        Controller.getInstance().showMainList();
    }

}
