public interface GUINetworkCoordinator_IF
{
    public void connectionStatus(boolean established, boolean usernameOk);
    public void uploadStarted(String user, String filename);
}
