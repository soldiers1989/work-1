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
    public class ParkLeader
    {
        public int ParkLeaderId { get; set; }

        [Display(Name = "公司名称")]
        public int? CorpBaseInfoId { get; set; }
        [ForeignKey("CorpBaseInfoId")]
        public virtual CorpBaseInfo CorpBaseInfo { get; set; }

        [Display(Name = "客户名称")]
        public string CorpBaseInfoName
        {
            get
            {
                if (CorpBaseInfo != null)
                {
                    return CorpBaseInfo.Name;
                }
                return "";
            }
        }

        [Display(Name = "客户代表")]
        public string CustomerRepresentative { get; set; }

        [Display(Name = "所属领域")]
        public int Area { get; set; }

        [Display(Name = "公司地址")]
        public string Address { get; set; }

        [Display(Name = "注册资金")]
        public string RegisterAccount { get; set; }

        [Display(Name = "公司规模")]
        public string CompanySize { get; set; }

        [Display(Name = "备注")]
        public string Remarks { get; set; }

        [Display(Name = "创建人")]
        public string Creator { get; set; }

        [Display(Name = "创建时间")]
        public DateTime CreatedTime { get; set; }

        [Display(Name = "修改人")]
        public string ModifyName { get; set; }

        [Display(Name = "修改日期")]
        public DateTime ModifyTime { get; set; }
    }
}
