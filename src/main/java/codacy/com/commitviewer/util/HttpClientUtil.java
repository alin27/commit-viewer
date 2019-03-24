package codacy.com.commitviewer.util;

import codacy.com.commitviewer.exception.GitHubTimeoutException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Slf4j
@UtilityClass
public class HttpClientUtil {

    public String getResponseString(String urlString, int connectionTimeout) {
//        StringBuilder response = new StringBuilder();
//
//        try {
//            HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Content-Type", "application/json");
//            con.setConnectTimeout(connectionTimeout);
//            con.setReadTimeout(connectionTimeout);
//
//            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
//
//                String line;
//
//                while ((line = in.readLine()) != null) {
//                    response.append(line);
//                    response.append(System.lineSeparator());
//                }
//            }
//
//            if (con.getResponseCode() == HttpStatus.REQUEST_TIMEOUT.value()) {
//                throw new GitHubTimeoutException("The request has timeout");
//            }
//
//        } catch (Exception e) {
//            if (e instanceof GitHubTimeoutException) {
//                log.warn("Unable to complete the request to git hub in specified time");
//            } else if (e instanceof ProtocolException | e instanceof MalformedURLException | e instanceof IOException){
//                log.error("Unable to connect to git hub. Error: " + e.getMessage());
//            }
//        }
//        return response.toString();
        return "";
    }
}