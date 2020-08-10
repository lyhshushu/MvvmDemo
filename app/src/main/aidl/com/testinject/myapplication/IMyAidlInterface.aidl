// IMyAidlInterface.aidl
package com.testinject.myapplication;

// Declare any non-default types here with import statements
import com.testinject.myapplication.User;

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.只提示支持的基本数据类型
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
      List<String> getData();
      void setData(String s);

      User getUser();
      void SetUser(in User user);
}
