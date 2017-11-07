package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;



































public class HtmlLink
  extends HtmlElement
{
  public static final String TAG_NAME = "link";
  private WebResponse cachedWebResponse_;
  
  HtmlLink(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getCharsetAttribute()
  {
    return getAttribute("charset");
  }
  







  public final String getHrefAttribute()
  {
    return getAttribute("href");
  }
  







  public final String getHrefLangAttribute()
  {
    return getAttribute("hreflang");
  }
  







  public final String getTypeAttribute()
  {
    return getAttribute("type");
  }
  







  public final String getRelAttribute()
  {
    return getAttribute("rel");
  }
  







  public final String getRevAttribute()
  {
    return getAttribute("rev");
  }
  







  public final String getMediaAttribute()
  {
    return getAttribute("media");
  }
  







  public final String getTargetAttribute()
  {
    return getAttribute("target");
  }
  








  public WebResponse getWebResponse(boolean downloadIfNeeded)
    throws IOException
  {
    return getWebResponse(downloadIfNeeded, null);
  }
  










  public WebResponse getWebResponse(boolean downloadIfNeeded, WebRequest request)
    throws IOException
  {
    if ((downloadIfNeeded) && (cachedWebResponse_ == null)) {
      WebClient webclient = getPage().getWebClient();
      if (request == null) {
        cachedWebResponse_ = webclient.loadWebResponse(getWebRequest());
      }
      else {
        cachedWebResponse_ = webclient.loadWebResponse(request);
      }
    }
    return cachedWebResponse_;
  }
  



  public WebRequest getWebRequest()
    throws MalformedURLException
  {
    HtmlPage page = (HtmlPage)getPage();
    URL url = page.getFullyQualifiedUrl(getHrefAttribute());
    
    WebRequest request = new WebRequest(url);
    
    request.setAdditionalHeader("Referer", page.getUrl().toExternalForm());
    
    String accept = page.getWebClient().getBrowserVersion().getCssAcceptHeader();
    request.setAdditionalHeader("Accept", accept);
    
    return request;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DISPLAY_BLOCK2)) {
      return HtmlElement.DisplayStyle.NONE;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
  



  public boolean mayBeDisplayed()
  {
    return false;
  }
}
