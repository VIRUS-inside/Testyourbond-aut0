package net.sourceforge.htmlunit.corejs.javascript.regexp;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

public class RegExpImpl implements net.sourceforge.htmlunit.corejs.javascript.RegExpProxy {
  protected String input;
  protected boolean multiline;
  protected SubString[] parens;
  protected SubString lastMatch;
  protected SubString lastParen;
  protected SubString leftContext;
  protected SubString rightContext;
  
  public RegExpImpl() {}
  
  public boolean isRegExp(Scriptable obj) {
    return obj instanceof NativeRegExp;
  }
  
  public Object compileRegExp(net.sourceforge.htmlunit.corejs.javascript.Context cx, String source, String flags) {
    return NativeRegExp.compileRE(cx, source, flags, false);
  }
  
  public Scriptable wrapRegExp(net.sourceforge.htmlunit.corejs.javascript.Context cx, Scriptable scope, Object compiled)
  {
    return new NativeRegExp(scope, (RECompiled)compiled);
  }
  
  public Object action(net.sourceforge.htmlunit.corejs.javascript.Context cx, Scriptable scope, Scriptable thisObj, Object[] args, int actionType)
  {
    GlobData data = new GlobData();
    mode = actionType;
    str = net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.toString(thisObj);
    
    switch (actionType) {
    case 1: 
      NativeRegExp re = createRegExp(cx, scope, args, 1, false);
      Object rval = matchOrReplace(cx, scope, thisObj, args, this, data, re);
      
      return arrayobj == null ? rval : arrayobj;
    

    case 3: 
      NativeRegExp re = createRegExp(cx, scope, args, 1, false);
      return matchOrReplace(cx, scope, thisObj, args, this, data, re);
    

    case 2: 
      boolean useRE = ((args.length > 0) && ((args[0] instanceof NativeRegExp))) || (args.length > 2);
      
      NativeRegExp re = null;
      String search = null;
      if (useRE) {
        re = createRegExp(cx, scope, args, 2, true);
      } else {
        Object arg0 = args.length < 1 ? net.sourceforge.htmlunit.corejs.javascript.Undefined.instance : args[0];
        search = net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.toString(arg0);
      }
      
      Object arg1 = args.length < 2 ? net.sourceforge.htmlunit.corejs.javascript.Undefined.instance : args[1];
      String repstr = null;
      net.sourceforge.htmlunit.corejs.javascript.Function lambda = null;
      if ((arg1 instanceof net.sourceforge.htmlunit.corejs.javascript.Function)) {
        lambda = (net.sourceforge.htmlunit.corejs.javascript.Function)arg1;
      } else {
        repstr = net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.toString(arg1);
      }
      
      lambda = lambda;
      repstr = repstr;
      dollar = (repstr == null ? -1 : repstr.indexOf('$'));
      charBuf = null;
      leftIndex = 0;
      Object val;
      Object val;
      if (useRE) {
        val = matchOrReplace(cx, scope, thisObj, args, this, data, re);
      } else {
        String str = str;
        int index = str.indexOf(search);
        Object val; if (index >= 0) {
          int slen = search.length();
          lastParen = null;
          leftContext = new SubString(str, 0, index);
          lastMatch = new SubString(str, index, slen);
          
          rightContext = new SubString(str, index + slen, str.length() - index - slen);
          val = Boolean.TRUE;
        } else {
          val = Boolean.FALSE;
        }
      }
      
      if (charBuf == null) {
        if ((global) || (val == null) || (!val.equals(Boolean.TRUE)))
        {
          return str;
        }
        SubString lc = leftContext;
        replace_glob(data, cx, scope, this, index, length);
      }
      SubString rc = rightContext;
      charBuf.append(str, index, index + length);
      return charBuf.toString();
    }
    
    
    throw net.sourceforge.htmlunit.corejs.javascript.Kit.codeBug();
  }
  


  private static NativeRegExp createRegExp(net.sourceforge.htmlunit.corejs.javascript.Context cx, Scriptable scope, Object[] args, int optarg, boolean forceFlat)
  {
    Scriptable topScope = net.sourceforge.htmlunit.corejs.javascript.ScriptableObject.getTopLevelScope(scope);
    NativeRegExp re; NativeRegExp re; if ((args.length == 0) || (args[0] == net.sourceforge.htmlunit.corejs.javascript.Undefined.instance)) {
      RECompiled compiled = NativeRegExp.compileRE(cx, "", "", false);
      re = new NativeRegExp(topScope, compiled); } else { NativeRegExp re;
      if ((args[0] instanceof NativeRegExp)) {
        re = (NativeRegExp)args[0];
      } else {
        String src = net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.toString(args[0]);
        String opt;
        String opt; if (optarg < args.length) {
          args[0] = src;
          opt = net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.toString(args[optarg]);
        } else {
          opt = null;
        }
        RECompiled compiled = NativeRegExp.compileRE(cx, src, opt, forceFlat);
        
        re = new NativeRegExp(topScope, compiled);
      } }
    return re;
  }
  




