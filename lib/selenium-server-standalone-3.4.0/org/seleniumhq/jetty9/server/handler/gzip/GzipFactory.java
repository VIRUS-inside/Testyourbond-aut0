package org.seleniumhq.jetty9.server.handler.gzip;

import java.util.zip.Deflater;
import org.seleniumhq.jetty9.server.Request;

public abstract interface GzipFactory
{
  public abstract Deflater getDeflater(Request paramRequest, long paramLong);
  
  public abstract boolean isMimeTypeGzipable(String paramString);
  
  public abstract void recycle(Deflater paramDeflater);
}
