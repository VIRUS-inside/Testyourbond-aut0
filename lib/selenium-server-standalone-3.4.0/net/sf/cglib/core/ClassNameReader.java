package net.sf.cglib.core;

import java.util.ArrayList;
import java.util.List;
import net.sf.cglib.asm..ClassReader;
import net.sf.cglib.asm..ClassVisitor;




















public class ClassNameReader
{
  private static final EarlyExitException EARLY_EXIT = new EarlyExitException(null);
  
  private ClassNameReader() {}
  
  public static String getClassName(.ClassReader r) {
    return getClassInfo(r)[0];
  }
  
  public static String[] getClassInfo(.ClassReader r)
  {
    final List array = new ArrayList();
    try {
      r.accept(new .ClassVisitor(327680, null)
      {


        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
        {

          array.add(name.replace('/', '.'));
          if (superName != null) {
            array.add(superName.replace('/', '.'));
          }
          for (int i = 0; i < interfaces.length; i++) {
            array.add(interfaces[i].replace('/', '.'));
          }
          
          throw ClassNameReader.EARLY_EXIT; } }, 6);
    }
    catch (EarlyExitException localEarlyExitException) {}
    

    return (String[])array.toArray(new String[0]);
  }
  
  private static class EarlyExitException
    extends RuntimeException
  {
    private EarlyExitException() {}
  }
}
