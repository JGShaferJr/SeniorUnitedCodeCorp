package de.fu_berlin.inf.dpp.ui.widgets.chat;

import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class TTS implements Runnable {
    protected BlockingQueue<String> queue;
    private static final Logger LOG = Logger.getLogger(TTS.class);
    protected static final String pyFile = "tts.py";
    private Process proc = null;

    public TTS(BlockingQueue<String> queue) {
        super();
        LOG.debug("[SUCC] Started TTS thread");
        this.queue = queue;
        Runtime rt = Runtime.getRuntime();
        String pyFile = AudioProducer.getPythonFile(this.pyFile);
        String[] arguments = { "python", pyFile };
        try {
            proc = rt.exec(arguments);
        } catch (Exception e) {
            LOG.error("[SUCC] Error " + e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!Thread.currentThread().isInterrupted()) {
            String message;
            try {
                message = queue.take();

                java.io.OutputStream in = proc.getOutputStream();
                in.write(message.getBytes(Charset.forName("UTF-8")));
                in.flush();
                LOG.debug("[SUCC]: TTS: \"" + message + "\"");
            } catch (Exception e) {
                LOG.error("[SUCC] Error " + e.getMessage(), e);
            }
        }
        LOG.debug("[SUCC] Destroying TTS program");
        proc.destroy();
    }

}
