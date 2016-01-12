package lt.andfix.demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

/**
 * 作者： lt on 2016/1/12
 */
public class AppActivity extends Activity implements DownloadStatusListener {
    private ThinDownloadManager downloadManager;
    private int downloadId;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    private  static  final  String TAG="mainactivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
        final TextView t= (TextView) findViewById(R.id.text);
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
            Uri downloadUri = Uri.parse("https://github.com/1ydliutao/andfixDemo/tree/master/src/main/java/patch/out.apatch");
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
        Toast.makeText(this, "Hook fail"+errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {

    }
}
