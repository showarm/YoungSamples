package info.ipeanut.youngsamples.recyclerview.windowimageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import info.ipeanut.youngsamples.R;

/**
 * 创建时间: 2018/01/31 16:37 <br>
 * 作者: chenshao <br>
 * 描述: https://github.com/Bleoo/WindowImageView/blob/master/window-image-view/src/main/java/io/github/bleoo/windowImageView/WindowImageView.java
 *
 * 1 整个长条图片作为Drawable画到WindowImageView的画布上
 * 2 列表滑动的时候，画布不断的上移
 *
 */
public class WindowImageView extends View {
  private static final String TAG = "WindowImageView";

  private Context mContext;
  private int resId;
  private boolean isFrescoEnable;

  private float mMimDisPlayTop;   // min draw top
  private float disPlayTop;       // current draw top
  private int[] location;         // location in window

  private int rescaleHeight;
  private int realWidth;

  private boolean isMeasured;

  private DrawableController mDrawableController;

  public WindowImageView(Context context) {
    super(context);
    init(context,null);
  }

  public WindowImageView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context,attrs);
  }

  public WindowImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context,attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public WindowImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    mContext = context;
    if (null != attrs){
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WindowImageView);
      isFrescoEnable = typedArray.getBoolean(R.styleable.WindowImageView_frescoEnable,false);
      resId = typedArray.getResourceId(R.styleable.WindowImageView_src,0);
      typedArray.recycle();
    }
    location = new int[2];

    mDrawableController = new DrawableController(mContext, this);
    mDrawableController.setFrescoEnable(isFrescoEnable);
    mDrawableController.setProcessListener(new DrawableController.ProcessListener() {
      @Override public void onProcessFinished(int width, int height) {
        rescaleHeight = height;
        resetTransMultiple(height);
        getLocationInWindow(location);
        disPlayTop = -(location[1] - rvLocation[1]) * translationMultiple;
        boundTop();

        postInvalidate();
      }
    });

  }

  private void boundTop() {
    if (disPlayTop > 0) {
      disPlayTop = 0;
    }
    if (disPlayTop < mMimDisPlayTop) {
      disPlayTop = mMimDisPlayTop;
    }
  }

  /**
   * 计算系数，  translationMultiple 滑动时✖️的一个系数，是图片未显示的高度/item未滑到的高度
   * @param scaledHeight bitmap的高度
   */
  private void resetTransMultiple(int scaledHeight) {
    if (recyclerView != null) {
            /*
            bitmap与recyclerView的长度对比

                |------------------------| recyclerView
                |----| item
                     |-------------------| can move length : recyclerViewHeight - thisHeight
                |----------------| bitmap
              or
                |-----------------------------| bitmap
                bitmap draw top : 0 ~ bitmapHeight - thisHeight
             */
      mMimDisPlayTop = -scaledHeight + getHeight();
      translationMultiple = 1.0f * -mMimDisPlayTop / (rvHeight - getHeight());
    }
  }
  private int measureHandle(int defSize,int measureSpec){
    int result = 0;

    int sepcMode = MeasureSpec.getMode(measureSpec);
    int sepcSize = MeasureSpec.getSize(measureSpec);
    if (sepcMode == MeasureSpec.EXACTLY || sepcMode == MeasureSpec.AT_MOST){
      result = sepcSize;
    } else{
      result = defSize;
    }

    return result;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    realWidth = measureHandle(getSuggestedMinimumWidth(),widthMeasureSpec);
    int height = measureHandle(getSuggestedMinimumHeight(),heightMeasureSpec);

    setMeasuredDimension(realWidth,height);

    isMeasured = true;
    mDrawableController.process();

  }

  //int tem = 0;
  @Override public void draw(Canvas canvas) {
    super.draw(canvas);

    Drawable drawable = mDrawableController.getTargetDrawable();
    if (drawable != null) {
      if (drawable instanceof BitmapDrawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        if (bitmapDrawable.getBitmap().isRecycled()) {
          return;
        }
      }

      canvas.save();
      canvas.translate(0,disPlayTop);
      drawable.setBounds(0,0,getWidth(),rescaleHeight);
      drawable.draw(canvas);
      // 如果没有translate，画布画的是drawable(0,0)开始的bounds部分，也就是最上面的。
      // translate时,disPlayTop一直是负值，translate的参数是dx,dy，
      // 图片保持不动，dy<0，画布向下，，dy>0，画布向上，
      // 从下往上滑，disPlayTop < 0 且递增，也就是从 -bitmap.getHeight()到0，所以从底部到顶部展示图片。

      canvas.restore();
    }

  }

  public int getRealWidth() {
    return realWidth;
  }

  public int getImageResource() {
    return resId;
  }

  // ----------------------------- RecyclerView bind -----------------------------

  private RecyclerView recyclerView;
  private RecyclerView.OnScrollListener rvScrollListener;

  /** 滑动时✖️的一个系数，是图片未显示的高度/item未滑到的高度  **/
  private float translationMultiple = 1.0f;
  private int[] rvLocation;
  private int rvHeight;

  public void bindRecyclerView(RecyclerView recyclerView) {
    if (recyclerView == null || recyclerView.equals(this.recyclerView)) {
      return;
    }
    unbindRecyclerView();
    this.recyclerView = recyclerView;
    rvLocation = new int[2];
    recyclerView.getLocationInWindow(rvLocation);
    rvHeight = recyclerView.getLayoutManager().getHeight();
    recyclerView.addOnScrollListener(rvScrollListener = new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        // 当前item，也就是windowImageView可见
        if (getTopDistance() > 0 && getTopDistance() + getHeight() < rvHeight) {
          disPlayTop += dy * translationMultiple;
          boundTop();

          Log.e("onScrolled",dx + " == " + dy + " == "+disPlayTop+ " == "+translationMultiple);

          if (isMeasured) {
            invalidate();
          }
        }
      }
    });
  }

  private int getTopDistance() {
    getLocationInWindow(location);
    return location[1] - rvLocation[1];
  }

  public void unbindRecyclerView() {
    if (recyclerView != null) {
      if (rvScrollListener != null) {
        recyclerView.removeOnScrollListener(rvScrollListener);
      }
      recyclerView = null;
    }
  }

  public void setImageResource(@DrawableRes int resId) {
    this.resId = resId;
    if (isMeasured && mDrawableController != null && !isFrescoEnable) {
      mDrawableController.process();
    }
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    mDrawableController.doDetach();
  }

  @Override
  public void onStartTemporaryDetach() {
    super.onStartTemporaryDetach();
    mDrawableController.doDetach();
  }

  @Override
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    mDrawableController.doAttach();
  }

  @Override
  public void onFinishTemporaryDetach() {
    super.onFinishTemporaryDetach();
    mDrawableController.doAttach();
  }

}
