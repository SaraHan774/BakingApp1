package com.sarahan.bakingapp_2.JsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FetchJson {
    public static final String JSON_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    static String fetchJsonData(){ //network connection이 null을 반환함.
        HttpsURLConnection urlConnection  = null;
        BufferedReader reader = null;
        String recipeJsonString = null;

        try{

            URL requestRecipeUrl = new URL(JSON_URL);
            urlConnection = (HttpsURLConnection)requestRecipeUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int resCode = urlConnection.getResponseCode();
            System.out.println("res code: " + resCode);
            if(resCode != HttpsURLConnection.HTTP_OK)
                return null;

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            //buffer 로 바꿔보기
            String line;
            while((line = reader.readLine()) != null){ //disconnection boilerplate 제거하기!!!
                builder.append(line);
                builder.append("\n");
                if(builder.length() == 0){
                    return null;
                }
            }
            recipeJsonString = builder.toString();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(urlConnection != null){ //안하면 null pointer exception
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }
        return recipeJsonString;
    }

}
