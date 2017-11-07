package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.WString;
import com.sun.jna.win32.StdCallLibrary;
import java.util.Arrays;
import java.util.List;







































































































public abstract interface Sspi
  extends StdCallLibrary
{
  public static final int MAX_TOKEN_SIZE = 12288;
  public static final int SECPKG_CRED_INBOUND = 1;
  public static final int SECPKG_CRED_OUTBOUND = 2;
  public static final int SECURITY_NATIVE_DREP = 16;
  public static final int ISC_REQ_ALLOCATE_MEMORY = 256;
  public static final int ISC_REQ_CONFIDENTIALITY = 16;
  public static final int ISC_REQ_CONNECTION = 2048;
  public static final int ISC_REQ_DELEGATE = 1;
  public static final int ISC_REQ_EXTENDED_ERROR = 16384;
  public static final int ISC_REQ_INTEGRITY = 65536;
  public static final int ISC_REQ_MUTUAL_AUTH = 2;
  public static final int ISC_REQ_REPLAY_DETECT = 4;
  public static final int ISC_REQ_SEQUENCE_DETECT = 8;
  public static final int ISC_REQ_STREAM = 32768;
  public static final int SECBUFFER_VERSION = 0;
  public static final int SECBUFFER_EMPTY = 0;
  public static final int SECBUFFER_DATA = 1;
  public static final int SECBUFFER_TOKEN = 2;
  
  public static class SecHandle
    extends Structure
  {
    public Pointer dwLower;
    public Pointer dwUpper;
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwLower", "dwUpper" });
    }
    






    public SecHandle() {}
    





    public boolean isNull()
    {
      return (dwLower == null) && (dwUpper == null);
    }
    
    public static class ByReference
      extends Sspi.SecHandle
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class PSecHandle
    extends Structure
  {
    public Sspi.SecHandle.ByReference secHandle;
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "secHandle" });
    }
    
    public PSecHandle() {}
    
    public PSecHandle(Sspi.SecHandle h)
    {
      super();
      read();
    }
    

    public static class ByReference
      extends Sspi.PSecHandle
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  

  public static class CredHandle
    extends Sspi.SecHandle
  {
    public CredHandle() {}
  }
  

  public static class CtxtHandle
    extends Sspi.SecHandle
  {
    public CtxtHandle() {}
  }
  

  public static class SecBuffer
    extends Structure
  {
    public int cbBuffer;
    
    public static class ByReference
      extends Sspi.SecBuffer
      implements Structure.ByReference
    {
      public ByReference() {}
      
      public ByReference(int type, int size)
      {
        super(size);
      }
      
      public ByReference(int type, byte[] token) {
        super(token);
      }
    }
    








    public int BufferType = 0;
    
    public Pointer pvBuffer;
    

    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "cbBuffer", "BufferType", "pvBuffer" });
    }
    





    public SecBuffer() {}
    





    public SecBuffer(int type, int size)
    {
      cbBuffer = size;
      pvBuffer = new Memory(size);
      BufferType = type;
    }
    






    public SecBuffer(int type, byte[] token)
    {
      cbBuffer = token.length;
      pvBuffer = new Memory(token.length);
      pvBuffer.write(0L, token, 0, token.length);
      BufferType = type;
    }
    




    public byte[] getBytes()
    {
      return pvBuffer == null ? null : pvBuffer.getByteArray(0L, cbBuffer);
    }
  }
  


  public static class SecBufferDesc
    extends Structure
  {
    public int ulVersion = 0;
    


    public int cBuffers = 1;
    


    public Sspi.SecBuffer.ByReference[] pBuffers = { new Sspi.SecBuffer.ByReference() };
    

    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "ulVersion", "cBuffers", "pBuffers" });
    }
    





    public SecBufferDesc() {}
    





    public SecBufferDesc(int type, byte[] token)
    {
      pBuffers[0] = new Sspi.SecBuffer.ByReference(type, token);
    }
    




    public SecBufferDesc(int type, int tokenSize)
    {
      pBuffers[0] = new Sspi.SecBuffer.ByReference(type, tokenSize);
    }
    
    public byte[] getBytes() {
      return pBuffers[0].getBytes();
    }
  }
  
  public static class SECURITY_INTEGER
    extends Structure
  {
    public int dwLower;
    public int dwUpper;
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwLower", "dwUpper" });
    }
    



    public SECURITY_INTEGER() {}
  }
  



  public static class TimeStamp
    extends Sspi.SECURITY_INTEGER
  {
    public TimeStamp() {}
  }
  



  public static class PSecPkgInfo
    extends Structure
  {
    public Sspi.SecPkgInfo.ByReference pPkgInfo;
    



    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "pPkgInfo" });
    }
    


    public PSecPkgInfo() {}
    

    public Sspi.SecPkgInfo.ByReference[] toArray(int size)
    {
      return (Sspi.SecPkgInfo.ByReference[])pPkgInfo.toArray(size);
    }
    



    public static class ByReference
      extends Sspi.PSecPkgInfo
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  



  public static class SecPkgInfo
    extends Structure
  {
    public int fCapabilities;
    

    public short wVersion = 1;
    


    public short wRPCID;
    


    public int cbMaxToken;
    


    public WString Name;
    

    public WString Comment;
    


    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "fCapabilities", "wVersion", "wRPCID", "cbMaxToken", "Name", "Comment" });
    }
    
    public SecPkgInfo() {}
    
    public static class ByReference
      extends Sspi.SecPkgInfo
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
}
