package javax.xml.transform.sax;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class SAXSource
  implements Source
{
  public static final String FEATURE = "http://javax.xml.transform.sax.SAXSource/feature";
  private XMLReader reader;
  private InputSource inputSource;
  
  public SAXSource() {}
  
  public SAXSource(XMLReader paramXMLReader, InputSource paramInputSource)
  {
    reader = paramXMLReader;
    inputSource = paramInputSource;
  }
  
  public SAXSource(InputSource paramInputSource)
  {
    inputSource = paramInputSource;
  }
  
  public void setXMLReader(XMLReader paramXMLReader)
  {
    reader = paramXMLReader;
  }
  
  public XMLReader getXMLReader()
  {
    return reader;
  }
  
  public void setInputSource(InputSource paramInputSource)
  {
    inputSource = paramInputSource;
  }
  
  public InputSource getInputSource()
  {
    return inputSource;
  }
  
  public void setSystemId(String paramString)
  {
    if (null == inputSource) {
      inputSource = new InputSource(paramString);
    } else {
      inputSource.setSystemId(paramString);
    }
  }
  
  public String getSystemId()
  {
    if (inputSource == null) {
      return null;
    }
    return inputSource.getSystemId();
  }
  
  public static InputSource sourceToInputSource(Source paramSource)
  {
    if ((paramSource instanceof SAXSource)) {
      return ((SAXSource)paramSource).getInputSource();
    }
    if ((paramSource instanceof StreamSource))
    {
      StreamSource localStreamSource = (StreamSource)paramSource;
      InputSource localInputSource = new InputSource(localStreamSource.getSystemId());
      localInputSource.setByteStream(localStreamSource.getInputStream());
      localInputSource.setCharacterStream(localStreamSource.getReader());
      localInputSource.setPublicId(localStreamSource.getPublicId());
      return localInputSource;
    }
    return null;
  }
}
