import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*****************************************************************************************
 *  <p>
 *  Class Name:     P2PFSGui_elements
 *  <p>
 *  Purpose:        Contain all the GUI elements in one location accessible by multiple
 *                  classes
 *  <p>
 *  Create By:      David Wei
 *  Date:           8/3/2016
 *  Last Modified:  Initial Revision
 *  IDE Used:       Intellij 2016.1.3
 *  <p>
 ****************************************************************************************/
public class P2PFSGui_elements
{
    private ScrollPane SP;
    private Scene scene;
    private Stage stage;

    // Define Array lists for 3 types of lists
    // -- List of users who are local user (Only one user)
    private ArrayList<P2PFSGui_user> myList;
    // -- List of users who are we can download from
    private ArrayList<P2PFSGui_user> userList;
    // -- List of users who are downloading from us
    private ArrayList<P2PFSGui_user> uploadingList;

    public final int MAX_WINDOW_HEIGHT = 500;
    public final int MAX_WINDOW_WIDTH = 510;

    public final int POPUP_WINDOW_HEIGHT = 100;
    public final int POPUP_WINDOW_WIDTH = 300;

    public P2PFSGui_elements(Stage s)
    {
        stage = s;

        myList = new ArrayList<P2PFSGui_user>();
        userList = new ArrayList<P2PFSGui_user>();
        uploadingList = new ArrayList<P2PFSGui_user>();
    }

    public Scene getScene()
    {
        return scene;
    }

    public ScrollPane getScrollPane()
    {
        return SP;
    }

    public Stage getStage()
    {
        return stage;
    }

    private P2PFSGui_user getUser(ArrayList<P2PFSGui_user> list, String name)
    {
        Iterator userItr = list.iterator();

        // Search for User
        while (userItr.hasNext())
        {
            P2PFSGui_user user = (P2PFSGui_user) userItr.next();

            if (user.getUsername().equals(name))
            {
                return user;
            }
        }

        // Throw exception if no user found
        throw new NoSuchElementException();
    }

    public P2PFSGui_user getMe()
    {
        return myList.get(0);
    }

    public P2PFSGui_user getUser(String name)
    {
       try
        {
            return getUser(userList, name);
        } catch (NoSuchElementException e)
        {
            throw e;
        }
    }

    public P2PFSGui_user getUploadUser(String name)
    {
        try
        {
            return getUser(uploadingList, name);
        } catch (NoSuchElementException e)
        {
            throw e;
        }
    }

    public void setScene(Scene s)
    {
        scene = s;
    }

    public void setScrollPane(ScrollPane s)
    {
        SP = s;
    }

    public void setStage(Stage s)
    {
        stage = s;
    }

    public void setMe(P2PFSGui_user user)
    {
        myList.add(user);
    }

    // Add and Remove users
    public void addUser(P2PFSGui_user user)
    {
        userList.add(user);
        redraw();
    }

    public void removeUser(String name)
    {
        Iterator userItr = userList.iterator();

        while (userItr.hasNext())
        {
            P2PFSGui_user user = (P2PFSGui_user) userItr.next();

            if (user.getUsername().equals(name))
            {
                userList.remove(user);
                break;
            }
        }

        redraw();
    }

    public void upload(String user, String filename)
    {
        // Create new file
        P2PFSGui_file file = new P2PFSGui_file(filename, this);
        file.setStatus(P2PFSGui_file.FileStatus.uploadingFile);

        // Set User
        P2PFSGui_user upldUser;

        // Check if user exists
        try
        {
            // if so, add file
            upldUser = getUser(uploadingList, user);
            upldUser.addFile(file);

        } catch (NoSuchElementException e)
        {
            // else add new user
            upldUser = new P2PFSGui_user(user, this);
            upldUser.addFile(file);
            uploadingList.add(upldUser);
        }

        redraw();
    }

    public void finishUpload(String user, String filename)
    {
        // Set Download User
        P2PFSGui_user upldUser;

        // Check if user exists
        try
        {
            upldUser = getUser(uploadingList, user);
            upldUser.removeFile(filename);

            // if dnldUser has no more files, remove dnld user
            if (upldUser.getNumFiles() == 0)
                uploadingList.remove(upldUser);

        } catch (NoSuchElementException e)
        {
            throw e;
        }

        redraw();
    }
    public void finishDownload(String user, String filename)
    {
        // Set Download User
        P2PFSGui_user dnldUser;

        // Check if user exists
        try
        {
            dnldUser = getUser(user);
            dnldUser.getFile(filename).setStatus(P2PFSGui_file.FileStatus.normalFile);
        } catch (NoSuchElementException e)
        {
            throw e;
        }

        redraw();
    }

    // Redraw the GUI
    public void redraw()
    {
        // Get current TabPane
        TabPane oldTP = (TabPane) SP.getContent();

        // Create TabPane
        TabPane tp = new TabPane();

        // Add Create new tabs for each type of files
        Tab myFilesTab = new Tab();
        Tab dnldToMeTab = new Tab();
        Tab dnldFromMeTab = new Tab();

        // Create the tabs and create VBox with content from lists
        myFilesTab.setText("My Files");
        myFilesTab.setContent(createVB(myList));
        dnldFromMeTab.setText("Downloading From Me");
        dnldFromMeTab.setContent(createVB(uploadingList));
        dnldToMeTab.setText("Download To Me");
        dnldToMeTab.setContent(createVB(userList));

        // Add tabs to TabPane
        tp.getTabs().add(myFilesTab);
        tp.getTabs().add(dnldToMeTab);
        tp.getTabs().add(dnldFromMeTab);

        // Set tabs to not have any close buttons
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Show the tab that was shown before
        tp.getSelectionModel().select(oldTP.getSelectionModel().getSelectedIndex());

        SP.setContent(tp);
    }

    private VBox createVB(ArrayList<P2PFSGui_user> list)
    {
        VBox vb = new VBox();
        vb.setLayoutX(5);
        vb.setSpacing(10);
        vb.setMinHeight(MAX_WINDOW_HEIGHT - 30);
        vb.setStyle("-fx-padding: 20;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;");

        Iterator userItr = list.iterator();

        while (userItr.hasNext())
        {
            P2PFSGui_user user = (P2PFSGui_user) userItr.next();

            for (HBox hb : user.getHBoxList())
            {
                vb.getChildren().add(hb);
            }
        }

        return vb;
    }

    public void closeGui()
    {
        System.out.println("Number of Users Downloading Items = " + uploadingList.size());

        if (uploadingList.size() > 0)
        {
            P2PFSGui_notification notify = new P2PFSGui_notification();

            notify.createNotification("Users are still downloading files. \nApplication will close " +
                    "when all downloads are finished", "Finishing downloads");

        } else
        {
            System.out.println("Closing Gui");
        }

        stage.close();

    }
}

