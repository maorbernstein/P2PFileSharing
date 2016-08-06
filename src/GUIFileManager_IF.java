public interface GUIFileManager_IF
{
    public void fileOpSuccess();
    public void fileNotFound();
    public void IOexception();
    public void downloadPercentComplete(String username, String filename, double percent);
    public void uploadPercentComplete(String username, String filename, double percent);
    public void addNewFile(String[][] files);
    public void removeFile(String[][] files);
    public void updateFile(String[][] files);
}