using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using Newtonsoft.Json;
using Wiseonline.Eisp.Domain.Attributes;
using Wiseonline.Eisp.Utility.Attributes;
using Wiseonline.Eisp.Utility.Helpers;
using Wiseonline.Eisp.Utility.Helpers.Common;
using Wiseonline.Eisp.Web.Areas.WF.Models;

namespace Wiseonline.Eisp.Domain
{
    /// <summary>
    /// 租赁合同
    /// </summary> 
    [JsonObject(MemberSerialization.OptIn)]
    public class TenancyContract : BaseSassBizObject
    {
        public TenancyContract()
        {
            TenancyContractRooms = new Collection<Room>();
            TenancyContractPayPeriods = new Collection<PayPeriod>();
            ReceivableAndReceivedAccounts = new Collection<ReceivableAndReceivedAccount>();
            TenancyAccounts = new Collection<TenancyAccount>();
            MarginPayList = new Collection<MarginPayDeficit>();
            Refitments = new Collection<Refitment>();


            ContractType = ContractType.ContractNew;
            ApprovalType = ApprovalType.Quick;
            IsIncubatorObj = BooleanStatus.No;
            ReletRemindMode = ReletRemindMode.FourMonth;
            MarginUnit = CurrencyUnits.RMB;
            CalculationMode = CalculationMode.ActualDaysPerMonth;
            DecimalPointKeep = DecimalPointKeep.KeepOne;
            HouseUse = HouseUse.Office;
            MarginMode = 3;
            PayMonth = PayMonth.CurrentMonth;
            PayDay = 10;
            SignDate = DateTime.Now;
            Status = TenancyContractStatus.Approving;
            IsStandardContract = true;
        }

        #region 基本信息

        [Key]
        [JsonProperty]
        [QueryResultColumn(true, -1, new[] { "Customers" })]
        public int TenancyContractId { get; set; }

        [JsonProperty]
        [QueryResultColumn(true, -2, new[] { "Customers" })]
        public int? ConcreteWorkflowId { get; set; }

        /// <summary>
        /// 工作流
        /// </summary>
        [ForeignKey("ConcreteWorkflowId")]
        public virtual ConcreteWorkflow ConcreteWorkflow { get; set; }

        public int? BelongToPropertyContractId { get; set; }

        /// <summary>
        /// 物业合同
        /// </summary>
        [ForeignKey("BelongToPropertyContractId")]
        [DisplayName("物业合同")]
        public virtual PropertyContract BelongToPropertyContract { get; set; }

        /// <summary>
        /// 是否标准合同
        /// </summary>
        [DisplayName("标准合同")]
        public bool IsStandardContract { get; set; }

        [DisplayName("标准合同")]
        [MobileDisplay(24)]
        public string IsStandardContractDisplay
        {
            get { return IsStandardContract ? "是" : "否"; }
        }

        /// <summary>
        /// 流水号
        /// </summary>
        public long? Sequence { get; set; }

        /// <summary>
        /// 招商合同号
        /// </summary>
        [Required]
        [DisplayName("招商合同号")]
        [JsonProperty]
        [FuzzyQuery]
        [MobileDisplay(1)]
        [QueryResultColumn(true, 1, new[] { "Customers" })]
        public string ContractNumber { get; set; }

        /// <summary>
        /// 统一合同号
        /// </summary>
        public string GlobalContractNumber { get; set; }

        /// <summary>
        /// 出租方
        /// </summary>
        [JsonProperty]
        public int ProjectOwnerValue { get; set; }

        /// <summary>
        /// 出租方
        /// </summary>
        [DisplayName("出租方")]
        [NotMapped]
        public ProjectOwners ProjectOwner
        {
            get { return (ProjectOwners)ProjectOwnerValue; }
            set { ProjectOwnerValue = (int)value; }
        }

        /// <summary>
        /// 出租方(浦软系统公司ID)
        /// </summary>
        public int ProjectOwnerCompanyId
        {
            get
            {
                return CompanyIdEnum.PuRuanGuFen;
            }
        }

        [DisplayName("出租方")]
        [MobileDisplay(7)]
        public string ProjectOwnerName
        {
            get { return ProjectOwner.GetEnumDisplay(); }

        }


        /// <summary>
        /// 租赁面积
        /// </summary>
        [JsonProperty]
        [DisplayName("租赁面积")]
        [QueryResultColumn(false, 19, new[] { "Customers" })]
        [MobileDisplay(27)]
        public decimal RentArea { get; set; }

        /// <summary>       
        /// 计费面积
        /// </summary>
        [JsonProperty]
        [DisplayName("计费面积")]
        [QueryResultColumn(true, 9, new[] { "Customers" })]
        [MobileDisplay(28)]
        public decimal PayArea { get; set; }

        [DisplayName("所属园区")]
        [JsonProperty]
        public int? LandProjectId { get; set; }

