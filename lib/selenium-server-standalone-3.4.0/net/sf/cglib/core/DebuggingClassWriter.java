package net.sf.cglib.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import net.sf.cglib.asm..ClassReader;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..ClassWriter;

















public class DebuggingClassWriter
  extends .ClassVisitor
{
  public static final String DEBUG_LOCATION_PROPERTY = "cglib.debugLocation";
  private static String debugLocation = System.getProperty("cglib.debugLocation");
  static { if (debugLocation != null) {
      System.err.println("CGLIB debugging enabled, writing to '" + debugLocation + "'");
      try {
        Class clazz = Class.forName("net.sf.cglib.asm.util.$TraceClassVisitor");
        traceCtor = clazz.getConstructor(new Class[] { .ClassVisitor.class, PrintWriter.class });
      }
      catch (Throwable localThrowable) {}
    }
  }
  
  public DebuggingClassWriter(int flags) {
    super(327680, new .ClassWriter(flags));
  }
  

  private static Constructor traceCtor;
  private String className;
  private String superName;
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
  {
    className = name.replace('/', '.');
    this.superName = superName.replace('/', '.');
    super.visit(version, access, name, signature, superName, interfaces);
  }
  
  public String getClassName() {
    return className;
  }
  
  public String getSuperName() {
    return superName;
  }
  
  public byte[] toByteArray()
  {
    (byte[])AccessController.doPrivileged(new PrivilegedAction()
    {

      public Object run()
      {
        byte[] b = ((.ClassWriter)DebuggingClassWriter.access$001(DebuggingClassWriter.this)).toByteArray();
        if (DebuggingClassWriter.debugLocation != null) {
          String dirs = className.replace('.', File.separatorChar);
          try {
            new File(DebuggingClassWriter.debugLocation + File.separatorChar + dirs).getParentFile().mkdirs();
            
            File file = new File(new File(DebuggingClassWriter.debugLocation), dirs + ".class");
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            try {
              out.write(b);
            } finally {
              out.close();
            }
            
            if (DebuggingClassWriter.traceCtor != null) {
              file = new File(new File(DebuggingClassWriter.debugLocation), dirs + ".asm");
              out = new BufferedOutputStream(new FileOutputStream(file));
              try {
                .ClassReader cr = new .ClassReader(b);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
                .ClassVisitor tcv = (.ClassVisitor)DebuggingClassWriter.traceCtor.newInstance(new Object[] { null, pw });
                cr.accept(tcv, 0);
                pw.flush();
              } finally {
                out.close();
              }
            }
          } catch (Exception e) {
            throw new CodeGenerationException(e);
          }
        }
        return b;
      }
    });
  }
}
