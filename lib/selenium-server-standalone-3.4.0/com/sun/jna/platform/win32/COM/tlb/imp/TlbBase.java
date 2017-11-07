package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





































public abstract class TlbBase
{
  public static final String CR = "\n";
  public static final String CRCR = "\n\n";
  public static final String TAB = "\t";
  public static final String TABTAB = "\t\t";
  protected TypeLibUtil typeLibUtil;
  protected TypeInfoUtil typeInfoUtil;
  protected int index;
  protected StringBuffer templateBuffer;
  protected StringBuffer classBuffer;
  protected String content = "";
  
  protected String filename = "DefaultFilename";
  
  protected String name = "DefaultName";
  

  public static String[] IUNKNOWN_METHODS = { "QueryInterface", "AddRef", "Release" };
  


  public static String[] IDISPATCH_METHODS = { "GetTypeInfoCount", "GetTypeInfo", "GetIDsOfNames", "Invoke" };
  

  protected String bindingMode = "dispid";
  
  public TlbBase(int index, TypeLibUtil typeLibUtil, TypeInfoUtil typeInfoUtil) {
    this(index, typeLibUtil, typeInfoUtil, "dispid");
  }
  
  public TlbBase(int index, TypeLibUtil typeLibUtil, TypeInfoUtil typeInfoUtil, String bindingMode) {
    this.index = index;
    this.typeLibUtil = typeLibUtil;
    this.typeInfoUtil = typeInfoUtil;
    this.bindingMode = bindingMode;
    
    String filename = getClassTemplate();
    try {
      readTemplateFile(filename);
      classBuffer = templateBuffer;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  





  public void logError(String msg)
  {
    log("ERROR", msg);
  }
  





  public void logInfo(String msg)
  {
    log("INFO", msg);
  }
  




  public StringBuffer getClassBuffer()
  {
    return classBuffer;
  }
  





  public void createContent(String content)
  {
    replaceVariable("content", content);
  }
  
  public void setFilename(String filename) {
    if (!filename.endsWith("java"))
      filename = filename + ".java";
    this.filename = filename;
  }
  
  public String getFilename() {
    return filename;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  







  protected void log(String level, String msg)
  {
    String _msg = level + " " + getTime() + " : " + msg;
    System.out.println(_msg);
  }
  




  private String getTime()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    return sdf.format(new Date());
  }
  






  protected abstract String getClassTemplate();
  





  protected void readTemplateFile(String filename)
    throws IOException
  {
    templateBuffer = new StringBuffer();
    BufferedReader reader = null;
    try {
      InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
      
      reader = new BufferedReader(new InputStreamReader(is));
      String line = null;
      while ((line = reader.readLine()) != null)
        templateBuffer.append(line + "\n");
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }
  






  protected void replaceVariable(String name, String value)
  {
    if (value == null) {
      value = "";
    }
    Pattern pattern = Pattern.compile("\\$\\{" + name + "\\}");
    Matcher matcher = pattern.matcher(classBuffer);
    String replacement = value;
    String result = "";
    
    while (matcher.find()) {
      result = matcher.replaceAll(replacement);
    }
    
    if (result.length() > 0)
      classBuffer = new StringBuffer(result);
  }
  
  protected void createPackageName(String packagename) {
    replaceVariable("packagename", packagename);
  }
  





  protected void createClassName(String name)
  {
    replaceVariable("classname", name);
  }
  






  protected boolean isReservedMethod(String method)
  {
    for (int i = 0; i < IUNKNOWN_METHODS.length; i++) {
      if (IUNKNOWN_METHODS[i].equalsIgnoreCase(method)) {
        return true;
      }
    }
    for (int i = 0; i < IDISPATCH_METHODS.length; i++) {
      if (IDISPATCH_METHODS[i].equalsIgnoreCase(method)) {
        return true;
      }
    }
    return false;
  }
  
  protected boolean isVTableMode() {
    if (bindingMode.equalsIgnoreCase("vtable")) {
      return true;
    }
    return false;
  }
  
  protected boolean isDispIdMode() {
    if (bindingMode.equalsIgnoreCase("dispid")) {
      return true;
    }
    return false;
  }
}
