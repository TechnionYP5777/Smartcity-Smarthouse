package il.ac.technion.cs.smarthouse.system;

import org.junit.Test;

import com.google.gson.annotations.Expose;

import org.junit.Assert;

public class SavableTest {
    static int DEFAULT_MY_INT = 52;
    static int NEW_MY_INT = 123;

    static class A implements Savable {
        public static int constructorCalledCounter;

        @Expose public int myInt1 = DEFAULT_MY_INT;
        public int myInt2 = DEFAULT_MY_INT;
        @Expose public B b = new B();
        @Expose public C c = new C();

        public A() {
            ++constructorCalledCounter;
        }

        @Override public String toString() {
            return "A(" + myInt1 + ", " + myInt2 + "; " + constructorCalledCounter + ") " + b + " " + c + "\t ===> " + toJsonString();
        }
    }

    static class B implements Savable {
        public static int constructorCalledCounter;

        @Expose public int myInt1 = DEFAULT_MY_INT;
        public int myInt2 = DEFAULT_MY_INT;

        public B() {
            ++constructorCalledCounter;
        }

        @Override public String toString() {
            return "B(" + myInt1 + ", " + myInt2 + "; " + constructorCalledCounter + ")";
        }
    }

    static class C {
        public static int constructorCalledCounter;

        @Expose public int myInt1 = DEFAULT_MY_INT;
        public int myInt2 = DEFAULT_MY_INT;

        public C() {
            ++constructorCalledCounter;
        }

        @Override public String toString() {
            return "C(" + myInt1 + ", " + myInt2 + "; " + constructorCalledCounter + ")";
        }
    }

    @SuppressWarnings("static-method")
    @Test public void savableInSavableTest() throws Exception {
        A a = new A();

        Assert.assertEquals(A.constructorCalledCounter, 1);
        Assert.assertEquals(B.constructorCalledCounter, 1);
        Assert.assertEquals(C.constructorCalledCounter, 1);

        System.out.println((a + ""));

        a.myInt1 = a.myInt2 = a.b.myInt1 = a.b.myInt2 = a.c.myInt1 = a.c.myInt2 = NEW_MY_INT;

        System.out.println((a + ""));

        a.populate(a.toJsonString());

        System.out.println((a + ""));

        Assert.assertEquals(A.constructorCalledCounter, 1);
        Assert.assertEquals(B.constructorCalledCounter, 1);
        Assert.assertEquals(C.constructorCalledCounter, 2);

        Assert.assertEquals(a.myInt1, NEW_MY_INT);
        Assert.assertEquals(a.myInt2, NEW_MY_INT);
        
        Assert.assertEquals(a.b.myInt1, NEW_MY_INT);
        Assert.assertEquals(a.b.myInt2, NEW_MY_INT);
        
        Assert.assertEquals(a.c.myInt1, NEW_MY_INT);
        Assert.assertEquals(a.c.myInt2, DEFAULT_MY_INT);
        
        //----
        
        A aa = new A();
        System.out.println((aa + ""));
        
        Assert.assertEquals(A.constructorCalledCounter, 2);
        Assert.assertEquals(B.constructorCalledCounter, 2);
        Assert.assertEquals(C.constructorCalledCounter, 3);
        
        aa.populate(a.toJsonString());
        System.out.println((aa + ""));
        
        Assert.assertEquals(A.constructorCalledCounter, 2);
        Assert.assertEquals(B.constructorCalledCounter, 2);
        Assert.assertEquals(C.constructorCalledCounter, 4);
        
        Assert.assertEquals(aa.myInt1, NEW_MY_INT);
        Assert.assertEquals(aa.myInt2, DEFAULT_MY_INT);
        
        Assert.assertEquals(aa.b.myInt1, NEW_MY_INT);
        Assert.assertEquals(aa.b.myInt2, DEFAULT_MY_INT);
        
        Assert.assertEquals(aa.c.myInt1, NEW_MY_INT);
        Assert.assertEquals(aa.c.myInt2, DEFAULT_MY_INT);
    }
}
