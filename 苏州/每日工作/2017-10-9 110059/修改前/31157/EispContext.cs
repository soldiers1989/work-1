//-----------------------------------------------------------------------
// <copyright file="EispContext.cs" company="Wiseonline">
//     Copyright (c) Shanghai Pudong Software Park Wiseonline Softeware,Inc. All rights reserved.
// </copyright>
// <author>XiaoMouzhi</author>
//-----------------------------------------------------------------------
using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity;
using Wiseonline.Eisp.Domain;
using Wiseonline.Eisp.Domain.ClmDomains.BaseBusiness;
using Wiseonline.Eisp.Domain.ClmDomains.Contract;
using Wiseonline.Eisp.Domain.Common;
using Wiseonline.Eisp.Domain.ContractManagement;
using Wiseonline.Eisp.Domain.EispDomain.Reports;
using Wiseonline.Eisp.Utility;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Data.Objects;
using System.Data;
using System.Data.Entity.Infrastructure;
using System.Security.Principal;
using System.Threading;
using System.Data.Common;
namespace Wiseonline.Eisp.Data
{
    /// <summary>
    /// Eisp 数据操作上下文,实现工作单元模式和存储模式
    /// </summary>
    public class EispContext : DbContext
    {
        public EispContext(string connectionString) : base(connectionString) { }
        public DbSet<Category> Categories { get; set; }
        public DbSet<CommonType> CommonTypes { get; set; }
        public DbSet<Expense> Expenses { get; set; }
        public DbSet<User> Users { get; set; }
        public DbSet<EhrUserInfo> EhrUserInfoes { get; set; }
        public DbSet<Role> Roles { get; set; }
        public DbSet<Company> Companys { get; set; }
        public DbSet<Duty> Dutys { get; set; }
        public DbSet<Department> Depts { get; set; }
        public DbSet<ActionPermission> ActionPermissions { get; set; }
        public DbSet<ConferenceRoom> ConferenceRoom { get; set; }
        public DbSet<DBAudit> DBAudits { get; set; }
        public DbSet<LoginLog> LoginLogs { get; set; }
        public DbSet<VacationModifyLog> VacationModifyLogs { get; set; }
        public DbSet<SubGroup> SubGroups { get; set; }
        public DbSet<DataPermission> DataPermissions { get; set; }
        public DbSet<Menu> Menus { get; set; }
        public DbSet<VideoInfo> VideoInfos { get; set; }
        public DbSet<PivotGridTemplate> PivotGridTemplates { get; set; }

        public DbSet<Report> Reports { get; set; }
        public DbSet<ReportCategoryMainType> ReportCategoryMainTypes { get; set; }
        public DbSet<ReportCategoryType> ReportCategoryTypes { get; set; }
        public DbSet<ReportBaseInfo> ReportBaseInfoes { get; set; }
        public DbSet<ReportIssueTemplate> ReportIssueTemplates { get; set; }
        public DbSet<ConsolidationScopeBriefMonthlyRecord> ConsolidationScopeBriefMonthlyRecords { get; set; }
        public DbSet<ConsolidationScopeMonthlyRecord> ConsolidationScopeMonthlyRecords { get; set; }
        public DbSet<ProjectCategory> ProjectCategories { get; set; }
        public DbSet<FinIndiMonthlyRecord> FinIndiMonthlyRecords { get; set; }
        public DbSet<MajorProduceMonthlyRecord> MajorProduceMonthlyRecords { get; set; }
        public DbSet<UncoFinIndiMonthlyRecord> UncoFinIndiMonthlyRecords { get; set; }
        public DbSet<ParkScaleDevelopmentAnnualRecord> ParkScaleDevelopmentAnnualRecords { get; set; }
        public DbSet<IndustryDevSemiannualRecord> IndustryDevSemiannualReports { get; set; }
        public DbSet<Park> Parks { get; set; }
        public DbSet<ZuChongzhiParkBuildingandHouseNoRecord> ZuChongzhiParkBuildingandHouseNoRecords { get; set; }
        public DbSet<VehicleCategory> VehicleCategories { get; set; }
        public DbSet<VehicleUseExpensesMonthlyRecord> VehicleUseExpensesMonthlyRecords { get; set; }
        public DbSet<NewFixedAssetsMonthlyRecord> NewFixedAssetsMonthlyReports { get; set; }
        public DbSet<HouseInfoAnnualRecord> HouseInfoAnnualReports { get; set; }
        public DbSet<LoansMonthlyRecord> LoansMonthlyRecords { get; set; }
        public DbSet<ContractImplementationQuarterlyRecord> ContractImplementationQuarterlyRecords { get; set; }
        public DbSet<BiddingProjectQuarterlyRecord> BiddingProjectQuarterlyRecords { get; set; }
        public DbSet<ProjectImageScheduleMonthlyRecord> ProjectImageScheduleMonthlyRecords { get; set; }
        public DbSet<CarConsumptionandIssuingMonthlyRecord> CarConsumptionandIssuingMonthlyRecords { get; set; }
        public DbSet<EnergyUseQuarterlyRecord> EnergyUseQuarterlyRecords { get; set; }
        public DbSet<FixedAssetInvestmentMonthlyRecord> FixedAssetInvestmentMonthlyRecords { get; set; }
        public DbSet<FixedAssetInvestmentYearlyModeRecord> FixedAssetInvestmentYearlyModeRecords { get; set; }
        public DbSet<FinancialExcelMonthReport> FinancialExcelMonthReports { get; set; }
        public DbSet<Workflow> Workflows { get; set; }
        public DbSet<WorkflowStatus> WorkflowStatus { get; set; }
        public DbSet<WorkflowRule> WorkflowRules { get; set; }
        public DbSet<ConcreteWorkflowOperation> Operations { get; set; }
        public DbSet<DelegateSetting> DelegateSettings { get; set; }
        public DbSet<FormForWorkflow> FormForWorkflows { get; set; }
        public DbSet<ConcreteWorkflow> ConcreteWorkflows { get; set; }
        public DbSet<MessageTemplate> MessageTemplates { get; set; }
        public DbSet<PassRoundForPerusal> PassRoundForPerusals { get; set; }
        public DbSet<CustomerSuggestion> CustomerSuggestions { get; set; }
        public DbSet<WorkflowRuleDefault> WorkflowRuleDefaults { get; set; }

        public DbSet<PromulgatedMessage> PromulgatedMessages { get; set; }
        public DbSet<CompanyBulletin> CompanyBulletin { get; set; }
        public DbSet<PromulgatePartyMessage> PromulgatePartyMessages { get; set; }
        public DbSet<SpspPartyBuild> SpspPartyBulids { get; set; }

        public DbSet<ServiceCategory> ServiceCategories { get; set; }
        public DbSet<ServiceProject> ServiceProjects { get; set; }
        public DbSet<ServiceModuleFrequencyRecord> ServiceModuleFrequencyReports { get; set; }

        public DbSet<QuestionMainCategory> QuestionMainCategories { get; set; }
        public DbSet<QuestionCategory> QuestionCategories { get; set; }
        public DbSet<CustomerQuestionRecord> CustomerQuestionReports { get; set; }

        public DbSet<TalkingProjectRecord> TalkingProjectReports { get; set; }
        public DbSet<NewlyCustomerRecord> NewlyCustomerReports { get; set; }
        public DbSet<ReceivableAndReceivedAnalysisRecord> ReceivableAndReceivedAnalysisRecords { get; set; }
        public DbSet<YearlyPlanAnalysisRecord> YearlyPlanAnalysisRecords { get; set; } 

