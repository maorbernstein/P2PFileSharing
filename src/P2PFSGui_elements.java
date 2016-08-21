import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static javafx.application.Platform.isFxApplicationThread;
import static javafx.application.Platform.runLater;

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
class P2PFSGui_elements
{
    private ScrollPane SP;
    private Scene scene;
    private Stage stage;

    private FileManagerGUI_IF fm;
    private UserManagerGUI_IF um;

    // Define Array lists for 3 types of lists
    // -- List of users who are local user (Only one user)
    private ArrayList<P2PFSGui_user> myList;
    // -- List of users who are we can download from
    private ArrayList<P2PFSGui_user> userList;
    // -- List of users who are downloading from us
    private ArrayList<P2PFSGui_user> uploadingList;

    final static int MAX_WINDOW_HEIGHT = 500;
    final static int MAX_WINDOW_WIDTH = 510;

    final static int POPUP_WINDOW_HEIGHT = 150;
    final static int POPUP_WINDOW_WIDTH = 300;

    final static int GUI_TIMEOUT_SEC = 60;

    P2PFSGui_elements(Stage s, FileManagerGUI_IF f, UserManagerGUI_IF u)
    {
        stage = s;

        myList = new ArrayList<P2PFSGui_user>();
        userList = new ArrayList<P2PFSGui_user>();
        uploadingList = new ArrayList<P2PFSGui_user>();

        fm = f;
        um = u;
    }

    Scene getScene()
    {
        return scene;
    }

    ScrollPane getScrollPane()
    {
        return SP;
    }

    Stage getStage()
    {
        return stage;
    }


    UserManagerGUI_IF getUm()
    {
        return um;
    }


    FileManagerGUI_IF getFm()
    {
        return fm;
    }
    private P2PFSGui_user getUser(ArrayList<P2PFSGui_user> list, String name)
    {
        // Search for User
        for (P2PFSGui_user user : list)
        {
            if (user.getUsername().equals(name))
            {
                return user;
            }
        }

        // Throw exception if no user found
        throw new NoSuchElementException();
    }

    P2PFSGui_user getMe()
    {
        return myList.get(0);
    }

    P2PFSGui_user getUser(String name)
    {
       try
        {
            return getUser(userList, name);
        } catch (NoSuchElementException e)
        {
            throw e;
        }
    }

    P2PFSGui_user getUploadUser(String name)
    {
        try
        {
            return getUser(uploadingList, name);
        } catch (NoSuchElementException e)
        {
            throw e;
        }
    }

    void setScene(Scene s)
    {
        scene = s;
    }

    void setScrollPane(ScrollPane s)
    {
        SP = s;
    }

    void setStage(Stage s)
    {
        stage = s;
    }

    void setMe(P2PFSGui_user user)
    {
        myList.add(user);
    }

    // Add and Remove users
    void addUser(P2PFSGui_user user)
    {
        userList.add(user);
        redraw();
    }

    void removeUser(String name)
    {

        for (P2PFSGui_user user : userList)
        {
            if (user.getUsername().equals(name))
            {
                userList.remove(user);
                break;
            }
        }

        redraw();
    }

    void upload(String user, String filename)
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

    void finishUpload(String user, String filename)
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
    void finishDownload(String user, String filename)
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
    void redraw()
    {
        // Get current TabPane
        TabPane oldTP = (TabPane) SP.getContent();

        // Create TabPane
        TabPane tp = new TabPane();

        // Add Create new tabs for each type of files
        Tab myFilesTab = new Tab();
        Tab networkFilesTab = new Tab();
        Tab requestedFilesTab = new Tab();

        // Create the tabs and create VBox with content from lists
        myFilesTab.setText("My Files");
        myFilesTab.setContent(createVB(myList));
        networkFilesTab.setText("Network Files");
        networkFilesTab.setContent(createVB(userList));
        requestedFilesTab.setText("Requested Files");
        requestedFilesTab.setContent(createVB(uploadingList));

        // Add tabs to TabPane
        tp.getTabs().add(myFilesTab);
        tp.getTabs().add(networkFilesTab);
        tp.getTabs().add(requestedFilesTab);

        // Set tabs to not have any close buttons
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Show the tab that was shown before
        tp.getSelectionModel().select(oldTP.getSelectionModel().getSelectedIndex());

        if (isFxApplicationThread())
        {
            SP.setContent(tp);
        }
        else
        {
            runLater(() -> SP.setContent(tp));
        }
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

        for (P2PFSGui_user user : list)
        {
            for (HBox hb : user.getHBoxList())
            {
                vb.getChildren().add(hb);
            }
        }

        return vb;
    }

    // Checks if all downloads are finished before closing the GUI
    boolean closeGui()
    {

        um.close();

        System.out.println("Number of Users Downloading Items = " + uploadingList.size());

        if (uploadingList.size() > 0)
        {
            P2PFSGui_notification notify = new P2PFSGui_notification();

            notify.createNotification("Users are still downloading files. \nPlease wait until " +
                    "all downloads are finished", "Finishing downloads");

            return false;
        } else
        {
            System.out.println("Closing Gui");
            return true;
        }
    }
}

