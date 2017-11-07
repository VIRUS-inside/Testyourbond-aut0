package org.yaml.snakeyaml.extensions.compactnotation;







public class PackageCompactConstructor
  extends CompactConstructor
{
  private String packageName;
  






  public PackageCompactConstructor(String packageName)
  {
    this.packageName = packageName;
  }
  
  protected Class<?> getClassForName(String name) throws ClassNotFoundException
  {
    if (name.indexOf('.') < 0) {
      try {
        return Class.forName(packageName + "." + name);
      }
      catch (ClassNotFoundException e) {}
    }
    

    return super.getClassForName(name);
  }
}