        public DbSet<CanvassBusinessMonthlyRecord> CanvassBusinessMonthlyReports { get; set; }
        public DbSet<ProjectDevScheduleQuarterlyRecord> ProjectDevScheduleQuarterlyReports { get; set; }
        public DbSet<ConstructProjectAuditRecord> ConstructProjectAuditRecords { get; set; }
        public DbSet<PurchaseMainCategory> PurchaseMainCategories { get; set; }
        public DbSet<PurchaseCategory> PurchaseCategories { get; set; }
        public DbSet<PurchaseItem> PurchaseItems { get; set; }
        public DbSet<PurchaseMonthlyRecord> PurchaseMonthlyRecords { get; set; }

        public DbSet<News> Newses { get; set; }
        //综合信息回复类
        public DbSet<NewsReply> NewsReplies { get; set; }

        public DbSet<ConstType> ConstTypes { get; set; }
        public DbSet<BusinessState> BusinessStates { get; set; }
        public DbSet<BusinessStateType> BusinessStateTypes { get; set; }
        /// <summary>
        /// 内网邮件 Added by luyf 2012-11-29
        /// </summary>
        public DbSet<IntranetMail> IntranetMails { get; set; }
        /// <summary>
        /// 日常接待
        /// </summary>
        public DbSet<DailyReception> DailyReceptions { get; set; }
        public DbSet<Area> Areas { get; set; }
        /// <summary>
        /// 个人邮件状态
        /// </summary>
        public DbSet<PersonMailState> PersonMailStates { get; set; }
        public DbSet<Mail> BI_Mail { get; set; }
        public DbSet<MailAttach> BI_MailAttach { get; set; }
        public DbSet<StaffClub> StaffClubs { get; set; }
        public DbSet<KnowledgeCenter> KnowledgeCenters { get; set; }
        public DbSet<DimAssets> DimAssetss { get; set; }
        public DbSet<DimCustomer> DimCustomers { get; set; }
        public DbSet<DimHouse> DimHouses { get; set; }
        public DbSet<FactAssetsSummary> FactAssetsSummarys { get; set; }
        public DbSet<FactRentalHouseSummary> FactRentalHouseSummarys { get; set; }
        public DbSet<FactRentSummary> FactRentSummarys { get; set; }
        public DbSet<Attachment> Attachments { get; set; }
        public DbSet<AttachmentLog> AttachmentLogs { get; set; }
        public DbSet<Favorite> Favorites { get; set; }

        // 业务分析 房屋租赁分析
        public DbSet<RentalHouse> RentalHouses { get; set; }

        public DbSet<CustomerType> CustomerTypes { get; set; }
        // 业务分析 资产价值分析
        public DbSet<AssetsValueAndService> AssetsValueAndServices { get; set; }

        public DbSet<AssetFeature> AssetFeatures { get; set; }
        public DbSet<CustomerReceptionRecord> CustomerReceptionRecords { get; set; }
        public DbSet<RegisterCapitalRecord> RegisterCapitalRecords { get; set; }
        public DbSet<ReportPermission> ReportPermissions { get; set; }
        public DbSet<CanvassBusinessRecord> CanvassBusinessRecords { get; set; }
        public DbSet<Report_RentState> Report_RentStates { get; set; }
        public DbSet<Report_RentType> Report_RentTypes { get; set; }
        public DbSet<NewlyRentRecord> NewlyRentRecords { get; set; }

        //public DbSet<UserHandleRule> UserHandleRules { get; set; }
        //public DbSet<RoleHandleRule> RoleHandleRules { get; set; }


        //**年公司新闻见报情况季报 Company News newspapers quarterly
        //园区内软件企业发展情况半年报 The development of semi-annual report of the software companies in the park
        //园区资质、荣誉情况半年报 Park qualification situation semiannual reports
        //股东情况年报 Shareholders Annual Report
        //下属控参股公司情况年报 Under the control of the shares of the company report
        //////历年主要经济指标年报
        //人员结构情况月报 Personnel structure situation quarterly
        //知识产权情况年报 Intellectual property situation report
        //应收账款净额与存货增减变化情况月报 Accounts receivable, net changes in inventories Monthly changes
        public DbSet<NewsQuarterlyRecord> NewsQuarterlyRecords { get; set; }
        public DbSet<EnterpriseDevelopmentSemiannuallyRecord> EnterpriseDevelopmentSemiannuallyRecords { get; set; }
        public DbSet<ParkQualificationSemiannuallyRecord> ParkQualificationSemiannuallyRecords { get; set; }
        public DbSet<ShareholdersYearlyRecord> ShareholdersYearlyRecords { get; set; }
        public DbSet<UnderControlSharesCompanyRecord> UnderControlSharesCompanyRecords { get; set; }
        public DbSet<YearsEconomicIndicatorRecord> YearsEconomicIndicatorRecords { get; set; }
        public DbSet<PersonnelStructureQuarterlyRecord> PersonnelStructureQuarterlyRecords { get; set; }
        public DbSet<IntellectualPropertyYearlyRecord> IntellectualPropertyYearlyRecords { get; set; }
        public DbSet<AccountsReceivableChangesMonthlyRecord> AccountsReceivableChangesMonthlyRecords { get; set; }


        public DbSet<DeptCategory> DeptCategories { get; set; }
        public DbSet<WebSiteHitsMonthRecord> WebSiteHitsMonthRecords { get; set; }
        public DbSet<FixedAssetsStatisticsRecord> FixedAssetsStatisticsRecords { get; set; }
        public DbSet<FixedAssetsScrapInfoRecord> FixedAssetsScrapInfoRecords { get; set; }
        public DbSet<FixedAssetsTransferRecord> FixedAssetsTransferRecords { get; set; }
        public DbSet<LowValueAssetsCategory> LowValueAssetsCategories { get; set; }
        public DbSet<PurchaseLowValueAssetsRecord> PurchaseLowValueAssetsRecords { get; set; }
        public DbSet<OfficeEquipmentApplyDeptCategory> OfficeEquipmentApplyDeptCategories { get; set; }
        public DbSet<ApplyForOfficeEquipmentRecord> ApplyForOfficeEquipmentRecords { get; set; }
        public DbSet<CostManagementRecord> CostManagementRecords { get; set; }
        public DbSet<StaffSalaryRecord> StaffSalaryRecords { get; set; }
        public DbSet<TeamBuildingExpenseRecord> TeamBuildingExpenseRecords { get; set; }
        public DbSet<SalaryCategory> SalaryCategorys { get; set; }
        public DbSet<CostItemCategory> CostItemCategorys { get; set; }
        public DbSet<WarningValue> WaringValues { get; set; }
        public DbSet<Financial> Financials { get; set; }
        public DbSet<Schedule> Schedule { get; set; }
        public DbSet<Calendar> BI_Calendar { get; set; }
        public DbSet<CalendarAccess> BI_CalendarAccess { get; set; }
        public DbSet<MeetRoom> BI_MeetRoom { get; set; }
        //部门报表,参控股企业报表
        public DbSet<DepartmentReportType> DepartmentReportTypes { get; set; }
        public DbSet<DepartmentReport> DepartmentReports { get; set; }
        public DbSet<CompanyReportType> CompanyReportTypes { get; set; }
        public DbSet<CompanyReport> CompanyReports { get; set; }
        public DbSet<RegisterCompanyRecord> RegisterCompanyRecords { get; set; }
        public DbSet<ExpiredContractRecord> ExpiredContractRecords { get; set; }

