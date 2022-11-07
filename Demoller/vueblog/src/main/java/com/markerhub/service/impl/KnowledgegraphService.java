package com.markerhub.service.impl;

import com.markerhub.entity.*;
import com.markerhub.entity.Process;
import com.markerhub.mapper.knowledgeGraphMapper;
import com.markerhub.service.IKnowledgegraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KnowledgegraphService implements IKnowledgegraphService {

    @Autowired
    private knowledgeGraphMapper knowledgeGraphMapper;

    @Override
    public List<Map<String, Object>> getDomainList(String domainname, String createuser, int sserviceId) {
        List<Map<String, Object>> list = knowledgeGraphMapper.getDomainList(domainname, createuser, sserviceId);
        return list;
    }

    @Override
    public void saveDomain(Map<String, Object> map) {
        knowledgeGraphMapper.saveDomain(map);
    }

    @Override
    public void updateDomain(Map<String, Object> map) {
        knowledgeGraphMapper.updateDomain(map);
    }

    @Override
    public void deleteDomain(Integer id) {
        knowledgeGraphMapper.deleteDomain(id);
    }

    @Override
    public List<Map<String, Object>> getDomainByName(String domainname) {
        System.out.println(domainname);
        return knowledgeGraphMapper.getDomainByName(domainname);
    }

    @Override
    public List<Map<String, Object>> getDomains() {
        return knowledgeGraphMapper.getDomains();
    }

    @Override
    public List<Map<String, Object>> getDomainById(Integer domainid) {
        return knowledgeGraphMapper.getDomainById(domainid);
    }

    @Override
    public void saveNodeImage(List<Map<String, Object>> mapList) {
        knowledgeGraphMapper.saveNodeImage(mapList);
    }

    @Override
    public void saveNodeContent(Map<String, Object> map) {
        knowledgeGraphMapper.saveNodeContent(map);
    }

    @Override
    public void updateNodeContent(Map<String, Object> map) {
        knowledgeGraphMapper.updateNodeContent(map);
    }

    @Override
    public List<Map<String, Object>> getNodeImageList(Integer domainid, Integer nodeid) {
        return knowledgeGraphMapper.getNodeImageList(domainid, nodeid);
    }

    @Override
    public List<Map<String, Object>> getNodeContent(Integer domainid, Integer nodeid) {
        return knowledgeGraphMapper.getNodeContent(domainid, nodeid);
    }

    @Override
    public void deleteNodeImage(Integer domainid, Integer nodeid) {
        knowledgeGraphMapper.deleteNodeImage(domainid, nodeid);
    }

    @Override
    public void createStatus(Status status) {
        knowledgeGraphMapper.createStatus(status);
    }

    @Override
    public List<Map<String, Object>> getStatusList(int domainId, int nodeId) {
        return knowledgeGraphMapper.getStatusList(domainId, nodeId);
    }

    @Override
    public void updateStatus(Status status) {
        knowledgeGraphMapper.updateStatus(status);
    }

    @Override
    public void deleteStatus(String statusId) {
        knowledgeGraphMapper.deleteStatus(statusId);
    }

    @Override
    public void createOrder(Order order) {
        knowledgeGraphMapper.createOrder(order);
    }

    @Override
    public List<Map<String, Object>> getOrderList(int domainId, int nodeId) {
        return knowledgeGraphMapper.getOrderList(domainId, nodeId);
    }

    @Override
    public void updateOrder(Order order) {
        knowledgeGraphMapper.updateOrder(order);
    }

    @Override
    public void deleteOrder(String orderId) {
        knowledgeGraphMapper.deleteOrder(orderId);
    }

    @Override
    public void createAction(Action action) {
        knowledgeGraphMapper.createAction(action);
    }

    @Override
    public void createParams(Param param) {
        knowledgeGraphMapper.createParam(param);
    }

    @Override
    public void createRes(Response res) {
        knowledgeGraphMapper.createRes(res);
    }

    @Override
    public List<Map<String, Object>> getActionList(int domainId, int nodeId) {
        return knowledgeGraphMapper.getActionList(domainId, nodeId);
    }

    @Override
    public List<Map<String, Object>> getParamListByActionId(String actionId) {
        return knowledgeGraphMapper.getParamListByActionId(actionId);
    }

    @Override
    public List<String> getCodeListByActionId(String actionId) {
        return knowledgeGraphMapper.getCodeListByActionId(actionId);
    }

    @Override
    public void updateAction(Action action) {
        knowledgeGraphMapper.updateAction(action);
    }

    @Override
    public void updateParam(Param param) {
        knowledgeGraphMapper.updateParam(param);
    }

    @Override
    public void deleteAllResByActionId(String actionId) {
        knowledgeGraphMapper.deleteAllResByActionId(actionId);
    }

    @Override
    public void deleteAction(String actionId) {
        knowledgeGraphMapper.deleteAction(actionId);
    }

    @Override
    public void deleteAllParamsByActionId(String actionId) {
        knowledgeGraphMapper.deleteAllParamsByActionId(actionId);
    }

    @Override
    public void createWord(Word word) {
        knowledgeGraphMapper.createWord(word);
    }

    @Override
    public List<Map<String, Object>> getWordList(int domainId) {
        return knowledgeGraphMapper.getWordList(domainId);
    }

    @Override
    public void deleteWord(String wordId) {
        knowledgeGraphMapper.deleteWord(wordId);
    }

    @Override
    public void deleteWordBydomainId(Integer domainId) {
        knowledgeGraphMapper.deleteWordBydomainId(domainId);
    }

    @Override
    public void updateWord(Word word) {
        knowledgeGraphMapper.updateWord(word);

    }

    @Override
    public List<Map<String, Object>> getAllMsg(int domainId, int sserviceId) {
        return knowledgeGraphMapper.getAllMsg(domainId, sserviceId);
    }

    @Override
    public List<Map<String, Object>> getMsg(int sserviceId) {
        return knowledgeGraphMapper.getMsg(sserviceId);
    }

    @Override
    public List<Map<String, Object>> getDomainsBySsId(int sserviceId) {
        return knowledgeGraphMapper.getDomainsBySsId(sserviceId);
    }

    @Override
    public List<Map<String, Object>> getAllActionByDomainId(int domainId) {
        return knowledgeGraphMapper.getAllActionByDomainId(domainId);
    }

    @Override
    public void createMsg(Msg msg) {
        knowledgeGraphMapper.createMsg(msg);
    }

    @Override
    public void deleteMsg(String msgId) {
        knowledgeGraphMapper.deleteMsg(msgId);
    }

    @Override
    public void deleteMsgbydomainId(Integer domainId) {
        knowledgeGraphMapper.deleteMsgbydomainId(domainId);
    }

    @Override
    public void updateMsg(Msg msg) {
        knowledgeGraphMapper.updateMsg(msg);
    }

    @Override
    public void updateDomain2(Domain domain) {
        knowledgeGraphMapper.updateDomain2(domain);
    }

    @Override
    public Map<String, Object> getDomain(int domainId) {
        return knowledgeGraphMapper.getDomain(domainId);
    }

    @Override
    public void addProcess(Process process) {
        knowledgeGraphMapper.addProcess(process);
    }

    @Override
    public List<Map<String, Object>> getProcessList(int domainId, int nodeId) {
        return knowledgeGraphMapper.getProcessList(domainId, nodeId);
    }

    @Override
    public boolean isProcessExist(String processId) {
        if (knowledgeGraphMapper.isProcessExist(processId).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateProcess(Process process) {
        knowledgeGraphMapper.updateProcess(process);
    }

    @Override
    public void deleteProcess(String processId) {
        knowledgeGraphMapper.deleteProcess(processId);
    }

    @Override
    public List<Map<String, Object>> getStepList(int domainId, int nodeId) {
        return knowledgeGraphMapper.getStepList(domainId, nodeId);
    }

    @Override
    public void addStep(Step step) {
        knowledgeGraphMapper.addStep(step);
    }

    @Override
    public void updateStep(Step step) {
        knowledgeGraphMapper.updateStep(step);
    }

    @Override
    public void deleteStep(String stepId) {
        knowledgeGraphMapper.deleteStep(stepId);
    }

    @Override
    public List<Map<String, Object>> getStepTag(int domainId, int nodeId) {
        return knowledgeGraphMapper.getStepTag(domainId, nodeId);
    }

    @Override
    public void addStepTag(StepTag stepTag) {
        knowledgeGraphMapper.addStepTag(stepTag);
    }


    @Override
    public void deleteStepTag(String stepTagId) {
        knowledgeGraphMapper.deleteStepTag(stepTagId);
    }

    @Override
    public void deleteProcessAndTag(Integer nodeId, Integer stepNum) {
        knowledgeGraphMapper.deleteProcessByStepNum(nodeId, stepNum);
        knowledgeGraphMapper.deleteStepTagByStepNum(nodeId, stepNum);
    }

    @Override
    public void saveMxGraph(MxGraph mxGraph) {
        knowledgeGraphMapper.saveMxGraph(mxGraph);
    }

    @Override
    public void deleteMxGraphBySserviceId(int sserviceId) {
        knowledgeGraphMapper.deleteMxGraphBySserviceId(sserviceId);
    }

    @Override
    public String getMxGraph(Integer sserviceId) {
        return knowledgeGraphMapper.getMxGraph(sserviceId);
    }


    @Override
    public void saveStateFlow(StateFlow stateFlow) {
        knowledgeGraphMapper.saveStateFlow(stateFlow);
    }

    @Override
    public void deleteStateFlowByNodeIdAndDomainId(Integer domainId, Integer nodeId, String mode) {
        knowledgeGraphMapper.deleteStateFlowByNodeIdAndDomainId(domainId, nodeId, mode);
    }

    @Override
    public String getStateFlow(Integer domainId, Integer nodeId, String mode) {
        return knowledgeGraphMapper.getStateFlow(domainId, nodeId, mode);
    }


    @Override
    public List<Map<String, Object>> getStateActionList(int domainId, int nodeId, String stateName, String mode) {
        return knowledgeGraphMapper.getStateActionList(domainId, nodeId, stateName, mode);
    }

    @Override
    public void addStateAction(StateAction stateAction) {
        knowledgeGraphMapper.addStateAction(stateAction);
    }

    @Override
    public void updateStateAction(StateAction stateAction) {
        knowledgeGraphMapper.updateStateAction(stateAction);
    }

    @Override
    public void deleteStateAction(String stateActionId) {
        knowledgeGraphMapper.deleteStateAction(stateActionId);
    }

    @Override
    public void updateStateActionName(String newName, String oldName, int domainId, int nodeId) {
        knowledgeGraphMapper.updateStateActionName(newName, oldName, domainId, nodeId);
    }

    @Override
    public void deleteParamByParamId(String paramId) {
        knowledgeGraphMapper.deleteParamByParamId(paramId);
    }


    @Override
    public List<Map<String, Object>> getUserAttrList(int domainId, int nodeId) {
        return knowledgeGraphMapper.getUserAttrList(domainId, nodeId);
    }

    @Override
    public String getUserAttr(String attrLabel) {
        return knowledgeGraphMapper.getUserAttr(attrLabel);
    }

    @Override
    public void updateUserAttr(UserAttr userAttr) {
        knowledgeGraphMapper.updateUserAttr(userAttr);
    }

    @Override
    public boolean isUserAttrExist(String attrId) {
        if (knowledgeGraphMapper.isUserAttrExist(attrId).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void createUserAttr(UserAttr userAttr) {
        knowledgeGraphMapper.createUserAttr(userAttr);
    }

    @Override
    public void deleteUserAttrById(String attrId) {
        knowledgeGraphMapper.deleteUserAttrById(attrId);
    }
}
