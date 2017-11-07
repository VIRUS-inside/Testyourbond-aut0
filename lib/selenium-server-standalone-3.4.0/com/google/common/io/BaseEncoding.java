package com.google.common.io;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.Nullable;







































































































@GwtCompatible(emulated=true)
public abstract class BaseEncoding
{
  BaseEncoding() {}
  
  public static final class DecodingException
    extends IOException
  {
    DecodingException(String message)
    {
      super();
    }
    
    DecodingException(Throwable cause) {
      super();
    }
  }
  


  public String encode(byte[] bytes)
  {
    return encode(bytes, 0, bytes.length);
  }
  



  public final String encode(byte[] bytes, int off, int len)
  {
    Preconditions.checkPositionIndexes(off, off + len, bytes.length);
    StringBuilder result = new StringBuilder(maxEncodedSize(len));
    try {
      encodeTo(result, bytes, off, len);
    } catch (IOException impossible) {
      throw new AssertionError(impossible);
    }
    return result.toString();
  }
  




  @GwtIncompatible
  public abstract OutputStream encodingStream(Writer paramWriter);
  



  @GwtIncompatible
  public final ByteSink encodingSink(final CharSink encodedSink)
  {
    Preconditions.checkNotNull(encodedSink);
    new ByteSink()
    {
      public OutputStream openStream() throws IOException {
        return encodingStream(encodedSink.openStream());
      }
    };
  }
  

  private static byte[] extract(byte[] result, int length)
  {
    if (length == result.length) {
      return result;
    }
    byte[] trunc = new byte[length];
    System.arraycopy(result, 0, trunc, 0, length);
    return trunc;
  }
  






  public abstract boolean canDecode(CharSequence paramCharSequence);
  






  public final byte[] decode(CharSequence chars)
  {
    try
    {
      return decodeChecked(chars);
    } catch (DecodingException badInput) {
      throw new IllegalArgumentException(badInput);
    }
  }
  





  final byte[] decodeChecked(CharSequence chars)
    throws BaseEncoding.DecodingException
  {
    chars = padding().trimTrailingFrom(chars);
    byte[] tmp = new byte[maxDecodedSize(chars.length())];
    int len = decodeTo(tmp, chars);
    return extract(tmp, len);
  }
  




  @GwtIncompatible
  public abstract InputStream decodingStream(Reader paramReader);
  




  @GwtIncompatible
  public final ByteSource decodingSource(final CharSource encodedSource)
  {
    Preconditions.checkNotNull(encodedSource);
    new ByteSource()
    {
      public InputStream openStream() throws IOException {
        return decodingStream(encodedSource.openStream());
      }
    };
  }
  








  abstract int maxEncodedSize(int paramInt);
  








  abstract void encodeTo(Appendable paramAppendable, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;
  








  abstract int maxDecodedSize(int paramInt);
  








  abstract int decodeTo(byte[] paramArrayOfByte, CharSequence paramCharSequence)
    throws BaseEncoding.DecodingException;
  








  abstract CharMatcher padding();
  







  private static final BaseEncoding BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", 
  
    Character.valueOf('='));
  

  public abstract BaseEncoding omitPadding();
  

  public abstract BaseEncoding withPadChar(char paramChar);
  
  public abstract BaseEncoding withSeparator(String paramString, int paramInt);
  
  public abstract BaseEncoding upperCase();
  
  public abstract BaseEncoding lowerCase();
  
  public static BaseEncoding base64()
  {
    return BASE64; }
  

  private static final BaseEncoding BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", 
  