        public DbSet<ReportAutoIssueConfig> ReportAutoIssueConfiges { get; set; }

        //公司文化
        public DbSet<Culture> Culture { get; set; }

        //商业中心审批
        public DbSet<BizCenterContract> BizCenterContracts { get; set; }

        //论坛
        public DbSet<ForumUser> ForumUsers { get; set; }
        public DbSet<ForumBoard> ForumBoards { get; set; }
        public DbSet<ForumReply> ForumReplys { get; set; }
        public DbSet<ForumTopic> ForumTopics { get; set; }
        public DbSet<ForumVote> ForumVotes { get; set; }
        public DbSet<ForumPraise> ForumPraises { get; set; }
        public DbSet<UserVote> UserVotes { get; set; }

        //我要报修
        public DbSet<Question> Questions { get; set; }
        public DbSet<CustomerComment> CustomerComments { get; set; }
        public DbSet<CustomerVisitRecord> CustomerVisitRecords { get; set; }
        public DbSet<QuestionAssignHistory> QuestionAssignHistories { get; set; }
        public DbSet<QuestionReply> QuestionReplies { get; set; }
        public DbSet<QuestionPraise> QuestionPraise { get; set; }
        public DbSet<QuestionAttention> QuestionAttentions { get; set; }

        //服务中心工作流程
        public DbSet<SCElse> SCElses { get; set; }
        public DbSet<AreaUse> AreaUses { get; set; }
        public DbSet<BillingApply> BillingApplies { get; set; }
        public DbSet<BillProjectType> BillProjectTypes { get; set; }
        public DbSet<ParkingRefund> ParkingRefunds { get; set; }
        public DbSet<PartyAndUnion> PartyAndUnions { get; set; }
        public DbSet<DeclareReport> DeclareReports { get; set; }
        public DbSet<CompanyRegister> CompanyRegisters { get; set; }
        public DbSet<CompanyRegisterRecord> CompanyRegisterRecords { get; set; }
        public DbSet<RegisteredLetter> RegisteredLetters { get; set; }
        public DbSet<DeliverInformation> DeliverInformations { get; set; }
        public DbSet<DeliverInfoDetail> DeliverInfoDetails { get; set; }



        //联系人查看权限
        public DbSet<ContactsPermission> ContactsDetailCompanyPermissions { get; set; }

        ////积分来源
        //public DbSet<PointsSource> PointsSources { get; set; }

        //短信
        public DbSet<MobileMessage> MobileMessages { get; set; }

        //当前活动主题(综合信息模块)
        public DbSet<CurrentTitle> CurrentTitles { get; set; }


        //党支部
        public DbSet<PartyBranch> PartyBranchs { get; set; }

        //党务简报收集单
        public DbSet<PartyAffairsBriefing> PartyAffairsBriefings { get; set; }
        //IT技术服务流程单
        public DbSet<ITTechnicalServiceProcess> ITTechnicalServiceProcesses { get; set; }
        //IT收文
        public DbSet<ReceiveDoc> ReceiveDocs { get; set; }
        //大型活动
        public DbSet<ActivityApply> ActivityApplies { get; set; }
        //大型活动
        public DbSet<HoldAddress> HoldAddresses { get; set; }
        public DbSet<MainGuestsAndContact> MainGuestsAndContacts { get; set; }
        //信息系统及权限申请流程
        public DbSet<InformationAndPermissionApply> InformationAndPermissionApplies { get; set; }
        //用印（总部）
        public DbSet<SealHQApply> SealHQApplies { get; set; }
        //用印（控参股企业）
        public DbSet<SealHSEApply> SealHSEApplies { get; set; }

        //建设工程费用付费申请凭证
        public DbSet<ConstructionProjectPaymentRequestDoc> ConstructionProjectPaymentRequestDocs { get; set; }

        //任务督办流程单
        public DbSet<TaskOversee> TaskOversees { get; set; }
        //任务催办流程单
        public DbSet<TaskUrge> TaskUrges { get; set; }

        //数据修改申请
        public DbSet<DataModificationApplication> DataModificationApplications { get; set; }

        //投资协议审批单
        public DbSet<InvestmentAgreement> InvestmentAgreements { get; set; }

        //内部工作报批单
        public DbSet<InternalWorkApproval> InternalWorkApprovals { get; set; }
        //控参股企业工作报批单
        public DbSet<NewAndControllingInternalApproval> NewAndControllingInternalApprovals { get; set; }
        //控参股企业内部工作报告
        public DbSet<AccusedHoldingCompaniesWorkReport> AccusedHoldingCompaniesWorkReports { get; set; }
        //公司内部工作报告
        public DbSet<InternalReport> InternalReports { get; set; }
        public DbSet<RegisterCompanyRecordStatType> RegisterCompanyRecordStatTypes { get; set; }
        public DbSet<MergePackage> MergePackages { get; set; }
        //停电/解除合同通知
        public DbSet<RescindContract> RescindContracts { get; set; }
        //三全系统问题处理单
        public DbSet<EispProblemProcess> EispProblemProcesses { get; set; }

        #region 新增流程

        //接待报批单
        public DbSet<ReceptionAndApproval> ReceptionAndApprovals { get; set; }
        //来宾/陪同
        public DbSet<GuestsAndAccompanying> GuestsAndAccompanyings { get; set; }
        //日程安排
        public DbSet<ReceptionAndApprovalSchedule> ReceptionAndApprovalSchedules { get; set; }

        //董事议案呈报
        public DbSet<DirectorMeeting> DirectorMeetings { get; set; }

        #endregion

        //汇智工作流
        public DbSet<Egression> WiseonlineEgressions { get; set; }
        public DbSet<OverTimeWork> OverTimeWorks { get; set; }
        public DbSet<SealProcess> SealProcesses { get; set; }
        public DbSet<ReleaseOnline> ReleaseOnlines { get; set; }
        public DbSet<VehicleUsing> VehicleUsings { get; set; }
        public DbSet<TerminateVacation> TerminateVacations { get; set; }
        public DbSet<DelegationAuthorization> DelegationAuthorizations { get; set; }
        public DbSet<AskingForSignReport> AskingForSignReports { get; set; }
        public DbSet<EntertainMealApplication> EntertainMealApplications { get; set; }

        //Excel导入
        //public DbSet<ImportExcelData> ImportExcelDatas { get; set; }
        public DbSet<ImportExcelConfiguration> ImportExcelConfigurations { get; set; }
        public DbSet<ImportExcelConfigDetail> ImportExcelConfigDetails { get; set; }
        public DbSet<ImportExcelExKeyValue> ImportExcelExKeyValues { get; set; }

        //非法字符注入排除
        public DbSet<CheckBadStrException> CheckBadStrExceptions { get; set; }

        /// <summary>
        /// 休假申请单
        /// </summary>
        public DbSet<Vacation> Vacations { get; set; }

        /// <summary>
        /// 浦软休假申请单
        /// </summary>
        public DbSet<SpspVacation> SpspVacations { get; set; }

        /// <summary>
        /// 固定资产申购单
        /// </summary>
        public DbSet<FixedAssetsPurchase> FixedAssetsPurchase { get; set; }

