package com.rulei.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Rulei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {
    /**
     * 返回数据
     */
    private Object data;

    /**
     * 返回数据类型
     */
    private Class<?> type;

    /**
     * 消息
     */
    private String message;

    /**
     * 异常信息
     */
    private Exception exception;
}
