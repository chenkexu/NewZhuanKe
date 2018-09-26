package com.dfwr.zhuanke.zhuanke.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.dfwr.zhuanke.zhuanke.application.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;


/**
 *
 * 界面相关的工具类
 *
 * Created by xzhang on 2017/5/15.
 */

public class UIUtils {


    /**
     * 字符串换成UTF-8
     *
     * @param str
     * @return
     */
    public static String stringToUtf8(String str) {
        String result = null;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }



    /**
     * utf-8换成字符串
     *
     * @param str
     * @return
     */
    public static String utf8ToString(String str) {
        String result = null;
        try {
            result = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    public static String readChannelFromApkMetaInfo(Context context) {
        String channel = null;

        String sourceDir = context.getApplicationInfo().sourceDir;
        final String start_flag = "META-INF/channel_";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(start_flag)) {
                    channel = entryName.replace(start_flag, "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return channel;
    }



    /** dip转换px */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /** 获取上下文 */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /** pxz转换dip */

    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    /** 获取字符数组 */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /** 获取颜色id */
    public static int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    /** 根据id获取尺寸 */
    public static int getDimens(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }




    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static Uri setQQUri(String key) {
        Uri data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key);
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return data;
    }


    public boolean joinQQGroup() {
        String key = "qDmYFkk2XtU903xEk2vrPayicCb-lAeh";
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }




    /**
     * 网络图片转换为Bitmap
     *
     * @Author: ChengBin
     * @Time: 16/4/5 下午2:41
     */
    public static Bitmap netPicToBmp(String src) {
        try {
            Log.d("FileUtil", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(5 * 1000);
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            //设置固定大小
            //需要的大小
            float newWidth = 100f;
            float newHeigth = 100f;

            //图片大小
            int width = myBitmap.getWidth();
            int height = myBitmap.getHeight();

            //缩放比例
            float scaleWidth = newWidth / width;
            float scaleHeigth = newHeigth / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeigth);

            Bitmap bitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height, matrix, true);
            return bitmap;
        } catch (IOException e) {
            return null;
        }
    }







    public static Bitmap getHttpBitmap(String shareImage) {
        URL url;
        Throwable th;
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            URL pictureUrl = new URL(shareImage);
            try {
                in = pictureUrl.openStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inPurgeable = true;
                options.inSampleSize = 2;
                bitmap = (Bitmap) new SoftReference(BitmapFactory.decodeStream(in, null, options)).get();
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        url = pictureUrl;
                    }
                }
                url = pictureUrl;
            } catch (Exception e2) {
                url = pictureUrl;
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e3) {
                    }
                }
                return bitmap;
            } catch (Throwable th2) {
                th = th2;
                url = pictureUrl;
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        } catch (Throwable th3) {
            th = th3;
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return bitmap;
    }




    //首先传入两张图片
    private Bitmap mergeThumbnailBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        //以其中一张图片的大小作为画布的大小，或者也可以自己自定义
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap
                .getHeight(), firstBitmap.getConfig());
        //生成画布
        Canvas canvas = new Canvas(bitmap);
        //因为我传入的secondBitmap的大小是不固定的，所以我要将传来的secondBitmap调整到和画布一样的大小
        float w = firstBitmap.getWidth();
        float h = firstBitmap.getHeight();
        Matrix m = new Matrix();
        //确定secondBitmap大小比例
        m.setScale(w / secondBitmap.getWidth(), h / secondBitmap.getHeight());
        Paint paint = new Paint();
        //给画笔设定透明值，想将哪个图片进行透明化，就将画笔用到那张图片上
        paint.setAlpha(150);
        canvas.drawBitmap(firstBitmap, 0, 0, null);
        canvas.drawBitmap(secondBitmap, m, paint);
        return bitmap;
    }



    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, 0, 0, null);
        return bitmap;
    }



    /**
     * 对单独某个View进行截图
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(screenshot);
        c.translate(-v.getScrollX(), -v.getScrollY());
        v.draw(c);
        return screenshot;
    }

    public static <T> boolean notEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isEmpty(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    // 将px值转换为dip或dp值
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将dip或dp值转换为px值
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 将px值转换为sp值
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    // 将sp值转换为px值
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 屏幕宽度（像素）
    public static int getWindowWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    // 根据Unicode编码判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

}
