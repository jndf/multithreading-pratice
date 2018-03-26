package multithreading;


public class Demo2 {
    /**
     * by yao
     * 使用runnable接口来实现多线程，
     * 需要注意的地方是 runnable需要借助Thread来实现，作为Thread的一个target存在，
     * 由于两者都有run方法，在target存在时，优先调用target的方法，否则才调用原始的run
     * @param args
     */
    public static void main(String[] args) {
        Runner1 runner1 = new  Runner1();
        Runner2 runner2 = new  Runner2();
        Thread thread1 = new Thread(runner1);
        Thread thread2 = new Thread(runner2);
        thread1.start();
        thread2.start();
    }

        static class Runner1 implements  Runnable{
            public void run() {
                for(int i = 0;i<10;i++){
                    System.out.println("第1个测试线程，运行第"+i+"次");
                }
            }
        }

    static class Runner2 implements  Runnable{
        public void run() {
            for(int i = 0;i<10;i++){
                System.out.println("第2个测试线程，运行第"+i+"次");
            }
        }
    }


}
