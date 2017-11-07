package org.seleniumhq.jetty9.server;

import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import org.seleniumhq.jetty9.util.LazyList;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;












































public class InclusiveByteRange
{
  private static final Logger LOG = Log.getLogger(InclusiveByteRange.class);
  
  long first = 0L;
  long last = 0L;
  
  public InclusiveByteRange(long first, long last)
  {
    this.first = first;
    this.last = last;
  }
  
  public long getFirst()
  {
    return first;
  }
  
  public long getLast()
  {
    return last;
  }
  








  public static List<InclusiveByteRange> satisfiableRanges(Enumeration<String> headers, long size)
  {
    Object satRanges = null;
    


    while (headers.hasMoreElements())
    {
      String header = (String)headers.nextElement();
      StringTokenizer tok = new StringTokenizer(header, "=,", false);
      String t = null;
      try
      {
        for (;;) {
          if (!tok.hasMoreTokens()) {
            break label367;
          }
          try {
            t = tok.nextToken().trim();
            
            long first = -1L;
            long last = -1L;
            int d = t.indexOf('-');
            if ((d < 0) || (t.indexOf("-", d + 1) >= 0))
            {
              if ("bytes".equals(t))
                continue;
              LOG.warn("Bad range format: {}", new Object[] { t });
              break;
            }
            if (d == 0)
            {
              if (d + 1 < t.length()) {
                last = Long.parseLong(t.substring(d + 1).trim());
              }
              else {
                LOG.warn("Bad range format: {}", new Object[] { t });
              }
              
            }
            else if (d + 1 < t.length())
            {
              first = Long.parseLong(t.substring(0, d).trim());
              last = Long.parseLong(t.substring(d + 1).trim());
            }
            else {
              first = Long.parseLong(t.substring(0, d).trim());
            }
            if (((first == -1L) && (last == -1L)) || (
            

              (first != -1L) && (last != -1L) && (first > last))) {
              break;
            }
            if (first < size)
            {
              InclusiveByteRange range = new InclusiveByteRange(first, last);
              satRanges = LazyList.add(satRanges, range);
            }
          }
          catch (NumberFormatException e)
          {
            LOG.warn("Bad range format: {}", new Object[] { t });
            LOG.ignore(e);
          }
        }
      }
      catch (Exception e)
      {
        label367:
        LOG.warn("Bad range format: {}", new Object[] { t });
        LOG.ignore(e);
      }
    }
    return LazyList.getList(satRanges, true);
  }
  

  public long getFirst(long size)
  {
    if (first < 0L)
    {
      long tf = size - last;
      if (tf < 0L)
        tf = 0L;
      return tf;
    }
    return first;
  }
  

  public long getLast(long size)
  {
    if (first < 0L) {
      return size - 1L;
    }
    if ((last < 0L) || (last >= size))
      return size - 1L;
    return last;
  }
  

  public long getSize(long size)
  {
    return getLast(size) - getFirst(size) + 1L;
  }
  


  public String toHeaderRangeString(long size)
  {
    StringBuilder sb = new StringBuilder(40);
    sb.append("bytes ");
    sb.append(getFirst(size));
    sb.append('-');
    sb.append(getLast(size));
    sb.append("/");
    sb.append(size);
    return sb.toString();
  }
  

  public static String to416HeaderRangeString(long size)
  {
    StringBuilder sb = new StringBuilder(40);
    sb.append("bytes */");
    sb.append(size);
    return sb.toString();
  }
  



  public String toString()
  {
    StringBuilder sb = new StringBuilder(60);
    sb.append(Long.toString(first));
    sb.append(":");
    sb.append(Long.toString(last));
    return sb.toString();
  }
}
