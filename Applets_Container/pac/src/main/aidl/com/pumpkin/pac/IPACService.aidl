// IPACService.aidl
package com.pumpkin.pac;

import com.pumpkin.pac.ICallback;

interface IPACService {
    // 根据不同的action，进行处理
    void handle(String action,in ICallback callback);
}