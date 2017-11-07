package net.sf.cglib.transform;

import net.sf.cglib.asm..AnnotationVisitor;
import net.sf.cglib.asm..Attribute;
import net.sf.cglib.asm..FieldVisitor;
import net.sf.cglib.asm..TypePath;













public class FieldVisitorTee
  extends .FieldVisitor
{
  private .FieldVisitor fv1;
  private .FieldVisitor fv2;
  
  public FieldVisitorTee(.FieldVisitor fv1, .FieldVisitor fv2)
  {
    super(327680);
    this.fv1 = fv1;
    this.fv2 = fv2;
  }
  
  public .AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    return AnnotationVisitorTee.getInstance(fv1.visitAnnotation(desc, visible), fv2
      .visitAnnotation(desc, visible));
  }
  
  public void visitAttribute(.Attribute attr) {
    fv1.visitAttribute(attr);
    fv2.visitAttribute(attr);
  }
  
  public void visitEnd() {
    fv1.visitEnd();
    fv2.visitEnd();
  }
  
  public .AnnotationVisitor visitTypeAnnotation(int typeRef, .TypePath typePath, String desc, boolean visible) {
    return AnnotationVisitorTee.getInstance(fv1.visitTypeAnnotation(typeRef, typePath, desc, visible), fv2
      .visitTypeAnnotation(typeRef, typePath, desc, visible));
  }
}
