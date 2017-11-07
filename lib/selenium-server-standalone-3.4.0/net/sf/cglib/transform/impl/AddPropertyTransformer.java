package net.sf.cglib.transform.impl;

import java.util.Map;
import java.util.Set;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassEmitterTransformer;











public class AddPropertyTransformer
  extends ClassEmitterTransformer
{
  private final String[] names;
  private final .Type[] types;
  
  public AddPropertyTransformer(Map props)
  {
    int size = props.size();
    names = ((String[])props.keySet().toArray(new String[size]));
    types = new .Type[size];
    for (int i = 0; i < size; i++) {
      types[i] = ((.Type)props.get(names[i]));
    }
  }
  
  public AddPropertyTransformer(String[] names, .Type[] types) {
    this.names = names;
    this.types = types;
  }
  
  public void end_class() {
    if (!TypeUtils.isAbstract(getAccess())) {
      EmitUtils.add_properties(this, names, types);
    }
    super.end_class();
  }
}
