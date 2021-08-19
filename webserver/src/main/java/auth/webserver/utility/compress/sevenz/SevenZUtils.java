package auth.webserver.utility.compress.sevenz;

import auth.webserver.model.zip.ProjectFile;
import auth.webserver.utility.DebugDump;
import auth.webserver.utility.ProjectFileUtils;
import auth.webserver.utility.GUID;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * tar文件解压与压缩
 */
public class SevenZUtils {


    /**
     * @param name 压缩文件名，可以写为null保持默认
     */
    //递归压缩
    private static void compress(SevenZOutputFile out, File input, String name) throws IOException {
        if (name == null) name = input.getName();
        SevenZArchiveEntry entry = null;
        //如果路径为目录（文件夹）
        if (input.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = input.listFiles();

            if (flist.length == 0)//如果文件夹为空，则只需在目的地.7z文件中写入一个目录进入
            {
                entry = out.createArchiveEntry(input, name + "/");
                out.putArchiveEntry(entry);
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                for (int i = 0; i < flist.length; i++)
                    SevenZUtils.compress(out, flist[i], name + "/" + flist[i].getName());
        } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入7z文件中
        {
            FileInputStream fos = new FileInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(fos);
            entry = out.createArchiveEntry(input, name);
            out.putArchiveEntry(entry);
            int len = -1;
            //将源文件写入到7z文件中
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) out.write(buf, 0, len);
            bis.close();
            fos.close();
            out.closeArchiveEntry();
        }
    }

    /**
     * 7z文件压缩
     *
     * @param inputFile  待压缩文件夹/文件名
     * @param outputFile 生成的压缩包名字
     */

    public static void Compress7z(String inputFile, String outputFile) throws Exception {
        File input = new File(inputFile);
        if (!input.exists()) throw new Exception(input.getPath() + "待压缩文件不存在");
        SevenZOutputFile out = new SevenZOutputFile(new File(outputFile));

        SevenZUtils.compress(out, input, null);
        out.close();
    }


    /**
     * 7z文件解压缩
     *
     * @param srcFile
     * @param destDirFile
     * @throws Exception
     */
    private static List<ProjectFile> un7z(File srcFile, File destDirFile) throws Exception {
        if (srcFile == null) throw new Exception(srcFile.getPath() + "所指文件不存在");
        if (!srcFile.isFile()) throw new Exception(srcFile.getPath() + "所指文件不是文件");

        if (destDirFile == null) throw new Exception(destDirFile.getPath() + "目标文件夹为空");
        if (!destDirFile.isDirectory()) throw new Exception(destDirFile.getPath() + "目标文件夹为空");

        List<ProjectFile> projectFileList = new ArrayList<>();

        //开始解压
        SevenZFile zIn = new SevenZFile(srcFile);
        SevenZArchiveEntry entry = null;
        File file = null;
        while ((entry = zIn.getNextEntry()) != null) if (!entry.isDirectory()) {

            ProjectFile projectFile = new ProjectFile();
            String fileext = ProjectFileUtils.getExtensionNameWithSeparator(entry.getName()) + "";
            String diskFileName = GUID.getGUID() + fileext;
            projectFile.setName(entry.getName());
            projectFile.setDiskFileName(diskFileName);


            file = new File(destDirFile, diskFileName);
            if (!file.exists()) new File(file.getParent()).mkdirs();//创建此文件的上级目录
            OutputStream out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = zIn.read(buf)) != -1) bos.write(buf, 0, len);
            // 关流顺序，先打开的后关闭
            bos.close();
            out.close();

            projectFileList.add(projectFile);
        }


        return projectFileList;
    }

    private static List<ProjectFile> un7z(String seven7ZFileName, String destDirPath) throws Exception {
        if (seven7ZFileName == null) throw new Exception("文件名为空");
        if (!ProjectFileUtils.getExtensionName(seven7ZFileName).toLowerCase().equals("7z"))
            throw new Exception("文件扩展名错误");

        File srcFile = new File(seven7ZFileName);//获取当前压缩文件
        if (!srcFile.exists()) throw new Exception(srcFile.getPath() + "所指文件不存在");


        File parentFile = new File(destDirPath);
        parentFile.mkdirs();

        return SevenZUtils.un7z(srcFile, parentFile);
    }

    public static List<ProjectFile> un7z(String seven7ZFileName) throws Exception {
        DebugDump.printDebugInfo(seven7ZFileName);

        if (seven7ZFileName == null) throw new Exception("文件名为空");
        String destPath = ProjectFileUtils.getFileNameNoEx(seven7ZFileName);
        DebugDump.printDebugInfo(destPath);

        return SevenZUtils.un7z(seven7ZFileName, destPath);
    }


}
