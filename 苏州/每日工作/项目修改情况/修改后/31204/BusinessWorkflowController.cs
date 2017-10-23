using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.Mvc;
using LinqKit;
using Microsoft.Web.Mvc;
using Newtonsoft.Json;
using Wiseonline.Eisp.Data;
using Wiseonline.Eisp.Service;
using Wiseonline.Eisp.Domain;
using Wiseonline.Eisp.Utility.Helpers;
using Wiseonline.Eisp.Web.Helpers;
using Wiseonline.Eisp.Web.Controllers;
using Wiseonline.Eisp.Web.ViewModel;
using Wiseonline.Eisp.Web.Areas.WF.Models;
using Wiseonline.Eisp.Web.Dto;
using Wiseonline.Eisp.Helpers;
using System.Text.RegularExpressions;
using System.Linq.Expressions;
using System.Threading.Tasks;

namespace Wiseonline.Eisp.Web.Areas.WF.Controllers
{
    public class BusinessWorkflowController : BaseWorkFlowController
    {
        private const string _template = "【Type】(【SN】)结束，请及时查阅。【CurrentDateTime】"; //默认值以防null
        private const string _mailtemplate = "办件提醒：【Type】(【SN】)结束，请及时查阅。【CurrentDateTime】"; //默认值以防null
        private readonly IFormForWorkflowService forWorkflowSvc;
        private readonly IConcreteWorkflowService concreteWorkflowSvc;
        private readonly IWorkflowStatusService workflowStatusSvc;
        private readonly IWorkflowRuleService workflowRuleService;
        private readonly IConcreteWorkflowOperationService concreteWorkflowOperationSvc;
        private readonly IPassRoundForPerusalService passRoundForPerusalService;
        private readonly IDelegateSettingService delegateSettingService;
        private readonly IMessageTemplateService messageTemplateService;
        private readonly ISecurityService securityService;
        private readonly ICompanyService cmpnSvc = null;
        private readonly IDepartmentService dptmntSvc = null;
        private readonly IEispProblemProcessService eispProblemProcessService;
        private readonly ITaskOverseeService taskOverseeService;
        private readonly IITTechnicalServiceProcessService iTTechnicalServiceProcessService;

        private readonly ISuperService<WorkflowRuleDefault> workflowRuleDefaultService;
        private readonly ISqlViewObjectService<HasDoneSearchTenant> hasDoneSearchTenantService;

        private readonly IMobileCommonHelper mobileCommonHelper = null;
        private readonly IMailHelper mailHelper = null;
        private readonly ISuperService<AssetsManager> _assetsManagerService;

        private readonly IOverTimeWorkService overTimeWorkService;
        private readonly IBizCenterContractService bizCenterContractService;
        private readonly IPropertyContractService _PropertyContractService;

        private readonly ISuperService<DeliverInformation> _deliverInformationSvc;
        private readonly ISuperService<CompanyRegister> _companyRegisterSrvc;
        private readonly ISuperService<Question> _questionSrvc;
        public BusinessWorkflowController(IFormForWorkflowService formWorkflowSvc,
                                          IConcreteWorkflowService concreteWorkflowSvc,
                                          IWorkflowStatusService workflowStatusSvc,
                                          IWorkflowRuleService workflowRuleService, ISecurityService securityService,
                                          IConcreteWorkflowOperationService concreteWorkflowOperationSvc,
                                          IPassRoundForPerusalService passRoundForPerusalService,
                                          IDelegateSettingService delegateSettingService,
                                          IMessageTemplateService messageTemplateService, IDepartmentService dptmntSvc,
                                          ICompanyService cmpnSvc,
                                          IEispProblemProcessService eispProblemProcessService,
                                          ITaskOverseeService taskOverseeService,
                                          IITTechnicalServiceProcessService iTTechnicalServiceProcessService,
                                          IMobileMessageService mobileMessageService,
                                          ISuperService<WorkflowRuleDefault> serviceWorkflowRuleDefault,
                                          ISqlViewObjectService<HasDoneSearchTenant> hasDoneSearchTenantService,
                                          ISuperService<AssetsManager> assetsManagerService,
                                          ISuperService<DeliverInformation> _deliverInformationSvc,
                                          ISuperService<CompanyRegister> _companyRegisterSrvc,
                                          ISuperService<Question> _questionSrvc,
                                          IMobileCommonHelper mobileCommonHelper, IMailHelper mailHelper, ICheckBadStrExceptionService checkBadStrExceptionService, IOverTimeWorkService overTimeWorkService, IBizCenterContractService bizCenterContractService, IPropertyContractService propertyContractService)
            : base(formWorkflowSvc, mobileMessageService, checkBadStrExceptionService)
        {
            forWorkflowSvc = formWorkflowSvc;
            this.concreteWorkflowSvc = concreteWorkflowSvc;
            this.workflowStatusSvc = workflowStatusSvc;
            this.workflowRuleService = workflowRuleService;
            this.concreteWorkflowOperationSvc = concreteWorkflowOperationSvc;
            this.passRoundForPerusalService = passRoundForPerusalService;
            this.delegateSettingService = delegateSettingService;
            this.messageTemplateService = messageTemplateService;
            this.securityService = securityService;
            this.dptmntSvc = dptmntSvc;
            this.cmpnSvc = cmpnSvc;
            this.eispProblemProcessService = eispProblemProcessService;
            this.iTTechnicalServiceProcessService = iTTechnicalServiceProcessService;
            this.taskOverseeService = taskOverseeService;
            this.workflowRuleDefaultService = serviceWorkflowRuleDefault;
            this.hasDoneSearchTenantService = hasDoneSearchTenantService;
            this.hasDoneSearchTenantService.Sql = SqlConst.Sql_HasDoneTenant;
            this.mobileCommonHelper = mobileCommonHelper;
            this.mailHelper = mailHelper;
            this._assetsManagerService = assetsManagerService;
            this.overTimeWorkService = overTimeWorkService;
            this.bizCenterContractService = bizCenterContractService;
            this._PropertyContractService = propertyContractService;
            this._deliverInformationSvc = _deliverInformationSvc;
            this._companyRegisterSrvc = _companyRegisterSrvc;
            this._questionSrvc = _questionSrvc;
        }

        //
        // GET: /WF/Workflow/

        public ActionResult Index()
        {
            return View();
        }

        /// <summary>
        /// 新建
        /// </summary>
        /// <param name="id"></param>
        /// <param name="url"></param>
        /// <returns></returns>
        public ActionResult Create(int id, string url, string currentTitle)
        {
            if (string.IsNullOrEmpty(currentTitle))
            {
                ViewBag.Title = "新建公文";
            }
            else
            {
                ViewBag.Title = currentTitle;
            }

            IEnumerable<FormForWorkflow> formForWorkflows;

            // 如果是Alm资产的工作流id == 128，不做用户过滤，方便测试阶段测试2014/4/14
            // 如果是Ecm资产的工作流id == 64，不做用户过滤，方便测试阶段测试2014/5/24
            formForWorkflows = //(id == 128 || id == 64) ?
                //forWorkflowSvc.GetFormForWorkflowsByWorkflowFormType(id, null) : 
                forWorkflowSvc.GetFormForWorkflowsByWorkflowFormType(id, CurrentUser.UserName);
            // ---

            var ffwf = new List<WorkflowClass>();
            Type enumType = typeof(WorkflowFormType);
            foreach (WorkflowFormType w in Enum.GetValues(enumType))
            {
                if (EnumEx.ToInt(w) == (int)WorkflowFormType.Report ||
                    EnumEx.ToInt(w) == (int)WorkflowFormType.Supervise ||
                    EnumEx.ToInt(w) == (int)WorkflowFormType.SpspVC) continue;
                var name = EnumEx.GetEnumDisplay(w);
                ffwf.Add(new WorkflowClass()
                {
                    className = name,
                    data = formForWorkflows.Where(m => m.WorkflowFormTypeValue == EnumEx.ToInt(w)).ToList()
                });
            }
            ViewBag.url = url;
            return View(ffwf);
        }

        /// <summary>
        /// 待办
        /// </summary>
        /// <param name="id"></param>
        /// <param name="url"></param>
        /// <returns></returns>
        public ActionResult ToDo(int id, string url)
        {
            ViewBag.detailUrl = url;
            return PartialView(id);
        }

        public ActionResult ToDoJson(int id, int page = 1, int rows = 20)
        {
            var concreteWorkflows = concreteWorkflowSvc.GetToDoConcreteWorkflows(this.CurrentUser, id, true);
            var count = concreteWorkflows.Count();
            var tt = concreteWorkflows.OrderByDescending(n => n.CreatedDate).Skip((page - 1) * rows).Take(rows);
            var conWfViewModelList = (from c in tt
                                      select c.Dto(this.CurrentUser.UserName)).ToList();
            return Json(new { total = count, rows = conWfViewModelList });
        }

        /// <summary>
        /// 跟踪
        /// </summary>
        /// <param name="id"></param>
        /// <param name="url"></param>
        /// <returns></returns>
        public ActionResult MyStart(int id, string url)
        {
            ViewBag.detailUrl = url;
            return PartialView(id);
        }

