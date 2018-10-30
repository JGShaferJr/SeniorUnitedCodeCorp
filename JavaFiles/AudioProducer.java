package de.fu_berlin.inf.dpp.ui.widgets.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!Thread.currentThread().isInterrupted()) {
            Runtime rt = Runtime.getRuntime();
            String pyFile = getPythonFile(this.pyFile);
            LOG.debug("[SUCC] Python file location: " + pyFile);
            String[] arguments = { "python", pyFile };
            Process proc;
            try {
                proc = rt.exec(arguments);
                get_output(proc);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private void get_output(Process proc) {
        LOG.debug("[SUCC] Running python");
        BufferedReader stdout = new BufferedReader(
            new InputStreamReader(proc.getInputStream()));
        BufferedReader stderr = new BufferedReader(
            new InputStreamReader(proc.getErrorStream()));
        try {
            String s;
            while ((s = stdout.readLine()) != null) {
                LOG.debug("[SUCC] adding to queue:" + s);
                queue.put(s);
            }
            while ((s = stderr.readLine()) != null) {
                LOG.error("[SUCC] python err: " + s);
            }
            int retVal = proc.waitFor();
            LOG.debug("[SUCC] Ret val: " + retVal);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOG.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            LOG.error("[SUCC] " + e.getMessage(), e);
        }
    }

}
