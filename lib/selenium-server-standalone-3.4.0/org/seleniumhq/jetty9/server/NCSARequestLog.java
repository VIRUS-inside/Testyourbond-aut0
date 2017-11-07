package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.TimeZone;
import org.seleniumhq.jetty9.util.RolloverFileOutputStream;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.log.Logger;


























@ManagedObject("NCSA standard format request log")
public class NCSARequestLog
  extends AbstractNCSARequestLog
{
  private String _filename;
  private boolean _append;
  private int _retainDays;
  private boolean _closeOut;
  private String _filenameDateFormat = null;
  
  private transient OutputStream _out;
  
  private transient OutputStream _fileOut;
  
  private transient Writer _writer;
  

  public NCSARequestLog()
  {
    setExtended(true);
    _append = true;
    _retainDays = 31;
  }
  








  public NCSARequestLog(String filename)
  {
    setExtended(true);
    _append = true;
    _retainDays = 31;
    setFilename(filename);
  }
  









  public void setFilename(String filename)
  {
    if (filename != null)
    {
      filename = filename.trim();
      if (filename.length() == 0)
        filename = null;
    }
    _filename = filename;
  }
  






  @ManagedAttribute("file of log")
  public String getFilename()
  {
    return _filename;
  }
  








  public String getDatedFilename()
  {
    if ((_fileOut instanceof RolloverFileOutputStream))
      return ((RolloverFileOutputStream)_fileOut).getDatedFilename();
    return null;
  }
  


  protected boolean isEnabled()
  {
    return _fileOut != null;
  }
  






  public void setRetainDays(int retainDays)
  {
    _retainDays = retainDays;
  }
  






  @ManagedAttribute("number of days that log files are kept")
  public int getRetainDays()
  {
    return _retainDays;
  }
  







  public void setAppend(boolean append)
  {
    _append = append;
  }
  






  @ManagedAttribute("existing log files are appends to the new one")
  public boolean isAppend()
  {
    return _append;
  }
  







  public void setFilenameDateFormat(String logFileDateFormat)
  {
    _filenameDateFormat = logFileDateFormat;
  }
  






  public String getFilenameDateFormat()
  {
    return _filenameDateFormat;
  }
  

  public void write(String requestEntry)
    throws IOException
  {
    synchronized (this)
    {
      if (_writer == null)
        return;
      _writer.write(requestEntry);
      _writer.write(StringUtil.__LINE_SEPARATOR);
      _writer.flush();
    }
  }
  






  protected synchronized void doStart()
    throws Exception
  {
    if (_filename != null)
    {
      _fileOut = new RolloverFileOutputStream(_filename, _append, _retainDays, TimeZone.getTimeZone(getLogTimeZone()), _filenameDateFormat, null);
      _closeOut = true;
      LOG.info("Opened " + getDatedFilename(), new Object[0]);
    }
    else {
      _fileOut = System.err;
    }
    _out = _fileOut;
    
    synchronized (this)
    {
      _writer = new OutputStreamWriter(_out);
    }
    super.doStart();
  }
  






  protected void doStop()
    throws Exception
  {
    synchronized (this)
    {
      super.doStop();
      try
      {
        if (_writer != null) {
          _writer.flush();
        }
      }
      catch (IOException e) {
        LOG.ignore(e);
      }
      if ((_out != null) && (_closeOut)) {
        try
        {
          _out.close();
        }
        catch (IOException e)
        {
          LOG.ignore(e);
        }
      }
      _out = null;
      _fileOut = null;
      _closeOut = false;
      _writer = null;
    }
  }
}
