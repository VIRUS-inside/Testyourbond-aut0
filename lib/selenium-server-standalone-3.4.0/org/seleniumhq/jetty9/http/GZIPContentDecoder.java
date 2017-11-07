package org.seleniumhq.jetty9.http;

import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipException;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.component.Destroyable;

























public class GZIPContentDecoder
  implements Destroyable
{
  private final Inflater _inflater = new Inflater(true);
  private final ByteBufferPool _pool;
  private final int _bufferSize;
  private State _state;
  private int _size;
  private int _value;
  private byte _flags;
  private ByteBuffer _inflated;
  
  public GZIPContentDecoder()
  {
    this(null, 2048);
  }
  
  public GZIPContentDecoder(int bufferSize)
  {
    this(null, bufferSize);
  }
  
  public GZIPContentDecoder(ByteBufferPool pool, int bufferSize)
  {
    _bufferSize = bufferSize;
    _pool = pool;
    reset();
  }
  





  public ByteBuffer decode(ByteBuffer compressed)
  {
    decodeChunks(compressed);
    if ((BufferUtil.isEmpty(_inflated)) || (_state == State.CRC) || (_state == State.ISIZE)) {
      return BufferUtil.EMPTY_BUFFER;
    }
    ByteBuffer result = _inflated;
    _inflated = null;
    return result;
  }
  












  protected boolean decodedChunk(ByteBuffer chunk)
  {
    if (_inflated == null)
    {
      _inflated = chunk;
    }
    else
    {
      int size = _inflated.remaining() + chunk.remaining();
      if (size <= _inflated.capacity())
      {
        BufferUtil.append(_inflated, chunk);
        BufferUtil.put(chunk, _inflated);
        release(chunk);
      }
      else
      {
        ByteBuffer bigger = acquire(size);
        int pos = BufferUtil.flipToFill(bigger);
        BufferUtil.put(_inflated, bigger);
        BufferUtil.put(chunk, bigger);
        BufferUtil.flipToFlush(bigger, pos);
        release(_inflated);
        release(chunk);
        _inflated = bigger;
      }
    }
    return false;
  }
  






  protected void decodeChunks(ByteBuffer compressed)
  {
    ByteBuffer buffer = null;
    try
    {
      for (;;)
      {
        switch (1.$SwitchMap$org$eclipse$jetty$http$GZIPContentDecoder$State[_state.ordinal()])
        {

        case 1: 
          _state = State.ID;
          break;
        


        case 2: 
          if ((_flags & 0x4) == 4)
          {
            _state = State.EXTRA_LENGTH;
            _size = 0;
            _value = 0;
          }
          else if ((_flags & 0x8) == 8) {
            _state = State.NAME;
          } else if ((_flags & 0x10) == 16) {
            _state = State.COMMENT;
          } else if ((_flags & 0x2) == 2)
          {
            _state = State.HCRC;
            _size = 0;
            _value = 0;
          }
          else
          {
            _state = State.DATA; }
          break;
        


        case 3: 
          do
          {
            for (;;)
            {
              if (buffer == null) {
                buffer = acquire(_bufferSize);
              }
              try
              {
                int length = _inflater.inflate(buffer.array(), buffer.arrayOffset(), buffer.capacity());
                buffer.limit(length);
              }
              catch (DataFormatException x)
              {
                throw new ZipException(x.getMessage());
              }
              
              if (buffer.hasRemaining())
              {
                ByteBuffer chunk = buffer;
                buffer = null;
                if (decodedChunk(chunk))
                  return;
              } else {
                if (!_inflater.needsInput())
                  break;
                if (!compressed.hasRemaining())
                  return;
                if (compressed.hasArray())
                {
                  _inflater.setInput(compressed.array(), compressed.arrayOffset() + compressed.position(), compressed.remaining());
                  compressed.position(compressed.limit());

                }
                else
                {
                  byte[] input = new byte[compressed.remaining()];
                  compressed.get(input);
                  _inflater.setInput(input);
                }
              }
            } } while (!_inflater.finished());
          
          int remaining = _inflater.getRemaining();
          compressed.position(compressed.limit() - remaining);
          _state = State.CRC;
          _size = 0;
          _value = 0;
          break;
        







        default: 
          if (!compressed.hasRemaining()) {
            break label992;
          }
          byte currByte = compressed.get();
          switch (1.$SwitchMap$org$eclipse$jetty$http$GZIPContentDecoder$State[_state.ordinal()])
          {

          case 4: 
            _value += ((currByte & 0xFF) << 8 * _size);
            _size += 1;
            if (_size == 2)
            {
              if (_value != 35615)
                throw new ZipException("Invalid gzip bytes");
              _state = State.CM;
            }
            

            break;
          case 5: 
            if ((currByte & 0xFF) != 8)
              throw new ZipException("Invalid gzip compression method");
            _state = State.FLG;
            break;
          

          case 6: 
            _flags = currByte;
            _state = State.MTIME;
            _size = 0;
            _value = 0;
            break;
          


          case 7: 
            _size += 1;
            if (_size == 4) {
              _state = State.XFL;
            }
            

            break;
          case 8: 
            _state = State.OS;
            break;
          


          case 9: 
            _state = State.FLAGS;
            break;
          

          case 10: 
            _value += ((currByte & 0xFF) << 8 * _size);
            _size += 1;
            if (_size == 2) {
              _state = State.EXTRA;
            }
            

            break;
          case 11: 
            _value -= 1;
            if (_value == 0)
            {

              _flags = ((byte)(_flags & 0xFFFFFFFB));
              _state = State.FLAGS;
            }
            


            break;
          case 12: 
            if (currByte == 0)
            {

              _flags = ((byte)(_flags & 0xFFFFFFF7));
              _state = State.FLAGS;
            }
            


            break;
          case 13: 
            if (currByte == 0)
            {

              _flags = ((byte)(_flags & 0xFFFFFFEF));
              _state = State.FLAGS;
            }
            


            break;
          case 14: 
            _size += 1;
            if (_size == 2)
            {

              _flags = ((byte)(_flags & 0xFFFFFFFD));
              _state = State.FLAGS;
            }
            

            break;
          case 15: 
            _value += ((currByte & 0xFF) << 8 * _size);
            _size += 1;
            if (_size == 4)
            {

              _state = State.ISIZE;
              _size = 0;
              _value = 0;
            }
            

            break;
          case 16: 
            _value += ((currByte & 0xFF) << 8 * _size);
            _size += 1;
            if (_size == 4)
            {
              if (_value != _inflater.getBytesWritten()) {
                throw new ZipException("Invalid input size");
              }
              
              reset(); return;
            }
            

            break;
          default: 
            throw new ZipException(); }
          
          break; }
        
      }
    } catch (ZipException x) { label992:
      throw new RuntimeException(x);
    }
    finally
    {
      if (buffer != null) {
        release(buffer);
      }
    }
  }
  
  private void reset() {
    _inflater.reset();
    _state = State.INITIAL;
    _size = 0;
    _value = 0;
    _flags = 0;
  }
  

  public void destroy()
  {
    _inflater.end();
  }
  
  public boolean isFinished()
  {
    return _state == State.INITIAL;
  }
  
  private static enum State
  {
    INITIAL,  ID,  CM,  FLG,  MTIME,  XFL,  OS,  FLAGS,  EXTRA_LENGTH,  EXTRA,  NAME,  COMMENT,  HCRC,  DATA,  CRC,  ISIZE;
    

    private State() {}
  }
  
  public ByteBuffer acquire(int capacity)
  {
    return _pool == null ? BufferUtil.allocate(capacity) : _pool.acquire(capacity, false);
  }
  







  public void release(ByteBuffer buffer)
  {
    if ((_pool != null) && (buffer != BufferUtil.EMPTY_BUFFER)) {
      _pool.release(buffer);
    }
  }
}
