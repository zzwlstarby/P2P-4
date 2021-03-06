package com.atguigu.p2p.more;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.atguigu.p2p.R;
import com.atguigu.p2p.base.BaseFragment;
import com.atguigu.p2p.shoushi.GestureEditActivity;
import com.atguigu.p2p.utils.AppNetConfig;
import com.atguigu.p2p.utils.LoadNet;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by ${
 * 李岩
 * QQ/微信: 642609666} on 3/10 0010.
 * 功能:更多
 */

public class MoreFragment extends BaseFragment {

    @InjectView(R.id.base_title)
    TextView baseTitle;
    @InjectView(R.id.base_back)
    ImageView baseBack;
    @InjectView(R.id.base_setting)
    ImageView baseSetting;
    @InjectView(R.id.tv_more_regist)
    TextView tvMoreRegist;
    @InjectView(R.id.toggle_more)
    ToggleButton toggleMore;
    @InjectView(R.id.tv_more_reset)
    TextView tvMoreReset;
    @InjectView(R.id.tv_more_phone)
    TextView tvMorePhone;
    @InjectView(R.id.rl_more_contact)
    RelativeLayout rlMoreContact;
    @InjectView(R.id.tv_more_fankui)
    TextView tvMoreFankui;
    @InjectView(R.id.tv_more_share)
    TextView tvMoreShare;
    @InjectView(R.id.tv_more_about)
    TextView tvMoreAbout;

    @Override
    protected void initData(String json) {
        //设置头布局
        initTitle();
        //设置点击事件
        initListener();
        //设置选择状态
        setTogState();
    }

    private void initListener() {
        toggleMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //打开手势识别器
                if (isChecked) {
                    //存储当前的状态,用来记录是否打开手势密码
                    saveState(isChecked);

                    //判断是否设置过
                    if (!getIsSetting()) {

                        //已经设置手势识别器
                        setSetting(true);
                        startActivity(new Intent(getActivity(), GestureEditActivity.class));

                    }
                } else {
                    //关闭手势密码
                    //存储当前的状态,用来记录是否打开手势密码
                    saveState(isChecked);
                }
            }
        });


        tvMoreReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //充值手势密码
                //设置手势密码
                startActivity(new Intent(getActivity(), GestureEditActivity.class));
            }
        });

        //设置客服拨打电话
        tvMorePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri uri = Uri.parse("tel:010-56253825");
                intent.setData(uri);
                startActivity(intent);
            }
        });

        tvMoreFankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(getActivity(), R.layout.dialog_fankui, null);
                new AlertDialog.Builder(getActivity())
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, String> map = new HashMap<>();
                                map.put("department", "");
                                map.put("content", "");
                                LoadNet.getDataNet(AppNetConfig.FEEDBACK, map, new LoadNet.OnGetNet() {
                                    @Override
                                    public void onSuccess(String content) {
                                        //提交是否成功
                                    }

                                    @Override
                                    public void onFailure(String content) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        //分享
        tvMoreShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
                oks.setTitle("标题");
                // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
                oks.setTitleUrl("http://www.baidu.com");
                // text是分享文本，所有平台都需要这个字段
                oks.setText("我是卖菊男孩景园");
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://www.baidu.com");
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                oks.setComment("卖菊啦");
                // site是分享此内容的网站名称，仅在QQ空间使用
                oks.setSite("ShareSDK");
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
                oks.show(getActivity());
            }
        });

    }

    private void setSetting(boolean setting) {
        SharedPreferences sp = getActivity().getSharedPreferences("setting",
                Context.MODE_PRIVATE);
        sp.edit().putBoolean("isSetting", setting).commit();
    }

    private void saveState(boolean isOpen) {
        SharedPreferences sp = getActivity().getSharedPreferences("tog_state", Context.MODE_PRIVATE);
        sp.edit().putBoolean("isOpen", isOpen).commit();
    }

    public boolean getIsSetting() {
        SharedPreferences sp
                = getActivity().getSharedPreferences("setting",
                Context.MODE_PRIVATE);
        return sp.getBoolean("isSetting", false);
    }

    public boolean getState() {
        SharedPreferences sp = getActivity().getSharedPreferences("tog_state", Context.MODE_PRIVATE);
        return sp.getBoolean("isOpen", false);
    }

    private void initTitle() {
        baseBack.setVisibility(View.GONE);
        baseSetting.setVisibility(View.GONE);
        baseTitle.setText("设置更多");
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragmemt_more;
    }

    @Override
    public String getChildUrl() {
        return null;
    }

    //设置button状态
    private void setTogState() {
        SharedPreferences sp = getActivity().getSharedPreferences("tog_state", Context.MODE_PRIVATE);
        boolean isOpen = sp.getBoolean("isOpen", false);
        toggleMore.setChecked(isOpen);
    }
}