        /// <summary>
        /// 采购申购单
        /// </summary>
        public DbSet<PurchaseRequest> PurchaseRequests { get; set; }

        /// <summary>
        /// 供应商评审
        /// </summary>
        public DbSet<SupplyReview> SupplyReviews { get; set; }
        public DbSet<Supply> Supplies { get; set; }

        /// <summary>
        /// 合同审批单
        /// </summary>
        public DbSet<ContractApproval> ContractApprovals { get; set; }

        /// <summary>
        /// 办公用品采购单
        /// </summary>
        public DbSet<OfficeSuppliesPurchase> OfficeSuppliesPurchases { get; set; }

        /// <summary>
        /// 比价分析单
        /// </summary>
        public DbSet<PriceAnalysis> PriceAnalysis { get; set; }

        /// <summary>
        /// 项目采购单
        /// </summary>
        public DbSet<ProjectPurchaseApproval> ProjectPurchaseApprovals { get; set; }

        /// <summary>
        /// 物业合同指派人员配置信息
        /// </summary>
        public DbSet<PropertyContractAssignerConfig> PropertyContractAssignerConfigs { get; set; }

        /// <summary>
        /// 培训审批
        /// </summary>
        public DbSet<TrainingApproval> TrainingApprovals { get; set; }

        /// <summary>
        /// 培训反馈
        /// </summary>
        public DbSet<TrainingFeedback> TrainingFeedbacks { get; set; }

        /// <summary>
        /// 发文打印
        /// </summary>
        public DbSet<PromulgatePrint> PromulgatePrints { get; set; }

        /// <summary>
        /// 汇智内部项目流程
        /// </summary>
        public DbSet<WizProjectEstablishment> WizProjectEstablishments { get; set; }
        public DbSet<WizProjectEstablishmentCost> WizProjectEstablishmentCosts { get; set; }
        public DbSet<WizProjectEstablishmentPlan> WizProjectEstablishmentPlans { get; set; }
        public DbSet<WizProjectChange> WizProjectChanges { get; set; }
        public DbSet<WizProjectComplete> WizProjectCompletes { get; set; }
        public DbSet<WizProjectCloseOut> WizProjectCloseOuts { get; set; }
        public DbSet<WizProjectCloseOutCost> WizProjectCloseOutCosts { get; set; }
        public DbSet<WizProjectCloseOutIncome> WizProjectCloseOutIncomes { get; set; }
        public DbSet<WizProjectEvaluation> WizProjectEvaluations { get; set; }
        public DbSet<WizProjectEvaluationPoint> WizProjectEvaluationPoints { get; set; }

        #region 活动报名
        public DbSet<CustomerEvent> CustomerEvents { get; set; }
        public DbSet<CustomerEventAudit> CustomerEventAudits { get; set; }
        public DbSet<CustomerEventCorpInfo> CustomerEventCorpInfos { get; set; }
        #endregion

        #region 客户危废物管理
        public DbSet<WasteManage> WasteManages { get; set; }
        #endregion
        #region 客户隐患排查管理
        public DbSet<TroubleManage> TroubleManages { get; set; }
        #endregion
        #region 客户培训记录管理
        public DbSet<TrainManage> TrainManages { get; set; }
        #endregion

        #region 竞争对手分析
        public DbSet<CompetitorAnalysis> CompetitorAnalysis { get; set; }
        #endregion

        #region Clm
        //两全表
        public DbSet<ArrearageCall> ArrearageCalls { get; set; }
        public DbSet<ArrearageRecord> ArrearageRecords { get; set; }
        public DbSet<RegisterContract> RegisterContracts { get; set; }
        public DbSet<TenancyContract> TenancyContract { get; set; }
        public DbSet<LetterOfIntent> LetterOfIntents { get; set; }
        public DbSet<AdditionalRemark> AdditionalRemarks { get; set; }
        public DbSet<LandPartition> LandPartitions { get; set; }
        public DbSet<LandProject> LandProjects { get; set; }
        public DbSet<BookRoomInfo> BookRoomInfos { get; set; }
        public DbSet<BookRoomDetail> BookRoomDetails { get; set; }
        public DbSet<Building> Buildings { get; set; }
        public DbSet<CorpBaseInfo> CorpBaseInfos { get; set; }
        public DbSet<CustomerEnterpriseInfo> CustomerEnterpriseInfos { get; set; }
        public DbSet<CustomerReception> CustomerReception { get; set; }
        public DbSet<QuestionBank> QuestionBanks { get; set; }
        public DbSet<CustomerReceptionRoom> CustomerReceptionRoom { get; set; }
        public DbSet<Floor> Floors { get; set; }
        public DbSet<Room> Rooms { get; set; }
        public DbSet<PaymentInfo> PaymentInfos { get; set; }
        public DbSet<PaymentInfoDetail> PaymentInfoDetails { get; set; }
        public DbSet<MarginPayDeficit> MarginPayDeficits { get; set; }
        public DbSet<MarginPayRecord> MarginPayRecords { get; set; }
        public DbSet<Meter> Meters { get; set; }
        public DbSet<SwitchNumberBoxMaintenance> SwitchNumberBoxMaintenances { get; set; }
        public DbSet<Refitment> Refitmentse { get; set; }
        public DbSet<CertificateManage> CertificateManages { get; set; }
        public DbSet<ContactWay> ContactWays { get; set; }
        public DbSet<ShareholderInfo> ShareholderInfoes { get; set; }
        public DbSet<TeamInfo> TeamInfoes { get; set; }
        public DbSet<InvestmentAndFinancing> InvestmentAndFinancings { get; set; }
        public DbSet<BasicSituation> BasicSituations { get; set; }
        public DbSet<ReceivableAdjustment> ReceivableAdjustments { get; set; }
        public DbSet<ReceivableAdjustmentRecord> ReceivableAdjustmentRecords { get; set; }
        public DbSet<RentCollectRateHistory> RentCollectRateHistorys { get; set; }
        public DbSet<RentCollectRateHistoryDetail> RentCollectRateHistoryDetails { get; set; }

        public DbSet<PropertyContract> PropertyContracts { get; set; }
        public DbSet<PropertyContractMeter> PropertyContractMeters { get; set; }
        public DbSet<PropertyContractChildMeterRecord> PropertyContractChildMeterRecords { get; set; }
        public DbSet<PropertyAccount> PropertyAccounts { get; set; }
        public DbSet<TenancyAccount> TenancyAccounts { get; set; }
        public DbSet<EnergyConsumptionAccount> EnergyConsumptionAccounts { get; set; }
        public DbSet<ReceivableAndReceivedAccount> ReceivableAndReceivedAccounts { get; set; }
        public DbSet<MeterRecord> MeterRecords { get; set; }
        public DbSet<AdditionAirConditionRecord> AdditionAirConditionRecords { get; set; }
        public DbSet<SerialNumber> SerialNumbers { get; set; }

        public DbSet<StokeHolderCorpInfo> StokeHolderCorpInfos { get; set; }
        public DbSet<EnterPriceDataForDevPlan> EnterPriceDataForDevPlans { get; set; }
        public DbSet<BusinessFinance> BusinessFinances { get; set; }
        public DbSet<HumanResource> HumanResources { get; set; }
        public DbSet<IntellectualProperty> IntellectualProperties { get; set; }
        public DbSet<CorpAptitude> CorpAptitudes { get; set; }

