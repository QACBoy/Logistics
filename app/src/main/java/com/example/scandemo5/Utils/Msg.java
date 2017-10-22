package com.example.scandemo5.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.scandemo5.Activity.Storage.MainActivity;
import com.example.scandemo5.Data.ClintInfo;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.MyApp;
import com.example.scandemo5.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.List;

/**
 * Created by Sam on 2017/9/17.
 */

public class Msg {

    private static long Sleep_time = 400; //ms

    public interface CallBack{  //弹窗回调
        void confirm(DialogPlus dialog);
    }
    public static void showMsg(final Activity activity, final String title, final String msg, final CallBack callBack){
        if(Global.dialog != null)Global.dialog.dismiss();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Global.dialog = DialogPlus.newDialog(activity)
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .setGravity(Gravity.CENTER)
                        .setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return 1;
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return position;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                convertView = activity.getLayoutInflater().inflate(R.layout.show_msg,null);
                                ((TextView)convertView.findViewById(R.id.title_msg)).setText(title);
                                ((TextView)convertView.findViewById(R.id.content_msg)).setText(msg);
                                return convertView;
                            }
                        })
                        .setFooter(R.layout.msg_foot)
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                switch (view.getId()){
                                    case R.id.msg_footer_close_button://点击关闭按钮
                                        dialog.dismiss();
                                        break;
                                    case R.id.msg_footer_confirm_button://点击保存按钮
                                        if(callBack != null)
                                            callBack.confirm(dialog);
                                        else
                                            dialog.dismiss();
                                        break;
                                }
                            }
                        })
                        .create();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Global.dialog.show();
                    }
                });
            }
        }).start();
    }

    public static void showMsg(final Activity activity,final String title, final String msg, final Bitmap image, final View.OnClickListener callBack){
        if(Global.dialog != null)Global.dialog.dismiss();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Global.dialog = DialogPlus.newDialog(activity)
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .setGravity(Gravity.TOP)
                        .setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return 1;
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return position;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                convertView = activity.getLayoutInflater().inflate(R.layout.show_msg,null);
                                ((TextView)convertView.findViewById(R.id.title_msg)).setText(title);
                                ((TextView)convertView.findViewById(R.id.content_msg)).setText(msg);
                                ((ImageView)convertView.findViewById(R.id.image_mag)).setImageBitmap(image);
                                return convertView;
                            }
                        })
                        .create();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Global.dialog.show();
                    }
                });
            }
        }).start();
    }

    public static void showFunciton(final Activity activity, final String title, final List dates, final OnItemClickListener callBack){
        if(Global.dialog != null)Global.dialog.dismiss();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Global.dialog = DialogPlus.newDialog(activity)
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .setGravity(Gravity.CENTER)
                        .setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return dates.size();
                            }

                            @Override
                            public Object getItem(int position) {
                                return dates.get(position);
                            }

                            @Override
                            public long getItemId(int position) {
                                return position;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                convertView  = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.noimage_item_view, parent, false);
                                ClintInfo data = (ClintInfo) dates.get(position);
                                ((TextView) convertView.findViewById(R.id.title)).setText(data.client_no);
                                ((TextView) convertView.findViewById(R.id.author)).setText(data.client_name);
                                return convertView;
                            }
                        })
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                if(callBack != null)
                                    callBack.onItemClick(dialog,item,view,position);
                            }
                        })
                        .create();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Global.dialog.show();
                    }
                });
            }
        }).start();
    }

    //连续扫描
    private static boolean ifSave;

    public static void showSacn(final Activity activity, final JMap<String,String> map){
        ifSave = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Global.dialog = DialogPlus.newDialog(activity)
                        .setGravity(Gravity.CENTER)
                        .setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return map.size();
                            }

                            @Override
                            public Object getItem(int position) {
                                return map.get(position);
                            }

                            @Override
                            public long getItemId(int position) {
                                return position;
                            }

                            @Override
                            public View getView(final int position, View convertView, ViewGroup parent) {
                                convertView = LayoutInflater.from(activity).inflate(R.layout.handle_item, null);

                                TextView tKey = (TextView) convertView.findViewById(R.id.handle_item_key);
                                EditText tValue = (EditText) convertView.findViewById(R.id.handle_item_value);
                                tValue.setInputType(InputType.TYPE_CLASS_NUMBER);

                                if(position >= map.size() - 5) {
                                    tKey.setText(RMap.getrMap().get(map.get(position)));
                                    tValue.setText(map.get(map.get(position)));


                                    //,,对库位编号的点击添加事件(扫描结果填写)
                                    if(position - 4 == 2){
                                        tValue.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                MainActivity.mainActivity.LocationNo_EditText = v;
                                                Global.setTYPE_SCA(Global.ScanType.rk_LocationNo);
                                                return false;
                                            }
                                        });
                                    }


                                    if (position - 4 > 2) {
                                        tValue.setFocusableInTouchMode(false);
                                        tValue.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(final View v) {
                                                TimeSelector timeSelector = new TimeSelector(activity, new TimeSelector.ResultHandler() {
                                                    @Override
                                                    public void handle(String time) {
                                                        time = time.substring(0,10);
                                                        ((EditText)v).setText(time);
                                                        SQLite.Goods goods = SQLite.getInstance().getGoodsByGoodNo(map.get("goods_no"));
                                                        if(position - 4 == 3){
                                                            //出厂日期
                                                            ((EditText)(v.getRootView().findViewById(R.id.ids_EXP).findViewById(R.id.handle_item_value))).setText(DateDeal.add(time,Integer.parseInt(goods.ex_day)));
                                                        }else if(position - 4 == 4){
                                                            //到期日期
                                                            ((EditText)(v.getRootView().findViewById(R.id.ids_MFG).findViewById(R.id.handle_item_value))).setText(DateDeal.reduce(time,Integer.parseInt(goods.ex_day)));
                                                        }
                                                    }
                                                }, "2015-01-01 00:00", "2030-12-31 24:00");
                                                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                                                timeSelector.setIsLoop(true);
                                                timeSelector.show();
                                            }
                                        });
                                    }else {
                                        tValue.setSingleLine();
                                        tValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                                    }
                                    switch (position - 4) {
                                        case 0:
                                            convertView.setId(R.id.ids_quantity);
                                            break;
                                        case 1:
                                            convertView.setId(R.id.ids_LOT);
                                            break;
                                        case 2:
                                            convertView.setId(R.id.ids_location_no);
                                            break;
                                        case 3:
                                            convertView.setId(R.id.ids_MFG);
                                            break;
                                        case 4:
                                            convertView.setId(R.id.ids_EXP);
                                            break;
                                    }
                                }else {
                                    convertView.setId(position);
                                    tKey.setText(RMap.getrMap().get(map.get(position)));
                                    tValue.setText(map.get(map.get(position)));
                                }
                                return convertView;
                            }
                        })
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .setFooter(R.layout.dialog_foot).setContentBackgroundResource(R.color.balck).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                switch (view.getId()){
                                    case R.id.footer_close_button://点击关闭按钮
                                        ifSave = false;
                                        dialog.dismiss();
                                        break;
                                    case R.id.footer_confirm_button://点击保存按钮
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        })
                        .setOnBackPressListener(new OnBackPressListener() {
                            @Override
                            public void onBackPressed(DialogPlus dialogPlus) {
                                dialogPlus.dismiss();
                            }
                        })
                        .setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogPlus dialog) {
                                if(ifSave)
                                    addtoMainactivity(dialog);
                                Global.setTYPE_SCA(Global.ScanType.rk_GoodsNo);
                            }
                        })
                        .create();
                MainActivity.mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Global.dialog.show();
                    }
                });
            }
        }).start();
    }

    private static void addtoMainactivity(DialogPlus dialog){
        String barcode = ((EditText) dialog.getHolderView().findViewById(new Integer(2)).findViewById(R.id.handle_item_value)).getText().toString();
        String goods_no = ((EditText) dialog.getHolderView().findViewById(new Integer(0)).findViewById(R.id.handle_item_value)).getText().toString();
        String goods_name = ((EditText) dialog.getHolderView().findViewById(new Integer(1)).findViewById(R.id.handle_item_value)).getText().toString();
        String sl = ((EditText)(dialog.getHolderView().findViewById(R.id.ids_quantity).findViewById(R.id.handle_item_value))).getText().toString();
        String pc = ((EditText)(dialog.getHolderView().findViewById(R.id.ids_LOT).findViewById(R.id.handle_item_value))).getText().toString();
        String kw = ((EditText)(dialog.getHolderView().findViewById(R.id.ids_location_no).findViewById(R.id.handle_item_value))).getText().toString();
        String sc = ((EditText)(dialog.getHolderView().findViewById(R.id.ids_MFG).findViewById(R.id.handle_item_value))).getText().toString();
        String dq = ((EditText)(dialog.getHolderView().findViewById(R.id.ids_EXP).findViewById(R.id.handle_item_value))).getText().toString();
        MainActivity.mainActivity.addScanDataEnd(new UpLoad.ScanData(barcode, goods_no, goods_name, sc, dq, pc,kw, sl));
    }
}
