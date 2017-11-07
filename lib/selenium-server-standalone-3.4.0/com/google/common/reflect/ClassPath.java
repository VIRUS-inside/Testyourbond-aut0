package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.MultimapBuilder.MultimapBuilderWithKeys;
import com.google.common.collect.MultimapBuilder.SetMultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import javax.annotation.Nullable;

















@Beta
public final class ClassPath
{
  private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
  
  private static final Predicate<ClassInfo> IS_TOP_LEVEL = new Predicate()
  {
    public boolean apply(ClassPath.ClassInfo info)
    {
      return className.indexOf('$') == -1;
    }
  };
  


  private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
  
  private static final String CLASS_FILE_NAME_EXTENSION = ".class";
  private final ImmutableSet<ResourceInfo> resources;
  
  private ClassPath(ImmutableSet<ResourceInfo> resources)
  {
    this.resources = resources;
  }
  








  public static ClassPath from(ClassLoader classloader)
    throws IOException
  {
    DefaultScanner scanner = new DefaultScanner();
    scanner.scan(classloader);
    return new ClassPath(scanner.getResources());
  }
  



  public ImmutableSet<ResourceInfo> getResources()
  {
    return resources;
  }
  




  public ImmutableSet<ClassInfo> getAllClasses()
  {
    return FluentIterable.from(resources).filter(ClassInfo.class).toSet();
  }
  
  public ImmutableSet<ClassInfo> getTopLevelClasses()
  {
    return FluentIterable.from(resources).filter(ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
  }
  
  public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName)
  {
    Preconditions.checkNotNull(packageName);
    ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
    for (UnmodifiableIterator localUnmodifiableIterator = getTopLevelClasses().iterator(); localUnmodifiableIterator.hasNext();) { ClassInfo classInfo = (ClassInfo)localUnmodifiableIterator.next();
      if (classInfo.getPackageName().equals(packageName)) {
        builder.add(classInfo);
      }
    }
    return builder.build();
  }
  



