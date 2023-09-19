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
        //mid
        Test m = new Test("mid");
        mid m_level = new midLevel();
        //top
        Test b = new Test("bot");
        top b_level = new topLevel();
        x = m;
        x_level = m_level;
        m = b;
        m_level = b_level;
        b = x;
        b_level = x_level;
        x = m.method(x);
        x_level = m_level.combine(x_level);
        m = x.method(b);
        m_level = x_level.combine(b_level);
        b = b.method(m);
        b_level = b_level.combine(m_level);
    }
}