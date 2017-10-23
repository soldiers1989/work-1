using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;
using Wiseonline.Eisp.Domain;
using Wiseonline.Eisp.Utility.Attributes;

namespace Wiseonline.Eisp.Web.ViewModel.WorkflowForm
{
    public class CompanyRegisterChangeRecordViewModel
    {
        public int CompanyRegistersChangeRecordId { get; set; }

        [Display(Name = "编号")]
        public string SN { get; set; }

        [Display(Name = "发起人")]
        public string Editor { get; set; }

        [Display(Name = "更改时间")]
        public DateTime? EditTime { get; set; }

        [Display(Name = "更改内容")]
        public string ContentDetail { get; set; }

        [Display(Name = "内外资企业工商注册")]
        public int CompanyRegisterId { get; set; }
    }
}