  private static Object matchOrReplace(net.sourceforge.htmlunit.corejs.javascript.Context cx, Scriptable scope, Scriptable thisObj, Object[] args, RegExpImpl reImpl, GlobData data, NativeRegExp re)
  {
    String str = str;
    global = ((re.getFlags() & 0x1) != 0);
    int[] indexp = { 0 };
    Object result = null;
    if (mode == 3) {
      result = re.executeRegExp(cx, scope, reImpl, str, indexp, 0);
      
      if ((result != null) && (result.equals(Boolean.TRUE))) {
        result = Integer.valueOf(leftContext.length);
      } else
        result = Integer.valueOf(-1);
    } else if (global) {
      lastIndex = Double.valueOf(0.0D);
      for (int count = 0; indexp[0] <= str.length(); count++) {
        result = re.executeRegExp(cx, scope, reImpl, str, indexp, 0);
        
        if ((result == null) || (!result.equals(Boolean.TRUE)))
          break;
        if (mode == 1) {
          match_glob(data, cx, scope, count, reImpl);
        } else {
          if (mode != 2)
            net.sourceforge.htmlunit.corejs.javascript.Kit.codeBug();
          SubString lastMatch = lastMatch;
          int leftIndex = leftIndex;
          int leftlen = index - leftIndex;
          leftIndex = (index + length);
          replace_glob(data, cx, scope, reImpl, leftIndex, leftlen);
        }
        if (lastMatch.length == 0) {
          if (indexp[0] == str.length())
            break;
          indexp[0] += 1;
        }
      }
    } else {
      result = re.executeRegExp(cx, scope, reImpl, str, indexp, mode == 2 ? 0 : 1);
    }
    


    return result;
  }
  

  public int find_split(net.sourceforge.htmlunit.corejs.javascript.Context cx, Scriptable scope, String target, String separator, Scriptable reObj, int[] ip, int[] matchlen, boolean[] matched, String[][] parensp)
  {
    int i = ip[0];
    int length = target.length();
    

    int version = cx.getLanguageVersion();
    NativeRegExp re = (NativeRegExp)reObj;
    for (;;)
    {
      int ipsave = ip[0];
      ip[0] = i;
      Object ret = re.executeRegExp(cx, scope, this, target, ip, 0);
      
      if (ret != Boolean.TRUE)
      {
        ip[0] = ipsave;
        matchlen[0] = 1;
        matched[0] = false;
        return length;
      }
      i = ip[0];
      ip[0] = ipsave;
      matched[0] = true;
      
      SubString sep = lastMatch;
      matchlen[0] = length;
      if (matchlen[0] != 0) {
        break;
      }
      


      if (i != ip[0]) {
        break;
      }
      


      if (i == length) {
        if (version == 120) {
          matchlen[0] = 1;
          int result = i;
          break label176; }
        int result = -1;
        break label176;
      }
      i++;
    }
    


    int result = i - matchlen[0];
    
    label176:
    int size = parens == null ? 0 : parens.length;
    parensp[0] = new String[size];
    for (int num = 0; num < size; num++) {
      SubString parsub = getParenSubString(num);
      parensp[0][num] = parsub.toString();
    }
    return result;
  }
  



  SubString getParenSubString(int i)
  {
    if ((parens != null) && (i < parens.length)) {
      SubString parsub = parens[i];
      if (parsub != null) {
        return parsub;
      }
    }
    return SubString.emptySubString;
  }
  



  private static void match_glob(GlobData mdata, net.sourceforge.htmlunit.corejs.javascript.Context cx, Scriptable scope, int count, RegExpImpl reImpl)
  {
    if (arrayobj == null) {
      arrayobj = cx.newArray(scope, 0);
    }
    SubString matchsub = lastMatch;
    String matchstr = matchsub.toString();
    arrayobj.put(count, arrayobj, matchstr);
  }
  

