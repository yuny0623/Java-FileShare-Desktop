package org.imageghost.HTTP;

import org.imageghost.CustomException.NoServerException;
import org.imageghost.Config;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class HTTPConnection {

    public static boolean checkServerLive(){
        String response = null;
        String URL = Config.HEALTH_CHECK_URL;
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
        if(response == null){
            System.out.println("is dead: " + response);
            return false;
        }
        if(response.equals("health-check")){
            System.out.println("is live: " + response);
            return true;
        }else{
            System.out.println("is dead: " + response);
            return false;
        }
    }

    public static String httpGetRequest(String pURL, String pathVariable){
        if(!HTTPConnection.checkServerLive()){
            throw new NoServerException("Server is not running.");
        }
        String myResult = "";
        try {
            URL url = new URL(pURL + pathVariable);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("GET");

            http.setRequestProperty("content-type", "application/json;utf-8");

            int responseCode = http.getResponseCode();
            System.out.println("response code in GET request:" +  + responseCode);

            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
            }

            myResult = builder.toString();
            return myResult;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myResult;
    }

    public static String httpPostRequest(String pURL, HashMap <String, String> pList) {
        String myResult = "";

        try {
            URL url = new URL(pURL);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

            http.setRequestProperty("content-type", "application/json;utf-8");

            StringBuffer buffer = new StringBuffer();

            JSONObject sendJSONData = new JSONObject();
            sendJSONData.put("ownerPublicKey", pList.get("ownerPublicKey"));
            sendJSONData.put("ciphertext", pList.get("ciphertext"));

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(sendJSONData.toJSONString());
            writer.flush();
            int responseCode = http.getResponseCode();
            System.out.println("response code in POST request:" + responseCode);

            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
            }
            myResult = builder.toString();
            return myResult;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myResult;
    }
}
