package com.jeecg.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.cgform.entity.upload.CgUploadEntity;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.jeecg.demo.entity.TestSmartCabinetEntity;
import com.jeecg.demo.service.TestSmartCabinetServiceI;

/**
 * @Title: Controller
 * @Description: 测试智能柜基础表
 * @author onlineGenerator
 * @date 2018-09-02 20:26:58
 * @version V1.0
 *
 */
@Controller
//@Api(value = "TestSmartCabinet", description = "订单机票信息", tags = "testSmartCabinetController")
@RequestMapping("/testSmartCabinetController")
public class TestSmartCabinetController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(TestSmartCabinetController.class);

	@Autowired
	private TestSmartCabinetServiceI testSmartCabinetService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;

	/**
	 * 测试智能柜基础表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/demo/testSmartCabinetList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(TestSmartCabinetEntity testSmartCabinet, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TestSmartCabinetEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, testSmartCabinet,
				request.getParameterMap());
		try {
			// 自定义追加查询条件

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.testSmartCabinetService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除测试智能柜基础表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TestSmartCabinetEntity testSmartCabinet, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		testSmartCabinet = systemService.getEntity(TestSmartCabinetEntity.class, testSmartCabinet.getId());
		message = "测试智能柜基础表删除成功";
		try {
			testSmartCabinetService.delete(testSmartCabinet);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "测试智能柜基础表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除测试智能柜基础表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "测试智能柜基础表删除成功";
		try {
			for (String id : ids.split(",")) {
				TestSmartCabinetEntity testSmartCabinet = systemService.getEntity(TestSmartCabinetEntity.class, id);
				testSmartCabinetService.delete(testSmartCabinet);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "测试智能柜基础表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加测试智能柜基础表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TestSmartCabinetEntity testSmartCabinet, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "测试智能柜基础表添加成功";
		try {
			testSmartCabinetService.save(testSmartCabinet);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "测试智能柜基础表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新测试智能柜基础表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TestSmartCabinetEntity testSmartCabinet, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "测试智能柜基础表更新成功";
		TestSmartCabinetEntity t = testSmartCabinetService.get(TestSmartCabinetEntity.class, testSmartCabinet.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(testSmartCabinet, t);
			testSmartCabinetService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "测试智能柜基础表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 测试智能柜基础表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TestSmartCabinetEntity testSmartCabinet, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(testSmartCabinet.getId())) {
			testSmartCabinet = testSmartCabinetService.getEntity(TestSmartCabinetEntity.class,
					testSmartCabinet.getId());
			req.setAttribute("testSmartCabinet", testSmartCabinet);
		}
		return new ModelAndView("com/jeecg/demo/testSmartCabinet-add");
	}

	/**
	 * 测试智能柜基础表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TestSmartCabinetEntity testSmartCabinet, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(testSmartCabinet.getId())) {
			testSmartCabinet = testSmartCabinetService.getEntity(TestSmartCabinetEntity.class,
					testSmartCabinet.getId());
			req.setAttribute("testSmartCabinet", testSmartCabinet);
		}
		return new ModelAndView("com/jeecg/demo/testSmartCabinet-update");
	}

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "testSmartCabinetController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(TestSmartCabinetEntity testSmartCabinet, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(TestSmartCabinetEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, testSmartCabinet,
				request.getParameterMap());
		List<TestSmartCabinetEntity> testSmartCabinets = this.testSmartCabinetService.getListByCriteriaQuery(cq, false);
		modelMap.put(NormalExcelConstants.FILE_NAME, "测试智能柜基础表");
		modelMap.put(NormalExcelConstants.CLASS, TestSmartCabinetEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("测试智能柜基础表列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, testSmartCabinets);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(TestSmartCabinetEntity testSmartCabinet, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "测试智能柜基础表");
		modelMap.put(NormalExcelConstants.CLASS, TestSmartCabinetEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,
				new ExportParams("测试智能柜基础表列表", "导出人:" + ResourceUtil.getSessionUser().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<TestSmartCabinetEntity> listTestSmartCabinetEntitys = ExcelImportUtil
						.importExcel(file.getInputStream(), TestSmartCabinetEntity.class, params);
				for (TestSmartCabinetEntity testSmartCabinet : listTestSmartCabinetEntitys) {
					testSmartCabinetService.save(testSmartCabinet);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(ExceptionUtil.getExceptionMessage(e));
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}

	/**
	 * 获取文件附件信息
	 * 
	 * @param id testSmartCabinet主键id
	 */
	@RequestMapping(params = "getFiles")
	@ResponseBody
	public AjaxJson getFiles(String id) {
		List<CgUploadEntity> uploadBeans = cgFormFieldService.findByProperty(CgUploadEntity.class, "cgformId", id);
		List<Map<String, Object>> files = new ArrayList<Map<String, Object>>(0);
		for (CgUploadEntity b : uploadBeans) {
			String title = b.getAttachmenttitle();// 附件名
			String fileKey = b.getId();// 附件主键
			String path = b.getRealpath();// 附件路径
			String field = b.getCgformField();// 表单中作为附件控件的字段
			Map<String, Object> file = new HashMap<String, Object>();
			file.put("title", title);
			file.put("fileKey", fileKey);
			file.put("path", path);
			file.put("field", field == null ? "" : field);
			files.add(file);
		}
		AjaxJson j = new AjaxJson();
		j.setObj(files);
		return j;
	}

}
