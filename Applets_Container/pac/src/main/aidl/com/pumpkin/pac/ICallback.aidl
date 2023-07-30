// ICallback.aidl
package com.pumpkin.pac;

// aidl 通用回调

interface ICallback {
    void callback(int code, String action, String message);
}