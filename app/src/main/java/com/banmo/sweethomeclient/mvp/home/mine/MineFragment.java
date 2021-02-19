package com.banmo.sweethomeclient.mvp.home.mine;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.service.UserService;
import com.banmo.sweethomeclient.databinding.FragmentMineBinding;
import com.banmo.sweethomeclient.mvp.home.Flush;
import com.banmo.sweethomeclient.mvp.home.HomeActivity;
import com.banmo.sweethomeclient.mvp.login.LoginActivity;
import com.banmo.sweethomeclient.pojo.UpdateIMG;
import com.banmo.sweethomeclient.pojo.User;
import com.banmo.sweethomeclient.tool.BitmapTools;
import com.banmo.sweethomeclient.tool.DateTools;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.banmo.sweethomeclient.client.ConnectorClient.offline;
import static com.banmo.sweethomeclient.tool.BitmapTools.parseBitmapByUri;
import static java.nio.charset.StandardCharsets.ISO_8859_1;

public class MineFragment extends Fragment implements Flush {

    private static final String TAG = "MineFragment";
    private static final Handler handler = new Handler(Looper.getMainLooper());
    ChangeFriendSizeDialog changeFriendSizeDialog;
    ChangePasswordDialog changePasswordDialog;
    private Date tmpDate;
    private FragmentMineBinding inflate;

    public void showDatePickerDialog(Activity activity, int themeResId, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        // 绑定监听器(How the parent is notified that the date is set.)
        new DatePickerDialog(activity, themeResId, (view, year, monthOfYear, dayOfMonth) -> {
            // 此处得到选择的时间，可以进行你想要的操作
            Calendar instance = Calendar.getInstance();
            instance.set(year, monthOfYear, dayOfMonth);
            tmpDate = instance.getTime();
            inflate.fragmentMineAgeEt.setText(DateTools.formatToDay(tmpDate));
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
        flushViewFromData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        inflate = FragmentMineBinding.inflate(inflater, container, false);
        initClickListener();
        return inflate.getRoot();
    }


    private void initClickListener() {
        inflate.fragmentMineHeadImg.setOnClickListener(v -> startActivityForResult(Intent.createChooser(
                new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), "选择图像..."), 2));

        inflate.fragmentMineLogoutBtn.setOnClickListener(v -> {
            logout();
        });

        View.OnClickListener changeFriendSizeFunc = v -> {
            int friendSize = changeFriendSizeDialog.getFriendSize();
            User user = UserInfos.user;
            user.setUserfriendsize(friendSize);
            new Thread(() -> UserService.updateUserInfo(user)).start();
            changeFriendSizeDialog.dismiss();
        };
        inflate.fragmentMineChangeFriendSizeBtn.setOnClickListener(v -> {
            changeFriendSizeDialog = new ChangeFriendSizeDialog(getActivity(), R.style.Theme_AppCompat_Dialog, changeFriendSizeFunc);
            changeFriendSizeDialog.show();
        });

        View.OnClickListener changePWDFunc = v -> {
            String oldPWD = changePasswordDialog.getOldPWD();
            String newPWD1 = changePasswordDialog.getNewPWD1();
            String newPWD2 = changePasswordDialog.getNewPWD2();
            if (oldPWD.equals(UserInfos.user.getUserpassword())) {
                if (newPWD1.equals(newPWD2)) {
                    User user = UserInfos.user;
                    user.setUserpassword(newPWD2);
                    new Thread(() -> UserService.updateUserInfo(user)).start();
                    changePasswordDialog.dismiss();
                    logout();
                    Toast.makeText(getActivity(), "请重新登录", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "两次密码输入不同", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "旧密码输入错误", Toast.LENGTH_SHORT).show();
            }
        };
        inflate.fragmentMineChangePwdBtn.setOnClickListener(v -> {
            changePasswordDialog = new ChangePasswordDialog(getActivity(), R.style.Theme_AppCompat_Dialog, changePWDFunc);
            changePasswordDialog.show();
        });

        // 选择生日日期
        inflate.fragmentMineAgeEt.setOnClickListener(v -> showDatePickerDialog(getActivity(), 4, Calendar.getInstance(Locale.CHINA)));
        inflate.fragmentMineSaveChangeBtn.setOnClickListener(v -> {
            User user = UserInfos.user;
            user.setUserbirth(tmpDate);
            String username = inflate.fragmentMineUsernameEt.getText().toString();
            user.setUsername(username);
            user.setUserphone(inflate.fragmentMinePhoneEt.getText().toString());
            user.setUsersex((byte) inflate.fragmentMineSexySp.getSelectedItemPosition());
            new Thread(() -> UserService.updateUserInfo(user)).start();
        });
        inflate.fragmentMineDestroyUserBtn.setOnClickListener(v -> new Thread(() -> UserService.destroyUser(UserInfos.user)).start());
    }

    private void logout() {
        UserInfos.clearAllInfos();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        offline();
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        if (requestCode == 1) {
            HomeActivity.switchFragment(1);
        } else if (requestCode == 2) {
            assert data != null;
            Uri uri = data.getData();
            Bitmap bm = parseBitmapByUri(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
            inflate.fragmentMineHeadImg.setImageBitmap(bm);//想图像显示在ImageView视图上，private ImageView img;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //压缩输出
            assert bm != null;
            BitmapTools.compressBmp(bm)
                    .compress(Bitmap.CompressFormat.JPEG, 100, out);
            UpdateIMG updateIMG = new UpdateIMG();
            byte[] bytes = out.toByteArray();
            String jpeg = new String(bytes, ISO_8859_1);

            updateIMG.setJpeg(jpeg);
            updateIMG.setUserid(UserInfos.getUserid());
            new Thread(() -> {
                UserService.postUserHeadImg(updateIMG);
                flushViewFromData();
            }).start();
            Toast.makeText(getActivity(), "上传头像ing", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void flushViewFromData() {
        Log.e(TAG, "flushInfo: ");
        User user = UserInfos.user;
        String userid = user.getUserid().toString();
        new Thread(() -> {
            Bitmap headImg = UserService.getHeadImgByUseridAndFlushCache(user.getUserid());
            handler.postDelayed(() -> {
                inflate.fragmentMineHeadImg.setImageBitmap(headImg);
                inflate.fragmentMineIdEt.setText(userid);
                inflate.fragmentMineCreateTimeEt.setText(DateTools.formatToDay(user.getUsercreatetime()));
                inflate.fragmentMineUsernameEt.setText(user.getUsername());
                Byte usersex = user.getUsersex();
                inflate.fragmentMineSexySp.setSelection(usersex);
                inflate.fragmentMineMailEt.setText(user.getUsermail());
                inflate.fragmentMinePhoneEt.setText(user.getUserphone());
                tmpDate = user.getUserbirth();
                inflate.fragmentMineAgeEt.setText((DateTools.formatToDay(tmpDate)));
            }, 0);
        }).start();

    }
}
