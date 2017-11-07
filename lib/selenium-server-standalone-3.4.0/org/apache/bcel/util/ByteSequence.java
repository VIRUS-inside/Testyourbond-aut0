package org.apache.bcel.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FilterInputStream;

























































public final class ByteSequence
  extends DataInputStream
{
  private ByteArrayStream byte_stream;
  
  public ByteSequence(byte[] bytes)
  {
    super(new ByteArrayStream(bytes));
    byte_stream = ((ByteArrayStream)in);
  }
  
  public final int getIndex() { return byte_stream.getPosition(); }
  final void unreadByte() { byte_stream.unreadByte(); }
  
  private static final class ByteArrayStream extends ByteArrayInputStream {
    ByteArrayStream(byte[] bytes) { super(); }
    final int getPosition() { return pos; }
    final void unreadByte() { if (pos > 0) pos -= 1;
    }
  }
}
