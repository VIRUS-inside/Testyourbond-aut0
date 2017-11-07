package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;


































public class HtmlArea
  extends HtmlElement
{
  public static final String TAG_NAME = "area";
  
  HtmlArea(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  


  protected boolean doClickStateUpdate(boolean shiftKey, boolean ctrlKey)
    throws IOException
  {
    HtmlPage enclosingPage = (HtmlPage)getPage();
    WebClient webClient = enclosingPage.getWebClient();
    
    String href = getHrefAttribute().trim();
    if (!href.isEmpty()) {
      HtmlPage page = (HtmlPage)getPage();
      if (StringUtils.startsWithIgnoreCase(href, "javascript:")) {
        page.executeJavaScriptIfPossible(
          href, "javascript url", getStartLineNumber());
        return false;
      }
      try
      {
        url = enclosingPage.getFullyQualifiedUrl(getHrefAttribute());
      } catch (MalformedURLException e) {
        URL url;
        throw new IllegalStateException(
          "Not a valid url: " + getHrefAttribute()); }
      URL url;
      WebRequest request = new WebRequest(url);
      request.setAdditionalHeader("Referer", page.getUrl().toExternalForm());
      WebWindow webWindow = enclosingPage.getEnclosingWindow();
      webClient.getPage(
        webWindow, 
        enclosingPage.getResolvedTarget(getTargetAttribute()), 
        request);
    }
    return false;
  }
  






  public final String getShapeAttribute()
  {
    return getAttribute("shape");
  }
  






  public final String getCoordsAttribute()
  {
    return getAttribute("coords");
  }
  






  public final String getHrefAttribute()
  {
    return getAttribute("href");
  }
  






  public final String getNoHrefAttribute()
  {
    return getAttribute("nohref");
  }
  






  public final String getAltAttribute()
  {
    return getAttribute("alt");
  }
  






  public final String getTabIndexAttribute()
  {
    return getAttribute("tabindex");
  }
  






  public final String getAccessKeyAttribute()
  {
    return getAttribute("accesskey");
  }
  






  public final String getOnFocusAttribute()
  {
    return getAttribute("onfocus");
  }
  






  public final String getOnBlurAttribute()
  {
    return getAttribute("onblur");
  }
  






  public final String getTargetAttribute()
  {
    return getAttribute("target");
  }
  





  boolean containsPoint(int x, int y)
  {
    String shape = ((String)StringUtils.defaultIfEmpty(getShapeAttribute(), "rect")).toLowerCase(Locale.ROOT);
    
    if (("default".equals(shape)) && (getCoordsAttribute() != null)) {
      return true;
    }
    
    if (("rect".equals(shape)) && (getCoordsAttribute() != null)) {
      Rectangle2D rectangle = parseRect();
      return rectangle.contains(x, y);
    }
    
    if (("circle".equals(shape)) && (getCoordsAttribute() != null)) {
      Ellipse2D ellipse = parseCircle();
      return ellipse.contains(x, y);
    }
    
    if (("poly".equals(shape)) && (getCoordsAttribute() != null)) {
      GeneralPath path = parsePoly();
      return path.contains(x, y);
    }
    
    return false;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DISPLAY_BLOCK)) {
      return HtmlElement.DisplayStyle.NONE;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
  



  public boolean isDisplayed()
  {
    DomNode parent = getParentNode();
    if ((parent != null) && ((parent instanceof HtmlMap)) && (parent.isDisplayed())) {
      return !isEmpty();
    }
    return false;
  }
  
  private Rectangle2D parseRect() {
    String[] coords = StringUtils.split(getCoordsAttribute(), ',');
    
    double leftX = 0.0D;
    if (coords.length > 0) {
      leftX = Double.parseDouble(coords[0].trim());
    }
    double topY = 0.0D;
    if (coords.length > 1) {
      topY = Double.parseDouble(coords[1].trim());
    }
    double rightX = 0.0D;
    if (coords.length > 2) {
      rightX = Double.parseDouble(coords[2].trim());
    }
    double bottomY = 0.0D;
    if (coords.length > 3) {
      bottomY = Double.parseDouble(coords[3].trim());
    }
    Rectangle2D rectangle = new Rectangle2D.Double(leftX, topY, 
      rightX - leftX, bottomY - topY);
    return rectangle;
  }
  
  private Ellipse2D parseCircle() {
    String[] coords = StringUtils.split(getCoordsAttribute(), ',');
    String radiusString = coords[2].trim();
    
    try
    {
      radius = Integer.parseInt(radiusString);
    } catch (NumberFormatException nfe) {
      int radius;
      throw new NumberFormatException("Circle radius of " + radiusString + " is not yet implemented.");
    }
    int radius;
    double centerX = Double.parseDouble(coords[0].trim());
    double centerY = Double.parseDouble(coords[1].trim());
    Ellipse2D ellipse = new Ellipse2D.Double(centerX - radius / 2.0D, centerY - radius / 2.0D, 
      radius, radius);
    return ellipse;
  }
  
  private GeneralPath parsePoly() {
    String[] coords = StringUtils.split(getCoordsAttribute(), ',');
    GeneralPath path = new GeneralPath();
    for (int i = 0; i + 1 < coords.length; i += 2) {
      if (i == 0) {
        path.moveTo(Float.parseFloat(coords[i]), Float.parseFloat(coords[(i + 1)]));
      }
      else {
        path.lineTo(Float.parseFloat(coords[i]), Float.parseFloat(coords[(i + 1)]));
      }
    }
    path.closePath();
    return path;
  }
  
  private boolean isEmpty() {
    String shape = ((String)StringUtils.defaultIfEmpty(getShapeAttribute(), "rect")).toLowerCase(Locale.ROOT);
    
    if (("default".equals(shape)) && (getCoordsAttribute() != null)) {
      return false;
    }
    
    if (("rect".equals(shape)) && (getCoordsAttribute() != null)) {
      Rectangle2D rectangle = parseRect();
      return rectangle.isEmpty();
    }
    
    if (("circle".equals(shape)) && (getCoordsAttribute() != null)) {
      Ellipse2D ellipse = parseCircle();
      return ellipse.isEmpty();
    }
    
    if (("poly".equals(shape)) && (getCoordsAttribute() != null)) {
      return false;
    }
    
    return false;
  }
  



  public boolean handles(Event event)
  {
    if (("blur".equals(event.getType())) || ("focus".equals(event.getType()))) {
      return true;
    }
    return super.handles(event);
  }
}
