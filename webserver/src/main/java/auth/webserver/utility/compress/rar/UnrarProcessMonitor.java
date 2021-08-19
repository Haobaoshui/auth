package auth.webserver.utility.compress.rar;

import com.github.junrar.UnrarCallback;
import com.github.junrar.Volume;
import com.github.junrar.impl.FileVolume;

import java.io.IOException;

public class UnrarProcessMonitor implements UnrarCallback {
    private String fileName;

    UnrarProcessMonitor(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 返回false的话，对于某些分包的rar是没办法解压正确的
     */
    @Override
    public boolean isNextVolumeReady(Volume volume) {
        try {
            fileName = ((FileVolume) volume).getFile().getCanonicalPath();
        } catch (IOException e) {


            return false;
        }
        return true;
    }

    @Override
    public void volumeProgressChanged(long l, long l1) {
        //输出进度
        // System.out.println("Unrar " + fileName + " rate: " + (double) l / l1 * 100 + "%");
    }

}
