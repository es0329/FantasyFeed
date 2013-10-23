package com.es0329.fantasyfeed;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

/**
 * @author <a href="https://twitter.com/es0329">Eric</a>
 */
public class MainActivity extends FragmentActivity {
	private Fragment currentFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			currentFragment = new MasterFragment();
		} else {
			currentFragment = getSupportFragmentManager().getFragment(
					savedInstanceState, "content");
		}
		setFragment(currentFragment);
	}

	protected void setFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmentHolder, currentFragment).commit();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "content",
				currentFragment);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = (Window) getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
}
