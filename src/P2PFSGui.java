import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*****************************************************************************************
 * <p>
 * Class Name:     P2PFSGUI
 * <p>
 * Purpose:        PURPOSE
 * <p>
 * Create By:      David Wei
 * Date:           7/18/2016
 * Last Modified:  Initial Revision
 * IDE Used:       Intellij 2016.1.3
 * <p>
 ****************************************************************************************/

public class P2PFSGui extends Application
{
    private GridPane startUpGrid;
    private Scene startUpScene;
    private Stage startUpStage;

    private VBox mainVB;
    private Scene mainScene;
    private Stage mainStage;
    private TableView mainTableView;

    private ScrollBar scrollBar;

    private String username;

    private final int MAX_WINDOW_HEIGHT = 500;
    private final String USERNAME_REGEX = "^[A-Z|a-z][A-Z|a-z|0-9]+$";

    private Image newFile;
    private Image downloadingFile;
    private Image normalFile;
    private Image updatedFile;

    @Override
    public void start(Stage mainStage)
    {
        newFile = new Image("/resources/newFile.png");
        normalFile = new Image("/resources/normalFile.png");
        downloadingFile = new Image("/resources/downloadingFile.png");
        updatedFile = new Image("/resources/updatedFile.png");

        //newFile = new Image(getClass().getResourceAsStream("newFile.png");
        //normalFile = new Image(getClass().getResourceAsStream("normalFile.png"));
        //downloadingFile = new Image(getClass().getResourceAsStream("downloadingFile.png"));
        //updatedFile = new Image(getClass().getResourceAsStream("updatedFile.png"));

        createStartUpScene();
    }

    private void createStartUpScene()
    {
        Button joinNetworkBtn = new Button();
        joinNetworkBtn.setText("Join Network");

        Button createNetworkBtn = new Button();
        createNetworkBtn.setText("Create Network");
        //HBox createNetworkBHB = new HBox();
        //createNetworkBHB.setAlignment(Pos.CENTER_RIGHT);
        //createNetworkBHB.getChildren().add(createNetworkBtn);

        Label usernameLabel = new Label("Username: ");
        TextField usernameTextField = new TextField();

        joinNetworkBtn.setOnAction( (ActionEvent e) -> {

            username = usernameTextField.getText();
            System.out.println("Joining Network");

            FileChooser invFileChooser = new FileChooser();
            invFileChooser.setTitle("Select Invitation File");
            File file = invFileChooser.showOpenDialog(startUpStage);

            System.out.println("Opening file: " + file.getName());

            createMainStage();

            if(username.length() < 3)
            {
                createErrorStage("Username must be longer than 3 characters.");
                System.out.println("Username: " + username + " is invalid.");
            }
            else
            {
                // TODO: Add try catch block to verify joining network went OK

                createMainStage();

                System.out.println("Username: " + username);
                mainStage.show();
                startUpStage.close();
            }
        });

        createNetworkBtn.setOnAction((ActionEvent e) -> {

            username = usernameTextField.getText();
            System.out.println("Creating Network");

            if(checkUsername())
            {
                createMainStage();

                System.out.println("Username: " + username);
                mainStage.show();
                startUpStage.close();
            }
        });

        startUpGrid = new GridPane();
        startUpGrid.setAlignment(Pos.CENTER);
        startUpGrid.setHgap(10);
        startUpGrid.setVgap(10);
        //startUpGrid.setPadding(new Insets(25, 25, 25, 25));

        startUpGrid.add(usernameLabel, 0, 0);
        startUpGrid.add(usernameTextField, 1, 0);
        startUpGrid.add(joinNetworkBtn, 0, 1);
        //startUpGrid.add(createNetworkBHB, 1, 1);
        startUpGrid.add(createNetworkBtn, 1, 1);

        startUpScene = new Scene(startUpGrid, 300, 100);

        startUpStage = new Stage();
        startUpStage.setScene(startUpScene);
        startUpStage.initModality(Modality.APPLICATION_MODAL);
        startUpStage.setTitle("P2P File Sharing");

        startUpStage.show();
    }

    private void createMainStage()
    {
        VBox mainGroup = new VBox();
        mainScene = new Scene(mainGroup, 510, MAX_WINDOW_HEIGHT);

        final Menu menu = new Menu("Invite");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        mainVB = new VBox();
        mainVB.setLayoutX(5);
        mainVB.setSpacing(10);
        mainVB.setMinHeight(MAX_WINDOW_HEIGHT - 30);
        mainVB.setStyle("-fx-padding: 20;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;");

        populateVBox(mainVB);

        final ScrollPane scrollPane = new ScrollPane(mainVB);
        scrollPane.setMinHeight(mainVB.getMinHeight());

        mainGroup.getChildren().addAll(menuBar, mainVB, scrollPane);
        //mainGroup.getChildren().addAll(menuBar, mainVB);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.setTitle("P2P File Sharing");
    }

