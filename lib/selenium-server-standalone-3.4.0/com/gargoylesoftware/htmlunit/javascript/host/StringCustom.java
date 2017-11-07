package com.gargoylesoftware.htmlunit.javascript.host;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;






























public final class StringCustom
{
  private StringCustom() {}
  
  public static boolean contains(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    if (args.length < 1) {
      return false;
    }
    String string = Context.toString(thisObj);
    String search = Context.toString(args[0]);
    
    if (args.length < 2) {
      return string.contains(search);
    }
    
    int start = (int)Math.max(0.0D, Context.toNumber(args[1]));
    return string.indexOf(search, start) > -1;
  }
}
