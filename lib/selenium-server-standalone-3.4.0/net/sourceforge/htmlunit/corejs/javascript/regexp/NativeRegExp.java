package net.sourceforge.htmlunit.corejs.javascript.regexp;

import java.io.Serializable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.IdFunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.IdScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Kit;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.TopLevel.Builtins;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;



















public class NativeRegExp
  extends IdScriptableObject
  implements Function
{
  static final long serialVersionUID = 4965263491464903264L;
  private static final Object REGEXP_TAG = new Serializable() {};
  

  public static final int JSREG_GLOB = 1;
  

  public static final int JSREG_FOLD = 2;
  

  public static final int JSREG_MULTILINE = 4;
  

  public static final int TEST = 0;
  

  public static final int MATCH = 1;
  

  public static final int PREFIX = 2;
  

  private static final boolean debug = false;
  

  private static final byte REOP_SIMPLE_START = 1;
  

  private static final byte REOP_EMPTY = 1;
  

  private static final byte REOP_BOL = 2;
  

  private static final byte REOP_EOL = 3;
  

  private static final byte REOP_WBDRY = 4;
  

  private static final byte REOP_WNONBDRY = 5;
  

  private static final byte REOP_DOT = 6;
  

  private static final byte REOP_DIGIT = 7;
  

  private static final byte REOP_NONDIGIT = 8;
  

  private static final byte REOP_ALNUM = 9;
  

  private static final byte REOP_NONALNUM = 10;
  

  private static final byte REOP_SPACE = 11;
  

  private static final byte REOP_NONSPACE = 12;
  

  private static final byte REOP_BACKREF = 13;
  

  private static final byte REOP_FLAT = 14;
  

  private static final byte REOP_FLAT1 = 15;
  

  private static final byte REOP_FLATi = 16;
  

  private static final byte REOP_FLAT1i = 17;
  

  private static final byte REOP_UCFLAT1 = 18;
  

  private static final byte REOP_UCFLAT1i = 19;
  
  private static final byte REOP_CLASS = 22;
  
  private static final byte REOP_NCLASS = 23;
  
  private static final byte REOP_SIMPLE_END = 23;
  
  private static final byte REOP_QUANT = 25;
  
  private static final byte REOP_STAR = 26;
  
  private static final byte REOP_PLUS = 27;
  
  private static final byte REOP_OPT = 28;
  
  private static final byte REOP_LPAREN = 29;
  
  private static final byte REOP_RPAREN = 30;
  
  private static final byte REOP_ALT = 31;
  
  private static final byte REOP_JUMP = 32;
  
  private static final byte REOP_ASSERT = 41;
  
  private static final byte REOP_ASSERT_NOT = 42;
  
  private static final byte REOP_ASSERTTEST = 43;
  
  private static final byte REOP_ASSERTNOTTEST = 44;
  
  private static final byte REOP_MINIMALSTAR = 45;
  
  private static final byte REOP_MINIMALPLUS = 46;
  
  private static final byte REOP_MINIMALOPT = 47;
  
  private static final byte REOP_MINIMALQUANT = 48;
  
  private static final byte REOP_ENDCHILD = 49;
  
  private static final byte REOP_REPEAT = 51;
  
  private static final byte REOP_MINIMALREPEAT = 52;
  
  private static final byte REOP_ALTPREREQ = 53;
  
  private static final byte REOP_ALTPREREQi = 54;
  
  private static final byte REOP_ALTPREREQ2 = 55;
  
  private static final byte REOP_END = 57;
  
  private static final int ANCHOR_BOL = -2;
  
  private static final int INDEX_LEN = 2;
  
  private static final int Id_lastIndex = 1;
  
  private static final int Id_source = 2;
  
  private static final int Id_global = 3;
  
  private static final int Id_ignoreCase = 4;
  
  private static final int Id_multiline = 5;
  
  private static final int MAX_INSTANCE_ID = 5;
  
  private static final int Id_compile = 1;
  
  private static final int Id_toString = 2;
  
  private static final int Id_toSource = 3;
  
  private static final int Id_exec = 4;
  
  private static final int Id_test = 5;
  
  private static final int Id_prefix = 6;
  
  private static final int MAX_PROTOTYPE_ID = 6;
  
  private RECompiled re;
  

  public static void init(Context cx, Scriptable scope, boolean sealed)
  {
    NativeRegExp proto = new NativeRegExp();
    re = compileRE(cx, "", null, false);
    proto.activatePrototypeMap(6);
    proto.setParentScope(scope);
    proto.setPrototype(getObjectPrototype(scope));
    
    NativeRegExpCtor ctor = new NativeRegExpCtor();
    

    proto.defineProperty("constructor", ctor, 2);
    
    ScriptRuntime.setFunctionProtoAndParent(ctor, scope);
    
    ctor.setImmunePrototypeProperty(proto);
    
    if (sealed) {
      proto.sealObject();
      ctor.sealObject();
    }
    
    defineProperty(scope, "RegExp", ctor, 2);
  }
  
  NativeRegExp(Scriptable scope, RECompiled regexpCompiled) {
    re = regexpCompiled;
    lastIndex = Double.valueOf(0.0D);
    ScriptRuntime.setBuiltinProtoAndParent(this, scope, TopLevel.Builtins.RegExp);
  }
  

  public String getClassName()
  {
    return "RegExp";
  }
  







  public String getTypeOf()
  {
    return "object";
  }
  
  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    return execSub(cx, scope, args, 1);
  }
  
  public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
    return (Scriptable)execSub(cx, scope, args, 1);
  }
  
  Scriptable compile(Context cx, Scriptable scope, Object[] args) {
    if ((args.length > 0) && ((args[0] instanceof NativeRegExp))) {
      if ((args.length > 1) && (args[1] != Undefined.instance))
      {
        throw ScriptRuntime.typeError0("msg.bad.regexp.compile");
      }
      NativeRegExp thatObj = (NativeRegExp)args[0];
      re = re;
      lastIndex = lastIndex;
      return this;
    }
    
    String s = (args.length == 0) || ((args[0] instanceof Undefined)) ? "" : escapeRegExp(args[0]);
    
    String global = (args.length > 1) && (args[1] != Undefined.instance) ? ScriptRuntime.toString(args[1]) : null;
    re = compileRE(cx, s, global, false);
    lastIndex = Double.valueOf(0.0D);
    return this;
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    buf.append('/');
    if (re.source.length != 0) {
      buf.append(re.source);
    }
    else {
      buf.append("(?:)");
    }
    buf.append('/');
    if ((re.flags & 0x1) != 0)
      buf.append('g');
    if ((re.flags & 0x2) != 0)
      buf.append('i');
    if ((re.flags & 0x4) != 0)
      buf.append('m');
    return buf.toString();
  }
  
  NativeRegExp() {}
  
  private static RegExpImpl getImpl(Context cx)
  {
    return (RegExpImpl)ScriptRuntime.getRegExpProxy(cx);
  }
  
  private static String escapeRegExp(Object src) {
    String s = ScriptRuntime.toString(src);
    
    StringBuilder sb = null;
    int start = 0;
    int slash = s.indexOf('/');
    while (slash > -1) {
      if ((slash == start) || (s.charAt(slash - 1) != '\\')) {
        if (sb == null) {
          sb = new StringBuilder();
        }
        sb.append(s, start, slash);
        sb.append("\\/");
        start = slash + 1;
      }
      slash = s.indexOf('/', slash + 1);
    }
    if (sb != null) {
      sb.append(s, start, s.length());
      s = sb.toString();
    }
    return s;
  }
  
  private Object execSub(Context cx, Scriptable scopeObj, Object[] args, int matchType)
  {
    RegExpImpl reImpl = getImpl(cx);
    String str;
    if (args.length == 0) {
      String str = input;
      if (str == null) {
        str = ScriptRuntime.toString(Undefined.instance);
      }
    } else {
      str = ScriptRuntime.toString(args[0]);
    }
    double d = 0.0D;
    if ((re.flags & 0x1) != 0) {
      d = ScriptRuntime.toInteger(lastIndex);
    }
    Object rval;
    Object rval;
    if ((d < 0.0D) || (str.length() < d)) {
      lastIndex = Double.valueOf(0.0D);
      rval = null;
    } else {
      int[] indexp = { (int)d };
      rval = executeRegExp(cx, scopeObj, reImpl, str, indexp, matchType);
      if ((re.flags & 0x1) != 0) {
        lastIndex = Double.valueOf((rval == null) || (rval == Undefined.instance) ? 0.0D : indexp[0]);
      }
    }
    
    return rval;
  }
  
  static RECompiled compileRE(Context cx, String str, String global, boolean flat)
  {
    RECompiled regexp = new RECompiled(str);
    int length = str.length();
    int flags = 0;
    if (global != null) {
      for (int i = 0; i < global.length(); i++) {
        char c = global.charAt(i);
        int f = 0;
        if (c == 'g') {
          f = 1;
        } else if (c == 'i') {
          f = 2;
        } else if (c == 'm') {
          f = 4;
        } else {
          reportError("msg.invalid.re.flag", String.valueOf(c));
        }
        if ((flags & f) != 0) {
          reportError("msg.invalid.re.flag", String.valueOf(c));
        }
        flags |= f;
      }
    }
    flags = flags;
    
    CompilerState state = new CompilerState(cx, source, length, flags);
    
    if ((flat) && (length > 0))
    {


      result = new RENode((byte)14);
      result.chr = cpbegin[0];
      result.length = length;
      result.flatIndex = 0;
      progLength += 5;
    } else {
      if (!parseDisjunction(state)) {
        return null;
      }
      

      if (maxBackReference > parenCount) {
        state = new CompilerState(cx, source, length, flags);
        backReferenceLimit = parenCount;
        if (!parseDisjunction(state)) {
          return null;
        }
      }
    }
    program = new byte[progLength + 1];
    if (classCount != 0) {
      classList = new RECharSet[classCount];
      classCount = classCount;
    }
    int endPC = emitREBytecode(state, regexp, 0, result);
    program[(endPC++)] = 57;
    









    parenCount = parenCount;
    

    switch (program[0]) {
    case 18: 
    case 19: 
      anchorCh = ((char)getIndex(program, 1));
      break;
    case 15: 
    case 17: 
      anchorCh = ((char)(program[1] & 0xFF));
      break;
    case 14: 
    case 16: 
      int k = getIndex(program, 1);
      anchorCh = source[k];
      break;
    case 2: 
      anchorCh = -2;
      break;
    case 31: 
      RENode n = result;
      if ((kid.op == 2) && (kid2.op == 2)) {
        anchorCh = -2;
      }
      


      break;
    }
    
    


    return regexp;
  }
  
  static boolean isDigit(char c) {
    return ('0' <= c) && (c <= '9');
  }
  
  private static boolean isWord(char c) {
    return (('a' <= c) && (c <= 'z')) || (('A' <= c) && (c <= 'Z')) || (isDigit(c)) || (c == '_');
  }
  
  private static boolean isControlLetter(char c)
  {
    return (('a' <= c) && (c <= 'z')) || (('A' <= c) && (c <= 'Z'));
  }
  
  private static boolean isLineTerm(char c) {
    return ScriptRuntime.isJSLineTerminator(c);
  }
  
  private static boolean isREWhiteSpace(int c) {
    return ScriptRuntime.isJSWhitespaceOrLineTerminator(c);
  }
  








  private static char upcase(char ch)
  {
    if (ch < '') {
      if (('a' <= ch) && (ch <= 'z')) {
        return (char)(ch + '￠');
      }
      return ch;
    }
    char cu = Character.toUpperCase(ch);
    return cu < '' ? ch : cu;
  }
  
  private static char downcase(char ch) {
    if (ch < '') {
      if (('A' <= ch) && (ch <= 'Z')) {
        return (char)(ch + ' ');
      }
      return ch;
    }
    char cl = Character.toLowerCase(ch);
    return cl < '' ? ch : cl;
  }
  



  private static int toASCIIHexDigit(int c)
  {
    if (c < 48)
      return -1;
    if (c <= 57) {
      return c - 48;
    }
    c |= 0x20;
    if ((97 <= c) && (c <= 102)) {
      return c - 97 + 10;
    }
    return -1;
  }
  





  private static boolean parseDisjunction(CompilerState state)
  {
    if (!parseAlternative(state))
      return false;
    char[] source = cpbegin;
    int index = cp;
    if ((index != source.length) && (source[index] == '|'))
    {
      cp += 1;
      RENode result = new RENode((byte)31);
      kid = state.result;
      if (!parseDisjunction(state))
        return false;
      kid2 = state.result;
      state.result = result;
      



      if ((kid.op == 14) && (kid2.op == 14)) {
        op = ((flags & 0x2) == 0 ? 53 : 54);
        
        chr = kid.chr;
        index = kid2.chr;
        



        progLength += 13;
      } else if ((kid.op == 22) && (kid.index < 256) && (kid2.op == 14) && ((flags & 0x2) == 0))
      {

        op = 55;
        chr = kid2.chr;
        index = kid.index;
        



        progLength += 13;
      } else if ((kid.op == 14) && (kid2.op == 22) && (kid2.index < 256) && ((flags & 0x2) == 0))
      {

        op = 55;
        chr = kid.chr;
        index = kid2.index;
        



        progLength += 13;
      }
      else {
        progLength += 9;
      }
    }
    return true;
  }
  



  private static boolean parseAlternative(CompilerState state)
  {
    RENode headTerm = null;
    RENode tailTerm = null;
    char[] source = cpbegin;
    for (;;) {
      if ((cp == cpend) || (source[cp] == '|') || ((parenNesting != 0) && (source[cp] == ')')))
      {
        if (headTerm == null) {
          result = new RENode((byte)1);
        } else
          result = headTerm;
        return true;
      }
      if (!parseTerm(state))
        return false;
      if (headTerm == null) {
        headTerm = result;
        tailTerm = headTerm;
      } else {
        next = result; }
      while (next != null) {
        tailTerm = next;
      }
    }
  }
  
  private static boolean calculateBitmapSize(CompilerState state, RENode target, char[] src, int index, int end)
  {
    char rangeStart = '\000';
    



    int max = 0;
    boolean inRange = false;
    
    bmsize = 0;
    sense = true;
    
    if (index == end) {
      return true;
    }
    if (src[index] == '^') {
      index++;
      sense = false;
    }
    
    while (index != end) {
      int localMax = 0;
      int nDigits = 2;
      switch (src[index]) {
      case '\\': 
        index++;
        char c = src[(index++)];
        switch (c) {
        case 'b': 
          localMax = 8;
          break;
        case 'f': 
          localMax = 12;
          break;
        case 'n': 
          localMax = 10;
          break;
        case 'r': 
          localMax = 13;
          break;
        case 't': 
          localMax = 9;
          break;
        case 'v': 
          localMax = 11;
          break;
        case 'c': 
          if ((index < end) && (isControlLetter(src[index]))) {
            localMax = (char)(src[(index++)] & 0x1F);
          } else
            index--;
          localMax = 92;
          break;
        case 'u': 
          nDigits += 2;
        
        case 'x': 
          int n = 0;
          for (int i = 0; (i < nDigits) && (index < end); i++) {
            c = src[(index++)];
            n = Kit.xDigitToInt(c, n);
            if (n < 0)
            {

              index -= i + 1;
              n = 92;
              break;
            }
          }
          localMax = n;
          break;
        case 'd': 
          if (inRange) {
            reportError("msg.bad.range", "");
            return false;
          }
          localMax = 57;
          break;
        case 'D': 
        case 'S': 
        case 'W': 
        case 's': 
        case 'w': 
          if (inRange) {
            reportError("msg.bad.range", "");
            return false;
          }
          bmsize = 65536;
          return true;
        





        case '0': 
        case '1': 
        case '2': 
        case '3': 
        case '4': 
        case '5': 
        case '6': 
        case '7': 
          int n = c - '0';
          c = src[index];
          if (('0' <= c) && (c <= '7')) {
            index++;
            n = 8 * n + (c - '0');
            c = src[index];
            if (('0' <= c) && (c <= '7')) {
              index++;
              int i = 8 * n + (c - '0');
              if (i <= 255) {
                n = i;
              } else
                index--;
            }
          }
          localMax = n;
          break;
        case '8': case '9': case ':': case ';': case '<': case '=': case '>': case '?': case '@': case 'A': case 'B': case 'C': case 'E': case 'F': case 'G': case 'H': case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': 
        case 'T': case 'U': case 'V': case 'X': case 'Y': case 'Z': case '[': case '\\': case ']': case '^': case '_': case '`': case 'a': case 'e': case 'g': case 'h': case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p': case 'q': default: 
          localMax = c; }
        break;
      

      default: 
        localMax = src[(index++)];
      }
      
      if (inRange) {
        if (rangeStart > localMax) {
          reportError("msg.bad.range", "");
          return false;
        }
        inRange = false;
      }
      else if ((index < end - 1) && 
        (src[index] == '-')) {
        index++;
        inRange = true;
        rangeStart = (char)localMax;
        continue;
      }
      

      if ((flags & 0x2) != 0) {
        char cu = upcase((char)localMax);
        char cd = downcase((char)localMax);
        localMax = cu >= cd ? cu : cd;
      }
      if (localMax > max)
        max = localMax;
    }
    bmsize = (max + 1);
    return true;
  }
  


































  private static void doFlat(CompilerState state, char c)
  {
    result = new RENode((byte)14);
    result.chr = c;
    result.length = 1;
    result.flatIndex = -1;
    progLength += 3;
  }
  
  private static int getDecimalValue(char c, CompilerState state, int maxValue, String overflowMessageId)
  {
    boolean overflow = false;
    int start = cp;
    char[] src = cpbegin;
    int value = c - '0';
    for (; cp != cpend; cp += 1) {
      c = src[cp];
      if (!isDigit(c)) {
        break;
      }
      if (!overflow) {
        int v = value * 10 + (c - '0');
        if (v < maxValue) {
          value = v;
        } else {
          overflow = true;
          value = maxValue;
        }
      }
    }
    if (overflow) {
      reportError(overflowMessageId, 
        String.valueOf(src, start, cp - start));
    }
    return value;
  }
  
  private static boolean parseTerm(CompilerState state) {
    char[] src = cpbegin;
    char c = src[(cp++)];
    int nDigits = 2;
    int parenBaseCount = parenCount;
    



    switch (c)
    {
    case '^': 
      state.result = new RENode((byte)2);
      progLength += 1;
      return true;
    case '$': 
      state.result = new RENode((byte)3);
      progLength += 1;
      return true;
    case '\\': 
      if (cp < cpend) {
        c = src[(cp++)];
        switch (c)
        {
        case 'b': 
          state.result = new RENode((byte)4);
          progLength += 1;
          return true;
        case 'B': 
          state.result = new RENode((byte)5);
          progLength += 1;
          return true;
        









        case '0': 
          reportWarning(cx, "msg.bad.backref", "");
          
          int num = 0;
          

          while ((num < 32) && (cp < cpend)) {
            c = src[cp];
            if ((c < '0') || (c > '7')) break;
            cp += 1;
            num = 8 * num + (c - '0');
          }
          

          c = (char)num;
          doFlat(state, c);
          break;
        case '1': 
        case '2': 
        case '3': 
        case '4': 
        case '5': 
        case '6': 
        case '7': 
        case '8': 
        case '9': 
          int termStart = cp - 1;
          int num = getDecimalValue(c, state, 65535, "msg.overlarge.backref");
          
          if (num > backReferenceLimit) {
            reportWarning(cx, "msg.bad.backref", "");
          }
          


          if (num > backReferenceLimit) {
            cp = termStart;
            if (c >= '8')
            {

              c = '\\';
              doFlat(state, c);
            }
            else {
              cp += 1;
              num = c - '0';
              while ((num < 32) && (cp < cpend)) {
                c = src[cp];
                if ((c < '0') || (c > '7')) break;
                cp += 1;
                num = 8 * num + (c - '0');
              }
              

              c = (char)num;
              doFlat(state, c);
            }
          }
          else {
            state.result = new RENode((byte)13);
            resultparenIndex = (num - 1);
            progLength += 3;
            if (maxBackReference < num) {
              maxBackReference = num;
            }
          }
          break;
        case 'f': 
          c = '\f';
          doFlat(state, c);
          break;
        case 'n': 
          c = '\n';
          doFlat(state, c);
          break;
        case 'r': 
          c = '\r';
          doFlat(state, c);
          break;
        case 't': 
          c = '\t';
          doFlat(state, c);
          break;
        case 'v': 
          c = '\013';
          doFlat(state, c);
          break;
        
        case 'c': 
          if ((cp < cpend) && 
            (isControlLetter(src[cp]))) {
            c = (char)(src[(cp++)] & 0x1F);

          }
          else
          {
            cp -= 1;
            c = '\\';
          }
          doFlat(state, c);
          break;
        
        case 'u': 
          nDigits += 2;
        

        case 'x': 
          int n = 0;
          
          for (int i = 0; 
              (i < nDigits) && (cp < cpend); i++) {
            c = src[(cp++)];
            n = Kit.xDigitToInt(c, n);
            if (n < 0)
            {

              cp -= i + 2;
              n = src[(cp++)];
              break;
            }
          }
          c = (char)n;
          
          doFlat(state, c);
          break;
        
        case 'd': 
          state.result = new RENode((byte)7);
          progLength += 1;
          break;
        case 'D': 
          state.result = new RENode((byte)8);
          progLength += 1;
          break;
        case 's': 
          state.result = new RENode((byte)11);
          progLength += 1;
          break;
        case 'S': 
          state.result = new RENode((byte)12);
          progLength += 1;
          break;
        case 'w': 
          state.result = new RENode((byte)9);
          progLength += 1;
          break;
        case 'W': 
          state.result = new RENode((byte)10);
          progLength += 1;
          break;
        case ':': case ';': case '<': case '=': case '>': case '?': case '@': case 'A': case 'C': case 'E': case 'F': case 'G': case 'H': case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'T': 
        case 'U': case 'V': case 'X': case 'Y': case 'Z': case '[': case '\\': case ']': case '^': case '_': case '`': case 'a': case 'e': case 'g': case 'h': case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p': case 'q': default: 
          state.result = new RENode((byte)14);
          resultchr = c;
          resultlength = 1;
          resultflatIndex = (cp - 1);
          progLength += 3;
          break;
        }
      }
      else
      {
        reportError("msg.trail.backslash", "");
        return false;
      }
      break;
    case '(':  RENode result = null;
      int termStart = cp;
      if ((cp + 1 < cpend) && (src[cp] == '?') && (((c = src[(cp + 1)]) == '=') || (c == '!') || (c == ':')))
      {

        cp += 2;
        if (c == '=') {
          result = new RENode((byte)41);
          
          progLength += 4;
        } else if (c == '!') {
          result = new RENode((byte)42);
          
          progLength += 4;
        }
      } else {
        result = new RENode((byte)29);
        
        progLength += 6;
        parenIndex = (parenCount++);
      }
      parenNesting += 1;
      if (!parseDisjunction(state))
        return false;
      if ((cp == cpend) || (src[cp] != ')')) {
        reportError("msg.unterm.paren", "");
        return false;
      }
      cp += 1;
      parenNesting -= 1;
      if (result != null) {
        kid = state.result;
        state.result = result;
      }
      
      break;
    case ')': 
      reportError("msg.re.unmatched.right.paren", "");
      return false;
    case '[': 
      state.result = new RENode((byte)22);
      int termStart = cp;
      resultstartIndex = termStart;
      for (;;) {
        if (cp == cpend) {
          reportError("msg.unterm.class", "");
          return false;
        }
        if (src[cp] == '\\') {
          cp += 1;
        }
        else if (src[cp] == ']') {
          resultkidlen = (cp - termStart);
          break;
        }
        
        cp += 1;
      }
      resultindex = (classCount++);
      



      if (!calculateBitmapSize(state, state.result, src, termStart, cp++))
      {
        return false; }
      progLength += 3;
      break;
    
    case '.': 
      state.result = new RENode((byte)6);
      progLength += 1;
      break;
    case '*': 
    case '+': 
    case '?': 
      reportError("msg.bad.quant", String.valueOf(src[(cp - 1)]));
      return false;
    default: 
      state.result = new RENode((byte)14);
      resultchr = c;
      resultlength = 1;
      resultflatIndex = (cp - 1);
      progLength += 3;
    }
    
    
    RENode term = state.result;
    if (cp == cpend) {
      return true;
    }
    boolean hasQ = false;
    switch (src[cp]) {
    case '+': 
      state.result = new RENode((byte)25);
      resultmin = 1;
      resultmax = -1;
      
      progLength += 8;
      hasQ = true;
      break;
    case '*': 
      state.result = new RENode((byte)25);
      resultmin = 0;
      resultmax = -1;
      
      progLength += 8;
      hasQ = true;
      break;
    case '?': 
      state.result = new RENode((byte)25);
      resultmin = 0;
      resultmax = 1;
      
      progLength += 8;
      hasQ = true;
      break;
    
    case '{': 
      int min = 0;
      int max = -1;
      int leftCurl = cp;
      






      if ((++cp < src.length) && (isDigit(c = src[cp]))) {
        cp += 1;
        min = getDecimalValue(c, state, 65535, "msg.overlarge.min");
        c = src[cp];
        if (c == ',') {
          c = src[(++cp)];
          if (isDigit(c)) {
            cp += 1;
            max = getDecimalValue(c, state, 65535, "msg.overlarge.max");
            
            c = src[cp];
            if (min > max) {
              reportError("msg.max.lt.min", 
                String.valueOf(src[cp]));
              return false;
            }
          }
        } else {
          max = min;
        }
        
        if (c == '}') {
          state.result = new RENode((byte)25);
          resultmin = min;
          resultmax = max;
          

          progLength += 12;
          hasQ = true;
        }
      }
      if (!hasQ) {
        cp = leftCurl;
      }
      break;
    }
    
    if (!hasQ) {
      return true;
    }
    cp += 1;
    resultkid = term;
    resultparenIndex = parenBaseCount;
    resultparenCount = (parenCount - parenBaseCount);
    if ((cp < cpend) && (src[cp] == '?')) {
      cp += 1;
      resultgreedy = false;
    } else {
      resultgreedy = true; }
    return true;
  }
  
  private static void resolveForwardJump(byte[] array, int from, int pc) {
    if (from > pc)
      throw Kit.codeBug();
    addIndex(array, from, pc - from);
  }
  
  private static int getOffset(byte[] array, int pc) {
    return getIndex(array, pc);
  }
  
  private static int addIndex(byte[] array, int pc, int index) {
    if (index < 0)
      throw Kit.codeBug();
    if (index > 65535)
      throw Context.reportRuntimeError("Too complex regexp");
    array[pc] = ((byte)(index >> 8));
    array[(pc + 1)] = ((byte)index);
    return pc + 2;
  }
  
  private static int getIndex(byte[] array, int pc) {
    return (array[pc] & 0xFF) << 8 | array[(pc + 1)] & 0xFF;
  }
  




  private static int emitREBytecode(CompilerState state, RECompiled re, int pc, RENode t)
  {
    byte[] program = program;
    
    while (t != null) {
      program[(pc++)] = op;
      switch (op) {
      case 1: 
        pc--;
        break;
      case 53: 
      case 54: 
      case 55: 
        boolean ignoreCase = op == 54;
        addIndex(program, pc, ignoreCase ? upcase(chr) : chr);
        pc += 2;
        addIndex(program, pc, ignoreCase ? 
          upcase((char)index) : index);
        pc += 2;
      
      case 31: 
        RENode nextAlt = kid2;
        int nextAltFixup = pc;
        pc += 2;
        pc = emitREBytecode(state, re, pc, kid);
        program[(pc++)] = 32;
        int nextTermFixup = pc;
        pc += 2;
        resolveForwardJump(program, nextAltFixup, pc);
        pc = emitREBytecode(state, re, pc, nextAlt);
        
        program[(pc++)] = 32;
        nextAltFixup = pc;
        pc += 2;
        
        resolveForwardJump(program, nextTermFixup, pc);
        resolveForwardJump(program, nextAltFixup, pc);
        break;
      


      case 14: 
        if (flatIndex != -1) {
          while ((next != null) && (next.op == 14) && (flatIndex + length == next.flatIndex))
          {
            length += next.length;
            next = next.next;
          }
        }
        if ((flatIndex != -1) && (length > 1)) {
          if ((flags & 0x2) != 0) {
            program[(pc - 1)] = 16;
          } else
            program[(pc - 1)] = 14;
          pc = addIndex(program, pc, flatIndex);
          pc = addIndex(program, pc, length);
        }
        else if (chr < 'Ā') {
          if ((flags & 0x2) != 0) {
            program[(pc - 1)] = 17;
          } else
            program[(pc - 1)] = 15;
          program[(pc++)] = ((byte)chr);
        } else {
          if ((flags & 0x2) != 0) {
            program[(pc - 1)] = 19;
          } else
            program[(pc - 1)] = 18;
          pc = addIndex(program, pc, chr);
        }
        
        break;
      case 29: 
        pc = addIndex(program, pc, parenIndex);
        pc = emitREBytecode(state, re, pc, kid);
        program[(pc++)] = 30;
        pc = addIndex(program, pc, parenIndex);
        break;
      case 13: 
        pc = addIndex(program, pc, parenIndex);
        break;
      case 41: 
        int nextTermFixup = pc;
        pc += 2;
        pc = emitREBytecode(state, re, pc, kid);
        program[(pc++)] = 43;
        resolveForwardJump(program, nextTermFixup, pc);
        break;
      case 42: 
        int nextTermFixup = pc;
        pc += 2;
        pc = emitREBytecode(state, re, pc, kid);
        program[(pc++)] = 44;
        resolveForwardJump(program, nextTermFixup, pc);
        break;
      case 25: 
        if ((min == 0) && (max == -1)) {
          program[(pc - 1)] = (greedy ? 26 : 45);
        } else if ((min == 0) && (max == 1)) {
          program[(pc - 1)] = (greedy ? 28 : 47);
        } else if ((min == 1) && (max == -1)) {
          program[(pc - 1)] = (greedy ? 27 : 46);
        } else {
          if (!greedy)
            program[(pc - 1)] = 48;
          pc = addIndex(program, pc, min);
          
          pc = addIndex(program, pc, max + 1);
        }
        pc = addIndex(program, pc, parenCount);
        pc = addIndex(program, pc, parenIndex);
        int nextTermFixup = pc;
        pc += 2;
        pc = emitREBytecode(state, re, pc, kid);
        program[(pc++)] = 49;
        resolveForwardJump(program, nextTermFixup, pc);
        break;
      case 22: 
        if (!sense)
          program[(pc - 1)] = 23;
        pc = addIndex(program, pc, index);
        classList[index] = new RECharSet(bmsize, startIndex, kidlen, sense);
        
        break;
      }
      
      
      t = next;
    }
    return pc;
  }
  

  private static void pushProgState(REGlobalData gData, int min, int max, int cp, REBackTrackData backTrackLastToSave, int continuationOp, int continuationPc)
  {
    stateStackTop = new REProgState(stateStackTop, min, max, cp, backTrackLastToSave, continuationOp, continuationPc);
  }
  
  private static REProgState popProgState(REGlobalData gData)
  {
    REProgState state = stateStackTop;
    stateStackTop = previous;
    return state;
  }
  
  private static void pushBackTrackState(REGlobalData gData, byte op, int pc)
  {
    REProgState state = stateStackTop;
    backTrackStackTop = new REBackTrackData(gData, op, pc, cp, continuationOp, continuationPc);
  }
  

  private static void pushBackTrackState(REGlobalData gData, byte op, int pc, int cp, int continuationOp, int continuationPc)
  {
    backTrackStackTop = new REBackTrackData(gData, op, pc, cp, continuationOp, continuationPc);
  }
  




  private static boolean flatNMatcher(REGlobalData gData, int matchChars, int length, String input, int end)
  {
    if (cp + length > end)
      return false;
    for (int i = 0; i < length; i++)
    {
      if (regexp.source[(matchChars + i)] != input.charAt(cp + i)) {
        return false;
      }
    }
    cp += length;
    return true;
  }
  
  private static boolean flatNIMatcher(REGlobalData gData, int matchChars, int length, String input, int end)
  {
    if (cp + length > end)
      return false;
    char[] source = regexp.source;
    for (int i = 0; i < length; i++) {
      char c1 = source[(matchChars + i)];
      char c2 = input.charAt(cp + i);
      if ((c1 != c2) && (upcase(c1) != upcase(c2))) {
        return false;
      }
    }
    cp += length;
    return true;
  }
  


















  private static boolean backrefMatcher(REGlobalData gData, int parenIndex, String input, int end)
  {
    if ((parens == null) || (parenIndex >= parens.length))
      return false;
    int parenContent = gData.parensIndex(parenIndex);
    if (parenContent == -1) {
      return true;
    }
    int len = gData.parensLength(parenIndex);
    if (cp + len > end) {
      return false;
    }
    if ((regexp.flags & 0x2) != 0)
      for (int i = 0; i < len; i++) {
        char c1 = input.charAt(parenContent + i);
        char c2 = input.charAt(cp + i);
        if ((c1 != c2) && (upcase(c1) != upcase(c2)))
          return false;
      }
    if (!input.regionMatches(parenContent, input, cp, len)) {
      return false;
    }
    cp += len;
    return true;
  }
  
  private static void addCharacterToCharSet(RECharSet cs, char c)
  {
    int byteIndex = c / '\b';
    if (c >= length) {
      throw ScriptRuntime.constructError("SyntaxError", "invalid range in character class");
    }
    
    int tmp26_25 = byteIndex; byte[] tmp26_22 = bits;tmp26_22[tmp26_25] = ((byte)(tmp26_22[tmp26_25] | '\001' << (c & 0x7)));
  }
  



  private static void addCharacterRangeToCharSet(RECharSet cs, char c1, char c2)
  {
    int byteIndex1 = c1 / '\b';
    int byteIndex2 = c2 / '\b';
    
    if ((c2 >= length) || (c1 > c2)) {
      throw ScriptRuntime.constructError("SyntaxError", "invalid range in character class");
    }
    

    c1 = (char)(c1 & 0x7);
    c2 = (char)(c2 & 0x7);
    
    if (byteIndex1 == byteIndex2) {
      int tmp58_56 = byteIndex1; byte[] tmp58_53 = bits;tmp58_53[tmp58_56] = ((byte)(tmp58_53[tmp58_56] | 255 >> 7 - (c2 - c1) << c1));
    } else {
      int tmp84_82 = byteIndex1; byte[] tmp84_79 = bits;tmp84_79[tmp84_82] = ((byte)(tmp84_79[tmp84_82] | 'ÿ' << c1));
      for (int i = byteIndex1 + 1; i < byteIndex2; i++)
        bits[i] = -1;
      int tmp124_122 = byteIndex2; byte[] tmp124_119 = bits;tmp124_119[tmp124_122] = ((byte)(tmp124_119[tmp124_122] | 255 >> '\007' - c2));
    }
  }
  
  private static void processCharSet(REGlobalData gData, RECharSet charSet)
  {
    synchronized (charSet) {
      if (!converted) {
        processCharSetImpl(gData, charSet);
        converted = true;
      }
    }
  }
  
  private static void processCharSetImpl(REGlobalData gData, RECharSet charSet)
  {
    int src = startIndex;
    int end = src + strlength;
    
    char rangeStart = '\000';
    




    boolean inRange = false;
    
    int byteLength = (length + 7) / 8;
    bits = new byte[byteLength];
    
    if (src == end) {
      return;
    }
    if (regexp.source[src] == '^') {
      assert (!sense);
      src++;
    } else {
      assert (sense);
    }
    
    while (src != end) {
      int nDigits = 2;
      char thisCh; switch (regexp.source[src]) {
      case '\\': 
        src++;
        char c = regexp.source[(src++)];
        char thisCh; char thisCh; char thisCh; char thisCh; char thisCh; char thisCh; char thisCh; char thisCh; char thisCh; switch (c) {
        case 'b': 
          thisCh = '\b';
          break;
        case 'f': 
          thisCh = '\f';
          break;
        case 'n': 
          thisCh = '\n';
          break;
        case 'r': 
          thisCh = '\r';
          break;
        case 't': 
          thisCh = '\t';
          break;
        case 'v': 
          thisCh = '\013';
          break;
        case 'c':  char thisCh;
          if ((src < end) && 
            (isControlLetter(regexp.source[src]))) {
            thisCh = (char)(regexp.source[(src++)] & 0x1F);
          } else {
            src--;
            thisCh = '\\';
          }
          break;
        case 'u': 
          nDigits += 2;
        
        case 'x': 
          int n = 0;
          for (int i = 0; (i < nDigits) && (src < end); i++) {
            c = regexp.source[(src++)];
            int digit = toASCIIHexDigit(c);
            if (digit < 0)
            {



              src -= i + 1;
              n = 92;
              break;
            }
            n = n << 4 | digit;
          }
          thisCh = (char)n;
          break;
        





        case '0': 
        case '1': 
        case '2': 
        case '3': 
        case '4': 
        case '5': 
        case '6': 
        case '7': 
          int n = c - '0';
          c = regexp.source[src];
          if (('0' <= c) && (c <= '7')) {
            src++;
            n = 8 * n + (c - '0');
            c = regexp.source[src];
            if (('0' <= c) && (c <= '7')) {
              src++;
              int i = 8 * n + (c - '0');
              if (i <= 255) {
                n = i;
              } else
                src--;
            }
          }
          thisCh = (char)n;
          break;
        
        case 'd': 
          addCharacterRangeToCharSet(charSet, '0', '9');
          break;
        case 'D': 
          addCharacterRangeToCharSet(charSet, '\000', '/');
          
          addCharacterRangeToCharSet(charSet, ':', (char)(length - 1));
          
          break;
        case 's': 
          for (int i = length - 1; i >= 0; i--) {
            if (isREWhiteSpace(i))
              addCharacterToCharSet(charSet, (char)i);
          }
        case 'S': 
          for (int i = length - 1; i >= 0; i--) {
            if (!isREWhiteSpace(i))
              addCharacterToCharSet(charSet, (char)i);
          }
        case 'w': 
          for (int i = length - 1; i >= 0; i--) {
            if (isWord((char)i))
              addCharacterToCharSet(charSet, (char)i);
          }
        case 'W': 
          for (int i = length - 1; i >= 0; i--) {
            if (!isWord((char)i))
              addCharacterToCharSet(charSet, (char)i);
          }
        case '8': case '9': case ':': case ';': case '<': case '=': case '>': case '?': case '@': case 'A': case 'B': case 'C': case 'E': case 'F': case 'G': case 'H': case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'T': case 'U': case 'V': case 'X': case 'Y': case 'Z': case '[': case '\\': case ']': case '^': case '_': case '`': case 'a': case 'e': case 'g': case 'h': case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p': case 'q': default: 
          thisCh = c; }
        break;
      



      default: 
        char thisCh = regexp.source[(src++)];
        


        if (inRange) {
          if ((regexp.flags & 0x2) != 0) {
            assert (rangeStart <= thisCh);
            for (char c = rangeStart; c <= thisCh; 
                






                c == 0)
            {
              addCharacterToCharSet(charSet, c);
              char uch = upcase(c);
              char dch = downcase(c);
              if (c != uch)
                addCharacterToCharSet(charSet, uch);
              if (c != dch)
                addCharacterToCharSet(charSet, dch);
              c = (char)(c + '\001');
            }
          }
          
          addCharacterRangeToCharSet(charSet, rangeStart, thisCh);
          
          inRange = false;
        } else {
          if ((regexp.flags & 0x2) != 0) {
            addCharacterToCharSet(charSet, upcase(thisCh));
            addCharacterToCharSet(charSet, downcase(thisCh));
          } else {
            addCharacterToCharSet(charSet, thisCh);
          }
          if ((src < end - 1) && 
            (regexp.source[src] == '-')) {
            src++;
            inRange = true;
            rangeStart = thisCh;
          }
        }
        
        break;
      }
      
    }
  }
  

  private static boolean classMatcher(REGlobalData gData, RECharSet charSet, char ch)
  {
    if (!converted) {
      processCharSet(gData, charSet);
    }
    
    int byteIndex = ch >> '\003';
    return ((length == 0) || (ch >= length) || ((bits[byteIndex] & '\001' << (ch & 0x7)) == 0)) ^ sense;
  }
  

  private static boolean reopIsSimple(int op)
  {
    return (op >= 1) && (op <= 23);
  }
  





  private static int simpleMatch(REGlobalData gData, String input, int op, byte[] program, int pc, int end, boolean updatecp)
  {
    boolean result = false;
    


    int startcp = cp;
    
    switch (op) {
    case 1: 
      result = true;
      break;
    case 2: 
      if ((cp == 0) || (
        (multiline) && 
        (isLineTerm(input.charAt(cp - 1)))))
      {


        result = true; }
      break;
    case 3: 
      if ((cp == end) || (
        (multiline) && (isLineTerm(input.charAt(cp)))))
      {


        result = true; }
      break;
    
    case 4: 
      result = ((cp == 0) || (!isWord(input.charAt(cp - 1))) ? 1 : 0) ^ ((cp >= end) || (!isWord(input.charAt(cp))) ? 1 : 0);
      break;
    
    case 5: 
      result = ((cp == 0) || (!isWord(input.charAt(cp - 1))) ? 1 : 0) ^ ((cp < end) && (isWord(input.charAt(cp))) ? 1 : 0);
      break;
    case 6: 
      if ((cp != end) && (!isLineTerm(input.charAt(cp)))) {
        result = true;
        cp += 1;
      }
      break;
    case 7: 
      if ((cp != end) && (isDigit(input.charAt(cp)))) {
        result = true;
        cp += 1;
      }
      break;
    case 8: 
      if ((cp != end) && (!isDigit(input.charAt(cp)))) {
        result = true;
        cp += 1;
      }
      break;
    case 9: 
      if ((cp != end) && (isWord(input.charAt(cp)))) {
        result = true;
        cp += 1;
      }
      break;
    case 10: 
      if ((cp != end) && (!isWord(input.charAt(cp)))) {
        result = true;
        cp += 1;
      }
      break;
    case 11: 
      if ((cp != end) && (isREWhiteSpace(input.charAt(cp)))) {
        result = true;
        cp += 1;
      }
      break;
    case 12: 
      if ((cp != end) && (!isREWhiteSpace(input.charAt(cp)))) {
        result = true;
        cp += 1;
      }
      break;
    case 13: 
      int parenIndex = getIndex(program, pc);
      pc += 2;
      result = backrefMatcher(gData, parenIndex, input, end);
      
      break;
    case 14: 
      int offset = getIndex(program, pc);
      pc += 2;
      int length = getIndex(program, pc);
      pc += 2;
      result = flatNMatcher(gData, offset, length, input, end);
      
      break;
    case 15: 
      char matchCh = (char)(program[(pc++)] & 0xFF);
      if ((cp != end) && (input.charAt(cp) == matchCh)) {
        result = true;
        cp += 1;
      }
      
      break;
    case 16: 
      int offset = getIndex(program, pc);
      pc += 2;
      int length = getIndex(program, pc);
      pc += 2;
      result = flatNIMatcher(gData, offset, length, input, end);
      
      break;
    case 17: 
      char matchCh = (char)(program[(pc++)] & 0xFF);
      if (cp != end) {
        char c = input.charAt(cp);
        if ((matchCh == c) || (upcase(matchCh) == upcase(c))) {
          result = true;
          cp += 1;
        } }
      break;
    

    case 18: 
      char matchCh = (char)getIndex(program, pc);
      pc += 2;
      if ((cp != end) && (input.charAt(cp) == matchCh)) {
        result = true;
        cp += 1;
      }
      
      break;
    case 19: 
      char matchCh = (char)getIndex(program, pc);
      pc += 2;
      if (cp != end) {
        char c = input.charAt(cp);
        if ((matchCh == c) || (upcase(matchCh) == upcase(c))) {
          result = true;
          cp += 1;
        } }
      break;
    


    case 22: 
    case 23: 
      int index = getIndex(program, pc);
      pc += 2;
      if ((cp != end) && 
        (classMatcher(gData, regexp.classList[index], input
        .charAt(cp)))) {
        cp += 1;
        result = true; }
      break;
    


    case 20: 
    case 21: 
    default: 
      throw Kit.codeBug();
    }
    if (result) {
      if (!updatecp)
        cp = startcp;
      return pc;
    }
    cp = startcp;
    return -1;
  }
  
  private static boolean executeREBytecode(REGlobalData gData, String input, int end)
  {
    int pc = 0;
    byte[] program = regexp.program;
    int continuationOp = 57;
    int continuationPc = 0;
    boolean result = false;
    
    int op = program[(pc++)];
    




    if ((regexp.anchorCh < 0) && (reopIsSimple(op))) {
      boolean anchor = false;
      while (cp <= end) {
        int match = simpleMatch(gData, input, op, program, pc, end, true);
        
        if (match >= 0) {
          anchor = true;
          pc = match;
          op = program[(pc++)];
          break;
        }
        skipped += 1;
        cp += 1;
      }
      if (!anchor) {
        return false;
      }
    }
    for (;;)
    {
      if (reopIsSimple(op)) {
        int match = simpleMatch(gData, input, op, program, pc, end, true);
        
        result = match >= 0;
        if (result)
          pc = match;
      } else {
        switch (op) {
        case 53: 
        case 54: 
        case 55: 
          char matchCh1 = (char)getIndex(program, pc);
          pc += 2;
          char matchCh2 = (char)getIndex(program, pc);
          pc += 2;
          
          if (cp == end) {
            result = false;
          }
          else {
            char c = input.charAt(cp);
            if (op == 55) {
              if ((c != matchCh1) && (!classMatcher(gData, regexp.classList[matchCh2], c)))
              {
                result = false;
                break label1963;
              }
            } else {
              if (op == 54)
                c = upcase(c);
              if ((c != matchCh1) && (c != matchCh2))
                result = false; } }
          break;
        



        case 31: 
          int nextpc = pc + getOffset(program, pc);
          pc += 2;
          op = program[(pc++)];
          int startcp = cp;
          if (reopIsSimple(op)) {
            int match = simpleMatch(gData, input, op, program, pc, end, true);
            
            if (match < 0) {
              op = program[(nextpc++)];
              pc = nextpc;
              continue;
            }
            result = true;
            pc = match;
            op = program[(pc++)];
          }
          byte nextop = program[(nextpc++)];
          pushBackTrackState(gData, nextop, nextpc, startcp, continuationOp, continuationPc);
          

          break;
        
        case 32: 
          int offset = getOffset(program, pc);
          pc += offset;
          op = program[(pc++)];
          
          break;
        
        case 29: 
          int parenIndex = getIndex(program, pc);
          pc += 2;
          gData.setParens(parenIndex, cp, 0);
          op = program[(pc++)];
          
          break;
        case 30: 
          int parenIndex = getIndex(program, pc);
          pc += 2;
          int cap_index = gData.parensIndex(parenIndex);
          gData.setParens(parenIndex, cap_index, cp - cap_index);
          
          op = program[(pc++)];
          
          break;
        
        case 41: 
          int nextpc = pc + getIndex(program, pc);
          
          pc += 2;
          op = program[(pc++)];
          if ((reopIsSimple(op)) && (simpleMatch(gData, input, op, program, pc, end, false) < 0))
          {
            result = false;
          }
          else {
            pushProgState(gData, 0, 0, cp, backTrackStackTop, continuationOp, continuationPc);
            

            pushBackTrackState(gData, (byte)43, nextpc);
          }
          break;
        case 42: 
          int nextpc = pc + getIndex(program, pc);
          
          pc += 2;
          op = program[(pc++)];
          if (reopIsSimple(op)) {
            int match = simpleMatch(gData, input, op, program, pc, end, false);
            
            if ((match >= 0) && (program[match] == 44))
            {
              result = false;
              break label1963;
            }
          }
          pushProgState(gData, 0, 0, cp, backTrackStackTop, continuationOp, continuationPc);
          

          pushBackTrackState(gData, (byte)44, nextpc);
          
          break;
        
        case 43: 
        case 44: 
          REProgState state = popProgState(gData);
          cp = index;
          backTrackStackTop = backTrack;
          continuationPc = continuationPc;
          continuationOp = continuationOp;
          if (op == 44) {
            result = !result;
          }
          
          break;
        

        case 25: 
        case 26: 
        case 27: 
        case 28: 
        case 45: 
        case 46: 
        case 47: 
        case 48: 
          boolean greedy = false;
          int max; int max; int max; switch (op) {
          case 26: 
            greedy = true;
          
          case 45: 
            int min = 0;
            max = -1;
            break;
          case 27: 
            greedy = true;
          
          case 46: 
            int min = 1;
            max = -1;
            break;
          case 28: 
            greedy = true;
          
          case 47: 
            int min = 0;
            max = 1;
            break;
          case 25: 
            greedy = true;
          
          case 48: 
            int min = getOffset(program, pc);
            pc += 2;
            
            int max = getOffset(program, pc) - 1;
            pc += 2;
            break;
          case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39: case 40: case 41: case 42: case 43: case 44: default: 
            throw Kit.codeBug(); }
          int max;
          int min; pushProgState(gData, min, max, cp, null, continuationOp, continuationPc);
          
          if (greedy) {
            pushBackTrackState(gData, (byte)51, pc);
            continuationOp = 51;
            continuationPc = pc;
            
            pc += 6;
            op = program[(pc++)];
          }
          else if (min != 0) {
            continuationOp = 52;
            continuationPc = pc;
            
            pc += 6;
            op = program[(pc++)];
          } else {
            pushBackTrackState(gData, (byte)52, pc);
            popProgState(gData);
            pc += 4;
            pc += getOffset(program, pc);
            op = program[(pc++)];
          }
          

          break;
        


        case 49: 
          result = true;
          
          pc = continuationPc;
          op = continuationOp;
          break;
        case 51: 
          int nextpc;
          do
          {
            REProgState state = popProgState(gData);
            if (!result)
            {
              if (min == 0)
                result = true;
              continuationPc = continuationPc;
              continuationOp = continuationOp;
              pc += 4;
              

              pc += getOffset(program, pc);
              break;
            }
            if ((min == 0) && (cp == index))
            {
              result = false;
              continuationPc = continuationPc;
              continuationOp = continuationOp;
              pc += 4;
              pc += getOffset(program, pc);
              break;
            }
            int new_min = min;int new_max = max;
            if (new_min != 0)
              new_min--;
            if (new_max != -1)
              new_max--;
            if (new_max == 0) {
              result = true;
              continuationPc = continuationPc;
              continuationOp = continuationOp;
              pc += 4;
              pc += getOffset(program, pc);
              break;
            }
            nextpc = pc + 6;
            int nextop = program[nextpc];
            int startcp = cp;
            if (reopIsSimple(nextop)) {
              nextpc++;
              int match = simpleMatch(gData, input, nextop, program, nextpc, end, true);
              
              if (match < 0) {
                result = new_min == 0;
                continuationPc = continuationPc;
                continuationOp = continuationOp;
                pc += 4;
                


                pc += getOffset(program, pc);
                break;
              }
              result = true;
              nextpc = match;
            }
            continuationOp = 51;
            continuationPc = pc;
            pushProgState(gData, new_min, new_max, startcp, null, continuationOp, continuationPc);
            
            if (new_min == 0) {
              pushBackTrackState(gData, (byte)51, pc, startcp, continuationOp, continuationPc);
              
              int parenCount = getIndex(program, pc);
              int parenIndex = getIndex(program, pc + 2);
              for (int k = 0; k < parenCount; k++) {
                gData.setParens(parenIndex + k, -1, 0);
              }
            }
          } while (program[nextpc] == 49);
          
          pc = nextpc;
          op = program[(pc++)];
          
          break;
        
        case 52: 
          REProgState state = popProgState(gData);
          if (!result)
          {


            if ((max == -1) || (max > 0)) {
              pushProgState(gData, min, max, cp, null, continuationOp, continuationPc);
              

              continuationOp = 52;
              continuationPc = pc;
              int parenCount = getIndex(program, pc);
              pc += 2;
              int parenIndex = getIndex(program, pc);
              pc += 4;
              for (int k = 0; k < parenCount; k++) {
                gData.setParens(parenIndex + k, -1, 0);
              }
              op = program[(pc++)];
              continue;
            }
            
            continuationPc = continuationPc;
            continuationOp = continuationOp;


          }
          else if ((min == 0) && (cp == index))
          {
            result = false;
            continuationPc = continuationPc;
            continuationOp = continuationOp;
          }
          else {
            int new_min = min;int new_max = max;
            if (new_min != 0)
              new_min--;
            if (new_max != -1)
              new_max--;
            pushProgState(gData, new_min, new_max, cp, null, continuationOp, continuationPc);
            
            if (new_min != 0) {
              continuationOp = 52;
              continuationPc = pc;
              int parenCount = getIndex(program, pc);
              pc += 2;
              int parenIndex = getIndex(program, pc);
              pc += 4;
              for (int k = 0; k < parenCount; k++) {
                gData.setParens(parenIndex + k, -1, 0);
              }
              op = program[(pc++)];
              continue; }
            continuationPc = continuationPc;
            continuationOp = continuationOp;
            pushBackTrackState(gData, (byte)52, pc);
            popProgState(gData);
            pc += 4;
            pc += getOffset(program, pc);
            op = program[(pc++)];
          }
          break;
        


        case 57: 
          return true;
        case 33: case 34: case 35: case 36: case 37: case 38: 
        case 39: case 40: case 50: case 56: default: 
          throw Kit.codeBug("invalid bytecode");
        }
        
      }
      

      label1963:
      
      if (!result) {
        REBackTrackData backTrackData = backTrackStackTop;
        if (backTrackData != null) {
          backTrackStackTop = previous;
          parens = parens;
          cp = cp;
          stateStackTop = stateStackTop;
          continuationOp = continuationOp;
          continuationPc = continuationPc;
          pc = pc;
          op = op;
        }
        else {
          return false;
        }
      } else {
        op = program[(pc++)];
      }
    }
  }
  
  private static boolean matchRegExp(REGlobalData gData, RECompiled re, String input, int start, int end, boolean multiline)
  {
    if (parenCount != 0) {
      parens = new long[parenCount];
    } else {
      parens = null;
    }
    
    backTrackStackTop = null;
    stateStackTop = null;
    
    gData.multiline = ((multiline) || ((flags & 0x4) != 0));
    regexp = re;
    
    int anchorCh = regexp.anchorCh;
    



    for (int i = start; i <= end; i++)
    {




      if (anchorCh >= 0) {
        for (;;) {
          if (i == end) {
            return false;
          }
          char matchCh = input.charAt(i);
          if ((matchCh == anchorCh) || (((regexp.flags & 0x2) != 0) && 
          
            (upcase(matchCh) == upcase((char)anchorCh)))) {
            break;
          }
          
          i++;
        }
      }
      cp = i;
      skipped = (i - start);
      for (int j = 0; j < parenCount; j++) {
        parens[j] = -1L;
      }
      boolean result = executeREBytecode(gData, input, end);
      
      backTrackStackTop = null;
      stateStackTop = null;
      if (result) {
        return true;
      }
      if ((anchorCh == -2) && (!gData.multiline)) {
        skipped = end;
        return false;
      }
      i = start + skipped;
    }
    return false;
  }
  



  Object executeRegExp(Context cx, Scriptable scope, RegExpImpl res, String str, int[] indexp, int matchType)
  {
    REGlobalData gData = new REGlobalData();
    
    int start = indexp[0];
    int end = str.length();
    if (start > end) {
      start = end;
    }
    

    boolean matches = matchRegExp(gData, re, str, start, end, multiline);
    
    if (!matches) {
      if (matchType != 2)
        return null;
      return Undefined.instance;
    }
    int index = cp;
    int ep = indexp[0] = index;
    int matchlen = ep - (start + skipped);
    index -= matchlen;
    Scriptable obj;
    Object result;
    Scriptable obj;
    if (matchType == 0)
    {



      Object result = Boolean.TRUE;
      obj = null;


    }
    else
    {


      result = cx.newArray(scope, 0);
      obj = (Scriptable)result;
      
      String matchstr = str.substring(index, index + matchlen);
      obj.put(0, obj, matchstr);
    }
    
    if (re.parenCount == 0) {
      parens = null;
      lastParen = SubString.emptySubString;
    } else {
      SubString parsub = null;
      
      parens = new SubString[re.parenCount];
      for (int num = 0; num < re.parenCount; num++) {
        int cap_index = gData.parensIndex(num);
        
        if (cap_index != -1) {
          int cap_length = gData.parensLength(num);
          parsub = new SubString(str, cap_index, cap_length);
          parens[num] = parsub;
          if (matchType != 0) {
            obj.put(num + 1, obj, parsub.toString());
          }
        } else if (matchType != 0) {
          obj.put(num + 1, obj, Undefined.instance);
        }
      }
      lastParen = parsub;
    }
    
    if (matchType != 0)
    {



      obj.put("index", obj, Integer.valueOf(start + skipped));
      obj.put("input", obj, str);
    }
    
    if (lastMatch == null) {
      lastMatch = new SubString();
      leftContext = new SubString();
      rightContext = new SubString();
    }
    lastMatch.str = str;
    lastMatch.index = index;
    lastMatch.length = matchlen;
    
    leftContext.str = str;
    if (cx.getLanguageVersion() == 120)
    {












      leftContext.index = start;
      leftContext.length = skipped;


    }
    else
    {

      leftContext.index = 0;
      leftContext.length = (start + skipped);
    }
    
    rightContext.str = str;
    rightContext.index = ep;
    rightContext.length = (end - ep);
    
    return result;
  }
  
  int getFlags() {
    return re.flags;
  }
  
  private static void reportWarning(Context cx, String messageId, String arg)
  {
    if (cx.hasFeature(11)) {
      String msg = ScriptRuntime.getMessage1(messageId, arg);
      Context.reportWarning(msg);
    }
  }
  
  private static void reportError(String messageId, String arg) {
    String msg = ScriptRuntime.getMessage1(messageId, arg);
    throw ScriptRuntime.constructError("SyntaxError", msg);
  }
  







  protected int getMaxInstanceId()
  {
    return 5;
  }
  



  protected int findInstanceIdInfo(String s)
  {
    int id = 0;
    String X = null;
    
    int s_length = s.length();
    if (s_length == 6) {
      int c = s.charAt(0);
      if (c == 103) {
        X = "global";
        id = 3;
      } else if (c == 115) {
        X = "source";
        id = 2;
      }
    } else if (s_length == 9) {
      int c = s.charAt(0);
      if (c == 108) {
        X = "lastIndex";
        id = 1;
      } else if (c == 109) {
        X = "multiline";
        id = 5;
      }
    } else if (s_length == 10) {
      X = "ignoreCase";
      id = 4;
    }
    if ((X != null) && (X != s) && (!X.equals(s))) {
      id = 0;
    }
    



    if (id == 0)
      return super.findInstanceIdInfo(s);
    int attr;
    int attr;
    switch (id) {
    case 1: 
      attr = lastIndexAttr;
      break;
    case 2: 
    case 3: 
    case 4: 
    case 5: 
      attr = 7;
      break;
    default: 
      throw new IllegalStateException(); }
    int attr;
    return instanceIdInfo(attr, id);
  }
  
  protected String getInstanceIdName(int id)
  {
    switch (id) {
    case 1: 
      return "lastIndex";
    case 2: 
      return "source";
    case 3: 
      return "global";
    case 4: 
      return "ignoreCase";
    case 5: 
      return "multiline";
    }
    return super.getInstanceIdName(id);
  }
  
  protected Object getInstanceIdValue(int id)
  {
    switch (id) {
    case 1: 
      return lastIndex;
    case 2: 
      return new String(re.source);
    case 3: 
      return ScriptRuntime.wrapBoolean((re.flags & 0x1) != 0);
    case 4: 
      return ScriptRuntime.wrapBoolean((re.flags & 0x2) != 0);
    case 5: 
      return ScriptRuntime.wrapBoolean((re.flags & 0x4) != 0);
    }
    return super.getInstanceIdValue(id);
  }
  
  protected void setInstanceIdValue(int id, Object value)
  {
    switch (id) {
    case 1: 
      lastIndex = value;
      return;
    case 2: 
    case 3: 
    case 4: 
    case 5: 
      return;
    }
    super.setInstanceIdValue(id, value);
  }
  
  protected void setInstanceIdAttributes(int id, int attr)
  {
    switch (id) {
    case 1: 
      lastIndexAttr = attr;
      return;
    }
    super.setInstanceIdAttributes(id, attr); }
  
  protected void initPrototypeId(int id) { String s;
    String s;
    String s;
    String s;
    String s;
    String s; switch (id) {
    case 1: 
      int arity = 2;
      s = "compile";
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
      int arity = 1;
      s = "exec";
      break;
    case 5: 
      int arity = 1;
      s = "test";
      break;
    case 6: 
      int arity = 1;
      s = "prefix";
      break;
    default: 
      throw new IllegalArgumentException(String.valueOf(id)); }
    int arity;
    String s; initPrototypeMethod(REGEXP_TAG, id, s, arity);
  }
  

  public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if (!f.hasTag(REGEXP_TAG)) {
      return super.execIdCall(f, cx, scope, thisObj, args);
    }
    int id = f.methodId();
    switch (id) {
    case 1: 
      return realThis(thisObj, f).compile(cx, scope, args);
    
    case 2: 
    case 3: 
      return realThis(thisObj, f).toString();
    
    case 4: 
      return realThis(thisObj, f).execSub(cx, scope, args, 1);
    
    case 5: 
      Object x = realThis(thisObj, f).execSub(cx, scope, args, 0);
      return Boolean.TRUE.equals(x) ? Boolean.TRUE : Boolean.FALSE;
    

    case 6: 
      return realThis(thisObj, f).execSub(cx, scope, args, 2);
    }
    throw new IllegalArgumentException(String.valueOf(id));
  }
  
  private static NativeRegExp realThis(Scriptable thisObj, IdFunctionObject f)
  {
    if (!(thisObj instanceof NativeRegExp))
      throw incompatibleCallError(f);
    return (NativeRegExp)thisObj;
  }
  




  protected int findPrototypeId(String s)
  {
    int id = 0;
    String X = null;
    
    switch (s.length()) {
    case 4: 
      int c = s.charAt(0);
      if (c == 101) {
        X = "exec";
        id = 4;
      } else if (c == 116) {
        X = "test";
        id = 5;
      }
      break;
    case 6: 
      X = "prefix";
      id = 6;
      break;
    case 7: 
      X = "compile";
      id = 1;
      break;
    case 8: 
      int c = s.charAt(3);
      if (c == 111) {
        X = "toSource";
        id = 3;
      } else if (c == 116) {
        X = "toString";
        id = 2;
      }
      break;
    }
    if ((X != null) && (X != s) && (!X.equals(s))) {
      id = 0;
    }
    

    return id;
  }
  








  Object lastIndex = Double.valueOf(0.0D);
  private int lastIndexAttr = 6;
}
