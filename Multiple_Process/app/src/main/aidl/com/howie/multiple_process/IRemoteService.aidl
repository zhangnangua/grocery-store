// IRemoteService.aidl
package com.howie.multiple_process;

// Declare any non-default types here with import statements

interface IRemoteService {
   /**
    * 获取服务进程id
    */
    int getPid();

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}