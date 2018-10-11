package de.fu_berlin.inf.dpp.ui.widgets.chat;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class VideoStreamer {
    // private Process proc = null;
    private static final Logger LOG = Logger.getLogger(VideoStreamer.class);
    // private final String pyFile = "server.py";
    private FacsvatarServerLauncher launcher;

    @Override
    protected void finalize() throws Throwable {
        LOG.debug("[SUCC] Wizard FACSvatar finalizing");
        launcher.KillServer();
    }

    static String GetIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e2) {
            LOG.debug(e2.getMessage(), e2);
            return "127.0.0.1";
        }
        // String ip = null;
        // try (final DatagramSocket socket = new DatagramSocket()) {
        // socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        // ip = socket.getLocalAddress().getHostAddress();
        // } catch (UnknownHostException e) {
        // e.printStackTrace();
        // } catch (SocketException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }
        // return ip;
    }

    public VideoStreamer() {
        String ip = GetIP();
        LOG.debug("[SUCC] Wizard FACSvatar starting up, using IP " + ip);
        launcher = new FacsvatarServerLauncher(ip);
    }
}
