package net.sourceforge.htmlunit.corejs.javascript;


public class IdFunctionObject
  extends BaseFunction
{
  static final long serialVersionUID = -5332312783643935019L;
  private final IdFunctionCall idcall;
  private final Object tag;
  private final int methodId;
  private int arity;
  private boolean useCallAsConstructor;
  private String functionName;
  
  public IdFunctionObject(IdFunctionCall idcall, Object tag, int id, int arity)
  {
    if (arity < 0) {
      throw new IllegalArgumentException();
    }
    this.idcall = idcall;
    this.tag = tag;
    methodId = id;
    this.arity = arity;
    if (arity < 0) {
      throw new IllegalArgumentException();
    }
  }
  
  public IdFunctionObject(IdFunctionCall idcall, Object tag, int id, String name, int arity, Scriptable scope) {
    super(scope, null);
    
    if (arity < 0)
      throw new IllegalArgumentException();
    if (name == null) {
      throw new IllegalArgumentException();
    }
    this.idcall = idcall;
    this.tag = tag;
    methodId = id;
    this.arity = arity;
    functionName = name;
  }
  
  public void initFunction(String name, Scriptable scope) {
    if (name == null)
      throw new IllegalArgumentException();
    if (scope == null)
      throw new IllegalArgumentException();
    functionName = name;
    setParentScope(scope);
  }
  
  public final boolean hasTag(Object tag) {
    return tag == null ? false : this.tag == null ? true : tag.equals(this.tag);
  }
  
  public Object getTag() {
    return tag;
  }
  
  public final int methodId() {
    return methodId;
  }
  
  public final void markAsConstructor(Scriptable prototypeProperty) {
    useCallAsConstructor = true;
    setImmunePrototypeProperty(prototypeProperty);
  }
  
  public final void addAsProperty(Scriptable target) {
    ScriptableObject.defineProperty(target, functionName, this, 2);
  }
  
  public void exportAsScopeProperty()
  {
    addAsProperty(getParentScope());
  }
  


  public Scriptable getPrototype()
  {
    Scriptable proto = super.getPrototype();
    if (proto == null) {
      proto = getFunctionPrototype(getParentScope());
      setPrototype(proto);
    }
    return proto;
  }
  

  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    return idcall.execIdCall(this, cx, scope, thisObj, args);
  }
  
  public Scriptable createObject(Context cx, Scriptable scope)
  {
    if (useCallAsConstructor) {
      return null;
    }
    



    throw ScriptRuntime.typeError1("msg.not.ctor", functionName);
  }
  
  public int getArity()
  {
    return arity;
  }
  
  public int getLength()
  {
    return getArity();
  }
  
  public String getFunctionName()
  {
    return functionName == null ? "" : functionName;
  }
  
  public final RuntimeException unknown()
  {
    return new IllegalArgumentException("BAD FUNCTION ID=" + methodId + " MASTER=" + idcall);
  }
}
