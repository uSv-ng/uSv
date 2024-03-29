package com.usv.yzzkao;

import com.usv.data.YZZKDataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

public class SlideCutListView extends ListView {
	private int slidePosition, preSlidePosition;
	private int downY;
	private int downX;
	public static View itemView, preItemView;
	private Scroller scroller;
	private static final int SNAP_VELOCITY = 600;
	private VelocityTracker velocityTracker;
	public static boolean isSlide = false;
	private boolean isResponse = true;
	public static boolean isObstruct = true;
	private int mTouchSlop;
	private RemoveListener mRemoveListener;
	
	

	private static Animation scaleHideAnimation;
	private static Animation scaleShowAnimation;
	
	private YZZKDataBase YZZKDB;
	private SQLiteDatabase sdbw, sdbr;

	public SlideCutListView(Context context) {
		this(context, null);
	}

	public SlideCutListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideCutListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		scroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	public void setRemoveListener(RemoveListener removeListener) {
		this.mRemoveListener = removeListener;
	}

	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {

			//Log.e("sunzn", "dispatchTouchEvent ACTION_DOWN isSlide = " + isSlide);

			addVelocityTracker(event);

			downX = (int) event.getX();
			downY = (int) event.getY();

			slidePosition = pointToPosition(downX, downY);

			if (slidePosition == AdapterView.INVALID_POSITION) {
				return super.dispatchTouchEvent(event);
			}

			if (preItemView != null && preItemView.findViewById(R.id.tv_coating).getVisibility() == View.GONE) {
				itemView = preItemView;
				slidePosition = preSlidePosition;
			} else {
				itemView = getChildAt(slidePosition - getFirstVisiblePosition());
				preItemView = itemView;
				preSlidePosition = slidePosition;
			}

			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY || ((downX - event.getX()) > mTouchSlop && Math.abs(event.getY() - downY) < mTouchSlop)) {
					isSlide = true;
			}
			int isFinish = CheckedIs();
			if(1 == isFinish) {
				isSlide = false;
			}
			
			break;
		}
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			isObstruct = true;
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
		{
			if (itemView.findViewById(R.id.tv_coating).getVisibility() == View.VISIBLE) {
				isSlide = false;
			} else {
				isSlide = true;
			}
			int isFinish = CheckedIs();
			if(1 == isFinish) {
				isSlide = false;
			}
			
			break;
		}
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public int CheckedIs () {
		YZZKDB = new YZZKDataBase(SlideCutListView.this.getContext());
		//sdbw = YZZKDB.getWritableDatabase();
		sdbr = YZZKDB.getReadableDatabase();
		TextView tv = (TextView)itemView.findViewById(R.id.tv_item);
		String str = tv.getText().toString();
		String checked = "0";
		int isfinish = 0;
		Cursor cur = sdbr.query("JIHUA", new String[]{"checked"}, "things=?", new String[]{str}, null, null, null, null);
		while(cur.moveToNext()) {
			checked = cur.getString(cur.getColumnIndex("checked"));
		}
		isfinish = Integer.valueOf(checked);
		return isfinish;
	}
	public boolean onTouchEvent(MotionEvent ev) {
		//在这里进行是否完成判断
		
			if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
				addVelocityTracker(ev);
				final int action = ev.getAction();
				switch (action) {
				case MotionEvent.ACTION_MOVE:
					Log.e("sunzn", "onTouchEvent ACTION_MOVE isSlide = " + isSlide);
					if (isObstruct) {
						
						if (itemView.findViewById(R.id.tv_coating).getVisibility() == View.VISIBLE && isResponse == true) {
								scaleHideAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f);
								scaleHideAnimation.setDuration(250);
							scaleHideAnimation.setAnimationListener(new AnimationListener() {
								public void onAnimationStart(Animation animation) {
									isResponse = false;
									isObstruct = false;
								}
	
								public void onAnimationRepeat(Animation animation) {
	
								}
	
								public void onAnimationEnd(Animation animation) {
									isResponse = true;
									itemView.findViewById(R.id.tv_coating).setVisibility(View.GONE);
									itemView.findViewById(R.id.tv_functions).setClickable(true);
									itemView.findViewById(R.id.tv_functions).setOnClickListener(new OnClickListener() {
										//这里是点击“删除”执行的操作
										public void onClick(View v) {
											//这行代码的removeItem方法写在在TaskActivity里面
											mRemoveListener.removeItem(slidePosition);
											
										}
									});
								}
							});
							itemView.findViewById(R.id.tv_coating).startAnimation(scaleHideAnimation);
						} 
						else if (itemView.findViewById(R.id.tv_coating).getVisibility() == View.GONE && isResponse == true) {
							scaleShowAnimation = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f);
							scaleShowAnimation.setDuration(250);
							scaleShowAnimation.setAnimationListener(new AnimationListener() {
								public void onAnimationStart(Animation animation) {
									isResponse = false;
									isObstruct = false;
								}
								public void onAnimationRepeat(Animation animation) {
								}
								public void onAnimationEnd(Animation animation) {
									isSlide = false;
									isResponse = true;
									itemView.findViewById(R.id.tv_coating).setVisibility(View.VISIBLE);
								}
							});
	
							itemView.findViewById(R.id.tv_coating).startAnimation(scaleShowAnimation);
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					isObstruct = true;
					recycleVelocityTracker();
					isSlide = true;
					break;
				}
				return true;
			}
		return super.onTouchEvent(ev);
	}

	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			postInvalidate();
			if (scroller.isFinished()) {
				if (mRemoveListener == null) {
					throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");
				}

				itemView.scrollTo(0, 0);
			}
		}
	}

	private void addVelocityTracker(MotionEvent event) {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(event);
	}

	private void recycleVelocityTracker() {
		if (velocityTracker != null) {
			velocityTracker.recycle();
			velocityTracker = null;
		}
	}

	private int getScrollVelocity() {
		velocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) velocityTracker.getXVelocity();
		return velocity;
	}

	public interface RemoveListener {
		public void removeItem(int position);
	}

}
