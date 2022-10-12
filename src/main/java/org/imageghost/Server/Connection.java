package org.imageghost.Server;

import org.imageghost.ClientCustomException.NoServerException;
import org.imageghost.Config;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class Connection {
    /*
        Check Server is running.
     */
    public static boolean checkServerLive(){
        String response = null;
        String URL = Config.healthCheckURL;
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

    /*
        get request to server
     */
    public static String httpGetRequest(String pURL, String pathVariable){
        if(!Connection.checkServerLive()){
            throw new NoServerException("Server is not running.");
        }
        String myResult = "";
        try {
            //   URL 설정하고 접속하기
            URL url = new URL(pURL + pathVariable); // URL 설정

            HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true); // 서버에서 읽기 모드 지정
            http.setDoOutput(true); // 서버로 쓰기 모드 지정
            http.setRequestMethod("GET"); // 전송 방식은 GET

            http.setRequestProperty("content-type", "application/json;utf-8"); // set property to application/json

            //--------------------------
            //   Response Code
            //--------------------------
            int responseCode = http.getResponseCode();
            System.out.println("response code in GET request:" +  + responseCode);

            //--------------------------
            //   receive from server
            //--------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            // IOException Error occur.

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

    /*
        post request to server
     */
    public static String httpPostRequest(String pURL, HashMap <String, String> pList) {
        String myResult = "";

        try {
            //   URL 설정하고 접속하기
            URL url = new URL(pURL); // URL 설정

            HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true); // 서버에서 읽기 모드 지정
            http.setDoOutput(true); // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST"); // 전송 방식은 POST

            http.setRequestProperty("content-type", "application/json;utf-8"); // set property to application/json

            //--------------------------
            //   send to server
            //--------------------------
            StringBuffer buffer = new StringBuffer();

            JSONObject sendJSONData = new JSONObject();
            sendJSONData.put("ownerPublicKey", pList.get("ownerPublicKey"));
            sendJSONData.put("ciphertext", pList.get("ciphertext"));

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(sendJSONData.toJSONString());
            writer.flush();

            //--------------------------
            //   Response Code
            //--------------------------
            int responseCode = http.getResponseCode();
            System.out.println("response code in POST request:" + responseCode);

            //--------------------------
            //   receive from server
            //--------------------------
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
