public interface GUIFileManager_IF
{
    public void fileOpSuccess();
    public void fileNotFound();
    public void IOexception();
    public void downloadPercentComplete(String username, String filename, double percent);
    public void uploadPercentComplete(String username, String filename, double percent);
    public void addNewFile(String user, String filename);
    public void removeFile(String user, String filename);
    public void updateFile(String user, String filename);
}