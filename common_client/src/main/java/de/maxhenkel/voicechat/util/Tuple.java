package de.maxhenkel.voicechat.util;

public class Tuple<A, B> {
    private A a;
    private B b;

    public Tuple(A object, B object2) {
        this.a = object;
        this.b = object2;
    }

    public A getFirst() {
        return this.a;
    }

    public void setFirst(A object) {
        this.a = object;
    }

    public B getSecond() {
        return this.b;
    }

    public void setSecond(B object) {
        this.b = object;
    }
}