        public ActionResult MyStartJson(int id, int page = 1, int rows = 20)
        {
            var totalMyStartConcreteWorkflows = concreteWorkflowSvc.GetMyStartConcreteWorkflows(CurrentUser.UserName, id);
            var count = totalMyStartConcreteWorkflows.Count();
            var tt = totalMyStartConcreteWorkflows.Skip((page - 1) * rows).Take(rows);
            var myStartConcreteWorkflowViewModelList = (from c in tt
                                                        select c.Dto(this.CurrentUser.UserName, true)).ToList();

            var myStartConcreWorkflowUrl = Url.Action("ContractDetail", "Home", new { area = "ContractManagement" });


            foreach (var myStartContractToDoViewModel in myStartConcreteWorkflowViewModelList)
            {
                myStartContractToDoViewModel.viewUrl =
                    string.Format(
                        myStartConcreWorkflowUrl + "?formforworkflowid=0&concreteworkflowid={0}&isView={1}"
                        , myStartContractToDoViewModel.ConcreteWorkflowId, myStartContractToDoViewModel.isView);
            }
            var conWfViewModelList = (from c in tt
                                      select c.Dto(this.CurrentUser.UserName)).OrderByDescending(n => n.CreateTime).ToList();
            return Json(new { total = count, rows = conWfViewModelList });
        }

        /// <summary>
        /// 详情
        /// </summary>
        /// <param name="formforworkflowid"></param>
        /// <param name="concreteworkflowid"></param>
        /// <param name="isView"></param>
        /// <param name="formforWorkFlowName"></param>
        /// <returns></returns>
        public ActionResult Detail(int formforworkflowid, int concreteworkflowid, bool? isView = false, string formforWorkFlowName = "")
        {
            bool CanBack = false;
            bool CanEnd = false;
            bool CanSend = false;
            ConcreteWorkflow CWF = new ConcreteWorkflow();
            //int fileCount = 0;

            if (!string.IsNullOrWhiteSpace(formforWorkFlowName))
            {
                var forWorkflow = forWorkflowSvc.GetFormForWorkflowByName(formforWorkFlowName);
                if (forWorkflow != null)
                {
                    formforworkflowid = forWorkflow.FormForWorkflowId;
                }
            }

            //创建
            if (concreteworkflowid == 0)
            {
                CWF.FormForWorkflow = forWorkflowSvc.GetFormForWorkflow(formforworkflowid);
                CWF.FormForWorkflowId = formforworkflowid;
                CWF.CurrentStatus = workflowStatusSvc.GetStartedStatus(CWF.FormForWorkflow.WorkflowId);
                CWF.CurrentStatusId = CWF.CurrentStatus.WorkflowStatusId;
            }
            else
            {
                var _WorkflowFormType = concreteWorkflowSvc.GetConcreteWorkflow(concreteworkflowid).FormForWorkflow.WorkflowFormTypeValue;
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
                    CanSend = rules.Where(x => x.IsSequence && x.NextStatus.IdForTheWorkflow != 0).Any();
                    CanEnd = rules.Where(x => x.NextStatus.IdForTheWorkflow == 0).Any();
                }
                //经办
                //var HasDoneconcreteWorkflows = concreteWorkflowSvc.GetHasDoneConcreteWorkflowsByID(this.CurrentUser, concreteworkflowid, _WorkflowFormType);
                //flag = flag || (HasDoneconcreteWorkflows.Any() && (isView == true ? true : false));
                //经办不判断权限,工作流一览可见(没事别想太多,原来控制个毛,自作孽-.-|||)
                flag = flag || (isView == true ? true : false);

                //set IsRead Status
                if (!isView.GetValueOrDefault())
                {
                    CWF = concreteWorkflowSvc.GetConcreteWorkflow(concreteworkflowid);
                    var currentOperation = CWF.CurrentOperations.Where(k => k.UserName == this.CurrentUser.UserName).FirstOrDefault();
                    if (currentOperation != null && !currentOperation.IsRead.GetValueOrDefault())
                    {
                        var operation =
                            concreteWorkflowOperationSvc.GetConcreteWorkflowOperation(
                                currentOperation.ConcreteWorkflowOperationId);
                        operation.IsRead = true;
                        concreteWorkflowOperationSvc.SaveConcreteWorkflowOperation();
                    }
                }


                if (flag)
                {
                    CWF = concreteWorkflowSvc.GetConcreteWorkflow(concreteworkflowid);
                    //                    //附件个数
                    //                    fileCount = attachmentService.GetAttachments((int)AttachmentType.Office, CWF.ConcreteWorkflowId).Count();
                }
                else
                {
                    return View("PermissionDenied");
                }
            }
            ViewBag.CanBack = CanBack;
            ViewBag.CanEnd = CanEnd;
            ViewBag.CanSend = CanSend;
            ViewBag.isView = isView;
            //ViewBag.fileCount = fileCount;
            return PartialView(CWF);
        }


        public ActionResult PermissionDenied()
        {
            return View();
        }


        [HttpPost]
        public ActionResult ActiveSubmit(int id)
        {
            var concreteWorkflow = concreteWorkflowSvc.GetConcreteWorkflow(id);
            #region  激活流程流转
            var activeOpt =
                concreteWorkflow.ConcreteWorkflowOperations.Where(
                    k => k.IsActive == true && k.UserName == this.CurrentUser.UserName && k.HaveFinished).FirstOrDefault();
            if (activeOpt != null)
            {
                activeOpt.IsActive = false;
                var lastid =
                    concreteWorkflow.ConcreteWorkflowOperations.OrderByDescending(k => k.OperatedTime).FirstOrDefault().WorkflowStatusId;
                concreteWorkflow.CurrentStatusId = lastid;
                concreteWorkflowOperationSvc.UpdateConcreteWorkflowOperation(activeOpt);
                return this.Content(Msg("true", "激活状态已恢复", new Dictionary<string, object>() { { "type", concreteWorkflow.FormForWorkflow.WorkflowFormTypeValue } }));
            }
            #endregion
            return this.Content(Msg("false", "提交失败"));

        }

        //向companyRegister中插入数据时，设置SN
        public string GetMax()
        {
            string currentYear = "XC-" + DateTime.Today.Year + "-";
            Expression<Func<Question, bool>> where = o => o.SN.Contains(currentYear);
            var sn = int.Parse(_questionSrvc.GetObjects().Where(where).Max(s => s.SN.Substring(8)) ?? "0") + 1;
            return currentYear + sn.ToString("D6");
        }

