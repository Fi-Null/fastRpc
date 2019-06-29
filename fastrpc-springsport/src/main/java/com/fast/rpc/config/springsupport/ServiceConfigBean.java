package com.fast.rpc.config.springsupport;

import com.fast.rpc.config.ProtocolConfig;
import com.fast.rpc.config.RegistryConfig;
import com.fast.rpc.config.ServiceConfig;
import com.fast.rpc.util.CollectionUtil;
import com.fast.rpc.util.FrameworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


/**
 * @ClassName ServiceConfigBean
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:36
 * @Version 1.0
 **/
public class ServiceConfigBean<T> extends ServiceConfig<T> implements BeanFactoryAware,
        InitializingBean,
        ApplicationListener<ContextRefreshedEvent>,
        DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private transient BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void destroy() throws Exception {
        super.destroy0();
    }

    @Override
    public void afterPropertiesSet() {
        logger.debug("check service interface:%s config", getInterfaceName());

        checkRegistryConfig();
        checkProtocolConfig();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!isExported()) {
            export();
        }
    }

    private void checkRegistryConfig() {
        if (CollectionUtil.isEmpty(getRegistries())) {
            for (String name : FastRpcNamespaceHandler.registryDefineNames) {
                RegistryConfig rc = beanFactory.getBean(name, RegistryConfig.class);
                if (rc == null) {
                    continue;
                }
                if (FastRpcNamespaceHandler.registryDefineNames.size() == 1) {
                    setRegistry(rc);
                } else if (rc.isDefault() != null && rc.isDefault().booleanValue()) {
                    setRegistry(rc);
                }
            }
        }
        if (CollectionUtil.isEmpty(getRegistries())) {
            setRegistry(FrameworkUtils.getDefaultRegistryConfig());
        }
    }

    private void checkProtocolConfig() {
        if (CollectionUtil.isEmpty(getProtocols())) {
            for (String name : FastRpcNamespaceHandler.protocolDefineNames) {
                ProtocolConfig pc = beanFactory.getBean(name, ProtocolConfig.class);
                if (pc == null) {
                    continue;
                }
                if (FastRpcNamespaceHandler.protocolDefineNames.size() == 1) {
                    setProtocol(pc);
                } else if (pc.isDefault() != null && pc.isDefault().booleanValue()) {
                    setProtocol(pc);
                }
            }
        }
        if (CollectionUtil.isEmpty(getProtocols())) {
            setProtocol(FrameworkUtils.getDefaultProtocolConfig());
        }
    }
}
