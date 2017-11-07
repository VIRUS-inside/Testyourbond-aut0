package org.openqa.selenium.remote;

import java.io.File;
import java.util.logging.Logger;


















public class LocalFileDetector
  implements FileDetector
{
  public LocalFileDetector() {}
  
  private static final Logger log = Logger.getLogger(LocalFileDetector.class.getName());
  
  public File getLocalFile(CharSequence... keys) {
    StringBuilder builder = new StringBuilder();
    for (CharSequence chars : keys) {
      builder.append(chars);
    }
    
    String filepath = builder.toString();
    

    if (filepath.isEmpty()) {
      return null;
    }
    
    File file = new File(filepath);
    

    File parentDir = file.getParentFile();
    if (parentDir == null) {
      parentDir = new File(".");
    }
    File toUpload = new File(parentDir, file.getName());
    
    log.fine("Detected local file: " + toUpload.exists());
    
    return (toUpload.exists()) && (toUpload.isFile()) ? toUpload : null;
  }
}
