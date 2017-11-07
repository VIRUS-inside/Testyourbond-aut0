package net.sf.cglib.transform;

import net.sf.cglib.asm..AnnotationVisitor;
import net.sf.cglib.asm..Attribute;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..FieldVisitor;
import net.sf.cglib.asm..MethodVisitor;









public abstract class AbstractClassFilterTransformer
  extends AbstractClassTransformer
{
  private ClassTransformer pass;
  private .ClassVisitor target;
  
  public void setTarget(.ClassVisitor target)
  {
    super.setTarget(target);
    pass.setTarget(target);
  }
  
  protected AbstractClassFilterTransformer(ClassTransformer pass) {
    this.pass = pass;
  }
  


  protected abstract boolean accept(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString);
  


  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
  {
    target = (accept(version, access, name, signature, superName, interfaces) ? pass : cv);
    target.visit(version, access, name, signature, superName, interfaces);
  }
  
  public void visitSource(String source, String debug) {
    target.visitSource(source, debug);
  }
  
  public void visitOuterClass(String owner, String name, String desc) {
    target.visitOuterClass(owner, name, desc);
  }
  
  public .AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    return target.visitAnnotation(desc, visible);
  }
  
  public void visitAttribute(.Attribute attr) {
    target.visitAttribute(attr);
  }
  
  public void visitInnerClass(String name, String outerName, String innerName, int access) {
    target.visitInnerClass(name, outerName, innerName, access);
  }
  



  public .FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
  {
    return target.visitField(access, name, desc, signature, value);
  }
  



  public .MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    return target.visitMethod(access, name, desc, signature, exceptions);
  }
  
  public void visitEnd() {
    target.visitEnd();
    target = null;
  }
}
