package eu.realmcompany.regna.diagnostics.timings;

import lombok.Getter;

public class Timer {

    @Getter
    private long timerStart  = 0;
    @Getter
    private long timerStop   = 0;
    private long timerResult = 0;

    public Timer start() {
        this.timerStart = System.nanoTime();
        return this;
    }

    public Timer stop() {
        this.timerStop = System.nanoTime();
        this.timerResult = this.timerStop - this.timerStart;
        return this;
    }

    public long resultNano() {
        return this.timerResult;
    }

    public double resultMilli() {
        return this.timerResult / 1e+6;
    }

    public double resultSeconds() {
        return this.timerResult / 1e+9;
    }

    public static Timer timings() {
        return new Timer();
    }
}
