class Test {
    public String t;

    public Test(String t) {this.t = t;}

    public void method(Test x) {
    }
}

public class Application {
    public static void main(String[] args) {
        //top
        Test x = new Test("Top");

        //mid
        Test m = new Test("mid");

        //top
        Test b = new Test("bot");

        x = m;
        m = b;
        b = x;

        x = m.method(x);
        m = x.method(b);
        b = b.method(m);
    }
}
