package com.example.mybrowser2;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.mybrowser2.FoundWebView.ScrollInterface;

public class MainActivity extends Activity {
	final Activity activity = this;
	// private MyWebView webView;
	private FoundWebView webView;
	private MyEditText netAddress;
	private ImageView openAddress;
	private RelativeLayout layout;
	private LinearLayout addressFrame;

	private ProgressBar pb;
	private String curUrl;
	private String curTitle;
	private ListView list;
	private ListViewAdapter adapter;
	List<HistoryItem> items;
	private RelativeLayout menubarLayout;
	private LinearLayout menuLayout;
	private RelativeLayout addressbar;
	private LinearLayout webViewLayout;

	private ImageView backward;
	private ImageView forward;
	private ImageView home;
	private ImageView settings;

	private Bitmap bmp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());

		list = new ListView(this);
		webView = new FoundWebView(this);
		// webView = (FoundWebView)findViewById(R.id.mWebView);
		webView.setOnCustomScroolChangeListener(new ScrollInterface() {
			@Override
			public void onSChanged(int l, int t, int oldl, int oldt) {
				float m = webView.getContentHeight();
				float n = webView.getScale();
				float p = webView.getHeight();
				float q = webView.getScrollY();
				float r = webView.getScrollX();
				float x = webView.getScaleX();
				float y = webView.getScaleY();
				if ((int) (webView.getContentHeight() * webView.getScale()) == (webView
						.getHeight() + webView.getScrollY())) {
					// 滑动到底部，你要做的事·····
					Toast.makeText(getApplicationContext(), "bottom",
							Toast.LENGTH_SHORT).show();
				}
				if (webView.getScrollY() == 0) {
//					if (addressFrame.getVisibility() != View.VISIBLE) {
//						addressFrame.setVisibility(View.VISIBLE);
//					}
					// 滑动到顶部，你要做的事····
					Toast.makeText(getApplicationContext(), "top",
							Toast.LENGTH_SHORT).show();
				} else {
//					if (addressFrame.getVisibility() != View.GONE) {
//						addressFrame.setVisibility(View.GONE);
//					}
				}
			}
		});

		layout = (RelativeLayout) findViewById(R.id.mainLayout);


		addressbar =  (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.address_bar, null);
		layout.addView(addressbar);
		addressFrame = (LinearLayout) addressbar.findViewById(R.id.addressFrame);
        netAddress = (MyEditText) addressbar.findViewById(R.id.netAddress);
		netAddress.setSelectAllOnFocus(true);
		openAddress = (ImageView) addressbar.findViewById(R.id.open_address);
		//menubarLayout = (LinearLayout) findViewById(R.layout.menubar);
		// webView = (MyWebView) findViewById(R.id.webview);
		// webView = new MyWebView(this);


		webViewLayout = (LinearLayout) findViewById(R.id.web_view);
