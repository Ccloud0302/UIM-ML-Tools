package com.markerhub.mapper;

import com.markerhub.entity.*;
import com.markerhub.entity.Process;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface knowledgeGraphMapper {
    List<Map<String, Object>> getDomains();

    List<Map<String, Object>> getDomainList(@Param("domainname") String domainname, @Param("createuser") String createuser, @Param("sserviceId") int sserviceId);

    List<Map<String, Object>> getRelationshipList(@Param("domainid") Integer domainid, @Param("relationtype") Integer relationtype, @Param("shipname") String shipname);

    void saveDomain(@Param("params") Map<String, Object> map);

    void updateDomain(@Param("params") Map<String, Object> map);

    void deleteDomain(@Param("id") Integer id);

    List<Map<String, Object>> getDomainByName(@Param("domainname") String domainname);

    List<Map<String, Object>> getDomainById(@Param("domainid") Integer domainid);

    void saveNodeImage(@Param("maplist") List<Map<String, Object>> mapList);

    void saveNodeContent(@Param("params") Map<String, Object> map);

    void updateNodeContent(@Param("params") Map<String, Object> map);

    List<Map<String, Object>> getNodeImageList(@Param("domainid") Integer domainid, @Param("nodeid") Integer nodeid);

    List<Map<String, Object>> getNodeContent(@Param("domainid") Integer domainid, @Param("nodeid") Integer nodeid);

    void deleteNodeImage(@Param("domainid") Integer domainid, @Param("nodeid") Integer nodeid);

    void createStatus(Status status);
    List<Map<String,Object>> getStatusList(int domainId, int nodeId);
    void updateStatus(Status status);
    void deleteStatus(String statusId);

    void createOrder(Order order);
    List<Map<String,Object>> getOrderList(int domainId, int nodeId);
    void updateOrder(Order order);
    void deleteOrder(String orderId);

    void createAction(Action action);
    void createParam(com.markerhub.entity.Param param);
    void createRes(Response res);
    List<Map<String, Object>> getActionList(int domainId, int nodeId);
    List<Map<String,Object>> getParamListByActionId(String actionId);
    List<String> getCodeListByActionId(String actionId);
    void updateAction(Action action);
    void updateParam(com.markerhub.entity.Param param);
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
    List<Map<String,Object>> isProcessExist(String processId);
    void updateProcess(Process process);
    void deleteProcess(String processId);

    List<Map<String,Object>> getStepList(int domainId, int nodeId);
    void addStep(Step step);
    void updateStep(Step step);
    void deleteStep(String stepId);

    List<Map<String,Object>> getStepTag(int domainId, int nodeId);
    void addStepTag(StepTag stepTag);
    void deleteStepTag(String stepTagId);

    void deleteProcessByStepNum(Integer nodeId, Integer stepNum);
    void deleteStepTagByStepNum(Integer nodeId, Integer stepNum);

    void saveMxGraph(MxGraph mxGraph);
    void deleteMxGraphBySserviceId(Integer sserviceId);
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
    List<Map<String,Object>> isUserAttrExist(String attrId);
    void createUserAttr(UserAttr userAttr);
    void deleteUserAttrById(String attrId);

}
