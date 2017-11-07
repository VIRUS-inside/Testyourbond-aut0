package org.openqa.selenium.remote.server;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Base64.Encoder;
import javax.imageio.ImageIO;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;















class SnapshotScreenListener
  extends AbstractWebDriverEventListener
{
  private final Session session;
  
  public SnapshotScreenListener(Session session)
  {
    this.session = session;
  }
  
  public void onException(Throwable throwable, WebDriver driver)
  {
    if (Platform.getCurrent().is(Platform.ANDROID))
    {
      return;
    }
    try
    {
      workAroundD3dBugInVista();
      
      Rectangle size = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
      BufferedImage image = new Robot().createScreenCapture(size);
      
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ImageIO.write(image, "png", outputStream);
      
      String encoded = Base64.getEncoder().encodeToString(outputStream.toByteArray());
      
      session.attachScreenshot(encoded);
    }
    catch (Throwable localThrowable) {}
  }
  
  private void workAroundD3dBugInVista()
  {
    if (Platform.getCurrent().is(Platform.WINDOWS)) {
      System.setProperty("sun.java2d.d3d", "false");
    }
  }
}
