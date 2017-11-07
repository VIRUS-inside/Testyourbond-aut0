package org.seleniumhq.jetty9.http;





















public class CompressedContentFormat
{
  public static final CompressedContentFormat GZIP = new CompressedContentFormat("gzip", ".gz");
  public static final CompressedContentFormat BR = new CompressedContentFormat("br", ".br");
  public static final CompressedContentFormat[] NONE = new CompressedContentFormat[0];
  
  public final String _encoding;
  public final String _extension;
  public final String _etag;
  public final String _etagQuote;
  public final PreEncodedHttpField _contentEncoding;
  
  public CompressedContentFormat(String encoding, String extension)
  {
    _encoding = encoding;
    _extension = extension;
    _etag = ("--" + encoding);
    _etagQuote = (_etag + "\"");
    _contentEncoding = new PreEncodedHttpField(HttpHeader.CONTENT_ENCODING, encoding);
  }
  

  public boolean equals(Object o)
  {
    if (!(o instanceof CompressedContentFormat))
      return false;
    CompressedContentFormat ccf = (CompressedContentFormat)o;
    if ((_encoding == null) && (_encoding != null))
      return false;
    if ((_extension == null) && (_extension != null)) {
      return false;
    }
    return (_encoding.equalsIgnoreCase(_encoding)) && (_extension.equalsIgnoreCase(_extension));
  }
  
  public static boolean tagEquals(String etag, String tag)
  {
    if (etag.equals(tag)) {
      return true;
    }
    int dashdash = tag.indexOf("--");
    if (dashdash > 0)
      return etag.regionMatches(0, tag, 0, dashdash - 2);
    return false;
  }
}
