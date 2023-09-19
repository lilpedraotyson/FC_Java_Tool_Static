interface high {

    public high combine(high x);

    public low combine(low x);
}

interface low extends high {

    public low combine(high x);

    public low combine(low x);
}

class highLevel implements high {

    public highLevel() {
    }

    public high combine(high x) {
        return this;
    }

    public low combine(low x) {
        return new lowLevel();
    }
}

class lowLevel implements low {

    public lowLevel() {
    }

    public low combine(high x) {
        return this;
    }

    public low combine(low x) {
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
        low literal_level = new lowLevel();
    }
}