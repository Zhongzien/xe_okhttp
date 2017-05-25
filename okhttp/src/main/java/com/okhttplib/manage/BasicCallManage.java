package com.okhttplib.manage;

import android.util.SparseArray;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class BasicCallManage {

    public static Map<String, SparseArray<Call>> callMap = new ConcurrentHashMap<>();

    public static void putCall(String key, Call value) {
        if (key != null) {
            SparseArray<Call> sArray = callMap.get(key);
            if (sArray == null) sArray = new SparseArray();
            sArray.put(value.hashCode(), value);
            callMap.put(key, sArray);
        }
    }

    public static void removeCallOrSet(String key, Call value) {
        if (key == null) return;
        SparseArray<Call> sArray = callMap.get(key);
        if (sArray == null) return;
        if (value != null) {
            if (!value.isCanceled())
                value.cancel();
            if (sArray.get(value.hashCode()) != null) {
                sArray.delete(value.hashCode());
            }
        } else {
            int size = sArray.size();
            for (int i = 0; i < size; i++) {
                Call call = sArray.valueAt(i);
                if (call != null) {
                    if (!call.isCanceled())
                        call.cancel();
                    sArray.delete(call.hashCode());
                }
            }
        }
        if (sArray.size() <= 0)
            callMap.remove(key);
    }

    public static void removeCall(String key, Call value) {
        if (key == null) return;
        if (value != null) {
            SparseArray<Call> sArray = callMap.get(key);
            if (!value.isCanceled()) value.cancel();
            if (sArray.get(value.hashCode()) != null)
                sArray.remove(value.hashCode());
            if (sArray.size() <= 0)
                callMap.remove(key);
        }
    }
}
