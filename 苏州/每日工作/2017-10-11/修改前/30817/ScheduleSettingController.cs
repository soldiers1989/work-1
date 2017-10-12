using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Wiseonline.Eisp.Data.Infrastructure;
using Wiseonline.Eisp.Service;
using Wiseonline.Eisp.Domain;
using Wiseonline.Eisp.Web.Helpers;

namespace Wiseonline.Eisp.Web.Controllers.Management
{
    public class ScheduleSettingController : BaseController
    {
        //
        // GET: /ScheduleSetting/
        private readonly ISecurityService securityService;

        private readonly IDataPermissionService dataPermissionService;

        public ScheduleSettingController(ISecurityService securityService,IDataPermissionService dataPermissionService)
        {
            this.securityService = securityService;
            this.dataPermissionService = dataPermissionService;
        }

        [PermissionDisplay(ModuleName = "后台管理", MenuName = "日程", Name = "日程默认数据显示")]
        public ActionResult Index()
        {
            var data = dataPermissionService.GetDataPermissionsByExpression(d => d.companySchedule.Value == true
                && d.UserDetail.Department.CompanyID == d.company);
            string array = string.Empty;
            foreach (var s in data)
            {
                array = array + s.user + ",";
            }
            ViewBag.Users = array;
            return View();
        }

        public ActionResult SaveScheduleSetting(string userName)
        {
            bool msg = false;
            try
            {
                var list =
                    dataPermissionService.GetDataPermissionsByExpression(d => d.companySchedule.Value == true).ToList();
                List<string> UpdateUsers = new List<string>();
                foreach (string  s in userName.Split(','))
                {
                    var data = list.Where(l => l.user == s && l.UserDetail.Department.CompanyID == l.company);
                    if (data.Count() >0)
                    {
                        list.Remove(data.First());
                    }
                    else
                    {
                        UpdateUsers.Add(s);
                    }
                }
                foreach (var l in list)
                {
                    l.companySchedule = false;
                    dataPermissionService.UpdateDataPermission(l);
                }
                foreach (var updateUser in UpdateUsers)
                {
                    var user = securityService.GetUser(updateUser);
                    var temp = dataPermissionService.GetDataPermissionsByExpression(d => d.user == updateUser && d.company == user.Department.CompanyID);
                    if (temp.Count()> 0)
                    {
                        temp.First().companySchedule = true;
                        dataPermissionService.UpdateDataPermission(temp.First());
                    }
                    else
                    {
                        dataPermissionService.CreateDataPermission(new DataPermission()
                            {
                                user = updateUser,
                                company = user.Department.CompanyID,
                                companySchedule = true
                            });
                    }
                }
                msg = true;
            }
            catch (Exception)
            {
                msg = false;
            }
            return Json(new { success = msg });
        }
    }
}
