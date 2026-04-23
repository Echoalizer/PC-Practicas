//package readersWriters;
//
//import util.Producto;
//
//public class AlmacenRW implements AlmacenRWI {
//    private final ReadWriteController controller;
//
//    private final Producto[] buffer;  // usado como buffer circular de tamaño N
//
//    public AlmacenRW(int N) {
//        this.buffer = new Producto[N];
//        this.controller = new RWSem();
//    }
//
//    public AlmacenRW(int N, ReadWriteController controller) {
//        this.buffer = new Producto[N];
//        this.controller = controller;
//    }
//
//    @Override
//    public void escribir(Producto producto, int pos) {
//        try {
//            controller.request_write();
//
//            buffer[pos] = producto;
//
//            controller.release_write();
//        } catch (InterruptedException ignored) {
//        }
//    }
//
//    @Override
//    public Producto leer(int pos) {
//        Producto sol = null;
//        try {
//            controller.request_read();
//
//            // leer
//            sol = buffer[pos];
/// /            System.out.println(sol);
//
//            controller.release_read();
//        } catch (InterruptedException ignored) {
//        }
//        return sol;
//    }
//
//    public void checkBuffer() {
//        System.out.println( buffer[0]);
//    }
//}
