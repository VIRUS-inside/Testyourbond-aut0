package net.sourceforge.htmlunit.corejs.javascript.regexp;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.TopLevel.Builtins;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

class NativeRegExpCtor
  extends BaseFunction
{
  static final long serialVersionUID = -5733330028285400526L;
  private static final int Id_multiline = 1;
  private static final int Id_STAR = 2;
  private static final int Id_input = 3;
  private static final int Id_UNDERSCORE = 4;
  private static final int Id_lastMatch = 5;
  private static final int Id_AMPERSAND = 6;
  private static final int Id_lastParen = 7;
  private static final int Id_PLUS = 8;
  private static final int Id_leftContext = 9;
  private static final int Id_BACK_QUOTE = 10;
  private static final int Id_rightContext = 11;
  
  NativeRegExpCtor() {}
  
  public String getFunctionName()
  {
    return "RegExp";
  }
  
  public int getLength()
  {
    return 2;
  }
  
  public int getArity()
  {
    return 2;
  }
  

  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if ((args.length > 0) && ((args[0] instanceof NativeRegExp)) && ((args.length == 1) || (args[1] == Undefined.instance)))
    {
      return args[0];
    }
    return construct(cx, scope, args);
  }
  
  public Scriptable construct(Context cx, Scriptable scope, Object[] args)
  {
    NativeRegExp re = new NativeRegExp();
    re.compile(cx, scope, args);
    ScriptRuntime.setBuiltinProtoAndParent(re, scope, TopLevel.Builtins.RegExp);
    
    return re;
  }
  
  private static RegExpImpl getImpl() {
    Context cx = Context.getCurrentContext();
    return (RegExpImpl)ScriptRuntime.getRegExpProxy(cx);
  }
  


  private static final int Id_QUOTE = 12;
  

  private static final int DOLLAR_ID_BASE = 12;
  

  private static final int Id_DOLLAR_1 = 13;
  
  private static final int Id_DOLLAR_2 = 14;
  
  private static final int Id_DOLLAR_3 = 15;
  
  private static final int Id_DOLLAR_4 = 16;
  
  private static final int Id_DOLLAR_5 = 17;
  
  private static final int Id_DOLLAR_6 = 18;
  
  private static final int Id_DOLLAR_7 = 19;
  
  private static final int Id_DOLLAR_8 = 20;
  
  private static final int Id_DOLLAR_9 = 21;
  
  private static final int MAX_INSTANCE_ID = 21;
  
  protected int getMaxInstanceId()
  {
    return super.getMaxInstanceId() + 21;
  }
  



  protected int findInstanceIdInfo(String s)
  {
    int id = 0;
    String X = null;
    
    switch (s.length()) {
    case 2: 
      switch (s.charAt(1)) {
      case '&': 
        if (s.charAt(0) == '$')
          id = 6;
        break;
      

      case '\'': 
        if (s.charAt(0) == '$')
          id = 12;
        break;
      

      case '*': 
        if (s.charAt(0) == '$')
          id = 2;
        break;
      

      case '+': 
        if (s.charAt(0) == '$')
          id = 8;
        break;
      

      case '1': 
        if (s.charAt(0) == '$')
          id = 13;
        break;
      

      case '2': 
        if (s.charAt(0) == '$')
          id = 14;
        break;
      

      case '3': 
        if (s.charAt(0) == '$')
          id = 15;
        break;
      

      case '4': 
        if (s.charAt(0) == '$')
          id = 16;
        break;
      

      case '5': 
        if (s.charAt(0) == '$')
          id = 17;
        break;
      

      case '6': 
        if (s.charAt(0) == '$')
          id = 18;
        break;
      

      case '7': 
        if (s.charAt(0) == '$')
          id = 19;
        break;
      

      case '8': 
        if (s.charAt(0) == '$')
          id = 20;
        break;
      

      case '9': 
        if (s.charAt(0) == '$')
          id = 21;
        break;
      

      case '_': 
        if (s.charAt(0) == '$')
          id = 4;
        break;
      

      case '`': 
        if (s.charAt(0) == '$') {
          id = 10;
          break label663;
        }
        break;
      }
      break;
    case 5: 
      X = "input";
      id = 3;
      break;
    case 9: 
      int c = s.charAt(4);
      if (c == 77) {
        X = "lastMatch";
        id = 5;
      } else if (c == 80) {
        X = "lastParen";
        id = 7;
      } else if (c == 105) {
        X = "multiline";
        id = 1;
      }
      break;
    case 11: 
      X = "leftContext";
      id = 9;
      break;
    case 12: 
      X = "rightContext";
      id = 11;
      break;
    }
    if ((X != null) && (X != s) && (!X.equals(s))) {
      id = 0;
    }
    
    label663:
    if (id == 0)
      return super.findInstanceIdInfo(s);
    int attr;
    int attr;
    int attr; int attr; int attr; switch (id) {
    case 1: 
      attr = multilineAttr;
      break;
    case 2: 
      attr = starAttr;
      break;
    case 3: 
      attr = inputAttr;
      break;
    case 4: 
      attr = underscoreAttr;
      break;
    default: 
      attr = 5;
    }
    
    
    return instanceIdInfo(attr, super.getMaxInstanceId() + id);
  }
  


  protected String getInstanceIdName(int id)
  {
    int shifted = id - super.getMaxInstanceId();
    if ((1 <= shifted) && (shifted <= 21)) {
      switch (shifted) {
      case 1: 
        return "multiline";
      case 2: 
        return "$*";
      
      case 3: 
        return "input";
      case 4: 
        return "$_";
      
      case 5: 
        return "lastMatch";
      case 6: 
        return "$&";
      
      case 7: 
        return "lastParen";
      case 8: 
        return "$+";
      
      case 9: 
        return "leftContext";
      case 10: 
        return "$`";
      
      case 11: 
        return "rightContext";
      case 12: 
        return "$'";
      }
      
      int substring_number = shifted - 12 - 1;
      char[] buf = { '$', (char)(49 + substring_number) };
      return new String(buf);
    }
    return super.getInstanceIdName(id);
  }
  
  protected Object getInstanceIdValue(int id)
  {
    int shifted = id - super.getMaxInstanceId();
    if ((1 <= shifted) && (shifted <= 21)) {
      RegExpImpl impl = getImpl();
      Object stringResult;
      Object stringResult; Object stringResult; Object stringResult; Object stringResult; Object stringResult; switch (shifted) {
      case 1: 
      case 2: 
        return ScriptRuntime.wrapBoolean(multiline);
      
      case 3: 
      case 4: 
        stringResult = input;
        break;
      
      case 5: 
      case 6: 
        stringResult = lastMatch;
        break;
      
      case 7: 
      case 8: 
        stringResult = lastParen;
        break;
      
      case 9: 
      case 10: 
        stringResult = leftContext;
        break;
      
      case 11: 
      case 12: 
        stringResult = rightContext;
        break;
      

      default: 
        int substring_number = shifted - 12 - 1;
        stringResult = impl.getParenSubString(substring_number);
        break;
      }
      
      return stringResult == null ? "" : stringResult.toString();
    }
    return super.getInstanceIdValue(id);
  }
  
  protected void setInstanceIdValue(int id, Object value)
  {
    int shifted = id - super.getMaxInstanceId();
    switch (shifted) {
    case 1: 
    case 2: 
      getImplmultiline = ScriptRuntime.toBoolean(value);
      return;
    
    case 3: 
    case 4: 
      getImplinput = ScriptRuntime.toString(value);
      return;
    
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    case 12: 
      return;
    }
    int substring_number = shifted - 12 - 1;
    if ((0 <= substring_number) && (substring_number <= 8)) {
      return;
    }
    
    super.setInstanceIdValue(id, value);
  }
  
  protected void setInstanceIdAttributes(int id, int attr)
  {
    int shifted = id - super.getMaxInstanceId();
    switch (shifted) {
    case 1: 
      multilineAttr = attr;
      return;
    case 2: 
      starAttr = attr;
      return;
    case 3: 
      inputAttr = attr;
      return;
    case 4: 
      underscoreAttr = attr;
      return;
    

    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    case 12: 
      return;
    }
    int substring_number = shifted - 12 - 1;
    if ((0 <= substring_number) && (substring_number <= 8))
    {
      return;
    }
    
    super.setInstanceIdAttributes(id, attr);
  }
  
  private int multilineAttr = 4;
  private int starAttr = 4;
  private int inputAttr = 4;
  private int underscoreAttr = 4;
}
