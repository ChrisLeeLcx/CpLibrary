package cn.lee.cplibrary.util.timer;

import android.os.Handler;
import android.os.Message;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 开启线程:设置线程的执行次数，执行间隔，执行延迟时间等，只要用于倒计时
 * 使用方法：
 * 1、先初始化实例
 * 2、调用start方法 ：开启
 * 3、不使用的时候：调用cancel，经常在Activity的OnDestroy方法中使用
 * eg：  用于倒计时：
 * scheduledTimer = new ScheduledTimer(new ScheduledHandler() {
 * @Override public void post(int times) { //time是线程到目前为止，执行的时间，单位s
 * codeBtn.setText((60 - times) + "s");
 * }
 * @Override public void end() { //线程结束
 * codeBtn.setText("重新获取");
 * }
 * }, 0, 1000, 60);
 * scheduledTimer.start();
 * // if (scheduledTimer!=null) { //取消线程
 * // scheduledTimer.cancel();
 * // }
 */
public class ScheduledTimer implements Runnable {
    private ScheduledExecutorService executorService;

    private ScheduledFuture future;

    private int corePoolSize = 1;

    private volatile int times = 0;

    private int maxTimes;

    private int initialDelay;

    private int delay;

    private boolean isSingerTask;

    private String TAG = "ScheduledTimer";

    private ScheduledTask scheduledTask;

    private ScheduledHandler scheduledHandler;

    public static final int NEVER_STOP = 0;

    public ScheduledTimer(int initialDelay) {
        this(null, initialDelay, initialDelay, 1);

    }

    public ScheduledTimer(int initialDelay, int delay, int maxTimes) {
        this(null, initialDelay, delay, maxTimes);

    }

    /**
     * @param scheduledTask
     * @param initialDelay  the time to delay first execution 单位ms
     * @param delay         ：the delay between the termination of one
     *                      execution and the s of the next 单位ms
     * @param maxTimes      ：线程执行次数
     *  1、用于倒计时  eg： ScheduledTimer（task， 2000， 1000， 60） 表示， 延迟2秒钟执行task任务， 然后以后每间隔1秒钟执行下一次任务，执行60次
     *  2、用于普通线程：eg：ScheduledTimer（task， 0， 0， 1）;
     */
    public ScheduledTimer(ScheduledTask scheduledTask, int initialDelay, int delay,
                          int maxTimes) {
        this.scheduledTask = scheduledTask;
        this.maxTimes = maxTimes;
        this.delay = delay;
        this.initialDelay = initialDelay;
        executorService = Executors.newScheduledThreadPool(corePoolSize);

    }

    public ScheduledTimer(ScheduledHandler scheduledHandler, int initialDelay,
                          int delay, int maxTimes, boolean... flags) {
        this.scheduledHandler = scheduledHandler;
        this.maxTimes = maxTimes;
        this.delay = delay;
        this.initialDelay = initialDelay;

        if (null != flags && flags.length > 0) {
            this.isSingerTask = flags[0];
        }

        executorService = Executors.newScheduledThreadPool(corePoolSize);

    }

    public void start() {
        cancel();
        future = executorService.scheduleWithFixedDelay(this, initialDelay,
                delay, TimeUnit.MILLISECONDS);

    }

    public void cancel() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }

    }

    @Override
    public void run() {
        times++;

        if (scheduledTask != null) {
            scheduledTask.run(times);
        }
        if (scheduledHandler != null) {
            Message message = Message.obtain();
            message.what = 1;
            handler.sendMessage(message);
        }
        if (maxTimes != 0 && times >= maxTimes) {
            if (future != null) {
                future.cancel(true);
            }

            if (scheduledTask != null) {
                scheduledTask.end();
            }
            if (scheduledHandler != null) {
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);
            }
        }

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (scheduledHandler != null) {
                    scheduledHandler.post(times);
                }
            }
            if (msg.what == 2) {
                if (scheduledHandler != null) {
                    scheduledHandler.end();
                }

            }
        }
    };
}
