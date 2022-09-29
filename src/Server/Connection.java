package Server;

import ClientCustomException.NoServerException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;





public class Connection {

    /*
        Check Server is running.
     */
    public static boolean checkServerLive(){
        String response = null;
        String URL = "http://localhost:8080/health-check";
        String GET = "GET";
        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return false;
            }
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            bufferedReader.close();
            response = stringBuffer.toString();
        }catch(Exception e){
            e.printStackTrace();
        }

        if(response.equals("health-check")){
            System.out.println(response);
            return true;
        }else{
            System.out.println(response);
            return false;
        }
    }

    /*
        Server Get Request
     */
    public static HashMap<String, String> httpRequestGet(String urlPath, String pathVariable){
        if(Connection.checkServerLive()){
            throw new NoServerException("Server is not running.");
        }

        return new HashMap<>();
    }

    /*
        body 로 데이터 전달 
        Server Post Request
     */
    public static HashMap<String, String> httpRequestPost(String urlPath, HashMap<String, String> data){
        if(Connection.checkServerLive()){
            throw new NoServerException("Server is not running.");
        }

        return new HashMap<>();
    }

    /*
        path variable 로 데이터 전달 
     */
    public static HashMap<String, String> httpRequestPostWithPathVariable(String urlPath, String pathVariable){
        if(Connection.checkServerLive()){
            throw new NoServerException("Server is not running.");
        }

        return new HashMap<>();
    }
}
