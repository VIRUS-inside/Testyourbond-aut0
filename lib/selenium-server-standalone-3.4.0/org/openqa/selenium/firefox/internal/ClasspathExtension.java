package org.openqa.selenium.firefox.internal;

import com.google.common.io.Closeables;
import com.google.common.io.Resources;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.io.FileHandler;

















public class ClasspathExtension
  implements Extension
{
  private final Class<?> loadResourcesUsing;
  private final String loadFrom;
  
  public ClasspathExtension(Class<?> loadResourcesUsing, String loadFrom)
  {
    this.loadResourcesUsing = loadResourcesUsing;
    this.loadFrom = loadFrom;
  }
  
  public void writeTo(File extensionsDir) throws IOException {
    if (!FileHandler.isZipped(loadFrom)) {
      throw new WebDriverException("Will only install zipped extensions for now");
    }
    
    File holdingPen = new File(extensionsDir, "webdriver-staging");
    FileHandler.createDir(holdingPen);
    
    File extractedXpi = new File(holdingPen, loadFrom);
    File parentDir = extractedXpi.getParentFile();
    if (!parentDir.exists()) {
      parentDir.mkdirs();
    }
    
    URL resourceUrl = Resources.getResource(loadResourcesUsing, loadFrom);
    OutputStream stream = null;
    try
    {
      stream = new FileOutputStream(extractedXpi);
      Resources.copy(resourceUrl, stream);
    } finally {
      Closeables.close(stream, false);
    }
    new FileExtension(extractedXpi).writeTo(extensionsDir);
  }
}