    Character.valueOf('='));
  













  public static BaseEncoding base64Url()
  {
    return BASE64_URL;
  }
  
  private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", 
    Character.valueOf('='));
  











  public static BaseEncoding base32()
  {
    return BASE32;
  }
  
  private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", 
    Character.valueOf('='));
  











  public static BaseEncoding base32Hex()
  {
    return BASE32_HEX;
  }
  
  private static final BaseEncoding BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");
  












  public static BaseEncoding base16()
  {
    return BASE16;
  }
  
  private static final class Alphabet extends CharMatcher
  {
    private final String name;
    private final char[] chars;
    final int mask;
    final int bitsPerChar;
    final int charsPerChunk;
    final int bytesPerChunk;
    private final byte[] decodabet;
    private final boolean[] validPadding;
    
    Alphabet(String name, char[] chars) {
      this.name = ((String)Preconditions.checkNotNull(name));
      this.chars = ((char[])Preconditions.checkNotNull(chars));
      try {
        bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
      } catch (ArithmeticException e) {
        throw new IllegalArgumentException("Illegal alphabet length " + chars.length, e);
      }
      




      int gcd = Math.min(8, Integer.lowestOneBit(bitsPerChar));
      try {
        charsPerChunk = (8 / gcd);
        bytesPerChunk = (bitsPerChar / gcd);
      } catch (ArithmeticException e) {
        throw new IllegalArgumentException("Illegal alphabet " + new String(chars), e);
      }
      
      mask = (chars.length - 1);
      
      byte[] decodabet = new byte[''];
      Arrays.fill(decodabet, (byte)-1);
      for (int i = 0; i < chars.length; i++) {
        char c = chars[i];
        Preconditions.checkArgument(CharMatcher.ascii().matches(c), "Non-ASCII character: %s", c);
        Preconditions.checkArgument(decodabet[c] == -1, "Duplicate character: %s", c);
        decodabet[c] = ((byte)i);
      }
      this.decodabet = decodabet;
      
      boolean[] validPadding = new boolean[charsPerChunk];
      for (int i = 0; i < bytesPerChunk; i++) {
        validPadding[IntMath.divide(i * 8, bitsPerChar, RoundingMode.CEILING)] = true;
      }
      this.validPadding = validPadding;
    }
    
    char encode(int bits) {
      return chars[bits];
    }
    
    boolean isValidPaddingStartPosition(int index) {
      return validPadding[(index % charsPerChunk)];
    }
    
    boolean canDecode(char ch) {
      return (ch <= '') && (decodabet[ch] != -1);
    }
    
    int decode(char ch) throws BaseEncoding.DecodingException {
      if ((ch > '') || (decodabet[ch] == -1))
      {

        throw new BaseEncoding.DecodingException("Unrecognized character: " + (CharMatcher.invisible().matches(ch) ? "0x" + Integer.toHexString(ch) : Character.valueOf(ch)));
      }
      return decodabet[ch];
    }
    
    private boolean hasLowerCase() {
      for (char c : chars) {
        if (Ascii.isLowerCase(c)) {
          return true;
        }
      }
      return false;
    }
    
    private boolean hasUpperCase() {
      for (char c : chars) {
        if (Ascii.isUpperCase(c)) {
          return true;
        }
      }
      return false;
    }
    
    Alphabet upperCase() {
      if (!hasLowerCase()) {
        return this;
      }
      Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
      char[] upperCased = new char[chars.length];
      for (int i = 0; i < chars.length; i++) {
        upperCased[i] = Ascii.toUpperCase(chars[i]);
      }
      return new Alphabet(name + ".upperCase()", upperCased);
    }
    
    Alphabet lowerCase()
    {
      if (!hasUpperCase()) {
        return this;
      }
      Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
      char[] lowerCased = new char[chars.length];
      for (int i = 0; i < chars.length; i++) {
        lowerCased[i] = Ascii.toLowerCase(chars[i]);
      }
      return new Alphabet(name + ".lowerCase()", lowerCased);
    }
    

    public boolean matches(char c)
    {
      return (CharMatcher.ascii().matches(c)) && (decodabet[c] != -1);
    }
    
    public String toString()
    {
      return name;
    }
    
    public boolean equals(@Nullable Object other)
    {
      if ((other instanceof Alphabet)) {
        Alphabet that = (Alphabet)other;
        return Arrays.equals(chars, chars);
      }
      return false;
    }
    
    public int hashCode()
    {
      return Arrays.hashCode(chars);
    }
  }
  
  static class StandardBaseEncoding extends BaseEncoding {
    final BaseEncoding.Alphabet alphabet;
    @Nullable
    final Character paddingChar;
    private transient BaseEncoding upperCase;
    private transient BaseEncoding lowerCase;
    
    StandardBaseEncoding(String name, String alphabetChars, @Nullable Character paddingChar) { this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar); }
    
    StandardBaseEncoding(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar)
    {
      this.alphabet = ((BaseEncoding.Alphabet)Preconditions.checkNotNull(alphabet));
      Preconditions.checkArgument((paddingChar == null) || 
        (!alphabet.matches(paddingChar.charValue())), "Padding character %s was already in alphabet", paddingChar);
      

      this.paddingChar = paddingChar;
    }
    
    CharMatcher padding()
    {
      return paddingChar == null ? CharMatcher.none() : CharMatcher.is(paddingChar.charValue());
    }
    
    int maxEncodedSize(int bytes)
    {
      return alphabet.charsPerChunk * IntMath.divide(bytes, alphabet.bytesPerChunk, RoundingMode.CEILING);
    }
    
    @GwtIncompatible
    public OutputStream encodingStream(final Writer out)
    {
      Preconditions.checkNotNull(out);
      new OutputStream() {
        int bitBuffer = 0;
        int bitBufferLength = 0;
        int writtenChars = 0;
        
        public void write(int b) throws IOException
        {
          bitBuffer <<= 8;
          bitBuffer |= b & 0xFF;
          bitBufferLength += 8;
          while (bitBufferLength >= alphabet.bitsPerChar) {
            int charIndex = bitBuffer >> bitBufferLength - alphabet.bitsPerChar & alphabet.mask;
            out.write(alphabet.encode(charIndex));
            writtenChars += 1;
            bitBufferLength -= alphabet.bitsPerChar;
          }
        }
        
        public void flush() throws IOException
        {
          out.flush();
        }
        
        public void close() throws IOException
        {
          if (bitBufferLength > 0) {
            int charIndex = bitBuffer << alphabet.bitsPerChar - bitBufferLength & alphabet.mask;
            out.write(alphabet.encode(charIndex));
            writtenChars += 1;
            if (paddingChar != null) {
              while (writtenChars % alphabet.charsPerChunk != 0) {
                out.write(paddingChar.charValue());
                writtenChars += 1;
              }
            }
          }
          out.close();
        }
      };
    }
    
    void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException
    {
      Preconditions.checkNotNull(target);
      Preconditions.checkPositionIndexes(off, off + len, bytes.length);
      for (int i = 0; i < len; i += alphabet.bytesPerChunk) {
        encodeChunkTo(target, bytes, off + i, Math.min(alphabet.bytesPerChunk, len - i));
      }
    }
    
    void encodeChunkTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
      Preconditions.checkNotNull(target);
      Preconditions.checkPositionIndexes(off, off + len, bytes.length);
      Preconditions.checkArgument(len <= alphabet.bytesPerChunk);
      long bitBuffer = 0L;
      for (int i = 0; i < len; i++) {
        bitBuffer |= bytes[(off + i)] & 0xFF;
        bitBuffer <<= 8;
      }
      
      int bitOffset = (len + 1) * 8 - alphabet.bitsPerChar;
      int bitsProcessed = 0;
      while (bitsProcessed < len * 8) {
        int charIndex = (int)(bitBuffer >>> bitOffset - bitsProcessed) & alphabet.mask;
        target.append(alphabet.encode(charIndex));
        bitsProcessed += alphabet.bitsPerChar;
      }
      if (paddingChar != null) {
        while (bitsProcessed < alphabet.bytesPerChunk * 8) {
          target.append(paddingChar.charValue());
          bitsProcessed += alphabet.bitsPerChar;
        }
      }
    }
    
    int maxDecodedSize(int chars)
    {
      return (int)((alphabet.bitsPerChar * chars + 7L) / 8L);
    }
    
    public boolean canDecode(CharSequence chars)
    {
      chars = padding().trimTrailingFrom(chars);
      if (!alphabet.isValidPaddingStartPosition(chars.length())) {
        return false;
      }
      for (int i = 0; i < chars.length(); i++) {
        if (!alphabet.canDecode(chars.charAt(i))) {
          return false;
        }
      }
      return true;
    }
    
    int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException
    {
      Preconditions.checkNotNull(target);
      chars = padding().trimTrailingFrom(chars);
      if (!alphabet.isValidPaddingStartPosition(chars.length())) {
        throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
      }
      int bytesWritten = 0;
      for (int charIdx = 0; charIdx < chars.length(); charIdx += alphabet.charsPerChunk) {
        long chunk = 0L;
        int charsProcessed = 0;
        for (int i = 0; i < alphabet.charsPerChunk; i++) {
          chunk <<= alphabet.bitsPerChar;
          if (charIdx + i < chars.length()) {
            chunk |= alphabet.decode(chars.charAt(charIdx + charsProcessed++));
          }
        }
        int minOffset = alphabet.bytesPerChunk * 8 - charsProcessed * alphabet.bitsPerChar;
        for (int offset = (alphabet.bytesPerChunk - 1) * 8; offset >= minOffset; offset -= 8) {
          target[(bytesWritten++)] = ((byte)(int)(chunk >>> offset & 0xFF));
        }
      }
      return bytesWritten;
    }
    
    @GwtIncompatible
    public InputStream decodingStream(final Reader reader)
    {
      Preconditions.checkNotNull(reader);
      new InputStream() {
        int bitBuffer = 0;
        int bitBufferLength = 0;
        int readChars = 0;
        boolean hitPadding = false;
        final CharMatcher paddingMatcher = padding();
        
        public int read() throws IOException
        {
          for (;;) {
            int readChar = reader.read();
            if (readChar == -1) {
              if ((!hitPadding) && (!alphabet.isValidPaddingStartPosition(readChars))) {
                throw new BaseEncoding.DecodingException("Invalid input length " + readChars);
              }
              return -1;
            }
            readChars += 1;
            char ch = (char)readChar;
            if (paddingMatcher.matches(ch)) {
              if ((!hitPadding) && ((readChars == 1) || 
                (!alphabet.isValidPaddingStartPosition(readChars - 1)))) {
                throw new BaseEncoding.DecodingException("Padding cannot start at index " + readChars);
              }
              hitPadding = true;
            } else { if (hitPadding) {
                throw new BaseEncoding.DecodingException("Expected padding character but found '" + ch + "' at index " + readChars);
              }
              
              bitBuffer <<= alphabet.bitsPerChar;
              bitBuffer |= alphabet.decode(ch);
              bitBufferLength += alphabet.bitsPerChar;
              
              if (bitBufferLength >= 8) {
                bitBufferLength -= 8;
                return bitBuffer >> bitBufferLength & 0xFF;
              }
            }
          }
        }
        
        public void close() throws IOException
        {
          reader.close();
        }
      };
    }
    
    public BaseEncoding omitPadding()
    {
      return paddingChar == null ? this : newInstance(alphabet, null);
    }
    
    public BaseEncoding withPadChar(char padChar)
    {
      if ((8 % alphabet.bitsPerChar == 0) || ((paddingChar != null) && 
        (paddingChar.charValue() == padChar))) {
        return this;
      }
      return newInstance(alphabet, Character.valueOf(padChar));
    }
    

    public BaseEncoding withSeparator(String separator, int afterEveryChars)
    {
      Preconditions.checkArgument(
        padding().or(alphabet).matchesNoneOf(separator), "Separator (%s) cannot contain alphabet or padding characters", separator);
      

      return new BaseEncoding.SeparatedBaseEncoding(this, separator, afterEveryChars);
    }
    



    public BaseEncoding upperCase()
    {
      BaseEncoding result = upperCase;
      if (result == null) {
        BaseEncoding.Alphabet upper = alphabet.upperCase();
        
        result = this.upperCase = upper == alphabet ? this : newInstance(upper, paddingChar);
      }
      return result;
    }
    
    public BaseEncoding lowerCase()
    {
      BaseEncoding result = lowerCase;
      if (result == null) {
        BaseEncoding.Alphabet lower = alphabet.lowerCase();
        
        result = this.lowerCase = lower == alphabet ? this : newInstance(lower, paddingChar);
      }
      return result;
    }
    
    BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
      return new StandardBaseEncoding(alphabet, paddingChar);
    }
    
    public String toString()
    {
      StringBuilder builder = new StringBuilder("BaseEncoding.");
      builder.append(alphabet.toString());
      if (8 % alphabet.bitsPerChar != 0) {
        if (paddingChar == null) {
          builder.append(".omitPadding()");
        } else {
          builder.append(".withPadChar('").append(paddingChar).append("')");
        }
      }
      return builder.toString();
    }
    
    public boolean equals(@Nullable Object other)
    {
      if ((other instanceof StandardBaseEncoding)) {
        StandardBaseEncoding that = (StandardBaseEncoding)other;
        return (alphabet.equals(alphabet)) && 
          (Objects.equal(paddingChar, paddingChar));
      }
      return false;
    }
    
    public int hashCode()
    {
      return alphabet.hashCode() ^ Objects.hashCode(new Object[] { paddingChar });
    }
  }
  
  static final class Base16Encoding extends BaseEncoding.StandardBaseEncoding {
    final char[] encoding = new char['Ȁ'];
    
    Base16Encoding(String name, String alphabetChars) {
      this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()));
    }
    
    private Base16Encoding(BaseEncoding.Alphabet alphabet) {
      super(null);
      Preconditions.checkArgument(BaseEncoding.Alphabet.access$000(alphabet).length == 16);
      for (int i = 0; i < 256; i++) {
        encoding[i] = alphabet.encode(i >>> 4);
        encoding[(i | 0x100)] = alphabet.encode(i & 0xF);
      }
    }
    
    void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException
    {
      Preconditions.checkNotNull(target);
      Preconditions.checkPositionIndexes(off, off + len, bytes.length);
      for (int i = 0; i < len; i++) {
        int b = bytes[(off + i)] & 0xFF;
        target.append(encoding[b]);
        target.append(encoding[(b | 0x100)]);
      }
    }
    
    int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException
    {
      Preconditions.checkNotNull(target);
      if (chars.length() % 2 == 1) {
        throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
      }
      int bytesWritten = 0;
      for (int i = 0; i < chars.length(); i += 2) {
        int decoded = alphabet.decode(chars.charAt(i)) << 4 | alphabet.decode(chars.charAt(i + 1));
        target[(bytesWritten++)] = ((byte)decoded);
      }
      return bytesWritten;
    }
    
    BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar)
    {
      return new Base16Encoding(alphabet);
    }
  }
  
  static final class Base64Encoding extends BaseEncoding.StandardBaseEncoding {
    Base64Encoding(String name, String alphabetChars, @Nullable Character paddingChar) {
      this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
    }
    
    private Base64Encoding(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
      super(paddingChar);
      Preconditions.checkArgument(BaseEncoding.Alphabet.access$000(alphabet).length == 64);
    }
    
    void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException
    {
      Preconditions.checkNotNull(target);
      Preconditions.checkPositionIndexes(off, off + len, bytes.length);
      int i = off;
      for (int remaining = len; remaining >= 3; remaining -= 3) {
        int chunk = (bytes[(i++)] & 0xFF) << 16 | (bytes[(i++)] & 0xFF) << 8 | bytes[(i++)] & 0xFF;
        target.append(alphabet.encode(chunk >>> 18));
        target.append(alphabet.encode(chunk >>> 12 & 0x3F));
        target.append(alphabet.encode(chunk >>> 6 & 0x3F));
        target.append(alphabet.encode(chunk & 0x3F));
      }
      if (i < off + len) {
        encodeChunkTo(target, bytes, i, off + len - i);
      }
    }
    
    int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException
    {
      Preconditions.checkNotNull(target);
      chars = padding().trimTrailingFrom(chars);
      if (!alphabet.isValidPaddingStartPosition(chars.length())) {
        throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
      }
      int bytesWritten = 0;
      for (int i = 0; i < chars.length();) {
        int chunk = alphabet.decode(chars.charAt(i++)) << 18;
        chunk |= alphabet.decode(chars.charAt(i++)) << 12;
        target[(bytesWritten++)] = ((byte)(chunk >>> 16));
        if (i < chars.length()) {
          chunk |= alphabet.decode(chars.charAt(i++)) << 6;
          target[(bytesWritten++)] = ((byte)(chunk >>> 8 & 0xFF));
          if (i < chars.length()) {
            chunk |= alphabet.decode(chars.charAt(i++));
            target[(bytesWritten++)] = ((byte)(chunk & 0xFF));
          }
        }
      }
      return bytesWritten;
    }
    
    BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar)
    {
      return new Base64Encoding(alphabet, paddingChar);
    }
  }
  
  @GwtIncompatible
  static Reader ignoringReader(Reader delegate, final CharMatcher toIgnore) {
    Preconditions.checkNotNull(delegate);
    Preconditions.checkNotNull(toIgnore);
    new Reader()
    {
      public int read() throws IOException {
        int readChar;
        do {
          readChar = val$delegate.read();
        } while ((readChar != -1) && (toIgnore.matches((char)readChar)));
        return readChar;
      }
      
      public int read(char[] cbuf, int off, int len) throws IOException
      {
        throw new UnsupportedOperationException();
      }
      
      public void close() throws IOException
      {
        val$delegate.close();
      }
    };
  }
  
  static Appendable separatingAppendable(final Appendable delegate, final String separator, int afterEveryChars)
  {
    Preconditions.checkNotNull(delegate);
    Preconditions.checkNotNull(separator);
    Preconditions.checkArgument(afterEveryChars > 0);
    new Appendable() {
      int charsUntilSeparator = val$afterEveryChars;
      
      public Appendable append(char c) throws IOException
      {
        if (charsUntilSeparator == 0) {
          delegate.append(separator);
          charsUntilSeparator = val$afterEveryChars;
        }
        delegate.append(c);
        charsUntilSeparator -= 1;
        return this;
      }
      
      public Appendable append(CharSequence chars, int off, int len) throws IOException
      {
        throw new UnsupportedOperationException();
      }
      
      public Appendable append(CharSequence chars) throws IOException
      {
        throw new UnsupportedOperationException();
      }
    };
  }
  

  @GwtIncompatible
  static Writer separatingWriter(final Writer delegate, String separator, int afterEveryChars)
  {
    Appendable seperatingAppendable = separatingAppendable(delegate, separator, afterEveryChars);
    new Writer()
    {
      public void write(int c) throws IOException {
        val$seperatingAppendable.append((char)c);
      }
      
      public void write(char[] chars, int off, int len) throws IOException
      {
        throw new UnsupportedOperationException();
      }
      
      public void flush() throws IOException
      {
        delegate.flush();
      }
      
      public void close() throws IOException
      {
        delegate.close();
      }
    };
  }
  
  static final class SeparatedBaseEncoding extends BaseEncoding {
    private final BaseEncoding delegate;
    private final String separator;
    private final int afterEveryChars;
    private final CharMatcher separatorChars;
    
    SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
      this.delegate = ((BaseEncoding)Preconditions.checkNotNull(delegate));
      this.separator = ((String)Preconditions.checkNotNull(separator));
      this.afterEveryChars = afterEveryChars;
      Preconditions.checkArgument(afterEveryChars > 0, "Cannot add a separator after every %s chars", afterEveryChars);
      
      separatorChars = CharMatcher.anyOf(separator).precomputed();
    }
    
    CharMatcher padding()
    {
      return delegate.padding();
    }
    
    int maxEncodedSize(int bytes)
    {
      int unseparatedSize = delegate.maxEncodedSize(bytes);
      return unseparatedSize + separator
        .length() * IntMath.divide(Math.max(0, unseparatedSize - 1), afterEveryChars, RoundingMode.FLOOR);
    }
    
    @GwtIncompatible
    public OutputStream encodingStream(Writer output)
    {
      return delegate.encodingStream(separatingWriter(output, separator, afterEveryChars));
    }
    
    void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException
    {
      delegate.encodeTo(separatingAppendable(target, separator, afterEveryChars), bytes, off, len);
    }
    
    int maxDecodedSize(int chars)
    {
      return delegate.maxDecodedSize(chars);
    }
    
    public boolean canDecode(CharSequence chars)
    {
      return delegate.canDecode(separatorChars.removeFrom(chars));
    }
    
    int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException
    {
      return delegate.decodeTo(target, separatorChars.removeFrom(chars));
    }
    
    @GwtIncompatible
    public InputStream decodingStream(Reader reader)
    {
      return delegate.decodingStream(ignoringReader(reader, separatorChars));
    }
    
    public BaseEncoding omitPadding()
    {
      return delegate.omitPadding().withSeparator(separator, afterEveryChars);
    }
    
    public BaseEncoding withPadChar(char padChar)
    {
      return delegate.withPadChar(padChar).withSeparator(separator, afterEveryChars);
    }
    
    public BaseEncoding withSeparator(String separator, int afterEveryChars)
    {
      throw new UnsupportedOperationException("Already have a separator");
    }
    
    public BaseEncoding upperCase()
    {
      return delegate.upperCase().withSeparator(separator, afterEveryChars);
    }
    
    public BaseEncoding lowerCase()
    {
      return delegate.lowerCase().withSeparator(separator, afterEveryChars);
    }
    
    public String toString()
    {
      return delegate + ".withSeparator(\"" + separator + "\", " + afterEveryChars + ")";
    }
  }
}