        /// <summary>
        /// 所属园区 
        /// </summary>
        [ForeignKey("LandProjectId")]
        public virtual LandProject LandProject { get; set; }

        /// <summary>
        /// 所属园区
        /// </summary>
        [JsonProperty]
        [Display(Name = "所属园区")]
        [QueryResultColumn(false, 14)]
        [MobileDisplay(3)]
        public string BelongLandProject
        {
            get { return LandProject == null ? string.Empty : LandProject.Name; }
        }


        /// <summary>
        /// 是否孵化器合同
        /// </summary>
        [JsonProperty]
        [DisplayName("孵化器合同")]
        public int IncubatorContract { get; set; }

        /// <summary>
        /// 是否孵化器合同
        /// </summary>
        [DisplayName("孵化器合同")]
        [NotMapped]
        public BooleanStatus IsIncubatorObj
        {
            set { IncubatorContract = (int)value; }
            get { return (BooleanStatus)IncubatorContract; }
        }


        /// <summary>
        /// 商业广场合同
        /// </summary>
        [JsonProperty]
        [DisplayName("商业广场合同")]
        public int IsSquareContractValue { get; set; }

        /// <summary>
        /// 商业广场合同
        /// </summary>
        [DisplayName("商业广场合同")]
        [NotMapped]
        public BooleanStatus IsSquareContract
        {
            set { IsSquareContractValue = (int)value; }
            get { return (BooleanStatus)IsSquareContractValue; }
        }

        [DisplayName("商业广场合同")]
        [MobileDisplay(25)]
        public string IsSquareContractDisplay
        {
            get
            {
                return IsSquareContract.GetEnumDisplay();
            }
        }

        /// <summary>
        /// 是否孵化合同
        /// </summary>
        [JsonProperty]
        [DisplayName("孵化合同")]
        [MobileDisplay(4)]
        public string IsIncubatorObjDisplay
        {
            get
            {
                return IsIncubatorObj.GetEnumDisplay();
            }
        }

        #endregion

        /// <summary>
        /// 固定客户名称（合同签订时的客户名称）
        /// </summary>
        [DisplayName("承租方")]
        public string StaticTenantName { get; set; }

        public int? ContractFileId { get; set; }

        /// <summary>
        /// 正文文件是使用模板生成（否则是上传）
        /// </summary>
        public bool? FileCreatedFromTemplate { get; set; }

        /// <summary>
        /// 合同正文
        /// </summary>
        [ForeignKey("ContractFileId")]
        [DisplayName("合同正文")]
        public virtual Attachment ContractFile { get; set; }

        /// <summary>
        /// 获取租赁周期的起始日期
        /// </summary>
        public DateTime BeginDateByRentPeriod
        {
            get
            {
                if (TenancyContractPayPeriods == null || TenancyContractPayPeriods.Count < 1)
                {
                    return DateTime.MinValue;
                }
                else
                {
                    return TenancyContractPayPeriods.Min(p => p.BeginDate);
                }
            }
        }

        #region 状态
        /// <summary>
        /// 是否已交房
        /// </summary>
        [Display(Name = "是否已交房")]
        public bool? HasCheckRoom { get; set; }

        /// <summary>
        /// 交房日期
        /// </summary>
        public DateTime? CheckRoomDate { get; set; }

        /// <summary>
        /// 商业中心合同交房延期天数
        /// </summary>
        [NotMapped]
        public int DelayDays
        {
            get
            {
                if (IsSquareContractValue == 1)
                {
                    if (CheckRoomDate != null && BeginDate != null)
                    {
                        var delayDaySpan = (DateTime)CheckRoomDate - (DateTime)BeginDate;
                        return delayDaySpan.Days;
                    }
                }
                return 0;
            }
        }

        public int OperatingStatusValue { get; set; }

        /// <summary>
        /// 租赁处理状态
        /// </summary>
        [Display(Name = "处理状态")]
        [NotMapped]
        public TenancyContractOperatingType OperatingStatus
        {
            get { return (TenancyContractOperatingType)OperatingStatusValue; }
            set { OperatingStatusValue = (int)value; }
        }

        [JsonProperty]
        public int TenancyContractStatusValue { get; set; }

        /// <summary>
        /// 租赁合同状态
        /// </summary>
        [Display(Name = "合同状态")]
        [NotMapped]
        public TenancyContractStatus Status
        {
            get { return (TenancyContractStatus)TenancyContractStatusValue; }
            set { TenancyContractStatusValue = (int)value; }
        }

        [JsonProperty]
        [Display(Name = "合同状态")]
        [MobileDisplay(21)]
        public string TenancyContractStatusName
        {
            get
            {
                return Status.GetEnumDisplay();
            }
        }

        ///// <summary>
        ///// 租赁合同状态
        ///// </summary>
        //[NonPersistent]
        //[Display(Name = "合同状态")]
        //public ValidTenancyContractStatus ValidStatus
        //{
        //    get { return (ValidTenancyContractStatus)TenancyContractStatusValue; }
        //}

