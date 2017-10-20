package com.example.scandemo5.Utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scandemo5.Activity.WelcomeActivity;
import com.example.scandemo5.Data.ClintInfo;
import com.example.scandemo5.Data.UserInfo;
import com.example.scandemo5.MyApp;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import static android.content.ContentValues.TAG;

/**
 * Created by CJ on 2017/6/7.
 */

public class Http {   //单例化模式

    public final String Success = "1";
    public final String Fail = "-1";

    private RequestQueue mqueue;
    public String get_goods_info = "";// 用于测试的url "http://192.168.1.166/WebService1.asmx/show"
    public String access = "";
    public String get_client_info = "";
    public String move_goods = "";
    public String get_procure_list = "";
    public String get_rk_detail = "";
    public String get_stock = "";
    public String test_json = "";

    public String get_location = "http://119.29.223.148/dms/public/location/get/";
    public String get_distribution = "http://119.29.223.148/dms/public/distribution/get/";//20171019012
    public String address_distribution = "http://119.29.223.148/dms/public/distribution/address/";//20171019012
    private static Http http;


    private Http(){
        mqueue = Volley.newRequestQueue(MyApp.getContext());
        access = "http://" + Global.getSharedPreferences().getString("url",null) + "/webservice/n_webservice.asmx/access";
        get_client_info = "http://" + Global.getSharedPreferences().getString("url",null) + "/webservice/n_webservice.asmx/get_client_info";
        move_goods = "http://" + Global.getSharedPreferences().getString("url",null) + "/webservice/n_webservice.asmx/move_goods";
        get_goods_info = "http://" + Global.getSharedPreferences().getString("url",null) + "/webservice/n_webservice.asmx/get_goods_info";
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
    public interface OBJCallback<T>{   //定义回调接口
        void done(String isSuccess,List<T> data);
    }

    public String DealXmlStr(String str){ //处理网络返回xml文件函数
        int start = str.indexOf("\">") + 2;
        int end = str.lastIndexOf("</");
        return str.substring(start,end);
    }

    public void Post(String url, final Map map, final Callback callback){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,      //  (1)服务器IP
                new Response.Listener<String>() {   // (2) Volley的监听器
                    @Override
                    public void onResponse(String s) {  //回调函数，返回服务器回应的信息
                        s = DealXmlStr(s);
                        if (!Global.isNullorEmpty(s))
                            callback.done(s); //调用回调函数，实现异步
                        else
                            Toast.makeText(MyApp.getContext(),"网络异常，请检查网络",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onErrorResponse: "+volleyError.getMessage());
                        callback.done("NetError");
                        Toast.makeText(MyApp.getContext(),"网络连接失败，请检查网络",Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "getParams: " + map.size());
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
                        s = DealXmlStr(s);
                        if (!Global.isNullorEmpty(s))
                            callback.done(s); //调用回调函数，实现异步
                        else
                            Toast.makeText(MyApp.getContext(),"网络异常，请检查网络",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onErrorResponse: "+volleyError.getMessage());
                        callback.done("NetError");
                        Toast.makeText(MyApp.getContext(),"网络连接失败，请检查网络",Toast.LENGTH_LONG).show();
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

    //接口函数

    public void access(String username, final String password, final Callback callback){
        Map map = new HashMap();
        map.put("as_user",username);
        map.put("as_password", Encryption.md5(password));
        User.getUser().setUsername(username);
        Http.getInstance().Post(access, map, new Http.Callback() {
            @Override
            public void done(String data) {
                if ("-1".equals(data)) {
                    Toast.makeText(MyApp.getContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    callback.done(Fail);
                }
                else if("NetError".equals(data)){
                    Log.d("", "done: " + data);
                    callback.done("0");
                }
                else {
                    Global.getSharedPreferences().edit().putBoolean("isLogin", true).commit();
                    List<UserInfo> userInfo = DJson.JsonToList(data,UserInfo.class);
                    User.getUser().setPassword(password);
                    User.getUser().setAs_user(userInfo.get(0).as_user);
                    User.getUser().setGroup_node_id(userInfo.get(0).group_node_id);
                    callback.done(Success);
                }
            }
        });
    }

    public void Get_client_info(String key, final OBJCallback callback){
        Map map = new HashMap();
        map.put("as_user",User.getUser().getUsername());
        map.put("as_password", User.getUser().getPassword());
        map.put("as_key", key);
        Http.getInstance().Post(get_client_info, map, new Callback() {
            @Override
            public void done(String data) {
                if("无数据".equals(data)){
                    Toast.makeText(MyApp.getContext(),"未找到该供应商",Toast.LENGTH_LONG).show();
                    callback.done(Fail,null);
                }else {
                    List<ClintInfo> Procures = DJson.JsonToList(data,ClintInfo.class);
                    callback.done(Success,Procures);
                }
            }
        });
    }

    public void Get_goods_info(final Callback callback){
        Map map = new HashMap();
        map.put("as_user",User.getUser().getUsername());
        map.put("as_password",User.getUser().getPassword());
        Http.getInstance().Post(get_goods_info,map, new Http.Callback() {
            @Override
            public void done(String data) {
                callback.done(data);
            }
        });
    }

}
