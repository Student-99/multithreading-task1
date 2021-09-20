import java.util.Random;
import java.util.concurrent.Callable;

public class MyCallable implements Callable<String> {
    private int totalCount = 0;

    @Override
    public String call() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(2500);

                System.out.println(Thread.currentThread().getName() + " Всем привет!");
                totalCount++;
                if (new Random().nextBoolean()){
                    Thread.currentThread().interrupt();
                }
            }
        } catch (InterruptedException err) {

        } finally {
            System.out.printf("%s завершен\n", Thread.currentThread().getName());
            return String.format("Поток %s прошел %d итераций",Thread.currentThread().getName(),totalCount);
        }
    }
}