        public int EnterPriceContractStatus { get; set; }

        /// <summary>
        /// 企业合同状态
        /// </summary>
        [NotMapped]
        public EnterPriceContractStatusEnum EnterPriceContractStatusEnum
        {
            get { return (EnterPriceContractStatusEnum)EnterPriceContractStatus; }
            set { EnterPriceContractStatus = (int)value; }
        }

        #endregion

        /// <summary>
        /// 承租方编号
        /// </summary>
        [DisplayName("承租方")]
        [JsonProperty]
        [QueryResultColumn(true, -1, new[] { "Customers" })]
        [ExportIgnore]
        public int? TenantId { get; set; }

        /// <summary>
        /// 承租方
        /// </summary>
        [ForeignKey("TenantId")]
        [JsonProperty] //不能改 租赁合同高级查询需要它 
        public virtual CorpBaseInfo Tenant { get; set; }

        /// <summary>
        /// 租金类型
        /// </summary>
        [DisplayName("租金类型")]
        public RentTypeEnum RentType { get; set; }

        /// <summary>
        /// 续租提醒方式
        /// </summary>
        public int ReletRemindModeValue { get; set; }

        /// <summary>
        /// 续租提醒方式
        /// </summary>
        [DisplayName("续租提醒方式")]
        [NotMapped]
        public ReletRemindMode ReletRemindMode
        {
            get { return (ReletRemindMode)ReletRemindModeValue; }
            set { ReletRemindModeValue = (int)value; }
        }
        [DisplayName("续租提醒方式")]
        [MobileDisplay(32)]
        public string ReletRemindModeDisplay
        {
            get { return ReletRemindMode.GetEnumDisplay(); }
        }
        /// <summary>
        /// 保证金(N月租金)
        /// </summary>
        [DisplayName("保证金月数")]
        [MobileDisplay(11)]
        public int? MarginMode { get; set; }

        /// <summary>
        /// 租金周期(月)
        /// </summary>
        [DisplayName("租金周期(月)")]
        public int ChargePeriod { get; set; }

        /// <summary>
        /// 房间
        /// </summary>
        public virtual ICollection<Room> TenancyContractRooms { get; set; }

        [Display(Name = "租赁房源")]
        [MobileDisplay(OrderBy: 26, dataField: "TenancyContractRooms", isList: true)]
        public string TenancyContractRoomsDisplay
        {
            get { return "点击查看详细"; }
        }

        /// <summary>
        /// 租赁期
        /// </summary>
        public virtual ICollection<PayPeriod> TenancyContractPayPeriods { get; set; }

        /// <summary>
        /// 延期交房后的租赁期
        /// </summary>
        [NotMapped]
        public virtual ICollection<PayPeriod> DelayPayPeriods
        {
            get
            {
                //商业中心合同
                if (IsSquareContractValue == 1 && DelayDays != 0)
                {
                    //拷贝一份租赁期数据的副本，防止处理商业中心合同的延期时，把租赁期条目的更改反映到数据库
                    var copyedPayPeriods = (from c in TenancyContractPayPeriods
                                            select new PayPeriod(c)).ToList();

                    foreach (var period in copyedPayPeriods)
                    {
                        period.BeginDate = period.BeginDate.AddDays(DelayDays);
                        period.EndDate = period.EndDate.AddDays(DelayDays);
                    }

                    return copyedPayPeriods;
                }
                else
                {
                    return TenancyContractPayPeriods;
                }
            }
        }


        [Display(Name = "租赁日期")]
        [MobileDisplay(OrderBy: 29, dataField: "TenancyContractPayPeriods", isList: true)]
        public string TenancyContractPayPeriodsDisplay
        {
            get { return "点击查看详细"; }
        }

        #region 关联人员

        public string TCManagerUserName
        {
            get
            {
                if (TCManager != null)
                {
                    return TCManager.UserName;
                }
                return string.Empty;
            }
        }

        /// <summary>
        /// 当前客服主管
        /// </summary>
        [ForeignKey("TCManagerUserName")]
        public virtual User TCManager
        {
            get
            {
                if (Tenant != null)
                {
                    return Tenant.CustomerChargerObj;
                }
                return null;
            }
        }

        ///// <summary>
        ///// 客服主管流程编号
        ///// </summary>
        //public int? CustomerServiceManagerFlowId { get; set; }


        ///// <summary>
        ///// 经办人
        ///// </summary>
        //public virtual User Operator { get; set; }

        /// <summary>
        /// 经办人用户名
        /// </summary>
        [Display(Name = "经办人用户名")]
        public string OperatorUserName { get; set; }

        /// <summary>
        /// 经办人
        /// </summary>
        [ForeignKey("OperatorUserName")]
        [JsonIgnore]
        public virtual User Operator { get; set; }

