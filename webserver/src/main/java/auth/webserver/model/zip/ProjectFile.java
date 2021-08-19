package auth.webserver.model.zip;

import org.springframework.stereotype.Component;

@Component
public class ProjectFile {
    private String name;
    private String diskFileName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }
}