        /// <summary>
        /// 结束流程
        /// </summary>
        /// <param name="id">concreteID</param>
        /// <returns></returns>
        [HttpPost]
        public ActionResult EndWorkFlow(int id, string CurrentUrl = "")
        {
            string errMsg;
            do
            {
                //流程结束提醒发送人设定为 管理员
                var sendUser = securityService.GetUser("admin");
                bool rst_mobile = true;
                bool rst_mail = true;
                var concreteWorkflow = concreteWorkflowSvc.GetConcreteWorkflow(id);
                string mailTitle = GetWorkflowMailTitle(concreteWorkflow);
                var currentOperators =
                    concreteWorkflow.CurrentOperations.Where(
                        o => o.UserName == this.CurrentUser.UserName && o.HaveFinished == false).ToList();
                var operationList = concreteWorkflow.ConcreteWorkflowOperations.Where(o => o.HaveFinished);

                int Relatetype = 9;
                var nextRule = workflowRuleService.GetWorkflowRules(concreteWorkflow.FormForWorkflow.WorkflowId, concreteWorkflow.CurrentStatusId).FirstOrDefault(k => k.NextStatus.IdForTheWorkflow == 0);
                if (nextRule != null)
                {
                    Relatetype = nextRule.MessageTempleateType.GetValueOrDefault();
                }


                //结束前调用事件
                var BeforeEndParam =
                    new WorkflowTransitionExecutingEventArgs(currentOperators.FirstOrDefault().WorkflowStatus, null,
                                                             concreteWorkflow);
                var _temp = concreteWorkflow.FormForWorkflow.EditUrl.Split('/');
                string controllerName = _temp[_temp.Length - 2];

                object resultObj;
                try
                {
                    resultObj = ResultMethod(controllerName, "Executing", new object[] { BeforeEndParam });
                }
                catch (Exception ex)
                {
                    errMsg = "Executing 执行错误（" + ex.Message + ")";
                    errMsg = errMsg + ",ContollerName:" + controllerName;
                    Utility.Log.WriteInfo("EndWorkFlow-" + errMsg);
                    break;
                }


                var obj = resultObj as List<ValidateInfo>;
                if (obj != null)
                {
                    if (obj.Count > 0)
                    {
                        return PartialView("../ValidatePartialView", obj);
                    }
                }
                //操作列表状态

                var currentRule =
                        workflowRuleService.GetWorkflowRules(concreteWorkflow.FormForWorkflow.WorkflowId,
                                                             currentOperators.First().WorkflowStatusId)
                                           .Where(x => x.NextStatus.IdForTheWorkflow == 0);

                if (currentOperators.Any(x => x.WorkflowStatus.IdForTheWorkflow == 0))
                {
                    // var currentStatusID =
                    //                      concreteWorkflow.ConcreteWorkflowOperations.Where(k => k.HaveFinished)
                    //                          .OrderByDescending(r => r.OperatedTime)
                    //                          .FirstOrDefault().WorkflowStatusId;
                    foreach (var item in currentOperators)
                    {
                        item.HaveFinished = true;
                        item.OperatedTime = DateTime.Now;
                        item.OperationName = "结束流程";
                        concreteWorkflowOperationSvc.SaveConcreteWorkflowOperation();
                    }
                    //eisp结束没有rule
                    /*                    currentRule = workflowRuleService.GetWorkflowRules(concreteWorkflow.FormForWorkflow.WorkflowId,
                                                                                 currentStatusID)
                                                               .Where(x => x.NextStatus.IdForTheWorkflow == 0);*/
                }
                else
                {
                    foreach (var item in currentOperators)
                    {
                        item.HaveFinished = true;
                        item.OperatedTime = DateTime.Now;
                        item.OperationName = currentRule.FirstOrDefault().Name;
                        concreteWorkflowOperationSvc.SaveConcreteWorkflowOperation();
                    }

                    concreteWorkflow.CurrentStatusId = currentRule.FirstOrDefault().NextStatusId;

                    var temp = new ConcreteWorkflowOperation();
                    temp.ConcreteWorkflowId = concreteWorkflow.ConcreteWorkflowId;
                    temp.HaveFinished = true;
                    temp.UserName = this.CurrentUser.UserName;
                    temp.OperatedTime = DateTime.Now;
                    temp.OperationName = "结束流程";
                    temp.WorkflowStatusId = currentRule.FirstOrDefault().NextStatusId;
                    concreteWorkflowOperationSvc.CreateConcreteWorkflowOperation(temp);
                }

                //转交材料时，判断业务名称是否为“内资企业工商注册”或者“外资企业工商注册”,为真时向companyRegister插入一条数据
                var result = _deliverInformationSvc.GetMany(o => o.ConcreteWorkflowId == id).ToList();
                if (concreteWorkflow.FormForWorkflowId == 332 && (result[0].BusinessNameId == 40 || result[0].BusinessNameId == 41))
                {
                    CompanyRegister companyRegister = new CompanyRegister();
                    companyRegister.SN = GetMax();
                    companyRegister.CreatedTime = DateTime.Now;
                    companyRegister.Creator = result[0].Creator;
                    companyRegister.CorpBaseInfoId = result[0].CorpBaseInfoId;
                    companyRegister.ContractName = result[0].ContractName;
                    companyRegister.ContractPhone = result[0].ContractPhone;
                    if (result[0].BusinessNameId == 40)
                    {
                        companyRegister.CompanyNature = "内资";
                    }
                    else
                    {
                        companyRegister.CompanyNature = "外资";
                    }
                    companyRegister.ConsultancyService = "核名";
                    companyRegister.IsAccept = "否";
                    companyRegister.AcceptInfo = result[0].AcceptInfo;
                    companyRegister.FillTime = DateTime.Now;
                    companyRegister.IsResponse = "是";
                    companyRegister.ResponseTime = DateTime.Now;
                    companyRegister.BusinessScope = "";
                    companyRegister.CompanyType = "";
                    companyRegister.ConsultancyServiceRemarks = "";
                    companyRegister.CorpBaseInfoOther = "";
                    companyRegister.EstablishTime = DateTime.Now;
                    companyRegister.LegalRepresent = "";
                    companyRegister.LicenseAddress = "";
                    companyRegister.LicenseName = "";
                    companyRegister.ModifyName = CurrentUser.UserName;
                    companyRegister.ModifyTime = DateTime.Now;
                    companyRegister.PayCapital = "";
                    companyRegister.RegisterAccount = "";
                    companyRegister.RegisterNo = "";
                    companyRegister.Remarks = "";
                    companyRegister.Merchant = "";
                    _companyRegisterSrvc.CreateModel(companyRegister);
                }

                int typevalue = concreteWorkflow.FormForWorkflow.WorkflowFormTypeValue;
                var text = messageTemplateService.GetMessageTemplate(Relatetype);
                var propertyContract =
                   _PropertyContractService.GetMany(a => a.ConcreteWorkflowId == id).FirstOrDefault();
                var cwfo = new ConcreteWorkflowOperation();
                if (propertyContract != null)
                {
                     cwfo = operationList.Where(x => x.WorkflowStatus.IdForTheWorkflow == 2).First();
                }
                else
                {
                     cwfo = operationList.Where(x => x.WorkflowStatus.IdForTheWorkflow == 1).First();   
                }
                

                //ToDo: 发送办结短信(发起人)
                if (concreteWorkflow.FormForWorkflow.Workflow.IsEndTextStartUser)
                {
                    var mobileMessage = new MobileMessage();
                    mobileMessage.SendUserName = sendUser.UserName;

                    switch (typevalue)
                    {
                        case (int)WorkflowFormType.Office:
                            // Relatetype = (int)MessageTemplateType.AskEnd; //请示签报
                            mobileMessage.Type = MobileMessageType.RequestSign;
                            break;
                        case (int)WorkflowFormType.Supervise:
                            // Relatetype = (int)MessageTemplateType.TaskEnd; //督办\催办
                            mobileMessage.Type = MobileMessageType.TaskHandle;
                            break;
                        case (int)WorkflowFormType.Contract:
                            // Relatetype = (int)MessageTemplateType.TaskEnd; 
                            mobileMessage.Type = MobileMessageType.Clm;
                            break;
                        case (int)WorkflowFormType.Alm:
                            // Relatetype = (int)MessageTemplateType.TaskEnd; 
                            mobileMessage.Type = MobileMessageType.Alm;
                            break;
                        case (int)WorkflowFormType.Ecm:
                            // Relatetype = (int)MessageTemplateType.TaskEnd; 
                            mobileMessage.Type = MobileMessageType.Ecm;
                            break;
                    }
                    mobileMessage.Message = GetReplaceMsg(text != null ? text.Content : _template, concreteWorkflow);


                    mobileMessage.ReceiverUserName = cwfo.UserName;
                    mobileMessage.MobilePhone = cwfo.UserObj.MobilePhone;

                    try
                    {
                        rst_mobile = rst_mobile && EmppSender(mobileMessage, true);
                    }
                    catch (Exception ex)
                    {
                        errMsg = " EmppSender 执行错误（" + ex.Message + ")";
                        Utility.Log.WriteInfo("EndWorkFlow-" + errMsg);
                        break;
                    }

                }
                concreteWorkflow.PreOperator = "";
                if (concreteWorkflow.IsStop)
                {
                    concreteWorkflow.IsStop = false;
                    concreteWorkflowSvc.UpdateConcreteWorkflow(concreteWorkflow);
                }
                concreteWorkflowSvc.SaveConcreteWorkflow();

                if (concreteWorkflow.FormForWorkflow.Workflow.IsEndMailStartUser)
                {
                    //邮件提醒
                    var mailcontent = GetReplaceMsg(text != null ? text.MailContent : _mailtemplate, concreteWorkflow);
                    CurrentUrl = CurrentUrl.IndexOf("&isDraft") > -1 ? CurrentUrl.Substring(0, CurrentUrl.IndexOf("&isDraft")) : CurrentUrl;
                    CurrentUrl = CurrentUrl.IndexOf("&isView") > -1 ? CurrentUrl.Substring(0, CurrentUrl.IndexOf("&isView")) : CurrentUrl;

                    mailcontent = mailcontent + "<a href ='" + CurrentUrl + "&isView=True' class='mail_detail_Link' target='_blank'>查看</a>";
                    try
                    {
                        rst_mail = rst_mail &&
                                  SendWorkflowMail(typevalue, mailcontent, new string[] { cwfo.UserName }, mailTitle, true);
                    }
                    catch (Exception ex)
                    {
                        errMsg = " SendWorkflowMail 执行错误（" + ex.Message + ")";
                        Utility.Log.WriteInfo("EndWorkFlow-" + errMsg);
                        break;
                    }

                }

                //督办结束
                if (concreteWorkflow.FormForWorkflow.WorkflowFormType == WorkflowFormType.Supervise)
                {
                    var oversee = taskOverseeService.GetTaskOverseeByConcreteID(id);
                    //相关领导
                    var leader = oversee.RelatedLeader;
                    //抄送对象
                    var ccObj = oversee.CcLeaderIds;

                    var array = (leader + "," + ccObj).Split(',');
                    var _text = messageTemplateService.GetMessageTemplate(13);
                    var mobileMessage = new MobileMessage();
                    mobileMessage.Message = GetReplaceMsg(text != null ? _text.Content : _template, concreteWorkflow);
                    var mailcontent = GetReplaceMsg(text != null ? _text.MailContent : _mailtemplate, concreteWorkflow);
                    foreach (string s in array)
                    {
                        if (s == "") continue;
                        mobileMessage.ReceiverUserName = s;
                        mobileMessage.MobilePhone = securityService.GetUser(s).MobilePhone;
                        rst_mobile = rst_mobile && EmppSender(mobileMessage, true);
                    }

                    try
                    {
                        rst_mail = rst_mail && SendWorkflowMail(typevalue, mailcontent, array, mailTitle);
                    }
                    catch (Exception ex)
                    {
                        errMsg = " SendWorkflowMail 执行错误（" + ex.Message + ")";
                        Utility.Log.WriteInfo("EndWorkFlow-" + errMsg);
                        break;
                    }

                }

                //结束后调用事件
                var EndParam = new WorkflowTransitionExecutedEventArgs(
                    currentOperators.FirstOrDefault().WorkflowStatus, null, concreteWorkflow);

                try
                {
                    ExecuteMethod(controllerName, "Executed", new object[] { EndParam });
                }
                catch (Exception ex)
                {
                    errMsg = " Executed 执行错误（" + ex.Message + ")";
                    Utility.Log.WriteInfo("EndWorkFlow-" + errMsg);
                    break;
                }

                #region do Circulate
                controllerName = CovertSpecialForWrongStandard.CovertWrongControllerName(controllerName);
                passRoundForPerusalService.AutoExecut(currentRule.FirstOrDefault(), concreteWorkflow.ConcreteWorkflowId, new List<string>(), this.CurrentUser, concreteWorkflowSvc.GetBusinessModel(controllerName, concreteWorkflow.ConcreteWorkflowId));
                #endregion

               if (!rst_mobile && !rst_mail)
                {
                    return
                        this.Content(string.Format("{{\"success\": {0},\"message\":\"{1}\",\"type\":\"{2}\"}}", "true",
                                                   "流程已结束,短信或邮件发送失败!",
                                                   concreteWorkflow.FormForWorkflow.WorkflowFormTypeValue));
                }
                else
                {
                    return
                        this.Content(string.Format("{{\"success\": {0},\"message\":\"{1}\",\"type\":\"{2}\"}}", "true",
                                                   "流程已结束!", concreteWorkflow.FormForWorkflow.WorkflowFormTypeValue));
                }

            } while (false);

            return this.Content(Msg("false", "流程已结束!但有错误：" + errMsg));

        }