  public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName)
  {
    Preconditions.checkNotNull(packageName);
    String packagePrefix = packageName + '.';
    ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
    for (UnmodifiableIterator localUnmodifiableIterator = getTopLevelClasses().iterator(); localUnmodifiableIterator.hasNext();) { ClassInfo classInfo = (ClassInfo)localUnmodifiableIterator.next();
      if (classInfo.getName().startsWith(packagePrefix)) {
        builder.add(classInfo);
      }
    }
    return builder.build();
  }
  


  @Beta
  public static class ResourceInfo
  {
    private final String resourceName;
    

    final ClassLoader loader;
    

    static ResourceInfo of(String resourceName, ClassLoader loader)
    {
      if (resourceName.endsWith(".class")) {
        return new ClassPath.ClassInfo(resourceName, loader);
      }
      return new ResourceInfo(resourceName, loader);
    }
    
    ResourceInfo(String resourceName, ClassLoader loader)
    {
      this.resourceName = ((String)Preconditions.checkNotNull(resourceName));
      this.loader = ((ClassLoader)Preconditions.checkNotNull(loader));
    }
    







    public final URL url()
    {
      URL url = loader.getResource(resourceName);
      if (url == null) {
        throw new NoSuchElementException(resourceName);
      }
      return url;
    }
    






    public final ByteSource asByteSource()
    {
      return Resources.asByteSource(url());
    }
    







    public final CharSource asCharSource(Charset charset)
    {
      return Resources.asCharSource(url(), charset);
    }
    
    public final String getResourceName()
    {
      return resourceName;
    }
    
    public int hashCode()
    {
      return resourceName.hashCode();
    }
    
    public boolean equals(Object obj)
    {
      if ((obj instanceof ResourceInfo)) {
        ResourceInfo that = (ResourceInfo)obj;
        return (resourceName.equals(resourceName)) && (loader == loader);
      }
      return false;
    }
    

    public String toString()
    {
      return resourceName;
    }
  }
  

  @Beta
  public static final class ClassInfo
    extends ClassPath.ResourceInfo
  {
    private final String className;
    

    ClassInfo(String resourceName, ClassLoader loader)
    {
      super(loader);
      className = ClassPath.getClassName(resourceName);
    }
    





    public String getPackageName()
    {
      return Reflection.getPackageName(className);
    }
    





    public String getSimpleName()
    {
      int lastDollarSign = className.lastIndexOf('$');
      if (lastDollarSign != -1) {
        String innerClassName = className.substring(lastDollarSign + 1);
        

        return CharMatcher.digit().trimLeadingFrom(innerClassName);
      }
      String packageName = getPackageName();
      if (packageName.isEmpty()) {
        return className;
      }
      

      return className.substring(packageName.length() + 1);
    }
    





    public String getName()
    {
      return className;
    }
    




    public Class<?> load()
    {
      try
      {
        return loader.loadClass(className);
      }
      catch (ClassNotFoundException e) {
        throw new IllegalStateException(e);
      }
    }
    
    public String toString()
    {
      return className;
    }
  }
  



  static abstract class Scanner
  {
    Scanner() {}
    


    private final Set<File> scannedUris = Sets.newHashSet();
    
    public final void scan(ClassLoader classloader) throws IOException {
      for (UnmodifiableIterator localUnmodifiableIterator = getClassPathEntries(classloader).entrySet().iterator(); localUnmodifiableIterator.hasNext();) { Map.Entry<File, ClassLoader> entry = (Map.Entry)localUnmodifiableIterator.next();
        scan((File)entry.getKey(), (ClassLoader)entry.getValue());
      }
    }
    
    protected abstract void scanDirectory(ClassLoader paramClassLoader, File paramFile)
      throws IOException;
    
    protected abstract void scanJarFile(ClassLoader paramClassLoader, JarFile paramJarFile) throws IOException;
    
    @VisibleForTesting
    final void scan(File file, ClassLoader classloader) throws IOException
    {
      if (scannedUris.add(file.getCanonicalFile())) {
        scanFrom(file, classloader);
      }
    }
    
    private void scanFrom(File file, ClassLoader classloader) throws IOException {
      try {
        if (!file.exists()) {
          return;
        }
      } catch (SecurityException e) {
        ClassPath.logger.warning("Cannot access " + file + ": " + e);
        
        return;
      }
      if (file.isDirectory()) {
        scanDirectory(classloader, file);
      } else {
        scanJar(file, classloader);
      }
    }
    
    private void scanJar(File file, ClassLoader classloader) throws IOException
    {
      try {
        jarFile = new JarFile(file);
      } catch (IOException e) {
        JarFile jarFile;
        return;
      }
      try { JarFile jarFile;
        for (e = getClassPathFromManifest(file, jarFile.getManifest()).iterator(); e.hasNext();) { File path = (File)e.next();
          scan(path, classloader);
        }
        scanJarFile(classloader, jarFile); return;
      } finally {
        try {
          jarFile.close();
        }
        catch (IOException localIOException2) {}
      }
    }
    






    @VisibleForTesting
    static ImmutableSet<File> getClassPathFromManifest(File jarFile, @Nullable Manifest manifest)
    {
      if (manifest == null) {
        return ImmutableSet.of();
      }
      ImmutableSet.Builder<File> builder = ImmutableSet.builder();
      
      String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
      if (classpathAttribute != null) {
        for (String path : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute))
        {
          try {
            url = getClassPathEntry(jarFile, path);
          } catch (MalformedURLException e) {
            URL url;
            ClassPath.logger.warning("Invalid Class-Path entry: " + path); }
          continue;
          URL url;
          if (url.getProtocol().equals("file")) {
            builder.add(new File(url.getFile()));
          }
        }
      }
      return builder.build();
    }
    
    @VisibleForTesting
    static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
      LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
      
      ClassLoader parent = classloader.getParent();
      if (parent != null) {
        entries.putAll(getClassPathEntries(parent));
      }
      if ((classloader instanceof URLClassLoader)) {
        URLClassLoader urlClassLoader = (URLClassLoader)classloader;
        for (URL entry : urlClassLoader.getURLs()) {
          if (entry.getProtocol().equals("file")) {
            File file = new File(entry.getFile());
            if (!entries.containsKey(file)) {
              entries.put(file, classloader);
            }
          }
        }
      }
      return ImmutableMap.copyOf(entries);
    }
    






    @VisibleForTesting
    static URL getClassPathEntry(File jarFile, String path)
      throws MalformedURLException { return new URL(jarFile.toURI().toURL(), path); }
  }
  
  @VisibleForTesting
  static final class DefaultScanner extends ClassPath.Scanner {
    DefaultScanner() {}
    
    private final SetMultimap<ClassLoader, String> resources = MultimapBuilder.hashKeys().linkedHashSetValues().build();
    
    ImmutableSet<ClassPath.ResourceInfo> getResources() {
      ImmutableSet.Builder<ClassPath.ResourceInfo> builder = ImmutableSet.builder();
      for (Map.Entry<ClassLoader, String> entry : resources.entries()) {
        builder.add(ClassPath.ResourceInfo.of((String)entry.getValue(), (ClassLoader)entry.getKey()));
      }
      return builder.build();
    }
    
    protected void scanJarFile(ClassLoader classloader, JarFile file)
    {
      Enumeration<JarEntry> entries = file.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = (JarEntry)entries.nextElement();
        if ((!entry.isDirectory()) && (!entry.getName().equals("META-INF/MANIFEST.MF")))
        {

          resources.get(classloader).add(entry.getName());
        }
      }
    }
    
    protected void scanDirectory(ClassLoader classloader, File directory) throws IOException {
      scanDirectory(directory, classloader, "");
    }
    
    private void scanDirectory(File directory, ClassLoader classloader, String packagePrefix) throws IOException
    {
      File[] files = directory.listFiles();
      if (files == null) {
        ClassPath.logger.warning("Cannot read directory " + directory);
        
        return;
      }
      for (File f : files) {
        String name = f.getName();
        if (f.isDirectory()) {
          scanDirectory(f, classloader, packagePrefix + name + "/");
        } else {
          String resourceName = packagePrefix + name;
          if (!resourceName.equals("META-INF/MANIFEST.MF")) {
            resources.get(classloader).add(resourceName);
          }
        }
      }
    }
  }
  
  @VisibleForTesting
  static String getClassName(String filename) {
    int classNameEnd = filename.length() - ".class".length();
    return filename.substring(0, classNameEnd).replace('/', '.');
  }
}
