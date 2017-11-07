package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.AwtRenderingBackend;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.GaeRenderingBackend;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.RenderingBackend;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement;
import java.io.IOException;
import javax.imageio.ImageReader;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
































@JsxClass
public class CanvasRenderingContext2D
  extends SimpleScriptable
{
  private static final int PIXELS_PER_CHAR = 10;
  private final HTMLCanvasElement canvas_;
  private RenderingBackend renderingBackend_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CanvasRenderingContext2D()
  {
    canvas_ = null;
    renderingBackend_ = null;
  }
  



  public CanvasRenderingContext2D(HTMLCanvasElement canvas)
  {
    canvas_ = canvas;
    renderingBackend_ = null;
  }
  
  private RenderingBackend getRenderingBackend() {
    if (renderingBackend_ == null) {
      int imageWidth = Math.max(1, canvas_.getWidth());
      int imageHeight = Math.max(1, canvas_.getHeight());
      if (GAEUtils.isGaeMode()) {
        renderingBackend_ = new GaeRenderingBackend(imageWidth, imageHeight);
      }
      else {
        renderingBackend_ = new AwtRenderingBackend(imageWidth, imageHeight);
      }
    }
    return renderingBackend_;
  }
  



  @JsxGetter
  public Object getFillStyle()
  {
    return null;
  }
  



  @JsxSetter
  public void setFillStyle(String fillStyle)
  {
    getRenderingBackend().setFillStyle(fillStyle);
  }
  



  @JsxGetter
  public Object getStrokeStyle()
  {
    return null;
  }
  





  @JsxSetter
  public void setStrokeStyle(Object strokeStyle) {}
  




  @JsxGetter
  public double getLineWidth()
  {
    return 0.0D;
  }
  





  @JsxSetter
  public void setLineWidth(Object lineWidth) {}
  




  @JsxGetter
  public double getGlobalAlpha()
  {
    return 0.0D;
  }
  









  @JsxSetter
  public void setGlobalAlpha(Object globalAlpha) {}
  









  @JsxFunction
  public void arc(double x, double y, double radius, double startAngle, double endAngle, boolean anticlockwise) {}
  









  @JsxFunction
  public void arcTo(double x1, double y1, double x2, double y2, double radius) {}
  









  @JsxFunction
  public void beginPath() {}
  








  @JsxFunction
  public void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y) {}
  








  @JsxFunction
  public void clearRect(int x, int y, int w, int h)
  {
    getRenderingBackend().clearRect(x, y, w, h);
  }
  








  @JsxFunction
  public void clip() {}
  








  @JsxFunction
  public void closePath() {}
  








  @JsxFunction
  public void createImageData() {}
  








  @JsxFunction
  public void createLinearGradient(double x0, double y0, double r0, double x1, Object y1, Object r1) {}
  








  @JsxFunction
  public void createPattern() {}
  








  @JsxFunction
  public void createRadialGradient() {}
  







  @JsxFunction
  public void drawImage(Object image, int sx, int sy, Object sWidth, Object sHeight, Object dx, Object dy, Object dWidth, Object dHeight)
  {
    Integer dWidthI = null;
    Integer dHeightI = null;
    Integer sWidthI = null;
    Integer sHeightI = null;
    Integer dxI; Integer dyI; if (dx != Undefined.instance) {
      Integer dxI = Integer.valueOf(((Number)dx).intValue());
      Integer dyI = Integer.valueOf(((Number)dy).intValue());
      dWidthI = Integer.valueOf(((Number)dWidth).intValue());
      dHeightI = Integer.valueOf(((Number)dHeight).intValue());
    }
    else {
      dxI = Integer.valueOf(sx);
      dyI = Integer.valueOf(sy);
    }
    if (sWidth != Undefined.instance) {
      sWidthI = Integer.valueOf(((Number)sWidth).intValue());
      sHeightI = Integer.valueOf(((Number)sHeight).intValue());
    }
    try
    {
      if ((image instanceof HTMLImageElement)) {
        ImageReader imageReader = 
          ((HtmlImage)((HTMLImageElement)image).getDomNodeOrDie()).getImageReader();
        getRenderingBackend().drawImage(imageReader, dxI.intValue(), dyI.intValue());
      }
    }
    catch (IOException ioe) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CANVAS_DRAW_THROWS_FOR_MISSING_IMG)) {
        throw Context.throwAsScriptRuntimeEx(ioe);
      }
    }
  }
  




  public String toDataURL(String type)
  {
    try
    {
      if (type == null) {
        type = "image/png";
      }
      return "data:" + type + ";base64," + getRenderingBackend().encodeToString(type);
    }
    catch (IOException ioe) {
      throw Context.throwAsScriptRuntimeEx(ioe);
    }
  }
  









  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void ellipse(double x, double y, double radiusX, double radiusY, double rotation, double startAngle, double endAngle, boolean anticlockwise) {}
  









  @JsxFunction
  public void fill() {}
  









  @JsxFunction
  public void fillRect(int x, int y, int w, int h)
  {
    getRenderingBackend().fillRect(x, y, w, h);
  }
  






  @JsxFunction
  public void fillText() {}
  






  @JsxFunction
  public ImageData getImageData(int sx, int sy, int sw, int sh)
  {
    ImageData imageData = new ImageData(getRenderingBackend(), sx, sy, sw, sh);
    imageData.setParentScope(getParentScope());
    imageData.setPrototype(getPrototype(imageData.getClass()));
    return imageData;
  }
  






  @JsxFunction
  public void getLineDash() {}
  





  @JsxFunction
  public void getLineData() {}
  





  @JsxFunction
  public void isPointInPath() {}
  





  @JsxFunction
  public void lineTo(double x, double y) {}
  





  @JsxFunction
  public TextMetrics measureText(Object text)
  {
    if ((text == null) || (Undefined.instance == text)) {
      throw Context.throwAsScriptRuntimeEx(
        new RuntimeException("Missing argument for CanvasRenderingContext2D.measureText()."));
    }
    
    String textValue = Context.toString(text);
    

    int width = textValue.length() * 10;
    
    TextMetrics metrics = new TextMetrics(width);
    metrics.setParentScope(getParentScope());
    metrics.setPrototype(getPrototype(metrics.getClass()));
    return metrics;
  }
  







  @JsxFunction
  public void moveTo(double x, double y) {}
  







  @JsxFunction
  public void putImageData() {}
  






  @JsxFunction
  public void quadraticCurveTo(double controlPointX, double controlPointY, double endPointX, double endPointY) {}
  






  @JsxFunction
  public void rect(double x, double y, double w, double h) {}
  






  @JsxFunction
  public void restore() {}
  






  @JsxFunction
  public void rotate() {}
  






  @JsxFunction
  public void save() {}
  






  @JsxFunction
  public void scale(Object x, Object y) {}
  






  @JsxFunction
  public void setLineDash() {}
  






  @JsxFunction
  public void setTransform() {}
  






  @JsxFunction
  public void stroke() {}
  






  @JsxFunction
  public void strokeRect(int x, int y, int w, int h)
  {
    getRenderingBackend().strokeRect(x, y, w, h);
  }
  





  @JsxFunction
  public void strokeText() {}
  





  @JsxFunction
  public void transform() {}
  





  @JsxFunction
  public void translate(Object x, Object y) {}
  





  @JsxGetter
  public HTMLCanvasElement getCanvas()
  {
    return canvas_;
  }
}
