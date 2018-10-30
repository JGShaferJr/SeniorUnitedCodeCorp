package de.fu_berlin.inf.dpp.ui.widgets.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class FacsvatarServerLauncher {

    FacsvatarServerLauncher(String ip) {

        MakeConfig(ip);

        Runnable openface = new Runnable() {
            @Override
            public void run() {
                try {
                    String line;
                    ProcessBuilder procBuild = new ProcessBuilder(
                        AudioProducer.getPythonFile("run_openface.bat"),
                        AudioProducer.getPythonFile("Openface_build"));
                    procBuild.redirectErrorStream(true);
                    process = procBuild.start();
                    BufferedReader input = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                    while ((line = input.readLine()) != null) {
                        System.out.println(line);
                    }
                    input.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

        };

        thread = new Thread(openface);
        thread.start();
    }

    void KillServer() {
        try {
            System.out.println("Destroying open face");

            process.destroyForcibly();
            process.waitFor();
            System.out.println("Destroyed OpenFace");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Thread thread;

    Process process;

    public void MakeConfig(String input_ip) {
        try {/*
              * DocumentBuilderFactory dbFactory =
              * DocumentBuilderFactory.newInstance(); DocumentBuilder dBuilder =
              * dbFactory.newDocumentBuilder(); Document doc =
              * dBuilder.newDocument();
              *
              * // root element Element rootElement =
              * doc.createElement("Config"); doc.appendChild(rootElement);
              *
              * Element mode = doc.createElement("Mode");
              * mode.appendChild(doc.createTextNode("push"));
              * rootElement.appendChild(mode);
              *
              * Element my_ip = doc.createElement("IP");
              * my_ip.appendChild(doc.createTextNode(input_ip));
              * rootElement.appendChild(my_ip);
              *
              * Element port = doc.createElement("Port");
              * port.appendChild(doc.createTextNode("5570"));
              * rootElement.appendChild(port);
              *
              * Element topic = doc.createElement("Topic");
              * topic.appendChild(doc.createTextNode("openface"));
              * rootElement.appendChild(topic);
              *
              *
              * TransformerFactory transformerFactory =
              * TransformerFactory.newInstance(); Transformer transformer =
              * transformerFactory.newTransformer(); DOMSource source = new
              * DOMSource(doc);
              *
              * //needs correct file location to put the config into the
              * openface directory StreamResult result = new StreamResult(new
              * File("OpenFace_build\\config.xml"));
              * transformer.transform(source, result);
              */
            PrintWriter out = new PrintWriter(
                AudioProducer.getPythonFile("OpenFace_build\\config.xml"));
            out.println("<Config><Mode>push</Mode><IP>" + input_ip
                + "</IP><Port>5570</Port><Topic>openface</Topic></Config>");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}