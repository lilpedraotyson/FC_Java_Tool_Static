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
}

public class Application {

    public static void main(String[] args) {
        bot literal_level = new botLevel();
    }
}