        private bool SendWorkflowMail(int typeValue, string content, string[] users, string mailTitle = "", bool isSpec = false)
        {
            if (string.IsNullOrEmpty(mailTitle))
                mailTitle = "【无详细】";
            var mailFrom = isSpec ? securityService.GetUser("admin") : this.CurrentUser;
            string mailTos = string.Join(",", users);
            switch (typeValue)
            {
                case (int)WorkflowFormType.Report:
                    mailTitle = string.Format("OA报表提醒: {0}", mailTitle);
                    break;
                case (int)WorkflowFormType.Office:
                    mailTitle = string.Format("OA请示签报提醒: {0}", mailTitle);
                    break;
                case (int)WorkflowFormType.Supervise:
                    mailTitle = string.Format("OA任务督办提醒: {0}", mailTitle);
                    break;
                case (int)WorkflowFormType.Wiseonline:
                    mailTitle = string.Format("OA工作流提醒: {0}", mailTitle);
                    break;
                case (int)WorkflowFormType.Contract:
                    mailTitle = string.Format("合同提醒: {0}", mailTitle);
                    break;
                case (int)WorkflowFormType.Ecm:
                    mailTitle = string.Format("合同提醒: {0}", mailTitle);
                    break;
            }
            //return MailHelper.SendPromptMail(mailFrom.UserName, im, mailFrom);
            return mailHelper.SendPromptMail(mailTos, "", mailFrom.UserName, mailTitle, content,
                                             (int)EmergencyStatus.Normal,
                                             0, 0, 0);
        }

        /// <summary>
        /// 获取工作流提醒的邮件标题
        /// </summary>
        /// <param name="concreteWorkflow"></param>
        /// <returns></returns>
        private string GetWorkflowMailTitle(ConcreteWorkflow concreteWorkflow)
        {
            StringBuilder title = new StringBuilder();

            if (concreteWorkflow.FormForWorkflow!=null)
            {
                title.AppendFormat("【{0}】", concreteWorkflow.FormForWorkflow.Name);
            }

            if (!string.IsNullOrEmpty(concreteWorkflow.Sn))
            {
                title.AppendFormat("【{0}】", concreteWorkflow.Sn);
            }
            else if (!string.IsNullOrEmpty(concreteWorkflow.Title))
            {
                title.AppendFormat("【{0}】", concreteWorkflow.Title);
            }

            if (string.IsNullOrEmpty(title.ToString()))
            {
                title.Append("【无详细】");
            }
           
            return title.ToString();
        }

        /// <summary>
        /// 显示操作进度列表
        /// </summary>
        /// <param name="concreteWorkflowId"></param>
        /// <returns></returns>
        public ActionResult ViewOperations(int concreteWorkflowId)
        {
            var operationList = concreteWorkflowOperationSvc.GetConcreteWorkflowOperationsAll(concreteWorkflowId).Select(o => o.Dto());
            var passRoundForPerusalList = passRoundForPerusalService.GetPassRoundForPerusals(concreteWorkflowId).Select(o => o.Dto2());
            var operationViewModelList = new List<OperationViewModel>();
            operationViewModelList.AddRange(operationList.OrderBy(k => k.OperatedTime.ToString() == "0001/1/1 0:00:00" ? DateTime.Now : k.OperatedTime));
            operationViewModelList.AddRange(passRoundForPerusalList);

            return View(operationViewModelList);
        }

        /// <summary>
        /// 新增操作记录
        /// </summary>
        /// <param name="id">工作流ID(concreteWorkflowId)</param>
        /// <returns></returns>
        [HttpGet]
        public ActionResult AddOperator(int id)
        {
            var concrete = concreteWorkflowSvc.GetConcreteWorkflow(id);
            var flow = concrete.FormForWorkflow.Workflow.WorkflowStatusList.ToList().Select(k => new
            {
                WorkflowStatusId = k.WorkflowStatusId,
                Name = k.Name
            });
            ViewBag.Status = JsonConvert.SerializeObject(flow);
            return View(new ConcreteWorkflowOperation()
            {
                ConcreteWorkflowId = id
            });
        }

        [HttpPost]
        public ActionResult AddOperator(ConcreteWorkflowOperation model)
        {
            try
            {
                var operation = new ConcreteWorkflowOperation();
                ModelCopier.CopyModel(model, operation);
                operation.HaveFinished = true;
                concreteWorkflowOperationSvc.CreateConcreteWorkflowOperation(operation);
                return this.Content(Msg("true", "保存成功"));
            }
            catch (Exception)
            {
                return this.Content(Msg("false", "保存失败"));
            }
        }

        /// <summary>
        /// 流转页面
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public ActionResult ViewTransfer(int concreteid, bool? IsBack, bool? IsOversee)
        {
            var concreWf = concreteWorkflowSvc.GetConcreteWorkflow(concreteid);
            ViewBag.status = workflowRuleService.GetNextRulesByConditions(concreWf, IsBack, this.CurrentUser.UserName);
            //加班流程发送加班人默认

            if (concreWf.FormForWorkflow.Name.Trim() == "加班申请单")
            {
                var flowName =
                    workflowStatusSvc.GetWorkflowStatuss()
                        .FirstOrDefault(a => a.WorkflowStatusId == concreWf.CurrentStatusId && a.Name == "分管领导审批");
                if (flowName != null)
                {
                    var workman = overTimeWorkService.GetOverTimeWorkByConcreId(concreteid);
                    if (workman != null)
                    {
                        ViewBag.workmanUsername = workman.OvertimeName;
                        ViewBag.workmanDisplay = workman.OvertimeNameDisplayName;

                    }
                }
              
            }
            var viewmodel = new ConcreteWorkflowTransferViewModel();
            viewmodel.ConcreteWorkflowId = concreteid;
            viewmodel.NeedSendMail = concreWf.CurrentStatus.IsSendRelations == true ? true : false;
            viewmodel.NeedSendMessage = true;
            viewmodel.IsBack = (IsBack ?? false) ? 1 : 0;
            if (concreWf.FormForWorkflow.Name.Trim() == "客户退租审批单")
                viewmodel.DeadLine = DateTime.Now.AddDays(1);
            else
                viewmodel.DeadLine = DateTime.Now.AddDays(3);
            viewmodel.NeedSendRelation = true;
            ViewBag.Relation = concreWf.CurrentStatus.IsSendRelations == true ? true : false;
            ViewBag.ConcreWorkflow = concreWf;
            viewmodel.FormWorkflowType = Enum.GetName(typeof(WorkflowFormType), concreWf.FormForWorkflow.WorkflowFormType);
            ViewBag.IsOversee = IsOversee == true ? true : false;
            return View(viewmodel);
        }

