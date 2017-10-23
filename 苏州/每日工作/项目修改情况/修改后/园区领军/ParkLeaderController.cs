using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Microsoft.Web.Mvc;
using Wiseonline.Eisp.Data;
using Wiseonline.Eisp.Data.Infrastructure;
using Wiseonline.Eisp.Domain;
using Wiseonline.Eisp.Helpers;
using Wiseonline.Eisp.Service;
using Wiseonline.Eisp.Web.Controllers;
using Wiseonline.Eisp.Web.Helpers;

namespace Wiseonline.Eisp.Web.Areas.Clm.Controllers
{
    public class ParkLeaderController : BaseController
    {
        private readonly ISuperService<ParkLeader> _parkLeaderServ;

        public ParkLeaderController(ISuperService<ParkLeader> parkLeaderServ)
        {
            this._parkLeaderServ = parkLeaderServ;

        }

        // GET: /Clm/ParkLeader/
        [PermissionDisplay(ModuleName = "产业服务", MenuName = "园区领军", Name = "园区领军", AreaName = "Clm")]
        public ActionResult Index()
        {
            return View();
        }

        [HttpGet]
        public ActionResult Create()
        {
            var ParkLeader = new ParkLeader();
            return View(ParkLeader);
        }

        [HttpGet]
        public ActionResult Edit(int id)
        {

            var patentManage = _parkLeaderServ.GetModelById(id);
            return View("Create", patentManage);
        }

        public ActionResult GetParkLeaderList(FormCollection mode, bool exportExcel = false)
        {
            var expression = FormCollectionToQueryExpression(mode);
            return QueryDetail(expression, exportExcel);
        }

        private ActionResult QueryDetail(IList<QueryExpression> queryExpression, bool exportExcel = false)
        {
            if (exportExcel)
            {
                var datalist = _parkLeaderServ.QueryList(queryExpression).ToList();
                return BaseExportExcelDetail(datalist, n => n.CreatedTime);
            }
            else
            {
                var queryParams = base.GetQueryParams<ParkLeader>();
                var resultList = _parkLeaderServ.QueryListByPage(queryExpression, queryParams, o => o.CreatedTime);
                var r = resultList.Results;
                return Content(r.GetGridJson(resultList.RowCount));
            }
        }
        /// <summary>
        /// [创建]动作处理Action
        /// </summary>
        [HttpPost]
        public ActionResult Save(ParkLeader mode, FormCollection collection)
        {
            try
            {
                if (mode.ParkLeaderId > 0)
                {
                    mode.ModifyTime = DateTime.Now;
                    mode.ModifyName = CurrentUser.UserName;
                    _parkLeaderServ.UpdateModel(mode);
                    return Content(Msg("true", "保存成功！"));
                }
                else
                {
                    mode.CreatedTime = DateTime.Now;
                    mode.Creator = CurrentUser.UserName;
                    mode.ModifyTime = DateTime.Now;
                    mode.ModifyName = CurrentUser.UserName;
                    _parkLeaderServ.CreateModel(mode);
                    return Content(Msg("true", "保存成功！"));
                }
            }
            catch (Exception)
            {
                return Content(Msg("false", "保存失败！"));
            }
        }

        [HttpPost]
        public ActionResult Delete(int id)
        {
            try
            {
                _parkLeaderServ.DeleteModel(id);
                return Content(Msg("true", "删除成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "删除失败！"));
            }

        }
    }
}
