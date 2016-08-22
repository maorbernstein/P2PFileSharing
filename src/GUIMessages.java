import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

/*****************************************************************************************
 *  <p>
 *  Class Name:     GUIFile
 *  <p>
 *  Purpose:        Class to contain files
 *  <p>
 *  Create By:      David Wei
 *  Date:           8/3/2016
 *  Last Modified:  Initial Revision
 *  IDE Used:       Intellij 2016.1.3
 *  <p>
 ****************************************************************************************/
class GUIMessages
{
    private String message;
    private String username;
    private boolean isMe;

    GUIMessages(String user, String m, Boolean me)
    {
        username = user;
        message = m;
        isMe = me;
    }

    public HBox createHBox()
    {
        HBox hb = new HBox();

        Text msg = new Text(10, 50, message);
        Text name = new Text(10, 50, username + ":  ");

        msg.setWrappingWidth(GUIElements.MAX_WINDOW_WIDTH - 145);

        if (isMe)
        {
            name.setFill(Color.RED);
        }
        else
        {
            name.setFill(Color.BLUE);
        }

        hb.getChildren().add(name);
        hb.getChildren().add(msg);

        return hb;
    }

}
