package org.seleniumhq.jetty9.server.handler.gzip;

import java.nio.ByteBuffer;
import java.nio.channels.WritePendingException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import org.seleniumhq.jetty9.http.CompressedContentFormat;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.MimeTypes;
import org.seleniumhq.jetty9.http.PreEncodedHttpField;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.HttpConfiguration;
import org.seleniumhq.jetty9.server.HttpOutput.Interceptor;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Callback;
import org.seleniumhq.jetty9.util.IteratingCallback.Action;
import org.seleniumhq.jetty9.util.IteratingNestedCallback;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

















public class GzipHttpOutputInterceptor
  implements HttpOutput.Interceptor
{
  public static Logger LOG = Log.getLogger(GzipHttpOutputInterceptor.class);
  private static final byte[] GZIP_HEADER = { 31, -117, 8, 0, 0, 0, 0, 0, 0, 0 };
  
  public static final HttpField VARY_ACCEPT_ENCODING_USER_AGENT = new PreEncodedHttpField(HttpHeader.VARY, HttpHeader.ACCEPT_ENCODING + ", " + HttpHeader.USER_AGENT);
  public static final HttpField VARY_ACCEPT_ENCODING = new PreEncodedHttpField(HttpHeader.VARY, HttpHeader.ACCEPT_ENCODING.asString());
  
  private static enum GZState { MIGHT_COMPRESS,  NOT_COMPRESSING,  COMMITTING,  COMPRESSING,  FINISHED;
    private GZState() {} } private final AtomicReference<GZState> _state = new AtomicReference(GZState.MIGHT_COMPRESS);
  private final CRC32 _crc = new CRC32();
  
  private final GzipFactory _factory;
  
  private final HttpOutput.Interceptor _interceptor;
  private final HttpChannel _channel;
  private final HttpField _vary;
  private final int _bufferSize;
  private final boolean _syncFlush;
  private Deflater _deflater;
  private ByteBuffer _buffer;
  
  public GzipHttpOutputInterceptor(GzipFactory factory, HttpChannel channel, HttpOutput.Interceptor next, boolean syncFlush)
  {
    this(factory, VARY_ACCEPT_ENCODING_USER_AGENT, channel.getHttpConfiguration().getOutputBufferSize(), channel, next, syncFlush);
  }
  
  public GzipHttpOutputInterceptor(GzipFactory factory, HttpField vary, HttpChannel channel, HttpOutput.Interceptor next, boolean syncFlush)
  {
    this(factory, vary, channel.getHttpConfiguration().getOutputBufferSize(), channel, next, syncFlush);
  }
  
  public GzipHttpOutputInterceptor(GzipFactory factory, HttpField vary, int bufferSize, HttpChannel channel, HttpOutput.Interceptor next, boolean syncFlush)
  {
    _factory = factory;
    _channel = channel;
    _interceptor = next;
    _vary = vary;
    _bufferSize = bufferSize;
    _syncFlush = syncFlush;
  }
  
  public HttpOutput.Interceptor getNextInterceptor()
  {
    return _interceptor;
  }
  

  public boolean isOptimizedForDirectBuffers()
  {
    return false;
  }
  


  public void write(ByteBuffer content, boolean complete, Callback callback)
  {
    switch (1.$SwitchMap$org$eclipse$jetty$server$handler$gzip$GzipHttpOutputInterceptor$GZState[((GZState)_state.get()).ordinal()])
    {
    case 1: 
      commit(content, complete, callback);
      break;
    
    case 2: 
      _interceptor.write(content, complete, callback);
      return;
    
    case 3: 
      callback.failed(new WritePendingException());
      break;
    
    case 4: 
      gzip(content, complete, callback);
      break;
    
    default: 
      callback.failed(new IllegalStateException("state=" + _state.get()));
    }
    
  }
  
  private void addTrailer()
  {
    int i = _buffer.limit();
    _buffer.limit(i + 8);
    
    int v = (int)_crc.getValue();
    _buffer.put(i++, (byte)(v & 0xFF));
    _buffer.put(i++, (byte)(v >>> 8 & 0xFF));
    _buffer.put(i++, (byte)(v >>> 16 & 0xFF));
    _buffer.put(i++, (byte)(v >>> 24 & 0xFF));
    
    v = _deflater.getTotalIn();
    _buffer.put(i++, (byte)(v & 0xFF));
    _buffer.put(i++, (byte)(v >>> 8 & 0xFF));
    _buffer.put(i++, (byte)(v >>> 16 & 0xFF));
    _buffer.put(i++, (byte)(v >>> 24 & 0xFF));
  }
  

  private void gzip(ByteBuffer content, boolean complete, Callback callback)
  {
    if ((content.hasRemaining()) || (complete)) {
      new GzipBufferCB(content, complete, callback).iterate();
    } else {
      callback.succeeded();
    }
  }
  
  protected void commit(ByteBuffer content, boolean complete, Callback callback)
  {
    Response response = _channel.getResponse();
    int sc = response.getStatus();
    if ((sc > 0) && ((sc < 200) || (sc == 204) || (sc == 205) || (sc >= 300)))
    {
      LOG.debug("{} exclude by status {}", new Object[] { this, Integer.valueOf(sc) });
      noCompression();
      
      if (sc == 304)
      {
        String request_etags = (String)_channel.getRequest().getAttribute("o.e.j.s.h.gzip.GzipHandler.etag");
        String response_etag = response.getHttpFields().get(HttpHeader.ETAG);
        if ((request_etags != null) && (response_etag != null))
        {
          String response_etag_gzip = etagGzip(response_etag);
          if (request_etags.contains(response_etag_gzip)) {
            response.getHttpFields().put(HttpHeader.ETAG, response_etag_gzip);
          }
        }
      }
      _interceptor.write(content, complete, callback);
      return;
    }
    

    String ct = response.getContentType();
    if (ct != null)
    {
      ct = MimeTypes.getContentTypeWithoutCharset(ct);
      if (!_factory.isMimeTypeGzipable(StringUtil.asciiToLowerCase(ct)))
      {
        LOG.debug("{} exclude by mimeType {}", new Object[] { this, ct });
        noCompression();
        _interceptor.write(content, complete, callback);
        return;
      }
    }
    

    HttpFields fields = response.getHttpFields();
    String ce = fields.get(HttpHeader.CONTENT_ENCODING);
    if (ce != null)
    {
      LOG.debug("{} exclude by content-encoding {}", new Object[] { this, ce });
      noCompression();
      _interceptor.write(content, complete, callback);
      return;
    }
    

    if (_state.compareAndSet(GZState.MIGHT_COMPRESS, GZState.COMMITTING))
    {

      if (_vary != null)
      {
        if (fields.contains(HttpHeader.VARY)) {
          fields.addCSV(HttpHeader.VARY, _vary.getValues());
        } else {
          fields.add(_vary);
        }
      }
      long content_length = response.getContentLength();
      if ((content_length < 0L) && (complete)) {
        content_length = content.remaining();
      }
      _deflater = _factory.getDeflater(_channel.getRequest(), content_length);
      
      if (_deflater == null)
      {
        LOG.debug("{} exclude no deflater", new Object[] { this });
        _state.set(GZState.NOT_COMPRESSING);
        _interceptor.write(content, complete, callback);
        return;
      }
      
      fields.put(GZIP_contentEncoding);
      _crc.reset();
      _buffer = _channel.getByteBufferPool().acquire(_bufferSize, false);
      BufferUtil.fill(_buffer, GZIP_HEADER, 0, GZIP_HEADER.length);
      

      response.setContentLength(-1);
      String etag = fields.get(HttpHeader.ETAG);
      if (etag != null) {
        fields.put(HttpHeader.ETAG, etagGzip(etag));
      }
      LOG.debug("{} compressing {}", new Object[] { this, _deflater });
      _state.set(GZState.COMPRESSING);
      
      gzip(content, complete, callback);
    }
    else {
      callback.failed(new WritePendingException());
    }
  }
  
  private String etagGzip(String etag) {
    int end = etag.length() - 1;
    return etag + GZIP_etag;
  }
  
  public void noCompression()
  {
    do
    {
      switch (1.$SwitchMap$org$eclipse$jetty$server$handler$gzip$GzipHttpOutputInterceptor$GZState[((GZState)_state.get()).ordinal()])
      {
      case 2: 
        return;
      }
      
    } while (!_state.compareAndSet(GZState.MIGHT_COMPRESS, GZState.NOT_COMPRESSING));
    return;
    


    throw new IllegalStateException(((GZState)_state.get()).toString());
  }
  


  public void noCompressionIfPossible()
  {
    do
    {
      switch (1.$SwitchMap$org$eclipse$jetty$server$handler$gzip$GzipHttpOutputInterceptor$GZState[((GZState)_state.get()).ordinal()])
      {
      case 2: 
      case 4: 
        return;
      }
      
    } while (!_state.compareAndSet(GZState.MIGHT_COMPRESS, GZState.NOT_COMPRESSING));
    return;
    


    throw new IllegalStateException(((GZState)_state.get()).toString());
  }
  


  public boolean mightCompress()
  {
    return _state.get() == GZState.MIGHT_COMPRESS;
  }
  
  private class GzipBufferCB extends IteratingNestedCallback
  {
    private ByteBuffer _copy;
    private final ByteBuffer _content;
    private final boolean _last;
    
    public GzipBufferCB(ByteBuffer content, boolean complete, Callback callback) {
      super();
      _content = content;
      _last = complete;
    }
    
    protected IteratingCallback.Action process()
      throws Exception
    {
      if (_deflater == null) {
        return IteratingCallback.Action.SUCCEEDED;
      }
      if (_deflater.needsInput())
      {
        if (BufferUtil.isEmpty(_content))
        {
          if (_deflater.finished())
          {
            _factory.recycle(_deflater);
            _deflater = null;
            _channel.getByteBufferPool().release(_buffer);
            _buffer = null;
            if (_copy != null)
            {
              _channel.getByteBufferPool().release(_copy);
              _copy = null;
            }
            return IteratingCallback.Action.SUCCEEDED;
          }
          
          if (!_last)
          {
            return IteratingCallback.Action.SUCCEEDED;
          }
          
          _deflater.finish();
        }
        else if (_content.hasArray())
        {
          byte[] array = _content.array();
          int off = _content.arrayOffset() + _content.position();
          int len = _content.remaining();
          BufferUtil.clear(_content);
          
          _crc.update(array, off, len);
          _deflater.setInput(array, off, len);
          if (_last) {
            _deflater.finish();
          }
        }
        else {
          if (_copy == null)
            _copy = _channel.getByteBufferPool().acquire(_bufferSize, false);
          BufferUtil.clearToFill(_copy);
          int took = BufferUtil.put(_content, _copy);
          BufferUtil.flipToFlush(_copy, 0);
          if (took == 0) {
            throw new IllegalStateException();
          }
          byte[] array = _copy.array();
          int off = _copy.arrayOffset() + _copy.position();
          int len = _copy.remaining();
          
          _crc.update(array, off, len);
          _deflater.setInput(array, off, len);
          if ((_last) && (BufferUtil.isEmpty(_content))) {
            _deflater.finish();
          }
        }
      }
      BufferUtil.compact(_buffer);
      int off = _buffer.arrayOffset() + _buffer.limit();
      int len = _buffer.capacity() - _buffer.limit() - (_last ? 8 : 0);
      if (len > 0)
      {
        int produced = _deflater.deflate(_buffer.array(), off, len, _syncFlush ? 2 : 0);
        _buffer.limit(_buffer.limit() + produced);
      }
      boolean finished = _deflater.finished();
      
      if (finished) {
        GzipHttpOutputInterceptor.this.addTrailer();
      }
      _interceptor.write(_buffer, finished, this);
      return IteratingCallback.Action.SCHEDULED;
    }
  }
}
