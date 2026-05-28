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

    public SharedBuffer(int N, ProducerConsumerController controller) {
        this.N = N;
        this.buffer = new String[N];
        this.controller = controller;
    }

    @Override
    public void enviar(String str) throws InterruptedException {
        this.enviar(str, 0);
    }

    @Override
    public String extraer() throws InterruptedException {
        return this.extraer(0);
    }

    // metodos para enviar y recibir, con id para los locks
    public void enviar(String str, int id) throws InterruptedException {
        controller.acquireProd(id);

        buffer[fin] = str;
        fin = (fin + 1) % N;

        controller.releaseProd(id);
    }

    public String extraer(int id) throws InterruptedException {
        String ret;

        controller.acquireCons(id);

        ret = buffer[ini];  // null si estuviera vacío
        buffer[ini] = null;
        ini = (ini + 1) % N;

        controller.releaseCons(id);

        return ret;
    }
}
