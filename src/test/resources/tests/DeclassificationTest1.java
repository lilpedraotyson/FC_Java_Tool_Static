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

        //declassification(r, left) {
        if (x < r + 1) {
            x = l + r;
            r = b + 3 + b;
            b = x;

            x = l.method(x) + 32;
            //declassification(l, right) {
            if (x) {
                x = b;
                r = l + 2;
            }
            r = x.method(b) + b.method(x);
            //}
            b = b.method(l) + x.method(r) + 2;
        } else {
            x = 1;
            r = b + 2 + l + r;
            if (x) {
                r = b;
                r = l + 2;
            } else {
                l = b + x + 3;
            }
            b = x;
        }
        //}
    }
}
