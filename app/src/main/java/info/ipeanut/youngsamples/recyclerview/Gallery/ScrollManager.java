package info.ipeanut.youngsamples.recyclerview.Gallery;

import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import info.ipeanut.youngsamples.Utils;

/**
 * 创建时间: 2018/02/01 14:59 <br>
 * 作者: chenshao <br>
 * 描述:
 */
public class ScrollManager {

  private GalleryRecyclerView mGalleryRecyclerView;

  private LinearSnapHelper mLinearySnapHelper;
  private PagerSnapHelper mPagerSnapHelper;

  private int mPosition = 0;

  // 使偏移量为左边距 + 左边Item的可视部分宽度
  private int mConsumeX = 0;
  private int mConsumeY = 0;
  // 滑动方向
  private int slideDirct = SLIDE_RIGHT;

  private static final int SLIDE_LEFT = 1;    // 左滑
  private static final int SLIDE_RIGHT = 2;   // 右滑
  private static final int SLIDE_TOP = 3;     // 上滑
  private static final int SLIDE_BOTTOM = 4;  // 下滑

  public ScrollManager(GalleryRecyclerView mGalleryRecyclerView) {
    this.mGalleryRecyclerView = mGalleryRecyclerView;
  }

  /**
   * 控制rv的fling行为，就是fling的时候可以展示几页
   *
   * @param helper
   */
  public void initSnapHelper(int helper) {
    switch (helper) {
      case GalleryRecyclerView.LinearySnapHelper:
        mLinearySnapHelper = new LinearSnapHelper(); //默认效果
        mLinearySnapHelper.attachToRecyclerView(mGalleryRecyclerView);
        break;
      case GalleryRecyclerView.PagerSnapHelper:
        mPagerSnapHelper = new PagerSnapHelper(); // viewPager效果
        mPagerSnapHelper.attachToRecyclerView(mGalleryRecyclerView);
        break;
    }
  }

  /**
   * 监听滑动，添加动画
   */
  public void initScrollListener() {
    GalleryScrollerListener mScrollerListener = new GalleryScrollerListener();
    mGalleryRecyclerView.addOnScrollListener(mScrollerListener);
  }

  public class GalleryScrollerListener extends RecyclerView.OnScrollListener{
    @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
    }

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      onHoritiontalScroll(recyclerView,dx);

    }
  }

  public void updateComsume() {
    mConsumeX += Utils.dpToPx(GalleryItemDecoration.mLeftPageVisibleWidth + GalleryItemDecoration.mPageMargin * 2);
    mConsumeY += Utils.dpToPx(GalleryItemDecoration.mLeftPageVisibleWidth + GalleryItemDecoration.mPageMargin * 2);

  }

  private void onHoritiontalScroll(final RecyclerView recyclerView, int dx) {
    mConsumeX += dx;

    if (dx > 0) {
      // 右滑
      slideDirct = SLIDE_RIGHT;
    } else {
      // 左滑
      slideDirct = SLIDE_LEFT;
    }

    recyclerView.post(new Runnable() {
      @Override public void run() {

        int shouldConsumeX = GalleryItemDecoration.mItemComusemX;
        // 获取当前的位置
        int position = getPosition(mConsumeX, shouldConsumeX);
        float offset = (float) mConsumeX / (float) shouldConsumeX;     // 位置浮点值（即总消耗距离 / 每一页理论消耗距离 = 一个浮点型的位置值）

        // 避免offset值取整时进一，从而影响了percent值
        if (offset >= mGalleryRecyclerView.getLinearLayoutManager().findFirstVisibleItemPosition() + 1 && slideDirct == SLIDE_RIGHT) {
          return;
        }

        // 获取当前页移动的百分值
        float percent = offset - ((int) offset);

        AnimManager.getInstance().setAnimation(recyclerView,position,percent);

      }
    });

  }

  private int getPosition(int mConsumeX, int shouldConsumeX) {
    float offset = (float) mConsumeX / (float) shouldConsumeX;
    int position = Math.round(offset);        // 四舍五入获取位置
    mPosition = position;
    return position;
  }

  public int getPosition() {
    return mPosition;
  }
}