//		webViewLayout =  (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.webview_layout, null);
//		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//		//lp1.addRule(RelativeLayout.ABOVE, R.id.address_bar);
//		layout.addView(webViewLayout, lp1);
		netAddress.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO) {
					setTitleBarIcon(null, false);
					String url = netAddress.getText().toString();
					openUrl(url);
                }
				return false;
			}

		});

		netAddress.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_DEL) {
					if (netAddress.length() == 0) {
						if (webViewLayout.findViewById(webView.getId()) != null) {
							webViewLayout.removeView(webView);
						}
						if (webViewLayout.findViewById(list.getId()) == null) {
							webViewLayout.addView(list);
						}
					}
				}
				return false;
			}

		});

		pb = (ProgressBar) findViewById(R.id.progressBar1);

		DBManager db = new DBManager(this);
		items = db.findAll();
		adapter = new ListViewAdapter(this, items);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String url = items.get(arg2).getUrl();
				openUrl(url);
			}

		});
		list.setId(1001);
		list.setAdapter(adapter);
		//list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,data));
		webView.setId(1002);
		webViewLayout.addView(list);
		//layout.addView(menubarLayout);
		//layout.addView(webView);
		menubarLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.menubar, null);
		menuLayout = (LinearLayout) menubarLayout.findViewById(R.id.menubar);
		backward = (ImageView) menubarLayout.findViewById(R.id.backward);
		forward = (ImageView) menubarLayout.findViewById(R.id.forward);
		home = (ImageView) menubarLayout.findViewById(R.id.home);
		settings = (ImageView) menubarLayout.findViewById(R.id.settings);
		layout.addView(menubarLayout);
		menuLayout.setOnClickListener(menubarListener);
		backward.setOnClickListener(menubarListener);
		forward.setOnClickListener(menubarListener);
		home.setOnClickListener(menubarListener);
		settings.setOnClickListener(menubarListener);

		openAddress.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				setTitleBarIcon(null, false);
				String url = netAddress.getText().toString();
				openUrl(url);
			}
		});

		netAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {// 获得焦点
					// 在这里可以对获得焦点进行处理
					EditText clickEditText = (EditText) v;
					if (curUrl != null && !curUrl.isEmpty()) {
						clickEditText.setText(curUrl);
						clickEditText.selectAll();
					}
					setTitleBarIcon(null, true);
					openAddress.setVisibility(View.VISIBLE);
					menubarLayout.setVisibility(View.GONE);

				} else {// 失去焦点
					// 在这里可以对输入的文本内容进行有效的验证
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// 隐藏软键盘
					imm.hideSoftInputFromWindow(netAddress.getWindowToken(), 0);
					// 显示软键盘
					// imm.showSoftInputFromInputMethod(tv.getWindowToken(), 0);

					if (curTitle != null && !curTitle.isEmpty()) {
						netAddress.setText(curTitle);
					}
					openAddress.setVisibility(View.GONE);
					menubarLayout.setVisibility(View.VISIBLE);
				}
			}
		});

		// 设置WebView属性，能够执行Javascript脚本
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 设置滚动条风格
		webView.setHorizontalScrollBarEnabled(false);
		// webView.getSettings().setSupportZoom(true);
		// webView.getSettings().setBuiltInZoomControls(true); //设置可以支持缩放
		// webView.setInitialScale(25); //为25%，最小缩放等级 ，可以在这里根据需求来设置缩放比例.
		webView.setHorizontalScrollbarOverlay(true);
		// 加载需要显示的网页
		// webview.loadUrl("http://www.sohu.com/");
		// 设置Web视图
		webView.setWebViewClient(new HelloWebViewClient());
		webView.setWebChromeClient(new HelloWebChromeClient());
	}

	private View.OnClickListener menubarListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.backward:
				if (webView.canGoBack()) {
					webView.goBack();
				}
				//Toast.makeText(activity, "backward", Toast.LENGTH_SHORT).show();
				break;
			case R.id.forward:
				if (webView.canGoForward()) {
					webView.goForward();
				}
				//Toast.makeText(activity, "forward", Toast.LENGTH_SHORT).show();
				break;
			case R.id.home:
				openUrl("www.baidu.com");
				//Toast.makeText(activity, "home", Toast.LENGTH_SHORT).show();
				break;
			case R.id.settings:
				Toast.makeText(activity, "settings", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private void setTitleBarIcon(Bitmap leftIcon, boolean flag) {

		if (leftIcon != null) {
			Drawable drawable1 = new BitmapDrawable(leftIcon);
			drawable1.setBounds(0, 0, 40, 40);
			netAddress.setCompoundDrawables(drawable1, null, null, null);
		} else {
			if (flag) {
				Drawable drawable2 = getResources().getDrawable(R.drawable.cancel);
				drawable2.setBounds(0, 0, 40, 40);
				netAddress.setCompoundDrawables(null, null, drawable2, null);
			} else {
				netAddress.setCompoundDrawables(null, null, null, null);
			}
		}
	}

	private void openUrl(String url) {
		if (url == null || url.length() == 0) {
			return;
		}

		if (webViewLayout.findViewById(list.getId()) != null) {
			webViewLayout.removeView(list);
		}
		if (webViewLayout.findViewById(webView.getId()) == null) {
			webViewLayout.addView(webView);
		}

		if (URLUtil.isNetworkUrl(url) == true) {
			webView.loadUrl(url);
		} else {
			Toast.makeText(getApplicationContext(), "wrong url",
					Toast.LENGTH_SHORT);
			url = "http://" + url;
			webView.loadUrl(url);
		}

		netAddress.clearFocus();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();
				return true;
			} else {
				new AlertDialog.Builder(this)
						.setTitle("退出")
						.setMessage("退出浏览器吗？")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								}).setNegativeButton("否", null).show();
			}

		}

		return super.onKeyDown(keyCode, event);
	}

	private class HelloWebChromeClient extends WebChromeClient {

		private Animation animation;

		public void onProgressChanged(WebView view, int newProgress) {
			pb.setVisibility(View.VISIBLE);
			pb.setMax(100);
			pb.setProgress(newProgress);
			if (newProgress == 100) {
				pb.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onReceivedIcon (WebView view, Bitmap icon) {
			bmp = icon;
			setTitleBarIcon(icon, false);
			// TODO Auto-generated method stub
//			if (title != null && !title.isEmpty()) {
//				curTitle = title;
//				netAddress.setText(curTitle);
//			}
//			super.onReceivedTitle(view, title);
			//bmp = icon;
			super.onReceivedIcon(view, icon);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			// TODO Auto-generated method stub
			if (title != null && !title.isEmpty()) {
				curTitle = title;
				netAddress.setText(curTitle);
			}
			super.onReceivedTitle(view, title);
		}
	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public void doUpdateVisitedHistory(WebView view, String url,
				boolean isReload) {
			// TODO Auto-generated method stub
			super.doUpdateVisitedHistory(view, url, isReload);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			curUrl = url;

			WebHistoryItem item = webView.copyBackForwardList().getCurrentItem();
			if (item != null) {
				Long tsLong = System.currentTimeMillis()/1000;
				String ts = tsLong.toString();
				DBManager db = new DBManager(getApplicationContext());
				db.add(item.getTitle(), item.getUrl(), ts);
			}

			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			if (addressFrame.getVisibility() != View.VISIBLE) {
				addressFrame.setVisibility(View.VISIBLE);
			}
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	class MyWebView extends WebView {
		Context context;
		GestureDetector gd;

		public MyWebView(Context context) {
			super(context);

			this.context = context;
			gd = new GestureDetector(context, sogl);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			return gd.onTouchEvent(event);
		}

		GestureDetector.SimpleOnGestureListener sogl = new GestureDetector.SimpleOnGestureListener() {

			/*
			 * public boolean onFling(MotionEvent event1, MotionEvent event2,
			 * float velocityX, float velocityY) { if (event1.getRawX() >
			 * event2.getRawX()) { show_toast("swipe left"); } else {
			 * show_toast("swipe right"); } return true; }
			 */

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// TODO Auto-generated method stub
				/*
				 * if (e1.getRawX() > e2.getRawX()) { show_toast("swipe left");
				 * } else { show_toast("swipe right"); }
				 */
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		};

		void show_toast(final String text) {
			Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			t.show();
		}
	}
}
