package net.sourceforge.htmlunit.corejs.javascript;
















public class Synchronizer
  extends Delegator
{
  private Object syncObject;
  















  public Synchronizer(Scriptable obj)
  {
    super(obj);
  }
  








  public Synchronizer(Scriptable obj, Object syncObject)
  {
    super(obj);
    this.syncObject = syncObject;
  }
  




  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    Object sync = syncObject != null ? syncObject : thisObj;
    synchronized ((sync instanceof Wrapper) ? ((Wrapper)sync).unwrap() : sync)
    {
      return ((Function)obj).call(cx, scope, thisObj, args);
    }
  }
}
