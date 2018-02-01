package info.ipeanut.youngsamples;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * 创建时间: 2018/02/01 15:58 <br>
 * 作者: chenshao <br>
 * 描述:
 */
public class Utils {

  public static int px2sp(int pxValue) {
    return (int) (pxValue / Resources.getSystem().getDisplayMetrics().scaledDensity + 0.5f);
  }

  /**
   * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
   */
  public static int dip2px(int dpValue) {
    return (int) (dpValue * Resources.getSystem().getDisplayMetrics().density + .5f);
  }

  public static int dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }

  public static int pxToDp(int px) {
    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
  }

  /**
   * 获取屏幕宽度
   *
   * @return
   */
  public static int getScreenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;
  }

  /**
   * 获取屏幕高度
   *
   * @return
   */
  public static int getScreenHeigth() {
    return Resources.getSystem().getDisplayMetrics().heightPixels;
  }

  /**
   * 获取屏幕宽度
   *
   * @return
   */
  public static int getScreenWidth(Activity activity) {
    DisplayMetrics outMetrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
    return outMetrics.widthPixels;

  }

}
