interface top {

    public top combine(top x);

    public top combine(bot x);

    public top combine(mid x);
}

interface bot extends mid {

    public top combine(top x);

    public bot combine(bot x);

    public mid combine(mid x);
}

interface mid extends top {

    public top combine(top x);

    public mid combine(bot x);

    public mid combine(mid x);
}

class topLevel implements top {

    public topLevel() {
    }

    public top combine(top x) {
        return this;
    }

    public top combine(bot x) {
        return this;
    }

    public top combine(mid x) {
        return this;
    }
}

class botLevel implements bot {

    public botLevel() {
    }

    public top combine(top x) {
        return new topLevel();
    }

    public bot combine(bot x) {
        return this;
    }

    public mid combine(mid x) {
        return new midLevel();
    }
}

class midLevel implements mid {

    public midLevel() {
    }

    public top combine(top x) {
        return new topLevel();
    }

    public mid combine(bot x) {
        return this;
    }

    public mid combine(mid x) {
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
        //left
        Test l = new Test("left");
        //bot
        Test b = new Test("bot");
        bot b_level = new botLevel();
        x = l;
        x_level = l_level;
        r = b;
        r_level = b_level;
        b = x;
        b_level = x_level;
        bot x_1_level = new botLevel();
        //declassification(x, bot) {
        x = m.method(x);
        x_1_level = m_level.combine(x_1_level);
        r = x.method(b);
        r_level = x_1_level.combine(b_level);
        b = b.method(l);
        b_level = b_level.combine(l_level);
        x = b;
        x_1_level = b_level;
        r = l;
        r_level = x_1_level;
        //}
        x = r;
        x_level = r_level;
        r = m.method(x);
        r_level = m_level.combine(x_level);
        top b_2_level = new topLevel();
        //declassification(b, top) {
        r = b.method(b);
        r_level = b_2_level.combine(b_2_level);
        //}
        r = b.method(l);
        r_level = b_level.combine(l_level);
    }
}