        /// <summary>
        /// 主办部门
        /// </summary>
        [Display(Name = "主办部门")]
        public int? DepartmentId { get; set; }

        /// <summary>
        /// 主办部门对象
        /// </summary>
        [ForeignKey("DepartmentId")]
        public virtual Department Department { get; set; }

        #endregion

        [JsonProperty]
        public int ApprovalTypeValue { get; set; }

        /// <summary>
        /// 审批类型
        /// </summary>
        [DisplayName("审批类型")]
        [NotMapped]
        public ApprovalType ApprovalType
        {
            get { return (ApprovalType)ApprovalTypeValue; }
            set { ApprovalTypeValue = (int)value; }
        }

        [JsonProperty]
        [Display(Name = "审批类型")]
        [QueryResultColumn(false, 11)]
        [MobileDisplay(23)]
        public string ApprovalTypeName
        {
            get { return ApprovalType.GetEnumDisplay(); }
        }

        [JsonProperty]
        public int ContractTypeValue { get; set; }

        /// <summary>
        /// 合同类型
        /// </summary>
        [DisplayName("合同类型")]
        [NotMapped]
        public ContractType ContractType
        {
            get { return (ContractType)ContractTypeValue; }
            set { ContractTypeValue = (int)value; }
        }

        [JsonProperty]
        [Display(Name = "合同类型")]
        [QueryResultColumn(true, 2)]
        [MobileDisplay(2)]
        public string ContractTypeName
        {
            get { return ContractType.GetEnumDisplay(); }
        }

        [JsonProperty]
        public int CalculationModeValue { get; set; }

        /// <summary>
        /// 计算方式
        /// </summary>
        [DisplayName("计算方式")]
        [NotMapped]
        public CalculationMode CalculationMode
        {
            get { return (CalculationMode)CalculationModeValue; }
            set { CalculationModeValue = (int)value; }
        }

        [DisplayName("计算方式")]
        [MobileDisplay(6)]
        public string CalculationModeDisplay
        {
            get { return CalculationMode.GetEnumDisplay(); }
        }


        [JsonProperty]
        public int MarginUnitValue { get; set; }

        /// <summary>
        /// 保证金单位
        /// </summary>
        [DisplayName("保证金单位")]
        [NotMapped]
        public CurrencyUnits MarginUnit
        {
            get { return (CurrencyUnits)MarginUnitValue; }
            set { MarginUnitValue = (int)value; }
        }

        [DisplayName("保证金单位")]
        [MobileDisplay(10)]
        public string MarginUnitDisplay
        {
            get { return MarginUnit.GetEnumDisplay(); }
        }

        [JsonProperty]
        public int HouseUseValue { get; set; }

        /// <summary>
        /// 房屋用途
        /// </summary>
        [DisplayName("房屋用途")]
        [NotMapped]
        public HouseUse HouseUse
        {
            get { return (HouseUse)HouseUseValue; }
            set { HouseUseValue = (int)value; }
        }

        [JsonProperty]
        [Display(Name = "房屋用途")]
        [QueryResultColumn(true, 10)]
        [MobileDisplay(9)]
        public string HouseUseName
        {
            get { return HouseUse.GetEnumDisplay(); }
        }

        [DisplayName("租金计算精度")]
        [JsonProperty]
        public int DecimalPointKeepValue { get; set; }

        /// <summary>
        /// 计算精度
        /// </summary>
        [JsonIgnore]
        [DisplayName("租金计算精度")]
        [NotMapped]
        public DecimalPointKeep DecimalPointKeep
        {
            get { return (DecimalPointKeep)DecimalPointKeepValue; }
            set { DecimalPointKeepValue = (int)value; }
        }

        [DisplayName("计算精度")]
        [MobileDisplay(8)]
        public string DecimalPointKeepDisplay
        {
            get { return DecimalPointKeep.GetEnumDisplay(); }
        }



        /// <summary>
        /// 是否虚拟合同
        /// </summary>
        [JsonProperty]
        public bool? IsVirtual { get; set; }

        /// <summary>
        /// 允许交房(该字段已废止)
        /// </summary>
        [JsonProperty]
        public bool? AgreeForGiveRoom { get; set; }

        /// <summary>
        /// 备注
        /// </summary>
        [FuzzyQuery]
        [DisplayName("备注")]
        [MobileDisplay(33)]
        public string Remark { get; set; }

        [Display(Name = "经办人意见")]
        [MobileDisplay(34)]
        public string OperatorUserSuggestion { get; set; }

        /// <summary>
        /// 获取或设置累计备注列表
        /// </summary>
        [Display(Name = "累计备注")]
        [JsonIgnore]
        public virtual ICollection<AdditionalRemark> AdditionalRemarks { get; set; }

        /// <summary>
        /// 联系房间
        /// </summary>
        [JsonProperty]
        [DisplayName("联系房间")]
        [MobileDisplay(22)]
        public string RelationRoom { get; set; }

