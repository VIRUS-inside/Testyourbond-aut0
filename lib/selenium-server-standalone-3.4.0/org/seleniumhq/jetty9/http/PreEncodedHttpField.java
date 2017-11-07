package org.seleniumhq.jetty9.http;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;































public class PreEncodedHttpField
  extends HttpField
{
  private static final Logger LOG = Log.getLogger(PreEncodedHttpField.class);
  private static final HttpFieldPreEncoder[] __encoders;
  
  static
  {
    List<HttpFieldPreEncoder> encoders = new ArrayList();
    Iterator<HttpFieldPreEncoder> iter = ServiceLoader.load(HttpFieldPreEncoder.class, PreEncodedHttpField.class.getClassLoader()).iterator();
    while (iter.hasNext())
    {
      try
      {
        HttpFieldPreEncoder encoder = (HttpFieldPreEncoder)iter.next();
        if (index(encoder.getHttpVersion()) >= 0) {
          encoders.add(encoder);
        }
      }
      catch (Error|RuntimeException e) {
        LOG.debug(e);
      }
    }
    LOG.debug("HttpField encoders loaded: {}", new Object[] { encoders });
    int size = encoders.size();
    
    __encoders = new HttpFieldPreEncoder[size == 0 ? 1 : size];
    for (HttpFieldPreEncoder e : encoders)
    {
      int i = index(e.getHttpVersion());
      if (__encoders[i] == null) {
        __encoders[i] = e;
      } else {
        LOG.warn("multiple PreEncoders for " + e.getHttpVersion(), new Object[0]);
      }
    }
    
    if (__encoders[0] == null) {
      __encoders[0] = new Http1FieldPreEncoder();
    }
  }
  
  private static int index(HttpVersion version) {
    switch (1.$SwitchMap$org$eclipse$jetty$http$HttpVersion[version.ordinal()])
    {
    case 1: 
    case 2: 
      return 0;
    
    case 3: 
      return 1;
    }
    
    return -1;
  }
  

  private final byte[][] _encodedField = new byte[__encoders.length][];
  
  public PreEncodedHttpField(HttpHeader header, String name, String value)
  {
    super(header, name, value);
    for (int i = 0; i < __encoders.length; i++) {
      _encodedField[i] = __encoders[i].getEncodedField(header, header.asString(), value);
    }
  }
  
  public PreEncodedHttpField(HttpHeader header, String value) {
    this(header, header.asString(), value);
  }
  
  public PreEncodedHttpField(String name, String value)
  {
    this(null, name, value);
  }
  
  public void putTo(ByteBuffer bufferInFillMode, HttpVersion version)
  {
    bufferInFillMode.put(_encodedField[index(version)]);
  }
}
