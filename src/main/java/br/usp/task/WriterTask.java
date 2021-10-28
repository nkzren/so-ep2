package br.usp.task;

public class WriterTask implements Runnable {
    @Override
    public void run() {
        try {
            // Faz 100 acessos, modifica e para por 1ms
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
