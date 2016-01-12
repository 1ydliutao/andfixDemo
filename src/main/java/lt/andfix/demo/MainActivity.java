package lt.andfix.demo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements DownloadStatusListener {
    private ThinDownloadManager downloadManager;
    private int downloadId;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    private  static  final  String TAG="mainactivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
        final  TextView t= (TextView) findViewById(R.id.text);
        Button bt= (Button) findViewById(R.id.bt);
        Button hot= (Button) findViewById(R.id.hot);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(t);
            }
        });
        hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadingFile();
            }
        });
    }

    private void toast(TextView  t) {
        t.setText("错误点击了一次");
    }
    private void downLoadingFile() {
        if (downloadManager.query(downloadId) == DownloadManager.STATUS_NOT_FOUND) {
            File filesDir = getExternalFilesDir("");
            Uri downloadUri = Uri.parse("https://raw.githubusercontent.com/THEONE10211024/HotFixDemo/master/app/src/main/java/patch/out.apatch");
//			Uri downloadUri = Uri.parse("https://raw.githubusercontent.com/alibaba/AndFix/master/tools/apkpatch-1.0.3.zip");

            String patchFileString = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + App.APATCH_PATH;
            Uri destinationUri = Uri.parse(patchFileString);
            final DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                    .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                    .setRetryPolicy(new DefaultRetryPolicy())
                    .setDownloadListener(this);
            downloadId = downloadManager.add(downloadRequest);
        }
    }

    @Override
    public void onDownloadComplete(int id) {
        // add patch at runtime
        try {
            // .apatch file path
            String patchFileString = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + App.APATCH_PATH;
            /*File filesDir = getExternalFilesDir("");
			Uri destinationUri = Uri.parse(filesDir+"/out.apatch");
			mPatchManager.addPatch(destinationUri.getPath());*/
            App mp = (App) getApplication();
            mp.getPatchManager().addPatch(patchFileString);

            Log.e(TAG, "apatch:" + patchFileString + " added.");
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
        Toast.makeText(this, "Hook Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadFailed(int id, int errorCode, String errorMessage) {

    }

    @Override
    public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {

    }
}
