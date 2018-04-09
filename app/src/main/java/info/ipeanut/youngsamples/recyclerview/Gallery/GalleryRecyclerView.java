package info.ipeanut.youngsamples.recyclerview.Gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import info.ipeanut.youngsamples.R;

/**
 * 创建时间: 2018/01/31 20:38 <br>
 * 作者: chenshao <br>
 * 描述: https://www.tuicool.com/articles/feA3mu3
 *
 * 1 PagerSnapHelper
 *
 *  // 自定义 https://github.com/rubensousa/RecyclerViewSnap
 */
public class GalleryRecyclerView extends RecyclerView {
  private int FLING_SPEED = 1000; // 滑动速度

  public static final int LinearySnapHelper = 0;
  public static final int PagerSnapHelper = 1;

  private ScrollManager mScrollManager;
  private GalleryItemDecoration mDecoration;
  public GalleryRecyclerView(Context context) {
    this(context, null);
  }

  public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GalleryRecyclerView);
    int helper = ta.getInteger(R.styleable.GalleryRecyclerView_helper, LinearySnapHelper);
    ta.recycle();

    attachDecoration();
    attachToRecyclerHelper(helper);
  }

  private void attachDecoration() {
    mDecoration = new GalleryItemDecoration();
    addItemDecoration(mDecoration);
  }

  private void attachToRecyclerHelper(int helper) {
    mScrollManager  = new ScrollManager(this);
    mScrollManager.initSnapHelper(helper);
    mScrollManager.initScrollListener();
  }

  public int getOrientation() {

    if (getLayoutManager() instanceof LinearLayoutManager) {
      if (getLayoutManager() instanceof GridLayoutManager) {
        throw new RuntimeException("请设置LayoutManager为LinearLayoutManager");
      } else {
        return ((LinearLayoutManager) getLayoutManager()).getOrientation();
      }
    } else {
      throw new RuntimeException("请设置LayoutManager为LinearLayoutManager");
    }
  }

  public LinearLayoutManager getLinearLayoutManager() {
    if (getLayoutManager() instanceof LinearLayoutManager) {
      if (getLayoutManager() instanceof GridLayoutManager) {
        throw new RuntimeException("请设置LayoutManager为LinearLayoutManager");

      } else {
        return (LinearLayoutManager) getLayoutManager();
      }
    } else {
      throw new RuntimeException("请设置LayoutManager为LinearLayoutManager");
    }
  }

  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus) {
    super.onWindowFocusChanged(hasWindowFocus);

    if (getAdapter().getItemCount() <= 0) {
      return;
    }
    if (mScrollManager != null) {
      mScrollManager.updateComsume();
    }
    // 获得焦点后滑动至第0项，避免第0项的margin不对
    smoothScrollToPosition(0);
  }

  @Override
  public boolean fling(int velocityX, int velocityY) {
    velocityX = balancelocity(velocityX);
    velocityY = balancelocity(velocityY);
    return super.fling(velocityX, velocityY);
  }

  /**
   * 返回滑动速度值
   *
   * @param velocity
   * @return
   */
  private int balancelocity(int velocity) {
    if (velocity > 0) {
      return Math.min(velocity, FLING_SPEED);
    } else {
      return Math.max(velocity, -FLING_SPEED);
    }
  }

  /**
   * 设置滑动速度（像素/s）
   *
   * @param speed
   * @return
   */
  public GalleryRecyclerView initFlingSpeed(int speed) {
    this.FLING_SPEED = speed;
    return this;
  }

  /**
   * 设置页面参数，单位dp
   *
   * @param pageMargin           默认：0dp
   * @param leftPageVisibleWidth 默认：50dp
   * @return
   */
  public GalleryRecyclerView initPageParams(int pageMargin, int leftPageVisibleWidth) {
    GalleryItemDecoration.mPageMargin = pageMargin;
    GalleryItemDecoration.mLeftPageVisibleWidth = leftPageVisibleWidth;
    return this;
  }

  /**
   * 设置动画因子
   *
   * @param factor
   * @return
   */
  public GalleryRecyclerView setAnimFactor(float factor) {
    AnimManager.getInstance().setmAnimFactor(factor);
    return this;
  }

  /**
   * 设置动画类型
   *
   * @param type
   * @return
   */
  public GalleryRecyclerView setAnimType(int type) {
    AnimManager.getInstance().setmAnimType(type);
    return this;
  }

  public int getScrolledPosition() {
    if (mScrollManager == null) {
      return 0;
    } else {
      return mScrollManager.getPosition();
    }
  }

  public interface OnItemClickListener {
    void onItemClick(View view, int position);
  }

}
