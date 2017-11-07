package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.TreeTraverser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SecureDirectoryStream;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;



































@Beta
@AndroidIncompatible
@GwtIncompatible
public final class MoreFiles
{
  private MoreFiles() {}
  
  public static ByteSource asByteSource(Path path, OpenOption... options)
  {
    return new PathByteSource(path, options, null);
  }
  
  private static final class PathByteSource extends ByteSource
  {
    private static final LinkOption[] FOLLOW_LINKS = new LinkOption[0];
    private final Path path;
    private final OpenOption[] options;
    private final boolean followLinks;
    
    private PathByteSource(Path path, OpenOption... options)
    {
      this.path = ((Path)Preconditions.checkNotNull(path));
      this.options = ((OpenOption[])options.clone());
      followLinks = followLinks(this.options);
    }
    
    private static boolean followLinks(OpenOption[] options)
    {
      for (OpenOption option : options) {
        if (option == LinkOption.NOFOLLOW_LINKS) {
          return false;
        }
      }
      return true;
    }
    
    public InputStream openStream() throws IOException
    {
      return java.nio.file.Files.newInputStream(path, options);
    }
    
    private BasicFileAttributes readAttributes() throws IOException {
      return java.nio.file.Files.readAttributes(path, BasicFileAttributes.class, new LinkOption[] { followLinks ? FOLLOW_LINKS : LinkOption.NOFOLLOW_LINKS });
    }
    


    public Optional<Long> sizeIfKnown()
    {
      try
      {
        attrs = readAttributes();
      } catch (IOException e) {
        BasicFileAttributes attrs;
        return Optional.absent();
      }
      
      BasicFileAttributes attrs;
      
      if ((attrs.isDirectory()) || (attrs.isSymbolicLink())) {
        return Optional.absent();
      }
      
      return Optional.of(Long.valueOf(attrs.size()));
    }
    
    public long size() throws IOException
    {
      BasicFileAttributes attrs = readAttributes();
      


      if (attrs.isDirectory())
        throw new IOException("can't read: is a directory");
      if (attrs.isSymbolicLink()) {
        throw new IOException("can't read: is a symbolic link");
      }
      
      return attrs.size();
    }
    
    public byte[] read() throws IOException
    {
      SeekableByteChannel channel = java.nio.file.Files.newByteChannel(path, options);Throwable localThrowable3 = null;
      try { return Files.readFile(
          Channels.newInputStream(channel), channel.size());
      }
      catch (Throwable localThrowable4)
      {
        localThrowable3 = localThrowable4;throw localThrowable4;
      }
      finally {
        if (channel != null) if (localThrowable3 != null) try { channel.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else channel.close();
      }
    }
    
    public String toString() {
      return "MoreFiles.asByteSource(" + path + ", " + Arrays.toString(options) + ")";
    }
  }
  









  public static ByteSink asByteSink(Path path, OpenOption... options)
  {
    return new PathByteSink(path, options, null);
  }
  
  private static final class PathByteSink extends ByteSink
  {
    private final Path path;
    private final OpenOption[] options;
    
    private PathByteSink(Path path, OpenOption... options) {
      this.path = ((Path)Preconditions.checkNotNull(path));
      this.options = ((OpenOption[])options.clone());
    }
    
    public OutputStream openStream()
      throws IOException
    {
      return java.nio.file.Files.newOutputStream(path, options);
    }
    
    public String toString()
    {
      return "MoreFiles.asByteSink(" + path + ", " + Arrays.toString(options) + ")";
    }
  }
  








  public static CharSource asCharSource(Path path, Charset charset, OpenOption... options)
  {
    return asByteSource(path, options).asCharSource(charset);
  }
  










  public static CharSink asCharSink(Path path, Charset charset, OpenOption... options)
  {
    return asByteSink(path, options).asCharSink(charset);
  }
  
