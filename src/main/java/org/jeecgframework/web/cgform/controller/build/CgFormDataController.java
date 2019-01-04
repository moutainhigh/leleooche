package org.jeecgframework.web.cgform.controller.build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import org.jeecgframework.web.cgform.entity.config.CgFormHeadEntity;
import org.jeecgframework.web.cgform.service.autolist.CgTableServiceI;
import org.jeecgframework.web.cgform.service.build.DataBaseService;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.cgform.util.TableJson;
import org.jeecgframework.web.system.enums.InterfaceEnum;
import org.jeecgframework.web.system.pojo.base.InterfaceRuleDto;
import org.jeecgframework.web.system.util.InterfaceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/cgFormDataController")
@Api(value = "online表单服务", description = "online表单接口", tags = "API_onlineForm")
public class CgFormDataController {
	@Autowired
	private DataBaseService dataBaseService;
	@Autowired
	private CgTableServiceI cgTableService;
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;
	/// **签名密钥key*/
	private static final String SIGN_KEY = "26F72780372E84B6CFAED6F7B19139CC47B1912B6CAED753";

	/**
	 * online表单对外接口：getFormInfo 获取表单数据
	 * 
	 * @return
	 */
	// @RequestMapping(params = "getFormInfo")
	@ApiOperation(value = "根据tableName和记录ID获取online表单详细信息", produces = "application/json", httpMethod = "GET")
	@RequestMapping(value = "/get/{tableName}/{id}", method = RequestMethod.GET)
	@ResponseBody
	public TableJson getFormInfo(@PathVariable("tableName") String tableName, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		// Map map = JSONHelper.json2Map(body);
		TableJson j = new TableJson();
		try {
			// 参数校验
			if (oConvertUtils.isEmpty(tableName)) {
				throw new BusinessException("tableName不能为空");
			}
			if (oConvertUtils.isEmpty(id)) {
				throw new BusinessException("id不能为空");
			}
			// 校验该表是否是online表单
			// String tableName = oConvertUtils.getString(map.get("tableName"));
			// String id = oConvertUtils.getString(map.get("id"));
			CgFormHeadEntity head = cgFormFieldService.getCgFormHeadByTableName(tableName);
			if (head == null) {
				throw new BusinessException("该表单不是online表单");
			}

			if (head.getJformType() == 1) {
				// 单表
				j.setTableType(head.getJformType());
				Map<String, Object> dataForm = dataBaseService.findOneForJdbc(tableName, id);
				j.setTableData(dataForm);
			} else if (head.getJformType() == 2) {
				// 主表
				j.setTableType(head.getJformType());
				Map<String, Object> mainForm = dataBaseService.findOneForJdbc(tableName, id);
				j.setTableData(mainForm);
				Map<String, Object> tableData = new HashMap<String, Object>();
				String subTableStr = head.getSubTableStr();
				if (StringUtils.isNotEmpty(subTableStr)) {
					String[] subTables = subTableStr.split(",");
					List<Map<String, Object>> subTableData = new ArrayList<Map<String, Object>>();
					for (String subTable : subTables) {
						subTableData = cgFormFieldService.getSubTableData(tableName, subTable, id);
						tableData.put(subTable, subTableData);
					}
				}
				j.setSubTableDate(tableData);
			} else {
				throw new BusinessException("不支持该类型的表单的操作");
			}
		} catch (BusinessException e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("系统异常");
		}
		return j;
	}

	/**
	 * online表单对外接口：deleteFormInfo 删除表单数据 注意：
	 * 
	 * @return
	 */
	// @RequestMapping(params = "deleteFormInfo")
	@ApiOperation(value = "根据tableName和记录ID删除一条记录")
	@RequestMapping(value = "/delete/{tableName}/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public TableJson deleteFormInfo(@PathVariable("tableName") String tableName, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		// Map map = JSONHelper.json2Map(body);
		TableJson j = new TableJson();
		try {
			// 参数校验
			if (oConvertUtils.isEmpty(tableName)) {
				throw new BusinessException("tableName不能为空");
			}
			if (oConvertUtils.isEmpty(id)) {
				throw new BusinessException("id不能为空");
			}
			// String tableName = oConvertUtils.getString(map.get("tableName"));
			// String id = oConvertUtils.getString(map.get("id"));
			// 校验该表是否是online表单
			CgFormHeadEntity head = cgFormFieldService.getCgFormHeadByTableName(tableName);
			if (head == null) {
				throw new BusinessException("该表单不是online表单");
			}
			if (head.getJformType() != 1 && head.getJformType() != 2) {
				throw new BusinessException("不支持该类型的表单的操作");
			}
			cgTableService.delete(tableName, id);
			j.setMsg("删除成功");
		} catch (BusinessException e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("系统异常");
		}
		return j;
	}

