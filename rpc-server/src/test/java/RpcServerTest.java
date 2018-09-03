import org.develcarl.rpc.aop.RpcInvokeHook;
import org.develcarl.rpc.server.RpcServer;
import org.develcarl.rpc.server.RpcServerBuilder;
import org.develcarl.rpc.test.TestInterface;
import org.junit.Test;

/**
 * @author yichen
 * @description
 * @date 2018/6/18
 **/
public class RpcServerTest {

    @Test
    public void testServer(){
        TestInterface testInterface = (str, a) -> (str);
        RpcInvokeHook hook = new RpcInvokeHook() {
            public void beforeInvoke(String methodName, Object[] args) {
                System.out.println("beforeInvoke in server" + methodName);
            }

            public void afterInvoke(String methodName, Object[] args) {
                System.out.println("afterInvoke in server" + methodName);
            }
        };

        RpcServer rpcServer = RpcServerBuilder.create()
                .serviceInterface(TestInterface.class)
                .serviceProvider(testInterface)
                .threads(1)
//                .hook(hook)
                .bind(3721)
                .build();

        rpcServer.start();
    }

}
