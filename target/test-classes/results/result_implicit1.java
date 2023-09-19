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
        if (x < r + 1) {
            b_level = x_level.combine(r_level.combine(literal_level));
            r_level = x_level.combine(r_level.combine(literal_level));
            x_level = x_level.combine(r_level.combine(literal_level));
            b_level = x_level.combine(r_level.combine(literal_level));
            r_level = x_level.combine(r_level.combine(literal_level));
            x_level = x_level.combine(r_level.combine(literal_level));
            x = l + r;
            x_level = l_level.combine(r_level);
            r = b + 3 + b;
            r_level = b_level.combine(b_level.combine(literal_level));
            b = x;
            b_level = x_level;
            x = m.method(x) + 32;
            x_level = m_level.combine(x_level.combine(literal_level));
            r = x.method(b) + b.method(x);
            r_level = x_level.combine(b_level.combine(b_level.combine(x_level)));
            b = b.method(l) + x.method(m) + 2;
            b_level = b_level.combine(l_level.combine(x_level.combine(m_level.combine(literal_level))));
        } else {
            b_level = x_level.combine(r_level.combine(literal_level));
            r_level = x_level.combine(r_level.combine(literal_level));
            x_level = x_level.combine(r_level.combine(literal_level));
            x = 1;
            x_level = literal_level;
            r = b + 2 + l + r;
            r_level = b_level.combine(l_level.combine(r_level.combine(literal_level)));
            b = x;
            b_level = x_level;
        }
    }
}