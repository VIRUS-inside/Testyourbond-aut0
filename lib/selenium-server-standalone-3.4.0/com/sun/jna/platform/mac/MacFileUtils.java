package com.sun.jna.platform.mac;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.FileUtils;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;












public class MacFileUtils
  extends FileUtils
{
  public MacFileUtils() {}
  
  public boolean hasTrash() { return true; }
  
  public static abstract interface FileManager extends Library { public abstract int FSRefMakePath(FSRef paramFSRef, byte[] paramArrayOfByte, int paramInt);
    
    public static final FileManager INSTANCE = (FileManager)Native.loadLibrary("CoreServices", FileManager.class);
    public static final int kFSFileOperationDefaultOptions = 0;
    public static final int kFSFileOperationsOverwrite = 1;
    public static final int kFSFileOperationsSkipSourcePermissionErrors = 2;
    public static final int kFSFileOperationsDoNotMoveAcrossVolumes = 4;
    public static final int kFSFileOperationsSkipPreflight = 8;
    public static final int kFSPathDefaultOptions = 0;
    public static final int kFSPathMakeRefDoNotFollowLeafSymlink = 1;
    
    public static class FSRef extends Structure {
      public FSRef() {}
      
      public byte[] hidden = new byte[80];
      protected List getFieldOrder() { return Arrays.asList(new String[] { "hidden" }); }
    }
    
    public abstract int FSPathMakeRef(String paramString, int paramInt, ByteByReference paramByteByReference);
    
    public abstract int FSPathMakeRefWithOptions(String paramString, int paramInt, FSRef paramFSRef, ByteByReference paramByteByReference);
    
    public abstract int FSPathMoveObjectToTrashSync(String paramString, PointerByReference paramPointerByReference, int paramInt);
    
    public abstract int FSMoveObjectToTrashSync(FSRef paramFSRef1, FSRef paramFSRef2, int paramInt);
  }
  
  public void moveToTrash(File[] files) throws IOException {
    File home = new File(System.getProperty("user.home"));
    File trash = new File(home, ".Trash");
    if (!trash.exists()) {
      throw new IOException("The Trash was not found in its expected location (" + trash + ")");
    }
    List<String> failed = new ArrayList();
    for (int i = 0; i < files.length; i++) {
      File src = files[i];
      MacFileUtils.FileManager.FSRef fsref = new MacFileUtils.FileManager.FSRef();
      int status = FileManager.INSTANCE.FSPathMakeRefWithOptions(src.getAbsolutePath(), 1, fsref, null);
      

      if (status != 0) {
        failed.add(src + " (FSRef: " + status + ")");
      }
      else {
        status = FileManager.INSTANCE.FSMoveObjectToTrashSync(fsref, null, 0);
        if (status != 0)
          failed.add(src + " (" + status + ")");
      }
    }
    if (failed.size() > 0) {
      throw new IOException("The following files could not be trashed: " + failed);
    }
  }
}
