package com.cf.biz.service.sys.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cf.biz.service.sys.IRollBackService;
import com.cf.cfsecurity.domain.TbDict;
import com.cf.cfsecurity.domain.TbRole;
import com.cf.cfsecurity.domain.TbSysparam;
import com.cf.cfsecurity.domain.TbUser;
import com.cf.common.base.BaseSupport;
import com.cf.common.utils.security.Util;
import com.cfibatis.impl.batch.Batchmate;

/**
 * 审核(事务类)
 * 
 * @author hs
 * 
 */
@Service("rollBackService")
@Transactional(rollbackFor = Exception.class)
public class RollBackServiceImpl implements IRollBackService {

    private static final Logger logger = Logger.getLogger(RollBackServiceImpl.class);

    // 角色信息维护
    public int modRole(TbRole tbRole, Map<String, String> params, List list) throws Exception {
        try {
            BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbRoleTemp.CopyRole", tbRole);
            BaseSupport.myBatisSessionTemplate.update("orm.sys.tbRoleTemp.updateRoleTemp", params);
            ArrayList<Batchmate> batchmates = new ArrayList<Batchmate>();
            for (int k = 0; k < list.size(); k++) {
                HashMap<String, String> tmpmap = (HashMap<String, String>) list.get(k);
                ;
                tmpmap.put("CREATOR", BaseSupport.CframeUtil.GetCurrentUserName());
                tmpmap.put("CREATE_TIME", Util.getCurrentDateTimeString());
                tmpmap.put("RES_ID", tmpmap.get("id"));
                Batchmate bt = new Batchmate();
                bt.setOptType(Batchmate.TYPE.INSERT);
                bt.setStatement("orm.sys.tbRole.insertRoleTempMenu");
                bt.setParameter(tmpmap);
                batchmates.add(bt);
            }
            Batchmate[] batchmates1 = new Batchmate[batchmates.size()];
            for (int i = 0; i < batchmates.size(); i++) {
                batchmates1[i] = batchmates.get(i);
            }
            BaseSupport.myBatisSessionTemplate.batch(batchmates1);
        } catch (Exception e) {
            logger.error("modRole-->" + e);
            throw new Exception(e.getMessage());
        }
        return 1;
    }

