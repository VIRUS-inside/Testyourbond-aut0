package org.openqa.selenium;





























public enum Architecture
{
  X86(new String[] { "x86", "i386", "ia32", "i686", "i486", "i86", "pentium", "pentium_pro", "pentium_pro+mmx", "pentium+mmx" }), 
  






  X64(new String[] { "amd64", "ia64", "x86_64" }), 
  
  ARM(new String[] { "arm" }), 
  
  MIPS32(new String[] { "mips32" }), 
  





  MIPS64(new String[] { "mips64" }), 
  


  ANY(new String[] { "" });
  


  private final String[] archIdentifiers;
  


  private Architecture(String... partOfArch)
  {
    archIdentifiers = partOfArch;
  }
  






  public boolean is(Architecture compareWith)
  {
    return equals(compareWith);
  }
  





  public int getDataModel()
  {
    return 64;
  }
  
  public String toString()
  {
    return name().toLowerCase();
  }
  




  public static Architecture getCurrent()
  {
    return extractFromSysProperty(System.getProperty("os.arch"));
  }
  








  public static Architecture extractFromSysProperty(String arch)
  {
    if (arch != null) {
      arch = arch.toLowerCase();
    }
    




    for (Architecture architecture : values()) {
      if (architecture != ANY)
      {


        for (String matcher : archIdentifiers) {
          if (matcher.equals(arch)) {
            return architecture;
          }
        }
      }
    }
    throw new UnsupportedOperationException("Unknown architecture: " + arch);
  }
}
