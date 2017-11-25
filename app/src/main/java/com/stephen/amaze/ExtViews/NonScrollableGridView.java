package com.stephen.amaze.ExtViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

public class NonScrollableGridView extends GridView {

	public NonScrollableGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	 boolean expanded = false;
	 
	 public boolean isExpanded() {
		 return expanded;
	 }
	 
	 @Override
	 public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 if (isExpanded()) {
			 int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
			 super.onMeasure(widthMeasureSpec, expandSpec);
			 
			 ViewGroup.LayoutParams params = getLayoutParams();
			 params.height = getMeasuredHeight();
			 
		 } else {
			 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		 }
		 
	 }
	 
	 public void setExpanded(boolean expanded) {
		 this.expanded = expanded;
	 }

}