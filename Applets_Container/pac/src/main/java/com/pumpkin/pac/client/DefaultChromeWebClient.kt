package com.pumpkin.pac.client

import android.net.Uri
import android.view.View
import android.webkit.*

/**
 * 默认的ChromeClient实现
 *
 * @author pumpkin
 */
internal class DefaultChromeWebClient : WebChromeClient() {

    /**
     * Android 5.0+适用
     * 告诉客户端显示文件选择器。
     * 这被称为处理具有“文件”输入类型的 HTML 表单，以响应用户按下“选择文件”按钮。
     * 要取消请求，请调用 filePathCallback.onReceiveValue(null) 并返回 true。
     */
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {

        // TODO: 2022/4/5 文件选择

        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)

    }

    /**
     * 权限请求
     */
    override fun onPermissionRequest(request: PermissionRequest?) {
        // TODO: 2022/4/5 权限请求

        super.onPermissionRequest(request)
    }

    override fun onPermissionRequestCanceled(request: PermissionRequest?) {
        // TODO: 2022/4/5 权限请求被取消
        super.onPermissionRequestCanceled(request)
    }

    /**
     * 页面加载进度监听
     */
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        // TODO: 2022/4/5 进度加载监听
        super.onProgressChanged(view, newProgress)
    }

    /**
     * 关于地理位置权限显示提示
     */
    override fun onGeolocationPermissionsShowPrompt(
        origin: String?,
        callback: GeolocationPermissions.Callback?
    ) {
        // TODO: 2022/4/5 onGeolocationPermissionsShowPrompt  关于地理位置权限显示提示
        super.onGeolocationPermissionsShowPrompt(origin, callback)
    }

    /**
     * 接收到标题变化
     */
    override fun onReceivedTitle(view: WebView?, title: String?) {
        // TODO: 2022/4/5 接收到标题变化
        super.onReceivedTitle(view, title)
    }

    /**
     * jsAlert
     */
    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        // TODO: 2022/4/5
        return super.onJsAlert(view, url, message, result)
    }

    /**
     * jsConfirm
     */
    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        // TODO: 2022/4/5
        return super.onJsConfirm(view, url, message, result)
    }

    /**
     * jsPrompt
     */
    override fun onJsPrompt(
        view: WebView?,
        url: String?,
        message: String?,
        defaultValue: String?,
        result: JsPromptResult?
    ): Boolean {
        // TODO: 2022/4/5
        return super.onJsPrompt(view, url, message, defaultValue, result)
    }

    /**
     * 通知宿主应用程序当前页面已进入全屏模式。 宿主应用程序必须以全屏模式显示包含 Web 内容（视频或其他 HTML 内容）的自定义视图。
     */
    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        // TODO: 2022/4/5 比如网页的视频全屏操作
        super.onShowCustomView(view, callback)
    }

    /**
     * 退出全屏
     */
    override fun onHideCustomView() {
        // TODO: 2022/4/5
        super.onHideCustomView()
    }

}