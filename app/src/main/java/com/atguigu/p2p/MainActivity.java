package com.atguigu.p2p;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.atguigu.p2p.banking.BankingFragment;
import com.atguigu.p2p.base.BaseAvtivity;
import com.atguigu.p2p.base.BaseFragment;
import com.atguigu.p2p.home.HomeFragment;
import com.atguigu.p2p.invest.InvestFragment;
import com.atguigu.p2p.more.MoreFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.InjectView;

public class MainActivity extends BaseAvtivity {

    @InjectView(R.id.rl_main)
    FrameLayout rlMain;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    private List<BaseFragment> mList;
    /**
     * 记录下标
     */
    private int position;
    /**
     * 临时缓存
     */
    private BaseFragment tempFragment;

    @Override
    protected void initData() {
        isGrantExternalRW(this);
        initFragment();
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        position = 0;
                        break;
                    case R.id.rb_invest:
                        position = 1;
                        break;
                    case R.id.rb_banking:
                        position = 2;
                        break;
                    case R.id.rb_more:
                        position = 3;
                        break;
                }

                switchFragment(mList.get(position));
            }


        });

        //设置默认布局
        rgMain.check(R.id.rb_home);
    }


    /**
     * 切换fragment布局
     *
     * @param baseFragment
     */
    private void switchFragment(BaseFragment baseFragment) {

        //判断是否点击的是同一个fragment
        if (baseFragment != tempFragment) {

            //得到管理事务
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            //如果没有被添加
            if (!baseFragment.isAdded()) {

                //缓存的隐藏
                if (tempFragment != null) {
                    ft.hide(tempFragment);
                }

                //添加
                ft.add(R.id.rl_main, baseFragment);

            } else {

                //缓存的隐藏
                if (tempFragment != null) {
                    ft.hide(tempFragment);
                }

                //显示
                ft.show(baseFragment);

            }

            //提交
            ft.commit();

            //缓存
            tempFragment = baseFragment;
        }
    }

    /**
     * 添加碎片布局
     */
    private void initFragment() {
        mList = new ArrayList<>();
        mList.add(new HomeFragment());
        mList.add(new InvestFragment());
        mList.add(new BankingFragment());
        mList.add(new MoreFragment());
    }

    private boolean isBack = false;

    /**
     * 设置点击返回键双击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isBack) {
                //退出
                finish();
            } else {
                Toast.makeText(MainActivity.this, "再点击一次退出", Toast.LENGTH_SHORT).show();
            }
            isBack = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isBack = false;
                }
            }, 2000);

            //事件拦截
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     *
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{

//写入外部存储权限
                    Manifest.permission.READ_EXTERNAL_STORAGE,
//读取外部存储权限
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//相机权限
                    Manifest.permission.CAMERA,
//定位权限
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    //拨打电话权限
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.USE_SIP,
                    Manifest.permission.PROCESS_OUTGOING_CALLS,
                    Manifest.permission.ADD_VOICEMAIL
            }, 1);
            return false;
        }
        return true;
    }

}
