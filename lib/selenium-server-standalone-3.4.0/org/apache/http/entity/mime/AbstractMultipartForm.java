package org.apache.http.entity.mime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;




































abstract class AbstractMultipartForm
{
  private static ByteArrayBuffer encode(Charset charset, String string)
  {
    ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
    ByteArrayBuffer bab = new ByteArrayBuffer(encoded.remaining());
    bab.append(encoded.array(), encoded.position(), encoded.remaining());
    return bab;
  }
  
  private static void writeBytes(ByteArrayBuffer b, OutputStream out) throws IOException
  {
    out.write(b.buffer(), 0, b.length());
  }
  
  private static void writeBytes(String s, Charset charset, OutputStream out) throws IOException
  {
    ByteArrayBuffer b = encode(charset, s);
    writeBytes(b, out);
  }
  
  private static void writeBytes(String s, OutputStream out) throws IOException
  {
    ByteArrayBuffer b = encode(MIME.DEFAULT_CHARSET, s);
    writeBytes(b, out);
  }
  
  protected static void writeField(MinimalField field, OutputStream out) throws IOException
  {
    writeBytes(field.getName(), out);
    writeBytes(FIELD_SEP, out);
    writeBytes(field.getBody(), out);
    writeBytes(CR_LF, out);
  }
  
  protected static void writeField(MinimalField field, Charset charset, OutputStream out) throws IOException
  {
    writeBytes(field.getName(), charset, out);
    writeBytes(FIELD_SEP, out);
    writeBytes(field.getBody(), charset, out);
    writeBytes(CR_LF, out);
  }
  
  private static final ByteArrayBuffer FIELD_SEP = encode(MIME.DEFAULT_CHARSET, ": ");
  private static final ByteArrayBuffer CR_LF = encode(MIME.DEFAULT_CHARSET, "\r\n");
  private static final ByteArrayBuffer TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");
  


  final Charset charset;
  


  final String boundary;
  


  public AbstractMultipartForm(Charset charset, String boundary)
  {
    Args.notNull(boundary, "Multipart boundary");
    this.charset = (charset != null ? charset : MIME.DEFAULT_CHARSET);
    this.boundary = boundary;
  }
  
  public AbstractMultipartForm(String boundary) {
    this(null, boundary);
  }
  

  public abstract List<FormBodyPart> getBodyParts();
  
  void doWriteTo(OutputStream out, boolean writeContent)
    throws IOException
  {
    ByteArrayBuffer boundaryEncoded = encode(charset, boundary);
    for (FormBodyPart part : getBodyParts()) {
      writeBytes(TWO_DASHES, out);
      writeBytes(boundaryEncoded, out);
      writeBytes(CR_LF, out);
      
      formatMultipartHeader(part, out);
      
      writeBytes(CR_LF, out);
      
      if (writeContent) {
        part.getBody().writeTo(out);
      }
      writeBytes(CR_LF, out);
    }
    writeBytes(TWO_DASHES, out);
    writeBytes(boundaryEncoded, out);
    writeBytes(TWO_DASHES, out);
    writeBytes(CR_LF, out);
  }
  




  protected abstract void formatMultipartHeader(FormBodyPart paramFormBodyPart, OutputStream paramOutputStream)
    throws IOException;
  



  public void writeTo(OutputStream out)
    throws IOException
  {
    doWriteTo(out, true);
  }
  













  public long getTotalLength()
  {
    long contentLen = 0L;
    for (FormBodyPart part : getBodyParts()) {
      ContentBody body = part.getBody();
      long len = body.getContentLength();
      if (len >= 0L) {
        contentLen += len;
      } else {
        return -1L;
      }
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      doWriteTo(out, false);
      byte[] extra = out.toByteArray();
      return contentLen + extra.length;
    }
    catch (IOException ex) {}
    return -1L;
  }
}
