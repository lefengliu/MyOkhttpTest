package cn.gamemaster.myokhttptest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener1;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SingleTaskActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "liuhuan";

    private ProgressBar progressBar;
    private TextView controller;
    private TextView statusTx;

    private boolean start;
    private DownloadTask task;
    private DownloadListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);
         initView();
         initData();
    }

    private void initView() {
        progressBar = findViewById(R.id.downloadProgress);
        controller = findViewById(R.id.actionController);
        statusTx = findViewById(R.id.downloadStatus);
        controller.setOnClickListener(this);
    }

    private void initData() {
        start = false;
        task = new DownloadTask.Builder(getUrl(), getParentFile())
                .setFilename(getFileName())
                // 下载进度回调的间隔时间（毫秒）
                .setMinIntervalMillisCallbackProcess(3000)
                // 任务过去已完成是否要重新下载
                .setPassIfAlreadyCompleted(false)
                .build();
        listener = new DownloadListener1() {
            @Override
            public void taskStart(@NonNull DownloadTask task, @NonNull Listener1Assist.Listener1Model model) {
                updateStatus("taskStart");
            }

            @Override
            public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {
                updateStatus("retry cause "+cause);
            }

            @Override
            public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {
                updateStatus("connected blockCount "+blockCount+" currentOffset "+currentOffset+" totalLength "+totalLength);
            }

            @Override
            public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {
                updateStatus("progress currentOffset "+currentOffset+" totalLength "+totalLength+" progress "+((int) ((currentOffset*1.0f/totalLength)*100)));
                progressBar.setProgress((int) ((currentOffset*1.0f/totalLength)*100));
            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull Listener1Assist.Listener1Model model) {
                updateStatus("taskEnd cause "+cause+" Exception "+(realCause == null ? " is null ": realCause.getMessage()));
                start = false;
                doAction();
            }
        };
    }

    private void updateStatus(String msg) {
        Log.d(TAG, msg);
        statusTx.setText(msg);
    }

    private String getUrl() {
        return "http://d.xunyou.mobi/hwgame/com.tencent.igce.apk";
    }

    private File getParentFile() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    private String getFileName() {
        return "okHttpDownloadDir";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionController:
                doAction();
                break;
        }
    }

    private void doAction() {
        if (start) {
            // 取消任务
            start = false;
            controller.setText("Start");
            task.cancel();
        } else {
            try {
                //异步执行任务
                start = true;
                controller.setText("Cancel");
                task.enqueue(listener);
            } catch (Throwable e) {
                Log.d(TAG, "task fail "+e.getMessage());
            }
        }


        // 同步执行任务
        //task.execute(listener);
    }
}
