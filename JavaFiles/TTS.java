package de.fu_berlin.inf.dpp.ui.widgets.chat;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class TTS implements Runnable {
    protected BlockingQueue<String> queue;
    private static final Logger LOG = Logger.getLogger(TTS.class);
    protected static final String pyFile = "tts.py";

    public TTS(BlockingQueue<String> queue) {
        super();
        LOG.debug("[SUCC] Started TTS thread");
        this.queue = queue;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!Thread.currentThread().isInterrupted()) {
            String message;
            try {
                message = queue.take();

                Runtime rt = Runtime.getRuntime();
                String pyFile = AudioProducer.getPythonFile(this.pyFile);
                String[] arguments = { "python", pyFile, message };
                Process proc;

                proc = rt.exec(arguments);
                LOG.debug("[SUCC]: TTS: \"" + message + "\"");
                proc.waitFor();
            } catch (Exception e) {
                LOG.error("[SUCC] Error " + e.getMessage(), e);
            }
        }
    }

}
