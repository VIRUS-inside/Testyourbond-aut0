package net.sourceforge.htmlunit.corejs.javascript;

import java.text.Collator;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import net.sourceforge.htmlunit.corejs.javascript.regexp.NativeRegExp;




















final class NativeString
  extends IdScriptableObject
{
  static final long serialVersionUID = 920268368584188687L;
  private static final Object STRING_TAG = "String";
  private static final int Id_length = 1;
  private static final int MAX_INSTANCE_ID = 1;
  
  static void init(Scriptable scope, boolean sealed)
  {
    NativeString obj = new NativeString("");
    obj.exportAsJSClass(45, scope, sealed);
  }
  
  private static final int ConstructorId_fromCharCode = -1;
  private static final int Id_constructor = 1;
  NativeString(CharSequence s)
  {
    string = s;
  }
  
  public String getClassName()
  {
    return "String";
  }
  
  private static final int Id_toString = 2;
  private static final int Id_toSource = 3;
  private static final int Id_valueOf = 4;
  private static final int Id_charAt = 5;
  protected int getMaxInstanceId()
  {
    return 1;
  }
  
  protected int findInstanceIdInfo(String s)
  {
    if (s.equals("length")) {
      return instanceIdInfo(7, 1);
    }
    return super.findInstanceIdInfo(s);
  }
  
  protected String getInstanceIdName(int id)
  {
    if (id == 1) {
      return "length";
    }
    return super.getInstanceIdName(id);
  }
  
  protected Object getInstanceIdValue(int id)
  {
    if (id == 1) {
      return ScriptRuntime.wrapInt(string.length());
    }
    return super.getInstanceIdValue(id);
  }
  
  protected void fillConstructorProperties(IdFunctionObject ctor)
  {
    addIdFunctionProperty(ctor, STRING_TAG, -1, "fromCharCode", 1);
    
    addIdFunctionProperty(ctor, STRING_TAG, -5, "charAt", 2);
    
    addIdFunctionProperty(ctor, STRING_TAG, -6, "charCodeAt", 2);
    
    addIdFunctionProperty(ctor, STRING_TAG, -7, "indexOf", 2);
    
    addIdFunctionProperty(ctor, STRING_TAG, -8, "lastIndexOf", 2);
    
    addIdFunctionProperty(ctor, STRING_TAG, -9, "split", 3);
    
    addIdFunctionProperty(ctor, STRING_TAG, -10, "substring", 3);
    
    addIdFunctionProperty(ctor, STRING_TAG, -11, "toLowerCase", 1);
    
    addIdFunctionProperty(ctor, STRING_TAG, -12, "toUpperCase", 1);
    
    addIdFunctionProperty(ctor, STRING_TAG, -13, "substr", 3);
    
    addIdFunctionProperty(ctor, STRING_TAG, -14, "concat", 2);
    
    addIdFunctionProperty(ctor, STRING_TAG, -15, "slice", 3);
    
    addIdFunctionProperty(ctor, STRING_TAG, -30, "equalsIgnoreCase", 2);
    
    addIdFunctionProperty(ctor, STRING_TAG, -31, "match", 2);
    
    addIdFunctionProperty(ctor, STRING_TAG, -32, "search", 2);
    
    addIdFunctionProperty(ctor, STRING_TAG, -33, "replace", 2);
    
    addIdFunctionProperty(ctor, STRING_TAG, -34, "localeCompare", 2);
    
    addIdFunctionProperty(ctor, STRING_TAG, -35, "toLocaleLowerCase", 1);
    
    super.fillConstructorProperties(ctor);
  }
  
  protected void initPrototypeId(int id)
  {
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    String s;
    switch (id) {
    case 1: 
      int arity = 1;
      s = "constructor";
      break;
    case 2: 
      int arity = 0;
      s = "toString";
      break;
    case 3: 
      int arity = 0;
      s = "toSource";
      break;
    case 4: 
      int arity = 0;
      s = "valueOf";
      break;
    case 5: 
      int arity = 1;
      s = "charAt";
      break;
    case 6: 
      int arity = 1;
      s = "charCodeAt";
      break;
    case 7: 
      int arity = 1;
      s = "indexOf";
      break;
    case 8: 
      int arity = 1;
      s = "lastIndexOf";
      break;
    case 9: 
      int arity = 2;
      s = "split";
      break;
    case 10: 
      int arity = 2;
      s = "substring";
      break;
    case 11: 
      int arity = 0;
      s = "toLowerCase";
      break;
    case 12: 
      int arity = 0;
      s = "toUpperCase";
      break;
    case 13: 
      int arity = 2;
      s = "substr";
      break;
    case 14: 
      int arity = 1;
      s = "concat";
      break;
    case 15: 
      int arity = 2;
      s = "slice";
      break;
    case 16: 
      int arity = 0;
      s = "bold";
      break;
    case 17: 
      int arity = 0;
      s = "italics";
      break;
    case 18: 
      int arity = 0;
      s = "fixed";
      break;
    case 19: 
      int arity = 0;
      s = "strike";
      break;
    case 20: 
      int arity = 0;
      s = "small";
      break;
    case 21: 
      int arity = 0;
      s = "big";
      break;
    case 22: 
      int arity = 0;
      s = "blink";
      break;
    case 23: 
      int arity = 0;
      s = "sup";
      break;
    case 24: 
      int arity = 0;
      s = "sub";
      break;
    case 25: 
      int arity = 0;
      s = "fontsize";
      break;
    case 26: 
      int arity = 0;
      s = "fontcolor";
      break;
    case 27: 
      int arity = 0;
      s = "link";
      break;
    case 28: 
      int arity = 0;
      s = "anchor";
      break;
    case 29: 
      int arity = 1;
      s = "equals";
      break;
    case 30: 
      int arity = 1;
      s = "equalsIgnoreCase";
      break;
    case 31: 
      int arity = 1;
      s = "match";
      break;
    case 32: 
      int arity = 1;
      s = "search";
      break;
    case 33: 
      int arity = 2;
      s = "replace";
      break;
    case 34: 
      int arity = 1;
      s = "localeCompare";
      break;
    case 35: 
      int arity = 0;
      s = "toLocaleLowerCase";
      break;
    case 36: 
      int arity = 0;
      s = "toLocaleUpperCase";
      break;
    case 37: 
      int arity = 0;
      s = "trim";
      break;
    case 38: 
      int arity = 0;
      s = "trimLeft";
      break;
    case 39: 
      int arity = 0;
      s = "trimRight";
      break;
    case 40: 
      int arity = 1;
      s = "includes";
      break;
    case 41: 
      int arity = 1;
      s = "startsWith";
      break;
    case 42: 
      int arity = 1;
      s = "endsWith";
      break;
    case 43: 
      int arity = 0;
      s = "normalize";
      break;
    case 44: 
      int arity = 1;
      s = "repeat";
      break;
    case 45: 
      int arity = 1;
      s = "codePointAt";
      break;
    default: 
      throw new IllegalArgumentException(String.valueOf(id)); }
    int arity;
    String s; initPrototypeMethod(STRING_TAG, id, s, arity);
  }
  private static final int Id_charCodeAt = 6;
  private static final int Id_indexOf = 7;
  private static final int Id_lastIndexOf = 8;
  private static final int Id_split = 9;
  
  public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if (!f.hasTag(STRING_TAG)) {
      return super.execIdCall(f, cx, scope, thisObj, args);
    }
    int id = f.methodId();
    for (;;) {
      switch (id) {
      case -35: 
      case -34: 
      case -33: 
      case -32: 
      case -31: 
      case -30: 
      case -15: 
      case -14: 
      case -13: 
      case -12: 
      case -11: 
      case -10: 
      case -9: 
      case -8: 
      case -7: 
      case -6: 
      case -5: 
        if (args.length > 0) {
          thisObj = ScriptRuntime.toObject(cx, scope, 
            ScriptRuntime.toCharSequence(args[0]));
          Object[] newArgs = new Object[args.length - 1];
          for (int i = 0; i < newArgs.length; i++)
            newArgs[i] = args[(i + 1)];
          args = newArgs;
        } else {
          thisObj = ScriptRuntime.toObject(cx, scope, 
            ScriptRuntime.toCharSequence(thisObj));
        }
        id = -id;
      }
      
    }
    
    int N = args.length;
    if (N < 1)
      return "";
    StringBuilder sb = new StringBuilder(N);
    for (int i = 0; i != N; i++) {
      sb.append(ScriptRuntime.toUint16(args[i]));
    }
    return sb.toString();
    



    CharSequence s = args.length >= 1 ? ScriptRuntime.toCharSequence(args[0]) : "";
    if (thisObj == null)
    {
      return new NativeString(s);
    }
    
    return (s instanceof String) ? s : s.toString();
    




    CharSequence cs = realThisstring;
    return (cs instanceof String) ? cs : cs.toString();
    

    CharSequence s = realThisstring;
    return "(new String(\"" + 
      ScriptRuntime.escapeString(s.toString()) + "\"))";
    




    CharSequence target = ScriptRuntime.toCharSequence(thisObj);
    double pos = ScriptRuntime.toInteger(args, 0);
    if ((pos < 0.0D) || (pos >= target.length())) {
      if (id == 5) {
        return "";
      }
      return ScriptRuntime.NaNobj;
    }
    char c = target.charAt((int)pos);
    if (id == 5) {
      return String.valueOf(c);
    }
    return ScriptRuntime.wrapInt(c);
    


    return ScriptRuntime.wrapInt(js_indexOf(7, 
      ScriptRuntime.toString(thisObj), args));
    




    String s = ScriptRuntime.toString(ScriptRuntime.requireObjectCoercible(thisObj, f));
    
    if ((args.length > 0) && ((args[0] instanceof NativeRegExp))) {
      throw ScriptRuntime.typeError2("msg.first.arg.not.regexp", String.class
        .getSimpleName(), f.getFunctionName());
    }
    
    int idx = js_indexOf(id, s, args);
    
    if (id == 40)
      return Boolean.valueOf(idx != -1);
    if (id == 41)
      return Boolean.valueOf(idx == 0);
    if (id == 42) {
      return Boolean.valueOf(idx != -1);
    }
    

    return ScriptRuntime.wrapInt(
      js_lastIndexOf(ScriptRuntime.toString(thisObj), args));
    

    return ScriptRuntime.checkRegExpProxy(cx).js_split(cx, scope, 
      ScriptRuntime.toString(thisObj), args);
    

    return js_substring(cx, ScriptRuntime.toCharSequence(thisObj), args);
    



    return ScriptRuntime.toString(thisObj)
      .toLowerCase(ScriptRuntime.ROOT_LOCALE);
    


    return ScriptRuntime.toString(thisObj)
      .toUpperCase(ScriptRuntime.ROOT_LOCALE);
    

    return js_substr(ScriptRuntime.toCharSequence(thisObj), args);
    

    return js_concat(ScriptRuntime.toString(thisObj), args);
    

    return js_slice(ScriptRuntime.toCharSequence(thisObj), args);
    

    return tagify(thisObj, "b", null, null);
    

    return tagify(thisObj, "i", null, null);
    

    return tagify(thisObj, "tt", null, null);
    

    return tagify(thisObj, "strike", null, null);
    

    return tagify(thisObj, "small", null, null);
    

    return tagify(thisObj, "big", null, null);
    

    return tagify(thisObj, "blink", null, null);
    

    return tagify(thisObj, "sup", null, null);
    

    return tagify(thisObj, "sub", null, null);
    

    return tagify(thisObj, "font", "size", args);
    

    return tagify(thisObj, "font", "color", args);
    

    return tagify(thisObj, "a", "href", args);
    

    return tagify(thisObj, "a", "name", args);
    


    String s1 = ScriptRuntime.toString(thisObj);
    String s2 = ScriptRuntime.toString(args, 0);
    return ScriptRuntime.wrapBoolean(id == 29 ? s1
      .equals(s2) : s1.equalsIgnoreCase(s2));
    

    int actionType;
    
    int actionType;
    
    if (id == 31) {
      actionType = 1; } else { int actionType;
      if (id == 32) {
        actionType = 3;
      } else
        actionType = 2;
    }
    return ScriptRuntime.checkRegExpProxy(cx).action(cx, scope, thisObj, args, actionType);
    







    Collator collator = Collator.getInstance(cx.getLocale());
    collator.setStrength(3);
    collator.setDecomposition(1);
    return ScriptRuntime.wrapNumber(collator
      .compare(ScriptRuntime.toString(thisObj), 
      ScriptRuntime.toString(args, 0)));
    

    return ScriptRuntime.toString(thisObj)
      .toLowerCase(cx.getLocale());
    

    return ScriptRuntime.toString(thisObj)
      .toUpperCase(cx.getLocale());
    

    String str = ScriptRuntime.toString(thisObj);
    char[] chars = str.toCharArray();
    
    int start = 0;
    while ((start < chars.length) && 
      (ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[start]))) {
      start++;
    }
    int end = chars.length;
    while ((end > start) && 
      (ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[(end - 1)]))) {
      end--;
    }
    
    return str.substring(start, end);
    

    String str = ScriptRuntime.toString(thisObj);
    char[] chars = str.toCharArray();
    
    int start = 0;
    while ((start < chars.length) && 
      (ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[start]))) {
      start++;
    }
    int end = chars.length;
    
    return str.substring(start, end);
    

    String str = ScriptRuntime.toString(thisObj);
    char[] chars = str.toCharArray();
    
    int start = 0;
    
    int end = chars.length;
    while ((end > start) && 
      (ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[(end - 1)]))) {
      end--;
    }
    
    return str.substring(start, end);
    

    String formStr = ScriptRuntime.toString(args, 0);
    
    Normalizer.Form form;
    if (Normalizer.Form.NFD.name().equals(formStr)) {
      form = Normalizer.Form.NFD; } else { Normalizer.Form form;
      if (Normalizer.Form.NFKC.name().equals(formStr)) {
        form = Normalizer.Form.NFKC; } else { Normalizer.Form form;
        if (Normalizer.Form.NFKD.name().equals(formStr)) {
          form = Normalizer.Form.NFKD; } else { Normalizer.Form form;
          if ((Normalizer.Form.NFC.name().equals(formStr)) || (args.length == 0))
          {
            form = Normalizer.Form.NFC;
          } else
            throw ScriptRuntime.rangeError("The normalization form should be one of NFC, NFD, NFKC, NFKD");
        } } }
    Normalizer.Form form;
    return Normalizer.normalize(
      ScriptRuntime.toString(ScriptRuntime.requireObjectCoercible(thisObj, f)), form);
    



    String str = ScriptRuntime.toString(ScriptRuntime.requireObjectCoercible(thisObj, f));
    double cnt = ScriptRuntime.toInteger(args, 0);
    
    if (cnt == 0.0D) {
      return "";
    }
    
    if ((cnt < 0.0D) || (cnt == Double.POSITIVE_INFINITY)) {
      throw ScriptRuntime.rangeError("Invalid count value");
    }
    long size = str.length() * cnt;
    
    if ((cnt >= 2.147483647E9D) || (size >= 2147483647L)) {
      size = 2147483647L;
    }
    
    StringBuilder retval = new StringBuilder((int)size);
    retval.append(str);
    
    int i = 1;
    while (i <= cnt / 2.0D) {
      retval.append(retval);
      i *= 2;
    }
    while (i++ < cnt) {
      retval.append(str);
    }
    return retval.toString();
    


    String str = ScriptRuntime.toString(ScriptRuntime.requireObjectCoercible(thisObj, f));
    double cnt = ScriptRuntime.toInteger(args, 0);
    
    return (cnt < 0.0D) || (cnt >= str.length()) ? Undefined.instance : 
      Integer.valueOf(str.codePointAt((int)cnt));
    


    throw new IllegalArgumentException("String.prototype has no method: " + f.getFunctionName());
  }
  
  private static final int Id_substring = 10;
  private static final int Id_toLowerCase = 11;
  private static final int Id_toUpperCase = 12;
  
  private static NativeString realThis(Scriptable thisObj, IdFunctionObject f)
  {
    if (!(thisObj instanceof NativeString))
      throw incompatibleCallError(f);
    return (NativeString)thisObj;
  }
  
  private static final int Id_substr = 13;
  private static final int Id_concat = 14;
  private static final int Id_slice = 15;
  
  private static String tagify(Object thisObj, String tag, String attribute, Object[] args)
  {
    String str = ScriptRuntime.toString(thisObj);
    StringBuilder result = new StringBuilder();
    result.append('<');
    result.append(tag);
    if (attribute != null) {
      result.append(' ');
      result.append(attribute);
      result.append("=\"");
      result.append(ScriptRuntime.toString(args, 0));
      result.append('"');
    }
    result.append('>');
    result.append(str);
    result.append("</");
    result.append(tag);
    result.append('>');
    return result.toString();
  }
  
  private static final int Id_bold = 16;
  
  public CharSequence toCharSequence()
  {
    return string;
  }
  
  private static final int Id_italics = 17;
  private static final int Id_fixed = 18;
  
  public String toString()
  {
    return (string instanceof String) ? (String)string : string.toString();
  }
  
  private static final int Id_strike = 19;
  private static final int Id_small = 20;
  private static final int Id_big = 21;
  private static final int Id_blink = 22;
  private static final int Id_sup = 23;
  private static final int Id_sub = 24;
  
  public Object get(int index, Scriptable start)
  {
    if ((0 <= index) && (index < string.length())) {
      return String.valueOf(string.charAt(index));
    }
    return super.get(index, start);
  }
  
  private static final int Id_fontsize = 25;
  private static final int Id_fontcolor = 26;
  
  public void put(int index, Scriptable start, Object value)
  {
    if ((0 <= index) && (index < string.length())) {
      return;
    }
    super.put(index, start, value);
  }
  
  private static final int Id_link = 27;
  private static final int Id_anchor = 28;
  private static final int Id_equals = 29;
  private static final int Id_equalsIgnoreCase = 30;
  
  private static int js_indexOf(int methodId, String target, Object[] args)
  {
    String searchStr = ScriptRuntime.toString(args, 0);
    double position = ScriptRuntime.toInteger(args, 1);
    
    if ((position > target.length()) && (methodId != 41) && (methodId != 42))
    {
      return -1;
    }
    if (position < 0.0D) {
      position = 0.0D;
    } else if (position > target.length()) {
      position = target.length();
    } else if ((methodId == 42) && ((position != position) || 
      (position > target.length()))) {
      position = target.length();
    }
    if (42 == methodId) {
      if ((args.length == 0) || (args.length == 1) || ((args.length == 2) && (args[1] == Undefined.instance)))
      {
        position = target.length(); }
      return target.substring(0, (int)position).endsWith(searchStr) ? 0 : -1;
    }
    
    return methodId == 41 ? 
      -1 : target.startsWith(searchStr, (int)position) ? 0 : target
      .indexOf(searchStr, (int)position);
  }
  
  private static final int Id_match = 31;
  private static final int Id_search = 32;
  private static final int Id_replace = 33;
  private static final int Id_localeCompare = 34;
  private static final int Id_toLocaleLowerCase = 35;
  
  private static int js_lastIndexOf(String target, Object[] args)
  {
    String search = ScriptRuntime.toString(args, 0);
    double end = ScriptRuntime.toNumber(args, 1);
    
    if ((end != end) || (end > target.length())) {
      end = target.length();
    } else if (end < 0.0D) {
      end = 0.0D;
    }
    return target.lastIndexOf(search, (int)end);
  }
  
  private static final int Id_toLocaleUpperCase = 36;
  private static final int Id_trim = 37;
  private static final int Id_trimLeft = 38;
  private static final int Id_trimRight = 39;
  private static final int Id_includes = 40;
  
  private static CharSequence js_substring(Context cx, CharSequence target, Object[] args)
  {
    int length = target.length();
    double start = ScriptRuntime.toInteger(args, 0);
    

    if (start < 0.0D) {
      start = 0.0D;
    } else if (start > length)
      start = length;
    double end;
    double end; if ((args.length <= 1) || (args[1] == Undefined.instance)) {
      end = length;
    } else {
      end = ScriptRuntime.toInteger(args[1]);
      if (end < 0.0D) {
        end = 0.0D;
      } else if (end > length) {
        end = length;
      }
      
      if (end < start) {
        if (cx.getLanguageVersion() != 120) {
          double temp = start;
          start = end;
          end = temp;
        }
        else {
          end = start;
        }
      }
    }
    return target.subSequence((int)start, (int)end);
  }
  
  private static final int Id_startsWith = 41;
  
  int getLength()
  {
    return string.length(); }
  
  private static final int Id_endsWith = 42;
  private static final int Id_normalize = 43;
  private static final int Id_repeat = 44;
  
  private static CharSequence js_substr(CharSequence target, Object[] args) {
    if (args.length < 1) {
      return target;
    }
    double begin = ScriptRuntime.toInteger(args[0]);
    
    int length = target.length();
    
    if (begin < 0.0D) {
      begin += length;
      if (begin < 0.0D)
        begin = 0.0D;
    } else if (begin > length) {
      begin = length; }
    double end;
    double end;
    if (args.length == 1) {
      end = length;
    } else {
      end = ScriptRuntime.toInteger(args[1]);
      if (end < 0.0D)
        end = 0.0D;
      end += begin;
      if (end > length) {
        end = length;
      }
    }
    return target.subSequence((int)begin, (int)end); }
  
  private static final int Id_codePointAt = 45;
  private static final int MAX_PROTOTYPE_ID = 45;
  private static final int ConstructorId_charAt = -5;
  private static final int ConstructorId_charCodeAt = -6;
  private static final int ConstructorId_indexOf = -7;
  private static String js_concat(String target, Object[] args) { int N = args.length;
    if (N == 0)
      return target;
    if (N == 1) {
      String arg = ScriptRuntime.toString(args[0]);
      return target.concat(arg);
    }
    


    int size = target.length();
    String[] argsAsStrings = new String[N];
    for (int i = 0; i != N; i++) {
      String s = ScriptRuntime.toString(args[i]);
      argsAsStrings[i] = s;
      size += s.length();
    }
    
    StringBuilder result = new StringBuilder(size);
    result.append(target);
    for (int i = 0; i != N; i++) {
      result.append(argsAsStrings[i]);
    }
    return result.toString(); }
  
  private static final int ConstructorId_lastIndexOf = -8;
  
  private static CharSequence js_slice(CharSequence target, Object[] args) { double begin = args.length < 1 ? 0.0D : ScriptRuntime.toInteger(args[0]);
    
    int length = target.length();
    if (begin < 0.0D) {
      begin += length;
      if (begin < 0.0D)
        begin = 0.0D;
    } else if (begin > length) {
      begin = length; }
    double end;
    double end;
    if ((args.length < 2) || (args[1] == Undefined.instance)) {
      end = length;
    } else {
      end = ScriptRuntime.toInteger(args[1]);
      if (end < 0.0D) {
        end += length;
        if (end < 0.0D)
          end = 0.0D;
      } else if (end > length) {
        end = length;
      }
      if (end < begin)
        end = begin;
    }
    return target.subSequence((int)begin, (int)end);
  }
  
  private static final int ConstructorId_split = -9;
  private static final int ConstructorId_substring = -10;
  private static final int ConstructorId_toLowerCase = -11;
  private static final int ConstructorId_toUpperCase = -12;
  private static final int ConstructorId_substr = -13;
  
  protected int findPrototypeId(String s) {
    int id = 0;
    String X = null;
    
    switch (s.length()) {
    case 3: 
      int c = s.charAt(2);
      if (c == 98) {
        if ((s.charAt(0) == 's') && (s.charAt(1) == 'u')) {
          id = 24;
          return id;
        }
      } else if (c == 103) {
        if ((s.charAt(0) == 'b') && (s.charAt(1) == 'i')) {
          id = 21;
          return id;
        }
      } else if ((c == 112) && 
        (s.charAt(0) == 's') && (s.charAt(1) == 'u'))
        id = 23;
      break;
    


    case 4: 
      int c = s.charAt(0);
      if (c == 98) {
        X = "bold";
        id = 16;
      } else if (c == 108) {
        X = "link";
        id = 27;
      } else if (c == 116) {
        X = "trim";
        id = 37;
      }
      break;
    case 5: 
      switch (s.charAt(4)) {
      case 'd': 
        X = "fixed";
        id = 18;
        break;
      case 'e': 
        X = "slice";
        id = 15;
        break;
      case 'h': 
        X = "match";
        id = 31;
        break;
      case 'k': 
        X = "blink";
        id = 22;
        break;
      case 'l': 
        X = "small";
        id = 20;
        break;
      case 't': 
        X = "split";
        id = 9;
      }
      
      break;
    case 6: 
      switch (s.charAt(1)) {
      case 'e': 
        int c = s.charAt(0);
        if (c == 114) {
          X = "repeat";
          id = 44;
        } else if (c == 115) {
          X = "search";
          id = 32;
        }
        break;
      case 'h': 
        X = "charAt";
        id = 5;
        break;
      case 'n': 
        X = "anchor";
        id = 28;
        break;
      case 'o': 
        X = "concat";
        id = 14;
        break;
      case 'q': 
        X = "equals";
        id = 29;
        break;
      case 't': 
        X = "strike";
        id = 19;
        break;
      case 'u': 
        X = "substr";
        id = 13;
      }
      
      break;
    case 7: 
      switch (s.charAt(1)) {
      case 'a': 
        X = "valueOf";
        id = 4;
        break;
      case 'e': 
        X = "replace";
        id = 33;
        break;
      case 'n': 
        X = "indexOf";
        id = 7;
        break;
      case 't': 
        X = "italics";
        id = 17;
      }
      
      break;
    case 8: 
      switch (s.charAt(6)) {
      case 'c': 
        X = "toSource";
        id = 3;
        break;
      case 'e': 
        X = "includes";
        id = 40;
        break;
      case 'f': 
        X = "trimLeft";
        id = 38;
        break;
      case 'n': 
        X = "toString";
        id = 2;
        break;
      case 't': 
        X = "endsWith";
        id = 42;
        break;
      case 'z': 
        X = "fontsize";
        id = 25;
      }
      
      break;
    case 9: 
      switch (s.charAt(0)) {
      case 'f': 
        X = "fontcolor";
        id = 26;
        break;
      case 'n': 
        X = "normalize";
        id = 43;
        break;
      case 's': 
        X = "substring";
        id = 10;
        break;
      case 't': 
        X = "trimRight";
        id = 39;
      }
      
      break;
    case 10: 
      int c = s.charAt(0);
      if (c == 99) {
        X = "charCodeAt";
        id = 6;
      } else if (c == 115) {
        X = "startsWith";
        id = 41;
      }
      break;
    case 11: 
      switch (s.charAt(2)) {
      case 'L': 
        X = "toLowerCase";
        id = 11;
        break;
      case 'U': 
        X = "toUpperCase";
        id = 12;
        break;
      case 'd': 
        X = "codePointAt";
        id = 45;
        break;
      case 'n': 
        X = "constructor";
        id = 1;
        break;
      case 's': 
        X = "lastIndexOf";
        id = 8;
      }
      
      break;
    case 13: 
      X = "localeCompare";
      id = 34;
      break;
    case 16: 
      X = "equalsIgnoreCase";
      id = 30;
      break;
    case 17: 
      int c = s.charAt(8);
      if (c == 76) {
        X = "toLocaleLowerCase";
        id = 35;
      } else if (c == 85) {
        X = "toLocaleUpperCase";
        id = 36;
      }
      break;
    }
    if ((X != null) && (X != s) && (!X.equals(s))) {
      id = 0;
    }
    

    return id;
  }
  
  private static final int ConstructorId_concat = -14;
  private static final int ConstructorId_slice = -15;
  private static final int ConstructorId_equalsIgnoreCase = -30;
  private static final int ConstructorId_match = -31;
  private static final int ConstructorId_search = -32;
  private static final int ConstructorId_replace = -33;
  private static final int ConstructorId_localeCompare = -34;
  private static final int ConstructorId_toLocaleLowerCase = -35;
  private CharSequence string;
}
