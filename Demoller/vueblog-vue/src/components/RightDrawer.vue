<template xmlns="http://www.w3.org/1999/html">
	<div class="drawer_container">
		<div v-if="drawerType === 'grid'">
			<div class="drawer_title">画布背景设置</div>
			<div class="drawer_wrap">
				<el-form label-position="left" label-width="85px">
					<el-form-item label="是否显示网格" label-width="100px">
						<el-switch v-model="showGrid" @change="ifShowGrid" />
					</el-form-item>
					<div v-show="showGrid">
						<el-form-item label="网格类型">
							<el-radio-group v-model="grid.type" @change="changeGridType">
								<el-radio v-for="item in gridTypeList" :label="item.value" :key="item.value">
									<span>{{item.label}}</span>
								</el-radio>
							</el-radio-group>
						</el-form-item>
						<el-form-item label="网格大小">
							<el-slider v-model="grid.size" :min="0" :max="30" @change="changeGrid"></el-slider>
						</el-form-item>
            <el-form-item label="网格平移">
              <el-switch v-model="panning" @change="ifPanning"></el-switch>
            </el-form-item>
						<el-form-item label="网格颜色">
							<el-color-picker v-model="grid.args.color" @change="changeGrid"/>
						</el-form-item>
						<el-form-item label="网格线宽度">
							<el-slider v-model="grid.args.thickness" :min="0" :max="20" @change="changeGrid"></el-slider>
						</el-form-item>
            <el-form-item label="节点">
              <el-input v-model="nodeId"></el-input>
            </el-form-item>
            <el-form-item label="上下文">
              <el-input v-model="domainId"></el-input>
            </el-form-item>
					</div>
				</el-form>
			</div>
		</div>
		<div v-if="drawerType === 'node'">
			<div class="drawer_title">节点设置</div>
			<div class="drawer_wrap">
				<el-form label-position="left" label-width="80px">
					<el-form-item label="节点文本">
						<el-input v-model="drawerNode.nodeText" @change="changeNodeText"></el-input>
					</el-form-item>
          <el-form-item label="状态描述">
            <el-input v-model="drawerNode.nodeDes" @change="changeNodeDes"></el-input>
          </el-form-item>
          <el-form-item label="可切换至">
            <el-checkbox-group v-model="drawerNode.nodeTranToMode" @change="changeNodeTrans">
              <el-checkbox v-for="mode in modeList" :label="mode" :key="mode">{{mode}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
					<el-form-item label="节点背景">
            <el-color-picker v-model="drawerNode.fill" @change="changeFill"/>
          </el-form-item>
          <el-form-item label="节点类型">
            <el-switch
                    v-model="drawerNode.fill"
                    active-text="Wait"
                    inactive-text="Acting"
                    v-if="!drawerNode.parent"
                    active-color="#EE8B8B"
                    inactive-color="#83EAA0"
                    active-value="#EE8B8B"
                    inactive-value="#83EAA0"
                    @change="changeFill">
                </el-switch>
          </el-form-item>
					<el-form-item label="字体大小">
						<el-slider v-model="drawerNode.fontSize" :min="10" :max="20" @change="changefontSize"></el-slider>
					</el-form-item>
					<el-form-item label="字体颜色">
							<el-color-picker v-model="drawerNode.fontFill" @change="changeFontFill"/>
					</el-form-item>
					<el-form-item label="边框宽度">
						<el-slider v-model="drawerNode.strokeWidth" :min="0" :max="10" @change="changeStrokeWidth"></el-slider>
					</el-form-item>
					<el-form-item label="边框颜色">
						<el-color-picker v-model="drawerNode.stroke" @change="changeStroke"/>
					</el-form-item>
					<el-form-item label="功能">
            <el-button type="primary" plain size="small" icon="md-trending-up" @click="toTopZIndex">置顶</el-button>
            <el-button type="primary" plain size="small" icon="md-trending-up" @click="toBottomZIndex">置底</el-button>
            <br>
            <el-button type="primary" plain size="small" icon="md-trending-up" @click="changePort" v-if="!drawerNode.parent">增加port</el-button>
            <el-button type="danger" plain size="small"  class="margin-left-10" icon="md-trash" @click="deleteNode">删除</el-button>
            <br>
            <el-button type="primary" plain size="small" icon="md-trending-up" @click="actionEditVis" v-if="!drawerNode.parent">动作编辑</el-button>

          </el-form-item>
				</el-form>
			</div>
		</div>
		<div v-if="drawerType === 'edge'">
			<div class="drawer_title">线条设置</div>
			<div class="drawer_wrap">
				<el-form label-position="left" label-width="80px">
          <el-form-item label="线条命令">
            <el-select v-model="drawerEdge.EdgeText" placeholder="请选择" @change="changeEdgeText">
              <el-option
                  v-for="item in commandList_this"
                  :key="item.id"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>
					<el-form-item label="线条宽度">
						<el-slider v-model="drawerEdge.edgeWidth" :min="1" :max="10" @change="changeEdgeWidth"></el-slider>
					</el-form-item>
          <el-form-item label="字体大小">
            <el-slider v-model="drawerEdge.fontSize" :min="5" :max="20" @change="changeEdgeTextSize"></el-slider>
          </el-form-item>
					<el-form-item label="线条颜色">
						<el-color-picker v-model="drawerEdge.edgeColor" @change="changeEdgeColor"/>
					</el-form-item>
					<el-form-item label="功能">
            <el-button type="primary" plain icon="md-trending-up" @click="toTopZIndex">置顶</el-button>
						<el-button type="danger" plain class="margin-left-10" icon="md-trash" @click="deleteNode">删除</el-button>
					</el-form-item>
				</el-form>
			</div>
		</div>
    <!-- 动作列表的对话框 -->
    <el-dialog title="动作列表" :visible.sync="actionEditVisible" width="50%">
      <el-table :data="actionList" style="width: 100%" :default-sort = "{prop: 'num', order: 'ascending'}">
        <el-table-column prop="num" label="序号" sortable width="100">
        </el-table-column>
        <el-table-column prop="mode" label="模式" width="150">
        </el-table-column>
        <el-table-column prop="stateName" label="状态名称" width="150">
        </el-table-column>
        <el-table-column prop="des" label="描述" width="200">
        </el-table-column>
        <el-table-column label="操作" width="300">
          <template slot-scope="scope">
            <el-button
                size="mini" @click="paramsEditVis(scope.$index, scope.row)">编辑参数</el-button>
            <el-button
                size="mini"
                @click="updateActionVis(scope.$index, scope.row)">编辑</el-button>
            <el-button
                size="mini"
                type="danger"
                @click="deleteAction(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="actionAddVis">添加动作</el-button>
      </span>
    </el-dialog>
    <!-- 添加事件的对话框 -->
    <el-dialog title="添加动作" :visible.sync="actionAddVisible" width="25%">
      <!-- 内容的主体区域 -->
      <el-form :model="actionForm" label-width="70px">
        <el-form-item label="顺序" prop="num">
          <el-input v-model="actionForm.num"></el-input>
        </el-form-item>
        <el-form-item label="模式" prop="mode">
          <el-input v-model="actionForm.mode" disabled></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="stateName">
          <el-input v-model="actionForm.stateName" disabled></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="des">
          <el-input v-model="actionForm.des"></el-input>
        </el-form-item>

      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="actionAddVisible = false">取 消</el-button>
        <el-button type="primary" @click="addAction">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 更新事件的对话框 -->
    <el-dialog title="更改动作" :visible.sync="actionUpdateVisible" width="25%">
      <!-- 内容的主体区域 -->
      <el-form :model="actionUpdateForm" label-width="70px">
        <el-form-item label="顺序" prop="num">
          <el-input v-model="actionUpdateForm.num"></el-input>
        </el-form-item>
        <el-form-item label="模式" prop="mode" >
          <el-input v-model="actionUpdateForm.mode" disabled></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="stateName">
          <el-input v-model="actionUpdateForm.stateName" disabled></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="des">
          <el-input v-model="actionUpdateForm.des"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="actionUpdateVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateAction">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 添加参数的对话框 -->
    <el-dialog title="添加参数" :visible.sync="paramAddVisible" width="25%">
      <!-- 内容的主体区域 -->
      <el-form :model="actionForm" label-width="70px">
        <el-form-item label="顺序" prop="num">
          <el-input v-model="actionForm.num"></el-input>
        </el-form-item>
        <el-form-item label="模式" prop="mode">
          <el-input v-model="actionForm.mode" disabled></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="stateName">
          <el-input v-model="actionForm.stateName" disabled></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="des">
          <el-input v-model="actionForm.des"></el-input>
        </el-form-item>

      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="actionAddVisible = false">取 消</el-button>
        <el-button type="primary" @click="addAction">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 更新参数的对话框 -->
    <el-dialog title="参数编辑" :visible.sync="paramsEditVisible" width="25%">
      <!-- 内容的主体区域 -->
      <el-row style="margin-bottom: 20px;">
        <el-form>
          <el-select v-model="selectValue_params" value-key="name" clearable placeholder="请选择参数" @change="isParamEdit">
            <el-option
                v-for="item in paramsList"
                :key="item.id"
                :label="item.name"
                :value="item">
            </el-option>
          </el-select>
          <el-button style="margin-left: 20px" @click="isParamAdd">新增</el-button>
      </el-form>
      </el-row>
      <el-form style="margin-top: 15px;margin-left: 50px" v-show="iseditapi">
        <el-form-item label="名称" border>
          <el-input style="width: 40%" v-model="selectValue_params.name"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-input style="width: 40%" title="type" v-model="selectValue_params.type"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input style="width: 40%" title="des" v-model="selectValue_params.des"></el-input>
        </el-form-item>
        <el-form-item style="margin-left: 250px">
          <el-button type="primary" @click="updateParam" plain>修改</el-button>
          <el-button type="danger" @click="deleteParam" plain>删除</el-button>
        </el-form-item>
      </el-form>
      <!--新增表单-->
      <el-form style="margin-top: 15px;margin-left: 50px" v-show="isaddapi">
        <el-form-item label="名称" border>
          <el-input style="width: 40%" v-model="param.name"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-input style="width: 40%" title="type" v-model="param.type"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input style="width: 40%" title="des" v-model="param.des"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="margin-left: 350px" @click="addParam">确定</el-button>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
    </el-dialog>
  </div>
</template>

<script>
import $ from "jquery";

// let contextRoot = "http://localhost:8081/";
let protocol = window.location.protocol; //协议
let host = window.location.host; //主机
let reg = /^localhost+/;
let host_l = host.split(":")
let contextRoot = protocol + "//" + host_l[0] + ":8081/";


export default {
	name:'RightDrawer',
	data() {
		return {
      param:{},
      actionId: "",
      typeOptions: [{id: 0, command: "Abort(safety)", detail: "Fast stop in case of safety event"},
        {id: 1, command: "Abort(severe)", detail: "Fast stop in case of severe machine fault"},
        {id: 2, command: "Stop", detail: "Process stop: Stop accepting input, run out machine, slow decel"},
        {id: 3, command: "Hold", detail: "Slow decel in case of Inot severe internal error"},
        {id: 4, command: "Suspend(blocker)", detail: "Machine blocker: slow decel when downstream equipment is not available"},
        {id: 5, command: "Suspend(starved)", detail: "Machine starved: slow decel when raw materials missing"},
        {id: 6, command: "-", detail: "Status event"},
      ],
      paramsList: [],
      selectValue_params: '',
      paramAddVisible: false,
      paramsEditVisible: false,
      actionForm: {
        mode: "",
        stateName: "",
        des: "",
      },
      actionUpdateForm: {
        mode: "",
        stateName: "",
        des: "",
      },
      modeList: ["Production", "Maintenance", "Manual"],
		  actionList:[
      ],
      actionAddVisible: false,
      actionUpdateVisible: false,
      isaddapi: false,
      iseditapi: false,
      actionEditVisible: false,
			gridTypeList:[
				{
					label:'四边网格',
					value:'mesh'
				},
				{
					label:'点状网格',
					value:'dot'
				}
			],
      panning:false,
			showGrid:true,
			drawerNode:{
        nodeTranToMode: [],
        nodeDes: "",
				fill:'#EE8B8B',
				nodeText:'',
				fontSize:null,
				fontFill:'',
				strokeWidth:null,
				stroke:'',
        parent: false,
			},
			drawerEdge:{
				EdgeText:'',
				edgeWidth:null,
				edgeColor:'',
        fontSize: 0
			},
      nodeId_this: "",
      domainId_this: 0,
      commandList_this: [
        {nodeId: 0, domainId: 0}
      ],
		}
	},
	props:{
    mode: {
      type: String
    },
    eventList: {
      type: Array | Object
    },
    commandList: {
      type: Array | Object
    },
    nodeId: {
      type: String | Number
    },
    domainId: {
      type :Number
    },
		drawerType: {
			type: String
		},
		selectCell:{
			type: String | Object
		},
		graph:{
			type: String | Object
		},
		grid:{
			type: Object
		}
	},
	created() {
	  var m = this.mode;
    this.modeList.forEach(function(item, index, arr) {
      if(item == m) {
        arr.splice(index, 1);
      }
    });
    // console.log(this.modeList);
	},
	mounted() {

  },
	watch:{
		selectCell: {
			handler(val) {
				if (val) {
					if (val.isNode()) { //节点
						this.drawerNode.fill = val.store.data.attrs.body.fill
						this.drawerNode.nodeText = val.store.data.attrs.label.text
            this.drawerNode.nodeDes = val.store.data.data.des
            this.drawerNode.nodeTranToMode = val.store.data.data.nodeTranToMode
            this.drawerNode.fontFill = val.store.data.attrs.label.fill
						this.drawerNode.fontSize = Number(val.store.data.attrs.label.fontSize)
						this.drawerNode.strokeWidth = Number(val.store.data.attrs.body.strokeWidth)
						this.drawerNode.stroke = val.store.data.attrs.body.stroke
            this.drawerNode.parent = val.store.data.data.parent

					} else { //边
						this.drawerEdge.EdgeText = val.store.data.labels ? val.store.data.labels[0].text : ''
						this.drawerEdge.edgeWidth = Number(val.store.data.attrs.line.strokeWidth)
						this.drawerEdge.edgeColor = val.store.data.attrs.line.stroke
            this.drawerEdge.fontSize = Number(val.store.data.attrs.text.fontSize)
					}
				}
			},
			immediate: true,
			deep: false
		},
    commandList: {
      handler(curVal, oldVal) {
        if (curVal) {
          this.commandList_this = curVal;
        }
      },
      deep:true //深度监听
    },
	},
	methods: {
    deleteParam(){
      var _this = this;
      var data = {paramId: _this.selectValue_params.id}
      console.log(data);
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "deleteParamByParamId",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.cleanParam();
            _this.getAllParams();
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
    updateParam(){
      var _this = this;
      var data = _this.selectValue_params;
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "updateParam",
        // contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.cleanParam();
            _this.getAllParams();
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
    getAllParams(){
      var _this = this;
      var data = {actionId: _this.actionId};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getParamsByActionId",
        success: function (result) {
          if (result.code == 200) {
            _this.paramsList = result.data;
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

    },
    addParam(){
      var _this = this;
      var data = _this.param;
      data.actionId = _this.actionId;
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "addParam",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.cleanParam();
            _this.getAllParams();
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
    cleanParam(){
      var _this = this;
      _this.isaddapi = false;
      _this.iseditapi = false;
      _this.param = {};
      _this.selectValue_params = '';
    },
    isParamEdit() {
      var _this = this;
      _this.iseditapi = true;
      _this.isaddapi = false;
    },
    isParamAdd() {
      var _this = this;
      _this.selectValue_params = '';
      _this.isaddapi = true;
      _this.iseditapi = false
    },
    paramsEditVis(index, row){
      var _this = this;
      this.actionId = row.id;
      this.paramsList = row.params;
      this.paramsEditVisible = true;
      this.getAllParams();
    },
    deleteAction(index, row){
      var _this = this;
      var data = {stateActionId: row.id};
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: contextRoot + "deleteStateAction",
        // contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.getActionList();
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
	  updateAction(){
      var _this = this;
      _this.actionUpdateForm.nodeId = _this.nodeId;
      _this.actionUpdateForm.domainId = _this.domainId;
      var data = _this.actionUpdateForm;
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "updateStateAction",
        // contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.actionUpdateVisible = false;
            _this.getActionList();
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
    updateAction2(newName, oldName){
      var _this = this;
      var data = {};
      data.nodeId = _this.nodeId;
      data.domainId = _this.domainId;
      data.newName = newName;
      data.oldName = oldName
      console.log(data);
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "updateStateActionName",
        // contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
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
    },
    updateActionVis(index, row){
      var _this = this;
      this.actionUpdateForm.num = row.num;
      this.actionUpdateForm.mode = row.mode;
      this.actionUpdateForm.stateName = row.stateName;
      this.actionUpdateForm.des = row.des;
      this.actionUpdateForm.id = row.id
      _this.actionUpdateVisible = true;
    },
	  //获取动作列表
    getActionList(){
      var _this = this;
      var data = {domainId: _this.domainId, nodeId: _this.nodeId, stateName: _this.drawerNode.nodeText, mode: _this.mode};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getStateActionList",
        success: function (result) {
          if (result.code == 200) {
            _this.actionList = result.data;
            // console.log(_this.actionList);
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
    },
	  //新增动作
    addAction(){
      var _this = this;
      _this.actionForm.nodeId = _this.nodeId;
      _this.actionForm.domainId = _this.domainId;
      var data = _this.actionForm;
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "addStateAction",
        // contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.actionAddVisible = false;
            _this.getActionList();
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
	  //动作编辑
    actionAddVis () {
      this.actionForm.num = this.actionList.length + 1;
      this.actionForm.mode = this.mode;
      this.actionForm.stateName = this.drawerNode.nodeText;
      this.actionForm.des = [];
      this.actionAddVisible = true;
    },
    actionEditVis () {
      this.getActionList();
      this.actionEditVisible = true;
    },

		// 网格设置
    ifShowGrid () {
			this.showGrid ? this.graph.showGrid() : this.graph.hideGrid()
		},
    ifPanning () {
      this.panning ? this.graph.enablePanning() : this.graph.disablePanning()
    },
		changeGridType (e) {
			this.grid.type = e
			this.changeGrid()
		},
		changeGrid () {
			this.graph.drawGrid({
				...this.grid
			})
		},
		// 节点设置
    changeNodeTrans(){
      this.selectCell.setData({nodeTranToMode: this.drawerNode.nodeTranToMode})
    },
    changePort(){
      this.selectCell.removePorts();
      this.selectCell.addPorts([
        {
          id: 'port1',
          group: 'top',
        },
        {
          id: 'port2',
          group: 'bottom',
        },
        {
          id: 'port3',
          group: 'left',
        },
        {
          id: 'port4',
          group: 'right',
        },
        {
          id: 'port11',
          group: 'top',
        },
        {
          id: 'port22',
          group: 'bottom',
        },
        {
          id: 'port33',
          group: 'left',
        },
        {
          id: 'port44',
          group: 'right',
        },
        {
          id: 'port111',
          group: 'top',
        },
        {
          id: 'port222',
          group: 'bottom',
        },
        {
          id: 'port333',
          group: 'left',
        },
        {
          id: 'port444',
          group: 'right',
        },
      ]);
    },
    changeNodeDes(){
      this.selectCell.setData({des: this.drawerNode.nodeDes})
    },
		changeStrokeWidth (val) {
			this.selectCell.attr('body/strokeWidth', val)
		},
		changefontSize (val) {
			this.selectCell.attr('label/fontSize',val)
		},
		changeNodeText () {
      var oldName = this.selectCell.getAttrByPath('label/text');
			this.selectCell.attr('label/text', this.drawerNode.nodeText)
      this.updateAction2(this.drawerNode.nodeText, oldName);
		},
		changeStroke (val) {
			this.drawerNode.stroke = val
			this.selectCell.attr('body/stroke', this.drawerNode.stroke)
		},
		changeFontFill (val) {
			this.drawerNode.fontFill = val
			this.selectCell.attr('label/fill', this.drawerNode.fontFill)
		},
		changeFill (val) {
			this.drawerNode.fill = val
			this.selectCell.attr('body/fill', val)
		},

		// 边设置
		changeEdgeText () {
			console.log(this.drawerEdge.EdgeText);
			this.selectCell.setLabels(
				[{attrs:{label:{text:this.drawerEdge.EdgeText}}}]
			)
		},
    changeEdgeTextSize (val) {
      this.drawerEdge.fontSize = val
      this.selectCell.attr('text/fontSize',  this.drawerEdge.fontSize)
    },
		changeEdgeWidth (val) {
			this.drawerEdge.edgeWidth = val
			this.selectCell.attr('line/strokeWidth', this.drawerEdge.edgeWidth)
		},
		changeEdgeColor (val) {
			this.drawerEdge.stroke = val
			this.selectCell.attr('line/stroke', this.drawerEdge.stroke)
		},
		// 置顶
		toTopZIndex () {
			this.selectCell.toFront()
		},
    // 置底
    toBottomZIndex () {
      this.selectCell.toBack()
    },
		// 删除
		deleteNode () {
			this.$emit('deleteNode')
		}
	}
}
</script>

<style lang="less" scoped>
  .drawer_container {
	  max-width: 300px;
	  min-width: 300px;
	.drawer_title {
		border-bottom: 1px solid #e8eaec;
		box-sizing: border-box;
		padding: 14px 16px;
		color: #333;
		font-size: 16px;
	}
	.drawer_wrap {
		box-sizing: border-box;
		padding: 20px 10px 20px 20px;
	}
}
</style>
