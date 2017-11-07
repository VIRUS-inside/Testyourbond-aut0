package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.AppletConfirmHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.applets.AppletClassLoader;
import com.gargoylesoftware.htmlunit.html.applets.AppletStubImpl;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

























public class HtmlObject
  extends HtmlElement
{
  private static final Log LOG = LogFactory.getLog(HtmlObject.class);
  

  private static final String APPLET_TYPE = "application/x-java-applet";
  

  private static final String ARCHIVE = "archive";
  
  private static final String CACHE_ARCHIVE = "cache_archive";
  
  private static final String CODEBASE = "codebase";
  
  public static final String TAG_NAME = "object";
  
  private Applet applet_;
  
  private AppletClassLoader appletClassLoader_;
  
  private List<URL> archiveUrls_;
  

  HtmlObject(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getDeclareAttribute()
  {
    return getAttribute("declare");
  }
  







  public final String getClassIdAttribute()
  {
    return getAttribute("classid");
  }
  







  public final String getCodebaseAttribute()
  {
    return getAttribute("codebase");
  }
  







  public final String getDataAttribute()
  {
    return getAttribute("data");
  }
  







  public final String getTypeAttribute()
  {
    return getAttribute("type");
  }
  







  public final String getCodeTypeAttribute()
  {
    return getAttribute("codetype");
  }
  







  public final String getArchiveAttribute()
  {
    return getAttribute("archive");
  }
  







  public final String getStandbyAttribute()
  {
    return getAttribute("standby");
  }
  







  public final String getHeightAttribute()
  {
    return getAttribute("height");
  }
  







  public final String getWidthAttribute()
  {
    return getAttribute("width");
  }
  







  public final String getUseMapAttribute()
  {
    return getAttribute("usemap");
  }
  







  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  







  public final String getTabIndexAttribute()
  {
    return getAttribute("tabindex");
  }
  







  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
  







  public final String getBorderAttribute()
  {
    return getAttribute("border");
  }
  







  public final String getHspaceAttribute()
  {
    return getAttribute("hspace");
  }
  







  public final String getVspaceAttribute()
  {
    return getAttribute("vspace");
  }
  




  protected void onAllChildrenAddedToPage(boolean postponed)
  {
    if ((getOwnerDocument() instanceof XmlPage)) {
      return;
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("Object node added: " + asXml());
    }
    
    String clsId = getClassIdAttribute();
    if (ATTRIBUTE_NOT_DEFINED != clsId) {
      ((HTMLObjectElement)getScriptableObject()).setClassid(clsId);
    }
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
  





  public Applet getApplet()
    throws IOException
  {
    setupAppletIfNeeded();
    return applet_;
  }
  




  private synchronized void setupAppletIfNeeded()
    throws IOException
  {
    if (applet_ != null) {
      return;
    }
    
    if ((StringUtils.isBlank(getTypeAttribute())) || (!getTypeAttribute().startsWith("application/x-java-applet"))) {
      return;
    }
    
    HashMap<String, String> params = new HashMap();
    params.put("name", getNameAttribute());
    
    params.put("height", getHeightAttribute());
    params.put("width", getWidthAttribute());
    
    DomNodeList<HtmlElement> paramTags = getElementsByTagName("param");
    for (HtmlElement paramTag : paramTags) {
      HtmlParameter parameter = (HtmlParameter)paramTag;
      params.put(parameter.getNameAttribute(), parameter.getValueAttribute());
    }
    
    if ((StringUtils.isEmpty((CharSequence)params.get("codebase"))) && (StringUtils.isNotEmpty(getCodebaseAttribute()))) {
      params.put("codebase", getCodebaseAttribute());
    }
    String codebaseProperty = (String)params.get("codebase");
    
    if ((StringUtils.isEmpty((CharSequence)params.get("archive"))) && (StringUtils.isNotEmpty(getArchiveAttribute()))) {
      params.put("archive", getArchiveAttribute());
    }
    
    HtmlPage page = (HtmlPage)getPage();
    WebClient webclient = page.getWebClient();
    
    AppletConfirmHandler handler = webclient.getAppletConfirmHandler();
    if ((handler != null) && (!handler.confirm(this))) {
      return;
    }
    
    String appletClassName = getAttribute("code");
    if (StringUtils.isEmpty(appletClassName)) {
      appletClassName = (String)params.get("code");
    }
    if (appletClassName.endsWith(".class")) {
      appletClassName = appletClassName.substring(0, appletClassName.length() - 6);
    }
    
    appletClassLoader_ = new AppletClassLoader((Window)getPage().getEnclosingWindow().getScriptableObject());
    
    String documentUrl = page.getUrl().toExternalForm();
    String baseUrl = UrlUtils.resolveUrl(documentUrl, ".");
    if (StringUtils.isNotEmpty(codebaseProperty))
    {
      baseUrl = UrlUtils.resolveUrl(baseUrl, codebaseProperty);
    }
    if (!baseUrl.endsWith("/")) {
      baseUrl = baseUrl + "/";
    }
    

    archiveUrls_ = new LinkedList();
    String[] archives = StringUtils.split((String)params.get("archive"), ',');
    if (archives != null) {
      for (int i = 0; i < archives.length; i++) {
        String tmpArchive = archives[i].trim();
        String tempUrl = UrlUtils.resolveUrl(baseUrl, tmpArchive);
        URL archiveUrl = UrlUtils.toUrlUnsafe(tempUrl);
        
        appletClassLoader_.addArchiveToClassPath(archiveUrl);
        archiveUrls_.add(archiveUrl);
      }
    }
    archives = StringUtils.split((String)params.get("cache_archive"), ',');
    if (archives != null) {
      for (int i = 0; i < archives.length; i++) {
        String tmpArchive = archives[i].trim();
        String tempUrl = UrlUtils.resolveUrl(baseUrl, tmpArchive);
        URL archiveUrl = UrlUtils.toUrlUnsafe(tempUrl);
        
        appletClassLoader_.addArchiveToClassPath(archiveUrl);
        archiveUrls_.add(archiveUrl);
      }
    }
    archiveUrls_ = Collections.unmodifiableList(archiveUrls_);
    

    if (archiveUrls_.isEmpty()) {
      String tempUrl = UrlUtils.resolveUrl(baseUrl, getAttribute("code"));
      URL classUrl = UrlUtils.toUrlUnsafe(tempUrl);
      
      WebResponse response = webclient.loadWebResponse(new WebRequest(classUrl));
      try {
        webclient.throwFailingHttpStatusCodeExceptionIfNecessary(response);
        appletClassLoader_.addClassToClassPath(appletClassName, response);

      }
      catch (FailingHttpStatusCodeException e)
      {
        LOG.error(e.getMessage(), e);
      }
    }
    try
    {
      Class<Applet> appletClass = appletClassLoader_.loadClass(appletClassName);
      applet_ = ((Applet)appletClass.newInstance());
      applet_.setStub(new AppletStubImpl(getHtmlPageOrNull(), params, 
        UrlUtils.toUrlUnsafe(baseUrl), UrlUtils.toUrlUnsafe(documentUrl)));
      applet_.init();
      applet_.start();
    }
    catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    catch (InstantiationException e) {
      throw new RuntimeException(e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
