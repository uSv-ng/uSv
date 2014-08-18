package com.usv.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.usv.yzzkao.R;
import com.usv.yzzkao.R.anim;
import com.usv.yzzkao.R.id;
import com.usv.yzzkao.R.layout;

public class ShareActivity extends Activity {

	private Button sharetofriend, sharetoqzone;
	
	private Button share_back;
	
	String imageurl, downloadurl;
	
	public static final String AppID = "101105873";
	protected Tencent mTencent;
	public IUiListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		sharetofriend = (Button) findViewById(R.id.shareToFriend);
		sharetofriend.setOnClickListener(new FriendListener());
		sharetoqzone = (Button) findViewById(R.id.shareToQzone);
		sharetoqzone.setOnClickListener(new QzoneListener());
		
		
		share_back = (Button)findViewById(R.id.share_back);
		share_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
				
			}
		});
		
		
		imageurl = "http://b171.photo.store.qq.com/psb?/V11LeOCI2W2u6Y/ahIfx4Hfy75dTQ"
				  +"uvDLQmLi7K2RPOap0vkT1PvamPamY!/b/dIM89GXvIQAA&bo=SABIAAAAAAADACU!";
		downloadurl = "http://zhushou.360.cn/detail/index/soft_id/1569667?recrefer=SE_D"
				      +"_%E8%B5%A2%E5%9C%A8%E4%B8%AD%E8%80%83#nogo";
		
		mTencent = Tencent.createInstance(AppID, this);
		
		listener = new IUiListener() {
			@Override
			public void onComplete(Object arg0) {
				System.out.println("share Success"+arg0.toString());
			}
			@Override
			public void onCancel() {
				System.out.println("share cancel");
			}
			@Override
			public void onError(UiError arg0) {
				System.out.println("share Error :"+arg0.errorMessage);
			}
};
	}
	
	public class FriendListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Bundle params = new Bundle();
			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		    params.putString(QQShare.SHARE_TO_QQ_TITLE, "赢在中考");//必填
		    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "中考生提分神器，我们都在用，再不用就out了");//选填
		    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, downloadurl);//必填
		    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageurl);
		    //params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, "file:///android_asset/image/symbol7272.png");
		    //ArrayList<String> imageUrls = new ArrayList<String>();
		    //imageUrls.add("http://imgcache.qq.com/music/photo/mid_album_300/V/E/000J1pJ50cDCVE.jpg");
		    //params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		    mTencent.shareToQQ(ShareActivity.this, params, listener);
		}
	}
	public class QzoneListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Bundle params = new Bundle();
		 	params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		    params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "赢在中考");//必填
		    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "中考生提分神器，我们都在用，再不用就out了");//选填
		    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, downloadurl);//必填
		    //params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/music/photo/mid_album_300/V/E/000J1pJ50cDCVE.jpg");
		    ArrayList<String> imageUrls = new ArrayList<String>();
		    imageUrls.add(imageurl);
		    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		    mTencent.shareToQzone(ShareActivity.this, params, listener);
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		overridePendingTransition(R.anim.in_alpha_scale, R.anim.push_right_out);
		return super.onKeyDown(keyCode, event);
	}
}
