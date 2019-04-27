package com.din.helper.log;

import android.os.Environment;
import android.util.Log;
import com.din.helper.date.DateHelper;
import com.din.helper.file.FileHelper;

import java.io.*;

/**
 * log日志统计保存
 */
public class Logger {

    private static final String TAG = "DZY";                // 日志TAG
    private static final String SUFFIX = ".log";            // 日志文件后缀
    private static final String DIR_LOG = "Log";            // 日志文件文件夹
    private static final String DIR_RUN_LOG = "run_log";    // 日志文件文件夹
    private static String logcatPath;                       // log文件路径
    private static boolean isDebug = true;                  // 是否是debug模式
    private LogDumper logDumper;                            // log输出文件线程
    private int PID;                                        // 进程的pid

    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int ONTHING = 6;
    private static final int level = VERBOSE;

    private Logger() {
        init();
        PID = android.os.Process.myPid();
    }

    private static class Instance {
        private static final Logger INSTANCE = new Logger();
    }

    public static Logger getInstance() {
        return Instance.INSTANCE;
    }

    public static void v(String msg) {
        if (level <= VERBOSE) {
            if (isDebug) {
                Log.v(TAG, msg);
            }
        }
    }

    public static void d(String msg) {
        if (level <= DEBUG) {
            if (isDebug) {
                Log.d(TAG, msg);
            }
        }
    }

    public static void i(String msg) {
        if (level <= INFO) {
            if (isDebug) {
                Log.i(TAG, msg);
            }
        }
    }

    public static void w(String msg) {
        if (level <= WARN) {
            if (isDebug) {
                Log.w(TAG, msg);
            }
        }
    }

    public static void e(String msg) {
        if (level <= ERROR) {
            if (isDebug) {
                Log.e(TAG, msg);
            }
        }
    }

    /**
     * 初始化日志存储目录（需要先申请文件读写权限）
     */
    public Logger init() {
        logcatPath = FileHelper.getInstance().getFolders(DIR_LOG, DIR_RUN_LOG);
        if (logcatPath.equals("") && logcatPath == null) {
            logcatPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + DIR_LOG + DIR_RUN_LOG;
        }
        return this;
    }

    /**
     * 初始化完成之后才可以开始保存日志
     * @return
     */
    public Logger start() {
        if (logDumper == null) {
            logDumper = new LogDumper(String.valueOf(PID), logcatPath);
        }
        if (!logDumper.isAlive()) {
            logDumper.start();
        }
        return this;
    }

    /**
     * 停止输出日志
     * @return
     */
    public Logger stop() {
        if (logDumper == null) {
            logDumper.stopLogs();
            logDumper = null;
        }
        return this;
    }

    /**
     * 打印所有的日志信息
     * @return
     */
    public Logger all() {
        logDumper.all();
        return this;
    }

    /**
     * 打印只带tag的日志信息
     * @return
     */
    public Logger tag() {
        logDumper.tag();
        return this;
    }

    public Logger v() {
        logDumper.v();
        return this;
    }

    public Logger d() {
        logDumper.d();
        return this;
    }

    public Logger i() {
        logDumper.i();
        return this;
    }

    public Logger w() {
        logDumper.w();
        return this;
    }

    public Logger e() {
        logDumper.e();
        return this;
    }

    /**
     * 输出日志文件的线程
     */
    private class LogDumper extends Thread {

        private Process logcatProcess;
        private BufferedReader mBufferedReader;
        private FileOutputStream mFileOutputStream;
        private boolean mRunning = true;
        private String cmds;
        private String mPID;

        public LogDumper(String pid, String dir) {
            mPID = pid;
            try {
                mFileOutputStream = new FileOutputStream(new File(dir, DateHelper.getTimeSecond() + SUFFIX));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (isDebug) {
                tag();
            } else {
                all();
            }
        }

        private LogDumper all() {
            cmds = "logcat  | grep \"(" + mPID + ")\"";
            return this;
        }

        private LogDumper tag() {
            cmds = "logcat -s " + TAG;
            return this;
        }

        private LogDumper v() {
            cmds = "logcat *:e *:v | grep \"(" + mPID + ")\"";
            return this;
        }

        private LogDumper d() {
            cmds = "logcat *:e *:d | grep \"(" + mPID + ")\"";
            return this;
        }

        private LogDumper i() {
            cmds = "logcat *:e *:i | grep \"(" + mPID + ")\"";
            return this;
        }

        private LogDumper w() {
            cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
            return this;
        }

        private LogDumper e() {
            cmds = "logcat *:e | grep \"(" + mPID + ")\"";
            return this;
        }

        private LogDumper stopLogs() {
            mRunning = false;
            return this;
        }

        @Override
        public void run() {
            super.run();
            try {
                logcatProcess = Runtime.getRuntime().exec(cmds);
                mBufferedReader = new BufferedReader(new InputStreamReader(logcatProcess.getInputStream()), 1024);
                String line = null;
                while (mRunning && (line = mBufferedReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    if (mFileOutputStream != null && line.contains(mPID)) {
                        mFileOutputStream.write(("|======|" + DateHelper.getTimeSecond() + " " + line + "\n").getBytes());
                    }
                }
                mBufferedReader.close();
                mFileOutputStream.close();
                logcatProcess.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (logcatProcess != null) {
                    logcatProcess.destroy();
                    logcatProcess = null;
                }
                if (mBufferedReader != null) {
                    try {
                        mBufferedReader.close();
                        mBufferedReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mFileOutputStream != null) {
                    try {
                        mFileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mFileOutputStream = null;
                }
            }
        }
    }
}