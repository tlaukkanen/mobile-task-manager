/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.views;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.menuactions.SortByDoneAction;
import com.substanceofcode.gtd.menuactions.SortByNameAction;

/**
 *
 * @author tommi
 */
public class SortMenu extends MenuCanvas {

    public SortMenu() {
        super("Sort by",
            new MenuItem[]{
                new MenuItem("Name", null, null, new SortByNameAction( Controller.getInstance().getCurrentFolder() )),
                new MenuItem("Done", null, null, new SortByDoneAction( Controller.getInstance().getCurrentFolder() ))
        });
    }

}
