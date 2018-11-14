package cn.lee.cplibrary.util.timer;

public interface ScheduledTask {
    public void run(int times);
    public void end();

}