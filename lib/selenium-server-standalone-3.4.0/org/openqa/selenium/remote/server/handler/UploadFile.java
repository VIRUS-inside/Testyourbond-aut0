package org.openqa.selenium.remote.server.handler;

import java.io.File;
import java.util.Map;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.openqa.selenium.io.Zip;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
















public class UploadFile
  extends WebDriverHandler<String>
  implements JsonParametersAware
{
  private String file;
  
  public UploadFile(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    TemporaryFilesystem tempfs = getSession().getTemporaryFileSystem();
    File tempDir = tempfs.createTempDir("upload", "file");
    
    Zip.unzip(file, tempDir);
    
    File[] allFiles = tempDir.listFiles();
    if ((allFiles == null) || (allFiles.length != 1)) {
      throw new WebDriverException("Expected there to be only 1 file. There were: " + allFiles.length);
    }
    

    return allFiles[0].getAbsolutePath();
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    file = ((String)allParameters.get("file"));
  }
}
