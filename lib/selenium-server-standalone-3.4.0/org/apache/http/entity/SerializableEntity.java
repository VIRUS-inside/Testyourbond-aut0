package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.apache.http.util.Args;












































public class SerializableEntity
  extends AbstractHttpEntity
{
  private byte[] objSer;
  private Serializable objRef;
  
  public SerializableEntity(Serializable ser, boolean bufferize)
    throws IOException
  {
    Args.notNull(ser, "Source object");
    if (bufferize) {
      createBytes(ser);
    } else {
      objRef = ser;
    }
  }
  



  public SerializableEntity(Serializable ser)
  {
    Args.notNull(ser, "Source object");
    objRef = ser;
  }
  
  private void createBytes(Serializable ser) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(baos);
    out.writeObject(ser);
    out.flush();
    objSer = baos.toByteArray();
  }
  
  public InputStream getContent() throws IOException, IllegalStateException
  {
    if (objSer == null) {
      createBytes(objRef);
    }
    return new ByteArrayInputStream(objSer);
  }
  
  public long getContentLength()
  {
    if (objSer == null) {
      return -1L;
    }
    return objSer.length;
  }
  

  public boolean isRepeatable()
  {
    return true;
  }
  
  public boolean isStreaming()
  {
    return objSer == null;
  }
  
  public void writeTo(OutputStream outstream) throws IOException
  {
    Args.notNull(outstream, "Output stream");
    if (objSer == null) {
      ObjectOutputStream out = new ObjectOutputStream(outstream);
      out.writeObject(objRef);
      out.flush();
    } else {
      outstream.write(objSer);
      outstream.flush();
    }
  }
}
