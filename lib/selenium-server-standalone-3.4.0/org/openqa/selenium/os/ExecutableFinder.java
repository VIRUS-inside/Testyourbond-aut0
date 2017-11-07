package org.openqa.selenium.os;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.UnmodifiableIterator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;
import org.openqa.selenium.Platform;



















public class ExecutableFinder
{
  private static final ImmutableSet<String> ENDINGS = Platform.getCurrent().is(Platform.WINDOWS) ? 
    ImmutableSet.of("", ".cmd", ".exe", ".com", ".bat") : ImmutableSet.of("");
  
  private final ImmutableSet.Builder<String> pathSegmentBuilder = new ImmutableSet.Builder();
  



  public ExecutableFinder() {}
  


  public String find(String named)
  {
    File file = new File(named);
    if (canExecute(file)) {
      return named;
    }
    
    if (Platform.getCurrent().is(Platform.WINDOWS)) {
      file = new File(named + ".exe");
      if (canExecute(file)) {
        return named + ".exe";
      }
    }
    
    addPathFromEnvironment();
    if (Platform.getCurrent().is(Platform.MAC)) {
      addMacSpecificPath();
    }
    
    for (UnmodifiableIterator localUnmodifiableIterator1 = pathSegmentBuilder.build().iterator(); localUnmodifiableIterator1.hasNext();) { pathSegment = (String)localUnmodifiableIterator1.next();
      for (localUnmodifiableIterator2 = ENDINGS.iterator(); localUnmodifiableIterator2.hasNext();) { String ending = (String)localUnmodifiableIterator2.next();
        file = new File(pathSegment, named + ending);
        if (canExecute(file))
          return file.getAbsolutePath();
      } }
    String pathSegment;
    UnmodifiableIterator localUnmodifiableIterator2;
    return null;
  }
  
  private void addPathFromEnvironment() {
    String pathName = "PATH";
    Map<String, String> env = System.getenv();
    if (!env.containsKey(pathName)) {
      for (String key : env.keySet()) {
        if (pathName.equalsIgnoreCase(key)) {
          pathName = key;
          break;
        }
      }
    }
    String path = (String)env.get(pathName);
    if (path != null) {
      pathSegmentBuilder.add(path.split(File.pathSeparator));
    }
  }
  
  private void addMacSpecificPath() {
    File pathFile = new File("/etc/paths");
    if (pathFile.exists()) {
      try {
        pathSegmentBuilder.addAll(Files.readAllLines(pathFile.toPath()));
      }
      catch (IOException localIOException) {}
    }
  }
  
  private static boolean canExecute(File file)
  {
    return (file.exists()) && (!file.isDirectory()) && (file.canExecute());
  }
}
