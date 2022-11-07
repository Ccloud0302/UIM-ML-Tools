<template>
  <div>
    <el-row class="row">
<!--      领域-->
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <el-select v-model="domain" value-key placeholder="请选择领域" class="knowledgeSelect" @change="getAllBoundedContext">
              <el-option
                  v-for="(item, index) in domainList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
            <div style="float: right">
              <el-button class="outputButton" style="padding: 8px;" type="success" plain @click="outPut('domain')">输出该领域</el-button>
              <el-popover placement="bottom-start" title="领域详情" trigger="hover" style="margin-left: 10px">
                <json-viewer :value=model :expand-depth=2 copyable></json-viewer>
                <div style="text-align: right;margin: 10px">
                  <el-button type="info" size="mini" @click="outPut('domainModel')">输出</el-button>
                </div>
                <el-button slot="reference" class="outputButton" style="padding: 8px;" type="info" plain>查看详细</el-button>
              </el-popover>
            </div>
          </div>
          <div>
            <el-descriptions title='' :column="3" border labelstyle="width:100px">
              <el-descriptions-item label="名称">{{domain}}</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
    </el-row>
    <el-row class="row">
<!--      上下文-->
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <el-select v-model="boundedContext" placeholder="请选择" class="knowledgeSelect" @change="getAllEntity">
            <el-option
                v-for="(item, index) in contextList"
                :key="item.id"
                :label="item.name"
                :value="item.name">
            </el-option>
          </el-select>
          <div style="float: right">
            <el-button class="outputButton" style="padding: 8px;" type="success" plain @click="outPut('context')">输出该上下文</el-button>
            <el-popover placement="bottom-start" title="上下文详情" trigger="hover" style="margin-left: 10px">
              <json-viewer :value=boundedContextModel :expand-depth=1 copyable></json-viewer>
              <div style="text-align: right; margin: 10px">
                <el-button type="info" size="mini" @click="outPut('contextModel')">输出</el-button>
              </div>
              <el-button slot="reference" class="outputButton" style="padding: 8px;" type="info" plain>查看详细</el-button>
            </el-popover>
          </div>
        </div>
        <div>
          <el-descriptions title='' :column="3" border labelstyle="width:100px">
            <el-descriptions-item label="名称">{{boundedContext}}</el-descriptions-item>
<!--            <el-descriptions-item label="通信">-->
<!--              <el-descriptions title='' :column="3" border>-->
<!--                <el-descriptions-item label="通信ID">{{boundedContextModel.communication.id}}</el-descriptions-item>-->
<!--                <el-descriptions-item label="通信名称">{{boundedContextModel.communication.name}}</el-descriptions-item>-->
<!--                <el-descriptions-item label="通信类型">{{boundedContextModel.communication.type}}</el-descriptions-item>-->
<!--                <el-descriptions-item label="触发动作">{{boundedContextModel.communication.triggerAction}}</el-descriptions-item>-->
<!--                <el-descriptions-item label="引发动作">{{boundedContextModel.communication.causeAction}}</el-descriptions-item>-->
<!--                <el-descriptions-item label="触发上下文">{{boundedContextModel.communication.causeContext}}</el-descriptions-item>-->
<!--              </el-descriptions>-->
<!--            </el-descriptions-item>-->
          </el-descriptions>
        </div>
      </el-card>
<!--      实体-->
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <el-select v-model="entity" placeholder="请选择实体" class="knowledgeSelect" @change="getAllOfEntity">
            <el-option
                v-for="(item,index) in entityList"
                :key="index"
                :label="item.name"
                :value="item.name">
            </el-option>
          </el-select>
          <div style="float: right">
            <el-button class="outputButton" style="padding: 8px;" type="success" plain @click="outPut('entity')">输出该实体</el-button>
            <el-popover title="实体详情" trigger="hover" style="margin-left: 10px" placement="right-smart">
              <json-viewer :value=entityModel :expand-depth=1 copyable></json-viewer>
              <div style="text-align: right; margin: 10px">
                <el-button type="info" size="mini" @click="outPut('entityModel')">输出</el-button>
              </div>
              <el-button slot="reference" class="outputButton" style="padding: 8px;" type="info" plain>查看详细</el-button>
            </el-popover>
          </div>
        </div>
        <div>
          <el-descriptions title='' :column="3" border labelstyle="width:80px">
            <el-descriptions-item label="UUID">{{entityModel.uuid}}</el-descriptions-item>
            <el-descriptions-item label="名称">{{ entityModel.name }}</el-descriptions-item>
            <el-descriptions-item label="类型">{{entityModel.type}}</el-descriptions-item>
            <el-descriptions-item label="别名">{{entityModel.code}}</el-descriptions-item>
            <el-descriptions-item label="命名空间">{{entityModel.namespace}}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
    </el-row>
    <el-row class="row">
