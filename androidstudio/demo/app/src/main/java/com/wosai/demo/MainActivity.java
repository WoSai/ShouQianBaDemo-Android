package com.wosai.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import cn.wosai.upay.PayMethod;
import cn.wosai.upay.UpayCallBack;
import cn.wosai.upay.UpayResult;
import cn.wosai.upay.UpayTask;
import cn.wosai.upay.OrderInfo;


public class MainActivity extends Activity {

    private ListView listView;
    private List<OrderInfo> mListData = new ArrayList<OrderInfo>();
    private MyAdapter mAdapter;

    static String storeId = "03322111001200200000024";
    static String appId = "1001200200000024";
    static String key = "dc1277093df84ff23b885cc776deca13";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        initView();

    }

    private void initView() {
        listView = (ListView) findViewById(R.id.lvList);
        mAdapter = new MyAdapter();
        listView.setAdapter(mAdapter);
        findViewById(R.id.checkout).setOnClickListener(clickListener);
        findViewById(R.id.lakalaCheckout).setOnClickListener(clickListener);
        findViewById(R.id.alipayCheckout).setOnClickListener(clickListener);
        findViewById(R.id.weChatCheckout).setOnClickListener(clickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                query(mAdapter.getItem(position));
            }
        });
    }

    private void checkout() {
        OrderInfo info = new OrderInfo();
        info.setOrderId("Test" + System.currentTimeMillis());
        info.setMerchantId(storeId);
        info.setAppId(appId);
        String sign = md5(info.getMerchantId() + info.getAppId() + key);
        info.setSign(sign);
        info.setOperId("1000");
        info.setSubject("喔噻体验商品");
        info.setAmount(1L);
        info.setCurType("156");
        new UpayTask(this).checkout(info, new UpayCallBack() {
            @Override
            public void onExecuteResult(UpayResult result) {
                if (result.getState() == UpayResult.PAID) {
                    update(result);
                }
            }
        });
    }

    private void lakalaCheckout() {
        OrderInfo info = new OrderInfo();
        info.setMerchantId(storeId);
        info.setOrderId("Test" + System.currentTimeMillis());
        info.setAppId(appId);
        String sign = md5(info.getMerchantId() + info.getAppId() + key);
        info.setSign(sign);
        info.setOperId("1000");
        info.setSubject("喔噻体验商品");
        info.setAmount(1L);
        info.setCurType("156");
        info.setPayMethod(PayMethod.UPAY_LAKALA);
        new UpayTask(this).checkout(info, new UpayCallBack() {
            @Override
            public void onExecuteResult(UpayResult result) {
                if (result.getState() == UpayResult.PAID) {
                    update(result);
                }
            }
        });
    }

    private void alipayCheckout() {
        OrderInfo info = new OrderInfo();
        info.setMerchantId(storeId);
        info.setOrderId("Test" + System.currentTimeMillis());
        info.setAppId(appId);
        String sign = md5(info.getMerchantId() + info.getAppId() + key);
        info.setSign(sign);
        info.setOperId("1000");
        info.setSubject("喔噻体验商品");
        info.setAmount(1L);
        info.setCurType("156");
        info.setPayMethod(PayMethod.UPAY_ALIPAY);
        new UpayTask(this).checkout(info, new UpayCallBack() {
            @Override
            public void onExecuteResult(UpayResult result) {
                if (result.getState() == UpayResult.PAID) {
                    update(result);
                }
            }
        });
    }

    private void weChatCheckout() {
        OrderInfo info = new OrderInfo();
        info.setMerchantId(storeId);
        info.setOrderId("Test" + System.currentTimeMillis());
        info.setAppId(appId);
        String sign = md5(info.getMerchantId() + info.getAppId() + key);
        info.setSign(sign);
        info.setOperId("1000");
        info.setSubject("喔噻体验商品");
        info.setAmount(1L);
        info.setCurType("156");
        info.setPayMethod(PayMethod.UPAY_WEIXIN);
        new UpayTask(this).checkout(info, new UpayCallBack() {
            @Override
            public void onExecuteResult(UpayResult result) {
                if (result.getState() == UpayResult.PAID) {
                    update(result);
                }
            }
        });
    }

    private void revoke(OrderInfo info) {
        new UpayTask(this).revoke(info, new UpayCallBack() {
            @Override
            public void onExecuteResult(UpayResult result) {
                if (result.getState() == UpayResult.REVOKED) {
                    update(result);
                }
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkout:
                    checkout();
                    break;
                case R.id.lakalaCheckout:
                    lakalaCheckout();
                    break;
                case R.id.alipayCheckout:
                    alipayCheckout();
                    break;
                case R.id.weChatCheckout:
                    weChatCheckout();
                    break;
            }
        }
    };

    private void update(UpayResult result) {
        if (result != null) {
            OrderInfo order = new OrderInfo();
            order.setMerchantId(storeId);
            order.setAppId(appId);
            String sign = md5(order.getMerchantId() + order.getAppId() + key);
            order.setSign(sign);
            order.setTransactionId(result.getOrderId());
            order.setAmount(result.getAmount());
            order.setAmount(result.getAmount());
            order.setTime(result.getTime());
            order.setStatus(result.getState());
            order.setBatchNo(result.getBatchNo());
            order.setVoucherNo(result.getVoucherNo());
            order.setPayMethod(result.getPayMethod());
            updateOrderList(order);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void query(OrderInfo info) {
        new UpayTask(this).queryOrder(info, new UpayCallBack() {
            @Override
            public void onExecuteResult(UpayResult result) {
                Message message = handler.obtainMessage();
                message.obj = result;
                handler.sendMessage(message);
            }
        });
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showResult((UpayResult) msg.obj);
        }
    };

    private void showResult(UpayResult result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        StringBuilder sb = new StringBuilder();
        sb.append("Account\t" + result.getAccount() + "\n");
        sb.append("Amount\t" + result.getAmount() + "\n");
        sb.append("BatchNo\t" + result.getBatchNo() + "\n");
        sb.append("OrderId\t" + result.getOrderId() + "\n");
        sb.append("State\t" + result.getState() + "\n");
        sb.append("Time\t" + result.getTime() + "\n");
        sb.append("VoucherNo\t" + result.getVoucherNo() + "\n");
        builder.setMessage(sb.toString());
        builder.create().show();
    }


    private void updateOrderList(final OrderInfo order) {
        int sameId = -1;
        for (int i = 0; i < mListData.size(); i++) {
            if (order.getTransactionId().equals(mListData.get(i).getTransactionId())) {
                sameId = i;
                break;
            }
        }
        if (sameId != -1) {
            mListData.remove(sameId);
        }
        mListData.add(order);
    }


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public OrderInfo getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (null == view) {
                holder = new ViewHolder();
                view = View.inflate(MainActivity.this, R.layout.item_list, null);
                holder.time = (TextView) view.findViewById(R.id.time);
                holder.orderNo = (TextView) view.findViewById(R.id.orderNo);
                holder.amount = (TextView) view.findViewById(R.id.amount);
                holder.account = (TextView) view.findViewById(R.id.account);
                holder.payMethod = (TextView) view.findViewById(R.id.payMethod);
                holder.status = (TextView) view.findViewById(R.id.status);
                holder.btnRevoke = (Button) view.findViewById(R.id.btnRevoke);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            OrderInfo order = getItem(position);
            holder.time.setText(order.getTime());
            holder.orderNo.setText(order.getTransactionId());
            holder.amount.setText(order.getAmount() + "");
            holder.account.setText(order.getMerchantId());
            holder.payMethod.setText(order.getPayMethod().name());
            holder.status.setText(order.getStatus() == 1 ? "成功" : "撤销");
            if (order.getStatus() == 1) {
                holder.btnRevoke.setText("撤销");
                holder.btnRevoke.setEnabled(true);
            } else {
                holder.btnRevoke.setText("已撤销");
                holder.btnRevoke.setEnabled(false);
            }
            holder.btnRevoke.setTag(position);
            holder.btnRevoke.setOnClickListener(listener);
            return view;
        }

        class ViewHolder {
            TextView orderNo;
            TextView time;
            TextView amount;
            TextView account;
            TextView payMethod;
            TextView status;
            Button btnRevoke;
        }
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = Integer.parseInt(String.valueOf(v.getTag().toString()));
            OrderInfo order = mListData.get(position);
            revoke(order);
        }
    };

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }


    }

    public static String md5(String input) {
        try {
            return String.format("%032x", new BigInteger(1, MessageDigest.getInstance("MD5").digest(input.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
