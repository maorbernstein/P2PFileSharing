import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class P2PFSGui_notification
{
    private static P2PFSGui_elements guiElems = new P2PFSGui_elements(new Stage());

    // General purpose notification
    public static void createNotification(String notificationText, String title)
    {
        // Setup GridPane for Notification PopUp
        GridPane notifyGrid = new GridPane();
        notifyGrid.setAlignment(Pos.CENTER);
        notifyGrid.setHgap(10);
        notifyGrid.setVgap(10);

        // Create text for notification
        Label text = new Label(notificationText);

        // Add Ok button
        Button okBtn = new Button();
        okBtn.setText("Ok");
        HBox okBtnFormat = new HBox();
        okBtnFormat.setAlignment(Pos.CENTER);
        okBtnFormat.getChildren().add(okBtn);

        // Add elements to GridPane
        notifyGrid.add(text,0, 0);
        notifyGrid.add(okBtnFormat, 0, 1);

        // Create scene
        Scene notifyScene = new Scene(notifyGrid, guiElems.POPUP_WINDOW_WIDTH, guiElems.POPUP_WINDOW_HEIGHT);

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
    public static void createError(String errorText)
    {
        createNotification(errorText, "Error");
    }
}
