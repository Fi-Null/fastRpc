package com.fast.rpc.config.springsupport;

import com.fast.rpc.common.URLParam;
import com.fast.rpc.config.ProtocolConfig;
import com.fast.rpc.config.ReferenceConfig;
import com.fast.rpc.config.RegistryConfig;
import com.fast.rpc.util.CollectionUtil;
import com.fast.rpc.util.FrameworkUtils;
import com.fast.rpc.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

/**
 * @ClassName ReferenceConfigBean
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:41
 * @Version 1.0
 **/
public class ReferenceConfigBean<T> extends ReferenceConfig<T> implements
        FactoryBean<T>, BeanFactoryAware,
        InitializingBean, DisposableBean {


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
    public T getObject() {
        return get();
    }

    @Override
    public Class<?> getObjectType() {
        return getInterfaceClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {

        logger.debug("check reference interface:%s config", getInterfaceName());
        //检查依赖的配置
        checkProtocolConfig();
        checkRegistryConfig();

        if (StringUtils.isEmpty(getGroup())) {
            setGroup(URLParam.group.getValue());
        }
        if (StringUtils.isEmpty(getVersion())) {
            setVersion(URLParam.version.getValue());
        }

        if (getTimeout() == null) {
            setTimeout(URLParam.requestTimeout.getIntValue());
        }
        if (getRetries() == null) {
            setRetries(URLParam.retries.getIntValue());
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
