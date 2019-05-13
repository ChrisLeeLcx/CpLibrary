package cn.lee.cplibrary.util.takephotos;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;

import cn.lee.cplibrary.util.AppUtils;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.permissionutil.PermissionUtils;

/**
 * 获取拍照或者相册中的图片
 */
public class PhotoUtil {
    public static File tempFile;
    //请求相机
    public static final int REQUEST_CAPTURE = 200;
    //请求相册
    public static final int REQUEST_PICK = 201;
    //请求截图
    public static final int REQUEST_CROP_PHOTO = 202;

    //请求访问外部存储
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 203;
    //请求写入外部存储
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 204;
    //请求相机权限
    public static final int CAMERA_ACCESSIBILITY = 209;
    //订单支付详情选择更多优惠券返回码
    public static final int MORE_VOLUME_CHECK_CODE = 301;
    //订单支付详情选择优惠卡请求码
    public static final int CARD_CHECK_CODE = 302;

    /*权限数组*/
    private static final String[] permissionArray = new String[]{
            Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
    };

    /**
     * 注意：拍照前，Activity的OnCreate方法中必须调用此方法初始化文件临时缓存路径
     *
     * @param activity
     * @param savedInstanceState
     */
    public static void init(Activity activity, Bundle savedInstanceState) {
        PermissionUtils.checkPermissionArray(activity, permissionArray, 0x10);
        createCameraTempFile(savedInstanceState);
    }

    /**
     * 申请拍照权限,并创建临时文件夹
     *
     * @param activity
     * @param savedInstanceState
     */
    public static void checkPermission(Activity activity, Bundle savedInstanceState) {
        PermissionUtils.checkPermissionArray(activity, permissionArray, 0x10);
        cn.lee.cplibrary.util.takephotos.PhotoUtil.createCameraTempFile(savedInstanceState);
    }

    public static void checkPermission(Fragment fragment, Bundle savedInstanceState) {
        PermissionUtils.checkPermissionArray(fragment, permissionArray, 0x10);
        cn.lee.cplibrary.util.takephotos.PhotoUtil.createCameraTempFile(savedInstanceState);
    }

    /**
     * 跳到系统相册页面
     */
    public static void gotoPhoto(Activity activity, Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (fragment == null) {
            activity.startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
        } else {
            fragment.startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
        }
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     */
    public static void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    private static Dialog picDialog;

    /**
     * 打开上传图片的Dialog 必须重写onActivityResult
     *
     * @param activity
     * @param fragment 不为null表示在Fragment中上传图片 ，是null表示在Activity中上传图片
     * @return
     */
    public static Dialog showPicChooseDialog(final Activity activity, final Fragment fragment) {
//        if (picDialog == null) {
        View view = LayoutInflater.from(activity).inflate(cn.lee.cplibrary.R.layout.cp_dialog_pic_choose, null);
        picDialog = CpComDialog.getBottomDialog(activity, true, view);
        //相册
        view.findViewById(cn.lee.cplibrary.R.id.photoAlbum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                picDialog.dismiss();
                //权限判断
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(activity, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    //跳转到调用系统图库
                    gotoPhoto(activity, fragment);
                } else {
                    ToastUtil.showToast(activity, "请允许访问相册");
                }
            }
        });
        //照相
        view.findViewById(cn.lee.cplibrary.R.id.photograph).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                picDialog.dismiss();
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE,CAMERA多个权限
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                CAMERA_ACCESSIBILITY);
                    }
                } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    //跳转到调用系统相机
                    gotoCarema(activity, fragment);
                } else {
                    ToastUtil.showToast(activity, "需设置权限才可拍照");
                }
            }
        });

        view.findViewById(cn.lee.cplibrary.R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        picDialog.dismiss();
                    }
                });
//        } else {
//
//            picDialog.show();
//        }
        return picDialog;


    }


    /**
     * Activity在onCreate方法中必须已经调用了方法PhotoUtil.init(this,savedInstanceState);
     * 跳转到照相机页
     */
    public static void gotoCarema(Activity activity, Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 23) {//FileProvider只能用于高版本的app中
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 222);
                ToastUtil.showToast(activity, "请允许软件开启相机");
                return;
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imgUri = FileProvider.getUriForFile(activity, AppUtils.getAppId(activity) + ".provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                if (fragment == null) {
                    activity.startActivityForResult(intent, REQUEST_CAPTURE);
                } else {
                    fragment.startActivityForResult(intent, REQUEST_CAPTURE);
                }
            }
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            if (fragment == null) {
                activity.startActivityForResult(intent, REQUEST_CAPTURE);
            } else {
                fragment.startActivityForResult(intent, REQUEST_CAPTURE);
            }
        }

    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * @param context
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public static File getImageFile(Context context, int requestCode, int resultCode, Intent data) {
        return getImageFile(context, requestCode, resultCode, data, "temp.jpg");

    }

    /**
     * @param imgName 文件名( 格式是.jpg,是在本方法中追加的)
     * @return
     */
    public static File getImageFile(Context context, int requestCode, int resultCode, Intent data, String imgName) {
        File file = null;
        if (resultCode != Activity.RESULT_OK) {
            return null;
        }
        Uri uri = null;
        switch (requestCode) {
            case cn.lee.cplibrary.util.takephotos.PhotoUtil.REQUEST_CAPTURE:
                uri = Uri.fromFile(tempFile);
                break;
            case cn.lee.cplibrary.util.takephotos.PhotoUtil.REQUEST_PICK:  //调用系统相册返回
                if (data != null) {
                    uri = data.getData();
                }
                break;
        }
        if (uri != null) {
            String filePath = cn.lee.cplibrary.util.takephotos.PhotoUtil.getRealFilePathFromUri(context, uri);//图片的路径
            String fileName = ImageUtils.getFileName(context, filePath, imgName + ".jpg");
            file = new File(fileName);
        }
        return file;
    }

    public static float getDesWidth() {
        return ImageUtils.getDesWidth();
    }
    /**
     * 设置压缩后宽高
     * @param desWidth
     */
    public static void setDesWidth(float desWidth) {
        ImageUtils.setDesWidth(desWidth);
    }

    /**
     * 设置质量
     * @param quality
     */
    public static void setQuality(int quality) {
        ImageUtils.setQuality(quality);
    }

    public static int getQuality() {
        return ImageUtils.getQuality();
    }
}



