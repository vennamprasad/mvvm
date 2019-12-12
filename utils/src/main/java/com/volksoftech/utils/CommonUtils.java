package com.volksoftech.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    public static final boolean IS_LOGS_ENABLED = true;
    public static final String DEFAULT_TAG = "DEBUG";
    public static final String DRAWABLE_TYPE = "drawable";
    public static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
    public static final String SHORT_DATE_FORMAT = "dd-MMM";
    public static final String FULL_DATE_FORMAT = "dd-MM-yyyy hh:mm";
    private final static String APP_TITLE = "YOUR-APP-NAME";
    private final static String APP_PNAME = "YOUR-PACKAGE-NAME";
    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 7;
    private static final String PREF_APP = "pref_app";
    private static final int READ_PHONE_STATE = 444;

    /**
     * Hides the activity's action bar
     *
     * @param activity the activity
     */
    public static void hideActionBar(AppCompatActivity activity) {
        // Call before calling setContentView();
        if (activity != null) {
            activity.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().hide();
            }
        }
    }

    /**
     * Sets the activity in Fullscreen mode
     *
     * @param activity the activity
     */
    public static void setFullScreen(AppCompatActivity activity) {
        // Call before calling setContentView();
        activity.getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void log(String tag, String msg) {
        if (IS_LOGS_ENABLED) {
            if (msg == null) {
                Log.i(tag, "--------------------------");
            } else {
                Log.i(tag, msg);
            }
        }
    }

    // --------------------------------------------> Network

    public static void log(String msg) {
        log(DEFAULT_TAG, msg);
    }

    // ------------------------------------------------> Activities Utils

    public static void logError(String tag, Throwable e) {
        if (IS_LOGS_ENABLED) {
            if (e != null) {
                String message = e.getMessage();
                if (message != null) {
                    Log.e(tag, message, e);
                }
            }
        }
    }

    public static void logError(Throwable e) {
        logError(DEFAULT_TAG, e);
    }

    public static void showToast(Context context, String msg, int... time) {
        int toastTime = Toast.LENGTH_LONG;
        if (time != null && time.length > 0) {
            toastTime = time[0];
        }
        Toast.makeText(context, msg, toastTime).show();
    }

    public static void showToast(Context context, int rid, int... time) {
        showToast(context, context.getString(rid), time);
    }

    public static void printAndShowToast(Context context, String msg) {
        if (IS_LOGS_ENABLED) {
            log(DEFAULT_TAG, msg);
            showToast(context, msg);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int[] getScreenSize(Context context) {
        int measuredWidth = 0;
        int measuredHeight = 0;
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        if (w != null) {
            w.getDefaultDisplay().getSize(size);
        }
        measuredWidth = size.x;
        measuredHeight = size.y;
        return new int[]{measuredWidth, measuredHeight};
    }

    public static void clearStackAndStartNewActivity(Context cxt, Class<?> cls) {
        Intent intent = new Intent(cxt, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        cxt.startActivity(intent);
    }

    public static void finishToParent(AppCompatActivity activity, boolean shouldUpRecreateTask) {
        if (activity == null) return;
        Intent upIntent = NavUtils.getParentActivityIntent(activity);
        if (upIntent != null) {
            if (NavUtils.shouldUpRecreateTask(activity, upIntent) || shouldUpRecreateTask) {
                // This activity is NOT part of this app's task, so
                // create a
                // new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(activity)
                        // Add all of this activity's parents to the back stack
                        .addNextIntentWithParentStack(upIntent)
                        // Navigate up to the closest parent
                        .startActivities();
                activity.finish();
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                NavUtils.navigateUpTo(activity, upIntent);
            }
        } else {
            activity.finish();
        }
    }

    public static void setActivityToFullScreen(AppCompatActivity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // ------------------------------------------------> Simple Validation

    public static void ActivityKeepScreenOn(AppCompatActivity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void addFragment(int mainFrameContainer, FragmentManager manager, Fragment nextFragment, boolean addToBackStack, String tag) {
        addFragmentWithAnimation(mainFrameContainer, manager, nextFragment, -1, -1, addToBackStack, tag);
    }

    // -----------------------------------------------> AsyncTask Utils

    public static void addFragmentWithAnimation(int mainFrameContainer, FragmentManager manager, Fragment nextFragment, int enterAnimRes, int exitAnimRes, String tag, boolean addToBackStack) {
        addFragmentWithAnimation(mainFrameContainer, manager, nextFragment, enterAnimRes, exitAnimRes, addToBackStack, tag);
    }

    // -----------------------------------------------> Clipboard Utils

    public static void addFragmentWithAnimation(int mainFrameContainer, FragmentManager manager, Fragment nextFragment, int enterAnimRes, int exitAnimRes, boolean addToBackStack, String tag) {
        manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (enterAnimRes > 0 && exitAnimRes > 0) {
            fragmentTransaction.setCustomAnimations(enterAnimRes, exitAnimRes);
        }
        fragmentTransaction.replace(mainFrameContainer, nextFragment, tag);
        if (addToBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public static int getResIdByName(Context context, String defType, String name) {
        int resource = context.getResources().getIdentifier(name, defType, context.getPackageName());
        return resource;
    }

    public static String getResNameById(Context context, int id) {
        String name = getStringResourceById(context, id);
        String[] imagePath = name.split("/");
        name = imagePath[imagePath.length - 1];
        return name;
    }

    public static int getDrawableIdByName(Context cxt, String name) {
        return getResIdByName(cxt, DRAWABLE_TYPE, name);
    }

    private static String getStringResourceById(Context context, int id) {
        String name;
        name = context.getResources().getString(id);
        return name;
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SafeVarargs
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    public static <T> void executeAsyncTask(AsyncTask<T, ?, ?> asyncTask, T... params) {
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    public static void copyToClipBoard(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    // -----------------------------------------------> File Utils
    public static String getFileExtension(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf("."));
            if (ext.contains("%")) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.contains("/")) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    public static String replaceFileNameWith(String path, String fileNameWithExt) {

        return splitPathToFolderPathAndFileNameWithExtByLastSlash(path)[0] + fileNameWithExt;
    }

    //---------------------> Time Units Conversion

    public static String[] splitPathToFolderPathAndFileNameWithExtByLastSlash(String fullPath) {

        String[] pathAndFileNameWithNoExt = null;

        int lastIndexOfSlash = fullPath.lastIndexOf("/");

        if (lastIndexOfSlash != -1) {
            // Has Extension
            String path = fullPath.substring(0, lastIndexOfSlash);
            String fileNameWithExt = fullPath.substring(lastIndexOfSlash + 1);

            if (!isStringEmpty(path) && !path.endsWith("/")) {
                path = path + "/";
            }

            pathAndFileNameWithNoExt = new String[]{path, fileNameWithExt};
        } else {
            // Has No Extension
            pathAndFileNameWithNoExt = new String[]{fullPath, null};

        }
        return pathAndFileNameWithNoExt;
    }

    public static String getFileNameWithExtension(String path) {
        File tempFile = new File(path);
        return tempFile.getName();
    }

    public static String getFileNameFromUrl(String url) {
        String fileName = null;
        if (url != null) {
            if (url.contains(File.separator)) {
                fileName = url.substring(url.lastIndexOf(File.separator) + 1);
            } else {
                fileName = url;
            }
        }
        return fileName;
    }

    //---------------------> Epoch

    public static void deleteFilesLessThanTheGivenMillis(File folder, String ext, long millis) {
        long minMillis = System.currentTimeMillis() - millis;
        if (folder.isDirectory()) {
            for (File child : Objects.requireNonNull(folder.listFiles())) {
                long fileLatsModifiedDate = child.lastModified();
                if (fileLatsModifiedDate < minMillis) {
                    if (ext == null) {
                        child.delete();
                    } else {
                        String path = child.getAbsolutePath();
                        if (path.endsWith(ext)) {
                            child.delete();
                        }
                    }
                }
            }
        }
    }

    public static void DeleteRecursive(File fileOrDirectory, String ext) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles())) {
                DeleteRecursive(child, ext);
            }

        }
        if (ext == null) {
            fileOrDirectory.delete();
        } else {
            String path = fileOrDirectory.getAbsolutePath();
            if (path.endsWith(ext)) {
                fileOrDirectory.delete();
            }
        }
    }

    //--------------------->

    public static int convertMilliSecondsToMonths(long milliSeconds) {
        return (int) ((((((milliSeconds / 1000) / 60) / 60) / 24) / 30)) + 1;
    }

    public static long convertMilliSecondsToSeconds(long milliSeconds) {
        return milliSeconds / 1000;
    }

    public static long convertSecondsToMilliSeconds(long seconds) {
        return seconds * 1000;
    }

    public static String convertEpochMilliSecondsToDefaultFormatedDate(long epochMilliSeconds, String format) {
        Date date = new Date(epochMilliSeconds);
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sf.format(date);
    }

    public static long convertStringDateToEpochMilliSeconds(String date, String format) throws ParseException {
        long epochMilli = 0;
        if (date != null) {
            SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
            Date dateObject = sf.parse(date);
            epochMilli = Objects.requireNonNull(dateObject).getTime();
        }
        return epochMilli;
    }

    public static boolean isToday(String dateInDateFormat, String format) {
        boolean isToday = false;
        if (!isStringEmpty(dateInDateFormat)) {
            String todayDate = convertEpochMilliSecondsToDefaultFormatedDate(System.currentTimeMillis(), format);
            if (!isStringEmpty(todayDate) && todayDate.equals(dateInDateFormat)) {
                isToday = true;
            }
        }
        return isToday;
    }

    public static long getEndMillisOfTheGivenDayMilliseconds(long miliiseconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(miliiseconds));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime().getTime();
    }

    public static int getAge(long userBirthDateInMillis) {
        int age = 0;
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTimeInMillis(userBirthDateInMillis);
        if (birthDate.after(today)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
        if ((birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) || (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) && (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            if (check_READ_PHONE_STATE(context,Manifest.permission.ACCESS_NETWORK_STATE)) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                for (NetworkInfo networkInfo : info)
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
            }
        }
        return false;
    }


    public static boolean isStringMatchRegex(String stringToBeChecked, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringToBeChecked);
        return matcher.matches();
    }

    public static boolean isStringEmpty(CharSequence charsequence) {
        return TextUtils.isEmpty(charsequence);
    }

    public static String capitalizeFirstLetter(String text) {
        text = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
        return text;
    }

    public static String removeWhiteSpaces(String text) {

        return text.replaceAll("\\s", "");
    }

    public static String removeLastChar(String stringText, String endingChar) {
        if (!stringText.equals("")) {
            if (stringText.endsWith(endingChar)) {
                stringText = stringText.substring(0, stringText.length() - 1);
            }
        }
        return stringText;
    }

    public static String convertTextToAsterisks(String text) {
        String hiddenText = "";
        if (!isStringEmpty(text)) {
            char[] pad = new char[text.length()];
            Arrays.fill(pad, '*');
            hiddenText = String.valueOf(pad);
        }
        return hiddenText;
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    /*WEB Utils*/
    public static void enableJavaScript(WebView webView, boolean enabled) {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(enabled);
        }
    }

    public static void enableWebViewDebugging(boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(enabled);
        }
    }

    public static void syncCookieWithSystemBrowser(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.createInstance(context);
            CookieSyncManager.getInstance().startSync();
            CookieSyncManager.getInstance().sync();
        }
    }

    /**
     * Launch Rate the app dialogue when the app is launched for the first time.
     *
     * @param mContext the m context
     */
    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }
        SharedPreferences.Editor editor = prefs.edit();
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }
        editor.apply();
    }

    /**
     * Launch Rate the app dialogue when the app is launched based of some random logic.
     *
     * @param mContext the m context
     * @param editor   the editor
     */
    @SuppressLint("SetTextI18n")
    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using "
                + APP_TITLE
                + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText("Rate " + APP_TITLE);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    mContext.startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + APP_PNAME)));
                }
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);
        dialog.setContentView(ll);
        dialog.show();
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                    Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets boolean data.
     *
     * @param context the context
     * @param key     the key
     * @return the boolean data
     */
    static public boolean getBooleanData(Context context, String key) {

        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    /**
     * Gets int data.
     *
     * @param context the context
     * @param key     the key
     * @return the int data
     */
    static public int getIntData(Context context, String key) {
        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getInt(key, 0);
    }

    /**
     * Gets string data.
     *
     * @param context the context
     * @param key     the key
     * @return the string data
     */
    // Get Data
    static public String getStringData(Context context, String key) {
        return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getString(key, null);
    }

    /**
     * Save data.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
    // Save Data
    static public void saveData(Context context, String key, String val) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putString(key, val).apply();
    }

    /**
     * Save data.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
    static public void saveData(Context context, String key, int val) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putInt(key, val).apply();
    }

    /**
     * Save data.
     *
     * @param context the context
     * @param key     the key
     * @param val     the val
     */
    public static void saveData(Context context, String key, boolean val) {
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, val)
                .apply();
    }


    public static SharedPreferences.Editor getSharedPrefEditor(Context context, String pref) {
        return context.getSharedPreferences(pref, Context.MODE_PRIVATE).edit();
    }

    public static void saveData(SharedPreferences.Editor editor) {
        editor.apply();
    }

    /**
     * Iterate over all keys of the JSON
     *
     * @param jsonObject the json object
     * @return the hash map
     */
    public static HashMap<String, String> iterateOverJSON(JSONObject jsonObject) {
        Iterator<String> iter = jsonObject.keys();
        HashMap<String, String> keyValueMap = new HashMap<>();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String value = jsonObject.getString(key);
                keyValueMap.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return keyValueMap;
    }

    /**
     * Read and parse a JSON file stored in assets folder
     *
     * @param context  the context
     * @param filename the filename
     * @return the json object
     */
    public static JSONObject loadJSONFromAsset(Context context, String filename) {
        String json = null;
        JSONObject jsonObject = null;
        try {

            InputStream is = context.getAssets().open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];

            final int read = is.read(buffer);
            is.close();
            if (read > 0) {
                json = new String(buffer, "UTF-8");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Check if permission granted boolean.
     *
     * @param context    the context
     * @param permission the permission
     * @return the boolean
     */
    public static boolean checkIfPermissionGranted(Context context, String permission) {
        return (context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Disable screenshot functionality.
     *
     * @param activity the activity
     */
    public static void disableScreenshotFunctionality(AppCompatActivity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    /**
     * Enable strict mode.
     *
     * @param enable the enable flag
     */
    public static void enableStrictMode(boolean enable) {
        if (enable) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    /**
     * Write String data to a csv file
     *
     * @param sFileName the s file name
     * @param data      the data
     * @throws IOException   the io exception
     * @throws JSONException the json exception
     */
    public static void generateCsvFile(String sFileName, String data)
            throws IOException, JSONException {
        JSONObject objectToWrite = new JSONObject(data);
        File folder = new File(Environment.getExternalStorageDirectory() + "/Folder");

        boolean var = false;
        if (!folder.exists()) {
            var = folder.mkdir();
        }

        System.out.println("" + var);

        final String filename = folder.toString() + "/" + sFileName;

        FileWriter writer = new FileWriter(filename, true);

        try {

            writer.append(objectToWrite.get("x").toString());
            writer.append(',');
            writer.append(objectToWrite.get("y").toString());
            writer.append(',');
            writer.append(objectToWrite.get("z").toString());
            writer.append('\n');
            writer.flush();
            writer.close();
        } catch (Exception e) {
            writer.flush();
            writer.close();
            e.printStackTrace();
        }
    }

    /**
     * Gets random number in range.
     *
     * @param min the min
     * @param max the max
     * @return the random number in range
     */
    public static int getRandomNumberInRange(int min, int max) {
        return (min + (int) (Math.random() * ((max - min) + 1)));
    }

    public static boolean check_READ_PHONE_STATE(final Context context, final String readPhoneState) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, readPhoneState) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, readPhoneState)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{readPhoneState}, READ_PHONE_STATE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{readPhoneState}, READ_PHONE_STATE);
                }
                return true;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
