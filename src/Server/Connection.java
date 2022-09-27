package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {

    public static boolean checkServerLive() throws Exception {
        String URL = "http://localhost:8080/health-check";
        String GET = "GET";

        java.net.URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(GET);

        int responseCode = connection.getResponseCode();
        if(responseCode != 200){
            return false;
        }
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        StringBuffer stringBuffer = new StringBuffer();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null)  {
            stringBuffer.append(inputLine);
        }
        bufferedReader.close();
        String response = stringBuffer.toString();

        if(response.equals("health-check")){
            System.out.println(response);
            return true;
        }else{
            System.out.println(response);
            return false;
        }
    }
}
