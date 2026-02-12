package util;

import java.util.Arrays;

public class LockRompeEmpate extends CustomLock{
    private final int[] in;
    private final int[] last;

    public LockRompeEmpate(int p) {
        super(p);
        in = new int[p];
        Arrays.fill(in, -1);  // is this the preferred way?
        last = new int[p];
    }

    @Override
    public boolean takeLock(int i) {
        for (int j = 0; j < processes; j++) {
            in[i] = j;
            last[j] = i;
            for (int k = 0; k < processes; k++)
                if (k != i) while (in[k] >= in[i] && last[j] == i)
                    ;  // bucle de espera activa -- hot standby
        }
        return false;
    }

    @Override
    public void releaseLock(int i) {
        in[i] = -1;
    }
}
