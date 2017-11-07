package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;




















public abstract interface Guid
{
  public static final IID IID_NULL = new IID();
  


  public static class GUID
    extends Structure
  {
    public int Data1;
    

    public short Data2;
    

    public short Data3;
    

    public GUID() {}
    


    public static class ByReference
      extends Guid.GUID
      implements Structure.ByReference
    {
      public ByReference() {}
      


      public ByReference(Guid.GUID guid)
      {
        super();
        
        Data1 = Data1;
        Data2 = Data2;
        Data3 = Data3;
        Data4 = Data4;
      }
      





      public ByReference(Pointer memory)
      {
        super();
      }
    }
    










    public byte[] Data4 = new byte[8];
    











    public GUID(GUID guid)
    {
      Data1 = Data1;
      Data2 = Data2;
      Data3 = Data3;
      Data4 = Data4;
      
      writeFieldsToMemory();
    }
    





    public GUID(String guid)
    {
      this(fromString(guid));
    }
    





    public GUID(byte[] data)
    {
      this(fromBinary(data));
    }
    





    public GUID(Pointer memory)
    {
      super();
      read();
    }
    






    public static GUID fromBinary(byte[] data)
    {
      if (data.length != 16) {
        throw new IllegalArgumentException("Invalid data length: " + data.length);
      }
      

      GUID newGuid = new GUID();
      long data1Temp = data[0] & 0xFF;
      data1Temp <<= 8;
      data1Temp |= data[1] & 0xFF;
      data1Temp <<= 8;
      data1Temp |= data[2] & 0xFF;
      data1Temp <<= 8;
      data1Temp |= data[3] & 0xFF;
      Data1 = ((int)data1Temp);
      
      int data2Temp = data[4] & 0xFF;
      data2Temp <<= 8;
      data2Temp |= data[5] & 0xFF;
      Data2 = ((short)data2Temp);
      
      int data3Temp = data[6] & 0xFF;
      data3Temp <<= 8;
      data3Temp |= data[7] & 0xFF;
      Data3 = ((short)data3Temp);
      
      Data4[0] = data[8];
      Data4[1] = data[9];
      Data4[2] = data[10];
      Data4[3] = data[11];
      Data4[4] = data[12];
      Data4[5] = data[13];
      Data4[6] = data[14];
      Data4[7] = data[15];
      
      newGuid.writeFieldsToMemory();
      
      return newGuid;
    }
    






    public static GUID fromString(String guid)
    {
      int y = 0;
      char[] _cnewguid = new char[32];
      char[] _cguid = guid.toCharArray();
      byte[] bdata = new byte[16];
      GUID newGuid = new GUID();
      

      if (guid.length() > 38) {
        throw new IllegalArgumentException("Invalid guid length: " + guid.length());
      }
      


      for (int i = 0; i < _cguid.length; i++) {
        if ((_cguid[i] != '{') && (_cguid[i] != '-') && (_cguid[i] != '}'))
        {
          _cnewguid[(y++)] = _cguid[i];
        }
      }
      
      for (int i = 0; i < 32; i += 2) {
        bdata[(i / 2)] = ((byte)((Character.digit(_cnewguid[i], 16) << 4) + Character.digit(_cnewguid[(i + 1)], 16) & 0xFF));
      }
      

      if (bdata.length != 16) {
        throw new IllegalArgumentException("Invalid data length: " + bdata.length);
      }
      

      long data1Temp = bdata[0] & 0xFF;
      data1Temp <<= 8;
      data1Temp |= bdata[1] & 0xFF;
      data1Temp <<= 8;
      data1Temp |= bdata[2] & 0xFF;
      data1Temp <<= 8;
      data1Temp |= bdata[3] & 0xFF;
      Data1 = ((int)data1Temp);
      
      int data2Temp = bdata[4] & 0xFF;
      data2Temp <<= 8;
      data2Temp |= bdata[5] & 0xFF;
      Data2 = ((short)data2Temp);
      
      int data3Temp = bdata[6] & 0xFF;
      data3Temp <<= 8;
      data3Temp |= bdata[7] & 0xFF;
      Data3 = ((short)data3Temp);
      
      Data4[0] = bdata[8];
      Data4[1] = bdata[9];
      Data4[2] = bdata[10];
      Data4[3] = bdata[11];
      Data4[4] = bdata[12];
      Data4[5] = bdata[13];
      Data4[6] = bdata[14];
      Data4[7] = bdata[15];
      
      newGuid.writeFieldsToMemory();
      
      return newGuid;
    }
    





    public static GUID newGuid()
    {
      SecureRandom ng = new SecureRandom();
      byte[] randomBytes = new byte[16];
      
      ng.nextBytes(randomBytes); byte[] 
        tmp21_18 = randomBytes;tmp21_18[6] = ((byte)(tmp21_18[6] & 0xF)); byte[] 
        tmp31_28 = randomBytes;tmp31_28[6] = ((byte)(tmp31_28[6] | 0x40)); byte[] 
        tmp41_38 = randomBytes;tmp41_38[8] = ((byte)(tmp41_38[8] & 0x3F)); byte[] 
        tmp51_48 = randomBytes;tmp51_48[8] = ((byte)(tmp51_48[8] | 0x80));
      
      return new GUID(randomBytes);
    }
    




    public byte[] toByteArray()
    {
      byte[] guid = new byte[16];
      
      byte[] bytes1 = new byte[4];
      bytes1[0] = ((byte)(Data1 >> 24));
      bytes1[1] = ((byte)(Data1 >> 16));
      bytes1[2] = ((byte)(Data1 >> 8));
      bytes1[3] = ((byte)(Data1 >> 0));
      
      byte[] bytes2 = new byte[4];
      bytes2[0] = ((byte)(Data2 >> 24));
      bytes2[1] = ((byte)(Data2 >> 16));
      bytes2[2] = ((byte)(Data2 >> 8));
      bytes2[3] = ((byte)(Data2 >> 0));
      
      byte[] bytes3 = new byte[4];
      bytes3[0] = ((byte)(Data3 >> 24));
      bytes3[1] = ((byte)(Data3 >> 16));
      bytes3[2] = ((byte)(Data3 >> 8));
      bytes3[3] = ((byte)(Data3 >> 0));
      
      System.arraycopy(bytes1, 0, guid, 0, 4);
      System.arraycopy(bytes2, 2, guid, 4, 2);
      System.arraycopy(bytes3, 2, guid, 6, 2);
      System.arraycopy(Data4, 0, guid, 8, 8);
      
      return guid;
    }
    





    public String toGuidString()
    {
      String HEXES = "0123456789ABCDEF";
      byte[] bGuid = toByteArray();
      
      StringBuilder hexStr = new StringBuilder(2 * bGuid.length);
      hexStr.append("{");
      
      for (int i = 0; i < bGuid.length; i++) {
        char ch1 = "0123456789ABCDEF".charAt((bGuid[i] & 0xF0) >> 4);
        char ch2 = "0123456789ABCDEF".charAt(bGuid[i] & 0xF);
        hexStr.append(ch1).append(ch2);
        
        if ((i == 3) || (i == 5) || (i == 7) || (i == 9)) {
          hexStr.append("-");
        }
      }
      hexStr.append("}");
      return hexStr.toString();
    }
    


    protected void writeFieldsToMemory()
    {
      writeField("Data1");
      writeField("Data2");
      writeField("Data3");
      writeField("Data4");
    }
    




    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "Data1", "Data2", "Data3", "Data4" });
    }
  }
  






  public static class CLSID
    extends Guid.GUID
  {
    public CLSID() {}
    





    public static class ByReference
      extends Guid.GUID
    {
      public ByReference() {}
      





      public ByReference(Guid.GUID guid)
      {
        super();
      }
      









      public ByReference(Pointer memory) {}
    }
    









    public CLSID(String guid)
    {
      super();
    }
    




    public CLSID(Guid.GUID guid)
    {
      super();
    }
  }
  







  public static class REFIID
    extends Guid.IID
  {
    public REFIID() {}
    







    public REFIID(Pointer memory)
    {
      super();
    }
    






    public REFIID(byte[] data)
    {
      super();
    }
  }
  








  public static class IID
    extends Guid.GUID
  {
    public IID() {}
    








    public IID(Pointer memory)
    {
      super();
    }
    





    public IID(String iid)
    {
      super();
    }
    






    public IID(byte[] data)
    {
      super();
    }
  }
}
