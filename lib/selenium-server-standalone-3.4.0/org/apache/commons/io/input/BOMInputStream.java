package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.ByteOrderMark;






















































































public class BOMInputStream
  extends ProxyInputStream
{
  private final boolean include;
  private final List<ByteOrderMark> boms;
  private ByteOrderMark byteOrderMark;
  private int[] firstBytes;
  private int fbLength;
  private int fbIndex;
  private int markFbIndex;
  private boolean markedAtStart;
  
  public BOMInputStream(InputStream delegate)
  {
    this(delegate, false, new ByteOrderMark[] { ByteOrderMark.UTF_8 });
  }
  







  public BOMInputStream(InputStream delegate, boolean include)
  {
    this(delegate, include, new ByteOrderMark[] { ByteOrderMark.UTF_8 });
  }
  







  public BOMInputStream(InputStream delegate, ByteOrderMark... boms)
  {
    this(delegate, false, boms);
  }
  



  private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator = new Comparator()
  {
    public int compare(ByteOrderMark bom1, ByteOrderMark bom2) {
      int len1 = bom1.length();
      int len2 = bom2.length();
      if (len1 > len2) {
        return -1;
      }
      if (len2 > len1) {
        return 1;
      }
      return 0;
    }
  };
  









  public BOMInputStream(InputStream delegate, boolean include, ByteOrderMark... boms)
  {
    super(delegate);
    if ((boms == null) || (boms.length == 0)) {
      throw new IllegalArgumentException("No BOMs specified");
    }
    this.include = include;
    
    Arrays.sort(boms, ByteOrderMarkLengthComparator);
    this.boms = Arrays.asList(boms);
  }
  






  public boolean hasBOM()
    throws IOException
  {
    return getBOM() != null;
  }
  









  public boolean hasBOM(ByteOrderMark bom)
    throws IOException
  {
    if (!boms.contains(bom)) {
      throw new IllegalArgumentException("Stream not configure to detect " + bom);
    }
    return (byteOrderMark != null) && (getBOM().equals(bom));
  }
  





  public ByteOrderMark getBOM()
    throws IOException
  {
    if (firstBytes == null) {
      fbLength = 0;
      
      int maxBomSize = ((ByteOrderMark)boms.get(0)).length();
      firstBytes = new int[maxBomSize];
      
      for (int i = 0; i < firstBytes.length; i++) {
        firstBytes[i] = in.read();
        fbLength += 1;
        if (firstBytes[i] < 0) {
          break;
        }
      }
      
      byteOrderMark = find();
      if ((byteOrderMark != null) && 
        (!include)) {
        if (byteOrderMark.length() < firstBytes.length) {
          fbIndex = byteOrderMark.length();
        } else {
          fbLength = 0;
        }
      }
    }
    
    return byteOrderMark;
  }
  






  public String getBOMCharsetName()
    throws IOException
  {
    getBOM();
    return byteOrderMark == null ? null : byteOrderMark.getCharsetName();
  }
  







  private int readFirstBytes()
    throws IOException
  {
    getBOM();
    return fbIndex < fbLength ? firstBytes[(fbIndex++)] : -1;
  }
  




  private ByteOrderMark find()
  {
    for (ByteOrderMark bom : boms) {
      if (matches(bom)) {
        return bom;
      }
    }
    return null;
  }
  










  private boolean matches(ByteOrderMark bom)
  {
    for (int i = 0; i < bom.length(); i++) {
      if (bom.get(i) != firstBytes[i]) {
        return false;
      }
    }
    return true;
  }
  










  public int read()
    throws IOException
  {
    int b = readFirstBytes();
    return b >= 0 ? b : in.read();
  }
  












  public int read(byte[] buf, int off, int len)
    throws IOException
  {
    int firstCount = 0;
    int b = 0;
    while ((len > 0) && (b >= 0)) {
      b = readFirstBytes();
      if (b >= 0) {
        buf[(off++)] = ((byte)(b & 0xFF));
        len--;
        firstCount++;
      }
    }
    int secondCount = in.read(buf, off, len);
    return secondCount < 0 ? -1 : firstCount > 0 ? firstCount : firstCount + secondCount;
  }
  








  public int read(byte[] buf)
    throws IOException
  {
    return read(buf, 0, buf.length);
  }
  






  public synchronized void mark(int readlimit)
  {
    markFbIndex = fbIndex;
    markedAtStart = (firstBytes == null);
    in.mark(readlimit);
  }
  





  public synchronized void reset()
    throws IOException
  {
    fbIndex = markFbIndex;
    if (markedAtStart) {
      firstBytes = null;
    }
    
    in.reset();
  }
  








  public long skip(long n)
    throws IOException
  {
    int skipped = 0;
    while ((n > skipped) && (readFirstBytes() >= 0)) {
      skipped++;
    }
    return in.skip(n - skipped) + skipped;
  }
}
