package org.eclipse.jetty.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;





































public class RolloverFileOutputStream
  extends FilterOutputStream
{
  private static Timer __rollover;
  static final String YYYY_MM_DD = "yyyy_mm_dd";
  static final String ROLLOVER_FILE_DATE_FORMAT = "yyyy_MM_dd";
  static final String ROLLOVER_FILE_BACKUP_FORMAT = "HHmmssSSS";
  static final int ROLLOVER_FILE_RETAIN_DAYS = 31;
  private RollTask _rollTask;
  private ZonedDateTime midnight;
  private SimpleDateFormat _fileBackupFormat;
  private SimpleDateFormat _fileDateFormat;
  private String _filename;
  private File _file;
  private boolean _append;
  private int _retainDays;
  
  public RolloverFileOutputStream(String filename)
    throws IOException
  {
    this(filename, true, 31);
  }
  







  public RolloverFileOutputStream(String filename, boolean append)
    throws IOException
  {
    this(filename, append, 31);
  }
  










  public RolloverFileOutputStream(String filename, boolean append, int retainDays)
    throws IOException
  {
    this(filename, append, retainDays, TimeZone.getDefault());
  }
  













  public RolloverFileOutputStream(String filename, boolean append, int retainDays, TimeZone zone)
    throws IOException
  {
    this(filename, append, retainDays, zone, null, null);
  }
  
















  public RolloverFileOutputStream(String filename, boolean append, int retainDays, TimeZone zone, String dateFormat, String backupFormat)
    throws IOException
  {
    super(null);
    
    if (dateFormat == null)
      dateFormat = "yyyy_MM_dd";
    _fileDateFormat = new SimpleDateFormat(dateFormat);
    
    if (backupFormat == null)
      backupFormat = "HHmmssSSS";
    _fileBackupFormat = new SimpleDateFormat(backupFormat);
    
    _fileBackupFormat.setTimeZone(zone);
    _fileDateFormat.setTimeZone(zone);
    
    if (filename != null)
    {
      filename = filename.trim();
      if (filename.length() == 0)
        filename = null;
    }
    if (filename == null) {
      throw new IllegalArgumentException("Invalid filename");
    }
    _filename = filename;
    _append = append;
    _retainDays = retainDays;
    setFile();
    
    synchronized (RolloverFileOutputStream.class)
    {
      if (__rollover == null) {
        __rollover = new Timer(RolloverFileOutputStream.class.getName(), true);
      }
      _rollTask = new RollTask(null);
      
      midnight = ZonedDateTime.now().toLocalDate().atStartOfDay(zone.toZoneId());
      
      scheduleNextRollover();
    }
  }
  




  private void scheduleNextRollover()
  {
    midnight = midnight.toLocalDate().plus(1L, ChronoUnit.DAYS).atStartOfDay(midnight.getZone());
    __rollover.schedule(_rollTask, midnight.toInstant().toEpochMilli());
  }
  

  public String getFilename()
  {
    return _filename;
  }
  

  public String getDatedFilename()
  {
    if (_file == null)
      return null;
    return _file.toString();
  }
  

  public int getRetainDays()
  {
    return _retainDays;
  }
  


  private synchronized void setFile()
    throws IOException
  {
    File file = new File(_filename);
    _filename = file.getCanonicalPath();
    file = new File(_filename);
    File dir = new File(file.getParent());
    if ((!dir.isDirectory()) || (!dir.canWrite())) {
      throw new IOException("Cannot write log directory " + dir);
    }
    Date now = new Date();
    

    String filename = file.getName();
    int i = filename.toLowerCase(Locale.ENGLISH).indexOf("yyyy_mm_dd");
    if (i >= 0)
    {



      file = new File(dir, filename.substring(0, i) + _fileDateFormat.format(now) + filename.substring(i + "yyyy_mm_dd".length()));
    }
    
    if ((file.exists()) && (!file.canWrite())) {
      throw new IOException("Cannot write log file " + file);
    }
    
    if ((out == null) || (!file.equals(_file)))
    {

      _file = file;
      if ((!_append) && (file.exists()))
        file.renameTo(new File(file.toString() + "." + _fileBackupFormat.format(now)));
      OutputStream oldOut = out;
      out = new FileOutputStream(file.toString(), _append);
      if (oldOut != null) {
        oldOut.close();
      }
    }
  }
  

  private void removeOldFiles()
  {
    if (_retainDays > 0)
    {
      ZonedDateTime now = ZonedDateTime.now(midnight.getZone());
      now.minus(_retainDays, ChronoUnit.DAYS);
      long expired = now.toInstant().toEpochMilli();
      
      File file = new File(_filename);
      File dir = new File(file.getParent());
      String fn = file.getName();
      int s = fn.toLowerCase(Locale.ENGLISH).indexOf("yyyy_mm_dd");
      if (s < 0)
        return;
      String prefix = fn.substring(0, s);
      String suffix = fn.substring(s + "yyyy_mm_dd".length());
      
      String[] logList = dir.list();
      for (int i = 0; i < logList.length; i++)
      {
        fn = logList[i];
        if ((fn.startsWith(prefix)) && (fn.indexOf(suffix, prefix.length()) >= 0))
        {
          File f = new File(dir, fn);
          if (f.lastModified() < expired)
          {
            f.delete();
          }
        }
      }
    }
  }
  


  public void write(byte[] buf)
    throws IOException
  {
    out.write(buf);
  }
  


  public void write(byte[] buf, int off, int len)
    throws IOException
  {
    out.write(buf, off, len);
  }
  


  public void close()
    throws IOException
  {
    synchronized (RolloverFileOutputStream.class) {
      try {
        super.close();
        

        out = null;
        _file = null;
      }
      finally
      {
        out = null;
        _file = null;
      }
    }
  }
  

  private class RollTask
    extends TimerTask
  {
    private RollTask() {}
    
    public void run()
    {
      try
      {
        RolloverFileOutputStream.this.setFile();
        RolloverFileOutputStream.this.scheduleNextRollover();
        RolloverFileOutputStream.this.removeOldFiles();

      }
      catch (IOException e)
      {
        e.printStackTrace(System.err);
      }
    }
  }
}
