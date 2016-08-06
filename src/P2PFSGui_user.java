import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*****************************************************************************************
 *  <p>
 *  Class Name:     P2PFSGui_user
 *  <p>
 *  Purpose:        Class to contain a User
 *  <p>
 *  Create By:      David Wei
 *  Date:           8/3/2016
 *  Last Modified:  Initial Revision
 *  IDE Used:       Intellij 2016.1.3
 *  <p>
 ****************************************************************************************/
public class P2PFSGui_user
{
    private String username;
    // Boolean to determine if User is local user
    private boolean isMe;
    private P2PFSGui_elements guiElem;

    // ArrayList to contain all files associated with user
    private ArrayList<P2PFSGui_file> filelist;

    public P2PFSGui_user(String name, P2PFSGui_elements elems, Boolean me)
    {
        username = name;
        isMe = me;

        guiElem = elems;

        filelist = new ArrayList<P2PFSGui_file>();
    }

    public P2PFSGui_user(String name, P2PFSGui_elements elems)
    {
        username = name;
        isMe = false;

        guiElem = elems;

        filelist = new ArrayList<P2PFSGui_file>();
    }

    public String getUsername()
    {
        return username;
    }

    public void removeFile(String filename)
    {
        Iterator fileItr = filelist.iterator();

        while(fileItr.hasNext())
        {
            P2PFSGui_file file = (P2PFSGui_file) fileItr.next();

            if(file.getFilename().equals(filename))
            {
                filelist.remove(file);
                break;
            }
        }
    }

    public P2PFSGui_file getFile(String filename)
    {
        Iterator fileItr = filelist.iterator();

        // Search for file
        while(fileItr.hasNext())
        {
            P2PFSGui_file file = (P2PFSGui_file) fileItr.next();

            if(file.getFilename().equals(filename))
            {
                return file;
            }
        }

        // Throw exception if no element is found
        throw new NoSuchElementException();
    }

    public void addFile(P2PFSGui_file file)
    {
        filelist.add(file);
    }

    public int getNumFiles()
    {
        return filelist.size();
    }

    // Create ArrayList of HBox elements to add to GUI
    public ArrayList<HBox> getHBoxList()
    {
        ArrayList<HBox> list = new ArrayList<HBox>();

        // Define separators for each user
        Separator sep1 = new Separator();
        sep1.setOrientation(Orientation.HORIZONTAL);
        Separator sep2 = new Separator();
        sep2.setOrientation(Orientation.HORIZONTAL);
        sep1.setMinWidth(guiElem.MAX_WINDOW_WIDTH - 70);
        sep2.setMinWidth(guiElem.MAX_WINDOW_WIDTH - 70);

        // Create HBoxes to hold separators
        HBox sep1HB = new HBox();
        HBox sep2HB = new HBox();
        sep1HB.getChildren().add(sep1);
        sep2HB.getChildren().add(sep2);

        // Add user to Arraylist
        list.add(sep1HB);
        list.add(createHBox());
        list.add(sep2HB);

        // Add files
        Iterator fileIterator = filelist.iterator();
        while(fileIterator.hasNext())
        {
            P2PFSGui_file element = (P2PFSGui_file) fileIterator.next();

            list.add(element.createHBox());
        }

        return list;
    }

    // Create HBox for User
    private HBox createHBox()
    {
        HBox hb = new HBox();

        // Create Username and Font
        Label user = new Label(username);
        user.setFont(new Font("Arial", 15));
        user.setMinWidth(guiElem.MAX_WINDOW_WIDTH - 110);

        // If user is local user
        if(isMe)
        {
            // Add Add button
            Button addBtn = new Button("Add");

            addBtn.setOnAction( (ActionEvent e) -> {
                FileChooser invFileChooser = new FileChooser();
                invFileChooser.setTitle("Select File to Add");
                File file = invFileChooser.showOpenDialog(guiElem.getStage());

                P2PFSGui_file newFile = new P2PFSGui_file(file.getName(), guiElem, isMe);

                filelist.add(newFile);

                guiElem.redraw();
            });

            hb.getChildren().addAll(user, addBtn);
        }
        else
        {
            hb.getChildren().add(user);
        }

        return hb;
    }
}
