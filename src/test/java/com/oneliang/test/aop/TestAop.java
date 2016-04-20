package com.oneliang.test.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestAop {
	public static void main(String[] args){
		TestBean testBean=new TestBean();
		InvocationHandler handler=new TestInvocationHandler(testBean); 
		Object object=Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), TestBean.class.getInterfaces(), handler);
		System.out.println(object);
		try {
			Method method=TestBean.class.getMethod("setTestInterface", TestInterface.class);
			System.out.println(method.invoke(testBean, object));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
//public class InvokeLogProxyFactory<TargetInterface> {
//    
//    public class MyInvocationHandler implements InvocationHandler {
//       
//       private Level logLevel;
//       
//       private TargetInterface impl;
//       
//       public MyInvocationHandler(Level logLevel, TargetInterface impl) {
//           this.logLevel = logLevel;
//           this.impl = impl;
//       }
//       public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//           final Logger log = Logger.getLogger( impl.getClass() );
//           String msg = MessageFormat.format( "Calling method ()",
//method.getName(), Arrays.toString( args ) );
//           log.log( logLevel, msg );
//           Object returnedResult = null;
//           try {
//               returnedResult = method.invoke( impl, args );
//           } catch ( InvocationTargetException e ) {
//              String msg1 = MessageFormat.format( "Call method[]: catch exceptions:", 
//method.getName() );
//              log.log( logLevel, msg1, e.getCause() );
//              throw e.getCause();
//           } catch( Throwable e ) {
//              log.error( "Runtime exception:", e );
//              throw e;
//           }
//           log.log( logLevel, "returned val=" + returnedResult );
//           return returnedResult;
//       }
//    }
//    
//    @SuppressWarnings("unchecked")
//    public TargetInterface create(TargetInterface impl, Level logLevel) {
//       MyInvocationHandler handler = new MyInvocationHandler( logLevel, impl);
//         Object result = Proxy.newProxyInstance(impl.getClass().getClassLoader(),
//                 impl.getClass().getInterfaces(),
//                 handler);
//         return (TargetInterface) result;      
//    }
//    
//}
