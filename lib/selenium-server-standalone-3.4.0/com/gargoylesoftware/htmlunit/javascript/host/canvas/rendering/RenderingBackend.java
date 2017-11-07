package com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering;

import java.io.IOException;
import javax.imageio.ImageReader;

public abstract interface RenderingBackend
{
  public abstract void setFillStyle(String paramString);
  
  public abstract void clearRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void fillRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void strokeRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void drawImage(ImageReader paramImageReader, int paramInt1, int paramInt2)
    throws IOException;
  
  public abstract byte[] getBytes(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract String encodeToString(String paramString)
    throws IOException;
}
