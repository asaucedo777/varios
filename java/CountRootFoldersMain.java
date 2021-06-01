import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CountRootFoldersMain {
  public  static final String PATH_NAME = "/Volumes/RECUPERADO";
  public static void main(String[] arg) {
    long startNano = System.nanoTime();
    long startMillis = System.currentTimeMillis();
    long countRootFolders = 0L;
    long countFiles = 0L;
    File root = new File(PATH_NAME);
    System.out.print("Listing root files..");
    long msToListNano = System.nanoTime();
    long msToListMillis = System.currentTimeMillis();
    System.out.println((msToListNano - startNano) + " ns");
    System.out.println("\nListing rest files..");
    int x = 0;
    String anim = "|/-\\";
    File[] rootFolders = root.listFiles();
    for (File rootFolder : rootFolders) {
      File[] files = rootFolder.listFiles();
      for (File archive : files) {
        countFiles++;
      }
      countRootFolders++;
      x++;
      String data = "\r" + anim.charAt(x % 4) + " " + countRootFolders;
      data += " folders " + countFiles + " files";
      try {
        System.out.write(data.getBytes());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    long msToEndMillis = System.currentTimeMillis();
    System.out.println("\nlistRestFiles: " + (msToEndMillis - msToListMillis) / 1000 / 60 + " minutos.");
    System.out.println(countRootFolders + " root folders");
    System.out.println(countFiles + " files");
  }

  public boolean moveFile(String fromFile, String toFile) {
    File origin = new File(fromFile);
    File destination = new File(toFile);
    if (origin.exists()) {
      try {
        InputStream in = new FileInputStream(origin);
        OutputStream out = new FileOutputStream(destination);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
        in.close();
        out.close();
        return origin.delete();
      } catch (IOException ioe) {
        ioe.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

}