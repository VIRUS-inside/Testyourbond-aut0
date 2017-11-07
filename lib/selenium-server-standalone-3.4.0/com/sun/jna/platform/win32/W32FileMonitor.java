package com.sun.jna.platform.win32;

import com.sun.jna.platform.FileMonitor;
import com.sun.jna.platform.FileMonitor.FileEvent;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;










public class W32FileMonitor
  extends FileMonitor
{
  private static final int BUFFER_SIZE = 4096;
  private Thread watcher;
  private WinNT.HANDLE port;
  public W32FileMonitor() {}
  
  private class FileInfo
  {
    public final File file;
    public final WinNT.HANDLE handle;
    public final int notifyMask;
    public final boolean recursive;
    public final WinNT.FILE_NOTIFY_INFORMATION info = new WinNT.FILE_NOTIFY_INFORMATION(4096);
    public final IntByReference infoLength = new IntByReference();
    public final WinBase.OVERLAPPED overlapped = new WinBase.OVERLAPPED();
    
    public FileInfo(File f, WinNT.HANDLE h, int mask, boolean recurse) { file = f;
      handle = h;
      notifyMask = mask;
      recursive = recurse;
    }
  }
  

  private final Map<File, FileInfo> fileMap = new HashMap();
  private final Map<WinNT.HANDLE, FileInfo> handleMap = new HashMap();
  private boolean disposing = false;
  private static int watcherThreadID;
  
  private void handleChanges(FileInfo finfo) throws IOException { Kernel32 klib = Kernel32.INSTANCE;
    WinNT.FILE_NOTIFY_INFORMATION fni = info;
    
    fni.read();
    do {
      FileMonitor.FileEvent event = null;
      File file = new File(file, fni.getFilename());
      switch (Action) {
      case 0: 
        break;
      case 3: 
        event = new FileMonitor.FileEvent(this, file, 4);
        break;
      case 1: 
        event = new FileMonitor.FileEvent(this, file, 1);
        break;
      case 2: 
        event = new FileMonitor.FileEvent(this, file, 2);
        break;
      case 4: 
        event = new FileMonitor.FileEvent(this, file, 16);
        break;
      case 5: 
        event = new FileMonitor.FileEvent(this, file, 32);
        break;
      
      default: 
        System.err.println("Unrecognized file action '" + Action + "'");
      }
      
      if (event != null) {
        notify(event);
      }
      
      fni = fni.next();
    } while (fni != null);
    

    if (!file.exists()) {
      unwatch(file);
      return;
    }
    
    if (!klib.ReadDirectoryChangesW(handle, info, info.size(), recursive, notifyMask, infoLength, overlapped, null))
    {

      if (!disposing) {
        int err = klib.GetLastError();
        throw new IOException("ReadDirectoryChangesW failed on " + file + ": '" + Kernel32Util.formatMessageFromLastErrorCode(err) + "' (" + err + ")");
      }
    }
  }
  


  private FileInfo waitForChange()
  {
    IntByReference rcount = new IntByReference();
    BaseTSD.ULONG_PTRByReference rkey = new BaseTSD.ULONG_PTRByReference();
    PointerByReference roverlap = new PointerByReference();
    if (!Kernel32.INSTANCE.GetQueuedCompletionStatus(port, rcount, rkey, roverlap, -1)) {
      return null;
    }
    synchronized (this) {
      return (FileInfo)handleMap.get(new WinNT.HANDLE(rkey.getValue().toPointer()));
    }
  }
  
  private int convertMask(int mask) {
    int result = 0;
    if ((mask & 0x1) != 0) {
      result |= 0x40;
    }
    if ((mask & 0x2) != 0) {
      result |= 0x3;
    }
    if ((mask & 0x4) != 0) {
      result |= 0x10;
    }
    if ((mask & 0x30) != 0) {
      result |= 0x3;
    }
    if ((mask & 0x40) != 0) {
      result |= 0x8;
    }
    if ((mask & 0x8) != 0) {
      result |= 0x20;
    }
    if ((mask & 0x80) != 0) {
      result |= 0x4;
    }
    if ((mask & 0x100) != 0) {
      result |= 0x100;
    }
    return result;
  }
  
  protected synchronized void watch(File file, int eventMask, boolean recursive)
    throws IOException
  {
    File dir = file;
    if (!dir.isDirectory()) {
      recursive = false;
      dir = file.getParentFile();
    }
    while ((dir != null) && (!dir.exists())) {
      recursive = true;
      dir = dir.getParentFile();
    }
    if (dir == null) {
      throw new FileNotFoundException("No ancestor found for " + file);
    }
    Kernel32 klib = Kernel32.INSTANCE;
    int mask = 7;
    
    int flags = 1107296256;
    
    WinNT.HANDLE handle = klib.CreateFile(file.getAbsolutePath(), 1, mask, null, 3, flags, null);
    


    if (WinBase.INVALID_HANDLE_VALUE.equals(handle)) {
      throw new IOException("Unable to open " + file + " (" + klib.GetLastError() + ")");
    }
    
    int notifyMask = convertMask(eventMask);
    FileInfo finfo = new FileInfo(file, handle, notifyMask, recursive);
    fileMap.put(file, finfo);
    handleMap.put(handle, finfo);
    
    port = klib.CreateIoCompletionPort(handle, port, handle.getPointer(), 0);
    if (WinBase.INVALID_HANDLE_VALUE.equals(port)) {
      throw new IOException("Unable to create/use I/O Completion port for " + file + " (" + klib.GetLastError() + ")");
    }
    



    if (!klib.ReadDirectoryChangesW(handle, info, info.size(), recursive, notifyMask, infoLength, overlapped, null))
    {

      int err = klib.GetLastError();
      throw new IOException("ReadDirectoryChangesW failed on " + file + ", handle " + handle + ": '" + Kernel32Util.formatMessageFromLastErrorCode(err) + "' (" + err + ")");
    }
    


    if (watcher == null) {
      watcher = new Thread("W32 File Monitor-" + watcherThreadID++)
      {
        public void run() {
          for (;;) {
            W32FileMonitor.FileInfo finfo = W32FileMonitor.this.waitForChange();
            if (finfo == null) {
              synchronized (W32FileMonitor.this) {
                if (fileMap.isEmpty()) {
                  watcher = null;
                  break;
                }
                
              }
            } else
              try
              {
                W32FileMonitor.this.handleChanges(finfo);
              }
              catch (IOException e)
              {
                e.printStackTrace();
              }
          }
        }
      };
      watcher.setDaemon(true);
      watcher.start();
    }
  }
  
  protected synchronized void unwatch(File file) {
    FileInfo finfo = (FileInfo)fileMap.remove(file);
    if (finfo != null) {
      handleMap.remove(handle);
      Kernel32 klib = Kernel32.INSTANCE;
      
      klib.CloseHandle(handle);
    }
  }
  
  public synchronized void dispose() {
    disposing = true;
    

    int i = 0;
    for (Object[] keys = fileMap.keySet().toArray(); !fileMap.isEmpty();) {
      unwatch((File)keys[(i++)]);
    }
    
    Kernel32 klib = Kernel32.INSTANCE;
    klib.PostQueuedCompletionStatus(port, 0, null, null);
    klib.CloseHandle(port);
    port = null;
    watcher = null;
  }
}
