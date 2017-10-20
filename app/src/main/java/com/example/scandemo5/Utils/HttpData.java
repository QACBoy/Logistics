package com.example.scandemo5.Utils;

import android.util.Log;

import com.example.scandemo5.MyApp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sam on 2017/9/17.
 */

public class HttpData {

    private static HttpData httpdata;
    private HttpData(){  }
    public static HttpData getInstance() {
        if(httpdata == null){
            synchronized (MyApp.getContext()) {
                httpdata = new HttpData();
            }
        }
        return httpdata;
    }

    private int Success = 0;
    private int Fail = 1;
    private int Unknown = 2;

    private CallBack _callback;
    private boolean isFinish = false;

    private int GoodState = Unknown;
    private int ProcureState = Unknown;

    public interface CallBack{  //弹窗回调
        void done(boolean isSuccess);
    }

    public void GetHttpData(CallBack callBack) throws Exception{

        isFinish = false;
        _callback = callBack;

        //商品信息
        Http.getInstance().Get_goods_info(new Http.Callback() {
            @Override
            public void done(String data) {
                if(!"NetError".equals(data)) {
//                    data = Global.DealXmlStr(data);
                    Log.d("1235", "done: 开始插入商品信息" + data);
                    SQLite.getInstance().InsertGoodsAll(data);
                    Log.d("1235", "done: 商品信息更新完成");
                    GoodState = Success;
                }else { GoodState = Fail; }
            }
        });

//        //供货商信息
//        Http.getInstance().Post(Http.getInstance().get_procure_list, null, new Http.Callback() {
//            @Override
//            public void done(String data) {
//                if(data != null && !"NetError".equals(data)) {
////                    data = Global.DealXmlStr(data);
//                    Log.d("1235", "done: 开始插入" + data);
//                    SQLite.getInstance().InsertProcureAll(data);
//                    Log.d("1235", "done: 供货商更新完成");
//                    ProcureState = Success;
//                }else { ProcureState = Fail; }
//            }
//        });

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (!isFinish){
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
//                            if(GoodState == Success && ProcureState == Success){
//                                isFinish = true;
//                                _callback.done(true);
//                            }
//                            if(GoodState == Fail || ProcureState == Fail){
//                                isFinish = true;
//                                _callback.done(false);
//                            }
                            if(GoodState == Success){
                                isFinish = true;
                                _callback.done(true);
                            }
                            if(GoodState == Fail){
                                isFinish = true;
                                _callback.done(false);
                            }
                        }
                    }
                }
        ).start();
    }
}
