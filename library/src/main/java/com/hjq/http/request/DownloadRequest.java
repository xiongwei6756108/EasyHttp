package com.hjq.http.request;

import android.content.Context;

import com.hjq.http.callback.DownloadCallback;
import com.hjq.http.config.RequestApi;
import com.hjq.http.config.RequestServer;
import com.hjq.http.listener.OnDownloadListener;
import com.hjq.http.model.HttpHeaders;
import com.hjq.http.model.HttpMethod;
import com.hjq.http.model.HttpParams;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/EasyHttp
 *    time   : 2019/07/20
 *    desc   : 下载请求
 */
public final class DownloadRequest extends BaseRequest<DownloadRequest> {

    /** 默认下载方式为 Get */
    private HttpMethod mMethod = HttpMethod.GET;

    /** 下载回调对象 */
    private DownloadCallback mDownloadCallback;

    /** 下载请求对象 */
    private Call mCall;

    /** 保存的文件 */
    private File mFile;
    /** 校验的 MD5 */
    private String mMD5;
    /** 下载监听回调 */
    private OnDownloadListener mListener;

    public DownloadRequest(Context context) {
        super(context);
    }

    /**
     * 设置请求方式
     */
    public DownloadRequest method(HttpMethod method) {
        mMethod = method;
        return this;
    }

    /**
     * 设置下载地址
     */
    public DownloadRequest url(String url) {
        server(new RequestServer(url));
        api(new RequestApi(""));
        return this;
    }

    /**
     * 设置保存的路径
     */
    public DownloadRequest file(File file) {
        mFile = file;
        return this;
    }

    public DownloadRequest file(String file) {
        mFile = new File(file);
        return this;
    }

    /**
     * 设置 MD5 值
     */
    public DownloadRequest md5(String md5) {
        mMD5 = md5;
        return this;
    }

    /**
     * 设置下载监听
     */
    public DownloadRequest listener(OnDownloadListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    protected Request create(String url, String tag, HttpParams params, HttpHeaders headers) {
        switch (mMethod) {
            case GET:
                // 如果这个下载请求方式是 Get
                return new GetRequest(getContext()).create(url, tag, params, headers);
            case POST:
                // 如果这个下载请求方式是 Post
                return new PostRequest(getContext()).create(url, tag, params, headers);
            default:
                throw new IllegalStateException("are you ok?");
        }
    }

    /**
     * 开始下载
     */
    public DownloadRequest start() {
        mCall = create();
        mDownloadCallback = new DownloadCallback(mFile, mMD5, mListener);
        mCall.enqueue(mDownloadCallback);
        return this;
    }

    /**
     * 取消下载
     */
    public DownloadRequest cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
        return this;
    }
}