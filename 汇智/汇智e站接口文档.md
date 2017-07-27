---
layout: post
title:  "汇智e站接口文档"
date:   2017-7-6 16:58:58
categories: [other]
---

# 项目名称: 公共服务平台
- 创建时间: 2015-11-05
- 创建人: 钟兰
- 当前版本号: 0.0.0.731
- 项目简介: 公共服务平台
## 模块名称: 登陆、注册
### 页面名称: 登录日志
#### 接口名称:获取列表
- 接口地址:/Setting/LoginLog/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| startDate  	|	  开始日期  	|	  string  |
| userName  	|	  用户名  	|	  string  |
| endDate  	|	  结束日期  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
| page  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - browser  	|	    	|	  string  |
| - id  	|	    	|	  number  |
| - ipAddr  	|	    	|	  string  |
| - createTime  	|	    	|	  string  |
| - status  	|	  登录状态 true false  	|	  string  |
| - userName  	|	    	|	  string  |
### 页面名称: 首页
#### 接口名称:忘记密码（步骤一）
- 接口地址:/Setting/User/ForgetPasswordStepOne
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| code  	|	  验证码  	|	  string  |
| username  	|	  用户登录名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:发送短信验证码
- 接口地址:/Public/Phone/SendCode/{phone}
- 接口类型:GET
- 接口描述:{phone}：手机号码
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| phone  	|	  手机号码  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:验证短信验证码(废弃)
- 接口地址:/Public/Phone/Validate/{phone}/{code}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	  返回消息  	|	  string  |
| success  	|	  返回状态  	|	  boolean  |
#### 接口名称:保存注册信息
- 接口地址:/Login/register
- 接口类型:POST
- 接口描述:注册时，填写信息，发送短信验证，填写收到的验证码，验证短信验证码，验证码校验通过再提交注册！
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| username  	|	  用户登录名  	|	  string  |
| phone  	|	  电话号码  	|	  string  |
| password  	|	  用户登录密码  	|	  string  |
| code  	|	  验证码  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:用户登录
- 接口地址:/j_spring_security_check
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| j_password  	|	  用户登录密码(加密后的)  	|	  string  |
| lgtp  	|	  登录类型front back  	|	  string  |
| j_username  	|	  用户登录名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:忘记密码（步骤二）
- 接口地址:/Setting/User/ForgetPasswordStepTwo
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| username  	|	  用户登录名  	|	  string  |
| password  	|	  密码  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:通过用户名获取验证码
- 接口地址:/Setting/User/SendCode/{username}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:注销
- 接口地址:/j_spring_security_logout
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	    	|	  boolean  |
| msg  	|	    	|	  string  |
#### 接口名称:静态化接口
- 接口地址:/Setting/Index/Create
- 接口类型:POST
- 接口描述:首页模板静态化生成接口
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
### 页面名称: 微信登录
#### 接口名称:登录
- 接口地址:/Login/WeiXinLogin/{phone}/{authCode}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:获取验证码
- 接口地址:/Login/WeiXinGetCode/{phone}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
## 模块名称: 我的小智
### 页面名称: 下方列表
#### 接口名称:我的业务
- 接口地址:/workflow/api/processinstances/my_apply/list/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}:页码数
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	  总记录数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - current_state  	|	  当前状态  	|	  string  |
| - modifytime  	|	  更新时间  	|	  string  |
| - process_name  	|	  服务类别  	|	  string  |
| - title  	|	    	|	  string  |
| - identity_field_value  	|	    	|	  string  |
| - id  	|	  操作id  	|	  number  |
| - creator  	|	  创建人  	|	  string  |
| - addtime  	|	  创建时间  	|	  string  |
| - sn  	|	  流水号  	|	  string  |
| - related_table  	|	    	|	  string  |
| - can_retrieve  	|	  是否可撤销  	|	    |
| - is_canceled  	|	  是否取消  	|	  boolean  |
| - endtime  	|	  完成时间  	|	    |
| - starttime  	|	  申请时间  	|	  string  |
| pagesize  	|	  每页显示条数  	|	  number  |
| page  	|	  页码  	|	  number  |
#### 接口名称:我的业务（已完成)
- 接口地址:/workflow/api/processinstances/my_apply/done/{page}/{pagesize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	    	|	  number  |
| page  	|	  页码  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - identity_field_value  	|	    	|	  string  |
| - related_table  	|	    	|	  string  |
| - is_canceled  	|	  是否取消  	|	  boolean  |
| - starttime  	|	  申请时间  	|	  string  |
| - process_name  	|	  服务类别  	|	  string  |
| - id  	|	  操作id  	|	  number  |
| - endtime  	|	  完成时间  	|	  string  |
| - addtime  	|	  创建时间  	|	  string  |
| - title  	|	    	|	  string  |
| - creator  	|	  创建人  	|	  string  |
| - sn  	|	  流水号  	|	  string  |
| - current_state  	|	  当前状态  	|	  string  |
| - modifytime  	|	    	|	  string  |
| pagesize  	|	  每页显示条数  	|	  number  |
#### 接口名称:未评价
- 接口地址:/Setting/MainBusiness/noComment/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - createDate  	|	  创建时间  	|	  string  |
| - objectid  	|	  主键id  	|	  number  |
| - businessType  	|	  类型  	|	  string  |
| - completeDate  	|	  完成时间  	|	  string  |
| - phone  	|	  联系方式  	|	  string  |
| - businessId  	|	  业务表objectid  	|	  number  |
| - businessTypeZh  	|	  类型中文  	|	  string  |
| - username  	|	  用户登录名  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - chineseName  	|	  用户名  	|	  string  |
| - email  	|	  邮箱  	|	  string  |
| - commentFlag  	|	  评价标示  	|	  number  |
| - company  	|	  公司  	|	  string  |
| - serialNumber  	|	  流水号  	|	  string  |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
#### 接口名称:查看评价详情
- 接口地址:/Comment/Info
- 接口类型:GET
- 接口描述:查看详情
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| type  	|	  参见下方响应参数业务类型  	|	  string  |
| id  	|	  objectid  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| businessType  	|	  业务类型  	|	  number  |
| duration  	|	  工作周期－打分项  	|	  number  |
| createTime  	|	  评价时间  	|	  number  |
| username  	|	  用户id  	|	  string  |
| quality  	|	  工作质量－打分项  	|	  number  |
| attitude  	|	  工作态度－打分项  	|	  number  |
| content  	|	  评价内容  	|	  string  |
| serialNumber  	|	  评价所对应的主题流水号  	|	  string  |
## 模块名称: 帮助
### 页面名称: 下班
#### 接口名称:新增帮助
- 接口地址:/Setting/SettingHelps/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  内容  	|	  string  |
| flag  	|	  帮助中心标示（f-前台，b-后台）  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看帮助列表
- 接口地址:/Setting/SettingHelps/｛page｝/{pagesize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
| page  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - flag  	|	  帮助中心标示（f-前台，b-后台）  	|	  string  |
| - content  	|	  内容  	|	  string  |
| - updateDate  	|	    	|	  string  |
| - createDate  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
#### 接口名称:修改帮助
- 接口地址:/Setting/SettingHelps/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| flag  	|	  帮助中心标示（f-前台，b-后台）  	|	  string  |
| content  	|	  内容  	|	  string  |
| objectid  	|	  主键  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看帮助详情
- 接口地址:/Setting/SettingHelps/Edit?type={f,b}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - content  	|	  内容  	|	  string  |
| - updateDate  	|	    	|	  string  |
| - flag  	|	  帮助中心标示（f-前台，b-后台）  	|	  string  |
| - objectid  	|	    	|	  number  |
| - createDate  	|	    	|	  string  |
## 模块名称: 流程监控
### 页面名称: 列表
#### 接口名称:显示流转情况列表
- 接口地址:/workflow/api/taskinstances/history/{id}/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{id}:逾期接口中的 process_instance_id 值
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| meta  	|	    	|	  object  |
| - limit  	|	    	|	  number  |
| - total_count  	|	    	|	  number  |
| - offset  	|	    	|	  number  |
| - next  	|	    	|	    |
| - previous  	|	    	|	    |
| objects  	|	    	|	  array<object>  |
| - is_due  	|	  是否超期  	|	  boolean  |
| - duetime  	|	  期限  	|	    |
| - state  	|	  状态  	|	  string  |
| - addtime  	|	    	|	  string  |
| - task_user  	|	  操作人  	|	  string  |
| - display_state  	|	    	|	  string  |
| - opinion_name  	|	    	|	  string  |
| - task  	|	    	|	  string  |
| - modifytime  	|	    	|	  string  |
| - task_name  	|	  任务名称  	|	  string  |
| - id  	|	    	|	  number  |
| - content  	|	  审批意见  	|	  string  |
| - complete_date  	|	  完成时间  	|	  string  |
#### 接口名称:我的代办
- 接口地址:/workflow/api/taskinstanceslist/my_todo/list/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}：每页显示条数
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| sn__contains  	|	  流水号  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页显示条数  	|	  number  |
| total  	|	  总记录数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - complete_date  	|	    	|	    |
| - task_user  	|	    	|	  string  |
| - is_due  	|	    	|	  boolean  |
| - id  	|	    	|	  number  |
| - duetime  	|	  逾期时间  	|	    |
| - creator  	|	  申请人  	|	  string  |
| - modifytime  	|	    	|	  string  |
| - process_name  	|	  服务类别  	|	  string  |
| - display_state  	|	    	|	  string  |
| - task_name  	|	  任务节点  	|	  string  |
| - content  	|	    	|	  string  |
| - addtime  	|	  送达时间  	|	  string  |
| - state  	|	  状态  	|	  string  |
| - sn  	|	  流水号  	|	  string  |
| page  	|	  页码  	|	  number  |
#### 接口名称:错发回收列表
- 接口地址:/workflow/api/taskinstanceslist/Retrieve/｛page｝/{pageSize}
- 接口类型:GET
- 接口描述:字段相见  工作流模块 /workflow/api/taskinstanceslist/retrieve/list/?format=json&task_user={username}
接接口
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
| result1  	|	    	|	  array<object>  |
| - task_user  	|	    	|	  string  |
| - sn  	|	    	|	  string  |
| - content  	|	    	|	  string  |
| - display_state  	|	    	|	  string  |
| - modifytime  	|	    	|	  string  |
| - duetime  	|	    	|	  string  |
| - process_name  	|	    	|	  string  |
| - id  	|	    	|	  number  |
| - complete_date  	|	    	|	  string  |
| - state  	|	    	|	  string  |
| - is_due  	|	    	|	  boolean  |
| - creator  	|	    	|	  string  |
| - task_name  	|	    	|	  string  |
| - addtime  	|	    	|	  string  |
| page  	|	    	|	  number  |
#### 接口名称:错发回收
- 接口地址:/workflow/api/taskinstances/recovery/{id}
- 接口类型:DELETE
- 接口描述:错发回收列表上的id
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:我的经办
- 接口地址:/workflow/api/processinstances/my_done/list/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}:每页条数
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| sn__contains  	|	  流水号  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页条数  	|	  number  |
| total  	|	  总条数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - id  	|	    	|	  number  |
| - current_taskinstance_id  	|	  当前任务实例id  	|	    |
| - title  	|	  标题  	|	  string  |
| - modifytime  	|	    	|	  string  |
| - starttime  	|	  开始时间  	|	  string  |
| - sn  	|	  流水号  	|	  string  |
| - process_name  	|	  业务名称  	|	  string  |
| - addtime  	|	    	|	  string  |
| - creator  	|	  发起人  	|	  string  |
| - current_state  	|	  当前状态  	|	  string  |
| - related_table  	|	    	|	  string  |
| - endtime  	|	  结束时间  	|	    |
| - identity_field_value  	|	  业务表ID  	|	  string  |
| - is_canceled  	|	  是否取消  	|	  boolean  |
| page  	|	  页码  	|	  number  |
#### 接口名称:逾期
- 接口地址:/workflow/api/taskinstanceslist/due/list/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - task_name  	|	  任务名称  	|	  string  |
| - sn  	|	  流水号  	|	  string  |
| - complete_date  	|	    	|	    |
| - content  	|	    	|	  string  |
| - pre_task_instance  	|	    	|	  string  |
| - creator  	|	  申请人  	|	  string  |
| - duetime  	|	  逾期时间  	|	  string  |
| - id  	|	    	|	  number  |
| - process_instance_id  	|	  后一步需要传  	|	    |
| - addtime  	|	  送达时间  	|	  string  |
| - state  	|	  状态  	|	  string  |
| - modifytime  	|	    	|	  string  |
| - identity_field_value  	|	  业务id  	|	  string  |
| - is_due  	|	    	|	  boolean  |
| - process_name  	|	  服务类别  	|	  string  |
| - display_state  	|	  状态(中文)  	|	  string  |
| - task_user  	|	  受理人  	|	  string  |
| total  	|	    	|	  number  |
#### 接口名称:流程跟踪
- 接口地址:/workflow/api/processinstances/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| process__business__id  	|	  服务类别  	|	  string  |
| starttime__lte  	|	  申请时间小于等于  	|	  string  |
| starttime__gte  	|	  申请时间大于等于  	|	  string  |
| sn__contains  	|	  流水号  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objects  	|	    	|	  array<object>  |
| - related_table  	|	    	|	  string  |
| - starttime  	|	    	|	  string  |
| - title  	|	    	|	  string  |
| - endtime  	|	    	|	  string  |
| - addtime  	|	    	|	  string  |
| - sn  	|	    	|	  string  |
| - modifytime  	|	    	|	  string  |
| - id  	|	    	|	  string  |
| - current_state  	|	    	|	  string  |
| - identity_field_value  	|	    	|	  string  |
| - is_canceled  	|	    	|	  boolean  |
| - current_taskinstance_id  	|	  当前任务实例id  	|	  string  |
| - creator  	|	    	|	  string  |
| - process_name  	|	    	|	  string  |
#### 接口名称:我的代办-副本123123
- 接口地址:/workflow/api/taskinstanceslist/my_todo/list/
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}：每页显示条数
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  页码  	|	  number  |
| total  	|	  总记录数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - modifytime  	|	    	|	  string  |
| - state  	|	  状态  	|	  string  |
| - sn  	|	  流水号  	|	  string  |
| - process_name  	|	  服务类别  	|	  string  |
| - display_state  	|	    	|	  string  |
| - id  	|	    	|	  number  |
| - is_due  	|	    	|	  boolean  |
| - duetime  	|	  逾期时间  	|	    |
| - task_name  	|	  任务节点  	|	  string  |
| - complete_date  	|	    	|	    |
| - creator  	|	  申请人  	|	  string  |
| - task_user  	|	    	|	  string  |
| - content  	|	    	|	  string  |
| - addtime  	|	  送达时间  	|	  string  |
| pagesize  	|	  每页显示条数  	|	  number  |
#### 接口名称:我的业务（已完成）
- 接口地址:/workflow/api/processinstances/my_apply/done/{page}/{pagesize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - title  	|	    	|	  string  |
| - current_state  	|	  当前状态  	|	  string  |
| - modifytime  	|	    	|	  string  |
| - addtime  	|	    	|	  string  |
| - is_canceled  	|	  是否取消  	|	  boolean  |
| - endtime  	|	  完成时间  	|	  string  |
| - id  	|	  操作id  	|	  number  |
| - sn  	|	  流水号  	|	  string  |
| - process_name  	|	  服务类别  	|	  string  |
| - can_retrieve  	|	  是否可撤销  	|	  boolean  |
| - creator  	|	  创建人  	|	  string  |
| - identity_field_value  	|	    	|	  string  |
| - starttime  	|	  申请时间  	|	  string  |
| - related_table  	|	    	|	  string  |
| total  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
## 模块名称: 新后台管理
### 页面名称: 须知管理
#### 接口名称:根据type查看须知详情
- 接口地址:/Setting/Readme/Type/Edit?type={type}
- 接口类型:GET
- 接口描述:{type}值

enterpristCultivate-企业培训，userHeadhunting-猎聘，userTestApplyfor-测试，userCommercialize-广告服务，userCopyright-著作权，userIncubator-孵化注册申请，enterApply-入驻申请
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  须知内容  	|	  string  |
| deleteflag  	|	  禁用启用（-1：禁用，1-启用）  	|	  number  |
| isPopup  	|	  是否弹窗(1-是，-1-否)  	|	  number  |
| type  	|	  须知类别  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| name  	|	  须知标题  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
#### 接口名称:查看须知详情
- 接口地址:/Setting/Readme/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| isPopup  	|	  是否弹窗(1-是，-1-否)  	|	  number  |
| name  	|	  须知标题  	|	  string  |
| type  	|	  须知类别  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| content  	|	  须知内容  	|	  string  |
| deleteflag  	|	  禁用启用（-1：禁用，1-启用）  	|	  number  |
#### 接口名称:须知列表
- 接口地址:/Setting/Readme/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	  总条数  	|	  number  |
| page  	|	  页码  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - name  	|	  须知标题  	|	  string  |
| - content  	|	  内容  	|	  string  |
| - isPopup  	|	  是否弹窗(1-是，-1-否)  	|	  number  |
| - objectid  	|	  主键id  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - type  	|	  须知类别  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| - deleteflag  	|	  禁用启用（-1：禁用，1-启用）  	|	  number  |
#### 接口名称:更新须知
- 接口地址:/Setting/Readme/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| isPopup  	|	  是否弹窗(1-是，-1-否)  	|	  number  |
| name  	|	  须知标题  	|	  string  |
| deleteflag  	|	  禁用启用（-1：禁用，1-启用）  	|	  number  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| content  	|	  须知内容  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| type  	|	  须知类别（不可更改，保持原值）  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 会议室
#### 接口名称:会议室列表
- 接口地址:/Mettingroom/Mettingroom/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页条数  	|	  number  |
| total  	|	  总条数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| - vsx  	|	  是否含有视频会议（-1-不含，1-含）  	|	  number  |
| - projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| - name  	|	  会议室名称  	|	  string  |
| - pic  	|	  封面图片  	|	  string  |
| - peoples  	|	  可容纳人数  	|	  string  |
| - price  	|	  参考价格  	|	  string  |
| - location  	|	  会议室位置  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| - objectid  	|	  主键id  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - content  	|	  内容  	|	  string  |
| - deleteFlag  	|	  禁用启用（-1-禁用,1-启用）  	|	  number  |
| - readme  	|	  须知  	|	  string  |
| page  	|	  当前页码  	|	  number  |
#### 接口名称:添加会议室
- 接口地址:/Mettingroom/Mettingroom/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  内容  	|	  string  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| price  	|	  参考价格  	|	  string  |
| peoples  	|	  可容纳人数  	|	  string  |
| vsx  	|	  是否含有视频会议（-1-不含，1-含）  	|	  number  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| location  	|	  会议室位置  	|	  string  |
| pic  	|	  封面图片  	|	  string  |
| readme  	|	  须知  	|	  string  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| name  	|	  会议室名称  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:修改会议室
- 接口地址:/Mettingroom/Mettingroom/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  内容  	|	  string  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| peoples  	|	  可容纳人数  	|	  string  |
| vsx  	|	  是否含有视频会议（-1-不含，1-含）  	|	  number  |
| price  	|	  参考价格  	|	  string  |
| objectid  	|	  主键id  	|	  string  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| readme  	|	  须知  	|	  string  |
| location  	|	  会议室位置  	|	  string  |
| pic  	|	  封面图片  	|	  string  |
| name  	|	  会议室名称  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:会议室详情
- 接口地址:/Mettingroom/Mettingroom/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| deleteFlag  	|	  禁用启用（-1-禁用,1-启用）  	|	  number  |
| createDate  	|	  创建时间  	|	  string  |
| price  	|	  参考价格  	|	  string  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| peoples  	|	  可容纳人数  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| readme  	|	  须知  	|	  string  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| name  	|	  会议室名称  	|	  string  |
| content  	|	  内容  	|	  string  |
| pic  	|	  封面图片  	|	  string  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| vsx  	|	  是否含有视频会议（-1-不含，1-含）  	|	  number  |
| location  	|	  会议室位置  	|	  string  |
#### 接口名称:禁用启用会议室
- 接口地址:/Mettingroom/Mettingroom/Enable/Delete/{objectid}/{flag}
- 接口类型:GET
- 接口描述:{objectid}:会议室objectid
{flag} ：-1-禁用，1-启用
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:会议室预定
- 接口地址:/Mettingroom/UserMeetingroom/Admin/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| chineseName  	|	  真实名称（user表realName）  	|	  string  |
| endTimeArray  	|	  结束时间17：00  	|	  array<number>  |
| company  	|	  公司名  	|	  string  |
| dateTimeArray  	|	  日期  	|	  array<string>  |
| peoples  	|	  参会人数  	|	  string  |
| topics  	|	  会议主题  	|	  string  |
| email  	|	  Email  	|	  string  |
| memo  	|	  备注  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| meetingroomId  	|	  会议室objectid  	|	  number  |
| teaReakMoney  	|	  茶歇人均标准  	|	  number  |
| teaReak  	|	  是否需要茶歇（1-需要，-1-不需要）  	|	  number  |
| vsx  	|	  是否含有视频会议（-1-不含，1-含）  	|	  number  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| beginTimeArray  	|	  开始时间 15:00  	|	  array<string>  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:会议室日程
- 接口地址:/Mettingroom/UserMeetingroomVerify/Itinerary/Find
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| beginDate  	|	    	|	    |
| endDate  	|	    	|	    |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| data  	|	  数据  	|	  object  |
| - begin_time  	|	  开始时间  	|	  string  |
| - end_time  	|	  结束时间  	|	    |
| - apply_date  	|	  申请日期  	|	  string  |
| - name  	|	  申请人  	|	  string  |
| mettingroomName  	|	  会议声明称  	|	  string  |
### 页面名称: 在线看房
#### 接口名称:新增房源
- 接口地址:/House/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| name  	|	  房源名称  	|	  string  |
| pic  	|	  房源图片  	|	  string  |
| content  	|	  简介  	|	  string  |
| address  	|	  房源地址  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:房源详情
- 接口地址:/House/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  内容  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| objectid  	|	  主键ID  	|	  number  |
| pic  	|	  图片  	|	  string  |
| name  	|	  名称  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| address  	|	  地址  	|	  string  |
| deleteFlag  	|	  否禁用（-1-禁用，1-启用）  	|	  number  |
#### 接口名称:在线房源列表
- 接口地址:/House/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	  总条数  	|	  number  |
| pagesize  	|	  每页条数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - content  	|	  内容  	|	  string  |
| - name  	|	  名称  	|	  string  |
| - objectid  	|	  主键ID  	|	  number  |
| - createDate  	|	  创建时间  	|	  string  |
| - deleteFlag  	|	  是否禁用（-1-禁用，1-启用）  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - address  	|	  地址  	|	  string  |
| - pic  	|	  图片地址  	|	  string  |
| page  	|	  页码  	|	  number  |
#### 接口名称:编辑房源
- 接口地址:/House/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	  主键id  	|	  string  |
| pic  	|	  房源图片  	|	  string  |
| name  	|	  房源名称  	|	  string  |
| content  	|	  简介  	|	  string  |
| address  	|	  房源地址  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 职位管理
#### 接口名称:修改职位
- 接口地址:/Jobs/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| name  	|	  职位名称  	|	  string  |
| objectid  	|	    	|	  string  |
| content  	|	  内容  	|	  string  |
| moneyTypeId  	|	  薪资类别id（数据字典读取type=moneyType）  	|	  string  |
| money  	|	  薪资  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:职位列表
- 接口地址:/Jobs/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - name  	|	  职位名称  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - moneyTypeId  	|	  薪资类别ID  	|	  number  |
| - money  	|	  薪资  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - moneyType  	|	  薪资类别  	|	  object  |
| - - settingDict  	|	    	|	    |
| - - objectid  	|	  数据字典ID  	|	  number  |
| - - name  	|	  数据字典名  	|	  string  |
| - - orderFlag  	|	  排序  	|	  number  |
| - - parentid  	|	  父级ID  	|	  number  |
| - - type  	|	  数据字典类型  	|	  string  |
| - deleteFlag  	|	  禁用启用（-1：禁用，1-启用）  	|	  number  |
| - objectid  	|	  主键id  	|	  number  |
| - content  	|	  职位描述  	|	  string  |
| total  	|	  总条数  	|	  number  |
| page  	|	  当前页  	|	  number  |
| pagesize  	|	  每页条数  	|	  number  |
#### 接口名称:添加职位
- 接口地址:/Jobs/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| name  	|	  职位名称  	|	  string  |
| content  	|	  内容  	|	  string  |
| money  	|	  薪资  	|	  string  |
| moneyTypeId  	|	  薪资类别id（数据字典读取type=moneyType）  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:发布禁用职位
- 接口地址:/Jobs/Enable/Delete/{objectid}/{flag}
- 接口类型:GET
- 接口描述:{objectid}:职位objectid
{flag} ：-1-禁用，1-启用
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:应聘情况
- 接口地址:/Jobs/UserJobs/Apply/List/{jobsId}/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page} : 页码
{pageSize}：每页显示条数
{jobsId} ： 职位objectid
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - company  	|	  公司  	|	  string  |
| - jobs  	|	  职位  	|	  object  |
| - - objectid  	|	  主键id  	|	  number  |
| - - updateDate  	|	  更新时间  	|	  string  |
| - - moneyType  	|	  薪资类别  	|	  object  |
| - - - type  	|	  数据字典类型  	|	  string  |
| - - - orderFlag  	|	  排序  	|	  number  |
| - - - settingDict  	|	    	|	    |
| - - - english  	|	  英文  	|	    |
| - - - name  	|	  薪资类别名称  	|	  string  |
| - - - objectid  	|	  主键id  	|	  number  |
| - - - parentid  	|	  父级id  	|	  number  |
| - - content  	|	  内容  	|	  string  |
| - - deleteFlag  	|	  禁用启用（-1：禁用，1-启用）  	|	  number  |
| - - money  	|	  薪资  	|	  string  |
| - - createDate  	|	  创建时间  	|	  string  |
| - - name  	|	  职位名称  	|	  string  |
| - - moneyTypeId  	|	  薪资类别id  	|	  number  |
| - createDate  	|	  创建时间  	|	  string  |
| - downloadNum  	|	  简历下载次数  	|	  number  |
| - phone  	|	  联系方式  	|	  string  |
| - memo  	|	  备注  	|	  string  |
| - username  	|	  用户名  	|	  string  |
| - chineseName  	|	  用户真实名  	|	  string  |
| - jobsid  	|	  职位id  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - email  	|	  Email  	|	  string  |
| - url  	|	  简历url  	|	  string  |
| - objectid  	|	  主键id  	|	  number  |
| page  	|	  当前页  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
| total  	|	    	|	  number  |
#### 接口名称:职位详情
- 接口地址:/Jobs/Edit/{id}
- 接口类型:GET
- 接口描述:响应参数字段含义参见职位列表
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| createDate  	|	  创建时间  	|	  string  |
| money  	|	  薪资  	|	  string  |
| content  	|	  职位描述  	|	  string  |
| moneyType  	|	  薪资类别  	|	  object  |
| - objectid  	|	  数据字典ID  	|	  number  |
| - name  	|	  数据字典名  	|	  string  |
| - type  	|	  数据字典类型  	|	  string  |
| - parentid  	|	  父级ID  	|	  number  |
| - settingDict  	|	    	|	    |
| - orderFlag  	|	  排序  	|	  number  |
| name  	|	  职位名称  	|	  string  |
| deleteFlag  	|	  禁用启用（-1：禁用，1-启用）  	|	  number  |
| objectid  	|	  主键id  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| moneyTypeId  	|	  薪资类别ID  	|	  number  |
#### 接口名称:导出
- 接口地址:/Jobs/UserJobs/DownloadResume?id={objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 孵化
#### 接口名称:导出
- 接口地址:/Incubator/UserIncubator/Export/List
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| beginDate  	|	  开始时间  	|	  string  |
| endDate  	|	  结束时间  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:孵化预约详情
- 接口地址:/Incubator/UserIncubator/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - chineseName  	|	  用户真实名称  	|	  string  |
| - appointmentDate  	|	  预约时间  	|	  number  |
| - objectid  	|	  主键ID  	|	  number  |
| - company  	|	  公司名称  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - phone  	|	  联系方式  	|	  string  |
| - memo  	|	  备注  	|	  string  |
| - username  	|	  用户登录名  	|	  string  |
| - rentType  	|	  租赁类别（1-新租/2-续租）  	|	  number  |
| - years  	|	  年限  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - email  	|	  邮箱  	|	  string  |
#### 接口名称:孵化预约列表
- 接口地址:/Incubator/UserIncubator/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  页码  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - rentType  	|	  租赁类别（1-新租/2-续租）  	|	  number  |
| - username  	|	  用户登录名  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - chineseName  	|	  用户真实名称  	|	  string  |
| - objectid  	|	  主键ID  	|	  number  |
| - memo  	|	  备注  	|	  string  |
| - company  	|	  公司名称  	|	  string  |
| - appointmentDate  	|	  预约时间  	|	  number  |
| - phone  	|	  联系方式  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - email  	|	  邮箱  	|	  string  |
| - years  	|	  年限  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
| total  	|	  总条数  	|	  number  |
#### 接口名称:孵化预约列表(搜索)
- 接口地址:/Incubator/UserIncubator/Search/List/{page}/{pageSize}
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| endDate  	|	  结束时间  	|	  string  |
| beginDate  	|	  开始日期  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页显示条数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - objectid  	|	  主键ID  	|	  number  |
| - createDate  	|	  创建时间  	|	  string  |
| - username  	|	  用户登录名  	|	  string  |
| - chineseName  	|	  用户真实名称  	|	  string  |
| - email  	|	  邮箱  	|	  string  |
| - memo  	|	  备注  	|	  string  |
| - rentType  	|	  租赁类别（1-新租/2-续租）  	|	  number  |
| - years  	|	  年限  	|	  number  |
| - appointmentDate  	|	  预约时间  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - company  	|	  公司名称  	|	  string  |
| - phone  	|	  联系方式  	|	  string  |
| total  	|	  总条数  	|	  number  |
| page  	|	  页码  	|	  number  |
#### 接口名称:删除孵化预约
- 接口地址:/Incubator/UserIncubator/Delete/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 账单查询日志
#### 接口名称:搜索列表
- 接口地址:/Setting/ClmSearchLog/Search/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:queryString方式传值
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| endDate  	|	  结束时间（格式：2016-04-15）  	|	  string  |
| beginDate  	|	  开始时间（格式：2016-04-12）  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  页码  	|	  number  |
| total  	|	    	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - username  	|	  用户名  	|	  string  |
| - createDate  	|	  查询时间  	|	  string  |
| - name  	|	  企业名称  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - objectid  	|	  主键id  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
#### 接口名称:列表
- 接口地址:/Setting/ClmSearchLog/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - name  	|	  企业名称  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - objectid  	|	  主键id  	|	  number  |
| - createDate  	|	  创建时间  	|	  string  |
| - username  	|	  用户名  	|	  string  |
| pagesize  	|	  每页条数  	|	  number  |
| page  	|	  页码  	|	  number  |
| total  	|	    	|	  number  |
### 页面名称: 自动评价配置
#### 接口名称:更改自动评价
- 接口地址:/Setting/SettingAutoComment/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  自动评价内容  	|	  string  |
| autoCommentDeadline  	|	  自动评价期限 单位:分钟  	|	  string  |
| objectid  	|	  主键id  	|	  string  |
| type  	|	  类别（广告-commercialize、教室-classroom、版权-copyright、会议室-  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看自动评价
- 接口地址:/Setting/SettingAutoComment/Edit?type={type}
- 接口类型:GET
- 接口描述:{type}:广告-commercialize、教室-classroom、版权-copyright、会议室-meetingroom、企业-enterpristCultivate、猎聘-headhunting、入驻-enterApply，测试-testApplyfor
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| createDate  	|	  创建时间  	|	  string  |
| autoCommentDeadline  	|	  自动评价期限 单位:分钟  	|	  number  |
| content  	|	  自动评价内容  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| type  	|	  类别（广告-commercialize、教室-classroom、版权-copyright、会议室-  	|	  string  |
### 页面名称: 教室管理
#### 接口名称:修改教室
- 接口地址:/Classroom/Classroom/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| computer  	|	  是否含有电脑（-1-不含，1-含）  	|	  number  |
| content  	|	  内容  	|	  string  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| area  	|	  面积  	|	  string  |
| pic  	|	  封面图片  	|	  string  |
| location  	|	  教室位置  	|	  string  |
| peoples  	|	  可容纳人数  	|	  string  |
| readme  	|	  须知  	|	  string  |
| price  	|	  参考价格  	|	  string  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| name  	|	  教室名称  	|	  string  |
| objectid  	|	  主键id  	|	  string  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:预定教室
- 接口地址:/Classroom/UserClassroom/Admin/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| halfDayArray  	|	  上午下午数组（a-上午，p-下午）  	|	  array<string>  |
| company  	|	  公司  	|	  string  |
| content  	|	  需求说明  	|	  string  |
| dateTimeArray  	|	  日期数组(时间格式2016/03/29)  	|	  array<string>  |
| classroomId  	|	  教室objectid  	|	  string  |
| email  	|	  Email  	|	  string  |
| chineseName  	|	  姓名(User表realName字段)  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| memo  	|	  备注  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:禁用启用教室
- 接口地址:/Classroom/Classroom/Enable/Delete/{objectid}/{flag}
- 接口类型:GET
- 接口描述:{objectid}:j教室objectid
{flag} ：-1-禁用，1-启用
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:教室详情
- 接口地址:/Classroom/Classroom/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| peoples  	|	  可容纳人数  	|	  string  |
| objectid  	|	  主键ID  	|	  number  |
| price  	|	  参考价格  	|	  string  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| readme  	|	  须知  	|	  string  |
| area  	|	  面积  	|	  string  |
| name  	|	  教室名称  	|	  string  |
| pic  	|	  封面图片  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| computer  	|	  是否含有电脑（-1-不含，1-含）  	|	  number  |
| location  	|	  地址  	|	  string  |
| content  	|	  内容  	|	  string  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| deleteFlag  	|	  禁用启用（-1-禁用,1-启用）  	|	  number  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
#### 接口名称:教室日程
- 接口地址:/Classroom/UserClassroomVerify/Itinerary/Find
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| endDate  	|	    	|	  string  |
| beginDate  	|	    	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:添加教室
- 接口地址:/Classroom/Classroom/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| area  	|	  面积  	|	  string  |
| computer  	|	  是否含有电脑（-1-不含，1-含）  	|	  number  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| name  	|	  教室名称  	|	  string  |
| pic  	|	  封面图片  	|	  string  |
| peoples  	|	  可容纳人数  	|	  string  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| location  	|	  教室位置  	|	  string  |
| readme  	|	  须知  	|	  string  |
| price  	|	  参考价格  	|	  string  |
| content  	|	  内容  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:教室列表
- 接口地址:/Classroom/Classroom/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  当前页  	|	  string  |
| result  	|	  结果集  	|	  array<object>  |
| - name  	|	  教室名称  	|	  string  |
| - location  	|	  教室位置  	|	  string  |
| - pic  	|	  封面图片  	|	  string  |
| - area  	|	  面积  	|	  string  |
| - computer  	|	  是否含有电脑（-1-不含，1-含）  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - peoples  	|	  可容纳人数  	|	  string  |
| - price  	|	  参考价格  	|	  string  |
| - projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - objectid  	|	  主键id  	|	  string  |
| - content  	|	  内容  	|	  string  |
| - isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| - readme  	|	  须知  	|	  string  |
| - microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  string  |
| - deleteFlag  	|	  禁用启用（-1-禁用,1-启用）  	|	  string  |
| pagesize  	|	  每页条数  	|	  string  |
| total  	|	  总条数  	|	  string  |
### 页面名称: 课程管理
#### 接口名称:导出
- 接口地址:/Cultivate/UserCultivate/Export/List/{courseIds}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| phone  	|	  联系方式  	|	  string  |
| chineseName  	|	  报名人姓名  	|	  string  |
| courseName  	|	  课程名称  	|	  string  |
| applyTime  	|	  报名时间  	|	    |
| email  	|	  E-mail  	|	  string  |
| schoolTime  	|	  上课时间  	|	  string  |
| censusRegister  	|	  是否沪籍  	|	  string  |
| socialInsurance  	|	  是否缴纳社保  	|	  string  |
#### 接口名称:更新课程
- 接口地址:/Cultivate/Course/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| price  	|	  课程价格  	|	  number  |
| content  	|	  课程简介  	|	  string  |
| courseOutline  	|	  课程大纲  	|	  string  |
| objectid  	|	  主键ID  	|	  string  |
| name  	|	  课程名称  	|	  string  |
| deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| pic  	|	  课程封面图片  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:课程报名情况
- 接口地址:/Cultivate/UserCultivate/Apply/List/{couseId}/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}：每页显示条数
{couseId}：课程表objectid
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页显示条数  	|	  number  |
| total  	|	    	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - phone  	|	  联系方式  	|	  string  |
| - objectid  	|	  主键ID  	|	  number  |
| - email  	|	  邮箱  	|	  string  |
| - company  	|	  公司  	|	  string  |
| - memo  	|	  备注  	|	    |
| - gradeId  	|	  等级ID  	|	  number  |
| - grade  	|	  等级  	|	  object  |
| - - objectid  	|	  等级objectid  	|	  number  |
| - - orderFlag  	|	  排序  	|	  number  |
| - - name  	|	  等级名称  	|	  string  |
| - - type  	|	  数据字典类型  	|	  string  |
| - - parentid  	|	  父级id  	|	  number  |
| - username  	|	  用户名  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - socialInsurance  	|	  是否在上海缴纳社保（1-是，-1否）  	|	  number  |
| - course  	|	  课程信息  	|	  object  |
| - - updateDate  	|	    	|	  string  |
| - - name  	|	  课程名称  	|	  string  |
| - - pic  	|	  封面图片  	|	  string  |
| - - content  	|	  课程简介  	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - price  	|	  价格  	|	  number  |
| - - createDate  	|	    	|	  string  |
| - - courseOutline  	|	  课程大纲  	|	  string  |
| - - deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| - education  	|	  学历  	|	  number  |
| - jobId  	|	  目前职位id  	|	  number  |
| - job  	|	  目前职位  	|	  object  |
| - - type  	|	  数据字典类型  	|	  string  |
| - - orderFlag  	|	  排序  	|	  number  |
| - - name  	|	  职位名称  	|	  string  |
| - - objectid  	|	  职位objectid  	|	  number  |
| - - parentid  	|	  父级id  	|	  number  |
| - workYear  	|	  工作年限  	|	  object  |
| - - parentid  	|	  父级ID  	|	  number  |
| - - name  	|	  工作年限名称  	|	  string  |
| - - objectid  	|	  工作年限ID  	|	  number  |
| - - type  	|	  数据字典类型  	|	  string  |
| - - orderFlag  	|	  排序  	|	  number  |
| - schoolTime  	|	  上课时间（1-7，逗号隔开，代表一周）  	|	  string  |
| - workYearId  	|	  工作年限id  	|	  number  |
| - censusRegister  	|	  是否沪级（1-是，-1-否）  	|	  number  |
| - courseId  	|	  课程表id  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - sex  	|	  性别  	|	  number  |
| page  	|	  当前页  	|	  number  |
#### 接口名称:查看课程详情
- 接口地址:/Cultivate/Course/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pic  	|	  封面图片  	|	  string  |
| deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| updateDate  	|	    	|	  string  |
| content  	|	  课程简介  	|	  string  |
| price  	|	  价格  	|	  number  |
| name  	|	  课程名称  	|	  string  |
| objectid  	|	    	|	  number  |
| createDate  	|	  创建时间  	|	  string  |
| sponsor  	|	  主办方  	|	  string  |
| courseOutline  	|	  课程大纲  	|	  string  |
#### 接口名称:新增课程
- 接口地址:/Cultivate/Course/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| price  	|	  课程价格  	|	  number  |
| pic  	|	  课程封面图片  	|	  string  |
| content  	|	  课程简介  	|	  string  |
| courseOutline  	|	  课程大纲  	|	  string  |
| sponsor  	|	  主办方  	|	  string  |
| name  	|	  课程名称  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:禁用启用课程
- 接口地址:/Cultivate/Course/Enable/Delete/{objectid}/{flag}
- 接口类型:GET
- 接口描述:{objectid}:课程objectid
{flag} ：-1-禁用，1-启用
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:所有课程列表
- 接口地址:/Cultivate/Course/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  页码  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
| total  	|	    	|	  number  |
| result  	|	  返回结果集  	|	  array<object>  |
| - name  	|	  课程名称  	|	  string  |
| - updateDate  	|	    	|	  string  |
| - deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| - price  	|	  价格  	|	  number  |
| - content  	|	  课程简介  	|	  string  |
| - createDate  	|	    	|	  string  |
| - pic  	|	  封面图片  	|	  string  |
| - objectid  	|	  主键ID  	|	  number  |
| - courseOutline  	|	  课程大纲  	|	  string  |
| - sponsor  	|	  主办方  	|	  string  |
### 页面名称: 素材/附件管理
#### 接口名称:上传素材（单个）
- 接口地址:/FileUpload/AccessoryNanageMaterialUploadDocFile
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| file  	|	  文件  	|	  object  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	  状态  	|	  boolean  |
| msg  	|	  消息  	|	  string  |
#### 接口名称:素材/附件列表
- 接口地址:/Setting/AccessoryManage/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:?type={type}
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页条数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - updateDate  	|	  更新时间  	|	  string  |
| - url  	|	  素材/附件地址  	|	  string  |
| - objectid  	|	  主键id  	|	  number  |
| - name  	|	  文件名称  	|	  string  |
| - author  	|	  上传者  	|	  string  |
| - type  	|	  类型（附件-accessory，素材-material）  	|	  string  |
| - createDate  	|	  上传时间  	|	  string  |
| - deleteFlag  	|	  禁用（-1）启用（1）  	|	  number  |
| page  	|	  当前页  	|	  number  |
| total  	|	  总记录数  	|	  number  |
#### 接口名称:上传附件（单个）
- 接口地址:/FileUpload/AccessoryNanageUploadDocFile
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| file  	|	  文件  	|	  object  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	  状态  	|	  boolean  |
| msg  	|	  消息  	|	  string  |
#### 接口名称:素材/附件详情
- 接口地址:/Setting/AccessoryManage/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - type  	|	  类型（附件-accessory，素材-material）  	|	  string  |
| - createDate  	|	  上传时间  	|	  string  |
| - url  	|	  素材/附件地址  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - author  	|	  上传者  	|	  string  |
| - name  	|	  文件名称  	|	  string  |
| - deleteFlag  	|	  禁用（-1）启用（1）  	|	  number  |
| - objectid  	|	  主键id  	|	  number  |
#### 接口名称:更新素材/附件
- 接口地址:/Setting/AccessoryManage/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| type  	|	  类型（附件-accessory，素材-material）  	|	  string  |
| author  	|	  上传者  	|	  string  |
| objectid  	|	  主键  	|	  string  |
| url  	|	  地址  	|	  string  |
| deleteFlag  	|	  禁用（-1）启用（1）  	|	  string  |
| name  	|	  文件名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:删除素材/附件
- 接口地址:/Setting/AccessoryManage/Delete/{objectid}
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
## 模块名称: 工作流定义模块
### 页面名称: 工作流过程定义
#### 接口名称:新增
- 接口地址:/workflow/api/process/
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| printview  	|	  打印页面地址or模板  	|	  string  |
| editview  	|	  编辑页面地址or模板  	|	  string  |
| createview  	|	  新增页面地址or模板  	|	  string  |
| alias  	|	  过程定义名称  	|	  string  |
| related_table  	|	  与当前过程定义相关联的业务数据表名称  	|	  string  |
| name  	|	  过程定义名称  	|	  string  |
| sn_template  	|	  编号模板  	|	  string  |
| identityfield  	|	  业务数据表的标识字段名称  	|	  string  |
| version  	|	  版本号  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 工作流业务类型定义
#### 接口名称:列表
- 接口地址:/workflow/api/business
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objects0  	|	    	|	  array<object>  |
| - addtime  	|	    	|	  string  |
| - name  	|	  名称  	|	  string  |
| - resource_uri  	|	    	|	  string  |
| - id  	|	    	|	  number  |
| - modifytime  	|	    	|	  string  |
| meta  	|	    	|	  object  |
| - previous  	|	    	|	    |
| - next  	|	    	|	    |
| - offset  	|	    	|	  number  |
| - total_count  	|	    	|	  number  |
| - limit  	|	    	|	  number  |
## 模块名称: 问卷系统
### 页面名称: 金数据
#### 接口名称:刷新token接口
- 接口地址:/questionnaire/refresh
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:授权接口
- 接口地址:https://account.jinshuju.net/oauth/authorize?client_id=6dc08d2543758706e03bd6cac787d487919cb067bd2af3d218df8df1aba3526b&response_type=code&redirect_uri=http://172.22.11.114:8080/questionnaire/index&scope=forms
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:获取list数据
- 接口地址:/questionnaire/list
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| description  	|	    	|	  string  |
| setting  	|	    	|	  object  |
| - color  	|	    	|	  string  |
| - push_url  	|	    	|	    |
| - search_state  	|	    	|	  string  |
| - search_url  	|	    	|	    |
| - success_redirect_url  	|	    	|	    |
| - icon  	|	    	|	  string  |
| - permission  	|	    	|	  string  |
| - success_redirect_fields  	|	    	|	  object  |
| - result_url  	|	    	|	    |
| - result_state  	|	    	|	  string  |
| - open_rule  	|	    	|	  string  |
| name  	|	  问卷标题  	|	  string  |
| entries_count  	|	    	|	  number  |
| shared  	|	    	|	  boolean  |
| created_at  	|	    	|	  string  |
| token  	|	  问卷访问地址  	|	  string  |
| id  	|	    	|	  string  |
| updated_at  	|	    	|	  string  |
## 模块名称: 用户中心
### 页面名称: 城市信息
#### 接口名称:根据城市父级ID查询
- 接口地址:/Setting/SettingCity/ParentId/{page}/{pageSize}/{parentid}
- 接口类型:GET
- 接口描述:{page}：页码
{pageSize}：每页显示条数
{parentid}：父级ID
注：查询省份{parentid}的值为  1000001
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  页码  	|	  number  |
| total  	|	  总条数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - patentid  	|	  父级城市ID  	|	  number  |
| - objectid  	|	  主键ID  	|	  number  |
| - name  	|	  城市名称  	|	  string  |
| - settingCity  	|	  父级城市对象  	|	  object  |
| - - objectid  	|	  父级城市主键ID  	|	  number  |
| - - patentid  	|	  父级ID  	|	  number  |
| - - name  	|	  父级城市名称  	|	  string  |
| - - settingCity  	|	    	|	    |
| pagesize  	|	  每页显示条数  	|	  number  |
### 页面名称: 商户评价管理
#### 接口名称:商户回复
- 接口地址:/Setting/MerchantReply/Add
- 接口类型:POST
- 接口描述:商户对用户评价的回复
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| merchant  	|	  商户ID  	|	  number  |
| content  	|	  回复内容  	|	  string  |
| eid  	|	  对应的用户评价ID  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:新增评价
- 接口地址:/Setting/MerchantEvaluate/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| taste  	|	  口味评分  	|	  number  |
| merchant  	|	  商户ID  	|	  number  |
| customer  	|	  用户ID  	|	  string  |
| env  	|	  环境评分  	|	  number  |
| service  	|	  服务评分  	|	  number  |
| comment  	|	  评论  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看评价详情(暂不用)
- 接口地址:/Setting/MerchantEvaluate/Edit/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| merchant  	|	  商户ID  	|	  number  |
| env  	|	  环境  	|	  number  |
| customer  	|	  客户ID  	|	  string  |
| objectid  	|	    	|	  number  |
| service  	|	  服务  	|	  number  |
| taste  	|	  口味  	|	  number  |
| comment  	|	  内容  	|	  string  |
#### 接口名称:评价列表(暂不用)
- 接口地址:/Setting/MerchantEvaluate/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| username  	|	  根据用户名查找  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - taste  	|	  品味评分  	|	  number  |
| - comment  	|	    	|	  string  |
| - createTime  	|	    	|	    |
| - customer  	|	  客户ID  	|	  string  |
| - service  	|	  服务评分  	|	  number  |
| - objectid  	|	    	|	  number  |
| - merchant  	|	  商户ID  	|	  number  |
| - env  	|	  环境评分  	|	  number  |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
### 页面名称: 通知管理
#### 接口名称:商户回应列表
- 接口地址:/Notification/ReplyList/{page}/{pageSize}
- 接口类型:GET
- 接口描述:商户回应列表
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
|   	|	    	|	    |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - objectid  	|	    	|	  number  |
| - author  	|	    	|	  string  |
| - accepter  	|	    	|	  string  |
| - readStatus  	|	    	|	  number  |
| - content  	|	    	|	  string  |
| - title  	|	    	|	  string  |
| - createTime  	|	    	|	  string  |
| total  	|	    	|	  number  |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
#### 接口名称:列表
- 接口地址:Notification/{page}/{pageSize}
- 接口类型:GET
- 接口描述:通知列表
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
|   	|	    	|	    |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  内容  	|	  string  |
| readStatus  	|	  1未读2已读  	|	  number  |
| author  	|	  作者  	|	  string  |
| createTime  	|	  创建时间  	|	  string  |
| title  	|	  通知标题  	|	  string  |
| id  	|	  序号  	|	  number  |
#### 接口名称:删除通知
- 接口地址:Notification/delete/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| id  	|	  序号  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果  	|	  number  |
### 页面名称: 账户安全
#### 接口名称:修改手机号码
- 接口地址:/Setting/User/ChangePhone
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| username  	|	  用户登录名  	|	  string  |
| phone  	|	  手机号码  	|	  string  |
| code  	|	  验证码  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	  消息  	|	  string  |
| success  	|	  状态  	|	  boolean  |
#### 接口名称:修改密码
- 接口地址:/Setting/User/ChangePassword
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| password  	|	  新密码［8-12］  	|	  string  |
| oldpassword  	|	  旧密码  	|	  string  |
| username  	|	  用户登录名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:忘记密码
- 接口地址:/Setting/User/SetNewPassword
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| phone  	|	  手机号码  	|	  string  |
| password  	|	  密码［8-12］  	|	  string  |
| username  	|	  用户登录名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	  返回消息  	|	  string  |
| success  	|	  状态  	|	  boolean  |
### 页面名称: 报修
#### 接口名称:查看报修信息
- 接口地址:UserRepair/Edit/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| repairTypeConfmDetail  	|	    	|	  object  |
| - objectid  	|	    	|	  number  |
| - type  	|	    	|	  string  |
| - settingDict  	|	    	|	  object  |
| - - settingDict  	|	    	|	    |
| - - objectid  	|	  问题确认大类id  	|	  number  |
| - - type  	|	    	|	  string  |
| - - name  	|	  问题确认大类名称  	|	  string  |
| - - parentid  	|	    	|	  number  |
| - name  	|	  问题确认名称  	|	  string  |
| - parentid  	|	    	|	  number  |
| acceptDate  	|	  受理时间  	|	    |
| repairTypeConfm  	|	  问题确认类型id  	|	  number  |
| applicant  	|	  报修人Id  	|	    |
| objectid  	|	  报修id  	|	  number  |
| memo  	|	  备注  	|	  string  |
| descriptionConfm  	|	  描述确认  	|	    |
| park  	|	    	|	  object  |
| - name  	|	  园区名称  	|	  string  |
| - updateDate  	|	    	|	  string  |
| - createDate  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - memo  	|	    	|	    |
| repairType  	|	  问题类型id  	|	  number  |
| typeId  	|	  报修分类  	|	  number  |
| serialNumber  	|	  流水号  	|	  string  |
| repairTypeDetail  	|	    	|	  object  |
| - type  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - name  	|	  问题类型名称  	|	  string  |
| - parentid  	|	    	|	  number  |
| - settingDict  	|	    	|	  object  |
| - - name  	|	  问题类型大类名称  	|	  string  |
| - - objectid  	|	  问题大类id  	|	  number  |
| - - settingDict  	|	    	|	    |
| - - type  	|	    	|	  string  |
| - - parentid  	|	    	|	  number  |
| photoUrl  	|	  照片地址  	|	    |
| createDate  	|	  报修时间  	|	    |
| company  	|	  公司  	|	  string  |
| contact  	|	  联系方式  	|	  string  |
| buildingId  	|	  楼宇id  	|	  number  |
| address  	|	  报修地址  	|	  string  |
| parkId  	|	  园区id  	|	  number  |
| description  	|	  描述  	|	  string  |
| building  	|	    	|	  object  |
| - objectid  	|	    	|	  number  |
| - parkid  	|	    	|	  number  |
| - createDate  	|	    	|	  string  |
| - name  	|	  楼宇名称  	|	  string  |
| - park  	|	    	|	  object  |
| - - objectid  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - - memo  	|	    	|	    |
| - - createDate  	|	    	|	  string  |
| - - updateDate  	|	    	|	  string  |
| - memo  	|	    	|	    |
| - updateDate  	|	    	|	  string  |
| commentFlag  	|	  评论标志  	|	  number  |
| voiceUrl  	|	  声音地址  	|	    |
#### 接口名称:编辑报修信息
- 接口地址:/UserRepair/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| acceptDate  	|	  受理时间  	|	    |
| parkId  	|	  园区Id  	|	  number  |
| commentFlag  	|	  评论标志  	|	  number  |
| address  	|	  报修地址  	|	  string  |
| buildingId  	|	  楼宇id  	|	  number  |
| descriptionConfm  	|	  问题描述确认  	|	    |
| contact  	|	  联系  	|	  string  |
| createDate  	|	  报修时间  	|	    |
| repairTypeConfm  	|	  问题类型确认Id  	|	  number  |
| description  	|	  问题描述  	|	  string  |
| repairType  	|	  问题类型Id  	|	  number  |
| typeId  	|	  报修分类：1物业；2：It  	|	  number  |
| serialNumber  	|	  流水号  	|	  string  |
| voiceUrl  	|	  声音地址  	|	    |
| applicant  	|	  报修人Id  	|	    |
| company  	|	  公司  	|	  string  |
| objectid  	|	  报修Id  	|	  number  |
| memo  	|	  备注  	|	  string  |
| photoUrl  	|	  照片地址  	|	    |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:添加报修
- 接口地址:/UserRepair/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| repairType  	|	  报修类型  	|	  number  |
| photo_url  	|	  图片路径  	|	  string  |
| descriptionConfm  	|	  描述修正  	|	  string  |
| company  	|	  公司名称  	|	  string  |
| description  	|	  描述  	|	  string  |
| address  	|	  报修地址  	|	  string  |
| parkId  	|	  所属园区ID  	|	  string  |
| appointDate  	|	  预约时间  	|	  string  |
| repairTypeConfm  	|	  受理人确认的报修类型  	|	  number  |
| contact  	|	  座机电话  	|	  number  |
| mobile  	|	  手机号码  	|	  string  |
| applicant  	|	  报修申请人ID  	|	  string  |
| memo  	|	  备注  	|	  string  |
| voice_url  	|	  声音文件地址  	|	  string  |
| buildingId  	|	  楼宇ID  	|	  string  |
| createDate  	|	  报修时间  	|	  number  |
| acceptDate  	|	  受理时间  	|	  number  |
| typeId  	|	  报修类型ID  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  0成功 1失败  	|	  number  |
### 页面名称: 头像设置
#### 接口名称:编辑用户头像
- 接口地址:/Setting/User/ChangeUserFace/{username}
- 接口类型:POST
- 接口描述:{username}：用户登录名
注：编辑用户头像时，先调用“普通图片上传”接口，图片上传成功后会返回URL地址，再调用本接口
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| upload  	|	  头像图片  	|	  object  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:获取头像
- 接口地址:/Setting/User/UserFace/{username}
- 接口类型:GET
- 接口描述:{username}:用户登录名
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
### 页面名称: 服务评价管理
#### 接口名称:评价次数统计
- 接口地址:Comment/StatisticsNumber
- 接口类型:GET
- 接口描述:[[3,4.3333,5.0000,5.0000]]

