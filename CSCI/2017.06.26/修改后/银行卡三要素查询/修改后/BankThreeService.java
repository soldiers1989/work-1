package com.zz.service.br;

import java.util.Map;

/**
 * 银行卡三要素查询
 * 
 * @author Administrator
 *
 */
public interface BankThreeService {

    public int insert(Map<String,String> parmes);

    public void insertS(Map<String,String> parmes);

    public void updateS(Map<String,String> parmes);
}
