package com.example.nd_excel.Util;

import android.app.Application;

public class App extends Application {
  /*  public static Content reTurnContent{
        return
    }*/
  public static App app;
  public static App getInstance(){
      //if (app==null)app=new App();
      return app;
  }

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
    }
}
