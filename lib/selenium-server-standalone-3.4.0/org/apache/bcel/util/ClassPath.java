package org.apache.bcel.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;





















































public class ClassPath
{
  private PathEntry[] paths;
  
  public ClassPath(String class_path)
  {
    ArrayList vec = new ArrayList();
    
    StringTokenizer tok = new StringTokenizer(class_path, System.getProperty("path.separator"));
    
    while (tok.hasMoreTokens())
    {
      String path = tok.nextToken();
      
      if (!path.equals("")) {
        File file = new File(path);
        try
        {
          if (file.exists()) {
            if (file.isDirectory()) {
              vec.add(new Dir(path));
            } else
              vec.add(new Zip(new ZipFile(file)));
          }
        } catch (IOException e) {
          System.err.println("CLASSPATH component " + file + ": " + e);
        }
      }
    }
    
    paths = new PathEntry[vec.size()];
    vec.toArray(paths);
  }
  


  public ClassPath()
  {
    this(getClassPath());
  }
  
  private static final void getPathComponents(String path, ArrayList list) {
    if (path != null) {
      StringTokenizer tok = new StringTokenizer(path, File.pathSeparator);
      
      while (tok.hasMoreTokens()) {
        String name = tok.nextToken();
        File file = new File(name);
        
        if (file.exists()) {
          list.add(name);
        }
      }
    }
  }
  



  public static final String getClassPath()
  {
    String class_path = System.getProperty("java.class.path");
    String boot_path = System.getProperty("sun.boot.class.path");
    String ext_path = System.getProperty("java.ext.dirs");
    
    ArrayList list = new ArrayList();
    
    getPathComponents(class_path, list);
    getPathComponents(boot_path, list);
    
    ArrayList dirs = new ArrayList();
    getPathComponents(ext_path, dirs);
    
    for (Iterator e = dirs.iterator(); e.hasNext();) {
      File ext_dir = new File((String)e.next());
      String[] extensions = ext_dir.list(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          name = name.toLowerCase();
          return (name.endsWith(".zip")) || (name.endsWith(".jar"));
        }
      });
      
      if (extensions != null) {
        for (int i = 0; i < extensions.length; i++)
          list.add(ext_path + File.separatorChar + extensions[i]);
      }
    }
    StringBuffer buf = new StringBuffer();
    
    for (Iterator e = list.iterator(); e.hasNext();) {
      buf.append((String)e.next());
      
      if (e.hasNext()) {
        buf.append(File.pathSeparatorChar);
      }
    }
    return buf.toString();
  }
  


  public InputStream getInputStream(String name)
    throws IOException
  {
    return getInputStream(name, ".class");
  }
  





  public InputStream getInputStream(String name, String suffix)
    throws IOException
  {
    InputStream is = null;
    try
    {
      is = getClass().getClassLoader().getResourceAsStream(name + suffix);
    }
    catch (Exception e) {}
    if (is != null) {
      return is;
    }
    return getClassFile(name, suffix).getInputStream();
  }
  



  public ClassFile getClassFile(String name, String suffix)
    throws IOException
  {
    for (int i = 0; i < paths.length; i++)
    {
      ClassFile cf;
      if ((cf = paths[i].getClassFile(name, suffix)) != null) {
        return cf;
      }
    }
    throw new IOException("Couldn't find: " + name + suffix);
  }
  


  public ClassFile getClassFile(String name)
    throws IOException
  {
    return getClassFile(name, ".class");
  }
  



  public byte[] getBytes(String name, String suffix)
    throws IOException
  {
    InputStream is = getInputStream(name, suffix);
    
    if (is == null) {
      throw new IOException("Couldn't find: " + name + suffix);
    }
    DataInputStream dis = new DataInputStream(is);
    byte[] bytes = new byte[is.available()];
    dis.readFully(bytes);
    dis.close();is.close();
    
    return bytes;
  }
  

  public byte[] getBytes(String name)
    throws IOException
  {
    return getBytes(name, ".class");
  }
  


  public String getPath(String name)
    throws IOException
  {
    int index = name.lastIndexOf('.');
    String suffix = "";
    
    if (index > 0) {
      suffix = name.substring(index);
      name = name.substring(0, index);
    }
    
    return getPath(name, suffix);
  }
  





  public String getPath(String name, String suffix)
    throws IOException { return getClassFile(name, suffix).getPath(); }
  private static abstract class PathEntry { private PathEntry() {}
    abstract ClassPath.ClassFile getClassFile(String paramString1, String paramString2) throws IOException;
    PathEntry(ClassPath.1 x0) { this(); }
  }
  

  public static abstract class ClassFile
  {
    public ClassFile() {}
    

    public abstract InputStream getInputStream()
      throws IOException;
    

    public abstract String getPath();
    
    public abstract long getTime();
    
    public abstract long getSize();
  }
  
  private static class Dir
    extends ClassPath.PathEntry
  {
    private String dir;
    
    Dir(String d)
    {
      super();dir = d;
    }
    
    ClassPath.ClassFile getClassFile(String name, String suffix) throws IOException { File file = new File(dir + File.separatorChar + name.replace('.', File.separatorChar) + suffix);
      

      return file.exists() ? new ClassPath.2(this, file) : null;
    }
    










    public String toString() { return dir; }
  }
  
  private static class Zip extends ClassPath.PathEntry {
    private ZipFile zip;
    
    Zip(ZipFile z) { super();zip = z;
    }
    
    ClassPath.ClassFile getClassFile(String name, String suffix) throws IOException { ZipEntry entry = zip.getEntry(name.replace('.', '/') + suffix);
      
      return entry != null ? new ClassPath.3(this, entry) : null;
    }
  }
}
