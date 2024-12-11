package com.example.rlapp.ui.drawermenu;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.rlapp.R;

import java.io.File;
import java.io.FileOutputStream;

public class ReferAFriendActivity extends AppCompatActivity {
    private ImageView ivImageRefer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);

        ivImageRefer = findViewById(R.id.iv_refer_image);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

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
}
