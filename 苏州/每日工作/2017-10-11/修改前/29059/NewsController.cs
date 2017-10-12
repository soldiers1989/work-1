//-----------------------------------------------------------------------
// <copyright file="NewsController.cs" company="Wiseonline">
//     Copyright (c) Shanghai Pudong Software Park Wiseonline Softeware,Inc. All rights reserved.
// </copyright>
// <author>Luyuquan</author>
//-----------------------------------------------------------------------
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Helpers;
using System.Web.Mvc;
using Microsoft.Web.Mvc;
using Wiseonline.Eisp.Data;
using Wiseonline.Eisp.Data.Infrastructure;
using Wiseonline.Eisp.Domain;
using Wiseonline.Eisp.Helpers;
using Wiseonline.Eisp.Service;
using Wiseonline.Eisp.Web.Helpers;
using Wiseonline.Eisp.Web.ViewModel;
using Wiseonline.Eisp.Web.Dto;
using System.Web.Mvc.Html;
using System.Linq.Expressions;
using System.Text;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Web.Script.Serialization;

using Wiseonline.Eisp.Utility;
using System.Configuration;
using System.IO;
using Newtonsoft.Json;

namespace Wiseonline.Eisp.Web.Controllers
{
    /// <summary>
    /// 新闻公告控制器
    /// </summary>
    public class NewsController : BaseController
    {
        private readonly IConstTypeService typeService = null;
        private readonly INewsService newsService = null;
        private readonly ICompanyService companyService = null;
        private readonly ISecurityService securityService = null;
        private readonly IAttachmentService attachmentService = null;
        private readonly IFavoriteService favoriteService = null;
        private readonly IPersonnelStructureQuarterlyReportService personnelStructureQuarterlyReportService = null;
        private readonly IBusinessStateService businessStateService = null;
        private readonly IBusinessStateTypeService businessStateTypeService = null;
        private readonly IDepartmentService deptService = null;
        private readonly ICultureService cultureService = null;
        private readonly INewsReplyService newsReplyService = null;
        private readonly IMobileCommonHelper mobileCommonHelper = null;
        private readonly IGetUserTree getUserTree = null;

        /// <summary>
        /// 汇智交流标识ID
        /// </summary>
        private readonly int Communication_TypeID = Convert.ToInt32(Application.CustomSettings["WiseCommunication"]);

        /// <summary>
        /// 综合信息 允许回复的类型ID
        /// </summary>
        private readonly string AllowReply_TypeIDs = Application.CustomSettings["AllowReplyTypeIDs"];


        public NewsController(INewsService _newsService, IConstTypeService _typeService, ICompanyService _companyService,
            ISecurityService _securityService, IAttachmentService _attachmentService,
            IFavoriteService _favoriteService,
            IPersonnelStructureQuarterlyReportService _personnelStructureQuarterlyReportService,
            IDepartmentService _deptService,
            IBusinessStateService _businessStateService,
            IBusinessStateTypeService businessStateTypeService, ICultureService _cultureService,
            IMobileMessageService mobileMessageService, INewsReplyService _newsReplyService,
            IMobileCommonHelper mobileCommonHelper, IGetUserTree getUserTree)
            : base(mobileMessageService)
        {
            this.newsService = _newsService;
            typeService = _typeService;
            companyService = _companyService;
            securityService = _securityService;
            attachmentService = _attachmentService;
            favoriteService = _favoriteService;
            personnelStructureQuarterlyReportService = _personnelStructureQuarterlyReportService;
            businessStateService = _businessStateService;
            this.businessStateTypeService = businessStateTypeService;
            deptService = _deptService;
            this.cultureService = _cultureService;
            this.newsReplyService = _newsReplyService;
            this.ValidateRequest = false;
            this.mobileCommonHelper = mobileCommonHelper;
            this.getUserTree = getUserTree;
        }

        #region 浦软信息

