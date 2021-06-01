import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MoveRootFoldersMain {
  public static final String PATH_NAME = "/Volumes/RECUPERADO";
  public static final String DESTINY_PATH_NAME = "/Volumes/RECUPERADO/extensions";

  public static void main(String[] arg) {
    long startNano = System.nanoTime();
    long countRootFolders = 0L;
    long countRootFoldersEmpty = 0L;
    long countInternalFolders = 0;
    long countTotalFiles = 0L;
    long countCorrectFiles = 0L;
    long countExistFiles = 0L;
    long countIncorrectFiles = 0L;
    File root = new File(PATH_NAME);
    System.out.print("Listing root files..");
    long msToListNano = System.nanoTime();
    long msToListMillis = System.currentTimeMillis();
    System.out.println((msToListNano - startNano) + " ns");
    System.out.println("\nListing rest files..");
    int x = 0;
    String anim = "|/-\\";
    try {
      if (null != root && root.exists()) {
        File[] rootFolders = root.listFiles();
        for (File rootFolder : rootFolders) {
          if (rootFolder.isDirectory() && !"extensions".equals(rootFolder.getName())) {
            File[] files = rootFolder.listFiles();
            if (files.length == 0) {
              countRootFoldersEmpty++;
              rootFolder.delete();
            } else {
              for (File archive : files) {
                if (archive.isDirectory()) {
                  countInternalFolders++;
                } else {
                  countTotalFiles++;
                  String extension = calculateExtension(archive.getName());
                  String destinyFolderPath = calculateDestinyPathFolder(extension);
                  boolean existDestinyFolder = existDestinyPathFolder(destinyFolderPath);
                  if (!existDestinyFolder) {
                    if (!createDestinyPathFolder(destinyFolderPath)) {
                      throw new Exception("No se ha podido crear path " + destinyFolderPath);
                    }
                  }
                  String destinyFileName = archive.getName();
                  String destinyFilePath = destinyFolderPath + "/" + destinyFileName;
                  if (!exists(destinyFilePath)) {
                    boolean correcto = moveFile(archive.getPath(), destinyFilePath);
                    if (correcto) {
                      countCorrectFiles++;
                    } else {
                      countIncorrectFiles++;
                    }
                  } else {
                    countExistFiles++;
                    boolean existDestinyFolder2 = existDestinyPathFolder(PATH_NAME + "/extensions/" + "a1/" + extension);
                    if (!existDestinyFolder2) {
                      if (!createDestinyPathFolder(PATH_NAME + "/extensions/" + "a1/" + extension)) {
                        throw new Exception("No se ha podido crear path " + destinyFolderPath);
                      }
                    }
                    boolean correcto = moveFile(archive.getPath(), PATH_NAME + "/extensions/" + "a1/" + extension + "/" + destinyFileName);
                    if (correcto) {
                      countCorrectFiles++;
                    } else {
                      countIncorrectFiles++;
                    }
                  }
                }
                String data = "\r" + anim.charAt(x % 4) + " " + countRootFolders;
                data += " folders " + countTotalFiles + " files " + countCorrectFiles + " oks " + countIncorrectFiles
                    + " kos " + countExistFiles + " exists ";
                try {
                  System.out.write(data.getBytes());
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }
            countRootFolders++;
            x++;
          }
        }
        long msToEndMillis = System.currentTimeMillis();
        System.out.println("\nlistRestFiles: " + (msToEndMillis - msToListMillis) / 1000 / 60);
        System.out.println(countRootFolders + " folders");
        System.out.println(countTotalFiles + " files");
        System.out.println(countRootFoldersEmpty + " empty folders");
        System.out.println(countInternalFolders + " internal folders");
        System.out.println(countCorrectFiles + " correct files");
        System.out.println(countIncorrectFiles + " incorrect files");
        System.out.println(countExistFiles + " exist files");
      } else {
        System.out.println("Ruta no encontrada " + PATH_NAME);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      System.out.println("Fin del proceso.");
    }
  }

  private static boolean existDestinyPathFolder(String destinyFolderPath) {
    File destinyFolder = new File(destinyFolderPath);
    return destinyFolder.exists();
  }

  private static boolean createDestinyPathFolder(String destinyFolderPath) throws Exception {
    File destinyFolder = new File(destinyFolderPath);
    boolean retorno = false;
    try {
      retorno = destinyFolder.mkdir();
    } catch (SecurityException e) {
      throw new Exception("Security exception");
    }
    return retorno;
  }

  private static String calculateExtension(String name) {
    String extension = name.contains(".") ? name.split("\\.")[1] : "";
    return extension;
  }

  private static String calculateDestinyPathFolder(String extension) {
    String retorno = "";
    if (null != extension && !"".equals(extension)) {
      retorno = DESTINY_PATH_NAME + "/" + extension;
    } else {
      retorno = DESTINY_PATH_NAME + "/" + "sin_extension";
    }
    return retorno;
  }

  private static boolean exists(String destinyPathFile) {
    return new File(destinyPathFile).exists();
  }

  public static boolean moveFile(String fromFile, String toFile) {
    boolean movido = false;
    File origin = new File(fromFile);
    File destination = new File(toFile);
    try {
      if (origin.exists()) {
        if (destination.createNewFile()) {
          InputStream in = null;
          OutputStream out = null;
          try {
            in = new FileInputStream(origin);
            out = new FileOutputStream(destination);
            long fileLength = origin.length();
            byte[] buf = new byte[8192];
            int len;
            if (fileLength < 8193) {
              while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
              }
              movido = origin.delete();
            }
          } catch (IOException ioe) {
            ioe.printStackTrace();
            movido = false;
          } finally {
            if (null != in) {
              in.close();
            }
            if (null != in) {
              out.close();
            }
          }
        } else {
          movido = origin.delete();
        }
      } else {
        movido = false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return movido;
  }

}