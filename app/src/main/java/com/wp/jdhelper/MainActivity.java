package com.wp.jdhelper;
import android.os.Handler;
import android.os.Message;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wp.jdhelper.network.HttpHelper;
import com.wp.jdhelper.network.JDReq;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText mItemIdText;

    private Button mGetButton;

    private Button mJumpButton;

    private TextView mResultText;


    private String mRebateUrl = "";

    private String mRealUrl = "";

    private RadioGroup mRadioGroup;

    private RadioButton mMainRadioButton;

    private String mJDMall = "com.jingdong.app.mall";

    private String JDGoodsId = "4099139";       //--小米6详情页

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    public String jdAppStr_goods = "openApp.jdMobile://virtual?params={\"category\":\"jump\",\"des\":\"productDetail\",\"skuId\":\""+JDGoodsId+"\",\"sourceType\":\"JSHOP_SOURCE_TYPE\",\"sourceValue\":\"JSHOP_SOURCE_VALUE\"}";
    public String jdAppStr_goods2 ="openapp.jdmobile://virtual?params={\"category\":\"jump\",\"des\":\"productDetail\",\n" +
            "\"skuId\":\"16636107317\",\"sourceType\":\"MWEIXIN_PRODUCTFLOAT_TYPE\",\"sourceValue\":\"MWEIXIN_PRODUCTFLOAT_VALUE\",\"M_sourceFrom\":\"sxbanner\",\n" +
            "\"msf_type\":\"click\",\"m_param\":{\"m_source\":\"0\",\"event_series\":{},\"jda\":\"9201833.615084006.1567766952.1570723020.1570786579.33\",\"usc\":\"kong\",\n" +
            "\"ucp\":\"t_1000437911_\",\"umd\":\"jingfen\",\"utr\":\"b0ecf019fc4b45f78763448e314de821\",\"jdv\":\"9201833|kong|t_1000437911_|jingfen|\n" +
            "b0ecf019fc4b45f78763448e314de821|1570786940820\",\"ref\":\"https://item.m.jd.com/product/16636107317.html?wxa_abtest=b&amp;ad_od=share&amp;&\n" +
            "amp;&amp;&amp;&cu=true&utm_source=kong&utm_medium=jingfen&utm_campaign=t_1000437911_&utm_term=b0ecf019fc4b45f78763448e314de821\",\n" +
            "\"psn\":\"615084006|33\",\"psq\":2,\n" +
            "\"unpl\":\"V2_ZzNtbRFWF0ciCUZcLEtZVmIHRVtLUUQXcA1PAHkZWVAzChFdclRCFX0URlVnGVwUZwUZWEFcQx1FCEZkexhdBGIBFFVCV3MlcAtBU3opXwVXAiJaQlNCHHAJR1NLKVwN\n" +
            "YDMSXENWQhJxCUdVfhxs3cGYxPjGj8urRQl2VUsYbFMJAxNcQ1dBF3QBRhl8GVgEbgYTXEVnQiV2\",\"pc_source\":\"\",\"mba_muid\":\"615084006\",\n" +
            "\"mba_sid\":\"15707865790603328812676376900\",\"std\":\"MO-J2011-1\",\"par\":\"wxa_abtest=b&amp;ad_od=share&amp;&amp;&amp;&amp;&cu=true&\n" +
            "utm_source=kong&utm_medium=jingfen&utm_campaign=t_1000437911_&utm_term=b0ecf019fc4b45f78763448e314de821\",\n" +
            "\"event_id\":\"MDownLoadFloat_TopOldExpo\",\"mt_xid\":\"\",\"mt_subsite\":\"\"},\"SE\":{\"mt_subsite\":\"\",\"__jdv\":\"9201833|kong|t_1000437911_|jingfen|\n" +
            "b0ecf019fc4b45f78763448e314de821|1570786940820\",\n" +
            "\"unpl\":\"V2_ZzNtbRFWF0ciCUZcLEtZVmIHRVtLUUQXcA1PAHkZWVAzChFdclRCFX0URlVnGVwUZwUZWEFcQx1FCEZkexhdBGIBFFVCV3MlcAtBU3opXwVXAiJaQlNCHHAJR1NLKVwN\n" +
            "YDMSXENWQhJxCUdVfhxs3cGYxPjGj8urRQl2VUsYbFMJAxNcQ1dBF3QBRhl8GVgEbgYTXEVnQiV2\",\n" +
            "\"__jda\":\"9201833.615084006.1567766952.1570723020.1570786579.33\"},\"wxa_abtest\":\"b\"}";
    public String jdWebStr_goods = "https://item.m.jd.com/product/"+JDGoodsId+".html";

    private static final String URL = "https://api.open.21ds.cn/jd_api_v1/getitemcpsurl?apkey=d6315632-59d5-c330-a01b-7703a6a697cd";

    private static final int INPUT_ERROR = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INPUT_ERROR:
                    Toast.makeText(MainActivity.this, "未输入返利地址" , Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        Intent intent2  = new Intent();

        intent2.setAction("com.miui.cloudservice.settings_search_proxy");

        intent2.putExtra("proxied_type","find_device");

        Log.i("123", intent2.toUri(0));
        mItemIdText = findViewById(R.id.item_id);
        mGetButton = findViewById(R.id.button);
        mJumpButton = findViewById(R.id.jump_goods);
        mJumpButton.setVisibility(View.INVISIBLE);
        mRadioGroup = findViewById(R.id.radio_group);
        mMainRadioButton = findViewById(R.id.main);
        mGetButton.setOnClickListener((v) -> {
            Runnable httpReq = () -> {
                try {
                    String url;
                    if ((url = getUrl()) == null) {
                        Message msg = Message.obtain();
                        msg.what = INPUT_ERROR;
                        mHandler.sendMessage(msg);
                        return;
                    }
                    mRebateUrl = HttpHelper.sendGet(url);
                    mRebateUrl = mRebateUrl.substring(mRebateUrl.indexOf("shortURL\":") + 11 , mRebateUrl.indexOf("\"},\"msg"));
                    mRebateUrl = mRebateUrl.replace("\\", "");
                    System.out.print("mRebateUrl: " + mRebateUrl);
                    if (!TextUtils.isEmpty(mRebateUrl)) {
                        mJumpButton.post(() -> mJumpButton.setVisibility(View.VISIBLE));
                    }
        mResultText = findViewById(R.id.result);
        mJumpButton = findViewById(R.id.jump_goods);
        mGetButton.setOnClickListener((v) -> {
            Runnable httpReq = () -> {
                try {
//                    string = HttpHelper.sendGet(getUrl());
                    string = HttpHelper.getRealUrl("https://u.jd.com/jgxB39");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            new Thread(httpReq).start();
        });

        mJumpButton.setOnClickListener((v) -> {
            Runnable httpReq = () -> {
                try {
                    mRealUrl = HttpHelper.getRealUrl(mRebateUrl);
                    String intentStr = getRealIntent(mRealUrl);
                    System.out.print(intentStr);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentStr));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            new Thread(httpReq).start();
        });

        mJumpButton.setOnClickListener((v) -> {
//            if (isInstallByread(mJDMall)) {
//                Toast.makeText(MainActivity.this, "京东已经安装", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(jdAppStr_goods2));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            } else {
//                Toast.makeText(MainActivity.this, "京东没有安装", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(jdWebStr_goods));
//                startActivity(intent);
//            }
            Runnable httpReq = () -> {
                try {
//                    string = HttpHelper.sendGet(getUrl());
                    JDReq.getQRCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            new Thread(httpReq).start();
        });

    }


    private String getRealIntent(String temp) {
        String mSkuId;
        if (temp.contains("product")) {
            mSkuId = temp.substring(temp.indexOf("product/") + 8, temp.indexOf(".html"));
        } else {
            mSkuId = temp.substring(temp.indexOf("sku=") + 4, temp.indexOf("&cu="));
        }
        String mUtmCampaign = temp.substring(temp.indexOf("utm_campaign=") + 13, temp.indexOf("&utm_term"));
        String mUtmMedium = temp.substring(temp.indexOf("utm_medium=") + 11, temp.indexOf("&utm_campaign"));
        String mUtmTerm = temp.substring(temp.indexOf("utm_term=") + 9);
        String mCurrentTime = Long.toString(System.currentTimeMillis());
        String mUtmSource = temp.substring(temp.indexOf("utm_source=") + 11, temp.indexOf("&utm_medium"));


        return "openapp.jdmobile://virtual?params={\"category\":\"jump\",\"des\":\"productDetail\",\n" +
                "\"skuId\":\"" + mSkuId + "\",\"sourceType\":\"MWEIXIN_PRODUCTFLOAT_TYPE\",\"sourceValue\":\"MWEIXIN_PRODUCTFLOAT_VALUE\",\"M_sourceFrom\":\"sxbanner\",\n" +
                "\"msf_type\":\"click\",\"m_param\":{\"m_source\":\"0\",\"event_series\":{},\"jda\":\"9201833.615084006.1567766952.1570723020.1570786579.33\",\"usc\":\"" + mUtmSource + "\",\n" +
                "\"ucp\":\"" + mUtmCampaign + "\",\"umd\":\"" + mUtmMedium + "\",\"utr\":\"" + mUtmTerm + "\",\"jdv\":\"9201833|" + mUtmSource + "|" + mUtmCampaign + "|" + mUtmMedium + "|\n" +
                "" + mUtmTerm + "|" + mCurrentTime + "\",\"ref\":\"https://item.m.jd.com/product/" + mSkuId + ".html?wxa_abtest=b&amp;ad_od=share&amp;&\n" +
                "amp;&amp;&amp;&cu=true&utm_source=" + mUtmSource + "&utm_medium=" + mUtmMedium + "&utm_campaign=" + mUtmCampaign + "&utm_term=" + mUtmTerm + "\",\n" +
                "\"psn\":\"615084006|33\",\"psq\":2,\n" +
                "\"unpl\":\"V2_ZzNtbRFWF0ciCUZcLEtZVmIHRVtLUUQXcA1PAHkZWVAzChFdclRCFX0URlVnGVwUZwUZWEFcQx1FCEZkexhdBGIBFFVCV3MlcAtBU3opXwVXAiJaQlNCHHAJR1NLKVwN\n" +
                "YDMSXENWQhJxCUdVfhxs3cGYxPjGj8urRQl2VUsYbFMJAxNcQ1dBF3QBRhl8GVgEbgYTXEVnQiV2\",\"pc_source\":\"\",\"mba_muid\":\"615084006\",\n" +
                "\"mba_sid\":\"15707865790603328812676376900\",\"std\":\"MO-J2011-1\",\"par\":\"wxa_abtest=b&amp;ad_od=share&amp;&amp;&amp;&amp;&cu=true&\n" +
                "utm_source=" + mUtmSource + "&utm_medium=" + mUtmMedium + "&utm_campaign=" + mUtmCampaign + "&utm_term=" + mUtmTerm + "\",\n" +
                "\"event_id\":\"MDownLoadFloat_TopOldExpo\",\"mt_xid\":\"\",\"mt_subsite\":\"\"},\"SE\":{\"mt_subsite\":\"\",\"__jdv\":\"9201833|" + mUtmSource + "|" + mUtmCampaign + "|" + mUtmMedium + "|\n" +
                "" + mUtmTerm + "|" + mCurrentTime + "\",\n" +
                "\"unpl\":\"V2_ZzNtbRFWF0ciCUZcLEtZVmIHRVtLUUQXcA1PAHkZWVAzChFdclRCFX0URlVnGVwUZwUZWEFcQx1FCEZkexhdBGIBFFVCV3MlcAtBU3opXwVXAiJaQlNCHHAJR1NLKVwN\n" +
                "YDMSXENWQhJxCUdVfhxs3cGYxPjGj8urRQl2VUsYbFMJAxNcQ1dBF3QBRhl8GVgEbgYTXEVnQiV2\",\n" +
                "\"__jda\":\"9201833.615084006.1567766952.1570723020.1570786579.33\"},\"wxa_abtest\":\"b\"}";
    }

    private String getUrl() {
        String result = "";
        String userInput;
        if (!TextUtils.isEmpty(mItemIdText.getText())) {
            userInput = mItemIdText.getText().toString();
        } else {
            return null;
        }
        int unionId = mMainRadioButton.isChecked() ? 1000437911 : 1001772498;
        if (userInput.startsWith("u.jd.com") || userInput.startsWith("https://u.jd.com")) {
            return userInput;
        } else {
            userInput = "https://wqitem.jd.com/item/view?sku=" + userInput;
        }
        try {
            result = URL + "&materialId=" + URLEncoder.encode(userInput, "UTF-8") + "&unionId=" + unionId;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}