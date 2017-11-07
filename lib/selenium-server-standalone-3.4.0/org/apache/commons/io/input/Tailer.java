package org.apache.commons.io.input;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

















































































































public class Tailer
  implements Runnable
{
  private static final int DEFAULT_DELAY_MILLIS = 1000;
  private static final String RAF_MODE = "r";
  private static final int DEFAULT_BUFSIZE = 4096;
  private static final Charset DEFAULT_CHARSET = ;
  



  private final byte[] inbuf;
  



  private final File file;
  



  private final Charset cset;
  



  private final long delayMillis;
  



  private final boolean end;
  



  private final TailerListener listener;
  



  private final boolean reOpen;
  



  private volatile boolean run = true;
  




  public Tailer(File file, TailerListener listener)
  {
    this(file, listener, 1000L);
  }
  





  public Tailer(File file, TailerListener listener, long delayMillis)
  {
    this(file, listener, delayMillis, false);
  }
  






  public Tailer(File file, TailerListener listener, long delayMillis, boolean end)
  {
    this(file, listener, delayMillis, end, 4096);
  }
  








  public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen)
  {
    this(file, listener, delayMillis, end, reOpen, 4096);
  }
  








  public Tailer(File file, TailerListener listener, long delayMillis, boolean end, int bufSize)
  {
    this(file, listener, delayMillis, end, false, bufSize);
  }
  









  public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize)
  {
    this(file, DEFAULT_CHARSET, listener, delayMillis, end, reOpen, bufSize);
  }
  











  public Tailer(File file, Charset cset, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize)
  {
    this.file = file;
    this.delayMillis = delayMillis;
    this.end = end;
    
    inbuf = new byte[bufSize];
    

    this.listener = listener;
    listener.init(this);
    this.reOpen = reOpen;
    this.cset = cset;
  }
  










  public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, int bufSize)
  {
    return create(file, listener, delayMillis, end, false, bufSize);
  }
  












  public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize)
  {
    return create(file, DEFAULT_CHARSET, listener, delayMillis, end, reOpen, bufSize);
  }
  













  public static Tailer create(File file, Charset charset, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize)
  {
    Tailer tailer = new Tailer(file, charset, listener, delayMillis, end, reOpen, bufSize);
    Thread thread = new Thread(tailer);
    thread.setDaemon(true);
    thread.start();
    return tailer;
  }
  









  public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end)
  {
    return create(file, listener, delayMillis, end, 4096);
  }
  










  public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen)
  {
    return create(file, listener, delayMillis, end, reOpen, 4096);
  }
  







  public static Tailer create(File file, TailerListener listener, long delayMillis)
  {
    return create(file, listener, delayMillis, false);
  }
  







  public static Tailer create(File file, TailerListener listener)
  {
    return create(file, listener, 1000L, false);
  }
  




  public File getFile()
  {
    return file;
  }
  





  protected boolean getRun()
  {
    return run;
  }
  




  public long getDelay()
  {
    return delayMillis;
  }
  


  public void run()
  {
    RandomAccessFile reader = null;
    try {
      long last = 0L;
      long position = 0L;
      
      while ((getRun()) && (reader == null)) {
        try {
          reader = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
          listener.fileNotFound();
        }
        if (reader == null) {
          Thread.sleep(delayMillis);
        }
        else {
          position = end ? file.length() : 0L;
          last = file.lastModified();
          reader.seek(position);
        }
      }
      while (getRun()) {
        boolean newer = FileUtils.isFileNewer(file, last);
        
        long length = file.length();
        if (length < position)
        {
          listener.fileRotated();
          
          try
          {
            save = reader;
            reader = new RandomAccessFile(file, "r");
            
            try
            {
              readLines(save);
            } catch (IOException ioe) {
              listener.handle(ioe);
            }
            position = 0L;
          }
          catch (FileNotFoundException e)
          {
            RandomAccessFile save;
            listener.fileNotFound();
          }
          
        }
        else
        {
          if (length > position)
          {
            position = readLines(reader);
            last = file.lastModified();
          } else if (newer)
          {



            position = 0L;
            reader.seek(position);
            

            position = readLines(reader);
            last = file.lastModified();
          }
          
          if (reOpen) {
            IOUtils.closeQuietly(reader);
          }
          Thread.sleep(delayMillis);
          if ((getRun()) && (reOpen)) {
            reader = new RandomAccessFile(file, "r");
            reader.seek(position);
          }
        }
      }
    } catch (InterruptedException e) { Thread.currentThread().interrupt();
      stop(e);
    } catch (Exception e) {
      stop(e);
    } finally {
      IOUtils.closeQuietly(reader);
    }
  }
  



  private void stop(Exception e)
  {
    listener.handle(e);
    stop();
  }
  


  public void stop()
  {
    run = false;
  }
  





  private long readLines(RandomAccessFile reader)
    throws IOException
  {
    ByteArrayOutputStream lineBuf = new ByteArrayOutputStream(64);
    long pos = reader.getFilePointer();
    long rePos = pos;
    
    boolean seenCR = false;
    int num; while ((getRun()) && ((num = reader.read(inbuf)) != -1)) {
      for (int i = 0; i < num; i++) {
        byte ch = inbuf[i];
        switch (ch) {
        case 10: 
          seenCR = false;
          listener.handle(new String(lineBuf.toByteArray(), cset));
          lineBuf.reset();
          rePos = pos + i + 1L;
          break;
        case 13: 
          if (seenCR) {
            lineBuf.write(13);
          }
          seenCR = true;
          break;
        default: 
          if (seenCR) {
            seenCR = false;
            listener.handle(new String(lineBuf.toByteArray(), cset));
            lineBuf.reset();
            rePos = pos + i + 1L;
          }
          lineBuf.write(ch);
        }
      }
      pos = reader.getFilePointer();
    }
    IOUtils.closeQuietly(lineBuf);
    reader.seek(rePos);
    
    if ((listener instanceof TailerListenerAdapter)) {
      ((TailerListenerAdapter)listener).endOfFileReached();
    }
    
    return rePos;
  }
}
