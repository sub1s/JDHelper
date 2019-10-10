package com.wp.jdhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wp.jdhelper.network.HttpHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText mItemIdText;

    private Button mGetButton;

    private TextView mResultText;

    private String string = "";

    private static final String URL = "https://api.open.21ds.cn/jd_api_v1/getitemcpsurl?apkey=d6315632-59d5-c330-a01b-7703a6a697cd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mItemIdText = findViewById(R.id.item_id);
        mGetButton = findViewById(R.id.button);
        mResultText = findViewById(R.id.result);
        mGetButton.setOnClickListener((v) -> {
            Runnable httpReq = () -> {
                try {
                    string = HttpHelper.sendGet(getUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mResultText.post(
                        () -> mResultText.setText(string)
                );
            };
            new Thread(httpReq).start();

        });
    }

    private String getUrl() {
        String result = "";
        String temp;
        if (!TextUtils.isEmpty(mItemIdText.getText())) {
            temp = mItemIdText.getText().toString();
        } else {
            temp = "123456";
        }

        try {
            result = URL + "&materialId=" + URLEncoder.encode(temp,"UTF-8") + "&unionId=1000437911";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
