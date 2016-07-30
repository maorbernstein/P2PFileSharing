public interface GUIFileManager_IF
{
    public void fileOpSuccess();
    public void fileNotFound();
    public void IOexception();
    public void percentComplete(double percent);
    public String getDownloadDirectory();
    public void addNewFile(String[][] files);
    public void removeFile(String[][] files);
    public void updateFile(String[][] files);
}