<!--      属性-->
      <el-card class="box-card" style="width: 500px">
        <div slot="header" class="clearfix">
          <el-select v-model="attr" placeholder="请选择属性" class="knowledgeSelect" @change="toAttrModel">
            <el-option
                v-for="(value,key,index) in attrObj"
                :key="index"
                :label="key"
                :value="value">
            </el-option>
          </el-select>
          <div style="float: right">
            <el-button class="outputButton" style="padding: 8px;" type="success" plain @click="outPut('attr')">输出该属性</el-button>
            <el-popover placement="bottom-start" title="属性详情" trigger="hover" style="margin-left: 10px">
              <json-viewer :value=attrModel :expand-depth=1 copyable></json-viewer>
              <div style="text-align: right; margin: 10px">
                <el-button type="info" size="mini" @click="outPut('attr')">输出</el-button>
              </div>
              <el-button slot="reference" class="outputButton" style="padding: 8px;" type="info" plain>查看详细</el-button>
            </el-popover>
          </div>
        </div>
        <div>
          <el-descriptions title='' :column="3" border labelstyle="width:80px">
            <el-descriptions-item label="名称">{{ findKey(attrModel, attr) }}</el-descriptions-item>
            <el-descriptions-item label="值">{{attr}}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
<!--      事件-->
      <el-card class="box-card" style="width: 600px">
        <div slot="header" class="clearfix">
          <el-select v-model="event" placeholder="请选择事件" class="knowledgeSelect" @change="toEventModel">
            <el-option
                v-for="item in eventList"
                :key="item.id"
                :label="item.name"
                :value="item.name">
            </el-option>
          </el-select>
          <div style="float: right">
            <el-button class="outputButton" style="padding: 8px;" type="success" plain @click="outPut('event')">输出该事件</el-button>
            <el-popover placement="bottom-start" title="事件详情" trigger="hover" style="margin-left: 10px">
              <json-viewer :value=eventModel :expand-depth=1 copyable></json-viewer>
              <div style="text-align: right; margin: 10px">
                <el-button type="info" size="mini" @click="outPut('event')">输出</el-button>
              </div>
              <el-button slot="reference" class="outputButton" style="padding: 8px;" type="info" plain>查看详细</el-button>
            </el-popover>
          </div>
        </div>
        <el-descriptions title='' :column="3" border labelstyle="width:50px">
          <el-descriptions-item label="ID">{{eventModel.id}}</el-descriptions-item>
          <el-descriptions-item label="名称">{{eventModel.name}}</el-descriptions-item>
          <el-descriptions-item label="类型" >{{eventModel.type}}</el-descriptions-item>
          <el-descriptions-item label="引发">{{eventModel.cause}}</el-descriptions-item>
          <el-descriptions-item label="来源">{{eventModel.source}}</el-descriptions-item>
          <el-descriptions-item label="描述" >{{eventModel.des}}</el-descriptions-item>
        </el-descriptions>
      </el-card>
<!--      状态-->
      <el-card class="box-card" style="width: 600px">
        <div slot="header" class="clearfix">
          <el-select v-model="mode" placeholder="请选择模式" class="knowledgeSelect" @change="getStateByMode" style="margin-right: 40px">
            <el-option
                v-for="item in modeList"
                :key="item.id"
                :label="item.name"
                :value="item.name">
            </el-option>
          </el-select>
          <el-select v-model="state" placeholder="请选择状态" class="knowledgeSelect" @change="toStateModel">
            <el-option
                v-for="item in stateByModeList"
                :key="item.id"
                :label="item.name"
                :value="item.name">
            </el-option>
          </el-select>
          <div style="float: right;margin-top: 5px">
            <el-button class="outputButton" style="padding: 8px;" type="success" plain @click="outPut('state')">输出该状态图</el-button>
            <el-popover placement="bottom-start" title="状态详情" trigger="hover" style="margin-left: 10px">
              <json-viewer :value=stateModel :expand-depth=1 copyable></json-viewer>
              <div style="text-align: right; margin: 10px">
                <el-button type="info" size="mini" @click="outPut('state')">输出</el-button>
              </div>
              <el-button slot="reference" class="outputButton" style="padding: 8px;" type="info" plain>查看详细</el-button>
            </el-popover>
          </div>
        </div>
        <div>
          <el-descriptions title='' :column="3" border labelstyle="width:60px">
            <el-descriptions-item label="模式">{{ mode }}</el-descriptions-item>
            <el-descriptions-item label="名称">{{stateModel.name}}</el-descriptions-item>
            <el-descriptions-item label="描述">{{stateModel.des}}</el-descriptions-item>
            <el-descriptions-item label="ID">{{stateModel.id}}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
    </el-row>
  </div>
