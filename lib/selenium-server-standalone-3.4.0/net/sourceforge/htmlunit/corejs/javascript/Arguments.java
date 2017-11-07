package net.sourceforge.htmlunit.corejs.javascript;


class Arguments
  extends IdScriptableObject
{
  static final long serialVersionUID = 4275508002492040609L;
  
  private static final String FTAG = "Arguments";
  
  private static final int Id_callee = 1;
  
  private static final int Id_length = 2;
  
  private static final int Id_caller = 3;
  private static final int MAX_INSTANCE_ID = 3;
  private Object callerObj;
  private Object calleeObj;
  private Object lengthObj;
  
  public Arguments(NativeCall activation)
  {
    this.activation = activation;
    
    Scriptable parent = activation.getParentScope();
    setParentScope(parent);
    setPrototype(ScriptableObject.getObjectPrototype(parent));
    
    args = originalArgs;
    lengthObj = Integer.valueOf(args.length);
    
    NativeFunction f = function;
    calleeObj = f;
    
    int version = f.getLanguageVersion();
    if ((version <= 130) && (version != 0))
    {
      callerObj = null;
    } else {
      callerObj = NOT_FOUND;
    }
  }
  
  public Arguments(Arguments original) {
    activation = activation;
    
    setParentScope(original.getParentScope());
    setPrototype(original.getPrototype());
    
    args = args;
    lengthObj = lengthObj;
    calleeObj = calleeObj;
    
    callerObj = callerObj;
  }
  

  public String getClassName()
  {
    if (Context.getContext().hasFeature(102)) {
      return "Object";
    }
    return "Arguments";
  }
  
  private Object arg(int index) {
    if ((index < 0) || (args.length <= index))
      return NOT_FOUND;
    return args[index];
  }
  

  private void putIntoActivation(int index, Object value)
  {
    String argName = activation.function.getParamOrVarName(index);
    activation.put(argName, activation, value);
  }
  
  private Object getFromActivation(int index) {
    String argName = activation.function.getParamOrVarName(index);
    return activation.get(argName, activation);
  }
  
  private void replaceArg(int index, Object value) {
    if (sharedWithActivation(index)) {
      putIntoActivation(index, value);
    }
    synchronized (this) {
      if ((activation != null) && (args == activation.originalArgs)) {
        args = ((Object[])args.clone());
      }
      args[index] = value;
    }
  }
  
  private void removeArg(int index) {
    synchronized (this) {
      if (args[index] != NOT_FOUND) {
        if (args == activation.originalArgs) {
          args = ((Object[])args.clone());
        }
        args[index] = NOT_FOUND;
      }
    }
  }
  


  public boolean has(int index, Scriptable start)
  {
    if (arg(index) != NOT_FOUND) {
      return true;
    }
    return super.has(index, start);
  }
  
  public Object get(int index, Scriptable start)
  {
    Object value = arg(index);
    if (value == NOT_FOUND) {
      return super.get(index, start);
    }
    if (sharedWithActivation(index)) {
      return getFromActivation(index);
    }
    return value;
  }
  

  private boolean sharedWithActivation(int index)
  {
    NativeFunction f = activation.function;
    int definedCount = f.getParamCount();
    if (index < definedCount)
    {

      if (index < definedCount - 1) {
        String argName = f.getParamOrVarName(index);
        for (int i = index + 1; i < definedCount; i++) {
          if (argName.equals(f.getParamOrVarName(i))) {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }
  
  public void put(int index, Scriptable start, Object value)
  {
    if (arg(index) == NOT_FOUND) {
      super.put(index, start, value);
    } else {
      replaceArg(index, value);
    }
  }
  
  public void delete(int index)
  {
    if ((0 <= index) && (index < args.length)) {
      removeArg(index);
    }
    super.delete(index);
  }
  






  protected int getMaxInstanceId()
  {
    return 3;
  }
  



  protected int findInstanceIdInfo(String s)
  {
    int id = 0;
    String X = null;
    
    int s_length = s.length();
    if (s_length == 6) {
      int c = s.charAt(5);
      if (c == 101) {
        X = "callee";
        id = 1;
      } else if (c == 104) {
        X = "length";
        id = 2;
      } else if (c == 114) {
        X = "caller";
        id = 3;
      }
    }
    if ((X != null) && (X != s) && (!X.equals(s))) {
      id = 0;
    }
    


    if (id == 0)
      return super.findInstanceIdInfo(s);
    int attr;
    int attr;
    int attr; switch (id) {
    case 1: 
      attr = calleeAttr;
      break;
    case 3: 
      attr = callerAttr;
      break;
    case 2: 
      attr = lengthAttr;
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
      return "callee";
    case 2: 
      return "length";
    case 3: 
      return "caller";
    }
    return null;
  }
  
  protected Object getInstanceIdValue(int id)
  {
    switch (id) {
    case 1: 
      return calleeObj;
    case 2: 
      return lengthObj;
    case 3: 
      Object value = callerObj;
      if (value == UniqueTag.NULL_VALUE) {
        value = null;
      } else if (value == null) {
        NativeCall caller = activation.parentActivationCall;
        if (caller != null) {
          value = caller.get("arguments", caller);
        }
      }
      return value;
    }
    
    return super.getInstanceIdValue(id);
  }
  
  protected void setInstanceIdValue(int id, Object value)
  {
    switch (id) {
    case 1: 
      calleeObj = value;
      return;
    case 2: 
      lengthObj = value;
      return;
    case 3: 
      callerObj = (value != null ? value : UniqueTag.NULL_VALUE);
      return;
    }
    super.setInstanceIdValue(id, value);
  }
  
  protected void setInstanceIdAttributes(int id, int attr)
  {
    switch (id) {
    case 1: 
      calleeAttr = attr;
      return;
    case 2: 
      lengthAttr = attr;
      return;
    case 3: 
      callerAttr = attr;
      return;
    }
    super.setInstanceIdAttributes(id, attr);
  }
  
  Object[] getIds(boolean getAll)
  {
    Object[] ids = super.getIds(getAll);
    if (args.length != 0) {
      boolean[] present = new boolean[args.length];
      int extraCount = args.length;
      for (int i = 0; i != ids.length; i++) {
        Object id = ids[i];
        if ((id instanceof Integer)) {
          int index = ((Integer)id).intValue();
          if ((0 <= index) && (index < args.length) && 
            (present[index] == 0)) {
            present[index] = true;
            extraCount--;
          }
        }
      }
      
      if (!getAll)
      {
        for (int i = 0; i < present.length; i++) {
          if ((present[i] == 0) && (super.has(i, this))) {
            present[i] = true;
            extraCount--;
          }
        }
      }
      if (extraCount != 0) {
        Object[] tmp = new Object[extraCount + ids.length];
        System.arraycopy(ids, 0, tmp, extraCount, ids.length);
        ids = tmp;
        int offset = 0;
        for (int i = 0; i != args.length; i++) {
          if ((present == null) || (present[i] == 0)) {
            ids[offset] = Integer.valueOf(i);
            offset++;
          }
        }
        if (offset != extraCount)
          Kit.codeBug();
      }
    }
    return ids;
  }
  
  protected ScriptableObject getOwnPropertyDescriptor(Context cx, Object id)
  {
    double d = ScriptRuntime.toNumber(id);
    int index = (int)d;
    if (d != index) {
      return super.getOwnPropertyDescriptor(cx, id);
    }
    Object value = arg(index);
    if (value == NOT_FOUND) {
      return super.getOwnPropertyDescriptor(cx, id);
    }
    if (sharedWithActivation(index)) {
      value = getFromActivation(index);
    }
    if (super.has(index, this)) {
      ScriptableObject desc = super.getOwnPropertyDescriptor(cx, id);
      desc.put("value", desc, value);
      return desc;
    }
    Scriptable scope = getParentScope();
    if (scope == null)
      scope = this;
    return buildDataDescriptor(scope, value, 0);
  }
  


  protected void defineOwnProperty(Context cx, Object id, ScriptableObject desc, boolean checkValid)
  {
    super.defineOwnProperty(cx, id, desc, checkValid);
    
    double d = ScriptRuntime.toNumber(id);
    int index = (int)d;
    if (d != index) {
      return;
    }
    Object value = arg(index);
    if (value == NOT_FOUND) {
      return;
    }
    if (isAccessorDescriptor(desc)) {
      removeArg(index);
      return;
    }
    
    Object newValue = getProperty(desc, "value");
    if (newValue == NOT_FOUND) {
      return;
    }
    replaceArg(index, newValue);
    
    if (isFalse(getProperty(desc, "writable"))) {
      removeArg(index);
    }
  }
  
  public Object getDefaultValue(Class<?> typeHint)
  {
    return "[object " + getClassName() + "]";
  }
  









  private int callerAttr = 2;
  private int calleeAttr = 2;
  private int lengthAttr = 2;
  private NativeCall activation;
  private Object[] args;
}
