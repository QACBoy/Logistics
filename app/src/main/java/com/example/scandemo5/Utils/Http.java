package com.example.scandemo5.Utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scandemo5.Activity.MainActivity;
import com.example.scandemo5.MyApp;

import java.io.UnsupportedEncodingException;
import java.util.Map;


import static android.content.ContentValues.TAG;

/**
 * Created by CJ on 2017/6/7.
 */

public class Http {   //单例化模式

    private RequestQueue mqueue;
    public String get_goods_info = "";// 用于测试的url "http://192.168.1.166/WebService1.asmx/show"
    public String access = "";
    public String get_procure_list = "";
    public String get_rk_detail = "";
    public String get_stock = "";
    public String test_json = "";

    public String get_location = "http://119.29.223.148/dms/public/location/get/";

    private static Http http;


    private Http(){
        mqueue = Volley.newRequestQueue(MyApp.getContext());
        get_goods_info = "http://" + Global.getSharedPreferences().getString("url",null) + "/webservice/n_webservice.asmx/get_goods_info";
        access = "http://" + Global.getSharedPreferences().getString("url",null) + "/webservice/n_webservice.asmx/access";
        get_procure_list = "http://" + Global.getSharedPreferences().getString("url",null) + "/webservice/n_webservice.asmx/get_procure_list";
        get_rk_detail = "http://" + Global.getSharedPreferences().getString("url",null) + "/webservice/n_webservice.asmx/get_rk_detail";
        get_stock = "http://" + Global.getSharedPreferences().getString("url",null) + "/webservice/n_webservice.asmx/get_stock";
        test_json = "http://" + Global.getSharedPreferences().getString("url",null) + "/webservice/n_webservice.asmx/test_json";
    }

    public static Http getInstance() {
        if(http == null){
            synchronized (MyApp.getContext()) {
                http = new Http();
            }
        }
        return http;
    }

    public interface Callback{   //定义回调接口
         void done(String data);
    }


    public void Post(String url, final Map map, final Callback callback){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,      //  (1)服务器IP
                new Response.Listener<String>() {   // (2) Volley的监听器
                    @Override
                    public void onResponse(String s) {  //回调函数，返回服务器回应的信息
                        callback.done(s); //调用回调函数，实现异步
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onErrorResponse: "+volleyError.getMessage());
                        callback.done("NetError");
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }

            protected  Response<String> parseNetworkResponse(NetworkResponse response){
                try {
                    String jsonString = new String(response.data, "UTF-8");
                    return Response.success(
                            jsonString,
                            HttpHeaderParser.parseCacheHeaders(response)
                    );
                }catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }catch (Exception ee) {
                    return Response.error(new ParseError(ee));
                }
            }
        };    //StringRequest stringRequest = new.....结束
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(200,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));  //超时设置
        mqueue.add(stringRequest);    //把请求放到队列中
    }

    public void Get(String url, final Callback callback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,      //  (1)服务器IP
                new Response.Listener<String>() {   // (2) Volley的监听器
                    @Override
                    public void onResponse(String s) {  //回调函数，返回服务器回应的信息
                        callback.done(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onErrorResponse: "+volleyError.getMessage());
                        callback.done("NetError");
                    }
                }
        ){
            protected  Response<String> parseNetworkResponse(NetworkResponse response){
                try {
                    String jsonString = new String(response.data, "UTF-8");
                    return Response.success(
                            jsonString,
                            HttpHeaderParser.parseCacheHeaders(response)
                    );
                }catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }catch (Exception ee) {
                    return Response.error(new ParseError(ee));
                }
            }
        };    //StringRequest stringRequest = new.....结束
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(200,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));  //超时设置
        mqueue.add(stringRequest);    //把请求放到队列中
    }


}
