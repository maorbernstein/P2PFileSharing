import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/*****************************************************************************************
 *  <p>
 *  Class Name:     P2PFSGui_notification
 *  <p>
 *  Purpose:        General purpose notification generator
 *  <p>
 *  Create By:      David Wei
 *  Date:           8/4/2016
 *  Last Modified:  Initial Revision
 *  IDE Used:       Intellij 2016.1.3
 *  <p>
 ****************************************************************************************/
class P2PFSGui_notification
{
    private static final Image notifyImg = new Image("/resources/notify.png");
    private static final Image errorImg = new Image("/resources/error.png");

    private static Stage loadingStage;

    // General purpose notification
    static void createPopup(String notificationText, String title, Image notifyImg)
    {
        // Setup GridPane for Notification PopUp
        GridPane notifyGrid = new GridPane();
        notifyGrid.setAlignment(Pos.CENTER);
        notifyGrid.setHgap(10);
        notifyGrid.setVgap(10);

        // Create text for notification
        Label text = new Label(notificationText);

        ImageView img = new ImageView(notifyImg);
        img.setFitHeight(30);
        img.setFitWidth(30);
        img.setPreserveRatio(true);
        Label icon = new Label("", img);

        HBox iconFormat = new HBox();
        iconFormat.setAlignment(Pos.CENTER);
        iconFormat.getChildren().add(icon);

        // Add Ok button
        Button okBtn = new Button();
        okBtn.setText("Ok");
        HBox okBtnFormat = new HBox();
        okBtnFormat.setAlignment(Pos.CENTER);
        okBtnFormat.getChildren().add(okBtn);

        // Add elements to GridPane
        notifyGrid.add(iconFormat, 0, 0);
        notifyGrid.add(text, 0, 1);
        notifyGrid.add(okBtnFormat, 0, 2);

        // Create scene

        Scene notifyScene = new Scene(notifyGrid, P2PFSGui_elements.POPUP_WINDOW_WIDTH, P2PFSGui_elements.POPUP_WINDOW_HEIGHT);

        // Create stage
        Stage notifyStage = new Stage();
        notifyStage.setScene(notifyScene);
        notifyStage.setTitle(title);

        // Show Popup
        notifyStage.show();

        // Set action on clicking OK button to close
        okBtn.setOnAction((ActionEvent e) -> {
            notifyStage.close();
        });

    }

    // Create Error Notifications
    static void createError(String errorText)
    {
        createPopup(errorText, "Error", errorImg);
    }

    static void createNotification(String text, String title)
    {
        createPopup(text, title, notifyImg);
    }

    static void createLoadingPopup()
    {
        // Setup GridPane for Notification PopUp
        GridPane loadingGrid = new GridPane();
        loadingGrid.setAlignment(Pos.CENTER);
        loadingGrid.setHgap(10);
        loadingGrid.setVgap(10);

        // Create text for notification
        Label text = new Label("Connecting to Network");

        ProgressBar pBar = new ProgressBar();
        HBox pBarFormat = new HBox();
        pBarFormat.setAlignment(Pos.CENTER);
        pBarFormat.getChildren().add(pBar);

        // Add elements to GridPane
        loadingGrid.add(text, 0, 0);
        loadingGrid.add(pBarFormat, 0, 1);

        // Create scene
        Scene loadingScene = new Scene(loadingGrid, P2PFSGui_elements.POPUP_WINDOW_WIDTH, P2PFSGui_elements.POPUP_WINDOW_HEIGHT);

        // Create stage
        loadingStage = new Stage();
        loadingStage.setScene(loadingScene);
        loadingStage.setTitle("Connecting to Network");

        // Show Popup
        loadingStage.show();

    }

    public static void destroyLoadingPopup()
    {
        loadingStage.close();
    }


}
