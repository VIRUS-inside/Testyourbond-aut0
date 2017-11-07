package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;























































@GwtIncompatible
public abstract class CharSource
{
  protected CharSource() {}
  
  @Beta
  public ByteSource asByteSource(Charset charset)
  {
    return new AsByteSource(charset);
  }
  







  public abstract Reader openStream()
    throws IOException;
  






  public BufferedReader openBufferedStream()
    throws IOException
  {
    Reader reader = openStream();
    return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
  }
  















  @Beta
  public Optional<Long> lengthIfKnown()
  {
    return Optional.absent();
  }
  

















  @Beta
  public long length()
    throws IOException
  {
    Optional<Long> lengthIfKnown = lengthIfKnown();
    if (lengthIfKnown.isPresent()) {
      return ((Long)lengthIfKnown.get()).longValue();
    }
    
    Closer closer = Closer.create();
    try {
      Reader reader = (Reader)closer.register(openStream());
      return countBySkipping(reader);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  
  private long countBySkipping(Reader reader) throws IOException {
    long count = 0L;
    long read;
    while ((read = reader.skip(Long.MAX_VALUE)) != 0L) {
      count += read;
    }
    return count;
  }
  






  @CanIgnoreReturnValue
  public long copyTo(Appendable appendable)
    throws IOException
  {
    Preconditions.checkNotNull(appendable);
    
    Closer closer = Closer.create();
    try {
      Reader reader = (Reader)closer.register(openStream());
      return CharStreams.copy(reader, appendable);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  





  @CanIgnoreReturnValue
  public long copyTo(CharSink sink)
    throws IOException
  {
    Preconditions.checkNotNull(sink);
    
    Closer closer = Closer.create();
    try {
      Reader reader = (Reader)closer.register(openStream());
      Writer writer = (Writer)closer.register(sink.openStream());
      return CharStreams.copy(reader, writer);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  



  public String read()
    throws IOException
  {
    Closer closer = Closer.create();
    try {
      Reader reader = (Reader)closer.register(openStream());
      return CharStreams.toString(reader);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  







  @Nullable
  public String readFirstLine()
    throws IOException
  {
    Closer closer = Closer.create();
    try {
      BufferedReader reader = (BufferedReader)closer.register(openBufferedStream());
      return reader.readLine();
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  








  public ImmutableList<String> readLines()
    throws IOException
  {
    Closer closer = Closer.create();
    try {
      BufferedReader reader = (BufferedReader)closer.register(openBufferedStream());
      List<String> result = Lists.newArrayList();
      String line;
      while ((line = reader.readLine()) != null) {
        result.add(line);
      }
      return ImmutableList.copyOf(result);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  












  @Beta
  @CanIgnoreReturnValue
  public <T> T readLines(LineProcessor<T> processor)
    throws IOException
  {
    Preconditions.checkNotNull(processor);
    
    Closer closer = Closer.create();
    try {
      Reader reader = (Reader)closer.register(openStream());
      return CharStreams.readLines(reader, processor);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  










  public boolean isEmpty()
    throws IOException
  {
    Optional<Long> lengthIfKnown = lengthIfKnown();
    if ((lengthIfKnown.isPresent()) && (((Long)lengthIfKnown.get()).longValue() == 0L)) {
      return true;
    }
    Closer closer = Closer.create();
    try {
      Reader reader = (Reader)closer.register(openStream());
      return reader.read() == -1;
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
  










  public static CharSource concat(Iterable<? extends CharSource> sources)
  {
    return new ConcatenatedCharSource(sources);
  }
  

















  public static CharSource concat(Iterator<? extends CharSource> sources)
  {
    return concat(ImmutableList.copyOf(sources));
  }
  











  public static CharSource concat(CharSource... sources)
  {
    return concat(ImmutableList.copyOf(sources));
  }
  






  public static CharSource wrap(CharSequence charSequence)
  {
    return new CharSequenceCharSource(charSequence);
  }
  




  public static CharSource empty()
  {
    return EmptyCharSource.INSTANCE;
  }
  

  private final class AsByteSource
    extends ByteSource
  {
    final Charset charset;
    
    AsByteSource(Charset charset)
    {
      this.charset = ((Charset)Preconditions.checkNotNull(charset));
    }
    
    public CharSource asCharSource(Charset charset)
    {
      if (charset.equals(this.charset)) {
        return CharSource.this;
      }
      return super.asCharSource(charset);
    }
    
    public InputStream openStream() throws IOException
    {
      return new ReaderInputStream(openStream(), charset, 8192);
    }
    
    public String toString()
    {
      return CharSource.this.toString() + ".asByteSource(" + charset + ")";
    }
  }
  
  private static class CharSequenceCharSource extends CharSource
  {
    private static final Splitter LINE_SPLITTER = Splitter.onPattern("\r\n|\n|\r");
    private final CharSequence seq;
    
    protected CharSequenceCharSource(CharSequence seq)
    {
      this.seq = ((CharSequence)Preconditions.checkNotNull(seq));
    }
    
    public Reader openStream()
    {
      return new CharSequenceReader(seq);
    }
    
    public String read()
    {
      return seq.toString();
    }
    
    public boolean isEmpty()
    {
      return seq.length() == 0;
    }
    
    public long length()
    {
      return seq.length();
    }
    
    public Optional<Long> lengthIfKnown()
    {
      return Optional.of(Long.valueOf(seq.length()));
    }
    



    private Iterable<String> lines()
    {
      new Iterable()
      {
        public Iterator<String> iterator() {
          new AbstractIterator() {
            Iterator<String> lines = CharSource.CharSequenceCharSource.LINE_SPLITTER.split(seq).iterator();
            
            protected String computeNext()
            {
              if (lines.hasNext()) {
                String next = (String)lines.next();
                
                if ((lines.hasNext()) || (!next.isEmpty())) {
                  return next;
                }
              }
              return (String)endOfData();
            }
          };
        }
      };
    }
    
    public String readFirstLine()
    {
      Iterator<String> lines = lines().iterator();
      return lines.hasNext() ? (String)lines.next() : null;
    }
    
    public ImmutableList<String> readLines()
    {
      return ImmutableList.copyOf(lines());
    }
    
    public <T> T readLines(LineProcessor<T> processor) throws IOException
    {
      for (String line : lines()) {
        if (!processor.processLine(line)) {
          break;
        }
      }
      return processor.getResult();
    }
    
    public String toString()
    {
      return "CharSource.wrap(" + Ascii.truncate(seq, 30, "...") + ")";
    }
  }
  
  private static final class EmptyCharSource extends CharSource.CharSequenceCharSource
  {
    private static final EmptyCharSource INSTANCE = new EmptyCharSource();
    
    private EmptyCharSource() {
      super();
    }
    
    public String toString()
    {
      return "CharSource.empty()";
    }
  }
  
  private static final class ConcatenatedCharSource extends CharSource
  {
    private final Iterable<? extends CharSource> sources;
    
    ConcatenatedCharSource(Iterable<? extends CharSource> sources) {
      this.sources = ((Iterable)Preconditions.checkNotNull(sources));
    }
    
    public Reader openStream() throws IOException
    {
      return new MultiReader(sources.iterator());
    }
    
    public boolean isEmpty() throws IOException
    {
      for (CharSource source : sources) {
        if (!source.isEmpty()) {
          return false;
        }
      }
      return true;
    }
    
    public Optional<Long> lengthIfKnown()
    {
      long result = 0L;
      for (CharSource source : sources) {
        Optional<Long> lengthIfKnown = source.lengthIfKnown();
        if (!lengthIfKnown.isPresent()) {
          return Optional.absent();
        }
        result += ((Long)lengthIfKnown.get()).longValue();
      }
      return Optional.of(Long.valueOf(result));
    }
    
    public long length() throws IOException
    {
      long result = 0L;
      for (CharSource source : sources) {
        result += source.length();
      }
      return result;
    }
    
    public String toString()
    {
      return "CharSource.concat(" + sources + ")";
    }
  }
}