    /*
     * 角色信息审核
     */
    public int addRole(TbRole tbRole, Map<String, String> params) throws Exception {
        try {
            tbRole.setCHECK_TIME(Util.getCurrentDateTimeString());
            tbRole.setCHECK_TLRNO(BaseSupport.CframeUtil.GetCurrentUserName());
            if (tbRole.getOP_FLAG().equals("增加")) {
                BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbRole.addRole", tbRole);
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbRole.delRoleResource", params);
                // 再插入
                List<HashMap<String, String>> list = BaseSupport.myBatisSessionTemplate.selectList("orm.sys.tbRole.selectRoleResourceCount", tbRole);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbRole.insertRoleMenu", it.next());
                }
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbRole.delRoleTempMenu", params);
            } else if (tbRole.getOP_FLAG().equals("修改")) {
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbRole.delRole", params);
                BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbRole.addRole", tbRole);
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbRole.delRoleResource", params);
                // 再插入
                List<HashMap<String, String>> list = BaseSupport.myBatisSessionTemplate.selectList("orm.sys.tbRole.selectRoleResourceCount", tbRole);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbRole.insertRoleMenu", it.next());
                }
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbRole.delRoleTempMenu", params);
            } else if (tbRole.getOP_FLAG().equals("删除")) {
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbRole.delRole", params);
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbRole.delRoleResource", params);
            }
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbRoleTemp.delRoletempByROLE_ID", params);
            BaseSupport.myBatisSessionTemplate.update("orm.sys.tbRole.updateRoleCheck", tbRole);
        } catch (Exception e) {
            logger.error("addRole-->" + e);
            throw new Exception(e.getMessage());
        }
        return 1;
    }

    /*
     * 角色信息审核(失败)
     */
    public void delRole(Map<String, String> params) throws Exception {
        try {
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbRoleTemp.delRoletempByROLE_ID", params);
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbRole.delRoleTempMenu", params);
        } catch (Exception e) {
            logger.error("delRole-->" + e);
            throw new Exception(e.getMessage());
        }
    }

    /*
     * 用户信息维护
     */
    public int modUser(TbUser tbUser, Map<String, String> params, List list) throws Exception {
        try {
            BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbUserTemp.addUserTemp", tbUser);
            BaseSupport.myBatisSessionTemplate.update("orm.sys.tbUserTemp.updateUserTemp", params);
            ArrayList<Batchmate> batchmates = new ArrayList<Batchmate>();
            for (int k = 0; k < list.size(); k++) {
                HashMap<String, String> tmpmap = (HashMap<String, String>) list.get(k);
                tmpmap.put("CREATOR", BaseSupport.CframeUtil.GetCurrentUserName());
                tmpmap.put("CREATE_TIME", Util.getCurrentDateTimeString());
                Batchmate bt = new Batchmate();
                bt.setOptType(Batchmate.TYPE.INSERT);
                if (tmpmap.get("checked").equals("true")) {
                    bt.setStatement("orm.sys.tbUser.insertUserRoleTemp");
                } else {
                    continue;
                }
                bt.setParameter(tmpmap);
                batchmates.add(bt);
            }
            Batchmate[] batchmates1 = new Batchmate[batchmates.size()];
            for (int i = 0; i < batchmates.size(); i++) {
                batchmates1[i] = batchmates.get(i);
            }
            BaseSupport.myBatisSessionTemplate.batch(batchmates1);
        } catch (Exception e) {
            logger.error("delRole-->" + e);
            throw new Exception(e.getMessage());
        }
        return 1;
    }

    /*
     * 用户信息审核
     */
    public int addUser(TbUser tbUser, Map<String, String> params) throws Exception {
        try {
            tbUser.setCHECK_TIME(Util.getCurrentDateTimeString());
            tbUser.setCHECKER(BaseSupport.CframeUtil.GetCurrentUserName());
            if (tbUser.getOP_FLAG().equals("增加")) {
                BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbUser.addUser", tbUser);
                // 先删除
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbUser.delUserRole", params);
                List<HashMap<String, String>> list = BaseSupport.myBatisSessionTemplate.selectList("orm.sys.tbUser.selectUserRoleCount", tbUser);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbUser.insertUserRole", it.next());
                }
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbUserTemp.delUserRoleTemp", params);
                BaseSupport.myBatisSessionTemplate.update("orm.sys.tbUser.updateUserCheck", tbUser);
            } else if (tbUser.getOP_FLAG().equals("修改")) {
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbUser.delUser", params);
                BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbUser.addUser", tbUser);
                // 先删除
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbUser.delUserRole", params);
                // 再插入
                List<HashMap<String, String>> list = BaseSupport.myBatisSessionTemplate.selectList("orm.sys.tbUser.selectUserRoleCount", tbUser);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbUser.insertUserRole", it.next());
                }
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbUserTemp.delUserRoleTemp", params);
                BaseSupport.myBatisSessionTemplate.update("orm.sys.tbUser.updateUserCheck", tbUser);
            } else if (tbUser.getOP_FLAG().equals("删除")) {
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbUser.delUser", params);
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbUser.delUserRole", params);
            }
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbUserTemp.delUsertempByUSER_ID", params);
        } catch (Exception e) {
            logger.error("delRole-->" + e);
            throw new Exception(e.getMessage());
        }
        return 1;
    }

    /*
     * 用户信息审核(失败)
     */
    public void delUser(Map<String, String> params) throws Exception {
        try {
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbUserTemp.delUsertempByUSER_ID", params);
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbUserTemp.delUserRoleTemp", params);
        } catch (Exception e) {
            logger.error("delRole-->" + e);
            throw new Exception(e.getMessage());
        }
    }

    // 系统参数维护
    public int modSysParam(TbSysparam tbSysparam, Map<String, String> params) throws Exception {
        try {
            BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbSysParamtemp.addParamTemp", tbSysparam);
            BaseSupport.myBatisSessionTemplate.update("orm.sys.tbSysParamtemp.updateParamTemp", params);
        } catch (Exception e) {
            logger.error("modSysParam-->" + e);
            throw new Exception(e.getMessage());
        }
        return 1;
    }

    /*
     * 系统参数审核(成功)
     */
    public int addSysParam(TbSysparam tbSysparam, Map<String, String> params) throws Exception {
        try {
            tbSysparam.setCHECK_TIME(Util.getCurrentDateTimeString());
            tbSysparam.setCHECK_TLRNO(BaseSupport.CframeUtil.GetCurrentUserName());
            if (tbSysparam.getOP_FLAG().equals("增加")) {
                BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbSysParam.addParam", tbSysparam);
            } else if (tbSysparam.getOP_FLAG().equals("修改")) {
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbSysParam.delParam", params);
                BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbSysParam.addParam", tbSysparam);
            } else if (tbSysparam.getOP_FLAG().equals("删除")) {
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbSysParam.delParam", params);
            }
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbSysParamtemp.delParamTemp", params);
        } catch (Exception e) {
            logger.error("addSysParam-->" + e);
            throw new Exception(e.getMessage());
        }
        return 1;
    }

    /*
     * 系统参数审核(失败)
     */
    public void delSysParam(Map<String, String> params) throws Exception {
        try {
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbSysParamtemp.delParamTemp", params);
        } catch (Exception e) {
            logger.error("delSysParam-->" + e);
            throw new Exception(e.getMessage());
        }
    }

    // 数据字典维护
    public int modDict(TbDict tbDict, Map<String, String> params) throws Exception {
        try {
            BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbDictTemp.addDictTemp", tbDict);
            BaseSupport.myBatisSessionTemplate.update("orm.sys.tbDictTemp.updateDictTemp", params);
        } catch (Exception e) {
            logger.error("modDict-->" + e);
            throw new Exception(e.getMessage());
        }
        return 1;
    }

    /*
     * 数据字典审核(成功)
     */
    public int addDict(TbDict tbDict, Map<String, String> params) throws Exception {
        try {
            tbDict.setCHECK_TIME(Util.getCurrentDateTimeString());
            tbDict.setCHECK_TLRNO(BaseSupport.CframeUtil.GetCurrentUserName());
            if (tbDict.getOP_FLAG().equals("增加")) {
                BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbDict.addDict", tbDict);
            } else if (tbDict.getOP_FLAG().equals("修改")) {
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbDict.delDict", params);
                BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbDict.addDict", tbDict);
            } else if (tbDict.getOP_FLAG().equals("删除")) {
                BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbDict.delDict", params);
            }
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbDictTemp.delDictTemp", params);
        } catch (Exception e) {
            logger.error("addDict-->" + e);
            throw new Exception(e.getMessage());
        }
        return 1;
    }

    /*
     * 数据字典审核(失败)
     */
    public void delDict(Map<String, String> params) throws Exception {
        try {
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbDictTemp.delDictTemp", params);
        } catch (Exception e) {
            logger.error("delDict-->" + e);
            throw new Exception(e.getMessage());
        }
    }

    /*
     * 新增角色（未审核）
     */
    public void addRole(Map<String, String> params) throws Exception {
        try {
            BaseSupport.myBatisSessionTemplate.delete("orm.sys.tbDictTemp.delDictTemp", params);
        } catch (Exception e) {
            logger.error("addRole-->" + e);
            throw new Exception(e.getMessage());
        }
    }

    // 合作机构信息维护
    public int modMerchant(Map<String, String> map, Map<String, String> params, List list) throws Exception {
        try {
            BaseSupport.myBatisSessionTemplate.insert("orm.zz.tbMerchantTempMapper.CopyMerchant", map);
            BaseSupport.myBatisSessionTemplate.update("orm.zz.tbMerchantTempMapper.updateMerchantTemp", params);
        } catch (Exception e) {
            logger.error("modMerchant-->" + e);
            throw new Exception(e.getMessage());
        }
        return 1;
    }
}
