package com.sun.jna.platform.win32.COM.tlb.imp;

import java.io.PrintStream;
import java.util.Hashtable;










public class TlbCmdlineArgs
  extends Hashtable<String, String>
  implements TlbConst
{
  public TlbCmdlineArgs(String[] args)
  {
    readCmdArgs(args);
  }
  
  public int getIntParam(String key) {
    String param = getRequiredParam(key);
    return new Integer(param).intValue();
  }
  
  public String getParam(String key) {
    return (String)get(key);
  }
  
  public String getRequiredParam(String key) {
    String param = getParam(key);
    if (param == null) {
      throw new TlbParameterNotFoundException("Commandline parameter not found: " + key);
    }
    
    return param;
  }
  
  private void readCmdArgs(String[] args) {
    if (args.length < 2) {
      showCmdHelp();
    }
    for (int i = 0; i < args.length; i++) {
      String cmd = args[i];
      if (cmd.startsWith("-")) {
        put(args[i].substring(1), args[(i + 1)]);
      }
    }
  }
  
  public boolean isTlbFile() {
    return containsKey("tlb.file");
  }
  
  public boolean isTlbId() {
    return containsKey("tlb.id");
  }
  
  public String getBindingMode() {
    if (containsKey("bind.mode")) {
      return getParam("bind.mode");
    }
    return "vtable";
  }
  
  public void showCmdHelp() {
    String helpStr = "usage: TlbImp [-tlb.id -tlb.major.version -tlb.minor.version] [-tlb.file] [-bind.mode vTable, dispId] [-output.dir]\n\noptions:\n-tlb.id               The guid of the type library.\n-tlb.major.version    The major version of the type library.\n-tlb.minor.version    The minor version of the type library.\n-tlb.file             The file name containing the type library.\n-bind.mode            The binding mode used to create the Java code.\n-output.dir           The optional output directory, default is the user temp directory.\n\nsamples:\nMicrosoft Shell Controls And Automation:\n-tlb.file shell32.dll\n-tlb.id {50A7E9B0-70EF-11D1-B75A-00A0C90564FE} -tlb.major.version 1 -tlb.minor.version 0\n\nMicrosoft Word 12.0 Object Library:\n-tlb.id {00020905-0000-0000-C000-000000000046} -tlb.major.version 8 -tlb.minor.version 4\n\n";
    



























    System.out.println(helpStr);
    System.exit(0);
  }
}