  /* Error */
  public static ImmutableList<Path> listFiles(Path dir)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 10	java/nio/file/Files:newDirectoryStream	(Ljava/nio/file/Path;)Ljava/nio/file/DirectoryStream;
    //   4: astore_1
    //   5: aconst_null
    //   6: astore_2
    //   7: aload_1
    //   8: invokestatic 11	com/google/common/collect/ImmutableList:copyOf	(Ljava/lang/Iterable;)Lcom/google/common/collect/ImmutableList;
    //   11: astore_3
    //   12: aload_1
    //   13: ifnull +33 -> 46
    //   16: aload_2
    //   17: ifnull +23 -> 40
    //   20: aload_1
    //   21: invokeinterface 12 1 0
    //   26: goto +20 -> 46
    //   29: astore 4
    //   31: aload_2
    //   32: aload 4
    //   34: invokevirtual 14	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   37: goto +9 -> 46
    //   40: aload_1
    //   41: invokeinterface 12 1 0
    //   46: aload_3
    //   47: areturn
    //   48: astore_3
    //   49: aload_3
    //   50: astore_2
    //   51: aload_3
    //   52: athrow
    //   53: astore 5
    //   55: aload_1
    //   56: ifnull +33 -> 89
    //   59: aload_2
    //   60: ifnull +23 -> 83
    //   63: aload_1
    //   64: invokeinterface 12 1 0
    //   69: goto +20 -> 89
    //   72: astore 6
    //   74: aload_2
    //   75: aload 6
    //   77: invokevirtual 14	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   80: goto +9 -> 89
    //   83: aload_1
    //   84: invokeinterface 12 1 0
    //   89: aload 5
    //   91: athrow
    //   92: astore_1
    //   93: aload_1
    //   94: invokevirtual 16	java/nio/file/DirectoryIteratorException:getCause	()Ljava/io/IOException;
    //   97: athrow
    // Line number table:
    //   Java source line #238	-> byte code offset #0
    //   Java source line #239	-> byte code offset #7
    //   Java source line #240	-> byte code offset #12
    //   Java source line #239	-> byte code offset #46
    //   Java source line #238	-> byte code offset #48
    //   Java source line #240	-> byte code offset #53
    //   Java source line #241	-> byte code offset #93
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	98	0	dir	Path
    //   4	80	1	stream	DirectoryStream<Path>
    //   92	2	1	e	DirectoryIteratorException
    //   6	69	2	localThrowable3	Throwable
    //   48	4	3	localThrowable1	Throwable
    //   48	4	3	localThrowable4	Throwable
    //   29	4	4	localThrowable	Throwable
    //   53	37	5	localObject	Object
    //   72	4	6	localThrowable2	Throwable
    // Exception table:
    //   from	to	target	type
    //   20	26	29	java/lang/Throwable
    //   7	12	48	java/lang/Throwable
    //   7	12	53	finally
    //   48	55	53	finally
    //   63	69	72	java/lang/Throwable
    //   0	46	92	java/nio/file/DirectoryIteratorException
    //   48	92	92	java/nio/file/DirectoryIteratorException
  }
  
  public static TreeTraverser<Path> directoryTreeTraverser()
  {
    return DirectoryTreeTraverser.INSTANCE;
  }
  
  private static final class DirectoryTreeTraverser extends TreeTraverser<Path>
  {
    private static final DirectoryTreeTraverser INSTANCE = new DirectoryTreeTraverser();
    
    private DirectoryTreeTraverser() {}
    
    public Iterable<Path> children(Path dir) { if (java.nio.file.Files.isDirectory(dir, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
        try {
          return MoreFiles.listFiles(dir);
        }
        catch (IOException e) {
          throw new DirectoryIteratorException(e);
        }
      }
      return ImmutableList.of();
    }
  }
  



  public static Predicate<Path> isDirectory(LinkOption... options)
  {
    LinkOption[] optionsCopy = (LinkOption[])options.clone();
    new Predicate()
    {
      public boolean apply(Path input) {
        return java.nio.file.Files.isDirectory(input, val$optionsCopy);
      }
      
      public String toString()
      {
        return "MoreFiles.isDirectory(" + Arrays.toString(val$optionsCopy) + ")";
      }
    };
  }
  



