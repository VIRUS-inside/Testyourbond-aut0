package net.sourceforge.htmlunit.corejs.javascript;

import java.io.Serializable;
import net.sourceforge.htmlunit.corejs.javascript.xml.XMLLib;














public class NativeGlobal
  implements Serializable, IdFunctionCall
{
  static final long serialVersionUID = 6080442165748707530L;
  private static final String URI_DECODE_RESERVED = ";/?:@&=+$,#";
  private static final int INVALID_UTF8 = Integer.MAX_VALUE;
  
  public NativeGlobal() {}
  
  public static void init(Context cx, Scriptable scope, boolean sealed)
  {
    NativeGlobal obj = new NativeGlobal();
    int arity;
    String name; for (int id = 1; id <= 13; id++)
    {
      arity = 1;
      String name; String name; String name; String name; String name; String name; String name; String name; String name; String name; String name; String name; switch (id) {
      case 1: 
        name = "decodeURI";
        break;
      case 2: 
        name = "decodeURIComponent";
        break;
      case 3: 
        name = "encodeURI";
        break;
      case 4: 
        name = "encodeURIComponent";
        break;
      case 5: 
        name = "escape";
        break;
      case 6: 
        name = "eval";
        break;
      case 7: 
        name = "isFinite";
        break;
      case 8: 
        name = "isNaN";
        break;
      case 9: 
        name = "isXMLName";
        break;
      case 10: 
        name = "parseFloat";
        break;
      case 11: 
        String name = "parseInt";
        arity = 2;
        break;
      case 12: 
        name = "unescape";
        break;
      case 13: 
        name = "uneval";
        break;
      default: 
        throw Kit.codeBug();
      }
      IdFunctionObject f = new IdFunctionObject(obj, FTAG, id, name, arity, scope);
      
      if (sealed) {
        f.sealObject();
      }
      f.exportAsScopeProperty();
    }
    
    ScriptableObject.defineProperty(scope, "NaN", ScriptRuntime.NaNobj, 7);
    
    ScriptableObject.defineProperty(scope, "Infinity", 
      ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7);
    
    ScriptableObject.defineProperty(scope, "undefined", Undefined.instance, 7);
    





    for (TopLevel.NativeErrors error : TopLevel.NativeErrors.values()) {
      if (error != TopLevel.NativeErrors.Error)
      {



        String name = error.name();
        
        ScriptableObject errorProto = (ScriptableObject)ScriptRuntime.newBuiltinObject(cx, scope, TopLevel.Builtins.Error, ScriptRuntime.emptyArgs);
        
        errorProto.put("name", errorProto, name);
        errorProto.put("message", errorProto, "");
        IdFunctionObject ctor = new IdFunctionObject(obj, FTAG, 14, name, 1, scope);
        
        ctor.markAsConstructor(errorProto);
        errorProto.put("constructor", errorProto, ctor);
        errorProto.setAttributes("constructor", 2);
        if (sealed) {
          errorProto.sealObject();
          ctor.sealObject();
        }
        ctor.exportAsScopeProperty();
      }
    }
  }
  
  public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
    if (f.hasTag(FTAG)) {
      int methodId = f.methodId();
      switch (methodId) {
      case 1: 
      case 2: 
        String str = ScriptRuntime.toString(args, 0);
        return decode(str, methodId == 1);
      

      case 3: 
      case 4: 
        String str = ScriptRuntime.toString(args, 0);
        return encode(str, methodId == 3);
      

      case 5: 
        return js_escape(args);
      
      case 6: 
        return js_eval(cx, scope, args);
      case 7: 
        boolean result;
        boolean result;
        if (args.length < 1) {
          result = false;
        } else {
          double d = ScriptRuntime.toNumber(args[0]);
          result = (d == d) && (d != Double.POSITIVE_INFINITY) && (d != Double.NEGATIVE_INFINITY);
        }
        
        return ScriptRuntime.wrapBoolean(result);
      case 8: 
        boolean result;
        
        boolean result;
        
        if (args.length < 1) {
          result = true;
        } else {
          double d = ScriptRuntime.toNumber(args[0]);
          result = d != d;
        }
        return ScriptRuntime.wrapBoolean(result);
      

      case 9: 
        Object name = args.length == 0 ? Undefined.instance : args[0];
        XMLLib xmlLib = XMLLib.extractFromScope(scope);
        return ScriptRuntime.wrapBoolean(xmlLib.isXMLName(cx, name));
      

      case 10: 
        return js_parseFloat(args);
      
      case 11: 
        return js_parseInt(args);
      
      case 12: 
        return js_unescape(args);
      
      case 13: 
        Object value = args.length != 0 ? args[0] : Undefined.instance;
        
        return ScriptRuntime.uneval(cx, scope, value);
      



      case 14: 
        return NativeError.make(cx, scope, f, args);
      }
    }
    throw f.unknown();
  }
  


  private Object js_parseInt(Object[] args)
  {
    String s = ScriptRuntime.toString(args, 0);
    int radix = ScriptRuntime.toInt32(args, 1);
    
    int len = s.length();
    if (len == 0) {
      return ScriptRuntime.NaNobj;
    }
    boolean negative = false;
    int start = 0;
    char c;
    do {
      c = s.charAt(start);
      if (!ScriptRuntime.isStrWhiteSpaceChar(c))
        break;
      start++;
    } while (start < len);
    
    if (c != '+') { if ((negative = c == '-' ? 1 : 0) == 0) {}
    } else { start++;
    }
    int NO_RADIX = -1;
    if (radix == 0) {
      radix = -1;
    } else { if ((radix < 2) || (radix > 36))
        return ScriptRuntime.NaNobj;
      if ((radix == 16) && (len - start > 1) && (s.charAt(start) == '0')) {
        c = s.charAt(start + 1);
        if ((c == 'x') || (c == 'X'))
          start += 2;
      }
    }
    if (radix == -1) {
      radix = 10;
      if ((len - start > 1) && (s.charAt(start) == '0')) {
        c = s.charAt(start + 1);
        if ((c == 'x') || (c == 'X')) {
          radix = 16;
          start += 2;
        } else if (('0' <= c) && (c <= '9') && 
          (!Context.getCurrentContext().hasFeature(110)))
        {
          radix = 8;
        }
      }
    }
    

    double d = ScriptRuntime.stringToNumber(s, start, radix);
    return ScriptRuntime.wrapNumber(negative ? -d : d);
  }
  





  private Object js_parseFloat(Object[] args)
  {
    if (args.length < 1) {
      return ScriptRuntime.NaNobj;
    }
    String s = ScriptRuntime.toString(args[0]);
    int len = s.length();
    int start = 0;
    char c;
    for (;;)
    {
      if (start == len) {
        return ScriptRuntime.NaNobj;
      }
      c = s.charAt(start);
      if (!ScriptRuntime.isStrWhiteSpaceChar(c)) {
        break;
      }
      start++;
    }
    
    int i = start;
    if ((c == '+') || (c == '-')) {
      i++;
      if (i == len) {
        return ScriptRuntime.NaNobj;
      }
      c = s.charAt(i);
    }
    
    if (c == 'I')
    {
      if ((i + 8 <= len) && (s.regionMatches(i, "Infinity", 0, 8))) { double d;
        double d;
        if (s.charAt(start) == '-') {
          d = Double.NEGATIVE_INFINITY;
        } else {
          d = Double.POSITIVE_INFINITY;
        }
        return ScriptRuntime.wrapNumber(d);
      }
      return ScriptRuntime.NaNobj;
    }
    

    int decimal = -1;
    int exponent = -1;
    boolean exponentValid = false;
    for (; i < len; i++) {
      switch (s.charAt(i)) {
      case '.': 
        if (decimal != -1)
          break label526;
        decimal = i;
        break;
      
      case 'E': 
      case 'e': 
        if (exponent != -1)
          break label526;
        if (i == len - 1) {
          break label526;
        }
        exponent = i;
        break;
      

      case '+': 
      case '-': 
        if (exponent != i - 1)
          break label526;
        if (i == len - 1)
          i--;
        break;
      


      case '0': 
      case '1': 
      case '2': 
      case '3': 
      case '4': 
      case '5': 
      case '6': 
      case '7': 
      case '8': 
      case '9': 
        if (exponent != -1) {
          exponentValid = true;
        }
        break;
      case ',': case '/': case ':': case ';': case '<': case '=': case '>': case '?': case '@': case 'A': case 'B': case 'C': case 'D': case 'F': case 'G': case 'H': case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': 
      case 'P': case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': case 'Z': case '[': case '\\': case ']': case '^': case '_': case '`': case 'a': case 'b': case 'c': case 'd': default: 
        break label526;
      }
    }
    label526:
    if ((exponent != -1) && (!exponentValid)) {
      i = exponent;
    }
    s = s.substring(start, i);
    try {
      return Double.valueOf(s);
    } catch (NumberFormatException ex) {}
    return ScriptRuntime.NaNobj;
  }
  








  private Object js_escape(Object[] args)
  {
    int URL_XALPHAS = 1;int URL_XPALPHAS = 2;int URL_PATH = 4;
    
    String s = ScriptRuntime.toString(args, 0);
    
    int mask = 7;
    if (args.length > 1) {
      double d = ScriptRuntime.toNumber(args[1]);
      if ((d != d) || ((mask = (int)d) != d) || (0 != (mask & 0xFFFFFFF8)))
      {
        throw Context.reportRuntimeError0("msg.bad.esc.mask");
      }
    }
    
    StringBuilder sb = null;
    int k = 0; for (int L = s.length(); k != L; k++) {
      int c = s.charAt(k);
      if ((mask != 0) && (((c >= 48) && (c <= 57)) || ((c >= 65) && (c <= 90)) || ((c >= 97) && (c <= 122)) || (c == 64) || (c == 42) || (c == 95) || (c == 45) || (c == 46) || ((0 != (mask & 0x4)) && ((c == 47) || (c == 43)))))
      {


        if (sb != null) {
          sb.append((char)c);
        }
      } else {
        if (sb == null) {
          sb = new StringBuilder(L + 3);
          sb.append(s);
          sb.setLength(k);
        }
        int hexSize;
        int hexSize;
        if (c < 256) {
          if ((c == 32) && (mask == 2)) {
            sb.append('+');
            continue;
          }
          sb.append('%');
          hexSize = 2;
        } else {
          sb.append('%');
          sb.append('u');
          hexSize = 4;
        }
        

        for (int shift = (hexSize - 1) * 4; shift >= 0; shift -= 4) {
          int digit = 0xF & c >> shift;
          int hc = digit < 10 ? 48 + digit : 55 + digit;
          sb.append((char)hc);
        }
      }
    }
    
    return sb == null ? s : sb.toString();
  }
  



  private Object js_unescape(Object[] args)
  {
    String s = ScriptRuntime.toString(args, 0);
    int firstEscapePos = s.indexOf('%');
    if (firstEscapePos >= 0) {
      int L = s.length();
      char[] buf = s.toCharArray();
      int destination = firstEscapePos;
      for (int k = firstEscapePos; k != L;) {
        char c = buf[k];
        k++;
        if ((c == '%') && (k != L)) { int end;
          int start;
          int end; if (buf[k] == 'u') {
            int start = k + 1;
            end = k + 5;
          } else {
            start = k;
            end = k + 2;
          }
          if (end <= L) {
            int x = 0;
            for (int i = start; i != end; i++) {
              x = Kit.xDigitToInt(buf[i], x);
            }
            if (x >= 0) {
              c = (char)x;
              k = end;
            }
          }
        }
        buf[destination] = c;
        destination++;
      }
      s = new String(buf, 0, destination);
    }
    return s;
  }
  



  private Object js_eval(Context cx, Scriptable scope, Object[] args)
  {
    Scriptable global = ScriptableObject.getTopLevelScope(scope);
    return ScriptRuntime.evalSpecial(cx, global, global, args, "eval code", 1);
  }
  
  static boolean isEvalFunction(Object functionObj)
  {
    if ((functionObj instanceof IdFunctionObject)) {
      IdFunctionObject function = (IdFunctionObject)functionObj;
      if ((function.hasTag(FTAG)) && (function.methodId() == 6)) {
        return true;
      }
    }
    return false;
  }
  




  @Deprecated
  public static EcmaError constructError(Context cx, String error, String message, Scriptable scope)
  {
    return ScriptRuntime.constructError(error, message);
  }
  






  @Deprecated
  public static EcmaError constructError(Context cx, String error, String message, Scriptable scope, String sourceName, int lineNumber, int columnNumber, String lineSource)
  {
    return ScriptRuntime.constructError(error, message, sourceName, lineNumber, lineSource, columnNumber);
  }
  






  private static String encode(String str, boolean fullUri)
  {
    byte[] utf8buf = null;
    StringBuilder sb = null;
    
    int k = 0; for (int length = str.length(); k != length; k++) {
      char C = str.charAt(k);
      if (encodeUnescaped(C, fullUri)) {
        if (sb != null) {
          sb.append(C);
        }
      } else {
        if (sb == null) {
          sb = new StringBuilder(length + 3);
          sb.append(str);
          sb.setLength(k);
          utf8buf = new byte[6];
        }
        if ((56320 <= C) && (C <= 57343))
          throw uriError();
        int V;
        int V;
        if ((C < 55296) || (56319 < C)) {
          V = C;
        } else {
          k++;
          if (k == length) {
            throw uriError();
          }
          char C2 = str.charAt(k);
          if ((56320 > C2) || (C2 > 57343)) {
            throw uriError();
          }
          V = (C - 55296 << 10) + (C2 - 56320) + 65536;
        }
        int L = oneUcs4ToUtf8Char(utf8buf, V);
        for (int j = 0; j < L; j++) {
          int d = 0xFF & utf8buf[j];
          sb.append('%');
          sb.append(toHexChar(d >>> 4));
          sb.append(toHexChar(d & 0xF));
        }
      }
    }
    return sb == null ? str : sb.toString();
  }
  
  private static char toHexChar(int i) {
    if (i >> 4 != 0)
      Kit.codeBug();
    return (char)(i < 10 ? i + 48 : i - 10 + 65);
  }
  
  private static int unHex(char c) {
    if (('A' <= c) && (c <= 'F'))
      return c - 'A' + 10;
    if (('a' <= c) && (c <= 'f'))
      return c - 'a' + 10;
    if (('0' <= c) && (c <= '9')) {
      return c - '0';
    }
    return -1;
  }
  
  private static int unHex(char c1, char c2)
  {
    int i1 = unHex(c1);
    int i2 = unHex(c2);
    if ((i1 >= 0) && (i2 >= 0)) {
      return i1 << 4 | i2;
    }
    return -1;
  }
  
  private static String decode(String str, boolean fullUri) {
    char[] buf = null;
    int bufTop = 0;
    
    int k = 0; for (int length = str.length(); k != length;) {
      char C = str.charAt(k);
      if (C != '%') {
        if (buf != null) {
          buf[(bufTop++)] = C;
        }
        k++;
      } else {
        if (buf == null)
        {

          buf = new char[length];
          str.getChars(0, k, buf, 0);
          bufTop = k;
        }
        int start = k;
        if (k + 3 > length)
          throw uriError();
        int B = unHex(str.charAt(k + 1), str.charAt(k + 2));
        if (B < 0)
          throw uriError();
        k += 3;
        if ((B & 0x80) == 0) {
          C = (char)B;

        }
        else
        {
          if ((B & 0xC0) == 128)
          {
            throw uriError(); }
          int minUcs4Char; if ((B & 0x20) == 0) {
            int utf8Tail = 1;
            int ucs4Char = B & 0x1F;
            minUcs4Char = 128; } else { int minUcs4Char;
            if ((B & 0x10) == 0) {
              int utf8Tail = 2;
              int ucs4Char = B & 0xF;
              minUcs4Char = 2048; } else { int minUcs4Char;
              if ((B & 0x8) == 0) {
                int utf8Tail = 3;
                int ucs4Char = B & 0x7;
                minUcs4Char = 65536; } else { int minUcs4Char;
                if ((B & 0x4) == 0) {
                  int utf8Tail = 4;
                  int ucs4Char = B & 0x3;
                  minUcs4Char = 2097152; } else { int minUcs4Char;
                  if ((B & 0x2) == 0) {
                    int utf8Tail = 5;
                    int ucs4Char = B & 0x1;
                    minUcs4Char = 67108864;
                  }
                  else {
                    throw uriError(); } } } } }
          int minUcs4Char;
          int ucs4Char; int utf8Tail; if (k + 3 * utf8Tail > length)
            throw uriError();
          for (int j = 0; j != utf8Tail; j++) {
            if (str.charAt(k) != '%')
              throw uriError();
            B = unHex(str.charAt(k + 1), str.charAt(k + 2));
            if ((B < 0) || ((B & 0xC0) != 128))
              throw uriError();
            ucs4Char = ucs4Char << 6 | B & 0x3F;
            k += 3;
          }
          
          if ((ucs4Char < minUcs4Char) || ((ucs4Char >= 55296) && (ucs4Char <= 57343)))
          {
            ucs4Char = Integer.MAX_VALUE;
          } else if ((ucs4Char == 65534) || (ucs4Char == 65535)) {
            ucs4Char = 65533;
          }
          if (ucs4Char >= 65536) {
            ucs4Char -= 65536;
            if (ucs4Char > 1048575) {
              throw uriError();
            }
            char H = (char)((ucs4Char >>> 10) + 55296);
            C = (char)((ucs4Char & 0x3FF) + 56320);
            buf[(bufTop++)] = H;
          } else {
            C = (char)ucs4Char;
          }
        }
        if ((fullUri) && (";/?:@&=+$,#".indexOf(C) >= 0)) {
          for (int x = start; x != k; x++) {
            buf[(bufTop++)] = str.charAt(x);
          }
        } else {
          buf[(bufTop++)] = C;
        }
      }
    }
    return buf == null ? str : new String(buf, 0, bufTop);
  }
  
  private static boolean encodeUnescaped(char c, boolean fullUri) {
    if ((('A' <= c) && (c <= 'Z')) || (('a' <= c) && (c <= 'z')) || (('0' <= c) && (c <= '9')))
    {
      return true;
    }
    if ("-_.!~*'()".indexOf(c) >= 0) {
      return true;
    }
    if (fullUri) {
      return ";/?:@&=+$,#".indexOf(c) >= 0;
    }
    return false;
  }
  
  private static EcmaError uriError() {
    return ScriptRuntime.constructError("URIError", 
      ScriptRuntime.getMessage0("msg.bad.uri"));
  }
  






  private static int oneUcs4ToUtf8Char(byte[] utf8Buffer, int ucs4Char)
  {
    int utf8Length = 1;
    

    if ((ucs4Char & 0xFFFFFF80) == 0) {
      utf8Buffer[0] = ((byte)ucs4Char);
    }
    else {
      int a = ucs4Char >>> 11;
      utf8Length = 2;
      while (a != 0) {
        a >>>= 5;
        utf8Length++;
      }
      int i = utf8Length;
      for (;;) { i--; if (i <= 0) break;
        utf8Buffer[i] = ((byte)(ucs4Char & 0x3F | 0x80));
        ucs4Char >>>= 6;
      }
      utf8Buffer[0] = ((byte)(256 - (1 << 8 - utf8Length) + ucs4Char));
    }
    return utf8Length;
  }
  
  private static final Object FTAG = "Global";
  private static final int Id_decodeURI = 1;
  private static final int Id_decodeURIComponent = 2;
  private static final int Id_encodeURI = 3;
  private static final int Id_encodeURIComponent = 4;
  private static final int Id_escape = 5;
  private static final int Id_eval = 6;
  private static final int Id_isFinite = 7;
  private static final int Id_isNaN = 8;
  private static final int Id_isXMLName = 9;
  private static final int Id_parseFloat = 10;
  private static final int Id_parseInt = 11;
  private static final int Id_unescape = 12;
  private static final int Id_uneval = 13;
  private static final int LAST_SCOPE_FUNCTION_ID = 13;
  private static final int Id_new_CommonError = 14;
}
