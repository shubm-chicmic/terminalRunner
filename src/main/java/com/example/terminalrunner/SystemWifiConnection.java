package com.example.terminalrunner;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.TimerTask;

public class SystemWifiConnection {

    public static void main(String[] args) {
        // Trust all certificates before launching the JavaFX application
        CustomTrustManager.trustAllCertificates();
        showWifiTray();
    }

    public static Image resizeImage(Image originalImage, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        System.out.println("Icon width: " + resizedImage.getWidth(null) + ", height: " + resizedImage.getHeight(null));

        return resizedImage;
    }

    private static Boolean isConnected = false;

    public static void showWifiTray() {
        Platform.setImplicitExit(false); // Don't exit the application when the window is closed
        Image icon = null;
        Image disconnectedImage = new ImageIcon(SystemWifiConnection.class.getResource("/disconnect-wifi.png")).getImage();
        Image connectedImage = new ImageIcon(SystemWifiConnection.class.getResource("/wifi.png")).getImage();
        if (isConnected) {
            icon = connectedImage;
        } else {
            icon = disconnectedImage;
        }
        //        icon = resizeImage(icon, 26, 26);
        // Create a system tray icon
        TrayIcon trayIcon = new TrayIcon(icon);

        trayIcon.setToolTip("Your Application Name");
        PopupMenu popupMenu = new PopupMenu();

        // Create an "Exit" MenuItem
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle the exit action here
                System.exit(0); // Exit the application
            }
        });

        // Add the "Exit" MenuItem to the PopupMenu
        popupMenu.add(exitMenuItem);

        // Set the PopupMenu for the TrayIcon
        trayIcon.setPopupMenu(popupMenu);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    if (!isConnected) {
                        sendHttpRequestToConnect();
                        trayIcon.setImage(connectedImage);
                    } else {
                        sendHttpRequestToDisconnect();
                        trayIcon.setImage(disconnectedImage);
                    }
                }
//                System.out.println("wifi clicked");
            }
        });

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String username = "subham.kumar";
    public static String password = "Mishra@12345";

    private static void sendHttpRequestToConnect() {
        String url = "https://192.168.2.254:8090/login.xml"; // Updated URL


        String mode = "191";
        String a = "1694430682539";
        String productType = "0";

        // Create the request body with separate parameters and URL encode them
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
        String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);
        String encodedMode = URLEncoder.encode(mode, StandardCharsets.UTF_8);
        String encodedA = URLEncoder.encode(a, StandardCharsets.UTF_8);
        String encodedProductType = URLEncoder.encode(productType, StandardCharsets.UTF_8);

        String requestBody = "mode=" + encodedMode + "&username=" + encodedUsername + "&password=" + encodedPassword +
                "&a=" + encodedA + "&producttype=" + encodedProductType;
        CustomTrustManager.trustAllCertificates();
        try {
            // Create a custom trust manager that trusts all certificates
            System.out.println("Creating a custom trust manager");
            System.out.println("Sending HTTP Request to: " + url);
            System.out.println("Request Body: " + requestBody);

            // Create an SSL context with the custom trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new CustomTrustManager()}, null);

            // Use the custom SSL context for HttpClient
            CloseableHttpClient client = HttpClients.custom()
                    .setSslcontext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE) // Bypass hostname verification
                    .build();


            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Accept", "*/*");
            httpPost.setHeader("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
            httpPost.setHeader("Cache-Control", "no-cache");
            httpPost.setHeader("Connection", "keep-alive");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Origin", "https://192.168.2.254:8090");
            httpPost.setHeader("Pragma", "no-cache");
            httpPost.setHeader("Referer", "https://192.168.2.254:8090");
            httpPost.setHeader("Sec-Fetch-Dest", "empty");
            httpPost.setHeader("Sec-Fetch-Mode", "cors");
            httpPost.setHeader("Sec-Fetch-Site", "same-origin");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
            httpPost.setEntity(new StringEntity(requestBody));
            // Add other headers here


            try (CloseableHttpResponse response = client.execute(httpPost)) {

                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Response Status Code: " + statusCode);

                HttpEntity responseEntity = response.getEntity();
//                String responseBody = EntityUtils.toString(responseEntity);
                if (response.getEntity() != null) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.out.println("Response Body: " + responseBody);
                } else {
                    System.out.println("Response Body is empty.");
                }

                if (statusCode == 200) {
                    // Successful response handling logic here
                    System.out.println("status code 200");
                    java.util.Timer timer = new java.util.Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            sendHttpRequestToCheckConnection();
                        }
                    }, 0, 420000); // Delay of 0 milliseconds, repeat every 5000 milliseconds (5 seconds)
                } else {
                    // Handle other status codes as needed
                    System.out.println("sc!200");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
            // Handle exceptions and display an error message
//            showAlert("Error", "HTTP Request Failed", AlertType.ERROR);
        }
    }

    public static void sendHttpRequestToDisconnect() {

    }

    private static void sendHttpRequestToCheckConnection() {
        String checkConnectionUrl = "https://192.168.2.254:8090/live?mode=192&username=subham.kumar&a=1694668600867&producttype=0";

        try {
            // Create an SSL context and HTTP client as you did for the login request

            HttpGet checkConnectionRequest = new HttpGet(checkConnectionUrl);
            checkConnectionRequest.setHeader("Accept", "*/*");
            checkConnectionRequest.setHeader("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
            // Add other headers if necessary
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new CustomTrustManager()}, null);
            CloseableHttpClient client = HttpClients.custom()
                    .setSslcontext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE) // Bypass hostname verification
                    .build();


            try (CloseableHttpResponse connectionResponse = client.execute(checkConnectionRequest)) {
                int statusCode = connectionResponse.getStatusLine().getStatusCode();
                System.out.println("Connection Check Response Status Code: " + statusCode);

                if (statusCode == 200) {
                    // Connection is still alive, you can add further handling here
                    System.out.println("Connection is alive.");
                } else {
                    // Handle other status codes as needed
                    System.out.println("Connection is not alive.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception while checking connection: " + e.getMessage());
        }
    }




    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

class CustomTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        // Implement your custom client trust logic here
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        // Implement your custom server trust logic here
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public static void trustAllCertificates() {
        try {
            // Create a custom trust manager that trusts all certificates
            TrustManager[] trustAllCertificates = new TrustManager[]{
                    new CustomTrustManager()
            };

            // Get the SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            // Set the default SSL socket factory to use the custom trust manager
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Disable hostname verification
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
