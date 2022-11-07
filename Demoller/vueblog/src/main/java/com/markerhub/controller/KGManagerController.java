package com.markerhub.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.csvreader.CsvWriter;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.markerhub.config.MultipartFileToFile;
import com.markerhub.config.WebAppConfig;
import com.markerhub.entity.*;
import com.markerhub.entity.Process;
import com.markerhub.query.GraphQuery;
import com.markerhub.service.IKGGraphService;
import com.markerhub.service.IKnowledgegraphService;
import com.markerhub.service.SserviceService;
import com.markerhub.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

@Controller
@RequestMapping(value = "/")
public class KGManagerController extends BaseController {
    @Autowired
    private Neo4jUtil neo4jUtil;
    @Autowired
    private WebAppConfig config;
    @Autowired
    private IKGGraphService KGGraphService;
    @Autowired
    private IKnowledgegraphService kgservice;
    @Autowired
    SserviceService sserviceService;

    @GetMapping("/")
    public String home(Model model) {
        return "kg/home";
    }

    @GetMapping("/kg/index")
    public String index(Model model) {
        return "kg/index";
    }

    @ResponseBody
    @RequestMapping(value = "/getgraph") // call db.labels
    public R<GraphPageRecord<Map<String, Object>>> getgraph(GraphQuery queryItem, int sserviceId) {
        R<GraphPageRecord<Map<String, Object>>> result = new R<GraphPageRecord<Map<String, Object>>>();
        GraphPageRecord<Map<String, Object>> resultRecord = new GraphPageRecord<Map<String, Object>>();
        try {
            String name = "tc";
            PageHelper.startPage(queryItem.getPageIndex(), queryItem.getPageSize(), true);
            List<Map<String, Object>> domainList = kgservice.getDomainList(queryItem.getDomain(), name, sserviceId);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(domainList);
            System.out.println("==============================");
            System.out.println(pageInfo.getList());
            System.out.println("==============================");
            long total = pageInfo.getTotal();
            resultRecord.setPageIndex(queryItem.getPageIndex());
            resultRecord.setPageSize(queryItem.getPageSize());
            resultRecord.setTotalCount(new Long(total).intValue());
            resultRecord.setNodeList(pageInfo.getList());
            result.code = 200;
            result.setData(resultRecord);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getdomainnodes")
    public R<List<HashMap<String, Object>>> getdomainnodes(String domain) {
        R<List<HashMap<String, Object>>> result = new R<List<HashMap<String, Object>>>();
        try {
            List<HashMap<String, Object>> graphNodes = KGGraphService.getdomainnodes(domain);
            System.out.println(graphNodes);
            result.code = 200;
            result.data = graphNodes;

        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getdomaingraph")
    public R<HashMap<String, Object>> getDomainGraph(GraphQuery query) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        try {
            HashMap<String, Object> graphData = KGGraphService.getdomaingraph(query);
            result.code = 200;
            result.data = graphData;

        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getcypherresult")
    public R<HashMap<String, Object>> getcypherresult(String cypher) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        String error = "";
        try {
            HashMap<String, Object> graphData = neo4jUtil.GetGraphNodeAndShip(cypher);
            result.code = 200;
            result.data = graphData;
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            error = e.getMessage();
            result.setMsg("服务器错误");
        } finally {
            if (StringUtil.isNotBlank(error)) {
                result.code = 500;
                result.setMsg(error);
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getrelationnodecount")
    public R<String> getrelationnodecount(String domain, long nodeid) {
        R<String> result = new R<String>();
        try {
            long totalcount = 0;
            if (!StringUtil.isBlank(domain)) {
                totalcount = KGGraphService.getrelationnodecount(domain, nodeid);
                result.code = 200;
                result.setData(String.valueOf(totalcount));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/createdomain")
    public R<String> createdomain(String domain, int sserviceId) {
        R<String> result = new R<String>();
        System.out.println(sserviceId);
        try {
            if (!StringUtil.isBlank(domain)) {
                List<Map<String, Object>> domainItem = kgservice.getDomainByName(domain);
                if (domainItem.size() > 0) {
                    result.code = 300;
                    result.setMsg("领域已存在");
                } else {
                    String name = "tc";
                    Map<String, Object> maps = new HashMap<String, Object>();
                    maps.put("name", domain);
                    maps.put("nodecount", 1);
                    maps.put("shipcount", 0);
                    maps.put("status", 1);
                    maps.put("createuser", name);
                    maps.put("sserviceId", sserviceId);
                    kgservice.saveDomain(maps);// 保存到mysql
                    KGGraphService.createdomain(domain);// 保存到图数据
                    result.code = 200;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getmorerelationnode")
    public R<HashMap<String, Object>> getmorerelationnode(String domain, String nodeid) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        try {
            if (!StringUtil.isBlank(domain)) {
                HashMap<String, Object> graphModel = KGGraphService.getmorerelationnode(domain, nodeid);
                if (graphModel != null) {
                    result.code = 200;
                    result.setData(graphModel);
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updatenodename")
    public R<HashMap<String, Object>> updatenodename(String domain, String nodeid, String nodename) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        HashMap<String, Object> graphNodeList = new HashMap<String, Object>();
        try {
            if (!StringUtil.isBlank(domain)) {
                graphNodeList = KGGraphService.updatenodename(domain, nodeid, nodename);
                if (graphNodeList.size() > 0) {
                    result.code = 200;
                    result.setData(graphNodeList);
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/updateCorrdOfNode")
    public R<String> updateCorrdOfNode(String domain, String uuid, Double fx, Double fy) {
        R<String> result = new R<String>();
        try {
            KGGraphService.updateCorrdOfNode(domain, uuid, fx, fy);
            result.code = 200;
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/createnode")
    public R<HashMap<String, Object>> createnode(QAEntityItem entity, HttpServletRequest request,
                                                 HttpServletResponse response) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        HashMap<String, Object> graphNode = new HashMap<String, Object>();
        System.out.println("entity----------" + entity);
        try {
            String domain = request.getParameter("domain");
            graphNode = KGGraphService.createnode(domain, entity);
            if (graphNode != null && graphNode.size() > 0) {
                result.code = 200;
                result.setData(graphNode);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/batchcreatenode")
    public R<HashMap<String, Object>> batchcreatenode(String domain, String sourcename, String[] targetnames,
                                                      String relation) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        HashMap<String, Object> rss = new HashMap<String, Object>();
        try {
            rss = KGGraphService.batchcreatenode(domain, sourcename, relation, targetnames);
            result.code = 200;
            result.setData(rss);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/batchcreatechildnode")
    public R<HashMap<String, Object>> batchcreatechildnode(String domain, String sourceid, Integer entitytype,
                                                           String[] targetnames, String relation) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        HashMap<String, Object> rss = new HashMap<String, Object>();
        try {
            rss = KGGraphService.batchcreatechildnode(domain, sourceid, entitytype, targetnames, relation);
            result.code = 200;
            result.setData(rss);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/batchcreatesamenode")
    public R<List<HashMap<String, Object>>> batchcreatesamenode(String domain, Integer entitytype,
                                                                String[] sourcenames) {
        R<List<HashMap<String, Object>>> result = new R<List<HashMap<String, Object>>>();
        List<HashMap<String, Object>> rss = new ArrayList<HashMap<String, Object>>();
        try {
            rss = KGGraphService.batchcreatesamenode(domain, entitytype, sourcenames);
            result.code = 200;
            result.setData(rss);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/createlink")
    public R<HashMap<String, Object>> createlink(String domain, long sourceid, long targetid, String ship) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        try {
            HashMap<String, Object> cypherResult = KGGraphService.createlink(domain, sourceid, targetid, ship);
            result.code = 200;
            result.setData(cypherResult);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updatelink")
    public R<HashMap<String, Object>> updatelink(String domain, long shipid, String shipname) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        try {
            HashMap<String, Object> cypherResult = KGGraphService.updatelink(domain, shipid, shipname);
            result.code = 200;
            result.setData(cypherResult);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deletenode")
    public R<List<HashMap<String, Object>>> deletenode(String domain, long nodeid) {
        R<List<HashMap<String, Object>>> result = new R<List<HashMap<String, Object>>>();
        try {
            List<HashMap<String, Object>> rList = KGGraphService.deletenode(domain, nodeid);
            result.code = 200;
            result.setData(rList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deletedomain")
    public R<List<HashMap<String, Object>>> deletedomain(Integer domainid, String domain) {
        R<List<HashMap<String, Object>>> result = new R<List<HashMap<String, Object>>>();
        try {
            kgservice.deleteDomain(domainid);
            KGGraphService.deleteKGdomain(domain);
            kgservice.deleteWordBydomainId(domainid);
            kgservice.deleteMsgbydomainId(domainid);
            result.code = 200;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deletelink")
    public R<HashMap<String, Object>> deletelink(String domain, long shipid) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        try {
            KGGraphService.deletelink(domain, shipid);
            result.code = 200;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/importgraph")
    public JSONObject importgraph(@RequestParam(value = "file", required = true) MultipartFile file,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject res = new JSONObject();
        if (file == null) {
            res.put("code", "500");
            res.put("msg", "请先选择有效的文件");
            return res;
        }
        // 领域不能为空
        String domainId = request.getParameter("domainId");
        if (StringUtil.isBlank(domainId)) {
            res.put("code", "500");
            res.put("msg", "请先选择领域");
            return res;
        }
        System.out.println("开始导入csv...");
        System.out.println(domainId);
        Map<String, Object> domain = kgservice.getDomain(Integer.parseInt(domainId));
        String label = (String) domain.get("name");
        System.out.println(label);

        File file1 = MultipartFileToFile.multipartFileToFile(file);

        InputStreamReader reader = new InputStreamReader(new FileInputStream(file1), "GBK");
        BufferedReader bfreader = new BufferedReader(reader);

//		List<Map<String, Object>> dataList = getFormatData(bfreader);
//        System.out.println(dataList);

        try {
            List<Map<String, Object>> mapList = new ArrayList<>();
            String line;
            while ((line = bfreader.readLine()) != null) {//包含该行内容的字符串，不包含任何行终止符，如果已到达流末尾，则返回 null
//				System.out.println(line);
                String new_line = line.replace("\"", "");
//				String[] strBuff  = new_line.split("\t");
                String[] strBuff = new_line.split(",");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sourcenode", strBuff[0]);
                map.put("targetnode", strBuff[1]);
                map.put("relationship", strBuff[2]);
                mapList.add(map);
            }
//			System.out.println(mapList);
            List<List<String>> list = new ArrayList<>();
            for (Map<String, Object> item : mapList) {
                List<String> lst = new ArrayList<>();
                lst.add(item.get("sourcenode").toString());
                lst.add(item.get("targetnode").toString());
                lst.add(item.get("relationship").toString());
                list.add(lst);
            }
            System.out.println(list);

            String savePath = config.getLocation();
            String filename = "tc" + System.currentTimeMillis() + ".csv";
            CSVUtil.createCsvFile(list, savePath, filename);

//			String serverUrl=request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

            String csvUrl = "file:///" + filename;
//          System.out.println(csvUrl);

            KGGraphService.batchInsertByCSV(label, csvUrl, 0);
            System.out.println("开始导入词汇表");
            for (Map<String, Object> itemMap : mapList) {
                System.out.println(itemMap);
                Word word1 = new Word();
                String sourcenode = (String) itemMap.get("sourcenode");
                word1.setName(sourcenode);
                word1.setDomainId(Integer.parseInt(domainId));
                kgservice.createWord(word1);

                Word word2 = new Word();
                String targetnode = (String) itemMap.get("targetnode");
                word2.setName(targetnode);
                word2.setDomainId(Integer.parseInt(domainId));
                System.out.println(word2);
                kgservice.createWord(word2);
            }

            res.put("code", 200);
            res.put("message", "success!");
            return res;
        } catch (Exception e) {
            res.put("code", 500);
            res.put("message", "服务器错误!");
        }
        return res;
    }

    private List<Map<String, Object>> getFormatData(MultipartFile file) throws Exception {
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            String fileName = file.getOriginalFilename();
            if (!fileName.endsWith(".csv")) {
                Workbook workbook = null;
                if (ExcelUtil.isExcel2007(fileName)) {
                    workbook = new XSSFWorkbook(file.getInputStream());
                } else {
                    workbook = new HSSFWorkbook(file.getInputStream());
                }
                // 有多少个sheet
                int sheets = workbook.getNumberOfSheets();
                for (int i = 0; i < sheets; i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    int rowSize = sheet.getPhysicalNumberOfRows();
                    for (int j = 0; j < rowSize; j++) {
                        Row row = sheet.getRow(j);
                        int cellSize = row.getPhysicalNumberOfCells();
                        if (cellSize != 3) continue; //只读取3列
                        row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                        Cell cell0 = row.getCell(0);//节点1
                        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                        Cell cell1 = row.getCell(1);//节点2
                        row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                        Cell cell2 = row.getCell(2);//关系
                        if (null == cell0 || null == cell1 || null == cell2) {
                            continue;
                        }
                        String sourceNode = cell0.getStringCellValue();
                        String targetNode = cell1.getStringCellValue();
                        String relationShip = cell2.getStringCellValue();
                        if (StringUtil.isBlank(sourceNode) || StringUtils.isBlank(targetNode) || StringUtils.isBlank(relationShip))
                            continue;
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("sourcenode", sourceNode);
                        map.put("targetnode", targetNode);
                        map.put("relationship", relationShip);
                        mapList.add(map);
                    }
                }
            } else if (fileName.endsWith(".csv")) {
                List<List<String>> list = CSVUtil.readCsvFile(file);
                for (int i = 0; i < list.size(); i++) {
                    List<String> lst = list.get(i);
                    if (lst.size() != 3) continue;
                    String sourceNode = lst.get(0);
                    String targetNode = lst.get(1);
                    String relationShip = lst.get(2);
                    if (StringUtil.isBlank(sourceNode) || StringUtils.isBlank(targetNode) || StringUtils.isBlank(relationShip))
                        continue;
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("sourcenode", sourceNode);
                    map.put("targetnode", targetNode);
                    map.put("relationship", relationShip);
                    mapList.add(map);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return mapList;
    }

    @ResponseBody
    @RequestMapping(value = "/exportgraph")
    public JSONObject exportgraph(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject res = new JSONObject();
        String label = request.getParameter("domain");
        String filePath = config.getLocation();
        String fileName = UUID.randomUUID() + ".csv";
        String fileUrl = filePath + File.separator + fileName;
        String cypher = String.format(
                "MATCH (n:%s) -[r]->(m:%s) return n.name as source,m.name as target,r.name as relation", label, label);
        List<HashMap<String, Object>> list = neo4jUtil.GetGraphItem(cypher);
        File file = new File(fileUrl);
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("文件不存在，新建成功！");
            } else {
                System.out.println("文件存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CsvWriter csvWriter = new CsvWriter(fileUrl, ',', Charset.forName("UTF-8"));
        String[] header = {"source", "target", "relation"};
        csvWriter.writeRecord(header);
        for (HashMap<String, Object> hashMap : list) {
            int colSize = hashMap.size();
            String[] cntArr = new String[colSize];
            cntArr[0] = hashMap.get("source").toString().replace("\"", "");
            cntArr[1] = hashMap.get("target").toString().replace("\"", "");
            cntArr[2] = hashMap.get("relation").toString().replace("\"", "");
            try {
                csvWriter.writeRecord(cntArr);
            } catch (IOException e) {
                log.error("CSVUtil->createFile: 文件输出异常" + e.getMessage());
            }
        }
        csvWriter.close();
        String serverUrl = request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String csvUrl = serverUrl + "/kg/download/" + fileName;
        res.put("code", 200);
        res.put("csvurl", csvUrl);
        res.put("message", "success!");
        return res;

    }

    // 文件下载相关代码
    @GetMapping(value = "/download/{filename}")
    public String download(@PathVariable("filename") String filename, HttpServletRequest request,
                           HttpServletResponse response) {
        String filePath = config.getLocation();
        String fileUrl = filePath + filename;
        if (fileUrl != null) {
            File file = new File(fileUrl);
            if (file.exists()) {
                //response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + filename + ".csv");// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/getnodeimage")
    public R<List<Map<String, Object>>> getNodeImagelist(int domainid, int nodeid) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> images = kgservice.getNodeImageList(domainid, nodeid);
            result.code = 200;
            result.setData(images);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getnodecontent")
    public R<Map<String, Object>> getNodeContent(int domainid, int nodeid) {
        R<Map<String, Object>> result = new R<Map<String, Object>>();
        try {
            List<Map<String, Object>> contents = kgservice.getNodeContent(domainid, nodeid);
            if (contents != null && contents.size() > 0) {
                result.code = 200;
                result.setData(contents.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getnodedetail")
    public R<Map<String, Object>> getNodeDetail(int domainid, int nodeid) {
        R<Map<String, Object>> result = new R<Map<String, Object>>();
        try {
            Map<String, Object> res = new HashMap<String, Object>();
            res.put("content", "");
            res.put("imagelist", new String[]{});
            List<Map<String, Object>> contents = kgservice.getNodeContent(domainid, nodeid);
            if (contents != null && contents.size() > 0) {
                res.replace("content", contents.get(0).get("Content"));
            }
            List<Map<String, Object>> images = kgservice.getNodeImageList(domainid, nodeid);
            if (images != null && images.size() > 0) {
                res.replace("imagelist", images);
            }
            result.code = 200;
            result.setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/savenodeimage")
    public R<String> saveNodeImage(@RequestBody Map<String, Object> params) {
        R<String> result = new R<String>();
        try {
            String username = "tc";
            int domainid = (int) params.get("domainid");
            String nodeid = params.get("nodeid").toString();
            String imagelist = params.get("imagelist").toString();
            List<Map<String, Object>> domainList = kgservice.getDomainById(domainid);
            if (domainList != null && domainList.size() > 0) {
                String domainName = domainList.get(0).get("name").toString();
                kgservice.deleteNodeImage(domainid, Integer.parseInt(nodeid));
                List<Map> imageItems = JSON.parseArray(imagelist, Map.class);
                List<Map<String, Object>> submitItemList = new ArrayList<Map<String, Object>>();
                if (!imageItems.isEmpty()) {
                    for (Map<String, Object> item : imageItems) {
                        String file = item.get("fileurl").toString();
                        int sourcetype = 0;
                        Map<String, Object> sb = new HashMap<String, Object>();
                        sb.put("file", file);
                        sb.put("imagetype", sourcetype);
                        sb.put("domainid", domainid);
                        sb.put("nodeid", nodeid);
                        sb.put("status", 1);
                        sb.put("createuser", username);
                        sb.put("createtime", DateUtil.getDateNow());
                        submitItemList.add(sb);
                    }
                }
                if (submitItemList != null && submitItemList.size() > 0) {
                    kgservice.saveNodeImage(submitItemList);
                    // 更新到图数据库,表明该节点有附件,加个标识,0=没有,1=有
                    KGGraphService.updateNodeFileStatus(domainName, Long.parseLong(nodeid), 1);
                    result.code = 200;
                    result.setMsg("操作成功");
                } else {
                    KGGraphService.updateNodeFileStatus(domainName, Long.parseLong(nodeid), 0);
                    result.code = 200;
                    result.setMsg("操作成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/savenodecontent")
    public R<String> savenodecontent(@RequestBody Map<String, Object> params) {
        R<String> result = new R<String>();
        try {
            String username = "tc";
            int domainid = (int) params.get("domainid");
            String nodeid = params.get("nodeid").toString();
            String content = params.get("content").toString();
            List<Map<String, Object>> domainList = kgservice.getDomainById(domainid);
            if (domainList != null && domainList.size() > 0) {
                String domainName = domainList.get(0).get("name").toString();
                // 检查是否存在
                List<Map<String, Object>> items = kgservice.getNodeContent(domainid, Integer.parseInt(nodeid));
                if (items != null && items.size() > 0) {
                    Map<String, Object> olditem = items.get(0);
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("domainid", olditem.get("DomainId"));
                    item.put("nodeid", olditem.get("NodeId"));
                    item.put("content", content);
                    item.put("modifyuser", username);
                    item.put("modifytime", DateUtil.getDateNow());
                    kgservice.updateNodeContent(item);
                    result.code = 200;
                    result.setMsg("更新成功");
                } else {
                    Map<String, Object> sb = new HashMap<String, Object>();
                    sb.put("content", content);
                    sb.put("domainid", domainid);
                    sb.put("nodeid", nodeid);
                    sb.put("status", 1);
                    sb.put("createuser", username);
                    sb.put("createtime", DateUtil.getDateNow());
                    if (sb != null && sb.size() > 0) {
                        kgservice.saveNodeContent(sb);
                        result.code = 200;
                        result.setMsg("保存成功");
                    }
                }
                // 更新到图数据库,表明该节点有附件,加个标识,0=没有,1=有
                KGGraphService.updateNodeFileStatus(domainName, Long.parseLong(nodeid), 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getNodeAttrById")
    public R<HashMap<String, Object>> getNodeAttrById(String domain, int nodeid) {
        R<HashMap<String, Object>> result = new R<HashMap<String, Object>>();
        try {
            HashMap<String, Object> contents = KGGraphService.getNodeAttrById(domain, nodeid);
            result.code = 200;
            result.setData(contents);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getStatusList")
    public R<List<Map<String, Object>>> getStatusList(int domainId, int nodeId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> statusList = kgservice.getStatusList(domainId, nodeId);
            result.setCode(200);
            result.setData(statusList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/createStatus")
    public R<String> createStatus(@RequestBody Status status) {
        R<String> result = new R<String>();
        try {
            //添加指令
            kgservice.createStatus(status);

            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/updateStatus")
    public R<String> updateStatus(@RequestBody Status status) {
        R<String> result = new R<String>();
        try {
            //修改指令
            kgservice.updateStatus(status);

            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteStatus")
    public R<String> deleteStatus(@RequestBody Status status) {
        R<String> result = new R<String>();
        try {
            //修改动作
            kgservice.deleteStatus(status.getId());

            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getOrderList")
    public R<List<Map<String, Object>>> getOrderList(int domainId, int nodeId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> orderList = kgservice.getOrderList(domainId, nodeId);
            result.setCode(200);
            result.setData(orderList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/createOrder")
    public R<String> createOrder(@RequestBody Order order) {
        R<String> result = new R<String>();
        try {
            //添加指令
            System.out.println(order);
            kgservice.createOrder(order);

            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/updateOrder")
    public R<String> updateOrder(@RequestBody Order order) {
        R<String> result = new R<String>();
        try {
            //修改指令
            kgservice.updateOrder(order);

            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteOrder")
    public R<String> deleteOrder(@RequestBody Order order) {
        R<String> result = new R<String>();
        try {
            //修改动作
            kgservice.deleteOrder(order.getId());

            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getActionList")
    public R<List<Map<String, Object>>> getActionList(int domainId, int nodeId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> actionList = kgservice.getActionList(domainId, nodeId);
            //获取动作参数
//            for (Map<String, Object> action : actionList) {
//                Object actionId = action.get("id");
//                List<Map<String, Object>> paramList = kgservice.getParamListByActionId((String) actionId);
////                List<String> codeList = kgservice.getCodeListByActionId((String) actionId);
//                action.put("params", paramList);
////                action.put("res", codeList);
//            }
            result.setCode(200);
            result.setData(actionList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/createAction")
    public R<String> createAction(@RequestBody Action action) {
        R<String> result = new R<String>();
        try {
            //添加动作
//			System.out.println(action);
            kgservice.createAction(action);
            //添加动作参数
//            List<Param> paramList = action.getParams();
//            for (Param param : paramList) {
//                param.setActionId(action.getId());
//                kgservice.createParams(param);
//            }
//            //添加response code
//            List<String> codes = action.getRes();
//            for (String code : codes) {
//                Response res = new Response();
//                res.setCode(code);
//                res.setActionId(action.getId());
//                kgservice.createRes(res);
//            }
            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/updateAction")
    public R<String> updateAction(@RequestBody Action action) {
        R<String> result = new R<String>();
        try {
            //修改动作
            System.out.println(action);
            kgservice.updateAction(action);
            //修改动作参数
//            List<Param> paramList = action.getParams();
//            for (Param param : paramList) {
//                if (param.getId() == null) {
//                    param.setActionId(action.getId());
//                    kgservice.createParams(param);
//                }
//                kgservice.updateParam(param);
//            }
//            //修改response code
//            List<String> codes = action.getRes();
//            //全删
//            kgservice.deleteAllResByActionId(action.getId());
//            for (String code : codes) {
//                //重新加
//                Response res = new Response();
//                res.setCode(code);
//                res.setActionId(action.getId());
//                kgservice.createRes(res);
//            }
            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAction")
    public R<String> deleteAction(@RequestBody Action action) {
        R<String> result = new R<String>();
        try {
            //修改动作
            kgservice.deleteAction(action.getId());
//            //参数全删
//            kgservice.deleteAllParamsByActionId(action.getId());
//            //code全删
//            kgservice.deleteAllResByActionId(action.getId());
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/createWord")
    public R<String> createWord(@RequestBody Word word) {
        R<String> result = new R<String>();
        System.out.println("word");
        System.out.println(word);
        try {
            kgservice.createWord(word);
            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getWordList")
    public R<List<Map<String, Object>>> getWordList(int domainId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> wordList = kgservice.getWordList(domainId);
            System.out.println(wordList);
            result.setCode(200);
            result.setData(wordList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteWord")
    public R<String> deleteWord(@RequestBody String wordId) {
        R<String> result = new R<String>();
        Map wordIdMap = JSONObject.parseObject(wordId);
        String wordId_new = (String) wordIdMap.get("wordId");
        System.out.println(wordId_new);
        try {
            kgservice.deleteWord(wordId_new);
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updateWord")
    public R<String> updateWord(@RequestBody Word word) {
        R<String> result = new R<String>();
//		System.out.println(word);
        try {
            kgservice.updateWord(word);
            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getAllMsg")
    public R<List<Map<String, Object>>> getAllMsg(int domainId, int sserviceId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> msgList = kgservice.getAllMsg(domainId, sserviceId);
//			System.out.println(msgList);
            result.setCode(200);
            result.setData(msgList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getMsg")
    public R<List<Map<String, Object>>> getMsg(int sserviceId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> msgList = kgservice.getMsg(sserviceId);
//			System.out.println(msgList);
            result.setCode(200);
            result.setData(msgList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getDomainsBySsId")
    public R<List<Map<String, Object>>> getDomainsBySsId(int sserviceId, int domainId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> domainList = kgservice.getDomainsBySsId(sserviceId);
            System.out.println(domainList);

            for (Map<String, Object> domain : domainList) {
                if (domain.get("id").equals(domainId)) {
                    domainList.remove(domain);
                    break;
                }
            }
            System.out.println(domainList);
            result.setCode(200);
            result.setData(domainList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getAllDomainsBySsId")
    public R<List<Map<String, Object>>> getAllDomainsBySsId(int sserviceId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> domainList = kgservice.getDomainsBySsId(sserviceId);
            result.setCode(200);
            result.setData(domainList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getAllActionByDomainId")
    public R<List<Map<String, Object>>> getAllActionByDomainId(int domainId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            System.out.println(domainId);
            List<Map<String, Object>> triggerActionList = kgservice.getAllActionByDomainId(domainId);
            System.out.println(triggerActionList);
            result.setCode(200);
            result.setData(triggerActionList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/createMsg")
    public R<String> createMsg(@RequestBody Msg msg) {
        R<String> result = new R<String>();
//		System.out.println(msg);
        try {
            kgservice.createMsg(msg);
            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteMsg")
    public R<String> deleteMsg(@RequestBody String msgId) {
        R<String> result = new R<String>();
        Map msgIdMap = JSONObject.parseObject(msgId);
        String msgId_new = (String) msgIdMap.get("msgId");
        System.out.println(msgId_new);
        try {
            kgservice.deleteMsg(msgId_new);
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updateMsg")
    public R<String> updateMsg(@RequestBody Msg msg) {
        R<String> result = new R<String>();
        System.out.println(msg);
        try {
            kgservice.updateMsg(msg);
            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/updateDomain")
    public R<String> updateDomain(@RequestBody Domain domain) {
        R<String> result = new R<String>();
        System.out.println(domain);
        try {
            String oldName = (String) getDomain(domain.getId()).getData().get("name");
            KGGraphService.updatedomainname(oldName, domain.getName());

            kgservice.updateDomain2(domain);
            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/getDomain")
    public R<Map<String, Object>> getDomain(int domainId) {
        R<Map<String, Object>> result = new R<Map<String, Object>>();
        try {
            Map<String, Object> domain = kgservice.getDomain(domainId);
            System.out.println(domain);
            result.setCode(200);
            result.setData(domain);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/addProcess")
    public R<String> addProcess(@RequestBody Process process) {
        R<String> result = new R<String>();
        System.out.println(process);
        try {
            kgservice.addProcess(process);
            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getProcessList")
    public R<List<Map<String, Object>>> getProcessList(int domainId, int nodeId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> processList = kgservice.getProcessList(domainId, nodeId);
            System.out.println(processList);
            result.setCode(200);
            result.setData(processList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/isProcessExist")
    public R<Boolean> isProcessExist(String processId) {
        R<Boolean> result = new R<Boolean>();
        try {
            Boolean exist;
            System.out.println(processId);
            if (processId != null) {
                exist = kgservice.isProcessExist(processId);
            } else {
                exist = false;
            }
            ;
            result.setCode(200);
            result.setData(exist);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteProcess")
    public R<String> deleteProcess(@RequestBody String processId) {
        R<String> result = new R<String>();
        Map processIdMap = JSONObject.parseObject(processId);
        String processId_new = (String) processIdMap.get("processId");
        try {
            kgservice.deleteProcess(processId_new);
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updateProcess")
    public R<String> updateProcess(@RequestBody Process process) {
        R<String> result = new R<String>();
        System.out.println(process);
        try {
            kgservice.updateProcess(process);
            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getStepList")
    public R<List<Map<String, Object>>> getStepList(int domainId, int nodeId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> stepList = kgservice.getStepList(domainId, nodeId);
            Collections.sort(stepList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Integer value1 = (Integer) o1.get("step_num");
                    Integer value2 = (Integer) o2.get("step_num");
                    return value1.compareTo(value2);
                }
            });
//            System.out.println(stepList);
            result.setCode(200);
            result.setData(stepList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/addStep")
    public R<String> addStep(@RequestBody Step step) {
        R<String> result = new R<String>();
        try {
            kgservice.addStep(step);
            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updateStep")
    public R<String> updateStep(@RequestBody Step step) {
        R<String> result = new R<String>();
//        System.out.println(step);
        try {
            kgservice.updateStep(step);
            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteStep")
    public R<String> deleteStep(@RequestBody String stepId) {
        R<String> result = new R<String>();
        Map stepIdMap = JSONObject.parseObject(stepId);
        String stepId_new = (String) stepIdMap.get("stepId");
        try {
            kgservice.deleteStep(stepId_new);
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getStepTag")
    public R<List<Map<String, Object>>> getStepTag(int domainId, int nodeId) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> stepTagList = kgservice.getStepTag(domainId, nodeId);
//            Collections.sort(stepList, new Comparator<Map<String, Object>>() {
//                @Override
//                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//                    Integer value1= (Integer) o1.get("step_num");
//                    Integer value2= (Integer) o2.get("step_num");
//                    return value1.compareTo(value2);
//                }
//            });
//            System.out.println(stepTagList);
            result.setCode(200);
            result.setData(stepTagList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/addStepTag")
    public R<String> addStepTag(@RequestBody StepTag stepTag) {
        R<String> result = new R<String>();
        try {
            kgservice.addStepTag(stepTag);
            result.setData(stepTag.getId());
            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteStepTag")
    public R<String> deleteStepTag(@RequestBody String stepTagId) {
        R<String> result = new R<String>();
        Map stepTagIdMap = JSONObject.parseObject(stepTagId);
        String stepTagId_new = (String) stepTagIdMap.get("stepTagId");
        try {
            kgservice.deleteStepTag(stepTagId_new);
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteProcessAndTag")
    public R<String> deleteProcessAndTag(int nodeId, int stepNum) {
        R<String> result = new R<String>();
        System.out.println(stepNum);
        try {
            kgservice.deleteProcessAndTag(nodeId, stepNum);
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/addMxGraph")
    public R<String> addMxGraph(String graphData, Integer sserviceId) {
        R<String> result = new R<String>();
//        System.out.println(graphData);
//        System.out.println(sserviceId);
        try {
            if (!StringUtil.isBlank(graphData)) {
                kgservice.deleteMxGraphBySserviceId(sserviceId);
                MxGraph mxGraph = new MxGraph();
                mxGraph.setGraphData(graphData);
                mxGraph.setSserviceId(sserviceId);
//                Map<String, Object> maps = new HashMap<String, Object>();
//                maps.put("graphData", graphData);
//                maps.put("sserviceId", sserviceId);
                System.out.println(mxGraph);
                kgservice.saveMxGraph(mxGraph);// 保存到mysql
                result.code = 200;
                result.setMsg("保存成功");

            }
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getMxGraph")
    public R<String> getMxGraph(Integer sserviceId) {
        R<String> result = new R<String>();
//        System.out.println(sserviceId);
        try {
            String graphData = kgservice.getMxGraph(sserviceId);
//            System.out.println(graphData);
            result.setCode(200);
            result.setData(graphData);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/toModel")
    public R<String> toModel(int sserviceId) {
        R<String> result = new R<String>();
        HashMap<String, Object> Model = new HashMap<>();
        try {
            //获取某个领域内所有上下文
            System.out.println("######################开始######################");
            List<Map<String, Object>> domainList = kgservice.getDomainsBySsId(sserviceId);
            Sservice sservice = sserviceService.getById(sserviceId);
            String sserviceName = sservice.getName();
            System.out.println("1.1、================领域名称================");
            System.out.println(sserviceName);
            System.out.println("1.2、================所有上下文================");
            System.out.println(domainList);

            List<HashMap<String, Object>> newDomainList = new ArrayList<>();
            for (Map<String, Object> map : domainList) {
                Integer domainId = (Integer) map.get("id");
                String domainName = (String) map.get("name");
                System.out.println("=========上下文：" + domainName + "========");
                GraphQuery query = new GraphQuery();
                query.setDomain(domainName);
                //获取某个上下文内所有节点细节和关系
                HashMap<String, Object> graphData = KGGraphService.getdomaingraph(query);
                System.out.println("2、================该上下文所有实体和关系");
                List<Map<String, Object>> nodeList = (List<Map<String, Object>>) graphData.get("node");
                HashMap<String, Object> nodeMap = new HashMap<>();

                System.out.println(nodeList);
                List<Map<String, Object>> relList = (List<Map<String, Object>>) graphData.get("relationship");
                System.out.println(relList);
                //获取某个上下文内所有通信（和其他上下文的联系）
                List<Map<String, Object>> msgList = kgservice.getAllMsg(domainId, sserviceId);
                System.out.println("3、================该上下文内所有通信");
                System.out.println(msgList);
                //获取某个上下文内所有节点（用来进一步查询节点动作）
                List<HashMap<String, Object>> nodes = KGGraphService.getdomainnodes(domainName);
                for (Map<String, Object> node : nodeList) {
                    Integer nodeId = Integer.parseInt(node.get("uuid").toString());
                    String nodeName = (String) node.get("code");
                    System.out.println("4.0、================该上下文内节点" + nodeId + "的自定义属性");
                    List<Map<String, Object>> userAttrList = kgservice.getUserAttrList(domainId, nodeId);
                    System.out.println(userAttrList);

                    System.out.println("4.1、================该上下文内节点" + nodeId + "的事件");
                    List<Map<String, Object>> actionList = kgservice.getActionList(domainId, nodeId);
                    System.out.println(actionList);

//                    System.out.println("4.2、================该上下文内节点" + nodeId + "的指令");
//                    List<Map<String, Object>> orderList = kgservice.getOrderList(domainId, nodeId);
//                    System.out.println(orderList);

                    System.out.println("4.3、================该上下文内节点" + nodeId + "的状态");
                    List<String> modeList = new ArrayList<>();
                    modeList.add("Production");
                    modeList.add("Maintenance");
                    modeList.add("Manual");
                    List<Map<String, Object>> statusList = new ArrayList<>();
                    for (String mode : modeList) {
                        String stateJson = kgservice.getStateFlow(domainId, nodeId, mode);
                        if(stateJson != null){
                            //把状态图中的状态抽取出来放数组里
                            Map<String, List<Map<String, Object>>> stateFlowMap = (Map<String, List<Map<String, Object>>>) JSON.parse(stateJson);
                            System.out.println(stateFlowMap);
                            List<Map<String, Object>> edgeList = new ArrayList<>();
                            List<Map<String, Object>> stateList = new ArrayList<>();
                            for (Map<String, Object> state: stateFlowMap.get("cells")){
                                if(state.get("shape").equals("rect")){
                                    if((Integer)((Map<String, Object>)state.get("size")).get("width") < 600){
                                        Map<String, Object> stateMap = new HashMap<>();
                                        stateMap.put("id", state.get("id"));
                                        stateMap.put("name", ((Map<String, Object>)((Map<String, Object>)state.get("attrs")).get("label")).get("text"));
                                        stateMap.put("des", ((Map<String, Object>)state.get("data")).get("des"));
                                        stateList.add(stateMap);
                                    }
                                } else if (state.get("shape").equals("edge")) {
                                    List<Map<String, Object>> labels = (List<Map<String, Object>>)state.get("labels");
                                    if(labels.get(0).containsKey("attrs")){
                                        Map<String, Object> edgeMap = new HashMap<>();
                                        edgeMap.put("text", ((Map<String, Object>)((Map<String, Object>)labels.get(0).get("attrs")).get("label")).get("text"));
                                        edgeMap.put("sourceId", ((Map<String, Object>)state.get("source")).get("cell"));
                                        edgeMap.put("targetId", ((Map<String, Object>)state.get("target")).get("cell"));
                                        edgeList.add(edgeMap);
                                    }
                                }
                            };
                            System.out.println(stateList);
                            System.out.println(edgeList);

                            //把命令和状态绑定
                            for (int i = 0; i < stateList.size(); i++) {
                                for (int j = 0; j < edgeList.size(); j++) {
                                    if(edgeList.get(j).get("sourceId").equals(stateList.get(i).get("id"))){
                                        stateList.get(i).put("postCommand", edgeList.get(j).get("text"));
                                    }else if (edgeList.get(j).get("targetId").equals(stateList.get(i).get("id"))){
                                        stateList.get(i).put("preCommand", edgeList.get(j).get("text"));
                                    }
                                }
                            }
                            System.out.println(stateList);

                        Map<String,Object> statusMap = new HashMap<>();
                        statusMap.put("Mode", mode);
                        statusMap.put("State", stateList);
                        statusList.add(statusMap);
                        System.out.println(statusList);
                        }
                    }
                    System.out.println("*******节点结束******");

                    //往节点map内添加动作、状态和指令
//                    node.put("指令", orderList);
                    node.put("userAttr", userAttrList);
                    node.put("event", actionList);
                    node.put("state", statusList);
                    nodeMap.put(nodeName.replace(" ", ""), node);
                }

                //整合↓
                //该上下文新的节点List
                List<HashMap<String, Object>> newNodeList = new ArrayList<>();
                newNodeList.add(nodeMap);
//                System.out.println(newNodeList);
                //往上下文map里加通信、关系、实体
                HashMap<String, Object> domainMap = new HashMap<>();
                domainMap.put("communication", msgList);
                domainMap.put("relationship", relList);
                domainMap.put("entityList", newNodeList);
//                System.out.println(domainMap);
                HashMap<String, Object> newDomainMap = new HashMap<>();
                newDomainMap.put("contextName", domainName);
                newDomainMap.put("BoundedContext", domainMap);
                System.out.println(newDomainMap);
                newDomainList.add(newDomainMap);
                System.out.println("*****************上下文结束*****************");
            }

            System.out.println("&&&&&&&&&&&&&&&最终模型&&&&&&&&&&&&&&&&&&");
            Model.put("domainName", sserviceName);
            Model.put("Domain", newDomainList);
            HashMap<String, Object> Final = new HashMap<>();
            Final.put("Model",Model);
            System.out.println(Final);
            String jsonString = JSON.toJSONString(Final);
            // 输出json文件
            CreateFileUtil.createJsonFile(jsonString, "C:\\Users\\shiha\\Desktop", sserviceName);

            //输出xml文件
            String modelJsonStr = JSON.toJSONString(Final);
            //json转xml
            boolean toXml = toXML.jsonToXML(modelJsonStr, sserviceName);
            if(toXml==true){
                result.setData(jsonString);
                result.setCode(200);
                result.setMsg("成功输出");
            }


        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/toModelNotInput")
    public R<String> toModelNotInput(int sserviceId) {
        R<String> result = new R<String>();
        HashMap<String, Object> Model = new HashMap<>();
        try {
            //获取某个领域内所有上下文
            System.out.println("######################开始######################");
            List<Map<String, Object>> domainList = kgservice.getDomainsBySsId(sserviceId);
            Sservice sservice = sserviceService.getById(sserviceId);
            String sserviceName = sservice.getName();
            System.out.println("1.1、================领域名称================");
            System.out.println(sserviceName);
            System.out.println("1.2、================所有上下文================");
            System.out.println(domainList);

            List<HashMap<String, Object>> newDomainList = new ArrayList<>();
            for (Map<String, Object> map : domainList) {
                Integer domainId = (Integer) map.get("id");
                String domainName = (String) map.get("name");
                System.out.println("=========上下文：" + domainName + "========");
                GraphQuery query = new GraphQuery();
                query.setDomain(domainName);
                //获取某个上下文内所有节点细节和关系
                HashMap<String, Object> graphData = KGGraphService.getdomaingraph(query);
                System.out.println("2、================该上下文所有实体和关系");
                List<Map<String, Object>> nodeList = (List<Map<String, Object>>) graphData.get("node");
                HashMap<String, Object> nodeMap = new HashMap<>();

                System.out.println(nodeList);
                List<Map<String, Object>> relList = (List<Map<String, Object>>) graphData.get("relationship");
                System.out.println(relList);
                //获取某个上下文内所有通信（和其他上下文的联系）
                List<Map<String, Object>> msgList = kgservice.getAllMsg(domainId, sserviceId);
                System.out.println("3、================该上下文内所有通信");
                System.out.println(msgList);
                //获取某个上下文内所有节点（用来进一步查询节点动作）
                List<HashMap<String, Object>> nodes = KGGraphService.getdomainnodes(domainName);
                for (Map<String, Object> node : nodeList) {
                    Integer nodeId = Integer.parseInt(node.get("uuid").toString());
                    String nodeName = (String) node.get("code");
                    System.out.println("4.0、================该上下文内节点" + nodeId + "的自定义属性");
                    List<Map<String, Object>> userAttrList = kgservice.getUserAttrList(domainId, nodeId);

                    System.out.println("4.1、================该上下文内节点" + nodeId + "的事件");
                    List<Map<String, Object>> actionList = kgservice.getActionList(domainId, nodeId);
                    System.out.println(actionList);

//                    System.out.println("4.2、================该上下文内节点" + nodeId + "的指令");
//                    List<Map<String, Object>> orderList = kgservice.getOrderList(domainId, nodeId);
//                    System.out.println(orderList);

                    System.out.println("4.3、================该上下文内节点" + nodeId + "的状态");
                    List<String> modeList = new ArrayList<>();
                    modeList.add("Production");
                    modeList.add("Maintenance");
                    modeList.add("Manual");
                    List<Map<String, Object>> statusList = new ArrayList<>();
                    for (String mode : modeList) {
                        String stateJson = kgservice.getStateFlow(domainId, nodeId, mode);
                        if(stateJson != null){
                            //把状态图中的状态抽取出来放数组里
                            Map<String, List<Map<String, Object>>> stateFlowMap = (Map<String, List<Map<String, Object>>>) JSON.parse(stateJson);
                            System.out.println(stateFlowMap);
                            List<Map<String, Object>> edgeList = new ArrayList<>();
                            List<Map<String, Object>> stateList = new ArrayList<>();
                            for (Map<String, Object> state: stateFlowMap.get("cells")){
                                if(state.get("shape").equals("rect")){
                                    if((Integer)((Map<String, Object>)state.get("size")).get("width") < 600){
                                        Map<String, Object> stateMap = new HashMap<>();
                                        stateMap.put("id", state.get("id"));
                                        stateMap.put("name", ((Map<String, Object>)((Map<String, Object>)state.get("attrs")).get("label")).get("text"));
                                        stateMap.put("des", ((Map<String, Object>)state.get("data")).get("des"));
                                        stateList.add(stateMap);
                                    }
                                } else if (state.get("shape").equals("edge")) {
                                    List<Map<String, Object>> labels = (List<Map<String, Object>>)state.get("labels");
                                    if(labels.get(0).containsKey("attrs")){
                                        Map<String, Object> edgeMap = new HashMap<>();
                                        edgeMap.put("text", ((Map<String, Object>)((Map<String, Object>)labels.get(0).get("attrs")).get("label")).get("text"));
                                        edgeMap.put("sourceId", ((Map<String, Object>)state.get("source")).get("cell"));
                                        edgeMap.put("targetId", ((Map<String, Object>)state.get("target")).get("cell"));
                                        edgeList.add(edgeMap);
                                    }
                                }
                            };
                            System.out.println(stateList);
                            System.out.println(edgeList);

                            //把命令和状态绑定
                            for (int i = 0; i < stateList.size(); i++) {
                                for (int j = 0; j < edgeList.size(); j++) {
                                    if(edgeList.get(j).get("sourceId").equals(stateList.get(i).get("id"))){
                                        stateList.get(i).put("postCommand", edgeList.get(j).get("text"));
                                    }else if (edgeList.get(j).get("targetId").equals(stateList.get(i).get("id"))){
                                        stateList.get(i).put("preCommand", edgeList.get(j).get("text"));
                                    }
                                }
                            }
                            System.out.println(stateList);

                            Map<String,Object> statusMap = new HashMap<>();
                            statusMap.put("Mode", mode);
                            statusMap.put("State", stateList);
                            statusList.add(statusMap);
                            System.out.println(statusList);
                        }
                    }
                    System.out.println("*******节点结束******");

                    //往节点map内添加动作、状态和指令
//                    node.put("指令", orderList);
                    node.put("userAttr", userAttrList);
                    node.put("event", actionList);
                    node.put("state", statusList);
                    nodeMap.put(nodeName.replace(" ", ""), node);
                }

                //整合↓
                //该上下文新的节点List
                List<HashMap<String, Object>> newNodeList = new ArrayList<>();
                newNodeList.add(nodeMap);
//                System.out.println(newNodeList);
                //往上下文map里加通信、关系、实体
                HashMap<String, Object> domainMap = new HashMap<>();
                domainMap.put("communication", msgList);
                domainMap.put("relationship", relList);
                domainMap.put("entityList", newNodeList);
//                System.out.println(domainMap);
                HashMap<String, Object> newDomainMap = new HashMap<>();
                newDomainMap.put("contextName", domainName);
                newDomainMap.put("BoundedContext", domainMap);
                System.out.println(newDomainMap);
                newDomainList.add(newDomainMap);
                System.out.println("*****************上下文结束*****************");
            }

            System.out.println("&&&&&&&&&&&&&&&最终模型&&&&&&&&&&&&&&&&&&");
            Model.put("domainName", sserviceName);
            Model.put("Domain", newDomainList);
            HashMap<String, Object> Final = new HashMap<>();
            Final.put("Model",Model);
            String jsonString = JSON.toJSONString(Final);
            System.out.println(jsonString);
            result.setData(jsonString);
            result.setCode(200);
            result.setMsg("获取成功");

        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/addStateFlow")
    public R<String> addStateFlow(String graphData, Integer domainId, Integer nodeId, String mode) {
        R<String> result = new R<String>();
        System.out.println(graphData);
        System.out.println(domainId);
        System.out.println(nodeId);
        System.out.println("mode");
        try {
            if (!StringUtil.isBlank(graphData)) {
                kgservice.deleteStateFlowByNodeIdAndDomainId(domainId, nodeId, mode);
                StateFlow stateFlow = new StateFlow();
                stateFlow.setGraphData(graphData);
                stateFlow.setDomainId(domainId);
                stateFlow.setNodeId(nodeId);
                stateFlow.setMode(mode);

                System.out.println(stateFlow);
                kgservice.saveStateFlow(stateFlow);// 保存到mysql
                result.code = 200;
                result.setMsg("保存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getStateFlow")
    public R<String> getStateFlow(Integer domainId, Integer nodeId, String mode) {
        R<String> result = new R<String>();
        System.out.println(mode);
        try {
            String graphData = kgservice.getStateFlow(domainId, nodeId, mode);
//            System.out.println(graphData);
            result.setCode(200);
            result.setData(graphData);
            result.setMsg("获取到数据");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/addStateAction")
    public R<String> addStateAction(StateAction stateAction) {
        R<String> result = new R<String>();
//        System.out.println(stateAction);
        try {
            kgservice.addStateAction(stateAction);
            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getStateActionList")
    public R<List<Map<String, Object>>> getStateActionList(int domainId, int nodeId, String stateName, String mode) {
        R<List<Map<String, Object>>> result = new R<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> stateActionList = kgservice.getStateActionList(domainId, nodeId, stateName, mode);
            System.out.println(stateActionList);
            for (Map<String, Object> action : stateActionList) {
                Object actionId = action.get("id");
                List<Map<String, Object>> paramList = kgservice.getParamListByActionId((String) actionId);
                action.put("params", paramList);
            }
            result.setCode(200);
            result.setData(stateActionList);
            result.setMsg("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updateStateAction")
    public R<String> updateStateAction(StateAction stateAction) {
        R<String> result = new R<String>();
//        System.out.println(stateAction);
        try {
            kgservice.updateStateAction(stateAction);
            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updateStateActionName")
    public R<String> updateStateActionName(String newName, String oldName, int domainId, int nodeId) {
        R<String> result = new R<String>();
        System.out.println(newName);
        try {
            kgservice.updateStateActionName(newName, oldName, domainId, nodeId);
            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteStateAction")
    public R<String> deleteStateAction(@RequestBody String stateActionId) {
        R<String> result = new R<String>();
        Map stateActionIdMap = JSONObject.parseObject(stateActionId);
        String stateActionId_new = (String) stateActionIdMap.get("stateActionId");
        try {
            kgservice.deleteStateAction(stateActionId_new);
            kgservice.deleteAllParamsByActionId(stateActionId_new);
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }



    @ResponseBody
    @RequestMapping(value = "/addParam")
    public R<String> addParam(Param param) {
        R<String> result = new R<String>();
        System.out.println(param);
        try {
            kgservice.createParams(param);
            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getParamsByActionId")
    public R<List<Map<String, Object>>> getParamsByActionId(String actionId) {
        R<List<Map<String, Object>>> result = new R<>();
        try {
            List<Map<String, Object>> paramList = kgservice.getParamListByActionId(actionId);
            result.setCode(200);
            result.setData(paramList);
            result.setMsg("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updateParam")
    public R<String> updateParam(Param param) {
        R<String> result = new R<>();
        try {
            System.out.println(param);
            kgservice.updateParam(param);
            result.code = 200;
            result.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteParamByParamId")
    public R<String> deleteParamByParamId(@RequestBody String paramId) {
        R<String> result = new R<String>();
        Map paramIdMap = JSONObject.parseObject(paramId);
        String paramId_new = (String) paramIdMap.get("paramId");
        try {
            kgservice.deleteParamByParamId(paramId_new);
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    //获取、添加、删除自定义属性
    @ResponseBody
    @RequestMapping(value = "/updateUserAttr")
    public R<String> updateUserAttr(@RequestBody UserAttrs userAttrs) {
        R<String> result = new R<>();
        for (Map<String, Object> attrMap : userAttrs.getAttrList()) {
            String id = (String) attrMap.get("id");
            UserAttr userAttr = new UserAttr();
            userAttr.setId(id);
            userAttr.setAttrValue((String)attrMap.get("attrValue"));
            userAttr.setAttrLabel((String)attrMap.get("attrLabel"));
            userAttr.setDomainId(userAttrs.getDomainId());
            userAttr.setNodeId(userAttrs.getNodeId());
            System.out.println(userAttr);
            if(kgservice.isUserAttrExist(id) == true){
                kgservice.updateUserAttr(userAttr);
            }else {
                kgservice.createUserAttr(userAttr);
            };
        }
        try {
            result.code = 200;
            result.setMsg("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getUserAttrList")
    public R<List<Map<String, Object>>> getUserAttrList(int domainId, int nodeId) {
        R<List<Map<String, Object>>> result = new R<>();
        try {
            List<Map<String, Object>> UserAttrList = kgservice.getUserAttrList(domainId, nodeId);
            result.setCode(200);
            result.setData(UserAttrList);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/getUserAttr")
    public R<String> getUserAttr(String attrLabel) {
        R<String> result = new R<>();
        try {
            String UserAttr = kgservice.getUserAttr(attrLabel);
            result.setCode(200);
            result.setData(UserAttr);
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteUserAttr")
    public R<String> deleteUserAttr(String attrId) {
        R<String> result = new R<>();
        try {
            System.out.println(attrId);
            kgservice.deleteUserAttrById(attrId);
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }
}
