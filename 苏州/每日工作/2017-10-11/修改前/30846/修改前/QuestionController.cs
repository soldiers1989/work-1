using System;
using System.Data;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Web.Mvc;
using Microsoft.Web.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using NPOI.SS.Formula.Functions;
using Wiseonline.Eisp.Data;
using Wiseonline.Eisp.Data.Infrastructure;
using Wiseonline.Eisp.Domain;
using Wiseonline.Eisp.Service;
using Wiseonline.Eisp.Utility.Helpers;
using Wiseonline.Eisp.Web.Dto;
using Wiseonline.Eisp.Web.Helpers;
using System.Linq.Expressions;
using ExpressionHelper = Wiseonline.Eisp.Data.Infrastructure.ExpressionHelper;
using Wiseonline.Eisp.Web.ViewModel;
using Wiseonline.Eisp.Utility;
using Wiseonline.Eisp.Web.ViewModel.WorkflowForm;
using LinqKit;
using Application = Wiseonline.Eisp.Utility.Application;

namespace Wiseonline.Eisp.Web.Controllers
{
    public class QuestionController : BaseWorkFlowController
    {
        private const int _WorkflowFormType = (int)WorkflowFormType.OMS;
        private static readonly string filePath = Application.CustomSettings["QuestionImg"].ToString();
        private const string GetWorkflowTitle_MethodName = "GetTitles";
        private readonly string JGCX_RoleName = Application.CustomSettings["JGCXRoleName"];
        private readonly IFormForWorkflowService forWorkflowSvc;
        private readonly IConcreteWorkflowService concreteWorkflowSvc;
        private readonly IWorkflowStatusService workflowStatusSvc;
        private readonly IWorkflowRuleService workflowRuleService;
        private readonly IConcreteWorkflowOperationService concreteWorkflowOperationSvc;
        private readonly IAttachmentService attachmentService;
        private readonly IInternalReportService internalReportService;
        private readonly IAccusedHoldingCompaniesWorkReportService accusedHoldingCompaniesWorkReportService;
        private readonly ISuperService<Question> _questionSrvc;
        private readonly ISuperService<CustomerComment> _customercommentSrvc;
        private readonly ISuperService<CustomerVisitRecord> _customervisitrecordSrvc;
        private readonly ISuperService<QuestionReply> _questionReplySrvc;
        private readonly ISuperService<QuestionPraise> _questionPraiseSrvc;
        private readonly ISuperService<QuestionAttention> _questionAttentionSrvc;
        private readonly ISuperService<User> _userService; 
        private readonly INewsService _newsService;
        private readonly ISecurityService _securityService;
        private readonly ISqlViewObjectService<QuestionGridModel> _questionSQLSrvc;
        private readonly ISuperService<QuestionAssignHistory> _assignHistoryService;
        private readonly ISuperService<DataModificationApplication> _dataModificationApplicationService;
        private readonly ISuperService<Department> _deptService;
        private readonly IPassRoundForPerusalService passRoundForPerusalService;

