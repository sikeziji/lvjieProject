package com.yzlm.cyl.cfragment.Dialog.Component;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Dialog.ZDialogFragment;
import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.lang.ref.WeakReference;

import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.getNowSystemTime;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mCompName;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCmds;

/**
 * Created by caoyiliang on 2017/1/18.
 */

public class Dialog_FunctionTest extends ZDialogFragment {
    private static TextView mTxtTemp, mTxtTime, mTxtPressure;
    private static Callbacks mCallbacks;
    private String Command;
    private String Component;
    //  private Handler funcTestHandler;
    private long firstClick;
    // 计算点击的次数
    private int count;
    private Handler mHandler;

    @Override
    protected int getDialogView() {
        return R.layout.dialog_functiontest;
    }

    @Override
    protected void InitUI(View view) {

        Command = getPrompt("alert-FunctionTest").split("_")[1];
        Component = getPrompt("alert-FunctionTest").split("_")[0];
        TextView mD_text = (TextView) view.findViewById(R.id.D_text);
        mD_text.setText(getString(R.string.component) + "[" + Component + "]" + getString(R.string.doing) + "[" + Command + "]...");

        mTxtTemp = (TextView) view.findViewById(R.id.txtFunTestTemp);
        mTxtTime = (TextView) view.findViewById(R.id.txtFunTestTime);
        mTxtPressure = (TextView) view.findViewById(R.id.txtFunTestPressure);
        if (QueryMeasCateg(mCompName).equals("4")) {
            mTxtPressure.setVisibility(View.GONE);
        } else {
            mTxtPressure.setVisibility(View.VISIBLE);
        }
        ImageButton mImageButton_jjtz = (ImageButton) view.findViewById(R.id.dialog_jjtz);
        mImageButton_jjtz.setOnClickListener(new BtnOKClick());
        new Thread(new currActionIsDone()).start();

        mHandler = new MyHandler(MainActivity.main);
    }


    private static void functionTestMsgHandler(Message msg) {
        try {
            if (msg.what == 100) {

                if (mTxtTemp != null)
                    if (QueryMeasCateg(mCompName).equals("3") && mCompName.equals("CODmn")){
                        mTxtTemp.setText(context.getString(R.string.Outside_temperature) +  ":" + (getCmds(mCompName).getCmd(55).getValue() == null ? "—" : String.format("%.1f ℃", (float) getCmds(mCompName).getCmd(55).getValue())) + "\n" + context.getString(R.string.Inner_temperature) +  ":" + (getCmds(mCompName).getCmd(601).getValue() == null ? "—" : String.format("%.1f ℃", (float) getCmds(mCompName).getCmd(601).getValue())));
                    }else {
                        mTxtTemp.setText(context.getString(R.string.temperature) + ": " + (getCmds(mCompName).getCmd(55).getValue() == null ? "--" : String.format("%.1f ℃", (float) getCmds(mCompName).getCmd(55).getValue())));
                    }
                if (mTxtTime != null)
                    mTxtTime.setText(context.getString(R.string.func_action_time) + ":" + (getCmds(mCompName).getCmd(62).getValue() == null ? "--" : getCmds(mCompName).getCmd(62).getValue() + "s"));
                if (mTxtPressure != null) {
                    if (!(getCmds(mCompName).getCmd(9010).getValue() == null)) {
                        if (Integer.parseInt(String.valueOf(getCmds(mCompName).getCmd(9010).getValue())) != 0) {
                            mTxtPressure.setVisibility(getCmds(mCompName).getCmd(9010).getValue() == null ? (View.GONE) : (View.VISIBLE));
                            mTxtPressure.setText(context.getString(R.string.pressure) + ": " + getCmds(mCompName).getCmd(9010).getValue() + "KPa");
                        }
                    }
                }
            } else {
                if (mCallbacks != null) {
                    mCallbacks.onDialogRS();
                }
            }
        } catch (Exception e) {
            Log.i("exception", "functionTest");
        }
    }

    @Override
    protected void dialogCancel() {

    }

    public interface Callbacks {
        void onDialogRS();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    protected boolean setCanceledOnTouchOutside() {
        return false;
    }

    @Override
    protected String getTitleName() {
        return null;
    }

    @Override
    protected String getPositiveButtonName() {
        return null;
    }

    @Override
    protected String getNegativeButtonName() {
        return null;
    }

    @Override
    protected void PositiveButtonListener() {

    }

    @Override
    protected void NegativeButtonListener() {

    }

    private class currActionIsDone implements Runnable {
        @Override
        public void run() {
            int times = 0, actionTime = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                    actionTime++;
                    ActionTable at = Global.getActions(Component);
                    if (at != null) {
                        String currAction = at.GetActionName(getCmds(Component).getCmd(52).getValue());
                        if (!currAction.equals(Command)) {
                            if ((times++ > 5 && actionTime < 10) || actionTime > 10) {

                                dialog.dismiss();

                                //W200,且是"开采样"动作
                                if (QueryMeasBoardType(Component).equals("2") && Command.equals(getString(R.string.open_pump))){
                                    //关采样
                                    SendManager.SendCmd("IO" + "_" + "接口板停止_06_02", S1, 3, 500, new byte[]{0x00,0x00,0x00,0x00});
                                }

                                mHandler.sendMessage(mHandler.obtainMessage(101, getNowSystemTime()));
                                break;
                            }
                        } else {
                            if (actionTime > 600) {
                                dialog.dismiss();

                                //W200,且是"开采样"动作
                                if (QueryMeasBoardType(Component).equals("2") && Command.equals(getString(R.string.open_pump))){
                                    //关采样
                                    SendManager.SendCmd("IO" + "_" + "接口板停止_06_02", S1, 3, 500, new byte[]{0x00,0x00,0x00,0x00});
                                }

                                mHandler.sendMessage(mHandler.obtainMessage(101, getNowSystemTime()));
                                break;
                            }
                        }
                    }

                    mHandler.sendMessage(mHandler.obtainMessage(100, getNowSystemTime()));
                    if (QueryMeasCateg(mCompName).equals("4")) {
                        byte[] arrayOfByte3 = DataUtil.shortToByte((short) 9010);
                        DataUtil.reverse(arrayOfByte3);
                        SendManager.SendCmd(mCompName + "_查voc测量参数" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 3, 200, 1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class BtnOKClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
            if (firstClick != 0 && System.currentTimeMillis() - firstClick > 300) {
                count = 0;
            }
            count++;
            if (count == 1) {
                firstClick = System.currentTimeMillis();
            } else if (count == 2) {
                long lastClick = System.currentTimeMillis();
                // 两次点击小于300ms 也就是连续点击
                if (lastClick - firstClick < 300) {// 判断是否是执行了双击事件
                    //System.out.println(">>>>>>>>执行了双击事件");
                    SendManager.SendCmd(Component + "_紧急停止" + "_8_07", S0, 3, 1000, null);

                    //W200,且是"开采样"动作
                    if (QueryMeasBoardType(Component).equals("2") && Command.equals(getString(R.string.open_pump))){
                        //关采样
                        SendManager.SendCmd("IO" + "_" + "接口板停止_06_02", S1, 3, 500, new byte[]{0x00,0x00,0x00,0x00});
                    }

                    //dialog.dismiss();
                    //mCallbacks.onDialogRS();
                }
            }
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        MyHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            System.out.println(msg);
            functionTestMsgHandler(msg);
        }
    }

}