        public DbSet<BusinessDataPermission> BusinessDataPermissions { get; set; }
        public DbSet<CorpContactRecord> CorpContactRecords { get; set; }
        public DbSet<CorpDemandsProcess> CorpDemandsProcesses { get; set; }
        public DbSet<CorpIndustryInformation> CorpIndustryInformations { get; set; }
        public DbSet<AdvanceChargeChageRecord> AdvanceChargeChageRecords { get; set; }


        #region 补充合同相关

        public DbSet<SupplementaryAgreement> SupplementaryAgreements { get; set; }

        #endregion

        #region 销售合同相关

        public DbSet<SalesAgreement> SalesAgreements { get; set; }
        public DbSet<SalesAgreementRoom> SalesAgreementRooms { get; set; }
        public DbSet<ReceiveInfo> ReceiveInfos { get; set; }

        #endregion

        public DbSet<Eviction> Evictions { get; set; }
        public DbSet<EvictionReason> EvictionReasons { get; set; }
        public DbSet<CustomerManagerWorkflow> CustomerManagerWorkflows { get; set; }
        public DbSet<CommercialPlazaMonthlySaleRecord> CommercialPlazaMonthlySaleRecords { get; set; }

        #region 商业广场

        public DbSet<PricingConfigModel> PricingConfigModels { get; set; }
        public DbSet<PricingConfigField> PricingConfigFields { get; set; }
        public DbSet<CommercialPlazaChargePay> CommercialPlazaChargePays { get; set; }
        public DbSet<Invoice> Invoices { get; set; }
        public DbSet<CheckRoom> CheckRooms { get; set; }
        #endregion

        #endregion

        #region Ecm

        public DbSet<CorpTrader> CorpTraders { get; set; }
        public DbSet<VendorReevaluationRecord> VendorReevaluationRecords { get; set; }
        public DbSet<VendorEvaluationRecord> VendorEvaluationRecords { get; set; }
        public DbSet<PaymentSchedulePlan> PaymentSchedulePlans { get; set; }
        public DbSet<GetPaymentSchedulePlan> GetPaymentSchedulePlans { get; set; }
        public DbSet<EnterPriceContract> EnterPriceContracts { get; set; }
        public DbSet<ProjectInfo> ProjectInfos { get; set; }
        public DbSet<BaseContractTypeLevelTwo> BaseContractTypeLevelTwos { get; set; }
        public DbSet<OpeningBank> OpeningBank { get; set; }
        public DbSet<BaseContractCore> BaseContractCores { get; set; }
        public DbSet<CustomerServiceBusinessProcess> CustomerServiceBusinessProcess { get; set; }
        public DbSet<NoteRegister> NoteRegisters { get; set; }
        public DbSet<EnterPriceContractAccount> EnterPriceContractAccounts { get; set; }
        public DbSet<PaymentConfirm> PaymentConfirms { get; set; }
        public DbSet<GetPaymentConfirm> GetPaymentConfirms { get; set; }
        public DbSet<HistoryEnterPriceContractFromSpsp> HistoryEnterPriceContractFromSpsp { get; set; }
        public DbSet<WiseOnlineCustomContract> WiseOnlineCustomContract { get; set; }
        public DbSet<MakeContractInvalid> MakeContractInvalid { get; set; }
        public DbSet<Watermark> Watermark { get; set; }

        #endregion

        #region Alm

        //public DbSet<AssetsCard> AssetsCards { get; set; }
        public DbSet<CheckRule> CheckRules { get; set; }
        //public DbSet<ReceiveRecordAssetsCard> ReceiveRecordAssetsCards { get; set; }
        public DbSet<ServiceRule> ServiceRules { get; set; }


        public DbSet<GovernmentDepartment> GovernmentDepartments { get; set; }
        public DbSet<Vendor> Vendors { get; set; }
        public DbSet<CorpEventData> CorpEventDatas { get; set; }
        public DbSet<VendorQualification> VendorQualifications { get; set; }

        #region 资产各类信息处理

        public DbSet<AssetsInvalidInfo> AssetsInvalidInfos { get; set; }
        public DbSet<AssetsRepairmentInfo> AssetsRepairmentInfos { get; set; } 

        #endregion

        #region 资产模型

        public DbSet<BaseAssets> BaseAssets { get; set; }
        public DbSet<FixedAssets> FixedAssets { get; set; }
        public DbSet<FixedRelateBaseAssets> FixedRelateBaseAssets { get; set; }

        #endregion

        #region 配置模型
        //存储位置
        public DbSet<StorageLocation> StorageLocations { get; set; }

        //单位类型
        public DbSet<UnitCorpType> UnitCorpTypes { get;set; }

        //紧急维修事由
        public DbSet<RepairmentType> RepairmentTypes { get; set; }

        //资产分类
        public DbSet<AssetsCategory> AssetsCategories { get; set; }

        //资产状态
        public DbSet<AssetsStatus> AssetsStatuses { get; set; }

        //资产特征
        public DbSet<AssetsType> AssetsTypes { get; set; }

        #endregion

        #region ALM流程
        //资产申购
        public DbSet<BasePurchaseRequisition> BasePurchaseRequisitions { get; set; }
        public DbSet<CommonPurchaseRequisition> CommonPurchaseRequisitions { get; set; }
        public DbSet<CommonRequisitionItem> CommonRequisitionItems { get; set; }

        //入库登记
        public DbSet<PurchaseOrderManager> PurchaseOrderManagers { get; set; }
        //public DbSet<PurchaseOrder> PurchaseOrders { get; set; }

        //资产验收
        public DbSet<CommonAssetsCheck> CommonAssetsChecks { get; set; }
        //public DbSet<CommonAssetsCheckRecord> CommonAssetsCheckRecords { get; set; }

        //资产领用
        public DbSet<ReceiveRecord> ReceiveRecords { get; set; }
        public DbSet<ReceiveRecordDetail> ReceiveRecordDetails { get; set; }

        //资产维修
        public DbSet<RepairmentApplyOfBusinessAssets> RepairmentApplyOfBusinessAssets { get; set; }
        public DbSet<RepairmentApplyOfBusinessAssetsDetail> RepairmentApplyOfBusinessAssetsDetails { get; set; }

        //资产调拨
        //public DbSet<BaseAssetsAllocationRecord> BaseAssetsAllocationRecords { get; set; }
        public DbSet<AssetsAllocationRecord> AssetsAllocationRecords { get; set; }
        public DbSet<AssetsAllocationRecordDetail> AssetsAllocationRecordDetails { get; set; }

        //资产报废
        public DbSet<AssetsInvalidRecord> AssetsInvalidRecords { get; set; }
        public DbSet<AssetsInvalidRecordDetail> AssetsInvalidRecordDetails { get; set; }

        //资产盘点（主流程表）
        public DbSet<StockTakePlan> StockTakePlans { get; set; }
        //部门盘点（子流程表）
        public DbSet<DepartmentStockTakeRecord> DepartmentStockTakeRecords { get; set; }
        //部门盘点关联资产
        public DbSet<DepartmentStockTakeBaseAssets> DepartmentStockTakeBaseAssets { get; set; }
        //资产管理员维护表（针对盘点部门层级不明确）
        public DbSet<AssetsManager> AssetsManagers { get; set; }
        //盘点计划 - 资产管理员维护表中间表
        public DbSet<AssetsManagerPlanRelation> AssetsManagerPlanRelations { get; set; }
        //盘盈表
        public DbSet<InventoryProfit> InventoryProfits { get; set; }

