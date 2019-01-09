package cn.ac.iie.di.dpp.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;

/**
 * @author Fighter Created on 2018/10/12.
 */
public class UnCompressUtils {
    public static final int BUFFER_SIZE = 1024;

    /**
     * 解压 tar 文件
     *
     * @param tarFile tar 压缩文件
     * @param destDir tar 压缩文件解压后保存的目录
     * @return 返回 tar 压缩文件里的文件名的 list
     * @throws Exception
     */
    public static void unTar(File tarFile, String destDir) throws Exception {
        // 如果 destDir 为 null, 空字符串, 或者全是空格, 则解压到压缩文件所在目录
        if (StringUtils.isBlank(destDir)) {
            destDir = tarFile.getParent();
        }

        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        //先创建父目录，防止解压时先遇到文件而无法复制文件
        new File(destDir).mkdirs();
        TarArchiveInputStream is = null;
//        List<String> fileNames = new ArrayList<String>();

        try {
            is = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(tarFile), BUFFER_SIZE));
            TarArchiveEntry entry = null;

            while ((entry = is.getNextTarEntry()) != null) {
//                fileNames.add(entry.getName());

                if (entry.isDirectory()) {
                    File directory = new File(destDir, entry.getName());
                    directory.mkdirs();
                } else {
                    OutputStream os = null;
                    try {
                        //父目录destDir必须存在，否则os为null
                        os = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())), BUFFER_SIZE);
                        IOUtils.copy(is, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
//        return fileNames;
    }

    /**
     * 解压 tar 文件
     *
     * @param tarfile tar 压缩文件的路径
     * @param destDir tar 压缩文件解压后保存的目录
     * @return 返回 tar 压缩文件里的文件名的 list
     * @throws Exception
     *//*
    public static List<String> unTar(String tarfile, String destDir) throws Exception {
        File tarFile = new File(tarfile);
        return unTar(tarFile, destDir);
    }*/
}
