import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*****************************************************************************************
 * <p>
 * Class Name:     P2PFSGUI
 * <p>
 * Purpose:        The Coordinator class for the GUI
 * <p>
 * Create By:      David Wei
 * Date:           7/18/2016
 * Last Modified:  Initial Revision
 * IDE Used:       Intellij 2016.1.3
 * <p>
 ****************************************************************************************/

public class P2PFSGui extends Application implements
        GUIFileManager_IF, GUINetworkCoordinator_IF, GUIUserManager_IF
{
    // Define the GUI Elements
    private GridPane startUpGrid;
    private Scene startUpScene;
    private Stage startUpStage;

    // Username of offline person
    private String username;

    // Define guiElems object to hold all GUI components
    private P2PFSGui_elements guiElems;

    // Define error element to throw notifications as needed
    private P2PFSGui_notification notify = new P2PFSGui_notification();

    // Define regular expression for format of username
    private final String USERNAME_REGEX = "^[A-Z|a-z][A-Z|a-z|0-9]+$";

    // Define Username max and min lengths
    private final int USERNAME_MAX_LENGTH = 10;
    private final int USERNAME_MIN_LENGTH = 3;

    // Start the GUI
    @Override
    public void start(Stage mainStage)
    {
        // Send GUI Stage element to guiElems
        guiElems = new P2PFSGui_elements(mainStage);

        createStartUpScene();
    }

    // Create popup window to ask for username and whether to join network or not
    private void createStartUpScene()
    {
        // Create buttons for join and create network
        Button joinNetworkBtn = new Button();
        joinNetworkBtn.setText("Join Network");

        Button createNetworkBtn = new Button();
        createNetworkBtn.setText("Create Network");

        // Create text and text field to grab username
        Label usernameLabel = new Label("Username: ");
        TextField usernameTextField = new TextField();

        // Add event handler to join network button
        joinNetworkBtn.setOnAction( (ActionEvent e) -> {

            // Grab Username
            username = usernameTextField.getText();
            System.out.println("Joining Network");

            // Grab invitation file
            FileChooser invFileChooser = new FileChooser();
            invFileChooser.setTitle("Select Invitation File");
            File file = invFileChooser.showOpenDialog(startUpStage);
            System.out.println("Opening file: " + file.getName());

            // Check for valid username
            if(checkUsername())
            {
                System.out.println("Username verified");
                System.out.println("Username: " + username);

                if(true) // TODO: add join network code here
                {
                    System.out.println("Network joined successfully");

                    createMainStage();

                    P2PFSGui_user me = new P2PFSGui_user(username, guiElems, true);
                    guiElems.setMe(me);
                    guiElems.redraw();

                    guiElems.getStage().show();
                    startUpStage.close();
                }
                else
                {
                    System.out.println("Failed to join network");
                    notify.createError("Failed to join network");
                }
            }
        });

        createNetworkBtn.setOnAction((ActionEvent e) -> {

            username = usernameTextField.getText();
            System.out.println("Creating Network");

            // Verify Username
            if(checkUsername())
            {
                System.out.println("Username verified");
                System.out.println("Username: " + username);

                createMainStage();

                // Set current user as main user.
                P2PFSGui_user me = new P2PFSGui_user(username, guiElems, true);
                guiElems.setMe(me);
                guiElems.redraw();

                guiElems.getStage().show();
                startUpStage.close();
            }
        });

        // Setup Grid to hold elements for Startup Pop-Up
        startUpGrid = new GridPane();
        startUpGrid.setAlignment(Pos.CENTER);
        startUpGrid.setHgap(10);
        startUpGrid.setVgap(10);

        // Add created elements to the GridPane
        startUpGrid.add(usernameLabel, 0, 0);
        startUpGrid.add(usernameTextField, 1, 0);
        startUpGrid.add(joinNetworkBtn, 0, 1);
        startUpGrid.add(createNetworkBtn, 1, 1);

        // Set the GridPane as element in a new Scene
        startUpScene = new Scene(startUpGrid, guiElems.POPUP_WINDOW_WIDTH,
                guiElems.POPUP_WINDOW_HEIGHT);

        // Create new Stage and add Scene created earlier
        startUpStage = new Stage();
        startUpStage.setScene(startUpScene);
        // Set Pop-up as temporary
        startUpStage.initModality(Modality.APPLICATION_MODAL);
        startUpStage.setTitle("P2P File Sharing");

        // Show the startup pop-up
        startUpStage.show();
    }

    private void createMainStage()
    {
        // Create Main VBox to contain all main window elements
        VBox mainGroup = new VBox();
        guiElems.setScene(new Scene(mainGroup, guiElems.MAX_WINDOW_WIDTH,
                guiElems.MAX_WINDOW_HEIGHT));

        //
        final Button inviteBtn = new Button("Invite");
        inviteBtn.setOnAction( (ActionEvent e) -> {
            // TODO: Add generate Invitation file
            // TODO: Print out Invitation file Location

            notify.createNotification("Invitation file Generated at \nDIRECTORY", "Invitation File Generated");
            System.out.println("Generated Invitation File");
        });

        // Create ScrollPane to contain all elements and add scroll bar
        guiElems.setScrollPane(new ScrollPane(new TabPane()));
        guiElems.getScrollPane().setMinHeight(guiElems.MAX_WINDOW_HEIGHT - 30);

        // Add all elements to main group and set the stage
        mainGroup.getChildren().addAll(inviteBtn, guiElems.getScrollPane());
        guiElems.setStage(new Stage());
        guiElems.getStage().setScene(guiElems.getScene());
        guiElems.getStage().setTitle("P2P File Sharing");
    }

    private Boolean checkUsername()
    {
        // Used for Regular Expression Pattern Matching
        Pattern p = Pattern.compile(USERNAME_REGEX);
        Matcher m = p.matcher(username);

        // Checks if Username is more than 3 characters
        if(username.length() < USERNAME_MIN_LENGTH)
        {
            notify.createError("Username must be longer than " + USERNAME_MIN_LENGTH
                    + " characters.");
            System.out.println("Username: " + username + " is invalid.");

            return false;
        }
        else if(username.length() > USERNAME_MAX_LENGTH)
        {
            notify.createError("Username must be less than " + USERNAME_MAX_LENGTH
                    + " characters long.");
            System.out.println("Username: " + username + " is invalid.");

            return false;
        }
        // Checks that Username is of pattern (Alpha char) + (Alpha numeric char)*x
        else if (!m.find())
        {
            notify.createError("Usernames may not contain the following characters:\n!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~");
            System.out.println("Username: " + username + " is invalid.");

            return false;
        }

        return true;
    }

    @Override
    public void fileOpSuccess()
    {
        System.out.println("File Operation was successful");
    }

    @Override
    public void fileNotFound()
    {
        notify.createError("File not found");
        System.out.println("File not Found");
    }

    @Override
    public void IOexception()
    {
        notify.createError("I/O Exception");
        System.out.println("I/O Exception encountered");
    }

    @Override
    public void uploadPercentComplete(String user, String filename, double percent)
    {
        // Update percentage of file
        try
        {
            guiElems.getUploadUser(user).getFile(filename).setUploadProgress(percent);
        } catch (NoSuchElementException e)
        {
            System.out.println("Update Percentage file not found. \n    Username: " + user
                    + "\n   Filename: " + filename);
        }

        if(percent == 1)
        {
            guiElems.finishUpload(user, filename);
        }

        System.out.println("Updated Percentage");
    }

    @Override
    public void downloadPercentComplete(String user, String filename, double percent)
    {
        // Update percentage of file
        try
        {
            guiElems.getUser(user).getFile(filename).setDownloadProgress(percent);
        } catch (NoSuchElementException e)
        {
            System.out.println("Update Percentage file not found. \n    Username: " + user
                    + "\n   Filename: " + filename);
        }

        if(percent == 1)
        {
            guiElems.finishDownload(user, filename);
        }

        System.out.println("Updated Percentage");
    }

    @Override
    public void addNewFile(String[][] files)
    {
        for(int i = 0; i < files.length; i++)
        {
            for (int j = 1; j < files[i].length; j++)
            {
                // TODO: fix the file name / username
                guiElems.getUser(files[i][0]).addFile(
                        new P2PFSGui_file(files[i][j], guiElems, false));

                System.out.println("Added User: " + files[i][0] + " Filename: " + files[i][j]);
            }
        }

        guiElems.redraw();
    }

    @Override
    public void removeFile(String[][] files)
    {
        for(int i = 0; i < files.length; i++)
        {
            for (int j = 1; j < files[i].length; j++)
            {
                try
                {
                    // TODO: fix the file name / username
                    guiElems.getUser(files[i][0]).removeFile(files[i][j]);
                    System.out.println("Removed User: " + files[i][0] + " Filename: " + files[i][j]);
                } catch (NoSuchElementException e)
                {
                    System.out.println("Remove Element not found. \n    Username: " + files[i][0]
                            + "\n   Filename: " + files[i][j]);
                }
            }
        }

        guiElems.redraw();
    }

    @Override
    public void updateFile(String[][] files)
    {
        for(int i = 0; i < files.length; i++)
        {
            for (int j = 1; j < files[i].length; j++)
            {
                // TODO: fix the file name / username
                try
                {
                    guiElems.getUser(files[i][0]).getFile(
                            files[i][j]).setStatus(P2PFSGui_file.FileStatus.updatedFile);
                }catch (NoSuchElementException e)
                {
                    System.out.println("Update Element not found. \n    Username: " + files[i][0]
                            + "\n   Filename: " + files[i][j]);
                }
                System.out.println("Updated User: " + files[i][0] + " Filename: " + files[i][j]);
            }
        }

        guiElems.redraw();
    }

    @Override
    public void addUser(String username)
    {
        guiElems.addUser(new P2PFSGui_user(username, guiElems, false));
        System.out.println("Added User: " + username );
    }

    @Override
    public void removeUser(String username)
    {
        guiElems.removeUser(username);
        System.out.println("Removed User: " + username );
    }

    @Override
    public void connectionStatus(boolean established)
    {
        if (!established)
        {
            notify.createError("Connection Failure");
            System.out.println("Connection Failure");
        } else
        {
            System.out.println("Connection Successful");

        }
    }

    @Override
    public void uploadStarted(String user, String filename)
    {
        guiElems.upload(user, filename);
        System.out.println("Upload Started");
    }

    @Override
    public void stop()
    {
        guiElems.closeGui();
    }
}