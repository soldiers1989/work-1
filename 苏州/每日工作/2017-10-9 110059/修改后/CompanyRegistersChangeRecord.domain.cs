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
    public class CompanyRegistersChangeRecord
    {
        public int CompanyRegistersChangeRecordId { get; set; }

        [Display(Name = "编号")]
        public string SN { get; set; }

        [Display(Name = "发起人")]
        public string Editor { get; set; }

        [Display(Name = "更改时间")]
        public DateTime? EditTime { get; set; }

        [Display(Name = "更改内容")]
        public string Content { get; set; }

        [Display(Name = "内外资企业工商注册")]
        public int CompanyRegisterId { get; set; }
        [ForeignKey("CompanyRegisterId")]
        public virtual CompanyRegister CompanyRegister { get; set; }
    }
}
