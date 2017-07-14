package com.zz.service.br;

import java.util.Map;

/**
 * 法院被执行人 ——— 个人接口类
 * @author Administrator
 *
 */
public interface ExecutionService {
    public int insertExecution(Map<String,String> parmes);
    public void deleteExecutionS(Map<String,String> parmes);
    public void insertExecutionS(Map<String,String> parmes);
}
