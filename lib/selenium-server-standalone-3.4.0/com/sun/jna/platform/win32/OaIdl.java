package com.sun.jna.platform.win32;

import com.sun.jna.IntegerType;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.Union;
import com.sun.jna.platform.win32.COM.TypeComp;
import com.sun.jna.ptr.ByReference;
import java.util.Arrays;
import java.util.List;































































public abstract interface OaIdl
{
  public static class EXCEPINFO
    extends Structure
  {
    public WinDef.WORD wCode;
    public WinDef.WORD wReserved;
    public WTypes.BSTR bstrSource;
    public WTypes.BSTR bstrDescription;
    public WTypes.BSTR bstrHelpFile;
    public WinDef.DWORD dwHelpContext;
    public WinDef.PVOID pvReserved;
    public ByReference pfnDeferredFillIn;
    public WinDef.SCODE scode;
    
    public EXCEPINFO() {}
    
    public EXCEPINFO(Pointer p)
    {
      super();
    }
    





    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "wCode", "wReserved", "bstrSource", "bstrDescription", "bstrHelpFile", "dwHelpContext", "pvReserved", "pfnDeferredFillIn", "scode" });
    }
    
    public static class ByReference extends OaIdl.EXCEPINFO implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class VARIANT_BOOL extends IntegerType {
    public static final int SIZE = 2;
    
    public VARIANT_BOOL() { this(0L); }
    
    public VARIANT_BOOL(long value)
    {
      super(value);
    }
  }
  
  public static class _VARIANT_BOOL extends OaIdl.VARIANT_BOOL
  {
    public _VARIANT_BOOL() {
      this(0L);
    }
    
    public _VARIANT_BOOL(long value) {
      super();
    }
  }
  
  public static class VARIANT_BOOLByReference extends ByReference {
    public VARIANT_BOOLByReference() {
      this(new OaIdl.VARIANT_BOOL(0L));
    }
    
    public VARIANT_BOOLByReference(OaIdl.VARIANT_BOOL value) {
      super();
      setValue(value);
    }
    
    public void setValue(OaIdl.VARIANT_BOOL value) {
      getPointer().setShort(0L, value.shortValue());
    }
    
    public OaIdl.VARIANT_BOOL getValue() {
      return new OaIdl.VARIANT_BOOL(getPointer().getShort(0L));
    }
  }
  
  public static class _VARIANT_BOOLByReference extends ByReference {
    public _VARIANT_BOOLByReference() {
      this(new OaIdl.VARIANT_BOOL(0L));
    }
    
    public _VARIANT_BOOLByReference(OaIdl.VARIANT_BOOL value) {
      super();
      setValue(value);
    }
    
    public void setValue(OaIdl.VARIANT_BOOL value) {
      getPointer().setShort(0L, value.shortValue());
    }
    
    public OaIdl.VARIANT_BOOL getValue() {
      return new OaIdl.VARIANT_BOOL(getPointer().getShort(0L));
    }
  }
  

  public static class DATE
    extends Structure
  {
    public double date;
    

    public DATE() {}
    
    public DATE(double date)
    {
      this.date = date;
    }
    





    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "date" });
    }
    
    public static class ByReference extends OaIdl.DATE implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class DISPID extends WinDef.LONG {
    public DISPID() { this(0); }
    
    public DISPID(int value)
    {
      super();
    }
  }
  
  public static class DISPIDByReference extends ByReference {
    public DISPIDByReference() {
      this(new OaIdl.DISPID(0));
    }
    
    public DISPIDByReference(OaIdl.DISPID value) {
      super();
      setValue(value);
    }
    
    public void setValue(OaIdl.DISPID value) {
      getPointer().setInt(0L, value.intValue());
    }
    
    public OaIdl.DISPID getValue() {
      return new OaIdl.DISPID(getPointer().getInt(0L));
    }
  }
  
  public static class MEMBERID extends OaIdl.DISPID {
    public MEMBERID() {
      this(0);
    }
    
    public MEMBERID(int value) {
      super();
    }
  }
  
  public static class MEMBERIDByReference extends ByReference {
    public MEMBERIDByReference() {
      this(new OaIdl.MEMBERID(0));
    }
    
    public MEMBERIDByReference(OaIdl.MEMBERID value) {
      super();
      setValue(value);
    }
    
    public void setValue(OaIdl.MEMBERID value) {
      getPointer().setInt(0L, value.intValue());
    }
    
    public OaIdl.MEMBERID getValue() {
      return new OaIdl.MEMBERID(getPointer().getInt(0L));
    }
  }
  



  public static final DISPID DISPID_COLLECT = new DISPID(-8);
  


  public static final DISPID DISPID_CONSTRUCTOR = new DISPID(-6);
  


  public static final DISPID DISPID_DESTRUCTOR = new DISPID(-7);
  




  public static final DISPID DISPID_EVALUATE = new DISPID(-5);
  




  public static final DISPID DISPID_NEWENUM = new DISPID(-4);
  


  public static final DISPID DISPID_PROPERTYPUT = new DISPID(-3);
  



  public static final DISPID DISPID_UNKNOWN = new DISPID(-1);
  




  public static final DISPID DISPID_VALUE = new DISPID(0);
  
  public static final MEMBERID MEMBERID_NIL = new MEMBERID(DISPID_UNKNOWN.intValue());
  
  public static final int FADF_AUTO = 1;
  
  public static final int FADF_STATIC = 2;
  
  public static final int FADF_EMBEDDED = 4;
  
  public static final int FADF_FIXEDSIZE = 16;
  
  public static final int FADF_RECORD = 32;
  
  public static final int FADF_HAVEIID = 64;
  
  public static final int FADF_HAVEVARTYPE = 128;
  
  public static final int FADF_BSTR = 256;
  
  public static final int FADF_UNKNOWN = 512;
  
  public static final int FADF_DISPATCH = 1024;
  
  public static final int FADF_VARIANT = 2048;
  
  public static final int FADF_RESERVED = 61448;
  

  public static class TYPEKIND
    extends Structure
  {
    public int value;
    
    public static final int TKIND_ENUM = 0;
    
    public static final int TKIND_RECORD = 1;
    
    public static final int TKIND_MODULE = 2;
    
    public static final int TKIND_INTERFACE = 3;
    
    public static final int TKIND_DISPATCH = 4;
    
    public static final int TKIND_COCLASS = 5;
    
    public static final int TKIND_ALIAS = 6;
    public static final int TKIND_UNION = 7;
    public static final int TKIND_MAX = 8;
    public TYPEKIND() {}
    
    public static class ByReference
      extends OaIdl.TYPEKIND
      implements Structure.ByReference
    {
      public ByReference() {}
      
      public ByReference(int value)
      {
        super();
      }
      
      public ByReference(OaIdl.TYPEKIND typekind) {
        super();
        value = value;
      }
    }
    




    public TYPEKIND(int value)
    {
      this.value = value;
    }
    
    public TYPEKIND(Pointer pointer) {
      super();
      read();
    }
    



















    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "value" });
    }
  }
  
  public static class DESCKIND extends Structure {
    public int value;
    public static final int DESCKIND_NONE = 0;
    public static final int DESCKIND_FUNCDESC = 1;
    public static final int DESCKIND_VARDESC = 2;
    public static final int DESCKIND_TYPECOMP = 3;
    public static final int DESCKIND_IMPLICITAPPOBJ = 4;
    public static final int DESCKIND_MAX = 5;
    
    public DESCKIND() {}
    
    public DESCKIND(int value) { this.value = value; }
    
    public DESCKIND(Pointer pointer)
    {
      super();
      read();
    }
    













    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "value" });
    }
    
    public static class ByReference extends OaIdl.DESCKIND implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class SAFEARRAY extends Structure
  {
    public WinDef.USHORT cDims;
    public WinDef.USHORT fFeatures;
    public WinDef.ULONG cbElements;
    public WinDef.ULONG cLocks;
    public WinDef.PVOID pvData;
    public OaIdl.SAFEARRAYBOUND[] rgsabound = { new OaIdl.SAFEARRAYBOUND() };
    
    public SAFEARRAY() {}
    
    public SAFEARRAY(Pointer pointer)
    {
      super();
      read();
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "cDims", "fFeatures", "cbElements", "cLocks", "pvData", "rgsabound" });
    }
    
    public static class ByReference extends OaIdl.SAFEARRAY implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class SAFEARRAYBOUND extends Structure
  {
    public WinDef.ULONG cElements;
    public WinDef.LONG lLbound;
    
    public SAFEARRAYBOUND() {}
    
    public SAFEARRAYBOUND(Pointer pointer)
    {
      super();
      read();
    }
    
    public SAFEARRAYBOUND(int cElements, int lLbound) {
      this.cElements = new WinDef.ULONG(cElements);
      this.lLbound = new WinDef.LONG(lLbound);
      write();
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "cElements", "lLbound" });
    }
    
    public static class ByReference extends OaIdl.SAFEARRAYBOUND implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class CURRENCY extends Union
  {
    public _CURRENCY currency;
    public WinDef.LONGLONG int64;
    
    public CURRENCY() {}
    
    public CURRENCY(Pointer pointer)
    {
      super();
      read();
    }
    
    public static class _CURRENCY extends Structure
    {
      public WinDef.ULONG Lo;
      public WinDef.LONG Hi;
      
      public _CURRENCY() {}
      
      public _CURRENCY(Pointer pointer)
      {
        super();
        read();
      }
      


      protected List getFieldOrder() { return Arrays.asList(new String[] { "Lo", "Hi" }); }
    }
    
    public static class ByReference extends OaIdl.CURRENCY implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class DECIMAL extends Structure {
    public short wReserved;
    public _DECIMAL1 decimal1;
    public NativeLong Hi32;
    public _DECIMAL2 decimal2;
    
    public DECIMAL() {}
    
    public DECIMAL(Pointer pointer) { super(); }
    
    public static class ByReference
      extends OaIdl.DECIMAL implements Structure.ByReference
    {
      public ByReference() {}
    }
    
    public static class _DECIMAL1 extends Union
    {
      public WinDef.USHORT signscale;
      public _DECIMAL1_DECIMAL decimal1_DECIMAL;
      
      public _DECIMAL1()
      {
        setType("signscale");
      }
      
      public _DECIMAL1(Pointer pointer) {
        super();
        setType("signscale");
        read();
      }
      
      public static class _DECIMAL1_DECIMAL extends Structure
      {
        public WinDef.BYTE scale;
        public WinDef.BYTE sign;
        
        public _DECIMAL1_DECIMAL() {}
        
        public _DECIMAL1_DECIMAL(Pointer pointer)
        {
          super();
        }
        
        protected List getFieldOrder()
        {
          return Arrays.asList(new String[] { "scale", "sign" });
        }
      }
    }
    
    public static class _DECIMAL2 extends Union
    {
      public WinDef.ULONGLONG Lo64;
      public _DECIMAL2_DECIMAL decimal2_DECIMAL;
      
      public _DECIMAL2() {
        setType("Lo64");
      }
      
      public _DECIMAL2(Pointer pointer) {
        super();
        setType("Lo64");
        read();
      }
      
      public static class _DECIMAL2_DECIMAL extends Structure
      {
        public WinDef.BYTE Lo32;
        public WinDef.BYTE Mid32;
        
        public _DECIMAL2_DECIMAL() {}
        
        public _DECIMAL2_DECIMAL(Pointer pointer)
        {
          super();
        }
        
        protected List getFieldOrder()
        {
          return Arrays.asList(new String[] { "Lo32", "Mid32" });
        }
      }
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "wReserved", "decimal1", "Hi32", "decimal2" });
    }
  }
  
  public static class SYSKIND extends Structure
  {
    public int value;
    public static final int SYS_WIN16 = 0;
    public static final int SYS_WIN32 = 1;
    public static final int SYS_MAC = 2;
    public static final int SYS_WIN64 = 3;
    
    public SYSKIND() {}
    
    public SYSKIND(int value)
    {
      this.value = value;
    }
    
    public SYSKIND(Pointer pointer) {
      super();
      read();
    }
    







    protected List getFieldOrder() { return Arrays.asList(new String[] { "value" }); }
    
    public static class ByReference extends OaIdl.SYSKIND implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class LIBFLAGS extends Structure { public int value;
    public static final int LIBFLAG_FRESTRICTED = 1;
    public static final int LIBFLAG_FCONTROL = 2;
    public static final int LIBFLAG_FHIDDEN = 4;
    public static final int LIBFLAG_FHASDISKIMAGE = 8;
    
    public LIBFLAGS() {}
    
    public LIBFLAGS(int value) { this.value = value; }
    
    public LIBFLAGS(Pointer pointer)
    {
      super();
      read();
    }
    







    protected List getFieldOrder() { return Arrays.asList(new String[] { "value" }); }
    
    public static class ByReference extends OaIdl.LIBFLAGS implements Structure.ByReference { public ByReference() {}
    }
  }
  
  public static class TLIBATTR extends Structure { public Guid.GUID guid;
    public WinDef.LCID lcid;
    public OaIdl.SYSKIND syskind;
    
    public static class ByReference extends OaIdl.TLIBATTR implements Structure.ByReference { public ByReference() {}
      
      public ByReference(Pointer pointer) { super();
        read();
      }
    }
    

    public WinDef.WORD wMajorVerNum;
    
    public WinDef.WORD wMinorVerNum;
    
    public WinDef.WORD wLibFlags;
    
    public TLIBATTR() {}
    

    public TLIBATTR(Pointer pointer)
    {
      super();
      read();
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "guid", "lcid", "syskind", "wMajorVerNum", "wMinorVerNum", "wLibFlags" });
    }
  }
  


  public static class BINDPTR
    extends Union
  {
    public OaIdl.FUNCDESC lpfuncdesc;
    

    public OaIdl.VARDESC lpvardesc;
    

    public TypeComp lptcomp;
    


    public BINDPTR() {}
    

    public BINDPTR(OaIdl.VARDESC lpvardesc)
    {
      this.lpvardesc = lpvardesc;
      setType(OaIdl.VARDESC.class);
    }
    

    public BINDPTR(TypeComp lptcomp)
    {
      this.lptcomp = lptcomp;
      setType(TypeComp.class);
    }
    

    public BINDPTR(OaIdl.FUNCDESC lpfuncdesc)
    {
      this.lpfuncdesc = lpfuncdesc;
      setType(OaIdl.FUNCDESC.class);
    }
    
    public static class ByReference extends OaIdl.BINDPTR implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class FUNCDESC extends Structure {
    public OaIdl.MEMBERID memid;
    public OaIdl.ScodeArg.ByReference lprgscode;
    public OaIdl.ElemDescArg.ByReference lprgelemdescParam;
    public OaIdl.FUNCKIND funckind;
    public OaIdl.INVOKEKIND invkind;
    public OaIdl.CALLCONV callconv;
    public WinDef.SHORT cParams;
    public WinDef.SHORT cParamsOpt;
    public WinDef.SHORT oVft;
    public WinDef.SHORT cScodes;
    public OaIdl.ELEMDESC elemdescFunc;
    public WinDef.WORD wFuncFlags;
    
    public FUNCDESC() {}
    
    public FUNCDESC(Pointer pointer) {
      super();
      read();
      
      if (cParams.shortValue() > 1) {
        lprgelemdescParam.elemDescArg = new OaIdl.ELEMDESC[cParams.shortValue()];
        
        lprgelemdescParam.read();
      }
    }
    
    protected List<String> getFieldOrder()
    {
      return Arrays.asList(new String[] { "memid", "lprgscode", "lprgelemdescParam", "funckind", "invkind", "callconv", "cParams", "cParamsOpt", "oVft", "cScodes", "elemdescFunc", "wFuncFlags" });
    }
    
    public static class ByReference
      extends OaIdl.FUNCDESC implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class ElemDescArg extends Structure
  {
    public OaIdl.ELEMDESC[] elemDescArg = { new OaIdl.ELEMDESC() };
    
    public ElemDescArg() {}
    
    public ElemDescArg(Pointer pointer)
    {
      super();
      read();
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "elemDescArg" });
    }
    
    public static class ByReference extends OaIdl.ElemDescArg implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class ScodeArg extends Structure {
    public WinDef.SCODE[] scodeArg = { new WinDef.SCODE() };
    
    public ScodeArg() {}
    
    public ScodeArg(Pointer pointer)
    {
      super();
      read();
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "scodeArg" });
    }
    

    public static class ByReference
      extends OaIdl.ScodeArg
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  

  public static class VARDESC
    extends Structure
  {
    public OaIdl.MEMBERID memid;
    
    public WTypes.LPOLESTR lpstrSchema;
    
    public _VARDESC _vardesc;
    
    public OaIdl.ELEMDESC elemdescVar;
    
    public WinDef.WORD wVarFlags;
    
    public OaIdl.VARKIND varkind;
    
    public VARDESC() {}
    

    public static class _VARDESC
      extends Union
    {
      public NativeLong oInst;
      
      public Variant.VARIANT.ByReference lpvarValue;
      

      public _VARDESC()
      {
        setType("lpvarValue");
        read();
      }
      
      public _VARDESC(Pointer pointer) {
        super();
        setType("lpvarValue");
        read();
      }
      





      public _VARDESC(Variant.VARIANT.ByReference lpvarValue)
      {
        this.lpvarValue = lpvarValue;
        setType("lpvarValue");
      }
      

      public _VARDESC(NativeLong oInst)
      {
        this.oInst = oInst;
        setType("oInst");
      }
      
      public static class ByReference extends OaIdl.VARDESC._VARDESC implements Structure.ByReference {
        public ByReference() {}
      }
    }
    
    public VARDESC(Pointer pointer) {
      super();
      _vardesc.setType("lpvarValue");
      read();
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "memid", "lpstrSchema", "_vardesc", "elemdescVar", "wVarFlags", "varkind" });
    }
    


    public static class ByReference
      extends OaIdl.VARDESC
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  


  public static class ELEMDESC
    extends Structure
  {
    public OaIdl.TYPEDESC tdesc;
    

    public _ELEMDESC _elemdesc;
    


    public static class _ELEMDESC
      extends Union
    {
      public OaIdl.IDLDESC idldesc;
      

      public OaIdl.PARAMDESC paramdesc;
      


      public _ELEMDESC() {}
      

      public _ELEMDESC(Pointer pointer)
      {
        super();
        setType("paramdesc");
        read();
      }
      




      public _ELEMDESC(OaIdl.PARAMDESC paramdesc)
      {
        this.paramdesc = paramdesc;
        setType("paramdesc");
      }
      




      public _ELEMDESC(OaIdl.IDLDESC idldesc)
      {
        this.idldesc = idldesc;
        setType("idldesc");
      }
      
      public static class ByReference extends OaIdl.ELEMDESC._ELEMDESC implements Structure.ByReference { public ByReference() {}
      } }
    
    protected List getFieldOrder() { return Arrays.asList(new String[] { "tdesc", "_elemdesc" }); }
    

    public ELEMDESC() {}
    
    public ELEMDESC(Pointer pointer)
    {
      super();
      read();
    }
    

    public static class ByReference
      extends OaIdl.ELEMDESC
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class FUNCKIND
    extends Structure
  {
    public static final int FUNC_VIRTUAL = 0;
    public static final int FUNC_PUREVIRTUAL = 1;
    public static final int FUNC_NONVIRTUAL = 2;
    public static final int FUNC_STATIC = 3;
    public static final int FUNC_DISPATCH = 4;
    public int value;
    
    public FUNCKIND() {}
    
    public FUNCKIND(int value)
    {
      this.value = value;
    }
    

    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "value" });
    }
    
    public static class ByReference extends OaIdl.FUNCKIND implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class INVOKEKIND extends Structure {
    public static final INVOKEKIND INVOKE_FUNC = new INVOKEKIND(1);
    
    public static final INVOKEKIND INVOKE_PROPERTYGET = new INVOKEKIND(2);
    
    public static final INVOKEKIND INVOKE_PROPERTYPUT = new INVOKEKIND(4);
    
    public static final INVOKEKIND INVOKE_PROPERTYPUTREF = new INVOKEKIND(8);
    
    public int value;
    
    public INVOKEKIND() {}
    
    public INVOKEKIND(int value)
    {
      this.value = value;
    }
    

    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "value" });
    }
    

    public static class ByReference
      extends OaIdl.INVOKEKIND
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  

  public static class CALLCONV
    extends Structure
  {
    public static final int CC_FASTCALL = 0;
    
    public static final int CC_CDECL = 1;
    
    public static final int CC_MSCPASCAL = 2;
    
    public static final int CC_PASCAL = 2;
    
    public static final int CC_MACPASCAL = 3;
    
    public static final int CC_STDCALL = 4;
    public static final int CC_FPFASTCALL = 5;
    public static final int CC_SYSCALL = 6;
    public static final int CC_MPWCDECL = 7;
    public static final int CC_MPWPASCAL = 8;
    public static final int CC_MAX = 9;
    public int value;
    
    public CALLCONV() {}
    
    public CALLCONV(int value)
    {
      this.value = value;
    }
    
    protected List<String> getFieldOrder()
    {
      return Arrays.asList(new String[] { "value" });
    }
    
    public static class ByReference
      extends OaIdl.CALLCONV
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class VARKIND
    extends Structure
  {
    public static final int VAR_PERINSTANCE = 0;
    public static final int VAR_STATIC = 1;
    public static final int VAR_CONST = 2;
    public static final int VAR_DISPATCH = 3;
    public int value;
    
    public VARKIND() {}
    
    public VARKIND(int value)
    {
      this.value = value;
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "value" });
    }
    
    public static class ByReference extends OaIdl.VARKIND implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class TYPEDESC extends Structure {
    public _TYPEDESC _typedesc;
    public WTypes.VARTYPE vt;
    
    public TYPEDESC() {
      read();
    }
    
    public TYPEDESC(Pointer pointer) {
      super();
      read();
    }
    
    public TYPEDESC(_TYPEDESC _typedesc, WTypes.VARTYPE vt) {
      this._typedesc = _typedesc;
      this.vt = vt;
    }
    



    public static class _TYPEDESC
      extends Union
    {
      public OaIdl.TYPEDESC.ByReference lptdesc;
      

      public OaIdl.ARRAYDESC.ByReference lpadesc;
      

      public OaIdl.HREFTYPE hreftype;
      


      public _TYPEDESC()
      {
        setType("hreftype");
        read();
      }
      
      public _TYPEDESC(Pointer pointer) {
        super();
        setType("hreftype");
        read();
      }
      
      public OaIdl.TYPEDESC.ByReference getLptdesc() {
        setType("lptdesc");
        read();
        return lptdesc;
      }
      
      public OaIdl.ARRAYDESC.ByReference getLpadesc() {
        setType("lpadesc");
        read();
        return lpadesc;
      }
      
      public OaIdl.HREFTYPE getHreftype() {
        setType("hreftype");
        read();
        return hreftype;
      }
    }
    

    protected List getFieldOrder() { return Arrays.asList(new String[] { "_typedesc", "vt" }); }
    
    public static class ByReference extends OaIdl.TYPEDESC implements Structure.ByReference { public ByReference() {}
    }
  }
  
  public static class IDLDESC extends Structure { public BaseTSD.ULONG_PTR dwReserved;
    public WinDef.USHORT wIDLFlags;
    public IDLDESC() {}
    
    public static class ByReference extends OaIdl.IDLDESC implements Structure.ByReference { public ByReference() {}
      
      public ByReference(OaIdl.IDLDESC idldesc) { super(wIDLFlags); }
    }
    








    public IDLDESC(Pointer pointer)
    {
      super();
      read();
    }
    

    public IDLDESC(BaseTSD.ULONG_PTR dwReserved, WinDef.USHORT wIDLFlags)
    {
      this.dwReserved = dwReserved;
      this.wIDLFlags = wIDLFlags;
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwReserved", "wIDLFlags" });
    }
  }
  

  public static class ARRAYDESC
    extends Structure
  {
    public OaIdl.TYPEDESC tdescElem;
    
    public short cDims;
    
    public OaIdl.SAFEARRAYBOUND[] rgbounds = { new OaIdl.SAFEARRAYBOUND() };
    

    public ARRAYDESC() {}
    
    public ARRAYDESC(Pointer pointer)
    {
      super();
      read();
    }
    
    protected List getFieldOrder() {
      return Arrays.asList(new String[] { "tdescElem", "cDims", "rgbounds" });
    }
    








    public ARRAYDESC(OaIdl.TYPEDESC tdescElem, short cDims, OaIdl.SAFEARRAYBOUND[] rgbounds)
    {
      this.tdescElem = tdescElem;
      this.cDims = cDims;
      if (rgbounds.length != this.rgbounds.length)
        throw new IllegalArgumentException("Wrong array size !");
      this.rgbounds = rgbounds;
    }
    

    public static class ByReference
      extends OaIdl.ARRAYDESC
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  

  public static class PARAMDESC
    extends Structure
  {
    public Pointer pparamdescex;
    
    public WinDef.USHORT wParamFlags;
    
    public PARAMDESC() {}
    
    public PARAMDESC(Pointer pointer)
    {
      super();
      read();
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "pparamdescex", "wParamFlags" });
    }
    
    public static class ByReference extends OaIdl.PARAMDESC implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class PARAMDESCEX extends Structure
  {
    public WinDef.ULONG cBytes;
    public Variant.VariantArg varDefaultValue;
    
    public PARAMDESCEX() {}
    
    public PARAMDESCEX(Pointer pointer)
    {
      super();
      read();
    }
    


    protected List<String> getFieldOrder() { return Arrays.asList(new String[] { "cBytes", "varDefaultValue" }); }
    
    public static class ByReference extends OaIdl.PARAMDESCEX implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class HREFTYPE extends WinDef.DWORD {
    public HREFTYPE() {}
    
    public HREFTYPE(long value) { super(); }
  }
  
  public static class HREFTYPEByReference extends WinDef.DWORDByReference
  {
    public HREFTYPEByReference() {
      this(new OaIdl.HREFTYPE(0L));
    }
    
    public HREFTYPEByReference(WinDef.DWORD value) {
      super();
    }
    
    public void setValue(OaIdl.HREFTYPE value) {
      getPointer().setInt(0L, value.intValue());
    }
    
    public OaIdl.HREFTYPE getValue() {
      return new OaIdl.HREFTYPE(getPointer().getInt(0L));
    }
  }
  

  public static class TYPEATTR
    extends Structure
  {
    public Guid.GUID guid;
    
    public WinDef.LCID lcid;
    
    public WinDef.DWORD dwReserved;
    
    public OaIdl.MEMBERID memidConstructor;
    
    public OaIdl.MEMBERID memidDestructor;
    
    public WTypes.LPOLESTR lpstrSchema;
    
    public WinDef.ULONG cbSizeInstance;
    
    public OaIdl.TYPEKIND typekind;
    
    public WinDef.WORD cFuncs;
    
    public WinDef.WORD cVars;
    
    public WinDef.WORD cImplTypes;
    public WinDef.WORD cbSizeVft;
    public WinDef.WORD cbAlignment;
    public WinDef.WORD wTypeFlags;
    public WinDef.WORD wMajorVerNum;
    public WinDef.WORD wMinorVerNum;
    public OaIdl.TYPEDESC tdescAlias;
    public OaIdl.IDLDESC idldescType;
    
    public TYPEATTR() {}
    
    public TYPEATTR(Pointer pointer)
    {
      super();
      read();
    }
    
    protected List getFieldOrder() {
      return Arrays.asList(new String[] { "guid", "lcid", "dwReserved", "memidConstructor", "memidDestructor", "lpstrSchema", "cbSizeInstance", "typekind", "cFuncs", "cVars", "cImplTypes", "cbSizeVft", "cbAlignment", "wTypeFlags", "wMajorVerNum", "wMinorVerNum", "tdescAlias", "idldescType" });
    }
    
    public static class ByReference
      extends OaIdl.TYPEATTR
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
}
