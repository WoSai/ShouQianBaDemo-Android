package com.wosai.demo;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;

import cn.wosai.upay.OrderInfo;
import cn.wosai.upay.UpayCallBack;
import cn.wosai.upay.UpayResult;
import cn.wosai.upay.UpayTask;


public class MainActivity extends ActionBarActivity {

    static String key = "rtsle45o2rlk3osdmecmndslwpqrlkjw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkout();

            }
        });
    }

    public static String md5(String input) {
        try {
            return String.format("%032x", new BigInteger(1, MessageDigest
                    .getInstance("MD5").digest(input.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkout() {
        OrderInfo info = new OrderInfo();
        info.setOrderId("No"+System.currentTimeMillis());
        info.setMerchantId("100512354");
        info.setAppId("150039203");
        String sign = md5(info.getMerchantId() + info.getAppId() + key);
        info.setSign(sign);
        info.setOperId("1000");
        info.setSubject("喔噻体验商品");
        info.setAmount(1L);
        info.setCurType("156");
        new UpayTask(this).checkout(info, new UpayCallBack() {
            @Override
            public void onExecuteResult(UpayResult result) {
                if (result != null) {
                    Toast.makeText(MainActivity.this, result.toString(),
                            Toast.LENGTH_LONG).show();
                    showResult(result);
                }

            }
        });
    }

    private void showResult(UpayResult result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("account----" + result.getAccount() + "\n");
        stringBuilder.append("amount----" + result.getAmount() + "\n");
        stringBuilder.append("BatchNo----" + result.getBatchNo() + "\n");
        stringBuilder.append("OrderId----" + result.getOrderId() + "\n");
        stringBuilder.append("State----" + result.getState() + "\n");
        stringBuilder.append("VoucherNo----" + result.getVoucherNo() + "\n");
        stringBuilder.append("Time----" + result.getTime() + "\n");
        stringBuilder.append("PayMethod----" + result.getPayMethod());
        builder.setMessage(stringBuilder.toString());
        builder.create().show();
    }


    private void queryOrder(OrderInfo info){
        //查询订单接口
        new UpayTask(this).queryOrder(info, new UpayCallBack() {
            @Override
            public void onExecuteResult(UpayResult result) {

            }
        });
    }
}
