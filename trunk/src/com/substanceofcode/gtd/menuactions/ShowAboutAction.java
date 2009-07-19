/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.menuactions;

import com.substanceofcode.gtd.controllers.Controller;
import com.substanceofcode.gtd.views.MenuAction;

/**
 *
 * @author tommi
 */
public class ShowAboutAction implements MenuAction {

    public void activate() {
        Controller.getInstance().showAbout();
    }

}
