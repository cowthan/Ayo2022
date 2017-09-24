package org.ayo.notify.actionsheet;

import android.content.Context;


public class ActionSheetUtils {

	
	public static void showActionSheet(Context c, String[] items, ActionSheetDialog.OnSheetItemClickListener callback){
		ActionSheetDialog a = new ActionSheetDialog(c)
		.builder()
		.setCancelable(true)
		.setCanceledOnTouchOutside(true);
		for(String item: items){
			a.addSheetItem(item, ActionSheetDialog.SheetItemColor.Blue, callback);
		}
		a.show();
	}
}
