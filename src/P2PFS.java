import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;

/*****************************************************************************************
 * <p>
 * Class Name:     CLASS_NAME
 * <p>
 * Purpose:        PURPOSE
 * <p>
 * Create By:      David Wei
 * Date:           7/18/2016
 * Last Modified:  Initial Revision
 * IDE Used:       Intellij 2016.1.3
 * <p>
 ****************************************************************************************/
public class P2PFS extends Application
{
    private P2PFSGui gui;
    private FileManager fm;
    private UserManager um;
    private NetworkCoordinator nm;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        fm = new FileManager();
        um = new UserManager();
        nm = new NetworkCoordinator();

        gui = new P2PFSGui(fm, um, primaryStage);

        fm.linkGui(gui);
        fm.linkNC(nm);

        um.linkGui(gui);
        um.linkNC(nm);
        um.linkFM(fm);

        nm.linkGui(gui);
        nm.linkFM(fm);
        nm.linkUM(um);

        gui.start(primaryStage);

    }

}
