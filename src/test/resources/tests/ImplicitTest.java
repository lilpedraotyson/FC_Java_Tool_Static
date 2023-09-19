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

        //right
        Test r = new Test("right");

        //left
        Test l = new Test("left");

        //bot
        Test b = new Test("bot");

        if (x < r + 1) {
            x = l;
            r = b;
            b = x;

            x = m.method(x);
            r = x.method(b);
            b = b.method(l);
        }
    }
}