        /// <summary>
        /// 综合信息分类总览
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "纳米园信息", Name = "显示分类预览")]
        public ActionResult Index()
        {
            var news = newsService.GetIndexPageNews(CurrentUser.UserName, 5).OrderBy(nty => nty.CompanyType);

            var companyList = news.Select(n => n.CompanyType).Distinct();
            var typeList = news.Select(n => n.TypeModel).Distinct();

            ViewBag.CompanyList = companyList;
            ViewBag.TypeList = typeList;

            ViewBag.News = news.ToArray(); //所有信息条目
            ViewBag.Title = "公司动态";
            ViewBag.MarkNewDays = ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue;
            //标识为新增信息的日期量;    //标识为新增信息的日期量
            ViewBag.RowCount = 5; //定义每种类型显示的信息条数
            return View();
        }


        /// <summary>
        /// 综合信息“更多”列表
        /// </summary>
        /// <param name="companyID">公司编号</param>
        /// <param name="typeID">信息分类编号</param>
        /// <param name="publishType">发表类型（由我发表/未提交审核/审核中）</param>
        /// <param name="customType">自定义信息子类型</param>
        /// <returns></returns>
        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "公司信息", Name = "显示信息列表")]
        public ActionResult ShowMore(int? companyID, int? typeID, int? publishType, string customType = "")
            //,int? pageIndex, int? departmentID, string searchText = "", 
        {
            var inputParams = new NewsSearchParams();

            if (companyID != null)
            {
                inputParams.CompanyId = (int) companyID;
            }

            if (typeID > 0)
            {
                inputParams.TypeId = (int) typeID;
            }

            if (publishType > 0)
            {
                inputParams.PublishType = (int) publishType;
            }

            if (!string.IsNullOrWhiteSpace(customType))
            {
                inputParams.CustomType = customType;
            }

            inputParams.PageIndex = 1;
            return ShowMoreNewses(inputParams);
        }


        /// <summary>
        /// 异步查询综合信息
        /// </summary>
        /// <param name="inputParams"></param>
        /// <returns></returns>
        [HttpPost]
        public ActionResult ShowMoreNewses(NewsSearchParams inputParams)
        {
            if (inputParams == null)
            {
                //通讯错误,返回错误提示
                throw new Exception("异步查询发生错误！");
            }

            inputParams.PageSize = 10;
            inputParams.CurrentUserName = CurrentUser.UserName;
            inputParams.CurrentUserCompanyId = CurrentUser.Department.CompanyID;
            //获取信息
            var newsList = newsService.GetNewsByPage(inputParams);

            if (newsList == null || newsList.Results == null)
            {
                //查询出错,返回错误提示
            }

            //获取每条信息的附件数量
            foreach (var aNews in newsList.Results)
            {
                aNews.AttachmentCount =
                    attachmentService.GetAttachments((int) AttachmentType.NewsAttachment, aNews.NewsId).Count();
            }

            #region 将当前的查询条件值编号传入页面

            StringBuilder tmpJsonStr = new StringBuilder();
            JavaScriptSerializer jsonSerializer = new JavaScriptSerializer();
            jsonSerializer.Serialize(inputParams, tmpJsonStr);
            ViewBag.SearchParams = tmpJsonStr.ToString();
            ViewBag.SearchParamsObj = inputParams;

            //公司集合
            var cmyList = companyService.GetCompanies();
            //分类集合
            var typeList = typeService.GetConstTypes().ToList();
            ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue; //标识为新增信息的日期量
            ViewBag.CompanyCollection = cmyList;
            if (!(CurrentUser.Roles.Any(r => r.Describe == "重点工作发起人")))
            {
                typeList = typeList.Where(s => s.TypeName != "重点工作").ToList();
            }
            ViewBag.TypeCollection = typeList.Where(r => r.MainType == TypeEnum.News || r.MainType == TypeEnum.Oms);
            ViewBag.TypeZeroCollection = typeList.Where(r => r.MainType == TypeEnum.News || r.MainType == TypeEnum.Oms).Select(n => n.Description).Distinct();
            //r.TypeId != (int)ShowNewsType.Others &&
            ViewBag.Company = companyService.GetCompanies().FirstOrDefault(c => c.CompanyID == inputParams.CompanyId);
            ViewBag.CurrentType = typeList.FirstOrDefault(t => t.TypeId == inputParams.TypeId);
            var getType = (ShowNewsType) Enum.ToObject(typeof (ShowNewsType), inputParams.TypeId);
            ViewBag.Title =getType.GetEnumDisplay();

            #endregion

            return View("ShowMore", newsList);
        }

        /// <summary>
        /// 获取部门数据
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public string GetDepartmentData(int? companyID)
        {
            IEnumerable<Department> deptData = null;
            if (companyID > 0)
            {
                deptData = deptService.GetDepartmentByCompany((int) companyID);
            }
            else
            {
                deptData = deptService.GetDepartments();
            }

            StringBuilder jsonStr = new StringBuilder("[{\"id\":\"0\",\"text\":\"不限定\"},");

            foreach (var item in deptData)
            {
                jsonStr.Append("{");
                jsonStr.Append("\"id\":");
                jsonStr.Append("\"" + item.DeptID + "\"");
                jsonStr.Append(",\"text\":");
                jsonStr.Append("\"" + item.DeptName + "\"");
                jsonStr.Append("},");
            }

            return jsonStr.ToString().Trim(',') + "]";
        }

        /// <summary>
        /// 获取信息列表
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        //  [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "获取信息(程序调用,不需设权限)")]
        public ActionResult GetNewsList(int? type)
        {
            List<Expression<Func<News, bool>>> queryExpressionList = new List<Expression<Func<News, bool>>>();
            if (type == null)
            {
                queryExpressionList.Add(n => n.Status_Value == (int) NewsStatus.Auditting);
            }
            else
            {
                queryExpressionList.Add(n => n.AddUser != null && n.AddUser.UserName == CurrentUser.UserName);
            }
            var newsList = newsService.GetNewsesByExpsList(queryExpressionList, 1, 99999);
            var lqVar = from n in newsList.Results
                select n.Dto();

            return Json(new {total = lqVar.Count(), rows = lqVar}, JsonRequestBehavior.AllowGet);
        }


        /// <summary>
        /// 查看信息审核页面
        /// </summary>
        /// <returns></returns>
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "查看待审核信息")]
        public ActionResult AuditList()
        {
            return View();
        }

        /// <summary>
        /// 获取待审核信息列表
        /// </summary>
        /// <returns></returns>
        //     [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "获取待审核信息(程序调用,不需设权限)")]
        public ActionResult GetAudittingNewsList()
        {
            List<Expression<Func<News, bool>>> queryExpressionList = new List<Expression<Func<News, bool>>>();

            queryExpressionList.Add(
                n =>
                    n.Status_Value == (int) NewsStatus.Auditting && n.AuditUser != null &&
                    n.AuditUser == CurrentUser.UserName);

            var newsList = newsService.GetNewsesByExpsList(queryExpressionList, 1, 99999);

            var lqVar = from n in newsList.Results
                select n.Dto();

            return Json(new {total = lqVar.Count(), rows = lqVar}, JsonRequestBehavior.AllowGet);
        }

        /// <summary>
        /// 编辑信息
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        public ActionResult GetEditList(string IdString)
        {
            ViewBag.IdString = IdString;
            return View();
        }

        /// <summary>
        /// 获取信息列表
        /// </summary>
        /// <param name="IdString">编号字符串</param>
        /// <returns></returns>
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "查看可编辑信息")]
        public ActionResult GetCurrentPageNewsList(string IdString)
        {
            List<Expression<Func<News, bool>>> queryExpressionList = new List<Expression<Func<News, bool>>>();

            queryExpressionList.Add(
                n =>
                    n.AddUser.UserName == CurrentUser.UserName &&
                    (n.Status_Value == (int) NewsStatus.Auditting || n.Status_Value == (int) NewsStatus.Reject) &&
                    n.IsDel == false);

            if (!string.IsNullOrWhiteSpace(IdString))
            {
                string[] tmpStrAry = IdString.Split(',');
                List<int> idList = new List<int>();
                int tmpId = 0;
                foreach (string idstr in tmpStrAry)
                {
                    if (int.TryParse(idstr, out tmpId))
                    {
                        idList.Add(tmpId);
                    }
                }
                int[] resultIdArray = idList.ToArray();
                queryExpressionList.Add(n => resultIdArray.Contains(n.NewsId));
            }

            var newsList = newsService.GetNewsesByExpsList(queryExpressionList, 1, 10);
            var lqVar = from n in newsList.Results
                select n.Dto();

            return Json(new {total = lqVar.Count(), rows = lqVar}, JsonRequestBehavior.AllowGet);
        }

        //
        //        /// <summary>
        //        /// 编辑信息
        //        /// </summary>
        //        /// <returns></returns>
        //        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "编辑信息")]
        //        public ActionResult Edit(string IdString)
        //        {
        //            ViewBag.IdString = IdString;
        //            return View();
        //        }

        /// <summary>
        /// 获取一条待编辑信息
        /// </summary>
        /// <param name="id">编号字符串</param>
        /// <returns></returns>
        [HttpPost]
        public JsonResult GetAnNews(int id)
        {
            NewsViewModel returnObj = null;
            var anews = newsService.GetNews(id);
            if (anews != null)
            {
                returnObj = anews.Dto();

                var fileList = attachmentService.GetAttachments((int) AttachmentType.NewsAttachment, anews.NewsId);

                returnObj.FileList =
                    fileList.Select(f => new AttachInfoStru {Id = f.AttachmentId, Name = f.FileName}).ToArray();
            }
            return Json(returnObj);
        }


        /// <summary>
        /// 浏览具体信息
        /// </summary>
        /// <param name="newsID"></param>
        /// <returns></returns>
        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "信息查看")]
        public ActionResult ShowNews(int newsID, int pageIndex = 1, int pageSize = 10)
        {
            News aNews = newsService.GetNews(newsID);
            if (aNews.Status == NewsStatus.Audited)
            {
                aNews.VisitNum = aNews.VisitNum + 1;
                newsService.UpdateNews(aNews);
            }

            aNews = newsService.GetNews(newsID);
            var attachments = attachmentService.GetAttachments((int) AttachmentType.NewsAttachment, newsID);
            ViewBag.Attachments = attachments;
            //ViewBag.Title = aNews.Title;
            ViewBag.TypeName = aNews.Type.TypeName;

            #region 综合信息回复

            //是否有回复功能
            ViewBag.HasReplyMode = false;
            //获取可回复的综合信息类型id数组
            var allowReplyTypies = AllowReply_TypeIDs.Split(',');

            if (allowReplyTypies.Contains(aNews.TypeID.ToString()))
            {
                ViewBag.HasReplyMode = true;
                var newsReplies = newsReplyService.GetListByPage(o => o.NewsId == aNews.NewsId,
                    o => o.ReplyDate,
                    pageIndex, pageSize, true);
                //绑定至ViewData供前台获取
                ViewData["NewsReplies"] = newsReplies;
            }

            #endregion

            foreach (var attachment in attachments)
            {
                if (attachment.Path.Contains(".pdf"))
                {
                    //ViewBag.PdfPath = AppDomain.CurrentDomain.BaseDirectory + attachment.Path;
                    ViewBag.PdfPath = attachment.Path.Replace('\\', '/');
                }
            }

            if (aNews.PublishWay_Value == (int) PublishWay.Normal)
            {
                return View(aNews);
            }
            else
            {
                return View("ShowSpecNews", aNews);
            }
        }

        public ActionResult SaveNewReply(NewsReply newsReply, int newsId)
        {
            try
            {
                newsReply.ReplyUser = CurrentUser.UserName;
                newsReply.ReplyDate = DateTime.Now;
                newsReply.NewsId = newsId;
                newsReplyService.CreateModel(newsReply);

                return Json(new {result = true, message = "留言成功！", newsId = newsId}, "text/html");
            }
            catch (Exception)
            {
                return Json(new {result = true, message = "留言失败，请稍后再试！"}, "text/html");
            }
        }

        [ValidateInput(false)]
        public ActionResult PreviewNews(int PublishTypeId, string Title, string Content)
        {
            News previewNewsModel = new News();
            previewNewsModel.PublishTypeId = PublishTypeId;
            previewNewsModel.Title = Title;
            previewNewsModel.Content = Content;
            previewNewsModel.AddDate = DateTime.Now;
            previewNewsModel.AddUser = CurrentUser;

            //var files = AttachmentHelper.GetUploadFileDictionay(this).Select(fd => fd.File).ToList();
            //if (files.Any())
            //{
            //    foreach (var attachment in files)
            //    {
            //        if (attachment.FileName.Contains(".pdf"))
            //        {
            //            ViewBag.PdfPath = AppDomain.CurrentDomain.BaseDirectory + AttachmentHelper.SaveFileCore(attachment, "NewsPreview", true, true);
            //            break;
            //        }
            //    }
            //}
            return View("ShowNews", previewNewsModel);

        }

        /// <summary>
        /// 发布信息
        /// </summary>
        /// <param name="news"></param>
        /// <param name="isSpecNews">是否为 快捷发布等特殊发布方式</param>
        /// <returns></returns>
        [HttpPost]
        [ValidateInput(true)]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "信息保存")]
        public ActionResult SaveNews(NewsViewModel news, bool isSpecNews = false)
        {
            var errMsg = "";
            var result = false;

            //if (filesDicList != null)
            //{
            //    var selecedFiles =
            //        filesDicList.Where(f => guidArray.Contains(f.FileGuid)).Select(fd => fd.File).ToList();

            //    if (news.PublishTypeId == 1 && !selecedFiles.Any(s => s.FileName.Contains(".pdf")))
            //    {       
            //        return Json(new { Success = false, Message = "未上传PDF文件！" });
            //    }
            //}

            do
            {
                if (!HasPermission("SaveNews", "News", ActionMethodConst.POST))
                {
                    errMsg = "你没有此操作权限！";
                    break;
                }


                //拷贝前端回传的信息
                var newModel = new News();
                ModelCopier.CopyModel(news, newModel);
                newModel.ModifyDate = DateTime.Now;

                //保存
                if (!isSpecNews)
                {
                    result = newsService.PublishNews(newModel, CurrentUser, ref errMsg);
                }
                else
                {
                    result = newsService.PublishNews(newModel, CurrentUser, ref errMsg, true);
                }

                if (!result)
                {
                    break;
                }


                bool fileResult = true;

                if (!isSpecNews)
                {
                    var filesDicList = AttachmentHelper.GetUploadFileDictionay(this);
                    if (filesDicList != null)
                    {
                        string[] guidArray = new string[0];
                        if (!string.IsNullOrWhiteSpace(news.CurrentFilesGuidSting))
                        {
                            guidArray = news.CurrentFilesGuidSting.Split(',');
                        }
                        var selecedFiles =
                            filesDicList.Where(f => guidArray.Contains(f.FileGuid)).Select(fd => fd.File).ToList();

                        //保存附件
                        foreach (var item in selecedFiles)
                        {
                            fileResult = AttachmentHelper.SaveAttachment(attachmentService, item,
                                AttachmentType.NewsAttachment, newModel.NewsId, CurrentUser.UserName, ref errMsg);
                            if (!fileResult) //发生错误时跳出
                            {
                                break;
                            }
                        }
                    }

                }
                else
                {
                    var fileList =
                        attachmentService.GetAttachments((int) AttachmentType.Office, news.RelatedWorkflowId).ToList();
                    if (fileList.Count > 0)
                    {
                        foreach (var attachment in fileList)
                        {
                            try
                            {
                                attachment.TypeID = (int) AttachmentType.NewsAttachment;
                                attachment.RelateID = newModel.NewsId;
                                attachment.AddUser = newModel.AddUser.UserName;
                                attachmentService.CreateAttachment(attachment);
                            }
                            catch (Exception)
                            {
                                fileResult = false;
                            }
                        }
                    }
                }

                if (!fileResult)
                {
                    errMsg = "信息保存成功,但保存附件时发生错误,原因:" + errMsg;
                    break;
                }

                #region 给审核人发送短信

                if (news.ConfirmAuditor)
                {
                    if (news.AuditUserArray != null && news.AuditUserArray.Length > 0)
                    {
                        var user = securityService.GetUser(news.AuditUserArray[0]);
                        if (HasPermission(user, "Audit", "News", ActionMethodConst.POST))
                        {
                            //EispWebService.EmppSender(user.MobilePhone, "您有一条待审核的浦软信息。");
                            var mobileMessage = new MobileMessage();
                            mobileMessage.Message = "您有一条待审核的信息。";
                            mobileMessage.SendUserName = CurrentUser.UserName;
                            mobileMessage.ReceiverUserName = user.UserName;
                            mobileMessage.Type = MobileMessageType.PuRuanNews;
                            mobileMessage.MobilePhone = user.MobilePhone;
                            EmppSender(mobileMessage, true);
                        }
                        else
                        {
                            result = false;
                            errMsg = "指定的审核人没有审核权限!";
                        }
                    }
                    else
                    {
                        result = false;
                        errMsg = "未指定审核人!";
                    }
                }

                #endregion

            } while (false);

            return Json(new {Success = result, Message = errMsg});
        }

        [HttpGet]
        public ActionResult Audit()
        {
            return View();
        }

        /// <summary>
        /// 批准提交审核的信息
        /// </summary>
        /// <param name="newsList"></param>
        /// <returns></returns>
        [HttpPost]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "信息审核")]
        public ActionResult Audit(IEnumerable<NewsViewModel> newsList)
        {
            if (!HasPermission("Audit", "News", ActionMethodConst.POST))
            {
                return Json(new {Message = string.Format("你没有此操作权限！")});
            }

            string errMsg = "";
            foreach (NewsViewModel item in newsList)
            {
                if (!newsService.AuditNews(item.NewsId, CurrentUser.UserName, ref errMsg))
                {
                    return Json(new {Message = errMsg});
                }
                //只有类型为通知公告时才发手机推送
                if (item.TypeID == (int) ShowNewsType.Announcement)
                {
                    var noticationArray = new string[] {};
                    if (!string.IsNullOrEmpty(item.BrowserIDs))
                    {
                        noticationArray = item.BrowserIDs.Split(',');
                        //MobileCommonHelper.MergerArray(noticationArray, item.BrowserIDs.Split(','));
                    }
                    if (!string.IsNullOrEmpty(item.BrowserGroupIDs))
                    {
                        var browserGroupIds = item.BrowserGroupIDs.Split(',');
                        for (int i = 0; i < browserGroupIds.Length; i++)
                        {
                            var group = getUserTree.GetUserNameArrayByGroup(browserGroupIds[i]);
                            noticationArray = MobileCommonHelper.MergerArray(noticationArray, group);
                        }
                    }
                    var title = string.Format("公告提醒：[{0}]{1}", item.CompanyShortName, item.Title);
                    //消息推送
                    Task.Factory.StartNew(
                        () =>
                            mobileCommonHelper.SendNoticationByHttp(noticationArray, title,
                                DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"),
                                MsgType.News, item.NewsId),
                        TaskCreationOptions.PreferFairness)
                        .LogIfException();
                }
            }

            return Json(new {Message = string.Format("信息审核成功！")});
        }

        /// <summary>
        /// 驳回提交审核的信息
        /// </summary>
        /// <param name="newsList"></param>
        /// <returns></returns>
        [HttpPost]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "信息驳回")]
        public ActionResult Reject(IEnumerable<NewsViewModel> newsList)
        {
            News anew = null;

            User tmpUser = null;

            if (newsList == null)
            {
                return Json(new {Message = string.Format("没有选中任何信息！")});
            }

            foreach (NewsViewModel item in newsList)
            {
                anew = newsService.GetNews(item.NewsId);
                if (anew != null)
                {
                    if (item.Status_Value != (int) NewsStatus.Auditting)
                    {
                        return Json(new {Message = "只有待审核状态的信息能够被驳回!"});
                    }

                    anew.Status = NewsStatus.Reject;
                    anew.ModifyDate = DateTime.Now;

                    newsService.SaveNews();

                    #region 给信息添加人发送短信

                    tmpUser = securityService.GetUser(anew.AddUser.UserName);
                    if (tmpUser != null)
                    {
                        //EispWebService.EmppSender(tmpUser.MobilePhone, "您有一条被驳回的浦软信息。");
                        var mobileMessage = new MobileMessage();
                        mobileMessage.Message = "您有一条被驳回的信息。";
                        mobileMessage.SendUserName = CurrentUser.UserName;
                        mobileMessage.ReceiverUserName = tmpUser.UserName;
                        mobileMessage.Type = MobileMessageType.PuRuanNews;
                        mobileMessage.SendDateTime = DateTime.Now;
                        mobileMessage.MobilePhone = tmpUser.MobilePhone;
                        EmppSender(mobileMessage, true);
                    }

                    #endregion
                }
            }

            return Json(new {Message = string.Format("信息已被驳回！")});
        }

        /// <summary>
        /// 添加收藏
        /// </summary>
        /// <param name="newsID"></param>
        /// <returns></returns>
        [HttpPost]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "添加收藏")]
        public ActionResult AddFavorite(int newsID, int? favoriteTypeId = (int) FavoriteType.News)
        {
            string errMsg = "";
            newsService.AddFavorite(newsID, CurrentUser.UserName, ref errMsg, favoriteTypeId);

            return Json(new {Message = errMsg}, JsonRequestBehavior.AllowGet);
        }


        public JsonResult SetTop(int newsId, bool setTop)
        {
            var newsModel = newsService.GetNews(newsId);
            if (newsModel != null)
            {
                newsModel.IsTop = setTop;
            }
            newsService.UpdateNews(newsModel);
            return Json(new {IsSuccess = true});
        }

        /// <summary>
        /// 获取综合信息类型(发表信息时类型选择框的数据)
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpPost]
        public string GetNewsTypeList(int id)
        {
            var typeList = typeService.GetConstTypes().Where(p => p.MainTypeValue == id || p.MainTypeValue == 60);
                // && p.TypeId != (int)ShowNewsType.Others

            if (!(CurrentUser.Roles.Any(r => r.Describe == "重点工作发起人")))
            {
                typeList = typeList.Where(s => s.TypeName != "重点工作");
            }

            StringBuilder jsonStr = new StringBuilder("[");

            foreach (var item in typeList)
            {
                jsonStr.Append("{");
                jsonStr.Append("\"id\":");
                jsonStr.Append("\"" + item.TypeId + "\"");
                jsonStr.Append(",\"text\":");
                jsonStr.Append("\"" + item.TypeName + "\"");
                jsonStr.Append("},");
            }

            return jsonStr.ToString().Trim(',') + "]";
        }


        /// <summary>
        /// 发布信息
        /// </summary>
        /// <returns></returns>
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "信息添加")]
        public ActionResult Create()
        {
            return View();
        }

        /// <summary>
        /// 信息删除
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpPost]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "信息删除")]
        public JsonResult Delete(int id)
        {
            bool result = true;
            string errMsg = "信息已经删除!";
            var newss = newsService.GetNews(id);
            result = newss != null && newsService.DeleteNews(id, CurrentUser.UserName, ref errMsg);

            return Json(new {Success = result, Message = errMsg});
        }


        #endregion

        #region 企业简况

        /// <summary>
        /// 【企业简况】页面
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业简况", Name = "查看企业简况")]
        public ActionResult ShowIntro(int? companyID)
        {
            if (companyID.HasValue == false)
                companyID = CurrentUser.Department.CompanyID;
            ViewBag.Title = "企业简况";
            var company = companyService.GetCompany(companyID.Value);
            CompanyViewModel cvm = new CompanyViewModel();
            if (company != null)
            {
                cvm = Dto.CompanyEx.Dto(company);
            }
            var report =
                personnelStructureQuarterlyReportService.GetPersonnelStructureQuarterlyRecords()
                    .Where(o => o.CompanyId == companyID)
                    .OrderByDescending(o => o.Report.TheYear*12 + o.Report.TheMonth)
                    .FirstOrDefault();
            var businessStateTypes = businessStateTypeService.GetBusinessStateTypes(companyID.Value);
            var businessStates = businessStateService.GetBusinessStates(companyID.Value);
            var businessStatesList = new List<BusinessState>();
            foreach (var businessStateType in businessStateTypes)
            {
                var businessState =
                    businessStates.LastOrDefault(
                        o => o.StateType == StateType.Pass && o.BusinessStateType == businessStateType);
                if (businessState != null)
                    businessStatesList.Add(businessState);
            }
            if (businessStatesList.Count == 0)
            {
                var businessState = businessStates.LastOrDefault(o => o.StateType == StateType.Pass);
                if (businessState != null)
                    businessStatesList.Add(businessState);
            }
            ViewBag.BusinessStates = businessStatesList;
            ViewBag.CurrentUserCompanyID = this.CurrentUser.Department.CompanyID;

            ViewData["RecordEntity"] = report.Dto();
            return View(cvm);
        }

        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业简况", Name = "企业简介")]
        public ActionResult ShowCompanyIntroduction()
        {
            return View();
        }

        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业简况", Name = "企业组织架构图")]
        public ActionResult ShowCompanyArchitecture()
        {
            return View();
        }

        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业简况", Name = "企业人员结构情况")]
        public ActionResult ShowCompanyStaffStructure()
        {
            return View();
        }

        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业简况", Name = "企业经营动态")]
        public ActionResult ShowCompanyBusinessState()
        {
            return View();
        }

        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业简况", Name = "企业简况子菜单")]
        public ActionResult ShowCompanies()
        {
            var companies = companyService.GetCompanies();
            var contentResult = new ContentResult();
            contentResult.ContentType = "text/html";
            StringBuilder sb = new StringBuilder(1000);
            foreach (var item in companies)
            {
                sb.AppendFormat(@"
            <li>
            <a href=""{0}"">▶ {1}</a></li>", Url.Action("ShowIntro", new {companyID = item.CompanyID}),
                    item.CompanyShortName);
            }
            contentResult.Content = sb.ToString();
            return contentResult;
        }

        [HttpGet]
        public ActionResult GetBusinessState(int companyID)
        {
            var businessStates = businessStateService.GetBusinessStates(companyID);
            var legalBusinessStates = from businessState in businessStates
                select new
                {
                    businessState.BusinessStateId,
                    businessState.Title,
                    Creator = businessState.Creator.DisplayName,
                    businessState.Content,
                    State = businessState.StateType.GetEnumDisplay(),
                    StateInt = businessState.StateType,
                    Type = businessState.BusinessStateType == null ? "" : businessState.BusinessStateType.Name,
                    AuditDate = businessState.AuditDate,
                };
            return Json(new {total = legalBusinessStates.Count(), rows = legalBusinessStates},
                JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业简况", Name = "编辑经营动态")]
        public ActionResult EditBusinessState(int businessStateID)
        {
            BusinessState returnBusinessState = null;
            if (businessStateID == -1)
            {
                returnBusinessState = new BusinessState()
                {
                    BusinessStateId = -1,
                    CompanyID = this.CurrentUser.Department.Company.CompanyID,
                    Company = this.CurrentUser.Department.Company,
                    CreatorUserName = this.CurrentUser.UserName,
                    Creator = this.CurrentUser,
                    StateType = StateType.ToSubmit,
                };
            }
            else
                returnBusinessState = businessStateService.GetBusinessState(businessStateID);
            return Json(new
            {
                CompanyID = returnBusinessState.CompanyID,
                Company = returnBusinessState.Company.Name,
                CreatorUserName = returnBusinessState.CreatorUserName,
                Creator = returnBusinessState.Creator.DisplayName,
                StateType = returnBusinessState.StateType.GetEnumDisplay(),
                BusinessStateId = returnBusinessState.BusinessStateId,
                BusinessStateTypeID =
                    returnBusinessState.BusinessStateTypeID == 0
                        ? ""
                        : returnBusinessState.BusinessStateTypeID.ToString(),
                Content = returnBusinessState.Content,
                Title = returnBusinessState.Title,
                State = returnBusinessState.State,
            }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        [ValidateInput(false)]
        public ActionResult EditBusinessState(BusinessState businessState)
        {
            if (businessState.BusinessStateId == -1)
                businessStateService.CreateBusinessState(businessState);
            else
                businessStateService.UpdateBusinessState(businessState);

            var result = new ContentResult();
            result.Content = string.Format("<body><pre>{0}</pre></body>", "保存成功");
            result.ContentType = "text/html";
            return result;
            //return Json(new { success = true, message = "保存成功" });
        }

        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业简况", Name = "删除经营动态")]
        public ActionResult DeleteBusinessState(int businessStateID)
        {
            var businessState = businessStateService.GetBusinessState(businessStateID);
            if (businessState.StateType == StateType.ToSubmit || businessState.StateType == StateType.Reject)
            {
                businessStateService.DeleteBusinessState(businessStateID);
                return Json(new {success = true, message = "删除成功"}, JsonRequestBehavior.AllowGet);
            }
            return Json(new {success = false, message = "不能删除已提交的记录"}, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        public ActionResult CommitBusinessState(int businessStateID)
        {
            var businessState = businessStateService.GetBusinessState(businessStateID);
            businessState.StateType = StateType.ToAudit;
            businessState.StateModifyDate = DateTime.Now;
            businessStateService.UpdateBusinessState(businessState);

            var users = securityService.GetUsersByCompany(this.CurrentUser.Department.CompanyID);
            foreach (var user in users)
            {
                if (HasPermission(user, "AuditBusinessState", "News", ActionMethodConst.GET))
                {
                    //EispWebService.EmppSender(user.MobilePhone, "您有一条待审核的经营动态信息");
                    var mobileMessage = new MobileMessage();
                    mobileMessage.Message = "您有一条待审核的经营动态信息。";
                    mobileMessage.SendUserName = CurrentUser.UserName;
                    mobileMessage.ReceiverUserName = user.UserName;
                    mobileMessage.Type = MobileMessageType.PuRuanNews;
                    mobileMessage.MobilePhone = user.MobilePhone;
                    EmppSender(mobileMessage, true);
                }
            }

            return Json(new {success = true, message = "提交成功"}, JsonRequestBehavior.AllowGet);
        }

        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业简况", Name = "审核经营动态")]
        public ActionResult AuditBusinessState(int businessStateID)
        {
            var businessState = businessStateService.GetBusinessState(businessStateID);
            businessState.AuditDate = DateTime.Now;
            businessState.AuditorUserName = this.CurrentUser.UserName;
            businessState.Auditor = this.CurrentUser;
            return PartialView(businessState);
        }

        [HttpPost]
        [ValidateInput(false)]
        public ActionResult AuditBusinessState(BusinessState businessState, string actionId)
        {
            if (actionId == "pass")
            {
                businessState.StateType = StateType.Pass;
                businessState.StateModifyDate = DateTime.Now;
            }
            else
            {
                businessState.StateType = StateType.Reject;
                businessState.StateModifyDate = DateTime.Now;
                //   var users = securityService.GetUsersByCompany(this.CurrentUser.Department.CompanyID);
                var creator = securityService.GetUser(businessState.CreatorUserName);
                if (creator != null)
                {
                    if (HasPermission(creator, "EditBusinessState", "News", ActionMethodConst.GET))
                    {
                        //EispWebService.EmppSender(user.MobilePhone, "您有一条被驳回的经营动态信息");
                        var mobileMessage = new MobileMessage();
                        mobileMessage.Message = "您有一条被驳回的经营动态信息。";
                        mobileMessage.SendUserName = CurrentUser.UserName;
                        mobileMessage.ReceiverUserName = creator.UserName;
                        mobileMessage.Type = MobileMessageType.PuRuanNews;
                        mobileMessage.MobilePhone = creator.MobilePhone;
                        EmppSender(mobileMessage, true);
                    }
                }
            }
            businessStateService.UpdateBusinessState(businessState);

            var result = new ContentResult();
            result.Content = string.Format("<body><pre>{0}</pre></body>", "保存成功");
            result.ContentType = "text/html";
            return result;
            //return Json(new { success = true, message = "保存成功" });
        }

        [HttpPost]
        [ValidateInput(false)]
        public ActionResult SaveBusinessStateTypes(IEnumerable<BusinessStateType> updated,
            IEnumerable<BusinessStateType> inserted, IEnumerable<BusinessStateType> deleted)
        {
            businessStateTypeService.UpdateBusinessStateTypes(updated, this.CurrentUser.Department.CompanyID);
            businessStateTypeService.CreateBusinessStateTypes(inserted, this.CurrentUser.Department.CompanyID);
            businessStateTypeService.DeleteBusinessStateTypes(deleted);
            return Json(new {success = true, message = "保存成功"});
        }

        [HttpGet]
        public ActionResult GetBusinessStateTypes(int companyID)
        {
            var values = from bst in businessStateTypeService.GetBusinessStateTypes(companyID)
                select new
                {
                    bst.BusinessStateTypeID,
                    bst.Name
                };
            return Json(values, JsonRequestBehavior.AllowGet);
        }

        ///// <summary>
        ///// 通告信息
        ///// </summary>
        ///// <returns></returns>
        //public ActionResult NoticeNews()
        //{
        //    ViewBag.MarkNewDays = ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue;    //标识为新增信息的日期量;
        //    var news = newsService.GetTopNNoticeNews(true, 6, CurrentUser.UserName); //前N条新闻
        //    return View(news);
        //}

        //// <summary>
        //// 除了通告信息以外的信息
        //// </summary>
        //// <returns></returns>
        ////public ActionResult OrtherNewsExceptNotice()
        ////{
        ////    ViewBag.MarkNewDays = ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue;    //标识为新增信息的日期量;
        ////    var news = newsService.GetTopNNoticeNews(false, 6, CurrentUser.UserName); //前N条新闻
        ////    return View(news);
        ////}

        #endregion

        #region 企业文化

        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业文化", Name = "企业文化信息（临时）")]
        public ActionResult ShowTempCulture()
        {
            return View();
        }

        /// <summary>
        /// 综合信息“更多”列表
        /// </summary>
        /// <param name="companyID">公司编号</param>
        /// <param name="typeID">信息分类编号</param>
        /// <param name="publishType">发表类型（由我发表/未提交审核/审核中）</param>
        /// <param name="customType">自定义信息子类型</param>
        /// <returns></returns>
        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业文化", Name = "显示信息列表")]
        public ActionResult ShowMoreCultures(int? companyID, int? typeID, int? publishType, string customType = "")
            //,int? pageIndex, int? departmentID, string searchText = "", 
        {
            var inputParams = new NewsSearchParams();

            if (companyID != null)
            {
                inputParams.CompanyId = (int) companyID;
            }

            if (typeID > 0)
            {
                inputParams.TypeId = (int) typeID;
            }

            if (publishType > 0)
            {
                inputParams.PublishType = (int) publishType;
            }

            if (!string.IsNullOrWhiteSpace(customType))
            {
                inputParams.CustomType = customType;
            }

            inputParams.PageIndex = 1;
            return ShowMoreCultures(inputParams);
        }


        /// <summary>
        /// 异步查询综合信息
        /// </summary>
        /// <param name="inputParams"></param>
        /// <returns></returns>
        [HttpPost]
        public ActionResult ShowMoreCultures(NewsSearchParams inputParams)
        {
            if (inputParams == null)
            {
                //通讯错误,返回错误提示
                throw new Exception("异步查询发生错误！");
            }

            inputParams.PageSize = 10;
            inputParams.CurrentUserName = CurrentUser.UserName;
            inputParams.CurrentUserCompanyId = CurrentUser.Department.CompanyID;
            inputParams.FavoriteType = (int) FavoriteType.CompanyCulture;
            //获取信息
            var newsList = newsService.GetNewsByPage(inputParams);

            if (newsList == null || newsList.Results == null)
            {
                //查询出错,返回错误提示
            }

            //获取每条信息的附件数量
            foreach (var aNews in newsList.Results)
            {
                aNews.AttachmentCount =
                    attachmentService.GetAttachments((int) AttachmentType.NewsAttachment, aNews.NewsId).Count();
            }

            #region 将当前的查询条件值编号传入页面

            StringBuilder tmpJsonStr = new StringBuilder();
            JavaScriptSerializer jsonSerializer = new JavaScriptSerializer();
            jsonSerializer.Serialize(inputParams, tmpJsonStr);
            ViewBag.SearchParams = tmpJsonStr.ToString();
            ViewBag.SearchParamsObj = inputParams;
            ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue; //标识为新增信息的日期量
            ////分类集合
            var typeList = typeService.GetConstTypes().Where(r => r.MainType == TypeEnum.CompanyCulture).ToList();
            var currentConsttype = typeList.FirstOrDefault(t => t.TypeId == inputParams.TypeId);
            ViewBag.TypeCollection = typeList;
            ViewBag.CurrentType = currentConsttype;

            #endregion

            return View("ShowMoreCultures", newsList);
        }

        /// <summary>
        /// 发布信息
        /// </summary>
        /// <returns></returns>
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业文化", Name = "信息添加")]
        public ActionResult CreateCulture()
        {
            return View();
        }

        /// <summary>
        /// 发布信息
        /// </summary>
        /// <param name="news"></param>
        /// <param name="isSpecNews">是否为 快捷发布等特殊发布方式</param>
        /// <returns></returns>
        [HttpPost]
        [ValidateInput(true)]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业文化", Name = "信息保存")]
        public ActionResult SaveCulture(NewsViewModel news)
        {
            var errMsg = "";
            var result = false;

            do
            {
                if (!HasPermission("SaveNews", "News", ActionMethodConst.POST))
                {
                    errMsg = "你没有此操作权限！";
                    break;
                }

                var type = typeService.GetConstType(news.TypeID);
                if (type.TypeName.Contains("PDF"))
                {
                    news.PublishType = PublishType.Pdf;
                }
                else
                {
                    news.PublishType = PublishType.Movie;
                }

                var filesDicList = AttachmentHelper.GetUploadFileDictionay(this);
                string[] guidArray = new string[0];
                if (!string.IsNullOrWhiteSpace(news.CurrentFilesGuidSting))
                {
                    guidArray = news.CurrentFilesGuidSting.Split(',');
                }
                if (filesDicList != null)
                {
                    var selecedFiles =
                        filesDicList.Where(f => guidArray.Contains(f.FileGuid)).Select(fd => fd.File).ToList();
                    if (selecedFiles.Count < 1)
                    {
                        return Json(new {Success = false, Message = "未上传任何文件！"});
                    }
                    else if (selecedFiles.Count > 1)
                    {
                        return Json(new {Success = false, Message = "此类型附件只能上传一份！"});
                    }
                    else if (news.PublishTypeId == (int) PublishType.Pdf &&
                             !selecedFiles.Any(s => s.FileName.Contains(".pdf")))
                    {
                        return Json(new {Success = false, Message = "未检测到.pdf结尾文件！"});
                    }
                    else if (news.PublishTypeId == (int) PublishType.Movie &&
                             !selecedFiles.Any(s => s.FileName.Contains(".mp4")))
                    {
                        return Json(new {Success = false, Message = "未检测到.mp4结尾文件！"});
                    }

                }
                else
                {
                    return Json(new {Success = false, Message = "上传异常，请关闭当前窗口并稍后重试！"});
                }

                //拷贝前端回传的信息
                var newModel = new News();
                ModelCopier.CopyModel(news, newModel);
                newModel.ModifyDate = DateTime.Now;


                result = newsService.PublishNews(newModel, CurrentUser, ref errMsg, true);

                if (!result)
                {
                    break;
                }


                bool fileResult = true;

                if (filesDicList != null)
                {
                    if (!string.IsNullOrWhiteSpace(news.CurrentFilesGuidSting))
                    {
                        guidArray = news.CurrentFilesGuidSting.Split(',');
                    }
                    var selecedFiles =
                        filesDicList.Where(f => guidArray.Contains(f.FileGuid)).Select(fd => fd.File).ToList();

                    //保存附件
                    foreach (var item in selecedFiles)
                    {
                        fileResult = AttachmentHelper.SaveAttachment(attachmentService, item,
                            AttachmentType.NewsAttachment, newModel.NewsId, CurrentUser.UserName, ref errMsg);
                        if (!fileResult) //发生错误时跳出
                        {
                            break;
                        }
                    }
                }


                if (!fileResult)
                {
                    errMsg = "信息保存成功,但保存附件时发生错误,原因:" + errMsg;
                    break;
                }

            } while (false);

            return Json(new {Success = result, Message = errMsg});
        }

        /// <summary>
        /// 浏览具体信息
        /// </summary>
        /// <param name="newsID"></param>
        /// <returns></returns>
        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "浦软信息", Name = "信息查看")]
        public ActionResult ShowCompanyCulture(int newsID)
        {
            News aNews = newsService.GetNews(newsID);
            if (aNews.Status == NewsStatus.Audited)
            {
                aNews.VisitNum = aNews.VisitNum + 1;
                newsService.UpdateNews(aNews);
            }

            aNews = newsService.GetNews(newsID);
            var attachments = attachmentService.GetAttachments((int) AttachmentType.NewsAttachment, newsID).ToList();
            //ViewBag.Title = aNews.Title;
            ViewBag.TypeName = aNews.Type.TypeName;
            ViewBag.FilePath = string.Empty;
            for (int i = 0; i < attachments.Count; i++)
            {
                ViewBag.FilePath = attachments[i].Path.Replace('\\', '/');
            }
            return View(aNews);
        }


        #region ANN：该块中的方法暂无页面实现，请勿调用

        /// <summary>
        /// 显示企业文化
        /// </summary>
        /// <param name="_companyid"></param>
        /// <returns>View()</returns>
        [HttpGet]
        public ActionResult ShowCulture(int? _companyid)
        {
            ViewBag.showbtn = false;
            if (_companyid.HasValue == false)
            {
                _companyid = CurrentUser.Department.CompanyID;
            }
            else
            {
                if (_companyid == CurrentUser.Department.CompanyID)
                {
                    ViewBag.showbtn = true;
                }


            }
            Culture culturetemp = null;

            string strChildFilePath = "";
            ViewBag.Title = "企业文化";
            var company = companyService.GetCompany(_companyid.Value);
            CompanyViewModel cvm = new CompanyViewModel();
            if (company != null)
            {
                cvm = Dto.CompanyEx.Dto(company);
            }
            culturetemp = this.cultureService.GetCulture(company.CompanyID);
            ViewBag.CompanyID = _companyid;
            if (culturetemp != null)
            {
                if (culturetemp.CultContent.Length > 0)
                {
                    ViewBag.CompanyCulture = culturetemp.CultContent;
                }
                else
                {
                    ViewBag.CompanyCulture = "未添加企业文化信息";
                }
            }
            else
            {
                ViewBag.CompanyCulture = "未添加企业文化信息";
            }

            return View(cvm);
        }

        public ActionResult ShowCompaniesCulture()
        {
            var companies = companyService.GetCompanies();
            var contentResult = new ContentResult();
            contentResult.ContentType = "text/html";
            StringBuilder sb = new StringBuilder(1000);
            foreach (var item in companies)
            {
                sb.AppendFormat(@"
            <li>
            <a href=""{0}"">▶ {1}</a></li>", Url.Action("ShowCulture", new {_companyid = item.CompanyID}),
                    item.CompanyShortName);
            }
            contentResult.Content = sb.ToString();
            return contentResult;
        }

        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业文化", Name = "修改企业文化信息")]
        [HttpPost]
        public ActionResult CreateOrUPdateCulture(Culture culture)
        {
            bool success = false;
            string msg = "";
            Culture culturetemp = null;
            if (culture != null)
            {
                try
                {
                    culturetemp = this.cultureService.GetCulture(culture.CompanyID);
                    if (culturetemp == null)
                    {
                        cultureService.CreateCulture(culture);
                    }
                    else
                    {
                        culturetemp.CultContent = culture.CultContent;
                        culturetemp.CompanyID = culture.CompanyID;
                        cultureService.UpdateCulture(culturetemp);
                    }

                    success = true;
                    msg = "更新成功！";
                }
                catch (Exception)
                {

                    msg = "更新失败！";
                }
            }

            var result = new ContentResult();
            result.Content = string.Format("{{\"success\": \"{1}\",\"msg\":\"{0}\"}}", msg, success);
            result.ContentType = "text/html";
            return result;

        }

        public ActionResult Deleteculture(string companyid)
        {
            string msg = "";
            bool success = false;
            Culture culturetemp = null;
            if (companyid != null)
            {
                try
                {
                    culturetemp = this.cultureService.GetCulture(int.Parse(companyid));
                    if (culturetemp != null)
                    {
                        culturetemp.CultContent = "";
                        cultureService.UpdateCulture(culturetemp);
                        msg = "删除成功！";
                        success = true;
                    }
                    else
                    {
                        msg = "删除成功！";
                        success = true;
                    }

                }
                catch (Exception)
                {
                    msg = "删除失败！";
                }
            }

            var result = new ContentResult();
            result.Content = string.Format("{{\"success\": \"{1}\",\"msg\":\"{0}\"}}", msg, success);
            result.ContentType = "text/html";
            return result;
        }

        #endregion

        #endregion

        #region 汇智交流

        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "汇智交流", Name = "汇智交流")]
        public ActionResult WiseCommunication(int pageIndex = 1)
        {
            var paras = new NewsSearchParams();
            paras.PageIndex = pageIndex;
            paras.PageSize = 10;
            paras.TypeId = Communication_TypeID;
            paras.CurrentUserName = CurrentUser.UserName;
            var result = newsService.GetNewsByPage(paras);
            ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue; //标识为新增信息的日期量

            return View(result);
        }

        /// <summary>
        /// 发布交流信息
        /// </summary>
        /// <returns></returns>
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "汇智交流", Name = "信息添加")]
        public ActionResult CreateCommunication()
        {
            return View();
        }

        /// <summary>
        /// 发布交流信息
        /// </summary>
        /// <param name="news"></param>
        /// <returns></returns>
        [HttpPost]
        [ValidateInput(true)]
        public ActionResult SaveCommunication(NewsViewModel news)
        {
            var errMsg = "";
            var result = false;

            do
            {

                //拷贝前端回传的信息
                var newModel = new News();
                ModelCopier.CopyModel(news, newModel);
                newModel.ModifyDate = DateTime.Now;
                newModel.TypeID = Communication_TypeID;
                result = newsService.PublishNews(newModel, CurrentUser, ref errMsg, true);

                if (!result)
                {
                    break;
                }


                bool fileResult = true;
                var filesDicList = AttachmentHelper.GetUploadFileDictionay(this);

                if (filesDicList != null)
                {
                    string[] guidArray = new string[0];
                    if (!string.IsNullOrWhiteSpace(news.CurrentFilesGuidSting))
                    {
                        guidArray = news.CurrentFilesGuidSting.Split(',');
                    }

                    var selecedFiles = filesDicList.Where(f => guidArray.Contains(f.FileGuid)).Select(fd => fd.File);
                    //保存附件
                    foreach (var item in selecedFiles)
                    {
                        fileResult = AttachmentHelper.SaveAttachment(attachmentService, item,
                            AttachmentType.NewsAttachment, newModel.NewsId, CurrentUser.UserName, ref errMsg);
                        if (!fileResult) //发生错误时跳出
                        {
                            break;
                        }
                    }
                }

                if (!fileResult)
                {
                    errMsg = "信息保存成功,但保存附件时发生错误,原因:" + errMsg;
                    break;
                }

            } while (false);

            return Json(new {Success = result, Message = errMsg});
        }


        #endregion

        #region 党建工会

        /// <summary>
        /// 综合信息之党建工会
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "党建工会", Name = "显示分类预览")]
        public ActionResult ConstructionOfParty()
        {

            var news = newsService.GetIndexPageNewsOnConstrParty(CurrentUser.UserName, 5)
                .OrderBy(nty => nty.CompanyType);

            var companyList = news.Select(n => n.CompanyType).Distinct();
            var typeList = news.Select(n => n.TypeModel).Distinct();

            ViewBag.CompanyList = companyList;
            ViewBag.TypeList = typeList;

            ViewBag.News = news.ToArray(); //所有信息条目
            ViewBag.Title = "党建工会";
            ViewBag.MarkNewDays = ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue;
            //标识为新增信息的日期量;    //标识为新增信息的日期量
            ViewBag.RowCount = 5; //定义每种类型显示的信息条数
            return View();

        }

        #endregion

        #region  制度一览
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "制度建设", Name = "制度一览")]
        public ActionResult ShowRegime()
        {
            return View();
        }
        #endregion

        #region  管理工作提升
        /// <summary>
        /// 综合信息之内控
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "管理提升", Name = "管理提升分类")]
        public ActionResult NeiKong()
        {

            var news = newsService.GetIndexPageNewsNeiKong(CurrentUser.UserName, 5)
                .OrderBy(nty => nty.CompanyType);

            var companyList = news.Select(n => n.CompanyType).Distinct();
            var typeList = news.Select(n => n.TypeModel).Distinct();

            ViewBag.CompanyList = companyList;
            ViewBag.TypeList = typeList;

            ViewBag.News = news.ToArray(); //所有信息条目
            ViewBag.Title = "管理制度";
            ViewBag.MarkNewDays = ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue;
            //标识为新增信息的日期量;    //标识为新增信息的日期量
            ViewBag.RowCount = 5; //定义每种类型显示的信息条数
            return View();

        }
        #endregion

        /// <summary>
        /// 综合信息之企业文化
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [PermissionDisplay(ModuleName = "综合信息", MenuName = "企业文化", Name = "企业文化分类")]
        public ActionResult CultureSuzhouPark()
        {

            var news = newsService.GetIndexPageNewsCultureSuzhou(CurrentUser.UserName, 5)
                .OrderBy(nty => nty.CompanyType);

            var companyList = news.Select(n => n.CompanyType).Distinct();
            var typeList = news.Select(n => n.TypeModel).Distinct();

            ViewBag.CompanyList = companyList;
            ViewBag.TypeList = typeList;

            ViewBag.News = news.ToArray(); //所有信息条目
            ViewBag.Title = "企业文化";
            ViewBag.MarkNewDays = ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue;
            //标识为新增信息的日期量;    //标识为新增信息的日期量
            ViewBag.RowCount = 5; //定义每种类型显示的信息条数
            return View();

        }

        #region  安全生产工作
         [PermissionDisplay(ModuleName = "综合信息", MenuName = "品牌建设", Name = "品牌建设")]
        public ActionResult TobeSafeSoon()
        {
            var news = newsService.GetIndexPageNewsBrandBuilding(CurrentUser.UserName, 5)
                 .OrderBy(nty => nty.CompanyType);

            var companyList = news.Select(n => n.CompanyType).Distinct();
            var typeList = news.Select(n => n.TypeModel).Distinct();

            ViewBag.CompanyList = companyList;
            ViewBag.TypeList = typeList;

            ViewBag.News = news.ToArray(); //所有信息条目
            ViewBag.Title = "品牌建设";
            ViewBag.MarkNewDays = ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue;
            //标识为新增信息的日期量;    //标识为新增信息的日期量
            ViewBag.RowCount = 5; //定义每种类型显示的信息条数
            return View();
        }
        #endregion


         //#region 重点工程

         ///// <summary>
         ///// 综合信息之浦软党建
         ///// </summary>
         ///// <returns></returns>
         //[HttpGet]
         //[PermissionDisplay(ModuleName = "综合信息", MenuName = "重点工程", Name = "重点工程分类")]
         //public ActionResult ImportentEngineer()
         //{

         //    var news = newsService.GetIndexPageNewsImportentEngineer(CurrentUser.UserName, 5)
         //        .OrderBy(nty => nty.CompanyType);

         //    var companyList = news.Select(n => n.CompanyType).Distinct();
         //    var typeList = news.Select(n => n.TypeModel).Distinct();

         //    ViewBag.CompanyList = companyList;
         //    ViewBag.TypeList = typeList;

         //    ViewBag.News = news.ToArray(); //所有信息条目
         //    ViewBag.Title = "重点工程";
         //    ViewBag.MarkNewDays = ViewBag.MarkNewDays = Application.Instance.MarkNewDayValue;
         //    //标识为新增信息的日期量;    //标识为新增信息的日期量
         //    ViewBag.RowCount = 5; //定义每种类型显示的信息条数
         //    return View();

         //}

         //#endregion


         [PermissionDisplay(ModuleName = "综合信息", MenuName = "品牌文化", Name = "品牌素材")]
         public ActionResult AppDownLoad()
         {
             return View();
         }


         public string GetApps()
         {
             var listTree = new List<EasyUITreeViewModel>();
             var dirPath = AppDomain.CurrentDomain.BaseDirectory + "UploadFiles\\Brand\\应用下载";
             var dir = new DirectoryInfo(dirPath);
             var dirs = dir.GetDirectories();
             foreach (var d in dirs)
             {
                 var treeNode = new EasyUITreeViewModel
                 {
                     Text = d.Name
                 };
                 listTree.Add(treeNode);
                 treeNode.Children = CreateChildrenNode(d.FullName);
             }
             return JsonConvert.SerializeObject(listTree); ;
         }

         public List<EasyUITreeViewModel> CreateChildrenNode(string path)
         {
             var tree = new List<EasyUITreeViewModel>();
             var dir = new DirectoryInfo(path);
             var dirs = dir.GetDirectories();
             var files = dir.GetFiles();
             foreach (var d in dirs)
             {
                 var treeNode = new EasyUITreeViewModel
                 {
                     Text = d.Name
                 };
                 tree.Add(treeNode);
                 treeNode.Children = CreateChildrenNode(d.FullName);
             }
             foreach (var f in files)
             {
                 var treeNode = new EasyUITreeViewModel
                 {
                     Id = path.Substring(path.IndexOf("UploadFiles")) + "\\" + f.Name,
                     Text = f.Name
                 };
                 tree.Add(treeNode);
             }
             return tree;
         }

         public FileStreamResult DownLoad(string filePath)
         {
             string absoluFilePath = AppDomain.CurrentDomain.BaseDirectory + filePath;
             string fileName = filePath.Substring(filePath.LastIndexOf("\\") + 2);
             Response.Charset = "utf-8";
             return File(new FileStream(absoluFilePath, FileMode.Open), "application/octet-stream", fileName);
         }




         [HttpGet]
         [PermissionDisplay(ModuleName = "综合信息", MenuName = "制度建设", Name = "制度一览")]
         public ActionResult DownLoads()
         {
             return View();
         }


         public string GetAppss()
         {
             var listTree = new List<EasyUITreeViewModel>();
             var dirPath = AppDomain.CurrentDomain.BaseDirectory + "UploadFiles\\Regime\\制度一览";
             var dir = new DirectoryInfo(dirPath);
             var dirs = dir.GetDirectories();
             foreach (var e in dirs)
             {
                 var treeNode = new EasyUITreeViewModel
                 {
                     Text = e.Name
                 };
                 listTree.Add(treeNode);
                 treeNode.Children = CreateChildrenNodes(e.FullName);
             }
             return JsonConvert.SerializeObject(listTree); ;
         }

         public List<EasyUITreeViewModel> CreateChildrenNodes(string path)
         {
             var tree = new List<EasyUITreeViewModel>();
             var dir = new DirectoryInfo(path);
             var dirs = dir.GetDirectories();
             var files = dir.GetFiles();
             foreach (var d in dirs)
             {
                 var treeNode = new EasyUITreeViewModel
                 {
                     Text = d.Name
                 };
                 tree.Add(treeNode);
                 treeNode.Children = CreateChildrenNodes(d.FullName);
             }
             foreach (var f in files)
             {
                 var treeNode = new EasyUITreeViewModel
                 {
                     Id = path.Substring(path.IndexOf("UploadFiles")) + "\\" + f.Name,
                     Text = f.Name
                 };
                 tree.Add(treeNode);
             }
             return tree;
         }

         public FileStreamResult DownLoadss(string filePath)
         {
             string absoluFilePath = AppDomain.CurrentDomain.BaseDirectory + filePath;
             string fileName = filePath.Substring(filePath.LastIndexOf("\\") + 2);
             Response.Charset = "utf-8";
             return File(new FileStream(absoluFilePath, FileMode.Open), "application/octet-stream", fileName);
         }


         [HttpGet]
         [PermissionDisplay(ModuleName = "综合信息", MenuName = "制度建设", Name = "常用表单")]
         public ActionResult GetForms()
         {
             return View();
         }


         public string GetFormApp()
         {
             var listTree = new List<EasyUITreeViewModel>();
             var dirPath = AppDomain.CurrentDomain.BaseDirectory + "UploadFiles\\CommonForms\\常用表单";
             var dir = new DirectoryInfo(dirPath);
             var dirs = dir.GetDirectories();
             foreach (var e in dirs)
             {
                 var treeNode = new EasyUITreeViewModel
                 {
                     Text = e.Name
                 };
                 listTree.Add(treeNode);
                 treeNode.Children = CreateChildrenNodes(e.FullName);
             }
             return JsonConvert.SerializeObject(listTree); ;
         }

         public List<EasyUITreeViewModel> CreateFormChildrenNodes(string path)
         {
             var tree = new List<EasyUITreeViewModel>();
             var dir = new DirectoryInfo(path);
             var dirs = dir.GetDirectories();
             var files = dir.GetFiles();
             foreach (var d in dirs)
             {
                 var treeNode = new EasyUITreeViewModel
                 {
                     Text = d.Name
                 };
                 tree.Add(treeNode);
                 treeNode.Children = CreateFormChildrenNodes(d.FullName);
             }
             foreach (var f in files)
             {
                 var treeNode = new EasyUITreeViewModel
                 {
                     Id = path.Substring(path.IndexOf("UploadFiles")) + "\\" + f.Name,
                     Text = f.Name
                 };
                 tree.Add(treeNode);
             }
             return tree;
         }

         public FileStreamResult GetDownLoad(string filePath)
         {
             string absoluFilePath = AppDomain.CurrentDomain.BaseDirectory + filePath;
             string fileName = filePath.Substring(filePath.LastIndexOf("\\") + 2);
             Response.Charset = "utf-8";
             return File(new FileStream(absoluFilePath, FileMode.Open), "application/octet-stream", fileName);
         }

    }

    }