  private static void replace_glob(GlobData rdata, net.sourceforge.htmlunit.corejs.javascript.Context cx, Scriptable scope, RegExpImpl reImpl, int leftIndex, int leftlen)
  {
    int replen;
    
    String lambdaStr;
    int replen;
    if (lambda != null)
    {

      SubString[] parens = parens;
      int parenCount = parens == null ? 0 : parens.length;
      Object[] args = new Object[parenCount + 3];
      args[0] = lastMatch.toString();
      for (int i = 0; i < parenCount; i++) {
        SubString sub = parens[i];
        if (sub != null) {
          args[(i + 1)] = sub.toString();
        } else {
          args[(i + 1)] = net.sourceforge.htmlunit.corejs.javascript.Undefined.instance;
        }
      }
      args[(parenCount + 1)] = Integer.valueOf(leftContext.length);
      args[(parenCount + 2)] = str;
      



      if (reImpl != net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.getRegExpProxy(cx))
        net.sourceforge.htmlunit.corejs.javascript.Kit.codeBug();
      RegExpImpl re2 = new RegExpImpl();
      multiline = multiline;
      input = input;
      net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.setRegExpProxy(cx, re2);
      try {
        Scriptable parent = net.sourceforge.htmlunit.corejs.javascript.ScriptableObject.getTopLevelScope(scope);
        Object result = lambda.call(cx, parent, parent, args);
        lambdaStr = net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.toString(result);
      } finally { String lambdaStr;
        net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.setRegExpProxy(cx, reImpl); }
      String lambdaStr;
      replen = lambdaStr.length();
    } else {
      lambdaStr = null;
      replen = repstr.length();
      if (dollar >= 0) {
        int[] skip = new int[1];
        int dp = dollar;
        do {
          SubString sub = interpretDollar(cx, reImpl, repstr, dp, skip);
          
          if (sub != null) {
            replen += length - skip[0];
            dp += skip[0];
          } else {
            dp++;
          }
          dp = repstr.indexOf('$', dp);
        } while (dp >= 0);
      }
    }
    
    int growth = leftlen + replen + rightContext.length;
    StringBuilder charBuf = rdata.charBuf;
    if (charBuf == null) {
      charBuf = new StringBuilder(growth);
      rdata.charBuf = charBuf;
    } else {
      charBuf.ensureCapacity(rdata.charBuf.length() + growth);
    }
    
    charBuf.append(leftContext.str, leftIndex, leftIndex + leftlen);
    if (lambda != null) {
      charBuf.append(lambdaStr);
    } else {
      do_replace(rdata, cx, reImpl);
    }
  }
  



  private static SubString interpretDollar(net.sourceforge.htmlunit.corejs.javascript.Context cx, RegExpImpl res, String da, int dp, int[] skip)
  {
    if (da.charAt(dp) != '$') {
      net.sourceforge.htmlunit.corejs.javascript.Kit.codeBug();
    }
    
    int version = cx.getLanguageVersion();
    if ((version != 0) && (version <= 140))
    {
      if ((dp > 0) && (da.charAt(dp - 1) == '\\'))
        return null;
    }
    int daL = da.length();
    if (dp + 1 >= daL) {
      return null;
    }
    char dc = da.charAt(dp + 1);
    if (NativeRegExp.isDigit(dc))
    {
      if ((version != 0) && (version <= 140))
      {
        if (dc == '0') {
          return null;
        }
        


        int num = 0;
        int cp = dp;
        for (;;) { cp++; if ((cp >= daL) || (!NativeRegExp.isDigit(dc = da.charAt(cp)))) break;
          int tmp = 10 * num + (dc - '0');
          if (tmp < num)
            break;
          num = tmp;
        }
      }
      int parenCount = parens == null ? 0 : parens.length;
      int num = dc - '0';
      if (num > parenCount)
        return null;
      int cp = dp + 2;
      if (dp + 2 < daL) {
        dc = da.charAt(dp + 2);
        if (NativeRegExp.isDigit(dc)) {
          int tmp = 10 * num + (dc - '0');
          if (tmp <= parenCount) {
            cp++;
            num = tmp;
          }
        }
      }
      if (num == 0) {
        return null;
      }
      
      num--;
      skip[0] = (cp - dp);
      return res.getParenSubString(num);
    }
    
    skip[0] = 2;
    switch (dc) {
    case '$': 
      return new SubString("$");
    case '&': 
      return lastMatch;
    case '+': 
      return lastParen;
    case '`': 
      if (version == 120)
      {







        leftContext.index = 0;
        leftContext.length = lastMatch.index;
      }
      return leftContext;
    case '\'': 
      return rightContext;
    }
    return null;
  }
  



