package com.hy.rpc.protocol.rmi;

import com.hy.rpc.RpcContext;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.RemoteInvocation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class RmiRemoteInvocation extends RemoteInvocation {
    private static final long serialVersionUID = 1L;
    private static final String dubboAttachmentsAttrName = "dubbo.attachments";

    /**
     * executed on consumer side
     */
    public RmiRemoteInvocation(MethodInvocation methodInvocation) {
        super(methodInvocation);
        // 添加附加值的属性
        addAttribute(dubboAttachmentsAttrName, new HashMap<String, String>(RpcContext.getContext().getAttachments()));
    }

    /**
     * Need to restore context on provider side (Though context will be overridden by Invocation's attachment
     * when ContextFilter gets executed, we will restore the attachment when Invocation is constructed, check more
     * 需要在提供者端恢复上下文（尽管上下文将被Invocation的附件覆盖
     * 当ContextFilter执行时，我们将在构造Invocation时恢复附件，检查更多
     * from {@link com.hy.rpc.proxy.InvokerInvocationHandler}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object targetObject) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        // 获得上下文
        RpcContext context = RpcContext.getContext();
        // 设置参数
        context.setAttachments((Map<String, String>) getAttribute(dubboAttachmentsAttrName));
        try {
            return super.invoke(targetObject);
        } finally {
            // 清空参数
            context.setAttachments(null);
        }
    }
}
