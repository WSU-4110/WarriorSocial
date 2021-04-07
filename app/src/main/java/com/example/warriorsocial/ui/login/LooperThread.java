package com.example.warriorsocial.ui.login;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class LooperThread extends Thread {
        public Handler mHandler;

        public void run() {
            Looper.prepare();

            mHandler = new Handler(Looper.myLooper()) {
                public void handleMessage(Message msg) {
                    // process incoming messages here
                }
            };

            Looper.loop();
        }
    }

