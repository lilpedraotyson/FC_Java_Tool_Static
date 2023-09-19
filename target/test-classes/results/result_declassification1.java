interface top {

    public top combine(top x);

    public top combine(left x);

    public top combine(bot x);

    public top combine(right x);
}

interface left extends top {

    public top combine(top x);

    public left combine(left x);

    public left combine(bot x);

    public top combine(right x);
}

interface bot extends left, right {

    public top combine(top x);

    public left combine(left x);

    public bot combine(bot x);

    public right combine(right x);
}

interface right extends top {

    public top combine(top x);

    public top combine(left x);

    public right combine(bot x);

    public right combine(right x);
}

class topLevel implements top {

    public topLevel() {
    }

    public top combine(top x) {
        return this;
    }

    public top combine(left x) {
        return this;
    }

    public top combine(bot x) {
        return this;
    }

    public top combine(right x) {
        return this;
    }
}

class leftLevel implements left {

    public leftLevel() {
    }

    public top combine(top x) {
        return new topLevel();
    }

    public left combine(left x) {
        return this;
    }

    public left combine(bot x) {
        return this;
    }

    public top combine(right x) {
        return new topLevel();
    }
}

class botLevel implements bot {

    public botLevel() {
    }

    public top combine(top x) {
        return new topLevel();
    }

    public left combine(left x) {
        return new leftLevel();
    }

    public bot combine(bot x) {
        return this;
    }

    public right combine(right x) {
        return new rightLevel();
    }
}

class rightLevel implements right {

    public rightLevel() {
    }

    public top combine(top x) {
        return new topLevel();
    }

    public top combine(left x) {
        return new topLevel();
    }

    public right combine(bot x) {
        return this;
    }

    public right combine(right x) {
        return this;
    }
}

class Test {

    public String t;

    public Test(String t) {
        this.t = t;
    }

    public void method(Test x) {
    }
}

public class Application {

    public static void main(String[] args) {
        //}
        bot literal_level = new botLevel();
        //top
        Test x = new Test("Top");
        top x_level = new topLevel();
        //right
        Test r = new Test("right");
        right r_level = new rightLevel();
        //left
        Test l = new Test("left");
        left l_level = new leftLevel();
        //bot
        Test b = new Test("bot");
        bot b_level = new botLevel();
        left r_2_level = new leftLevel();
        //declassification(r, left) {
        if (x < r + 1) {
            b_level = x_level.combine(r_2_level.combine(r_2_level));
            r_2_level = x_level.combine(r_2_level.combine(r_2_level));
            r_2_level = x_level.combine(r_2_level.combine(r_2_level));
            x_level = x_level.combine(r_2_level.combine(r_2_level));
            x_level = x_level.combine(r_2_level.combine(r_2_level));
            b_level = x_level.combine(r_2_level.combine(r_2_level));
            r_2_level = x_level.combine(r_2_level.combine(r_2_level));
            x_level = x_level.combine(r_2_level.combine(r_2_level));
            x = l + r;
            x_level = l_level.combine(r_2_level);
            r = b + 3 + b;
            r_2_level = b_level.combine(b_level.combine(r_2_level));
            b = x;
            b_level = x_level;
            x = l.method(x) + 32;
            x_level = l_level.combine(x_level.combine(r_2_level));
            right l_1_level = new rightLevel();
            //declassification(l, right) {
            if (x) {
                l_1_level = l_1_level;
                l_1_level = l_1_level;
                x = b;
                l_1_level = l_1_level;
                r = l + 2;
                l_1_level = l_1_level.combine(l_1_level);
            }
            r = x.method(b) + b.method(x);
            r_2_level = x_level.combine(b_level.combine(b_level.combine(x_level)));
            //}
            b = b.method(l) + x.method(r) + 2;
            b_level = b_level.combine(l_level.combine(x_level.combine(r_2_level.combine(r_2_level))));
        } else {
            b_level = x_level.combine(r_2_level.combine(r_2_level));
            l_level = x_level.combine(r_2_level.combine(r_2_level));
            r_2_level = x_level.combine(r_2_level.combine(r_2_level));
            r_2_level = x_level.combine(r_2_level.combine(r_2_level));
            r_2_level = x_level.combine(r_2_level.combine(r_2_level));
            x_level = x_level.combine(r_2_level.combine(r_2_level));
            x = 1;
            x_level = r_2_level;
            r = b + 2 + l + r;
            r_2_level = b_level.combine(l_level.combine(r_2_level.combine(r_2_level)));
            if (x) {
                r_2_level = x_level;
                r_2_level = x_level;
                r = b;
                r_2_level = b_level;
                r = l + 2;
                r_2_level = l_level.combine(r_2_level);
            } else {
                l_level = x_level;
                l = b + x + 3;
                l_level = b_level.combine(x_level.combine(r_2_level));
            }
            b = x;
            b_level = x_level;
        }
    }
}