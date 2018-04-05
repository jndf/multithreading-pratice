package multithreading;

public class Demo1 extends Thread{

    /**
     * *by yaojianan
     * 使用Thread来操作线程 使用start来开始线程，执行线程中的run部分
     */
    public void run(){
        for(int i=0;i<10;i++){
            System.out.println(Thread.currentThread().getName()+"第"+i+"次运行");
        }

    }

    public static void main(String[] args) {
        for (int i = 0;i<10;i++){
            Demo1 demo1 = new Demo1();
            demo1.start();
        }
    }
}
