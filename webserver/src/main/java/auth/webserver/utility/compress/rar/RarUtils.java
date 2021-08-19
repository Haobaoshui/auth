package auth.webserver.utility.compress.rar;

import auth.webserver.utility.GUID;
import auth.webserver.model.zip.ProjectFile;
import auth.webserver.utility.ProjectFileUtils;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 解压和压缩rar文件
 * 使用junrar无法实现winrar5.0及其以上版本的解压问题：WinRAR5之后，在rar格式的基础上，推出了另一种rar，叫RAR5，winrar官方并没有开源算法，jar包无法解析这种格式
 * 因此，这里使用7z.exe进行解压缩，也可以使用winrar.exe进行解压缩
 */
public class RarUtils {
    /**
     * 通过配置文件名读取内容
     *
     * @param fileName
     * @return
     */
    private static Properties readPropertiesFile(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            return props;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用7z程序进行解压缩
     * 在WinRAR5之后，rar推出了RAR5，而java-unrar解析不了rar5格式，因此采用7z.exe解压缩
     * 也可以使用winrar.exe进行解压缩
     * <p>
     * 解压缩的命令如下：C:\01_MyApp\7-Zip\7z.exe x newPack.zip -oc:\Doc -aoa
     * <p>
     * 其中，“C:\01_MyApp\7-Zip\7z.exe”为7z.exe可执行文件的完整路径;
     * “x”选项表示解压缩，并且使得压缩包内的文件所在的目录结构保持不变。如果希望解压缩后所有的文件都存放在同一个目录下，则使用 e 这个命令。
     * “newPack.zip”表示压缩包的文件名。该压缩包是存放在当前目录下的。
     * “-oc:\Doc”表示把压缩包内的文件解压缩到 c:\Doc 目录下。-o 这个参数用于指定输出目录。
     * “-aoa”表示直接覆盖现有文件，而没有任何提示。
     * 类似的参数还有：
     * -aos 跳过现有文件，其不会被覆盖。
     * -aou 如果相同文件名的文件以存在，将自动重命名被释放的文件。举个例子，文件 file.txt 将被自动重命名为 file_1.txt。
     * -aot 如果相同文件名的文件以存在，将自动重命名现有的文件。举个例子，文件 file.txt 将被自动重命名为 file_1.txt。
     *
     * @param rarFileName
     * @return
     * @throws Exception
     */
    private static List<ProjectFile> unRarBy7Z(String rarFileName) throws Exception {

        String outFilePath = ProjectFileUtils.getFileNameNoEx(rarFileName);


        Properties properties = RarUtils.readPropertiesFile("application.properties");

//        String cmdPath = properties.getProperty("z7z.path");
        String cmdPath = "C:/Program Files/7-Zip";
        if (cmdPath == null) throw new Exception(rarFileName + " 无法解压缩");

        if (!cmdPath.endsWith("\\")) cmdPath += File.separator;

        String cmd = cmdPath + "7z.exe x " + rarFileName + " -o" + outFilePath + " -aoa";


        try {
            Process proc = Runtime.getRuntime().exec(cmd);

            int r = proc.waitFor();

            //这段主要是将二级文件夹内的文件拷贝出来，放到上一级文件中
            //不然就会出现前端找不到文件的问题
            File fileRoot = new File(outFilePath);
            File files[] = fileRoot.listFiles();
            File[] filePath=null;
            for (File file : files){
                if (file.isDirectory()){
                    filePath = new File(file.getPath()).listFiles();
                    for (File targetFile : filePath){
                        String fileNameWithTail = targetFile.getName();
                        targetFile.renameTo(new File(fileRoot.getPath()+"/"+fileNameWithTail));
                    }
                }
            }

            if (r != 0) {

                if (proc.exitValue() == 0) return RarUtils.convert(outFilePath);
                return null;

            }
            return RarUtils.convert(outFilePath);
        } catch (Exception e1) {
            //  DebugDump.printDebugInfo();
            //  e1.printStackTrace();
        }
        return null;

    }

    /**
     * 解压缩rar文件
     *
     * @param rarFileName
     * @return
     * @throws Exception
     */
    public static List<ProjectFile> unrar(String rarFileName) throws Exception {
        if (rarFileName == null) throw new Exception("文件名为空");
        if (!ProjectFileUtils.getExtensionName(rarFileName).toLowerCase().equals("rar")) throw new Exception("文件扩展名错误");


        try {
            File file = new File(rarFileName);
            if (!file.isFile()) throw new Exception("文件不存在或格式错误");


            String outFilePath = ProjectFileUtils.getFileNameNoEx(rarFileName);

            File dstDiretory = new File(outFilePath);
            // 目标目录不存在时，创建该文件夹
            if (!dstDiretory.exists()) dstDiretory.mkdirs();


            Archive archive = null;

            archive = new Archive(file, new UnrarProcessMonitor(rarFileName));

            if (archive != null) {
                if (archive.isEncrypted()) throw new Exception(rarFileName + "为加密文件，无法解压缩!");
                List<FileHeader> files = archive.getFileHeaders();
                List<ProjectFile> projectFileList = new ArrayList<>();
                for (FileHeader fh : files) {
                    if (fh.isEncrypted()) throw new Exception(rarFileName + "为加密文件，无法解压缩！");
                    String fileName = fh.getFileNameW();
                    if (fileName != null && fileName.trim().length() > 0) {

                        ProjectFile projectFile = new ProjectFile();
                        String fileext = ProjectFileUtils.getExtensionNameWithSeparator(fileName) + "";
                        String diskFileName = GUID.getGUID() + fileext;
                        projectFile.setName(fileName);
                        projectFile.setDiskFileName(diskFileName);


                        String saveFileName = outFilePath + File.separator + diskFileName;
                        File saveFile = new File(saveFileName);
                        File parent = saveFile.getParentFile();
                        if (!parent.exists()) parent.mkdirs();
                        if (!saveFile.exists()) saveFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(saveFile);
                        try {
                            archive.extractFile(fh, fos);
                            fos.flush();
                            fos.close();
                        } catch (RarException e) {

                            if (e.getType().equals(RarException.RarExceptionType.notImplementedYet)) {
                            }
                        } finally {
                        }
                        projectFileList.add(projectFile);
                    }
                }
                return projectFileList;
            }
        } catch (RarException | IOException e) {


            //使用命令行解压缩
            return RarUtils.unRarBy7Z(rarFileName);

        }
        return null;

    }

    private static List<ProjectFile> convert(String outFileDir) {


        List<String> filenameList = ProjectFileUtils.getFileList(outFileDir);
        if (filenameList == null || filenameList.size() == 0) return null;


        List<ProjectFile> projectFileList = new ArrayList<>();
        for (String fileName : filenameList) {
            ProjectFile projectFile = new ProjectFile();
            String fileext = ProjectFileUtils.getExtensionNameWithSeparator(fileName) + "";
            String diskFileName = GUID.getGUID() + fileext;
            projectFile.setName(fileName);
            projectFile.setDiskFileName(diskFileName);

            File oldfile = new File(outFileDir + File.separator + fileName);
            File newFile = new File(outFileDir + File.separator + diskFileName);
            oldfile.renameTo(newFile);
            oldfile.delete();
            projectFileList.add(projectFile);

        }

        return projectFileList;
    }

}
