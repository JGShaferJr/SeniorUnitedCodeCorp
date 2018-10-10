package de.fu_berlin.inf.dpp.ui.widgets.chat;

import java.io.IOException;

import org.apache.log4j.Logger;

public class VideoStreamer {
    private Process proc = null;
    private static final Logger LOG = Logger.getLogger(VideoStreamer.class);
    private final String pyFile = "server.py";

    @Override
    protected void finalize() throws Throwable {
        if (!is_running()) {
            kill_process();
        }
    }

    public VideoStreamer() {
        LOG.debug("[SUCC] Attempting to stream video");
        String[] args = { "python", AudioProducer.getPythonFile(pyFile) };
        try {
            proc = Runtime.getRuntime().exec(args);
            LOG.debug("[SUCC] Started video streaming");
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    public boolean is_running() {
        return proc != null;
    }

    public void kill_process() {
        proc.destroy();
    }
}
