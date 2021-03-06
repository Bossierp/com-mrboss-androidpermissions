package com.mrboss.androidpermissions;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import android.app.Activity;
import runtimepermissions.PermissionsResultAction;
import runtimepermissions.PermissionsManager;

public class AndroidPermissions extends CordovaPlugin {
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		final CallbackContext context = callbackContext;

		if(action.equals("androidPermissions")){
			try {
				/**
				 * 请求所有必要的权限
				 */
				PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this.cordova.getActivity(), new PermissionsResultAction() {
					@Override
					public void onGranted() {
						//Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onDenied(String permission) {
						//Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
					}
				});

				JSONObject r = new JSONObject();
				try {
					r.put("code", 0);
					r.put("success", "true");
					r.put("message", "已请求所需权限");
					context.success(r);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				JSONObject r = new JSONObject();
				try {
					r.put("code", -1);
					r.put("success", "true");
					r.put("message", e.getMessage());
					context.error(r);
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			}
		}

		closeAndroidPDialog();

		return true;
	}
	
	private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
