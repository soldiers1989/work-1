insert into Menus (MenuId,Name,TheUrl,ParentMenuId) values ('412','产业服务','/Clm/CertificateManage',412);
insert into Menus (MenuId,Name,TheUrl,ParentMenuId,OrderId) values ('413','产证管理','/Clm/CertificateManage',412,1);
insert into Menus (MenuId,Name,TheUrl,ParentMenuId,OrderId) values ('414','人才申报','/Clm/TalentDeclaration',412,2);
insert into Menus (MenuId,Name,TheUrl,ParentMenuId,OrderId) values ('415','走访记录','/Clm/VisitRecord',412,3);
insert into Menus (MenuId,Name,TheUrl,ParentMenuId,OrderId) values ('416','客户培训','/Clm/CustomerTraining',412,4);
insert into Menus (MenuId,Name,TheUrl,ParentMenuId,OrderId) values ('418','专利情况','/Clm/PatentManage',412,6);
insert into Menus (MenuId,Name,TheUrl,ParentMenuId,OrderId) values ('419','项目申报','/Clm/ProjectApply',412,7);
insert into Menus (MenuId,Name,TheUrl,ParentMenuId,OrderId) values ('420','资质获批管理','/Clm/QualificationApprovedManagement',413,8);
insert into Menus (MenuId,Name,TheUrl,ParentMenuId,OrderId) values ('421','药品临床批件','/Clm/DrugClinicalApproval',413,9);
insert into Menus (MenuId,Name,TheUrl,ParentMenuId,OrderId) values ('422','医疗器械生产许可证','/Clm/MedicalDeviceProduction',413,10);


INSERT INTO CommonEnumObjects (DisplayName,ObjectName,CreatedTime,Creator,TenantCompanyId) VALUES ('学业水平','AcademicLevel',getdate(),'admin',1);
INSERT INTO CommonEnumObjects (DisplayName,ObjectName,CreatedTime,Creator,TenantCompanyId) VALUES ('人才类型','QLevel',getdate(),'admin',1);
INSERT INTO CommonEnumObjects (DisplayName,ObjectName,CreatedTime,Creator,TenantCompanyId) VALUES ('级别','TalentType',getdate(),'admin',1);

insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('大专',1,18,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('本科',1,18,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('博士',1,18,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('硕士',1,18,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('其他',1,18,getdate(),'admin',1);

insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('千人计划',1,19,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('省双创',1,19,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('姑苏领军',1,19,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('姑苏天使计划',1,19,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('园区领军',1,19,getdate(),'admin',1);

insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('国家',1,20,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('省',1,20,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('市',1,20,getdate(),'admin',1);
insert into CommonEnumFileds (Name,IsValid,BelongCommonEnumObjectId,CreatedTime,Creator,TenantCompanyId) values ('区',1,20,getdate(),'admin',1);