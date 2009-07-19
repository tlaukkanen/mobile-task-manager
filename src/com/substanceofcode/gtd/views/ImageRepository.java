/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.substanceofcode.gtd.views;

import com.substanceofcode.utils.ImageUtil;
import javax.microedition.lcdui.Image;

/**
 *
 * @author tommi
 */
public class ImageRepository {

    private static Image bulletImage = ImageUtil.loadImage("/images/bullet_white.png");
    private static Image tickImage = ImageUtil.loadImage("/images/tick.png");
    private static Image folderImage = ImageUtil.loadImage("/images/folder.png");
    private static Image backImage = ImageUtil.loadImage("/images/arrow_medium_left.png");
    private static Image starImage = ImageUtil.loadImage("/images/star.png");
    private static Image noteImage = ImageUtil.loadImage("/images/comment.png");

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

}
