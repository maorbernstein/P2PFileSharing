public interface GUIFileManager_IF
{
    public void fileOpSuccess();
    public void fileNotFount();
    public void IOexception();
    public void percentComplete(double percent);
    public String getDownloadDirectory();
}