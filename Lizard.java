package de.fu_berlin.inf.dpp.ui.widgets.chat;

import org.apache.log4j.Logger;

public class Lizard {
    String ip_addr;
    FacsvatarClientLauncher client;
    private static final Logger LOG = Logger.getLogger(Lizard.class);

    @Override
    protected void finalize() throws Throwable {
        LOG.debug("[SUCC] Lizard FACSvatar finalizing");
        client.KillSubProcesses();
    }

    public Lizard(String ip_addr) {
        this.ip_addr = ip_addr;
        LOG.debug("[SUCC] Lizard FACSvatar starting up");
        client = new FacsvatarClientLauncher(ip_addr);
    }
}
