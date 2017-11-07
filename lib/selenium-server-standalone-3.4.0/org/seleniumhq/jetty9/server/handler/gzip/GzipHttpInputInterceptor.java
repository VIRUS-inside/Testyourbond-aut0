package org.seleniumhq.jetty9.server.handler.gzip;

import java.nio.ByteBuffer;
import org.seleniumhq.jetty9.http.GZIPContentDecoder;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.server.HttpInput.Content;
import org.seleniumhq.jetty9.server.HttpInput.Interceptor;
import org.seleniumhq.jetty9.util.component.Destroyable;






















public class GzipHttpInputInterceptor
  implements HttpInput.Interceptor, Destroyable
{
  private final Decoder _decoder;
  private ByteBuffer _chunk;
  
  public GzipHttpInputInterceptor(ByteBufferPool pool, int bufferSize)
  {
    _decoder = new Decoder(pool, bufferSize, null);
  }
  

  public HttpInput.Content readFrom(HttpInput.Content content)
  {
    _decoder.decodeChunks(content.getByteBuffer());
    final ByteBuffer chunk = _chunk;
    
    if (chunk == null) {
      return null;
    }
    new HttpInput.Content(chunk)
    {

      public void succeeded()
      {
        _decoder.release(chunk);
      }
    };
  }
  

  public void destroy()
  {
    _decoder.destroy();
  }
  
  private class Decoder extends GZIPContentDecoder
  {
    private Decoder(ByteBufferPool pool, int bufferSize)
    {
      super(bufferSize);
    }
    

    protected boolean decodedChunk(ByteBuffer chunk)
    {
      _chunk = chunk;
      return true;
    }
    

    public void decodeChunks(ByteBuffer compressed)
    {
      _chunk = null;
      super.decodeChunks(compressed);
    }
  }
}
