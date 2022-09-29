package Server;

import ClientCustomException.NoServerException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
    public static boolean httpRequestPost(String urlPath, HashMap<String, String> data){
        if(Connection.checkServerLive()){
            throw new NoServerException("Server is not running.");
        }

        return true;
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

    public static String postRequest(String pURL, HashMap < String, String > pList) {

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

            http.setRequestProperty("content-type", "applicaiton/json;utf-8");
            //--------------------------
            // 헤더 세팅
            //--------------------------
            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다

            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuffer buffer = new StringBuffer();

            // HashMap으로 전달받은 파라미터가 null이 아닌경우 버퍼에 넣어준다
            if (pList != null) {

                Set key = pList.keySet();

                for (Iterator iterator = key.iterator(); iterator.hasNext();) {
                    String keyName = (String) iterator.next();
                    String valueName = pList.get(keyName);
                    buffer.append(keyName).append("=").append(valueName);
                }
            }

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();


            //--------------------------
            //   Response Code
            //--------------------------
            //http.getResponseCode();


            //--------------------------
            //   서버에서 전송받기
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
