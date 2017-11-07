package com.sun.jna.platform.win32.COM;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid.CLSID.ByReference;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.OaIdl.MEMBERID;
import com.sun.jna.platform.win32.OaIdl.TLIBATTR;
import com.sun.jna.platform.win32.OaIdl.TYPEKIND;
import com.sun.jna.platform.win32.OaIdl.TYPEKIND.ByReference;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WTypes.BSTRByReference;
import com.sun.jna.platform.win32.WTypes.LPOLESTR;
import com.sun.jna.platform.win32.WinDef.BOOL;
import com.sun.jna.platform.win32.WinDef.BOOLByReference;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;
import com.sun.jna.platform.win32.WinDef.LCID;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinDef.USHORT;
import com.sun.jna.platform.win32.WinDef.USHORTByReference;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;
















public class TypeLibUtil
{
  public static final OleAuto OLEAUTO = OleAuto.INSTANCE;
  

  private ITypeLib typelib;
  

  private WinDef.LCID lcid = Kernel32.INSTANCE.GetUserDefaultLCID();
  



  private String name;
  



  private String docString;
  


  private int helpContext;
  


  private String helpFile;
  



  public TypeLibUtil(String clsidStr, int wVerMajor, int wVerMinor)
  {
    Guid.CLSID.ByReference clsid = new Guid.CLSID.ByReference();
    
    WinNT.HRESULT hr = Ole32.INSTANCE.CLSIDFromString(new WString(clsidStr), clsid);
    
    COMUtils.checkRC(hr);
    

    PointerByReference pTypeLib = new PointerByReference();
    hr = OleAuto.INSTANCE.LoadRegTypeLib(clsid, wVerMajor, wVerMinor, lcid, pTypeLib);
    
    COMUtils.checkRC(hr);
    

    typelib = new TypeLib(pTypeLib.getValue());
    
    initTypeLibInfo();
  }
  
  public TypeLibUtil(String file)
  {
    PointerByReference pTypeLib = new PointerByReference();
    WinNT.HRESULT hr = OleAuto.INSTANCE.LoadTypeLib(new WString(file), pTypeLib);
    COMUtils.checkRC(hr);
    

    typelib = new TypeLib(pTypeLib.getValue());
    
    initTypeLibInfo();
  }
  


  private void initTypeLibInfo()
  {
    TypeLibDoc documentation = getDocumentation(-1);
    name = documentation.getName();
    docString = documentation.getDocString();
    helpContext = documentation.getHelpContext();
    helpFile = documentation.getHelpFile();
  }
  




  public int getTypeInfoCount()
  {
    return typelib.GetTypeInfoCount().intValue();
  }
  






  public OaIdl.TYPEKIND getTypeInfoType(int index)
  {
    OaIdl.TYPEKIND.ByReference typekind = new OaIdl.TYPEKIND.ByReference();
    WinNT.HRESULT hr = typelib.GetTypeInfoType(new WinDef.UINT(index), typekind);
    COMUtils.checkRC(hr);
    return typekind;
  }
  






  public ITypeInfo getTypeInfo(int index)
  {
    PointerByReference ppTInfo = new PointerByReference();
    WinNT.HRESULT hr = typelib.GetTypeInfo(new WinDef.UINT(index), ppTInfo);
    COMUtils.checkRC(hr);
    return new TypeInfo(ppTInfo.getValue());
  }
  






  public TypeInfoUtil getTypeInfoUtil(int index)
  {
    return new TypeInfoUtil(getTypeInfo(index));
  }
  




  public OaIdl.TLIBATTR getLibAttr()
  {
    PointerByReference ppTLibAttr = new PointerByReference();
    WinNT.HRESULT hr = typelib.GetLibAttr(ppTLibAttr);
    COMUtils.checkRC(hr);
    
    return new OaIdl.TLIBATTR(ppTLibAttr.getValue());
  }
  




  public TypeComp GetTypeComp()
  {
    PointerByReference ppTComp = new PointerByReference();
    WinNT.HRESULT hr = typelib.GetTypeComp(ppTComp);
    COMUtils.checkRC(hr);
    
    return new TypeComp(ppTComp.getValue());
  }
  






