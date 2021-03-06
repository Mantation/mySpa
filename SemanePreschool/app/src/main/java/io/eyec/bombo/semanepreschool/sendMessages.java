package io.eyec.bombo.semanepreschool;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import constants.constants;

public class sendMessages extends AsyncTask <String,Void,String>{

    final static private String FCM_URL = "https://fcm.googleapis.com/fcm/send";//"https://fcm.googleapis.com/fcm/send";

    /**
     * Method to send push notification to Android FireBased Cloud messaging
     * Server.
     *
     * @param tokenId    Generated and provided from Android Client Developer
     * @param server_key Key which is Generated in FCM Server
     * @param message    which contains actual information.
     */

    public static void send_FCM_Notification(String tokenId, String server_key, String message, String title) {


        try {
// Create URL instance.
            URL url = new URL(FCM_URL);
// create connection.
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
//set method as POST or GET
            conn.setRequestMethod("POST");
//pass FCM server key
            conn.setRequestProperty("Authorization", "key=" + server_key);
//Specify Message Format
            conn.setRequestProperty("Content-Type", "application/json");
//Create JSON Object & pass value
            JSONObject infoJson = new JSONObject();

            infoJson.put("title", title);
            infoJson.put("body", message);

            JSONObject json = new JSONObject();
            //json.put("to", tokenId.trim()); modified to the one below
            json.put("to", tokenId.trim());
            json.put("notification", infoJson);

            System.out.println("json :" + json.toString());
            System.out.println("infoJson :" + infoJson.toString());
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            int status = 0;
            if (null != conn) {
                status = conn.getResponseCode();
            }
            if (status != 0) {

                if (status == 200) {
//SUCCESS message
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    System.out.println("Android Notification Response : " + reader.readLine());
                } else if (status == 401) {
//client side error
                    System.out.println("Notification Response : TokenId : " + tokenId + " Error occurred :");
                } else if (status == 501) {
//server side error
                    System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + tokenId);
                } else if (status == 503) {
//server side error
                    System.out.println("Notification Response : FCM Service is Unavailable TokenId : " + tokenId);
                }
            }
        } catch (MalformedURLException mlfexception) {
// Prototcal Error
            System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());
        } catch (Exception mlfexception) {
//URL problem
            System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());
        }

    }

    public static void send_FCM_NotificationMulti(List<String> putIds2, String server_key, String message, String title) {
        try {
            // Create URL instance.
            URL url = new URL(FCM_URL);
            // create connection.
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //set method as POST or GET
            conn.setRequestMethod("POST");
            //pass FCM server key
            conn.setRequestProperty("Authorization", "key=" + server_key);
            //Specify Message Format
            conn.setRequestProperty("Content-Type", "application/json");
            //Create JSON Object & pass value

            JSONArray regId = null;
            JSONObject objData = null;
            JSONObject data = null;
            JSONObject notif = null;

            regId = new JSONArray();
            for (int i = 0; i < putIds2.size(); i++) {
                regId.put(putIds2.get(i));
            }
            data = new JSONObject();
            data.put("message", message);
            notif = new JSONObject();
            notif.put("title", title);
            notif.put("text", message);

            objData = new JSONObject();
            objData.put("registration_ids", regId);
            objData.put("data", data);
            objData.put("notification", notif);
            System.out.println("!_@rj@_group_PASS:>" + objData.toString());


            System.out.println("json :" + objData.toString());
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(objData.toString());
            wr.flush();
            int status = 0;
            if (null != conn) {
                status = conn.getResponseCode();
            }
            if (status != 0) {

                if (status == 200) {
                    //SUCCESS message
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    System.out.println("Android Notification Response : " + reader.readLine());
                } else if (status == 401) {
                    //client side error
                    System.out.println("Notification Response : TokenId : " + regId.toString() + " Error occurred :");
                } else if (status == 501) {
                    //server side error
                    System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + regId.toString());
                } else if (status == 503) {
                    //server side error
                    System.out.println("Notification Response : FCM Service is Unavailable TokenId : " + regId.toString());
                }
            }
        } catch (MalformedURLException mlfexception) {
            // Prototcal Error
            System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());
        } catch (IOException mlfexception) {
            //URL problem
            System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());
        } catch (Exception exception) {
            //General Error or exception.
            System.out.println("Error occurred while sending push Notification!.." + exception.getMessage());
        }


    }

    @Override
    protected String doInBackground(String... strings) {
        try {
// Create URL instance.
            URL url = new URL(FCM_URL);
// create connection.
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
//set method as POST or GET
            conn.setRequestMethod("POST");
//pass FCM server key
            conn.setRequestProperty("Authorization", "key=" + constants.Server_key);
//Specify Message Format
            conn.setRequestProperty("Content-Type", "application/json");
//Create JSON Object & pass value
            JSONObject infoJson = new JSONObject();

            infoJson.put("title", strings[0]);
            infoJson.put("body", strings[1]);

            JSONObject json = new JSONObject();
            //json.put("to", tokenId.trim()); modified to the one below
            json.put("to", strings[2].trim());
            json.put("notification", infoJson);
            json.put("priority","high");
            json.put("sound","enabled");
            json.put("volume" , "3.21.15");
            json.put("click_action","io.eyec.bombo.semanepreschool_TARGET_NOTIFICATION");
            System.out.println("json :" + json.toString());
            System.out.println("infoJson :" + infoJson.toString());
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            int status = 0;
            if (null != conn) {
                status = conn.getResponseCode();
            }
            if (status != 0) {

                if (status == 200) {
//SUCCESS message
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    System.out.println("Android Notification Response : " + reader.readLine());
                } else if (status == 401) {
//client side error
                    System.out.println("Notification Response : TokenId : " + strings[2] + " Error occurred :");
                } else if (status == 501) {
//server side error
                    System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + strings[2]);
                } else if (status == 503) {
//server side error
                    System.out.println("Notification Response : FCM Service is Unavailable TokenId : " + strings[2]);
                }
            }
        } catch (MalformedURLException mlfexception) {
// Prototcal Error
            System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());
        } catch (Exception mlfexception) {
//URL problem
            System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());
        }

        return null;
    }
}