        /// <summary>
        /// 具体工作流流转执行
        /// </summary>
        /// <param name="viewMode"></param>
        /// <returns></returns>
        [HttpPost]
        public ActionResult Transfer(ConcreteWorkflowTransferViewModel viewMode)
        {
            var concreteWorkflow = concreteWorkflowSvc.GetConcreteWorkflow(viewMode.ConcreteWorkflowId);
            //附件类型（手机端使用）
            var attachmentType = concreteWorkflow.FormForWorkflow.AttachementTypeValue.HasValue
                                     ? concreteWorkflow.FormForWorkflow.AttachementTypeValue.Value
                                     : 0;
            //需要推送的工作流类型
            int noticationFormType = (int)WorkflowFormType.Office + (int)WorkflowFormType.Wiseonline +
                                     (int)WorkflowFormType.Contract + (int)WorkflowFormType.Supervise +
                                     (int)WorkflowFormType.Ecm;
            //是否需要手机推送
            bool needNotication = ((concreteWorkflow.FormForWorkflow.WorkflowFormTypeValue | noticationFormType) ==
                                   noticationFormType);
            var currentRule = workflowRuleService.GetWorkflowRule(viewMode.CurrentWorkflowRuleId);
            string mailTitle = GetWorkflowMailTitle(concreteWorkflow);
            //发送前调用事件
            var BeforeTransferParam = new WorkflowTransitionExecutingEventArgs(currentRule.Status,
                currentRule.NextStatus, concreteWorkflow, this.User);
            var _temp = concreteWorkflow.FormForWorkflow.EditUrl.Split('/');
            string controllerName = _temp[_temp.Length - 2];
            var resultObj = ResultMethod(controllerName, "Executing", new object[] { BeforeTransferParam });
            var obj = resultObj as List<ValidateInfo>;
            if (obj != null)
            {
                if (obj.Count > 0)
                {
                    return PartialView("../ValidatePartialView", obj);
                }
            }

            #region 判断是否全部完成
            if (!concreteWorkflow.HaveFinished)
            {
                IEnumerable<ConcreteWorkflowOperation> currentOperators;
                //多人操作仅报表使用
                if (concreteWorkflow.FormForWorkflow.WorkflowId == 1 && concreteWorkflow.CurrentStatus.IsMulUserOperate.HasValue && concreteWorkflow.CurrentStatus.IsMulUserOperate.Value)
                {
                    var CurrentStatusId = concreteWorkflow.CurrentStatusId;
                    currentOperators = concreteWorkflow.ConcreteWorkflowOperations.Where(o => o.WorkflowStatusId == CurrentStatusId && o.HaveFinished == false);
                }
                else
                { //留言              
                    var ds = delegateSettingService.GetDelegateSettingAboutOnePerson(this.CurrentUser.UserName, concreteWorkflow.FormForWorkflowId)
                        .Where(o => o.Mandatary == this.CurrentUser.UserName).Select(x => x.Client).ToList();
                    ds.Add(this.CurrentUser.UserName);
                    currentOperators = concreteWorkflow.CurrentOperations.Where(o => ds.Contains(o.UserName) && o.HaveFinished == false);
                }
                foreach (var item in currentOperators)
                {
                    item.HaveFinished = true;
                    if (!string.IsNullOrWhiteSpace(viewMode.Message))
                        item.Content = viewMode.Message;
                    item.OperatedTime = DateTime.Now;
                    item.OperationName = currentRule.Name;
                    item.UserName = this.CurrentUser.UserName;
                    if (currentRule.Status.IdForTheWorkflow == 1 && currentRule.NextStatus.IdForTheWorkflow == 0 && concreteWorkflow.FormForWorkflow.WorkflowFormType != WorkflowFormType.Supervise)
                    {
                        item.WorkflowStatusId = currentRule.NextStatusId;
                        concreteWorkflow.CurrentStatusId = currentRule.NextStatus.WorkflowStatusId;
                        concreteWorkflowSvc.SaveConcreteWorkflow();
                    }
                    concreteWorkflowOperationSvc.SaveConcreteWorkflowOperation();
                }
            }
            #endregion
            bool rst_mobile = true;
            bool rst_mail = true;
            string Message = "已流转下一步";
            #region 短信内容处理

            var content = currentRule.MessageTempleates != null ? currentRule.MessageTempleates.Content : _template;
            var mailcontent = currentRule.MessageTempleates != null ? currentRule.MessageTempleates.MailContent : _mailtemplate;
            //有序会签模板特殊处理
            if (!concreteWorkflow.HaveFinished && currentRule.Status.IsCountersignSequence)
            {
                //get last status
                var _time = concreteWorkflow.CurrentOperations.OrderBy(k => (k.OperatedTime == null ? DateTime.Now : k.OperatedTime)).First().OperatedTime;
                var last = concreteWorkflow.ConcreteWorkflowOperations.Where(k => k.OperatedTime < _time)
                    .OrderBy(k => k.OperatedTime)
                    .Last()
                    .WorkflowStatus;
                var _rule = workflowRuleService.GetWorkflowRules(concreteWorkflow.FormForWorkflow.WorkflowId, last.WorkflowStatusId);
                var rule = _rule.Where(k => k.NextStatusId == currentRule.StatusId).First();
                if (rule != null)
                {
                    content = rule.MessageTempleates != null ? rule.MessageTempleates.Content : _template;
                    mailcontent = rule.MessageTempleates != null ? rule.MessageTempleates.MailContent : _mailtemplate;
                }
            }

            //CLM发起人发送邮件有问题
           
                viewMode.CurrentUrl = viewMode.CurrentUrl.IndexOf("&isDraft") > -1
                    ? viewMode.CurrentUrl.Substring(0, viewMode.CurrentUrl.IndexOf("&isDraft"))
                    : viewMode.CurrentUrl;

                viewMode.CurrentUrl = viewMode.CurrentUrl.IndexOf("&isView") > -1
                    ? viewMode.CurrentUrl.Substring(0, viewMode.CurrentUrl.IndexOf("&isView"))
                    : viewMode.CurrentUrl;

                mailcontent = mailcontent + "<a href ='" + viewMode.CurrentUrl + "&isView=True' class='mail_detail_Link' target='_blank'>查看</a>";
          

            if (viewMode.NeedSendMessage || viewMode.NeedSendRelation || viewMode.NeedSendMail)
            {
                content = GetReplaceMsg(content, concreteWorkflow);
                mailcontent = GetReplaceMsg(mailcontent, concreteWorkflow);
            }
            #endregion
            var mobileMessage = new MobileMessage();
            mobileMessage.Message = content;
            mobileMessage.SendUserName = CurrentUser.UserName;

            #region 判断发送短信的类型以及提醒相关的内容类型
            int Relatetype = 0; //提醒相关用
            int typevalue = concreteWorkflow.FormForWorkflow.WorkflowFormTypeValue;
            switch (typevalue)
            {
                case (int)WorkflowFormType.Office: Relatetype = 9; //请示签报
                    mobileMessage.Type = MobileMessageType.RequestSign;
                    break;
                case (int)WorkflowFormType.Supervise: Relatetype = 4; //督办\催办
                    mobileMessage.Type = MobileMessageType.TaskHandle;
                    break;
            }
            #endregion

            bool isToEnd = concreteWorkflow.CurrentStatus.IdForTheWorkflow == 0;

            #region 全部完成后发送短信及流转到下一步
            if (currentRule.Status.IdForTheWorkflow == 1 && currentRule.NextStatus.IdForTheWorkflow == 0 && concreteWorkflow.FormForWorkflow.WorkflowFormType != WorkflowFormType.Supervise)
            {
                //点点办公消息推送
                //Task.Factory.StartNew(() =>
                //                      mobileCommonHelper.SendNoticationByHttp(
                //                          new string[] { userForTask.UserName },
                //                          mailTitle, DateTime.Now.ToString(),
                //                          MsgType.Workflow,
                //                          concreteWorkflow.ConcreteWorkflowId,
                //                          controllerName, isToEnd),
                //                      TaskCreationOptions.PreferFairness).LogException();
                //直接结束
                //发送短信
                if (viewMode.NeedSendMessage)
                {
                    mobileMessage.ReceiverUserName = this.CurrentUser.UserName;
                    mobileMessage.MobilePhone = this.CurrentUser.MobilePhone;
                    rst_mobile = rst_mobile && EmppSender(mobileMessage, true);
                }
                //发送邮件
                if (viewMode.NeedSendMail)
                {
                    rst_mail = rst_mail && SendWorkflowMail(typevalue, mailcontent, new string[] { this.CurrentUser.UserName }, mailTitle);
                }

                Message = "此流程已结束";
            }
            else
            {
                if (concreteWorkflow.HaveFinished)
                {
                    //设置到下一个节点状态并增加下一人节点的流转人员
                    concreteWorkflow.CurrentStatusId = currentRule.NextStatus.WorkflowStatusId;
                    concreteWorkflow.PreOperator = this.CurrentUser.UserName;
                    //缓存操作列表(供发送相关用)
                    var operationList = concreteWorkflow.ConcreteWorkflowOperations.Where(o => o.HaveFinished).ToList();

                    //发送第一个人(会签)
                    var sendmsgFlag = 0;
                    var sendUser = "";
                    foreach (var item in viewMode.UserNames.ToList())
                    {
                        if (item == "") continue;
                        var user = item;
                        if (sendmsgFlag == 0)
                            sendUser = item;
                        //委托转换
                        var ds = delegateSettingService.GetDelegateSettingByClientAndWorkFlowForm(item, concreteWorkflow.FormForWorkflowId, concreteWorkflow.FormForWorkflow.WorkflowFormTypeValue);
                        if (ds != null)
                        {
                            user = ds.Mandatary;
                            if (sendmsgFlag == 0)
                                sendUser = user;
                        }

                        var temp = new ConcreteWorkflowOperation();
                        temp.ConcreteWorkflowId = viewMode.ConcreteWorkflowId;
                        temp.HaveFinished = false;
                        temp.UserName = user;
                        temp.UserObj = securityService.GetUser(user);
                        temp.WorkflowStatusId = currentRule.NextStatusId;
                        temp.DeadLine = viewMode.DeadLine;
                        concreteWorkflowOperationSvc.CreateConcreteWorkflowOperation(temp);
                        //发送短信
                        if (viewMode.NeedSendMessage && sendmsgFlag == 0)
                        {
                            mobileMessage.ReceiverUserName = user;
                            mobileMessage.MobilePhone = temp.UserObj.MobilePhone;
                            rst_mobile = rst_mobile && EmppSender(mobileMessage, true);
                        }
                        //发送邮件
                        if (viewMode.NeedSendMail && sendmsgFlag == 0)
                        {
                            rst_mail = rst_mail && SendWorkflowMail(typevalue, mailcontent.Replace("&isView=True", ""), new string[] { sendUser }, mailTitle);
                        }
                        //无序会签都发送短信(sendmsgFlag标识为0发送短信,1不发送)
                        if (currentRule.NextStatus.IsCountersignSequence)
                        {
                            sendmsgFlag = 1;
                        }
                        if (needNotication)
                        {
                            //点点办公消息推送
                            Task.Factory.StartNew(() =>
                                                  mobileCommonHelper.SendNoticationByHttp(
                                                      new string[] { user },
                                                      mailTitle, DateTime.Now.ToString(),
                                                      MsgType.Workflow,
                                                      concreteWorkflow.ConcreteWorkflowId, attachmentType, controllerName, isToEnd),
                                                  TaskCreationOptions.PreferFairness).LogIfException();
                        }
                    }

                    //发送相关人员（勾选邮件或短信，发送相关功能）
                    if (viewMode.NeedSendRelation)
                    {
                        var specSendUser = securityService.GetUser("admin");
                        var concreteWorkflowOpreations = operationList.Where(o => (
                                                     o.WorkflowStatus.IsCountersign == null ||
                                                     !o.WorkflowStatus.IsCountersign.Value) && (
                                                     o.WorkflowStatus.IsSendRelations == null ||
                                                     !o.WorkflowStatus.IsSendRelations.Value))
                                              .DistinctBy(o => o.UserName).ToList();

                        if (viewMode.NeedSendMessage)
                        {
                            //操作列表上的人群发功能
                            var text = messageTemplateService.GetMessageTemplate(Relatetype);
                            mobileMessage.Message = GetReplaceMsg(text != null ? text.Content : _template,
                                                                  concreteWorkflow);
                            mailcontent = GetReplaceMsg(text != null ? text.MailContent : _mailtemplate,
                                                                  concreteWorkflow);
                            mobileMessage.SendUserName = specSendUser.UserName;
                            foreach (ConcreteWorkflowOperation cwfo in concreteWorkflowOpreations)
                            {
                                var _user = cwfo.UserObj;
                                mobileMessage.ReceiverUserName = cwfo.UserName;
                                mobileMessage.MobilePhone = cwfo.UserObj.MobilePhone;
                                rst_mobile = rst_mobile && EmppSender(mobileMessage, true);
                            }
                        }
                        if (viewMode.NeedSendMail)
                        {
                            //发送邮件
                            rst_mail = rst_mail &&
                                       SendWorkflowMail(typevalue, mailcontent,
                                       concreteWorkflowOpreations.Select(x => x.UserName).ToArray(), mailTitle, true);
                        }
                        if (needNotication)
                        {
                            //点点办公消息推送
                            Task.Factory.StartNew(
                                () =>
                                mobileCommonHelper.SendNoticationByHttp(
                                    concreteWorkflowOpreations.Select(x => x.UserName).ToArray(),
                                    mailTitle, DateTime.Now.ToString(),
                                    MsgType.Workflow,
                                    concreteWorkflow.ConcreteWorkflowId, attachmentType, controllerName, isToEnd),
                                TaskCreationOptions.PreferFairness).LogIfException();
                        }
                    }
                    if (concreteWorkflow.IsStop)
                    {
                        concreteWorkflow.IsStop = false;
                        concreteWorkflowSvc.UpdateConcreteWorkflow(concreteWorkflow);
                    }
                    concreteWorkflowSvc.SaveConcreteWorkflow();
                }
                else
                {
                    //有会签(有序时提供发送后面人会签,无序则不发送)
                    if (currentRule.Status.IsCountersignSequence)
                    {
                        var operation = concreteWorkflow.CurrentOperations.Where(x => !x.HaveFinished).First();
                        //发送短信
                        if (viewMode.NeedSendMessage)
                        {
                            mobileMessage.ReceiverUserName = operation.UserName;
                            mobileMessage.MobilePhone = operation.UserObj.MobilePhone;
                            rst_mobile = rst_mobile && EmppSender(mobileMessage, true);
                        }
                        //发送邮件
                        if (viewMode.NeedSendMail)
                        {
                            rst_mail = rst_mail &&
                                       SendWorkflowMail(typevalue, mailcontent, new string[] { operation.UserName },
                                           mailTitle);
                        }
                        if (needNotication)
                        {
                            //点点办公消息推送
                            Task.Factory.StartNew(
                                () => mobileCommonHelper.SendNoticationByHttp(new string[] { operation.UserName },
                                                                              mailTitle, DateTime.Now.ToString(),
                                                                              MsgType.Workflow,
                                                                              concreteWorkflow.ConcreteWorkflowId,
                                                                              attachmentType, controllerName, isToEnd),
                                TaskCreationOptions.PreferFairness).LogIfException();
                        }
                    }
                }
            }
            #endregion

            //发送后调用事件
            WorkflowTransitionExecutedEventArgs TransferParam = new WorkflowTransitionExecutedEventArgs(
                currentRule.Status,
                currentRule.NextStatus,
                concreteWorkflow);
            ExecuteMethod(controllerName, "Executed", new object[] { TransferParam });


            #region do Circulate
            controllerName = CovertSpecialForWrongStandard.CovertWrongControllerName(controllerName);
            if (controllerName == "WiseOnlineCustomContractChange" || controllerName == "WiseOnlineCustomContractSupplement" || controllerName == "WiseOnlineCustomTemplateContract")
            {
                controllerName = "WiseOnlineCustomContract";
            }
            passRoundForPerusalService.AutoExecut(currentRule, concreteWorkflow.ConcreteWorkflowId, viewMode.UserNames.ToList(), this.CurrentUser, concreteWorkflowSvc.GetBusinessModel(controllerName, concreteWorkflow.ConcreteWorkflowId));
            #endregion
            if (!rst_mail && !rst_mobile)
            {
                return this.Content(Msg("true", Message + ",短信或邮件发送失败!"));
            }
            return this.Content(Msg("true", Message + "!"));
        }

