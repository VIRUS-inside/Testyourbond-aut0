package org.apache.bcel.verifier.statics;

import java.util.HashMap;
import java.util.HashSet;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.DescendingVisitor;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Node;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.classfile.Visitor;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.PassVerifier;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.ClassConstraintException;
import org.apache.bcel.verifier.exc.LocalVariableInfoInconsistentException;
import org.apache.bcel.verifier.exc.VerifierConstraintViolatedException;

































public final class Pass2Verifier
  extends PassVerifier
  implements Constants
{
  private LocalVariablesInfo[] localVariablesInfos;
  private Verifier myOwner;
  
  public Pass2Verifier(Verifier owner)
  {
    myOwner = owner;
  }
  








  public LocalVariablesInfo getLocalVariablesInfo(int method_nr)
  {
    if (verify() != VerificationResult.VR_OK) return null;
    if ((method_nr < 0) || (method_nr >= localVariablesInfos.length)) {
      throw new AssertionViolatedException("Method number out of range.");
    }
    return localVariablesInfos[method_nr];
  }
  





















  public VerificationResult do_verify()
  {
    VerificationResult vr1 = myOwner.doPass1();
    if (vr1.equals(VerificationResult.VR_OK))
    {


      localVariablesInfos = new LocalVariablesInfo[Repository.lookupClass(myOwner.getClassName()).getMethods().length];
      
      VerificationResult vr = VerificationResult.VR_OK;
      try {
        constant_pool_entries_satisfy_static_constraints();
        field_and_method_refs_are_valid();
        every_class_has_an_accessible_superclass();
        final_methods_are_not_overridden();
      }
      catch (ClassConstraintException cce) {
        vr = new VerificationResult(2, cce.getMessage());
      }
      return vr;
    }
    
    return VerificationResult.VR_NOTYET;
  }
  












  private void every_class_has_an_accessible_superclass()
  {
    HashSet hs = new HashSet();
    JavaClass jc = Repository.lookupClass(myOwner.getClassName());
    int supidx = -1;
    
    while (supidx != 0) {
      supidx = jc.getSuperclassNameIndex();
      
      if (supidx == 0) {
        if (jc != Repository.lookupClass(Type.OBJECT.getClassName())) {
          throw new ClassConstraintException("Superclass of '" + jc.getClassName() + "' missing but not " + Type.OBJECT.getClassName() + " itself!");
        }
      }
      else {
        String supername = jc.getSuperclassName();
        if (!hs.add(supername)) {
          throw new ClassConstraintException("Circular superclass hierarchy detected.");
        }
        Verifier v = VerifierFactory.getVerifier(supername);
        VerificationResult vr = v.doPass1();
        
        if (vr != VerificationResult.VR_OK) {
          throw new ClassConstraintException("Could not load in ancestor class '" + supername + "'.");
        }
        jc = Repository.lookupClass(supername);
        
        if (jc.isFinal()) {
          throw new ClassConstraintException("Ancestor class '" + supername + "' has the FINAL access modifier and must therefore not be subclassed.");
        }
      }
    }
  }
  










  private void final_methods_are_not_overridden()
  {
    HashMap hashmap = new HashMap();
    JavaClass jc = Repository.lookupClass(myOwner.getClassName());
    
    int supidx = -1;
    while (supidx != 0) {
      supidx = jc.getSuperclassNameIndex();
      
      ConstantPoolGen cpg = new ConstantPoolGen(jc.getConstantPool());
      Method[] methods = jc.getMethods();
      for (int i = 0; i < methods.length; i++) {
        String name_and_sig = methods[i].getName() + methods[i].getSignature();
        
        if (hashmap.containsKey(name_and_sig)) {
          if (methods[i].isFinal()) {
            throw new ClassConstraintException("Method '" + name_and_sig + "' in class '" + hashmap.get(name_and_sig) + "' overrides the final (not-overridable) definition in class '" + jc.getClassName() + "'.");
          }
          
          if (!methods[i].isStatic()) {
            hashmap.put(name_and_sig, jc.getClassName());
          }
          

        }
        else if (!methods[i].isStatic()) {
          hashmap.put(name_and_sig, jc.getClassName());
        }
      }
      

      jc = Repository.lookupClass(jc.getSuperclassName());
    }
  }
  









  private void constant_pool_entries_satisfy_static_constraints()
  {
    JavaClass jc = Repository.lookupClass(myOwner.getClassName());
    new CPESSC_Visitor(jc, null); }
  
  private class CPESSC_Visitor extends EmptyVisitor implements Visitor { private Class CONST_Class;
    private Class CONST_Fieldref;
    private Class CONST_Methodref;
    private Class CONST_InterfaceMethodref;
    private Class CONST_String;
    private Class CONST_Integer;
    private Class CONST_Float;
    
    CPESSC_Visitor(JavaClass x1, Pass2Verifier.1 x2) { this(x1); }
    

    private Class CONST_Long;
    
    private Class CONST_Double;
    
    private Class CONST_NameAndType;
    
    private Class CONST_Utf8;
    
    private final JavaClass jc;
    
    private final ConstantPool cp;
    
    private final int cplen;
    
    private DescendingVisitor carrier;
    private HashSet field_names = new HashSet();
    private HashSet field_names_and_desc = new HashSet();
    private HashSet method_names_and_desc = new HashSet();
    
    private CPESSC_Visitor(JavaClass _jc) {
      jc = _jc;
      cp = _jc.getConstantPool();
      cplen = cp.getLength();
      
      CONST_Class = ConstantClass.class;
      CONST_Fieldref = ConstantFieldref.class;
      CONST_Methodref = ConstantMethodref.class;
      CONST_InterfaceMethodref = ConstantInterfaceMethodref.class;
      CONST_String = ConstantString.class;
      CONST_Integer = ConstantInteger.class;
      CONST_Float = ConstantFloat.class;
      CONST_Long = ConstantLong.class;
      CONST_Double = ConstantDouble.class;
      CONST_NameAndType = ConstantNameAndType.class;
      CONST_Utf8 = ConstantUtf8.class;
      
      carrier = new DescendingVisitor(_jc, this);
      carrier.visit();
    }
    
    private void checkIndex(Node referrer, int index, Class shouldbe) {
      if ((index < 0) || (index >= cplen)) {
        throw new ClassConstraintException("Invalid index '" + index + "' used by '" + Pass2Verifier.tostring(referrer) + "'.");
      }
      Constant c = cp.getConstant(index);
      if (!shouldbe.isInstance(c)) {
        String isnot = shouldbe.toString().substring(shouldbe.toString().lastIndexOf(".") + 1);
        throw new ClassCastException("Illegal constant '" + Pass2Verifier.tostring(c) + "' at index '" + index + "'. '" + Pass2Verifier.tostring(referrer) + "' expects a '" + shouldbe + "'.");
      }
    }
    

    public void visitJavaClass(JavaClass obj)
    {
      Attribute[] atts = obj.getAttributes();
      boolean foundSourceFile = false;
      boolean foundInnerClasses = false;
      


      boolean hasInnerClass = new Pass2Verifier.InnerClassDetector(Pass2Verifier.this, jc).innerClassReferenced();
      
      for (int i = 0; i < atts.length; i++) {
        if ((!(atts[i] instanceof SourceFile)) && (!(atts[i] instanceof Deprecated)) && (!(atts[i] instanceof InnerClasses)) && (!(atts[i] instanceof Synthetic)))
        {


          addMessage("Attribute '" + Pass2Verifier.tostring(atts[i]) + "' as an attribute of the ClassFile structure '" + Pass2Verifier.tostring(obj) + "' is unknown and will therefore be ignored.");
        }
        
        if ((atts[i] instanceof SourceFile)) {
          if (!foundSourceFile) foundSourceFile = true; else {
            throw new ClassConstraintException("A ClassFile structure (like '" + Pass2Verifier.tostring(obj) + "') may have no more than one SourceFile attribute.");
          }
        }
        if ((atts[i] instanceof InnerClasses)) {
          if (!foundInnerClasses) { foundInnerClasses = true;
          }
          else if (hasInnerClass) {
            throw new ClassConstraintException("A Classfile structure (like '" + Pass2Verifier.tostring(obj) + "') must have exactly one InnerClasses attribute if at least one Inner Class is referenced (which is the case). More than one InnerClasses attribute was found.");
          }
          
          if (!hasInnerClass) {
            addMessage("No referenced Inner Class found, but InnerClasses attribute '" + Pass2Verifier.tostring(atts[i]) + "' found. Strongly suggest removal of that attribute.");
          }
        }
      }
      
      if ((hasInnerClass) && (!foundInnerClasses))
      {



        addMessage("A Classfile structure (like '" + Pass2Verifier.tostring(obj) + "') must have exactly one InnerClasses attribute if at least one Inner Class is referenced (which is the case). No InnerClasses attribute was found.");
      }
    }
    

    public void visitConstantClass(ConstantClass obj)
    {
      if (obj.getTag() != 7) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
    }
    
    public void visitConstantFieldref(ConstantFieldref obj) {
      if (obj.getTag() != 9) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
      checkIndex(obj, obj.getClassIndex(), CONST_Class);
      checkIndex(obj, obj.getNameAndTypeIndex(), CONST_NameAndType);
    }
    
    public void visitConstantMethodref(ConstantMethodref obj) { if (obj.getTag() != 10) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
      checkIndex(obj, obj.getClassIndex(), CONST_Class);
      checkIndex(obj, obj.getNameAndTypeIndex(), CONST_NameAndType);
    }
    
    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) { if (obj.getTag() != 11) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
      checkIndex(obj, obj.getClassIndex(), CONST_Class);
      checkIndex(obj, obj.getNameAndTypeIndex(), CONST_NameAndType);
    }
    
    public void visitConstantString(ConstantString obj) { if (obj.getTag() != 8) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
      checkIndex(obj, obj.getStringIndex(), CONST_Utf8);
    }
    
    public void visitConstantInteger(ConstantInteger obj) { if (obj.getTag() != 3) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
    }
    
    public void visitConstantFloat(ConstantFloat obj) {
      if (obj.getTag() != 4) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
    }
    
    public void visitConstantLong(ConstantLong obj) {
      if (obj.getTag() != 5) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
    }
    
    public void visitConstantDouble(ConstantDouble obj) {
      if (obj.getTag() != 6) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
    }
    
    public void visitConstantNameAndType(ConstantNameAndType obj) {
      if (obj.getTag() != 12) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      
      checkIndex(obj, obj.getSignatureIndex(), CONST_Utf8);
    }
    
    public void visitConstantUtf8(ConstantUtf8 obj) { if (obj.getTag() != 1) {
        throw new ClassConstraintException("Wrong constant tag in '" + Pass2Verifier.tostring(obj) + "'.");
      }
    }
    



    public void visitField(Field obj)
    {
      if (jc.isClass()) {
        int maxone = 0;
        if (obj.isPrivate()) maxone++;
        if (obj.isProtected()) maxone++;
        if (obj.isPublic()) maxone++;
        if (maxone > 1) {
          throw new ClassConstraintException("Field '" + Pass2Verifier.tostring(obj) + "' must only have at most one of its ACC_PRIVATE, ACC_PROTECTED, ACC_PUBLIC modifiers set.");
        }
        
        if ((obj.isFinal()) && (obj.isVolatile())) {
          throw new ClassConstraintException("Field '" + Pass2Verifier.tostring(obj) + "' must only have at most one of its ACC_FINAL, ACC_VOLATILE modifiers set.");
        }
      }
      else {
        if (!obj.isPublic()) {
          throw new ClassConstraintException("Interface field '" + Pass2Verifier.tostring(obj) + "' must have the ACC_PUBLIC modifier set but hasn't!");
        }
        if (!obj.isStatic()) {
          throw new ClassConstraintException("Interface field '" + Pass2Verifier.tostring(obj) + "' must have the ACC_STATIC modifier set but hasn't!");
        }
        if (!obj.isFinal()) {
          throw new ClassConstraintException("Interface field '" + Pass2Verifier.tostring(obj) + "' must have the ACC_FINAL modifier set but hasn't!");
        }
      }
      
      if ((obj.getAccessFlags() & 0xFF20) > 0) {
        addMessage("Field '" + Pass2Verifier.tostring(obj) + "' has access flag(s) other than ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_VOLATILE, ACC_TRANSIENT set (ignored).");
      }
      
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      
      String name = obj.getName();
      if (!Pass2Verifier.validFieldName(name)) {
        throw new ClassConstraintException("Field '" + Pass2Verifier.tostring(obj) + "' has illegal name '" + obj.getName() + "'.");
      }
      

      checkIndex(obj, obj.getSignatureIndex(), CONST_Utf8);
      
      String sig = ((ConstantUtf8)cp.getConstant(obj.getSignatureIndex())).getBytes();
      try
      {
        t = Type.getType(sig);
      } catch (ClassFormatError cfe) {
        Type t;
        throw new ClassConstraintException("Illegal descriptor (==signature) '" + sig + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
      }
      
      String nameanddesc = name + sig;
      if (field_names_and_desc.contains(nameanddesc)) {
        throw new ClassConstraintException("No two fields (like '" + Pass2Verifier.tostring(obj) + "') are allowed have same names and descriptors!");
      }
      if (field_names.contains(name)) {
        addMessage("More than one field of name '" + name + "' detected (but with different type descriptors). This is very unusual.");
      }
      field_names_and_desc.add(nameanddesc);
      field_names.add(name);
      
      Attribute[] atts = obj.getAttributes();
      for (int i = 0; i < atts.length; i++) {
        if ((!(atts[i] instanceof ConstantValue)) && (!(atts[i] instanceof Synthetic)) && (!(atts[i] instanceof Deprecated)))
        {

          addMessage("Attribute '" + Pass2Verifier.tostring(atts[i]) + "' as an attribute of Field '" + Pass2Verifier.tostring(obj) + "' is unknown and will therefore be ignored.");
        }
        if (!(atts[i] instanceof ConstantValue)) {
          addMessage("Attribute '" + Pass2Verifier.tostring(atts[i]) + "' as an attribute of Field '" + Pass2Verifier.tostring(obj) + "' is not a ConstantValue and is therefore only of use for debuggers and such.");
        }
      }
    }
    


    public void visitMethod(Method obj)
    {
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      
      String name = obj.getName();
      if (!Pass2Verifier.validMethodName(name, true)) {
        throw new ClassConstraintException("Method '" + Pass2Verifier.tostring(obj) + "' has illegal name '" + name + "'.");
      }
      

      checkIndex(obj, obj.getSignatureIndex(), CONST_Utf8);
      
      String sig = ((ConstantUtf8)cp.getConstant(obj.getSignatureIndex())).getBytes();
      Type t;
      Type[] ts;
      try
      {
        t = Type.getReturnType(sig);
        ts = Type.getArgumentTypes(sig);
      }
      catch (ClassFormatError cfe)
      {
        throw new ClassConstraintException("Illegal descriptor (==signature) '" + sig + "' used by Method '" + Pass2Verifier.tostring(obj) + "'.");
      }
      

      Type act = t;
      if ((act instanceof ArrayType)) act = ((ArrayType)act).getBasicType();
      if ((act instanceof ObjectType)) {
        Verifier v = VerifierFactory.getVerifier(((ObjectType)act).getClassName());
        VerificationResult vr = v.doPass1();
        if (vr != VerificationResult.VR_OK) {
          throw new ClassConstraintException("Method '" + Pass2Verifier.tostring(obj) + "' has a return type that does not pass verification pass 1: '" + vr + "'.");
        }
      }
      
      for (int i = 0; i < ts.length; i++) {
        act = ts[i];
        if ((act instanceof ArrayType)) act = ((ArrayType)act).getBasicType();
        if ((act instanceof ObjectType)) {
          Verifier v = VerifierFactory.getVerifier(((ObjectType)act).getClassName());
          VerificationResult vr = v.doPass1();
          if (vr != VerificationResult.VR_OK) {
            throw new ClassConstraintException("Method '" + Pass2Verifier.tostring(obj) + "' has an argument type that does not pass verification pass 1: '" + vr + "'.");
          }
        }
      }
      

      if ((name.equals("<clinit>")) && (ts.length != 0)) {
        throw new ClassConstraintException("Method '" + Pass2Verifier.tostring(obj) + "' has illegal name '" + name + "'. It's name resembles the class or interface initialization method which it isn't because of its arguments (==descriptor).");
      }
      
      if (jc.isClass()) {
        int maxone = 0;
        if (obj.isPrivate()) maxone++;
        if (obj.isProtected()) maxone++;
        if (obj.isPublic()) maxone++;
        if (maxone > 1) {
          throw new ClassConstraintException("Method '" + Pass2Verifier.tostring(obj) + "' must only have at most one of its ACC_PRIVATE, ACC_PROTECTED, ACC_PUBLIC modifiers set.");
        }
        
        if (obj.isAbstract()) {
          if (obj.isFinal()) throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_FINAL modifier set.");
          if (obj.isNative()) throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_NATIVE modifier set.");
          if (obj.isPrivate()) throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_PRIVATE modifier set.");
          if (obj.isStatic()) throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_STATIC modifier set.");
          if (obj.isStrictfp()) throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_STRICT modifier set.");
          if (obj.isSynchronized()) { throw new ClassConstraintException("Abstract method '" + Pass2Verifier.tostring(obj) + "' must not have the ACC_SYNCHRONIZED modifier set.");
          }
        }
      }
      else if (!name.equals("<clinit>")) {
        if (!obj.isPublic()) {
          throw new ClassConstraintException("Interface method '" + Pass2Verifier.tostring(obj) + "' must have the ACC_PUBLIC modifier set but hasn't!");
        }
        if (!obj.isAbstract()) {
          throw new ClassConstraintException("Interface method '" + Pass2Verifier.tostring(obj) + "' must have the ACC_STATIC modifier set but hasn't!");
        }
        if ((obj.isPrivate()) || (obj.isProtected()) || (obj.isStatic()) || (obj.isFinal()) || (obj.isSynchronized()) || (obj.isNative()) || (obj.isStrictfp()))
        {





          throw new ClassConstraintException("Interface method '" + Pass2Verifier.tostring(obj) + "' must not have any of the ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED, ACC_NATIVE, ACC_ABSTRACT, ACC_STRICT modifiers set.");
        }
      }
      


      if (name.equals("<init>"))
      {

        if ((obj.isStatic()) || (obj.isFinal()) || (obj.isSynchronized()) || (obj.isNative()) || (obj.isAbstract()))
        {



          throw new ClassConstraintException("Instance initialization method '" + Pass2Verifier.tostring(obj) + "' must not have any of the ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED, ACC_NATIVE, ACC_ABSTRACT modifiers set.");
        }
      }
      

      if (name.equals("<clinit>")) {
        if ((obj.getAccessFlags() & 0xF7FF) > 0) {
          addMessage("Class or interface initialization method '" + Pass2Verifier.tostring(obj) + "' has superfluous access modifier(s) set: everything but ACC_STRICT is ignored.");
        }
        if (obj.isAbstract()) {
          throw new ClassConstraintException("Class or interface initialization method '" + Pass2Verifier.tostring(obj) + "' must not be abstract. This contradicts the Java Language Specification, Second Edition (which omits this constraint) but is common practice of existing verifiers.");
        }
      }
      
      if ((obj.getAccessFlags() & 0xF2C0) > 0) {
        addMessage("Method '" + Pass2Verifier.tostring(obj) + "' has access flag(s) other than ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED, ACC_NATIVE, ACC_ABSTRACT, ACC_STRICT set (ignored).");
      }
      
      String nameanddesc = name + sig;
      if (method_names_and_desc.contains(nameanddesc)) {
        throw new ClassConstraintException("No two methods (like '" + Pass2Verifier.tostring(obj) + "') are allowed have same names and desciptors!");
      }
      method_names_and_desc.add(nameanddesc);
      
      Attribute[] atts = obj.getAttributes();
      int num_code_atts = 0;
      for (int i = 0; i < atts.length; i++) {
        if ((!(atts[i] instanceof Code)) && (!(atts[i] instanceof ExceptionTable)) && (!(atts[i] instanceof Synthetic)) && (!(atts[i] instanceof Deprecated)))
        {


          addMessage("Attribute '" + Pass2Verifier.tostring(atts[i]) + "' as an attribute of Method '" + Pass2Verifier.tostring(obj) + "' is unknown and will therefore be ignored.");
        }
        if ((!(atts[i] instanceof Code)) && (!(atts[i] instanceof ExceptionTable)))
        {
          addMessage("Attribute '" + Pass2Verifier.tostring(atts[i]) + "' as an attribute of Method '" + Pass2Verifier.tostring(obj) + "' is neither Code nor Exceptions and is therefore only of use for debuggers and such.");
        }
        if (((atts[i] instanceof Code)) && ((obj.isNative()) || (obj.isAbstract()))) {
          throw new ClassConstraintException("Native or abstract methods like '" + Pass2Verifier.tostring(obj) + "' must not have a Code attribute like '" + Pass2Verifier.tostring(atts[i]) + "'.");
        }
        if ((atts[i] instanceof Code)) num_code_atts++;
      }
      if ((!obj.isNative()) && (!obj.isAbstract()) && (num_code_atts != 1)) {
        throw new ClassConstraintException("Non-native, non-abstract methods like '" + Pass2Verifier.tostring(obj) + "' must have exactly one Code attribute (found: " + num_code_atts + ").");
      }
    }
    




    public void visitSourceFile(SourceFile obj)
    {
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      
      String name = ((ConstantUtf8)cp.getConstant(obj.getNameIndex())).getBytes();
      if (!name.equals("SourceFile")) {
        throw new ClassConstraintException("The SourceFile attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'SourceFile' but '" + name + "'.");
      }
      
      checkIndex(obj, obj.getSourceFileIndex(), CONST_Utf8);
      
      String sourcefilename = ((ConstantUtf8)cp.getConstant(obj.getSourceFileIndex())).getBytes();
      String sourcefilenamelc = sourcefilename.toLowerCase();
      
      if ((sourcefilename.indexOf('/') != -1) || (sourcefilename.indexOf('\\') != -1) || (sourcefilename.indexOf(':') != -1) || (sourcefilenamelc.lastIndexOf(".java") == -1))
      {


        addMessage("SourceFile attribute '" + Pass2Verifier.tostring(obj) + "' has a funny name: remember not to confuse certain parsers working on javap's output. Also, this name ('" + sourcefilename + "') is considered an unqualified (simple) file name only."); }
    }
    
    public void visitDeprecated(Deprecated obj) {
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      
      String name = ((ConstantUtf8)cp.getConstant(obj.getNameIndex())).getBytes();
      if (!name.equals("Deprecated"))
        throw new ClassConstraintException("The Deprecated attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'Deprecated' but '" + name + "'.");
    }
    
    public void visitSynthetic(Synthetic obj) {
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      String name = ((ConstantUtf8)cp.getConstant(obj.getNameIndex())).getBytes();
      if (!name.equals("Synthetic")) {
        throw new ClassConstraintException("The Synthetic attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'Synthetic' but '" + name + "'.");
      }
    }
    

    public void visitInnerClasses(InnerClasses obj)
    {
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      
      String name = ((ConstantUtf8)cp.getConstant(obj.getNameIndex())).getBytes();
      if (!name.equals("InnerClasses")) {
        throw new ClassConstraintException("The InnerClasses attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'InnerClasses' but '" + name + "'.");
      }
      
      InnerClass[] ics = obj.getInnerClasses();
      
      for (int i = 0; i < ics.length; i++) {
        checkIndex(obj, ics[i].getInnerClassIndex(), CONST_Class);
        int outer_idx = ics[i].getOuterClassIndex();
        if (outer_idx != 0) {
          checkIndex(obj, outer_idx, CONST_Class);
        }
        int innername_idx = ics[i].getInnerNameIndex();
        if (innername_idx != 0) {
          checkIndex(obj, innername_idx, CONST_Utf8);
        }
        int acc = ics[i].getInnerAccessFlags();
        acc &= 0xF9E0;
        if (acc != 0) {
          addMessage("Unknown access flag for inner class '" + Pass2Verifier.tostring(ics[i]) + "' set (InnerClasses attribute '" + Pass2Verifier.tostring(obj) + "').");
        }
      }
    }
    





    public void visitConstantValue(ConstantValue obj)
    {
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      
      String name = ((ConstantUtf8)cp.getConstant(obj.getNameIndex())).getBytes();
      if (!name.equals("ConstantValue")) {
        throw new ClassConstraintException("The ConstantValue attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'ConstantValue' but '" + name + "'.");
      }
      
      Object pred = carrier.predecessor();
      if ((pred instanceof Field)) {
        Field f = (Field)pred;
        
        Type field_type = Type.getType(((ConstantUtf8)cp.getConstant(f.getSignatureIndex())).getBytes());
        
        int index = obj.getConstantValueIndex();
        if ((index < 0) || (index >= cplen)) {
          throw new ClassConstraintException("Invalid index '" + index + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
        }
        Constant c = cp.getConstant(index);
        
        if ((CONST_Long.isInstance(c)) && (field_type.equals(Type.LONG))) {
          return;
        }
        if ((CONST_Float.isInstance(c)) && (field_type.equals(Type.FLOAT))) {
          return;
        }
        if ((CONST_Double.isInstance(c)) && (field_type.equals(Type.DOUBLE))) {
          return;
        }
        if ((CONST_Integer.isInstance(c)) && ((field_type.equals(Type.INT)) || (field_type.equals(Type.SHORT)) || (field_type.equals(Type.CHAR)) || (field_type.equals(Type.BYTE)) || (field_type.equals(Type.BOOLEAN)))) {
          return;
        }
        if ((CONST_String.isInstance(c)) && (field_type.equals(Type.STRING))) {
          return;
        }
        
        throw new ClassConstraintException("Illegal type of ConstantValue '" + obj + "' embedding Constant '" + c + "'. It is referenced by field '" + Pass2Verifier.tostring(f) + "' expecting a different type: '" + field_type + "'.");
      }
    }
    






    public void visitCode(Code obj)
    {
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      
      String name = ((ConstantUtf8)cp.getConstant(obj.getNameIndex())).getBytes();
      if (!name.equals("Code")) {
        throw new ClassConstraintException("The Code attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'Code' but '" + name + "'.");
      }
      
      Method m = null;
      if (!(carrier.predecessor() instanceof Method)) {
        addMessage("Code attribute '" + Pass2Verifier.tostring(obj) + "' is not declared in a method_info structure but in '" + carrier.predecessor() + "'. Ignored.");
        return;
      }
      
      m = (Method)carrier.predecessor();
      


      if (obj.getCode().length == 0) {
        throw new ClassConstraintException("Code array of Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') must not be empty.");
      }
      

      CodeException[] exc_table = obj.getExceptionTable();
      for (int i = 0; i < exc_table.length; i++) {
        int exc_index = exc_table[i].getCatchType();
        if (exc_index != 0) {
          checkIndex(obj, exc_index, CONST_Class);
          ConstantClass cc = (ConstantClass)cp.getConstant(exc_index);
          checkIndex(cc, cc.getNameIndex(), CONST_Utf8);
          String cname = ((ConstantUtf8)cp.getConstant(cc.getNameIndex())).getBytes().replace('/', '.');
          
          Verifier v = VerifierFactory.getVerifier(cname);
          VerificationResult vr = v.doPass1();
          
          if (vr != VerificationResult.VR_OK) {
            throw new ClassConstraintException("Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') has an exception_table entry '" + Pass2Verifier.tostring(exc_table[i]) + "' that references '" + cname + "' as an Exception but it does not pass verification pass 1: " + vr);
          }
          


          JavaClass e = Repository.lookupClass(cname);
          JavaClass t = Repository.lookupClass(Type.THROWABLE.getClassName());
          JavaClass o = Repository.lookupClass(Type.OBJECT.getClassName());
          while (e != o) {
            if (e == t)
              break;
            v = VerifierFactory.getVerifier(e.getSuperclassName());
            vr = v.doPass1();
            if (vr != VerificationResult.VR_OK) {
              throw new ClassConstraintException("Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') has an exception_table entry '" + Pass2Verifier.tostring(exc_table[i]) + "' that references '" + cname + "' as an Exception but '" + e.getSuperclassName() + "' in the ancestor hierachy does not pass verification pass 1: " + vr);
            }
            
            e = Repository.lookupClass(e.getSuperclassName());
          }
          
          if (e != t) { throw new ClassConstraintException("Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') has an exception_table entry '" + Pass2Verifier.tostring(exc_table[i]) + "' that references '" + cname + "' as an Exception but it is not a subclass of '" + t.getClassName() + "'.");
          }
        }
      }
      



      int method_number = -1;
      Method[] ms = Repository.lookupClass(myOwner.getClassName()).getMethods();
      for (int mn = 0; mn < ms.length; mn++) {
        if (m == ms[mn]) {
          method_number = mn;
          break;
        }
      }
      if (method_number < 0) {
        throw new AssertionViolatedException("Could not find a known BCEL Method object in the corresponding BCEL JavaClass object.");
      }
      localVariablesInfos[method_number] = new LocalVariablesInfo(obj.getMaxLocals());
      
      int num_of_lvt_attribs = 0;
      
      Attribute[] atts = obj.getAttributes();
      for (int a = 0; a < atts.length; a++) {
        if ((!(atts[a] instanceof LineNumberTable)) && (!(atts[a] instanceof LocalVariableTable)))
        {
          addMessage("Attribute '" + Pass2Verifier.tostring(atts[a]) + "' as an attribute of Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') is unknown and will therefore be ignored.");
        }
        else {
          addMessage("Attribute '" + Pass2Verifier.tostring(atts[a]) + "' as an attribute of Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + m + "') will effectively be ignored and is only useful for debuggers and such.");
        }
        




        if ((atts[a] instanceof LocalVariableTable))
        {
          LocalVariableTable lvt = (LocalVariableTable)atts[a];
          
          checkIndex(lvt, lvt.getNameIndex(), CONST_Utf8);
          
          String lvtname = ((ConstantUtf8)cp.getConstant(lvt.getNameIndex())).getBytes();
          if (!lvtname.equals("LocalVariableTable")) {
            throw new ClassConstraintException("The LocalVariableTable attribute '" + Pass2Verifier.tostring(lvt) + "' is not correctly named 'LocalVariableTable' but '" + lvtname + "'.");
          }
          
          Code code = obj;
          int max_locals = code.getMaxLocals();
          

          LocalVariable[] localvariables = lvt.getLocalVariableTable();
          
          for (int i = 0; i < localvariables.length; i++) {
            checkIndex(lvt, localvariables[i].getNameIndex(), CONST_Utf8);
            String localname = ((ConstantUtf8)cp.getConstant(localvariables[i].getNameIndex())).getBytes();
            if (!Pass2Verifier.validJavaIdentifier(localname)) {
              throw new ClassConstraintException("LocalVariableTable '" + Pass2Verifier.tostring(lvt) + "' references a local variable by the name '" + localname + "' which is not a legal Java simple name.");
            }
            
            checkIndex(lvt, localvariables[i].getSignatureIndex(), CONST_Utf8);
            String localsig = ((ConstantUtf8)cp.getConstant(localvariables[i].getSignatureIndex())).getBytes();
            Type t;
            try {
              t = Type.getType(localsig);
            }
            catch (ClassFormatError cfe) {
              throw new ClassConstraintException("Illegal descriptor (==signature) '" + localsig + "' used by LocalVariable '" + Pass2Verifier.tostring(localvariables[i]) + "' referenced by '" + Pass2Verifier.tostring(lvt) + "'.");
            }
            int localindex = localvariables[i].getIndex();
            if (((t == Type.LONG) || (t == Type.DOUBLE) ? localindex + 1 : localindex) >= code.getMaxLocals()) {
              throw new ClassConstraintException("LocalVariableTable attribute '" + Pass2Verifier.tostring(lvt) + "' references a LocalVariable '" + Pass2Verifier.tostring(localvariables[i]) + "' with an index that exceeds the surrounding Code attribute's max_locals value of '" + code.getMaxLocals() + "'.");
            }
            try
            {
              localVariablesInfos[method_number].add(localindex, localname, localvariables[i].getStartPC(), localvariables[i].getLength(), t);
            }
            catch (LocalVariableInfoInconsistentException lviie) {
              throw new ClassConstraintException("Conflicting information in LocalVariableTable '" + Pass2Verifier.tostring(lvt) + "' found in Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + Pass2Verifier.tostring(m) + "'). " + lviie.getMessage());
            }
          }
          
          num_of_lvt_attribs++;
          if (num_of_lvt_attribs > obj.getMaxLocals()) {
            throw new ClassConstraintException("Number of LocalVariableTable attributes of Code attribute '" + Pass2Verifier.tostring(obj) + "' (method '" + Pass2Verifier.tostring(m) + "') exceeds number of local variable slots '" + obj.getMaxLocals() + "' ('There may be no more than one LocalVariableTable attribute per local variable in the Code attribute.').");
          }
        }
      }
    }
    
    public void visitExceptionTable(ExceptionTable obj)
    {
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      
      String name = ((ConstantUtf8)cp.getConstant(obj.getNameIndex())).getBytes();
      if (!name.equals("Exceptions")) {
        throw new ClassConstraintException("The Exceptions attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'Exceptions' but '" + name + "'.");
      }
      
      int[] exc_indices = obj.getExceptionIndexTable();
      
      for (int i = 0; i < exc_indices.length; i++) {
        checkIndex(obj, exc_indices[i], CONST_Class);
        
        ConstantClass cc = (ConstantClass)cp.getConstant(exc_indices[i]);
        checkIndex(cc, cc.getNameIndex(), CONST_Utf8);
        String cname = ((ConstantUtf8)cp.getConstant(cc.getNameIndex())).getBytes().replace('/', '.');
        
        Verifier v = VerifierFactory.getVerifier(cname);
        VerificationResult vr = v.doPass1();
        
        if (vr != VerificationResult.VR_OK) {
          throw new ClassConstraintException("Exceptions attribute '" + Pass2Verifier.tostring(obj) + "' references '" + cname + "' as an Exception but it does not pass verification pass 1: " + vr);
        }
        


        JavaClass e = Repository.lookupClass(cname);
        JavaClass t = Repository.lookupClass(Type.THROWABLE.getClassName());
        JavaClass o = Repository.lookupClass(Type.OBJECT.getClassName());
        while (e != o) {
          if (e == t)
            break;
          v = VerifierFactory.getVerifier(e.getSuperclassName());
          vr = v.doPass1();
          if (vr != VerificationResult.VR_OK) {
            throw new ClassConstraintException("Exceptions attribute '" + Pass2Verifier.tostring(obj) + "' references '" + cname + "' as an Exception but '" + e.getSuperclassName() + "' in the ancestor hierachy does not pass verification pass 1: " + vr);
          }
          
          e = Repository.lookupClass(e.getSuperclassName());
        }
        
        if (e != t) { throw new ClassConstraintException("Exceptions attribute '" + Pass2Verifier.tostring(obj) + "' references '" + cname + "' as an Exception but it is not a subclass of '" + t.getClassName() + "'.");
        }
      }
    }
    



    public void visitLineNumberTable(LineNumberTable obj)
    {
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      
      String name = ((ConstantUtf8)cp.getConstant(obj.getNameIndex())).getBytes();
      if (!name.equals("LineNumberTable")) {
        throw new ClassConstraintException("The LineNumberTable attribute '" + Pass2Verifier.tostring(obj) + "' is not correctly named 'LineNumberTable' but '" + name + "'.");
      }
    }
    











    public void visitUnknown(Unknown obj)
    {
      checkIndex(obj, obj.getNameIndex(), CONST_Utf8);
      

      addMessage("Unknown attribute '" + Pass2Verifier.tostring(obj) + "'. This attribute is not known in any context!");
    }
    





    public void visitLocalVariableTable(LocalVariableTable obj) {}
    





    public void visitLocalVariable(LocalVariable obj) {}
    





    public void visitCodeException(CodeException obj) {}
    





    public void visitConstantPool(ConstantPool obj) {}
    




    public void visitInnerClass(InnerClass obj) {}
    




    public void visitLineNumber(LineNumber obj) {}
  }
  




  private void field_and_method_refs_are_valid()
  {
    JavaClass jc = Repository.lookupClass(myOwner.getClassName());
    DescendingVisitor v = new DescendingVisitor(jc, new FAMRAV_Visitor(jc, null));
    v.visit();
  }
  
  private class FAMRAV_Visitor
    extends EmptyVisitor
    implements Visitor
  {
    private final JavaClass jc;
    private final ConstantPool cp;
    
    FAMRAV_Visitor(JavaClass x1, Pass2Verifier.1 x2)
    {
      this(x1);
    }
    
    private FAMRAV_Visitor(JavaClass _jc) {
      jc = _jc;
      cp = _jc.getConstantPool();
    }
    
    public void visitConstantFieldref(ConstantFieldref obj) {
      if (obj.getTag() != 9) {
        throw new ClassConstraintException("ConstantFieldref '" + Pass2Verifier.tostring(obj) + "' has wrong tag!");
      }
      int name_and_type_index = obj.getNameAndTypeIndex();
      ConstantNameAndType cnat = (ConstantNameAndType)cp.getConstant(name_and_type_index);
      String name = ((ConstantUtf8)cp.getConstant(cnat.getNameIndex())).getBytes();
      if (!Pass2Verifier.validFieldName(name)) {
        throw new ClassConstraintException("Invalid field name '" + name + "' referenced by '" + Pass2Verifier.tostring(obj) + "'.");
      }
      
      int class_index = obj.getClassIndex();
      ConstantClass cc = (ConstantClass)cp.getConstant(class_index);
      String className = ((ConstantUtf8)cp.getConstant(cc.getNameIndex())).getBytes();
      if (!Pass2Verifier.validClassName(className)) {
        throw new ClassConstraintException("Illegal class name '" + className + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
      }
      
      String sig = ((ConstantUtf8)cp.getConstant(cnat.getSignatureIndex())).getBytes();
      try
      {
        t = Type.getType(sig);
      }
      catch (ClassFormatError cfe) {
        Type t;
        throw new ClassConstraintException("Illegal descriptor (==signature) '" + sig + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
      }
    }
    
    public void visitConstantMethodref(ConstantMethodref obj) {
      if (obj.getTag() != 10) {
        throw new ClassConstraintException("ConstantMethodref '" + Pass2Verifier.tostring(obj) + "' has wrong tag!");
      }
      int name_and_type_index = obj.getNameAndTypeIndex();
      ConstantNameAndType cnat = (ConstantNameAndType)cp.getConstant(name_and_type_index);
      String name = ((ConstantUtf8)cp.getConstant(cnat.getNameIndex())).getBytes();
      if (!Pass2Verifier.validClassMethodName(name)) {
        throw new ClassConstraintException("Invalid (non-interface) method name '" + name + "' referenced by '" + Pass2Verifier.tostring(obj) + "'.");
      }
      
      int class_index = obj.getClassIndex();
      ConstantClass cc = (ConstantClass)cp.getConstant(class_index);
      String className = ((ConstantUtf8)cp.getConstant(cc.getNameIndex())).getBytes();
      if (!Pass2Verifier.validClassName(className)) {
        throw new ClassConstraintException("Illegal class name '" + className + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
      }
      
      String sig = ((ConstantUtf8)cp.getConstant(cnat.getSignatureIndex())).getBytes();
      try
      {
        Type t = Type.getReturnType(sig);
        Type[] ts = Type.getArgumentTypes(sig);
        if ((name.equals("<init>")) && (t != Type.VOID)) {
          throw new ClassConstraintException("Instance initialization method must have VOID return type.");
        }
      }
      catch (ClassFormatError cfe)
      {
        throw new ClassConstraintException("Illegal descriptor (==signature) '" + sig + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
      }
    }
    
    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) {
      if (obj.getTag() != 11) {
        throw new ClassConstraintException("ConstantInterfaceMethodref '" + Pass2Verifier.tostring(obj) + "' has wrong tag!");
      }
      int name_and_type_index = obj.getNameAndTypeIndex();
      ConstantNameAndType cnat = (ConstantNameAndType)cp.getConstant(name_and_type_index);
      String name = ((ConstantUtf8)cp.getConstant(cnat.getNameIndex())).getBytes();
      if (!Pass2Verifier.validInterfaceMethodName(name)) {
        throw new ClassConstraintException("Invalid (interface) method name '" + name + "' referenced by '" + Pass2Verifier.tostring(obj) + "'.");
      }
      
      int class_index = obj.getClassIndex();
      ConstantClass cc = (ConstantClass)cp.getConstant(class_index);
      String className = ((ConstantUtf8)cp.getConstant(cc.getNameIndex())).getBytes();
      if (!Pass2Verifier.validClassName(className)) {
        throw new ClassConstraintException("Illegal class name '" + className + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
      }
      
      String sig = ((ConstantUtf8)cp.getConstant(cnat.getSignatureIndex())).getBytes();
      try
      {
        Type t = Type.getReturnType(sig);
        Type[] ts = Type.getArgumentTypes(sig);
        if ((name.equals("<clinit>")) && (t != Type.VOID)) {
          addMessage("Class or interface initialization method '<clinit>' usually has VOID return type instead of '" + t + "'. Note this is really not a requirement of The Java Virtual Machine Specification, Second Edition.");
        }
      }
      catch (ClassFormatError cfe)
      {
        throw new ClassConstraintException("Illegal descriptor (==signature) '" + sig + "' used by '" + Pass2Verifier.tostring(obj) + "'.");
      }
    }
  }
  






  private static final boolean validClassName(String name)
  {
    return true;
  }
  






  private static boolean validMethodName(String name, boolean allowStaticInit)
  {
    if (validJavaLangMethodName(name)) { return true;
    }
    if (allowStaticInit) {
      return (name.equals("<init>")) || (name.equals("<clinit>"));
    }
    
    return name.equals("<init>");
  }
  





  private static boolean validClassMethodName(String name)
  {
    return validMethodName(name, false);
  }
  





  private static boolean validJavaLangMethodName(String name)
  {
    if (!Character.isJavaIdentifierStart(name.charAt(0))) { return false;
    }
    for (int i = 1; i < name.length(); i++) {
      if (!Character.isJavaIdentifierPart(name.charAt(i))) return false;
    }
    return true;
  }
  





  private static boolean validInterfaceMethodName(String name)
  {
    if (name.startsWith("<")) return false;
    return validJavaLangMethodName(name);
  }
  




  private static boolean validJavaIdentifier(String name)
  {
    if (!Character.isJavaIdentifierStart(name.charAt(0))) { return false;
    }
    for (int i = 1; i < name.length(); i++) {
      if (!Character.isJavaIdentifierPart(name.charAt(i))) return false;
    }
    return true;
  }
  




  private static boolean validFieldName(String name)
  {
    return validJavaIdentifier(name);
  }
  

















  private class InnerClassDetector
    extends EmptyVisitor
  {
    private boolean hasInnerClass = false;
    private JavaClass jc;
    private ConstantPool cp;
    
    private InnerClassDetector() {}
    
    public InnerClassDetector(JavaClass _jc) { jc = _jc;
      cp = jc.getConstantPool();
      new DescendingVisitor(jc, this).visit();
    }
    


    public boolean innerClassReferenced()
    {
      return hasInnerClass;
    }
    
    public void visitConstantClass(ConstantClass obj) {
      Constant c = cp.getConstant(obj.getNameIndex());
      if ((c instanceof ConstantUtf8)) {
        String classname = ((ConstantUtf8)c).getBytes();
        if (classname.startsWith(jc.getClassName().replace('.', '/') + "$")) {
          hasInnerClass = true;
        }
      }
    }
  }
  


  private static String tostring(Node n)
  {
    return new StringRepresentation(n).toString();
  }
}
