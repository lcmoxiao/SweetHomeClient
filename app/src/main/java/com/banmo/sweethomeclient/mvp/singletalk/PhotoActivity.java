package com.banmo.sweethomeclient.mvp.singletalk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.service.TransService;
import com.banmo.sweethomeclient.client.tool.DateFormatTools;
import com.banmo.sweethomeclient.client.tool.SqLiteTOOLs;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import static com.banmo.sweethomeclient.mvp.singletalk.SingleTalkActivity.adapter;

public class PhotoActivity extends AppCompatActivity {

    final int TAKE_PICTURE = 1;
    final int SELECT_PICTURE = 2;

    Button sendBtn;
    Button backBtn;
    ImageView contentIv;

    //数据缓存区
    Bitmap tmpImg;
    int friendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        sendBtn = findViewById(R.id.activity_photo_sendBtn);
        backBtn = findViewById(R.id.activity_photo_backBtn);
        contentIv = findViewById(R.id.activity_photo_contentIv);


        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");
        friendID = intent.getIntExtra("friendID", 0);
        if (flag.equals("makePhoto")) {
            startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), TAKE_PICTURE);
        } else if (flag.equals("selectPhoto")) {
            startActivityForResult(Intent.createChooser(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), "选择图像..."), SELECT_PICTURE);
        }

        sendBtn.setOnClickListener(v -> {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tmpImg.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArray = baos.toByteArray();

            if (UserInfos.isOnMatching()) {
                if (UserInfos.isGroupMatching()) {
                    TransService.sendImgMsg(UUID.randomUUID().hashCode(), UserInfos.getUserid(), 0, UserInfos.groupid, byteArray);
                } else {
                    TransService.sendImgMsg(UUID.randomUUID().hashCode(), UserInfos.getUserid(), UserInfos.getMatcherID(), 0, byteArray);
                }
            } else {
                //从左到右分别为 msgType，UserID，TIME，CONTENT
                SqLiteTOOLs.insert(3, friendID, DateFormatTools.formatToSecond(new Date()), byteArray);
                TransService.sendImgMsg(UUID.randomUUID().hashCode(), UserInfos.getUserid(), friendID, 0, byteArray);
            }
            Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.parseColor("#FFEC808D"));
            adapter.addItem(new MsgDateBean(bmp, byteArray, DateFormatTools.getNowTime(), UserInfos.getUserid(), 3));
            finish();
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PICTURE) {
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                contentIv.setImageBitmap(bm);//想图像显示在ImageView视图上，private ImageView img;
                tmpImg = bm;
            } else if (requestCode == SELECT_PICTURE && data != null && data.getData() != null) {
                Uri uri = data.getData();
                Bitmap bm = parseBitmapByUri(uri);
                contentIv.setImageBitmap(bm);
                tmpImg = bm;
            } else {
                finish();
            }
        } else {
            finish();
        }
    }


    //通过Uri获取bitmap
    private Bitmap parseBitmapByUri(Uri uri) {
        try {
            InputStream input = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeStream(input, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("e", "getImgFromUri failed");
        return null;
    }

}
