package com.pumpkin.applets_container.view.itemDecoration;

import static android.view.View.LAYOUT_DIRECTION_LTR;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalInternalItemDecoration extends RecyclerView.ItemDecoration {

    private final int mStartMargin;
    private final int mEndMargin;
    private final int mInternal;

    public HorizontalInternalItemDecoration(int startMargin, int endMargin, int internal) {
        mStartMargin = startMargin;
        mEndMargin = endMargin;
        mInternal = internal;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int layoutDirection = parent.getLayoutDirection();
        boolean ltr = layoutDirection == LAYOUT_DIRECTION_LTR;
        int left = 0;
        int right = 0;
        if (position == 0) {
            left = mStartMargin;
        } else {
            RecyclerView.Adapter adapter = parent.getAdapter();
            if (adapter != null) {
                left = mInternal;
                int itemCount = adapter.getItemCount();
                if (itemCount - 1 == position) {
                    right = mEndMargin;
                }
            }
        }
        if (ltr) {
            outRect.set(left, 0, right, 0);
        } else {
            outRect.set(right, 0, left, 0);
        }
    }
}
