package auth.webserver.utility;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProjectFileUtils {
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


    public static String getFileNameWithNoParentDir(String filename) {
        return getFileName(filename,false);
    }

    /**
     * 返回带文件后缀的文件名
     * @param filename
     * @param includeParentDir
     * @return
     */
    public static String getFileName(String filename,boolean includeParentDir) {
        if ((filename != null) && (filename.length() > 0)) {

            int backslash=0;
            if(!includeParentDir) {
                backslash = filename.lastIndexOf('\\');
                if (backslash < 0)
                    backslash = filename.lastIndexOf('/');
                if (backslash < 0)
                    backslash = 0;
            }
            return filename.substring(backslash);
        }
        return null;
    }

    /*
     * 读取指定路径下的文件名和目录名
     */
    public static List<String> getFileList(String strDirPath) {
        File file = new File(strDirPath);

        List<String> filenameList = new ArrayList<>();

        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                String fileName = fileList[i].getName();
                filenameList.add(fileName);
            }

            if (fileList[i].isDirectory()) {
                // String fileName = fileList[i].getName();
                // System.out.println("目录：" + fileName);
            }
        }

        return filenameList;
    }

    public static String getProjectPath() {
        //String path = (String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")
        //        + CommonConstant.PROJECT_UPLOAD_FILES_PATH)).replaceAll("file:/", "").replaceAll("%20", " ").trim();
        String path = "D:/UpLoadFile/static/project/uploadfiles/";
        System.out.println("项目路径为：" + path);
        //Properties properties = ProjectFileUtils.readPropertiesFile("application.properties");
        //String path = (String.valueOf(properties.getProperty("UpLoad.path")
        //        + CommonConstant.PROJECT_UPLOAD_FILES_PATH)).replaceAll("file:/", "").replaceAll("%20", " ").trim();
        //  if (path.indexOf(":") != 1) path = File.separator + path;
        return path;
    }

    //模板路径
    public static String getModelPath() {
        //String path = (String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")
        //        + CommonConstant.PROJECT_UPLOAD_MODELS_PATH)).replaceAll("file:/", "").replaceAll("%20", " ").trim();
        String path = "D:/UpLoadFile/static/project/uploadmodels/";
        System.out.println("模板路径为：" + path);

        //Properties properties = ProjectFileUtils.readPropertiesFile("application.properties");
        //String path = (String.valueOf(properties.getProperty("UpLoad.path")
        //        + CommonConstant.PROJECT_UPLOAD_MODELS_PATH)).replaceAll("file:/", "").replaceAll("%20", " ").trim();
        //  if (path.indexOf(":") != 1) path = File.separator + path;

        return path;
    }

    public static boolean isDir(String strFileNamePath) {

        File file = new File(strFileNamePath);

        if (file.isDirectory()) return true;
        return false;

    }

    /**
     * 获得文件后缀,不包括"."
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        filename = filename.toLowerCase();
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) return filename.substring(dot + 1);

        }
        return null;
    }

    /**
     * 获得文件后缀,包括"."
     *
     * @param filename
     * @return
     */
    public static String getExtensionNameWithSeparator(String filename) {
        filename = filename.toLowerCase();
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) return filename.substring(dot);

        }
        return null;
    }

    /**
     * 获得文件名，不包含文件后缀
     *
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');

            if ((dot > -1) && (dot < (filename.length()))) return filename.substring(0, dot);
        }
        return null;
    }

    /**
     * 获得文件名，不包含文件后缀
     *
     * @param filename
     * @param includeParentDir 是否包含文件名前面的路径,例如“a\b\c.txt"，如果不包含是返回c,包含则返回"a\b\c"
     * @return
     */
    public static String getFileNameNoEx(String filename,boolean includeParentDir) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            int backslash=0;
            if(!includeParentDir) {
                backslash = filename.lastIndexOf('\\');
                if (backslash < 0)
                    backslash = filename.lastIndexOf('/');
                if (backslash < 0)
                    backslash = 0;
            }
            if ((dot > -1) && (dot < (filename.length()))) return filename.substring(backslash, dot);
        }
        return null;
    }

    /**
     * 创建父目录
     *
     * <pre>
     * 当父目录不存在时，创建目录！
     * </pre>
     *
     * @param dirFile
     */
    public static void mkDir(File dirFile) {
        File parentFile = dirFile.getParentFile();
        if (!parentFile.exists()) {
            // 递归寻找上级目录
            ProjectFileUtils.mkDir(parentFile);
            parentFile.mkdir();
        }
    }

    /**
     * 创建父目录
     *
     * <pre>
     * 当父目录不存在时，创建目录！
     * </pre>
     *
     * @param dirPath
     */
    public static void mkDir(String dirPath) {
        File parentFile = new File(dirPath);
        parentFile.mkdirs();
    }


    /**
     * 获取指定路径的文件内容，以字节流形式
     * 路径为绝对路径
     * @param filePath
     */
    public static byte[] getFileContent(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists()){
            // 文件不存在
            throw new RuntimeException("指定路径文件不存在");
        }else {
            FileInputStream fIn = new FileInputStream(filePath);
            // ? 考虑BufferedInputStream输入流缓存对象优化
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            byte[] buffer = new byte[0];
            try {
                int x = 0;
                while ((x = fIn.read()) != -1) {
                    bOut.write(x);
                }
                // 把文件输出流的数据，放到字节数组
                buffer = bOut.toByteArray();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    //关闭所有的流
                    bOut.close();
                    fIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return buffer;
        }
    }
}
