import org.develcarl.rpc.aop.RpcInvokeHook;
import org.develcarl.rpc.client.RpcClientProxyBuilder;
import org.develcarl.rpc.test.TestInterface;
import org.junit.Test;

/**
 * @author yichen
 * @description
 * @date 2018/5/18
 **/
public class RpcClientTest {

    @Test
    public void test(){
        RpcInvokeHook hook = new RpcInvokeHook()
        {
            public void beforeInvoke(String method, Object[] args)
            {
                System.out.println("before invoke in client" + method);
            }

            public void afterInvoke(String method, Object[] args)
            {
                System.out.println("after invoke in client" + method);
            }
        };

        final TestInterface testInterface
                = RpcClientProxyBuilder.create(TestInterface.class)
                .timeout(2000)
                .threads(1)
                .hook(hook)
                .connect("127.0.0.1", 3721)
                .build();

//        for(int i=0; i<10; i++)
//        {
            System.out.println("invoke result = " + testInterface.testMethod01("org.develcarl.rpc.aop.RpcInvokeHook", 123));
//        }
    }

}
