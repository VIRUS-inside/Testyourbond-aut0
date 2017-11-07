package org.w3c.css.sac;

import java.io.InputStream;
import java.io.Reader;




















































public class InputSource
{
  private String uri;
  private InputStream byteStream;
  private String encoding;
  private Reader characterStream;
  private String title;
  private String media;
  
  public InputSource() {}
  
  public InputSource(String paramString)
  {
    setURI(paramString);
  }
  












  public InputSource(Reader paramReader)
  {
    setCharacterStream(paramReader);
  }
  




















  public void setURI(String paramString)
  {
    uri = paramString;
  }
  











  public String getURI()
  {
    return uri;
  }
  















  public void setByteStream(InputStream paramInputStream)
  {
    byteStream = paramInputStream;
  }
  









  public InputStream getByteStream()
  {
    return byteStream;
  }
  














  public void setEncoding(String paramString)
  {
    encoding = paramString;
  }
  







  public String getEncoding()
  {
    return encoding;
  }
  










  public void setCharacterStream(Reader paramReader)
  {
    characterStream = paramReader;
  }
  





  public Reader getCharacterStream()
  {
    return characterStream;
  }
  






  public void setTitle(String paramString)
  {
    title = paramString;
  }
  


  public String getTitle()
  {
    return title;
  }
  



  public void setMedia(String paramString)
  {
    media = paramString;
  }
  




  public String getMedia()
  {
    if (media == null) {
      return "all";
    }
    return media;
  }
}
