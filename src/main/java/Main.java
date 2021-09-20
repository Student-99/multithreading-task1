import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("Задание 1");
        task1(4,10_000); // Задание 1


        System.out.println("\nЗадание 2 invokeAll");
        task2_invokeAll(4); // Задание 2 - ожидаем выполнения всех потоков из пула
        System.out.println("\nЗадание 2 invokeAny");
        task2_invokeAny(4); // Задание 2 - ожидаем выполнения хотя бы одного потока из пула

    }

    public static void task2_invokeAll(int countCallablesAndPoolSize) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(countCallablesAndPoolSize);
        Set<Callable<String>> callables = createCallables(countCallablesAndPoolSize);

        List<Future<String>> futureTasks = executorService.invokeAll(callables);
        System.out.println("Результат выполнения всех потоков:");
        futureTasks.forEach(s -> {
            if (s.isDone()) {
                try {
                    System.out.println(s.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        executorService.shutdown();
    }
    public static void task2_invokeAny(int countCallablesAndPoolSize) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(countCallablesAndPoolSize);
        Set<Callable<String>> callables = createCallables(countCallablesAndPoolSize);

        String futureTasks = executorService.invokeAny(callables);

        System.out.println("Результат выполнения первого завершившегося потока:");
        System.out.println(futureTasks);

        executorService.shutdown();

    }

    public static void task1(int countThread, long workingTimeDuration) throws InterruptedException {
        ThreadGroup threadGroup = new ThreadGroup("first");
        Runnable runnable = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(2500);

                    System.out.println(Thread.currentThread().getName() + " Всем привет!");
                }
            } catch (InterruptedException err) {

            } finally {
                System.out.printf("%s завершен\n", Thread.currentThread().getName());
            }
        };

        for (int i = 0; i < countThread; i++) {
            Thread thread = new Thread(threadGroup, runnable);
            thread.setName("Я поток " + i);
            thread.start();
        }
        Thread.sleep(workingTimeDuration);

        threadGroup.interrupt();

        //сделал это ожидание чтоб все потоки успели завершиться прежде чем идти дальше.
        while (threadGroup.activeCount()>0){
            Thread.sleep(1_000);
        }
    }


    public static Set<Callable<String>> createCallables(int countCallables) {
        Set<Callable<String>> callables = new HashSet<>();

        for (int i = 0; i < countCallables; i++) {
            Callable<String> callable = new MyCallable();
            callables.add(callable);
        }
        return callables;
    }
}