按顺序 累计评价数    平均服务态度   平均服务质量   平均响应速度
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:列表
- 接口地址:/Comment/{page}/{pageSize}
- 接口类型:GET
- 接口描述:page: 当前页码
pageSize:页大小
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  页码  	|	  number  |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
| total  	|	  总页数  	|	  number  |
| total  	|	    	|	  number  |
| pageSize  	|	  页大小  	|	  number  |
| result  	|	    	|	  array<object>  |
| - quality  	|	  工作质量－打分项  	|	  number  |
| - businessType  	|	  评论类型  	|	  number  |
| - createTime  	|	  评论时间  	|	  string  |
| - content  	|	  评论内容  	|	  string  |
| - serialNumber  	|	  对应评价主题的流水号  	|	  string  |
| - duration  	|	  工作周期－打分项  	|	  number  |
| - userid  	|	  用户ID  	|	  string  |
| - attitude  	|	  工作态度－打分项  	|	  number  |
| - businessId  	|	  业务表id  	|	  string  |
#### 接口名称:查看评价详情
- 接口地址:/Comment/Edit/{userid}/{serialNumber}
- 接口类型:GET
- 接口描述:查看详情
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| serialNumber  	|	  评价所对应的主题流水号  	|	  string  |
| createTime  	|	  评价时间  	|	  number  |
| username  	|	  用户id  	|	  string  |
| attitude  	|	  工作态度－打分项  	|	  number  |
| duration  	|	  工作周期－打分项  	|	  number  |
| quality  	|	  工作质量－打分项  	|	  number  |
| content  	|	  评价内容  	|	  string  |
| businessType  	|	  业务类型  	|	  number  |
#### 接口名称:评价数据表
- 接口地址:/Comment/StatisticsData/{beginDate}/{endDate}/{query}/{flag}
- 接口类型:GET
- 接口描述:{beginDate}:起始时间
{endDate}：结束时间
{query}：类型参数  a：全部，1：物业，2：it,3：企业培训，4：猎聘申请，5：教室预订，6：会议室预订，7：广告服务申请，8：测试申请，9：著作权登记，10：入驻申请
{flag}：统计方式 d：按天   m：按月
返回数据中 series中按照顺序分别是  评价次数／服务态度／响应速度／服务质量
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:发布
- 接口地址:/Comment/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| businessType  	|	  评价类型  	|	  number  |
| businessId  	|	  未评论接口数据的businessid  	|	  string  |
| createTime  	|	  评价时间  	|	  string  |
| serialNumber  	|	  所属评价主题的流水号  	|	  string  |
| content  	|	  内容  	|	  string  |
| attitude  	|	  工作态度－打分项  	|	  number  |
| username  	|	  用户ID  	|	  string  |
| duration  	|	  工作周期－打分项  	|	  number  |
| quality  	|	  工作质量－打分项  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果  	|	  number  |
### 页面名称: 个人信息
#### 接口名称:所有用户数
- 接口地址:/Setting/User/TotalUser
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:内部用户列表
- 接口地址:/Setting/User/InsideUser/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}：每页显示条数
注：响应信息字段含义请参考/Setting/User/Edit/{username}接口
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - applyFlag  	|	    	|	  number  |
| - entrtpriseRoot  	|	    	|	  number  |
| - settingHometowntCity  	|	    	|	  object  |
| - - objectid  	|	    	|	  number  |
| - - patentid  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - - settingCity  	|	    	|	    |
| - roleArray  	|	    	|	    |
| - enterpriseId  	|	    	|	  number  |
| - hometownDetail  	|	    	|	  string  |
| - settingNation  	|	    	|	  object  |
| - - type  	|	    	|	  string  |
| - - name  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - hometownCity  	|	    	|	  number  |
| - enterprise  	|	    	|	  object  |
| - - name  	|	    	|	  string  |
| - - enterpriseScale  	|	    	|	  object  |
| - - - objectid  	|	    	|	  number  |
| - - - name  	|	    	|	  string  |
| - - - type  	|	    	|	  string  |
| - - memo  	|	    	|	  string  |
| - - enterpriseType  	|	    	|	  object  |
| - - - objectid  	|	    	|	  number  |
| - - - type  	|	    	|	  string  |
| - - - name  	|	    	|	  string  |
| - - contacts  	|	    	|	  string  |
| - - abbreviation  	|	    	|	  string  |
| - - industry  	|	    	|	  number  |
| - - contactsinfo  	|	    	|	  string  |
| - - createDate  	|	    	|	  string  |
| - - address  	|	    	|	  string  |
| - - enterpriseIndustry  	|	    	|	  object  |
| - - - objectid  	|	    	|	  number  |
| - - - type  	|	    	|	  string  |
| - - - name  	|	    	|	  string  |
| - - scale  	|	    	|	  number  |
| - - introduction  	|	    	|	  string  |
| - - deleteFlag  	|	    	|	  number  |
| - - type  	|	    	|	  number  |
| - - objectid  	|	    	|	  number  |
| - - updateDate  	|	    	|	  string  |
| - settingApartmentCity  	|	    	|	  object  |
| - - patentid  	|	    	|	  number  |
| - - objectid  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - - settingCity  	|	    	|	  object  |
| - - - name  	|	    	|	  string  |
| - - - objectid  	|	    	|	  number  |
| - - - patentid  	|	    	|	  number  |
| - - - settingCity  	|	    	|	  object  |
| - - - - name  	|	    	|	  string  |
| - - - - settingCity  	|	    	|	    |
| - - - - patentid  	|	    	|	  number  |
| - - - - objectid  	|	    	|	  number  |
| - accountNonExpired  	|	    	|	  boolean  |
| - nation  	|	    	|	  number  |
| - username  	|	    	|	  string  |
| - userFlag  	|	  用户标示（1-内部用户，2-普通用户，3-企业用户）  	|	    |
| - email  	|	    	|	  string  |
| - realName  	|	    	|	  string  |
| - roleList  	|	    	|	  array<object>  |
| - - name  	|	    	|	  string  |
| - - memo  	|	    	|	  string  |
| - - rolename  	|	    	|	  string  |
| - - permissionArray  	|	    	|	    |
| - - permissionList  	|	    	|	  array<object>  |
| - - - name  	|	    	|	  string  |
| - - - memo  	|	    	|	    |
| - - - objectid  	|	    	|	  number  |
| - - - url  	|	    	|	  string  |
| - - isShow  	|	    	|	  number  |
| - credentialsNonExpired  	|	    	|	  boolean  |
| - accountNonLocked  	|	    	|	  boolean  |
| - apartmentCity  	|	    	|	  number  |
| - userFace  	|	    	|	  string  |
| - apartmentDetail  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| - workYears  	|	    	|	  number  |
| - enabled  	|	    	|	  boolean  |
| - sex  	|	    	|	  number  |
| - signature  	|	    	|	    |
| - approved  	|	    	|	  boolean  |
| - cardid  	|	    	|	  string  |
| - deleteFlag  	|	    	|	  number  |
| - settingDict  	|	    	|	  object  |
| - - objectid  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - - type  	|	    	|	  string  |
| - marital  	|	    	|	  number  |
| - phone  	|	    	|	  string  |
| - education  	|	    	|	  number  |
| - birthday  	|	    	|	  string  |
| total  	|	    	|	  number  |
#### 接口名称:查看用户信息
- 接口地址:/Setting/User/Edit/{username}
- 接口类型:GET
- 接口描述:{username}：用户登录名
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| settingNation  	|	    	|	  object  |
| - name  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - type  	|	    	|	  string  |
| username  	|	    	|	  string  |
| enterprise  	|	    	|	  object  |
| - contacts  	|	    	|	  string  |
| - enterpriseType  	|	    	|	  object  |
| - - objectid  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - - type  	|	    	|	  string  |
| - contactsinfo  	|	    	|	  string  |
| - introduction  	|	    	|	  string  |
| - deleteFlag  	|	    	|	  number  |
| - memo  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - type  	|	    	|	  number  |
| - updateDate  	|	    	|	  string  |
| - address  	|	    	|	  string  |
| - industry  	|	    	|	  number  |
| - name  	|	    	|	  string  |
| - createDate  	|	    	|	  string  |
| - scale  	|	    	|	  number  |
| - enterpriseIndustry  	|	    	|	  object  |
| - - name  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - type  	|	    	|	  string  |
| - abbreviation  	|	    	|	  string  |
| - enterpriseScale  	|	    	|	  object  |
| - - type  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| cardid  	|	    	|	  string  |
| signature  	|	    	|	    |
| hometownCity  	|	    	|	  number  |
| roleList  	|	    	|	  array<object>  |
| - name  	|	    	|	  string  |
| - permissionList  	|	    	|	  array<object>  |
| - - url  	|	    	|	  string  |
| - - name  	|	    	|	  string  |
| - - memo  	|	    	|	    |
| - - objectid  	|	    	|	  number  |
| - permissionArray  	|	    	|	    |
| - rolename  	|	    	|	  string  |
| - memo  	|	    	|	  string  |
| - isShow  	|	    	|	  number  |
| userFlag  	|	  用户标示（1-内部用户，2-普通用户，3-企业用户）  	|	    |
| applyFlag  	|	    	|	  number  |
| accountNonLocked  	|	    	|	  boolean  |
| hometownDetail  	|	    	|	  string  |
| workYears  	|	    	|	  number  |
| nation  	|	    	|	  number  |
| email  	|	    	|	  string  |
| updateDate  	|	    	|	  string  |
| settingDict  	|	    	|	  object  |
| - type  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| realName  	|	    	|	  string  |
| approved  	|	    	|	  boolean  |
| marital  	|	    	|	  number  |
| enabled  	|	    	|	  boolean  |
| createDate  	|	    	|	  string  |
| birthday  	|	    	|	  string  |
| apartmentCity  	|	    	|	  number  |
| sex  	|	    	|	  number  |
| entrtpriseRoot  	|	    	|	  number  |
| credentialsNonExpired  	|	    	|	  boolean  |
| userFace  	|	    	|	  string  |
| name  	|	    	|	  string  |
| settingHometowntCity  	|	    	|	  object  |
| - objectid  	|	    	|	  number  |
| - settingCity  	|	    	|	    |
| - name  	|	    	|	  string  |
| - patentid  	|	    	|	  number  |
| phone  	|	    	|	  string  |
| enterpriseId  	|	    	|	  number  |
| accountNonExpired  	|	    	|	  boolean  |
| deleteFlag  	|	    	|	  number  |
| settingApartmentCity  	|	    	|	  object  |
| - settingCity  	|	    	|	  object  |
| - - objectid  	|	    	|	  number  |
| - - patentid  	|	    	|	  number  |
| - - settingCity  	|	    	|	  object  |
| - - - name  	|	    	|	  string  |
| - - - objectid  	|	    	|	  number  |
| - - - settingCity  	|	    	|	    |
| - - - patentid  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - patentid  	|	    	|	  number  |
| - name  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| apartmentDetail  	|	    	|	  string  |
| roleArray  	|	    	|	    |
| education  	|	    	|	  number  |
#### 接口名称:编辑个人信息
- 接口地址:/Setting/User/CompleteUserInfo
- 接口类型:POST
- 接口描述:注：用户登录名username为必填项
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| username  	|	  用户登录名  	|	  string  |
| apartmentCity  	|	  现居地城市ID  	|	  string  |
| signature  	|	  个性签名  	|	  string  |
| education  	|	  学历ID  	|	    |
| nation  	|	  国籍id  	|	  string  |
| hometownCity  	|	  家乡城市ID  	|	  string  |
| workyears  	|	  工作年限  	|	  string  |
| sex  	|	  性别（1-男 2-女）  	|	  string  |
| userFlag  	|	  用户标示（1-内部用户，2-普通用户，3-企业用户）  	|	  string  |
| marital  	|	  婚姻状况(1-未婚，2-已婚)  	|	  string  |
| birthday  	|	  生日  	|	  string  |
| enterpriseInput  	|	  企业名称  	|	  string  |
| department  	|	  内部用户部门ID（数据字典）  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:网站数据统计
- 接口地址:/Setting/User/StatisticsData
- 接口类型:GET
- 接口描述:{beginDate}：起始时间 eg 2015-12-03
{endDate}:结束时间   eg 2015-12-05
{flag}: 统计方式  d：日、m：月
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:删除用户
- 接口地址:/Setting/User/Delete/{username}
- 接口类型:GET
- 接口描述:{username}：用户登录名
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:本月注册用户数
- 接口地址:/Setting/User/MonthUser
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	    	|	  boolean  |
| msg  	|	    	|	  string  |
#### 接口名称:用户信息列表
- 接口地址:/Setting/User/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}：总页数
注：查询普通用户信息请带参数  ?userFlag=2
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	  总条数  	|	  number  |
| pagesize  	|	  总页数  	|	  number  |
| page  	|	  页码  	|	  number  |
| result  	|	  返回结果集  	|	  array<object>  |
| - name  	|	  用户昵称  	|	  string  |
| - phone  	|	  联系电话  	|	  string  |
| - deleteFlag  	|	  禁用启用标识符（默认为1-启用，-1表示禁用）  	|	  number  |
| - roleList  	|	  角色信息  	|	  array<object>  |
| - - isShow  	|	  企业管理员是否显示该角色（默认0-不显示，1-显示）  	|	  number  |
| - - permissionArray  	|	    	|	    |
| - - permissionList  	|	  权限信息  	|	  array<object>  |
| - - - url  	|	  权限地址  	|	  string  |
| - - - name  	|	  权限名称  	|	  string  |
| - - - objectid  	|	  权限id  	|	  number  |
| - - - memo  	|	  备注  	|	    |
| - - rolename  	|	  角色名称  	|	  string  |
| - - memo  	|	  备注  	|	  string  |
| - - name  	|	  角色名称  	|	  string  |
| - accountNonExpired  	|	    	|	  boolean  |
| - hometownDetail  	|	  家乡详细地址  	|	  string  |
| - enterpriseRoot  	|	  是否为企业管理员（1-不是，2-是）  	|	  number  |
| - nation  	|	  国籍  	|	  number  |
| - credentialsNonExpired  	|	    	|	  boolean  |
| - enterprise  	|	  企业信息  	|	  object  |
| - - introduction  	|	  企业简介  	|	  string  |
| - - contactsinfo  	|	  企业联系方式  	|	  string  |
| - - enterpriseIndustry  	|	  企业所属行业  	|	  object  |
| - - - objectid  	|	  编号  	|	  number  |
| - - - type  	|	  字典类型  	|	  string  |
| - - - name  	|	  所属行业名称  	|	  string  |
| - - enterpriseType  	|	  企业类型  	|	  object  |
| - - - objectid  	|	  编号  	|	  number  |
| - - - name  	|	  企业类型名称  	|	  string  |
| - - - type  	|	  字典类型  	|	  string  |
| - - enterpriseScale  	|	  企业规模  	|	  object  |
| - - - type  	|	  字典类型  	|	  string  |
| - - - objectid  	|	  编号  	|	  number  |
| - - - name  	|	  规模名  	|	  string  |
| - - name  	|	  企业名称  	|	  string  |
| - - industry  	|	  所属行业ID  	|	  number  |
| - - updateDate  	|	  更新时间  	|	  string  |
| - - address  	|	  企业地址  	|	  string  |
| - - contacts  	|	  企业联系人  	|	  string  |
| - - abbreviation  	|	  企业简称  	|	  string  |
| - - scale  	|	  企业规模ID  	|	  number  |
| - - type  	|	  企业类型ID  	|	  number  |
| - - enterprise  	|	  父级企业信息  	|	    |
| - - memo  	|	  备注  	|	  string  |
| - - createDate  	|	  创建时间  	|	  string  |
| - - parentid  	|	  父级企业ID  	|	  number  |
| - - objectid  	|	  企业编号  	|	  number  |
| - - deleteFlag  	|	  企业逻辑删除标记（0-默认，1-删除）  	|	  number  |
| - birthday  	|	  出生日期  	|	  string  |
| - settingDepartmentDict  	|	  部门结果集  	|	  object  |
| - - type  	|	  数据字典类型  	|	    |
| - - name  	|	  部门名称  	|	  string  |
| - - objectid  	|	  数据字典编号  	|	    |
| - signature  	|	  个性签名  	|	    |
| - username  	|	  用户名  	|	  string  |
| - hometownCity  	|	  家乡省份ID  	|	  number  |
| - applyFlag  	|	  成员申请状态(1-默认，2-同意，3-不同意)  	|	    |
| - apartmentCity  	|	  现居地城市ID  	|	  number  |
| - roleArray  	|	    	|	    |
| - workYears  	|	  工作年限  	|	  number  |
| - realName  	|	  真实姓名  	|	  string  |
| - userFlag  	|	  用户标示（1-内部用户，2-普通用户，3-企业用户）  	|	    |
| - settingHometowntCity  	|	  家乡城市信息  	|	  object  |
| - - patentid  	|	  父级城市ID  	|	  number  |
| - - name  	|	  名称  	|	  string  |
| - - objectid  	|	  id  	|	  number  |
| - - settingCity  	|	  父级城市信息  	|	    |
| - settingNation  	|	  国家信息  	|	  object  |
| - - objectid  	|	  id  	|	  number  |
| - - type  	|	  数据字典类型  	|	  string  |
| - - name  	|	  名称  	|	  string  |
| - apartmentDetail  	|	  现居住地详细地址  	|	  string  |
| - enterpriseId  	|	    	|	  number  |
| - settingApartmentCity  	|	  先居住地城市信息  	|	  object  |
| - - objectid  	|	  id  	|	  number  |
| - - settingCity  	|	  父级城市信息  	|	  object  |
| - - - patentid  	|	  父级城市ID  	|	  number  |
| - - - name  	|	  名称  	|	  string  |
| - - - objectid  	|	  id  	|	  number  |
| - - patentid  	|	  父级城市ID  	|	  number  |
| - - name  	|	  名称  	|	  string  |
| - cardid  	|	  汇智卡ID  	|	  string  |
| - accountNonLocked  	|	    	|	  boolean  |
| - enabled  	|	    	|	  boolean  |
| - email  	|	  邮箱  	|	  string  |
| - userFace  	|	  用户头像  	|	  string  |
| - approved  	|	    	|	  boolean  |
| - settingDict  	|	  学历信息  	|	  object  |
| - - type  	|	  数据字典类型  	|	  string  |
| - - objectid  	|	  id  	|	  number  |
| - - name  	|	  名称  	|	  string  |
| - education  	|	  学历ID  	|	  number  |
| - marital  	|	  婚姻状况(0-保密，1-已婚，2-未婚)  	|	  number  |
| - sex  	|	  性别（1-男 2-女）  	|	  number  |
#### 接口名称:根据角色名查询用户列表
- 接口地址:/Setting/User/getUserByRoleName
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| roleName  	|	  角色名称（以逗号隔开））  	|	    |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - roleArray  	|	    	|	    |
| - settingNation  	|	    	|	    |
| - cardid  	|	    	|	    |
| - deleteFlag  	|	    	|	  number  |
| - nation  	|	    	|	  number  |
| - sex  	|	    	|	  number  |
| - marital  	|	    	|	  number  |
| - settingDepartmentDict  	|	    	|	    |
| - workYears  	|	    	|	  number  |
| - enterprise  	|	    	|	    |
| - department  	|	    	|	  number  |
| - userFace  	|	    	|	    |
| - settingApartmentCity  	|	    	|	    |
| - settingHometowntCity  	|	    	|	    |
| - realName  	|	    	|	  string  |
| - phone  	|	    	|	  string  |
| - signature  	|	    	|	    |
| - birthday  	|	    	|	    |
| - enabled  	|	    	|	  boolean  |
| - settingDict  	|	    	|	    |
| - enterpriseId  	|	    	|	  number  |
| - education  	|	    	|	  number  |
| - roleList  	|	    	|	  array<object>  |
| - - name  	|	    	|	  string  |
| - - memo  	|	    	|	  string  |
| - - permissionList  	|	    	|	  array<object>  |
| - - - objectid  	|	    	|	  number  |
| - - - url  	|	    	|	  string  |
| - - - memo  	|	    	|	    |
| - - - name  	|	    	|	  string  |
| - - permissionArray  	|	    	|	    |
| - - rolename  	|	    	|	  string  |
| - - isShow  	|	    	|	  number  |
| - accountNonLocked  	|	    	|	  boolean  |
| - apartmentCity  	|	    	|	  number  |
| - createDate  	|	    	|	  string  |
| - credentialsNonExpired  	|	    	|	  boolean  |
| - enterpriseInput  	|	    	|	    |
| - approved  	|	    	|	  boolean  |
| - name  	|	    	|	  string  |
| - email  	|	    	|	    |
| - hometownCity  	|	    	|	  number  |
| - enterpriseRoot  	|	    	|	  number  |
| - accountNonExpired  	|	    	|	  boolean  |
| - userFlag  	|	    	|	  number  |
| - username  	|	    	|	  string  |
| - updateDate  	|	    	|	  string  |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
#### 接口名称:用户获取自己个人信息
- 接口地址:/Setting/User/MyInfo
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:注册用户列表
- 接口地址:/Setting/User/CommonUser/{page}/{pageSize}
- 接口类型:GET
- 接口描述:接口返回信息详见“内部用户信息列表”
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看个人信息
- 接口地址:/Setting/User/Edit/{username}
- 接口类型:GET
- 接口描述:{username}:用户登录名
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| userFlag  	|	  用户标示（1-内部用户，2-普通用户，3-企业用户）  	|	  string  |
| entrtpriseRoot  	|	  是否为企业管理员（1-不是，2-是）  	|	  string  |
| department  	|	  内部用户部门（数据字典ID）  	|	    |
| settingDepartmentDict  	|	  内部用户部门对象  	|	  object  |
| - type  	|	  数据字典类型  	|	  string  |
| - name  	|	  部门名称  	|	  string  |
#### 接口名称:今天注册用户数
- 接口地址:/Setting/User/TodayUser
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:更新用户
- 接口地址:/Setting/User/Update
- 接口类型:GET
- 接口描述:请求参数字段说明详见：“/Setting/User/Add”接口
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| apartmentDetail  	|	    	|	  string  |
| phone  	|	    	|	  string  |
| workYears  	|	    	|	  number  |
| hometownCity  	|	    	|	  number  |
| apartmentCity  	|	    	|	  number  |
| marital  	|	    	|	  number  |
| signature  	|	    	|	    |
| cardid  	|	    	|	  string  |
| enabled  	|	    	|	  boolean  |
| name  	|	    	|	  string  |
| approved  	|	    	|	  boolean  |
| userFlag  	|	  用户标示（1-内部用户，2-普通用户，3-企业用户）  	|	    |
| applyFlag  	|	    	|	  number  |
| deleteFlag  	|	    	|	  number  |
| email  	|	    	|	  string  |
| realName  	|	    	|	  string  |
| userFace  	|	    	|	  string  |
| birthday  	|	    	|	  string  |
| entrtpriseRoot  	|	    	|	  number  |
| sex  	|	    	|	  number  |
| username  	|	    	|	  string  |
| enterpriseId  	|	    	|	  number  |
| hometownDetail  	|	    	|	  string  |
| nation  	|	    	|	  number  |
| roleArray  	|	    	|	    |
| education  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:添加用户
- 接口地址:/Setting/User/Add
- 接口类型:POST
- 接口描述:注：请求参数字段含义请参考/Setting/User/Edit/{username}接口
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| roleArray  	|	  角色名称（可选）  	|	  string  |
| approved  	|	    	|	  boolean  |
| enterpriseInput  	|	  [50]  	|	    |
| birthday  	|	    	|	  string  |
| hometownDetail  	|	  [255]  	|	  string  |
| email  	|	  [50]  	|	  string  |
| hometownCity  	|	    	|	  number  |
| apartmentCity  	|	    	|	  number  |
| marital  	|	    	|	  number  |
| cardid  	|	    	|	  string  |
| department  	|	  部门信息（数据字典）  	|	  string  |
| sex  	|	    	|	  number  |
| applyFlag  	|	    	|	  number  |
| name  	|	  *昵称[20]  	|	  string  |
| deleteFlag  	|	    	|	  number  |
| userFace  	|	    	|	  string  |
| userFlag  	|	  *用户标示（1-内部用户，2-普通用户，3-企业用户）  	|	    |
| education  	|	    	|	  number  |
| nation  	|	    	|	  number  |
| signature  	|	  [255]  	|	    |
| entrtpriseRoot  	|	    	|	  number  |
| apartmentDetail  	|	  [255]  	|	  string  |
| enterpriseId  	|	    	|	  number  |
| phone  	|	  *电话号码[20]  	|	  string  |
| enabled  	|	    	|	  boolean  |
| workYears  	|	    	|	  number  |
| username  	|	  *用户登录名[20]  	|	  string  |
| realName  	|	  *用户真实姓名[20]  	|	  string  |
| password  	|	  *登录密码[16]  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:离开企业
- 接口地址:/Setting/User/LeaveEnterprise
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	    	|	  boolean  |
| msg  	|	    	|	  string  |
### 页面名称: 企业信息
#### 接口名称:企业内用户列表
- 接口地址:/Setting/User/EnterpriseAdminShowUser/{page}/{pageSize}/{enterpriseId}
- 接口类型:GET
- 接口描述:{page}:第几页
{pageSize}：每页条数
{enterpriseId}：企业ID
注：本接口可以带参数查询 eg: ?username=admin
查询企业内申请用户列表添加参数 ：applyFlag=1
查询企业内用户列表添加参数 ：applyFlag=2
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	    	|	  number  |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - email  	|	    	|	  string  |
| - signature  	|	    	|	    |
| - userFace  	|	    	|	  string  |
| - hometownCity  	|	    	|	  number  |
| - workYears  	|	    	|	  number  |
| - deleteFlag  	|	    	|	  number  |
| - entrtpriseRoot  	|	    	|	  number  |
| - realName  	|	    	|	  string  |
| - sex  	|	    	|	  number  |
| - enterpriseId  	|	    	|	  number  |
| - name  	|	    	|	  string  |
| - settingApartmentCity  	|	    	|	  object  |
| - - patentid  	|	    	|	  number  |
| - - objectid  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - - settingCity  	|	    	|	  object  |
| - - - name  	|	    	|	  string  |
| - - - settingCity  	|	    	|	  object  |
| - - - - name  	|	    	|	  string  |
| - - - - patentid  	|	    	|	  number  |
| - - - - objectid  	|	    	|	  number  |
| - - - - settingCity  	|	    	|	    |
| - - - objectid  	|	    	|	  number  |
| - - - patentid  	|	    	|	  number  |
| - cardid  	|	    	|	  string  |
| - apartmentCity  	|	    	|	  number  |
| - accountNonLocked  	|	    	|	  boolean  |
| - nation  	|	    	|	  number  |
| - enterprise  	|	    	|	  object  |
| - - address  	|	    	|	  string  |
| - - createDate  	|	    	|	  string  |
| - - introduction  	|	    	|	  string  |
| - - industry  	|	    	|	  number  |
| - - objectid  	|	    	|	  number  |
| - - contacts  	|	    	|	  string  |
| - - updateDate  	|	    	|	  string  |
| - - contactsinfo  	|	    	|	  string  |
| - - abbreviation  	|	    	|	  string  |
| - - memo  	|	    	|	  string  |
| - - deleteFlag  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - - enterpriseType  	|	    	|	  object  |
| - - - type  	|	    	|	  string  |
| - - - objectid  	|	    	|	  number  |
| - - - name  	|	    	|	  string  |
| - - scale  	|	    	|	  number  |
| - - enterpriseIndustry  	|	    	|	  object  |
| - - - objectid  	|	    	|	  number  |
| - - - name  	|	    	|	  string  |
| - - - type  	|	    	|	  string  |
| - - type  	|	    	|	  number  |
| - - enterpriseScale  	|	    	|	  object  |
| - - - type  	|	    	|	  string  |
| - - - name  	|	    	|	  string  |
| - - - objectid  	|	    	|	  number  |
| - settingHometowntCity  	|	    	|	  object  |
| - - name  	|	    	|	  string  |
| - - patentid  	|	    	|	  number  |
| - - settingCity  	|	    	|	    |
| - - objectid  	|	    	|	  number  |
| - applyFlag  	|	    	|	  number  |
| - approved  	|	    	|	  boolean  |
| - roleArray  	|	    	|	    |
| - accountNonExpired  	|	    	|	  boolean  |
| - settingDict  	|	    	|	  object  |
| - - objectid  	|	    	|	  number  |
| - - type  	|	    	|	  string  |
| - - name  	|	    	|	  string  |
| - roleList  	|	    	|	  array<object>  |
| - - name  	|	    	|	  string  |
| - - rolename  	|	    	|	  string  |
| - - permissionList  	|	    	|	  array<object>  |
| - - - name  	|	    	|	  string  |
| - - - memo  	|	    	|	    |
| - - - url  	|	    	|	  string  |
| - - - objectid  	|	    	|	  number  |
| - - permissionArray  	|	    	|	    |
| - - isShow  	|	    	|	  number  |
| - - memo  	|	    	|	  string  |
| - education  	|	    	|	  number  |
| - marital  	|	    	|	  number  |
| - username  	|	    	|	  string  |
| - phone  	|	    	|	  string  |
| - hometownDetail  	|	    	|	  string  |
| - apartmentDetail  	|	    	|	  string  |
| - settingNation  	|	    	|	  object  |
| - - type  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - credentialsNonExpired  	|	    	|	  boolean  |
| - enabled  	|	    	|	  boolean  |
| - birthday  	|	    	|	  string  |
| 参数字段请参见用户接口表描述  	|	    	|	    |
| pagesize  	|	    	|	  number  |
#### 接口名称:企业管理员审核用户申请操作
- 接口地址:/Setting/UserEnterpriseApplyInfo/EnableEnterpriseApply/{object}/{flag}
- 接口类型:GET
- 接口描述:{objectid}:用户申请表objectid
{flag}：0-不同意申请，1-同意申请
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:绑定企业
- 接口地址:/Setting/User/BindingEnterprise/{enterpriseId}
- 接口类型:GET
- 接口描述:{username}:用户登录名
{enterpriseId}：企业ID
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	  消息  	|	  string  |
| success  	|	  状态  	|	  boolean  |
#### 接口名称:企业信息
- 接口地址:/Setting/Enterprise/Edit/{objectid}
- 接口类型:GET
- 接口描述:{objectid}:企业ID
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| enterpriseIntake  	|	  入驻年份对象  	|	  object  |
| - type  	|	  数据字典类型  	|	  string  |
| - name  	|	  年份  	|	  string  |
| - objectid  	|	  数据字典id  	|	  string  |
|   	|	    	|	    |
| intake  	|	  入驻年份id  	|	  string  |
#### 接口名称:判断是否绑定企业
- 接口地址:/Setting/User/IsBindEnterprise
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	  消息  	|	  string  |
| success  	|	  状态  	|	  boolean  |
#### 接口名称:企业管理员删除内部用户
- 接口地址:/Setting/User/DeleteEnterpriseBind/{username}/{enterpriseid}
- 接口类型:GET
- 接口描述:{username}：需要删除的内部用户的登录名
{enterpriseid}：当前企业管理员所在企业的ID
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:企业内部用户登录后查看企业信息
- 接口地址:/Setting/Enterprise/EnterpriseInfo
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| industry  	|	    	|	  number  |
| name  	|	    	|	  string  |
| enterpriseType  	|	    	|	  object  |
| - type  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| scale  	|	    	|	  number  |
| memo  	|	    	|	    |
| createDate  	|	    	|	  string  |
| address  	|	    	|	  string  |
| type  	|	    	|	  number  |
| contacts  	|	    	|	  string  |
| abbreviation  	|	    	|	  string  |
| enterpriseIntake  	|	    	|	  object  |
| - objectid  	|	    	|	  number  |
| - type  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| intake  	|	    	|	  number  |
| contactsinfo  	|	    	|	  string  |
| deleteFlag  	|	    	|	  number  |
| enterpriseIndustry  	|	    	|	  object  |
| - type  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - name  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| username  	|	    	|	  string  |
| enterpriseScale  	|	    	|	  object  |
| - type  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - name  	|	    	|	  string  |
| updateDate  	|	    	|	  string  |
#### 接口名称:搜索企业信息
- 接口地址:/Setting/Enterprise/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}：每页条数
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| address  	|	  企业地址  	|	  string  |
| contacts  	|	  企业联系人  	|	  string  |
| abbreviation  	|	  企业简称  	|	  string  |
| name  	|	  企业名称  	|	  string  |
| contactsinfo  	|	  企业联系方式  	|	  string  |
| objectid  	|	  ID  	|	  number  |
| deleteFlag  	|	  企业逻辑禁用标记（0-默认，-1-已禁用）  	|	  number  |
| memo  	|	  备注  	|	  string  |
| introduction  	|	  企业简介  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:企业管理员查看申请列表
- 接口地址:/Setting/UserEnterpriseApplyInfo/ApplyList/{page}/{pagesize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - updateDate  	|	    	|	  string  |
| - objectid  	|	  主键ID  	|	  number  |
| - enterpriseId  	|	  企业id  	|	  number  |
| - applyFlag  	|	  申请状态（1-默认，2-同意，3-不同意，4-离开）  	|	  number  |
| - createDate  	|	    	|	  string  |
| - user  	|	  user对象  	|	  object  |
| - - sex  	|	    	|	  number  |
| - - userFlag  	|	    	|	  number  |
| - - education  	|	    	|	  number  |
| - - approved  	|	    	|	  boolean  |
| - - apartmentCity  	|	    	|	  number  |
| - - userFace  	|	    	|	    |
| - - enterpriseInput  	|	    	|	    |
| - - marital  	|	    	|	  number  |
| - - accountNonExpired  	|	    	|	  boolean  |
| - - department  	|	    	|	  number  |
| - - settingApartmentCity  	|	    	|	    |
| - - signature  	|	    	|	    |
| - - workYears  	|	    	|	  number  |
| - - settingHometowntCity  	|	    	|	    |
| - - roleList  	|	    	|	  object  |
| - - roleArray  	|	    	|	    |
| - - settingDepartmentDict  	|	    	|	    |
| - - enabled  	|	    	|	  boolean  |
| - - username  	|	    	|	  string  |
| - - createDate  	|	    	|	  string  |
| - - nation  	|	    	|	  number  |
| - - updateDate  	|	    	|	  string  |
| - - name  	|	    	|	  string  |
| - - enterpriseId  	|	    	|	  number  |
| - - cardid  	|	    	|	    |
| - - birthday  	|	    	|	    |
| - - hometownCity  	|	    	|	  number  |
| - - settingNation  	|	    	|	    |
| - - settingDict  	|	    	|	    |
| - - accountNonLocked  	|	    	|	  boolean  |
| - - deleteFlag  	|	    	|	  number  |
| - - credentialsNonExpired  	|	    	|	  boolean  |
| - - enterprise  	|	    	|	    |
| - - phone  	|	    	|	  string  |
| - - realName  	|	    	|	  string  |
| - - enterpriseRoot  	|	    	|	  number  |
| - - email  	|	    	|	    |
| - username  	|	    	|	  string  |
| - enterprise  	|	  企业对像  	|	  object  |
| - - contactsinfo  	|	    	|	  string  |
| - - enterpriseScale  	|	    	|	  object  |
| - - - objectid  	|	    	|	  number  |
| - - - name  	|	    	|	  string  |
| - - - type  	|	    	|	  string  |
| - - updateDate  	|	    	|	  string  |
| - - intake  	|	    	|	  number  |
| - - address  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - username  	|	    	|	  string  |
| - - enterpriseType  	|	    	|	  object  |
| - - - type  	|	    	|	  string  |
| - - - name  	|	    	|	  string  |
| - - - objectid  	|	    	|	  number  |
| - - industry  	|	    	|	  number  |
| - - enterpriseIntake  	|	    	|	  object  |
| - - - type  	|	    	|	  string  |
| - - - objectid  	|	    	|	  number  |
| - - - name  	|	    	|	  string  |
| - - abbreviation  	|	    	|	  string  |
| - - enterpriseIndustry  	|	    	|	  object  |
| - - - objectid  	|	    	|	  number  |
| - - - type  	|	    	|	  string  |
| - - - name  	|	    	|	  string  |
| - - scale  	|	    	|	  number  |
| - - type  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - - contacts  	|	    	|	  string  |
| - - deleteFlag  	|	    	|	  number  |
| - - memo  	|	    	|	    |
| - - createDate  	|	    	|	  string  |
| pagesize  	|	  每页显示条数  	|	  number  |
| total  	|	    	|	  number  |
| page  	|	  页码  	|	  number  |
## 模块名称: 汇智卡管理
### 页面名称: 统计
#### 接口名称:汇智卡主帐户统计
- 接口地址:/Payment/PrimeCardStatistic
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| startTime  	|	  开始时间，格式yyyyMMdd  	|	  string  |
| endTime  	|	  结束时间，yyyyMMdd  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| signData  	|	    	|	  string  |
| tranData  	|	    	|	  object  |
| - endTime  	|	    	|	  string  |
| - counts  	|	  主账户统计充值总笔数  	|	  string  |
| - starTime  	|	    	|	  string  |
| - sums  	|	  主账户统计充值总金额  	|	  string  |
#### 接口名称:沉寂卡总数统计
- 接口地址:/Payment/SleepCardStatistic
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| endTime  	|	  结束时间，yyyyMMdd  	|	  string  |
| startTime  	|	  开始时间，yyyyMMdd  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| tranData  	|	    	|	  object  |
| - unActiveCardNum  	|	    	|	  number  |
| - silenceCardNum  	|	    	|	  number  |
| - activeCardNum  	|	    	|	  number  |
| - normalCardNum  	|	    	|	  number  |
| signData  	|	    	|	  string  |
#### 接口名称:电子钱包统计
- 接口地址:/Payment/EWalletStatistic
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| endTime  	|	  结束时间，yyyyMMdd  	|	  string  |
| startTime  	|	  开始时间，yyyyMMdd  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| signData  	|	    	|	  string  |
| tranData  	|	    	|	  object  |
| - endTime  	|	    	|	  string  |
| - sums  	|	  消费总金额  	|	  string  |
| - counts  	|	  消费次数  	|	  string  |
| - startTime  	|	    	|	  string  |
#### 接口名称:汇智卡总数统计
- 接口地址:/Payment/TotalCardStatistic
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| signData  	|	    	|	  string  |
| tranData  	|	    	|	  object  |
| - cardnum  	|	  卡片总数量  	|	  string  |
#### 接口名称:金额预警提醒
- 接口地址:/Payment/AlertInfomation
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| dAmount  	|	  电子钱包金额  	|	  string  |
| zAmount  	|	  主账号金额  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| signData  	|	    	|	  string  |
| tranData  	|	    	|	  object  |
| - dcards  	|	    	|	  array<object>  |
| - - zAmount  	|	    	|	  number  |
| - - cardno  	|	  用户卡号  	|	  string  |
| - - type  	|	  1：主账号超额，2：电子钱包余额不足  	|	  string  |
| - - dAmount  	|	    	|	  number  |
| - zcards  	|	    	|	  array<object>  |
| - - cardno  	|	    	|	  string  |
| - - zAmount  	|	    	|	  number  |
| - - type  	|	    	|	  string  |
| - - dAmount  	|	    	|	  number  |
### 页面名称: 支付
#### 接口名称:交易明细查询(废弃)
- 接口地址:/Payment/QueryTransaction
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| cardNo  	|	  汇智 卡号  	|	  string  |
| startTime  	|	    	|	  string  |
| startDate  	|	    	|	  string  |
| endDate  	|	    	|	  string  |
| tranType  	|	  交易类型00 查询所有交易类型 02 圈存 07 电子钱包脱机消费 26 现金充值 25 现金充值撤  	|	  string  |
| endTime  	|	    	|	  string  |
| queryNum  	|	  查询笔数最多30笔  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	    	|	  string  |
| msg  	|	    	|	  object  |
| -   	|	  total  	|	  number  |
| - items  	|	    	|	  array<object>  |
| - - Tran_amount  	|	  交易金额  	|	  string  |
| - - Merchant_id  	|	  商户ID  	|	  string  |
| - - Host_tran_ls  	|	  交易流水号  	|	  string  |
| - - Tran_type  	|	  交易类型  	|	  string  |
| - - Merchant_name  	|	  商户名称  	|	  string  |
| - - Tran_date  	|	  交易日期  	|	  string  |
| - - Terminal_id  	|	  终端号  	|	  string  |
#### 接口名称:余额查询&绑定解绑(废弃)
- 接口地址:/Payment/QueryBindCard
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| password  	|	  汇智 卡密码  	|	  string  |
| cardNo  	|	  汇智卡号  	|	  string  |
| authCode  	|	  验证码  	|	  string  |
| queryFlag  	|	  是否需要密码(0代表解绑，1代表绑定，2已绑定)  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	    	|	  string  |
| msg  	|	    	|	  array<object>  |
| - UserName  	|	  用户姓名  	|	  string  |
| - ExpireDate  	|	  卡有效期  	|	  string  |
| - EpBalance  	|	  电子钱包余额  	|	  string  |
| - EdBalance  	|	  主账户余额  	|	  string  |
#### 接口名称:支付(废弃)
- 接口地址:/Payment/PayTransaction
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| orderAmt  	|	  订单金额  	|	  string  |
| cardNo  	|	  汇智卡号  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	    	|	  string  |
| msg  	|	    	|	  object  |
| - merSignMsg  	|	  验证签名字符串  	|	  string  |
| - postUrl  	|	  跳转的URL  	|	  string  |
| - tranDataBase64  	|	  加密交易数据  	|	  string  |
#### 接口名称:计算折扣金额
- 接口地址:/BizPayment/CardReChargeDiscount
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| orderAmtCount  	|	  订单金额  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	  返回的折扣金额  	|	  string  |
| result  	|	  true  	|	  string  |
#### 接口名称:发送充值验证码
- 接口地址:/BizPayment/SendRechargePhoneMessage
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	    	|	  boolean  |
| msg  	|	    	|	  string  |
### 页面名称: 后台接口
#### 接口名称:汇智卡充值操作列表
- 接口地址:/Setting/RechargeManage/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - amount  	|	    	|	  number  |
| - cardNo  	|	    	|	  string  |
| - createTime  	|	    	|	  number  |
| - checkGet  	|	  1未领发票2已领发票  	|	  number  |
| - id  	|	    	|	  number  |
| - checkCode  	|	  短信验证码  	|	  string  |
| - orderNo  	|	    	|	  string  |
| - account  	|	    	|	  string  |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
| page  	|	    	|	  number  |
#### 接口名称:领取发票
- 接口地址:/Setting/RechargeManage/GetCheck/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:汇智卡操作日志
- 接口地址:/Setting/PayLog/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| endTime  	|	    	|	  string  |
| startTime  	|	    	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - ipAddr  	|	    	|	  string  |
| - mobile  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - cardNo  	|	    	|	  string  |
| - createTime  	|	    	|	  string  |
| - type  	|	  1充值支付2绑定3历史查询  	|	  number  |
#### 接口名称:获取在线充值记录列表
- 接口地址:/Setting/PayLogById/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - type  	|	    	|	  number  |
| - objectid  	|	    	|	  number  |
| - mobile  	|	    	|	    |
| - status  	|	  交易状态(1成功0失败)  	|	  boolean  |
| - actualCharge  	|	    	|	  number  |
| - tranTime  	|	  充值时间  	|	  string  |
| - cardNo  	|	  汇智卡号  	|	  string  |
| - verifyCode  	|	  验证码  	|	  string  |
| - ipAddr  	|	    	|	  string  |
| - discountAmt  	|	    	|	  number  |
| - orderNo  	|	  订单号  	|	  string  |
| - tranSerialNo  	|	    	|	  string  |
| - memo  	|	    	|	  string  |
| - member  	|	    	|	  string  |
| - createTime  	|	    	|	  string  |
| - orderAmt  	|	  充值金额  	|	  string  |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
## 模块名称: 工作流引擎
### 页面名称: 业务流转操作
#### 接口名称:获取下一步处理人员信息
- 接口地址:api/user/task/list/{id}/{page}/{pageSize}
- 接口类型:GET
- 接口描述:id: 选中的下一步任务的id
{page}：页码
{pageSize}：每页显示条数
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - is_active  	|	    	|	  boolean  |
| - id  	|	    	|	  number  |
| - last_login  	|	    	|	  string  |
| - date_joined  	|	    	|	  string  |
| - last_name  	|	    	|	  string  |
| - is_staff  	|	    	|	  boolean  |
| - email  	|	    	|	  string  |
| - resource_uri  	|	    	|	  string  |
| - first_name  	|	    	|	  string  |
| - password  	|	    	|	  string  |
| - is_superuser  	|	    	|	  boolean  |
| - username  	|	    	|	  string  |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
| total  	|	    	|	  number  |
#### 接口名称:发起人撤销
- 接口地址:/workflow/api/usercallback
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| id  	|	  我的业务接口id（pid）  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:执行流转(新建流程)
- 接口地址:/workflow/api/newTransfer
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| *identity_field_value  	|	  业务表主键  	|	  number  |
| task_user  	|	  下一步执行者  	|	  string  |
| *sn  	|	  编号  	|	  string  |
| *related_table  	|	  业务表  	|	  string  |
| *title  	|	  标题  	|	    |
| *creator  	|	  创建者  	|	  string  |
| *process_id  	|	  工作流定义  	|	  string  |
| task_id  	|	  下一步任务  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:获取下一步所有任务节点
- 接口地址:/workflow/api/tasks/next/list/{id}/{page}/{pageSize}
- 接口类型:GET
- 接口描述:id: 当前task节点id
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - is_print  	|	    	|	  boolean  |
| - name  	|	    	|	  string  |
| - duedate  	|	    	|	  number  |
| - opinion_name  	|	    	|	  string  |
| - process  	|	    	|	  object  |
| - - is_inside  	|	    	|	  boolean  |
| - - roleurl  	|	    	|	    |
| - - is_active  	|	    	|	  boolean  |
| - - related_table  	|	    	|	  string  |
| - - userurl  	|	    	|	    |
| - - version  	|	    	|	  string  |
| - - modifytime  	|	    	|	  string  |
| - - createview  	|	    	|	  string  |
| - - alias  	|	    	|	  string  |
| - - id  	|	    	|	  number  |
| - - printview  	|	    	|	  string  |
| - - addtime  	|	    	|	  string  |
| - - name  	|	    	|	  string  |
| - - resource_uri  	|	    	|	  string  |
| - - editview  	|	    	|	  string  |
| - - sn_template  	|	    	|	  string  |
| - - is_completed  	|	    	|	  boolean  |
| - - identityfield  	|	    	|	  string  |
| - is_circularize  	|	    	|	  boolean  |
| - attached  	|	    	|	  string  |
| - is_active  	|	    	|	  boolean  |
| - readable_fields  	|	    	|	  string  |
| - is_use_default_transfer  	|	    	|	  boolean  |
| - addtime  	|	    	|	  string  |
| - resource_uri  	|	    	|	  string  |
| - logic  	|	    	|	  string  |
| - writeable_fields  	|	    	|	  string  |
| - id  	|	    	|	  number  |
| - circle_expression  	|	    	|	    |
| - is_grab  	|	    	|	  boolean  |
| - circle_roles  	|	    	|	    |
| - modifytime  	|	    	|	  string  |
| - assign_roles  	|	    	|	  string  |
| - nodetype  	|	    	|	  string  |
| total  	|	    	|	  number  |
| page  	|	    	|	  number  |
#### 接口名称:执行流转(已有流程)
- 接口地址:/workflow/api/HadTransfer
- 接口类型:POST
- 接口描述:选择去向和下一步执行者之后的保存事件.
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| *currenttask  	|	  当前业务任务id  	|	  number  |
| sn  	|	  编号  	|	  string  |
| *title  	|	  标题  	|	    |
| *task_id  	|	  下一步任务  	|	  string  |
| *id  	|	  key  	|	  number  |
| *task_user  	|	  下一步执行者  	|	  string  |
| *process_id  	|	  工作流定义  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 工作流列表上的操作
#### 接口名称:新增业务
- 接口地址:/workflow/api/create/{id}
- 接口类型:GET
- 接口描述:｛id｝：process
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| process  	|	  工作流定义id  	|	  string  |
| task_id  	|	  下一步任务id  	|	  number  |
| related_table  	|	    	|	  string  |
| task_user  	|	  下一步用户id  	|	  string  |
| view  	|	    	|	  string  |
| is_grab  	|	  是否抢单  	|	  boolean  |
#### 接口名称:修改业务
- 接口地址:/workflow/api/edit/{id}
- 接口类型:GET
- 接口描述:id:task_instance_id 待办/经办列表的主键
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| task_id  	|	  下一步任务id  	|	  number  |
| view  	|	  业务视图地址  	|	  string  |
| currenttask  	|	  当前业务任务节点id  	|	  number  |
| process  	|	  工作流定义id  	|	  number  |
| task_user  	|	  下一步用户id  	|	  string  |
| is_grab  	|	  是否抢单  	|	  boolean  |
| id  	|	  工作流业务实例id  	|	  number  |
#### 接口名称:获取业务
- 接口地址:/workflow/api/get/{id}
- 接口类型:GET
- 接口描述:id:task_instance_id 待办/经办列表的主键
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| is_grab  	|	  是否抢单  	|	  boolean  |
| process  	|	  工作流定义id  	|	  number  |
| view  	|	  业务视图地址  	|	  string  |
| task_id  	|	  下一步任务id  	|	  number  |
| id  	|	  工作流业务实例id  	|	  number  |
| task_user  	|	  下一步用户id  	|	  string  |
| currenttask  	|	  当前业务任务节点id  	|	  number  |
### 页面名称: 业务页面
#### 接口名称:提交审批意见
- 接口地址:/workflow/api/taskinstances/{id}/
- 接口类型:POST
- 接口描述:id: ti_id
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  意见  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:显示历史审批意见
- 接口地址:/workflow/api/taskinstances/history/opinion/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| piid  	|	    	|	  string  |
| tid  	|	    	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objects  	|	    	|	  array<object>  |
| - task  	|	    	|	  string  |
| - task_user  	|	  操作人  	|	  string  |
| - task_name  	|	  任务名称  	|	  string  |
| - addtime  	|	    	|	  string  |
| - complete_date  	|	  完成时间  	|	  string  |
| - content  	|	  审批意见  	|	  string  |
| - modifytime  	|	    	|	  string  |
| - opinion_name  	|	  意见名称  	|	  string  |
| - display_state  	|	  中文状态  	|	  string  |
| - id  	|	    	|	  number  |
| - duetime  	|	  期限  	|	  string  |
| - is_due  	|	  是否超期  	|	  boolean  |
| - state  	|	  状态  	|	  string  |
| meta  	|	    	|	  object  |
| - previous  	|	    	|	    |
| - next  	|	    	|	    |
| - limit  	|	    	|	  number  |
| - total_count  	|	    	|	  number  |
| - offset  	|	    	|	  number  |
## 模块名称: 统计数据
### 页面名称: 商户统计
#### 接口名称:商户分类统计
- 接口地址:/Setting/Merchant/StatisticsGraphData
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| GetMerchantSaleByType  	|	  优惠商户  	|	  array<object>  |
| - value  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| TotalMerchants  	|	  商户总数  	|	  number  |
| GetMerchantByPark  	|	  园区商户  	|	  array<object>  |
| - value  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| GetMerchantByType  	|	  商户类别  	|	  array<object>  |
| - value  	|	    	|	  string  |
| - name  	|	    	|	  string  |
### 页面名称: 公共服务平台
#### 接口名称:逾期统计-时间段
- 接口地址:/workflow/api/duestatdate
- 接口类型:GET
- 接口描述:｛beginDate｝:起始时间
{endDate}：结束时间
{query｝类型参数  a：全部，1：物业，2：it,3：企业培训，4：猎聘申请，5：教室预订，6：会议室预订，7：广告服务申请，8：测试申请，9：著作权登记，10：入驻申请
{flag}：统计类型 d：按天 m：按月
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| query  	|	    	|	  string  |
| flag  	|	    	|	  string  |
| endDate  	|	    	|	  string  |
| beginDate  	|	    	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:逾期统计-总数
- 接口地址:/workflow/api/duestatcount
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	  累计  	|	  number  |
| total_day  	|	  当日  	|	  number  |
| total_month  	|	  当月  	|	  number  |
#### 接口名称:服务统计-时间段
- 接口地址:/workflow/api/pistatdate
- 接口类型:GET
- 接口描述:{beginDate}:起始时间
{endDate}：结束时间
{query｝类型参数  a：全部，1：物业，2：it,3：企业培训，4：猎聘申请，5：教室预订，6：会议室预订，7：广告服务申请，8：测试申请，9：著作权登记，10：入驻申请
{flag}：统计类型 d：按天 m：按月
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| xAxis  	|	    	|	  object  |
| - data  	|	    	|	  array<string>  |
| series  	|	    	|	  object  |
| - data  	|	    	|	  array<string>  |
#### 接口名称:服务统计-总数
- 接口地址:/workflow/api/pistatcount
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total_month  	|	  当月  	|	  number  |
| total_day  	|	  当日  	|	  number  |
| total  	|	  累计  	|	  number  |
#### 接口名称:评价数据表
- 接口地址:/Comment/StatisticsData/{beginDate}/{endDate}/{query}/{flag}
- 接口类型:GET
- 接口描述:{beginDate}:起始时间
{endDate}：结束时间
{query}：类型参数  a：全部，1：物业，2：it,3：企业培训，4：猎聘申请，5：教室预订，6：会议室预订，7：广告服务申请，8：测试申请，9：著作权登记，10：入驻申请
{flag}：统计方式 d：按天   m：按月
返回数据中 series中按照顺序分别是  评价次数／服务态度／响应速度／服务质量
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| query  	|	  类型参数  	|	  string  |
| endDate  	|	  结束时间  	|	  string  |
| flag  	|	  统计方式  	|	  string  |
| beginDate  	|	  起始时间  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| quality  	|	    	|	  number  |
| duration  	|	    	|	  number  |
| objectid  	|	    	|	  number  |
| attitude  	|	    	|	  number  |
| content  	|	    	|	  string  |
| repairId  	|	    	|	  number  |
| serialNumber  	|	    	|	  string  |
| commentType  	|	    	|	  number  |
| createTime  	|	    	|	  object  |
| userid  	|	    	|	  string  |
#### 接口名称:评价次数统计
- 接口地址:Comment/StatisticsNumber
- 接口类型:GET
- 接口描述:[[3,4.3333,5.0000,5.0000]]

按顺序 累计评价数    平均服务态度   平均服务质量   平均响应速度
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
## 模块名称: 商户中心
### 页面名称: 获取商户中心信息
#### 接口名称:获取商户中心信息
- 接口地址:/Setting/Merchant/GetMerchantCenterInfo
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| saleList  	|	    	|	  object  |
| hitCount  	|	  商户点击量  	|	  number  |
| status  	|	  商户状态（1启用 -1禁用）  	|	  number  |
| sDict  	|	    	|	  object  |
| - objectid  	|	    	|	  number  |
| - orderFlag  	|	    	|	  number  |
| - type  	|	    	|	  string  |
| - settingDict  	|	    	|	    |
| - english  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| - parentid  	|	    	|	  number  |
| workStartTime  	|	  营业开始时间  	|	  string  |
| introduce  	|	  详细介绍  	|	  string  |
| createTime  	|	    	|	  string  |
| userC  	|	    	|	  object  |
| - deleteFlag  	|	    	|	  number  |
| - enabled  	|	    	|	  boolean  |
| - userFace  	|	    	|	  string  |
| - phone  	|	    	|	  string  |
| - username  	|	    	|	  string  |
| - birthday  	|	    	|	  string  |
| - realName  	|	    	|	  string  |
| - sex  	|	    	|	  number  |
| - userFlag  	|	    	|	  number  |
| - email  	|	    	|	    |
| - name  	|	    	|	  string  |
| activity  	|	  商户热度  	|	  number  |
| isInvite  	|	  是否特邀商户（0否1是）  	|	  number  |
| isCheck  	|	  是否审核通过（1:待审核;2通过3不通过）  	|	  number  |
| isNew  	|	  是否新商户  	|	  number  |
| thumbList  	|	    	|	  object  |
| objectid  	|	  流水号  	|	  number  |
| childType  	|	  商户子分类  	|	  number  |
| memo  	|	  审核未通过原因  	|	  string  |
| type  	|	  商户分类  	|	  number  |
| avar  	|	  商户头像  	|	  string  |
| parkType  	|	  园区商业类型  	|	  number  |
| name  	|	  商户名称  	|	  string  |
| parkName  	|	    	|	  object  |
| - objectid  	|	    	|	  number  |
| - memo  	|	    	|	    |
| - updateDate  	|	    	|	  string  |
| - createDate  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| username  	|	  商户用户名  	|	  string  |
| workEndTime  	|	  营业结束时间  	|	  string  |
| address  	|	  商户地址  	|	  string  |
| park  	|	  所属园区  	|	  number  |
| shortIntro  	|	  商户基本介绍  	|	  string  |
| phone  	|	  商户电话  	|	  string  |
| mEvaluate  	|	  商户相关指数的平均值  	|	  object  |
| - mMerchant  	|	    	|	    |
| - cCustomer  	|	    	|	  object  |
| - env  	|	    	|	  number  |
| - createTime  	|	    	|	    |
| - service  	|	    	|	  number  |
| - merchant  	|	    	|	  number  |
| - customer  	|	    	|	    |
| - comment  	|	    	|	    |
| - objectid  	|	    	|	  number  |
| - taste  	|	    	|	  number  |
| - replyList  	|	    	|	    |
#### 接口名称:商户优惠操作
- 接口地址:/Setting/MerchantSale/BanSet
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| isBan  	|	  1启用2禁用  	|	  number  |
| objectid  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:获取商户类别
- 接口地址:/Setting/Merchant/GetMenuList/{parentid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| parentid  	|	  父类ID  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| parentid  	|	    	|	  number  |
| english  	|	    	|	  string  |
| type  	|	    	|	  string  |
| name  	|	    	|	  string  |
| orderFlag  	|	    	|	  number  |
| settingDict  	|	    	|	    |
| objectid  	|	    	|	  number  |
#### 接口名称:商户点击量
- 接口地址:Merchant/Hit/{objectid}
- 接口类型:GET
- 接口描述:可通过hitCount进行商户排序
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:获取商户中心首页分类商户数据(前4条 )
- 接口地址:/Setting/Merchant/GetClassifiedProducts
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| play  	|	    	|	  object  |
| - value  	|	    	|	  array<object>  |
| - - coverImg  	|	    	|	  string  |
| - - origin  	|	    	|	  number  |
| - - typeEn  	|	    	|	  string  |
| - - companyId  	|	    	|	  number  |
| - - startTime  	|	    	|	  string  |
| - - isCheck  	|	    	|	  number  |
| - - endTime  	|	    	|	  string  |
| - - current  	|	    	|	  number  |
| - - isBan  	|	    	|	  number  |
| - - memo  	|	    	|	  string  |
| - - type  	|	    	|	  number  |
| - - discount  	|	    	|	  number  |
| - - title  	|	    	|	  string  |
| - - publishDate  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| children  	|	    	|	  object  |
| - value  	|	    	|	  object  |
| food  	|	    	|	  object  |
| - value  	|	    	|	  array<object>  |
| - - endTime  	|	    	|	  string  |
| - - type  	|	    	|	  number  |
| - - memo  	|	    	|	  string  |
| - - coverImg  	|	    	|	  string  |
| - - origin  	|	    	|	  number  |
| - - startTime  	|	    	|	  string  |
| - - title  	|	    	|	  string  |
| - - publishDate  	|	    	|	  string  |
| - - isBan  	|	    	|	  number  |
| - - companyId  	|	    	|	  number  |
| - - isCheck  	|	    	|	  number  |
| - - typeEn  	|	    	|	  string  |
| - - current  	|	    	|	  number  |
| - - objectid  	|	    	|	  number  |
| - - discount  	|	    	|	  number  |
| shopping  	|	    	|	  object  |
| - value  	|	    	|	  array<object>  |
| - - discount  	|	    	|	  number  |
| - - endTime  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - origin  	|	    	|	  number  |
| - - isBan  	|	    	|	  number  |
| - - coverImg  	|	    	|	  string  |
| - - type  	|	    	|	  number  |
| - - title  	|	    	|	  string  |
| - - typeEn  	|	    	|	  string  |
| - - publishDate  	|	    	|	  string  |
| - - current  	|	    	|	  number  |
| - - isCheck  	|	    	|	  number  |
| - - memo  	|	    	|	  string  |
| - - companyId  	|	    	|	  number  |
| - - startTime  	|	    	|	  string  |
| living  	|	    	|	  object  |
| - value  	|	    	|	  object  |
## 模块名称: 系统邮箱
### 页面名称: 工具
#### 接口名称:邮件绑定
- 接口地址:/SysEmail/SendAuthEmail
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:发送邮件
- 接口地址:/SysEmail/SendEmail
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| receiver  	|	  接收人邮件地址  	|	  string  |
| mailContent  	|	  邮件内容  	|	  string  |
| mailTitle  	|	  邮件标题  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
### 页面名称: 配置
#### 接口名称:获取配置
- 接口地址:/Setting/EmailConfig/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| account  	|	  帐号  	|	  string  |
| hostName  	|	  邮件服务器  	|	  string  |
| password  	|	  密码  	|	  string  |
| enable  	|	  1启用2禁用  	|	  number  |
| objectid  	|	    	|	  number  |
#### 接口名称:添加更新配置
- 接口地址:/Setting/EmailConfig/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| account  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| enable  	|	    	|	  number  |
| password  	|	    	|	  string  |
| hostName  	|	    	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
## 模块名称: 后台管理
### 页面名称: 角色权限配置
#### 接口名称:查看配置
- 接口地址:
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:删除配置
- 接口地址:
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:新增配置
- 接口地址:
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新配置
- 接口地址:
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:配置列表
- 接口地址:
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 权限管理
#### 接口名称:编辑权限
- 接口地址:/Setting/User/Permission/Edit/{objectid}
- 接口类型:GET
- 接口描述:{objectid}: 权限编号
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| id  	|	  序号  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| name  	|	  权限名称  	|	  string  |
| url  	|	  权限地址  	|	  string  |
| memo  	|	  备注  	|	  string  |
| objectid  	|	  编号  	|	  number  |
#### 接口名称:权限列表
- 接口地址:/Setting/User/Permission/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}：页码
{pageSize}：每页显示条数
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  权限结果集  	|	  array<object>  |
| - url  	|	  权限地址  	|	  string  |
| - objectid  	|	  编号  	|	  number  |
| - name  	|	  权限名称  	|	  string  |
| - memo  	|	  备注  	|	  string  |
| pagesize  	|	  每页显示条数  	|	  number  |
| total  	|	  总条数  	|	  number  |
| page  	|	  页码  	|	  number  |
#### 接口名称:删除权限
- 接口地址:/Setting/User/Permission/Delete/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| id  	|	  序号  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:添加权限
- 接口地址:/Setting/User/Permission/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新权限
- 接口地址:/Setting/User/Permission/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 关于我们、联系我们
#### 接口名称:列表
- 接口地址:/Setting/SettingAbout/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}：页码
{pageSize}：每页显示条数
传参数判断类型 eg：关于我们列表传参数    ?flag=1
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页显示条数  	|	  number  |
| page  	|	  页码  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - richtextid  	|	  富文本ID  	|	  number  |
| - createDate  	|	  创建时间  	|	  string  |
| - flag  	|	  类型标示（1-关于我们，2-联系我们）  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - settingRichText  	|	  富文本对象  	|	  object  |
| - - pic  	|	  封面图片  	|	  string  |
| - - objectid  	|	  ID  	|	  number  |
| - - deleteFlag  	|	  是否禁用（默认0，-1删除）  	|	  number  |
| - - content  	|	  内容  	|	  string  |
| - - memo  	|	  备注信息  	|	  string  |
| - - author  	|	  作者  	|	  string  |
| - - updateDate  	|	  更新时间  	|	  string  |
| - - title  	|	  标题  	|	  string  |
| - - createDate  	|	  创建时间  	|	  string  |
| - objectid  	|	  主键ID  	|	  number  |
| total  	|	  总条数  	|	  number  |
#### 接口名称:查看
- 接口地址:/Setting/SettingAbout/Edit/{objectid}
- 接口类型:POST
- 接口描述:{objectid}：主键ID
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| createDate  	|	  创建时间  	|	  string  |
| objectid  	|	  主键ID  	|	  number  |
| richtextid  	|	  富文本ID  	|	  number  |
| settingRichText  	|	  富文本对象  	|	  object  |
| - title  	|	  标题  	|	  string  |
| - deleteFlag  	|	  -1标示禁用  	|	  number  |
| - pic  	|	  封面图片  	|	  string  |
| - content  	|	  内容  	|	  string  |
| - objectid  	|	  ID  	|	  number  |
| - memo  	|	  备注  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - author  	|	  作者  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| flag  	|	  标识符（1-关于我们，2-联系我们）  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
#### 接口名称:更新
- 接口地址:/Setting/SettingAbout/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| richtextid  	|	  富文本ID  	|	  number  |
| objectid  	|	  主键ID  	|	  number  |
| flag  	|	  标识符（1-关于我们，2-联系我们）  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 维修工程师管理
#### 接口名称:获取工种列表
- 接口地址:/SettingMaintainer/JobType/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - type  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| total  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
#### 接口名称:编辑维修工程师
- 接口地址:/Setting/SettingMaintainer/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:新增维修工程师
- 接口地址:/Setting/SettingMaintainer/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| mobile  	|	  手机  	|	  string  |
| repairType  	|	  1:物业维修;2:网络维修  	|	  number  |
| realName  	|	  真实姓名  	|	  string  |
| jobType  	|	  工种ID数组  	|	  string  |
| department  	|	  所属部门  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看维修工程师列表
- 接口地址:/Setting/SettingMaintainer/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| repairType  	|	  1:物业维修 2:IT维修  	|	  number  |
| name  	|	  姓名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - objectid  	|	    	|	  number  |
| - name  	|	  姓名  	|	  string  |
| - mobile  	|	  手机号  	|	  string  |
| - jobType  	|	  工种ID  	|	  array<string>  |
| - repairType  	|	  0:物业维修 1:IT维修  	|	  number  |
#### 接口名称:修改维修工程师
- 接口地址:/Setting/SettingMaintainer/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| jobType  	|	    	|	  string  |
| mobile  	|	    	|	  string  |
| realName  	|	    	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:删除维修工程师
- 接口地址:/Setting/SettingMaintainer/Delete/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:按角色获取工程师列表
- 接口地址:/Setting/SettingMaintainer/Role/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| reapirType  	|	  1 物业维修 2IT维修  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| repairType  	|	    	|	  number  |
| jobType  	|	    	|	  array<string>  |
| department  	|	  部门  	|	  string  |
| name  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| mobile  	|	    	|	  string  |
### 页面名称: 法定节假日
#### 接口名称:是否休息日
- 接口地址:/Setting/CheerDay/IsCheerDay
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:更新法定节假日
- 接口地址:/Setting/CheerDay/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| cheerDay  	|	  法定节假日  	|	  string  |
| objectid  	|	  主键id  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看法定节假日
- 接口地址:/Setting/CheerDay/Edit/{objectid}
- 接口类型:GET
- 接口描述:{objectid}:主键id
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	  主键id  	|	  number  |
| cheerDay  	|	  法定节假日  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
#### 接口名称:法定节假日列表
- 接口地址:/Setting/CheerDate/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}:每页显示条数
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页显示条数  	|	  number  |
| page  	|	  页码  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - objectid  	|	  主键id  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - cheerDay  	|	  法定节假日  	|	  string  |
| total  	|	  总记录数  	|	  number  |
#### 接口名称:删除法定节假日
- 接口地址:/Setting/CheerDay/Delete/{objectid}
- 接口类型:GET
- 接口描述:{objectid}:主键id
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:新增法定节假日
- 接口地址:/Setting/CheerDay/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| cheerDay  	|	  法定节假日  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 注册会员管理
#### 接口名称:禁用、启用用户
- 接口地址:/Setting/User/EnableUser/{username}/{flag}
- 接口类型:GET
- 接口描述:{username}：用户登录名
{flag}：0-禁用用户  1-启用用户
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:重置内部会员登录密码
- 接口地址:/Setting/User/ResetPassword/{username}
- 接口类型:GET
- 接口描述:{username}:内部用户登录名
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
### 页面名称: 企业
#### 接口名称:重置企业管理员密码
- 接口地址:/Setting/Enterprise/RestEnterpriseAdminPassword/{enterpriseId}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:禁用启用企业
- 接口地址:/Setting/Enterprise/EnableEnterprise/{enterpriseId}/{flag}
- 接口类型:GET
- 接口描述:{enterpriseId}:企业编号ID
{flag}：0-禁用，1-启用
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	  消息  	|	  string  |
| success  	|	  状态  	|	  boolean  |
#### 接口名称:查看企业信息
- 接口地址:/Setting/Enterprise/Edit/{objectid}
- 接口类型:GET
- 接口描述:{objectid}：企业编号
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| enterpriseIndustry  	|	  所属行业  	|	  object  |
| - name  	|	  所属行业名称  	|	  string  |
| - objectid  	|	  ID  	|	  number  |
| - type  	|	  数据字典类型  	|	  string  |
| enterpriseScale  	|	  企业规模  	|	  object  |
| - name  	|	  企业规模名称  	|	  string  |
| - type  	|	  数据字典类型  	|	  string  |
| - objectid  	|	  ID  	|	  number  |
| contactsinfo  	|	  企业联系方式  	|	  string  |
| industry  	|	  所属行业ID(数据字典)  	|	  number  |
| objectid  	|	  企业ID  	|	  number  |
| memo  	|	  备注  	|	  string  |
| type  	|	  企业类型ID（数据字典）  	|	  number  |
| enterpriseType  	|	  企业类型  	|	  object  |
| - name  	|	  企业类型名称  	|	  string  |
| - objectid  	|	  ID  	|	  number  |
| - type  	|	  数据字典类型  	|	  string  |
| scale  	|	  企业规模ID（数据字典）  	|	  number  |
| intake  	|	  入驻年ID（数据字典）  	|	  string  |
| deleteFlag  	|	  企业禁用启用（1-默认启用，-1-禁用）  	|	  number  |
| address  	|	  企业地址  	|	  string  |
| username  	|	  企业用户名  	|	  string  |
| abbreviation  	|	  企业简称  	|	  string  |
| name  	|	  企业名称  	|	  string  |
| contacts  	|	  企业联系人  	|	  string  |
#### 接口名称:添加企业信息
- 接口地址:/Setting/Enterprise/Add
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| type  	|	  *企业类型ID（数据字典）  	|	  number  |
| intake  	|	  *入驻年（数据字典）  	|	    |
| address  	|	  *企业地址[100]  	|	  string  |
| memo  	|	  备注[255]  	|	  string  |
| contacts  	|	  *企业联系人[20]  	|	  string  |
| username  	|	  *企业用户名[20](8-20位英文和数字组合，首字母必须为英文字母，特殊符号不可用)  	|	  string  |
| contactsinfo  	|	  *企业联系方式[20]  	|	  string  |
| scale  	|	  *企业规模ID（数据字典）  	|	  number  |
| name  	|	  *企业名称[100]  	|	  string  |
| industry  	|	  *所属行业ID(数据字典)  	|	  number  |
| deleteFlag  	|	  企业禁用启用（1-默认启用，-1-禁用）  	|	  number  |
| abbreviation  	|	  企业简称[50]  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:修改企业信息
- 接口地址:/Setting/Enterprise/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| deleteFlag  	|	  企业禁用启用（1-默认启用，-1-禁用）  	|	  number  |
| contacts  	|	  企业联系人[20]  	|	  string  |
| username  	|	  企业用户名[20](8-20位英文和数字组合，首字母必须为英文字母，特殊符号不可用)  	|	  string  |
| abbreviation  	|	  企业简称[50]  	|	  string  |
| scale  	|	  企业规模ID（数据字典）  	|	  number  |
| memo  	|	  备注[255]  	|	  string  |
| intake  	|	  入驻年（数据字典）  	|	  string  |
| type  	|	  企业类型ID（数据字典）  	|	  number  |
| contactsinfo  	|	  企业联系方式[20]  	|	  string  |
| name  	|	  企业名称[100]  	|	  string  |
| industry  	|	  所属行业ID(数据字典)  	|	  number  |
| objectid  	|	  企业ID  	|	  number  |
| address  	|	  企业地址[100]  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:企业列表
- 接口地址:/Setting/Enterprise/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}:第几页
{pageSize}：每页显示条数
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页显示条数  	|	  number  |
| total  	|	  总条数  	|	  number  |
| result  	|	  企业列表结果集  	|	  array<object>  |
| - scale  	|	  企业规模ID  	|	  number  |
| - contactsinfo  	|	  企业联系方式  	|	  string  |
| - memo  	|	  备注  	|	  string  |
| - objectid  	|	  企业编号  	|	  number  |
| - parentid  	|	  父级企业ID  	|	  number  |
| - type  	|	  企业类型ID  	|	  number  |
| - industry  	|	  所属行业ID  	|	  number  |
| - enterpriseIndustry  	|	  企业所属行业  	|	  object  |
| - - objectid  	|	  编号  	|	  number  |
| - - name  	|	  所属行业名称  	|	  string  |
| - - type  	|	  字典类型  	|	  string  |
| - enterpriseType  	|	  企业类型  	|	  object  |
| - - name  	|	  企业类型名称  	|	  string  |
| - - type  	|	  字典类型  	|	  string  |
| - - objectid  	|	  编号  	|	  number  |
| - address  	|	  企业地址  	|	  string  |
| - name  	|	  企业名称  	|	  string  |
| - abbreviation  	|	  企业简称  	|	  string  |
| - deleteFlag  	|	  企业禁用启用（1-默认启用，-1-禁用）  	|	  number  |
| - contacts  	|	  企业联系人  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - enterpriseScale  	|	  企业规模  	|	  object  |
| - - type  	|	  字典类型  	|	  string  |
| - - objectid  	|	  编号  	|	  number  |
| - - name  	|	  规模名  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - enterprise  	|	  父级企业信息  	|	  object  |
| page  	|	  第几页  	|	  number  |
### 页面名称: 富文本管理
#### 接口名称:富文本列表
- 接口地址:/Setting/RichText/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}：页码
{pageSize}：每页显示条数
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - pic  	|	  封面图片  	|	  string  |
| - objectid  	|	  富文本ID  	|	  number  |
| - deleteFlag  	|	  禁用标识符（默认1-启用，-1-禁用）  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - title  	|	  富文本标题  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - content  	|	  内容  	|	  string  |
| - author  	|	  作者  	|	  string  |
| - memo  	|	  备注信息  	|	  string  |
| page  	|	  页码  	|	  number  |
| total  	|	  总记录数  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
#### 接口名称:新增富文本
- 接口地址:/Setting/RichText/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| memo  	|	  备注信息  	|	  string  |
| deleteFlag  	|	  禁用标识符（默认1-启用，-1-禁用）  	|	  number  |
| pic  	|	  封面图片[255]  	|	  string  |
| author  	|	  作者[20]  	|	  string  |
| content  	|	  *内容  	|	  string  |
| title  	|	  *标题[50]  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:删除富文本
- 接口地址:/Setting/RichText/Delete/{objectid}
- 接口类型:GET
- 接口描述:{objectid}:富文本ID
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:禁用、启用富文本
- 接口地址:/Setting/RichText/EnableRichText/{objectid}/{flag}
- 接口类型:GET
- 接口描述:{objectid}：富文本ID
{flag}：0-禁用 1-启用
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:更新富文本
- 接口地址:/Setting/RichText/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| title  	|	  富文本标题  	|	  string  |
| pic  	|	  封面图片  	|	  string  |
| memo  	|	  备注  	|	  string  |
| content  	|	  内容  	|	  string  |
| deleteFlag  	|	  禁用标识符（默认1-启用，-1-禁用）  	|	  number  |
| author  	|	  作者  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看富文本
- 接口地址:/Setting/RichText/Edit/{objectid}
- 接口类型:GET
- 接口描述:{objectid}:富文本ID
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  内容  	|	  string  |
| deleteFlag  	|	  禁用标识符（默认1-启用，-1-禁用）  	|	  number  |
| title  	|	  富文本标题  	|	  string  |
| author  	|	  作者  	|	  string  |
| objectid  	|	  富文本ID  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| memo  	|	  备注信息  	|	  string  |
| pic  	|	  封面图片  	|	  string  |
#### 接口名称:根据标题查看富文本详情
- 接口地址:/Setting/RichText/ByTitle/Edit?title=title1
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| createDate  	|	  创建时间  	|	  string  |
| objectid  	|	  富文本ID  	|	  number  |
| title  	|	  富文本标题  	|	  string  |
| author  	|	  作者  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| memo  	|	  备注信息  	|	  string  |
| content  	|	  内容  	|	  string  |
| deleteFlag  	|	  禁用标识符（默认1-启用，-1-禁用）  	|	  number  |
| pic  	|	  封面图片  	|	  string  |
#### 接口名称:富文本上传附件
- 接口地址:/FileUpload/UploadDocSpecial
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 服务信息
#### 接口名称:添加服务信息
- 接口地址:/Setting/ServiceInfo/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| url  	|	  跳转地址[255]  	|	  string  |
| pic  	|	  图片地址[255]  	|	  string  |
| parentid  	|	  *父级ID（为0时表示顶级）  	|	  number  |
| name  	|	  *名称[50]  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:服务信息列表
- 接口地址:/Setting/ServiceInfo/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}：页码
{pageSize}：每页显示条数
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  页码  	|	  number  |
| result  	|	  服务信息结果集  	|	  array<object>  |
| - name  	|	  名称  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - pic  	|	  图标  	|	    |
| - serviceInfo  	|	  父级服务信息信息  	|	    |
| - url  	|	  跳转URL  	|	    |
| - createDate  	|	  创建时间  	|	  string  |
| - objectid  	|	  编号  	|	  number  |
| - parentid  	|	  父级ID  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
| total  	|	  总条数  	|	  number  |
#### 接口名称:修改服务信息
- 接口地址:/Setting/ServiceInfo/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pic  	|	  图片地址  	|	  string  |
| objectid  	|	  编号  	|	  number  |
| url  	|	  跳转URL  	|	  string  |
| name  	|	  名称  	|	  string  |
| parentid  	|	  父级ID  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:编辑服务信息
- 接口地址:/WSP/Setting/ServiceInfo/Edit/{objectid}
- 接口类型:GET
- 接口描述:{objectid}：服务信息ID
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pic  	|	  图片地址  	|	  string  |
| url  	|	  跳转URL  	|	  string  |
| name  	|	  名称  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| parentid  	|	  父级ID  	|	  number  |
| serviceInfo  	|	  父级服务信息  	|	  object  |
| objectid  	|	  编号  	|	  number  |
#### 接口名称:删除服务信息
- 接口地址:/Setting/ServiceInfo/Delete/{objectid}
- 接口类型:GET
- 接口描述:{objectid}：服务信息编号
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
### 页面名称: 帮助中心
#### 接口名称:获取子项菜单列表
- 接口地址:/Setting/SettingHelp/ChildLevel/{parentid}
- 接口类型:GET
- 接口描述:{parentid}：父级id
响应参数详见 顶级菜单
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| parentid  	|	  父级id  	|	  number  |
#### 接口名称:顶级菜单列表
- 接口地址:/Setting/SettingHelp/TopLevel
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| settingRichText  	|	  富文本对象  	|	  object  |
| - objectid  	|	  富文本id  	|	  number  |
| - createDate  	|	  创建时间  	|	  string  |
| - memo  	|	  备注  	|	  string  |
| - title  	|	  标题  	|	  string  |
| - pic  	|	  封面图片  	|	  string  |
| - author  	|	  作者  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - deleteFlag  	|	  禁用标示（1-启用，－1-禁用）  	|	  number  |
| - content  	|	  内容  	|	  string  |
| title  	|	  标题  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| richTextId  	|	  富文本id  	|	  number  |
| parentid  	|	  父级id  	|	  number  |
| deleteFlag  	|	  禁用标志（1-启用，－1禁用）  	|	  number  |
| objectid  	|	  主键id  	|	  number  |
| createDate  	|	  创建时间  	|	  string  |
| type  	|	  前后台表示（1-前台，2-后台）  	|	  number  |
### 页面名称: 附件
#### 接口名称:富文本上传图片
- 接口地址:/FileUpload/UploadImgSpecial
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:简单文件上传-文档
- 接口地址:/FileUpload/SimpleUploadDocFile
- 接口类型:POST
- 接口描述:简单文件上传
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| type  	|	  文件类型  	|	  string  |
| file  	|	  文件对象  	|	  object  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	  消息  	|	  string  |
| success  	|	  结果代码  	|	  number  |
#### 接口名称:普通多图片上传
- 接口地址:/FileUpload/UploadImgComm
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| file  	|	    	|	  string  |
| file  	|	    	|	  string  |
| file  	|	    	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| path  	|	    	|	  array<string>  |
| success  	|	    	|	  boolean  |
| msg  	|	    	|	  string  |
#### 接口名称:简单文件上传-图片
- 接口地址:/FileUpload/SimpleUploadFile
- 接口类型:POST
- 接口描述:简单文件上传
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| file  	|	  文件对象  	|	  object  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	  结果代码  	|	  number  |
| msg  	|	  消息  	|	  string  |
### 页面名称: 广告管理
#### 接口名称:查看广告(可匿名)
- 接口地址:/Setting/LivingCenterAdver/Edit/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	    	|	  number  |
| merchant  	|	    	|	  number  |
| title  	|	    	|	  string  |
| imgUrl  	|	    	|	  string  |
| content  	|	    	|	  string  |
#### 接口名称:获取广告列表(可匿名)
- 接口地址:/Setting/LivingCenterAdver/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - orderA  	|	  显示顺序  	|	  number  |
| - merchantId  	|	  商户id  	|	  number  |
| - title  	|	  广告标题  	|	  string  |
| - linkA  	|	  广告图片链接地址  	|	  string  |
| - isBan  	|	  是否禁用(1禁用2启用)  	|	  number  |
| - objectid  	|	    	|	  number  |
| - imgUrl  	|	  图片地址  	|	  string  |
| - isShow  	|	  是否显示(1仅显示图片2显示链接3显示富文本)  	|	  number  |
| - content  	|	  广告内容  	|	  string  |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
#### 接口名称:禁用启用
- 接口地址:/Setting/LivingCenterAdver/isBan
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| isBan  	|	  1禁用2启用  	|	  number  |
| objectid  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:删除广告
- 接口地址:/Setting/LivingCenterAdver/Delete/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新广告
- 接口地址:/Setting/LivingCenterAdver/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:新增广告
- 接口地址:/Setting/LivingCenterAdver/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 数据字典管理
#### 接口名称:编辑数据字典信息
- 接口地址:/Setting/SettingDict/Edit/{objectid}
- 接口类型:GET
- 接口描述:{objectid}:数据字典编号
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| type  	|	  数据字典类型  	|	  string  |
| name  	|	  数据字典名称  	|	  string  |
| objectid  	|	  编号  	|	  number  |
#### 接口名称:删除数据字典信息
- 接口地址:/Setting/SettingDict/Delete/{objectid}
- 接口类型:GET
- 接口描述:{objectid}：数据字典编号
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:根据父级id返回数据字典信息
- 接口地址:/Setting/SettingDict/ParentId/｛page｝/｛pagesize｝/｛parentid｝
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - objectid  	|	  主键id  	|	  number  |
| - type  	|	  类型  	|	  string  |
| - settingDict  	|	  父级对象  	|	  object  |
| - - objectid  	|	  主键id  	|	  number  |
| - - settingDict  	|	  父级对象  	|	    |
| - - name  	|	  名称  	|	  string  |
| - - type  	|	  类型  	|	  string  |
| - - parentid  	|	  父级id  	|	  number  |
| - name  	|	  名称  	|	  string  |
| - parentid  	|	  父级id  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
| page  	|	  页码  	|	  number  |
| total  	|	  总记录数  	|	  number  |
#### 接口名称:修改数据字典信息
- 接口地址:/Setting/SettingDict/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| type  	|	  数据字典类型  	|	  string  |
| name  	|	  数据字典名称  	|	  string  |
| objectid  	|	  数据字典ID  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:添加数据字典
- 接口地址:/Setting/SettingDict/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| type  	|	  *数据字典类型[20]  	|	  string  |
| parentid  	|	  添加顶级的时候传0,  	|	  string  |
| name  	|	  *数据字典名称[50]  	|	  string  |
| orderFlag  	|	  排序  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:数据字典列表
- 接口地址:/Setting/SettingDict/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}:每页条数
数据字典类型（education-学历，nation-国籍，enterpriseType-企业类型，enterpriseScale-企业规模，years-年代，department-部门，worktype－工种,grade-培训等级,workYears-工作年限,jobTitle-目前职位,moneyType-薪资类型,businessType-业务方向,cultivateProject-培训项目, copyrightType-申请类别,
    period-登记周期）
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页条数  	|	  number  |
| page  	|	  页码  	|	  number  |
| total  	|	  总记录数  	|	  number  |
| result  	|	  返回数据字典结果集  	|	  array<object>  |
| - name  	|	  名称  	|	  string  |
| - objectid  	|	  编号  	|	  number  |
| - type  	|	  数据字典类型（education-学历，nation-国籍，enterpriseType-企业类型）  	|	  string  |
#### 接口名称:数据字典顶级分类接口
- 接口地址:/Setting/SettingDict/Top/List{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	  总记录数  	|	  number  |
| page  	|	  页码  	|	  number  |
| result  	|	  返回数据字典结果集  	|	  array<object>  |
| - type  	|	  数据字典类型（education-学历，nation-国籍，enterpriseType-企业类型）  	|	  string  |
| - name  	|	  名称  	|	  string  |
| - objectid  	|	  编号  	|	  number  |
| pagesize  	|	  每页条数  	|	  number  |
#### 接口名称:报修类型列表
- 接口地址:/Setting/SettingDict/ParentIdExceptTop/List/0/0/?type=repairsCommFlag
- 接口类型:GET
- 接口描述:{page}:页码
{pageSize}:每页条数
数据字典类型（education-学历，nation-国籍，enterpriseType-企业类型，enterpriseScale-企业规模，years-年代，department-部门，worktype－工种,grade-培训等级,workYears-工作年限,jobTitle-目前职位,moneyType-薪资类型,businessType-业务方向,cultivateProject-培训项目, copyrightType-申请类别,
    period-登记周期）
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  返回数据字典结果集  	|	  array<object>  |
| - name  	|	  名称  	|	  string  |
| - objectid  	|	  编号  	|	  number  |
| - type  	|	  数据字典类型（education-学历，nation-国籍，enterpriseType-企业类型）  	|	  string  |
| page  	|	  页码  	|	  number  |
| pagesize  	|	  每页条数  	|	  number  |
| total  	|	  总记录数  	|	  number  |
### 页面名称: 业务处理
#### 接口名称:新增派工
- 接口地址:/UserRepairAssign/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| arriveTime  	|	  到达时间  	|	  number  |
| repairId  	|	  报修id  	|	  number  |
| assignTime  	|	  派工时间  	|	  number  |
| engineer  	|	  工程师id  	|	  number  |
| objectid  	|	  派工id  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:新增维修记录
- 接口地址:/UserRepairRecode/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| repairId  	|	  维修Id  	|	  number  |
| objectid  	|	  主键  	|	  number  |
| memo  	|	  维修内容  	|	  string  |
| deadline  	|	  期限时间  	|	  string  |
| createTime  	|	  维修时间  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看派工列表
- 接口地址:/UserRepairAssign/{page}/{pageSize}
- 接口类型:GET
- 接口描述:@type=array_map;返回派工列表。
若请求增加参数repairId（报修的id），则返回该次报修的派工列表
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	    	|	  number  |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - objectid  	|	  派工id  	|	  number  |
| - engineerDetail  	|	  工程师详情  	|	  object  |
| - - mobile  	|	  手机  	|	  string  |
| - - repairType  	|	  维修类型  	|	  number  |
| - - name  	|	  姓名  	|	  string  |
| - - objectid  	|	  派工Id  	|	  number  |
| - - jobType  	|	  工种  	|	  array<string>  |
| - engineer  	|	  工程师id  	|	  number  |
| - repairDetail  	|	  维修详情  	|	  object  |
| - - voiceUrl  	|	  声音  	|	    |
| - - applicant  	|	  报修人  	|	    |
| - - parkId  	|	  园区  	|	  number  |
| - - description  	|	  报修描述  	|	  string  |
| - - repairTypeConfm  	|	  报修类型修正  	|	  number  |
| - - descriptionConfm  	|	  描述修正  	|	    |
| - - contact  	|	  联系方式  	|	  string  |
| - - serialNumber  	|	  流水号  	|	  string  |
| - - buildingId  	|	  大楼  	|	  number  |
| - - company  	|	  公司  	|	  string  |
| - - acceptDate  	|	  受理时间  	|	    |
| - - typeId  	|	  1：物业；2：IT  	|	  number  |
| - - photoUrl  	|	  照片  	|	    |
| - - address  	|	  报修地址  	|	  string  |
| - - createDate  	|	  保修时间  	|	    |
| - - memo  	|	  备注  	|	  string  |
| - - objectid  	|	  维修id  	|	  number  |
| - - repairType  	|	  报修类型  	|	  number  |
| - assignTime  	|	  派工时间  	|	  number  |
| - arriveTime  	|	  到达时间  	|	    |
| - repairId  	|	  维修id  	|	  number  |
| pagesize  	|	    	|	  number  |
#### 接口名称:查看维修记录
- 接口地址:/UserRepairRecode/{page}/{pageSize}
- 接口类型:GET
- 接口描述:请求参数repairId(报修id)返回本次报修的记录
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - repairId  	|	  维修id  	|	  number  |
| - deadline  	|	  超期时间  	|	  number  |
| - createTime  	|	  维修时间  	|	  number  |
| - objectid  	|	  主键  	|	  number  |
| - memo  	|	  维修记录  	|	  string  |
| - userRepair  	|	  维修的详情  	|	  object  |
| - - descriptionConfm  	|	    	|	    |
| - - voiceUrl  	|	    	|	    |
| - - company  	|	    	|	  string  |
| - - buildingId  	|	    	|	  number  |
| - - applicant  	|	    	|	    |
| - - objectid  	|	    	|	  number  |
| - - createDate  	|	    	|	    |
| - - memo  	|	    	|	  string  |
| - - acceptDate  	|	    	|	    |
| - - contact  	|	    	|	  string  |
| - - repairTypeConfm  	|	    	|	  number  |
| - - parkId  	|	    	|	  number  |
| - - serialNumber  	|	    	|	  string  |
| - - repairType  	|	    	|	  number  |
| - - address  	|	    	|	  string  |
| - - photoUrl  	|	    	|	    |
| - - typeId  	|	    	|	  number  |
| - - description  	|	    	|	  string  |
| total  	|	    	|	  number  |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
#### 接口名称:更新派工
- 接口地址:/UserRepairAssign/Update
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| arriveTime  	|	  到达时间  	|	  number  |
| repairId  	|	  报修id  	|	  number  |
| engineer  	|	  工程师id  	|	  number  |
| objectid  	|	  派工id  	|	  number  |
| assignTime  	|	  派工时间  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新维修记录
- 接口地址:/UserRepairRecode/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| createTime  	|	  维修时间  	|	  number  |
| deadline  	|	  维修期限  	|	  number  |
| memo  	|	  维修内容  	|	  string  |
| objectid  	|	  主键  	|	  number  |
| repairId  	|	  维修id  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 报修设置
#### 接口名称:编辑基本设置
- 接口地址:/Setting/SettingRepairBase/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| baseTerm  	|	  服务须知id  	|	  number  |
| baseEnable  	|	  服务状态(1启用0禁用)  	|	  number  |
| baseTermName  	|	  服务须知名称  	|	  string  |
| repairType  	|	  1:物业维修;2:网络维修  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:修改自动分配配置
- 接口地址:/Setting/SettingAutoDispatch/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| parkId  	|	  园区ID  	|	  number  |
| acceptorId  	|	  角色ID  	|	  string  |
| objectid  	|	  唯一标识  	|	  number  |
| buildingId  	|	  楼宇ID  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:修改有效期设置
- 接口地址:/Setting/SettingRepairValidDate/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| workEndTime  	|	  工作日结束时间  	|	  string  |
| nonWorkStartTime  	|	  非工作日开始时间  	|	  string  |
| objectid  	|	  1代表物业2代表网络  	|	  number  |
| workStartTime  	|	  工作日开始时间  	|	  string  |
| alertInfo  	|	  无效提示  	|	  string  |
| nonWorkEndTime  	|	  非工作日结束时间  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:修改基本设置
- 接口地址:/Setting/SettingRepairBase/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| baseTerm  	|	  服务须知路径  	|	  string  |
| baseEnable  	|	  服务状态(1启用0禁用)  	|	  number  |
| objectid  	|	  1:物业维修;2:网络维修  	|	  number  |
| repairType  	|	  1:物业维修;2:网络维修  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:编辑有效期设置
- 接口地址:/Setting/SettingRepairValidDate/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| workStartTime  	|	  工作日开始时间  	|	  string  |
| objectid  	|	  报修设置id  	|	  number  |
| workEndTime  	|	  工作日结束时间  	|	  string  |
| alertInfo  	|	  无效提示  	|	  string  |
| repairType  	|	  1:物业维修;2:网络维修  	|	  number  |
| nonWorkStartTime  	|	  非工作日开始时间  	|	  string  |
| nonWorkEndTime  	|	  非工作日结束时间  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:编辑自动分配配置
- 接口地址:/Setting/SettingAutoDispatch/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| acceptorId  	|	  维修人员ID  	|	  number  |
| parkId  	|	  园区ID  	|	  number  |
| buildingId  	|	  楼宇ID  	|	  number  |
#### 接口名称:编辑评价设置
- 接口地址:/Setting/SettingRepairComment/Edit/{objectid}
- 接口类型:GET
- 接口描述:obecjtid : 1代表物业报修设置  2代表网络报修设置
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| autoCommentDeadline  	|	  自动评价期限(换算成分钟)  	|	  number  |
| autoCommentInfo  	|	  评价信息  	|	  string  |
| autoCommentQuality  	|	  服务质量  	|	  number  |
| autoCommentAttitude  	|	  服务态度  	|	  number  |
| autoCommentSpeed  	|	  响应速度  	|	  number  |
#### 接口名称:获取自动分配配置列表
- 接口地址:/Setting/SettingAutoDispatch/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| parkName  	|	  园区名称  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
| total  	|	    	|	  number  |
| page  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - parkName  	|	    	|	  string  |
| - buildingId  	|	    	|	  number  |
| - acceptor  	|	    	|	  string  |
| - acceptorId  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - parkId  	|	    	|	  number  |
| - buildingName  	|	    	|	  string  |
#### 接口名称:修改评价设置
- 接口地址:/Setting/SettingRepairComment/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| autoCommentInfo  	|	    	|	  string  |
| autoCommentAttitude  	|	    	|	  number  |
| autoCommentDeadline  	|	    	|	  number  |
| objectid  	|	    	|	  number  |
| autoCommentQuality  	|	    	|	  number  |
| autoCommentSpeed  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 附件管理
#### 接口名称:更新附件信息
- 接口地址:/Attachment/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看附件信息
- 接口地址:/Attachment/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:删除附件
- 接口地址:/Attachment/Delete/{objectid}
- 接口类型:GET
- 接口描述:{objectid}:附件ID
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:附件列表
- 接口地址:/Attachment/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 素材管理
#### 接口名称:删除素材
- 接口地址:/Attachment/Delete/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:素材列表
- 接口地址:/Attachment/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看素材
- 接口地址:/Attachment/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新素材
- 接口地址:/Attachment/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 商户评价
#### 接口名称:评价禁用设置
- 接口地址:/Setting/MerchantReplySet/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  内容  	|	  string  |
| objectid  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	    	|	  boolean  |
| msg  	|	    	|	  string  |
#### 接口名称:评价列表(可匿名)
- 接口地址:/Setting/MerchantEvaluate/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| username  	|	  如果需要按用户返回  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	    	|	  number  |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - customer  	|	  客户ID  	|	  string  |
| - service  	|	  服务评分  	|	  number  |
| - taste  	|	  品味评分  	|	  number  |
| - merchant  	|	  商户ID  	|	  number  |
| - objectid  	|	    	|	  number  |
| - comment  	|	    	|	  string  |
| - env  	|	  环境评分  	|	  number  |
| - createTime  	|	  时间  	|	  string  |
| pagesize  	|	    	|	  number  |
#### 接口名称:商户回复禁用
- 接口地址:/Setting/MerchantReply/BlockReply
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	    	|	    |
| blockStr  	|	  提示内容  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:获取评价禁用内容
- 接口地址:/Setting/MerchantReplySet/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	  设置为1即可  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:删除评价
- 接口地址:/Setting/MerchantEvaluate/Delete/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:商户回复列表
- 接口地址:/Setting/MerchantReply/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| startTime  	|	    	|	    |
| endTime  	|	    	|	    |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - eid  	|	    	|	  number  |
| - createTime  	|	    	|	  string  |
| - content  	|	    	|	  string  |
| - isBlock  	|	    	|	  number  |
| - objectid  	|	    	|	  number  |
| - mMerchant  	|	    	|	  object  |
| - - username  	|	    	|	  string  |
| - - type  	|	    	|	  number  |
| - - isNew  	|	    	|	  number  |
| - - thumbList  	|	    	|	  array<object>  |
| - - - objectid  	|	    	|	  number  |
| - - - mid  	|	    	|	  number  |
| - - - createTime  	|	    	|	  string  |
| - - - url  	|	    	|	  string  |
| - - createTime  	|	    	|	  string  |
| - - mEvaluate  	|	    	|	    |
| - - park  	|	    	|	  number  |
| - - shortIntro  	|	    	|	  string  |
| - - userC  	|	    	|	  object  |
| - - - sex  	|	    	|	  number  |
| - - - username  	|	    	|	  string  |
| - - - userFlag  	|	    	|	  number  |
| - - - name  	|	    	|	  string  |
| - - - birthday  	|	    	|	  string  |
| - - - email  	|	    	|	  string  |
| - - - userFace  	|	    	|	  string  |
| - - - phone  	|	    	|	  string  |
| - - - deleteFlag  	|	    	|	  number  |
| - - - realName  	|	    	|	  string  |
| - - - enabled  	|	    	|	  boolean  |
| - - activity  	|	    	|	  number  |
| - - workEndTime  	|	    	|	  string  |
| - - address  	|	    	|	  string  |
| - - parkType  	|	    	|	  number  |
| - - isCheck  	|	    	|	  number  |
| - - objectid  	|	    	|	  number  |
| - - avar  	|	    	|	  string  |
| - - memo  	|	    	|	  string  |
| - - introduce  	|	    	|	  string  |
| - - sDict  	|	    	|	  object  |
| - - - type  	|	    	|	  string  |
| - - - name  	|	    	|	  string  |
| - - - orderFlag  	|	    	|	  number  |
| - - - english  	|	    	|	  string  |
| - - - parentid  	|	    	|	  number  |
| - - - settingDict  	|	    	|	    |
| - - - objectid  	|	    	|	  number  |
| - - saleList  	|	    	|	    |
| - - name  	|	    	|	  string  |
| - - phone  	|	    	|	  string  |
| - - parkName  	|	    	|	  object  |
| - - - memo  	|	    	|	    |
| - - - objectid  	|	    	|	  number  |
| - - - name  	|	    	|	  string  |
| - - - createDate  	|	    	|	  string  |
| - - - updateDate  	|	    	|	  string  |
| - - workStartTime  	|	    	|	  string  |
| - - isInvite  	|	    	|	  number  |
| - - status  	|	    	|	  number  |
| - bContent  	|	    	|	  string  |
| - merchant  	|	    	|	  number  |
| total  	|	    	|	  number  |
### 页面名称: 商户管理
#### 接口名称:删除商户
- 接口地址:/Setting/Merchant/Delete/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:新增优惠缩略图
- 接口地址:/Setting/MerchantSaleThumbs/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| url  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| createTime  	|	    	|	  string  |
| mid  	|	  商户ID  	|	  number  |
#### 接口名称:更新优惠
- 接口地址:/Setting/MerchantSale/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| discount  	|	    	|	  number  |
| publishDate  	|	    	|	  string  |
| coverImg  	|	    	|	  string  |
| type  	|	  折扣(441)原价(442)其它(443)  	|	  number  |
| origin  	|	    	|	  number  |
| title  	|	    	|	  string  |
| endTime  	|	    	|	  string  |
| shortIntro  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| memo  	|	    	|	  string  |
| startTime  	|	    	|	  string  |
| current  	|	    	|	  number  |
| content  	|	    	|	  string  |
| companyId  	|	    	|	  number  |
| bCheck  	|	  0:待审核;1:审核通过;2:审核不通过  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:删除优惠缩略图
- 接口地址:/Setting/MerchantSaleThumbs/Delete/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:获取优惠信息LIST搜索菜单(可匿名)
- 接口地址:/Setting/MerchantSale/GetSearchMenu
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| label  	|	    	|	  string  |
| fieldName  	|	    	|	  string  |
| child  	|	    	|	  array<object>  |
| - parentid  	|	    	|	  number  |
| - type  	|	    	|	  string  |
| - english  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - settingDict  	|	    	|	    |
| - orderFlag  	|	    	|	  number  |
| - name  	|	    	|	  string  |
#### 接口名称:优惠列表(可匿名)
- 接口地址:/Setting/MerchantSale/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| username  	|	  根据用户名查找  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - endTime  	|	  结束时间  	|	  string  |
| - content  	|	    	|	  string  |
| - origin  	|	  原价  	|	  number  |
| - startTime  	|	  开始时间  	|	  string  |
| - shortIntro  	|	  简介  	|	  string  |
| - type  	|	  折扣(441)原价(442)其它(443)  	|	  string  |
| - coverImg  	|	  封面图片  	|	  string  |
| - title  	|	  标题  	|	  string  |
| - memo  	|	  审核不通过原因  	|	  string  |
| - publishDate  	|	  发布时间  	|	  string  |
| - discount  	|	  折扣  	|	  number  |
| - bCheck  	|	  1:待审核;2:审核通过;3:审核不通过  	|	  number  |
| - objectid  	|	    	|	  number  |
| - companyId  	|	  企业ID  	|	  number  |
| - current  	|	  现价  	|	  number  |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
#### 接口名称:获取生活中心搜索菜单(可匿名)
- 接口地址:/Setting/Merchant/GetSearchLivingMenu
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| label  	|	    	|	  string  |
| fieldName  	|	    	|	  string  |
| child  	|	    	|	  array<object>  |
| - objectid  	|	    	|	  number  |
| - english  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| - orderFlag  	|	    	|	  number  |
| - parentid  	|	    	|	  number  |
| - type  	|	    	|	  string  |
#### 接口名称:获取商户LIST左侧菜单(可匿名)
- 接口地址:/Setting/Merchant/GetSearchMenu
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| child  	|	  数据字典数据结构  	|	  array<object>  |
| - orderFlag  	|	    	|	  number  |
| - objectid  	|	  查询字段对应的值  	|	  number  |
| - name  	|	  查询字段中文名称  	|	  string  |
| - settingDict  	|	    	|	    |
| - english  	|	    	|	  string  |
| - type  	|	    	|	  string  |
| - parentid  	|	    	|	  number  |
| fieldName  	|	  查询字段  	|	  string  |
| label  	|	  标签  	|	  string  |
#### 接口名称:商户热度(废弃)
- 接口地址:/Setting/Merchant/Activity
- 接口类型:POST
- 接口描述:商户热度
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| activity  	|	  热度值  	|	  number  |
| objectid  	|	  对象id  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:商户列表(可匿名)
- 接口地址:/Setting/Merchant/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:商户列表
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| discountType  	|	  折扣类型  	|	  number  |
| paramHit  	|	  点击量排序(true降序 false升序)  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - workStartTime  	|	  营业结束时间  	|	  string  |
| - address  	|	  商户地址  	|	  string  |
| - shortIntro  	|	  简短介绍  	|	  string  |
| - isCheck  	|	  是否审核通过；1:待审核;2通过3不通过  	|	  number  |
| - phone  	|	  商户电话  	|	  string  |
| - avar  	|	  商户头像  	|	  string  |
| - objectid  	|	    	|	  number  |
| - memo  	|	  审核不通过原因  	|	  string  |
| - type  	|	  商户类型  	|	  number  |
| - name  	|	  商户名称  	|	  string  |
| - status  	|	  禁用状态  	|	  number  |
| - parkType  	|	  园区类别  	|	  string  |
| - workEndTime  	|	  营业开始时间  	|	  string  |
| - activity  	|	  商户热度  	|	  number  |
| - childType  	|	  商户子类型  	|	  number  |
#### 接口名称:新增优惠
- 接口地址:/Setting/MerchantSale/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| current  	|	    	|	  number  |
| publishDate  	|	    	|	  string  |
| discount  	|	    	|	  number  |
| memo  	|	    	|	  string  |
| content  	|	    	|	  string  |
| startTime  	|	    	|	  string  |
| shortIntro  	|	    	|	  string  |
| title  	|	    	|	  string  |
| companyId  	|	    	|	  number  |
| endTime  	|	    	|	  string  |
| type  	|	  折扣(441)原价(442)其它(443)  	|	  string  |
| bCheck  	|	    	|	  number  |
| coverImg  	|	    	|	  string  |
| origin  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:新增商户
- 接口地址:/Setting/Merchant/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| address  	|	    	|	  string  |
| isCheck  	|	    	|	  number  |
| shortIntro  	|	  商户基本介绍  	|	  string  |
| type  	|	    	|	  number  |
| workStartTime  	|	    	|	  string  |
| workEndTime  	|	    	|	  string  |
| introduce  	|	  商户详细介绍  	|	  string  |
| memo  	|	  审核未通过原因  	|	  string  |
| name  	|	    	|	  string  |
| avar  	|	    	|	  string  |
| phone  	|	    	|	  string  |
| username  	|	  登录用户名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新商户
- 接口地址:/Setting/Merchant/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| shortIntro  	|	    	|	  string  |
| workEndTime  	|	    	|	  string  |
| name  	|	    	|	  string  |
| phone  	|	    	|	  string  |
| address  	|	    	|	  string  |
| objectid  	|	  必填项  	|	  number  |
| username  	|	  必填项  	|	    |
| memo  	|	    	|	  string  |
| introduce  	|	    	|	  string  |
| type  	|	  必填项  	|	  string  |
| bCheck  	|	    	|	  number  |
| workStartTime  	|	    	|	  string  |
| avar  	|	    	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:优惠审核
- 接口地址:/Setting/MerchantSale/Audit
- 接口类型:POST
- 接口描述:优惠审核
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| memo  	|	  不通过的原因  	|	  string  |
| objectid  	|	  对象id  	|	  number  |
| bCheck  	|	  1:待审核;2:审核通过;3:审核不通过  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:商户审核
- 接口地址:/Setting/Merchant/Audit
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	  对象objectid  	|	  number  |
| memo  	|	  不通过的原因  	|	  string  |
| bCheck  	|	  1:待审核;2:审核通过;3:审核不通过  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看商户(可匿名)
- 接口地址:/Setting/Merchant/Edit/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| address  	|	    	|	  string  |
| phone  	|	    	|	  string  |
| type  	|	    	|	  string  |
| introduce  	|	    	|	  string  |
| bCheck  	|	    	|	  number  |
| memo  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| avar  	|	    	|	  string  |
| workEndTime  	|	    	|	  string  |
| shortIntro  	|	    	|	  string  |
| name  	|	    	|	  string  |
| workStartTime  	|	    	|	  string  |
#### 接口名称:删除优惠
- 接口地址:/Setting/MerchantSale/Delete/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看优惠(可匿名)
- 接口地址:/Setting/MerchantSale/Edit/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| memo  	|	    	|	  string  |
| startTime  	|	    	|	  string  |
| discount  	|	    	|	  number  |
| origin  	|	    	|	  number  |
| current  	|	    	|	  number  |
| publishDate  	|	    	|	  string  |
| endTime  	|	    	|	  string  |
| shortIntro  	|	    	|	  string  |
| type  	|	  折扣(441)原价(442)其它(443)  	|	  string  |
| title  	|	    	|	  string  |
| content  	|	    	|	  string  |
| coverImg  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| bCheck  	|	    	|	  number  |
| companyId  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:获取商户历史数据
- 接口地址:/Setting/Merchant/History/List/{mid}/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| mid  	|	  商户表中的objectid  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - mid  	|	    	|	  number  |
| - type  	|	    	|	  string  |
| - workStartTime  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| - workEndTime  	|	    	|	  string  |
| - createTime  	|	    	|	  string  |
| - isCheck  	|	    	|	  number  |
| - objectid  	|	    	|	  number  |
| - phone  	|	    	|	  string  |
| - address  	|	    	|	  string  |
| page  	|	    	|	  number  |
#### 接口名称:商户状态设置
- 接口地址:/Setting/Merchant/Status
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| status  	|	  1:启用 -1:禁用  	|	  number  |
| objectid  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:商户优惠缩略图列表(可匿名)
- 接口地址:/Setting/MerchantSaleThumbs/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - url  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - mid  	|	  商户ID  	|	  number  |
| - createTime  	|	    	|	  string  |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
| total  	|	    	|	  number  |
### 页面名称: 楼宇管理
#### 接口名称:查看楼宇信息
- 接口地址:/Setting/Building/Edit/{id}
- 接口类型:GET
- 接口描述:{id}:楼宇ID
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| park  	|	  园区对象  	|	  object  |
| - updateDate  	|	  更新时间  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - name  	|	  园区名称  	|	  string  |
| - memo  	|	  备注  	|	    |
| - objectid  	|	  园区ID  	|	  number  |
| name  	|	  楼宇名称  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| memo  	|	  备注  	|	    |
| parkId  	|	  园区ID  	|	  number  |
| objectid  	|	  主键ID  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
#### 接口名称:更新楼宇
- 接口地址:/Setting/Building/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| memo  	|	  备注信息  	|	    |
| parkId  	|	  园区ID  	|	  number  |
| name  	|	  楼宇名称  	|	  string  |
| objectid  	|	  楼宇ID  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:添加楼宇
- 接口地址:/Setting/Building/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| memo  	|	  备注信息[255]  	|	    |
| parkId  	|	  *园区ID  	|	  number  |
| name  	|	  *楼宇名称[20]  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:楼宇列表
- 接口地址:/Setting/Building/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	  总记录数  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - objectid  	|	  楼宇ID  	|	  number  |
| - name  	|	  楼宇名称  	|	  string  |
| - memo  	|	  备注信息  	|	    |
| - parkId  	|	  园区ID  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - park  	|	  园区对象  	|	  object  |
| - - memo  	|	  备注信息  	|	    |
| - - objectid  	|	  园区ID  	|	  number  |
| - - name  	|	  园区名称  	|	  string  |
| - - updateDate  	|	  更新时间  	|	  string  |
| - - createDate  	|	  创建时间  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| page  	|	  页码  	|	  number  |
### 页面名称: 菜单管理
#### 接口名称:获取菜单列表
- 接口地址:/Setting/MenuAdmin/GetRoleMenu
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| name  	|	    	|	  string  |
| icon  	|	    	|	    |
| id  	|	    	|	  number  |
| parent  	|	    	|	  number  |
| url  	|	    	|	    |
#### 接口名称:菜单列表
- 接口地址:/Setting/MenuAdmin/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - id  	|	    	|	  number  |
| - name  	|	    	|	  string  |
| - url  	|	    	|	  string  |
| - icon  	|	    	|	  string  |
| - parent  	|	    	|	  number  |
| page  	|	    	|	  number  |
| total  	|	    	|	  number  |
#### 接口名称:新增菜单
- 接口地址:/Setting/MenuAdmin/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| url  	|	  菜单URL  	|	  string  |
| icon  	|	  图标  	|	  string  |
| parent  	|	  父级菜单ID  	|	  number  |
| name  	|	  菜单名称  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新菜单
- 接口地址:/Setting/MenuAdmin/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| id  	|	    	|	  number  |
| parent  	|	    	|	  number  |
| icon  	|	    	|	  string  |
| name  	|	    	|	  string  |
| url  	|	    	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:删除菜单
- 接口地址:/Setting/MenuAdmin/Delete/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| msg  	|	    	|	  string  |
| success  	|	    	|	  boolean  |
#### 接口名称:查看菜单
- 接口地址:/Setting/MenuAdmin/Edit/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| name  	|	    	|	  string  |
| url  	|	    	|	  string  |
| parent  	|	    	|	  number  |
| id  	|	    	|	  number  |
| icon  	|	    	|	  string  |
### 页面名称: 园区管理
#### 接口名称:添加园区
- 接口地址:/Setting/Park/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| memo  	|	  备注信息[255]  	|	    |
| name  	|	  *园区名称[20]  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:园区列表
- 接口地址:/Setting/Park/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}：页码
{pageSize}：每页条数
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	  总记录数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - updateDate  	|	  更新时间  	|	  string  |
| - objectid  	|	  ID  	|	  number  |
| - name  	|	  园区名称  	|	  string  |
| - memo  	|	  备注信息  	|	    |
| - createDate  	|	  创建时间  	|	  string  |
| pagesize  	|	  每页显示条数  	|	  number  |
| page  	|	  页码  	|	  number  |
#### 接口名称:修改园区
- 接口地址:/Setting/Park/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| memo  	|	  备注信息  	|	    |
| objectid  	|	  园区ID  	|	  string  |
| name  	|	  园区名称  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看园区
- 接口地址:/Setting/Park/Edit/{id}
- 接口类型:GET
- 接口描述:{id}:园区ID
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| createDate  	|	  创建时间  	|	  string  |
| objectid  	|	  主键ID  	|	  number  |
| memo  	|	  备注  	|	    |
| updateDate  	|	  更新时间  	|	  string  |
| name  	|	  园区名称  	|	  string  |
### 页面名称: 角色管理
#### 接口名称:角色列表
- 接口地址:/Setting/User/Role/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:{page}：页码
{pageSize}：每页显示条数
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	  总条数  	|	  number  |
| result  	|	  角色结果集  	|	  array<object>  |
| - name  	|	  角色名称  	|	  string  |
| - rolename  	|	  角色英文名  	|	  string  |
| - permissionList  	|	  权限结果集  	|	  array<object>  |
| - - url  	|	  权限地址  	|	  string  |
| - - memo  	|	  备注  	|	    |
| - - name  	|	  权限名称  	|	  string  |
| - - objectid  	|	  编号  	|	  number  |
| - isShow  	|	  企业管理员是否显示该角色（默认0-不显示，1-显示）  	|	    |
| - memo  	|	  备注  	|	  string  |
| - permissionArray  	|	    	|	    |
| page  	|	  页码  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
#### 接口名称:编辑角色
- 接口地址:/Setting/User/Role/Edit/{rolename}
- 接口类型:GET
- 接口描述:{rolename}:角色英文名
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| isShow  	|	  企业管理员是否显示该角色（默认0-不显示，1-显示）  	|	  string  |
| permissionList  	|	  权限结果集  	|	  array<object>  |
| - memo  	|	  备注  	|	    |
| - name  	|	  权限名称  	|	  string  |
| - url  	|	  权限地址  	|	  string  |
| - objectid  	|	  编号  	|	  number  |
| permissionArray  	|	    	|	    |
| memo  	|	  备注  	|	  string  |
| name  	|	  角色名称  	|	  string  |
| rolename  	|	  角色英文名  	|	  string  |
#### 接口名称:添加角色
- 接口地址:/Setting/User/Role/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| name  	|	  *角色名[50]  	|	  string  |
| isShow  	|	  企业管理员是否显示该角色（默认1-不显示，2-显示）  	|	  string  |
| rolename  	|	  *角色英文名[50]  	|	  string  |
| memo  	|	  备注[200]  	|	  string  |
| permissionArray  	|	  权限列表（逗号隔开）  	|	    |
#### 接口名称:修改角色
- 接口地址:/Setting/User/Role/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| rolename  	|	  角色英文名  	|	  string  |
| isShow  	|	  企业管理员是否显示该角色（默认0-不显示，1-显示）  	|	  string  |
| permissionArray  	|	  权限列表（逗号隔开）  	|	    |
| memo  	|	  备注  	|	  string  |
| name  	|	  角色名  	|	  string  |
#### 接口名称:删除角色
- 接口地址:/Setting/User/Role/Delete/{rolename}
- 接口类型:GET
- 接口描述:{rolename}:角色英文名
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	    	|	  boolean  |
| msg  	|	    	|	  string  |
## 模块名称: 服务中心
### 页面名称: 物业报修
#### 接口名称:查看报修信息
- 接口地址:UserRepair/Edit{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| acceptDate  	|	  受理时间  	|	    |
| typeId  	|	  报修分类  	|	  number  |
| descriptionConfm  	|	  描述确认  	|	    |
| address  	|	  报修地址  	|	  string  |
| commentFlag  	|	  评论标志  	|	  number  |
| voiceUrl  	|	  声音地址  	|	    |
| repairTypeConfmDetail  	|	    	|	  object  |
| - name  	|	  问题确认名称  	|	  string  |
| - type  	|	    	|	  string  |
| - parentid  	|	    	|	  number  |
| - objectid  	|	    	|	  number  |
| - settingDict  	|	    	|	  object  |
| - - parentid  	|	    	|	  number  |
| - - objectid  	|	  问题确认大类id  	|	  number  |
| - - settingDict  	|	    	|	    |
| - - name  	|	  问题确认大类名称  	|	  string  |
| - - type  	|	    	|	  string  |
| repairType  	|	  问题类型id  	|	  number  |
| repairTypeConfm  	|	  问题确认类型id  	|	  number  |
| repairTypeDetail  	|	    	|	  object  |
| - settingDict  	|	    	|	  object  |
| - - parentid  	|	    	|	  number  |
| - - objectid  	|	  问题大类id  	|	  number  |
| - - settingDict  	|	    	|	    |
| - - type  	|	    	|	  string  |
| - - name  	|	  问题类型大类名称  	|	  string  |
| - objectid  	|	    	|	  number  |
| - parentid  	|	    	|	  number  |
| - type  	|	    	|	  string  |
| - name  	|	  问题类型名称  	|	  string  |
| company  	|	  公司  	|	  string  |
| createDate  	|	  报修时间  	|	    |
| parkId  	|	  园区id  	|	  number  |
| serialNumber  	|	  流水号  	|	  string  |
| park  	|	    	|	  object  |
| - updateDate  	|	    	|	  string  |
| - createDate  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - memo  	|	    	|	    |
| - name  	|	  园区名称  	|	  string  |
| applicant  	|	  报修人Id  	|	    |
| building  	|	    	|	  object  |
| - park  	|	    	|	  object  |
| - - name  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - updateDate  	|	    	|	  string  |
| - - createDate  	|	    	|	  string  |
| - - memo  	|	    	|	    |
| - createDate  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - name  	|	  楼宇名称  	|	  string  |
| - memo  	|	    	|	    |
| - parkid  	|	    	|	  number  |
| - updateDate  	|	    	|	  string  |
| objectid  	|	  报修id  	|	  number  |
| contact  	|	  联系方式  	|	  string  |
| buildingId  	|	  楼宇id  	|	  number  |
| description  	|	  描述  	|	  string  |
| photoUrl  	|	  照片地址  	|	    |
| memo  	|	  备注  	|	  string  |
#### 接口名称:查看维修记录
- 接口地址:/UserRepairRecode/{page}/{pageSize}
- 接口类型:GET
- 接口描述:请求参数repairId(报修id)返回本次报修的记录
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - createTime  	|	  维修时间  	|	  number  |
| - deadline  	|	  超期时间  	|	  number  |
| - memo  	|	  维修记录  	|	  string  |
| - objectid  	|	  主键  	|	  number  |
| - userRepair  	|	  维修的详情  	|	  object  |
| - - createDate  	|	    	|	    |
| - - address  	|	    	|	  string  |
| - - parkId  	|	    	|	  number  |
| - - serialNumber  	|	    	|	  string  |
| - - buildingId  	|	    	|	  number  |
| - - company  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - descriptionConfm  	|	    	|	    |
| - - photoUrl  	|	    	|	    |
| - - memo  	|	    	|	  string  |
| - - applicant  	|	    	|	    |
| - - contact  	|	    	|	  string  |
| - - typeId  	|	    	|	  number  |
| - - voiceUrl  	|	    	|	    |
| - - description  	|	    	|	  string  |
| - - repairType  	|	    	|	  number  |
| - - acceptDate  	|	    	|	    |
| - - repairTypeConfm  	|	    	|	  number  |
| - repairId  	|	  维修id  	|	  number  |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
| page  	|	    	|	  number  |
#### 接口名称:查看派工列表
- 接口地址:/UserRepairAssign/{page}/{pageSize}
- 接口类型:GET
- 接口描述:@type=array_map;返回派工列表。
若请求增加参数repairId（报修的id），则返回该次报修的派工列表
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - repairDetail  	|	  维修详情  	|	  object  |
| - - repairTypeConfm  	|	  报修类型修正  	|	  number  |
| - - voiceUrl  	|	  声音  	|	    |
| - - serialNumber  	|	  流水号  	|	  string  |
| - - parkId  	|	  园区  	|	  number  |
| - - objectid  	|	  维修id  	|	  number  |
| - - typeId  	|	  1：物业；2：IT  	|	  number  |
| - - repairType  	|	  报修类型  	|	  number  |
| - - descriptionConfm  	|	  描述修正  	|	    |
| - - acceptDate  	|	  受理时间  	|	    |
| - - createDate  	|	  保修时间  	|	    |
| - - photoUrl  	|	  照片  	|	    |
| - - contact  	|	  联系方式  	|	  string  |
| - - memo  	|	  备注  	|	  string  |
| - - applicant  	|	  报修人  	|	    |
| - - address  	|	  报修地址  	|	  string  |
| - - company  	|	  公司  	|	  string  |
| - - description  	|	  报修描述  	|	  string  |
| - - buildingId  	|	  大楼  	|	  number  |
| - repairId  	|	  维修id  	|	  number  |
| - engineer  	|	  工程师id  	|	  number  |
| - assignTime  	|	  派工时间  	|	  number  |
| - arriveTime  	|	  到达时间  	|	    |
| - engineerDetail  	|	  工程师详情  	|	  object  |
| - - repairType  	|	  维修类型  	|	  number  |
| - - jobType  	|	  工种  	|	  array<string>  |
| - - objectid  	|	  派工Id  	|	  number  |
| - - name  	|	  姓名  	|	  string  |
| - - mobile  	|	  手机  	|	  string  |
| - objectid  	|	  派工id  	|	  number  |
| total  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
### 页面名称: 企业培训
#### 接口名称:查看培训需求项目及人数详细信息
- 接口地址:/EnterpristCultivate/EnterpristCultivateProject/ByEnterpriseCultivateId/List/{cultivateid}
- 接口类型:GET
- 接口描述:{cultivateid}:培训需求项目Objectid
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| project  	|	  培训需求项目对象  	|	  object  |
| - objectid  	|	  主键ID  	|	  number  |
| - english  	|	  英语标示  	|	    |
| - orderFlag  	|	  排序  	|	  number  |
| - type  	|	  数据字典类型  	|	  string  |
| - parentid  	|	  父级id  	|	  number  |
| - name  	|	  中文名称  	|	  string  |
| enterpristCultivateId  	|	  培训需求项目id  	|	  number  |
| createDate  	|	  创建时间  	|	  string  |
| enterpriseCultivate  	|	  培训需求项目对象  	|	  object  |
| - phone  	|	  联系方式  	|	  string  |
| - content  	|	  培训需求描述  	|	  string  |
| - companyAddress  	|	  公司地址  	|	  string  |
| - objectid  	|	  主键ID  	|	  number  |
| - username  	|	  用户登录名  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - company  	|	  公司  	|	  string  |
| - businessType  	|	  业务方向  	|	  string  |
| - serialNumber  	|	  流水号  	|	  string  |
| - chineseName  	|	  用户真实名  	|	  string  |
| - memo  	|	  备注  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - accessory  	|	  附件url  	|	  string  |
| - email  	|	  邮箱  	|	  string  |
| objectid  	|	  主键ID  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| priojectid  	|	  培训需求项目id  	|	  number  |
| peoples  	|	  人数  	|	  string  |
#### 接口名称:添加企业培训
- 接口地址:/EnterpriseCultivate/EnterpriseCultivate/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| email  	|	  邮箱  	|	  string  |
| companyAddress  	|	  公司地址  	|	  string  |
| businessType  	|	  业务方向  	|	  string  |
| content  	|	  培训需求  	|	  string  |
| chineseName  	|	  用户真实名  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| accessory  	|	  附件  	|	  string  |
| priojectidArray  	|	  培训需求项目id  	|	  array<string>  |
| company  	|	  公司名称  	|	  string  |
| peoplesArray  	|	  培训项目人数  	|	  array<string>  |
| memo  	|	  备注  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看培训需求项目列表
- 接口地址:/Setting/SettingDict/cultivateProject/List
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	  主键ID  	|	  string  |
| english  	|	  英文  	|	  string  |
| type  	|	  数据字典类别  	|	  string  |
| name  	|	  中文名  	|	  string  |
| parentid  	|	  父级ID  	|	  string  |
#### 接口名称:查看培训需求项目详细信息
- 接口地址:/EnterpriseCultivate/EnterpriseCultivate/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	  主键ID  	|	  number  |
| createDate  	|	  创建时间  	|	  string  |
| username  	|	  用户名  	|	  string  |
| serialNumber  	|	  流水号  	|	  string  |
| companyAddress  	|	  公司地址  	|	  string  |
| memo  	|	  备注  	|	  string  |
| content  	|	  培训需求描述  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| email  	|	  邮箱  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| chineseName  	|	  用户真实名  	|	  string  |
| company  	|	  公司  	|	  string  |
| accessory  	|	  附件地址  	|	  string  |
| businessType  	|	  业务方向  	|	  string  |
### 页面名称: 著作权登记
#### 接口名称:添加著作权登记
- 接口地址:/Copyright/UserCopyright/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| chineseName  	|	  用户真实名  	|	  string  |
| copyrightBusinessTypeId  	|	  业务类型数据字典objectid  	|	  string  |
| material  	|	  其它材料  	|	  string  |
| copyrightTypeId  	|	  申请类别objectid  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| applyForm  	|	  申请表  	|	  string  |
| periodId  	|	  登记周期objectid  	|	  string  |
| softAbbreviation  	|	  软件简称  	|	  string  |
| company  	|	  公司名  	|	  string  |
| content  	|	  需求说明  	|	  string  |
| copyrightUnit  	|	  申请单位  	|	  string  |
| memo  	|	  备注  	|	  string  |
| softVersion  	|	  软件版本  	|	  string  |
| email  	|	  邮箱  	|	  string  |
| softName  	|	  软件名称  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:著作权登记详情
- 接口地址:/Copyright/UserCopyright/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| softAbbreviation  	|	  软件简称  	|	  string  |
| memo  	|	  备注信息  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| material  	|	  其它材料  	|	  string  |
| company  	|	  公司名  	|	  string  |
| copyrightBusinessTypeId  	|	    	|	  number  |
| periodId  	|	    	|	  number  |
| email  	|	  邮箱  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| content  	|	  需求说明  	|	  string  |
| softVersion  	|	  软件版本  	|	  string  |
| softName  	|	  软件名称  	|	  string  |
| applyForm  	|	  申请表  	|	  string  |
| copyrightType  	|	  申请类别（个人申请、公司申请、联合申请）  	|	  object  |
| - parentid  	|	    	|	  number  |
| - orderFlag  	|	    	|	  number  |
| - settingDict  	|	    	|	    |
| - english  	|	    	|	  string  |
| - type  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - name  	|	  显示值  	|	  string  |
| copyrightTypeId  	|	    	|	  number  |
| copyrightUnit  	|	  申请单位  	|	  string  |
| copyrightBusinessType  	|	    	|	  object  |
| - type  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - english  	|	    	|	    |
| - name  	|	    	|	  string  |
| - settingDict  	|	    	|	    |
| - orderFlag  	|	    	|	  number  |
| - parentid  	|	    	|	  number  |
| createDate  	|	  申请时间  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| serialNumber  	|	  流水号  	|	  string  |
| username  	|	  用户登录名  	|	  string  |
| chineseName  	|	  用户真实名称  	|	  string  |
| period  	|	  登记周期（普通件、45个、35个、25个、20个、15个、10个、6个、4个、3个工作日）  	|	  object  |
| - type  	|	    	|	  string  |
| - settingDict  	|	    	|	    |
| - orderFlag  	|	    	|	  number  |
| - parentid  	|	    	|	  number  |
| - name  	|	  显示值  	|	  string  |
| - english  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
### 页面名称: 在线看房
#### 接口名称:在线房源列表
- 接口地址:/House/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  页码  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - address  	|	  地址  	|	  string  |
| - name  	|	  名称  	|	  string  |
| - deleteFlag  	|	  是否禁用（-1-禁用，1-启用）  	|	  number  |
| - createDate  	|	  创建时间  	|	  string  |
| - objectid  	|	  主键ID  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - pic  	|	  图片地址  	|	  string  |
| - content  	|	  内容  	|	  string  |
| pagesize  	|	  每页条数  	|	  number  |
| total  	|	  总条数  	|	  number  |
#### 接口名称:房源详情
- 接口地址:/House/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| name  	|	  名称  	|	  string  |
| content  	|	  内容  	|	  string  |
| address  	|	  地址  	|	  string  |
| deleteFlag  	|	  否禁用（-1-禁用，1-启用）  	|	  number  |
| objectid  	|	  主键ID  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| pic  	|	  图片  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
#### 接口名称:添加在线看房
- 接口地址:/House/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pic  	|	  图片地址  	|	  string  |
| name  	|	  名称  	|	  string  |
| content  	|	  内容  	|	  object  |
| address  	|	  地址  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 用户须知
#### 接口名称:查看用户须知
- 接口地址:/Setting/Readme/Type/Edit/{type}
- 接口类型:GET
- 接口描述:{type}参数详情

enterpristCultivate： 企业培训须知
userHeadhunting ：猎聘申请培训须知
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| name  	|	  标题  	|	  string  |
| isPopup  	|	  是否弹窗(1-是，-1-否)  	|	  number  |
| createDate  	|	  创建时间  	|	  string  |
| content  	|	  内容  	|	  string  |
| deleteflag  	|	  禁用启用（-1 禁用,1启用）  	|	  number  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| objectid  	|	  主键ID  	|	  number  |
| type  	|	  类型  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
### 页面名称: 账单查询
#### 接口名称:综合账单查询
- 接口地址:/Buiness/CLMBusiness/CheckTheBill/List
- 接口类型:GET
- 接口描述:queryString方式传值
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| typeId  	|	  类型ID，1为租金费；2为物业费  	|	  string  |
| year  	|	  年  	|	  string  |
| month  	|	  月  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| getEffectiveAccountsResultSpecified  	|	    	|	  boolean  |
| getEffectiveAccountsResult  	|	    	|	  object  |
| - accountWebService  	|	  数据集  	|	  array<object>  |
| - - account  	|	  金额  	|	  number  |
| - - contractNumberSpecified  	|	    	|	  boolean  |
| - - contractNumber  	|	  合同编号  	|	  string  |
| - - month  	|	  月  	|	  number  |
| - - year  	|	  年  	|	  number  |
| - accountWebServiceSpecified  	|	    	|	  boolean  |
### 页面名称: 是否完善个人资料
#### 接口名称:是否完善个人资料
- 接口地址:/Setting/User/IsCompleteInfo
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 测试申请
#### 接口名称:读取测试申请配置
- 接口地址:/TestApplyfor/UserTestApplyforConfig/Edit/1
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| type  	|	  类型（1）  	|	  number  |
| content  	|	  内容  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
#### 接口名称:添加测试申请
- 接口地址:/TestApplyfor/UserTestApplyfor/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| softName  	|	  软件名称  	|	  string  |
| testTypeId  	|	  测试类别  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| chineseName  	|	  用户  	|	  string  |
| content  	|	  需求说明  	|	  string  |
| memo  	|	  备注  	|	  string  |
| material  	|	  其它材料  	|	  string  |
| applyForm  	|	  申请表  	|	  string  |
| softAbbreviation  	|	  软件简称  	|	  string  |
| organizers  	|	  测送单位  	|	  string  |
| company  	|	  公司  	|	  string  |
| email  	|	  email  	|	  string  |
| softVersion  	|	  软件版本  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看测试申请详情
- 接口地址:/TestApplyfor/UserTestApplyfor/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| softAbbreviation  	|	  软件简称  	|	  string  |
| softName  	|	  软件名称  	|	  string  |
| email  	|	  email  	|	  string  |
| chineseName  	|	  申请人  	|	  string  |
| username  	|	  用户登录名  	|	  string  |
| company  	|	  公司  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| serialNumber  	|	  流水号  	|	  string  |
| testType  	|	  测试类别对象  	|	  object  |
| - english  	|	    	|	    |
| - type  	|	    	|	  string  |
| - parentid  	|	    	|	  number  |
| - settingDict  	|	    	|	    |
| - name  	|	  名称  	|	  string  |
| - objectid  	|	    	|	  number  |
| - orderFlag  	|	    	|	  number  |
| softVersion  	|	  版本号  	|	  string  |
| memo  	|	  备注信息  	|	  string  |
| testTypeId  	|	  测试类别id  	|	  number  |
| applyForm  	|	  申请表  	|	  string  |
| content  	|	  需求说明  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| organizers  	|	  测送单位  	|	  string  |
| createDate  	|	  申请时间  	|	  string  |
| material  	|	  其它材料  	|	  string  |
### 页面名称: 职位申请
#### 接口名称:我申请的职位详情
- 接口地址:/Jobs/UserJobs/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| downloadNum  	|	  简历下载次数  	|	  number  |
| jobsid  	|	  职位id  	|	  number  |
| objectid  	|	  主键ID  	|	  number  |
| username  	|	  用户名  	|	  string  |
| url  	|	  简历地址  	|	  string  |
| company  	|	  公司名  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| email  	|	  邮箱  	|	  string  |
| memo  	|	  备注  	|	  string  |
| jobs  	|	    	|	  object  |
| - content  	|	  内容  	|	    |
| - moneyTypeId  	|	  薪资类别id  	|	  string  |
| - name  	|	  职位名称  	|	  string  |
| - money  	|	  薪资  	|	    |
| - createDate  	|	  创建时间  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - moneyType  	|	  薪资  	|	  object  |
| - - type  	|	  数据字典类别  	|	  string  |
| - - name  	|	  薪资类别名称  	|	  string  |
| - - objectid  	|	  主键id  	|	  string  |
| - objectid  	|	  主键id  	|	  string  |
#### 接口名称:职位详情
- 接口地址:/Jobs/Edit/{id}
- 接口类型:GET
- 接口描述:响应参数字段含义参见职位列表
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| money  	|	  薪资  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| deleteFlag  	|	  禁用启用（-1：禁用，1-启用）  	|	  number  |
| moneyType  	|	  薪资类别  	|	  object  |
| - settingDict  	|	    	|	    |
| - objectid  	|	  数据字典ID  	|	  number  |
| - orderFlag  	|	  排序  	|	  number  |
| - parentid  	|	  父级ID  	|	  number  |
| - name  	|	  数据字典名  	|	  string  |
| - type  	|	  数据字典类型  	|	  string  |
| moneyTypeId  	|	  薪资类别ID  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| name  	|	  职位名称  	|	  string  |
| content  	|	  职位描述  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
#### 接口名称:提交职位申请信息
- 接口地址:/Jobs/UserJobs/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| email  	|	  email  	|	  string  |
| jobsid  	|	  职位id  	|	  number  |
| chineseName  	|	  用户真实名称  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| memo  	|	  备注信息  	|	  string  |
| url  	|	  简历url  	|	  string  |
| company  	|	  公司名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:我申请的职位列表
- 接口地址:/Jobs/UserJobs/My/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	  总条数  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
| result  	|	  返回结果集  	|	  array<object>  |
| - downloadNum  	|	  简历下载次数  	|	  number  |
| - email  	|	  邮箱  	|	  string  |
| - username  	|	  用户名  	|	  string  |
| - jobs  	|	    	|	  object  |
| - - updateDate  	|	  更新时间  	|	    |
| - - money  	|	  薪资  	|	    |
| - - content  	|	  内容  	|	    |
| - - createDate  	|	  创建时间  	|	    |
| - - name  	|	  职位名称  	|	    |
| - - objectid  	|	  职位objectid  	|	    |
| - - moneyType  	|	  薪资类别  	|	  object  |
| - - - name  	|	  薪资类别名称  	|	    |
| - - - type  	|	  数据字典类别  	|	    |
| - - - objectid  	|	  主键id  	|	    |
| - phone  	|	  联系方式  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - company  	|	  公司  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - url  	|	  简历url  	|	  number  |
| - memo  	|	  备注  	|	  string  |
| - objectid  	|	  主键id  	|	  number  |
| page  	|	  当前页  	|	  number  |
#### 接口名称:可用职位列表
- 接口地址:/Jobs/Enable/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - createDate  	|	  创建时间  	|	  string  |
| - name  	|	  职位名称  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - deleteFlag  	|	  禁用启用（-1：禁用，1-启用）  	|	  number  |
| - moneyTypeId  	|	  薪资类别ID  	|	  number  |
| - moneyType  	|	  薪资类别  	|	  object  |
| - - name  	|	  数据字典名  	|	  string  |
| - - objectid  	|	  数据字典ID  	|	  number  |
| - - orderFlag  	|	  排序  	|	  number  |
| - - parentid  	|	  父级ID  	|	  number  |
| - - type  	|	  数据字典类型  	|	  string  |
| - - settingDict  	|	    	|	    |
| - money  	|	  薪资  	|	  string  |
| - content  	|	  职位描述  	|	  string  |
| - objectid  	|	  主键id  	|	  number  |
| page  	|	  当前页  	|	  number  |
| pagesize  	|	  每页条数  	|	  number  |
| total  	|	  总条数  	|	  number  |
### 页面名称: 入驻申请
#### 接口名称:查看
- 接口地址:/Setting/EnterApply/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| sTypeId  	|	    	|	  number  |
| sStafffId  	|	  规模id  	|	  number  |
| sType  	|	  产业类别  	|	  object  |
| - settingDict  	|	    	|	    |
| - english  	|	    	|	    |
| - parentid  	|	    	|	  number  |
| - type  	|	    	|	  string  |
| - orderFlag  	|	    	|	  number  |
| - name  	|	  产业类别文字  	|	  string  |
| - objectid  	|	    	|	  number  |
| createDate  	|	  申请时间  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| company  	|	  公司  	|	  string  |
| chineseName  	|	  用户名  	|	    |
| username  	|	  用户登录名  	|	  string  |
| parkName  	|	  园区  	|	  object  |
| - name  	|	  园区名称  	|	  string  |
| - createDate  	|	    	|	  string  |
| - updateDate  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - memo  	|	    	|	    |
| updateDate  	|	    	|	    |
| area  	|	  面积  	|	  string  |
| sStaff  	|	  公司规模  	|	  object  |
| - type  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - english  	|	    	|	    |
| - parentid  	|	    	|	  number  |
| - orderFlag  	|	    	|	  number  |
| - settingDict  	|	    	|	    |
| - name  	|	  规模文字  	|	  string  |
| email  	|	  邮箱  	|	  string  |
| contact  	|	  联系方式  	|	  string  |
| details  	|	  需求说明  	|	  string  |
| memo  	|	  备注  	|	  string  |
| parkId  	|	  园区id  	|	  number  |
| serialNumber  	|	  流水号  	|	  string  |
#### 接口名称:列表
- 接口地址:/Setting/EnterApply/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| endTime  	|	  查询结束时间  	|	  string  |
| startTime  	|	  查询开始时间  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	    	|	  number  |
| result2  	|	    	|	  array<object>  |
| - details  	|	  需求说明  	|	  string  |
| - parkName  	|	  园区对象  	|	  object  |
| - - memo  	|	    	|	    |
| - - createDate  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - updateDate  	|	    	|	  string  |
| - - name  	|	  园区名称  	|	  string  |
| - area  	|	  面积  	|	  string  |
| - sType  	|	  产业类别  	|	  object  |
| - - orderFlag  	|	    	|	  number  |
| - - objectid  	|	    	|	  number  |
| - - name  	|	  类别名称  	|	  string  |
| - - english  	|	    	|	    |
| - - settingDict  	|	    	|	    |
| - - type  	|	    	|	  string  |
| - - parentid  	|	    	|	  number  |
| - sTypeId  	|	    	|	  number  |
| - email  	|	  邮箱  	|	  string  |
| - updateDate  	|	    	|	    |
| - createDate  	|	  申请时间  	|	  string  |
| - contact  	|	  联系方式  	|	  string  |
| - parkId  	|	  园区id  	|	  number  |
| - username  	|	    	|	  string  |
| - sStafffId  	|	    	|	  number  |
| - serialNumber  	|	  流水号  	|	  string  |
| - chineseName  	|	  用户名  	|	    |
| - memo  	|	  备注  	|	  string  |
| - company  	|	  公司  	|	  string  |
| - sStaff  	|	  公司规模  	|	  object  |
| - - settingDict  	|	    	|	    |
| - - english  	|	    	|	    |
| - - objectid  	|	    	|	  number  |
| - - parentid  	|	    	|	  number  |
| - - orderFlag  	|	    	|	  number  |
| - - name  	|	  规模名称  	|	  string  |
| - - type  	|	    	|	  string  |
| - objectid  	|	  主键id  	|	  number  |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
#### 接口名称:添加
- 接口地址:/Setting/EnterApply/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| details  	|	  需求说明  	|	  string  |
| chineseName  	|	  用户名  	|	    |
| memo  	|	  备注信息  	|	  string  |
| sTypeId  	|	  产业类别  	|	  number  |
| sStafffId  	|	  公司规模id  	|	  number  |
| parkId  	|	  园区id  	|	  number  |
| area  	|	  面积  	|	  string  |
| contact  	|	  联系方式  	|	  string  |
| company  	|	  公司  	|	  string  |
| email  	|	  邮箱  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 会议室预定
#### 接口名称:添加会议室
- 接口地址:/Mettingroom/Mettingroom/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| peoples  	|	  可容纳人数  	|	  string  |
| pic  	|	  封面图片  	|	  string  |
| location  	|	  会议室位置  	|	  string  |
| vsx  	|	  是否含有视频会议（-1-不含，1-含）  	|	  number  |
| readme  	|	  须知  	|	  string  |
| content  	|	  内容  	|	  string  |
| price  	|	  参考价格  	|	  string  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| name  	|	  会议室名称  	|	  string  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:会议室预定详情2（用户提交）
- 接口地址:/Mettingroom/UserMeetingroomApply/UserMeetingroom/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| createDate  	|	    	|	  string  |
| userMeetingroomId  	|	    	|	  number  |
| applyDate  	|	  申请时间  	|	  string  |
| endTime  	|	  结束时间  	|	  string  |
| beginTime  	|	  开始时间  	|	  string  |
| meetingroomId  	|	    	|	  number  |
| updateDate  	|	    	|	  string  |
| mettingroom  	|	  会议室对象（参考会议室接口）  	|	  object  |
| - location  	|	    	|	  string  |
| - pic  	|	    	|	  string  |
| - microphone  	|	    	|	  number  |
| - area  	|	    	|	  string  |
| - deleteFlag  	|	    	|	  number  |
| - readme  	|	    	|	  string  |
| - projector  	|	    	|	  number  |
| - objectid  	|	    	|	  number  |
| - createDate  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| - content  	|	    	|	  string  |
| - vsx  	|	    	|	  number  |
| - isRead  	|	    	|	  number  |
| - price  	|	    	|	  string  |
| - peoples  	|	    	|	  string  |
| - updateDate  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| userMeetingroom  	|	    	|	  object  |
| - createDate  	|	    	|	  string  |
| - chineseName  	|	    	|	  string  |
| - vsx  	|	    	|	  number  |
| - memo  	|	    	|	  string  |
| - peoples  	|	    	|	    |
| - phone  	|	    	|	  string  |
| - username  	|	    	|	  string  |
| - microphone  	|	    	|	  number  |
| - projector  	|	    	|	  number  |
| - teaReak  	|	    	|	  number  |
| - serialNumber  	|	    	|	  string  |
| - teaReakMoney  	|	    	|	  number  |
| - topics  	|	    	|	  string  |
| - email  	|	    	|	  string  |
| - company  	|	    	|	  string  |
| - updateDate  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
#### 接口名称:可用会议室列表
- 接口地址:/Mettingroom/Mettingroom/Enable/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页条数  	|	  number  |
| total  	|	  总条数  	|	  number  |
| page  	|	  当前页码  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - updateDate  	|	  更新时间  	|	  string  |
| - isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| - content  	|	  内容  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - name  	|	  会议室名称  	|	  string  |
| - projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| - pic  	|	  封面图片  	|	  string  |
| - peoples  	|	  可容纳人数  	|	  string  |
| - deleteFlag  	|	  禁用启用（-1-禁用,1-启用）  	|	  number  |
| - price  	|	  参考价格  	|	  string  |
| - readme  	|	  须知  	|	  string  |
| - microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| - vsx  	|	  是否含有视频会议（-1-不含，1-含）  	|	  number  |
| - objectid  	|	  主键id  	|	  number  |
| - location  	|	  会议室位置  	|	  string  |
#### 接口名称:会议室预定详情3（后台确认）
- 接口地址:/Mettingroom/UserMeetingroomVerify/UserMeetingroom/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| userMeetingroom  	|	    	|	  object  |
| - chineseName  	|	    	|	  string  |
| - username  	|	    	|	  string  |
| - projector  	|	    	|	  number  |
| - teaReak  	|	    	|	  number  |
| - topics  	|	    	|	  string  |
| - phone  	|	    	|	  string  |
| - teaReakMoney  	|	    	|	  number  |
| - createDate  	|	    	|	  string  |
| - vsx  	|	    	|	  number  |
| - company  	|	    	|	  string  |
| - email  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - serialNumber  	|	    	|	  string  |
| - microphone  	|	    	|	  number  |
| - peoples  	|	    	|	    |
| - updateDate  	|	    	|	  string  |
| - memo  	|	    	|	  string  |
| createDate  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| userMeetingroomId  	|	    	|	  number  |
| updateDate  	|	    	|	  string  |
| applyDate  	|	  申请时间  	|	  string  |
| mettingroom  	|	  会议室对象（参考会议室接口）  	|	  object  |
| - area  	|	    	|	  string  |
| - location  	|	    	|	  string  |
| - projector  	|	    	|	  number  |
| - isRead  	|	    	|	  number  |
| - createDate  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| - vsx  	|	    	|	  number  |
| - microphone  	|	    	|	  number  |
| - peoples  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - updateDate  	|	    	|	  string  |
| - deleteFlag  	|	    	|	  number  |
| - price  	|	    	|	  string  |
| - content  	|	    	|	  string  |
| - readme  	|	    	|	  string  |
| - pic  	|	    	|	  string  |
| endTime  	|	  结束时间  	|	  string  |
| meetingroomId  	|	    	|	  number  |
| beginTime  	|	  开始时间  	|	  string  |
#### 接口名称:会议室预定详情1
- 接口地址:/Mettingroom/UserMeetingroom/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| email  	|	  邮件  	|	  string  |
| peoples  	|	  参会人数  	|	    |
| updateDate  	|	    	|	  string  |
| company  	|	  公司  	|	  string  |
| chineseName  	|	  用户名  	|	  string  |
| username  	|	    	|	  string  |
| vsx  	|	  是否含有视频会议（-1-不含，1-含）  	|	  number  |
| createDate  	|	  申请时间  	|	  string  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| teaReak  	|	  是否需要茶歇（1-需要，-1-不需要）  	|	  number  |
| memo  	|	  备注  	|	  string  |
| serialNumber  	|	  流水号  	|	  string  |
| topics  	|	  须知  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| phone  	|	    	|	  string  |
| teaReakMoney  	|	  茶歇人均标准  	|	  number  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
#### 接口名称:会议室预定
- 接口地址:/Mettingroom/UserMeetingroom/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| phone  	|	  联系方式  	|	  string  |
| meetingroomId  	|	  会议室objectid  	|	  number  |
| topics  	|	  会议主题  	|	  string  |
| chineseName  	|	  真实名称（user表realName）  	|	  string  |
| beginTimeArray  	|	  开始时间 15:00  	|	  array<string>  |
| vsx  	|	  是否含有视频会议（-1-不含，1-含）  	|	  number  |
| email  	|	  Email  	|	  string  |
| company  	|	  公司名  	|	  string  |
| dateTimeArray  	|	  日期  	|	  array<string>  |
| teaReakMoney  	|	  茶歇人均标准  	|	  number  |
| memo  	|	  备注  	|	  string  |
| endTimeArray  	|	  结束时间17：00  	|	  array<number>  |
| peoples  	|	  参会人数  	|	  string  |
| teaReak  	|	  是否需要茶歇（1-需要，-1-不需要）  	|	  number  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:会议室详情
- 接口地址:/Mettingroom/Mettingroom/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| vsx  	|	  是否含有视频会议（-1-不含，1-含）  	|	  number  |
| content  	|	  内容  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| location  	|	  会议室位置  	|	  string  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| readme  	|	  须知  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| deleteFlag  	|	  禁用启用（-1-禁用,1-启用）  	|	  number  |
| pic  	|	  封面图片  	|	  string  |
| name  	|	  会议室名称  	|	  string  |
| peoples  	|	  可容纳人数  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| price  	|	  参考价格  	|	  string  |
### 页面名称: 个人培训
#### 接口名称:我的报名详情
- 接口地址:/Cultivate/UserCultivate/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - schoolTime  	|	  上课时间（1-7，逗号隔开，代表一周）  	|	  string  |
| - workYearId  	|	  工作年限id  	|	  number  |
| - education  	|	  学历  	|	  number  |
| - phone  	|	  联系方式  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - company  	|	  公司  	|	  string  |
| - gradeId  	|	  等级ID  	|	  number  |
| - memo  	|	  备注  	|	    |
| - courseId  	|	  课程表id  	|	  number  |
| - objectid  	|	  主键ID  	|	  number  |
| - sex  	|	  性别  	|	  number  |
| - email  	|	  邮箱  	|	  string  |
| - grade  	|	  等级  	|	  object  |
| - - objectid  	|	  等级objectid  	|	  number  |
| - - parentid  	|	  父级id  	|	  number  |
| - - name  	|	  等级名称  	|	  string  |
| - - orderFlag  	|	  排序  	|	  number  |
| - - type  	|	  数据字典类型  	|	  string  |
| - username  	|	  用户名  	|	  string  |
| - course  	|	  课程信息  	|	  object  |
| - - pic  	|	  封面图片  	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - courseOutline  	|	  课程大纲  	|	  string  |
| - - deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| - - name  	|	  课程名称  	|	  string  |
| - - content  	|	  课程简介  	|	  string  |
| - - createDate  	|	    	|	  string  |
| - - updateDate  	|	    	|	  string  |
| - - price  	|	  价格  	|	  number  |
| - censusRegister  	|	  是否沪级（1-是，-1-否）  	|	  number  |
| - job  	|	  目前职位  	|	  object  |
| - - parentid  	|	  父级id  	|	  number  |
| - - orderFlag  	|	  排序  	|	  number  |
| - - objectid  	|	  职位objectid  	|	  number  |
| - - type  	|	  数据字典类型  	|	  string  |
| - - name  	|	  职位名称  	|	  string  |
| - workYear  	|	  工作年限  	|	  object  |
| - - type  	|	  数据字典类型  	|	  string  |
| - - objectid  	|	  工作年限ID  	|	  number  |
| - - parentid  	|	  父级ID  	|	  number  |
| - - name  	|	  工作年限名称  	|	  string  |
| - - orderFlag  	|	  排序  	|	  number  |
| - socialInsurance  	|	  是否在上海缴纳社保（1-是，-1否）  	|	  number  |
| - createDate  	|	  创建时间  	|	  string  |
| - jobId  	|	  目前职位id  	|	  number  |
#### 接口名称:可用课程列表
- 接口地址:/Cultivate/Course/Enable/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	    	|	  number  |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - updateDate  	|	    	|	  string  |
| - createDate  	|	    	|	  string  |
| - price  	|	  价格  	|	  number  |
| - name  	|	  课程名称  	|	  string  |
| - deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| - sponsor  	|	  主办方  	|	  string  |
| - courseOutline  	|	  课程大纲  	|	  string  |
| - content  	|	  课程简介  	|	  string  |
| - pic  	|	  封面图片  	|	  string  |
| - objectid  	|	    	|	  number  |
#### 接口名称:我的报名列表
- 接口地址:/Cultivate/UserCultivate/My/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  当前页  	|	  number  |
| pagesize  	|	  每页显示条数  	|	  number  |
| total  	|	    	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - phone  	|	  联系方式  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - email  	|	  邮箱  	|	  string  |
| - workYearId  	|	  工作年限id  	|	  number  |
| - workYear  	|	  工作年限  	|	  object  |
| - - orderFlag  	|	  排序  	|	  number  |
| - - parentid  	|	  父级ID  	|	  number  |
| - - name  	|	  工作年限名称  	|	  string  |
| - - type  	|	  数据字典类型  	|	  string  |
| - - objectid  	|	  工作年限ID  	|	  number  |
| - memo  	|	  备注  	|	    |
| - company  	|	  公司  	|	  string  |
| - schoolTime  	|	  上课时间（1-7，逗号隔开，代表一周）  	|	  string  |
| - sex  	|	  性别  	|	  number  |
| - courseId  	|	  课程表id  	|	  number  |
| - socialInsurance  	|	  是否在上海缴纳社保（1-是，-1否）  	|	  number  |
| - censusRegister  	|	  是否沪级（1-是，-1-否）  	|	  number  |
| - education  	|	  学历  	|	  number  |
| - grade  	|	  等级  	|	  object  |
| - - type  	|	  数据字典类型  	|	  string  |
| - - objectid  	|	  等级objectid  	|	  number  |
| - - parentid  	|	  父级id  	|	  number  |
| - - name  	|	  等级名称  	|	  string  |
| - - orderFlag  	|	  排序  	|	  number  |
| - jobId  	|	  目前职位id  	|	  number  |
| - objectid  	|	  主键ID  	|	  number  |
| - updateDate  	|	  更新时间  	|	  string  |
| - username  	|	  用户名  	|	  string  |
| - course  	|	  课程信息  	|	  object  |
| - - updateDate  	|	    	|	  string  |
| - - createDate  	|	    	|	  string  |
| - - pic  	|	  封面图片  	|	  string  |
| - - courseOutline  	|	  课程大纲  	|	  string  |
| - - deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| - - name  	|	  课程名称  	|	  string  |
| - - price  	|	  价格  	|	  number  |
| - - objectid  	|	    	|	  number  |
| - - content  	|	  课程简介  	|	  string  |
| - job  	|	  目前职位  	|	  object  |
| - - name  	|	  职位名称  	|	  string  |
| - - objectid  	|	  职位objectid  	|	  number  |
| - - parentid  	|	  父级id  	|	  number  |
| - - orderFlag  	|	  排序  	|	  number  |
| - - type  	|	  数据字典类型  	|	  string  |
| - gradeId  	|	  等级ID  	|	  number  |
#### 接口名称:新增课程
- 接口地址:/Cultivate/Course/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| name  	|	  课程名称  	|	  string  |
| courseOutline  	|	  课程大纲  	|	  string  |
| pic  	|	  课程封面图片  	|	  string  |
| price  	|	  课程价格  	|	  number  |
| deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| content  	|	  课程简介  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看课程详情
- 接口地址:/Cultivate/Course/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| updateDate  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| price  	|	  价格  	|	  number  |
| createDate  	|	  创建时间  	|	  string  |
| name  	|	  课程名称  	|	  string  |
| deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| pic  	|	  封面图片  	|	  string  |
| content  	|	  课程简介  	|	  string  |
| sponsor  	|	  主办方  	|	  string  |
| courseOutline  	|	  课程大纲  	|	  string  |
#### 接口名称:添加报名
- 接口地址:/Cultivate/UserCultivate/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
|   	|	    	|	    |
| result  	|	    	|	  array<object>  |
| - sex  	|	  性别性别（1-男 2-女）  	|	  number  |
| - company  	|	  公司名  	|	  string  |
| - censusRegister  	|	  是否沪级（1-是，-1-否）  	|	  number  |
| - email  	|	  邮箱  	|	  string  |
| - educationId  	|	  学历id  	|	  number  |
| - schoolTimeArray  	|	  上课时间  	|	  array<string>  |
| - memo  	|	  备注  	|	    |
| - gradeId  	|	  等级  	|	  number  |
| - socialInsurance  	|	  是否在上海缴纳社保（1-是，-1否）  	|	  number  |
| - workYearId  	|	  工作年限id  	|	  number  |
| - courseId  	|	  课程表id  	|	  number  |
| - jobId  	|	  目前职位  	|	  number  |
| - chineseName  	|	  用户真实姓名  	|	    |
| - phone  	|	  电话  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:所有课程列表（包含禁用、启用）
- 接口地址:/Cultivate/Course/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  页码  	|	  number  |
| total  	|	    	|	  number  |
| result  	|	  返回结果集  	|	  array<object>  |
| - objectid  	|	  主键ID  	|	  number  |
| - createDate  	|	    	|	  string  |
| - updateDate  	|	    	|	  string  |
| - sponsor  	|	  主办方  	|	  string  |
| - courseOutline  	|	  课程大纲  	|	  string  |
| - price  	|	  价格  	|	  number  |
| - deleteFlag  	|	  是否禁用（默认-1 禁用，1-启用）  	|	  number  |
| - pic  	|	  封面图片  	|	  string  |
| - content  	|	  课程简介  	|	  string  |
| - name  	|	  课程名称  	|	  string  |
| pagesize  	|	  每页显示条数  	|	  number  |
### 页面名称: 孵化预约注册
#### 接口名称:我的孵化预约详情
- 接口地址:/Incubator/UserIncubator/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	  结果集  	|	  array<object>  |
| - phone  	|	  联系方式  	|	  string  |
| - email  	|	  邮箱  	|	  string  |
| - chineseName  	|	  用户真实名称  	|	  string  |
| - company  	|	  公司名称  	|	  string  |
| - username  	|	  用户登录名  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - objectid  	|	  主键ID  	|	  number  |
| - appointmentDate  	|	  预约时间  	|	  number  |
| - years  	|	  年限  	|	  number  |
| - memo  	|	  备注  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - rentType  	|	  租赁类别（1-新租/2-续租）  	|	  number  |
#### 接口名称:添加孵化预约
- 接口地址:/Incubator/UserIncubator/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| appointmentDate  	|	  预约时间  	|	  number  |
| memo  	|	  备注  	|	  string  |
| company  	|	  公司  	|	  string  |
| email  	|	  邮箱  	|	  string  |
| rentType  	|	  租赁类别（1-新租/2-续租）  	|	  number  |
| years  	|	  年限  	|	  number  |
| chineseName  	|	  用户真实名  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:我的孵化预约列表
- 接口地址:/Incubator/UserIncubator/My/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页显示条数  	|	  number  |
| page  	|	  页码  	|	  number  |
| total  	|	  总条数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - years  	|	  年限  	|	  number  |
| - appointmentDate  	|	  预约时间  	|	  number  |
| - company  	|	  公司名称  	|	  string  |
| - chineseName  	|	  用户真实名称  	|	  string  |
| - phone  	|	  联系方式  	|	  string  |
| - memo  	|	  备注  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - email  	|	  邮箱  	|	  string  |
| - username  	|	  用户登录名  	|	  string  |
| - rentType  	|	  租赁类别（1-新租/2-续租）  	|	  number  |
| - objectid  	|	  主键ID  	|	  number  |
| - createDate  	|	  创建时间  	|	  string  |
### 页面名称: 广告服务申请
#### 接口名称:添加广告服务申请
- 接口地址:/Commercialize/UserCommercialize/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| content  	|	  需求说明  	|	  string  |
| chineseName  	|	  用户真实名  	|	  string  |
| email  	|	  email  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| accessory  	|	  广告类容  	|	  string  |
| beginDate  	|	  开始时间  	|	  string  |
| memo  	|	  备注  	|	  string  |
| company  	|	  公司名  	|	  string  |
| endDate  	|	  结束时间  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看广告服务申请详情
- 接口地址:/Commercialize/UserCommercialize/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| username  	|	  用户名  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| beginDate  	|	  开始时间  	|	  number  |
| endDate  	|	  结束时间  	|	  number  |
| updateDate  	|	  更新时间  	|	  string  |
| email  	|	  邮箱  	|	  string  |
| content  	|	  需求说明  	|	  string  |
| company  	|	  公司  	|	  string  |
| serialNumber  	|	  流水号  	|	  string  |
| accessory  	|	  附件  	|	  string  |
| memo  	|	  备注  	|	  string  |
| objectid  	|	  主键ID  	|	  number  |
#### 接口名称:我的广告服务申请
- 接口地址:/Commercialize/UserCommercialize/My/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	  每页条数  	|	  number  |
| total  	|	  总条数  	|	  number  |
| result  	|	  结果集  	|	  array<object>  |
| - content  	|	  需求说明  	|	  string  |
| - memo  	|	  备注  	|	  string  |
| - company  	|	  公司名  	|	  string  |
| - username  	|	  用户名  	|	  string  |
| - endDate  	|	  结束时间  	|	  number  |
| - accessory  	|	  附件地址  	|	  string  |
| - email  	|	  邮箱  	|	  string  |
| - objectid  	|	  主键ID  	|	  number  |
| - serialNumber  	|	  流水号  	|	  string  |
| - beginDate  	|	  开始时间  	|	  number  |
| - phone  	|	  联系方式  	|	  string  |
| - createDate  	|	  创建时间  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| page  	|	  页码  	|	  number  |
### 页面名称: IT报修
### 页面名称: 猎聘申请
#### 接口名称:猎聘职位详情
- 接口地址:/Headhunting/HeadhuntingJobs/HeadhuntingJobs/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - updateDate  	|	    	|	  string  |
| - headhuntingId  	|	    	|	  number  |
| - money  	|	  薪资描述  	|	  string  |
| - conditions  	|	  任职条件  	|	  string  |
| - obligation  	|	  工作职责  	|	  string  |
| - objectid  	|	    	|	  number  |
| - userHeadhunting  	|	    	|	  object  |
| - - company  	|	    	|	  string  |
| - - content  	|	    	|	  string  |
| - - serialNumber  	|	    	|	  string  |
| - - createDate  	|	    	|	  string  |
| - - updateDate  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - email  	|	    	|	  string  |
| - - username  	|	    	|	  string  |
| - - memo  	|	    	|	  string  |
| - - phone  	|	    	|	  string  |
| - - accessory  	|	    	|	  string  |
| - - chineseName  	|	    	|	  string  |
| - name  	|	  职位名称  	|	  string  |
| - createDate  	|	    	|	  string  |
| total  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
#### 接口名称:添加猎聘
- 接口地址:/Headhunting/UserHeadhunting/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| accessory  	|	  附件  	|	  string  |
| jobsMoney  	|	  职位薪资数组  	|	  array<string>  |
| jobsName  	|	  职位名称数组  	|	  array<string>  |
| phone  	|	  联系方式  	|	  string  |
| jobsCondition  	|	  职位任职条件数组  	|	  array<string>  |
| company  	|	  公司  	|	  string  |
| memo  	|	  备注  	|	  string  |
| email  	|	  邮箱  	|	  string  |
| content  	|	  需求说明  	|	  string  |
| chineseName  	|	  用户真实姓名  	|	  string  |
| jobObligation  	|	  职位职责数组  	|	  array<string>  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:猎聘详情
- 接口地址:/Headhunting/UserHeadhunting/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| updateDate  	|	  更新时间  	|	  string  |
| content  	|	  需求说明  	|	  string  |
| serialNumber  	|	  流水号  	|	  string  |
| memo  	|	  备注  	|	  string  |
| company  	|	  公司  	|	  string  |
| username  	|	  用户登录名  	|	  string  |
| chineseName  	|	  用户名  	|	  string  |
| email  	|	  邮箱  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| createDate  	|	  申请时间  	|	  string  |
| accessory  	|	  附件  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
### 页面名称: 教室预定
#### 接口名称:添加教室
- 接口地址:/Classroom/Classroom/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| price  	|	  参考价格  	|	  string  |
| deleteFlag  	|	  禁用启用（-1-禁用，1-启用）  	|	  number  |
| area  	|	  面积  	|	  string  |
| location  	|	  教室位置  	|	  string  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| peoples  	|	  可容纳人数  	|	  string  |
| content  	|	  内容  	|	  string  |
| pic  	|	  封面图片  	|	  string  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| computer  	|	  是否含有电脑（-1-不含，1-含）  	|	  number  |
| name  	|	  教室名称  	|	  string  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
| readme  	|	  须知  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:可用教室列表
- 接口地址:/Classroom/Classroom/Enable/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	  当前页  	|	  string  |
| result  	|	  结果集  	|	  array<object>  |
| - pic  	|	  封面图片  	|	  string  |
| - content  	|	  内容  	|	  string  |
| - location  	|	  教室位置  	|	  string  |
| - updateDate  	|	  更新时间  	|	  string  |
| - peoples  	|	  可容纳人数  	|	  string  |
| - area  	|	  面积  	|	  string  |
| - isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| - createDate  	|	  创建时间  	|	  string  |
| - readme  	|	  须知  	|	  string  |
| - computer  	|	  是否含有电脑（-1-不含，1-含）  	|	  string  |
| - projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  string  |
| - price  	|	  参考价格  	|	  string  |
| - deleteFlag  	|	  禁用启用（-1-禁用,1-启用）  	|	  string  |
| - name  	|	  教室名称  	|	  string  |
| - objectid  	|	  主键id  	|	  string  |
| - microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  string  |
| total  	|	  总条数  	|	  string  |
| pagesize  	|	  每页条数  	|	  string  |
#### 接口名称:预定教室
- 接口地址:/Classroom/UserClassroom/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| chineseName  	|	  姓名(User表realName字段)  	|	  string  |
| memo  	|	  备注  	|	  string  |
| halfDayArray  	|	  上午下午数组（a-上午，p-下午）  	|	  array<string>  |
| content  	|	  需求说明  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| dateTimeArray  	|	  日期数组(时间格式2016/03/29)  	|	  array<string>  |
| company  	|	  公司  	|	  string  |
| email  	|	  Email  	|	  string  |
| classroomId  	|	  教室objectid  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:教室预定详情2（用户申请情况）
- 接口地址:/Classroom/UserClassroomApply/UserClassroom/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	    	|	  number  |
| applyDate  	|	  申请日期  	|	  number  |
| classroomId  	|	    	|	  number  |
| userClassroom  	|	    	|	  object  |
| - chineseName  	|	    	|	  string  |
| - content  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - memo  	|	    	|	  string  |
| - updateDate  	|	    	|	  string  |
| - email  	|	    	|	  string  |
| - username  	|	    	|	  string  |
| - company  	|	    	|	  string  |
| - phone  	|	    	|	  string  |
| - createDate  	|	    	|	  string  |
| - serialNumber  	|	    	|	  string  |
| halfDay  	|	  上午下午（a-上午，p-下午）  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| updateDate  	|	    	|	  string  |
| classroom  	|	  教室对象（值参考教室接口）  	|	  object  |
| - pic  	|	    	|	  string  |
| - content  	|	    	|	  string  |
| - computer  	|	    	|	  number  |
| - isRead  	|	    	|	  number  |
| - area  	|	  面积  	|	  string  |
| - price  	|	    	|	  string  |
| - readme  	|	    	|	  string  |
| - deleteFlag  	|	    	|	  number  |
| - objectid  	|	    	|	  number  |
| - createDate  	|	    	|	  string  |
| - projector  	|	    	|	  number  |
| - location  	|	    	|	  string  |
| - name  	|	    	|	  string  |
| - peoples  	|	    	|	  string  |
| - updateDate  	|	    	|	  string  |
| - microphone  	|	    	|	  number  |
| userClassroomId  	|	    	|	  number  |
#### 接口名称:教室预定详情3（确认情况）
- 接口地址:/Classroom/UserClassroomVerify/UserClassroom/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| halfDay  	|	  上午下午（a-上午，p-下午）  	|	  string  |
| classroom  	|	  教室对象（值参见教室接口）  	|	  object  |
| - name  	|	    	|	  string  |
| - readme  	|	    	|	  string  |
| - pic  	|	    	|	  string  |
| - createDate  	|	    	|	  string  |
| - peoples  	|	    	|	  string  |
| - isRead  	|	    	|	  number  |
| - computer  	|	    	|	  number  |
| - projector  	|	    	|	  number  |
| - updateDate  	|	    	|	  string  |
| - location  	|	    	|	  string  |
| - content  	|	    	|	  string  |
| - area  	|	    	|	  string  |
| - price  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - microphone  	|	    	|	  number  |
| - deleteFlag  	|	    	|	  number  |
| applyDate  	|	  申请时间  	|	  number  |
| updateDate  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
| userClassroom  	|	    	|	  object  |
| - createDate  	|	    	|	  string  |
| - username  	|	    	|	  string  |
| - company  	|	    	|	  string  |
| - memo  	|	    	|	  string  |
| - updateDate  	|	    	|	  string  |
| - serialNumber  	|	    	|	  string  |
| - email  	|	    	|	  string  |
| - chineseName  	|	    	|	  string  |
| - content  	|	    	|	  string  |
| - phone  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| createDate  	|	  创建时间  	|	  string  |
| userClassroomId  	|	    	|	  number  |
| classroomId  	|	    	|	  number  |
#### 接口名称:教室预定详情1
- 接口地址:/Classroom/UserClassroom/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| serialNumber  	|	  流水号  	|	  string  |
| phone  	|	  联系方式  	|	  string  |
| createDate  	|	  申请时间  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| email  	|	  邮箱  	|	  string  |
| content  	|	  需求说明  	|	  string  |
| memo  	|	  备注  	|	  string  |
| company  	|	  公司  	|	  string  |
| username  	|	  用户登录名  	|	  string  |
| objectid  	|	  主键id  	|	  number  |
| chineseName  	|	  用户名  	|	  string  |
#### 接口名称:教室详情
- 接口地址:/Classroom/Classroom/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	  主键ID  	|	  number  |
| area  	|	  面积  	|	  string  |
| location  	|	  地址  	|	  string  |
| readme  	|	  须知  	|	  string  |
| peoples  	|	  可容纳人数  	|	  string  |
| createDate  	|	  创建时间  	|	  string  |
| updateDate  	|	  更新时间  	|	  string  |
| computer  	|	  是否含有电脑（-1-不含，1-含）  	|	  number  |
| isRead  	|	  是否需要已读确认(1-是，-1-否)  	|	  number  |
| pic  	|	  封面图片  	|	  string  |
| microphone  	|	  是否含有麦克风（-1-不含，1-含）  	|	  number  |
| deleteFlag  	|	  禁用启用（-1-禁用,1-启用）  	|	  number  |
| name  	|	  教室名称  	|	  string  |
| price  	|	  参考价格  	|	  string  |
| content  	|	  内容  	|	  string  |
| projector  	|	  是否含有投影仪（-1-不含，1-含）  	|	  number  |
### 页面名称: 业务是否完成
#### 接口名称:业务是否完成
- 接口地址:/Setting/MainBusiness/IsComplete/Edit/{sn}
- 接口类型:GET
- 接口描述:{sn}:流水号
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
## 模块名称: 汇智卡管理(第二版)
### 页面名称: 绑定解绑
#### 接口名称:绑定解绑
- 接口地址:/BizPayment/BindUnBindCard
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| cardNo  	|	  卡号  	|	  string  |
| type  	|	  1绑定2解绑  	|	  string  |
| memberNo  	|	  用户名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 支付
#### 接口名称:充值(支付)
- 接口地址:/BizPayment/CardReCharge
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| orderAmt  	|	  订单金额  	|	  string  |
| memberNo  	|	  用户名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 查询
#### 接口名称:交易明细查询
- 接口地址:/BizPayment/QueryTransDetails
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| startDate  	|	  开始日期  	|	  string  |
| tranType  	|	  交易类型 00 查询所有交易类型 02 圈存 07 电子钱包脱机消费 26 现金充值 25 现金类型  	|	  string  |
| queryNum  	|	  查询笔数  	|	  string  |
| cardNo  	|	  卡号  	|	  string  |
| endDate  	|	  结束日期  	|	  string  |
| memberNo  	|	  用户名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| merchantNo  	|	    	|	  string  |
| code  	|	    	|	  string  |
| msg  	|	    	|	  string  |
| merchantName  	|	    	|	  string  |
| details0  	|	    	|	  array<object>  |
| - tranNo  	|	    	|	  string  |
| - tranType  	|	    	|	  string  |
| - tranAmount  	|	    	|	  string  |
| - tranDate  	|	    	|	  string  |
| - terminalNo  	|	    	|	  string  |
| cardNo  	|	    	|	  string  |
| memberNo  	|	    	|	  string  |
| totalNum  	|	    	|	  string  |
#### 接口名称:余额查询
- 接口地址:/BizPayment/QueryCardBalance
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| cardNo  	|	  卡号  	|	  string  |
| memberNo  	|	  会员唯一标识  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| edBalance  	|	  主账户余额  	|	  string  |
| memberNo  	|	    	|	  string  |
| epBalance  	|	  电子钱包余额  	|	  string  |
| cardNo  	|	    	|	  string  |
| code  	|	  0000	本次通信正常	 2000	业务操作异常	 2001	参数有误  	|	  string  |
| merchantNo  	|	    	|	  string  |
| msg  	|	    	|	  string  |
## 模块名称: 活动中心
### 页面名称: 活动统计
#### 接口名称:图表统计
- 接口地址:/ActivityCenter/ActivityMain/StatisticGraphData
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| GetActivityByType  	|	  根据类别统计  	|	  array<object>  |
| - name  	|	    	|	  string  |
| - value  	|	    	|	  string  |
| TotalApply  	|	  活动申请总人数  	|	  number  |
| TotalActivities  	|	  活动总数  	|	  number  |
### 页面名称: 活动申请
#### 接口名称:活动列表
- 接口地址:/ActivityCenter/ActivityMain/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - address  	|	    	|	  string  |
| - title  	|	    	|	  string  |
| - image  	|	    	|	  string  |
| - type  	|	    	|	  number  |
| - mType  	|	    	|	  object  |
| - - orderFlag  	|	    	|	  number  |
| - - settingDict  	|	    	|	    |
| - - type  	|	    	|	  string  |
| - - objectid  	|	    	|	  number  |
| - - english  	|	    	|	    |
| - - parentid  	|	    	|	  number  |
| - - name  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - createTime  	|	    	|	  string  |
| - details  	|	    	|	  string  |
| - isBan  	|	  1启用2禁用  	|	  number  |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
#### 接口名称:报名人数导入
- 接口地址:/ActivityCenter/ActivityApply/ExcelImport
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| file  	|	  excel文件  	|	  object  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:活动禁用
- 接口地址:/ActivityCenter/ActivityMain/BanSet
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectId  	|	    	|	  number  |
| isBan  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
## 模块名称: 首页广告管理
### 页面名称: 列表
#### 接口名称:获取广告列表(可匿名)
- 接口地址:/Setting/HomepageAdver/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - title  	|	  广告标题  	|	  string  |
| - orderA  	|	  显示顺序  	|	  number  |
| - isBan  	|	  是否禁用(1禁用2启用)  	|	  number  |
| - linkA  	|	  广告图片链接地址  	|	  string  |
| - isShow  	|	  是否显示(1仅显示图片2显示链接3显示富文本)  	|	  number  |
| - objectid  	|	    	|	  number  |
| - imgUrl  	|	  图片地址  	|	  string  |
| - content  	|	  广告内容  	|	  string  |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| total  	|	    	|	  number  |
### 页面名称: 删除
#### 接口名称:删除广告
- 接口地址:/Setting/HomepageAdver/Delete/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 查看
#### 接口名称:查看广告(可匿名)
- 接口地址:/Setting/HomepageAdver/Edit/{id}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| imgUrl  	|	    	|	  string  |
| content  	|	    	|	  string  |
| title  	|	    	|	  string  |
| objectid  	|	    	|	  number  |
### 页面名称: 添加
#### 接口名称:新增广告
- 接口地址:/Setting/HomepageAdver/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 禁用启用
#### 接口名称:禁用启用
- 接口地址:/Setting/HomepageAdver/isBan
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| isBan  	|	  1禁用2启用  	|	  number  |
| objectid  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| success  	|	    	|	  boolean  |
| msg  	|	    	|	  string  |
### 页面名称: 更新
#### 接口名称:更新广告
- 接口地址:/Setting/HomepageAdver/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| objectid  	|	    	|	  number  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
## 模块名称: 汇智卡管理第二版
### 页面名称: 个人申请
#### 接口名称:列表
- 接口地址:/CardApply/Person/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - company  	|	    	|	  string  |
| - id  	|	    	|	  number  |
| - realname  	|	  真实姓名  	|	  string  |
| - createTime  	|	    	|	  number  |
| - mark  	|	  备注  	|	  string  |
| - getTime  	|	  领取时间  	|	  string  |
| - contact  	|	    	|	  string  |
| - sn  	|	  流水号  	|	  string  |
| - idcard  	|	  身份证号码  	|	  number  |
| - email  	|	    	|	  string  |
| - status  	|	  0待处理1受理中2审核通过3审核不通过4中止  	|	  number  |
| - username  	|	    	|	  string  |
| total  	|	    	|	  number  |
#### 接口名称:列表-本人
- 接口地址:/CardApply/Person/Me/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:新增
- 接口地址:/CardApply/Person/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新
- 接口地址:/CardApply/Person/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:删除
- 接口地址:/CardApply/Person/Del/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新状态
- 接口地址:/CardApply/Person/Status/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看
- 接口地址:/CardApply/Person/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 企业申请
#### 接口名称:列表
- 接口地址:/CardApply/Enterprise/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - contact  	|	    	|	  string  |
| - username  	|	    	|	  string  |
| - status  	|	  0待处理1受理中2审核通过3审核不通过4中止  	|	  number  |
| - company  	|	    	|	  string  |
| - email  	|	    	|	  string  |
| - createTime  	|	    	|	  number  |
| - attachment  	|	    	|	  string  |
| - getTime  	|	  领取时间  	|	  string  |
| - id  	|	    	|	  number  |
| - sn  	|	  流水号  	|	  string  |
| - mark  	|	  备注  	|	  string  |
#### 接口名称:更新状态
- 接口地址:/CardApply/Enterprise/Status/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:查看
- 接口地址:/CardApply/Enterprise/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新
- 接口地址:/CardApply/Enterprise/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:列表-本人
- 接口地址:/CardApply/Enterprise/Me/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:新增
- 接口地址:/CardApply/Enterprise/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:删除
- 接口地址:/CardApply/Enterprise/Del/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 公告管理
#### 接口名称:查看
- 接口地址:/Payment/Annoucement/Edit/{ojbecitId}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:是否有公告
- 接口地址:/Payment/Annoucement/Judge
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:添加
- 接口地址:/Payment/Annoucement/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:更新
- 接口地址:/Payment/Annoucement/Update
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:列表
- 接口地址:/Payment/Annoucement/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - objectid  	|	    	|	  number  |
| - startTime  	|	    	|	  string  |
| - username  	|	    	|	  string  |
| - endTime  	|	    	|	  string  |
| - content  	|	    	|	  string  |
| - createTime  	|	    	|	  string  |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
#### 接口名称:删除
- 接口地址:/Payment/Annoucement/Del/{ojbecitId}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 挂失解挂
#### 接口名称:挂失解挂
- 接口地址:/BizPayment/LostHangCard
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| memberNo  	|	  用户名  	|	  string  |
| type  	|	  1挂失2解挂  	|	  string  |
| cardNo  	|	  卡号  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 支付
#### 接口名称:充值（支付）
- 接口地址:/BizPayment/CardReCharge
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| orderAmtCount  	|	  红包ID  	|	  number  |
| orderAmt  	|	  订单金额  	|	  number  |
| memberNo  	|	  用户名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 绑定解绑
#### 接口名称:绑定解绑
- 接口地址:/BizPayment/BindUnBindCard
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| cardNo  	|	  卡号  	|	  string  |
| type  	|	  1绑定2解绑  	|	  number  |
| memberNo  	|	  用户名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 查询
#### 接口名称:余额查询
- 接口地址:/BizPayment/QueryCardBalance
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| memberNo  	|	    	|	  string  |
| cardNo  	|	    	|	  string  |
| msg  	|	    	|	  string  |
| epBalance  	|	    	|	  string  |
| edBalance  	|	    	|	  string  |
| code  	|	    	|	  string  |
| merchantNo  	|	    	|	  string  |
#### 接口名称:交易明细
- 接口地址:/BizPayment/QueryTransDetails
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| merchantNo  	|	    	|	  string  |
| merchantName  	|	    	|	  string  |
| cardNo  	|	    	|	  string  |
| msg  	|	    	|	  string  |
| memberNo  	|	    	|	  string  |
| details0  	|	    	|	  array<object>  |
| - tranDate  	|	    	|	  string  |
| - tranAmount  	|	    	|	  string  |
| - tranNo  	|	    	|	  string  |
| - tranType  	|	    	|	  string  |
| - terminalNo  	|	    	|	  string  |
| totalNum  	|	    	|	  string  |
| code  	|	    	|	  string  |
## 模块名称: 汇智游船
### 页面名称: 前台
#### 接口名称:预订游船
- 接口地址:/HuizhiBoat/BoatApply/Add
- 接口类型:POST
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| email  	|	  邮箱  	|	  string  |
| username  	|	  用户名  	|	  string  |
| mobile  	|	  手机号  	|	  string  |
| company  	|	  公司名称  	|	  string  |
| dingEndTime  	|	  结束时间  	|	  string  |
| dingNumber  	|	  预订数量  	|	  number  |
| dingStartTime  	|	  开始时间  	|	  string  |
| name  	|	  真实姓名  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| result  	|	    	|	  array<object>  |
| - dingNumber  	|	    	|	  number  |
| - createTime  	|	    	|	  string  |
| - dingEndTime  	|	    	|	  string  |
| - username  	|	    	|	  string  |
| - mobile  	|	    	|	  string  |
| - company  	|	    	|	  string  |
| - email  	|	    	|	  string  |
| - objectid  	|	    	|	  number  |
| - name  	|	    	|	  string  |
| - dingStartTime  	|	    	|	  string  |
| total  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
#### 接口名称:获取时间段
- 接口地址:/HuizhiBoat/BoatApply/GetTimeList
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:我的预订
- 接口地址:/HuizhiBoat/BoatApply/Me/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| isActive  	|	    	|	  object  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
| page  	|	    	|	  number  |
| result0  	|	    	|	  array<object>  |
| - mobile  	|	  手机号  	|	  string  |
| - username  	|	  姓名  	|	  string  |
| - name  	|	  姓名  	|	  string  |
| - email  	|	  邮箱  	|	  string  |
| - dingStartTime  	|	    	|	  string  |
| - company  	|	  公司  	|	  string  |
| - createTime  	|	    	|	  string  |
| - dingEndTime  	|	  时间  	|	  string  |
| - dingNumber  	|	    	|	  number  |
| - objectid  	|	    	|	  number  |
#### 接口名称:查看详情
- 接口地址:/HuizhiBoat/BoatApply/Edit/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
#### 接口名称:取消预约
- 接口地址:/HuizhiBoat/BoatApply/Del/{objectid}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
### 页面名称: 后台
#### 接口名称:获取统计表
- 接口地址:/HuizhiBoat/BoatSchedule/{startDate}/{endDate}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
- 无请求参数
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| 12:00~12:30  	|	    	|	  array<number>  |
| 11:00~11:30  	|	    	|	  array<number>  |
| 11:30~12:00  	|	    	|	  array<number>  |
| 13:30~14:00  	|	    	|	  array<number>  |
| 12:30~13:00  	|	    	|	  array<number>  |
| 10:00~10:30  	|	    	|	  array<number>  |
| 10:30~11:00  	|	    	|	  array<number>  |
| 13:00~13:30  	|	    	|	  array<number>  |
#### 接口名称:游船列表
- 接口地址:/HuizhiBoat/BoatApply/List/{page}/{pageSize}
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| startTime  	|	  开始时间  	|	  string  |
| endTime  	|	  结束时间  	|	  string  |
| isCancel  	|	  是否取消预约  	|	  boolean  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| total  	|	    	|	  number  |
| result  	|	    	|	  array<object>  |
| - company  	|	  公司  	|	  string  |
| - dingNumber  	|	  预约数量  	|	  number  |
| - dingStartTime  	|	  开始时间  	|	  string  |
| - username  	|	    	|	  string  |
| - mobile  	|	  手机  	|	  string  |
| - name  	|	  姓名  	|	  string  |
| - createTime  	|	    	|	  string  |
| - email  	|	  邮箱  	|	  string  |
| - objectid  	|	    	|	  number  |
| - cardNo  	|	  汇智卡号  	|	  string  |
| - bCancel  	|	    	|	  boolean  |
| - dingEndTime  	|	  结束时间  	|	  string  |
| page  	|	    	|	  number  |
| pagesize  	|	    	|	  number  |
#### 接口名称:游船列表-导出
- 接口地址:/HuizhiBoat/BoatApply/Export/List
- 接口类型:GET
- 接口描述:
##### 请求参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |
| startTime  	|	  开始时间  	|	  string  |
| endTime  	|	  结束时间  	|	  string  |
##### 响应参数列表:
| 参数列表 | 参数名称 | 参数类型 |
| ------------- | ------------- | --------------- |