        /// <summary>
        /// 合同提前终止时间
        /// </summary>
        public DateTime? AheadOfContractAbortDate { get; set; }

        /// <summary>
        /// 物业位置
        /// </summary>
        [JsonProperty]
        [DisplayName("物业位置")]
        public string TenancyContractRoomString { get; set; }

        #region 金额相关

        /// <summary>
        /// 保证金金额
        /// </summary>
        [JsonProperty]
        [DisplayName("保证金金额")]
        [QueryResultColumn(false, 15, new[] { "Customers" })]
        [MobileDisplay(30)]
        [Required]
        public decimal? Margin { get; set; }

        /// <summary>
        /// 租金预收款
        /// </summary>
        [DisplayName("租金预收款")]
        public decimal? RentAdvanceCharge { get; set; }
        //        /// <summary>
        //        /// 装修押金
        //        /// </summary>
        //        [DisplayName("装修押金")]
        //        public decimal? FitmentGuarantee { get; set; }
        //        /// <summary>
        //        /// 垃圾清运费
        //        /// </summary>
        //        [DisplayName("垃圾清运费")]
        //        public decimal? ClearFee { get; set; }

        /// <summary>
        /// 预收款（包含租金预收 ）
        /// </summary> 
        public virtual ICollection<AdvanceCharge> AdvanceCharges { get; set; }

        /// <summary>
        /// 营业额
        /// </summary>
        public virtual ICollection<CommercialPlazaMonthlySaleRecord> CommercialPlazaMonthlySaleRecords { get; set; }

        /// <summary>
        /// 租金预收款条目
        /// </summary>
        public IEnumerable<AdvanceCharge> RentAdvanceCharges { get { return AdvanceCharges.Where(a => a.Type == AdvanceChargeType.RentAdvance); } }
        //        /// <summary>
        //        /// 垃圾清运费条目
        //        /// </summary>
        //        public IEnumerable<AdvanceCharge> ClearFees { get { return AdvanceCharges.Where(a => a.Type == AdvanceChargeType.FitmentGuarantee); } }
        //        /// <summary>
        //        /// 装修押金条目
        //        /// </summary>
        //        public IEnumerable<AdvanceCharge> FitmentGuarantees { get { return AdvanceCharges.Where(a => a.Type == AdvanceChargeType.FitmentGuarantee); } }


        /// <summary>
        /// 是否自动产生保证金补差
        /// </summary>
        public bool? AutoGenrateMarginPayDeficit { get; set; }

        /// <summary>
        /// 保证金补差
        /// </summary>
        [JsonProperty]
        [DisplayName("保证金补差")]
        //[MobileDisplay(31)]
        [QueryResultColumn(false, 16, new[] { "Customers" })]
        public decimal MarginByCurrentContract { get; set; }


        /// <summary>
        /// 历史合同已缴保证金金额
        /// </summary>
        [JsonProperty]
        [DisplayName("历史保证金")]
        [QueryResultColumn(false, 17, new[] { "Customers" })]
        //[MobileDisplay(29)]
        public decimal MarginByHistoryContract { get; set; }

        #endregion

        #region 关联合同
        public int? SourceTenancyContractId { get; set; }
        /// <summary>
        /// 原租赁合同
        /// </summary>
        [ForeignKey("SourceTenancyContractId")]
        public virtual TenancyContract SourceTenancyContract { get; set; }

        #endregion


        #region 时间信息
        public int PayMonthValue { get; set; }
        /// <summary>
        /// 支付时间(月份)
        /// </summary>
        [DisplayName("支付时间")]
        [NotMapped]
        public PayMonth PayMonth
        {
            get { return (PayMonth)PayMonthValue; }
            set { PayMonthValue = (int)value; }
        }
        [DisplayName("支付时间")]
        [MobileDisplay(12)]
        public string PayMonthDisplay
        {
            get { return PayMonth.GetEnumDisplay(); }
        }
        /// <summary>
        /// 支付月前(天)
        /// </summary>
        [JsonProperty]
        [DisplayName("支付月前(天)")]
        [MobileDisplay(13)]
        public int PayDay { get; set; }
        /// <summary>
        /// 签订日期
        /// </summary>
        [JsonProperty]
        [DisplayName("签订日期")]
        [QueryResultColumn(true, 6, new[] { "Customers" })]
        public DateTime? SignDate { get; set; }

        [DisplayName("签订日期")]
        [MobileDisplay(14)]
        public string SignDateDisplay
        {
            get { return SignDate.HasValue ? SignDate.Value.ToString("yyyy-MM-dd") : "暂无明细"; }
        }

        /// <summary>
        /// 起租日期
        /// </summary>
        [JsonProperty]
        [DisplayName("起租日期")]
        [QueryResultColumn(true, 4, new[] { "Customers" })]
        public DateTime? BeginDate { get; set; }

