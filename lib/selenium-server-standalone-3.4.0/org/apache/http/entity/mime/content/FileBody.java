package org.apache.http.entity.mime.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;












































public class FileBody
  extends AbstractContentBody
{
  private final File file;
  private final String filename;
  
  @Deprecated
  public FileBody(File file, String filename, String mimeType, String charset)
  {
    this(file, ContentType.create(mimeType, charset), filename);
  }
  







  @Deprecated
  public FileBody(File file, String mimeType, String charset)
  {
    this(file, null, mimeType, charset);
  }
  



  @Deprecated
  public FileBody(File file, String mimeType)
  {
    this(file, ContentType.create(mimeType), null);
  }
  
  public FileBody(File file) {
    this(file, ContentType.DEFAULT_BINARY, file != null ? file.getName() : null);
  }
  


  public FileBody(File file, ContentType contentType, String filename)
  {
    super(contentType);
    Args.notNull(file, "File");
    this.file = file;
    this.filename = (filename == null ? file.getName() : filename);
  }
  


  public FileBody(File file, ContentType contentType)
  {
    this(file, contentType, file != null ? file.getName() : null);
  }
  
  public InputStream getInputStream() throws IOException {
    return new FileInputStream(file);
  }
  
  public void writeTo(OutputStream out) throws IOException
  {
    Args.notNull(out, "Output stream");
    InputStream in = new FileInputStream(file);
    try {
      byte[] tmp = new byte['က'];
      int l;
      while ((l = in.read(tmp)) != -1) {
        out.write(tmp, 0, l);
      }
      out.flush();
    } finally {
      in.close();
    }
  }
  
  public String getTransferEncoding()
  {
    return "binary";
  }
  
  public long getContentLength()
  {
    return file.length();
  }
  
  public String getFilename()
  {
    return filename;
  }
  
  public File getFile() {
    return file;
  }
}
