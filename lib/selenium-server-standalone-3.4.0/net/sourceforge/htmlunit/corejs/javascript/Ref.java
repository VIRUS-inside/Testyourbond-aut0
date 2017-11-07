package net.sourceforge.htmlunit.corejs.javascript;

import java.io.Serializable;







public abstract class Ref
  implements Serializable
{
  static final long serialVersionUID = 4044540354730911424L;
  
  public Ref() {}
  
  public boolean has(Context cx)
  {
    return true;
  }
  

  public abstract Object get(Context paramContext);
  

  @Deprecated
  public abstract Object set(Context paramContext, Object paramObject);
  
  public Object set(Context cx, Scriptable scope, Object value)
  {
    return set(cx, value);
  }
  
  public boolean delete(Context cx) {
    return false;
  }
}
