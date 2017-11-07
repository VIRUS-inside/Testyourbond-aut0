package com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;





















public class AwtRenderingBackend
  implements RenderingBackend
{
  private static final Log LOG = LogFactory.getLog(AwtRenderingBackend.class);
  

  private final BufferedImage image_;
  

  private final Graphics2D graphics2D_;
  

  public AwtRenderingBackend(int imageWidth, int imageHeight)
  {
    image_ = new BufferedImage(imageWidth, imageHeight, 2);
    graphics2D_ = image_.createGraphics();
    graphics2D_.setBackground(new Color(0.0F, 0.0F, 0.0F, 0.0F));
    graphics2D_.setColor(Color.black);
    graphics2D_.clearRect(0, 0, imageWidth, imageHeight);
  }
  



  public void setFillStyle(String fillStyle)
  {
    String tmpFillStyle = fillStyle.replaceAll("\\s", "");
    Color color = null;
    if (tmpFillStyle.startsWith("rgb(")) {
      String[] colors = tmpFillStyle.substring(4, tmpFillStyle.length() - 1).split(",");
      color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
    }
    else if (tmpFillStyle.startsWith("rgba(")) {
      String[] colors = tmpFillStyle.substring(5, tmpFillStyle.length() - 1).split(",");
      color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]), 
        (int)(Float.parseFloat(colors[3]) * 255.0F));
    }
    else if (tmpFillStyle.startsWith("#")) {
      color = Color.decode(tmpFillStyle);
    }
    else {
      try {
        Field f = Color.class.getField(tmpFillStyle);
        color = (Color)f.get(null);
      }
      catch (Exception e) {
        LOG.info("Can not find color '" + tmpFillStyle + '\'');
        color = Color.black;
      }
    }
    graphics2D_.setColor(color);
  }
  



  public void clearRect(int x, int y, int w, int h)
  {
    graphics2D_.clearRect(x, y, w, h);
  }
  



  public void fillRect(int x, int y, int w, int h)
  {
    graphics2D_.fillRect(x, y, w, h);
  }
  



  public void strokeRect(int x, int y, int w, int h)
  {
    graphics2D_.drawRect(x, y, w, h);
  }
  


  public void drawImage(ImageReader imageReader, int dxI, int dyI)
    throws IOException
  {
    if (imageReader.getNumImages(true) != 0) {
      BufferedImage img = imageReader.read(0);
      graphics2D_.drawImage(img, dxI, dyI, image_.getWidth(), image_.getHeight(), null);
    }
  }
  



  public byte[] getBytes(int width, int height, int sx, int sy)
  {
    byte[] array = new byte[width * height * 4];
    int index = 0;
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        Color c = new Color(image_.getRGB(sx + x, sy + y), true);
        array[(index++)] = ((byte)c.getRed());
        array[(index++)] = ((byte)c.getGreen());
        array[(index++)] = ((byte)c.getBlue());
        array[(index++)] = ((byte)c.getAlpha());
      }
    }
    return array;
  }
  


  public String encodeToString(String type)
    throws IOException
  {
    String imageType = type;
    if ((imageType != null) && (imageType.startsWith("image/"))) {
      imageType = imageType.substring(6);
    }
    Object localObject1 = null;Object localObject4 = null; Object localObject3; try { ByteArrayOutputStream bos = new ByteArrayOutputStream();
      try { ImageIO.write(image_, imageType, bos);
        
        byte[] imageBytes = bos.toByteArray();
        return new String(new Base64().encode(imageBytes));
      } finally { if (bos != null) bos.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) localObject3.addSuppressed(localThrowable);
    }
  }
}
