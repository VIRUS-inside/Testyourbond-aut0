package org.apache.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.StringBuilderWriter;


















































































public class IOUtils
{
  public static final int EOF = -1;
  public static final char DIR_SEPARATOR_UNIX = '/';
  public static final char DIR_SEPARATOR_WINDOWS = '\\';
  public static final char DIR_SEPARATOR = File.separatorChar;
  
  public static final String LINE_SEPARATOR_UNIX = "\n";
  
  public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
  
  public static final String LINE_SEPARATOR;
  
  private static final int DEFAULT_BUFFER_SIZE = 4096;
  
  private static final int SKIP_BUFFER_SIZE = 2048;
  private static char[] SKIP_CHAR_BUFFER;
  private static byte[] SKIP_BYTE_BUFFER;
  
  static
  {
    StringBuilderWriter buf = new StringBuilderWriter(4);
    PrintWriter out = new PrintWriter(buf);
    out.println();
    LINE_SEPARATOR = buf.toString();
    out.close();
  }
  









































  public static void close(URLConnection conn)
  {
    if ((conn instanceof HttpURLConnection)) {
      ((HttpURLConnection)conn).disconnect();
    }
  }
  






















  public static void closeQuietly(Reader input)
  {
    closeQuietly(input);
  }
  





















  public static void closeQuietly(Writer output)
  {
    closeQuietly(output);
  }
  






















  public static void closeQuietly(InputStream input)
  {
    closeQuietly(input);
  }
  























  public static void closeQuietly(OutputStream output)
  {
    closeQuietly(output);
  }
  

































  public static void closeQuietly(Closeable closeable)
  {
    try
    {
      if (closeable != null) {
        closeable.close();
      }
    }
    catch (IOException localIOException) {}
  }
  











































  public static void closeQuietly(Closeable... closeables)
  {
    if (closeables == null) {
      return;
    }
    for (Closeable closeable : closeables) {
      closeQuietly(closeable);
    }
  }
  






















  public static void closeQuietly(Socket sock)
  {
    if (sock != null) {
      try {
        sock.close();
      }
      catch (IOException localIOException) {}
    }
  }
  























  public static void closeQuietly(Selector selector)
  {
    if (selector != null) {
      try {
        selector.close();
      }
      catch (IOException localIOException) {}
    }
  }
  























  public static void closeQuietly(ServerSocket sock)
  {
    if (sock != null) {
      try {
        sock.close();
      }
      catch (IOException localIOException) {}
    }
  }
  




















  public static InputStream toBufferedInputStream(InputStream input)
    throws IOException
  {
    return ByteArrayOutputStream.toBufferedInputStream(input);
  }
  




















  public static InputStream toBufferedInputStream(InputStream input, int size)
    throws IOException
  {
    return ByteArrayOutputStream.toBufferedInputStream(input, size);
  }
  









  public static BufferedReader toBufferedReader(Reader reader)
  {
    return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
  }
  