    private void createErrorStage(String errorText)
    {
        GridPane errorGrid = new GridPane();
        errorGrid.setAlignment(Pos.CENTER);
        errorGrid.setHgap(10);
        errorGrid.setVgap(10);

        Label text = new Label(errorText);

        Button okBtn = new Button();
        okBtn.setText("Ok");
        HBox okBtnFormat = new HBox();
        okBtnFormat.setAlignment(Pos.CENTER);
        okBtnFormat.getChildren().add(okBtn);

        errorGrid.add(text,0, 0);
        errorGrid.add(okBtnFormat, 0, 1);

        Scene errorScene = new Scene(errorGrid, 300, 100);

        Stage errorStage = new Stage();
        errorStage.setScene(errorScene);
        errorStage.setTitle("Error");

        errorStage.show();

        okBtn.setOnAction((ActionEvent e) -> {

            errorStage.close();
        });

    }

    private void populateVBox(VBox vb)
    {
        addUser(vb, username);

        // Add code to add files and other users
        addFile(vb, username, "normal", normalFile);

        addUser(vb, "Test User");

        // Add code to add files and other users
        addFile(vb, "Test User", "downloading", downloadingFile);
        addFile(vb, "Test User", "normal", normalFile);
        addFile(vb, "Test User", "updating", updatedFile);
        addFile(vb, "Test User", "new1", newFile);
        addFile(vb, "Test User", "new2", newFile);
        addFile(vb, "Test User", "new3", newFile);
        addFile(vb, "Test User", "new4", newFile);
        addFile(vb, "Test User", "new5", newFile);
        addFile(vb, "Test User", "new6", newFile);
        addFile(vb, "Test User", "new7", newFile);
        addFile(vb, "Test User", "new8", newFile);
        addFile(vb, "Test User", "new9", newFile);
        addFile(vb, "Test User", "new10", newFile);
    }

    private Boolean checkUsername()
    {
        Pattern p = Pattern.compile(USERNAME_REGEX);
        Matcher m = p.matcher(username);

        if(username.length() < 3)
        {
            createErrorStage("Username must be longer than 3 characters.");
            System.out.println("Username: " + username + " is invalid.");

            return false;
        }
        else if (!m.find())
        {
            createErrorStage("Usernames may not contain the following characters:\n!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~");
            System.out.println("Username: " + username + " is invalid.");

            return false;
        }

        return true;
    }

    private void addUser(VBox vb, String username)
    {
        HBox hb = new HBox();

        Label user = new Label(username);
        user.setFont(new Font("Arial", 15));
        user.setMinWidth(400);

        Button addBtn = new Button("Add");

        hb.getChildren().addAll(user, addBtn);

        Separator sepTop = new Separator();
        sepTop.setOrientation(Orientation.HORIZONTAL);
        Separator sepBottom = new Separator();
        sepBottom.setOrientation(Orientation.HORIZONTAL);

        vb.getChildren().add(sepBottom);
        vb.getChildren().add(hb);
        vb.getChildren().add(sepTop);
    }

    private void addFile(VBox vb, String owner, String filename, Image fileImg)
    {
        GridPane gp = new GridPane();

        ImageView img = new ImageView(fileImg);
        img.setFitHeight(10);
        img.setFitWidth(10);
        img.setPreserveRatio(true);

        Label icon = new Label("", img);
        Label file = new Label(filename);

        icon.setMinWidth(50);
        file.setMinWidth(275);

        Button updateBtn = new Button("Update");
        Button removeBtn = new Button("Remove");
        Button getBtn = new Button("Get");

        ProgressBar progressBar = new ProgressBar();

        progressBar.setProgress(0.75);

        gp.add(icon, 0, 0);
        gp.add(file, 1, 0);

        if(owner == username)
        {
            gp.add(updateBtn, 2, 0);
            gp.add(removeBtn, 3, 0);
        }
        else
        {
            if(fileImg == downloadingFile)
            {
                gp.add(progressBar, 3, 0);
            }
            else if(fileImg != normalFile)
            {
                gp.add(getBtn, 3, 0);
            }
        }

        vb.getChildren().add(gp);
    }
}

