package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import java.net.URL;
import java.util.Map;
































public class HtmlMeta
  extends HtmlElement
{
  public static final String TAG_NAME = "meta";
  
  HtmlMeta(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    
    if ("set-cookie".equalsIgnoreCase(getHttpEquivAttribute())) {
      performSetCookie();
    }
  }
  



  protected void performSetCookie()
  {
    SgmlPage page = getPage();
    WebClient client = page.getWebClient();
    URL url = page.getUrl();
    client.addCookie(getContentAttribute(), url, this);
  }
  



  public boolean mayBeDisplayed()
  {
    return false;
  }
  







  public final String getHttpEquivAttribute()
  {
    return getAttribute("http-equiv");
  }
  







  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  







  public final String getContentAttribute()
  {
    return getAttribute("content");
  }
  







  public final String getSchemeAttribute()
  {
    return getAttribute("scheme");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.NONE;
  }
}
