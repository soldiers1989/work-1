package com.cf.biz.service.sys;

import java.util.List;
import java.util.Map;

import com.cf.cfsecurity.domain.TbDict;
import com.cf.cfsecurity.domain.TbRole;
import com.cf.cfsecurity.domain.TbSysparam;
import com.cf.cfsecurity.domain.TbUser;

/**
 * 操作数据库（事务回滚）
 */
public interface IRollBackService {
    /**
     * 角色信息维护
     * @param params,tbRole
     * @return
     */ 
    public int modRole(TbRole tbRole,Map<String, String> params,List list) throws Exception;
    /**
     * 角色信息维护审核（成功）
     * @param params,tbRole
     * @return
     */
    public int addRole(TbRole tbRole,Map<String, String> params) throws Exception;
    
    /**
     * 角色信息维护审核（失败）
     * @param params
     * @return
     */
    public void delRole(Map<String, String> params) throws Exception;
    
    /**
     * 用户信息维护
     * @param params,tbUser
     * @return
     */     
    public int modUser(TbUser tbUser, Map<String, String> params,List list) throws Exception;
    
    /**
     * 用户信息维护审核（成功）
     * @param params,tbUser
     * @return
     */
    public int addUser(TbUser tbUser, Map<String, String> params) throws Exception;
    
    /**
     * 用户信息维护审核（失败）
     * @param params
     * @return
     */
    public void delUser(Map<String, String> params) throws Exception;
    
    /**
     * 系统参数维护
     * @param params,tbSysparam
     * @return
     */     
    public int modSysParam(TbSysparam tbSysparam, Map<String, String> params) throws Exception ;
    
    /**
     * 系统参数审核(成功)
     * @param params,tbSysparam
     * @return
     */     
    public int addSysParam(TbSysparam tbSysparam, Map<String, String> params) throws Exception;
    /**
     * 系统参数审核（失败）
     * @param params
     * @return
     */
    public void delSysParam(Map<String, String> params) throws Exception;
    
    /**
     * 数据字典维护
     * @param params,tbDict
     * @return
     */     
    public int modDict(TbDict tbDict, Map<String, String> params) throws Exception ;
    
    
    /**
     * 数据字典审核(成功)
     * @param params,tbDict
     * @return
     */     
    public int addDict(TbDict tbDict, Map<String, String> params) throws Exception;
    /**
     * 数据字典审核（失败）
     * @param params
     * @return
     */
    public void delDict(Map<String, String> params) throws Exception;
    /**
     * 合作机构信息维护
     * @param params,tbRole
     * @return
     */ 
    public int modMerchant(Map<String, String> map,Map<String, String> params,List list) throws Exception;
}
