/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.substanceofcode.gtd.controllers;

import com.substanceofcode.data.FileIOException;
import com.substanceofcode.data.FileSystem;
import com.substanceofcode.gtd.model.FileSelect;
import com.substanceofcode.gtd.model.FolderItem;
import com.substanceofcode.gtd.model.ImportFileSelect;
import com.substanceofcode.gtd.model.Item;
import com.substanceofcode.gtd.model.TodoItem;
import com.substanceofcode.gtd.tasks.AbstractTask;
import com.substanceofcode.gtd.tasks.BackupTask;
import com.substanceofcode.gtd.views.AboutCanvas;
import com.substanceofcode.gtd.views.ActionsMenu;
import com.substanceofcode.gtd.views.BackupPathTextBox;
import com.substanceofcode.gtd.views.FileBrowserCanvas;
import com.substanceofcode.gtd.views.FolderTextBox;
import com.substanceofcode.gtd.views.ItemCanvas;
import com.substanceofcode.gtd.views.ItemForm;
import com.substanceofcode.gtd.views.NoteForm;
import com.substanceofcode.gtd.views.SortMenu;
import com.substanceofcode.gtd.views.SplashCanvas;
import com.substanceofcode.gtd.views.TaskMenu;
import com.substanceofcode.gtd.views.WaitCanvas;
import com.substanceofcode.utils.Log;
import java.io.DataInputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author Tommi Laukkanen (tlaukkanen [at] gmail [dot] com) 
 */
public class Controller {

    private static Controller instance;
    private MIDlet midlet;
    private Display display;
    private FolderItem rootFolder;
    private FolderItem currentFolder;
    private TaskMenu taskMenu;

    private Controller(MIDlet midlet) {
        this.midlet = midlet;
        this.display = Display.getDisplay(midlet);
        try {
//FileSystem.getFileSystem().formatFileSystem();
            boolean rootFileExists = FileSystem.getFileSystem().containsFile("root");
            if (rootFileExists) {
                rootFolder = new FolderItem("Main", null);
                DataInputStream dis = FileSystem.getFileSystem().getFile("root");
                if(dis!=null) {
                    rootFolder.unserialize(dis);
                } else {
                    rootFolder = new FolderItem("Main", null);
                }
            } else {
                //FileSystem.getFileSystem().formatFileSystem();
                rootFolder = new FolderItem("Main", null);
            }
        } catch (Exception ex) {
            Log.debug("Error");
            ex.printStackTrace();
        }
        currentFolder = rootFolder;
    }

    public static Controller getInstance(MIDlet midlet) {
        if (instance == null) {
            instance = new Controller(midlet);
        }
        return instance;
    }

    public static Controller getInstance() {
        return instance;
    }

    public void showSplash() {
        SplashCanvas splash = new SplashCanvas();
        display.setCurrent(splash);
    }

    public void showMainList() {
        if (taskMenu == null) {
            taskMenu = new TaskMenu();
        }
        taskMenu.setFolder(currentFolder);
        try {
            FileSystem.getFileSystem().saveFile("root", rootFolder, true);
        } catch (FileIOException ex) {
            showError(ex);
        }
        display.setCurrent(taskMenu);
    }

    public Displayable getCurrentDisplay() {
        return display.getCurrent();
    }