  public TypeLibDoc getDocumentation(int index)
  {
    WTypes.BSTRByReference pBstrName = new WTypes.BSTRByReference();
    WTypes.BSTRByReference pBstrDocString = new WTypes.BSTRByReference();
    WinDef.DWORDByReference pdwHelpContext = new WinDef.DWORDByReference();
    WTypes.BSTRByReference pBstrHelpFile = new WTypes.BSTRByReference();
    
    WinNT.HRESULT hr = typelib.GetDocumentation(index, pBstrName, pBstrDocString, pdwHelpContext, pBstrHelpFile);
    
    COMUtils.checkRC(hr);
    
    TypeLibDoc typeLibDoc = new TypeLibDoc(pBstrName.getString(), pBstrDocString.getString(), pdwHelpContext.getValue().intValue(), pBstrHelpFile.getString());
    


    OLEAUTO.SysFreeString(pBstrName.getValue());
    OLEAUTO.SysFreeString(pBstrDocString.getValue());
    OLEAUTO.SysFreeString(pBstrHelpFile.getValue());
    
    return typeLibDoc;
  }
  





  public static class TypeLibDoc
  {
    private String name;
    




    private String docString;
    




    private int helpContext;
    




    private String helpFile;
    




    public TypeLibDoc(String name, String docString, int helpContext, String helpFile)
    {
      this.name = name;
      this.docString = docString;
      this.helpContext = helpContext;
      this.helpFile = helpFile;
    }
    




    public String getName()
    {
      return name;
    }
    




    public String getDocString()
    {
      return docString;
    }
    




    public int getHelpContext()
    {
      return helpContext;
    }
    




    public String getHelpFile()
    {
      return helpFile;
    }
  }
  









  public IsName IsName(String nameBuf, int hashVal)
  {
    WTypes.LPOLESTR szNameBuf = new WTypes.LPOLESTR(nameBuf);
    WinDef.ULONG lHashVal = new WinDef.ULONG(hashVal);
    WinDef.BOOLByReference pfName = new WinDef.BOOLByReference();
    
    WinNT.HRESULT hr = typelib.IsName(szNameBuf, lHashVal, pfName);
    COMUtils.checkRC(hr);
    
    return new IsName(szNameBuf.getValue(), pfName.getValue().booleanValue());
  }
  






  public static class IsName
  {
    private String nameBuf;
    




    private boolean name;
    





    public IsName(String nameBuf, boolean name)
    {
      this.nameBuf = nameBuf;
      this.name = name;
    }
    




    public String getNameBuf()
    {
      return nameBuf;
    }
    




    public boolean isName()
    {
      return name;
    }
  }
  











  public FindName FindName(String name, int hashVal, short found)
  {
    WTypes.BSTRByReference szNameBuf = new WTypes.BSTRByReference(OleAuto.INSTANCE.SysAllocString(name));
    
    WinDef.ULONG lHashVal = new WinDef.ULONG(hashVal);
    WinDef.USHORTByReference pcFound = new WinDef.USHORTByReference(found);
    
    WinNT.HRESULT hr = typelib.FindName(szNameBuf, lHashVal, null, null, pcFound);
    
    COMUtils.checkRC(hr);
    
    found = pcFound.getValue().shortValue();
    ITypeInfo[] ppTInfo = new ITypeInfo[found];
    OaIdl.MEMBERID[] rgMemId = new OaIdl.MEMBERID[found];
    hr = typelib.FindName(szNameBuf, lHashVal, ppTInfo, rgMemId, pcFound);
    
    COMUtils.checkRC(hr);
    
    FindName findName = new FindName(szNameBuf.getString(), ppTInfo, rgMemId, found);
    
    OLEAUTO.SysFreeString(szNameBuf.getValue());
    
    return findName;
  }
  





  public static class FindName
  {
    private String nameBuf;
    




    private ITypeInfo[] pTInfo;
    




    private OaIdl.MEMBERID[] rgMemId;
    




    private short pcFound;
    




    public FindName(String nameBuf, ITypeInfo[] pTInfo, OaIdl.MEMBERID[] rgMemId, short pcFound)
    {
      this.nameBuf = nameBuf;
      this.pTInfo = pTInfo;
      this.rgMemId = rgMemId;
      this.pcFound = pcFound;
    }
    




    public String getNameBuf()
    {
      return nameBuf;
    }
    




    public ITypeInfo[] getTInfo()
    {
      return pTInfo;
    }
    




    public OaIdl.MEMBERID[] getMemId()
    {
      return rgMemId;
    }
    




    public short getFound()
    {
      return pcFound;
    }
  }
  





  public void ReleaseTLibAttr(OaIdl.TLIBATTR pTLibAttr)
  {
    typelib.ReleaseTLibAttr(pTLibAttr);
  }
  




  public WinDef.LCID getLcid()
  {
    return lcid;
  }
  




  public ITypeLib getTypelib()
  {
    return typelib;
  }
  




  public String getName()
  {
    return name;
  }
  




  public String getDocString()
  {
    return docString;
  }
  




  public long getHelpContext()
  {
    return helpContext;
  }
  




  public String getHelpFile()
  {
    return helpFile;
  }
}
