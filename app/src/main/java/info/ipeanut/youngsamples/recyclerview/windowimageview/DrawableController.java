package info.ipeanut.youngsamples.recyclerview.windowimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;

/**
 * 创建时间: 2018/01/31 16:33 <br>
 * 作者: chenshao <br>
 * 描述:
 */
public class DrawableController {

  private Context mContext;
  private Matrix mMatrix;
  private ProcessListener listener;
  private WindowImageView mView;
  private boolean isProcessing = false;
  private boolean frescoEnable = false;

  private Drawable sourceDrawable;
  private Bitmap sourceBitmap;
  private Drawable targetDrawable;
  private Bitmap targetBitmap;

  private int processedWidth;
  private int processedHeight;

  public void setFrescoEnable(boolean frescoEnable) {
    this.frescoEnable = frescoEnable;
  }

  public DrawableController(Context context, WindowImageView view) {
    mContext = context;
    mView = view;

    mMatrix = new Matrix();
  }

  public void process(){

    if (frescoEnable){

    } else {

      if (isProcessing) return;
      isProcessing = true;
      new Thread(new Runnable() {
        @Override public void run() {

          int drawableResId = mView.getImageResource();
          if (drawableResId == 0) {
            isProcessing = false;
            return;
          }

          BitmapFactory.Options options = new BitmapFactory.Options();
          options.inJustDecodeBounds = true;
          BitmapFactory.decodeResource(mContext.getResources(),drawableResId,options);

          // options.outWidth is dp, need do dp -> px
          int outWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,options.outWidth
                  ,mContext.getResources().getDisplayMetrics());
          int outHeightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,options.outHeight
                  ,mContext.getResources().getDisplayMetrics());

          float scale = 1.0f * mView.getRealWidth() / outWidthPx;

          processedWidth = (int) (scale * outWidthPx);
          processedHeight = (int) (scale * outHeightPx);

          options.inJustDecodeBounds = false;
          options.inSampleSize = calculateInSampleSize(outWidthPx, outHeightPx, processedWidth, processedHeight);

          sourceBitmap = BitmapFactory.decodeResource(mContext.getResources(),drawableResId,options);
          Log.e("sourceBitmap",outWidthPx + " - " + sourceBitmap.getWidth());

          mMatrix.reset();
          mMatrix.postScale(scale,scale);
          targetBitmap = Bitmap.createBitmap(sourceBitmap,0,0,sourceBitmap.getWidth(),sourceBitmap.getHeight(),mMatrix,true);

          releaseTargetDrawable();
          targetDrawable = new BitmapDrawable(targetBitmap);

          listener.onProcessFinished(processedWidth, processedHeight);

          if (sourceBitmap != null) {
            sourceBitmap.recycle();
            sourceBitmap = null;
          }

          isProcessing = false;
        }
      }).start();

    }
  }

  public Bitmap getTargetBitmap() {
    return targetBitmap;
  }

  private void releaseTargetDrawable() {
    if (!frescoEnable && targetDrawable != null && targetDrawable instanceof BitmapDrawable) {
      BitmapDrawable bitmapDrawable = (BitmapDrawable) targetDrawable;
      Bitmap bitmap = bitmapDrawable.getBitmap();
      if (!bitmap.isRecycled()) {
        bitmap.isRecycled();
      }
      targetDrawable = null;
    }
  }

  private int calculateInSampleSize(int sourceWidth, int sourceHeight, int reqWidth, int reqHeight) {
    int inSampleSize = 1;
    if (sourceWidth > reqWidth || sourceHeight > reqHeight) {
      int halfWidth = sourceWidth / 2;
      int halfHeight = sourceHeight / 2;
      while ((halfWidth / inSampleSize > reqWidth)
          && (halfHeight / inSampleSize > reqHeight)) {
        inSampleSize *= 2;
      }
    }
    return inSampleSize;
  }

  public Drawable getTargetDrawable() {
    if (frescoEnable) {
      return null;
    } else {
      return targetDrawable;
    }
  }

  public void setProcessListener(ProcessListener listener) {
    this.listener = listener;
  }

  public interface ProcessListener {
    void onProcessFinished(int width, int height);
  }

  public void doDetach() {
    //if (mDraweeHolder != null) {
    //  mDraweeHolder.onDetach();
    //}
  }

  public void doAttach() {
    //if (mDraweeHolder != null) {
    //  mDraweeHolder.onAttach();
    //}
  }

  @Override
  protected void finalize() throws Throwable {
    if (sourceBitmap != null) {
      sourceBitmap.recycle();
      sourceBitmap = null;
    }
    if (targetBitmap != null) {
      targetBitmap.recycle();
      targetBitmap = null;
    }
    super.finalize();
  }
}
