package com.example.zkapp_identity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Tools {
    public static void main(String[] args) throws  Exception{
        String host = "https://goexpress.market.alicloudapi.com";       //�����ַ  ֧��http �� https �� WEBSOCKET
        String path = "/goexpress";                                     //��׺
        String appcode = "642bbbab91484ecfa6227d3128dc0f7c";                             //AppCode  ���Լ���AppCode ��������Ĳ鿴
        String no = "780098068058";                                     //�������������api�ӿڲ���
        String type = "zto";                                            //�������������api�ӿڲ���
        String urlSend = host + path + "?no=" + no + "&type=" + type;   //ƴ����������
        URL url = new URL(urlSend);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);//��ʽAuthorization:APPCODE (�м���Ӣ�Ŀո�)
        int httpCode = httpURLConnection.getResponseCode();
        String json = read(httpURLConnection.getInputStream());
        System.out.println("/* ��ȡ��������Ӧ״̬�� 200 ������400 Ȩ�޴��� �� 403 �������ꣻ */ ");
        System.out.println(httpCode);
        System.out.println("/* ��ȡ���ص�json   */ ");
        System.out.print(json);
    }
    /*
        ��ȡ���ؽ��
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
