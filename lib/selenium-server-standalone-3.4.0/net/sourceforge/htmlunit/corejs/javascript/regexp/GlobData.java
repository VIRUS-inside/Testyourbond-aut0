package net.sourceforge.htmlunit.corejs.javascript.regexp;

import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
































































































































































































































































































































































































































































































































































































































































































































final class GlobData
{
  int mode;
  boolean global;
  String str;
  Scriptable arrayobj;
  Function lambda;
  String repstr;
  int dollar = -1;
  StringBuilder charBuf;
  int leftIndex;
  
  GlobData() {}
}
