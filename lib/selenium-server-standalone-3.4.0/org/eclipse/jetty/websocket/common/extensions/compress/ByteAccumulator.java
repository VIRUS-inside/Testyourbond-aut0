package org.eclipse.jetty.websocket.common.extensions.compress;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.websocket.api.MessageTooLargeException;




















public class ByteAccumulator
{
  private final List<byte[]> chunks = new ArrayList();
  private final int maxSize;
  private int length = 0;
  
  public ByteAccumulator(int maxOverallBufferSize)
  {
    maxSize = maxOverallBufferSize;
  }
  
  public void copyChunk(byte[] buf, int offset, int length)
  {
    if (this.length + length > maxSize)
    {
      throw new MessageTooLargeException("Frame is too large");
    }
    
    byte[] copy = new byte[length - offset];
    System.arraycopy(buf, offset, copy, 0, length);
    
    chunks.add(copy);
    this.length += length;
  }
  
  public int getLength()
  {
    return length;
  }
  
  public void transferTo(ByteBuffer buffer)
  {
    if (buffer.remaining() < length)
    {
      throw new IllegalArgumentException(String.format("Not enough space in ByteBuffer remaining [%d] for accumulated buffers length [%d]", new Object[] {
        Integer.valueOf(buffer.remaining()), Integer.valueOf(length) }));
    }
    
    int position = buffer.position();
    for (byte[] chunk : chunks)
    {
      buffer.put(chunk, 0, chunk.length);
    }
    BufferUtil.flipToFlush(buffer, position);
  }
}
