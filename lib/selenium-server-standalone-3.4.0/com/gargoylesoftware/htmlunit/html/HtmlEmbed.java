package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
































public class HtmlEmbed
  extends HtmlElement
{
  public static final String TAG_NAME = "embed";
  
  HtmlEmbed(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public void saveAs(File file)
    throws IOException
  {
    HtmlPage page = (HtmlPage)getPage();
    WebClient webclient = page.getWebClient();
    
    URL url = page.getFullyQualifiedUrl(getAttribute("src"));
    WebRequest request = new WebRequest(url);
    request.setAdditionalHeader("Referer", page.getUrl().toExternalForm());
    WebResponse webResponse = webclient.loadWebResponse(request);
    
    Object localObject1 = null;Object localObject4 = null; Object localObject3; label141: try { fos = new FileOutputStream(file);
    } finally { FileOutputStream fos;
      localObject3 = localThrowable; break label141; if (localObject3 != localThrowable) { localObject3.addSuppressed(localThrowable);
      }
    }
  }
  

  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
}