  private static void do_replace(GlobData rdata, net.sourceforge.htmlunit.corejs.javascript.Context cx, RegExpImpl regExpImpl)
  {
    StringBuilder charBuf = rdata.charBuf;
    int cp = 0;
    String da = repstr;
    int dp = dollar;
    if (dp != -1) {
      int[] skip = new int[1];
      do {
        int len = dp - cp;
        charBuf.append(da.substring(cp, dp));
        cp = dp;
        SubString sub = interpretDollar(cx, regExpImpl, da, dp, skip);
        if (sub != null) {
          len = length;
          if (len > 0) {
            charBuf.append(str, index, index + len);
          }
          cp += skip[0];
          dp += skip[0];
        } else {
          dp++;
        }
        dp = da.indexOf('$', dp);
      } while (dp >= 0);
    }
    int daL = da.length();
    if (daL > cp) {
      charBuf.append(da.substring(cp, daL));
    }
  }
  





  public Object js_split(net.sourceforge.htmlunit.corejs.javascript.Context cx, Scriptable scope, String target, Object[] args)
  {
    Scriptable result = cx.newArray(scope, 0);
    

    boolean limited = (args.length > 1) && (args[1] != net.sourceforge.htmlunit.corejs.javascript.Undefined.instance);
    long limit = 0L;
    if (limited)
    {
      limit = net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.toUint32(args[1]);
      if (limit > target.length()) {
        limit = 1 + target.length();
      }
    }
    
    if ((args.length < 1) || (args[0] == net.sourceforge.htmlunit.corejs.javascript.Undefined.instance)) {
      result.put(0, result, target);
      return result;
    }
    
    String separator = null;
    int[] matchlen = new int[1];
    Scriptable re = null;
    net.sourceforge.htmlunit.corejs.javascript.RegExpProxy reProxy = null;
    if ((args[0] instanceof Scriptable)) {
      reProxy = net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.getRegExpProxy(cx);
      if (reProxy != null) {
        Scriptable test = (Scriptable)args[0];
        if (reProxy.isRegExp(test)) {
          re = test;
        }
      }
    }
    if (re == null) {
      separator = net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.toString(args[0]);
      matchlen[0] = separator.length();
    }
    

    int[] ip = { 0 };
    
    int len = 0;
    boolean[] matched = { false };
    String[][] parens = { null };
    int version = cx.getLanguageVersion();
    int match; while ((match = find_split(cx, scope, target, separator, version, reProxy, re, ip, matchlen, matched, parens)) >= 0)
    {
      if (((limited) && (len >= limit)) || (match > target.length()))
        break;
      String substr;
      String substr;
      if (target.length() == 0) {
        substr = target;
      } else {
        substr = target.substring(ip[0], match);
      }
      result.put(len, result, substr);
      len++;
      




      if ((re != null) && (matched[0] == 1)) {
        int size = parens[0].length;
        for (int num = 0; num < size; num++) {
          if ((limited) && (len >= limit))
            break;
          result.put(len, result, parens[0][num]);
          len++;
        }
        matched[0] = false;
      }
      ip[0] = (match + matchlen[0]);
      
      if ((version < 130) && (version != 0) && 
      




        (!limited) && (ip[0] == target.length())) {
        break;
      }
    }
    return result;
  }
  











  private static int find_split(net.sourceforge.htmlunit.corejs.javascript.Context cx, Scriptable scope, String target, String separator, int version, net.sourceforge.htmlunit.corejs.javascript.RegExpProxy reProxy, Scriptable re, int[] ip, int[] matchlen, boolean[] matched, String[][] parensp)
  {
    int i = ip[0];
    int length = target.length();
    





    if ((version == 120) && (re == null) && 
      (separator.length() == 1) && (separator.charAt(0) == ' '))
    {
      if (i == 0) {
        while ((i < length) && (Character.isWhitespace(target.charAt(i))))
          i++;
        ip[0] = i;
      }
      

      if (i == length) {
        return -1;
      }
      
      while ((i < length) && (!Character.isWhitespace(target.charAt(i)))) {
        i++;
      }
      
      int j = i;
      while ((j < length) && (Character.isWhitespace(target.charAt(j)))) {
        j++;
      }
      
      matchlen[0] = (j - i);
      return i;
    }
    










    if (i > length) {
      return -1;
    }
    




    if (re != null) {
      return reProxy.find_split(cx, scope, target, separator, re, ip, matchlen, matched, parensp);
    }
    






    if ((version != 0) && (version < 130) && (length == 0))
    {
      return -1;
    }
    









    if (separator.length() == 0) {
      if (version == 120) {
        if (i == length) {
          matchlen[0] = 1;
          return i;
        }
        return i + 1;
      }
      return i == length ? -1 : i + 1;
    }
    




    if (ip[0] >= length) {
      return length;
    }
    i = target.indexOf(separator, ip[0]);
    
    return i != -1 ? i : length;
  }
}