        //中大修
        public DbSet<RepairmentApplyOfYear> RepairmentApplyOfYears { get; set; }
        public DbSet<RepairmentApprovalOfYear> RepairmentApprovalOfYears { get; set; }
        public DbSet<RepairmentResultOfYear> RepairmentResultOfYears { get; set; }

        //保养
        public DbSet<AssetsMaintenance> AssetsMaintenances { get; set; }
        public DbSet<AssetsMaintenanceContract> AssetsMaintenanceContracts { get; set; }
        public DbSet<AssetsMaintenanceDetail> AssetsMaintenanceDetails { get; set; }
        #endregion

        #endregion

        #region sass
        public DbSet<TenantCompany> TenantCompanys { get; set; }
        #endregion

        #region 通用配置

        public DbSet<CommonEnumObject> CommonEnumObjects { get; set; }
        public DbSet<CommonEnumFiled> CommonEnumFileds { get; set; }

        #endregion

        public DbSet<ObjectDataPermission> ObjectDataPermissions { get; set; }

        public DbSet<QuickQueryConfiguration> QuickQueryConfigurations { get; set; }

        public virtual void Commit()
        {
            //base.SaveChanges();
            this.SaveChanges();
        }

        public override int SaveChanges()
        {
            ChangeTracker.DetectChanges();
            System.Data.Entity.Core.Objects.ObjectContext ctx = ((IObjectContextAdapter)this).ObjectContext;
            // string UserName = WindowsIdentity.GetCurrent().Name;
            IPrincipal principal = Thread.CurrentPrincipal;
            IIdentity identity = principal == null ? null : principal.Identity;
            string name = identity == null ? "" : identity.Name;

            //Thread.CurrentPrincipal = new GenericPrincipal(new GenericIdentity((userName), roles);
            List<System.Data.Entity.Core.Objects.ObjectStateEntry> objectStateEntryList =
                ctx.ObjectStateManager.GetObjectStateEntries(System.Data.Entity.EntityState.Added
                                                           | System.Data.Entity.EntityState.Modified
                                                           | System.Data.Entity.EntityState.Deleted)
                .ToList();

            List<DBAudit> AuditList = new List<DBAudit>();

            string Audittable = string.Empty;
            foreach (System.Data.Entity.Core.Objects.ObjectStateEntry entry in objectStateEntryList)
            {
                Audittable = entry.EntitySet.ToString();
                string auditTables = Application.Instance.AuditTables;

                if (!entry.IsRelationship && Audittable != "DBAudits" &&
                auditTables.Split(',').Contains(Audittable))
                {

                    //sIsAuditTble =entry.EntitySet="DBAudit"? true:false;
                    if (!string.IsNullOrEmpty(name))
                        switch (entry.State)
                        {
                            case System.Data.Entity.EntityState.Added:
                                AuditList = LogDetails(entry, name, AuditActions.I);
                                break;
                            case System.Data.Entity.EntityState.Deleted:
                                AuditList = LogDetails(entry, name, AuditActions.D);
                                break;
                            case System.Data.Entity.EntityState.Modified:
                                AuditList = LogDetails(entry, name, AuditActions.U);
                                break;

                        }
                }
            }

            AuditList.ForEach(l => this.DBAudits.Add(l));

            //using (var context = new ProjectTrackerEntities())
            //{
            //    for (int i = 0; i < AuditList.Count; i++)
            //    {
            //        context.DBAudits.Add(AuditList[i]);
            //        context.SaveChanges();
            //    }
            //}
            var result = 0;
#if  DEBUG || EASYTEST// Debug模式下用try-catch捕获异常方便调试, release模式不可用try-catch,避免降低性能
          

            //try
            //{
                result = base.SaveChanges();
            //}
            //catch (Exception e)
            //{
            //    Console.WriteLine(e);
            //}
#else
            try
            {
                result = base.SaveChanges();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
#endif

            return result;

        }


        public enum AuditActions
        {
            /// <summary>
            /// 插入
            /// </summary>
            I,
            /// <summary>
            /// 更新
            /// </summary>
            U,
            /// <summary>
            /// 删除
            /// </summary>
            D
        }
        public List<DBAudit> LogDetails(System.Data.Entity.Core.Objects.ObjectStateEntry entry, string UserName, AuditActions action)
        {
            List<DBAudit> dbAuditList = new List<DBAudit>();

            if (action == AuditActions.I)
            {

                var keyValues = new Dictionary<string, object>();
                var currentValues = entry.CurrentValues;

                // entry.Entity key = new EntityKey();

                DBAudit audit = new DBAudit();
                audit.OperationLogId = Guid.NewGuid();
                audit.RevisionStamp = DateTime.Now;
                audit.TableName = entry.EntitySet.Name;
                audit.TableNameID = entry.EntityKey == null || entry.EntityKey.EntityKeyValues == null ? "" : string.Join(",", entry.EntityKey.EntityKeyValues.Select(e => e.Value.ToString()));
                audit.UserName = UserName;
                audit.OldData = "";
                audit.Actions = action.ToString();
                for (int i = 0; i < currentValues.FieldCount; i++)
                {
                    audit.ChangedColumns = audit.ChangedColumns + currentValues.GetName(i);
                    audit.NewData = audit.NewData + currentValues.GetValue(i);
                    audit.ChangedColumns = audit.ChangedColumns + ", ";
                    audit.NewData = audit.NewData + ", ";
                }
                dbAuditList.Add(audit);
                //LogSave(audit);




            }
            else if (action == AuditActions.D)
            {
                var keyValues = new Dictionary<string, object>();
                var DeletedValues = entry.OriginalValues;

                // entry.Entity key = new EntityKey();


                DBAudit audit = new DBAudit();
                audit.OperationLogId = Guid.NewGuid();
                audit.RevisionStamp = DateTime.Now;
                audit.TableName = entry.EntitySet.Name;
                audit.TableNameID = entry.EntityKey == null || entry.EntityKey.EntityKeyValues == null ? "" : string.Join(",", entry.EntityKey.EntityKeyValues.Select(e => e.Value.ToString()));
                audit.UserName = UserName;
                audit.NewData = "";

                audit.Actions = action.ToString();
                for (int i = 0; i < DeletedValues.FieldCount; i++)
                {
                    audit.ChangedColumns = audit.ChangedColumns + DeletedValues.GetName(i);
                    audit.OldData = audit.OldData + DeletedValues.GetValue(i);
                    audit.ChangedColumns = audit.ChangedColumns + ", ";
                    audit.OldData = audit.OldData + ", ";
                }
                dbAuditList.Add(audit);
            }
            else
            {

                foreach (string propertyName in entry.GetModifiedProperties())
                {
                    //TODO：可捕获从Controller来的Update变化，Service中无效，待处理
                    DbDataRecord original = entry.OriginalValues;
                    string oldValue = original.GetValue(original.GetOrdinal(propertyName)).ToString();

                    System.Data.Entity.Core.Objects.CurrentValueRecord current = entry.CurrentValues;
                    string newValue = current.GetValue(current.GetOrdinal(propertyName)).ToString();
                    if (newValue != oldValue)
                    {
                        DBAudit audit = new DBAudit();

                        audit.OperationLogId = Guid.NewGuid();
                        audit.RevisionStamp = DateTime.Now;
                        audit.TableName = entry.EntitySet.Name;
                        audit.TableNameID = entry.EntityKey == null || entry.EntityKey.EntityKeyValues == null ? "" : string.Join(",", entry.EntityKey.EntityKeyValues.Select(e => e.Value.ToString()));
                        audit.UserName = UserName;
                        audit.ChangedColumns = propertyName;
                        audit.OldData = oldValue;
                        audit.NewData = newValue;
                        audit.Actions = action.ToString();
                        dbAuditList.Add(audit);
                        //LogSave(audit);
                    }

                }

            }
            return dbAuditList;
        }



        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Entity<RegisterContract>().Property(e => e.SignDate).IsOptional();
            //modelBuilder.Entity<RegisterContract>().Property(e => e.PartyBId).IsOptional();
            //modelBuilder.Entity<RegisterContract>().Property(e => e.PartyB).IsOptional();       

            //中间表映射
            modelBuilder.Entity<User>()
                        .HasMany(u => u.Roles)
                        .WithMany(r => r.Users)
                        .Map(m =>
                            {
                                m.ToTable("RoleMemberships");
                                m.MapLeftKey("UserName");
                                m.MapRightKey("RoleName");
                            });

            modelBuilder.Entity<AssetsMaintenance>()
                       .HasMany(u => u.BaseAssets)
                       .WithMany(r => r.AssetsMaintenances)
                       .Map(m =>
                       {
                           m.ToTable("AssetsMaintenancesBelongAssets");
                           m.MapLeftKey("AssetsMaintenanceId");
                           m.MapRightKey("BaseAssetId");
                       });

            modelBuilder.Entity<AssetsMaintenance>()
                       .HasMany(u => u.AssetsMaintenanceContracts)
                       .WithMany(r => r.AssetsMaintenances)
                       .Map(m =>
                       {
                           m.ToTable("AssetsMaintenancesBelongContract");
                           m.MapLeftKey("AssetsMaintenanceId");
                           m.MapRightKey("AssetsMaintenanceContractId");
                       });

            modelBuilder.Entity<User>()
                        .HasMany(w => w.WorkflowRules)
                        .WithMany(r => r.Users)
                        .Map(m =>
                            {
                                m.ToTable("UserWorkflowRules");
                                m.MapLeftKey("UserName");
                                m.MapRightKey("WorkflowRule");
                            });

            modelBuilder.Entity<User>()
                        .HasMany(w => w.Workflows)
                        .WithMany(r => r.Users)
                        .Map(m =>
                            {
                                m.ToTable("UserWorkflows");
                                m.MapLeftKey("UserName");
                                m.MapRightKey("Workflow");
                            });

            modelBuilder.Entity<Role>()
                        .HasMany(w => w.WorkflowRules)
                        .WithMany(r => r.Roles)
                        .Map(m =>
                            {
                                m.ToTable("RoleWorkflowRules");
                                m.MapLeftKey("RoleName");
                                m.MapRightKey("WorkflowRule");
                            });

            modelBuilder.Entity<User>()
                        .HasMany(w => w.Duties)
                        .WithMany(r => r.Users)
                        .Map(m =>
                            {
                                m.ToTable("UserDuty");
                                m.MapLeftKey("UserName");
                                m.MapRightKey("DutyID");
                            });

            modelBuilder.Entity<SubGroup>()
                        .HasMany(w => w.Users)
                        .WithMany(r => r.SubGroups)
                        .Map(m =>
                            {
                                m.ToTable("SubGroupUser");
                                m.MapLeftKey("SubGroupID");
                                m.MapRightKey("UserName");
                            });

            modelBuilder.Entity<MergePackage>()
                        .HasMany(u => u.Reports)
                        .WithMany(r => r.MergePackages)
                        .Map(m =>
                            {
                                m.ToTable("MergeReports");
                                m.MapLeftKey("MergePackageId");
                                m.MapRightKey("ReportId");
                            });

            modelBuilder.Entity<EnterPriceContract>()
                        .HasMany(w => w.ProjectInfos)
                        .WithMany(r => r.EnterPriceContracts)
                        .Map(m =>
                            {
                                m.ToTable("EnterPriceContractProjectInfo");
                                m.MapLeftKey("EnterPriceContractId");
                                m.MapRightKey("ProjectInfoId");
                            });

            modelBuilder.Entity<RepairmentApplyOfYear>()
            .HasMany(w => w.BaseAssets)
            .WithMany(r => r.RepairmentApplyOfYears)
            .Map(m =>
            {
                m.ToTable("RepairmentApplyOfYearAssets");
                m.MapLeftKey("RepairmentApplyOfYearId");
                m.MapRightKey("BaseAssetsId");
            });


            modelBuilder.Entity<RepairmentApprovalOfYear>()
            .HasMany(w => w.BaseAssets)
            .WithMany(r => r.RepairmentApprovalOfYears)
            .Map(m =>
            {
                m.ToTable("RepairmentApprovalOfYearAssets");
                m.MapLeftKey("RepairmentApprovalOfYearId");
                m.MapRightKey("BaseAssetsId");
            });


            /*            modelBuilder.Entity<Role>()
                     .HasMany(w => w.CirculatedWorkflowRules)
                     .WithMany(r => r.Roles)
                     .Map(m =>
                     {
                         m.ToTable("CirculatedWorkflowRules");
                         m.MapLeftKey("WorkflowRule");
                         m.MapRightKey("RoleName");
                     });*/
            modelBuilder.Entity<AccountsReceivableChangesMonthlyRecord>()
                        .Property(e => e.CurrentMonthAmount)
                        .HasPrecision(18, 6);
            modelBuilder.Entity<AccountsReceivableChangesMonthlyRecord>()
                        .Property(e => e.BeginYearAmount)
                        .HasPrecision(18, 6);
            modelBuilder.Entity<AccountsReceivableChangesMonthlyRecord>()
                        .Property(e => e.SamePeriodLastYearAmount)
                        .HasPrecision(18, 6);

            modelBuilder.Entity<YearsEconomicIndicatorRecord>().Property(e => e.TotalIncome).HasPrecision(18, 4);
            modelBuilder.Entity<YearsEconomicIndicatorRecord>().Property(e => e.TotalProfit).HasPrecision(18, 4);
            modelBuilder.Entity<YearsEconomicIndicatorRecord>().Property(e => e.PureProfit).HasPrecision(18, 4);
            modelBuilder.Entity<YearsEconomicIndicatorRecord>().Property(e => e.TotalAssets).HasPrecision(18, 4);
            modelBuilder.Entity<YearsEconomicIndicatorRecord>().Property(e => e.Taxes).HasPrecision(18, 4);
            modelBuilder.Entity<YearsEconomicIndicatorRecord>().Property(e => e.Bonus).HasPrecision(18, 4);

            modelBuilder.Entity<ShareholdersYearlyRecord>().Property(e => e.InvestmentAmount).HasPrecision(18, 4);

            modelBuilder.Entity<FinIndiMonthlyRecord>().Property(e => e.TenYearBudget).HasPrecision(18, 4);
            modelBuilder.Entity<FinIndiMonthlyRecord>().Property(e => e.CurrentMonthAmount).HasPrecision(18, 4);
            modelBuilder.Entity<FinIndiMonthlyRecord>().Property(e => e.SamePeriodLastYear).HasPrecision(18, 4);
            modelBuilder.Entity<FinIndiMonthlyRecord>().Property(e => e.CurrentYearCumulative).HasPrecision(18, 4);

            modelBuilder.Entity<UncoFinIndiMonthlyRecord>().Property(e => e.Amount).HasPrecision(18, 6);

            modelBuilder.Entity<CostManagementRecord>().Property(e => e.Budget).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.January).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.February).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.March).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.April).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.May).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.June).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.July).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.August).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.September).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.October).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.November).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.December).HasPrecision(18, 6);
            modelBuilder.Entity<CostManagementRecord>().Property(e => e.SumTotal).HasPrecision(18, 6);

            modelBuilder.Entity<ConsolidationScopeMonthlyRecord>()
                        .Property(e => e.CurrentYearCumulative)
                        .HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeMonthlyRecord>().Property(e => e.Headquarters).HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeMonthlyRecord>().Property(e => e.WiseSoftware).HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeMonthlyRecord>().Property(e => e.WiseTechnology).HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeMonthlyRecord>().Property(e => e.College).HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeMonthlyRecord>().Property(e => e.Ability).HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeMonthlyRecord>().Property(e => e.Kunshan).HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeMonthlyRecord>().Property(e => e.WisePerson).HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeMonthlyRecord>().Property(e => e.SpspProperty).HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeMonthlyRecord>()
                        .Property(e => e.ConsolidationAdjustments)
                        .HasPrecision(18, 6);

            modelBuilder.Entity<ConsolidationScopeBriefMonthlyRecord>()
                        .Property(e => e.TenYearBudget)
                        .HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeBriefMonthlyRecord>()
                        .Property(e => e.CurrentYearCumulative)
                        .HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeBriefMonthlyRecord>()
                        .Property(e => e.SamePeriodLastYear)
                        .HasPrecision(18, 6);
            modelBuilder.Entity<ConsolidationScopeBriefMonthlyRecord>()
                        .Property(e => e.BudgetCompletionRates)
                        .HasPrecision(18, 4);
            modelBuilder.Entity<ConsolidationScopeBriefMonthlyRecord>()
                        .Property(e => e.YoyChangeRates)
                        .HasPrecision(18, 4);


            //关闭级联删除，Added By Luyuquan,2012.11.27
            modelBuilder.Conventions.Remove<OneToManyCascadeDeleteConvention>();
            modelBuilder.Conventions.Remove<ManyToManyCascadeDeleteConvention>();

            #region 一对一关系映射

            //租赁合同with物业合同
            modelBuilder.Entity<TenancyContract>()
                        .HasOptional(b => b.BelongToPropertyContract)
                        .WithMany()
                        .HasForeignKey(b => b.BelongToPropertyContractId);

            modelBuilder.Entity<PropertyContract>()
                        .HasOptional(u => u.BelongToTenancyContract)
                        .WithMany()
                        .HasForeignKey(u => u.BelongToTenancyContractId);

            modelBuilder.Entity<RepairmentApplyOfYear>()
                        .HasOptional(b => b.RepairmentResultOfYear)
                        .WithMany()
                        .HasForeignKey(b => b.RepairmentResultOfYearId);

            modelBuilder.Entity<RepairmentResultOfYear>()
                        .HasOptional(b => b.RepairmentApplyOfYear)
                        .WithMany()
                        .HasForeignKey(b => b.RepairmentApplyOfYearId);

            modelBuilder.Entity<RepairmentResultOfYear>()
                        .HasOptional(b => b.RepairmentApplyOfYear)
                        .WithMany()
                        .HasForeignKey(b => b.RepairmentApplyOfYearId);

            modelBuilder.Entity<BaseAssets>()
                       .HasOptional(b => b.InventoryProfit)
                       .WithMany()
                       .HasForeignKey(b => b.BelongInventoryProfitId);

            modelBuilder.Entity<InventoryProfit>()
                        .HasOptional(u => u.BaseAssets)
                        .WithMany()
                        .HasForeignKey(u => u.BelongBaseAssetId);

            #endregion

            #region 一对多关系映射

            ////资产附件有两个AssetsCard类，AssetsCard和TargetAssetsCard,映射混乱，指定映射在AssetsCard上  
            //modelBuilder.Entity<Accessory>()
            //            .HasOptional(a => a.AssetsCard)
            //            .WithMany(a => a.Accessories)
            //            .Map(a => a.MapKey("AssetsCardId"));

