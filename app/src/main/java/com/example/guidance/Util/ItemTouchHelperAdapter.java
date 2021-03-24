package com.example.guidance.Util;

/**
 * Created by Conor K on 24/03/2021.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemSwiped(int position);
}