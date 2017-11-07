package org.openqa.selenium.remote;

import java.io.File;
















public class UselessFileDetector
  implements FileDetector
{
  public UselessFileDetector() {}
  
  public File getLocalFile(CharSequence... keys)
  {
    return null;
  }
}
