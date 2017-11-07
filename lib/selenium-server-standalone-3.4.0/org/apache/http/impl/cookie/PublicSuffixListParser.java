package org.apache.http.impl.cookie;

import java.io.IOException;
import java.io.Reader;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.util.PublicSuffixList;



































@Deprecated
@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class PublicSuffixListParser
{
  private final PublicSuffixFilter filter;
  private final org.apache.http.conn.util.PublicSuffixListParser parser;
  
  PublicSuffixListParser(PublicSuffixFilter filter)
  {
    this.filter = filter;
    parser = new org.apache.http.conn.util.PublicSuffixListParser();
  }
  






  public void parse(Reader reader)
    throws IOException
  {
    PublicSuffixList suffixList = parser.parse(reader);
    filter.setPublicSuffixes(suffixList.getRules());
    filter.setExceptions(suffixList.getExceptions());
  }
}
