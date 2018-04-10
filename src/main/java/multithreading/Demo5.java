package multithreading;


import java.util.concurrent.*;

/**
 * 写一个Fork/join 多线程框架
 *
 */
public class Demo5  {
    /**
     * ForkJoinTask：我们要使用ForkJoin框架，必须首先创建一个ForkJoin任务。
     * 它提供在任务中执行 fork() 和 join() 操作的机制，通常情况下我们不需要直接继承 ForkJoinTask 类，而只需要继承它的子类，Fork/Join 框架提供了以下两个子类：
     *
     * RecursiveAction ：用于没有返回结果的任务。
     * RecursiveTask ：用于有返回结果的任务。
     * ForkJoinPool ：ForkJoinTask 需要通过 ForkJoinPool 来执行，任务分割出的子任务会添加到当前工作线程所维护的双端队列中，进入队列的头部。
     * 当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务。
     */

    //线程
    static  class ForkJoinTest1 extends RecursiveTask{
        /**
         *  执行线程的代码块
         */
        @Override
        protected Object compute() {
            SumTest sumTest1 = new SumTest(1,2);
            SumTest sumTest2 = new SumTest(3,4);
            /**
             * fork将多个线程分流运算
             */
            sumTest1.fork();
            sumTest2.fork();
            /**
             * join将多个线程结果获取，这样就达到了forkjoin的把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的思想
             */
            return (int)sumTest1.join()+(int)sumTest2.join();
        }
    }

    //测试类
     static  class SumTest extends RecursiveTask{
        int startNum;
        int endNum;

        public SumTest(int startNum,int endNum){
            this.startNum = startNum;
            this.endNum = endNum;
        }

        //计算加法
        @Override
        protected Object compute() {
            return (int)startNum+endNum;
        }
    }

    public static void main(String[] args) {
        ForkJoinPool ForkJoinPool = new ForkJoinPool();

        ForkJoinTest1 forkJoinTest1 = new ForkJoinTest1();

        Future sum = ForkJoinPool.submit(forkJoinTest1);
        try {
            System.out.println(sum.get());
        }catch(InterruptedException e) {

        }catch(ExecutionException e) {

        }


    }





}