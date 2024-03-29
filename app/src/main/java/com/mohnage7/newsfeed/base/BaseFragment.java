package com.mohnage7.newsfeed.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    private AppCompatActivity activity;
    private Toast mToast;

    @LayoutRes
    protected abstract int layoutRes();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public AppCompatActivity getBaseActivity() {
        return activity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    /**
     * this method allows only one toast to be shown at a time.
     * if there's a toast is shown on the screen, cancel it and display the new one.
     *
     * @param title content to be displayed inside the toast
     */
    public void showToast(CharSequence title) {
        if (mToast == null) {
            mToast = Toast.makeText(getBaseActivity(), title, Toast.LENGTH_SHORT);
        } else {
            if (mToast.getView().isShown())
                mToast.cancel();
            mToast.setText(title);
        }
        // display the toast new/changed
        mToast.show();
    }
}