    public void exit() {
        try {
            FileSystem.getFileSystem().saveFile("root", rootFolder, true);
            midlet.notifyDestroyed();
        } catch (Exception ex) {
            Log.error("Error while exiting: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void showActionsMenu() {
        ActionsMenu menu = new ActionsMenu();
        display.setCurrent(menu);
    }

    public void addItem(String text) {
        TodoItem item = new TodoItem(text);
        currentFolder.addItem(item);
    }

    public void showItem(String string) {
        ItemForm form = new ItemForm(string);
        display.setCurrent(form);
    }

    public void purgeTasks() {
        Vector newItems = new Vector();
        Enumeration en = currentFolder.getItems().elements();
        while (en.hasMoreElements()) {
            Item it = (Item) en.nextElement();
            if (!it.hasChildren()) {
                /** Todo */
                TodoItem item = (TodoItem) it;
                if (!item.isDone()) {
                    newItems.addElement(item);
                }
            } else {
                /** Folder */
                newItems.addElement(it);
            }
        }
        currentFolder.setItems(newItems);
    }

    public void addFolder(String text) {
        FolderItem folder = new FolderItem(text, currentFolder);
        currentFolder.addItem(folder);
    }

    public void showFolder(String string) {
        FolderTextBox folderBox = new FolderTextBox(string);
        display.setCurrent(folderBox);
    }

    public void deleteSelectedItem() {
        taskMenu.deleteSelected();
    }

    public void deleteSelectedItem(int index) {
        int fix = 0;
        if(currentFolder.getParentFolder()!=null) {
            fix = -1;
        }
        if (index+fix < currentFolder.getItems().size()) {
            currentFolder.getItems().removeElementAt(index+fix);
        }
    }

    public void setCurrentFolder(FolderItem folder) {
        currentFolder = folder;
    }

    public synchronized FolderItem getCurrentFolder() {
        return currentFolder;
    }

    public void moveItemUp(int index) {
        int fix = 0;
        if(currentFolder.getParentFolder()!=null) {
            fix = -1;
        }
        if(index+fix-1 < currentFolder.getItems().size() && index+fix>0) {
            Vector items = currentFolder.getItems();
            Item itm = (Item)items.elementAt(index+fix-1);
            items.insertElementAt(itm, index+fix+1);
            items.removeElementAt(index+fix-1);
            showMainList();
        }
    }

    public void moveItemDown(int index) {
        int fix = 0;
        if(currentFolder.getParentFolder()!=null) {
            fix = -1;
        }
        if(index+fix+1 < currentFolder.getItems().size() && index+fix>=0) {
            Vector items = currentFolder.getItems();
            Item itm = (Item)items.elementAt(index+fix+1);
            items.insertElementAt(itm, index+fix);
            items.removeElementAt(index+fix+2);
            showMainList();
        }
    }

    public void editItem(TodoItem item) {
        ItemForm form = new ItemForm(item);
        display.setCurrent(form);
    }

    public void editFolder(FolderItem folder) {
        FolderTextBox form = new FolderTextBox(folder);
        display.setCurrent(form);
    }

    public void edit(int index) {
        if(index>=0 && index<currentFolder.getItems().size()) {
            Item item = (Item)currentFolder.getItems().elementAt(index);
            if(item.hasChildren()) {
                editFolder((FolderItem)item);
            } else {
                editItem((TodoItem)item);
            }
        }
    }

    public void showNoteForm(Item item) {
        NoteForm view = new NoteForm(item);
        display.setCurrent(view);
    }

    public void showItemDetails(Item item) {
        ItemCanvas view = new ItemCanvas(item);
        display.setCurrent(view);
    }

    public void showAbout() {
        try{
            AboutCanvas view = new AboutCanvas();
            display.setCurrent(view);
        } catch(Exception ex) {
            showError(ex);
        }
    }

    public void showError(Exception ex) {
        showError(ex.getMessage());
    }

    public void backup(String path) {
        BackupTask task = new BackupTask(rootFolder, path);
        activateTask(task, "Exporting to " + path);
    }

    public void showBackupPath() {
        BackupPathTextBox view = new BackupPathTextBox();
        display.setCurrent(view);
    }

    public void showSortMenu() {
        SortMenu sortMenu = new SortMenu();
        display.setCurrent(sortMenu);
    }

    public void showError(String err) {
        Alert alert = new Alert("Error", err, null, AlertType.ERROR);
        alert.setTimeout(0);
        display.setCurrent(alert, taskMenu);
    }

    public void showImportFileSelection() {
        FileSelect select = new ImportFileSelect();
        FileBrowserCanvas fileBrowserCanvas = new FileBrowserCanvas( select );
        fileBrowserCanvas.loadRoots();
        display.setCurrent(fileBrowserCanvas);
    }

    public void activateTask(AbstractTask task, String statusText) {
        try {
            WaitCanvas wait = new WaitCanvas(task);
            wait.setWaitText(statusText);
            display.setCurrent(wait);
        } catch(Exception ex) {
            showError("Error while activating task " + ex.getMessage());
        }
    }

    public FolderItem getRootFolder() {
        return this.rootFolder;
    }
}
