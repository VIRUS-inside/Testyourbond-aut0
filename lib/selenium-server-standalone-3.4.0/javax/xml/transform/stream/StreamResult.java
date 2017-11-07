package javax.xml.transform.stream;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import javax.xml.transform.Result;

public class StreamResult
  implements Result
{
  public static final String FEATURE = "http://javax.xml.transform.stream.StreamResult/feature";
  private String systemId;
  private OutputStream outputStream;
  private Writer writer;
  
  public StreamResult() {}
  
  public StreamResult(OutputStream paramOutputStream)
  {
    setOutputStream(paramOutputStream);
  }
  
  public StreamResult(Writer paramWriter)
  {
    setWriter(paramWriter);
  }
  
  public StreamResult(String paramString)
  {
    systemId = paramString;
  }
  
  public StreamResult(File paramFile)
  {
    setSystemId(paramFile);
  }
  
  public void setOutputStream(OutputStream paramOutputStream)
  {
    outputStream = paramOutputStream;
  }
  
  public OutputStream getOutputStream()
  {
    return outputStream;
  }
  
  public void setWriter(Writer paramWriter)
  {
    writer = paramWriter;
  }
  
  public Writer getWriter()
  {
    return writer;
  }
  
  public void setSystemId(String paramString)
  {
    systemId = paramString;
  }
  
  public void setSystemId(File paramFile)
  {
    systemId = FilePathToURI.filepath2URI(paramFile.getAbsolutePath());
  }
  
  public String getSystemId()
  {
    return systemId;
  }
}
