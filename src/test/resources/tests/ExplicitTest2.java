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

        x = 1;
        r = b + 2 + l + r;
        b = x;

        x = m.method(2) + r;
        r = x.method(b) + l + 3 + r + r;
        b = b.method(l);
    }
}
