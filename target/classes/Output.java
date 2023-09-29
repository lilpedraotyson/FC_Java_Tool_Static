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

    public Test method1(Test x) {
        return x;
    }
}

public class Application {

    public static void main(String[] args) {
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
        x = l;
        x_level = l_level;
        y = x.method1(x);
        y_level = x_level.combine(x_level);
        l = r.method1(x) + b + l;
        l_level = r_level.combine(x_level.combine(b_level.combine(l_level)));
        High y_1_level = new HighLevel();
        //declassification(y, High) {
        b = x;
        b_level = x_level;
        r = l.method1(b) * 10;
        r_level = y_1_level.combine(b_level.combine(literal_level));
        //}
        if (x.method1(b) + "implicit") {
            r_level = x_level.combine(b_level.combine(literal_level));
            b_level = x_level.combine(b_level.combine(literal_level));
            r_level = x_level.combine(b_level.combine(literal_level));
            r = b;
            r_level = b_level;
            b = b.method1(l);
            b_level = b_level.combine(l_level);
            r = x.method1(b) + 10 + "combine";
            r_level = x_level.combine(b_level.combine(literal_level));
        }
    }
}
