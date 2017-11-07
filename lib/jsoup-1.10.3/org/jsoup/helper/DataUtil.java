package org.jsoup.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;



public final class DataUtil
{
  private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:\"|')?([^\\s,;\"']*)");
  static final String defaultCharset = "UTF-8";
  private static final int bufferSize = 60000;
  private static final char[] mimeBoundaryChars = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    .toCharArray();
  

  static final int boundaryLength = 32;
  


  private DataUtil() {}
  


  public static Document load(File in, String charsetName, String baseUri)
    throws IOException
  {
    ByteBuffer byteData = readFileToByteBuffer(in);
    return parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
  }
  






  public static Document load(InputStream in, String charsetName, String baseUri)
    throws IOException
  {
    ByteBuffer byteData = readToByteBuffer(in);
    return parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
  }
  







  public static Document load(InputStream in, String charsetName, String baseUri, Parser parser)
    throws IOException
  {
    ByteBuffer byteData = readToByteBuffer(in);
    return parseByteData(byteData, charsetName, baseUri, parser);
  }
  




  static void crossStreams(InputStream in, OutputStream out)
    throws IOException
  {
    byte[] buffer = new byte[60000];
    int len;
    while ((len = in.read(buffer)) != -1) {
      out.write(buffer, 0, len);
    }
  }
  



  static Document parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
  {
    Document doc = null;
    

    charsetName = detectCharsetFromBom(byteData, charsetName);
    String docData;
    if (charsetName == null)
    {
      String docData = Charset.forName("UTF-8").decode(byteData).toString();
      doc = parser.parseInput(docData, baseUri);
      Elements metaElements = doc.select("meta[http-equiv=content-type], meta[charset]");
      String foundCharset = null;
      for (Element meta : metaElements) {
        if (meta.hasAttr("http-equiv")) {
          foundCharset = getCharsetFromContentType(meta.attr("content"));
        }
        if ((foundCharset == null) && (meta.hasAttr("charset"))) {
          foundCharset = meta.attr("charset");
        }
        if (foundCharset != null) {
          break;
        }
      }
      

      if ((foundCharset == null) && (doc.childNodeSize() > 0) && ((doc.childNode(0) instanceof XmlDeclaration))) {
        XmlDeclaration prolog = (XmlDeclaration)doc.childNode(0);
        if (prolog.name().equals("xml")) {
          foundCharset = prolog.attr("encoding");
        }
      }
      foundCharset = validateCharset(foundCharset);
      
      if ((foundCharset != null) && (!foundCharset.equals("UTF-8"))) {
        foundCharset = foundCharset.trim().replaceAll("[\"']", "");
        charsetName = foundCharset;
        byteData.rewind();
        docData = Charset.forName(foundCharset).decode(byteData).toString();
        doc = null;
      }
    } else {
      Validate.notEmpty(charsetName, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
      docData = Charset.forName(charsetName).decode(byteData).toString();
    }
    if (doc == null) {
      doc = parser.parseInput(docData, baseUri);
      doc.outputSettings().charset(charsetName);
    }
    return doc;
  }
  






  public static ByteBuffer readToByteBuffer(InputStream inStream, int maxSize)
    throws IOException
  {
    Validate.isTrue(maxSize >= 0, "maxSize must be 0 (unlimited) or larger");
    boolean capped = maxSize > 0;
    byte[] buffer = new byte[(capped) && (maxSize < 60000) ? maxSize : 60000];
    ByteArrayOutputStream outStream = new ByteArrayOutputStream(capped ? maxSize : 60000);
    
    int remaining = maxSize;
    
    while (!Thread.interrupted()) {
      int read = inStream.read(buffer);
      if (read == -1) break;
      if (capped) {
        if (read > remaining) {
          outStream.write(buffer, 0, remaining);
          break;
        }
        remaining -= read;
      }
      outStream.write(buffer, 0, read);
    }
    
    return ByteBuffer.wrap(outStream.toByteArray());
  }
  
  static ByteBuffer readToByteBuffer(InputStream inStream) throws IOException {
    return readToByteBuffer(inStream, 0);
  }
  
  static ByteBuffer readFileToByteBuffer(File file) throws IOException {
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "r");
      byte[] bytes = new byte[(int)randomAccessFile.length()];
      randomAccessFile.readFully(bytes);
      return ByteBuffer.wrap(bytes);
    } finally {
      if (randomAccessFile != null)
        randomAccessFile.close();
    }
  }
  
  static ByteBuffer emptyByteBuffer() {
    return ByteBuffer.allocate(0);
  }
  





  static String getCharsetFromContentType(String contentType)
  {
    if (contentType == null) return null;
    Matcher m = charsetPattern.matcher(contentType);
    if (m.find()) {
      String charset = m.group(1).trim();
      charset = charset.replace("charset=", "");
      return validateCharset(charset);
    }
    return null;
  }
  
  private static String validateCharset(String cs) {
    if ((cs == null) || (cs.length() == 0)) return null;
    cs = cs.trim().replaceAll("[\"']", "");
    try {
      if (Charset.isSupported(cs)) return cs;
      cs = cs.toUpperCase(Locale.ENGLISH);
      if (Charset.isSupported(cs)) { return cs;
      }
    }
    catch (IllegalCharsetNameException localIllegalCharsetNameException) {}
    return null;
  }
  


  static String mimeBoundary()
  {
    StringBuilder mime = new StringBuilder(32);
    Random rand = new Random();
    for (int i = 0; i < 32; i++) {
      mime.append(mimeBoundaryChars[rand.nextInt(mimeBoundaryChars.length)]);
    }
    return mime.toString();
  }
  
  private static String detectCharsetFromBom(ByteBuffer byteData, String charsetName) {
    byteData.mark();
    byte[] bom = new byte[4];
    if (byteData.remaining() >= bom.length) {
      byteData.get(bom);
      byteData.rewind();
    }
    if (((bom[0] == 0) && (bom[1] == 0) && (bom[2] == -2) && (bom[3] == -1)) || ((bom[0] == -1) && (bom[1] == -2) && (bom[2] == 0) && (bom[3] == 0)))
    {
      charsetName = "UTF-32";
    } else if (((bom[0] == -2) && (bom[1] == -1)) || ((bom[0] == -1) && (bom[1] == -2)))
    {
      charsetName = "UTF-16";
    } else if ((bom[0] == -17) && (bom[1] == -69) && (bom[2] == -65)) {
      charsetName = "UTF-8";
      byteData.position(3);
    }
    return charsetName;
  }
}
