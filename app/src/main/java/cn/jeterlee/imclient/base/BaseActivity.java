package cn.jeterlee.imclient.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jeterlee.imclient.GlobalApp;
import cn.jeterlee.imclient.config.Constant;
import cn.jeterlee.imclient.util.ToastUtils;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;
    private ProgressDialog mProgressDialog;
    protected GlobalApp mGlobalApp;
    protected SharedPreferences mSharedPreferences;
    protected Activity mActivity;

    protected abstract int getLayoutResource();

    protected abstract void onInitView(Bundle bundle);

    public <T extends View> T findView(int id) {
        // noinspection unchecked
        return (T) findViewById(id);
    }

    public <T extends View> T findView(int id, View.OnClickListener listener) {
        T t = findView(id);
        t.setOnClickListener(listener);
        return t;
    }

    public void startActivity(Class clazz, boolean isFinish) {
        startActivity(clazz, isFinish, null);
    }

    private void startActivity(Class clazz, boolean isFinish, String contact) {
        Intent intent = new Intent(this, clazz);
        if (contact != null) {
            intent.putExtra("username", contact);
        }
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        /*所有的Activity都依附于一个Application，在Activity中只要通过 getApplication（）方法，
        就能拿到当前应用中的Application对象*/
        mGlobalApp = (GlobalApp) getApplication();
        mGlobalApp.addActivity(this);
        if (getLayoutResource() != 0) {
            setContentView(getLayoutResource());
        }
        mUnbinder = ButterKnife.bind(this);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
        }
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        this.onInitView(savedInstanceState);
    }

    public void showDialog(String msg) {
        if ((mProgressDialog != null) && !mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();
        }
    }

    public void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void showToast(String msg) {
        ToastUtils.showShortToast(msg);
    }

    @Override
    protected void onDestroy() {
        // 解除ButterKnife绑定
        if (mUnbinder != Unbinder.EMPTY && mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
        mGlobalApp.removeActivity(this);
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        this.mUnbinder = null;
    }


    public void saveUser(String username, String pwd) {
        mSharedPreferences.edit()
                .putString(Constant.SP_KEY_USERNAME, username)
                .putString(Constant.SP_KEY_PWD, pwd)
                .apply();
    }

    public String getUserName() {
        return mSharedPreferences.getString(Constant.SP_KEY_USERNAME, "");
    }

    public String getPwd() {
        return mSharedPreferences.getString(Constant.SP_KEY_PWD, "");
    }
}
