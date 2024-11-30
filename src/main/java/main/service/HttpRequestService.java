package main.service;

import main.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

public class HttpRequestService {

    private final static Logger LOGGER = LogManager.getLogger(HttpRequestService.class);

    private static List<String> convertInputStreamIntoStringList(InputStream inputStream) {
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String output;
            while ((output = br.readLine()) != null) {
                result.add(output);
            }
        } catch (IOException e) {
            LOGGER.error("Unable to convert InputStream", e);
        }
        return result;
    }

    public static String sendRequest(String urlString, String httpMethod, Map<String, String> headers, JSONObject requestBody) {
        String result = Constants.EMPTY;
        try {
            URL url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("SSL");
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error("Illegal algorithm", e);
            }
            if (sslContext != null) {
                sslContext.getServerSessionContext().setSessionTimeout(0);
                try {
                    sslContext.init(null, new TrustManager[] {new TrustAllTrustManager()}, null);
                } catch (KeyManagementException e) {
                    LOGGER.error("SSLContext initialization fails", e);
                }
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            }
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            headers.forEach(connection::setRequestProperty);
            if (requestBody.length() > 0) {
                connection.getOutputStream().write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
            }
            List<String> messages;
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                LOGGER.error("The response code is not 200");
                LOGGER.error("url = " + urlString);
                LOGGER.error("http method = " + httpMethod);
                LOGGER.error("headers = " + headers);
                LOGGER.error("request body = " + requestBody);
                messages = convertInputStreamIntoStringList(connection.getErrorStream());
                messages.forEach(LOGGER::error);
                connection.disconnect();
                return result;
            } else {
                messages = convertInputStreamIntoStringList(connection.getInputStream());
                connection.disconnect();
            }
            if (messages.size() > 0) {
                if (messages.size() > 1) {
                    LOGGER.error("The response message size is greater than 1");
                    messages.forEach(LOGGER::error);
                    result = messages.stream().reduce(Constants.EMPTY, (output, item) -> output + Constants.COMMA + item);
                } else {
                    result = messages.get(0);
                }
            } else {
                LOGGER.error("Unable to get response since the size of messages is 0");
            }
        } catch (IOException e) {
            LOGGER.error("Unable to get response", e);
        }
        return result;
    }

    static class TrustAllTrustManager implements X509TrustManager, TrustManager {

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}