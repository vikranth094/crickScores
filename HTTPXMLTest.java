package cric;


import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class HTTPXMLTest
{
    public static void main(String[] args) 
    {
        try {
            new HTTPXMLTest().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void displayTray(String msg) throws AWTException, java.net.MalformedURLException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));
        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resizes the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("Cricket Score!");
        tray.add(trayIcon);
        trayIcon.displayMessage("Cricket Score!", msg, MessageType.INFO);
    }

    private void start() throws Exception
    {
        URL url = new URL("http://static.cricinfo.com/rss/livescores.xml");
        URLConnection connection = url.openConnection();

        Document doc = parseXML(connection.getInputStream());
        NodeList descNodes = doc.getElementsByTagName("description");
        

        for(int i=1; i<descNodes.getLength();i++)
        {
            System.out.println(i+ ") "+descNodes.item(i).getTextContent());
        }
        System.out.println("\nSelect match!!\n");
        Scanner in=new Scanner(System.in);
        int select=in.nextInt();
        System.out.println("Enter interval time ins seconds!!\n");
        int seconds=in.nextInt();
        
        in.close();
        
        while(true)
        {
        	URL url2 = new URL("http://static.cricinfo.com/rss/livescores.xml");
        	
            URLConnection connection2 = url2.openConnection();

            Document doc1 = parseXML(connection2.getInputStream());
            NodeList descNodes1 = doc1.getElementsByTagName("description");
            displayTray(descNodes1.item(select).getTextContent());
            Thread.sleep(1000*seconds);
        }
    }

    private Document parseXML(InputStream stream)
    throws Exception
    {
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
        try
        {
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

            doc = objDocumentBuilder.parse(stream);
        }
        catch(Exception ex)
        {
            throw ex;
        }       

        return doc;
    }
}