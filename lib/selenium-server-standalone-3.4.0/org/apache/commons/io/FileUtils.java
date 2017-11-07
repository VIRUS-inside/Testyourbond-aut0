package org.apache.commons.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.output.NullOutputStream;





















































public class FileUtils
{
  public static final long ONE_KB = 1024L;
  public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024L);
  




  public static final long ONE_MB = 1048576L;
  




  public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
  




  private static final long FILE_COPY_BUFFER_SIZE = 31457280L;
  




  public static final long ONE_GB = 1073741824L;
  



  public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
  




  public static final long ONE_TB = 1099511627776L;
  




  public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
  




  public static final long ONE_PB = 1125899906842624L;
  




  public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
  




  public static final long ONE_EB = 1152921504606846976L;
  




  public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
  



  public static final BigInteger ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
  



  public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
  



  public static final File[] EMPTY_FILE_ARRAY = new File[0];
  



  public FileUtils() {}
  



  public static File getFile(File directory, String... names)
  {
    if (directory == null) {
      throw new NullPointerException("directory must not be null");
    }
    if (names == null) {
      throw new NullPointerException("names must not be null");
    }
    File file = directory;
    for (String name : names) {
      file = new File(file, name);
    }
    return file;
  }
  






  public static File getFile(String... names)
  {
    if (names == null) {
      throw new NullPointerException("names must not be null");
    }
    File file = null;
    for (String name : names) {
      if (file == null) {
        file = new File(name);
      } else {
        file = new File(file, name);
      }
    }
    return file;
  }
  






  public static String getTempDirectoryPath()
  {
    return System.getProperty("java.io.tmpdir");
  }
  






  public static File getTempDirectory()
  {
    return new File(getTempDirectoryPath());
  }
  






  public static String getUserDirectoryPath()
  {
    return System.getProperty("user.home");
  }
  






  public static File getUserDirectory()
  {
    return new File(getUserDirectoryPath());
  }
  

















  public static FileInputStream openInputStream(File file)
    throws IOException
  {
    if (file.exists()) {
      if (file.isDirectory()) {
        throw new IOException("File '" + file + "' exists but is a directory");
      }
      if (!file.canRead()) {
        throw new IOException("File '" + file + "' cannot be read");
      }
    } else {
      throw new FileNotFoundException("File '" + file + "' does not exist");
    }
    return new FileInputStream(file);
  }
  



















  public static FileOutputStream openOutputStream(File file)
    throws IOException
  {
    return openOutputStream(file, false);
  }
  




















  public static FileOutputStream openOutputStream(File file, boolean append)
    throws IOException
  {
    if (file.exists()) {
      if (file.isDirectory()) {
        throw new IOException("File '" + file + "' exists but is a directory");
      }
      if (!file.canWrite()) {
        throw new IOException("File '" + file + "' cannot be written to");
      }
    } else {
      File parent = file.getParentFile();
      if ((parent != null) && 
        (!parent.mkdirs()) && (!parent.isDirectory())) {
        throw new IOException("Directory '" + parent + "' could not be created");
      }
    }
    
    return new FileOutputStream(file, append);
  }
  






  public static String byteCountToDisplaySize(BigInteger size)
  {
    String displaySize;
    




    String displaySize;
    




    if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
      displaySize = String.valueOf(size.divide(ONE_EB_BI)) + " EB"; } else { String displaySize;
      if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
        displaySize = String.valueOf(size.divide(ONE_PB_BI)) + " PB"; } else { String displaySize;
        if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
          displaySize = String.valueOf(size.divide(ONE_TB_BI)) + " TB"; } else { String displaySize;
          if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_GB_BI)) + " GB"; } else { String displaySize;
            if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
              displaySize = String.valueOf(size.divide(ONE_MB_BI)) + " MB"; } else { String displaySize;
              if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
                displaySize = String.valueOf(size.divide(ONE_KB_BI)) + " KB";
              } else
                displaySize = String.valueOf(size) + " bytes";
            } } } } }
    return displaySize;
  }
  














  public static String byteCountToDisplaySize(long size)
  {
    return byteCountToDisplaySize(BigInteger.valueOf(size));
  }
  











  public static void touch(File file)
    throws IOException
  {
    if (!file.exists()) {
      OutputStream out = openOutputStream(file);
      IOUtils.closeQuietly(out);
    }
    boolean success = file.setLastModified(System.currentTimeMillis());
    if (!success) {
      throw new IOException("Unable to set the last modification time for " + file);
    }
  }
  








  public static File[] convertFileCollectionToFileArray(Collection<File> files)
  {
    return (File[])files.toArray(new File[files.size()]);
  }
  










  private static void innerListFiles(Collection<File> files, File directory, IOFileFilter filter, boolean includeSubDirectories)
  {
    File[] found = directory.listFiles(filter);
    
    if (found != null) {
      for (File file : found) {
        if (file.isDirectory()) {
          if (includeSubDirectories) {
            files.add(file);
          }
          innerListFiles(files, file, filter, includeSubDirectories);
        } else {
          files.add(file);
        }
      }
    }
  }
  

























  public static Collection<File> listFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter)
  {
    validateListFilesParameters(directory, fileFilter);
    
    IOFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
    IOFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);
    

    Collection<File> files = new LinkedList();
    innerListFiles(files, directory, FileFilterUtils.or(new IOFileFilter[] { effFileFilter, effDirFilter }), false);
    
    return files;
  }
  









  private static void validateListFilesParameters(File directory, IOFileFilter fileFilter)
  {
    if (!directory.isDirectory()) {
      throw new IllegalArgumentException("Parameter 'directory' is not a directory: " + directory);
    }
    if (fileFilter == null) {
      throw new NullPointerException("Parameter 'fileFilter' is null");
    }
  }
  





  private static IOFileFilter setUpEffectiveFileFilter(IOFileFilter fileFilter)
  {
    return FileFilterUtils.and(new IOFileFilter[] { fileFilter, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE) });
  }
  





  private static IOFileFilter setUpEffectiveDirFilter(IOFileFilter dirFilter)
  {
    return dirFilter == null ? FalseFileFilter.INSTANCE : FileFilterUtils.and(new IOFileFilter[] { dirFilter, DirectoryFileFilter.INSTANCE });
  }
  




















  public static Collection<File> listFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter)
  {
    validateListFilesParameters(directory, fileFilter);
    
    IOFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
    IOFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);
    

    Collection<File> files = new LinkedList();
    if (directory.isDirectory()) {
      files.add(directory);
    }
    innerListFiles(files, directory, FileFilterUtils.or(new IOFileFilter[] { effFileFilter, effDirFilter }), true);
    
    return files;
  }
  


















  public static Iterator<File> iterateFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter)
  {
    return listFiles(directory, fileFilter, dirFilter).iterator();
  }
  




















  public static Iterator<File> iterateFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter)
  {
    return listFilesAndDirs(directory, fileFilter, dirFilter).iterator();
  }
  







  private static String[] toSuffixes(String[] extensions)
  {
    String[] suffixes = new String[extensions.length];
    for (int i = 0; i < extensions.length; i++) {
      suffixes[i] = ("." + extensions[i]);
    }
    return suffixes;
  }
  




  public static Collection<File> listFiles(File directory, String[] extensions, boolean recursive)
  {
    IOFileFilter filter;
    


    IOFileFilter filter;
    


    if (extensions == null) {
      filter = TrueFileFilter.INSTANCE;
    } else {
      String[] suffixes = toSuffixes(extensions);
      filter = new SuffixFileFilter(suffixes);
    }
    return listFiles(directory, filter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
  }
  














  public static Iterator<File> iterateFiles(File directory, String[] extensions, boolean recursive)
  {
    return listFiles(directory, extensions, recursive).iterator();
  }
  














  public static boolean contentEquals(File file1, File file2)
    throws IOException
  {
    boolean file1Exists = file1.exists();
    if (file1Exists != file2.exists()) {
      return false;
    }
    
    if (!file1Exists)
    {
      return true;
    }
    
    if ((file1.isDirectory()) || (file2.isDirectory()))
    {
      throw new IOException("Can't compare directories, only files");
    }
    
    if (file1.length() != file2.length())
    {
      return false;
    }
    
    if (file1.getCanonicalFile().equals(file2.getCanonicalFile()))
    {
      return true;
    }
    
    InputStream input1 = null;
    InputStream input2 = null;
    try {
      input1 = new FileInputStream(file1);
      input2 = new FileInputStream(file2);
      return IOUtils.contentEquals(input1, input2);
    }
    finally {
      IOUtils.closeQuietly(input1);
      IOUtils.closeQuietly(input2);
    }
  }
  

















  public static boolean contentEqualsIgnoreEOL(File file1, File file2, String charsetName)
    throws IOException
  {
    boolean file1Exists = file1.exists();
    if (file1Exists != file2.exists()) {
      return false;
    }
    
    if (!file1Exists)
    {
      return true;
    }
    
    if ((file1.isDirectory()) || (file2.isDirectory()))
    {
      throw new IOException("Can't compare directories, only files");
    }
    
    if (file1.getCanonicalFile().equals(file2.getCanonicalFile()))
    {
      return true;
    }
    
    Reader input1 = null;
    Reader input2 = null;
    try {
      if (charsetName == null)
      {
        input1 = new InputStreamReader(new FileInputStream(file1), Charset.defaultCharset());
        input2 = new InputStreamReader(new FileInputStream(file2), Charset.defaultCharset());
      } else {
        input1 = new InputStreamReader(new FileInputStream(file1), charsetName);
        input2 = new InputStreamReader(new FileInputStream(file2), charsetName);
      }
      return IOUtils.contentEqualsIgnoreEOL(input1, input2);
    }
    finally {
      IOUtils.closeQuietly(input1);
      IOUtils.closeQuietly(input2);
    }
  }
  














  public static File toFile(URL url)
  {
    if ((url == null) || (!"file".equalsIgnoreCase(url.getProtocol()))) {
      return null;
    }
    String filename = url.getFile().replace('/', File.separatorChar);
    filename = decodeUrl(filename);
    return new File(filename);
  }
  















  static String decodeUrl(String url)
  {
    String decoded = url;
    if ((url != null) && (url.indexOf('%') >= 0)) {
      int n = url.length();
      StringBuilder buffer = new StringBuilder();
      ByteBuffer bytes = ByteBuffer.allocate(n);
      for (int i = 0; i < n;) {
        if (url.charAt(i) == '%') {
          try {
            do {
              byte octet = (byte)Integer.parseInt(url.substring(i + 1, i + 3), 16);
              bytes.put(octet);
              i += 3;
            } while ((i < n) && (url.charAt(i) == '%'));
            




            if (bytes.position() <= 0) continue;
            bytes.flip();
            buffer.append(Charsets.UTF_8.decode(bytes).toString());
            bytes.clear(); continue;
          }
          catch (RuntimeException localRuntimeException) {}finally
          {
            if (bytes.position() > 0) {
              bytes.flip();
              buffer.append(Charsets.UTF_8.decode(bytes).toString());
              bytes.clear();
            }
          }
        } else
          buffer.append(url.charAt(i++));
      }
      decoded = buffer.toString();
    }
    return decoded;
  }
  


















  public static File[] toFiles(URL[] urls)
  {
    if ((urls == null) || (urls.length == 0)) {
      return EMPTY_FILE_ARRAY;
    }
    File[] files = new File[urls.length];
    for (int i = 0; i < urls.length; i++) {
      URL url = urls[i];
      if (url != null) {
        if (!url.getProtocol().equals("file")) {
          throw new IllegalArgumentException("URL could not be converted to a File: " + url);
        }
        
        files[i] = toFile(url);
      }
    }
    return files;
  }
  








  public static URL[] toURLs(File[] files)
    throws IOException
  {
    URL[] urls = new URL[files.length];
    
    for (int i = 0; i < urls.length; i++) {
      urls[i] = files[i].toURI().toURL();
    }
    
    return urls;
  }
  




















  public static void copyFileToDirectory(File srcFile, File destDir)
    throws IOException
  {
    copyFileToDirectory(srcFile, destDir, true);
  }
  


























  public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate)
    throws IOException
  {
    if (destDir == null) {
      throw new NullPointerException("Destination must not be null");
    }
    if ((destDir.exists()) && (!destDir.isDirectory())) {
      throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
    }
    File destFile = new File(destDir, srcFile.getName());
    copyFile(srcFile, destFile, preserveFileDate);
  }
  






















  public static void copyFile(File srcFile, File destFile)
    throws IOException
  {
    copyFile(srcFile, destFile, true);
  }
  


























  public static void copyFile(File srcFile, File destFile, boolean preserveFileDate)
    throws IOException
  {
    checkFileRequirements(srcFile, destFile);
    if (srcFile.isDirectory()) {
      throw new IOException("Source '" + srcFile + "' exists but is a directory");
    }
    if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
      throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
    }
    File parentFile = destFile.getParentFile();
    if ((parentFile != null) && 
      (!parentFile.mkdirs()) && (!parentFile.isDirectory())) {
      throw new IOException("Destination '" + parentFile + "' directory cannot be created");
    }
    
    if ((destFile.exists()) && (!destFile.canWrite())) {
      throw new IOException("Destination '" + destFile + "' exists but is read-only");
    }
    doCopyFile(srcFile, destFile, preserveFileDate);
  }
  











  public static long copyFile(File input, OutputStream output)
    throws IOException
  {
    FileInputStream fis = new FileInputStream(input);
    try {
      return IOUtils.copyLarge(fis, output);
    } finally {
      fis.close();
    }
  }
  
  /* Error */
  private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 14	java/io/File:exists	()Z
    //   4: ifeq +42 -> 46
    //   7: aload_1
    //   8: invokevirtual 15	java/io/File:isDirectory	()Z
    //   11: ifeq +35 -> 46
    //   14: new 16	java/io/IOException
    //   17: dup
    //   18: new 17	java/lang/StringBuilder
    //   21: dup
    //   22: invokespecial 18	java/lang/StringBuilder:<init>	()V
    //   25: ldc -117
    //   27: invokevirtual 20	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   30: aload_1
    //   31: invokevirtual 21	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   34: ldc 22
    //   36: invokevirtual 20	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   39: invokevirtual 23	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   42: invokespecial 24	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   45: athrow
    //   46: aconst_null
    //   47: astore_3
    //   48: aconst_null
    //   49: astore 4
    //   51: aconst_null
    //   52: astore 5
    //   54: aconst_null
    //   55: astore 6
    //   57: new 30	java/io/FileInputStream
    //   60: dup
    //   61: aload_0
    //   62: invokespecial 31	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   65: astore_3
    //   66: new 39	java/io/FileOutputStream
    //   69: dup
    //   70: aload_1
    //   71: invokespecial 153	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   74: astore 4
    //   76: aload_3
    //   77: invokevirtual 154	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   80: astore 5
    //   82: aload 4
    //   84: invokevirtual 155	java/io/FileOutputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   87: astore 6
    //   89: aload 5
    //   91: invokevirtual 156	java/nio/channels/FileChannel:size	()J
    //   94: lstore 7
    //   96: lconst_0
    //   97: lstore 9
    //   99: lconst_0
    //   100: lstore 11
    //   102: lload 9
    //   104: lload 7
    //   106: lcmp
    //   107: ifge +62 -> 169
    //   110: lload 7
    //   112: lload 9
    //   114: lsub
    //   115: lstore 13
    //   117: lload 13
    //   119: ldc2_w 157
    //   122: lcmp
    //   123: ifle +9 -> 132
    //   126: ldc2_w 157
    //   129: goto +5 -> 134
    //   132: lload 13
    //   134: lstore 11
    //   136: aload 6
    //   138: aload 5
    //   140: lload 9
    //   142: lload 11
    //   144: invokevirtual 159	java/nio/channels/FileChannel:transferFrom	(Ljava/nio/channels/ReadableByteChannel;JJ)J
    //   147: lstore 15
    //   149: lload 15
    //   151: lconst_0
    //   152: lcmp
    //   153: ifne +6 -> 159
    //   156: goto +13 -> 169
    //   159: lload 9
    //   161: lload 15
    //   163: ladd
    //   164: lstore 9
    //   166: goto -64 -> 102
    //   169: iconst_4
    //   170: anewarray 160	java/io/Closeable
    //   173: dup
    //   174: iconst_0
    //   175: aload 6
    //   177: aastore
    //   178: dup
    //   179: iconst_1
    //   180: aload 4
    //   182: aastore
    //   183: dup
    //   184: iconst_2
    //   185: aload 5
    //   187: aastore
    //   188: dup
    //   189: iconst_3
    //   190: aload_3
    //   191: aastore
    //   192: invokestatic 161	org/apache/commons/io/IOUtils:closeQuietly	([Ljava/io/Closeable;)V
    //   195: goto +34 -> 229
    //   198: astore 17
    //   200: iconst_4
    //   201: anewarray 160	java/io/Closeable
    //   204: dup
    //   205: iconst_0
    //   206: aload 6
    //   208: aastore
    //   209: dup
    //   210: iconst_1
    //   211: aload 4
    //   213: aastore
    //   214: dup
    //   215: iconst_2
    //   216: aload 5
    //   218: aastore
    //   219: dup
    //   220: iconst_3
    //   221: aload_3
    //   222: aastore
    //   223: invokestatic 161	org/apache/commons/io/IOUtils:closeQuietly	([Ljava/io/Closeable;)V
    //   226: aload 17
    //   228: athrow
    //   229: aload_0
    //   230: invokevirtual 97	java/io/File:length	()J
    //   233: lstore 7
    //   235: aload_1
    //   236: invokevirtual 97	java/io/File:length	()J
    //   239: lstore 9
    //   241: lload 7
    //   243: lload 9
    //   245: lcmp
    //   246: ifeq +59 -> 305
    //   249: new 16	java/io/IOException
    //   252: dup
    //   253: new 17	java/lang/StringBuilder
    //   256: dup
    //   257: invokespecial 18	java/lang/StringBuilder:<init>	()V
    //   260: ldc -94
    //   262: invokevirtual 20	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   265: aload_0
    //   266: invokevirtual 21	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   269: ldc -93
    //   271: invokevirtual 20	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   274: aload_1
    //   275: invokevirtual 21	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   278: ldc -92
    //   280: invokevirtual 20	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   283: lload 7
    //   285: invokevirtual 165	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   288: ldc -90
    //   290: invokevirtual 20	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   293: lload 9
    //   295: invokevirtual 165	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   298: invokevirtual 23	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   301: invokespecial 24	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   304: athrow
    //   305: iload_2
    //   306: ifeq +12 -> 318
    //   309: aload_1
    //   310: aload_0
    //   311: invokevirtual 167	java/io/File:lastModified	()J
    //   314: invokevirtual 63	java/io/File:setLastModified	(J)Z
    //   317: pop
    //   318: return
    // Line number table:
    //   Java source line #1132	-> byte code offset #0
    //   Java source line #1133	-> byte code offset #14
    //   Java source line #1136	-> byte code offset #46
    //   Java source line #1137	-> byte code offset #48
    //   Java source line #1138	-> byte code offset #51
    //   Java source line #1139	-> byte code offset #54
    //   Java source line #1141	-> byte code offset #57
    //   Java source line #1142	-> byte code offset #66
    //   Java source line #1143	-> byte code offset #76
    //   Java source line #1144	-> byte code offset #82
    //   Java source line #1145	-> byte code offset #89
    //   Java source line #1146	-> byte code offset #96
    //   Java source line #1147	-> byte code offset #99
    //   Java source line #1148	-> byte code offset #102
    //   Java source line #1149	-> byte code offset #110
    //   Java source line #1150	-> byte code offset #117
    //   Java source line #1151	-> byte code offset #136
    //   Java source line #1152	-> byte code offset #149
    //   Java source line #1153	-> byte code offset #156
    //   Java source line #1155	-> byte code offset #159
    //   Java source line #1156	-> byte code offset #166
    //   Java source line #1158	-> byte code offset #169
    //   Java source line #1159	-> byte code offset #195
    //   Java source line #1158	-> byte code offset #198
    //   Java source line #1161	-> byte code offset #229
    //   Java source line #1162	-> byte code offset #235
    //   Java source line #1163	-> byte code offset #241
    //   Java source line #1164	-> byte code offset #249
    //   Java source line #1167	-> byte code offset #305
    //   Java source line #1168	-> byte code offset #309
    //   Java source line #1170	-> byte code offset #318
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	319	0	srcFile	File
    //   0	319	1	destFile	File
    //   0	319	2	preserveFileDate	boolean
    //   47	175	3	fis	FileInputStream
    //   49	163	4	fos	FileOutputStream
    //   52	165	5	input	java.nio.channels.FileChannel
    //   55	152	6	output	java.nio.channels.FileChannel
    //   94	17	7	size	long
    //   233	51	7	srcLen	long
    //   97	68	9	pos	long
    //   239	55	9	dstLen	long
    //   100	43	11	count	long
    //   115	18	13	remain	long
    //   147	15	15	bytesCopied	long
    //   198	29	17	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   57	169	198	finally
    //   198	200	198	finally
  }
  
  public static void copyDirectoryToDirectory(File srcDir, File destDir)
    throws IOException
  {
    if (srcDir == null) {
      throw new NullPointerException("Source must not be null");
    }
    if ((srcDir.exists()) && (!srcDir.isDirectory())) {
      throw new IllegalArgumentException("Source '" + destDir + "' is not a directory");
    }
    if (destDir == null) {
      throw new NullPointerException("Destination must not be null");
    }
    if ((destDir.exists()) && (!destDir.isDirectory())) {
      throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
    }
    copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
  }
  






















  public static void copyDirectory(File srcDir, File destDir)
    throws IOException
  {
    copyDirectory(srcDir, destDir, true);
  }
  

























  public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate)
    throws IOException
  {
    copyDirectory(srcDir, destDir, null, preserveFileDate);
  }
  











































  public static void copyDirectory(File srcDir, File destDir, FileFilter filter)
    throws IOException
  {
    copyDirectory(srcDir, destDir, filter, true);
  }
  













































  public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate)
    throws IOException
  {
    checkFileRequirements(srcDir, destDir);
    if (!srcDir.isDirectory()) {
      throw new IOException("Source '" + srcDir + "' exists but is not a directory");
    }
    if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
      throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
    }
    

    List<String> exclusionList = null;
    if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
      File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
      if ((srcFiles != null) && (srcFiles.length > 0)) {
        exclusionList = new ArrayList(srcFiles.length);
        for (File srcFile : srcFiles) {
          File copiedFile = new File(destDir, srcFile.getName());
          exclusionList.add(copiedFile.getCanonicalPath());
        }
      }
    }
    doCopyDirectory(srcDir, destDir, filter, preserveFileDate, exclusionList);
  }
  




  private static void checkFileRequirements(File src, File dest)
    throws FileNotFoundException
  {
    if (src == null) {
      throw new NullPointerException("Source must not be null");
    }
    if (dest == null) {
      throw new NullPointerException("Destination must not be null");
    }
    if (!src.exists()) {
      throw new FileNotFoundException("Source '" + src + "' does not exist");
    }
  }
  












  private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate, List<String> exclusionList)
    throws IOException
  {
    File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
    if (srcFiles == null) {
      throw new IOException("Failed to list contents of " + srcDir);
    }
    if (destDir.exists()) {
      if (!destDir.isDirectory()) {
        throw new IOException("Destination '" + destDir + "' exists but is not a directory");
      }
    }
    else if ((!destDir.mkdirs()) && (!destDir.isDirectory())) {
      throw new IOException("Destination '" + destDir + "' directory cannot be created");
    }
    
    if (!destDir.canWrite()) {
      throw new IOException("Destination '" + destDir + "' cannot be written to");
    }
    for (File srcFile : srcFiles) {
      File dstFile = new File(destDir, srcFile.getName());
      if ((exclusionList == null) || (!exclusionList.contains(srcFile.getCanonicalPath()))) {
        if (srcFile.isDirectory()) {
          doCopyDirectory(srcFile, dstFile, filter, preserveFileDate, exclusionList);
        } else {
          doCopyFile(srcFile, dstFile, preserveFileDate);
        }
      }
    }
    

    if (preserveFileDate) {
      destDir.setLastModified(srcDir.lastModified());
    }
  }
  


















  public static void copyURLToFile(URL source, File destination)
    throws IOException
  {
    copyInputStreamToFile(source.openStream(), destination);
  }
  



















  public static void copyURLToFile(URL source, File destination, int connectionTimeout, int readTimeout)
    throws IOException
  {
    URLConnection connection = source.openConnection();
    connection.setConnectTimeout(connectionTimeout);
    connection.setReadTimeout(readTimeout);
    copyInputStreamToFile(connection.getInputStream(), destination);
  }
  














  public static void copyInputStreamToFile(InputStream source, File destination)
    throws IOException
  {
    try
    {
      copyToFile(source, destination);
    } finally {
      IOUtils.closeQuietly(source);
    }
  }
  















  public static void copyToFile(InputStream source, File destination)
    throws IOException
  {
    FileOutputStream output = openOutputStream(destination);
    try {
      IOUtils.copy(source, output);
      output.close();
    } finally {
      IOUtils.closeQuietly(output);
    }
  }
  






  public static void deleteDirectory(File directory)
    throws IOException
  {
    if (!directory.exists()) {
      return;
    }
    
    if (!isSymlink(directory)) {
      cleanDirectory(directory);
    }
    
    if (!directory.delete()) {
      String message = "Unable to delete directory " + directory + ".";
      
      throw new IOException(message);
    }
  }
  














  public static boolean deleteQuietly(File file)
  {
    if (file == null) {
      return false;
    }
    try {
      if (file.isDirectory()) {
        cleanDirectory(file);
      }
    }
    catch (Exception localException1) {}
    try
    {
      return file.delete();
    } catch (Exception ignored) {}
    return false;
  }
  























  public static boolean directoryContains(File directory, File child)
    throws IOException
  {
    if (directory == null) {
      throw new IllegalArgumentException("Directory must not be null");
    }
    
    if (!directory.isDirectory()) {
      throw new IllegalArgumentException("Not a directory: " + directory);
    }
    
    if (child == null) {
      return false;
    }
    
    if ((!directory.exists()) || (!child.exists())) {
      return false;
    }
    

    String canonicalParent = directory.getCanonicalPath();
    String canonicalChild = child.getCanonicalPath();
    
    return FilenameUtils.directoryContains(canonicalParent, canonicalChild);
  }
  





  public static void cleanDirectory(File directory)
    throws IOException
  {
    File[] files = verifiedListFiles(directory);
    
    IOException exception = null;
    for (File file : files) {
      try {
        forceDelete(file);
      } catch (IOException ioe) {
        exception = ioe;
      }
    }
    
    if (null != exception) {
      throw exception;
    }
  }
  




  private static File[] verifiedListFiles(File directory)
    throws IOException
  {
    if (!directory.exists()) {
      String message = directory + " does not exist";
      throw new IllegalArgumentException(message);
    }
    
    if (!directory.isDirectory()) {
      String message = directory + " is not a directory";
      throw new IllegalArgumentException(message);
    }
    
    File[] files = directory.listFiles();
    if (files == null) {
      throw new IOException("Failed to list contents of " + directory);
    }
    return files;
  }
  











  public static boolean waitFor(File file, int seconds)
  {
    long finishAt = System.currentTimeMillis() + seconds * 1000L;
    boolean wasInterrupted = false;
    try {
      while (!file.exists()) {
        long remaining = finishAt - System.currentTimeMillis();
        if (remaining < 0L) {
          return false;
        }
        try {
          Thread.sleep(Math.min(100L, remaining));
        } catch (InterruptedException ignore) {
          wasInterrupted = true;
        } catch (Exception ex) {
          break;
        }
      }
    } finally {
      if (wasInterrupted) {
        Thread.currentThread().interrupt();
      }
    }
    return true;
  }
  









  public static String readFileToString(File file, Charset encoding)
    throws IOException
  {
    InputStream in = null;
    try {
      in = openInputStream(file);
      return IOUtils.toString(in, Charsets.toCharset(encoding));
    } finally {
      IOUtils.closeQuietly(in);
    }
  }
  









  public static String readFileToString(File file, String encoding)
    throws IOException
  {
    return readFileToString(file, Charsets.toCharset(encoding));
  }
  









  @Deprecated
  public static String readFileToString(File file)
    throws IOException
  {
    return readFileToString(file, Charset.defaultCharset());
  }
  







  public static byte[] readFileToByteArray(File file)
    throws IOException
  {
    InputStream in = null;
    try {
      in = openInputStream(file);
      return IOUtils.toByteArray(in);
    } finally {
      IOUtils.closeQuietly(in);
    }
  }
  








  public static List<String> readLines(File file, Charset encoding)
    throws IOException
  {
    InputStream in = null;
    try {
      in = openInputStream(file);
      return IOUtils.readLines(in, Charsets.toCharset(encoding));
    } finally {
      IOUtils.closeQuietly(in);
    }
  }
  









  public static List<String> readLines(File file, String encoding)
    throws IOException
  {
    return readLines(file, Charsets.toCharset(encoding));
  }
  








  @Deprecated
  public static List<String> readLines(File file)
    throws IOException
  {
    return readLines(file, Charset.defaultCharset());
  }
  





























  public static LineIterator lineIterator(File file, String encoding)
    throws IOException
  {
    InputStream in = null;
    try {
      in = openInputStream(file);
      return IOUtils.lineIterator(in, encoding);
    } catch (IOException ex) {
      IOUtils.closeQuietly(in);
      throw ex;
    } catch (RuntimeException ex) {
      IOUtils.closeQuietly(in);
      throw ex;
    }
  }
  







  public static LineIterator lineIterator(File file)
    throws IOException
  {
    return lineIterator(file, null);
  }
  














  public static void writeStringToFile(File file, String data, Charset encoding)
    throws IOException
  {
    writeStringToFile(file, data, encoding, false);
  }
  










  public static void writeStringToFile(File file, String data, String encoding)
    throws IOException
  {
    writeStringToFile(file, data, encoding, false);
  }
  










  public static void writeStringToFile(File file, String data, Charset encoding, boolean append)
    throws IOException
  {
    OutputStream out = null;
    try {
      out = openOutputStream(file, append);
      IOUtils.write(data, out, encoding);
      out.close();
    } finally {
      IOUtils.closeQuietly(out);
    }
  }
  












  public static void writeStringToFile(File file, String data, String encoding, boolean append)
    throws IOException
  {
    writeStringToFile(file, data, Charsets.toCharset(encoding), append);
  }
  






  @Deprecated
  public static void writeStringToFile(File file, String data)
    throws IOException
  {
    writeStringToFile(file, data, Charset.defaultCharset(), false);
  }
  









  @Deprecated
  public static void writeStringToFile(File file, String data, boolean append)
    throws IOException
  {
    writeStringToFile(file, data, Charset.defaultCharset(), append);
  }
  







  @Deprecated
  public static void write(File file, CharSequence data)
    throws IOException
  {
    write(file, data, Charset.defaultCharset(), false);
  }
  









  @Deprecated
  public static void write(File file, CharSequence data, boolean append)
    throws IOException
  {
    write(file, data, Charset.defaultCharset(), append);
  }
  







  public static void write(File file, CharSequence data, Charset encoding)
    throws IOException
  {
    write(file, data, encoding, false);
  }
  








  public static void write(File file, CharSequence data, String encoding)
    throws IOException
  {
    write(file, data, encoding, false);
  }
  










  public static void write(File file, CharSequence data, Charset encoding, boolean append)
    throws IOException
  {
    String str = data == null ? null : data.toString();
    writeStringToFile(file, str, encoding, append);
  }
  












  public static void write(File file, CharSequence data, String encoding, boolean append)
    throws IOException
  {
    write(file, data, Charsets.toCharset(encoding), append);
  }
  









  public static void writeByteArrayToFile(File file, byte[] data)
    throws IOException
  {
    writeByteArrayToFile(file, data, false);
  }
  









  public static void writeByteArrayToFile(File file, byte[] data, boolean append)
    throws IOException
  {
    writeByteArrayToFile(file, data, 0, data.length, append);
  }
  











  public static void writeByteArrayToFile(File file, byte[] data, int off, int len)
    throws IOException
  {
    writeByteArrayToFile(file, data, off, len, false);
  }
  













  public static void writeByteArrayToFile(File file, byte[] data, int off, int len, boolean append)
    throws IOException
  {
    OutputStream out = null;
    try {
      out = openOutputStream(file, append);
      out.write(data, off, len);
      out.close();
    } finally {
      IOUtils.closeQuietly(out);
    }
  }
  














  public static void writeLines(File file, String encoding, Collection<?> lines)
    throws IOException
  {
    writeLines(file, encoding, lines, null, false);
  }
  













  public static void writeLines(File file, String encoding, Collection<?> lines, boolean append)
    throws IOException
  {
    writeLines(file, encoding, lines, null, append);
  }
  








  public static void writeLines(File file, Collection<?> lines)
    throws IOException
  {
    writeLines(file, null, lines, null, false);
  }
  










  public static void writeLines(File file, Collection<?> lines, boolean append)
    throws IOException
  {
    writeLines(file, null, lines, null, append);
  }
  















  public static void writeLines(File file, String encoding, Collection<?> lines, String lineEnding)
    throws IOException
  {
    writeLines(file, encoding, lines, lineEnding, false);
  }
  














  public static void writeLines(File file, String encoding, Collection<?> lines, String lineEnding, boolean append)
    throws IOException
  {
    FileOutputStream out = null;
    try {
      out = openOutputStream(file, append);
      BufferedOutputStream buffer = new BufferedOutputStream(out);
      IOUtils.writeLines(lines, lineEnding, buffer, encoding);
      buffer.flush();
      out.close();
    } finally {
      IOUtils.closeQuietly(out);
    }
  }
  










  public static void writeLines(File file, Collection<?> lines, String lineEnding)
    throws IOException
  {
    writeLines(file, null, lines, lineEnding, false);
  }
  












  public static void writeLines(File file, Collection<?> lines, String lineEnding, boolean append)
    throws IOException
  {
    writeLines(file, null, lines, lineEnding, append);
  }
  














  public static void forceDelete(File file)
    throws IOException
  {
    if (file.isDirectory()) {
      deleteDirectory(file);
    } else {
      boolean filePresent = file.exists();
      if (!file.delete()) {
        if (!filePresent) {
          throw new FileNotFoundException("File does not exist: " + file);
        }
        String message = "Unable to delete file: " + file;
        
        throw new IOException(message);
      }
    }
  }
  






  public static void forceDeleteOnExit(File file)
    throws IOException
  {
    if (file.isDirectory()) {
      deleteDirectoryOnExit(file);
    } else {
      file.deleteOnExit();
    }
  }
  





  private static void deleteDirectoryOnExit(File directory)
    throws IOException
  {
    if (!directory.exists()) {
      return;
    }
    
    directory.deleteOnExit();
    if (!isSymlink(directory)) {
      cleanDirectoryOnExit(directory);
    }
  }
  





  private static void cleanDirectoryOnExit(File directory)
    throws IOException
  {
    File[] files = verifiedListFiles(directory);
    
    IOException exception = null;
    for (File file : files) {
      try {
        forceDeleteOnExit(file);
      } catch (IOException ioe) {
        exception = ioe;
      }
    }
    
    if (null != exception) {
      throw exception;
    }
  }
  









  public static void forceMkdir(File directory)
    throws IOException
  {
    if (directory.exists()) {
      if (!directory.isDirectory()) {
        String message = "File " + directory + " exists and is " + "not a directory. Unable to create directory.";
        



        throw new IOException(message);
      }
    }
    else if (!directory.mkdirs())
    {

      if (!directory.isDirectory()) {
        String message = "Unable to create directory " + directory;
        
        throw new IOException(message);
      }
    }
  }
  








  public static void forceMkdirParent(File file)
    throws IOException
  {
    File parent = file.getParentFile();
    if (parent == null) {
      return;
    }
    forceMkdir(parent);
  }
  























  public static long sizeOf(File file)
  {
    if (!file.exists()) {
      String message = file + " does not exist";
      throw new IllegalArgumentException(message);
    }
    
    if (file.isDirectory()) {
      return sizeOfDirectory0(file);
    }
    return file.length();
  }
  




















  public static BigInteger sizeOfAsBigInteger(File file)
  {
    if (!file.exists()) {
      String message = file + " does not exist";
      throw new IllegalArgumentException(message);
    }
    
    if (file.isDirectory()) {
      return sizeOfDirectoryBig0(file);
    }
    return BigInteger.valueOf(file.length());
  }
  













  public static long sizeOfDirectory(File directory)
  {
    checkDirectory(directory);
    return sizeOfDirectory0(directory);
  }
  






  private static long sizeOfDirectory0(File directory)
  {
    File[] files = directory.listFiles();
    if (files == null) {
      return 0L;
    }
    long size = 0L;
    
    for (File file : files) {
      try {
        if (!isSymlink(file)) {
          size += sizeOf0(file);
          if (size < 0L) {
            break;
          }
        }
      }
      catch (IOException localIOException) {}
    }
    

    return size;
  }
  






  private static long sizeOf0(File file)
  {
    if (file.isDirectory()) {
      return sizeOfDirectory0(file);
    }
    return file.length();
  }
  








  public static BigInteger sizeOfDirectoryAsBigInteger(File directory)
  {
    checkDirectory(directory);
    return sizeOfDirectoryBig0(directory);
  }
  







  private static BigInteger sizeOfDirectoryBig0(File directory)
  {
    File[] files = directory.listFiles();
    if (files == null) {
      return BigInteger.ZERO;
    }
    BigInteger size = BigInteger.ZERO;
    
    for (File file : files) {
      try {
        if (!isSymlink(file)) {
          size = size.add(sizeOfBig0(file));
        }
      }
      catch (IOException localIOException) {}
    }
    

    return size;
  }
  






  private static BigInteger sizeOfBig0(File fileOrDir)
  {
    if (fileOrDir.isDirectory()) {
      return sizeOfDirectoryBig0(fileOrDir);
    }
    return BigInteger.valueOf(fileOrDir.length());
  }
  






  private static void checkDirectory(File directory)
  {
    if (!directory.exists()) {
      throw new IllegalArgumentException(directory + " does not exist");
    }
    if (!directory.isDirectory()) {
      throw new IllegalArgumentException(directory + " is not a directory");
    }
  }
  













  public static boolean isFileNewer(File file, File reference)
  {
    if (reference == null) {
      throw new IllegalArgumentException("No specified reference file");
    }
    if (!reference.exists()) {
      throw new IllegalArgumentException("The reference file '" + reference + "' doesn't exist");
    }
    
    return isFileNewer(file, reference.lastModified());
  }
  











  public static boolean isFileNewer(File file, Date date)
  {
    if (date == null) {
      throw new IllegalArgumentException("No specified date");
    }
    return isFileNewer(file, date.getTime());
  }
  











  public static boolean isFileNewer(File file, long timeMillis)
  {
    if (file == null) {
      throw new IllegalArgumentException("No specified file");
    }
    if (!file.exists()) {
      return false;
    }
    return file.lastModified() > timeMillis;
  }
  














  public static boolean isFileOlder(File file, File reference)
  {
    if (reference == null) {
      throw new IllegalArgumentException("No specified reference file");
    }
    if (!reference.exists()) {
      throw new IllegalArgumentException("The reference file '" + reference + "' doesn't exist");
    }
    
    return isFileOlder(file, reference.lastModified());
  }
  











  public static boolean isFileOlder(File file, Date date)
  {
    if (date == null) {
      throw new IllegalArgumentException("No specified date");
    }
    return isFileOlder(file, date.getTime());
  }
  











  public static boolean isFileOlder(File file, long timeMillis)
  {
    if (file == null) {
      throw new IllegalArgumentException("No specified file");
    }
    if (!file.exists()) {
      return false;
    }
    return file.lastModified() < timeMillis;
  }
  










  public static long checksumCRC32(File file)
    throws IOException
  {
    CRC32 crc = new CRC32();
    checksum(file, crc);
    return crc.getValue();
  }
  















  public static Checksum checksum(File file, Checksum checksum)
    throws IOException
  {
    if (file.isDirectory()) {
      throw new IllegalArgumentException("Checksums can't be computed on directories");
    }
    InputStream in = null;
    try {
      in = new CheckedInputStream(new FileInputStream(file), checksum);
      IOUtils.copy(in, new NullOutputStream());
    } finally {
      IOUtils.closeQuietly(in);
    }
    return checksum;
  }
  











  public static void moveDirectory(File srcDir, File destDir)
    throws IOException
  {
    if (srcDir == null) {
      throw new NullPointerException("Source must not be null");
    }
    if (destDir == null) {
      throw new NullPointerException("Destination must not be null");
    }
    if (!srcDir.exists()) {
      throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
    }
    if (!srcDir.isDirectory()) {
      throw new IOException("Source '" + srcDir + "' is not a directory");
    }
    if (destDir.exists()) {
      throw new FileExistsException("Destination '" + destDir + "' already exists");
    }
    boolean rename = srcDir.renameTo(destDir);
    if (!rename) {
      if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath() + File.separator)) {
        throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
      }
      copyDirectory(srcDir, destDir);
      deleteDirectory(srcDir);
      if (srcDir.exists()) {
        throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir + "'");
      }
    }
  }
  













  public static void moveDirectoryToDirectory(File src, File destDir, boolean createDestDir)
    throws IOException
  {
    if (src == null) {
      throw new NullPointerException("Source must not be null");
    }
    if (destDir == null) {
      throw new NullPointerException("Destination directory must not be null");
    }
    if ((!destDir.exists()) && (createDestDir)) {
      destDir.mkdirs();
    }
    if (!destDir.exists()) {
      throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "]");
    }
    
    if (!destDir.isDirectory()) {
      throw new IOException("Destination '" + destDir + "' is not a directory");
    }
    moveDirectory(src, new File(destDir, src.getName()));
  }
  












  public static void moveFile(File srcFile, File destFile)
    throws IOException
  {
    if (srcFile == null) {
      throw new NullPointerException("Source must not be null");
    }
    if (destFile == null) {
      throw new NullPointerException("Destination must not be null");
    }
    if (!srcFile.exists()) {
      throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
    }
    if (srcFile.isDirectory()) {
      throw new IOException("Source '" + srcFile + "' is a directory");
    }
    if (destFile.exists()) {
      throw new FileExistsException("Destination '" + destFile + "' already exists");
    }
    if (destFile.isDirectory()) {
      throw new IOException("Destination '" + destFile + "' is a directory");
    }
    boolean rename = srcFile.renameTo(destFile);
    if (!rename) {
      copyFile(srcFile, destFile);
      if (!srcFile.delete()) {
        deleteQuietly(destFile);
        throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
      }
    }
  }
  













  public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir)
    throws IOException
  {
    if (srcFile == null) {
      throw new NullPointerException("Source must not be null");
    }
    if (destDir == null) {
      throw new NullPointerException("Destination directory must not be null");
    }
    if ((!destDir.exists()) && (createDestDir)) {
      destDir.mkdirs();
    }
    if (!destDir.exists()) {
      throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "]");
    }
    
    if (!destDir.isDirectory()) {
      throw new IOException("Destination '" + destDir + "' is not a directory");
    }
    moveFile(srcFile, new File(destDir, srcFile.getName()));
  }
  














  public static void moveToDirectory(File src, File destDir, boolean createDestDir)
    throws IOException
  {
    if (src == null) {
      throw new NullPointerException("Source must not be null");
    }
    if (destDir == null) {
      throw new NullPointerException("Destination must not be null");
    }
    if (!src.exists()) {
      throw new FileNotFoundException("Source '" + src + "' does not exist");
    }
    if (src.isDirectory()) {
      moveDirectoryToDirectory(src, destDir, createDestDir);
    } else {
      moveFileToDirectory(src, destDir, createDestDir);
    }
  }
  

















  public static boolean isSymlink(File file)
    throws IOException
  {
    if (Java7Support.isAtLeastJava7())
    {
      return Java7Support.isSymLink(file);
    }
    
    if (file == null) {
      throw new NullPointerException("File must not be null");
    }
    if (FilenameUtils.isSystemWindows()) {
      return false;
    }
    File fileInCanonicalDir = null;
    if (file.getParent() == null) {
      fileInCanonicalDir = file;
    } else {
      File canonicalDir = file.getParentFile().getCanonicalFile();
      fileInCanonicalDir = new File(canonicalDir, file.getName());
    }
    
    if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
      return isBrokenSymlink(file);
    }
    return true;
  }
  








  private static boolean isBrokenSymlink(File file)
    throws IOException
  {
    if (file.exists()) {
      return false;
    }
    
    File canon = file.getCanonicalFile();
    File parentDir = canon.getParentFile();
    if ((parentDir == null) || (!parentDir.exists())) {
      return false;
    }
    


    File[] fileInDir = parentDir.listFiles(new FileFilter()
    {
      public boolean accept(File aFile) {
        return aFile.equals(val$canon);
      }
      
    });
    return (fileInDir != null) && (fileInDir.length > 0);
  }
}
