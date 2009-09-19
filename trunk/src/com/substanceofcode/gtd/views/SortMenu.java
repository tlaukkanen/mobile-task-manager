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
import com.substanceofcode.gtd.menuactions.SortByDoneAction;
import com.substanceofcode.gtd.menuactions.SortByNameAction;
import com.substanceofcode.gtd.menuactions.SortByPriorityAction;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class SortMenu extends MenuCanvas {

    public SortMenu() {
        super("Sort by",
            new MenuItem[]{
                new MenuItem("Name", null, null, new SortByNameAction( Controller.getInstance().getCurrentFolder() )),
                new MenuItem("Done", null, null, new SortByDoneAction( Controller.getInstance().getCurrentFolder() )),
                new MenuItem("Priority", null, null, new SortByPriorityAction( Controller.getInstance().getCurrentFolder() ))
        });
    }

}
