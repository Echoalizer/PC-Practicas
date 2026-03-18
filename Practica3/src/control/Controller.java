package control;

public interface Controller {

    public void acquireProd() throws InterruptedException;
    public void acquireCons() throws InterruptedException;

    public void releaseProd();
    public void releaseCons();
}