//            modelBuilder.Entity<PropertyContract>() 
//                        .HasMany(e => e.AdvanceCharges) 
////                        .WithMany(e => e.TenancyContracts)
//                        .Map(m =>
//                            {
//                                m.ToTable("TenancyContractRooms");
//                                m.MapLeftKey("TenancyContract_TenancyContractId");
//                                m.MapRightKey("Room_RoomId");
//                            });


            #endregion

            #region 继承关系映射

            //modelBuilder.Entity<BasePurchaseRequisition>()
            //            .Map(m => { m.ToTable("BasePurchaseRequisitions"); })
            //            .Map<CommonPurchaseRequisition>(m => { m.ToTable("CommonPurchaseRequisitions"); });



            //modelBuilder.Entity<BaseAssetsAllocationRecord>()
            //            .Map(m => { m.ToTable("BaseAssetsAllocationRecords"); })
            //            .Map<AssetsAllocationRecord>(m => { m.ToTable("AssetsAllocationRecords"); });




            modelBuilder.Entity<TenancyContract>()
                        .HasMany(e => e.TenancyContractRooms)
                        .WithMany(e => e.TenancyContracts)
                        .Map(m =>
                            {
                                m.ToTable("TenancyContractRooms");
                                m.MapLeftKey("TenancyContract_TenancyContractId");
                                m.MapRightKey("Room_RoomId");
                            });

            modelBuilder.Entity<EnterPriceContract>()
                        .HasMany(e => e.RelationEnterPriceContracts)
                        .WithMany(e => e.BelongEnterPriceContracts)
                        .Map(m =>
                            {
                                m.ToTable("RelationEnterPriseContracts");
                                m.MapLeftKey("MainContractId");
                                m.MapRightKey("SubContractId");
                            });

            modelBuilder.Entity<WiseOnlineCustomContract>()
                        .HasMany(e => e.RelationWiseOnlineCustomContracts)
                        .WithMany(e => e.BelongWiseOnlineCustomContracts)
                        .Map(m =>
                        {
                            m.ToTable("RelationWiseOnlineCustomContracts");
                            m.MapLeftKey("MainWiseonlineContractId");
                            m.MapRightKey("SubWiseonlineContractId");
                        });

       

            #endregion
        }
    }

    #region 更新数据库
    public partial class DropCreateEispDatabaseIfModelChanges : DropCreateDatabaseIfModelChanges<EispContext>
    {
        public static void Init()
        {
            Database.SetInitializer(new DropCreateEispDatabaseIfModelChanges());
            using (var db = new EispContext(Application.Instance.ConnectionString))
            {
                db.Database.Initialize(false);
            }
        }

        //数据初始化代码移到EispContext_Init.cs文件,Added By Luyuquan,2013.07.29
    }


    public partial class CreateEispDatabaseIfNotExists : CreateDatabaseIfNotExists<EispContext>
    {
        public static void Init()
        {
            Database.SetInitializer(new CreateEispDatabaseIfNotExists());
            using (var db = new EispContext(Application.Instance.ConnectionString))
            {
                db.Database.Initialize(false);
            }
        }
    }
    #endregion
}
