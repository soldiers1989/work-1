using System;
using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using Wiseonline.Eisp.Web.ViewModel.WorkflowForm;
using Wiseonline.Eisp.Domain;
using Microsoft.Web.Mvc;

namespace Wiseonline.Eisp.Web.Dto
{
    public static class CompanyRegisterEx
    {
        public static CompanyRegisterViewModel Dto(this CompanyRegister o)
        {
            var viewMode = new CompanyRegisterViewModel();
            ModelCopier.CopyModel(o, viewMode);
            viewMode.CompanyRegisterId = o.CompanyRegisterId;
            if (o.CorpBaseInfoId != null)
            {
                viewMode.CorpBaseInfoId = o.CorpBaseInfoId;
                viewMode.CorpBaseInfoName = o.CorpBaseInfoName;
            }
            else
            {
                viewMode.CorpBaseInfoId = o.CorpBaseInfoId;
                viewMode.CorpBaseInfoName = o.CorpBaseInfoOther;
            }
            viewMode.SN = o.SN;
            viewMode.Creator = o.Creator;
            viewMode.CreatorDisplayName = o.StartUser.DisplayName;
            viewMode.CreatedTime = o.CreatedTime.ToString("yyyy-MM-dd");
            viewMode.ModifyName = o.Creator;
            viewMode.ModifyDisplayName = o.StartUser.DisplayName;
            viewMode.ModifyTime = o.ModifyTime.ToString("yyyy-MM-dd");
            IsoDateTimeConverter timeConverter = new IsoDateTimeConverter();
            //这里使用自定义日期格式，如果不使用的话，默认是ISO8601格式  
            timeConverter.DateTimeFormat = "yyyy'-'MM'-'dd";
            viewMode.CompanyRegisterRecordJson = JsonConvert.SerializeObject(o.CompanyRegisterRecords, timeConverter);
            return viewMode;
        }
        public static CompanyRegisterRecordViewModel Dto(this CompanyRegisterRecord o)
        {
            var viewMode = new CompanyRegisterRecordViewModel();
            ModelCopier.CopyModel(o, viewMode);
            viewMode.CreatedTime = o.CreatedTime.ToString("yyyy-MM-dd");
            return viewMode;
        }

        public static CompanyRegisterChangeRecordViewModel Dto(this CompanyRegistersChangeRecord o)
        {
            var viewMode = new CompanyRegisterChangeRecordViewModel();
            ModelCopier.CopyModel(o, viewMode);
            //viewMode.EditTime = o.EditTime.ToString("yyyy-MM-dd");
            return viewMode;
        }
    }
}
