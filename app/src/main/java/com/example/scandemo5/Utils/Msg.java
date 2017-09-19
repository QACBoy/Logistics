package com.example.scandemo5.Utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.scandemo5.Activity.MainActivity;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.feezu.liuli.timeselector.TimeSelector;

/**
 * Created by Sam on 2017/9/17.
 */

public class Msg {

    public interface CallBack{  //弹窗回调
        void confirm(DialogPlus dialog);
    }
    public static void showMsg(final Activity activity, final String title, final String msg, final CallBack callBack){
        DialogPlus dialog = DialogPlus.newDialog(activity)
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
        dialog.show();
    }

    //连续扫描

    private static boolean ifSave;

    public static void showSacn(final Activity activity, final JMap<String,String> map){
        ifSave = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
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
                            public View getView(int position, View convertView, ViewGroup parent) {
                                convertView = LayoutInflater.from(activity).inflate(R.layout.handle_item, null);

                                TextView tKey = (TextView) convertView.findViewById(R.id.handle_item_key);
                                EditText tValue = (EditText) convertView.findViewById(R.id.handle_item_value);

                                if(position >= map.size() - 5) {
                                    tKey.setText(RMap.getrMap().get(map.get(position)));
                                    tValue.setText(map.get(map.get(position)));

                                    if (position - 4 > 2) {
                                        tValue.setFocusableInTouchMode(false);
                                        tValue.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(final View v) {
                                                TimeSelector timeSelector = new TimeSelector(activity, new TimeSelector.ResultHandler() {
                                                    @Override
                                                    public void handle(String time) {
                                                        ((EditText) v).setText(time.substring(0, 10));
                                                    }
                                                }, "2015-01-01 00:00", "2030-12-31 24:00");
                                                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                                                timeSelector.setIsLoop(true);
                                                timeSelector.show();
                                            }
                                        });
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
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
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
