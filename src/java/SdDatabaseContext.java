import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Environment;

public class SdDatabaseContext extends ContextWrapper {
	public SdDatabaseContext(Context context) {
		super(context);
	}

	/**
	 * 获得数据库路径，如果不存在，则创建对象对象
	 * 
	 * @param dbname
	 *            ，全路径
	 * @param mode
	 * @param factory
	 */
	@Override
	public File getDatabasePath(String dbname) {
		// 判断是否存在sd卡
		boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
		if (!sdExist) {// 如果不存在,
			return null;
		} else {// 如果存在，获取sd卡路径
			String sdDir = Environment.getExternalStorageDirectory().getPath();
			String dbDir = sdDir + "/" + dbname.substring(0, dbname.lastIndexOf("/"));
			File dirFile = new File(dbDir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			// 数据库文件是否创建成功
			boolean isFileCreateSuccess = false;
			// 判断文件是否存在，不存在则创建该文件
			File dbFile = new File(dbDir, dbname.substring(dbname.lastIndexOf("/") + 1));
			if (!dbFile.exists()) {
				try {
					isFileCreateSuccess = dbFile.createNewFile();// 创建文件
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				isFileCreateSuccess = true;
			}
			if (isFileCreateSuccess) {
				return dbFile;
			} else {
				return null;
			}
		}
	}

	/**
	 * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
	 * 
	 * @param name
	 * @param mode
	 * @param factory
	 */
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
		return result;
	}

	/**
	 * Android 4.0会调用此方法获取数据库。
	 * 
	 * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String,
	 *      int, android.database.sqlite.SQLiteDatabase.CursorFactory,
	 *      android.database.DatabaseErrorHandler)
	 * @param name
	 * @param mode
	 * @param factory
	 * @param errorHandler
	 */
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory, DatabaseErrorHandler errorHandler) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);

		return result;
	}
}