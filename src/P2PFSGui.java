import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

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

    private GridPane mainGrid;
    private Scene mainScene;
    private Stage mainStage;
    private TableView mainTableView;

    private String username;

    @Override
    public void start(Stage mainStage)
    {
        createStartUpScene();
    }

    private void createStartUpScene()
    {
        Button joinNetworkBtn = new Button();
        joinNetworkBtn.setText("Join Network");

        Button createNetworkBtn = new Button();
        createNetworkBtn.setText("Create Network");

        Label usernameLabel = new Label("Username: ");
        TextField usernameTextField = new TextField();

        joinNetworkBtn.setOnAction( (ActionEvent e) -> {

            username = usernameTextField.getText();
            System.out.println("Joining Network");

            FileChooser invFileChooser = new FileChooser();
            invFileChooser.setTitle("Select Invitation File");
            File file = invFileChooser.showOpenDialog(startUpStage);

            System.out.println("Username: " + username);
            System.out.println("Opening file: " + file.getName());

            createMainStage();

            mainStage.show();
            startUpStage.close();
        });

        createNetworkBtn.setOnAction((ActionEvent e) -> {

            username = usernameTextField.getText();
            System.out.println("Creating Network");
            System.out.println("Username: " + username);

            createMainStage();

            mainStage.show();
            startUpStage.close();
        });

        startUpGrid = new GridPane();
        startUpGrid.setAlignment(Pos.CENTER);
        startUpGrid.setHgap(10);
        startUpGrid.setVgap(10);
        //startUpGrid.setPadding(new Insets(25, 25, 25, 25));

        startUpGrid.add(usernameLabel, 0, 0);
        startUpGrid.add(usernameTextField, 1, 0);
        startUpGrid.add(joinNetworkBtn, 0, 1);
        startUpGrid.add(createNetworkBtn, 1, 1);

        startUpScene = new Scene(startUpGrid, 300, 250);

        startUpStage = new Stage();
        startUpStage.setScene(startUpScene);
        startUpStage.initModality(Modality.APPLICATION_MODAL);
        startUpStage.setTitle("P2P File Sharing");

        startUpStage.show();
    }

    private void createMainStage()
    {
        mainGrid = new GridPane();

        Label temp = new Label("Main Grid is now the active scene. Username: " + username);

        mainTableView = new TableView();

        TableColumn col_0 = new TableColumn("1");
        col_0.setMinWidth(100);
        TableColumn col_1 = new TableColumn("2");
        col_1.setMinWidth(100);
        TableColumn col_2 = new TableColumn("3");
        col_2.setMinWidth(100);
        TableColumn col_3 = new TableColumn("4");
        col_3.setMinWidth(100);
        TableColumn col_4 = new TableColumn("5");
        col_4.setMinWidth(100);

        mainTableView.getColumns().addAll(col_0, col_1, col_2, col_3, col_4);



        mainGrid.add(mainTableView,0,0);

        mainScene = new Scene(mainGrid, 500, 500);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.setTitle("P2P File Sharing");
    }
}

