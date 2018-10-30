package de.fu_berlin.inf.dpp.ui.widgets.chat;

// start unity (with system ip)
// start modules/n_proxy_m_bus.py with system ip, not local but the one on the network
// start modules/process_facstoblend/pub_blend.py
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class FacsvatarClientLauncher {

    public FacsvatarClientLauncher(String ip_address) {
        System.out.println("Starting unity.py");
        Runnable unity = new Runnable() {
            public void run() {
                try {
                    String line;
                    ProcessBuilder procBuild = new ProcessBuilder(
                        AudioProducer.getPythonFile(
                            "FACSvatar_client_unity\\unity_FACSvatar.exe"));
                    procBuild.redirectErrorStream(true);
                    process_unity = procBuild.start();
                    BufferedReader input = new BufferedReader(
                        new InputStreamReader(process_unity.getInputStream()));
                    while ((line = input.readLine()) != null) {
                        System.out.println(line);
                    }
                    input.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

        };

        thread_unity = new Thread(unity);
        thread_unity.start();

        System.out.println("Starting n_proxy_m_bus.py");
        proxy_launcher = new FacsvatarNProxyLauncher(ip_address);

        thread_proxy = new Thread(proxy_launcher);
        thread_proxy.start();

        System.out.println("Starting pub_blend.py");

        Runnable facsvatar_pub_blend = new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                String[] arguments = { "python3",

                    AudioProducer.getPythonFile(
                        "FACsvatar_client_interface\\modules\\process_facstoblend\\pub_blend.py") };
                try {
                    String line;
                    ProcessBuilder procBuild = new ProcessBuilder(arguments);
                    procBuild.redirectErrorStream(true);
                    process_pub_blend = procBuild.start();

                    BufferedReader input = new BufferedReader(
                        new InputStreamReader(
                            process_pub_blend.getInputStream()));
                    while ((line = input.readLine()) != null) {
                        System.out.println(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };

        thread_pub_blend = new Thread(facsvatar_pub_blend);
        thread_pub_blend.start();

    }

    void KillSubProcesses() {
        try {

            System.out.println("Destroying sub processes");

            proxy_launcher.process.destroyForcibly();
            proxy_launcher.process.waitFor();
            System.out.println("Destroyed n_proxy");

            process_pub_blend.destroyForcibly();
            process_pub_blend.waitFor();
            System.out.println("Destroyed pub_faces");

            // process_unity.destroyForcibly();
            // process_unity.waitFor();
            System.out.println("Destroyed unity");

            thread_proxy.join();
            thread_pub_blend.join();
            // thread_unity.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static String GetIP() {
        String ip = null;
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return ip;
    }

    FacsvatarNProxyLauncher proxy_launcher;

    Thread thread_proxy;
    Thread thread_pub_blend;
    Thread thread_unity;

    Process process_pub_blend;
    Process process_unity;

    public static void main(String args[]) throws SocketException {
        String ip = GetIP();
        System.out.println("IP: " + ip);

        FacsvatarClientLauncher client = new FacsvatarClientLauncher(ip);

        // FacsvatarServerLauncher server = new
        // FacsvatarServerLauncher("192.168.0.108");

        // keep program running till I want to end it
        Scanner input = new Scanner(System.in);
        String input_t = input.next();
        input.close();

        client.KillSubProcesses();
        // server.KillServer();
    }
}