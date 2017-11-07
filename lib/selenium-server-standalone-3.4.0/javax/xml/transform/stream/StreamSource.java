package javax.xml.transform.stream;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.transform.Source;

public class StreamSource
  implements Source
{
  public static final String FEATURE = "http://javax.xml.transform.stream.StreamSource/feature";
  private String publicId;
  private String systemId;
  private InputStream inputStream;
  private Reader reader;
  
  public StreamSource() {}
  
  public StreamSource(InputStream paramInputStream)
  {
    setInputStream(paramInputStream);
  }
  
  public StreamSource(InputStream paramInputStream, String paramString)
  {
    setInputStream(paramInputStream);
    setSystemId(paramString);
  }
  
  public StreamSource(Reader paramReader)
  {
    setReader(paramReader);
  }
  
  public StreamSource(Reader paramReader, String paramString)
  {
    setReader(paramReader);
    setSystemId(paramString);
  }
  
  public StreamSource(String paramString)
  {
    systemId = paramString;
  }
  
  public StreamSource(File paramFile)
  {
    setSystemId(paramFile);
  }
  
  public void setInputStream(InputStream paramInputStream)
  {
    inputStream = paramInputStream;
  }
  
  public InputStream getInputStream()
  {
    return inputStream;
  }
  
  public void setReader(Reader paramReader)
  {
    reader = paramReader;
  }
  
  public Reader getReader()
  {
    return reader;
  }
  
  public void setPublicId(String paramString)
  {
    publicId = paramString;
  }
  
  public String getPublicId()
  {
    return publicId;
  }
  
  public void setSystemId(String paramString)
  {
    systemId = paramString;
  }
  
  public String getSystemId()
  {
    return systemId;
  }
  
  public void setSystemId(File paramFile)
  {
    systemId = FilePathToURI.filepath2URI(paramFile.getAbsolutePath());
  }
}
