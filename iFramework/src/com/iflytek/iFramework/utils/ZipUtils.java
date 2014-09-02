package com.iflytek.iFramework.utils;


import java.io.InputStream;

/**
* Android Zip压缩解压缩
* 
*/
public final class ZipUtils {
  private ZipUtils(){
  }

  /**
   * 取得压缩包中的 文件列表(文件夹,文件自选)
   * @param zipFileString                压缩包名字
   * @param bContainFolder        是否包括 文件夹
   * @param bContainFile                是否包括 文件
   * @return
   * @throws Exception
   */
  public static java.util.List<java.io.File> getFileList(String zipFileString, boolean bContainFolder, 
          boolean bContainFile)throws Exception {
    java.util.List<java.io.File> fileList = new java.util.ArrayList<java.io.File>();
    java.util.zip.ZipInputStream inZip = 
                     new java.util.zip.ZipInputStream(new java.io.FileInputStream(zipFileString));
    java.util.zip.ZipEntry zipEntry;
    String szName = "";                
    while ((zipEntry = inZip.getNextEntry()) != null) {
        szName = zipEntry.getName();
        if (zipEntry.isDirectory()) {
          // get the folder name of the widget
          szName = szName.substring(0, szName.length() - 1);
          java.io.File folder = new java.io.File(szName);
          if (bContainFolder) {
            fileList.add(folder);
          }
        } else {
          java.io.File file = new java.io.File(szName);
          if (bContainFile) {
            fileList.add(file);
          }
        }
    }//end of while                
    inZip.close();
    return fileList;
  }

  /**
   * 返回压缩包中的文件InputStream
   * 
   * @param zipFilePath                压缩文件的名字
   * @param fileString        解压文件的名字
   * @return InputStream
   * @throws Exception
   */
public static InputStream upZip(String zipFilePath, String fileString)throws Exception {
        java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(zipFilePath);
        java.util.zip.ZipEntry zipEntry = zipFile.getEntry(fileString);

        return zipFile.getInputStream(zipEntry);
}

/**
* 解压一个压缩文档 到指定位置
* @param zipFileString        压缩包的名字
* @param outPathString        指定的路径
* @throws Exception
*/
public static void unZipFolder(InputStream input, String outPathString)throws Exception {
        java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(input);
        java.util.zip.ZipEntry zipEntry = null;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();

                if (zipEntry.isDirectory()) {
                  // get the folder name of the widget
                  szName = szName.substring(0, szName.length() - 1);
                  java.io.File folder = new java.io.File(outPathString + java.io.File.separator + szName);
                  folder.mkdirs();
                } else {
                  java.io.File file = new java.io.File(outPathString + java.io.File.separator + szName);
                  file.createNewFile();
                  // get the output stream of the file
                  java.io.FileOutputStream out = new java.io.FileOutputStream(file);
                  int len;
                  byte[] buffer = new byte[1024];
                  // read (len) bytes into buffer
                  while ((len = inZip.read(buffer)) != -1) {
                        // write (len) byte from buffer at the position 0
                        out.write(buffer, 0, len);
                        out.flush();
                  }
                  out.close();
                }
        }//end of while
                inZip.close();
        }

        /**
         * 解压一个压缩文档 到指定位置
         * @param zipFileString        压缩包的名字
         * @param outPathString        指定的路径
         * @throws Exception
         */
        public static void unZipFolder(String zipFileString, String outPathString)throws Exception {
                unZipFolder(new java.io.FileInputStream(zipFileString),outPathString);
        }//end of func


        /**
         * 压缩文件,文件夹
         * 
         * @param srcFilePath        要压缩的文件/文件夹名字
         * @param zipFilePath        指定压缩的目的和名字
         * @throws Exception
         */
        public static void zipFolder(String srcFilePath, String zipFilePath)throws Exception {
          //创建Zip包
          java.util.zip.ZipOutputStream outZip = 
              new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFilePath));

          //打开要输出的文件
          java.io.File file = new java.io.File(srcFilePath);

          //压缩
          zipFiles(file.getParent()+java.io.File.separator, file.getName(), outZip);

          //完成,关闭
          outZip.finish();
          outZip.close();

        }//end of func

        /**
         * 压缩文件
         * @param folderPath
         * @param filePath
         * @param zipOut
         * @throws Exception
         */
        private static void zipFiles(String folderPath, String filePath, 
                     java.util.zip.ZipOutputStream zipOut)throws Exception{
          if(zipOut == null){
            return;
          }

          java.io.File file = new java.io.File(folderPath+filePath);

          //判断是不是文件
          if (file.isFile()) {
            java.util.zip.ZipEntry zipEntry =  new java.util.zip.ZipEntry(filePath);
            java.io.FileInputStream inputStream = new java.io.FileInputStream(file);
            zipOut.putNextEntry(zipEntry);

            int len;
            byte[] buffer = new byte[4096];

            while((len=inputStream.read(buffer)) != -1) {
                 zipOut.write(buffer, 0, len);
            }

             zipOut.closeEntry();
          } else {
           //文件夹的方式,获取文件夹下的子文件
           String fileList[] = file.list();

           //如果没有子文件, 则添加进去即可
           if (fileList.length <= 0) {
                  java.util.zip.ZipEntry zipEntry =  
                       new java.util.zip.ZipEntry(filePath+java.io.File.separator);
                zipOut.putNextEntry(zipEntry);
                zipOut.closeEntry();                                
           }

           //如果有子文件, 遍历子文件
           for (int i = 0; i < fileList.length; i++) {
                zipFiles(folderPath, filePath+java.io.File.separator+fileList[i], zipOut);
           }//end of for

         }//end of if

     }//end of func
}
