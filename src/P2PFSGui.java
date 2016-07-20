import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;


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

public class P2PFSGui
{
    private Frame guiFrame;
    private Panel guiPanel;

    final int GUI_FRAME_HEIGHT = 400;
    final int GUI_FRAME_WIDTH = 400;

    public void createGui()
    {
        guiFrame = new Frame();

        // Set size of the Frame
        guiFrame.setSize(GUI_FRAME_WIDTH, GUI_FRAME_HEIGHT);

        // Set frame buffers
        //guiFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        // Add closing functionality
        guiFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        // Add Menu Bar
        createMenu();
        // Create table
        createTable();

        guiFrame.setVisible(true);

        guiPanel = new Panel();

        // Add created panel
        guiFrame.add(guiPanel);
    }

    private void createMenu()
    {
        // Create new Menu Bar
        MenuBar guiMenu = new MenuBar();

        // Create Groups Menu
        Menu groupsMenu = new Menu("Groups");

        // Create Group Menu Items
        MenuItem joinGroup = new MenuItem("Join Group");
        MenuItem inviteUser = new MenuItem("Invite User");

        // Add listners to each menu Item
        MenuItemListener mListener = new MenuItemListener();

        joinGroup.addActionListener(mListener);
        inviteUser.addActionListener(mListener);

        // Add items to Group Menu
        groupsMenu.add(joinGroup);
        groupsMenu.add(inviteUser);

        // Add Group Menu to Menu Bar
        guiMenu.add(groupsMenu);

        // Add Menu Bar to Frame
        guiFrame.setMenuBar(guiMenu);
        guiFrame.setVisible(true);
    }

    private void createTable()
    {
        // Define column Names
        String[] colNames = {"Username", "Filename", "button1", "button2"};
        Object[][] tableData =
        {
                {"User 1", "", "", "new"},
                {"", "File1", "Update", "Remove"},
                {"User 2", "", "", ""},
                {"", "File1", "", "", "get"}
        };

        // Create Table
        DefaultTableModel dTable = new DefaultTableModel(tableData, colNames);
        JTable guiTable = new JTable(dTable);

        // Remove table gridlines
        guiTable.setShowGrid(false);

        // Add Border
        Color borderColor = new Color(0);
        MatteBorder tableBorder = new MatteBorder(1, 1, 1, 1, borderColor);
        guiTable.setBorder(tableBorder);


        guiFrame.add(guiTable);
    }

    class MenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog( null,
                    "You pressed: " + e.getActionCommand() );

        }
    }
}