	/**
	 * online表单对外接口：addFormInfo 新增表单数据 注意：
	 * 
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "增加一条记录")
	@ResponseBody
	public TableJson addFormInfo(@RequestBody JSONObject body, HttpServletRequest request,
			HttpServletResponse response) {
		// Map map = JSONHelper.json2Map(body);
		Map map = JSONObject.toJavaObject(body, Map.class);
		TableJson j = new TableJson();
		try {
			// 参数校验
			if (oConvertUtils.isEmpty(map.get("tableName"))) {
				throw new BusinessException("tableName不能为空");
			}
			if (oConvertUtils.isEmpty(map.get("id"))) {
				throw new BusinessException("id不能为空");
			}
			if (map.get("data") == null) {
				throw new BusinessException("data不能为空");
			}
			// 校验该表是否是online表单
			String tableName = oConvertUtils.getString(map.get("tableName"));
			String id = oConvertUtils.getString(map.get("id"));
			Map dataMap = (Map) map.get("data");
			String data = JSONHelper.map2json(dataMap);
			CgFormHeadEntity head = cgFormFieldService.getCgFormHeadByTableName(tableName);
			if (head == null) {
				throw new BusinessException("该表单不是online表单");
			}
			if (head.getJformType() != 1 && head.getJformType() != 2) {
				throw new BusinessException("不支持该类型的表单的操作");
			}
			Map<String, Object> dataForm = dataBaseService.findOneForJdbc(tableName, id);
			if (dataForm != null) {
				throw new BusinessException("表单数据已存在");
			}
			if (head.getJformType() == 1) {
				Map<String, Object> formData;
				try {
					formData = new HashMap<String, Object>();
					formData = JSONHelper.json2Map(data);
				} catch (Exception e) {
					throw new BusinessException("json解析异常");
				}
				formData.put("id", id);
				dataBaseService.insertTable(tableName, formData);
			} else if (head.getJformType() == 2) {
				Map<String, List<Map<String, Object>>> formData;
				try {
					formData = new HashMap<String, List<Map<String, Object>>>();
					formData = JSONHelper.json2MapList(data);
					List<Map<String, Object>> list = formData.get(tableName);
					if (list == null || list.size() <= 0) {
						throw new BusinessException("主表数据异常");
					}
					Map<String, Object> mainMap = list.get(0);
					if (mainMap.get("id") == null || "".equals((String) mainMap.get("id"))) {
						throw new BusinessException("主表数据缺少id");
					}
					if (!id.equals((String) mainMap.get("id"))) {
						throw new BusinessException("id与主表id不一致");
					}
				} catch (Exception e) {
					throw new BusinessException("json解析异常");
				}
				dataBaseService.insertTableMore(formData, tableName);
			}
			j.setMsg("新增表单数据成功");
		} catch (BusinessException e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("系统异常");
		}

		return j;
	}

	/**
	 * online表单对外接口：updateFormInfo 更新表单数据 注意：
	 * 
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "修改一条记录")
	@ResponseBody
	public TableJson updateFormInfo(@RequestBody JSONObject body, HttpServletRequest request,
			HttpServletResponse response) {
		// Map map = JSONHelper.json2Map(body);
		Map map = JSONObject.toJavaObject(body, Map.class);
		TableJson j = new TableJson();
		try {
			// 参数校验
			if (oConvertUtils.isEmpty(map.get("tableName")) || map.get("tableName") == "") {
				throw new BusinessException("tableName不能为空");
			}
			if (oConvertUtils.isEmpty(map.get("id"))) {
				throw new BusinessException("id不能为空");
			}
			if (map.get("data") == null) {
				throw new BusinessException("data不能为空");
			}
			// 校验该表是否是online表单
			String tableName = oConvertUtils.getString(map.get("tableName"));
			String id = oConvertUtils.getString(map.get("id"));
			Map dataMap = (Map) map.get("data");
			String data = JSONHelper.map2json(dataMap);
			CgFormHeadEntity head = cgFormFieldService.getCgFormHeadByTableName(tableName);
			if (head == null) {
				throw new BusinessException("该表单不是online表单");
			}
			if (head.getJformType() != 1 && head.getJformType() != 2) {
				throw new BusinessException("不支持该类型的表单的操作");
			}
			Map<String, Object> dataForm = dataBaseService.findOneForJdbc(tableName, id);
			if (dataForm == null) {
				throw new BusinessException("表单数据不存在");
			}
			if (head.getJformType() == 1) {
				Map<String, Object> formData;
				try {
					formData = new HashMap<String, Object>();
					formData = JSONHelper.json2Map(data);
				} catch (Exception e) {
					throw new BusinessException("json解析异常");
				}
				dataBaseService.updateTable(tableName, id, formData);
			} else if (head.getJformType() == 2) {
				Map<String, List<Map<String, Object>>> formData;
				try {
					formData = new HashMap<String, List<Map<String, Object>>>();
					formData = JSONHelper.json2MapList(data);
					List<Map<String, Object>> list = formData.get(tableName);
					if (list == null || list.size() <= 0) {
						throw new BusinessException("主表数据异常");
					}
					Map<String, Object> mainMap = list.get(0);
					if (mainMap.get("id") == null || "".equals((String) mainMap.get("id"))) {
						throw new BusinessException("主表数据缺少id");
					}
					if (!id.equals((String) mainMap.get("id"))) {
						throw new BusinessException("id与主表id不一致");
					}
				} catch (Exception e) {
					throw new BusinessException("json解析异常");
				}
				dataBaseService.updateTableMore(formData, tableName);
			}
			j.setMsg("更新表单数据成功");
		} catch (BusinessException e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("系统异常");
		}
		return j;
	}

	@RequestMapping(value = "test", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "测试记录")
	@ResponseBody
	public ResponseMessage<?> get(@PathVariable("id") String id, HttpServletRequest request) {
		InterfaceRuleDto interfaceRuleDto = InterfaceUtil.getInterfaceRuleDto(request, InterfaceEnum.intertest_get);
		if (interfaceRuleDto == null) {
			return Result.error("您没有该接口的权限！");
		}
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}

		return Result.success("接口成功");
	}
}
