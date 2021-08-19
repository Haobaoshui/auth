package auth.webserver.utility.compress.tar;

import auth.webserver.model.zip.ProjectFile;
import auth.webserver.utility.ProjectFileUtils;
import auth.webserver.utility.GUID;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * tar文件解压与压缩
 */
public class TarUtils {
    private static final String BASE_DIR = "";

    private static final int BUFFER = 1024;

    private static final String EXT = ".tar";

    /**
     * 压缩
     *
     * @param srcPath
     * @param destPath
     * @throws Exception
     */
    public static void archive(String srcPath, String destPath)
            throws Exception {

        File srcFile = new File(srcPath);

        TarUtils.archive(srcFile, destPath);

    }

    /**
     * 压缩
     */
    private static void archive(File srcFile, File destFile) throws Exception {

        TarArchiveOutputStream taos = new TarArchiveOutputStream(
                new FileOutputStream(destFile));

        TarUtils.archive(srcFile, taos, TarUtils.BASE_DIR);

        taos.flush();
        taos.close();
    }

    /**
     * 压缩
     *
     * @param srcFile
     * @throws Exception
     */
    private static void archive(File srcFile) throws Exception {
        String name = srcFile.getName();
        String basePath = srcFile.getParent();
        String destPath = basePath + File.separator + name + TarUtils.EXT;
        TarUtils.archive(srcFile, destPath);
    }

    /**
     * 压缩文件
     *
     * @param srcFile
     * @param destPath
     * @throws Exception
     */
    private static void archive(File srcFile, String destPath) throws Exception {
        TarUtils.archive(srcFile, new File(destPath));
    }

    /**
     * 归档
     *
     * @param srcPath
     * @throws Exception
     */
    public static void archive(String srcPath) throws Exception {
        File srcFile = new File(srcPath);

        TarUtils.archive(srcFile);
    }

    /**
     * 压缩
     *
     * @param srcFile  源路径
     * @param taos     TarArchiveOutputStream
     * @param basePath 归档包内相对路径
     * @throws Exception
     */
    private static void archive(File srcFile, TarArchiveOutputStream taos,
                                String basePath) throws Exception {
        if (srcFile.isDirectory()) TarUtils.archiveDir(srcFile, taos, basePath);
        else
            TarUtils.archiveFile(srcFile, taos, basePath);
    }

    /**
     * 目录压缩
     *
     * @param dir
     * @param taos     TarArchiveOutputStream
     * @param basePath
     * @throws Exception
     */
    private static void archiveDir(File dir, TarArchiveOutputStream taos,
                                   String basePath) throws Exception {

        File[] files = dir.listFiles();

        if (files.length < 1) {
            TarArchiveEntry entry = new TarArchiveEntry(basePath
                    + dir.getName() + File.separator);

            taos.putArchiveEntry(entry);
            taos.closeArchiveEntry();
        }

        // 递归归档
        for (File file : files) TarUtils.archive(file, taos, basePath + dir.getName() + File.separator);
    }

    /**
     * 数据压缩
     */
    private static void archiveFile(File file, TarArchiveOutputStream taos,
                                    String dir) throws Exception {

        /**
         * 归档内文件名定义
         *
         * <pre>
         * 如果有多级目录，那么这里就需要给出包含目录的文件名
         * 如果用WinRAR打开归档包，中文名将显示为乱码
         * </pre>
         */
        TarArchiveEntry entry = new TarArchiveEntry(dir + file.getName());

        entry.setSize(file.length());

        taos.putArchiveEntry(entry);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));
        int count;
        byte[] data = new byte[TarUtils.BUFFER];
        while ((count = bis.read(data, 0, TarUtils.BUFFER)) != -1) taos.write(data, 0, count);

        bis.close();

        taos.closeArchiveEntry();
    }


    /**
     * 解压缩
     *
     * @param srcFile
     * @param destFile
     * @return
     * @throws Exception
     */
    private static List<ProjectFile> unTar(File srcFile, File destFile) throws Exception {
        TarArchiveInputStream tais = new TarArchiveInputStream(new FileInputStream(srcFile));

        List<ProjectFile> projectFileList = new ArrayList<>();

        TarArchiveEntry entry = null;
        while ((entry = tais.getNextTarEntry()) != null) {
            //目标文件名
            String fileext = ProjectFileUtils.getExtensionNameWithSeparator(entry.getName()) + "";
            String diskFileName = GUID.getGUID() + fileext;
            String dir = destFile.getPath() + File.separator + diskFileName;
            File dirFile = new File(dir);
            // 文件检查
            ProjectFileUtils.mkDir(dirFile);
            if (entry.isDirectory()) dirFile.mkdirs();
            else TarUtils.dearchiveFile(dirFile, tais);

            ProjectFile projectFile = new ProjectFile();


            projectFile.setName(entry.getName());
            projectFile.setDiskFileName(diskFileName);
            projectFileList.add(projectFile);

        }
        tais.close();
        return projectFileList;
    }

    /**
     * 文件 解压缩
     *
     * @param srcFilePath 源文件路径，加压到同名文件夹下
     * @throws Exception
     */
    public static List<ProjectFile> unTar(String srcFilePath) throws Exception {
        if (srcFilePath == null) throw new Exception("文件名为空");
        String destPath = ProjectFileUtils.getFileNameNoEx(srcFilePath);


        return TarUtils.unTar(srcFilePath, destPath);
    }

    /**
     * 文件 解压缩
     *
     * @param srcPath  源文件路径
     * @param destPath 目标文件路径
     * @throws Exception
     */
    private static List<ProjectFile> unTar(String srcPath, String destPath)
            throws Exception {
        File srcFile = new File(srcPath);


        if (!srcFile.exists()) throw new Exception(srcFile.getPath() + "所指文件不存在");


        File parentFile = new File(destPath);
        parentFile.mkdirs();

        return TarUtils.unTar(srcFile, parentFile);
    }

    /**
     * 文件解压缩
     *
     * @param destFile 目标文件
     * @param tais     TarArchiveInputStream
     * @throws Exception
     */
    private static void dearchiveFile(File destFile, TarArchiveInputStream tais)
            throws Exception {

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));

        int count;
        byte[] data = new byte[TarUtils.BUFFER];
        while ((count = tais.read(data, 0, TarUtils.BUFFER)) != -1) bos.write(data, 0, count);

        bos.close();
    }


}


