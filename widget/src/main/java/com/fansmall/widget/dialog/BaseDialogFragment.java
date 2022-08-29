package com.fansmall.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.fansmall.widget.R;

/**
 * @author : Anthony.Pan
 * @date   : 2022/8/29 12:02
 * @desc   :
 */
public abstract class BaseDialogFragment extends DialogFragment {
    protected int mWidth;
    protected int mHeight;
    protected View mRootView;

    private DialogInterface.OnCancelListener onCancelListener;
    private DialogInterface.OnDismissListener onDismissListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            mWidth = dm.widthPixels;
            mHeight = dm.heightPixels;
        } else {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;
        }
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mRootView = inflater.inflate(getLayoutRes(), null, false);
        return mRootView;
    }

    protected abstract int getLayoutRes();

    public void show(FragmentManager manager) {
//        super.show(manager, this.getClass().getSimpleName());
        manager.beginTransaction().add(this, "dialog")
                .commitAllowingStateLoss();
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener){
        this.onCancelListener = onCancelListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener){
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if(onDismissListener != null){
            onDismissListener.onDismiss(getDialog());
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        if(onCancelListener != null){
            onCancelListener.onCancel(getDialog());
        }
        super.onCancel(dialog);
    }
}