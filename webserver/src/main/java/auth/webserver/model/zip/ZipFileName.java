package auth.webserver.model.zip;

public class ZipFileName {
    String srcFileName;//源文件名称
    String destFileName;//解压后的目的文件名称

    public String getSrcFileName() {
        return srcFileName;
    }

    public void setSrcFileName(String srcFileName) {
        this.srcFileName = srcFileName;
    }

    public String getDestFileName() {
        return destFileName;
    }

    public void setDestFileName(String destFileName) {
        this.destFileName = destFileName;
    }
}
