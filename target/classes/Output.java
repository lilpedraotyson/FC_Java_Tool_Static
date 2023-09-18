import pyryy;

interface top {
}

interface left extends top {
}

interface bot extends left, right {
}

interface right extends top {
}

class topLevel implements top {

    public topLevel() {
    }

    public right combine(top x) {
        return new rightLevel();
    }

    public left combine(left x) {
        return new leftLevel();
    }

    public bot combine(bot x) {
        return new botLevel();
    }

    public right combine(right x) {
        return new rightLevel();
    }
}

class leftLevel implements left {

    public leftLevel() {
    }

    public left combine(top x) {
        return this;
    }

    public left combine(left x) {
        return this;
    }

    public bot combine(bot x) {
        return new botLevel();
    }

    public bot combine(right x) {
        return new botLevel();
    }
}

class botLevel implements bot {

    public botLevel() {
    }

    public bot combine(top x) {
        return this;
    }

    public bot combine(left x) {
        return this;
    }

    public bot combine(bot x) {
        return this;
    }

    public bot combine(right x) {
        return this;
    }
}

class rightLevel implements right {

    public rightLevel() {
    }

    public right combine(top x) {
        return this;
    }

    public bot combine(left x) {
        return new botLevel();
    }

    public bot combine(bot x) {
        return new botLevel();
    }

    public right combine(right x) {
        return this;
    }
}

class Try {

    public String t;

    public Try(String t) {
        this.t = t;
    }

    public void test1(Try x) {
        System.out.println("entrei1");
        System.out.println(x.toString());
    }
}

class Try1 {

    public String t1;

    public int t2;

    public static final boolean t3;

    public String t4;

    public Try1(String t) {
        this.t = t;
    }

    public Try1(String t, String t1) {
        this.t = t;
        this.t1 = t1;
        if (t == t1) {
            this.t2 = t1;
        }
    }

    public void test1(Try x) {
        System.out.println("entrei1");
        System.out.println(x.toString());
    }
}

public class Application_Linear {

    public static void main(String[] args) {
        //}
        bot literal_level = new botLevel();
        //top
        Try x = new Try("Top", 1, "afdasfa");
        top x_level = new topLevel();
        //right
        Try r = new Try("Right");
        right r_level = new rightLevel();
        //left
        Try l = new Try("Left");
        left l_level = new leftLevel();
        //bot
        Try y = new Try("Bot");
        bot y_level = new botLevel();
        left y_1_level = new leftLevel();
        //declassification(y, left) {
        y = x;
        y_1_level = x_level;
        x = m + "sad" + d + m.test(a, c, d) + 1 + true;
        x_level = m_level.combine(d_level.combine(m_level.combine(a_level.combine(c_level.combine(d_level.combine(literal_level))))));
        right x_2_level = new rightLevel();
        //declassification(x, right) {
        x = m.test1(y) + y;
        x_2_level = m_level.combine(y_1_level.combine(y_1_level));
        x = y + m;
        x_2_level = y_1_level.combine(m_level);
        x = y + 1;
        x_2_level = y_1_level.combine(literal_level);
        y = 1;
        y_1_level = literal_level;
        y = "sdsda";
        y_1_level = literal_level;
        y = true;
        y_1_level = literal_level;
        top y_3_level = new topLevel();
        //declassification(y, top) {
        if (x < y && z == 1) {
            n_level = x_2_level.combine(y_3_level.combine(z_level.combine(literal_level)));
            y_3_level = x_2_level.combine(y_3_level.combine(z_level.combine(literal_level)));
            x_2_level = x_2_level.combine(y_3_level.combine(z_level.combine(literal_level)));
            x = y;
            x_2_level = y_3_level;
            y = 1;
            y_3_level = literal_level;
            n = a + m.test(y);
            n_level = a_level.combine(m_level.combine(y_3_level));
        } else {
            d_level = x_2_level.combine(y_3_level.combine(z_level.combine(literal_level)));
            y_3_level = x_2_level.combine(y_3_level.combine(z_level.combine(literal_level)));
            a_level = x_2_level.combine(y_3_level.combine(z_level.combine(literal_level)));
            a_level = x_2_level.combine(y_3_level.combine(z_level.combine(literal_level)));
            if (x) {
                y_3_level = x_2_level;
                a_level = x_2_level;
                a_level = x_2_level;
                a = z;
                a_level = z_level;
                a = y;
                a_level = y_3_level;
                y = b;
                y_3_level = b_level;
            } else {
                d_level = x_2_level;
                d = m.test(y);
                d_level = m_level.combine(y_3_level);
            }
        }
        //}
        y = b;
        y_1_level = b_level;
        y = c;
        y_1_level = c_level;
        //}
        y = d;
        y_1_level = d_level;
    }
}
