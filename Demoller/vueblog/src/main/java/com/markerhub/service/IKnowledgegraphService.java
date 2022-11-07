package com.markerhub.service;


import com.markerhub.entity.*;
import com.markerhub.entity.Process;

import java.util.List;
import java.util.Map;

public interface IKnowledgegraphService {
	List<Map<String,Object>> getDomains();
	List<Map<String,Object>> getDomainList(String domainname,String createuser, int sserviceId);
	void saveDomain(Map<String, Object> map);
	void updateDomain(Map<String, Object> map);
	void deleteDomain(Integer id);
	List<Map<String,Object>> getDomainByName(String domainname);
	List<Map<String,Object>> getDomainById(Integer domainid);
	void saveNodeImage(List<Map<String, Object>> mapList);
	void saveNodeContent(Map<String, Object> map);
	void updateNodeContent(Map<String, Object> map);
	List<Map<String,Object>> getNodeImageList(Integer domainid,Integer nodeid);
	List<Map<String,Object>> getNodeContent(Integer domainid,Integer nodeid);
	void deleteNodeImage(Integer domainid,Integer nodeid);

	void createStatus(Status status);
	List<Map<String,Object>> getStatusList(int domainId, int nodeId);
	void updateStatus(Status status);
	void deleteStatus(String statusId);

	void createOrder(Order order);
	List<Map<String,Object>> getOrderList(int domainId, int nodeId);
	void updateOrder(Order order);
	void deleteOrder(String orderId);

	void createAction(Action action);
	void createParams(Param param);
	void createRes(Response res);
	List<Map<String,Object>> getActionList(int domainid, int nodeid);
	List<Map<String,Object>> getParamListByActionId(String actionId);
	List<String> getCodeListByActionId(String actionId);
	void updateAction(Action action);
	void updateParam(Param param);
	void deleteAction(String actionId);
	void deleteAllResByActionId(String actionId);
	void deleteAllParamsByActionId(String actionId);

	void createWord(Word word);
	List<Map<String,Object>> getWordList(int domainId);
	void deleteWord(String wordId);
	void deleteWordBydomainId(Integer domainId);
	void updateWord(Word word);

	List<Map<String,Object>> getAllMsg(int domainId, int sserviceId);
	List<Map<String,Object>> getMsg(int sserviceId);
	List<Map<String,Object>> getDomainsBySsId(int sserviceId);
	List<Map<String,Object>> getAllActionByDomainId(int domainId);
	void createMsg(Msg msg);
	void deleteMsg(String msgId);
	void deleteMsgbydomainId(Integer domainId);
	void updateMsg(Msg msg);
	void updateDomain2(Domain domain);
	Map<String, Object> getDomain(int domainId);

	void addProcess(Process process);
	List<Map<String,Object>> getProcessList(int domainId, int nodeId);
	boolean isProcessExist(String processId);
	void updateProcess(Process process);

	void deleteProcess(String processId);

	List<Map<String,Object>> getStepList(int domainId, int nodeId);
	void addStep(Step step);
	void updateStep(Step step);
	void deleteStep(String stepId);

	List<Map<String,Object>> getStepTag(int domainId, int nodeId);
	void addStepTag(StepTag stepTag);
	void deleteStepTag(String stepTagId);
	void deleteProcessAndTag(Integer nodeId, Integer stepNum);

	void saveMxGraph(MxGraph mxGraph);
	void deleteMxGraphBySserviceId(int sserviceId);
	String getMxGraph(Integer sserviceId);

	void saveStateFlow(StateFlow stateFlow);
	void deleteStateFlowByNodeIdAndDomainId(Integer domainId, Integer nodeId, String mode);
	String getStateFlow(Integer domainId, Integer nodeId, String mode);

	List<Map<String,Object>> getStateActionList(int domainId, int nodeId, String stateName, String mode);
	void addStateAction(StateAction stateAction);
	void updateStateAction(StateAction stateAction);
	void deleteStateAction(String stateActionId);
	void updateStateActionName(String newName, String oldName, int domainId, int nodeId);
	void deleteParamByParamId(String paramId);

	List<Map<String,Object>> getUserAttrList(int domainId, int nodeId);
	String getUserAttr(String attrLabel);
	void updateUserAttr(UserAttr userAttr);
	boolean isUserAttrExist(String attrId);
	void createUserAttr(UserAttr userAttr);
	void deleteUserAttrById(String attrId);

}