</template>

<script>

import $ from "jquery";

let protocol = window.location.protocol; //协议
let host = window.location.host; //主机
let reg = /^localhost+/;
let host_l = host.split(":")
let contextRoot = protocol + "//" + host_l[0]  +":8081/";

export default {
  name: "search",
  data(){
    return{

      nodeId: null,
      domainId: null,

      domain: '',
      domainName: '',
      model: {},

      boundedContext: '',
      boundedContextModel:{},

      entity: '',
      entityModel: {},

      attrObj: null,
      userAttrObj: null,
      attr:'',
      attrModel: {},
      event:'',
      eventModel: {},
      mode: '',
      state: '',
      stateModel: {},

      domainList: [],
      contextList: [],
      entityList: [],
      eventList: [],
      stateList: [],
      allModeStateList: [],
      stateByModeList: [],

      modeList: [{id: 0, name: "Production"},
        {id: 1, name: "Maintenance"},
        {id: 2, name: "Manual"}
      ],
      currentPage: 1,
      total: 0,
      pageSize: 15,
      name: '',
    }
  },
  mounted() {
    this.getAllDomain();
  },
  methods:{
    //输出单个
    outPut(m){
      var _this = this;
      if (m === "domain"){
        var obj0 = {"domainName": _this.domain};
        _this.saveToJson(obj0, "domainName");
      }else if(m === "domainModel"){
        var obj00 = _this.model;
        _this.saveToJson(obj00, "domainName");
      }else if(m === "context"){
        var obj1 = {"boundedContextName": _this.boundedContext};
        _this.saveToJson(obj1, "boundedContextName");
      }else if(m === "contextModel"){
        var obj11 = _this.boundedContextModel;
        _this.saveToJson(obj11, "contextName");
      }else if(m === "entity"){
        var obj2 = {"entityName": _this.entity};
        _this.saveToJson(obj2, "entityName");
      }else if(m === "entityModel"){
        var obj22 = _this.entityModel;
        _this.saveToJson(obj22, "name");
      }else if(m === "attr"){
        var obj3 = _this.attrModel;
        var a = document.createElement("a");
        a.download = 'none.json'.replace('none', _this.findKey(obj3, _this.attr));
        var blob = new Blob([JSON.stringify(obj3)], {type: "text/json"});
        a.href = window.URL.createObjectURL(blob);
        // 标签 data- 嵌入自定义属性  屏蔽后也可正常下载
        a.dataset.downloadurl = ["text/json", a.download, a.href].join(":");
        // 添加鼠标事件
        let event = new MouseEvent("click", {});
        // 向一个指定的事件目标派发一个事件
        a.dispatchEvent(event);
      }else if(m === "event"){
        var obj4 = _this.eventModel;
        _this.saveToJson(obj4, "name");
      }else if(m === "state"){
        var obj5 = _this.stateModel;
        obj5["mode"] = _this.mode;
        _this.saveToJson(obj5, "name");
      }
    },
    saveToJson(obj, key){
      // 以下都是: 生成json文件
      var a = document.createElement("a");
      a.download = 'none.json'.replace('none', obj[key]);
      var blob = new Blob([JSON.stringify(obj)], {type: "text/json"});
      a.href = window.URL.createObjectURL(blob);
      // 标签 data- 嵌入自定义属性  屏蔽后也可正常下载
      a.dataset.downloadurl = ["text/json", a.download, a.href].join(":");
      // 添加鼠标事件
      let event = new MouseEvent("click", {});
      // 向一个指定的事件目标派发一个事件
      a.dispatchEvent(event);
    },
    //获取状态图
    async getStateFlow(){
      var _this = this;
      var domainId = _this.contextList.find(function(d){return d.name == _this.boundedContext}).id;
      var nodeId = _this.entityList.find(function(d){return d.name == _this.entity}).value;
      _this.allModeStateList = [];
      for(let m of _this.modeList){
        var mName = m.name;
        console.log(mName);
        _this.stateList = [];
        var data = {domainId: domainId, nodeId: nodeId, mode: mName};
        await $.ajax({
          data: data,
          type: "POST",
          url: contextRoot + "getStateFlow",
          success: function (result) {
            if (result.code == 200) {
              const json = result.data;
              if (json) {
                const data = JSON.parse(json);
                _this.$message({
                  showClose: true,
                  message: result.msg,
                  type: 'success'
                });
                console.log(data);
                //把状态图中的状态抽取出来放数组里
                var edgeList = [];
                for(let state of data.cells){
                  if(state.shape == "rect" && state.size.width < 600){
                    _this.stateList.push({"id": state.id, "name": state.attrs.label.text, "des":state.data.des, "preCommand": "", "postCommand": ""})
                  } else if(state.shape == "edge"){
                    if("attrs" in state.labels[0]){
                      edgeList.push({"text": state.labels[0].attrs.label.text, "sourceId": state.source.cell, "targetId": state.target.cell})
                    }
                  }
                }
                //把命令和状态绑定
                for(let i in _this.stateList){
                  if(edgeList.find(function(d){return d.sourceId == _this.stateList[i].id}) != undefined){
                    _this.stateList[i].postCommand = edgeList.find(function(d){return d.sourceId == _this.stateList[i].id}).text
                  }
                  if(edgeList.find(function(d){return d.targetId == _this.stateList[i].id}) != undefined){
                    _this.stateList[i].preCommand = edgeList.find(function(d){return d.targetId == _this.stateList[i].id}).text
                  }
                }
                _this.allModeStateList.push({Mode: mName, State: _this.stateList});
                console.log(_this.allModeStateList);
              }
            } else {
              this.$message({
                showClose: true,
                message: result.msg,
                type: 'success'
              });
            }
          }
        });
      }
    },
    getStateByMode(){
      var _this = this;
      _this.stateByModeList = [];
      for(let s of _this.allModeStateList){
        if(s.Mode === _this.mode){
          _this.stateByModeList = s.State;
        };
      }
    },
    toStateModel(){
      var _this = this;
      if(_this.state!=""&&_this.state!=null&&_this.state!=undefined){
        var stateObj = _this.entityModel['state'];
        for(let key in stateObj){
          if(stateObj[key]["Mode"] === _this.mode){
            for(let state of stateObj[key]["State"]){
              if(state["name"] === _this.state){
                _this.stateModel = state;
              }
            }
          }
        }}
    },
    //获取事件
    getAllEvent(){
      var _this = this;
      var domainId = _this.contextList.find(function(d){return d.name == _this.boundedContext}).id;
      var nodeId = _this.entityList.find(function(d){return d.name == _this.entity}).value;
      var data = {domainId: domainId, nodeId: nodeId};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getActionList",
        success: function (result) {
          if (result.code == 200) {
            _this.eventList = result.data;
          } else {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'warning'
            });
          }
        }
      });
    },
    toEventModel(){
      var _this = this;
      if(_this.event!=""&&_this.event!=null&&_this.event!=undefined){
        var eventObj = _this.entityModel['event'];
        console.log(eventObj);
        for(let key in eventObj){
          if(eventObj[key]["name"] === _this.event){
            _this.eventModel = eventObj[key];
          }
        }}
    },
    //获取属性
    async getAllAttr(){
      var _this = this;
      var boundedContext = _this.boundedContext;
      var nodeId = _this.entityList.find(function(d){return d.name == _this.entity}).value;
      var data = {domain: boundedContext, nodeid: nodeId};
      await $.ajax({
        data: data,
        type: "POST",
        async: false,
        url: contextRoot + "getNodeAttrById",
        success: function (result) {
          if (result.code == 200) {
            _this.attrObj = result.data;
            _this.getAllUserAttr();
          }
        }
      });
      // console.log(_this.attrObj);
    },
    getAllUserAttr(){
      var _this = this;
      var domainId = _this.contextList.find(function(d){return d.name == _this.boundedContext}).id;
      var nodeId = _this.entityList.find(function(d){return d.name == _this.entity}).value;
      var data = {domainId: domainId, nodeId: nodeId};
      console.log(data);
      $.ajax({
        data: data,
        type: "POST",
        async: false,
        url: contextRoot + "getUserAttrList",
        success: function (result) {
          if (result.code == 200) {
            for(let userAttr of result.data){
              _this.attrObj[userAttr.attrLabel] = userAttr.attrValue;
            };
          }
        }
      });
    },
    toAttrModel(){
      var _this = this;
      _this.attrModel = {};
      if(_this.attr!=""&&_this.attr!=null&&_this.attr!=undefined) {
        var value = _this.attr;
        var key = _this.findKey(_this.attrObj, value)
        _this.attrModel[key]= value;
      }

    },
    findKey(obj, value){
      return Object.keys(obj).find((k)=>{
        return obj[k]==value
      })
    },

    //获取某个自定义属性
    getUserAttr(){
      var _this = this;
      var attrLabel = "";
      var data = {attrLabel: attrLabel};
      console.log(data);
      $.ajax({
        data: data,
        type: "POST",
        async: false,
        url: contextRoot + "getUserAttr",
        success: function (result) {
          if (result.code == 200) {
            console.log("success");
          }
        }
      });
    },
    //获取实体的各种
    getAllOfEntity(){
      var _this = this;
      _this.event='';
      _this.attr='';
      _this.state='';
      _this.getAllAttr();
      _this.getAllEvent();
      _this.getStateFlow();
      _this.toEntityModel();
    },
    //获取实体
    getAllEntity(){
      var _this = this;
      _this.loading = true;
      var contextName = _this.boundedContext;
      var data = {domain: contextName}
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getdomainnodes",
        success: function (result) {
          if (result.code == 200) {
            _this.entityList = result.data;
            _this.entity='';
            _this.event='';
            _this.attr='';
            _this.state='';
          }
        }
      });
      // console.log(_this.entityList);
      _this.toBoundedContextModel();

    },
    toEntityModel(){
      var _this = this;
      if(_this.entity!=""&&_this.entity!=null&&_this.entity!=undefined){
        var entityObj = _this.boundedContextModel["BoundedContext"]['entityList'][0];
        for(let key in entityObj){
          if(entityObj[key]["name"] === _this.entity){
            _this.entityModel = entityObj[key];
          }
        }
      }

      // _this.entityModel = model.
    },
    //获取领域
    getAllDomain() {
      var _this = this;
      _this.$axios.get("/sservices?currentPage=" + this.currentPage + "&pageSize=" + this.pageSize + "&name=" + this.name).then(res => {
        _this.domainList = res.data.data.records;
        _this.currentPage = res.data.data.current;
        _this.total = res.data.data.total;
        _this.pageSize = res.data.data.size;
      })
      // console.log(_this.domainList);
    },
    toModelNotInput() {
      var _this = this;
      if(_this.domain!=""&&_this.domain!=null&&_this.domain!=undefined){
        var domainId = _this.domainList.find(function(d){return d.name == _this.domain}).id;
        var data = {sserviceId: domainId};
        $.ajax({
          data: data,
          type: "POST",
          url: contextRoot + "toModelNotInput",
          success: function (result) {
            if (result.code == 200) {
              _this.model = JSON.parse(result.data)['Model'];
              console.log(_this.model);
              _this.$message({
                showClose: true,
                message: result.msg,
                type: 'success'
              });
            } else {
              _this.$message({
                showClose: true,
                message: result.msg,
                type: 'warning'
              });
            }
          }
        });
      }
    },
    //获取上下文
    getAllBoundedContext() {
      var _this = this;
      var domainId = _this.domainList.find(function(d){return d.name == _this.domain}).id;
      var data = {sserviceId: domainId};
      // console.log(data);
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getAllDomainsBySsId",
        success: function (result) {
          if (result.code == 200) {
            _this.contextList = result.data;
            _this.boundedContext = '';
            _this.entity='';
            _this.event='';
            _this.attr='';
            _this.state='';
          } else {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'warning'
            });
          }
        }
      });
      // console.log(_this.contextList);
      _this.toModelNotInput();
    },
    toBoundedContextModel(){
      var _this = this;
      if(_this.boundedContext!=""&&_this.boundedContext!=null&&_this.boundedContext!=undefined){
        var contextList = _this.model['Domain'];
        for(let context of contextList){
          if(context["contextName"] === _this.boundedContext){
            _this.boundedContextModel = context;
          }
        }}
    },
  }
}
</script>

<style scoped>
.row{
  text-align: center; /*居中*/
}
.el-row {
  margin-bottom: 20px;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

/*card*/
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
.knowledgeSelect{
  float: left;
}
.text {
  font-size: 16px;
}
.box-card {
  margin-left: 20px;
  margin-right: 20px;
  width: 550px;
  display: inline-block; /*居中*/
}
.outputButton{
  font-size: 16px;
}
</style>
