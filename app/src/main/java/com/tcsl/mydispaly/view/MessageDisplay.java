package com.tcsl.mydispaly.view;

import android.app.Presentation;
import android.content.Context;
import android.view.Display;

public class MessageDisplay extends Presentation {

	/**
	 * 辅屏幕 消息类
	 * @param outerContext
	 * @param display
	 */
	public MessageDisplay(Context outerContext, Display display) {
		super(outerContext, display);
	}

}
