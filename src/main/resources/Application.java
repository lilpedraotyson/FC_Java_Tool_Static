class Test {
    public String t;

    public Test(String t) {this.t = t;}

    public Test method1(Test x) {
        return x;
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

        x = l;
        y = x.method1(x);
        l = r.method1(x) + b + l;

        //declassification(y, High) {
        b = x;
        r = l.method1(b) * 10;
        //}

        if (x.method1(b) + "implicit") {
            r = b;
            b = b.method1(l);
            r = x.method1(b) + 10 + "combine";
        }
    }
}