        [DisplayName("起租日期")]
        [MobileDisplay(16)]
        public string BeginDateDisplay
        {
            get { return BeginDate.HasValue ? BeginDate.Value.ToString("yyyy-MM-dd") : "暂无明细"; }
        }

        /// <summary>
        /// 终租日期
        /// </summary>
        [JsonProperty]
        [DisplayName("终租日期")]
        [QueryResultColumn(true, 5, new[] { "Customers" })]
        public DateTime? EndDate { get; set; }

        [DisplayName("终租日期")]
        [MobileDisplay(17)]
        public string EndDateDisplay
        {
            get { return EndDate.HasValue ? EndDate.Value.ToString("yyyy-MM-dd") : "暂无明细"; }
        }

        /// <summary>
        /// 签订时填写的终租日期（合同变更时，要改变终租日期EndDate，OriginalEndDate作用就是保留原终租日期）
        /// </summary>
        [JsonProperty]
        [DisplayName("原终租日期")]
        public DateTime? OriginalEndDate { get; set; }

        /// <summary>
        /// 起付日期
        /// </summary>
        [JsonProperty]
        [DisplayName("起付日期")]
        [QueryResultColumn(false, 12, new[] { "Customers" })]
        public DateTime? BeginPayDate { get; set; }

        [DisplayName("起付日期")]
        [MobileDisplay(15)]
        public string BeginPayDateDisplay
        {
            get { return BeginPayDate.HasValue ? BeginPayDate.Value.ToString("yyyy-MM-dd") : "暂无明细"; }
        }
        #endregion

        #region 账单信息
        /// <summary>
        /// 获取或设置应实收列表列表
        /// </summary>
        [Display(Name = "应实收列表")]
        [JsonIgnore]
        public virtual ICollection<ReceivableAndReceivedAccount> ReceivableAndReceivedAccounts { get; set; }

        /// <summary>
        /// 获取或设置租金应实收列表列表列表
        /// </summary>
        [Display(Name = "租金应实收列表")]
        [JsonIgnore]
        public virtual ICollection<TenancyAccount> TenancyAccounts { get; set; }


        /// <summary>
        /// 获取或设置保证金列表
        /// </summary>
        [Display(Name = "保证金列表")]
        [JsonIgnore]
        public virtual ICollection<MarginPayDeficit> MarginPayList { get; set; }

        /// <summary>
        /// 获取或设置已缴结构恢复费列表
        /// </summary>
        [Display(Name = "已缴结构恢复费列表")]
        [JsonIgnore]
        public virtual ICollection<Refitment> Refitments { get; set; }
        #endregion

        #region 高级查询 kelsey
        /// <summary>
        /// 当前价格
        /// </summary>

        [JsonProperty]
        [Display(Name = "租赁价格")]
        [QueryResultColumn(false, 18, new[] { "Customers" })]
        [NonPersistent]
        public decimal Price
        {
            get
            {
                var firstPeriod =
                    TenancyContractPayPeriods.FirstOrDefault(m => DateTime.Now > m.BeginDate && DateTime.Now < m.EndDate);

                if (firstPeriod == null)
                {
                    firstPeriod = TenancyContractPayPeriods.FirstOrDefault(m => m.PayTypeValue == (int)PayPeriodType.Pay);
                    if (firstPeriod != null)
                    {
                        return firstPeriod.Price.ParseDecimal();
                    }
                }
                else
                {
                    return firstPeriod.Price.ParseDecimal();
                }

                return 0;
            }
        }

        [JsonProperty]
        [Display(Name = "月租赁总价")]
        [NonPersistent]
        public decimal MonthLyPrice
        {
            get
            {
                var firstPeriod =
                    TenancyContractPayPeriods.FirstOrDefault(m => DateTime.Now > m.BeginDate && DateTime.Now < m.EndDate);
                if (firstPeriod == null)
                {
                    firstPeriod = TenancyContractPayPeriods.FirstOrDefault(m => m.PayTypeValue == (int)PayPeriodType.Pay);
                    if (firstPeriod != null)
                    {
                        return firstPeriod.MonthlyPrice ?? 0;
                    }
                }
                else
                {
                    return firstPeriod.MonthlyPrice ?? 0;
                }
                return 0;
            }
        }
        #endregion


        #region 逻辑字段(非持久化)

        /// <summary>
        /// 处理状态
        /// </summary>
        [JsonProperty]
        [Display(Name = "当前状态")]
        [QueryResultColumn(false, 22, new[] { "Customers" })]
        public string OperatingStatusDisplay
        {
            get { return OperatingStatus.GetEnumDisplay(); }
        }

        /// <summary>
        /// 经办人姓名（打印）
        /// </summary>
        [NonPersistent]
        [Display(Name = "经办人")]
        [MobileDisplay(18)]
        public string OperatorName
        {
            get { return Operator == null ? "" : Operator.DisplayName; }
        }

