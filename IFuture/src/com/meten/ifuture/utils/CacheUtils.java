package com.meten.ifuture.utils;

import android.content.Context;

import com.lidroid.xutils.DbUtils;

import java.io.File;

public class CacheUtils {

	public static long getCacheSize(Context context){
		return getImageCacheSize(context)+getDBCacheSize(context);
	}
	
	public static String getCacheSizeStr(Context context){
		return formatFileSize(getCacheSize(context));
	}
	
	public static void clearCache(Context context){
//		String path = ImageLoader.getInstance().getDiskCache().getDirectory()
//				.getAbsolutePath();
        try{
            delete("/data/data/com.meten.ifuture/app_webview");
        }catch(Exception e){
            e.printStackTrace();
        }
		delete(context.getCacheDir());
        delete(context.getExternalCacheDir());
		DbUtils du = DBHelper.getDBUtils(context);
		String dbName = du.getDaoConfig().getDbName();
		context.deleteDatabase(dbName);
//		SharedPreferencesUtils.getInstance(context).clear();
	}
	
	public static long getImageCacheSize(Context context) {
		long imageCacheSize = 0;
		try {
			imageCacheSize = getFileSize(context.getCacheDir());
            imageCacheSize += getFileSize(context.getExternalCacheDir());
            imageCacheSize += getFileSize("/data/data/com.meten.ifuture/app_webview");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageCacheSize;
	}

	public static long getDBCacheSize(Context context) {
		long dbCacheSize = 0;
		DbUtils du = DBHelper.getDBUtils(context);
		String dbdir = du.getDatabase().getPath();
		File dbDirFile = new File(dbdir);
		try {
			dbCacheSize = getFileSize(dbDirFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbCacheSize;

	}
	
	public static long getShareCacheSize(){
		return 0;
	}

	public static long getFileSize(String filePath) throws Exception {
        return getFileSize(new File(filePath));
    }

    public static long getFileSize(File f) throws Exception {
		long size = 0;
		if(f == null || !f.exists()){
			return size;
		}
		if (f.isDirectory()) {
			File flist[] = f.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
		} else {
			size = f.length();
		}

		return size;
	}

      public static void delete(String filePath){
          File file = new File(filePath);
          delete(file);
      }

	  public static void delete(File file) {  
		  if(file == null || !file.exists()){
				return;
			}
	        if (file.isFile()) {  
	            file.delete();  
	            return;  
	        }  
	  
	        if(file.isDirectory()){  
	            File[] childFiles = file.listFiles();  
	            if (childFiles == null || childFiles.length == 0) {  
	                file.delete();  
	                return;  
	            }  
	      
	            for (int i = 0; i < childFiles.length; i++) {  
	                delete(childFiles[i]);  
	            }  
	            file.delete();
	        }
	    } 
	  
	  /**  
	     * 转换文件大小  
	     *  
	     * @param fileS  
	     * @return B/KB/MB/GB  
	     */  
	    public static String formatFileSize(long fileS) {  
	        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");  
	        String fileSizeString = "";  
	        if (fileS < 1024) {  
	            fileSizeString = df.format((double) fileS) + "B";  
	        } else if (fileS < 1048576) {  
	            fileSizeString = df.format((double) fileS / 1024) + "KB";  
	        } else if (fileS < 1073741824) {  
	            fileSizeString = df.format((double) fileS / 1048576) + "MB";  
	        } else {  
	            fileSizeString = df.format((double) fileS / 1073741824) + "G";  
	        }  
	        return fileSizeString;  
	    } 
}
