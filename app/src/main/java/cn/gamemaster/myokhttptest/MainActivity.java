package cn.gamemaster.myokhttptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUESTCODE = 1;
    private final String[] PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.singleTask).setOnClickListener(this);
        requestPermission(this);
    }

    private void requestPermission(Activity activity) {
        if(Build.VERSION.SDK_INT >= 23){
            //如果没有被授予
            if(!checkDangerousPermissions(activity, PERMISSION)){
                //请求权限,此处可以同时申请多个权限
                ActivityCompat.requestPermissions(activity, PERMISSION,  REQUESTCODE);
                return;
            }else{
                // do something....
            }
        }else {
            // do something....
        }
    }

    public boolean checkDangerousPermissions(Activity ac, String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(ac, permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE :
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // do something....
                }else{
                    showNoPermissionToast();
                }
                break;
        }
    }

    private void showNoPermissionToast() {
        Toast.makeText(MainActivity.this, "获取权限失败!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.singleTask:
                toSingleActivity();
                break;
        }
    }

    private void toSingleActivity() {
        if (checkDangerousPermissions(this, PERMISSION)) {
            Intent intent = new Intent(this, SingleTaskActivity.class);
            startActivity(intent);
        } else {
            showNoPermissionToast();
        }

    }
}