        /// <summary>
        /// 默认值设置
        /// </summary>
        /// <param name="Id"></param>
        /// <param name="UserName"></param>
        /// <returns></returns>
        public ActionResult DefaultSetting(int Id, string UserName)
        {
            var rule = workflowRuleService.GetWorkflowRule(Id);
            var def = workflowRuleDefaultService.GetMany(
                k => k.UserName == this.CurrentUser.UserName && k.StatusID == rule.StatusId);
            if (def.Any())
            {
                var dd = def.Last();
                dd.DefaultRuleID = rule.WorkflowRuleId;
                dd.DefaultUserName = UserName;
                workflowRuleDefaultService.UpdateModel(dd);
            }
            else
            {
                var dd = new WorkflowRuleDefault();
                dd.StatusID = rule.StatusId;
                dd.DefaultRuleID = rule.WorkflowRuleId;
                dd.DefaultUserName = UserName;
                dd.UserName = this.CurrentUser.UserName;
                workflowRuleDefaultService.CreateModel(dd);
            }
            return Content("设置成功");
        }

        /// <summary>
        /// 下一步人选
        /// </summary>
        /// <param name="wfRuleId"></param>
        /// <param name="concreteId"></param>
        /// <returns></returns>
        public ActionResult GetNextNodeStatusHandledUsers(int wfRuleId, int concreteId)
        {
            bool bindUser = false;//页面的用户选择控件是否默认选中某个用户
            bool lockUser = false;//页面的用户选择控件是否锁定选择(控件禁用,用户不可更改)
            bool multiple = false;

            string defaultUserName = string.Empty; //下一节点操作人用户名(下一节点只有一人时)

            //获取下一节点处理规则
            var wkflRule = workflowRuleService.GetWorkflowRule(wfRuleId);
            var companyList = cmpnSvc.GetCompanies();
            var deptList = dptmntSvc.GetDepartments();
            var resultUserData = new List<User>(); //页面用户选择控件的数据源

            var sbUsers = string.Empty;
            var contentReuslt = new ContentResult();

            //直接结束
            if (wkflRule.Status.IdForTheWorkflow == 1 && wkflRule.NextStatus.IdForTheWorkflow == 0)
            {
                bindUser = true;
                lockUser = true;
                resultUserData.Add(this.CurrentUser);
                defaultUserName = this.CurrentUser.UserName;

                sbUsers = GetUserTree.GetUsers(companyList, deptList, resultUserData);
                contentReuslt = this.Content(sbUsers);

                return Json(new { BindUser = bindUser, LockUser = lockUser, UserName = defaultUserName.Trim(';'), Tree = contentReuslt, IsMultiple = multiple });
            }

            var concreWorkflow = concreteWorkflowSvc.GetConcreteWorkflow(concreteId);
            //多人操作(仅报表使用)
            if (wkflRule.WorkflowId == 1)
            {
                if (wkflRule.NextStatus.IsMulUserOperate.HasValue)
                {
                    multiple = wkflRule.NextStatus.IsMulUserOperate.Value;
                }
            }
            else
            {
                //会签多选
                if (wkflRule.NextStatus.IsCountersign.HasValue)
                {
                    multiple = wkflRule.NextStatus.IsCountersign.Value;
                }
            }

            //当前具体工作流的已完成操作
            var concretelist = concreteWorkflowOperationSvc.GetConcreteWorkflowOperations(concreteId).ToList();

            //在工作流已完成操作中查找状态与当前处理规则下一节点状态编号相同的操作条目
            var preOperation = concretelist.FirstOrDefault(o => o.WorkflowStatusId == wkflRule.NextStatusId);

            //判断会签 (多人操作)
            if (wkflRule.Status.IsCountersign.HasValue && wkflRule.Status.IsCountersign.Value)
            {
                //会签操作记录
                var countersignOperations = concreteWorkflowOperationSvc.GetOneConcreteWorkflowOperationsByStatus(concreteId, wkflRule.StatusId);
                if (countersignOperations.Count() > 1 &&
                    concreWorkflow.FormForWorkflow.WorkflowFormType != WorkflowFormType.Report) //报表不判断会签
                {
                    preOperation =
                        concretelist.Where(o => o.WorkflowStatusId != wkflRule.StatusId && o.WorkflowStatusId == wkflRule.NextStatusId)
                                    .OrderBy(x => x.OperatedTime)
                                    .LastOrDefault(); //回避会签不返回发起人情况
                }
            }

            //具有处理下一节点的权限的用户
            IEnumerable<User> nextAllowUsers = workflowRuleService.GetRuleUsers(wkflRule.WorkflowRuleId).ToList(); ;

            //获取发起人 (过滤和默认时需要的参数) by kelsey 2014-07-17
            var startOperation = concretelist.FirstOrDefault(k => k.WorkflowStatus.IdForTheWorkflow == 1);
            User startUser = new User();
            if (startOperation != null)
            {
                startUser = startOperation.UserObj;
            }
            //according to RoleCondition to filter  By:kelsey 2013-10-09  删除之前的判断代码 如出现不对从后台进行配置
            if (!string.IsNullOrEmpty(wkflRule.RoleConditions))
            {
                try
                {
                    //var express = System.Linq.Dynamic.DynamicExpression.ParseLambda<User, bool>(wkflRule.RoleConditions, this.CurrentUser);
     
                    var express = System.Linq.Dynamic.DynamicExpression.ParseLambda<User, bool>(wkflRule.RoleConditions, new[] { this.CurrentUser, startUser });
                    nextAllowUsers = nextAllowUsers.Where(express.Compile()).ToList();
                }
                catch (Exception)
                {

                }
            }


            resultUserData = nextAllowUsers.ToList();

            //回退操作,直接绑定上一操作人到控件
            if (preOperation != null && !multiple)
            {
                bindUser = true;
                lockUser = true;
                defaultUserName = preOperation.UserName;
                if (resultUserData.All(u => u.UserName != preOperation.UserName))
                {
                    resultUserData.Add(securityService.GetUser(preOperation.UserName));
                }
            }

            //according to WorkflowRuleDefaults to set DefaultUser
            var def = workflowRuleDefaultService.GetMany(k => k.UserName == this.CurrentUser.UserName
                && k.StatusID == concreWorkflow.CurrentStatusId && wkflRule.IsSequence).ToList();
            if (def.Any() && resultUserData.Any(k => k.UserName == def.LastOrDefault().DefaultUserName))
            {
                bindUser = true;
                defaultUserName = def.LastOrDefault().DefaultUserName;
            }

            //according to RoleDefaultConditions to set Default   By:kelsey 2013-10-09 
            //wkflRule.RoleDefaultConditions = "decode(BaseContractTypeIndex,10,'admin;zhangsl',20,'zhangsl')";
            if (!bindUser && !string.IsNullOrEmpty(wkflRule.RoleDefaultConditions))
            {
                if (wkflRule.RoleDefaultConditions.ToLower().Contains("decode("))
                {
                    var _temp = concreWorkflow.FormForWorkflow.EditUrl.Split('/');
                    string controllerName = _temp[_temp.Length - 2];
                    var BussinessModel = ResultMethod(controllerName, "MatchConditions", new object[] { concreteId });
                    Regex reg2 = new Regex(@"(?<=\()(.*?)+(?=\))");
                    MatchCollection mc2 = reg2.Matches(wkflRule.RoleDefaultConditions);
                    var p = Expression.Parameter(BussinessModel.GetType(), "r");
                    var splitTemp = mc2[0].Value.Split(',');
                    for (int i = 1; i < splitTemp.Length; i++)
                    {
                        var e = System.Linq.Dynamic.DynamicExpression.ParseLambda(new[] { p }, null,
                           "r." + splitTemp[0] + "==" + splitTemp[i]);
                        var result = e.Compile().DynamicInvoke(BussinessModel);
                        if (Boolean.Parse(result.ToString()))
                        {
                            defaultUserName = splitTemp[i + 1];
                            bindUser = true;
                            break;
                        }
                    }
                }
                if (wkflRule.RoleDefaultConditions.Contains("@0.StockTakeDeptManager"))
                {
                    if (CurrentUser.Department.Company.Name == "上海浦东软件园股份有限公司")//非参控股企业
                    {
                        var depdId = CurrentUser.Department.DeptID.ToString();
                        var assetsManager =
                            _assetsManagerService.GetMany(
                                o =>
                                    o.ReceiveDeptIds.Contains(depdId) &&
                                    o.AssetsManagerUser.Department.CompanyID == CurrentUser.Department.CompanyID).FirstOrDefault();
                        if (assetsManager != null)
                            defaultUserName = assetsManager.StockTakeDeptManager;
                    }
                    else//参控股公司
                    {
                        var companyId = CurrentUser.Department.CompanyID.ToString();
                        var assetsManager =
                            _assetsManagerService.GetMany(
                                o =>
                                    o.ReceiveDeptIds.Contains(companyId) &&
                                    o.AssetsManagerUser.Department.CompanyID == CurrentUser.Department.CompanyID).FirstOrDefault();
                        if (assetsManager != null)
                            defaultUserName = assetsManager.StockTakeDeptManager;
                    }
                    bindUser = true;
                }
                else
                {
                    var leader = this.CurrentUser.EnterpriseManager;
                    var express =
                        System.Linq.Dynamic.DynamicExpression.ParseLambda<User, bool>(
                            wkflRule.RoleDefaultConditions, new object[] { leader, this.CurrentUser, startUser }); // add 当前人和发起人参数。by kelsey 2014-07-17
                    try
                    {
                        var NextUsers = nextAllowUsers.Where(express.Compile());
                        if (multiple) //会签默认多个用户 2014-07-15
                        {
                            defaultUserName = NextUsers.Select(k => k.UserName).Aggregate((a, b) => a + ";" + b);
                        }
                        else
                        {
                            defaultUserName = NextUsers.FirstOrDefault().UserName;
                        }
                        bindUser = true;
                    }
                    catch (Exception)
                    {

                    }
                }
            }

            #region 默认选中第一个人

            if (!bindUser && !multiple)
            {
                bindUser = true;
                defaultUserName = nextAllowUsers.Select(u => u.UserName).FirstOrDefault();
                defaultUserName = defaultUserName ?? "";
            }
            #endregion

            #region 特殊处理—— by Jiangd
            //绑定发起人
            if (wkflRule.IsToStartUser)
            {
                bindUser = true;
                //lockUser = true;
                defaultUserName = concreteWorkflowSvc.GetConcreteWorkflow(concreteId).ConcreteWorkflowOperations
                                              .OrderBy(c => c.ConcreteWorkflowOperationId).FirstOrDefault().UserName;
                var user = securityService.GetUser(defaultUserName);
                if (user != null)
                    resultUserData.Add(user);
            }


            //IT技术服务流程单,绑定报修人
            if (wkflRule.Workflow.Name == "IT技术服务流程单")
            {
                if (wkflRule.Name == "处理完成提交报修人确认")
                {
                    bindUser = true;
                    //lockUser = true;
                    defaultUserName =
                        iTTechnicalServiceProcessService.GetITTechnicalServiceProcessByConcreteID(concreteId)
                                                        .StartUserName;
                    var user = securityService.GetUser(defaultUserName);
                    if (user != null)
                        resultUserData.Add(user);
                }
            }

            //三全系统问题处理单,绑定报修人
            if (wkflRule.Workflow.Name == "三全系统问题处理单")
            {
                if (wkflRule.Name == "处理完成提交报修人确认")
                {
                    bindUser = true;
                    //lockUser = true;
                    defaultUserName =
                        eispProblemProcessService.GetEispProblemProcessByConcreteID(concreteId)
                                                        .StartUserName;
                    var user = securityService.GetUser(defaultUserName);
                    if (user != null)
                        resultUserData.Add(user);
                }
            }

            //人员结构、职工薪酬呈送发展计划部审核和复核时，默认人为王艳艳  #8387
            var concreteWorkflow = concreteWorkflowSvc.GetConcreteWorkflow(concreteId);
            if (concreteWorkflow.FormForWorkflow.Name == "人员结构情况月报" || concreteWorkflow.FormForWorkflow.Name == "职工薪酬情况月报")
            {
                if (wkflRule.Name == "呈送发展计划部" || wkflRule.Name == "审核通过")
                {
                    bindUser = true;
                    //lockUser = true;
                    defaultUserName = "wangyy";
                    //var user = securityService.GetUser(userName);
                    //if (user != null)
                    //    userData.Add(user);
                }
            }

            #endregion

            sbUsers = GetUserTree.GetUsers(companyList, deptList, resultUserData);
            contentReuslt = this.Content(sbUsers);

            return Json(new { BindUser = bindUser, LockUser = lockUser, UserName = defaultUserName.Trim(';'), Tree = contentReuslt, IsMultiple = multiple });
        }