        public QuestionController(IFormForWorkflowService formWorkflowSvc, IConcreteWorkflowService concreteWorkflowSvc,
            IWorkflowStatusService workflowStatusSvc, IWorkflowRuleService workflowRuleService, IConcreteWorkflowOperationService concreteWorkflowOperationSvc,
            IAttachmentService attachmentService, IInternalReportService internalReportService, IAccusedHoldingCompaniesWorkReportService accusedHoldingCompaniesWorkReportService,
            ISuperService<Question> questionSrvc,ISuperService<CustomerComment> customercommentSrvc,ISuperService<CustomerVisitRecord> customervisitrecordSrvc,
                                  ISuperService<QuestionReply> questionReplySrvc,
                                  ISuperService<QuestionPraise> questionPraiseSrvc,
                                  ISuperService<QuestionAttention> questionAttentionSrvc,
                                  ISecurityService securityService,
            IMobileMessageService mobileMessageService,
            ISqlViewObjectService<QuestionGridModel> _questionSQLSrvc,
           INewsService newsService, ISuperService<User> userService, ISuperService<QuestionAssignHistory> assignHistoryService,
            ISuperService<DataModificationApplication> dataModificationApplicationService, ISuperService<Department> deptService,
            IPassRoundForPerusalService passRoundForPerusalService)
            : base(formWorkflowSvc, mobileMessageService)
        {
            this.forWorkflowSvc = formWorkflowSvc;
            this.concreteWorkflowSvc = concreteWorkflowSvc;
            this.workflowStatusSvc = workflowStatusSvc;
            this.workflowRuleService = workflowRuleService;
            this.concreteWorkflowOperationSvc = concreteWorkflowOperationSvc;
            this.attachmentService = attachmentService;
            this.internalReportService = internalReportService;
            this.accusedHoldingCompaniesWorkReportService = accusedHoldingCompaniesWorkReportService;
            _questionSrvc = questionSrvc;
            _customercommentSrvc = customercommentSrvc;
            _customervisitrecordSrvc = customervisitrecordSrvc;
            _questionReplySrvc = questionReplySrvc;
            _questionPraiseSrvc = questionPraiseSrvc;
            _securityService = securityService;
            _questionAttentionSrvc = questionAttentionSrvc;
            _userService = userService;
            _newsService = newsService;
            ValidateRequest = false;
            this._questionSQLSrvc = _questionSQLSrvc;
            this._assignHistoryService = assignHistoryService;
            this._dataModificationApplicationService = dataModificationApplicationService;
            this._deptService = deptService;
            this.passRoundForPerusalService = passRoundForPerusalService;
        }

        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "首页", Name = "首页", AreaName = "Oms")]
        public ActionResult Index()
        {
            var permissionList = SetDataPermissionList(_questionSrvc.GetAll().ToList());
            var belongsDeptIds  = _deptService.GetMany(n=>n.CompanyID == CurrentUser.CompanyId).Select(n=>n.DeptID) ;
            //公司内部服务问题
            var list = permissionList.Where(s => !s.IsCompanyRange || belongsDeptIds.Contains(s.QuestionAskDeptID ?? 0));
            var allQuestions = list.OrderByDescending(s => s.QuestionDate ).Take(13).ToList();
            var myQuestions =
                list.Where(s => s.QuestionUserName == CurrentUser.UserName)
                    .OrderByDescending(s => s.QuestionDate)
                    .Take(6)
                    .ToList();
            var myAttentions =
                list.Where(s => s.QuestionAttentions.Any(k=>k.AttentionUserName == CurrentUser.UserName))
                    .OrderByDescending(s => s.QuestionDate)
                    .Take(6)
                    .ToList();
            Expression<Func<News, bool>> exp = s => s.TypeID == 39;
            List<Expression<Func<News, bool>>> explist = new List<Expression<Func<News, bool>>>();
            explist.Add(exp);
            var news = _newsService.GetNewsesByExpsList(explist,1,6,CurrentUser.UserName).Results.ToList();
            ViewData["AllQuestions"] = allQuestions;
            ViewData["MyQuestions"] = myQuestions;
            ViewData["MyAttentions"] = myAttentions;
            ViewData["News"] = news;


            ViewBag.Sheet = 1;
            return View();
        }

        /// <summary>
        /// Excel导出权限
        /// </summary>
        /// <returns></returns>
        //[PermissionDisplay(ModuleName = "服务中心", MenuName = "我要报修", Name = "我要报修Excel导出", AreaName = "Oms")]
        public ActionResult ExportExcel()
        {
            return View("Index");
        }


        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "服务管理", AreaName = "Oms")]
        public ActionResult All()
        {
            ViewBag.Sheet = 3;
            ViewBag.CurrentUserName = CurrentUser.UserName;
            var users = _securityService.GetRole("QuestionAdmin").Users;
            ViewBag.IsAdministrator = users.Any(s => s.UserName == CurrentUser.UserName);
            var processUsers = _securityService.GetRole("QuestionProcessUser").Users;
            ViewBag.IsProcessUser = processUsers.Any(s => s.UserName == CurrentUser.UserName);
            ViewBag.CurrentUserName = CurrentUser.UserName;
            return View(new Question());
        }

        public ActionResult SelfService()
        {
            ViewBag.Sheet = 6;
            return View();
        }


        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "客户意见", AreaName = "Oms")]
        public ActionResult CustomerComment()
        {
            ViewBag.Sheet = 6;
            return View();
        }
        public ActionResult CreateComment(int id)
        {
            var customerComment = new CustomerComment();
            customerComment.CreateTime = DateTime.Now;
            customerComment.DealName = CurrentUser.UserName;
            customerComment.Dealer = CurrentUser;
            var question = _questionSrvc.GetModelById(id);
            customerComment.QuestionId = id;
            customerComment.Question = question;
            return View(customerComment);
        }
        public ActionResult CommentDetail(int id)
        {
            var customerComment = _customercommentSrvc.GetModelById(id);
            return View("CreateComment",customerComment);
        }
        [HttpPost]
        public ActionResult CreateComment(FormCollection formCollection,CustomerComment mode)
        {
            try
            {
                var comment = _customercommentSrvc.GetMany(o => o.QuestionId == mode.QuestionId).FirstOrDefault();
                if (comment != null)
                {
                    comment.CustomerName = mode.CustomerName;
                    comment.CorpBaseInfoId = mode.CorpBaseInfoId;
                    comment.QuestionId = mode.QuestionId;
                    comment.QuestionTypeId = mode.QuestionTypeId;
                    comment.QuestionStatusId = mode.QuestionStatusId;
                    comment.CreateTime = mode.CreateTime;
                    comment.DealName = mode.DealName;
                    comment.CustomerComments = mode.CustomerComments;
                    comment.CustomerReviewId = mode.CustomerReviewId;
                    _customercommentSrvc.UpdateModel(comment);
                }
                else
                {
                    _customercommentSrvc.CreateModel(mode);
                }
                return Content(Msg("true", "保存成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "保存失败！"));
            }
        }

        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "客户来访记录", AreaName = "Oms")]
        public ActionResult CustomerVisitRecord()
        {
            ViewBag.Sheet = 7;
            return View();
        }
        public ActionResult CreateCustomerVisitRecord()
        {
            var customerVisitRecord = new CustomerVisitRecord();
            customerVisitRecord.CreateTime = DateTime.Now;
            customerVisitRecord.DealName = CurrentUser.UserName;
            customerVisitRecord.Dealer = CurrentUser;
            customerVisitRecord.DeptId = CurrentUser.Department.DeptID;
            customerVisitRecord.Department = CurrentUser.Department;
            customerVisitRecord.VisitTime = DateTime.Now;

            return View(customerVisitRecord);
        }
        public ActionResult CustomerVisitRecordDetail(int id)
        {
            var customervisitrecord = _customervisitrecordSrvc.GetModelById(id);
            return View("CreateCustomerVisitRecord", customervisitrecord);
        }
        [HttpPost]
        public ActionResult CreateCustomerVisitRecord(FormCollection formCollection, CustomerVisitRecord mode)
        {
            try
            {
                if (mode.CustomerVisitRecordId > 0)
                {
                    _customervisitrecordSrvc.UpdateModel(mode);
                }
                else
                {
                    _customervisitrecordSrvc.CreateModel(mode);
                }
                return Content(Msg("true", "保存成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "保存失败！"));
            }
        }

        [HttpPost]
        public ActionResult DeleteVisitRecord(int id)
        {
            try
            {
                _customervisitrecordSrvc.DeleteModel(id);
                return Content(Msg("true", "删除成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "删除失败！"));
            }

        }
        public ActionResult Contact()
        {
            ViewBag.Sheet = 5;
            return View();
        }

        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "我关注的服务", AreaName = "Oms")]
        public ActionResult MyAttention()
        {
            ViewBag.Sheet = 5;
            ViewBag.CurrentUserName = CurrentUser.UserName;
            var users = _securityService.GetRole("QuestionAdmin").Users;
            ViewBag.IsAdministrator = users.Any(s => s.UserName == CurrentUser.UserName);
            var processUsers = _securityService.GetRole("QuestionProcessUser").Users;
            ViewBag.IsProcessUser = processUsers.Any(s => s.UserName == CurrentUser.UserName);
            return View(new Question());
        }

        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "我的问题", AreaName = "Oms")]
        public ActionResult MyQuestion()
        {
            ViewBag.Sheet = 4;
            ViewBag.CurrentUserName = CurrentUser.UserName;
            var users = _securityService.GetRole("QuestionAdmin").Users;
            ViewBag.IsAdministrator = users.Any(s => s.UserName == CurrentUser.UserName);
            var processUsers = _securityService.GetRole("QuestionProcessUser").Users;
            ViewBag.IsProcessUser = processUsers.Any(s => s.UserName == CurrentUser.UserName);
            return View(new Question());
        }

        [HttpGet]
        public ActionResult Details(int id)
        {
            var users = _securityService.GetRole("QuestionAdmin").Users;

            ViewBag.IsAdministrator = users.Any(s => s.UserName == CurrentUser.UserName);
            var processUsers = _securityService.GetRole("QuestionProcessUser").Users;
            ViewBag.IsProcessUser = processUsers.Any(s => s.UserName == CurrentUser.UserName);

            var question = _questionSrvc.GetModelById(id);

            if (question.QuestionStatusId == (int) QuestionStatus.ToConfirm && CurrentUser.UserName == question.QuestionUserName)
                ViewBag.ToConfirmDetail = true;

            return View(question);
        }

        public ActionResult Search(FormCollection model, bool exportExcel = false)
        {
            this._questionSQLSrvc.Sql = GetSqlText(SqlConst.Sql_Question).Replace("@AttentionUser", "").Replace("@CurrentUserCompanyId", CurrentUser.CompanyId.ToString());
            List<QueryExpression> change = covertUnfinish(model);
            return QueryDetail(change, exportExcel);
            ;
        }

        public ActionResult SearchComment(FormCollection formCollection)
        {
            var queryParams = base.GetQueryParams<CustomerComment>();
            var result = _customercommentSrvc.QueryListByPage(FormCollectionToQueryExpression(formCollection), queryParams, n => n.CustomerCommentId);
            return this.Content(JsonConvert.SerializeObject(new { total = result.RowCount, rows = result.Results.Select(n => n.Dto()).ToList() }));
        }

        public ActionResult SearchVisitRecord(FormCollection formCollection)
        {
            var queryParams = base.GetQueryParams<CustomerVisitRecord>();
            var result = _customervisitrecordSrvc.QueryListByPage(FormCollectionToQueryExpression(formCollection), queryParams, n => n.CustomerVisitRecordId);
            return this.Content(JsonConvert.SerializeObject(new { total = result.RowCount, rows = result.Results.Select(n => n.Dto()).ToList() }));
        }

        private ActionResult QueryDetail(IList<QueryExpression> queryExpression, bool exportExcel = false)
        {
            //加数据权限
            _questionSQLSrvc.Sql = SetDataPermissionSql(_questionSQLSrvc.Sql);
            if (exportExcel)
            {
                var datalist = _questionSQLSrvc.QueryList(queryExpression).ToList();
                return BaseExportExcelDetail(datalist, n => n.MaxTime, defaultDescSort: true);
            }
            else
            {
                var queryInfo = GetQueryParams<QuestionGridModel>();
                IsoDateTimeConverter timeConverter = new IsoDateTimeConverter();
                timeConverter.DateTimeFormat = "yyyy-MM-dd";
                var result = _questionSQLSrvc.QueryListByPage(queryExpression, queryInfo, k => k.MaxTime, true, true);
                return this.Content(JsonConvert.SerializeObject(new { total = result.RowCount, rows = result.Results.ToList() }, timeConverter));
            }
        }

        /// <summary>
        /// 为Sql设置数据权限
        /// </summary>
        /// <param name="oriSql"></param>
        /// <returns></returns>
        private string SetDataPermissionSql(string oriSql)
        {
            var deptIds = new List<int>();
            int thisPermission = GetDataPermission(ref deptIds);
            if (thisPermission == 0)
                return oriSql;
            if (thisPermission == 1)
            {
                if (deptIds.Count > 0)
                {
                    string deptIdsString = string.Empty;
                    foreach (var deptId in deptIds)
                    {
                        deptIdsString = deptIdsString + deptId.ToString() + ",";
                    }
                    deptIdsString = deptIdsString.Substring(0, deptIdsString.Length - 1);
                    return oriSql.Replace("1=1", "1=1 AND QuestionUserDeptId in (" + deptIdsString + ")");
                }
            }
            return oriSql.Replace("1=1", "1=1 AND QuestionUserName = '" + CurrentUser.UserName + "'");
        }


        /// <summary>
        /// 为DataList设置数据权限
        /// </summary>
        /// <param name="oriList"></param>
        /// <returns></returns>
        private IList<Question> SetDataPermissionList(IList<Question> oriList)
        {
            var deptIds = new List<int>();
            int thisPermission = GetDataPermission(ref deptIds);
            if (thisPermission == 0)
                return oriList;
            if (thisPermission == 1)
            {
                return oriList.Where(n => deptIds.Contains((int)(n.QuestionUser.DeptID ?? 0))).ToList();
            }
            return oriList.Where(n => n.QuestionAskUserName == CurrentUser.UserName).ToList();
        }

        /// <summary>
        /// 设置数据权限
        /// </summary>
        /// <param name="paramStr">返回字符串</param>
        /// <returns>0:最高权限;1:部门限定;2:提出人员限定（本人）;</returns>
        private int GetDataPermission(ref List<int> resultDeptIds)
        {
            bool isAdministrator = _securityService.GetRole("QuestionAdmin").Users.Any(s => s.UserName == CurrentUser.UserName);
            bool isProcessUser = _securityService.GetRole("QuestionProcessUser").Users.Any(s => s.UserName == CurrentUser.UserName);
            bool isGeneralManagers = _securityService.GetRole("GeneralManagers").Users.Any(s => s.UserName == CurrentUser.UserName);

            //总经理、管理员和汇智软件处理人员可看到所有问题
            if (isAdministrator || isProcessUser || isGeneralManagers)
                return 0;

            //可查看部门
            var deptIds = new List<int>();
            //控参股企业主要负责人查看企业所有问题
            foreach (var dept in _deptService.GetMany(m => m.DeptName == "公司领导"))
            {
                if (dept.Users.Any(s => s.UserName == CurrentUser.UserName))
                {
                    deptIds.AddRange(dept.Company.Departments.Select(n => n.DeptID));
                }
            }
            //分管领导可看到自己分管部门所有问题;部门经理可看到自己部门所有问题
            foreach (var dept in _deptService.GetMany(m => m.DepartmentManager == CurrentUser.UserName || m.DepartmentLeader == CurrentUser.UserName))
            {
                deptIds.Add(dept.DeptID);
            }

            if (deptIds.Count > 0)
            {
                resultDeptIds = deptIds;
                return 1;
            }
            else
            {
                return 2;
            }
        } 

        private List<QueryExpression> covertUnfinish(FormCollection model)
        {
            var list = FormCollectionToQueryExpression(model).ToList();
            var newList = new QueryExpression();
            foreach (var tt in list)
            {
                if (model["QuestionStatusId"] != null && model["QuestionStatusId"].ToString() == "6")
                {
                    if (tt.FieldName == "QuestionStatusId")
                    {
                        tt.Method = "<";
                        tt.FieldValue = "5";
                    }
                }
                if (model["QuestionUserName"] != null)
                {
                    if (tt.FieldName == "QuestionUserName")
                    {
                        tt.Method = "===";
                    }
                }
                if (model["ProcessUserName"] != null)
                {
                    if (tt.FieldName == "ProcessUserName")
                    {
                        tt.Method = "===";
                    }
                }
                if (model["QuestionDate"] != null)
                {
                    if (tt.FieldName == "QuestionDate")
                    {
                        var time = tt.FieldValue;
                        tt.Method = "<=";
                        tt.FieldValue = time + " 23:59:59";
                        newList = new QueryExpression()
                        {
                            Method = ">=",
                            FieldValue = time + " 00:00:00",
                            FieldName = tt.FieldName
                        };
                    }
                }
            }
            list.Add(newList);
            return list;
        }

        public ActionResult SearchMyQuestion(FormCollection model, bool exportExcel = false)
        {
            var queryParams = covertUnfinish(model); ;
            queryParams.Add(new QueryExpression
                {
                    FieldName = "QuestionUserName",
                    FieldValue = CurrentUser.UserName,
                    Method = "==="
                });
            this._questionSQLSrvc.Sql = GetSqlText(SqlConst.Sql_Question).Replace("@AttentionUser", "").Replace("@CurrentUserCompanyId", CurrentUser.CompanyId.ToString());
            return QueryDetail(queryParams, exportExcel);
        }

        public ActionResult SearchMyAttention(FormCollection model, bool exportExcel = false)
        {
            var queryParams = covertUnfinish(model); ;
            queryParams.Add(new QueryExpression
            {
                FieldName = "AttentionUserName",
                FieldValue = CurrentUser.UserName,
                Method = "==="
            });
            this._questionSQLSrvc.Sql = GetSqlText(SqlConst.Sql_Question).Replace("@AttentionUser", ",AttentionUserName").Replace("@CurrentUserCompanyId", CurrentUser.CompanyId.ToString());
            return QueryDetail(queryParams, exportExcel);
        }

        public ActionResult SearchFAQ(FormCollection model, bool exportExcel = false)
        {
            var queryParams = covertUnfinish(model);
            queryParams.Add(new QueryExpression
            {
                FieldName = "IsFQA",
                FieldValue = "1",
                Method = "=="
            });
            this._questionSQLSrvc.Sql = GetSqlText(SqlConst.Sql_Question).Replace("@AttentionUser", "").Replace("@CurrentUserCompanyId", CurrentUser.CompanyId.ToString());
            return QueryDetail(queryParams, exportExcel);
        }

        public string GetMax()
        {
            /*string currentYear = "BX-" + DateTime.Today.Year + "-";
            Expression<Func<Question, bool>> where = o => o.SN.Contains(currentYear);
            var sn = int.Parse(_questionSrvc.GetObjects().Where(where).Max(s => s.SN.Substring(8)) ?? "0") + 1;
            return currentYear + sn.ToString("D4");*/
            var sn = int.Parse(_questionSrvc.GetObjects().Max(s => s.SN ?? "0")) + 1;
            return sn.ToString("D5");
        }

        [HttpGet]
        public ActionResult Create()
        {
            ViewBag.IsAdministrator = this.CurrentUser.Roles.Any(k => k.RoleName == "QuestionAdmin");
            var processUsers = _securityService.GetRole("QuestionProcessUser").Users;
            ViewBag.IsProcessUser = processUsers.Any(s => s.UserName == CurrentUser.UserName);

            var sn = GetMax();

            var model = new Question
            {
                QuestionStatusId = (int)QuestionStatus.ToProcess,
                QuestionStatus = QuestionStatus.ToProcess,
                QuestionTypeId = (int)QuestionType.Other,
                QuestionType = QuestionType.Other,
                QuestionUserName = CurrentUser.UserName,
                QuestionAskUserName = CurrentUser.UserName,
                QuestionAskDeptID = CurrentUser.DeptID,
                QuestionDate = DateTime.Now,
                SN = GetMax()
            };
            //3
            ViewBag.IsWiseOnline = this.CurrentUser.CompanyId == 3 ? true : false;
            return View(model);
        }

        [HttpPost]
        public ActionResult Create(Question viewModel)
        {
            var model = new Question();
            try
            {
                ModelCopier.CopyModel(viewModel, model);
                if (_questionSrvc.GetMany(k => k.SN == model.SN).Any())
                {
                    model.SN = GetMax();
                }

                _questionSrvc.CreateModel(model);
                #region 新增问题提醒
                //新增问题提醒（王莹、顾金华、陈慧琦、郑威）
                if (model.QuestionId != null && model.QuestionId > 0)
                {
                    var pusherNameStr = Application.GetConfigValue("QuestionCreatePushers");
                    var pusherNames = pusherNameStr.Split(',');
                    foreach (var pusherName in pusherNames)
                    {
                        string name = pusherName;
                        var thePusher = _userService.GetMany(m => m.UserName == name).FirstOrDefault();
                        if (thePusher != null)
                        {
                            var mobile = new MobileMessage()
                                {
                                    Message = model.QuestionUserDisplayName + "新增了一个问题【" + model.SN + "】。【服务中心】",
                                    MobilePhone = thePusher.MobilePhone,
                                    ReceiverUserName = thePusher.UserName,
                                    Type = MobileMessageType.Question
                                };
                            EmppSender(mobile, true);
                        }
                    }
                }
                #endregion
                return Content(Msg("true", "提问成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "提问失败！"));
            }
        }

        [HttpGet]
        public ActionResult Edit(int id, bool isAdministrator = false)
        {
            ViewBag.IsAdministrator = this.CurrentUser.Roles.Any(k => k.RoleName == "QuestionAdmin");
            var processUsers = _securityService.GetRole("QuestionProcessUser").Users;
            ViewBag.IsProcessUser = processUsers.Any(s => s.UserName == CurrentUser.UserName);
            var question = _questionSrvc.GetModelById(id);
            ViewBag.IsWiseOnline = question.IsCompanyRange;
            return View("Create", question);
        }

        [HttpPost]
        public ActionResult Edit(int id, FormCollection formCollection)
        {
            var model = _questionSrvc.GetModelById(id);
            if (TryUpdateModel(model))
            {
                

                _questionSrvc.SaveChanges();
                return Content(Msg("true", "修改成功！"));
            }
            else
            {
                return Content(Msg("false", "修改失败！"));
            }
        }

        [HttpPost]
        public ActionResult Delete(int id)
        {
            try
            {
                _questionSrvc.DeleteModelByLogic(id);
                return Content(Msg("true", "删除成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "删除失败！"));
            }

        }

        [HttpPost]
        public ActionResult Process(int questionId)
        {
            try
            {
                var question = _questionSrvc.GetModelById(questionId);
                question.QuestionStatusId = (int)QuestionStatus.Processing;
                _questionSrvc.UpdateModel(question);

                return Content(Msg("true", "处理成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "处理失败！"));
            }
        }

        [HttpPost]
        public ActionResult Resolve(int questionId)
        {
            try
            {
                var question = _questionSrvc.GetModelById(questionId);
                question.QuestionStatusId = (int)QuestionStatus.ToConfirm;
                _questionSrvc.UpdateModel(question);
                var mobile = new MobileMessage()
                {
                    Message = "您提交的问题【" + question.SN + "】已处理完毕，请及时确认。【服务中心】",
                    MobilePhone = question.QuestionAskUser.MobilePhone,
                    ReceiverUserName = question.QuestionAskUser.UserName,
                    Type = MobileMessageType.Question
                };
                EmppSender(mobile, true);
                return Content(Msg("true", "解决成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "解决失败！"));
            }
        }


        /*[HttpPost]
        public ActionResult Confirm(int questionId)
        {
            try
            {
                var question = _questionSrvc.GetModelById(questionId);
                question.QuestionStatusId = (int)QuestionStatus.ToEvaluate;
                _questionSrvc.UpdateModel(question);

                return Content(Msg("true", "确认成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "确认失败！"));
            }
        }*/

        //[HttpGet]
        //public ActionResult QuestionDetail(int id)
        //{
        //    var question = _questionSrvc.GetModelById(id);
        //    DataTable item = null;
        //    DataSet myData = new DataSet();
        //    string strsql = "select * from {0} where ConcreteWorkflowId='{1}'";
        //    string cmd = string.Format(strsql, question.QuestionAction, int.Parse(question.SN));
        //    myData = SqlHelper.ExecuteDataset(SqlHelper.GetEispConnSting(), CommandType.Text, cmd);
        //    item = myData.Tables[0];
        //    if (item.Rows.Count > 0)
        //    {
        //        for (int i = 0; i < item.Rows.Count; i++)
        //        {
                    
        //        }
        //    }
        //    return View(question);
        //}

        [HttpGet]
        public ActionResult Evaluate(int id)
        {
            var question = _questionSrvc.GetModelById(id);
            return View(question);
        }

        [HttpPost]
        public ActionResult Evaluate(int id, FormCollection formCollection)
        {
            var model = _questionSrvc.GetModelById(id);
            if (TryUpdateModel(model))
            {
                model.QuestionStatusId = (int)QuestionStatus.Finish;
                _questionSrvc.SaveChanges();
                return Content(Msg("true", "评价成功！"));
            }
            else
            {
                return Content(Msg("false", "评价失败！"));
            }
        }

        [HttpPost]
        public ActionResult Attention(int id)
        {
            try
            {
                if (
                    !(_questionAttentionSrvc.GetMany(
                        s => s.QuestionId == id && s.AttentionUserName == CurrentUser.UserName).Any()))
                {
                    var model = new QuestionAttention();
                    model.QuestionId = id;
                    model.AttentionUserName = CurrentUser.UserName;
                    _questionAttentionSrvc.CreateModel(model);
                    return Content(Msg("true", "关注成功！"));
                }
                else
                {
                    return Content(Msg("true", "您已关注！"));
                }
            }
            catch (Exception)
            {
                return Content(Msg("false", "关注失败！"));
            }
        }

        [HttpPost]
        public ActionResult Unfollow(int id)
        {
            try
            {
                var follow = _questionAttentionSrvc.GetMany(
                    s => s.QuestionId == id && s.AttentionUserName == CurrentUser.UserName);
                if (follow.Any())
                {
                    _questionAttentionSrvc.DeleteModel(follow.First().QuestionAttentionId);
                    return Content(Msg("true", "已取消关注！"));
                }
                else
                {
                    return Content(Msg("true", "您未关注！"));
                }
            }
            catch (Exception)
            {
                return Content(Msg("false", "取消关注失败！"));
            }
        }

        [HttpGet]
        public ActionResult Assign(int id)
        {
            var question = _questionSrvc.GetModelById(id);
            return View(question);
        }

        [HttpPost]
        public ActionResult Assign(int id, FormCollection formCollection)
        {
            var model = _questionSrvc.GetModelById(id);
            if (TryUpdateModel(model))
            {
                _questionSrvc.SaveChanges();

                var history = new QuestionAssignHistory()
                    {
                        QuestionId = model.QuestionId,
                        AssignUserName = CurrentUser.UserName,
                        ProcessUserName = model.ProcessUserName,
                        AssignDate = DateTime.Now
                    };
                _assignHistoryService.CreateModel(history);
                _assignHistoryService.SaveChanges();

                var mobile = new MobileMessage()
                {
                    Message = "您有一条" + model.QuestionUserDisplayName + "提交的报修【" + model.SN + "】，请及时处理。【服务中心】",
                    MobilePhone = model.ProcessUser.MobilePhone,
                    ReceiverUserName = model.ProcessUser.UserName,
                    Type = MobileMessageType.Question
                };
                EmppSender(mobile, true);
                return Content(Msg("true", "指定成功！"));
            }
            else
            {
                return Content(Msg("false", "指定失败！"));
            }
        }

        public JsonResult GetAssignHistory(int id)
        {
            var queryParams = base.GetQueryParams<QuestionAssignHistory>();
            var queryExp = new List<QueryExpression>();
            queryExp.Add(new QueryExpression()
                {
                    FieldName = "QuestionId",
                    FieldValue = id.ToString(),
                    Method = "=="
                });

            var result = _assignHistoryService.QueryListByPage(queryExp, queryParams,
                                                     o => o.AssignDate, true);

            var history = result.Results.Select(s => new
            {
                id = s.QuestionAssignHistoryId,
                text = s.HistoryListDisplay
            });
            var jsonResult = Json(new { total = result.RowCount, rows = history }, JsonRequestBehavior.AllowGet);
            return jsonResult;
        }


        public JsonResult Appeal()
        {
            var queryParams = base.GetQueryParams<DataModificationApplication>();
            var result = new PagedResult<DataModificationApplication>();

            if (!string.IsNullOrWhiteSpace(queryParams.SearchText))
            {
                var thisSearch = queryParams.SearchText;
                result =
                    _dataModificationApplicationService.GetListByPage(
                        n =>
                        n.SerialNumber.Contains(thisSearch) || n.SystemFunction.Contains(thisSearch) ||
                        n.StartUser.DisplayName.Contains(thisSearch), n => n.StartDate, queryParams.PageSize, queryParams.PageCount, true);
            }
            else
            {
              result = _dataModificationApplicationService.QueryListByPage(new List<QueryExpression>(), queryParams,
                                                     o => o.StartDate, true);  
            }

            var appeal = result.Results.Select(s => new
            {
                id = s.DataModificationApplicationId,
                sn = s.SerialNumberDisplay,
                user = s.StartUserDisplayName,
                datatime = s.StartDate == null? string.Empty : s.StartDate.Value.ToShortDateString(),
                functions = s.SystemFunction
            });
            var jsonResult = Json(new { total = result.RowCount, rows = appeal }, JsonRequestBehavior.AllowGet);
            return jsonResult;
        }

        public ActionResult FAQ()
        {
            ViewBag.Sheet = 5;
            ViewBag.CurrentUserName = CurrentUser.UserName;
            var users = _securityService.GetRole("QuestionAdmin").Users;
            ViewBag.IsAdministrator = users.Any(s => s.UserName == CurrentUser.UserName);
            var processUsers = _securityService.GetRole("QuestionProcessUser").Users;
            ViewBag.IsProcessUser = processUsers.Any(s => s.UserName == CurrentUser.UserName);
            return View(new Question());
        }

        public ActionResult SetFAQ(int id)
        {
            try
            {
                var model = _questionSrvc.GetModelById(id);
                model.IsFQA = true;
                _questionSrvc.UpdateModel(model);

                return Content(Msg("true", "设置成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "设置失败！"));
            }
        }

        public ActionResult UnFAQ(int id)
        {
            try
            {
                var model = _questionSrvc.GetModelById(id);
                model.IsFQA = false;
                _questionSrvc.UpdateModel(model);

                return Content(Msg("true", "设置成功！"));
            }
            catch (Exception)
            {
                return Content(Msg("false", "设置失败！"));
            }
        }

        [HttpPost]
        public ActionResult SaveReply(int questionId, string content)
        {
            string result;
            try
            {
                var questionReply = new QuestionReply();
                questionReply.QuestionId = questionId;
                questionReply.ReplyUserName = CurrentUser.UserName;
                questionReply.ReplyDate = DateTime.Now;
                questionReply.Content = content;
                _questionReplySrvc.CreateModel(questionReply);
                result = Msg("true", "回复成功！");
            }
            catch (Exception)
            {
                result = Msg("true", "回复失败！");
            }
            return Content(result);
        }
        /// <summary>
        /// 根据经办参数对象获取 请示签报 列表
        /// </summary>
        /// <param name="hasDoneParameters"></param>
        /// <returns></returns>
        public ActionResult GetHasDoneAskTask(HasDoneParameters hasDoneParameters)
        {
            List<HasDoneViewModel> hasDoneViewModels = new List<HasDoneViewModel>();
            PagedResult<HasDoneViewModel> hasdonePagedResults = new PagedResult<HasDoneViewModel>();
            ////此处拿到的HasDoneViewModels为Distinct后的不带分页筛选的数据
            hasDoneViewModels = GetASRsHasDoneViewModels(hasDoneParameters, hasDoneViewModels);
            if (hasDoneParameters.pageIndex < 1)
            {
                hasDoneParameters.pageIndex = 1;
            }
            if (hasDoneParameters.pageSize < 1)
            {
                hasDoneParameters.pageSize = 30;
            }
            hasdonePagedResults = GetHasDonePagedResults(hasDoneViewModels, hasDoneParameters.pageIndex, hasDoneParameters.pageSize);
            return PartialView("AskingForSignReportTable", hasdonePagedResults);
        }
        /// <summary>
        /// 根据经办参数类获取 经过Distinct的 请示签报类 HasDoneViewModel对象集合
        /// </summary>
        /// <param name="hasDoneParameters"></param>
        /// <param name="hasDoneViewModels"></param>
        /// <returns></returns>
        private List<HasDoneViewModel> GetASRsHasDoneViewModels(HasDoneParameters hasDoneParameters, List<HasDoneViewModel> hasDoneViewModels)
        {
            IEnumerable<ConcreteWorkflow> concreteWorkflows =
                concreteWorkflowSvc.GetHasDoneTaskByHasDoneParameters(this.CurrentUser, hasDoneParameters).Where(o=>o.ConcreteWorkflowOperations.Count>1 && o.HaveFinished==true).ToList();
            IEnumerable<ConcreteWorkflow> circulateWorkflows =
                concreteWorkflowSvc.GetCirculateConcreteWorkflows(this.CurrentUser, hasDoneParameters).ToList();

            if (hasDoneParameters.StateID == (int)WorkflowState.HasDone)
            {
                if (circulateWorkflows.Any())
                {
                    concreteWorkflows = concreteWorkflows.Concat(
                                  from l2 in circulateWorkflows
                                  select l2).Distinct().ToList();

                }
            }
            else if (hasDoneParameters.StateID == (int)WorkflowState.Circulate)
            {
                concreteWorkflows = circulateWorkflows;
            }
            var controllerList = new List<string>();
            concreteWorkflows.ForEach(o => controllerList.Add(
                o.FormForWorkflow.EditUrl.Substring(1, o.FormForWorkflow.EditUrl.LastIndexOf("/") - 1)));
            var workflowTitles = new List<WorkflowTitleViewModel>();
            foreach (var controller in controllerList.Distinct())
            {
                if (controller == null || controller.Contains("/")) continue;
                var tempList = ResultMethod(controller, GetWorkflowTitle_MethodName, null) as List<WorkflowTitleViewModel>;
                if (tempList == null) continue;

                workflowTitles.AddRange(tempList);
            }

            var attachments = attachmentService.GetAttachments();
            hasDoneViewModels = (from concreteWorkflow in concreteWorkflows
                                 join
                                     attachment in attachments.Where(x => x.TypeID == (int)AttachmentType.Office) on
                                     concreteWorkflow.ConcreteWorkflowId equals attachment.RelateID
                                     into temp

                                 from tt in temp.DefaultIfEmpty()
                                 select new HasDoneViewModel
                                 {
                                     Title =
                                         string.IsNullOrEmpty(concreteWorkflow.Sn)
                                             ? "无流程名"
                                             : concreteWorkflow.PassRoundForPerusals.Any(
                                                 x => x.UserName == CurrentUser.UserName && x.IsRead)
                                                   ? "[传阅]" + concreteWorkflow.Sn
                                                   : concreteWorkflow.Sn,
                                     WorkflowTitle =
                                         workflowTitles.Any(
                                             o => o.ConcreteWorkflowId == concreteWorkflow.ConcreteWorkflowId)
                                             ? workflowTitles.Where(
                                                 o => o.ConcreteWorkflowId == concreteWorkflow.ConcreteWorkflowId)
                                                             .FirstOrDefault()
                                                             .WorkflowTitle
                                             : concreteWorkflow.Title,
                                     Sender = concreteWorkflow.ConcreteWorkflowOperations.Any()
                                                  ? concreteWorkflow.ConcreteWorkflowOperations.FirstOrDefault()
                                                                    .UserObj.DisplayName
                                                  : "空",
                                     EndDate = concreteWorkflow.ConcreteWorkflowOperations.Any()
                                                   ? concreteWorkflow.ConcreteWorkflowOperations.LastOrDefault()
                                                                     .DeadLine == null
                                                         ? concreteWorkflow.CreatedDate
                                                         : concreteWorkflow.ConcreteWorkflowOperations.LastOrDefault
                                                               ().DeadLine.Value
                                                   : concreteWorkflow.CreatedDate,
                                     TaskName = concreteWorkflow.ConcreteWorkflowOperations.Any()
                                                    ? concreteWorkflow.ConcreteWorkflowOperations.LastOrDefault()
                                                                      .WorkflowStatus.Name
                                                    : "无流程名称",
                                     SN = concreteWorkflow.ConcreteWorkflowId.ToString(),
                                     Remark =
                                         concreteWorkflow.ConcreteWorkflowOperations.LastOrDefault(
                                             o =>
                                             !string.IsNullOrEmpty(o.UserName) && o.UserName.Equals(CurrentUser.UserName) && !string.IsNullOrEmpty(o.Content)) == null
                                             ? "无填写意见"
                                             : concreteWorkflow.ConcreteWorkflowOperations.LastOrDefault(
                                                 o =>
                                                 !string.IsNullOrEmpty(o.UserName) && o.UserName.Equals(CurrentUser.UserName) && o.Content != null &&
                                                 o.Content != "").Content,
                                     StarDate = concreteWorkflow.CreatedDate,
                                     DepthId =
                                         concreteWorkflow.ConcreteWorkflowOperations.Any()
                                             ? concreteWorkflow.ConcreteWorkflowOperations.LastOrDefault()
                                                               .UserObj.DeptID.Value
                                             : 1,
                                     WorkflowState = hasDoneParameters.StateID,
                                     FormForWorkflowId = concreteWorkflow.FormForWorkflowId,
                                     FormFroWorkflowName = concreteWorkflow.FormForWorkflow.Name,
                                     ConcreteWorkflowId = concreteWorkflow.ConcreteWorkflowId,
                                     HasAttachs = tt != null ? true : false,
                                     AttachNames = tt != null ? tt.FileName : ""
                                 }).ToList();

            #region ////TODO:Luyuf 2013-04-27 U Knew they r bad codes..
            //Kelsey 2013-12-16 Yep,it's pretty bad...U must recode and reference them in the file(ConcreteWorkflowService.cs)

            if (hasDoneParameters.DepID != null)
            {
                hasDoneViewModels = hasDoneViewModels.FindAll(o => o.DepthId.Value.Equals(hasDoneParameters.DepID.Value));
            }

            if (hasDoneParameters.DeadlineStart != new DateTime() &&
                hasDoneParameters.DeadlineEnd != new DateTime())
            {
                hasDoneParameters.DeadlineEnd = hasDoneParameters.DeadlineEnd.AddDays(1).AddSeconds(-1);
                hasDoneViewModels =
                    hasDoneViewModels.FindAll(
                        o =>
                        o.EndDate >= hasDoneParameters.DeadlineStart && o.EndDate <= hasDoneParameters.DeadlineEnd);
            }
            else if (hasDoneParameters.DeadlineStart != new DateTime())
            {
                hasDoneViewModels = hasDoneViewModels.FindAll(o => o.EndDate >= hasDoneParameters.DeadlineStart);
            }
            else if (hasDoneParameters.DeadlineEnd != new DateTime())
            {
                hasDoneParameters.DeadlineEnd = hasDoneParameters.DeadlineEnd.AddDays(1).AddSeconds(-1);
                hasDoneViewModels = hasDoneViewModels.FindAll(o => o.EndDate <= hasDoneParameters.DeadlineEnd);
            }

            if (!string.IsNullOrEmpty(hasDoneParameters.AttachmentName))
            {
                hasDoneViewModels =
                    hasDoneViewModels.FindAll(o => o.AttachNames.ToLower().Contains(hasDoneParameters.AttachmentName.ToLower()));
            }

            //经办&工作报告 关联逻辑
            if (hasDoneViewModels.Count > 0 &&
                !string.IsNullOrEmpty(hasDoneParameters.Title) &&
                hasDoneParameters.FormForWorkflowId > 0)
            {
                hasDoneViewModels.ForEach(
                    o => hasDoneViewModels.AddRange(GetLinkReportDatas(o.ConcreteWorkflowId, o.FormForWorkflowId)));
            }
            #endregion

            return hasDoneViewModels.DistinctBy(o => o.ConcreteWorkflowId).ToList();
        }
        /// <summary>
        /// 与经办关联的工作报告数据集
        /// </summary>
        /// <param name="concreteWorkflowId"></param>
        /// <param name="formForWorkflowId"></param>
        /// <returns></returns>
        private List<HasDoneViewModel> GetLinkReportDatas(int concreteWorkflowId, int formForWorkflowId)
        {
            List<HasDoneViewModel> linkReportViewModels = new List<HasDoneViewModel>();
            List<ConcreteWorkflow> linkReportsConcreteWorkflows = new List<ConcreteWorkflow>();
            ////关联的内部工作报告
            var internalReports =
                internalReportService.GetInternalReportsByExpression(
                    o => o.LinkConcreteWorkflowId == concreteWorkflowId &&
                         o.LinkFormforWorkflowId == formForWorkflowId).ToList();
            ////关联的参控股企业内部工作报告
            var accusedHoldingCompaniesWorkReports =
                accusedHoldingCompaniesWorkReportService.GetAccusedHoldingCompaniesWorkReportsByExpression(
                    o => o.LinkConcreteWorkflowId == concreteWorkflowId &&
                         o.LinkFormforWorkflowId == formForWorkflowId).ToList();

            if (internalReports.Count > 0)
            {
                linkReportsConcreteWorkflows = internalReports.Select(o => o.ConcreteWorkflow).ToList();
            }
            if (accusedHoldingCompaniesWorkReports.Count > 0)
            {
                linkReportsConcreteWorkflows.AddRange(accusedHoldingCompaniesWorkReports.Select(o => o.ConcreteWorkflow));
            }

            #region 获取流程对应的表单标题
            var controllerList = new List<string>();
            linkReportsConcreteWorkflows.ForEach(o => controllerList.Add(
                o.FormForWorkflow.EditUrl.Substring(1, o.FormForWorkflow.EditUrl.LastIndexOf("/") - 1)));
            var workflowTitles = new List<WorkflowTitleViewModel>();
            foreach (var controller in controllerList.Distinct())
            {
                if (controller == null || controller.Contains("/")) continue;
                var tempList = ResultMethod(controller, GetWorkflowTitle_MethodName, null) as List<WorkflowTitleViewModel>;
                if (tempList == null) continue;

                workflowTitles.AddRange(tempList);
            }
            #endregion

            linkReportViewModels = (from linkReportsConcreteWorkflow in linkReportsConcreteWorkflows
                                    select new HasDoneViewModel
                                    {
                                        Title =
                                            string.IsNullOrEmpty(linkReportsConcreteWorkflow.Sn)
                                                ? "[关联]无流程名"
                                                : "[关联]" + linkReportsConcreteWorkflow.Sn,
                                        WorkflowTitle =
                                            workflowTitles.Any(
                                                o =>
                                                o.ConcreteWorkflowId ==
                                                linkReportsConcreteWorkflow.ConcreteWorkflowId)
                                                ? workflowTitles.Where(
                                                    o =>
                                                    o.ConcreteWorkflowId ==
                                                    linkReportsConcreteWorkflow.ConcreteWorkflowId)
                                                                .FirstOrDefault()
                                                                .WorkflowTitle
                                                : "无标题名",
                                        Sender = linkReportsConcreteWorkflow.ConcreteWorkflowOperations.Any()
                                                     ? linkReportsConcreteWorkflow.ConcreteWorkflowOperations
                                                                                  .FirstOrDefault()
                                                                                  .UserObj.DisplayName
                                                     : "空",
                                        EndDate = linkReportsConcreteWorkflow.ConcreteWorkflowOperations.Any()
                                                      ? linkReportsConcreteWorkflow.ConcreteWorkflowOperations
                                                                                   .LastOrDefault().DeadLine == null
                                                            ? linkReportsConcreteWorkflow.CreatedDate
                                                            : linkReportsConcreteWorkflow.ConcreteWorkflowOperations
                                                                                         .LastOrDefault()
                                                                                         .DeadLine.Value
                                                      : linkReportsConcreteWorkflow.CreatedDate,
                                        TaskName = linkReportsConcreteWorkflow.ConcreteWorkflowOperations.Any()
                                                       ? linkReportsConcreteWorkflow.ConcreteWorkflowOperations
                                                                                    .LastOrDefault()
                                                                                    .WorkflowStatus.Name
                                                       : "无流程名称",
                                        SN = linkReportsConcreteWorkflow.ConcreteWorkflowId.ToString(),
                                        Remark =
                                            linkReportsConcreteWorkflow.ConcreteWorkflowOperations.LastOrDefault(
                                                o => !string.IsNullOrEmpty(o.UserName) && o.UserName.Equals(CurrentUser.UserName)) == null
                                                ? "无填写意见"
                                                : linkReportsConcreteWorkflow.ConcreteWorkflowOperations
                                                                             .LastOrDefault(
                                                                                 o =>
                                                                                 !string.IsNullOrEmpty(o.UserName) && o.UserName.Equals(
                                                                                     CurrentUser.UserName)).Content,
                                        StarDate = linkReportsConcreteWorkflow.CreatedDate,
                                        DepthId =
                                            linkReportsConcreteWorkflow.ConcreteWorkflowOperations.Any()
                                                ? linkReportsConcreteWorkflow.ConcreteWorkflowOperations
                                                                             .LastOrDefault().UserObj.DeptID.Value
                                                : 1,
                                        WorkflowState = linkReportsConcreteWorkflow.CurrentStatusId,
                                        FormForWorkflowId = linkReportsConcreteWorkflow.FormForWorkflowId,
                                        FormFroWorkflowName = linkReportsConcreteWorkflow.FormForWorkflow.Name,
                                        ConcreteWorkflowId = linkReportsConcreteWorkflow.ConcreteWorkflowId
                                    }
                                   ).ToList();

            return linkReportViewModels;
        }
        /// <summary>
        /// 根据条件获取PagedResult类型的hasdoneViewModel对象集合
        /// </summary>
        /// <param name="hasDoneViewModels"></param>
        /// <param name="pageIndex"></param>
        /// <param name="pageSize"></param>
        /// <returns></returns>
        private PagedResult<HasDoneViewModel> GetHasDonePagedResults(List<HasDoneViewModel> hasDoneViewModels, int pageIndex, int pageSize)
        {
            PagedResult<HasDoneViewModel> hasdonePagedResults = new PagedResult<HasDoneViewModel>();
            hasdonePagedResults.CurrentPage = pageIndex;
            hasdonePagedResults.PageSize = pageSize;
            if (hasDoneViewModels.Count > 0)
            {

                hasdonePagedResults.RowCount = hasDoneViewModels.Count;
                hasdonePagedResults.PageCount =
                    (int)Math.Ceiling(hasdonePagedResults.RowCount / (double)hasdonePagedResults.PageSize);
                hasdonePagedResults.Results = hasDoneViewModels.Skip((pageIndex - 1) * pageSize).Take(pageSize).ToList();
            }
            return hasdonePagedResults;

        }


        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "草稿箱", AreaName = "Oms")]
        public ActionResult Draft()
        {
            ViewBag.Sheet = 4;
            var concreteWorkflows = concreteWorkflowSvc.GetDraftConcreteWorkflows(this.CurrentUser, _WorkflowFormType);
            return View(concreteWorkflows);
        }


        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "暂停箱", AreaName = "Oms")]
        public ActionResult Suspend()
        {
            ViewBag.Sheet = 5;
            var concreteWorkflows = concreteWorkflowSvc.GetSuspendConcreteWorkflows(this.CurrentUser, _WorkflowFormType);
            return View(concreteWorkflows);
        }


        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "错发回收", AreaName = "Oms")]
        public ActionResult WrongRetrieve()
        {
            ViewBag.Sheet = 6;
            var concreteWorkflows = concreteWorkflowSvc.GetWrongRetrieveConcreteWorkflows(this.CurrentUser, _WorkflowFormType);
            return View(concreteWorkflows);
        }


        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "经办业务", AreaName = "Oms")]
        public ActionResult HasDone()
        {
            ViewBag.Sheet = 3;
            return View();
        }

        /// <summary>
        /// 已阅
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpPost]
        public ActionResult HasRead(int id, string content)
        {
            if (string.IsNullOrEmpty(content))
            {
                content = "已阅(默认)";
            }
            if (passRoundForPerusalService.HasRead(id, this.CurrentUser, content))
                return this.Content(Msg("true", "已阅！"));
            else
                return this.Content(Msg("false", "操作失败"));
        }
        /// <summary>
        /// 暂停功能实现
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ActionResult ToStop(int id)
        {
            try
            {
                var result = concreteWorkflowSvc.ToSuspend(id);
                return this.Content(string.Format("{{\"success\": {0},\"message\":\"{1}\"}}", result.ToString().ToLower(), "已暂停!"));
            }
            catch (Exception)
            {
                return this.Content(string.Format("{{\"success\": {0},\"message\":\"{1}\"}}", false.ToString().ToLower(), "操作失败!"));
            }
        }


        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "工作委托", AreaName = "Oms")]
        public ActionResult WorkDelegate()
        {
            ViewBag.Sheet = 7;
            return View(_WorkflowFormType);
        }

        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "新建流程", AreaName = "Oms")]
        public ActionResult CreateFlow()
        {
            ViewBag.Sheet = 1;
            var formForWorkflows = forWorkflowSvc.GetFormForWorkflowsByWorkflowFormType(_WorkflowFormType, CurrentUser.UserName);
            formForWorkflows = formForWorkflows.Where(o => !o.Name.Equals("培训反馈表")).OrderBy(o=>o.OrderId);
            var ffwf = new List<WorkflowClass>();
            Type enumType = typeof(WorkflowFormType);
            //foreach (WorkflowFormType w in Enum.GetValues(enumType))
            //{
            //    if (w.ToInt() == (int)WorkflowFormType.Qusetion) continue;
            //    var name = w.GetEnumDisplay();
            //    ffwf.Add(new WorkflowClass()
            //    {
            //        className = name,
            //        data = formForWorkflows.Where(m => m.WorkflowFormTypeValue == w.ToInt()).ToList()
            //    });
            //}
            ffwf.Add(new WorkflowClass()
            {
                className = "服务中心",
                data = formForWorkflows.Where(m => m.WorkflowFormTypeValue == 256).ToList()
            });
            return View(ffwf);
        }


        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "待办业务", AreaName = "Oms")]
        public ActionResult ToDo(bool? onlyPassRound)
        {
            ViewBag.Sheet = 2;
            var concreteWorkflows = concreteWorkflowSvc.GetToDoConcreteWorkflows(this.CurrentUser,
                                                                                 (int)WorkflowFormType.OMS, true);

            if (onlyPassRound == true)
            {
                concreteWorkflows = concreteWorkflows.Where(c => c.PassRoundForPerusals.Any(p => p.UserName == CurrentUser.UserName));
            }

            return View(concreteWorkflows);
        }


        [PermissionDisplay(ModuleName = "一站式管理", MenuName = "客户服务中心", Name = "工作流一览", AreaName = "Oms")]
        public ActionResult AllFlow()
        {
            ViewBag.Sheet = 8;
            ViewBag.Url = Url.Action("Detail", "Question");
            return View(_WorkflowFormType);
        }

        /// <summary>
        /// 回收功能
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ActionResult Retrieve(int id)
        {
            var concreteWorkflow = concreteWorkflowSvc.GetConcreteWorkflow(id);
            var concreteOperation = concreteWorkflow.ConcreteWorkflowOperations.LastOrDefault(o => !o.HaveFinished);
            var mobileMessage = new MobileMessage();
            mobileMessage.Message = string.Format("办件提醒：【{0}】(【{1}】)已被 {2} 错发回收。【{3}】",
                concreteWorkflow.FormForWorkflow.Name, concreteWorkflow.Sn, this.CurrentUser.DisplayName, DateTime.Now);
            mobileMessage.SendUserName = this.CurrentUser.UserName;
            mobileMessage.ReceiverUserName = concreteOperation.UserName;
            mobileMessage.Type = MobileMessageType.WrongRetrieve;
            mobileMessage.MobilePhone = concreteOperation.UserObj.MobilePhone;
            var rst_mobile = EmppSender(mobileMessage, true);

            concreteWorkflowSvc.ToRetrieve(id, this.CurrentUser);
            return RedirectToAction("WrongRetrieve");
        }

        public ActionResult Detail(int formforworkflowid, int concreteworkflowid, bool isDraft = false, bool? isView = false, string formforWorkFlowName = "")
        {
            //是否显示退回
            bool CanBack = false;
            bool CanEnd = false;
            //是否显示已阅
            bool hasCirculateWorkflows = false;
            ConcreteWorkflow CWF = new ConcreteWorkflow();
            int fileCount = 0;

            if (!string.IsNullOrWhiteSpace(formforWorkFlowName))
            {
                var forWorkflow = forWorkflowSvc.GetFormForWorkflowByName(formforWorkFlowName);
                if (forWorkflow != null)
                {
                    formforworkflowid = forWorkflow.FormForWorkflowId;
                }
            }

            //判断当前用户是否拥有建工查询权限
            bool hasJGCXRole = this.CurrentUser.Roles.Any(o => o.RoleName.Equals(JGCX_RoleName));
            //创建
            if (concreteworkflowid == 0)
            {
                //判断是否有权限新增 
                if (HasPermission("Create", "OfficeWorkFlow", ActionMethodConst.GET))
                {
                    CWF.FormForWorkflow = forWorkflowSvc.GetFormForWorkflow(formforworkflowid);
                    CWF.FormForWorkflowId = formforworkflowid;
                    CWF.CurrentStatus = workflowStatusSvc.GetStartedStatus(CWF.FormForWorkflow.WorkflowId);
                    CWF.CurrentStatusId = CWF.CurrentStatus.WorkflowStatusId;
                }
                else
                {
                    return View("PermissionDenied");
                }
            }
            else
            {
                bool flag = false;
                //待办
                var TODOconcreteWorkflows = concreteWorkflowSvc.GetToDoConcreteWorkflowsByID(this.CurrentUser, concreteworkflowid, _WorkflowFormType);
                flag = TODOconcreteWorkflows.Any() && (isView == true ? false : true);
                //判断退回按钮
                if (flag)
                {
                    var rules =
                        workflowRuleService.GetWorkflowRules(TODOconcreteWorkflows.First().FormForWorkflow.WorkflowId,
                            TODOconcreteWorkflows.First().CurrentStatusId);
                    CanBack = rules.Where(x => !x.IsSequence).Any();

                    CanEnd = rules.Where(x => x.Status.IdForTheWorkflow == 0).Any() || rules.Where(x => x.NextStatus.IdForTheWorkflow == 0).Any() || rules.Count() == 0;
                }
                if (hasJGCXRole)
                {
                    //建工查询角色不用判断是否为自己流程(跳过权限判断)
                    flag = true;
                }
                else
                {
                    //经办
                    var HasDoneconcreteWorkflows = concreteWorkflowSvc.GetHasDoneConcreteWorkflowsByID(this.CurrentUser, concreteworkflowid, _WorkflowFormType);
                    var TodoCirculateWorkflows = concreteWorkflowSvc.GetCirculateConcreteWorkflows(this.CurrentUser,
                        _WorkflowFormType, concreteworkflowid: concreteworkflowid);
                    hasCirculateWorkflows = TodoCirculateWorkflows.Any();
                    flag = flag ||
                           (HasDoneconcreteWorkflows.Any() && (isView == true ? true : false)) || hasCirculateWorkflows;
                }
                if (flag)
                {
                    CWF = concreteWorkflowSvc.GetConcreteWorkflow(concreteworkflowid);
                    //附件个数
                    fileCount = attachmentService.GetAttachments((int)AttachmentType.Question, CWF.ConcreteWorkflowId).Count();
                }
                else
                {
                    return View("PermissionDenied");
                }
            }
            if (CWF.CurrentStatus.IdForTheWorkflow < 1)
            {
                var nextRules = workflowRuleService.GetWorkflowRules(CWF.FormForWorkflow.WorkflowId, CWF.CurrentStatusId);
                ViewBag.HasBackInFinish = nextRules.Any() ? true : false;
            }
            ViewBag.CanBack = CanBack;
            ViewBag.isView = isView;
            ViewBag.fileCount = fileCount;
            ViewBag.IsCirculate = hasCirculateWorkflows;
            ViewBag.isDraft = isDraft;
            var next = workflowRuleService.GetNextRulesByConditions(CWF, false, CurrentUser.UserName);
            ViewBag.isEnd = next.Any(o => o.NextStatus.IdForTheWorkflow != 0);
            ViewBag.CanEnd = CanEnd;
            ViewBag.isSC = "Y";
            return View(CWF);
        }
    }
}