        /// <summary>
        /// 主办部门名称（打印）
        /// </summary>
        [NonPersistent]
        [Display(Name = "经办部门")]
        [MobileDisplay(19)]
        public string DepartmentName
        {
            get { return Department == null ? string.Empty : Department.DeptName; }
        }
        //add by kelsey 
        public bool IsHasSanlinRooms
        {
            get
            {
                return TenancyContractRooms.Any(k => k.LandProjectObj.Name.Contains("三林"));
            }
        }

        [NonPersistent]
        public DateTime? QueryEndDate
        {
            get { return EndDate; }
        }

        /// <summary>
        /// 终租日期年份,不持久化
        /// </summary>
        [JsonProperty]
        [NonPersistent]
        [DisplayName("终租日期年份")]
        public int? EndDateYear
        {
            get
            {
                if (EndDate != null)
                {
                    return EndDate.Value.Year;
                }

                return null;
            }
        }
        /// <summary>
        /// 终租日期月份,不持久化
        /// </summary>
        [JsonProperty]
        [NonPersistent]
        [DisplayName("终租日期月份")]
        public int? EndDateMonth
        {
            get
            {
                if (EndDate != null)
                {
                    return EndDate.Value.Month;
                }

                return null;
            }
        }

        /// <summary>
        /// 已收保证金总计
        /// </summary>
        [NotMapped]
        public decimal TotalMarginReceived
        {
            get
            {
                var list = MarginPayList.Where(r => r.PayStatus == PaymentState.Paid && r.MarginReceived != null).ToList();

                if (list.Any())
                {
                    return list.Sum(r => (decimal)r.MarginReceived);
                }
                return 0;
            }
        }

        public decimal TotalRefitmentsReceived
        {
            get
            {
                var list = Refitments.Where(r => r.StatusObj == RefitmentStatusEnum.Finish && r.ReceivedAmount != null).ToList();

                if (list.Any())
                {
                    return list.Sum(r => (decimal)r.ReceivedAmount);
                }
                return 0;
            }
        }


        //        /// <summary>
        //        /// 应收保证金
        //        /// </summary>
        //        [NotMapped]
        //        public decimal HistoryMarginReceivable
        //        {
        //            get
        //            {
        //                var list = MarginPayList.Where(r => r.MarginReceivableTotal != null).ToList();
        //
        //                if (list.Any())
        //                {
        //                    return list.Sum(r => (decimal)r.MarginReceivableTotal);
        //                }
        //                return 0;
        //            }
        //        }

        /// <summary>
        /// 物业合同号
        /// </summary>
        [JsonProperty]
        [NonPersistent]
        [DisplayName("物业合同号")]
        //[MobileDisplay]
        public string BelongToPropertyContractName
        {
            get
            {
                if (BelongToPropertyContract != null)
                {
                    return BelongToPropertyContract.ContractNumber;
                }
                return "";
            }
        }

        [JsonProperty]
        [QueryResultColumn(false, 13, new[] { "Customers" })]
        [DisplayName("孵化器合同")]
        [NonPersistent]
        public string IncubatorContractValue
        {
            get
            {
                return IsIncubatorObj.GetEnumDisplay();
            }
        }


        /// <summary>
        /// 状态类型（仅用于查询）
        /// Sql冲突项由TrasStatusType.js进行条件转换
        /// </summary>
        [NonPersistent]
        public int StatusType
        {
            get
            {
                if (Status == TenancyContractStatus.Effected)
                {
                    return 1;
                }

                if (Status == TenancyContractStatus.Aborted
                    || Status == TenancyContractStatus.ChangeOfContractAborted
                    || Status == TenancyContractStatus.ContinueOfContractAborted
                    || Status == TenancyContractStatus.ExpirationOfContractAborted
                    || Status == TenancyContractStatus.BreakOfContractAborted
                    || Status == TenancyContractStatus.Closed)
                {
                    return 2;
                }
                return 0;
            }
        }

        /// <summary>
        /// 收费记账状态类型（仅用于查询）
        /// Sql冲突项由TrasStatusType.js进行条件转换
        /// </summary>
        [NonPersistent]
        public int PaymentTenacyStatusType
        {
            get
            {
                if (Status == TenancyContractStatus.Effected)
                {
                    return 1;
                }

                if (Status == TenancyContractStatus.Aborted
                    || Status == TenancyContractStatus.ChangeOfContractAborted
                    || Status == TenancyContractStatus.ContinueOfContractAborted
                    || Status == TenancyContractStatus.ExpirationOfContractAborted
                    || Status == TenancyContractStatus.BreakOfContractAborted
                    || Status == TenancyContractStatus.Closed
                    || Status == TenancyContractStatus.Approving)
                {
                    return 2;
                }
                return 0;
            }
        }