        /// <summary>
        /// 草稿箱
        /// </summary>
        /// <param name="id"></param>
        /// <param name="url"></param>
        /// <returns></returns>
        public ActionResult Draft(int id, string url)
        {
            ViewBag.detailUrl = url;
            return PartialView(id);
        }

        public ActionResult DraftJson(int id, int page = 1, int rows = 20)
        {
            var concreteWorkflows = concreteWorkflowSvc.GetDraftConcreteWorkflows(this.CurrentUser, id);
            var count = concreteWorkflows.Count();
            var tt = concreteWorkflows.Skip((page - 1) * rows).Take(rows);
            var conWfViewModelList = (from c in tt
                                      select c.Dto(this.CurrentUser.UserName)).ToList();
            return Json(new { total = count, rows = conWfViewModelList });
        }

        /// <summary>
        /// 暂停箱
        /// </summary>
        /// <param name="id"></param>
        /// <param name="url"></param>
        /// <returns></returns>
        public ActionResult Suspend(int id, string url)
        {
            ViewBag.detailUrl = url;
            return PartialView(id);
        }

        public ActionResult SuspendJson(int id, int page = 1, int rows = 20)
        {
            var concreteWorkflows = concreteWorkflowSvc.GetSuspendConcreteWorkflows(this.CurrentUser, id);
            var count = concreteWorkflows.Count();
            var tt = concreteWorkflows.Skip((page - 1) * rows).Take(rows);
            var conWfViewModelList = (from c in tt
                                      select c.Dto(this.CurrentUser.UserName)).ToList();
            return Json(new { total = count, rows = conWfViewModelList });
        }

