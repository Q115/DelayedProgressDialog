package test.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

/*
 * Copyright 2018 Qi Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class DelayedProgressDialog extends DialogFragment {
    private static final int DELAY_MILLISECOND = 450;
    private static final int MINIMUM_SHOW_DURATION_MILLISECOND = 300;
    private static final int PROGRESS_CONTENT_SIZE_DP = 80;

    private ProgressBar mProgressBar;
    private boolean startedShowing;
    private long mStartMillisecond;
    private long mStopMillisecond;

    private FragmentManager fragmentManager;
    private String tag;
    private Handler showHandler;

    // default constructor. Needed so rotation doesn't crash
    public DelayedProgressDialog() {
        super();
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_progress, null));
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        mProgressBar = getDialog().findViewById(R.id.progress);

        if (getDialog().getWindow() != null) {
            int px = (int) (PROGRESS_CONTENT_SIZE_DP * getResources().getDisplayMetrics().density);
            getDialog().getWindow().setLayout(px, px);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void show(FragmentManager fm, String tag) {
        if (isAdded())
            return;

        this.fragmentManager = fm;
        this.tag = tag;
        mStartMillisecond = System.currentTimeMillis();
        startedShowing = false;
        mStopMillisecond = Long.MAX_VALUE;

        showHandler = new Handler();
        showHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // only show if not already cancelled
                if (mStopMillisecond > System.currentTimeMillis())
                    showDialogAfterDelay();
            }
        }, DELAY_MILLISECOND);
    }

    private void showDialogAfterDelay() {
        startedShowing = true;

        DialogFragment dialogFragment = (DialogFragment) fragmentManager.findFragmentByTag(tag);
        if (dialogFragment != null) {
            fragmentManager.beginTransaction().show(dialogFragment).commitAllowingStateLoss();
        } else {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    public void cancel() {
        if(showHandler == null)
            return;

        mStopMillisecond = System.currentTimeMillis();
        showHandler.removeCallbacksAndMessages(null);

        if (startedShowing) {
            if (mProgressBar != null) {
                cancelWhenShowing();
            } else {
                cancelWhenNotShowing();
            }
        } else
            dismiss();
    }

    private void cancelWhenShowing() {
        if (mStopMillisecond < mStartMillisecond + DELAY_MILLISECOND + MINIMUM_SHOW_DURATION_MILLISECOND) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, MINIMUM_SHOW_DURATION_MILLISECOND);
        } else {
            dismiss();
        }
    }

    private void cancelWhenNotShowing() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, DELAY_MILLISECOND);
    }

    @Override
    public void dismiss() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.remove(this);
        ft.commitAllowingStateLoss();
    }
}
