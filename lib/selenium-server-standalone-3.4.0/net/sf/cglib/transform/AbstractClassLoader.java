package net.sf.cglib.transform;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import net.sf.cglib.asm..Attribute;
import net.sf.cglib.asm..ClassReader;
import net.sf.cglib.core.ClassGenerator;
import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.DebuggingClassWriter;

















public abstract class AbstractClassLoader
  extends ClassLoader
{
  private ClassFilter filter;
  private ClassLoader classPath;
  private static ProtectionDomain DOMAIN = (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction()
  {
    public Object run()
    {
      return AbstractClassLoader.class.getProtectionDomain();
    }
  });
  





  protected AbstractClassLoader(ClassLoader parent, ClassLoader classPath, ClassFilter filter)
  {
    super(parent);
    this.filter = filter;
    this.classPath = classPath;
  }
  
  public Class loadClass(String name) throws ClassNotFoundException
  {
    Class loaded = findLoadedClass(name);
    
    if ((loaded != null) && 
      (loaded.getClassLoader() == this)) {
      return loaded;
    }
    

    if (!filter.accept(name)) {
      return super.loadClass(name);
    }
    
    try
    {
      InputStream is = classPath.getResourceAsStream(name
        .replace('.', '/') + ".class");
      

      if (is == null)
      {
        throw new ClassNotFoundException(name);
      }
      
      try
      {
        r = new .ClassReader(is);
      }
      finally {
        .ClassReader r;
        is.close();
      }
    } catch (IOException e) {
      .ClassReader r;
      throw new ClassNotFoundException(name + ":" + e.getMessage());
    }
    try {
      .ClassReader r;
      DebuggingClassWriter w = new DebuggingClassWriter(2);
      
      getGenerator(r).generateClass(w);
      byte[] b = w.toByteArray();
      Class c = super.defineClass(name, b, 0, b.length, DOMAIN);
      postProcess(c);
      return c;
    } catch (RuntimeException e) {
      throw e;
    } catch (Error e) {
      throw e;
    } catch (Exception e) {
      throw new CodeGenerationException(e);
    }
  }
  
  protected ClassGenerator getGenerator(.ClassReader r) {
    return new ClassReaderGenerator(r, attributes(), getFlags());
  }
  
  protected int getFlags() {
    return 0;
  }
  
  protected .Attribute[] attributes() {
    return null;
  }
  
  protected void postProcess(Class c) {}
}
