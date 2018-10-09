package de.fu_berlin.inf.dpp.ui.widgets.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class AudioProducer implements Runnable {
    protected BlockingQueue<String> queue;
    private static final Logger LOG = Logger.getLogger(AudioProducer.class);

    public AudioProducer(BlockingQueue<String> queue) {
        super();
        this.queue = queue;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!Thread.currentThread().isInterrupted()) {
            Runtime rt = Runtime.getRuntime();
            String[] arguments = { "python", "audio.py" };
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
        LOG.debug("Running python");
        BufferedReader stdInput = new BufferedReader(
            new InputStreamReader(proc.getInputStream()));
        BufferedReader stdErr = new BufferedReader(
            new InputStreamReader(proc.getInputStream()));
        String s = null;
        try {
            while ((s = stdInput.readLine()) != null) {
                LOG.debug("adding to queue");
                queue.put(s);
            }
            while ((s = stdErr.readLine()) != null) {
                LOG.error("python: " + s);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOG.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            LOG.error(e.getMessage(), e);
        }
    }

}
