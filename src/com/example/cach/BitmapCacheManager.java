package com.example.cach;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class BitmapCacheManager {
	private static final String TAG = BitmapCacheManager.class.getSimpleName();
	private LruCache<String, Bitmap> mCache = null;
	private View viewGroup;
	private int defaultRes, loadingRes = 0;
	private BitmapCreator creator;
	private String cacheDir;
	private static boolean isDebug = false;

	/**
	 * creater an imagecachemanager ,use HttpBitmapCreator as default,and have no file cache
	 * 
	 * @param maxSize
	 *            max cache size
	 * @param defaultRes
	 *            default image resource id
	 * @param listview
	 *            AbsListView
	 */
	public BitmapCacheManager(int maxSize, int defaultRes, View viewGroup) {
		this(maxSize, defaultRes, viewGroup, null, null);
	}

	/**
	 * creater an imagecachemanager ,use given BitmapCreator,and have no file cache
	 * 
	 * @param maxSize
	 *            max cache size
	 * @param defaultRes
	 *            default image resource id
	 * @param listview
	 *            AbsListView
	 * @param creator
	 *            BitmapCreator锛宨f null ,use HttpBitmapCreator
	 */
	public BitmapCacheManager(int maxSize, int defaultRes, View viewGroup, BitmapCreator creator) {
		this(maxSize, defaultRes, viewGroup, creator, null);
	}

	/**
	 * creater an imagecachemanager ,use given BitmapCreator,and use given cacheDir to cache image file
	 * 
	 * @param maxSize
	 *            max cache size
	 * @param defaultRes
	 *            default image resource id
	 * @param listview
	 *            AbsListView
	 * @param creator
	 *            BitmapCreator锛宨f null ,use HttpBitmapCreator
	 * @param cacheDir
	 *            file cace dir,if null means no file cache
	 */
	public BitmapCacheManager(int maxSize, int defaultRes, View viewGroup, BitmapCreator creator, String cacheDir) {
		mCache = new BitmapLruCache<String, Bitmap>(maxSize, new DefaultBitapSizeCaculator());
		if (viewGroup == null) {
			throw new NullPointerException("BitmapCacheManager : viewGroup is null");
		}
		this.viewGroup = viewGroup;
		this.defaultRes = defaultRes;
		setCacheDir(cacheDir);
		setCreator(creator);
	}

	public void setViewImage(String id, String imgPath, View view) {
		setViewImage(id, imgPath, view, true);
	}

	private static boolean process = true;

	public void setViewImage(String id, String imgPath, View view, boolean process) {
		BitmapCacheManager.process = process;
		if (TextUtils.isEmpty(imgPath) || view == null) {
			return;
		}
		String fullPath = id + "@@@" + imgPath;
		Bitmap bmp = mCache.get(imgPath);
		if (view instanceof ImageView) {
			if (bmp == null) {
				try {
					((ImageView) view).setImageResource(defaultRes);
				} catch (Exception e) {
				}
				view.setTag(fullPath);
				BitmapTask task = new BitmapTask(viewGroup, defaultRes, loadingRes, mCache, creator, cacheDir);
				task.execute(fullPath);
				d("no cache,create one ==========================>" + imgPath);
			} else {
				d("has memory cache ==>" + imgPath);
				((ImageView) view).setImageBitmap(bmp);
			}
		} else {
			if (bmp == null) {
				try {
					view.setBackgroundResource(defaultRes);
				} catch (Exception e) {
				}
				view.setTag(fullPath);
				BitmapTask task = new BitmapTask(viewGroup, defaultRes, loadingRes, mCache, creator, cacheDir);
				task.execute(fullPath);
				d("no cache,create one ==========================>" + imgPath);
			} else {
				d("has memory cache ==>" + imgPath);
				view.setBackgroundDrawable(new BitmapDrawable(bmp));
			}
		}

		bmp = null;
	}

	public String getCacheDir() {
		return cacheDir;
	}

	public void setCacheDir(String cacheDir) {
		this.cacheDir = cacheDir;
	}

	public int getDefaultRes() {
		return defaultRes;
	}

	public void setDefaultRes(int defaultRes) {
		this.defaultRes = defaultRes;
	}

	public BitmapCreator getCreator() {
		return creator;
	}

	/**
	 * set an BitmapCreator锛宒efault is HttpBitmapCreator
	 * 
	 * @param creator
	 */
	public void setCreator(BitmapCreator creator) {
		if (creator == null) {
			this.creator = new HttpBitmapCreator();
		} else {
			this.creator = creator;
		}
	}

	public BitmapSizeCalculator getCalculator() {
		BitmapSizeCalculator result = null;
		if (mCache instanceof BitmapLruCache) {
			BitmapLruCache cache = (BitmapLruCache) mCache;
			result = cache.getCalculator();
		}
		return result;
	}

	/**
	 * privoder an method to calculator bitmap size in memory锛屼负if null will return 1锛宮eans the element of the cache
	 * 
	 * @param calculator
	 */
	public void setCalculator(BitmapSizeCalculator calculator) {
		if (mCache instanceof BitmapLruCache) {
			BitmapLruCache cache = (BitmapLruCache) mCache;
			cache.setCalculator(calculator);
		}
	}

	static class BitmapTask extends AsyncTask<String, Integer, Bitmap> {
		public static ExecutorService THREAD_POOL_CACHE = Executors.newCachedThreadPool();
		private LruCache<String, Bitmap> mCache;
		private View viewGroup;
		private BitmapCreator creator;
		private String imgPath;
		private String fullPath;
		private int defaultRes, loadingRes;
		private String cacheDir;

		public BitmapTask(View viewGroup, int defaultRes, int loadingRes, LruCache<String, Bitmap> mCache, BitmapCreator creator, String cacheDir) {
			this.mCache = mCache;
			this.viewGroup = viewGroup;
			this.creator = creator;
			this.defaultRes = defaultRes;
			this.loadingRes = loadingRes;
			this.cacheDir = cacheDir;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			fullPath = params[0];
			String[] ps = fullPath.split("@@@");
			if (ps.length < 2) {
				return null;
			}
			imgPath = ps[1];
			Bitmap bmp = mCache.get(imgPath);
			if (bmp != null) {
				return bmp;
			}

			if (creator != null) {
				File dirFile = null;
				File file = null;
				String localName = getLocalName(imgPath);
				if (!TextUtils.isEmpty(cacheDir)) {
					dirFile = new File(cacheDir);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					file = new File(dirFile, localName + ".png");
				}
				try {
					if (file != null && file.exists() && file.length() > 0) {
						bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
					} else {
						bmp = creator.createBitmapAnsy(imgPath);
						if (!TextUtils.isEmpty(cacheDir) && bmp != null) {
							if (dirFile != null && dirFile.isDirectory()) {
								FileOutputStream fos = null;
								try {
									fos = new FileOutputStream(file);
									bmp.compress(CompressFormat.PNG, 100, fos);
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								} finally {
									if (fos != null) {
										try {
											fos.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
							}

						}
					}
				} catch (Throwable t) {
					e(t.getClass().getSimpleName() + "==" + t.getMessage() + "=================");
				}

			}
			if (bmp != null) {
				Bitmap temp = bmp;
				/*if (process) {
//					bmp = BitmapUtils.getRoundedBitmap(temp);//這個是我們程序里用的，要把圖片做成圓圈圖，你不用把他主調就行了
				}*/
				// temp.recycle();
				temp = null;
				mCache.put(imgPath, bmp);
			}
			return bmp;
		}

		@SuppressLint("NewApi")
		public void execute(String path) {
			fullPath = path;
			if (android.os.Build.VERSION.SDK_INT < 11) {
				super.execute(path);
			} else {
				super.executeOnExecutor(THREAD_POOL_CACHE, path);
			}
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			View view = viewGroup.findViewWithTag(fullPath);
			if (view == null) {
				return;
			}
			if (view instanceof ImageView) {
				if (result == null || result.isRecycled()) {
					try {
						((ImageView) view).setImageResource(defaultRes);
					} catch (Exception e) {
					}
				} else {
					((ImageView) view).setImageBitmap(result);
				}
			} else {
				if (result != null && !result.isRecycled()) {
					view.setBackgroundDrawable(new BitmapDrawable(result));
				} else {
					try {
						view.setBackgroundResource(defaultRes);
					} catch (Exception e) {
					}

				}
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (fullPath == null)
				return;

			View view = viewGroup.findViewWithTag(fullPath);
			if (view == null) {
				return;
			}
			if (loadingRes != -1) {
				return;
			}

			if (view instanceof ImageView) {
				((ImageView) view).setImageResource(defaultRes);
			} else {
				view.setBackgroundResource(defaultRes);
			}

		}
	}

	static String getLocalName(String path) {
		String result = null;
		try {
			result = URLEncoder.encode(path, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public interface BitmapCreator {
		public Bitmap createBitmapAnsy(String path);
	}

	public interface BitmapSizeCalculator {
		public int calculateSize(String key, Bitmap value);
	}

	static void d(String message) {
		if (isDebug) {
			Log.d(TAG, message);
		}
	}

	static void i(String message) {
		if (isDebug)
			Log.i(TAG, message);
	}

	static void w(String message) {
		if (isDebug)
			Log.w(TAG, message);
	}

	static void e(String message) {
		if (isDebug)
			Log.e(TAG, message);
	}

	public int getLoadingRes() {
		return loadingRes;
	}

	public void setLoadingRes(int loadingRes) {
		this.loadingRes = loadingRes;
	}

	static public class HttpBitmapCreator implements BitmapCreator {

		@Override
		public Bitmap createBitmapAnsy(String path) {
			if (TextUtils.isEmpty(path) || !path.startsWith("http")) {
				return null;
			}
			Bitmap bmp = null;
			try {
				URL url = new URL(path);
				bmp = BitmapFactory.decodeStream(url.openStream());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return bmp;
		}

	}

	static public class DefaultBitapSizeCaculator implements BitmapSizeCalculator {

		@Override
		public int calculateSize(String key, Bitmap value) {
			int result = 0;
			if (value != null) {
				result = value.getHeight() * value.getWidth() * 8;
				// System.err.println(value.getHeight() + " * " +
				// value.getWidth() + " * 8 " + " = " + result);
			}
			return result;
		}

	}

	static class BitmapLruCache<K, V> extends LruCache<String, Bitmap> {
		private BitmapSizeCalculator calculator;

		public BitmapLruCache(int maxSize, BitmapSizeCalculator calculator) {
			super(maxSize);
			this.calculator = calculator;
		}

		// @Override
		// protected void entryRemoved(boolean evicted, String key, Bitmap
		// oldValue, Bitmap newValue) {
		// super.entryRemoved(evicted, key, oldValue, newValue);
		// if (oldValue != null) {
		// oldValue.recycle();
		// oldValue = null;
		// }
		// }

		@Override
		protected Bitmap create(String key) {
			return super.create(key);
		}

		@Override
		protected int sizeOf(String key, Bitmap value) {
			int size = 1;
			if (calculator != null) {
				size = calculator.calculateSize(key, value);
			}
			return size;
		}

		public BitmapSizeCalculator getCalculator() {
			return calculator;
		}

		public void setCalculator(BitmapSizeCalculator calculator) {
			this.calculator = calculator;
		}

	}
}