  public static Predicate<Path> isRegularFile(LinkOption... options)
  {
    LinkOption[] optionsCopy = (LinkOption[])options.clone();
    new Predicate()
    {
      public boolean apply(Path input) {
        return java.nio.file.Files.isRegularFile(input, val$optionsCopy);
      }
      
      public String toString()
      {
        return "MoreFiles.isRegularFile(" + Arrays.toString(val$optionsCopy) + ")";
      }
    };
  }
  


  public static void touch(Path path)
    throws IOException
  {
    Preconditions.checkNotNull(path);
    try
    {
      java.nio.file.Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
    } catch (NoSuchFileException e) {
      try {
        java.nio.file.Files.createFile(path, new FileAttribute[0]);
      }
      catch (FileAlreadyExistsException localFileAlreadyExistsException) {}
    }
  }
  

















  public static void createParentDirectories(Path path, FileAttribute<?>... attrs)
    throws IOException
  {
    Path normalizedAbsolutePath = path.toAbsolutePath().normalize();
    Path parent = normalizedAbsolutePath.getParent();
    if (parent == null)
    {



      return;
    }
    




    if (!java.nio.file.Files.isDirectory(parent, new LinkOption[0])) {
      java.nio.file.Files.createDirectories(parent, attrs);
      if (!java.nio.file.Files.isDirectory(parent, new LinkOption[0])) {
        throw new IOException("Unable to create parent directories of " + path);
      }
    }
  }
  











  public static String getFileExtension(Path path)
  {
    Path name = path.getFileName();
    

    if (name == null) {
      return "";
    }
    
    String fileName = name.toString();
    int dotIndex = fileName.lastIndexOf('.');
    return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
  }
  




  public static String getNameWithoutExtension(Path path)
  {
    Path name = path.getFileName();
    

    if (name == null) {
      return "";
    }
    
    String fileName = name.toString();
    int dotIndex = fileName.lastIndexOf('.');
    return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
  }
  






























