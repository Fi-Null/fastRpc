package com.fast.rpc.cluster.ha;

import com.fast.rpc.cluster.HaStrategy;
import com.fast.rpc.cluster.LoadBalance;
import com.fast.rpc.common.URL;
import com.fast.rpc.common.URLParam;
import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;
import com.fast.rpc.exception.RpcFrameworkException;
import com.fast.rpc.rpc.Reference;
import com.fast.rpc.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName FailoverHaStrategy
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 22:13
 * @Version 1.0
 **/
public class FailoverHaStrategy<T> implements HaStrategy<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response call(Request request, LoadBalance loadBalance) {
        Reference<T> reference = loadBalance.select(request);
        URL refUrl = reference.getUrl();
        int tryCount = refUrl.getIntParameter(URLParam.retries.getName(), URLParam.retries.getIntValue());
        if (tryCount < 0) {
            tryCount = 0;
        }

        for (int i = 0; i <= tryCount; i++) {
            reference = loadBalance.select(request);
            try {
                return reference.call(request);
            } catch (RuntimeException e) {
                // 对于业务异常，直接抛出
                if (ExceptionUtil.isBizException(e)) {
                    throw e;
                } else if (i >= tryCount) {
                    throw e;
                }
                logger.warn(String.format("FailoverHaStrategy Call false for request:%s error=%s", request, e.getMessage()));
            }
        }
        throw new RpcFrameworkException("FailoverHaStrategy.call should not come here!");
    }
}
