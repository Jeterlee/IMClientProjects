package cn.jeterlee.imclient.base;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jeterlee.imclient.util.ToastUtils;

import static android.content.Context.MODE_PRIVATE;
import static cn.jeterlee.imclient.util.ContextUtils.getSharedPreferences;

public abstract class BaseFragment extends Fragment {

    protected FragmentActivity mActivity;
    protected FragmentManager mFragmentManager;
    protected View mRootView;
    protected SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;
    private Unbinder mUnbinder;

    protected abstract int getLayoutResource();

    protected abstract void onInitView(View view, Bundle bundle);

    public <T extends View> T findView(int id) {
        // noinspection unchecked
        return (T) getActivity().findViewById(id);
    }

    public <T extends View> T findView(int id, View.OnClickListener listener) {
        T t = findView(id);
        t.setOnClickListener(listener);
        return t;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mFragmentManager = getChildFragmentManager();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
        }
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutResource() != 0) {
            mRootView = inflater.inflate(getLayoutResource(), null);
        } else {
            mRootView = super.onCreateView(inflater, container, savedInstanceState);
        }
        assert mRootView != null;
        mUnbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView(mRootView, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != Unbinder.EMPTY && mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mUnbinder = null;
        this.mActivity = null;
        this.mRootView = null;
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
}
