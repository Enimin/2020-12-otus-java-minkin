package ru.rt;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TestLogingTest {
    private final PrintStream standartOut = System.out;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @BeforeEach
    void setUp(){
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown(){
        System.setOut(standartOut);
    }

    @Test
    void calculation1Test() {
        TestLogingInerface testLoging = TestInvocation.createMyClass();
        testLoging.calculation1(1);
        String outString = "executed method: calculation1, param(s): [1]";

        assertEquals(outString, out.toString().trim());
    }

    @Test
    void calculation11Test() {
        TestLogingInerface testLoging = TestInvocation.createMyClass();
        testLoging.calculation1("Test","OTUS");
        String outString = "executed method: calculation1, param(s): [Test, OTUS]";

        assertEquals(outString, out.toString().trim());
    }

    @Test
    void calculation2Test() {
        TestLogingInerface testLoging = TestInvocation.createMyClass();
        testLoging.calculation2(1,"2");
        String outString = "executed method: calculation2, param(s): [1, 2]";

        assertEquals(outString, out.toString().trim());
    }

    @Test
    void calculation3Test() {
        TestLogingInerface testLoging = TestInvocation.createMyClass();
        testLoging.calculation3(0,1,"2",true);
        String outString = "executed method: calculation3, param(s): [0, 1, 2, true]";

        assertEquals(outString, out.toString().trim());
    }

    @Test
    void calculation4Test() {
        TestLogingInerface testLoging = TestInvocation.createMyClass();
        testLoging.calculation4(1.0);
        String outString = "calculation4 is not annotated @Log";

        assertEquals(outString, out.toString().trim());
    }

}