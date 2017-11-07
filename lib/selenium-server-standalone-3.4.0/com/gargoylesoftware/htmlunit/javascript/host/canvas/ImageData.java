package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8ClampedArray;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.RenderingBackend;



























@JsxClass
public class ImageData
  extends SimpleScriptable
{
  private final RenderingBackend renderingContext_;
  private final int sx_;
  private final int sy_;
  private final int width_;
  private final int height_;
  private Uint8ClampedArray data_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public ImageData()
  {
    this(null, 0, 0, 0, 0);
  }
  
  ImageData(RenderingBackend context, int x, int y, int width, int height) {
    renderingContext_ = context;
    sx_ = x;
    sy_ = y;
    width_ = width;
    height_ = height;
  }
  



  @JsxGetter
  public int getWidth()
  {
    return width_;
  }
  



  @JsxGetter
  public int getHeight()
  {
    return height_;
  }
  




  @JsxGetter
  public Uint8ClampedArray getData()
  {
    if (data_ == null) {
      byte[] bytes = renderingContext_.getBytes(width_, height_, sx_, sy_);
      ArrayBuffer arrayBuffer = new ArrayBuffer();
      arrayBuffer.constructor(bytes.length);
      arrayBuffer.setBytes(0, bytes);
      
      data_ = new Uint8ClampedArray();
      data_.setParentScope(getParentScope());
      data_.setPrototype(getPrototype(data_.getClass()));
      
      data_.constructor(arrayBuffer, Integer.valueOf(0), Integer.valueOf(bytes.length));
    }
    
    return data_;
  }
}
