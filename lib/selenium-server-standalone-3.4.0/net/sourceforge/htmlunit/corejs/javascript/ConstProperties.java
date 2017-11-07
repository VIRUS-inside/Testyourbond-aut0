package net.sourceforge.htmlunit.corejs.javascript;

public abstract interface ConstProperties
{
  public abstract void putConst(String paramString, Scriptable paramScriptable, Object paramObject);
  
  public abstract void defineConst(String paramString, Scriptable paramScriptable);
  
  public abstract boolean isConst(String paramString);
}