  public static void deleteRecursively(Path path, RecursiveDeleteOption... options)
    throws IOException
  {
    Path parentPath = getParentPath(path);
    if (parentPath == null) {
      throw new FileSystemException(path.toString(), null, "can't delete recursively");
    }
    
    Collection<IOException> exceptions = null;
    try {
      boolean sdsSupported = false;
      DirectoryStream<Path> parent = java.nio.file.Files.newDirectoryStream(parentPath);Throwable localThrowable3 = null;
      try { if ((parent instanceof SecureDirectoryStream)) {
          sdsSupported = true;
          exceptions = deleteRecursivelySecure((SecureDirectoryStream)parent, path
            .getFileName());
        }
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;

      }
      finally
      {

        if (parent != null) if (localThrowable3 != null) try { parent.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else parent.close();
      }
      if (!sdsSupported) {
        checkAllowsInsecure(path, options);
        exceptions = deleteRecursivelyInsecure(path);
      }
    } catch (IOException e) {
      if (exceptions == null) {
        throw e;
      }
      exceptions.add(e);
    }
    

    if (exceptions != null) {
      throwDeleteFailed(path, exceptions);
    }
  }
  


































  public static void deleteDirectoryContents(Path path, RecursiveDeleteOption... options)
    throws IOException
  {
    Collection<IOException> exceptions = null;
    try { DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(path);Throwable localThrowable3 = null;
      try { if ((stream instanceof SecureDirectoryStream)) {
          SecureDirectoryStream<Path> sds = (SecureDirectoryStream)stream;
          exceptions = deleteDirectoryContentsSecure(sds);
        } else {
          checkAllowsInsecure(path, options);
          exceptions = deleteDirectoryContentsInsecure(stream);
        }
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;


      }
      finally
      {


        if (stream != null) if (localThrowable3 != null) try { stream.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else stream.close();
      } } catch (IOException e) { if (exceptions == null) {
        throw e;
      }
      exceptions.add(e);
    }
    

    if (exceptions != null) {
      throwDeleteFailed(path, exceptions);
    }
  }
  




  @Nullable
  private static Collection<IOException> deleteRecursivelySecure(SecureDirectoryStream<Path> dir, Path path)
  {
    Collection<IOException> exceptions = null;
    try {
      if (isDirectory(dir, path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
        SecureDirectoryStream<Path> childDir = dir.newDirectoryStream(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });Throwable localThrowable3 = null;
        try { exceptions = deleteDirectoryContentsSecure(childDir);
        }
        catch (Throwable localThrowable1)
        {
          localThrowable3 = localThrowable1;throw localThrowable1;
        } finally {
          if (childDir != null) { if (localThrowable3 != null) try { childDir.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { childDir.close();
            }
          }
        }
        if (exceptions == null) {
          dir.deleteDirectory(path);
        }
      } else {
        dir.deleteFile(path);
      }
      
      return exceptions;
    } catch (IOException e) {
      return addException(exceptions, e);
    }
  }
  




  @Nullable
  private static Collection<IOException> deleteDirectoryContentsSecure(SecureDirectoryStream<Path> dir)
  {
    Collection<IOException> exceptions = null;
    try {
      for (Path path : dir) {
        exceptions = concat(exceptions, deleteRecursivelySecure(dir, path.getFileName()));
      }
      
      return exceptions;
    } catch (DirectoryIteratorException e) {
      return addException(exceptions, e.getCause());
    }
  }
  



  @Nullable
  private static Collection<IOException> deleteRecursivelyInsecure(Path path)
  {
    Collection<IOException> exceptions = null;
    try {
      if (java.nio.file.Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
        DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(path);Throwable localThrowable3 = null;
        try { exceptions = deleteDirectoryContentsInsecure(stream);
        }
        catch (Throwable localThrowable1)
        {
          localThrowable3 = localThrowable1;throw localThrowable1;
        } finally {
          if (stream != null) { if (localThrowable3 != null) try { stream.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { stream.close();
            }
          }
        }
      }
      if (exceptions == null) {
        java.nio.file.Files.delete(path);
      }
      
      return exceptions;
    } catch (IOException e) {
      return addException(exceptions, e);
    }
  }
  





  @Nullable
  private static Collection<IOException> deleteDirectoryContentsInsecure(DirectoryStream<Path> dir)
  {
    Collection<IOException> exceptions = null;
    try {
      for (Path entry : dir) {
        exceptions = concat(exceptions, deleteRecursivelyInsecure(entry));
      }
      
      return exceptions;
    } catch (DirectoryIteratorException e) {
      return addException(exceptions, e.getCause());
    }
  }
  



  @Nullable
  private static Path getParentPath(Path path)
    throws IOException
  {
    Path parent = path.getParent();
    

    if (parent != null)
    {




      return parent;
    }
    

    if (path.getNameCount() == 0)
    {










      return null;
    }
    
    return path.getFileSystem().getPath(".", new String[0]);
  }
  



  private static void checkAllowsInsecure(Path path, RecursiveDeleteOption[] options)
    throws InsecureRecursiveDeleteException
  {
    if (!Arrays.asList(options).contains(RecursiveDeleteOption.ALLOW_INSECURE)) {
      throw new InsecureRecursiveDeleteException(path.toString());
    }
  }
  


  private static boolean isDirectory(SecureDirectoryStream<Path> dir, Path name, LinkOption... options)
    throws IOException
  {
    return 
    
      ((BasicFileAttributeView)dir.getFileAttributeView(name, BasicFileAttributeView.class, options)).readAttributes().isDirectory();
  }
  




  private static Collection<IOException> addException(@Nullable Collection<IOException> exceptions, IOException e)
  {
    if (exceptions == null) {
      exceptions = new ArrayList();
    }
    exceptions.add(e);
    return exceptions;
  }
  





  @Nullable
  private static Collection<IOException> concat(@Nullable Collection<IOException> exceptions, @Nullable Collection<IOException> other)
  {
    if (exceptions == null)
      return other;
    if (other != null) {
      exceptions.addAll(other);
    }
    return exceptions;
  }
  






  private static void throwDeleteFailed(Path path, Collection<IOException> exceptions)
    throws FileSystemException
  {
    FileSystemException deleteFailed = new FileSystemException(path.toString(), null, "failed to delete one or more files; see suppressed exceptions for details");
    
    for (IOException e : exceptions) {
      deleteFailed.addSuppressed(e);
    }
    throw deleteFailed;
  }
}
