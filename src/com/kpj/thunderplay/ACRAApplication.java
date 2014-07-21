package com.kpj.thunderplay;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "",
	mailTo = "kpjkpjkpjkpjkpjkpj+thunderplay@googlemail.com",
	mode = ReportingInteractionMode.TOAST,
	resToastText = R.string.crash_report)
public class ACRAApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		ACRA.init(this);
	}
}
