package lt.andfix.demo;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.IOException;

/**
 * 作者： lt on 2016/1/12
 */
public class App extends Application {
    private static final String TAG = "lt";

    public static final String APATCH_PATH = "/out.apatch";
    /**
     * patch manager
     */
    private PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
//		CrashReport.initCrashReport(this, "900012850", false);
    //    CrashHandler.getInstance().init(this);
        // initialize
        mPatchManager = new PatchManager(this);
        mPatchManager.init("1.1");
        Log.d(TAG, "inited.");

        // load patch
        mPatchManager.loadPatch();
        Log.d(TAG, "apatch loaded.");

        // add patch at runtime
        try {
            // .apatch file path
            String patchFileString = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + APATCH_PATH;
			/*File filesDir = getExternalFilesDir("");
			Uri destinationUri = Uri.parse(filesDir+"/out.apatch");
			mPatchManager.addPatch(destinationUri.getPath());*/
            mPatchManager.addPatch(patchFileString);
            Log.d(TAG, "apatch:" + patchFileString + " added.");
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }

    }
    public PatchManager getPatchManager(){
        return mPatchManager;
    }
}
