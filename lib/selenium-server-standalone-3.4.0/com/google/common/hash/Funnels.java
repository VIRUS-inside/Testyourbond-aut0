package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import javax.annotation.Nullable;






















@Beta
public final class Funnels
{
  private Funnels() {}
  
  public static Funnel<byte[]> byteArrayFunnel()
  {
    return ByteArrayFunnel.INSTANCE;
  }
  
  private static enum ByteArrayFunnel implements Funnel<byte[]> {
    INSTANCE;
    
    private ByteArrayFunnel() {}
    public void funnel(byte[] from, PrimitiveSink into) { into.putBytes(from); }
    

    public String toString()
    {
      return "Funnels.byteArrayFunnel()";
    }
  }
  






  public static Funnel<CharSequence> unencodedCharsFunnel()
  {
    return UnencodedCharsFunnel.INSTANCE;
  }
  
  private static enum UnencodedCharsFunnel implements Funnel<CharSequence> {
    INSTANCE;
    
    private UnencodedCharsFunnel() {}
    public void funnel(CharSequence from, PrimitiveSink into) { into.putUnencodedChars(from); }
    

    public String toString()
    {
      return "Funnels.unencodedCharsFunnel()";
    }
  }
  





  public static Funnel<CharSequence> stringFunnel(Charset charset)
  {
    return new StringCharsetFunnel(charset);
  }
  
  private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable {
    private final Charset charset;
    
    StringCharsetFunnel(Charset charset) {
      this.charset = ((Charset)Preconditions.checkNotNull(charset));
    }
    
    public void funnel(CharSequence from, PrimitiveSink into) {
      into.putString(from, charset);
    }
    
    public String toString()
    {
      return "Funnels.stringFunnel(" + charset.name() + ")";
    }
    
    public boolean equals(@Nullable Object o)
    {
      if ((o instanceof StringCharsetFunnel)) {
        StringCharsetFunnel funnel = (StringCharsetFunnel)o;
        return charset.equals(charset);
      }
      return false;
    }
    
    public int hashCode()
    {
      return StringCharsetFunnel.class.hashCode() ^ charset.hashCode();
    }
    
    Object writeReplace() {
      return new SerializedForm(charset);
    }
    
    private static class SerializedForm implements Serializable {
      private final String charsetCanonicalName;
      private static final long serialVersionUID = 0L;
      
      SerializedForm(Charset charset) { charsetCanonicalName = charset.name(); }
      
      private Object readResolve()
      {
        return Funnels.stringFunnel(Charset.forName(charsetCanonicalName));
      }
    }
  }
  






  public static Funnel<Integer> integerFunnel()
  {
    return IntegerFunnel.INSTANCE;
  }
  
  private static enum IntegerFunnel implements Funnel<Integer> {
    INSTANCE;
    
    private IntegerFunnel() {}
    public void funnel(Integer from, PrimitiveSink into) { into.putInt(from.intValue()); }
    

    public String toString()
    {
      return "Funnels.integerFunnel()";
    }
  }
  





  public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> elementFunnel)
  {
    return new SequentialFunnel(elementFunnel);
  }
  
  private static class SequentialFunnel<E> implements Funnel<Iterable<? extends E>>, Serializable {
    private final Funnel<E> elementFunnel;
    
    SequentialFunnel(Funnel<E> elementFunnel) {
      this.elementFunnel = ((Funnel)Preconditions.checkNotNull(elementFunnel));
    }
    
    public void funnel(Iterable<? extends E> from, PrimitiveSink into) {
      for (E e : from) {
        elementFunnel.funnel(e, into);
      }
    }
    
    public String toString()
    {
      return "Funnels.sequentialFunnel(" + elementFunnel + ")";
    }
    
    public boolean equals(@Nullable Object o)
    {
      if ((o instanceof SequentialFunnel)) {
        SequentialFunnel<?> funnel = (SequentialFunnel)o;
        return elementFunnel.equals(elementFunnel);
      }
      return false;
    }
    
    public int hashCode()
    {
      return SequentialFunnel.class.hashCode() ^ elementFunnel.hashCode();
    }
  }
  




  public static Funnel<Long> longFunnel()
  {
    return LongFunnel.INSTANCE;
  }
  
  private static enum LongFunnel implements Funnel<Long> {
    INSTANCE;
    
    private LongFunnel() {}
    public void funnel(Long from, PrimitiveSink into) { into.putLong(from.longValue()); }
    

    public String toString()
    {
      return "Funnels.longFunnel()";
    }
  }
  









  public static OutputStream asOutputStream(PrimitiveSink sink)
  {
    return new SinkAsStream(sink);
  }
  
  private static class SinkAsStream extends OutputStream {
    final PrimitiveSink sink;
    
    SinkAsStream(PrimitiveSink sink) {
      this.sink = ((PrimitiveSink)Preconditions.checkNotNull(sink));
    }
    
    public void write(int b)
    {
      sink.putByte((byte)b);
    }
    
    public void write(byte[] bytes)
    {
      sink.putBytes(bytes);
    }
    
    public void write(byte[] bytes, int off, int len)
    {
      sink.putBytes(bytes, off, len);
    }
    
    public String toString()
    {
      return "Funnels.asOutputStream(" + sink + ")";
    }
  }
}
