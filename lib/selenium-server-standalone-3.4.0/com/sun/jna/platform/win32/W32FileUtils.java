package com.sun.jna.platform.win32;

import com.sun.jna.WString;
import com.sun.jna.platform.FileUtils;
import java.io.File;
import java.io.IOException;










public class W32FileUtils
  extends FileUtils
{
  public W32FileUtils() {}
  
  public boolean hasTrash()
  {
    return true;
  }
  
  public void moveToTrash(File[] files) throws IOException {
    Shell32 shell = Shell32.INSTANCE;
    ShellAPI.SHFILEOPSTRUCT fileop = new ShellAPI.SHFILEOPSTRUCT();
    wFunc = 3;
    String[] paths = new String[files.length];
    for (int i = 0; i < paths.length; i++) {
      paths[i] = files[i].getAbsolutePath();
    }
    pFrom = new WString(fileop.encodePaths(paths));
    fFlags = 1620;
    int ret = shell.SHFileOperation(fileop);
    if (ret != 0) {
      throw new IOException("Move to trash failed: " + pFrom + ": " + Kernel32Util.formatMessageFromLastErrorCode(ret));
    }
    
    if (fAnyOperationsAborted) {
      throw new IOException("Move to trash aborted");
    }
  }
}