        #region ViewModel字段(前台使用,不持久化)
        /// <summary>
        /// 承租方名称(客户名称)
        /// </summary>
        [JsonProperty]
        [NonPersistent]
        [FuzzyQuery]
        [MobileDisplay(5)]
        [DisplayName("承租方")]
        [QueryResultColumn(false, 20, new[] { "Customers" })]
        public string TenantName
        {
            get
            {
                if (Tenant != null)
                {
                    return Tenant.Name;
                }
                return "";
            }
        }


        /// <summary>
        /// 承租方简称
        /// </summary>
        [JsonProperty]
        [NonPersistent]
        [FuzzyQuery]
        [QueryResultColumn(true, 3, new[] { "Customers" })]
        [DisplayName("承租方简称")]
        public string TenantShortName
        {
            get
            {
                if (Tenant != null)
                {
                    return Tenant.Attr;
                }
                return "";
            }
        }

        /// <summary>
        /// 剩余执行天数
        /// </summary>
        [JsonProperty]
        [NonPersistent]
        [DisplayName("剩余执行天数")]
        public string ResidueDays
        {
            get
            {
                if (BeginDate <= DateTime.Today && EndDate >= DateTime.Today)
                {
                    return (((EndDate - DateTime.Today).Value.Days + 1).ToString() + "天");
                }
                else if (EndDate < DateTime.Today)
                {
                    return "已逾期" + ((DateTime.Today - EndDate).Value.Days + 1).ToString() + "天";
                }
                return "";
            }
        }

        /// <summary>
        /// 租金变更日期
        /// </summary>
        [JsonProperty]
        [NonPersistent]
        [DisplayName("租金变更日期")]
        public DateTime PriceChangeDay
        {
            get
            {
                if (this.TenancyContractPayPeriods != null && this.TenancyContractPayPeriods.Count > 0)
                {
                    PayPeriod dataElement = null;

                    foreach (var tmp in this.TenancyContractPayPeriods.OrderBy(o => o.BeginDate))
                    {
                        if (dataElement == null)
                        {
                            dataElement = tmp;
                            continue;
                        }

                        if (dataElement.Price != tmp.Price)
                        {
                            if (dataElement.BeginDate <= DateTime.Now)
                            {
                                dataElement = tmp;
                                continue;
                            }
                            return dataElement.BeginDate;
                        }
                    }
                }
                return DateTime.MinValue;
            }
        }

        /// <summary>
        /// 剩余变更天数
        /// </summary>
        [JsonProperty]
        [NonPersistent]
        [DisplayName("剩余租金变更天数")]
        public string ResidueDaysForPriceChange
        {
            get
            {
                if (BeginDate <= DateTime.Today && EndDate >= DateTime.Today && PriceChangeDay >= DateTime.Today)
                {
                    TimeSpan ts1 = new TimeSpan(PriceChangeDay.Ticks);
                    TimeSpan ts2 = new TimeSpan(DateTime.Today.Ticks);
                    TimeSpan ts = ts1.Subtract(ts2).Duration();
                    return ts.Days.ToString() + "天";
                }

                return "";
            }
        }

        /// <summary>
        /// 租金两月内变更
        /// </summary>
        [JsonProperty]
        [NonPersistent]
        [DisplayName("租金两月内变更")]
        public int IsPriceChangeInTwoMonth
        {
            get
            {
                TimeSpan ts1 = new TimeSpan(PriceChangeDay.Ticks);
                TimeSpan ts2 = new TimeSpan(DateTime.Today.Ticks);
                TimeSpan ts = ts1.Subtract(ts2).Duration();
                if (ts1 > ts2 && ts.Days < 60)
                    return 1;

                return 0;
            }
        }

        /// <summary>
        /// 是否有效合同,不持久化
        /// </summary>
        [JsonProperty]
        [NonPersistent]
        [DisplayName("是否有效合同")]
        public int IsValid
        {
            get
            {
                if (TenancyContractStatusValue >= 30
                    && TenancyContractStatusValue != 40
                    && TenancyContractStatusValue != 45
                    && BeginDate <= DateTime.Now
                    && EndDate >= DateTime.Now)
                {
                    return 1;
                }

                return 0;
            }
        }

        /// <summary>
        /// 获取租赁起止日期
        /// </summary>
        [NonPersistent]
        [DisplayName("租赁起止日期")]
        public string PayperiodString
        {
            get
            {
                //2014.03.28，Luyuquan注释，原因：租赁合同本身具有起止日期字段，不需要计算租赁期才能得到
                //if (this.TenancyContractPayPeriods == null || this.TenancyContractPayPeriods.Count < 1)
                //{
                //    return null;
                //}
                //var pl = from p in this.TenancyContractPayPeriods
                //         orderby p.BeginDate ascending
                //         select p;
                //var beginDate = pl.First<PayPeriod>().BeginDate;
                //var endDate = pl.Last<PayPeriod>().EndDate;

                return string.Format("{0:yyyy-MM-dd}至{1:yyyy-MM-dd}", this.BeginDate, this.EndDate);
            }
        }

        #endregion
        #endregion
    }
}
