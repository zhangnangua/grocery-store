package com.pumpkin.applets_container.view.itemDecoration;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.pumpkin.pac.BuildConfig;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "GridItemDecoration";
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private final int startMargin;
    private final int endMargin;
    private final int itemWidth;
    private final int topMargin;
    private final int verticalInterval;

    public GridItemDecoration(int startMargin, int endMargin, int itemWidth, int topMargin, int verticalInterval) {
        this.startMargin = startMargin;
        this.endMargin = endMargin;
        this.itemWidth = itemWidth;
        this.topMargin = topMargin;
        this.verticalInterval = verticalInterval;
    }

    public GridItemDecoration(int startMargin, int endMargin, int itemWidth) {
        this(startMargin, endMargin, itemWidth, 0, 0);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof StaggeredGridLayoutManager)
                && !(layoutManager instanceof GridLayoutManager)) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }

        final Integer spanIndex = spanIndex(view, layoutManager);
//        if (DEBUG) {
//            Log.d(TAG, "getItemOffsets() -> spanIndex:" + spanIndex);
//        }
        if (spanIndex == null) {
            outRect.set(0, 0, 0, 0);
        } else {
            int spanCount;
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            } else {
                spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            }


            final int paddingStart = parent.getPaddingStart();
            final int paddingEnd = parent.getPaddingEnd();

            int parentWidth = parent.getMeasuredWidth();
            final int width = parentWidth - paddingStart - paddingEnd;
            final int internal = (width - itemWidth * spanCount) / (spanCount - 1);

            int perWidth = width / spanCount;

            int left = 0;
            int right = 0;
            if (isFirst(spanIndex, layoutManager, parent.getLayoutDirection())) {
                if (DEBUG) {
                    Log.d(TAG, "getItemOffsets() -> " + spanIndex + " isFirst");
                }
                left = startMargin;
                right = perWidth - itemWidth - left;
            } else if (isLast(spanIndex, spanCount, layoutManager, parent.getLayoutDirection())) {
                if (DEBUG) {
                    Log.d(TAG, "getItemOffsets() -> " + spanIndex + " isLast");
                }
                right = endMargin;
                left = perWidth - itemWidth - right;
            } else {
                if (DEBUG) {
                    Log.d(TAG, "getItemOffsets() -> " + spanIndex + " middle");
                }
                left = internal - (spanIndex * perWidth - spanIndex * itemWidth - (spanIndex - 1) * internal);
                right = perWidth - itemWidth - left;
            }
            int top = 0;
            if (isFirstLine(spanIndex, spanCount)) {
                top = topMargin;
            } else {
                top = verticalInterval;
            }
            if (DEBUG) {
                Log.d(TAG, "getItemOffsets() -> left:" + left + " right:" + right);
            }
            if (parent.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                outRect.set(left, top, right, 0);
            } else {
                outRect.set(right, top, left, 0);
            }

        }
    }

    private boolean isFirstLine(Integer spanIndex, int spanCount) {
        return spanIndex != null && spanIndex < spanCount;
    }

    private boolean isLast(Integer spanIndex, int spanCount, RecyclerView.LayoutManager layoutManager, int layoutDirection) {
        if (spanIndex == null) {
            return false;
        }
        if (layoutManager instanceof StaggeredGridLayoutManager && isRTL(layoutDirection)) {
            return spanIndex == 0;
        }
        return (spanIndex % spanCount == spanCount - 1);
    }

    private boolean isFirst(Integer spanIndex, RecyclerView.LayoutManager layoutManager, int layoutDirection) {
        if (layoutManager instanceof StaggeredGridLayoutManager && isRTL(layoutDirection)) {
            final int spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            return spanIndex != null && spanIndex == spanCount - 1;
        }
        return spanIndex != null && spanIndex == 0;
    }

    private boolean isRTL(int layoutDirection) {
        return layoutDirection == View.LAYOUT_DIRECTION_RTL;
    }

    /**
     * @return null 占满一行或者不需要处理的布局，其他返回view所在的列 span索引
     */
    private Integer spanIndex(View view, RecyclerView.LayoutManager layoutManager) {
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            final StaggeredGridLayoutManager.LayoutParams staggerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) layoutParams;

            if (staggerLayoutParams.isFullSpan()) {
                return null;
            } else {
                if (DEBUG) {
                    final int position = staggerLayoutParams.getViewLayoutPosition();
                    final int spanIndex = staggerLayoutParams.getSpanIndex();
                    final boolean first = isFirst(spanIndex, layoutManager, view.getLayoutDirection());
                    Log.d(TAG, "spanIndex() -> position:" + position + " spanIndex:" + spanIndex
                            + " first:" + first);
                }
                return staggerLayoutParams.getSpanIndex();
            }
        } else if (layoutParams instanceof GridLayoutManager.LayoutParams) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            final int position = ((GridLayoutManager.LayoutParams) layoutParams).getViewLayoutPosition();
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            final int spanSize = spanSizeLookup.getSpanSize(position);

            if (spanSize == spanCount) {
                return null;
            } else {
                return ((GridLayoutManager.LayoutParams) layoutParams).getSpanIndex();
            }
        }
        return null;
    }

}
