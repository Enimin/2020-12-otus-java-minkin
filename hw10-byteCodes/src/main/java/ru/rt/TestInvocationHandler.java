package ru.rt;

import ru.rt.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

class TestInvocation {
    private TestInvocation(){}

    static TestLogingInerface createMyClass() {
        InvocationHandler handler = new TestInvocationHandler(new TestLoging());
        return (TestLogingInerface) Proxy.newProxyInstance(TestInvocation.class.getClassLoader(),
                new Class<?>[]{TestLogingInerface.class}, handler);
    }


    static class TestInvocationHandler implements InvocationHandler{
        private Object obj;

        public TestInvocationHandler(Object o) {
            obj = o;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            boolean isAnnotate = obj.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Log.class);
            if (isAnnotate) {
                System.out.println("executed method: " + method.getName() + ", param(s): " + Arrays.toString(args));
            }
            return method.invoke(obj, args);
        }
    }
}
