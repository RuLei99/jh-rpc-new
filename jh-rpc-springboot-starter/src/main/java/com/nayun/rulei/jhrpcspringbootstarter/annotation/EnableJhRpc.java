package com.nayun.rulei.jhrpcspringbootstarter.annotation;

/**
 * 启用jh-Rpc注解
 *
 * @author Rulei
 */
import com.nayun.rulei.jhrpcspringbootstarter.bootstrap.RpcConsumerBootstrap;
import com.nayun.rulei.jhrpcspringbootstarter.bootstrap.RpcInitBootstrap;
import com.nayun.rulei.jhrpcspringbootstarter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableJhRpc {
    boolean startServer() default true;
}
