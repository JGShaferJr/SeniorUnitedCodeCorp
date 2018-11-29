package de.fu_berlin.inf.dpp.ui.widgets.chat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

import de.fu_berlin.inf.dpp.Saros;

public class AudioProducer implements Runnable {
    protected BlockingQueue<String> queue;
    private static final Logger LOG = Logger.getLogger(AudioProducer.class);
    protected static final String pyFile = "audio.py";
    private Process proc = null;

    public static String getPythonFile(String sfile) {
        URL url = Platform.getBundle(Saros.PLUGIN_ID).getEntry(sfile);
        try {
            url = FileLocator.toFileURL(url);
        } catch (IOException e1) {
            LOG.debug("Could not convert to file URL:", e1);
            return null;
        }

        File file = new File(url.getFile());
        return file.getAbsolutePath();
    }

    public AudioProducer(BlockingQueue<String> queue) {
        super();
        LOG.debug("[SUCC] Started Audio Producer");
        this.queue = queue;
        String filename = AudioProducer.getPythonFile(pyFile);
        LOG.debug("[SUCC]: Starting " + filename);
        String[] arguments = { "python3", filename };
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(arguments);
            processBuilder.redirectErrorStream(true);
            proc = processBuilder.start();

        } catch (IOException e) {
            LOG.debug("[SUCC] " + e.getMessage());
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            int in;
            String line = "";
            InputStream is = proc.getInputStream();
            LOG.debug("[SUCC]: starting loop");
            if (proc.isAlive()) {
                LOG.debug("[SUCC] still alive");
            } else {
                LOG.debug("[SUCC] not alive");
            }
            while ((in = is.read()) != -1) {
                if (in == '\n') {
                    LOG.debug("[SUCC]: Got line" + line);
                    queue.put(line);
                    line = "";
                }
                line += (char) in;
            }
            LOG.debug("[SUCC]: closing loop");
            is.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error("[SUCC] thread join error: " + e.getMessage(), e);
        }
    }

}
