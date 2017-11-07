package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;





































public class HtmlImage
  extends HtmlElement
{
  private static final Log LOG = LogFactory.getLog(HtmlImage.class);
  
  public static final String TAG_NAME = "img";
  
  public static final String TAG_NAME2 = "image";
  
  private final String originalQualifiedName_;
  
  private int lastClickX_;
  
  private int lastClickY_;
  private WebResponse imageWebResponse_;
  private transient ImageData imageData_;
  private int width_ = -1;
  private int height_ = -1;
  

  private boolean downloaded_;
  

  private boolean onloadInvoked_;
  
  private boolean createdByJavascript_;
  

  HtmlImage(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(unifyLocalName(qualifiedName), page, attributes);
    originalQualifiedName_ = qualifiedName;
    if (page.getWebClient().getOptions().isDownloadImages()) {
      try {
        downloadImageIfNeeded();
      }
      catch (IOException e) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Unable to download image for element " + this);
        }
      }
    }
  }
  
  private static String unifyLocalName(String qualifiedName) {
    if ((qualifiedName != null) && (qualifiedName.endsWith("image"))) {
      int pos = qualifiedName.lastIndexOf("image");
      return qualifiedName.substring(0, pos) + "img";
    }
    return qualifiedName;
  }
  



  protected void onAddedToPage()
  {
    doOnLoad();
    super.onAddedToPage();
  }
  





  public void setAttributeNS(String namespaceURI, String qualifiedName, String value, boolean notifyAttributeChangeListeners)
  {
    HtmlPage htmlPage = getHtmlPageOrNull();
    if (("src".equals(qualifiedName)) && (value != ATTRIBUTE_NOT_DEFINED) && (htmlPage != null)) {
      String oldValue = getAttributeNS(namespaceURI, qualifiedName);
      if (!oldValue.equals(value)) {
        super.setAttributeNS(namespaceURI, qualifiedName, value, notifyAttributeChangeListeners);
        

        onloadInvoked_ = false;
        downloaded_ = false;
        width_ = -1;
        height_ = -1;
        if (imageData_ != null) {
          imageData_.close();
          imageData_ = null;
        }
        
        String readyState = htmlPage.getReadyState();
        if ("loading".equals(readyState)) {
          PostponedAction action = new PostponedAction(getPage())
          {
            public void execute() throws Exception {
              doOnLoad();
            }
          };
          htmlPage.addAfterLoadAction(action);
          return;
        }
        doOnLoad();
        return;
      }
    }
    
    super.setAttributeNS(namespaceURI, qualifiedName, value, notifyAttributeChangeListeners);
  }
  














  public void doOnLoad()
  {
    if (onloadInvoked_) {
      return;
    }
    
    HtmlPage htmlPage = getHtmlPageOrNull();
    if (htmlPage == null) {
      return;
    }
    
    WebClient client = htmlPage.getWebClient();
    if (!client.getOptions().isJavaScriptEnabled()) {
      onloadInvoked_ = true;
      return;
    }
    
    if ((hasEventHandlers("onload")) && (!getSrcAttribute().isEmpty())) {
      onloadInvoked_ = true;
      boolean ok;
      try
      {
        downloadImageIfNeeded();
        int i = imageWebResponse_.getStatusCode();
        ok = ((i >= 200) && (i < 300)) || (i == 305);
      } catch (IOException e) {
        boolean ok;
        ok = false;
      }
      
      if (ok) {
        final Event event = new Event(this, "load");
        final Node scriptObject = (Node)getScriptableObject();
        
        String readyState = htmlPage.getReadyState();
        if ("loading".equals(readyState)) {
          PostponedAction action = new PostponedAction(getPage())
          {
            public void execute() throws Exception {
              scriptObject.executeEventLocally(event);
            }
          };
          htmlPage.addAfterLoadAction(action);
        }
        else {
          scriptObject.executeEventLocally(event);
        }
        
      }
      else if (LOG.isDebugEnabled()) {
        LOG.debug("Unable to download image for " + this + "; not firing onload event.");
      }
    }
  }
  







  public final String getSrcAttribute()
  {
    return getSrcAttributeNormalized();
  }
  






  public final String getAltAttribute()
  {
    return getAttribute("alt");
  }
  






  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  






  public final String getLongDescAttribute()
  {
    return getAttribute("longdesc");
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
  






  public final String getIsmapAttribute()
  {
    return getAttribute("ismap");
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
  






  public int getHeight()
    throws IOException
  {
    if (height_ < 0) {
      determineWidthAndHeight();
    }
    return height_;
  }
  






  public int getWidth()
    throws IOException
  {
    if (width_ < 0) {
      determineWidthAndHeight();
    }
    return width_;
  }
  






  public ImageReader getImageReader()
    throws IOException
  {
    readImageIfNeeded();
    return imageData_.getImageReader();
  }
  
  private void determineWidthAndHeight() throws IOException {
    ImageReader imgReader = getImageReader();
    width_ = imgReader.getWidth(0);
    height_ = imgReader.getHeight(0);
    


    if (imageData_ != null) {
      imageData_.close();
      imageData_ = null;
    }
  }
  









  public WebResponse getWebResponse(boolean downloadIfNeeded)
    throws IOException
  {
    if (downloadIfNeeded) {
      downloadImageIfNeeded();
    }
    return imageWebResponse_;
  }
  





  private void downloadImageIfNeeded()
    throws IOException
  {
    if (!downloaded_)
    {
      String src = getSrcAttribute();
      if ((!"".equals(src)) && (
        (!hasFeature(BrowserVersionFeatures.HTMLIMAGE_BLANK_SRC_AS_EMPTY)) || (!StringUtils.isBlank(src)))) {
        HtmlPage page = (HtmlPage)getPage();
        WebClient webclient = page.getWebClient();
        
        URL url = page.getFullyQualifiedUrl(src);
        String accept = webclient.getBrowserVersion().getImgAcceptHeader();
        WebRequest request = new WebRequest(url, accept);
        request.setAdditionalHeader("Referer", page.getUrl().toExternalForm());
        imageWebResponse_ = webclient.loadWebResponse(request);
      }
      
      if (imageData_ != null) {
        imageData_.close();
        imageData_ = null;
      }
      downloaded_ = ((hasFeature(BrowserVersionFeatures.JS_IMAGE_COMPLETE_RETURNS_TRUE_FOR_NO_REQUEST)) || (
        (imageWebResponse_ != null) && (imageWebResponse_.getContentType().contains("image"))));
      
      width_ = -1;
      height_ = -1;
    }
  }
  
  private void readImageIfNeeded() throws IOException {
    downloadImageIfNeeded();
    if (imageData_ == null) {
      if (imageWebResponse_ == null) {
        throw new IOException("No image response available (src=" + getSrcAttribute() + ")");
      }
      
      ImageInputStream iis = ImageIO.createImageInputStream(imageWebResponse_.getContentAsStream());
      Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
      if (!iter.hasNext()) {
        iis.close();
        throw new IOException("No image detected in response");
      }
      ImageReader imageReader = (ImageReader)iter.next();
      imageReader.setInput(iis);
      imageData_ = new ImageData(imageReader);
      

      while (iter.hasNext()) {
        ((ImageReader)iter.next()).dispose();
      }
    }
  }
  









  public Page click(int x, int y)
    throws IOException
  {
    lastClickX_ = x;
    lastClickY_ = y;
    return super.click();
  }
  








  public Page click()
    throws IOException
  {
    return click(0, 0);
  }
  




  protected boolean doClickStateUpdate(boolean shiftKey, boolean ctrlKey)
    throws IOException
  {
    if (getUseMapAttribute() != ATTRIBUTE_NOT_DEFINED)
    {
      String mapName = getUseMapAttribute().substring(1);
      HtmlElement doc = ((HtmlPage)getPage()).getDocumentElement();
      HtmlMap map = (HtmlMap)doc.getOneHtmlElementByAttribute("map", "name", mapName);
      for (DomElement element : map.getChildElements()) {
        if ((element instanceof HtmlArea)) {
          HtmlArea area = (HtmlArea)element;
          if (area.containsPoint(lastClickX_, lastClickY_)) {
            area.doClickStateUpdate(shiftKey, ctrlKey);
            return false;
          }
        }
      }
    }
    HtmlAnchor anchor = (HtmlAnchor)getEnclosingElement("a");
    if (anchor == null) {
      return false;
    }
    if (getIsmapAttribute() != ATTRIBUTE_NOT_DEFINED) {
      String suffix = "?" + lastClickX_ + "," + lastClickY_;
      anchor.doClickStateUpdate(false, false, suffix);
      return false;
    }
    anchor.doClickStateUpdate(shiftKey, ctrlKey);
    return false;
  }
  
  /* Error */
  public void saveAs(java.io.File file)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 76	com/gargoylesoftware/htmlunit/html/HtmlImage:downloadImageIfNeeded	()V
    //   4: aload_0
    //   5: getfield 223	com/gargoylesoftware/htmlunit/html/HtmlImage:imageWebResponse_	Lcom/gargoylesoftware/htmlunit/WebResponse;
    //   8: ifnull +132 -> 140
    //   11: aconst_null
    //   12: astore_2
    //   13: aconst_null
    //   14: astore_3
    //   15: aload_0
    //   16: getfield 223	com/gargoylesoftware/htmlunit/html/HtmlImage:imageWebResponse_	Lcom/gargoylesoftware/htmlunit/WebResponse;
    //   19: invokevirtual 405	com/gargoylesoftware/htmlunit/WebResponse:getContentAsStream	()Ljava/io/InputStream;
    //   22: astore 4
    //   24: new 536	java/io/FileOutputStream
    //   27: dup
    //   28: aload_1
    //   29: invokespecial 538	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   32: astore 5
    //   34: aload_0
    //   35: getfield 223	com/gargoylesoftware/htmlunit/html/HtmlImage:imageWebResponse_	Lcom/gargoylesoftware/htmlunit/WebResponse;
    //   38: invokevirtual 405	com/gargoylesoftware/htmlunit/WebResponse:getContentAsStream	()Ljava/io/InputStream;
    //   41: aload 5
    //   43: invokestatic 540	org/apache/commons/io/IOUtils:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;)I
    //   46: pop
    //   47: aload 5
    //   49: ifnull +24 -> 73
    //   52: aload 5
    //   54: invokevirtual 546	java/io/FileOutputStream:close	()V
    //   57: goto +16 -> 73
    //   60: astore_2
    //   61: aload 5
    //   63: ifnull +8 -> 71
    //   66: aload 5
    //   68: invokevirtual 546	java/io/FileOutputStream:close	()V
    //   71: aload_2
    //   72: athrow
    //   73: aload 4
    //   75: ifnull +65 -> 140
    //   78: aload 4
    //   80: invokevirtual 547	java/io/InputStream:close	()V
    //   83: goto +57 -> 140
    //   86: astore_3
    //   87: aload_2
    //   88: ifnonnull +8 -> 96
    //   91: aload_3
    //   92: astore_2
    //   93: goto +13 -> 106
    //   96: aload_2
    //   97: aload_3
    //   98: if_acmpeq +8 -> 106
    //   101: aload_2
    //   102: aload_3
    //   103: invokevirtual 550	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   106: aload 4
    //   108: ifnull +8 -> 116
    //   111: aload 4
    //   113: invokevirtual 547	java/io/InputStream:close	()V
    //   116: aload_2
    //   117: athrow
    //   118: astore_3
    //   119: aload_2
    //   120: ifnonnull +8 -> 128
    //   123: aload_3
    //   124: astore_2
    //   125: goto +13 -> 138
    //   128: aload_2
    //   129: aload_3
    //   130: if_acmpeq +8 -> 138
    //   133: aload_2
    //   134: aload_3
    //   135: invokevirtual 550	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   138: aload_2
    //   139: athrow
    //   140: return
    // Line number table:
    //   Java source line #571	-> byte code offset #0
    //   Java source line #572	-> byte code offset #4
    //   Java source line #573	-> byte code offset #11
    //   Java source line #573	-> byte code offset #15
    //   Java source line #574	-> byte code offset #24
    //   Java source line #575	-> byte code offset #34
    //   Java source line #576	-> byte code offset #47
    //   Java source line #578	-> byte code offset #140
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	141	0	this	HtmlImage
    //   0	141	1	file	java.io.File
    //   12	1	2	localObject1	Object
    //   60	28	2	localObject2	Object
    //   92	47	2	localObject3	Object
    //   14	1	3	localObject4	Object
    //   86	17	3	localThrowable1	Throwable
    //   118	17	3	localThrowable2	Throwable
    //   22	90	4	inputStream	java.io.InputStream
    //   32	35	5	fileOut	java.io.FileOutputStream
    // Exception table:
    //   from	to	target	type
    //   34	47	60	finally
    //   24	73	86	finally
    //   15	118	118	finally
  }
  
  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
  



  private static final class ImageData
    implements AutoCloseable
  {
    private final ImageReader imageReader_;
    


    ImageData(ImageReader imageReader)
    {
      imageReader_ = imageReader;
    }
    
    public ImageReader getImageReader() {
      return imageReader_;
    }
    


    protected void finalize()
      throws Throwable
    {
      close();
      super.finalize();
    }
    
    /* Error */
    public void close()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 15	com/gargoylesoftware/htmlunit/html/HtmlImage$ImageData:imageReader_	Ljavax/imageio/ImageReader;
      //   4: ifnull +133 -> 137
      //   7: aconst_null
      //   8: astore_1
      //   9: aconst_null
      //   10: astore_2
      //   11: aload_0
      //   12: getfield 15	com/gargoylesoftware/htmlunit/html/HtmlImage$ImageData:imageReader_	Ljavax/imageio/ImageReader;
      //   15: invokevirtual 33	javax/imageio/ImageReader:getInput	()Ljava/lang/Object;
      //   18: checkcast 39	javax/imageio/stream/ImageInputStream
      //   21: astore_3
      //   22: aload_3
      //   23: ifnull +99 -> 122
      //   26: aload_3
      //   27: invokeinterface 41 1 0
      //   32: goto +90 -> 122
      //   35: astore_1
      //   36: aload_3
      //   37: ifnull +9 -> 46
      //   40: aload_3
      //   41: invokeinterface 41 1 0
      //   46: aload_1
      //   47: athrow
      //   48: astore_2
      //   49: aload_1
      //   50: ifnonnull +8 -> 58
      //   53: aload_2
      //   54: astore_1
      //   55: goto +13 -> 68
      //   58: aload_1
      //   59: aload_2
      //   60: if_acmpeq +8 -> 68
      //   63: aload_1
      //   64: aload_2
      //   65: invokevirtual 42	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
      //   68: aload_1
      //   69: athrow
      //   70: astore_1
      //   71: invokestatic 46	com/gargoylesoftware/htmlunit/html/HtmlImage:access$0	()Lorg/apache/commons/logging/Log;
      //   74: aload_1
      //   75: invokevirtual 52	java/io/IOException:getMessage	()Ljava/lang/String;
      //   78: aload_1
      //   79: invokeinterface 58 3 0
      //   84: aload_0
      //   85: getfield 15	com/gargoylesoftware/htmlunit/html/HtmlImage$ImageData:imageReader_	Ljavax/imageio/ImageReader;
      //   88: aconst_null
      //   89: invokevirtual 64	javax/imageio/ImageReader:setInput	(Ljava/lang/Object;)V
      //   92: aload_0
      //   93: getfield 15	com/gargoylesoftware/htmlunit/html/HtmlImage$ImageData:imageReader_	Ljavax/imageio/ImageReader;
      //   96: invokevirtual 68	javax/imageio/ImageReader:dispose	()V
      //   99: goto +38 -> 137
      //   102: astore 4
      //   104: aload_0
      //   105: getfield 15	com/gargoylesoftware/htmlunit/html/HtmlImage$ImageData:imageReader_	Ljavax/imageio/ImageReader;
      //   108: aconst_null
      //   109: invokevirtual 64	javax/imageio/ImageReader:setInput	(Ljava/lang/Object;)V
      //   112: aload_0
      //   113: getfield 15	com/gargoylesoftware/htmlunit/html/HtmlImage$ImageData:imageReader_	Ljavax/imageio/ImageReader;
      //   116: invokevirtual 68	javax/imageio/ImageReader:dispose	()V
      //   119: aload 4
      //   121: athrow
      //   122: aload_0
      //   123: getfield 15	com/gargoylesoftware/htmlunit/html/HtmlImage$ImageData:imageReader_	Ljavax/imageio/ImageReader;
      //   126: aconst_null
      //   127: invokevirtual 64	javax/imageio/ImageReader:setInput	(Ljava/lang/Object;)V
      //   130: aload_0
      //   131: getfield 15	com/gargoylesoftware/htmlunit/html/HtmlImage$ImageData:imageReader_	Ljavax/imageio/ImageReader;
      //   134: invokevirtual 68	javax/imageio/ImageReader:dispose	()V
      //   137: return
      // Line number table:
      //   Java source line #618	-> byte code offset #0
      //   Java source line #620	-> byte code offset #7
      //   Java source line #620	-> byte code offset #11
      //   Java source line #622	-> byte code offset #22
      //   Java source line #624	-> byte code offset #70
      //   Java source line #625	-> byte code offset #71
      //   Java source line #628	-> byte code offset #84
      //   Java source line #629	-> byte code offset #92
      //   Java source line #627	-> byte code offset #102
      //   Java source line #628	-> byte code offset #104
      //   Java source line #629	-> byte code offset #112
      //   Java source line #630	-> byte code offset #119
      //   Java source line #628	-> byte code offset #122
      //   Java source line #629	-> byte code offset #130
      //   Java source line #632	-> byte code offset #137
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	138	0	this	ImageData
      //   71	13	1	e	IOException
      //   22	24	3	stream	ImageInputStream
      // Exception table:
      //   from	to	target	type
      //   11	48	48	finally
      //   7	70	70	java/io/IOException
      //   7	84	102	finally
    }
  }
  
  public boolean isComplete()
  {
    return (downloaded_) || 
      (hasFeature(BrowserVersionFeatures.JS_IMAGE_COMPLETE_RETURNS_TRUE_FOR_NO_REQUEST) ? 
      ATTRIBUTE_NOT_DEFINED == getSrcAttribute() : 
      imageData_ != null);
  }
  



  @Deprecated
  public boolean getComplete()
  {
    return isComplete();
  }
  



  public boolean isDisplayed()
  {
    String src = getSrcAttribute();
    if ((hasFeature(BrowserVersionFeatures.HTMLIMAGE_INVISIBLE_NO_SRC)) && (
      (ATTRIBUTE_NOT_DEFINED == src) || (
      (hasFeature(BrowserVersionFeatures.HTMLIMAGE_BLANK_SRC_AS_EMPTY)) && (StringUtils.isBlank(src))))) {
      return false;
    }
    return super.isDisplayed();
  }
  





  public void markAsCreatedByJavascript()
  {
    createdByJavascript_ = true;
  }
  






  public boolean wasCreatedByJavascript()
  {
    return createdByJavascript_;
  }
  




  public String getOriginalQualifiedName()
  {
    return originalQualifiedName_;
  }
  



  public String getLocalName()
  {
    if ((wasCreatedByJavascript()) && (
      (hasFeature(BrowserVersionFeatures.HTMLIMAGE_HTMLELEMENT)) || (hasFeature(BrowserVersionFeatures.HTMLIMAGE_HTMLUNKNOWNELEMENT)))) {
      return originalQualifiedName_;
    }
    return super.getLocalName();
  }
}
