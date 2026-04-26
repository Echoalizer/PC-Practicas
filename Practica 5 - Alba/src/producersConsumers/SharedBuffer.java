package producersConsumers;


public class SharedBuffer implements AlmacenPCI {
    private final ProducerConsumerController controller;

    private final int N;
    private final String[] buffer;  // usado como buffer circular de tamaño N

    private int ini = 0, fin = 0;

    public SharedBuffer(int N) {
        this.N = N;
        this.buffer = new String[N];
        this.controller = new ControllerSem(N);
    }

    @Override
    public void almacenar(String str) throws InterruptedException {
        controller.acquireProd();

        buffer[fin] = str;
        fin = (fin + 1) % N;

        controller.releaseProd();
    }

    @Override
    public String extraer() throws InterruptedException {
        String ret;

        controller.acquireCons();

        ret = buffer[ini];  // null si estuviera vacío
        buffer[ini] = null;
        ini = (ini + 1) % N;

        controller.releaseCons();

        return ret;
    }
}
