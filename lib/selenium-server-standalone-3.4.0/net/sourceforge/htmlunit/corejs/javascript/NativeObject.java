package net.sourceforge.htmlunit.corejs.javascript;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;






public class NativeObject
  extends IdScriptableObject
  implements Map
{
  static final long serialVersionUID = -6345305608474346996L;
  
  public NativeObject() {}
  
  private static final Object OBJECT_TAG = "Object";
  
  static void init(Scriptable scope, boolean sealed) {
    NativeObject obj = new NativeObject();
    obj.exportAsJSClass(12, scope, sealed); }
  
  private static final int ConstructorId_getPrototypeOf = -1;
  private static final int ConstructorId_keys = -2;
  private static final int ConstructorId_getOwnPropertyNames = -3;
  public String getClassName() { return "Object"; }
  

  public String toString()
  {
    return ScriptRuntime.defaultObjectToString(this);
  }
  
  protected void fillConstructorProperties(IdFunctionObject ctor)
  {
    addIdFunctionProperty(ctor, OBJECT_TAG, -1, "getPrototypeOf", 1);
    
    addIdFunctionProperty(ctor, OBJECT_TAG, -2, "keys", 1);
    addIdFunctionProperty(ctor, OBJECT_TAG, -3, "getOwnPropertyNames", 1);
    
    addIdFunctionProperty(ctor, OBJECT_TAG, -4, "getOwnPropertyDescriptor", 2);
    

    addIdFunctionProperty(ctor, OBJECT_TAG, -5, "defineProperty", 3);
    
    addIdFunctionProperty(ctor, OBJECT_TAG, -6, "isExtensible", 1);
    
    addIdFunctionProperty(ctor, OBJECT_TAG, -7, "preventExtensions", 1);
    
    addIdFunctionProperty(ctor, OBJECT_TAG, -8, "defineProperties", 2);
    
    addIdFunctionProperty(ctor, OBJECT_TAG, -9, "create", 2);
    
    addIdFunctionProperty(ctor, OBJECT_TAG, -10, "isSealed", 1);
    
    addIdFunctionProperty(ctor, OBJECT_TAG, -11, "isFrozen", 1);
    
    addIdFunctionProperty(ctor, OBJECT_TAG, -12, "seal", 1);
    addIdFunctionProperty(ctor, OBJECT_TAG, -13, "freeze", 1);
    
    addIdFunctionProperty(ctor, OBJECT_TAG, -14, "assign", 2);
    
    super.fillConstructorProperties(ctor); }
  
  protected void initPrototypeId(int id) { String s;
    String s;
    String s;
    String s;
    String s;
    String s; String s; String s; String s; String s; String s; String s; switch (id) {
    case 1: 
      int arity = 1;
      s = "constructor";
      break;
    case 2: 
      int arity = 0;
      s = "toString";
      break;
    case 3: 
      int arity = 0;
      s = "toLocaleString";
      break;
    case 4: 
      int arity = 0;
      s = "valueOf";
      break;
    case 5: 
      int arity = 1;
      s = "hasOwnProperty";
      break;
    case 6: 
      int arity = 1;
      s = "propertyIsEnumerable";
      break;
    case 7: 
      int arity = 1;
      s = "isPrototypeOf";
      break;
    case 8: 
      int arity = 0;
      s = "toSource";
      break;
    case 9: 
      int arity = 2;
      s = "__defineGetter__";
      break;
    case 10: 
      int arity = 2;
      s = "__defineSetter__";
      break;
    case 11: 
      int arity = 1;
      s = "__lookupGetter__";
      break;
    case 12: 
      int arity = 1;
      s = "__lookupSetter__";
      break;
    default: 
      throw new IllegalArgumentException(String.valueOf(id)); }
    int arity;
    String s; initPrototypeMethod(OBJECT_TAG, id, s, arity);
  }
  
  private static final int ConstructorId_getOwnPropertyDescriptor = -4;
  private static final int ConstructorId_defineProperty = -5;
  private static final int ConstructorId_isExtensible = -6;
  public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) { if (!f.hasTag(OBJECT_TAG)) {
      return super.execIdCall(f, cx, scope, thisObj, args);
    }
    int id = f.methodId();
    ScriptableObject desc; ScriptableObject newObject; Scriptable props; Object name; switch (id) {
    case 1: 
      if (thisObj != null)
      {
        return f.construct(cx, scope, args);
      }
      if ((args.length == 0) || (args[0] == null) || (args[0] == Undefined.instance))
      {
        return new NativeObject();
      }
      return ScriptRuntime.toObject(cx, scope, args[0]);
    

    case 3: 
      Object toString = ScriptableObject.getProperty(thisObj, "toString");
      if (!(toString instanceof Callable)) {
        throw ScriptRuntime.notFunctionError(toString);
      }
      Callable fun = (Callable)toString;
      return fun.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);
    

    case 2: 
      if (cx.hasFeature(4)) {
        String s = ScriptRuntime.defaultObjectToSource(cx, scope, thisObj, args);
        
        int L = s.length();
        if ((L != 0) && (s.charAt(0) == '(') && (s.charAt(L - 1) == ')'))
        {
          s = s.substring(1, L - 1);
        }
        return s;
      }
      return ScriptRuntime.defaultObjectToString(thisObj);
    

    case 4: 
      return thisObj;
    

    case 5: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      String s = ScriptRuntime.toStringIdOrIndex(cx, arg);
      boolean result; boolean result; if (s == null) {
        int index = ScriptRuntime.lastIndexResult(cx);
        result = thisObj.has(index, thisObj);
      } else {
        result = thisObj.has(s, thisObj);
      }
      return ScriptRuntime.wrapBoolean(result);
    


    case 6: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      String s = ScriptRuntime.toStringIdOrIndex(cx, arg);
      boolean result; if (s == null) {
        int index = ScriptRuntime.lastIndexResult(cx);
        boolean result = thisObj.has(index, thisObj);
        if ((result) && ((thisObj instanceof ScriptableObject))) {
          ScriptableObject so = (ScriptableObject)thisObj;
          int attrs = so.getAttributes(index);
          result = (attrs & 0x2) == 0;
        }
      } else {
        result = thisObj.has(s, thisObj);
        if ((result) && ((thisObj instanceof ScriptableObject))) {
          ScriptableObject so = (ScriptableObject)thisObj;
          int attrs = so.getAttributes(s);
          result = (attrs & 0x2) == 0;
        }
      }
      return ScriptRuntime.wrapBoolean(result);
    

    case 7: 
      boolean result = false;
      if ((args.length != 0) && ((args[0] instanceof Scriptable))) {
        Scriptable v = (Scriptable)args[0];
        do {
          v = v.getPrototype();
          if (v == thisObj) {
            result = true;
            break;
          }
        } while (v != null);
      }
      return ScriptRuntime.wrapBoolean(result);
    

    case 8: 
      return ScriptRuntime.defaultObjectToSource(cx, scope, thisObj, args);
    
    case 9: 
    case 10: 
      if ((args.length < 2) || (!(args[1] instanceof Callable))) {
        Object badArg = args.length >= 2 ? args[1] : Undefined.instance;
        
        throw ScriptRuntime.notFunctionError(badArg);
      }
      if (!(thisObj instanceof ScriptableObject)) {
        throw Context.reportRuntimeError2("msg.extend.scriptable", thisObj
          .getClass().getName(), String.valueOf(args[0]));
      }
      ScriptableObject so = (ScriptableObject)thisObj;
      String name = ScriptRuntime.toStringIdOrIndex(cx, args[0]);
      int index = name != null ? 0 : ScriptRuntime.lastIndexResult(cx);
      Callable getterOrSetter = (Callable)args[1];
      boolean isSetter = id == 10;
      so.setGetterOrSetter(name, index, getterOrSetter, isSetter);
      if ((so instanceof NativeArray)) {
        ((NativeArray)so).setDenseOnly(false);
      }
      return Undefined.instance;
    
    case 11: 
    case 12: 
      if ((args.length < 1) || (!(thisObj instanceof ScriptableObject))) {
        return Undefined.instance;
      }
      ScriptableObject so = (ScriptableObject)thisObj;
      String name = ScriptRuntime.toStringIdOrIndex(cx, args[0]);
      int index = name != null ? 0 : ScriptRuntime.lastIndexResult(cx);
      boolean isSetter = id == 12;
      Object gs;
      for (;;) {
        gs = so.getGetterOrSetter(name, index, isSetter);
        if (gs != null) {
          break;
        }
        
        Scriptable v = so.getPrototype();
        if (v == null)
          break;
        if (!(v instanceof ScriptableObject)) break;
        so = (ScriptableObject)v;
      }
      

      if (gs != null) {
        if ((gs instanceof MemberBox)) {
          gs = ((MemberBox)gs).asFunction(name, f.getParentScope(), f
            .getPrototype());
        }
        return gs;
      }
      
      return Undefined.instance;
    
    case -1: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      if (cx.hasFeature(112))
      {
        if ((arg instanceof String))
          return "";
        if ((arg instanceof Number))
          return Integer.valueOf(0);
        if ((arg instanceof Boolean)) {
          return Boolean.valueOf(false);
        }
      }
      Scriptable obj = ensureScriptable(arg);
      return obj.getPrototype();
    
    case -2: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      Scriptable obj = ensureScriptable(arg);
      Object[] ids = obj.getIds();
      for (int i = 0; i < ids.length; i++) {
        ids[i] = ScriptRuntime.toString(ids[i]);
      }
      return cx.newArray(scope, ids);
    
    case -3: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      ScriptableObject obj = ensureScriptableObject(arg);
      Object[] ids = obj.getAllIds();
      for (int i = 0; i < ids.length; i++) {
        ids[i] = ScriptRuntime.toString(ids[i]);
      }
      return cx.newArray(scope, ids);
    
    case -4: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      


      ScriptableObject obj = ensureScriptableObject(arg);
      Object nameArg = args.length < 2 ? Undefined.instance : args[1];
      String name = ScriptRuntime.toString(nameArg);
      Scriptable desc = obj.getOwnPropertyDescriptor(cx, name);
      return desc == null ? Undefined.instance : desc;
    
    case -5: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      ScriptableObject obj = ensureScriptableObject(arg);
      Object name = args.length < 2 ? Undefined.instance : args[1];
      Object descArg = args.length < 3 ? Undefined.instance : args[2];
      desc = ensureScriptableObject(descArg);
      obj.defineOwnProperty(cx, name, desc);
      return obj;
    
    case -6: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      ScriptableObject obj = ensureScriptableObject(arg);
      return Boolean.valueOf(obj.isExtensible());
    
    case -7: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      ScriptableObject obj = ensureScriptableObject(arg);
      obj.preventExtensions();
      return obj;
    
    case -8: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      ScriptableObject obj = ensureScriptableObject(arg);
      Object propsObj = args.length < 2 ? Undefined.instance : args[1];
      Scriptable props = Context.toObject(propsObj, getParentScope());
      obj.defineOwnProperties(cx, ensureScriptableObject(props));
      return obj;
    
    case -9: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      Scriptable obj = arg == null ? null : ensureScriptable(arg);
      
      newObject = new NativeObject();
      newObject.setParentScope(getParentScope());
      newObject.setPrototype(obj);
      
      if ((args.length > 1) && (args[1] != Undefined.instance)) {
        props = Context.toObject(args[1], getParentScope());
        newObject.defineOwnProperties(cx, 
          ensureScriptableObject(props));
      }
      
      return newObject;
    
    case -10: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      ScriptableObject obj = ensureScriptableObject(arg);
      
      if (obj.isExtensible()) {
        return Boolean.FALSE;
      }
      newObject = obj.getAllIds();props = newObject.length; for (desc = 0; desc < props; desc++) { Object name = newObject[desc];
        
        Object configurable = obj.getOwnPropertyDescriptor(cx, name).get("configurable");
        if (Boolean.TRUE.equals(configurable)) {
          return Boolean.FALSE;
        }
      }
      return Boolean.TRUE;
    
    case -11: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      ScriptableObject obj = ensureScriptableObject(arg);
      
      if (obj.isExtensible()) {
        return Boolean.FALSE;
      }
      newObject = obj.getAllIds();props = newObject.length; for (desc = 0; desc < props; desc++) { Object name = newObject[desc];
        ScriptableObject desc = obj.getOwnPropertyDescriptor(cx, name);
        if (Boolean.TRUE.equals(desc.get("configurable")))
          return Boolean.FALSE;
        if ((isDataDescriptor(desc)) && 
          (Boolean.TRUE.equals(desc.get("writable")))) {
          return Boolean.FALSE;
        }
      }
      return Boolean.TRUE;
    
    case -12: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      ScriptableObject obj = ensureScriptableObject(arg);
      
      newObject = obj.getAllIds();props = newObject.length; for (desc = 0; desc < props; desc++) { Object name = newObject[desc];
        ScriptableObject desc = obj.getOwnPropertyDescriptor(cx, name);
        if (Boolean.TRUE.equals(desc.get("configurable"))) {
          desc.put("configurable", desc, Boolean.FALSE);
          obj.defineOwnProperty(cx, name, desc, false);
        }
      }
      obj.preventExtensions();
      
      return obj;
    
    case -13: 
      Object arg = args.length < 1 ? Undefined.instance : args[0];
      ScriptableObject obj = ensureScriptableObject(arg);
      
      newObject = obj.getAllIds();props = newObject.length; for (desc = 0; desc < props; desc++) { name = newObject[desc];
        ScriptableObject desc = obj.getOwnPropertyDescriptor(cx, name);
        if ((isDataDescriptor(desc)) && 
          (Boolean.TRUE.equals(desc.get("writable"))))
          desc.put("writable", desc, Boolean.FALSE);
        if (Boolean.TRUE.equals(desc.get("configurable")))
          desc.put("configurable", desc, Boolean.FALSE);
        obj.defineOwnProperty(cx, name, desc, false);
      }
      obj.preventExtensions();
      
      return obj;
    
    case -14: 
      ScriptableObject target = ensureScriptableObject(args[0]);
      for (int i = 1; i < args.length; i++) {
        if (args[i] != Undefined.instance) {
          ScriptableObject obj = ensureScriptableObject(args[i]);
          props = obj.getIds();desc = props.length; for (name = 0; name < desc; name++) { Object objId = props[name];
            target.defineOwnProperty(cx, objId, obj
              .getOwnPropertyDescriptor(cx, objId));
          }
        }
      }
      return target;
    }
    
    
    throw new IllegalArgumentException(String.valueOf(id));
  }
  
  private static final int ConstructorId_preventExtensions = -7;
  private static final int ConstructorId_defineProperties = -8;
  private static final int ConstructorId_create = -9;
  
  public boolean containsKey(Object key) { if ((key instanceof String))
      return has((String)key, this);
    if ((key instanceof Number)) {
      return has(((Number)key).intValue(), this);
    }
    return false; }
  
  private static final int ConstructorId_isSealed = -10;
  
  public boolean containsValue(Object value) { for (Object obj : values()) {
      if ((value == obj) || ((value != null) && (value.equals(obj)))) {
        return true;
      }
    }
    return false; }
  
  private static final int ConstructorId_isFrozen = -11;
  
  public Object remove(Object key) { Object value = get(key);
    if ((key instanceof String)) {
      delete((String)key);
    } else if ((key instanceof Number)) {
      delete(((Number)key).intValue());
    }
    return value; }
  
  private static final int ConstructorId_seal = -12;
  private static final int ConstructorId_freeze = -13;
  public Set<Object> keySet() { return new KeySet(); }
  
  public Collection<Object> values()
  {
    return new ValueCollection();
  }
  
  public Set<Map.Entry<Object, Object>> entrySet() {
    return new EntrySet();
  }
  
  public Object put(Object key, Object value) {
    throw new UnsupportedOperationException();
  }
  
  public void putAll(Map m) {
    throw new UnsupportedOperationException();
  }
  
  public void clear() {
    throw new UnsupportedOperationException();
  }
  
  class EntrySet extends AbstractSet<Map.Entry<Object, Object>> {
    EntrySet() {}
    
    public Iterator<Map.Entry<Object, Object>> iterator() { new Iterator() {
        Object[] ids = getIds();
        Object key = null;
        int index = 0;
        
        public boolean hasNext() {
          return index < ids.length;
        }
        
        public Map.Entry<Object, Object> next() {
          final Object ekey = this.key = ids[(index++)];
          final Object value = get(key);
          new Map.Entry() {
            public Object getKey() {
              return ekey;
            }
            
            public Object getValue() {
              return value;
            }
            
            public Object setValue(Object value) {
              throw new UnsupportedOperationException();
            }
            
            public boolean equals(Object other)
            {
              if (!(other instanceof Map.Entry)) {
                return false;
              }
              Map.Entry<?, ?> e = (Map.Entry)other;
              return (ekey == null ? e.getKey() == null : ekey
                .equals(e.getKey())) && (value == null ? e
                .getValue() == null : value
                .equals(e.getValue()));
            }
            
            public int hashCode()
            {
              return 
                (ekey == null ? 0 : ekey.hashCode()) ^ (value == null ? 0 : value.hashCode());
            }
            
            public String toString()
            {
              return ekey + "=" + value;
            }
          };
        }
        
        public void remove() {
          if (key == null) {
            throw new IllegalStateException();
          }
          remove(key);
          key = null;
        }
      }; }
    

    public int size()
    {
      return NativeObject.this.size();
    }
  }
  
  class KeySet extends AbstractSet<Object> {
    KeySet() {}
    
    public boolean contains(Object key) {
      return containsKey(key);
    }
    
    public Iterator<Object> iterator()
    {
      new Iterator() {
        Object[] ids = getIds();
        Object key;
        int index = 0;
        
        public boolean hasNext() {
          return index < ids.length;
        }
        
        public Object next() {
          try {
            return this.key = ids[(index++)];
          } catch (ArrayIndexOutOfBoundsException e) {
            key = null;
            throw new NoSuchElementException();
          }
        }
        
        public void remove() {
          if (key == null) {
            throw new IllegalStateException();
          }
          remove(key);
          key = null;
        }
      };
    }
    
    public int size()
    {
      return NativeObject.this.size();
    }
  }
  
  class ValueCollection extends AbstractCollection<Object> {
    ValueCollection() {}
    
    public Iterator<Object> iterator() {
      new Iterator() {
        Object[] ids = getIds();
        Object key;
        int index = 0;
        
        public boolean hasNext() {
          return index < ids.length;
        }
        
        public Object next() {
          return get(this.key = ids[(index++)]);
        }
        
        public void remove() {
          if (key == null) {
            throw new IllegalStateException();
          }
          remove(key);
          key = null;
        }
      };
    }
    


    public int size() { return NativeObject.this.size(); }
  }
  
  private static final int ConstructorId_assign = -14;
  private static final int Id_constructor = 1;
  private static final int Id_toString = 2;
  private static final int Id_toLocaleString = 3;
  private static final int Id_valueOf = 4;
  private static final int Id_hasOwnProperty = 5;
  private static final int Id_propertyIsEnumerable = 6;
  
  protected int findPrototypeId(String s) { int id = 0;
    String X = null;
    
    switch (s.length()) {
    case 7: 
      X = "valueOf";
      id = 4;
      break;
    case 8: 
      int c = s.charAt(3);
      if (c == 111) {
        X = "toSource";
        id = 8;
      } else if (c == 116) {
        X = "toString";
        id = 2;
      }
      break;
    case 11: 
      X = "constructor";
      id = 1;
      break;
    case 13: 
      X = "isPrototypeOf";
      id = 7;
      break;
    case 14: 
      int c = s.charAt(0);
      if (c == 104) {
        X = "hasOwnProperty";
        id = 5;
      } else if (c == 116) {
        X = "toLocaleString";
        id = 3;
      }
      break;
    case 16: 
      int c = s.charAt(2);
      if (c == 100) {
        c = s.charAt(8);
        if (c == 71) {
          X = "__defineGetter__";
          id = 9;
        } else if (c == 83) {
          X = "__defineSetter__";
          id = 10;
        }
      } else if (c == 108) {
        c = s.charAt(8);
        if (c == 71) {
          X = "__lookupGetter__";
          id = 11;
        } else if (c == 83) {
          X = "__lookupSetter__";
          id = 12;
        }
      }
      break;
    case 20: 
      X = "propertyIsEnumerable";
      id = 6;
      break;
    }
    if ((X != null) && (X != s) && (!X.equals(s))) {
      id = 0;
    }
    

    return id;
  }
  
  private static final int Id_isPrototypeOf = 7;
  private static final int Id_toSource = 8;
  private static final int Id___defineGetter__ = 9;
  private static final int Id___defineSetter__ = 10;
  private static final int Id___lookupGetter__ = 11;
  private static final int Id___lookupSetter__ = 12;
  private static final int MAX_PROTOTYPE_ID = 12;
}
