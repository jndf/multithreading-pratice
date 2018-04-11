package multithreading;

import java.util.concurrent.*;

/**
 * 写一个Executor 多线程框架
 *
 */
public class Demo4 {

    /**
     * Executor包括四种创建线程池的对象
     * Java通过Executors提供四种线程池，分别为：
     * newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
     * newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     * newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
     * newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
     */



    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建一个定长的线程池 例子是10个线程的
            ExecutorService executorService = Executors.newFixedThreadPool(10);

        //1·框架执行execute方法，来执行一个线程类，但是没有返回值
            executorService.execute(new Demo2.Runner1());

        //2·使用submit执行线程，可以得到返回值
            Future<String> submit = executorService.submit(new Call1());
            System.out.println(submit.get());

        //3·CompletionService 可以将已完成任务与未完成的任务分离出来 ExecutorCompletionService此类将安排那些完成时提交的任务，把它们放置在可使用 take 访问的队列上
             CompletionService<String> completionService = new ExecutorCompletionService<String>(executorService);
             completionService.submit(new Call1());
             Future<String> future =completionService.take();
             System.out.println(future.get());

        //4·submit一个runnable和一个callable的区别
        //主要是靠futureTask区别，具体见我的博客   https://blog.csdn.net/qq_34582693?viewmode=list
        // 取消任务
        // future.cancel();

        // 线程池的关闭
        // executor.shutdown();

        //使用lambda表达式对内部类进行代码优化
    }


    static class Call1 implements  Callable {
        public String call() {
            return "返回callable线程";
        }
    }

    static class Run1 implements  Runnable {
        public void run() {
            //return "返回callable线程";
        }
    }
}