package org.eclipse.jetty.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Part;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;




























public class MultiPartInputStreamParser
{
  private static final Logger LOG = Log.getLogger(MultiPartInputStreamParser.class);
  public static final MultipartConfigElement __DEFAULT_MULTIPART_CONFIG = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
  public static final MultiMap<Part> EMPTY_MAP = new MultiMap(Collections.emptyMap());
  
  protected InputStream _in;
  protected MultipartConfigElement _config;
  protected String _contentType;
  protected MultiMap<Part> _parts;
  protected Exception _err;
  protected File _tmpDir;
  protected File _contextTmpDir;
  protected boolean _deleteOnExit;
  protected boolean _writeFilesWithFilenames;
  
  public class MultiPart
    implements Part
  {
    protected String _name;
    protected String _filename;
    protected File _file;
    protected OutputStream _out;
    protected ByteArrayOutputStream2 _bout;
    protected String _contentType;
    protected MultiMap<String> _headers;
    protected long _size = 0L;
    protected boolean _temporary = true;
    
    public MultiPart(String name, String filename)
      throws IOException
    {
      _name = name;
      _filename = filename;
    }
    

    public String toString()
    {
      return String.format("Part{n=%s,fn=%s,ct=%s,s=%d,t=%b,f=%s}", new Object[] { _name, _filename, _contentType, Long.valueOf(_size), Boolean.valueOf(_temporary), _file });
    }
    
    protected void setContentType(String contentType) {
      _contentType = contentType;
    }
    




    protected void open()
      throws IOException
    {
      if ((isWriteFilesWithFilenames()) && (_filename != null) && (_filename.trim().length() > 0))
      {
        createFile();

      }
      else
      {

        _out = (this._bout = new ByteArrayOutputStream2());
      }
    }
    
    protected void close()
      throws IOException
    {
      _out.close();
    }
    

    protected void write(int b)
      throws IOException
    {
      if ((_config.getMaxFileSize() > 0L) && (_size + 1L > _config.getMaxFileSize())) {
        throw new IllegalStateException("Multipart Mime part " + _name + " exceeds max filesize");
      }
      if ((_config.getFileSizeThreshold() > 0) && (_size + 1L > _config.getFileSizeThreshold()) && (_file == null)) {
        createFile();
      }
      _out.write(b);
      _size += 1L;
    }
    
    protected void write(byte[] bytes, int offset, int length)
      throws IOException
    {
      if ((_config.getMaxFileSize() > 0L) && (_size + length > _config.getMaxFileSize())) {
        throw new IllegalStateException("Multipart Mime part " + _name + " exceeds max filesize");
      }
      if ((_config.getFileSizeThreshold() > 0) && (_size + length > _config.getFileSizeThreshold()) && (_file == null)) {
        createFile();
      }
      _out.write(bytes, offset, length);
      _size += length;
    }
    


    protected void createFile()
      throws IOException
    {
      boolean USER = true;
      boolean WORLD = false;
      
      _file = File.createTempFile("MultiPart", "", _tmpDir);
      _file.setReadable(false, false);
      _file.setReadable(true, true);
      
      if (_deleteOnExit)
        _file.deleteOnExit();
      FileOutputStream fos = new FileOutputStream(_file);
      BufferedOutputStream bos = new BufferedOutputStream(fos);
      
      if ((_size > 0L) && (_out != null))
      {

        _out.flush();
        _bout.writeTo(bos);
        _out.close();
        _bout = null;
      }
      _out = bos;
    }
    


    protected void setHeaders(MultiMap<String> headers)
    {
      _headers = headers;
    }
    



    public String getContentType()
    {
      return _contentType;
    }
    



    public String getHeader(String name)
    {
      if (name == null)
        return null;
      return (String)_headers.getValue(name.toLowerCase(Locale.ENGLISH), 0);
    }
    



    public Collection<String> getHeaderNames()
    {
      return _headers.keySet();
    }
    



    public Collection<String> getHeaders(String name)
    {
      return _headers.getValues(name);
    }
    


    public InputStream getInputStream()
      throws IOException
    {
      if (_file != null)
      {

        return new BufferedInputStream(new FileInputStream(_file));
      }
      


      return new ByteArrayInputStream(_bout.getBuf(), 0, _bout.size());
    }
    






    public String getSubmittedFileName()
    {
      return getContentDispositionFilename();
    }
    
    public byte[] getBytes()
    {
      if (_bout != null)
        return _bout.toByteArray();
      return null;
    }
    



    public String getName()
    {
      return _name;
    }
    



    public long getSize()
    {
      return _size;
    }
    

    public void write(String fileName)
      throws IOException
    {
      BufferedOutputStream bos;
      if (_file == null)
      {
        _temporary = false;
        

        _file = new File(_tmpDir, fileName);
        
        bos = null;
      }
      Path src;
      try { bos = new BufferedOutputStream(new FileOutputStream(_file));
        _bout.writeTo(bos);
        bos.flush();
        


        if (bos != null)
          bos.close();
        _bout = null;
      }
      finally
      {
        if (bos != null)
          bos.close();
        _bout = null;
      }
      






      Path target = src.resolveSibling(fileName);
      Files.move(src, target, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
      _file = target.toFile();
    }
    





    public void delete()
      throws IOException
    {
      if ((_file != null) && (_file.exists())) {
        _file.delete();
      }
    }
    



    public void cleanUp()
      throws IOException
    {
      if ((_temporary) && (_file != null) && (_file.exists())) {
        _file.delete();
      }
    }
    




    public File getFile()
    {
      return _file;
    }
    





    public String getContentDispositionFilename()
    {
      return _filename;
    }
  }
  









  public MultiPartInputStreamParser(InputStream in, String contentType, MultipartConfigElement config, File contextTmpDir)
  {
    _contentType = contentType;
    _config = config;
    _contextTmpDir = contextTmpDir;
    if (_contextTmpDir == null) {
      _contextTmpDir = new File(System.getProperty("java.io.tmpdir"));
    }
    if (_config == null) {
      _config = new MultipartConfigElement(_contextTmpDir.getAbsolutePath());
    }
    if ((in instanceof ServletInputStream))
    {
      if (((ServletInputStream)in).isFinished())
      {
        _parts = EMPTY_MAP;
        return;
      }
    }
    _in = new ReadLineInputStream(in);
  }
  




  public Collection<Part> getParsedParts()
  {
    if (_parts == null) {
      return Collections.emptyList();
    }
    Collection<List<Part>> values = _parts.values();
    List<Part> parts = new ArrayList();
    for (List<Part> o : values)
    {
      List<Part> asList = LazyList.getList(o, false);
      parts.addAll(asList);
    }
    return parts;
  }
  





  public void deleteParts()
    throws MultiException
  {
    Collection<Part> parts = getParsedParts();
    MultiException err = new MultiException();
    for (Part p : parts)
    {
      try
      {
        ((MultiPart)p).cleanUp();
      }
      catch (Exception e)
      {
        err.add(e);
      }
    }
    _parts.clear();
    
    err.ifExceptionThrowMulti();
  }
  







  public Collection<Part> getParts()
    throws IOException
  {
    parse();
    throwIfError();
    

    Collection<List<Part>> values = _parts.values();
    List<Part> parts = new ArrayList();
    for (List<Part> o : values)
    {
      List<Part> asList = LazyList.getList(o, false);
      parts.addAll(asList);
    }
    return parts;
  }
  








  public Part getPart(String name)
    throws IOException
  {
    parse();
    throwIfError();
    return (Part)_parts.getValue(name, 0);
  }
  





  protected void throwIfError()
    throws IOException
  {
    if (_err != null)
    {
      if ((_err instanceof IOException))
        throw ((IOException)_err);
      if ((_err instanceof IllegalStateException))
        throw ((IllegalStateException)_err);
      throw new IllegalStateException(_err);
    }
  }
  





  protected void parse()
  {
    if ((_parts != null) || (_err != null)) {
      return;
    }
    

    long total = 0L;
    _parts = new MultiMap();
    

    if ((_contentType == null) || (!_contentType.startsWith("multipart/form-data"))) {
      return;
    }
    

    try
    {
      if (_config.getLocation() == null) {
        _tmpDir = _contextTmpDir;
      } else if ("".equals(_config.getLocation())) {
        _tmpDir = _contextTmpDir;
      }
      else {
        File f = new File(_config.getLocation());
        if (f.isAbsolute()) {
          _tmpDir = f;
        } else {
          _tmpDir = new File(_contextTmpDir, _config.getLocation());
        }
      }
      if (!_tmpDir.exists()) {
        _tmpDir.mkdirs();
      }
      String contentTypeBoundary = "";
      int bstart = _contentType.indexOf("boundary=");
      if (bstart >= 0)
      {
        int bend = _contentType.indexOf(";", bstart);
        bend = bend < 0 ? _contentType.length() : bend;
        contentTypeBoundary = QuotedStringTokenizer.unquote(value(_contentType.substring(bstart, bend)).trim());
      }
      
      String boundary = "--" + contentTypeBoundary;
      String lastBoundary = boundary + "--";
      byte[] byteBoundary = lastBoundary.getBytes(StandardCharsets.ISO_8859_1);
      

      String line = null;
      try
      {
        line = ((ReadLineInputStream)_in).readLine();
      }
      catch (IOException e)
      {
        LOG.warn("Badly formatted multipart request", new Object[0]);
        throw e;
      }
      
      if (line == null) {
        throw new IOException("Missing content for multipart request");
      }
      boolean badFormatLogged = false;
      line = line.trim();
      while ((line != null) && (!line.equals(boundary)) && (!line.equals(lastBoundary)))
      {
        if (!badFormatLogged)
        {
          LOG.warn("Badly formatted multipart request", new Object[0]);
          badFormatLogged = true;
        }
        line = ((ReadLineInputStream)_in).readLine();
        line = line == null ? line : line.trim();
      }
      
      if ((line == null) || (line.length() == 0)) {
        throw new IOException("Missing initial multi part boundary");
      }
      
      if (line.equals(lastBoundary)) {
        return;
      }
      
      boolean lastPart = false;
      
      while (!lastPart)
      {
        String contentDisposition = null;
        String contentType = null;
        String contentTransferEncoding = null;
        
        MultiMap<String> headers = new MultiMap();
        for (;;)
        {
          line = ((ReadLineInputStream)_in).readLine();
          

          if (line == null) {
            break label1402;
          }
          
          if ("".equals(line)) {
            break;
          }
          total += line.length();
          if ((_config.getMaxRequestSize() > 0L) && (total > _config.getMaxRequestSize())) {
            throw new IllegalStateException("Request exceeds maxRequestSize (" + _config.getMaxRequestSize() + ")");
          }
          
          int c = line.indexOf(':', 0);
          if (c > 0)
          {
            String key = line.substring(0, c).trim().toLowerCase(Locale.ENGLISH);
            String value = line.substring(c + 1, line.length()).trim();
            headers.put(key, value);
            if (key.equalsIgnoreCase("content-disposition"))
              contentDisposition = value;
            if (key.equalsIgnoreCase("content-type"))
              contentType = value;
            if (key.equals("content-transfer-encoding")) {
              contentTransferEncoding = value;
            }
          }
        }
        
        boolean form_data = false;
        if (contentDisposition == null)
        {
          throw new IOException("Missing content-disposition");
        }
        
        QuotedStringTokenizer tok = new QuotedStringTokenizer(contentDisposition, ";", false, true);
        String name = null;
        String filename = null;
        while (tok.hasMoreTokens())
        {
          String t = tok.nextToken().trim();
          String tl = t.toLowerCase(Locale.ENGLISH);
          if (t.startsWith("form-data")) {
            form_data = true;
          } else if (tl.startsWith("name=")) {
            name = value(t);
          } else if (tl.startsWith("filename=")) {
            filename = filenameValue(t);
          }
        }
        
        if ((form_data) && 
        







          (name != null))
        {




          MultiPart part = new MultiPart(name, filename);
          part.setHeaders(headers);
          part.setContentType(contentType);
          _parts.add(name, part);
          part.open();
          
          InputStream partInput = null;
          if ("base64".equalsIgnoreCase(contentTransferEncoding))
          {
            partInput = new Base64InputStream((ReadLineInputStream)_in);
          }
          else if ("quoted-printable".equalsIgnoreCase(contentTransferEncoding))
          {
            partInput = new FilterInputStream(_in)
            {
              public int read()
                throws IOException
              {
                int c = in.read();
                if ((c >= 0) && (c == 61))
                {
                  int hi = in.read();
                  int lo = in.read();
                  if ((hi < 0) || (lo < 0))
                  {
                    throw new IOException("Unexpected end to quoted-printable byte");
                  }
                  char[] chars = { (char)hi, (char)lo };
                  c = Integer.parseInt(new String(chars), 16);
                }
                return c;
              }
              
            };
          } else {
            partInput = _in;
          }
          
          try
          {
            int state = -2;
            
            boolean cr = false;
            boolean lf = false;
            

            for (;;)
            {
              int b = 0;
              int c; while ((c = state != -2 ? state : partInput.read()) != -1)
              {
                total += 1L;
                if ((_config.getMaxRequestSize() > 0L) && (total > _config.getMaxRequestSize())) {
                  throw new IllegalStateException("Request exceeds maxRequestSize (" + _config.getMaxRequestSize() + ")");
                }
                state = -2;
                

                if ((c == 13) || (c == 10))
                {
                  if (c != 13)
                    break;
                  partInput.mark(1);
                  int tmp = partInput.read();
                  if (tmp != 10) {
                    partInput.reset();
                  } else
                    state = tmp;
                  break;
                }
                


                if ((b >= 0) && (b < byteBoundary.length) && (c == byteBoundary[b]))
                {
                  b++;

                }
                else
                {

                  if (cr) {
                    part.write(13);
                  }
                  if (lf) {
                    part.write(10);
                  }
                  cr = lf = 0;
                  if (b > 0) {
                    part.write(byteBoundary, 0, b);
                  }
                  b = -1;
                  part.write(c);
                }
              }
              

              if (((b > 0) && (b < byteBoundary.length - 2)) || (b == byteBoundary.length - 1))
              {
                if (cr) {
                  part.write(13);
                }
                if (lf) {
                  part.write(10);
                }
                cr = lf = 0;
                part.write(byteBoundary, 0, b);
                b = -1;
              }
              

              if ((b > 0) || (c == -1))
              {

                if (b == byteBoundary.length)
                  lastPart = true;
                if (state != 10) break;
                state = -2; break;
              }
              


              if (cr) {
                part.write(13);
              }
              if (lf) {
                part.write(10);
              }
              cr = c == 13;
              lf = (c == 10) || (state == 10);
              if (state == 10) {
                state = -2;
              }
            }
          }
          finally {
            part.close();
          } } }
      label1402:
      if (lastPart)
      {
        while (line != null) {
          line = ((ReadLineInputStream)_in).readLine();
        }
      }
      throw new IOException("Incomplete parts");
    }
    catch (Exception e)
    {
      _err = e;
    }
  }
  
  public void setDeleteOnExit(boolean deleteOnExit)
  {
    _deleteOnExit = deleteOnExit;
  }
  
  public void setWriteFilesWithFilenames(boolean writeFilesWithFilenames)
  {
    _writeFilesWithFilenames = writeFilesWithFilenames;
  }
  
  public boolean isWriteFilesWithFilenames()
  {
    return _writeFilesWithFilenames;
  }
  
  public boolean isDeleteOnExit()
  {
    return _deleteOnExit;
  }
  


  private String value(String nameEqualsValue)
  {
    int idx = nameEqualsValue.indexOf('=');
    String value = nameEqualsValue.substring(idx + 1).trim();
    return QuotedStringTokenizer.unquoteOnly(value);
  }
  


  private String filenameValue(String nameEqualsValue)
  {
    int idx = nameEqualsValue.indexOf('=');
    String value = nameEqualsValue.substring(idx + 1).trim();
    
    if (value.matches(".??[a-z,A-Z]\\:\\\\[^\\\\].*"))
    {


      char first = value.charAt(0);
      if ((first == '"') || (first == '\''))
        value = value.substring(1);
      char last = value.charAt(value.length() - 1);
      if ((last == '"') || (last == '\'')) {
        value = value.substring(0, value.length() - 1);
      }
      return value;
    }
    




    return QuotedStringTokenizer.unquoteOnly(value, true);
  }
  

  private static class Base64InputStream
    extends InputStream
  {
    ReadLineInputStream _in;
    
    String _line;
    byte[] _buffer;
    int _pos;
    
    public Base64InputStream(ReadLineInputStream rlis)
    {
      _in = rlis;
    }
    
    public int read()
      throws IOException
    {
      if ((_buffer == null) || (_pos >= _buffer.length))
      {




        _line = _in.readLine();
        if (_line == null)
          return -1;
        if (_line.startsWith("--")) {
          _buffer = (_line + "\r\n").getBytes();
        } else if (_line.length() == 0) {
          _buffer = "\r\n".getBytes();
        }
        else {
          ByteArrayOutputStream baos = new ByteArrayOutputStream(4 * _line.length() / 3 + 2);
          B64Code.decode(_line, baos);
          baos.write(13);
          baos.write(10);
          _buffer = baos.toByteArray();
        }
        
        _pos = 0;
      }
      
      return _buffer[(_pos++)];
    }
  }
}
