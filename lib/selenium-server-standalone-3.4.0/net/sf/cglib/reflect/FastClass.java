package net.sf.cglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.AbstractClassGenerator.Source;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;











public abstract class FastClass
{
  private Class type;
  
  protected FastClass()
  {
    throw new Error("Using the FastClass empty constructor--please report to the cglib-devel mailing list");
  }
  
  protected FastClass(Class type) {
    this.type = type;
  }
  
  public static FastClass create(Class type)
  {
    return create(type.getClassLoader(), type);
  }
  
  public static FastClass create(ClassLoader loader, Class type) {
    Generator gen = new Generator();
    gen.setType(type);
    gen.setClassLoader(loader);
    return gen.create();
  }
  
  public static class Generator extends AbstractClassGenerator
  {
    private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(FastClass.class.getName());
    private Class type;
    
    public Generator() {
      super();
    }
    
    public void setType(Class type) {
      this.type = type;
    }
    
    public FastClass create() {
      setNamePrefix(type.getName());
      return (FastClass)super.create(type.getName());
    }
    
    protected ClassLoader getDefaultClassLoader() {
      return type.getClassLoader();
    }
    
    protected ProtectionDomain getProtectionDomain() {
      return ReflectUtils.getProtectionDomain(type);
    }
    
    public void generateClass(.ClassVisitor v) throws Exception {
      new FastClassEmitter(v, getClassName(), type);
    }
    
    protected Object firstInstance(Class type) {
      return ReflectUtils.newInstance(type, new Class[] { Class.class }, new Object[] { this.type });
    }
    

    protected Object nextInstance(Object instance)
    {
      return instance;
    }
  }
  
  public Object invoke(String name, Class[] parameterTypes, Object obj, Object[] args) throws InvocationTargetException {
    return invoke(getIndex(name, parameterTypes), obj, args);
  }
  
  public Object newInstance() throws InvocationTargetException {
    return newInstance(getIndex(Constants.EMPTY_CLASS_ARRAY), null);
  }
  
  public Object newInstance(Class[] parameterTypes, Object[] args) throws InvocationTargetException {
    return newInstance(getIndex(parameterTypes), args);
  }
  
  public FastMethod getMethod(Method method) {
    return new FastMethod(this, method);
  }
  
  public FastConstructor getConstructor(Constructor constructor) {
    return new FastConstructor(this, constructor);
  }
  
  public FastMethod getMethod(String name, Class[] parameterTypes) {
    try {
      return getMethod(type.getMethod(name, parameterTypes));
    } catch (NoSuchMethodException e) {
      throw new NoSuchMethodError(e.getMessage());
    }
  }
  
  public FastConstructor getConstructor(Class[] parameterTypes) {
    try {
      return getConstructor(type.getConstructor(parameterTypes));
    } catch (NoSuchMethodException e) {
      throw new NoSuchMethodError(e.getMessage());
    }
  }
  
  public String getName() {
    return type.getName();
  }
  
  public Class getJavaClass() {
    return type;
  }
  
  public String toString() {
    return type.toString();
  }
  
  public int hashCode() {
    return type.hashCode();
  }
  
  public boolean equals(Object o) {
    if ((o == null) || (!(o instanceof FastClass))) {
      return false;
    }
    return type.equals(type);
  }
  





  public abstract int getIndex(String paramString, Class[] paramArrayOfClass);
  





  public abstract int getIndex(Class[] paramArrayOfClass);
  





  public abstract Object invoke(int paramInt, Object paramObject, Object[] paramArrayOfObject)
    throws InvocationTargetException;
  





  public abstract Object newInstance(int paramInt, Object[] paramArrayOfObject)
    throws InvocationTargetException;
  




  public abstract int getIndex(Signature paramSignature);
  




  public abstract int getMaxIndex();
  




  protected static String getSignatureWithoutReturnType(String name, Class[] parameterTypes)
  {
    StringBuffer sb = new StringBuffer();
    sb.append(name);
    sb.append('(');
    for (int i = 0; i < parameterTypes.length; i++) {
      sb.append(.Type.getDescriptor(parameterTypes[i]));
    }
    sb.append(')');
    return sb.toString();
  }
}
