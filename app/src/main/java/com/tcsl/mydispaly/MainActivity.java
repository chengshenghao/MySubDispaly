package com.tcsl.mydispaly;

import com.tcsl.mydispaly.view.MessageDisplay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

//双屏服务
public class MainActivity extends Activity {
    private MessageDisplay messageDisplay = null;
    private MediaRouter mMediaRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化mMediaRouter，用于辅屏幕显示路由
        mMediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
    }


    /**
     * 显示辅屏幕对话框
     *
     * @param v
     */
    public void showMessage(View v) {
        showMessageDisplay();
    }


    /**
     * 在Activity中直接启动显示屏幕，副显示屏周期跟Activity一起销毁而退出。
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showMessageDisplay() {
        MediaRouter.RouteInfo route = mMediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO);
        Display presentationDisplay = route != null ? route.getPresentationDisplay() : null;
        if (presentationDisplay == null) {
            Toast.makeText(this, "未检测到辅显示屏", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取副屏宽、高信息
        DisplayManager mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();
        Point point = new Point();
        displays[1].getSize(point);

        messageDisplay = new MessageDisplay(this, presentationDisplay);
        try {
            messageDisplay.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
            messageDisplay.setContentView(R.layout.pressentation_message);
            WebView webView = (WebView) messageDisplay.findViewById(R.id.wv);
            //设置webview宽高尺寸
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) webView.getLayoutParams();
            params.width = point.x;
            params.height = point.y;
            webView.setLayoutParams(params);
            webView.getSettings().setJavaScriptEnabled(true);
            // 设置可以支持缩放
            webView.getSettings().setSupportZoom(true);
            // 设置出现缩放工具
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.loadUrl("https://www.baidu.com");
            messageDisplay.show();
            Toast.makeText(this, "显示信息对话框", Toast.LENGTH_SHORT).show();
        } catch (WindowManager.InvalidDisplayException ex) {
            messageDisplay = null;
        }
    }

}
