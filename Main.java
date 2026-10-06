public class Main {

    static class FibonacciThread extends Thread {

        private final Thread innerThread;

        public FibonacciThread() {
            super("Fibonacci-Thread");

            setPriority(Thread.MAX_PRIORITY);

            innerThread = new Thread(() -> {
                try {
                    System.out.println(
                            "Внутрішній thread started: "
                                    + Thread.currentThread().getName()
                    );

                    Thread.sleep(1000);

                    System.out.println("inner thread completed");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Inner-Thread");
        }

        @Override
        public void run() {

            innerThread.start();

            long first = 0;
            long second = 1;

            for (int i = 0; i < 10; i++) {

                try {

                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                System.out.println(
                        "number fibonaci #" + (i + 1) + ": " + first
                );

                Thread.yield();

                long next = first + second;
                first = second;
                second = next;
            }


            try {
                innerThread.join();

                System.out.println(
                        "Fibonacci-Thread: inner thread completed"
                );

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    static class MonitorThread extends Thread {

        private final Thread fibonacciThread;

        public MonitorThread(Thread fibonacciThread) {
            super("Monitor-Daemon");
            this.fibonacciThread = fibonacciThread;

            setDaemon(true);
        }

        @Override
        public void run() {

            while (fibonacciThread.isAlive()) {

                System.out.println(
                        "[Daemon] state Fibonacci-Thread: "
                                + fibonacciThread.getState()
                );

                try {
                    Thread.sleep(300);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println(
                    "[Daemon] Fibonacci-Thread completed"
            );
        }
    }


    public static void main(String[] args) {

        Thread mainThread = Thread.currentThread();

        System.out.println("General thread");
        System.out.println("name: " + mainThread.getName());
        System.out.println("Priority: " + mainThread.getPriority());
        System.out.println(
                "Group: " + mainThread.getThreadGroup().getName()
        );


        FibonacciThread fibonacciThread =
                new FibonacciThread();

        MonitorThread monitorThread =
                new MonitorThread(fibonacciThread);

        monitorThread.start();

        fibonacciThread.start();
        try {
            fibonacciThread.join();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        System.out.println();
        System.out.println("Fibonacci-Thread completed");

        System.out.println(
                "Fibonacci-Thread alive: "
                        + fibonacciThread.isAlive()
        );

        System.out.println(
                "General thread alive: "
                        + mainThread.isAlive()
        );

        System.out.println("The end");
    }
}