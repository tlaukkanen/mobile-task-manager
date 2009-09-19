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

import com.substanceofcode.utils.ImageUtil;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class ImageRepository {

    private static Image bulletImage = ImageUtil.loadImage("/images/bullet_white.png");
    private static Image tickImage = ImageUtil.loadImage("/images/tick.png");
    private static Image folderImage = ImageUtil.loadImage("/images/folder.png");
    private static Image backImage = ImageUtil.loadImage("/images/arrow_medium_left.png");
    private static Image starImage = ImageUtil.loadImage("/images/star.png");
    private static Image noteImage = ImageUtil.loadImage("/images/comment.png");
    private static Image greenFlagImage = ImageUtil.loadImage("/images/flag_green.png");
    private static Image yellowFlagImage = ImageUtil.loadImage("/images/flag_yellow.png");
    private static Image redFlagImage = ImageUtil.loadImage("/images/flag_red.png");


    public static Image getBullet() {
        return bulletImage;
    }

    public static Image getTick() {
        return tickImage;
    }

    static Image getFolder() {
        return folderImage;
    }

    static Image getBackArrow() {
        return backImage;
    }

    static Image getNote() {
        return noteImage;
    }

    static Image getStar() {
        return starImage;
    }

    static Image getGreenFlag() {
        return greenFlagImage;
    }

    static Image getYellowFlag() {
        return yellowFlagImage;
    }

    static Image getRedFlag() {
        return redFlagImage;
    }
}
