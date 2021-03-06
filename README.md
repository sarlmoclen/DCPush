# DCPush
![](https://img.shields.io/badge/release-1.0.8-brightgreen.svg)

推送框架

## 使用

在你的项目根工程bulid.gradle中添加，让所有工程都依赖SRouter
 
```gradle
api 'com.sarlmoclen.dcpush:DCPush:1.0.8'
```

在自定义Application中初始化

```java
public class MyApp extends Application {

    /**
     * 例子：ws://echo.websocket.org
     * ws:不加密
     * wss:加密
     */
    private String uriContent = "ws://echo.websocket.org";

    @Override
    public void onCreate() {
        super.onCreate();
        DCWebSocketManager.getInstance()
                .init(getApplicationContext(),uriContent)
                .setHeartBeatRate(10 * 1000)//设置心跳时间，最短设置5秒，默认5秒
                .setHeartContent("heart")//设置心跳内容，默认“HEART”
                .setDebug(BuildConfig.DEBUG)//设置是否是debug模式，控制是否打印log，默认关闭
                .setHttpHeaders(new HashMap<String, String>());//设置头内容，默认空
    }

}
```
 
自定义receiver并静态注册（8.0静态注册广播已经适配），receiver监听连接状态，返回收到数据

```java

public class DCReceiver extends DCWebSocketReceiver {

    @Override
    protected void onOpen(Context context, short httpStatus, String httpStatusMessage) {
        DCLog.log(TAG, "onOpen()-httpStatus:" + httpStatus + "-httpStatusMessage:" + httpStatusMessage);
        send(context, "onOpen()-httpStatus:" + httpStatus + "-httpStatusMessage:" + httpStatusMessage);
    }

    @Override
    protected void onMessage(Context context, String message) {
        DCLog.log(TAG, "onMessage()-message:" + message);
        send(context, "onMessage()-message:" + message);
    }

    @Override
    protected void onClose(Context context, int code, String reason, boolean remote) {
        DCLog.log(TAG, "onClose()-code:" + code + "-reason:" + reason + "-remote:" + remote);
        send(context, "onClose()-code:" + code + "-reason:" + reason + "-remote:" + remote);
    }

    @Override
    protected void onError(Context context, Exception ex) {
        DCLog.log(TAG, "onError()-ex:" + ex.toString());
        send(context, "onError()-ex:" + ex.toString());
    }

}

<receiver
    android:name=".demo.DCReceiver"
    android:enabled="true"
    android:exported="false" >
    <intent-filter>
        <action android:name="dc.socket.broadcast.ACTION" /><!--这里填‘dc.socket.broadcast.ACTION’-->
        <category android:name="com.example.websocket" /><!--这里填包名-->
    </intent-filter>
</receiver>
```
 
在需要使用的地方控制开启，关闭，发送数据

```java
//开启
DCWebSocketManager.getInstance().startSocket();
//发送
DCWebSocketManager.getInstance().sendMessage("test");
//关闭
DCWebSocketManager.getInstance().closeSocket();
```

## License
The Apache Software License, Version 2.0  [LICENSE](http://www.apache.org/licenses/LICENSE-2.0.txt)
