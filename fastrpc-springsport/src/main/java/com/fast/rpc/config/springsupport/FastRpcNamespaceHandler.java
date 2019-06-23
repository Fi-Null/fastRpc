package com.fast.rpc.config.springsupport;


import com.fast.rpc.config.ProtocolConfig;
import com.fast.rpc.config.RegistryConfig;
import com.fast.rpc.util.ConcurrentHashSet;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import java.util.Set;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class FastRpcNamespaceHandler extends NamespaceHandlerSupport {
    public final static Set<String> protocolDefineNames = new ConcurrentHashSet<String>();
    public final static Set<String> registryDefineNames = new ConcurrentHashSet<String>();
    public final static Set<String> serviceConfigDefineNames = new ConcurrentHashSet<String>();
    public final static Set<String> referenceConfigDefineNames = new ConcurrentHashSet<String>();

    @Override
    public void init() {
        registerBeanDefinitionParser("reference", new FastRpcBeanDefinitionParser(ReferenceConfigBean.class, false));
        registerBeanDefinitionParser("service", new FastRpcBeanDefinitionParser(ServiceConfigBean.class, true));
        registerBeanDefinitionParser("registry", new FastRpcBeanDefinitionParser(RegistryConfig.class, true));
        registerBeanDefinitionParser("protocol", new FastRpcBeanDefinitionParser(ProtocolConfig.class, true));
    }
}
