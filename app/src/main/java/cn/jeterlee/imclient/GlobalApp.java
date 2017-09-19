package cn.jeterlee.imclient;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import cn.jeterlee.imclient.base.BaseActivity;
import cn.jeterlee.imclient.util.Utils;

/**
 * ============================================================
 * <p>
 * 作者：Jeterlee
 * <p>
 * 创建日期：2017/9/19 0019
 * <p>
 * 描述：用于全局的初始化操作，生命周期跟进程的生命周期是一致。
 * <p>
 * 修订历史：
 * <p>
 * 网址：https://www.github.com/Jeterlee
 * ============================================================
 **/
public class GlobalApp extends Application {

    private List<BaseActivity> mBaseActivityList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.initialize(this);//初始化工具类，为了获取上下文
    }

    public void addActivity(BaseActivity activity) {
        if (!mBaseActivityList.contains(activity)) {
            mBaseActivityList.add(activity);
        }
    }

    public void removeActivity(BaseActivity activity) {
        mBaseActivityList.remove(activity);
    }
}
