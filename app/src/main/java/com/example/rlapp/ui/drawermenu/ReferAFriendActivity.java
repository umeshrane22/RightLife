package com.example.rlapp.ui.drawermenu;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferAFriendActivity extends AppCompatActivity {
    private ImageView ivImageRefer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);

        ivImageRefer = findViewById(R.id.iv_refer_image);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

        getReferralCode();

        Button btnCopyLink = findViewById(R.id.btn_copy_link);

        btnCopyLink.setOnClickListener(view -> copyText());


        Button btnShare = findViewById(R.id.btn_share);
        btnShare.setOnClickListener(view -> {

            BitmapDrawable bitmapDrawable = (BitmapDrawable) ivImageRefer.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            //intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, getImageToShare(bitmap));
            intent.putExtra(Intent.EXTRA_TEXT, "I'm inviting you to join me on the RightLife App, where health meets happiness in every tap.\n" +
                    "HealthCam and Voice Scan: Get insights into your physical and emotional well-being through facial recognition and voice pattern analysis.\"\n" +
                    "\n" +
                    "Together, let's craft our own adventure towards wellbeing! \n" +
                    "Download Now:  " + "https://rightlife.sng.link/Afiju/ui5f/r_ccc1b019cd");

            startActivity(Intent.createChooser(intent, "Share"));
        });
    }

    private void copyText() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", "I'm inviting you to join me on the RightLife App, where health meets happiness in every tap.\n" +
                "HealthCam and Voice Scan: Get insights into your physical and emotional well-being through facial recognition and voice pattern analysis.\"\n" +
                "\n" +
                "Together, let's craft our own adventure towards wellbeing! \n" +
                "Download Now:  " + "https://rightlife.sng.link/Afiju/ui5f/r_ccc1b019cd");
        clipboard.setPrimaryClip(clip);
    }

    private Uri getImageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "com.shareimage.fileprovider", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void getReferralCode() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getUserReferralCode(accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String strResponseBody = response.body().string();
                        Log.d("Umesh ", "referral Code = " + strResponseBody);

                        JSONObject jsonObject = new JSONObject(strResponseBody);
                        String code = jsonObject.getString("referralCode");
                        Log.d("Umesh A", "referral Code = " + strResponseBody);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Toast.makeText(ReferAFriendActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ReferAFriendActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
