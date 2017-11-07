package com.gargoylesoftware.htmlunit.svg;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.ScriptElement;
import com.gargoylesoftware.htmlunit.html.ScriptElementSupport;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import java.nio.charset.Charset;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;





























public class SvgScript
  extends SvgElement
  implements ScriptElement
{
  public static final String TAG_NAME = "script";
  private boolean executed_;
  
  SvgScript(String namespaceURI, String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(namespaceURI, qualifiedName, page, attributes);
  }
  



  public boolean isExecuted()
  {
    return executed_;
  }
  



  public void setExecuted(boolean executed)
  {
    executed_ = executed;
  }
  



  public final String getSrcAttribute()
  {
    return getSrcAttributeNormalized();
  }
  








  protected final String getSrcAttributeNormalized()
  {
    String attrib = getAttribute("src");
    if (ATTRIBUTE_NOT_DEFINED == attrib) {
      return attrib;
    }
    
    return StringUtils.replaceChars(attrib, "\r\n", "");
  }
  



  public final String getCharsetAttribute()
  {
    return getAttribute("charset");
  }
  



  public final Charset getCharset()
  {
    return EncodingSniffer.toCharset(getCharsetAttribute());
  }
  




  protected void onAllChildrenAddedToPage(boolean postponed)
  {
    ScriptElementSupport.onAllChildrenAddedToPage(this, postponed);
  }
}
