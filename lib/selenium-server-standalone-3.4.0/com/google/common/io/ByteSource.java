package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
















































@GwtIncompatible
public abstract class ByteSource
{
  protected ByteSource() {}
  
  public CharSource asCharSource(Charset charset)
  {
    return new AsCharSource(charset);
  }
  









  public abstract InputStream openStream()
    throws IOException;
  








  public InputStream openBufferedStream()
    throws IOException
  {
    InputStream in = openStream();
    return (in instanceof BufferedInputStream) ? (BufferedInputStream)in : new BufferedInputStream(in);
  }
  










  public ByteSource slice(long offset, long length)
  {
    return new SlicedByteSource(offset, length);
  }
  











  public boolean isEmpty()
    throws IOException
  {
    Optional<Long> sizeIfKnown = sizeIfKnown();
    if ((sizeIfKnown.isPresent()) && (((Long)sizeIfKnown.get()).longValue() == 0L)) {
      return true;
    }
    Closer closer = Closer.create();
    try {
      InputStream in = (InputStream)closer.register(openStream());
      return in.read() == -1;
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  













  @Beta
  public Optional<Long> sizeIfKnown()
  {
    return Optional.absent();
  }
  

















  public long size()
    throws IOException
  {
    Optional<Long> sizeIfKnown = sizeIfKnown();
    if (sizeIfKnown.isPresent()) {
      return ((Long)sizeIfKnown.get()).longValue();
    }
    
    Closer closer = Closer.create();
    long l;
    try { InputStream in = (InputStream)closer.register(openStream());
      return countBySkipping(in);
    }
    catch (IOException localIOException) {}finally
    {
      closer.close();
    }
    
    closer = Closer.create();
    try {
      InputStream in = (InputStream)closer.register(openStream());
      return ByteStreams.exhaust(in);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  


  private long countBySkipping(InputStream in)
    throws IOException
  {
    long count = 0L;
    long skipped;
    while ((skipped = ByteStreams.skipUpTo(in, 2147483647L)) > 0L) {
      count += skipped;
    }
    return count;
  }
  






  @CanIgnoreReturnValue
  public long copyTo(OutputStream output)
    throws IOException
  {
    Preconditions.checkNotNull(output);
    
    Closer closer = Closer.create();
    try {
      InputStream in = (InputStream)closer.register(openStream());
      return ByteStreams.copy(in, output);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  





  @CanIgnoreReturnValue
  public long copyTo(ByteSink sink)
    throws IOException
  {
    Preconditions.checkNotNull(sink);
    
    Closer closer = Closer.create();
    try {
      InputStream in = (InputStream)closer.register(openStream());
      OutputStream out = (OutputStream)closer.register(sink.openStream());
      return ByteStreams.copy(in, out);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  



  public byte[] read()
    throws IOException
  {
    Closer closer = Closer.create();
    try {
      InputStream in = (InputStream)closer.register(openStream());
      return ByteStreams.toByteArray(in);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  







  @Beta
  @CanIgnoreReturnValue
  public <T> T read(ByteProcessor<T> processor)
    throws IOException
  {
    Preconditions.checkNotNull(processor);
    
    Closer closer = Closer.create();
    try {
      InputStream in = (InputStream)closer.register(openStream());
      return ByteStreams.readBytes(in, processor);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  



  public HashCode hash(HashFunction hashFunction)
    throws IOException
  {
    Hasher hasher = hashFunction.newHasher();
    copyTo(Funnels.asOutputStream(hasher));
    return hasher.hash();
  }
  
  /* Error */
  public boolean contentEquals(ByteSource other)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 29	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   4: pop
    //   5: invokestatic 39	com/google/common/io/ByteStreams:createBuffer	()[B
    //   8: astore_2
    //   9: invokestatic 39	com/google/common/io/ByteStreams:createBuffer	()[B
    //   12: astore_3
    //   13: invokestatic 14	com/google/common/io/Closer:create	()Lcom/google/common/io/Closer;
    //   16: astore 4
    //   18: aload 4
    //   20: aload_0
    //   21: invokevirtual 4	com/google/common/io/ByteSource:openStream	()Ljava/io/InputStream;
    //   24: invokevirtual 15	com/google/common/io/Closer:register	(Ljava/io/Closeable;)Ljava/io/Closeable;
    //   27: checkcast 16	java/io/InputStream
    //   30: astore 5
    //   32: aload 4
    //   34: aload_1
    //   35: invokevirtual 4	com/google/common/io/ByteSource:openStream	()Ljava/io/InputStream;
    //   38: invokevirtual 15	com/google/common/io/Closer:register	(Ljava/io/Closeable;)Ljava/io/Closeable;
    //   41: checkcast 16	java/io/InputStream
    //   44: astore 6
    //   46: aload 5
    //   48: aload_2
    //   49: iconst_0
    //   50: aload_2
    //   51: arraylength
    //   52: invokestatic 40	com/google/common/io/ByteStreams:read	(Ljava/io/InputStream;[BII)I
    //   55: istore 7
    //   57: aload 6
    //   59: aload_3
    //   60: iconst_0
    //   61: aload_3
    //   62: arraylength
    //   63: invokestatic 40	com/google/common/io/ByteStreams:read	(Ljava/io/InputStream;[BII)I
    //   66: istore 8
    //   68: iload 7
    //   70: iload 8
    //   72: if_icmpne +11 -> 83
    //   75: aload_2
    //   76: aload_3
    //   77: invokestatic 41	java/util/Arrays:equals	([B[B)Z
    //   80: ifne +14 -> 94
    //   83: iconst_0
    //   84: istore 9
    //   86: aload 4
    //   88: invokevirtual 18	com/google/common/io/Closer:close	()V
    //   91: iload 9
    //   93: ireturn
    //   94: iload 7
    //   96: aload_2
    //   97: arraylength
    //   98: if_icmpeq +14 -> 112
    //   101: iconst_1
    //   102: istore 9
    //   104: aload 4
    //   106: invokevirtual 18	com/google/common/io/Closer:close	()V
    //   109: iload 9
    //   111: ireturn
    //   112: goto -66 -> 46
    //   115: astore 5
    //   117: aload 4
    //   119: aload 5
    //   121: invokevirtual 20	com/google/common/io/Closer:rethrow	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   124: athrow
    //   125: astore 10
    //   127: aload 4
    //   129: invokevirtual 18	com/google/common/io/Closer:close	()V
    //   132: aload 10
    //   134: athrow
    // Line number table:
    //   Java source line #338	-> byte code offset #0
    //   Java source line #340	-> byte code offset #5
    //   Java source line #341	-> byte code offset #9
    //   Java source line #343	-> byte code offset #13
    //   Java source line #345	-> byte code offset #18
    //   Java source line #346	-> byte code offset #32
    //   Java source line #348	-> byte code offset #46
    //   Java source line #349	-> byte code offset #57
    //   Java source line #350	-> byte code offset #68
    //   Java source line #351	-> byte code offset #83
    //   Java source line #359	-> byte code offset #86
    //   Java source line #351	-> byte code offset #91
    //   Java source line #352	-> byte code offset #94
    //   Java source line #353	-> byte code offset #101
    //   Java source line #359	-> byte code offset #104
    //   Java source line #353	-> byte code offset #109
    //   Java source line #355	-> byte code offset #112
    //   Java source line #356	-> byte code offset #115
    //   Java source line #357	-> byte code offset #117
    //   Java source line #359	-> byte code offset #125
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	135	0	this	ByteSource
    //   0	135	1	other	ByteSource
    //   8	89	2	buf1	byte[]
    //   12	65	3	buf2	byte[]
    //   16	112	4	closer	Closer
    //   30	17	5	in1	InputStream
    //   115	5	5	e	Throwable
    //   44	14	6	in2	InputStream
    //   55	40	7	read1	int
    //   66	5	8	read2	int
    //   84	26	9	bool	boolean
    //   125	8	10	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   18	86	115	java/lang/Throwable
    //   94	104	115	java/lang/Throwable
    //   112	115	115	java/lang/Throwable
    //   18	86	125	finally
    //   94	104	125	finally
    //   112	127	125	finally
  }
  
  public static ByteSource concat(Iterable<? extends ByteSource> sources)
  {
    return new ConcatenatedByteSource(sources);
  }
  

















  public static ByteSource concat(Iterator<? extends ByteSource> sources)
  {
    return concat(ImmutableList.copyOf(sources));
  }
  











  public static ByteSource concat(ByteSource... sources)
  {
    return concat(ImmutableList.copyOf(sources));
  }
  





  public static ByteSource wrap(byte[] b)
  {
    return new ByteArrayByteSource(b);
  }
  




  public static ByteSource empty()
  {
    return EmptyByteSource.INSTANCE;
  }
  

  private final class AsCharSource
    extends CharSource
  {
    final Charset charset;
    
    AsCharSource(Charset charset)
    {
      this.charset = ((Charset)Preconditions.checkNotNull(charset));
    }
    
    public ByteSource asByteSource(Charset charset)
    {
      if (charset.equals(this.charset)) {
        return ByteSource.this;
      }
      return super.asByteSource(charset);
    }
    
    public Reader openStream() throws IOException
    {
      return new InputStreamReader(openStream(), charset);
    }
    
    public String toString()
    {
      return ByteSource.this.toString() + ".asCharSource(" + charset + ")";
    }
  }
  

  private final class SlicedByteSource
    extends ByteSource
  {
    final long offset;
    final long length;
    
    SlicedByteSource(long offset, long length)
    {
      Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
      Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
      this.offset = offset;
      this.length = length;
    }
    
    public InputStream openStream() throws IOException
    {
      return sliceStream(ByteSource.this.openStream());
    }
    
    public InputStream openBufferedStream() throws IOException
    {
      return sliceStream(ByteSource.this.openBufferedStream());
    }
    
    private InputStream sliceStream(InputStream in) throws IOException {
      if (offset > 0L)
      {
        try {
          skipped = ByteStreams.skipUpTo(in, offset);
        } catch (Throwable e) { long skipped;
          Closer closer = Closer.create();
          closer.register(in);
          try {
            throw closer.rethrow(e);
          } finally {
            closer.close();
          }
        }
        long skipped;
        if (skipped < offset)
        {
          in.close();
          return new ByteArrayInputStream(new byte[0]);
        }
      }
      return ByteStreams.limit(in, length);
    }
    
    public ByteSource slice(long offset, long length)
    {
      Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
      Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
      long maxLength = this.length - offset;
      return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
    }
    
    public boolean isEmpty() throws IOException
    {
      return (length == 0L) || (super.isEmpty());
    }
    
    public Optional<Long> sizeIfKnown()
    {
      Optional<Long> optionalUnslicedSize = ByteSource.this.sizeIfKnown();
      if (optionalUnslicedSize.isPresent()) {
        long unslicedSize = ((Long)optionalUnslicedSize.get()).longValue();
        long off = Math.min(offset, unslicedSize);
        return Optional.of(Long.valueOf(Math.min(length, unslicedSize - off)));
      }
      return Optional.absent();
    }
    
    public String toString()
    {
      return ByteSource.this.toString() + ".slice(" + offset + ", " + length + ")";
    }
  }
  
  private static class ByteArrayByteSource extends ByteSource
  {
    final byte[] bytes;
    final int offset;
    final int length;
    
    ByteArrayByteSource(byte[] bytes) {
      this(bytes, 0, bytes.length);
    }
    
    ByteArrayByteSource(byte[] bytes, int offset, int length)
    {
      this.bytes = bytes;
      this.offset = offset;
      this.length = length;
    }
    
    public InputStream openStream()
    {
      return new ByteArrayInputStream(bytes, offset, length);
    }
    
    public InputStream openBufferedStream() throws IOException
    {
      return openStream();
    }
    
    public boolean isEmpty()
    {
      return length == 0;
    }
    
    public long size()
    {
      return length;
    }
    
    public Optional<Long> sizeIfKnown()
    {
      return Optional.of(Long.valueOf(length));
    }
    
    public byte[] read()
    {
      return Arrays.copyOfRange(bytes, offset, offset + length);
    }
    
    public long copyTo(OutputStream output) throws IOException
    {
      output.write(bytes, offset, length);
      return length;
    }
    
    public <T> T read(ByteProcessor<T> processor)
      throws IOException
    {
      processor.processBytes(bytes, offset, length);
      return processor.getResult();
    }
    
    public HashCode hash(HashFunction hashFunction) throws IOException
    {
      return hashFunction.hashBytes(bytes, offset, length);
    }
    
    public ByteSource slice(long offset, long length)
    {
      Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
      Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
      
      offset = Math.min(offset, this.length);
      length = Math.min(length, this.length - offset);
      int newOffset = this.offset + (int)offset;
      return new ByteArrayByteSource(bytes, newOffset, (int)length);
    }
    
    public String toString()
    {
      return 
        "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(bytes, offset, length), 30, "...") + ")";
    }
  }
  
  private static final class EmptyByteSource extends ByteSource.ByteArrayByteSource
  {
    static final EmptyByteSource INSTANCE = new EmptyByteSource();
    
    EmptyByteSource() {
      super();
    }
    
    public CharSource asCharSource(Charset charset)
    {
      Preconditions.checkNotNull(charset);
      return CharSource.empty();
    }
    
    public byte[] read()
    {
      return bytes;
    }
    
    public String toString()
    {
      return "ByteSource.empty()";
    }
  }
  
  private static final class ConcatenatedByteSource extends ByteSource
  {
    final Iterable<? extends ByteSource> sources;
    
    ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
      this.sources = ((Iterable)Preconditions.checkNotNull(sources));
    }
    
    public InputStream openStream() throws IOException
    {
      return new MultiInputStream(sources.iterator());
    }
    
    public boolean isEmpty() throws IOException
    {
      for (ByteSource source : sources) {
        if (!source.isEmpty()) {
          return false;
        }
      }
      return true;
    }
    
    public Optional<Long> sizeIfKnown()
    {
      long result = 0L;
      for (ByteSource source : sources) {
        Optional<Long> sizeIfKnown = source.sizeIfKnown();
        if (!sizeIfKnown.isPresent()) {
          return Optional.absent();
        }
        result += ((Long)sizeIfKnown.get()).longValue();
      }
      return Optional.of(Long.valueOf(result));
    }
    
    public long size() throws IOException
    {
      long result = 0L;
      for (ByteSource source : sources) {
        result += source.size();
      }
      return result;
    }
    
    public String toString()
    {
      return "ByteSource.concat(" + sources + ")";
    }
  }
}