  public static BufferedReader toBufferedReader(Reader reader, int size)
  {
    return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader, size);
  }
  








  public static BufferedReader buffer(Reader reader)
  {
    return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
  }
  









  public static BufferedReader buffer(Reader reader, int size)
  {
    return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader, size);
  }
  








  public static BufferedWriter buffer(Writer writer)
  {
    return (writer instanceof BufferedWriter) ? (BufferedWriter)writer : new BufferedWriter(writer);
  }
  









  public static BufferedWriter buffer(Writer writer, int size)
  {
    return (writer instanceof BufferedWriter) ? (BufferedWriter)writer : new BufferedWriter(writer, size);
  }
  









  public static BufferedOutputStream buffer(OutputStream outputStream)
  {
    if (outputStream == null) {
      throw new NullPointerException();
    }
    return (outputStream instanceof BufferedOutputStream) ? (BufferedOutputStream)outputStream : new BufferedOutputStream(outputStream);
  }
  











  public static BufferedOutputStream buffer(OutputStream outputStream, int size)
  {
    if (outputStream == null) {
      throw new NullPointerException();
    }
    return (outputStream instanceof BufferedOutputStream) ? (BufferedOutputStream)outputStream : new BufferedOutputStream(outputStream, size);
  }
  










  public static BufferedInputStream buffer(InputStream inputStream)
  {
    if (inputStream == null) {
      throw new NullPointerException();
    }
    return (inputStream instanceof BufferedInputStream) ? (BufferedInputStream)inputStream : new BufferedInputStream(inputStream);
  }
  











  public static BufferedInputStream buffer(InputStream inputStream, int size)
  {
    if (inputStream == null) {
      throw new NullPointerException();
    }
    return (inputStream instanceof BufferedInputStream) ? (BufferedInputStream)inputStream : new BufferedInputStream(inputStream, size);
  }
  













  public static byte[] toByteArray(InputStream input)
    throws IOException
  {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    copy(input, output);
    return output.toByteArray();
  }
  
















  public static byte[] toByteArray(InputStream input, long size)
    throws IOException
  {
    if (size > 2147483647L) {
      throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
    }
    
    return toByteArray(input, (int)size);
  }
  












  public static byte[] toByteArray(InputStream input, int size)
    throws IOException
  {
    if (size < 0) {
      throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
    }
    
    if (size == 0) {
      return new byte[0];
    }
    
    byte[] data = new byte[size];
    int offset = 0;
    
    int readed;
    while ((offset < size) && ((readed = input.read(data, offset, size - offset)) != -1)) {
      offset += readed;
    }
    
    if (offset != size) {
      throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
    }
    
    return data;
  }
  











  @Deprecated
  public static byte[] toByteArray(Reader input)
    throws IOException
  {
    return toByteArray(input, Charset.defaultCharset());
  }
  












  public static byte[] toByteArray(Reader input, Charset encoding)
    throws IOException
  {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    copy(input, output, encoding);
    return output.toByteArray();
  }
  


















  public static byte[] toByteArray(Reader input, String encoding)
    throws IOException
  {
    return toByteArray(input, Charsets.toCharset(encoding));
  }
  











  @Deprecated
  public static byte[] toByteArray(String input)
    throws IOException
  {
    return input.getBytes(Charset.defaultCharset());
  }
  







  public static byte[] toByteArray(URI uri)
    throws IOException
  {
    return toByteArray(uri.toURL());
  }
  







  public static byte[] toByteArray(URL url)
    throws IOException
  {
    URLConnection conn = url.openConnection();
    try {
      return toByteArray(conn);
    } finally {
      close(conn);
    }
  }
  







  public static byte[] toByteArray(URLConnection urlConn)
    throws IOException
  {
    InputStream inputStream = urlConn.getInputStream();
    try {
      return toByteArray(inputStream);
    } finally {
      inputStream.close();
    }
  }
  















  @Deprecated
  public static char[] toCharArray(InputStream is)
    throws IOException
  {
    return toCharArray(is, Charset.defaultCharset());
  }
  













  public static char[] toCharArray(InputStream is, Charset encoding)
    throws IOException
  {
    CharArrayWriter output = new CharArrayWriter();
    copy(is, output, encoding);
    return output.toCharArray();
  }
  


















  public static char[] toCharArray(InputStream is, String encoding)
    throws IOException
  {
    return toCharArray(is, Charsets.toCharset(encoding));
  }
  










  public static char[] toCharArray(Reader input)
    throws IOException
  {
    CharArrayWriter sw = new CharArrayWriter();
    copy(input, sw);
    return sw.toCharArray();
  }
  














  @Deprecated
  public static String toString(InputStream input)
    throws IOException
  {
    return toString(input, Charset.defaultCharset());
  }
  













  public static String toString(InputStream input, Charset encoding)
    throws IOException
  {
    StringBuilderWriter sw = new StringBuilderWriter();
    copy(input, sw, encoding);
    return sw.toString();
  }
  


















  public static String toString(InputStream input, String encoding)
    throws IOException
  {
    return toString(input, Charsets.toCharset(encoding));
  }
  









  public static String toString(Reader input)
    throws IOException
  {
    StringBuilderWriter sw = new StringBuilderWriter();
    copy(input, sw);
    return sw.toString();
  }
  







  @Deprecated
  public static String toString(URI uri)
    throws IOException
  {
    return toString(uri, Charset.defaultCharset());
  }
  







  public static String toString(URI uri, Charset encoding)
    throws IOException
  {
    return toString(uri.toURL(), Charsets.toCharset(encoding));
  }
  










  public static String toString(URI uri, String encoding)
    throws IOException
  {
    return toString(uri, Charsets.toCharset(encoding));
  }
  







  @Deprecated
  public static String toString(URL url)
    throws IOException
  {
    return toString(url, Charset.defaultCharset());
  }
  







  public static String toString(URL url, Charset encoding)
    throws IOException
  {
    InputStream inputStream = url.openStream();
    try {
      return toString(inputStream, encoding);
    } finally {
      inputStream.close();
    }
  }
  










  public static String toString(URL url, String encoding)
    throws IOException
  {
    return toString(url, Charsets.toCharset(encoding));
  }
  









  @Deprecated
  public static String toString(byte[] input)
    throws IOException
  {
    return new String(input, Charset.defaultCharset());
  }
  











  public static String toString(byte[] input, String encoding)
    throws IOException
  {
    return new String(input, Charsets.toCharset(encoding));
  }
  















  @Deprecated
  public static List<String> readLines(InputStream input)
    throws IOException
  {
    return readLines(input, Charset.defaultCharset());
  }
  












  public static List<String> readLines(InputStream input, Charset encoding)
    throws IOException
  {
    InputStreamReader reader = new InputStreamReader(input, Charsets.toCharset(encoding));
    return readLines(reader);
  }
  


















  public static List<String> readLines(InputStream input, String encoding)
    throws IOException
  {
    return readLines(input, Charsets.toCharset(encoding));
  }
  











  public static List<String> readLines(Reader input)
    throws IOException
  {
    BufferedReader reader = toBufferedReader(input);
    List<String> list = new ArrayList();
    String line = reader.readLine();
    while (line != null) {
      list.add(line);
      line = reader.readLine();
    }
    return list;
  }
  





























  public static LineIterator lineIterator(Reader reader)
  {
    return new LineIterator(reader);
  }
  




























  public static LineIterator lineIterator(InputStream input, Charset encoding)
    throws IOException
  {
    return new LineIterator(new InputStreamReader(input, Charsets.toCharset(encoding)));
  }
  































  public static LineIterator lineIterator(InputStream input, String encoding)
    throws IOException
  {
    return lineIterator(input, Charsets.toCharset(encoding));
  }
  










  @Deprecated
  public static InputStream toInputStream(CharSequence input)
  {
    return toInputStream(input, Charset.defaultCharset());
  }
  








  public static InputStream toInputStream(CharSequence input, Charset encoding)
  {
    return toInputStream(input.toString(), encoding);
  }
  














  public static InputStream toInputStream(CharSequence input, String encoding)
    throws IOException
  {
    return toInputStream(input, Charsets.toCharset(encoding));
  }
  










  @Deprecated
  public static InputStream toInputStream(String input)
  {
    return toInputStream(input, Charset.defaultCharset());
  }
  








  public static InputStream toInputStream(String input, Charset encoding)
  {
    return new ByteArrayInputStream(input.getBytes(Charsets.toCharset(encoding)));
  }
  














  public static InputStream toInputStream(String input, String encoding)
    throws IOException
  {
    byte[] bytes = input.getBytes(Charsets.toCharset(encoding));
    return new ByteArrayInputStream(bytes);
  }
  












  public static void write(byte[] data, OutputStream output)
    throws IOException
  {
    if (data != null) {
      output.write(data);
    }
  }
  











  public static void writeChunked(byte[] data, OutputStream output)
    throws IOException
  {
    if (data != null) {
      int bytes = data.length;
      int offset = 0;
      while (bytes > 0) {
        int chunk = Math.min(bytes, 4096);
        output.write(data, offset, chunk);
        bytes -= chunk;
        offset += chunk;
      }
    }
  }
  












  @Deprecated
  public static void write(byte[] data, Writer output)
    throws IOException
  {
    write(data, output, Charset.defaultCharset());
  }
  












  public static void write(byte[] data, Writer output, Charset encoding)
    throws IOException
  {
    if (data != null) {
      output.write(new String(data, Charsets.toCharset(encoding)));
    }
  }
  


















  public static void write(byte[] data, Writer output, String encoding)
    throws IOException
  {
    write(data, output, Charsets.toCharset(encoding));
  }
  











  public static void write(char[] data, Writer output)
    throws IOException
  {
    if (data != null) {
      output.write(data);
    }
  }
  










  public static void writeChunked(char[] data, Writer output)
    throws IOException
  {
    if (data != null) {
      int bytes = data.length;
      int offset = 0;
      while (bytes > 0) {
        int chunk = Math.min(bytes, 4096);
        output.write(data, offset, chunk);
        bytes -= chunk;
        offset += chunk;
      }
    }
  }
  














  @Deprecated
  public static void write(char[] data, OutputStream output)
    throws IOException
  {
    write(data, output, Charset.defaultCharset());
  }
  













  public static void write(char[] data, OutputStream output, Charset encoding)
    throws IOException
  {
    if (data != null) {
      output.write(new String(data).getBytes(Charsets.toCharset(encoding)));
    }
  }
  



















  public static void write(char[] data, OutputStream output, String encoding)
    throws IOException
  {
    write(data, output, Charsets.toCharset(encoding));
  }
  










  public static void write(CharSequence data, Writer output)
    throws IOException
  {
    if (data != null) {
      write(data.toString(), output);
    }
  }
  













  @Deprecated
  public static void write(CharSequence data, OutputStream output)
    throws IOException
  {
    write(data, output, Charset.defaultCharset());
  }
  












  public static void write(CharSequence data, OutputStream output, Charset encoding)
    throws IOException
  {
    if (data != null) {
      write(data.toString(), output, encoding);
    }
  }
  

















  public static void write(CharSequence data, OutputStream output, String encoding)
    throws IOException
  {
    write(data, output, Charsets.toCharset(encoding));
  }
  










  public static void write(String data, Writer output)
    throws IOException
  {
    if (data != null) {
      output.write(data);
    }
  }
  













  @Deprecated
  public static void write(String data, OutputStream output)
    throws IOException
  {
    write(data, output, Charset.defaultCharset());
  }
  











  public static void write(String data, OutputStream output, Charset encoding)
    throws IOException
  {
    if (data != null) {
      output.write(data.getBytes(Charsets.toCharset(encoding)));
    }
  }
  

















  public static void write(String data, OutputStream output, String encoding)
    throws IOException
  {
    write(data, output, Charsets.toCharset(encoding));
  }
  












  @Deprecated
  public static void write(StringBuffer data, Writer output)
    throws IOException
  {
    if (data != null) {
      output.write(data.toString());
    }
  }
  













  @Deprecated
  public static void write(StringBuffer data, OutputStream output)
    throws IOException
  {
    write(data, output, (String)null);
  }
  


















  @Deprecated
  public static void write(StringBuffer data, OutputStream output, String encoding)
    throws IOException
  {
    if (data != null) {
      output.write(data.toString().getBytes(Charsets.toCharset(encoding)));
    }
  }
  















  @Deprecated
  public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output)
    throws IOException
  {
    writeLines(lines, lineEnding, output, Charset.defaultCharset());
  }
  












  public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset encoding)
    throws IOException
  {
    if (lines == null) {
      return;
    }
    if (lineEnding == null) {
      lineEnding = LINE_SEPARATOR;
    }
    Charset cs = Charsets.toCharset(encoding);
    for (Object line : lines) {
      if (line != null) {
        output.write(line.toString().getBytes(cs));
      }
      output.write(lineEnding.getBytes(cs));
    }
  }
  


















  public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String encoding)
    throws IOException
  {
    writeLines(lines, lineEnding, output, Charsets.toCharset(encoding));
  }
  










  public static void writeLines(Collection<?> lines, String lineEnding, Writer writer)
    throws IOException
  {
    if (lines == null) {
      return;
    }
    if (lineEnding == null) {
      lineEnding = LINE_SEPARATOR;
    }
    for (Object line : lines) {
      if (line != null) {
        writer.write(line.toString());
      }
      writer.write(lineEnding);
    }
  }
  




















  public static int copy(InputStream input, OutputStream output)
    throws IOException
  {
    long count = copyLarge(input, output);
    if (count > 2147483647L) {
      return -1;
    }
    return (int)count;
  }
  














  public static long copy(InputStream input, OutputStream output, int bufferSize)
    throws IOException
  {
    return copyLarge(input, output, new byte[bufferSize]);
  }
  















  public static long copyLarge(InputStream input, OutputStream output)
    throws IOException
  {
    return copy(input, output, 4096);
  }
  















  public static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
    throws IOException
  {
    long count = 0L;
    int n;
    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    }
    return count;
  }
  























  public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length)
    throws IOException
  {
    return copyLarge(input, output, inputOffset, length, new byte['က']);
  }
  























  public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer)
    throws IOException
  {
    if (inputOffset > 0L) {
      skipFully(input, inputOffset);
    }
    if (length == 0L) {
      return 0L;
    }
    int bufferLength = buffer.length;
    int bytesToRead = bufferLength;
    if ((length > 0L) && (length < bufferLength)) {
      bytesToRead = (int)length;
    }
    
    long totalRead = 0L;
    int read; while ((bytesToRead > 0) && (-1 != (read = input.read(buffer, 0, bytesToRead)))) {
      output.write(buffer, 0, read);
      totalRead += read;
      if (length > 0L)
      {
        bytesToRead = (int)Math.min(length - totalRead, bufferLength);
      }
    }
    return totalRead;
  }
  















  @Deprecated
  public static void copy(InputStream input, Writer output)
    throws IOException
  {
    copy(input, output, Charset.defaultCharset());
  }
  















  public static void copy(InputStream input, Writer output, Charset inputEncoding)
    throws IOException
  {
    InputStreamReader in = new InputStreamReader(input, Charsets.toCharset(inputEncoding));
    copy(in, output);
  }
  





















  public static void copy(InputStream input, Writer output, String inputEncoding)
    throws IOException
  {
    copy(input, output, Charsets.toCharset(inputEncoding));
  }
  



















  public static int copy(Reader input, Writer output)
    throws IOException
  {
    long count = copyLarge(input, output);
    if (count > 2147483647L) {
      return -1;
    }
    return (int)count;
  }
  













  public static long copyLarge(Reader input, Writer output)
    throws IOException
  {
    return copyLarge(input, output, new char['က']);
  }
  













  public static long copyLarge(Reader input, Writer output, char[] buffer)
    throws IOException
  {
    long count = 0L;
    int n;
    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    }
    return count;
  }
  


















  public static long copyLarge(Reader input, Writer output, long inputOffset, long length)
    throws IOException
  {
    return copyLarge(input, output, inputOffset, length, new char['က']);
  }
  



















  public static long copyLarge(Reader input, Writer output, long inputOffset, long length, char[] buffer)
    throws IOException
  {
    if (inputOffset > 0L) {
      skipFully(input, inputOffset);
    }
    if (length == 0L) {
      return 0L;
    }
    int bytesToRead = buffer.length;
    if ((length > 0L) && (length < buffer.length)) {
      bytesToRead = (int)length;
    }
    
    long totalRead = 0L;
    int read; while ((bytesToRead > 0) && (-1 != (read = input.read(buffer, 0, bytesToRead)))) {
      output.write(buffer, 0, read);
      totalRead += read;
      if (length > 0L)
      {
        bytesToRead = (int)Math.min(length - totalRead, buffer.length);
      }
    }
    return totalRead;
  }
  



















  @Deprecated
  public static void copy(Reader input, OutputStream output)
    throws IOException
  {
    copy(input, output, Charset.defaultCharset());
  }
  






















  public static void copy(Reader input, OutputStream output, Charset outputEncoding)
    throws IOException
  {
    OutputStreamWriter out = new OutputStreamWriter(output, Charsets.toCharset(outputEncoding));
    copy(input, out);
    

    out.flush();
  }
  

























  public static void copy(Reader input, OutputStream output, String outputEncoding)
    throws IOException
  {
    copy(input, output, Charsets.toCharset(outputEncoding));
  }
  
















  public static boolean contentEquals(InputStream input1, InputStream input2)
    throws IOException
  {
    if (input1 == input2) {
      return true;
    }
    if (!(input1 instanceof BufferedInputStream)) {
      input1 = new BufferedInputStream(input1);
    }
    if (!(input2 instanceof BufferedInputStream)) {
      input2 = new BufferedInputStream(input2);
    }
    
    int ch = input1.read();
    while (-1 != ch) {
      int ch2 = input2.read();
      if (ch != ch2) {
        return false;
      }
      ch = input1.read();
    }
    
    int ch2 = input2.read();
    return ch2 == -1;
  }
  














  public static boolean contentEquals(Reader input1, Reader input2)
    throws IOException
  {
    if (input1 == input2) {
      return true;
    }
    
    input1 = toBufferedReader(input1);
    input2 = toBufferedReader(input2);
    
    int ch = input1.read();
    while (-1 != ch) {
      int ch2 = input2.read();
      if (ch != ch2) {
        return false;
      }
      ch = input1.read();
    }
    
    int ch2 = input2.read();
    return ch2 == -1;
  }
  













  public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2)
    throws IOException
  {
    if (input1 == input2) {
      return true;
    }
    BufferedReader br1 = toBufferedReader(input1);
    BufferedReader br2 = toBufferedReader(input2);
    
    String line1 = br1.readLine();
    String line2 = br2.readLine();
    while ((line1 != null) && (line2 != null) && (line1.equals(line2))) {
      line1 = br1.readLine();
      line2 = br2.readLine();
    }
    return line1 == null ? false : line2 == null ? true : line1.equals(line2);
  }
  



















  public static long skip(InputStream input, long toSkip)
    throws IOException
  {
    if (toSkip < 0L) {
      throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
    }
    




    if (SKIP_BYTE_BUFFER == null) {
      SKIP_BYTE_BUFFER = new byte['ࠀ'];
    }
    long remain = toSkip;
    while (remain > 0L)
    {
      long n = input.read(SKIP_BYTE_BUFFER, 0, (int)Math.min(remain, 2048L));
      if (n < 0L) {
        break;
      }
      remain -= n;
    }
    return toSkip - remain;
  }
  










  public static long skip(ReadableByteChannel input, long toSkip)
    throws IOException
  {
    if (toSkip < 0L) {
      throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
    }
    ByteBuffer skipByteBuffer = ByteBuffer.allocate((int)Math.min(toSkip, 2048L));
    long remain = toSkip;
    while (remain > 0L) {
      skipByteBuffer.position(0);
      skipByteBuffer.limit((int)Math.min(remain, 2048L));
      int n = input.read(skipByteBuffer);
      if (n == -1) {
        break;
      }
      remain -= n;
    }
    return toSkip - remain;
  }
  



















  public static long skip(Reader input, long toSkip)
    throws IOException
  {
    if (toSkip < 0L) {
      throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
    }
    




    if (SKIP_CHAR_BUFFER == null) {
      SKIP_CHAR_BUFFER = new char['ࠀ'];
    }
    long remain = toSkip;
    while (remain > 0L)
    {
      long n = input.read(SKIP_CHAR_BUFFER, 0, (int)Math.min(remain, 2048L));
      if (n < 0L) {
        break;
      }
      remain -= n;
    }
    return toSkip - remain;
  }
  

















  public static void skipFully(InputStream input, long toSkip)
    throws IOException
  {
    if (toSkip < 0L) {
      throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
    }
    long skipped = skip(input, toSkip);
    if (skipped != toSkip) {
      throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
    }
  }
  








  public static void skipFully(ReadableByteChannel input, long toSkip)
    throws IOException
  {
    if (toSkip < 0L) {
      throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
    }
    long skipped = skip(input, toSkip);
    if (skipped != toSkip) {
      throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
    }
  }
  

















  public static void skipFully(Reader input, long toSkip)
    throws IOException
  {
    long skipped = skip(input, toSkip);
    if (skipped != toSkip) {
      throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
    }
  }
  














  public static int read(Reader input, char[] buffer, int offset, int length)
    throws IOException
  {
    if (length < 0) {
      throw new IllegalArgumentException("Length must not be negative: " + length);
    }
    int remaining = length;
    while (remaining > 0) {
      int location = length - remaining;
      int count = input.read(buffer, offset + location, remaining);
      if (-1 == count) {
        break;
      }
      remaining -= count;
    }
    return length - remaining;
  }
  










  public static int read(Reader input, char[] buffer)
    throws IOException
  {
    return read(input, buffer, 0, buffer.length);
  }
  













  public static int read(InputStream input, byte[] buffer, int offset, int length)
    throws IOException
  {
    if (length < 0) {
      throw new IllegalArgumentException("Length must not be negative: " + length);
    }
    int remaining = length;
    while (remaining > 0) {
      int location = length - remaining;
      int count = input.read(buffer, offset + location, remaining);
      if (-1 == count) {
        break;
      }
      remaining -= count;
    }
    return length - remaining;
  }
  










  public static int read(InputStream input, byte[] buffer)
    throws IOException
  {
    return read(input, buffer, 0, buffer.length);
  }
  











  public static int read(ReadableByteChannel input, ByteBuffer buffer)
    throws IOException
  {
    int length = buffer.remaining();
    while (buffer.remaining() > 0) {
      int count = input.read(buffer);
      if (-1 == count) {
        break;
      }
    }
    return length - buffer.remaining();
  }
  














  public static void readFully(Reader input, char[] buffer, int offset, int length)
    throws IOException
  {
    int actual = read(input, buffer, offset, length);
    if (actual != length) {
      throw new EOFException("Length to read: " + length + " actual: " + actual);
    }
  }
  











  public static void readFully(Reader input, char[] buffer)
    throws IOException
  {
    readFully(input, buffer, 0, buffer.length);
  }
  














  public static void readFully(InputStream input, byte[] buffer, int offset, int length)
    throws IOException
  {
    int actual = read(input, buffer, offset, length);
    if (actual != length) {
      throw new EOFException("Length to read: " + length + " actual: " + actual);
    }
  }
  











  public static void readFully(InputStream input, byte[] buffer)
    throws IOException
  {
    readFully(input, buffer, 0, buffer.length);
  }
  












  public static byte[] readFully(InputStream input, int length)
    throws IOException
  {
    byte[] buffer = new byte[length];
    readFully(input, buffer, 0, buffer.length);
    return buffer;
  }
  










  public static void readFully(ReadableByteChannel input, ByteBuffer buffer)
    throws IOException
  {
    int expected = buffer.remaining();
    int actual = read(input, buffer);
    if (actual != expected) {
      throw new EOFException("Length to read: " + expected + " actual: " + actual);
    }
  }
  
  public IOUtils() {}
}
