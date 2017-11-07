package com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering;

import java.io.IOException;
import javax.imageio.ImageReader;




















































public class GaeRenderingBackend
  implements RenderingBackend
{
  public GaeRenderingBackend(int imageWidth, int imageHeight) {}
  
  public void setFillStyle(String fillStyle) {}
  
  public void clearRect(int x, int y, int w, int h) {}
  
  public void fillRect(int x, int y, int w, int h) {}
  
  public void strokeRect(int x, int y, int w, int h) {}
  
  public void drawImage(ImageReader imageReader, int dxI, int dyI)
    throws IOException
  {}
  
  public byte[] getBytes(int width, int height, int sx, int sy)
  {
    return new byte[width * height * 4];
  }
  


  public String encodeToString(String type)
    throws IOException
  {
    return "";
  }
}
