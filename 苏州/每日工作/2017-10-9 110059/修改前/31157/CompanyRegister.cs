using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;
using Wiseonline.Eisp.Utility.Attributes;
using Wiseonline.Eisp.Utility.Helpers;

namespace Wiseonline.Eisp.Domain
{
    /// <summary>
    /// 收文
    /// </summary>
    public class CompanyRegister
    {

        public int CompanyRegisterId { get; set; }

        [Display(Name = "编号")]
        public string SN { get; set; }

        [Display(Name = "发起人")]
        public string Creator { get; set; }
        [ForeignKey("Creator")]
        public virtual User StartUser { get; set; }
        [Display(Name = "发起人")]
        public string CreatorDisplayName
        {
            get { return StartUser != null ? StartUser.DisplayName : ""; }
        }

        [Display(Name = "公司名称")]
        public int? CorpBaseInfoId { get; set; }
        [ForeignKey("CorpBaseInfoId")]
        public virtual CorpBaseInfo CorpBaseInfo { get; set; }

        [Display(Name = "公司名称")]
        public string CorpBaseInfoName
        {
            get { return CorpBaseInfo != null ? CorpBaseInfo.Name : ""; }
        }

        [Display(Name = "公司名称")]
        public string CorpBaseInfoOther { get; set; }

        [Display(Name = "备注")]
        public string Remarks { get; set; }

        [Display(Name = "公司性质")]
        public string CompanyNature { get; set; }

        [Display(Name = "咨询事务")]
        public string ConsultancyService { get; set; }

        [Display(Name = "是否接收材料")]
        public string IsAccept { get; set; }

        [Display(Name = "收取材料情况")]
        public string AcceptInfo { get; set; }

        [Display(Name = "联系人")]
        public string ContractName { get; set; }

        [Display(Name = "联系电话")]
        public string ContractPhone { get; set; }

        [Display(Name = "补齐资料时间")]
        public DateTime? FillTime { get; set; }

        [Display(Name = "是否现场答复")]
        public string IsResponse { get; set; }

        [Display(Name = "约定答复时间")]
        public DateTime? ResponseTime { get; set; }

        [Display(Name = "开始日期")]
        public DateTime CreatedTime { get; set; }

        [Display(Name = "最近操作人")]
        public string ModifyName { get; set; }
        [ForeignKey("ModifyName")]
        public virtual User ModifyUser { get; set; }
        [Display(Name = "最近操作人")]
        public string ModifyDisplayName
        {
            get { return ModifyUser != null ? ModifyUser.DisplayName : ""; }
        }

        [Display(Name = "最近操作日期")]
        public DateTime ModifyTime { get; set; }

        [Display(Name = "咨询事务备注")]
        public string ConsultancyServiceRemarks { get; set; }

        [Display(Name = "名称")]
        public string LicenseName { get; set; }

        [Display(Name = "住所")]
        public string LicenseAddress { get; set; }

        [Display(Name = "法定代表人")]
        public string LegalRepresent { get; set; }

        [Display(Name = "注册资本")]
        public string RegisterAccount { get; set; }

        [Display(Name = "公司类型")]
        public string CompanyType { get; set; }

        [Display(Name = "实收资本")]
        public string PayCapital { get; set; }

        [Display(Name = "经营范围")]
        public string BusinessScope { get; set; }

        [Display(Name = "成立日期")]
        public DateTime EstablishTime { get; set; }

        [Display(Name = "注册号")]
        public string RegisterNo { get; set; }

        /// <summary>
        /// 咨询事务列表
        /// </summary>
        [JsonIgnore]
        public virtual ICollection<CompanyRegisterRecord> CompanyRegisterRecords { get; set; }

    }


}
