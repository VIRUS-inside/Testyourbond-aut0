package com.sun.jna.platform.win32.COM.tlb;

import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbBase;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbCmdlineArgs;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbCoClass;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbConst;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbDispInterface;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbEnum;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbInterface;
import com.sun.jna.platform.win32.OaIdl.TYPEKIND;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;




























public class TlbImp
  implements TlbConst
{
  private TypeLibUtil typeLibUtil;
  private File comRootDir;
  private File outputDir;
  private TlbCmdlineArgs cmdlineArgs;
  
  public static void main(String[] args)
  {
    new TlbImp(args);
  }
  
  public TlbImp(String[] args) {
    cmdlineArgs = new TlbCmdlineArgs(args);
    
    if (cmdlineArgs.isTlbId()) {
      String clsid = cmdlineArgs.getRequiredParam("tlb.id");
      int majorVersion = cmdlineArgs.getIntParam("tlb.major.version");
      
      int minorVersion = cmdlineArgs.getIntParam("tlb.minor.version");
      



      typeLibUtil = new TypeLibUtil(clsid, majorVersion, minorVersion);
      
      startCOM2Java();
    } else if (cmdlineArgs.isTlbFile()) {
      String file = cmdlineArgs.getRequiredParam("tlb.file");
      

      typeLibUtil = new TypeLibUtil(file);
      startCOM2Java();
    } else {
      cmdlineArgs.showCmdHelp();
    }
  }
  

  public void startCOM2Java()
  {
    try
    {
      createDir();
      
      String bindingMode = cmdlineArgs.getBindingMode();
      
      int typeInfoCount = typeLibUtil.getTypeInfoCount();
      for (int i = 0; i < typeInfoCount; i++) {
        OaIdl.TYPEKIND typekind = typeLibUtil.getTypeInfoType(i);
        
        if (value == 0) {
          createCOMEnum(i, getPackageName(), typeLibUtil);
        } else if (value == 1) {
          logInfo("'TKIND_RECORD' objects are currently not supported!");
        } else if (value == 2) {
          logInfo("'TKIND_MODULE' objects are currently not supported!");
        } else if (value == 3) {
          createCOMInterface(i, getPackageName(), typeLibUtil);
        }
        else if (value == 4) {
          createCOMDispInterface(i, getPackageName(), typeLibUtil);
        }
        else if (value == 5) {
          createCOMCoClass(i, getPackageName(), typeLibUtil, bindingMode);
        }
        else if (value == 6) {
          logInfo("'TKIND_ALIAS' objects are currently not supported!");
        } else if (value == 7) {
          logInfo("'TKIND_UNION' objects are currently not supported!");
        }
      }
      
      logInfo(typeInfoCount + " files sucessfully written to: " + comRootDir.toString());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void createDir() throws FileNotFoundException {
    String _outputDir = cmdlineArgs.getParam("output.dir");
    String path = "_jnaCOM_" + System.currentTimeMillis() + "\\myPackage\\" + typeLibUtil.getName().toLowerCase() + "\\";
    

    if (_outputDir != null) {
      comRootDir = new File(_outputDir + "\\" + path);
    } else {
      String tmp = System.getProperty("java.io.tmpdir");
      comRootDir = new File(tmp + "\\" + path);
    }
    
    if (comRootDir.exists()) {
      comRootDir.delete();
    }
    if (comRootDir.mkdirs()) {
      logInfo("Output directory sucessfully created.");
    } else {
      throw new FileNotFoundException("Output directory NOT sucessfully created to: " + comRootDir.toString());
    }
  }
  

  private String getPackageName()
  {
    return "myPackage." + typeLibUtil.getName().toLowerCase();
  }
  
  private void writeTextFile(String filename, String str) throws IOException {
    String file = comRootDir + File.separator + filename;
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
    
    bos.write(str.getBytes());
    bos.close();
  }
  
  private void writeTlbClass(TlbBase tlbBase) throws IOException {
    StringBuffer classBuffer = tlbBase.getClassBuffer();
    writeTextFile(tlbBase.getFilename(), classBuffer.toString());
  }
  








  private void createCOMEnum(int index, String packagename, TypeLibUtil typeLibUtil)
    throws IOException
  {
    TlbEnum tlbEnum = new TlbEnum(index, packagename, typeLibUtil);
    writeTlbClass(tlbEnum);
  }
  








  private void createCOMInterface(int index, String packagename, TypeLibUtil typeLibUtil)
    throws IOException
  {
    TlbInterface tlbInterface = new TlbInterface(index, packagename, typeLibUtil);
    
    writeTlbClass(tlbInterface);
  }
  








  private void createCOMDispInterface(int index, String packagename, TypeLibUtil typeLibUtil)
    throws IOException
  {
    TlbDispInterface tlbDispatch = new TlbDispInterface(index, packagename, typeLibUtil);
    
    writeTlbClass(tlbDispatch);
  }
  
  private void createCOMCoClass(int index, String packagename, TypeLibUtil typeLibUtil, String bindingMode) throws IOException
  {
    TlbCoClass tlbCoClass = new TlbCoClass(index, getPackageName(), typeLibUtil, bindingMode);
    
    writeTlbClass(tlbCoClass);
  }
  





  public static void logInfo(String msg)
  {
    System.out.println(msg);
  }
}
