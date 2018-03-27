package multithreading;


import com.oracle.jrockit.jfr.Producer;

import java.util.LinkedList;

public class Demo3 {

    public static void main(String[] args) {
        //定义一个仓库,消费者和生产者都使用这一个仓库
        Repertory repertory=new Repertory();
        //定义三个生产者(p1,p2,p3)
        Producer p1=new Producer(repertory);
        Producer p2=new Producer(repertory);
        Producer p3=new Producer(repertory);
        //定义两个消费者(c1,c2)
        Consumer c1=new Consumer(repertory);
        Consumer c2=new Consumer(repertory);
        //定义5个线程(t1,t2,t3,t4,t5)
        Thread t1=new Thread(p1,"张飞");
        Thread t2=new Thread(p2,"赵云");
        Thread t3=new Thread(p3,"关羽");
        Thread t4=new Thread(c1,"刘备");
        Thread t5=new Thread(c2,"曹操");
        //因为关羽跟赵云的生产积极性高,所以把他们的线程优先级调高一点
        t3.setPriority(10);
        t2.setPriority(10);

        //启动线程
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }


    /**
     * 定义一个资源类
     */
    static class Product {
        int id;
        public Product(int id) {
            this.id=id;
        }
    }

    /**
     * 仓库
     */
    static class Repertory {
        //定义一个集合类用于存放产品.规定仓库的最大容量为10.
        public LinkedList<Product> store=new LinkedList<Product>();
        public LinkedList<Product> getStore() {
            return store;
        }
        public void setStore(LinkedList<Product> store) {
            this.store = store;
        }
        /* 生产者方法
         * push()方法用于存放产品.
         * 参数含义:第一个是产品对象
         * 第二个是线程名称，用来显示是谁生产的产品.
         * 使用synchronized关键字修饰方法的目的：
         * 最多只能有一个线程同时访问该方法.
         * 主要是为了防止多个线程访问该方法的时候，将参数数据进行的覆盖，从而发生出错.
         */
        public synchronized void push(Product p,String threadName)
        {
            /* 仓库容量最大值为10,当容量等于10的时候进入等待状态.等待其他线程唤醒
             * 唤醒后继续循环，等到仓库的存量小于10时，跳出循环继续向下执行准备生产产品.
             */
            while(store.size()==10){
                try {
                    //打印日志
                    System.out.println(threadName+"报告:仓库已满--->进入等待状态--->呼叫老大过来消费");
                    //因为仓库容量已满，无法继续生产，进入等待状态，等待其他线程唤醒.
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //唤醒所有等待线程
            this.notifyAll();
            //将产品添加到仓库中.
            store.addLast(p);
            //打印生产日志
            System.out.println(threadName+"生产了:"+p.id+"号产品"+" "+"当前库存来:"+store.size());
            try {
                //为了方便观察结果,每次生产完后等待0.1秒.
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        /** 消费者方法
         * pop()方法用于存放产品.
         * 参数含义:线程名称，用来显示是谁生产的产品.
         * 使用synchronized关键字修饰方法的目的：
         * 最多只能有一个线程同时访问该方法.
         * 主要是为了防止多个线程访问该方法的时候，将参数数据进行的覆盖，从而发生出错.
         */
        public synchronized void pop(String threadName){
            /* 当仓库没有存货时,消费者需要进行等待.等待其他线程来唤醒
             * 唤醒后继续循环，等到仓库的存量大于0时，跳出循环继续向下执行准备消费产品.
             */
            while(store.size()==0)
            {
                try {
                    //打印日志
                    System.out.println(threadName+"下命令:仓库已空--->进入等待状态--->命令小弟赶快生产");
                    //因为仓库容量已空，无法继续消费，进入等待状态，等待其他线程唤醒.
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //唤醒所有等待线程
            this.notifyAll();
            //store.removeFirst()方法将产品从仓库中移出.
            //打印日志
            System.out.println(threadName+"消费了:"+store.removeFirst().id+"号产品"+" "+"当前库存来:"+store.size());
            try {
                //为了方便观察结果,每次生产完后等待1秒.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生产者
     */
    static class  Producer implements Runnable {
        //定义一个静态变量来记录产品号数.确保每一个产品的唯一性.
        public Integer count=0;
        //定义仓库
        Repertory repertory=null;
        //构造方法初始化repertory(仓库)
        public   Producer(Repertory repertory) {
            this.repertory=repertory;
        }
        /* run()方法因为该方法中存在非原子性操作count++;
         * 当多个线程同时访问时会发生count++的多次操作,导致出错
         * 为该方法添加同步错做,确保每一次只能有一个生产者进入该模块。
         * 这样就能保证count++这个操作的安全性.
         */
        @Override
        public void run() {
            while (true) {
                synchronized(Producer.class){
                    count++;
                    Product product=new Product(count);
                    repertory.push(product,Thread.currentThread().getName());
                }


            }

        }
    }

    /**
     * 消费者
     */
    static class Consumer implements Runnable {
        //定义仓库
        Repertory repertory=null;
        //构造方法初始化repertory(仓库)
        public Consumer(Repertory repertory) {
            this.repertory=repertory;
        }
        //实现run()方法,并将当前的线程名称传入.
        @Override
        public  void run() {
            while(true){
                repertory.pop(Thread.currentThread().getName());
            }
        }

    }


}