        /// <summary>
        /// 暂停功能
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ActionResult ToSuspend(int id, string type)
        {
            concreteWorkflowSvc.ToSuspend(id);
            return RedirectToAction("ContractSuspend", "Home", new { area = type });
        }

        /// <summary>
        /// 错发回收
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ActionResult Retrieve(int id)
        {
            return PartialView(id);
        }

        public ActionResult RetrieveJson(int id)
        {
            var concreteWorkflows = concreteWorkflowSvc.GetWrongRetrieveConcreteWorkflows(this.CurrentUser, id);
            var conWfViewModelList = (from c in concreteWorkflows
                                      select c.Dto()).ToList();
            return Json(new { total = conWfViewModelList.Count(), rows = conWfViewModelList });
        }

        /// <summary>
        /// 回收功能
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ActionResult ToRetrieve(int id)
        {
            concreteWorkflowSvc.ToRetrieve(id, this.CurrentUser);
            return Json("123");
        }

        /// <summary>
        /// 已阅功能
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ActionResult HasRead(int id, string type)
        {
            passRoundForPerusalService.HasRead(id, this.CurrentUser, "已阅(默认)");
            return RedirectToAction("ContractToDo", "Home", new { area = type });

        }

        /// <summary>
        /// 发起传阅功能
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ActionResult Circulate(string userName, int ConcreteID)
        {
            if (passRoundForPerusalService.Circulate(userName, ConcreteID))
                return this.Content(Msg("true", "已传阅！"));
            else
                return this.Content(Msg("false", "传阅失败"));
        }

        /// <summary>
        /// 经办
        /// </summary>
        /// <param name="id"></param>
        /// <param name="url"></param>
        /// <returns></returns>
        [HttpGet]
        public ActionResult HasDone(int id, string url)
        {
            ViewBag.detailUrl = url;
            ViewBag.ID = id;
            return PartialView(new HasDoneSearchViewModel());
        }

        [HttpPost]
        public ActionResult HasDone(HasDoneSearchViewModel param)
        {
            var cwf = concreteWorkflowSvc.GetHasDoneByParam(this.CurrentUser, param);
            var lq = (from c in cwf.Result
                      select c.Dto()).ToList();
            return Json(new { total = cwf.Count, rows = lq });
        }
        /// <summary>
        /// 客户经办（关联客户的经办查询）
        /// </summary>
        /// <param name="id"></param>
        /// <param name="url"></param>
        /// <returns></returns>
        [HttpGet]
        public ActionResult HasDoneTenant(int id, string url)
        {
            ViewBag.detailUrl = url;
            ViewBag.ID = id;
            return PartialView(new HasDoneSearchTenantViewModel());
        }

        [HttpPost]
        public ActionResult HasDoneTenant(FormCollection formCollection)
        {
            var queryParams = base.GetQueryParams<HasDoneSearchTenant>();
            hasDoneSearchTenantService.Sql = GetSqlText(SqlConst.Sql_HasDoneTenant);

            var cwf = hasDoneSearchTenantService.QueryListByPage(FormCollectionToQueryExpression(formCollection), queryParams,
                                                                    n => n.CreatedDate, isSqlServiceSide: true);

            var lq = (from c in cwf.Results
                      select c.Dto()).ToList();
            return Json(new { total = cwf.RowCount, rows = lq });
        }

        /// <summary>
        /// 经办查询-业务类型
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public ActionResult GeFormForWorkflowList(int id, bool isAll = true)
        {
            var typeList = forWorkflowSvc.GetFormForWorkflowsByWorkflowFormType(id);
            var indexTree = new List<EasyUITreeViewModel>();
            if (isAll)
            {
                indexTree.Add(new EasyUITreeViewModel()
                    {
                        Id = "-" + id,
                        Text = "所有",
                        IsChecked = true
                    });
            }
            foreach (FormForWorkflow s in typeList)
            {
                indexTree.Add(new EasyUITreeViewModel()
                {
                    Id = s.FormForWorkflowId.ToString(),
                    Text = s.Name
                });
            }
            return Json(indexTree);
        }

        public ActionResult WorkDelegate(int id)
        {
            return PartialView(id);
        }

        /// <summary>
        /// 获取委托页面
        /// </summary>
        /// <param name="flag"></param>
        /// <returns></returns>
        public ActionResult MyDelegate(int id, bool flag)
        {
            ViewBag.Type = id;
            ViewBag.flag = flag;
            if (flag)
            {
                var delegateSettings = delegateSettingService.GetDelegateSettingByClient(this.CurrentUser, id).OrderByDescending(x => x.CreateTime);
                return View(delegateSettings);
            }
            else
            {
                var delegateSettings = delegateSettingService.GetDelegateSettingByMandatary(this.CurrentUser, id).OrderByDescending(x => x.CreateTime);
                return View(delegateSettings);
            }
        }

        /// <summary>
        /// 所有流程
        /// </summary>
        /// <returns></returns>
        public ActionResult ConcreteWorkflowByType(int type, string url)
        {
            ViewBag.detailUrl = url;

            ViewBag.ID = type;
            return PartialView(new HasDoneSearchViewModel());
        }

        public ActionResult GetConcreteWorkflowByType(HasDoneSearchViewModel param)
        {
            var cwf = concreteWorkflowSvc.GetAllFlowByParam(param);
            var lq = (from c in cwf.Result
                      select c.Dto()).ToList();
            return Json(new { total = cwf.Count, rows = lq });
        }


        private IList<QueryExpression> GetDoneSearchExpressionList(HasDoneSearchViewModel mode)
        {
            FormCollection fc = new FormCollection();
            fc.Add("Title", mode.Title ?? "");
            fc.Add("Type", mode.Type ?? "");
            fc.Add("CreatedDate_begin", mode.CreatedDate_begin.ToString());
            fc.Add("CreatedDate_end", mode.CreatedDate_end.ToString());
            fc.Add("UsedUser", mode.UsedUser ?? "");
            fc.Add("Creator", mode.Creator ?? "");
            fc.Add("OperateUser", mode.OperateUser ?? "");
            fc.Add("Status", mode.Status.ToString());
            fc.Add("FinishDate_begin", mode.FinishDate_begin.ToString());
            fc.Add("FinishDate_end", mode.FinishDate_end.ToString());
            return FormCollectionToQueryExpression(fc);
        }

        /// <summary>
        /// 常用联系人
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public JsonResult GetFrequentContact(int id)
        {
            var FrequentContact = workflowRuleService.GetWorkflowRule(id).FrequentContact ?? "";
            var indexTree = new List<EasyUITreeViewModel>();
            string userName = null;
            foreach (var s in FrequentContact.Split(','))
            {
                var user = securityService.GetUser(s);
                if (user != null)
                {
                    userName = userName ?? user.UserName;
                    indexTree.Add(new EasyUITreeViewModel()
                    {
                        Id = user.UserName,
                        Text = user.DisplayName
                    });
                }
            }
            return Json(new { BindUser = true, LockUser = false, UserName = userName, Tree = this.Content(JsonConvert.SerializeObject(indexTree)), IsMultiple = false });
        }

        public JsonResult HasRight(int id)
        {
            var _WorkflowFormType = concreteWorkflowSvc.GetConcreteWorkflow(id).FormForWorkflow.WorkflowFormTypeValue;
            var TODOconcreteWorkflows = concreteWorkflowSvc.GetToDoConcreteWorkflowsByID(this.CurrentUser, id, _WorkflowFormType);
            var result = TODOconcreteWorkflows.Any();
            return Json(new { Success = result });
        }

        public JsonResult ActiveWF(int id)
        {
            try
            {
                var Model = concreteWorkflowOperationSvc.GetConcreteWorkflowOperation(id);
                Model.IsActive = true;
                Model.ConcreteWorkflow.CurrentStatusId = Model.WorkflowStatusId;
                concreteWorkflowOperationSvc.UpdateConcreteWorkflowOperation(Model);
                return Json(new { Success = true, message = "激活成功" });
            }
            catch
            {
                return Json(new { Success = false, message = "激活失败" });
            }
        }

        public JsonResult SaveSuggest(int id, string content)
        {
            try
            {
                var Model = concreteWorkflowOperationSvc.GetConcreteWorkflowOperation(id);
                Model.Content = content;
                concreteWorkflowOperationSvc.UpdateConcreteWorkflowOperation(Model);
                return Json(new { Success = true, message = "更新成功" });
            }
            catch
            {
                return Json(new { Success = false, message = "更新失败" });
            }
        }
    }
}
