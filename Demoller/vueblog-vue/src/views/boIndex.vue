<template>
  <div class="container">
    <el-container style="height: 100%" direction="vertical">
      <UbmlHeader></UbmlHeader>
      <div class="mind-box">
        <!-- 左侧 -->
        <el-scrollbar class="mind-l" style="width: 18%">
          <div class="ml-m">
            <h2 class="ml-ht">上下文列表</h2>
            <div class="ml-a-box" style="min-height:280px">
              <el-button type="info" style="margin: 2px 0 4px 2px;" plain size="small" @click="createdomain">新建上下文
<!--              <el-button type="info" style="margin: 2px 0 4px 2px;" plain size="small" @click="createdomain">Add context-->
              </el-button>
              <a @click="getNode2(m.name), matchdomaingraph(m,$event), removeDom(), node1=''" v-for="m in pageModel.nodeList"
                 href="javascript:void(0)">
                <el-tag closable style="margin:2px" @close="deletedomain(m.id,m.name)">{{ m.name }}</el-tag>
              </a>
              <el-button v-if="pageModel.pageIndex<pageModel.totalPage" type="info" style="margin: 2px 0 4px 2px;" plain
                         size="small" @click="getmoredomain">加载更多
              </el-button>
            </div>
          </div>
          <div class="ml-m">
            <h2 class="ml-ht-st">实体列表</h2>
<!--            <h2 class="ml-ht-st">List of entities</h2>-->
            <div class="ml-a-box" style="min-height:280px">
              <el-radio-group v-model="nodeName2" v-for="(n ,index) in nodesList" :key="index" @change="getAll(n.value)">
                <el-radio-button style="margin:2px" :label="n.name">{{ n.name }}</el-radio-button>
              </el-radio-group>
            </div>
          </div>
        </el-scrollbar>
        <!-- 左侧over -->
        <!-- 右侧 -->
        <div class="mind-con">
          <!-- 头部 -->
          <div class="mind-top clearfix">
            <div class="fl">
              <div class="search" v-show="domain!=''">
                <el-button @click="getdomaingraph(0)">
                  <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-search"></use>
                  </svg>
                </el-button>
                <el-input placeholder="请输入关键字" v-model="nodename" @keyup.enter.native="getdomaingraph"></el-input>
              </div>
              <span v-show="domain!=''">
	            <span class="dibmr" v-show="dibmr">
				        <span>The number of nodes：</span>
                <a v-for="(m,index) in pagesizelist" @click="setmatchsize(m,this)" :title="m.size"
                   href="javascript:void(0)"
                   :class="[m.isactive ? 'sd-active' : '', 'sd']">{{ m.size }}</a>
			        </span>
          </span>
            </div>
            <div class="fr">
              <a href="javascript:void(0)" @click="contextDetailShow(), getAllMsg(), getDomain()" class="svg-a-sm">
                <i class="el-icon-toilet-paper">上下文细节</i>
              </a>
              <a href="javascript:void(0)" @click="wordTableShow(), getAllWord()" class="svg-a-sm">
                <i class="el-icon-grape">词汇表</i>
              </a>
              <a href="javascript:void(0)" @click="cypherjson" class="svg-a-sm">
                <i class="el-icon-tickets">显示Json</i>
              </a>
              <!--              <a href="javascript:void(0)" @click="showCypher" class="svg-a-sm">-->
              <!--                <i class="el-icon-caret-right">执行Cypher</i>-->
              <!--              </a>-->
              <a href="javascript:void(0)" @click="updategraph" class="svg-a-sm">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-shuaxin"></use>
                </svg>
                刷新
              </a>
              <a href="javascript:void(0)" @click="toAas" class="svg-a-sm">
                <i class="el-icon-guide" v-text="convertNameList[0]"></i>
              </a>
              <a href="javascript:void(0)" @click="requestFullScreen" class="svg-a-sm">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-quanp"></use>
                </svg>
                全屏
              </a>
              <el-dropdown @command="operateCommand">
                <el-button type="primary">
                  输入/输出<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="image">导出图片</el-dropdown-item>
                  <el-dropdown-item command="import">导入</el-dropdown-item>
                  <el-dropdown-item command="export">导出</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
          <div class="cypher_toolbar clearfix" v-show="cyphertextshow">
            <div style="width: 80%;float: left">
              <el-input type="textarea" :rows="2" placeholder="请输入Cypher" v-model="cyphertext"></el-input>
            </div>
            <div style="padding: 7px;">
              <el-button type="success" @click="cypherrun" style="margin-left: 15px;" icon="el-icon-caret-right" plain>
                执行
              </el-button>
            </div>
          </div>
          <!-- 头部over -->
          <!-- 中部1 编排动作和状态 -->
          <el-scrollbar class="mind-cen" id="actionAndStatusDiv" v-show="actionAndStatusShow">
            <div class="common-layout">
              <StateFlow :actionAndStatusShow="actionAndStatusShow" :actionList="actionList" :orderList="orderList" :selectnodeid="selectnodeid" :domainid="domainid" :key="selectnodeid + domainid + actionAndStatusShow"></StateFlow>
                <!--状态图-->
                <!--动作编排-->
<!--                <el-main>-->
<!--                  <div style="margin-left: 1325px;margin-bottom: -40px">-->
<!--                    <el-button @click="getAll()" type="success" plain>保存</el-button>-->
<!--                    <el-button @click="getAll()" type="danger" plain>清空</el-button>-->
<!--                  </div>-->
<!--                  <div class="step_container">-->
<!--                    <template v-for="(stepNum, index) in stepNums">-->
<!--                      <div class="step_row_data" :key="index">-->
<!--                        <div class="step_column_data">-->
<!--                          <span v-if="!stepNum.step_explain" @click="titleInputShow(index)">-->
<!--                            {{ stepNum.step_title }}-->
<!--                          </span>-->
<!--                          <input v-else type="text" ref="step_explain" @keyup="(e) =>explainEnter(e.keyCode, index)"-->
<!--                                 v-model="stepNum.step_title" @blur="() => stepNum.step_explain = false"-->
<!--                                 @change="updateStep(stepNum)"-->
<!--                                 class="step_explain">-->
<!--                        </div>-->
<!--                        <div class="step_column_data">-->
<!--                          <div class="step_num">-->
<!--                            {{ stepNum.step_num }}-->
<!--                          </div>-->
<!--                          <div class="step_time">-->
<!--                            <span v-if="!stepNum.timeInputVisible"-->
<!--                                  @click="showTimeInput(index)">{{ stepNum.step_time }}</span>-->
<!--                            <input v-else type="text" ref="time_input" v-model="stepNum.step_time"-->
<!--                                   @keyup="(e) =>timeEnter(e.keyCode, index)"-->
<!--                                   @blur="() => stepNum.timeInputVisible = false" class="time_input"-->
<!--                                   @change="updateStep(stepNum)">-->
<!--                          </div>-->
<!--                          <div class="step_right">-->
<!--                            <el-tag-->
<!--                                v-for="(tag, index1) in stepNum.tags"-->
<!--                                closable-->
<!--                                :key="tag.tagName + index1"-->
<!--                                :type="tag.tagType"-->
<!--                                :disable-transitions="false"-->
<!--                                @contextmenu="contextMenu"-->
<!--                                @close="handleClose(tag, index, index1)"-->
<!--                                style="height: 40px;line-height: 36px">-->
<!--                              {{ tag.tagName }}-->
<!--                            </el-tag>-->
<!--                            <el-select-->
<!--                                v-if="stepNum.inputVisible"-->
<!--                                v-model="selectFlowValue"-->
<!--                                value-key="name"-->
<!--                                clearable-->
<!--                                placeholder="请选择"-->
<!--                                @change="handleSelectConfirm(index)"-->
<!--                                style="margin-bottom: 15px;width: 100px">-->
<!--                              <el-option-->
<!--                                  v-for="item in flowList"-->
<!--                                  :key="item.name"-->
<!--                                  :label="item.name"-->
<!--                                  :value="item">-->
<!--                              </el-option>-->
<!--                            </el-select>-->
<!--                            <el-button v-else class="button-new-tag" size="small" @click="showInput(index)">+ New Tag-->
<!--                            </el-button>-->
<!--                            <el-card class="box-card" style="width: 1200px">-->
<!--                              <el-table :data="processData[index+1]" style="width: 100%">-->
<!--                                <el-table-column type="expand">-->
<!--                                  <template slot-scope="props">-->
<!--                                    <el-form label-position="left" inline class="demo-table-expand">-->
<!--                                      <el-form-item label="ID">-->
<!--                                        <span>{{ props.row.id }}</span>-->
<!--                                      </el-form-item>-->
<!--                                      <el-form-item label="名称">-->
<!--                                        <span>{{ props.row.name }}</span>-->
<!--                                      </el-form-item>-->
<!--                                      <el-form-item label="类型">-->
<!--                                        <span>{{ props.row.type }}</span>-->
<!--                                      </el-form-item>-->
<!--                                      <el-form-item label="描述">-->
<!--                                        <span>{{ props.row.desc }}</span>-->
<!--                                      </el-form-item>-->
<!--                                    </el-form>-->
<!--                                  </template>-->
<!--                                </el-table-column>-->
<!--                                <el-table-column-->
<!--                                    label="类型"-->
<!--                                    prop="type">-->
<!--                                  <template slot-scope="scope">-->
<!--                                    <el-select-->
<!--                                        v-model="scope.row.type"-->
<!--                                        value-key="name"-->
<!--                                        clearable-->
<!--                                        @visible-change="addTagList(index)"-->
<!--                                        @change="updateProcess(scope.row)"-->
<!--                                        placeholder="请选择">-->
<!--                                      <el-option-->
<!--                                          v-for="item in tagSelectList"-->
<!--                                          :key="item.tagName"-->
<!--                                          :label="item.tagName"-->
<!--                                          :value="item.tagName">-->
<!--                                      </el-option>-->
<!--                                    </el-select>-->
<!--                                  </template>-->
<!--                                </el-table-column>-->
<!--                                <el-table-column-->
<!--                                    label="名称"-->
<!--                                    prop="name">-->
<!--                                  <template slot-scope="scope">-->
<!--                                    <el-select-->
<!--                                        v-model="scope.row.name"-->
<!--                                        value-key="name"-->
<!--                                        clearable-->
<!--                                        @visible-change="addNameList(scope.row.type)"-->
<!--                                        @change="updateProcess(scope.row)"-->
<!--                                        placeholder="请选择">-->
<!--                                      <el-option-->
<!--                                          v-for="item in nameSelectList"-->
<!--                                          :key="item.name"-->
<!--                                          :label="item.name"-->
<!--                                          :value="item.name">-->
<!--                                      </el-option>-->
<!--                                    </el-select>-->
<!--                                  </template>-->
<!--                                </el-table-column>-->
<!--                                <el-table-column-->
<!--                                    label="描述"-->
<!--                                    props="des">-->
<!--                                  <template slot-scope="props">-->
<!--                                    <span v-if="!props.row.descVisible"-->
<!--                                          @click="descInputShow(props)">{{ props.row.des }}</span>-->
<!--                                    <input v-else ref="descVisible" v-model="props.row.des"-->
<!--                                           @keyup="(e) =>descEnter(e.keyCode, props)"-->
<!--                                           @blur="() => props.row.descVisible = false"-->
<!--                                           @change="updateProcess(props.row)">-->
<!--                                  </template>-->
<!--                                </el-table-column>-->
<!--                                <el-table-column label="操作">-->
<!--                                  <template #default="scope">-->
<!--                                    <el-button style="padding-right: 0px; margin-top: 10px"-->
<!--                                               @click="isProcessExist(index, scope.$index, scope.row)">确定-->
<!--                                    </el-button>-->
<!--                                    <el-button-->
<!--                                        type="danger" style="padding-right: 0px; margin-top: 10px"-->
<!--                                        @click="deleteProcess(scope.$index, scope.row)">删除-->
<!--                                    </el-button>-->
<!--                                  </template>-->
<!--                                </el-table-column>-->
<!--                              </el-table>-->
<!--                              <div class="addTableColumn">-->
<!--                                <i @click="addTableColumn(index+1)" class="el-icon-circle-plus-outline"></i>-->
<!--                              </div>-->
<!--                            </el-card>-->
<!--                          </div>-->
<!--                        </div>-->
<!--                      </div>-->
<!--                    </template>-->
<!--                    <div class="addStep">-->
<!--                      <i @click="addStep" class="el-icon-plus"></i>-->
<!--                    </div>-->
<!--                    <div class="decreaseStep">-->
<!--                      <i @click="decreaseStep" class="el-icon-minus"></i>-->
<!--                    </div>-->
<!--                  </div>-->
<!--                </el-main>-->

            </div>
          </el-scrollbar>
          <!-- 中部1over -->
          <!-- 中部2 -->
          <el-scrollbar class="mind-cen" id="graphcontainerdiv" v-show="graphContainerShow">
            <div id="nodedetail" class="node_detail">
              <h5>详细数据</h5>
              <span class="node_pd" v-for="(m,k) in nodedetail">{{ k }}:{{ m }}</span>
            </div>
            <el-scrollbar style="position:absolute" v-show="jsonshow" id="jsoncontainer" class="jsoncontainer">
              <json-viewer :value=graph :expand-depth=2 copyable></json-viewer>
            </el-scrollbar>
            <el-scrollbar style="position:absolute" v-show="isContextDetail" id="contextdetailcontainer"
                          class="jsoncontainer">
              <el-form style="margin-left: 5px" :inline="true" :model="context" class="demo-form-inline">
                <el-form-item label="名称">
                  <el-input v-model="domain" placeholder="上下文名称" @blur="updateDomain"></el-input>
                </el-form-item>
                <el-form-item label="类型">
                  <el-select v-model="context.type" placeholder="上下文类型" @change="updateDomain">
                    <el-option label="核心" value="core"></el-option>
                    <el-option label="通用" value="common"></el-option>
                    <el-option label="定制" value="custom"></el-option>
                  </el-select>
                </el-form-item>
              </el-form>
              <div style="margin-left: 5px; margin-bottom: 5px"><h1>通信上下文</h1></div>
              <div>
                <el-button @click="contextVisible=true, getDomainsBySsId(), getAllTriggerActionByDomainId()">添加
                </el-button>
              </div>
              <el-table :data="msgTransmitTable" stripe style="width: 100%">
                <el-table-column prop="name" label="名称" width="160"></el-table-column>
                <el-table-column prop="type" label="类型" width="160"></el-table-column>
                <el-table-column prop="triggerAction" label="触发动作" width="160"></el-table-column>
                <el-table-column prop="causeContext" label="消息接收者" width="160"></el-table-column>
                <el-table-column prop="causeAction" label="引发动作" width="160"></el-table-column>
                <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                  <template #default="scope">
                    <el-button
                        @click.native.prevent="deleteRow(scope.$index, msgTransmitTable)"
                        @click="deleteMsg(scope.row.id, scope.row)"
                        type="text"
                        size="small">
                      移除
                    </el-button>
                    <el-button
                        @click="isMsgEdit(scope.row, scope.row)"
                        type="text"
                        size="small">
                      修改
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-scrollbar>
            <el-scrollbar style="position:absolute" v-show="isWordTable" id="wordtablecontainer"
                          class="jsoncontainer">
              <div><h1>词汇列表</h1></div>
              <div>
                <el-button @click="wordVisible=true">添加</el-button>
              </div>
              <el-table :data="wordTable" stripe style="width: 100%">
                <el-table-column prop="name" label="名称" width="180"></el-table-column>
                <el-table-column prop="type" label="类型" width="180"></el-table-column>
                <el-table-column prop="des" label="描述"></el-table-column>
                <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                  <template #default="scope">
                    <el-button
                        @click.native.prevent="deleteRow(scope.$index, wordTable)"
                        @click="deleteWord(scope.row.id, scope.row)"
                        type="text"
                        size="small">
                      删除
                    </el-button>
                    <el-button
                        @click="isWordEdit(scope.row, scope.row)"
                        type="text"
                        size="small">
                      修改
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-scrollbar>
            <div id="graphcontainer" class="graphcontainer">
            </div>
          </el-scrollbar>
          <!-- 中部2over -->
          <div class="svg-set-box"></div>
          <!-- 底部 -->
          <el-dialog title="新增消息" :visible.sync="contextVisible" width="25%">
            <el-form label-width="100px">
              <el-form-item label="名称" border>
                <el-input style="width: 55%" v-model="msgTransmit.name" placeholder="请输入名称"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-select v-model="msgTransmit.type" placeholder="请选择类型">
                  <el-option label="订阅者" value="订阅者"></el-option>
                  <el-option label="发布者" value="发布者"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="触发动作">
                <el-select v-model="msgTransmit.triggerAction" placeholder="请选择触发动作">
                  <el-option
                      v-for="item in triggerActionList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="消息接受者">
                <el-select v-model="causeContextId" placeholder="请选择消息接受上下文">
                  <el-option
                      v-for="item in contextList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="引发动作">
                <el-select v-model="msgTransmit.causeAction" placeholder="请选择引发动作"
                           @visible-change="getAllCauseActionByDomainId(causeContextId)">
                  <el-option
                      v-for="item in causeActionList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-button style="margin-left: 40px" @click="createMsg()">确定
              </el-button>
              <el-button style="margin-left: 40px" @click="contextVisible= false, causeContextId=''">关闭</el-button>
            </el-form>
            <!--            <div slot="footer" class="dialog-footer">-->
            <!--              <el-button @click="visible = false">取 消</el-button>-->
            <!--            </div>-->
          </el-dialog>
          <el-dialog title="修改消息" :visible.sync="msgEditVisible" width="25%">
            <el-form label-width="100px">
              <el-form-item label="名称" border>
                <el-input style="width: 55%" v-model="msgTransmit.name" placeholder="请输入名称"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-select v-model="msgTransmit.type" placeholder="请选择类型">
                  <el-option label="订阅者" value="订阅者"></el-option>
                  <el-option label="发布者" value="发布者"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="触发动作">
                <el-select v-model="msgTransmit.triggerAction" placeholder="请选择触发动作">
                  <el-option
                      v-for="item in triggerActionList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="消息接受者">
                <el-select v-model="msgTransmit.causeContext" placeholder="请选择消息接受上下文">
                  <el-option
                      v-for="item in contextList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="引发动作">
                <el-select v-model="msgTransmit.causeAction" placeholder="请选择引发动作"
                           @visible-change="getAllCauseActionByDomainId(msgTransmit.causeContext)">
                  <el-option
                      v-for="item in causeActionList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-button style="margin-left: 40px" @click="updateMsg()">确定
              </el-button>
              <el-button style="margin-left: 40px" @click="msgEditVisible= false, msgTransmit = {}">关闭</el-button>
            </el-form>

            <!--            <div slot="footer" class="dialog-footer">-->
            <!--              <el-button @click="visible = false">取 消</el-button>-->
            <!--            </div>-->
          </el-dialog>
          <el-dialog title="新增词汇" :visible.sync="wordVisible" width="20%">
            <el-form>
              <el-form-item label="名称" border>
                <el-input style="width: 60%" v-model="word.name"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-input style="width: 60%" title="type" v-model="word.type"></el-input>
              </el-form-item>
              <el-form-item label="描述">
                <el-input style="width: 60%" title="des" v-model="word.des"></el-input>
              </el-form-item>
              <el-button style="margin-left: 40px" @click="createWord">确定
              </el-button>
              <el-button style="margin-left: 40px" @click="wordVisible= false">关闭</el-button>
            </el-form>
            <!--            <div slot="footer" class="dialog-footer">-->
            <!--              <el-button @click="visible = false">取 消</el-button>-->
            <!--            </div>-->
          </el-dialog>
          <el-dialog title="修改词汇" :visible.sync="wordEditVisible" width="20%">
            <el-form>
              <el-form-item label="名称" border>
                <el-input style="width: 60%" v-model="word.name"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-input style="width: 60%" v-model="word.type"></el-input>
              </el-form-item>
              <el-form-item label="描述">
                <el-input style="width: 60%" v-model="word.des"></el-input>
              </el-form-item>
              <el-button style="margin-left: 40px" @click="updateWord">确定</el-button>
              <el-button style="margin-left: 40px" @click="wordEditVisible= false">关闭</el-button>
            </el-form>
            <!--            <div slot="footer" class="dialog-footer">-->
            <!--              <el-button @click="visible = false">取 消</el-button>-->
            <!--            </div>-->
          </el-dialog>
          <el-dialog title="编辑节点名称" :visible.sync="dialogNodeNameVisible" width="20%">
            <el-form :model="graphEntity">
              <el-form-item label="名称">
                <el-select v-model="graphEntity.name" value-key="name" placeholder="请从词汇表选择">
                  <el-option
                      v-for="item in wordTable"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
            <template #footer>
    <span class="dialog-footer">
      <el-button @click="dialogNodeNameVisible = false">取 消</el-button>
      <el-button type="primary" @click="updatenodename">确 定</el-button>
    </span>
            </template>
          </el-dialog>
          <el-dialog title="修改Param" :visible.sync="paramEditVisible" width="20%">
            <el-form>
              <el-form-item label="名称" border>
                <el-input style="width: 60%" v-model="selectValue_params.name"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-input style="width: 60%" title="type" v-model="selectValue_params.type"></el-input>
              </el-form-item>
              <el-form-item label="描述">
                <el-input style="width: 60%" title="des" v-model="selectValue_params.des"></el-input>
              </el-form-item>
              <el-button style="margin-left: 40px" @click="updateParamByActionId(selectValue.id)">确定</el-button>
            </el-form>
            <!--            <div slot="footer" class="dialog-footer">-->
            <!--              <el-button @click="visible = false">取 消</el-button>-->
            <!--            </div>-->
          </el-dialog>
          <el-dialog title="新增Param" :visible.sync="paramAdd2Visible" width="20%">
            <el-form>
              <el-form-item label="名称" border>
                <el-input style="width: 60%" v-model="param.name"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-input style="width: 60%" title="type" v-model="param.type"></el-input>
              </el-form-item>
              <el-form-item label="描述">
                <el-input style="width: 60%" title="des" v-model="param.des"></el-input>
              </el-form-item>
              <el-button style="margin-left: 40px" @click="selectValue.params.push(param), paramAdd2Visible= false">确定
              </el-button>
              <el-button style="margin-left: 40px" @click="paramAdd2Visible= false">关闭</el-button>
            </el-form>
            <!--            <div slot="footer" class="dialog-footer">-->
            <!--              <el-button @click="visible = false">取 消</el-button>-->
            <!--            </div>-->
          </el-dialog>
          <el-dialog title="新增Param" :visible.sync="paramAddVisible" width="20%">
            <el-form>
              <el-form-item label="名称" border>
                <el-input style="width: 60%" v-model="param.name"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-input style="width: 60%" title="type" v-model="param.type"></el-input>
              </el-form-item>
              <el-form-item label="描述">
                <el-input style="width: 60%" title="des" v-model="param.des"></el-input>
              </el-form-item>
              <el-button style="margin-left: 40px" @click="paramList.push(param), paramAddVisible= false">确定</el-button>
              <el-button style="margin-left: 40px" @click="paramAddVisible= false">关闭</el-button>
            </el-form>
            <!--            <div slot="footer" class="dialog-footer">-->
            <!--              <el-button @click="visible = false">取 消</el-button>-->
            <!--            </div>-->
          </el-dialog>
          <el-dialog title="csv导入图谱" :visible.sync="dialogFormVisible" width="30%">
            <el-form>
              <div style="margin-bottom: 20px; margin-left: 40px"><span style="color: #c62424">注意字符集为utf-8无bom格式，三元组结构：起始节点-结束节点-关系</span>
              </div>
              <el-form-item label="图谱上下文" label-width="120px">
                <!--                <el-input style="width:100%"-->
                <!--                                 v-model="uploadparam.domain" placeholder="请输入内容">-->
                <!--                  &lt;!&ndash;:fetch-suggestions="querySearch"&ndash;&gt;-->
                <!--                </el-input>-->
                <el-select v-model="uploadparam.domainId" placeholder="请选择上下文" @visible-change="getAllDomainsBySsId()">
                  <el-option
                      v-for="item in contextList0"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="选择文件" label-width="120px">
                <el-upload
                    class=""
                    :headers="headers"
                    ref="upload"
                    :action="uploadurl"
                    accept=".csv,.xls,.xlsx"
                    :show-file-list="true"
                    :data="uploadparam"
                    :on-success="csvsuccess"
                    :auto-upload="false">
                  <el-button slot="trigger" class="btn-bo" style="padding: 12px 24px;margin-bottom: 0px;">
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-daoru"></use>
                    </svg>
                    选择文件
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="submitUpload">确 定</el-button>
            </div>
          </el-dialog>
          <el-dialog title="导出模型" :visible.sync="exportFormVisible" width="30%">
            <el-form>
              <el-form-item label="请选择路径" label-width="100px">
                <el-input
                          v-model="jsonurl" placeholder="请输入保存路径">
                </el-input>
              </el-form-item>
              <el-form-item style="text-align: right">
                <el-button type="primary" @click="toModel">确 定</el-button>
              </el-form-item>
            </el-form>
          </el-dialog>
          <el-dialog title="导出Json" :visible.sync="exportJsonVisible" width="30%">
            <el-form>
              <el-form-item label="请选择路径" label-width="120px">
                <el-input style="width:100%"
                          v-model="jsonurl" placeholder="请输入保存路径">
                </el-input>
              </el-form-item>
              <el-button type="primary" @click="toJSON">确 定</el-button>
            </el-form>
          </el-dialog>
          <el-dialog id="editform" title="属性" :visible.sync="isedit" width="30%">
            <el-tabs type="card" tab-position="top" v-model="propactiveName" @tab-click="prophandleClick"
                     style="margin: 10px">
              <el-tab-pane label="属性编辑" name="propedit">
                <el-form :model="graphEntity">
                  <el-form-item label="名称" label-width="120px">
                    <el-select v-model="graphEntity.name" value-key="name" placeholder="请从词汇表选择">
                      <el-option
                          v-for="item in wordTable"
                          :key="item.id"
                          :label="item.name"
                          :value="item.name">
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="类型" label-width="120px">
                    <el-input v-model="graphEntity.type" style="width:324px"></el-input>
                  </el-form-item>
                  <el-form-item label="版本" label-width="120px">
                    <el-input v-model="graphEntity.modelVersion" style="width:324px"></el-input>
                  </el-form-item>
                  <el-form-item label="代码" label-width="120px">
                    <el-input v-model="graphEntity.code" style="width:324px"></el-input>
                  </el-form-item>
                  <el-form-item label="别名" label-width="120px">
                    <el-input v-model="graphEntity.alias" style="width:324px"></el-input>
                  </el-form-item>
                  <el-form-item label="命名空间" label-width="120px">
                    <el-input v-model="graphEntity.namespace" style="width:324px"></el-input>
                  </el-form-item>
                  <el-form-item label="语言" label-width="120px">
                    <el-input v-model="graphEntity.language" style="width:324px"></el-input>
                  </el-form-item>
                  <el-form-item label="可翻译" label-width="120px">
                    <el-switch
                        v-model="graphEntity.translated"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        active-value=true
                        inactive-value=false
                    >
                    </el-switch>
                  </el-form-item>
                  <el-form-item label="可扩展" label-width="120px">
                    <el-switch
                        v-model="graphEntity.extendable"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        active-value=true
                        inactive-value=false
                    >
                    </el-switch>
                  </el-form-item>

                  <el-form-item label="选择颜色" label-width="120px">
                    <el-color-picker id="colorpicker"
                                     v-model="graphEntity.color" :predefine="predefineColors">
                    </el-color-picker>
                  </el-form-item>
                  <el-form-item label="节点半径" label-width="120px">
                    <el-slider v-model="graphEntity.r" style="width:324px"></el-slider>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="自定义属性" name="userAttrEdit">
                <el-popover
                    placement="bottom-start"
                    style="margin-left: 20px"
                    v-model="userAttrLabelVisible">
                  <el-input style="margin-top: 10px;margin-bottom: 10px" placeholder="请输入属性名称" v-model="userAttrLabel"></el-input>
                  <el-input placeholder="请输入属性值" v-model="userAttrValue"></el-input>
                  <div style="text-align: right;margin-top: 10px">
                    <el-button type="primary" size="mini" @click="addDomain(userAttrLabel, userAttrValue)">确定</el-button>
                  </div>
                  <el-button type="primary"  slot="reference">新增属性</el-button>
                </el-popover>
                <el-form :model="dynamicValidateForm" ref="dynamicValidateForm" style="margin-top: 20px">
                  <el-form-item v-for="(userAttr, index) in dynamicValidateForm.userAttrs"
                                :label="userAttr.attrLabel" :key="userAttr.key" :prop="'userAttrs.' + index + '.value'"
                                 label-width="120px">
                    <el-input style="width:250px" v-model="userAttr.attrValue"></el-input>
                    <el-button style="margin-left: 20px" @click.prevent="removeUserAttr(userAttr)" type="danger" plain>删除</el-button>
                  </el-form-item>
                  <el-form-item style="text-align: right">
                    <el-button type="primary" @click="submitForm('dynamicValidateForm')" plain>更新</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="指令编辑" name="orderedit">
                <el-row style="margin-bottom: 20px;">
                  <el-form>
                    <el-select v-model="selectOrderValue" value-key="name" clearable placeholder="请选择命令"
                               style="margin-right: 15px" @change="isOrderEdit">
                      <el-option
                          v-for="item in orderList"
                          :key="item.id"
                          :label="item.name"
                          :value="item">
                      </el-option>
                    </el-select>
                    <el-button type="primary" plain @click="isOrderEdit">修改</el-button>
                    <el-button type="primary" plain @click="isOrderAdd">新增</el-button>
                  </el-form>
                </el-row>
                <el-form style="margin-top: 15px" v-show="isordereditapi">
                  <el-form-item label="名称" label-width="120px">
                    <el-input v-model="selectOrderValue.name" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item label="持续时间" label-width="120px">
                    <el-input v-model="selectOrderValue.duration" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item label="描述" label-width="120px">
                    <el-input v-model="selectOrderValue.des" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" style="margin-left: 350px" @click="updateOrder" plain>确定</el-button>
                    <el-button type="danger" @click="deleteOrder" plain>删除</el-button>
                  </el-form-item>
                </el-form>
                <el-form style="margin-top: 15px" v-show="isorderaddapi">
                  <el-form-item label="名称" label-width="120px">
                    <el-input v-model="order.name" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item label="持续时间" label-width="120px">
                    <el-input v-model="order.duration" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item label="描述" label-width="120px">
                    <el-input v-model="order.des" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button style="margin-left: 400px" @click="createOrder">确定</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="事件编辑" name="actionedit">
                <el-row style="margin-bottom: 20px;">
                  <el-form>
                    <el-select v-model="selectValue" value-key="name" clearable placeholder="请选择事件"
                               style="margin-right: 15px" @change="isActionEdit">
                      <el-option
                          v-for="item in actionList"
                          :key="item.id"
                          :label="item.name"
                          :value="item">
                      </el-option>
                    </el-select>
                    <el-button type="primary" plain @click="isActionEdit">修改</el-button>
                    <el-button type="primary" plain @click="isActionAdd">新增</el-button>
                  </el-form>
                </el-row>
<!--                修改表单-->
                <el-form style="margin-top: 15px" v-show="iseditapi">
                  <el-form-item label="名称" label-width="120px">
                    <el-input v-model="selectValue.name" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item label="类型" label-width="120px">
                    <el-tooltip placement="top" :content="type.detail" v-for="type in typeOptions">
                      <el-radio-group v-model="selectValue.type">
                        <el-radio-button style="margin:2px" :label="type.command" :key="type.id">{{ type.command }}</el-radio-button>
                      </el-radio-group>
                    </el-tooltip>
                  </el-form-item>
<!--                  <el-form-item label="参数" label-width="120px" v-show="iseditapi">-->
<!--                    <el-select v-model="selectValue_params" value-key="name" placeholder="请选择参数">-->
<!--                      <el-option-->
<!--                          v-for="item in selectValue.params"-->
<!--                          :key="item.id"-->
<!--                          :label="item.name"-->
<!--                          :value="item">-->
<!--                      </el-option>-->
<!--                    </el-select>-->
<!--                    <el-button style="margin-left: 20px" @click="paramEditVisible=true">修改</el-button>-->
<!--                    <el-button style="margin-left: 20px" @click="paramAdd2Visible=true">新增</el-button>-->
<!--                  </el-form-item>-->
                  <el-form-item label="引发" label-width="120px">
                    <el-input v-model="selectValue.cause" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item label="来源" label-width="120px">
                    <el-input v-model="selectValue.source" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" style="margin-left: 340px" @click="updateAction" plain>保存</el-button>
                    <el-button type="danger" @click="deleteAction" plain>删除</el-button>
                  </el-form-item>
                </el-form>
                <!--新增表单-->
                <el-form style="margin-top: 15px" v-show="isaddapi">
                  <el-form-item label="名称" label-width="120px">
                    <el-input v-model="action.name" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item label="类型" label-width="120px">
                    <el-tooltip placement="top" :content="type.detail" v-for="type in typeOptions">
                      <el-radio-group v-model="action.type">
                        <el-radio-button style="margin:2px" :label="type.command" :key="type.id">{{ type.command }}</el-radio-button>
                      </el-radio-group>
                    </el-tooltip>
                  </el-form-item>
<!--                  <el-form-item label="参数" label-width="120px" v-show="isaddapi">-->
<!--                    <el-table :data="paramList" style="width: 100%" max-height="250">-->
<!--                      <el-table-column fixed prop="name" label="名称"></el-table-column>-->
<!--                      <el-table-column prop="type" label="类型"></el-table-column>-->
<!--                      <el-table-column prop="des" label="描述"></el-table-column>-->
<!--                      <el-table-column fixed="right" label="操作">-->
<!--                        <template #default="scope">-->
<!--                          <el-button @click.native.prevent="deleteRow(scope.$index, paramList)" type="text"-->
<!--                                     size="small">移除-->
<!--                          </el-button>-->
<!--                        </template>-->
<!--                      </el-table-column>-->
<!--                    </el-table>-->
<!--                    <el-button @click="param= {}, paramAddVisible = true">新增</el-button>-->
<!--                  </el-form-item>-->
                  <el-form-item label="引发" label-width="120px">
                    <el-input v-model="action.cause" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item label="来源" label-width="120px">
                    <el-input v-model="action.source" style="width: 60%"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button style="margin-left: 420px" @click="createAction">确定</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
<!--              <el-tab-pane label="状态编辑" name="statusedit">-->
<!--                <el-row style="margin-bottom: 20px;">-->
<!--                  <el-form>-->
<!--                    <el-select v-model="selectStatusValue" value-key="name" clearable placeholder="请选择"-->
<!--                               style="margin-right: 15px" @change="isStatusEdit">-->
<!--                      <el-option-->
<!--                          v-for="item in statusList"-->
<!--                          :key="item.id"-->
<!--                          :label="item.name"-->
<!--                          :value="item">-->
<!--                      </el-option>-->
<!--                    </el-select>-->
<!--                    <el-button type="primary" plain @click="isStatusEdit">修改</el-button>-->
<!--                    <el-button type="primary" plain @click="isStatusAdd">新增</el-button>-->
<!--                  </el-form>-->
<!--                </el-row>-->
<!--                <el-form style="margin-top: 15px" v-show="isstatuseditapi">-->
<!--                  <el-form-item label="名称" label-width="120px">-->
<!--                    <el-input v-model="selectStatusValue.name" style="width: 60%"></el-input>-->
<!--                  </el-form-item>-->
<!--                  <el-form-item label="状态" label-width="120px">-->
<!--                    <el-switch-->
<!--                        v-model="selectStatusValue.active"-->
<!--                        active-color="#13ce66"-->
<!--                        inactive-color="#ff4949"-->
<!--                        active-value=1-->
<!--                        inactive-value=01-->
<!--                    >-->
<!--                    </el-switch>-->
<!--                  </el-form-item>-->
<!--                  <el-form-item label="描述" label-width="120px">-->
<!--                    <el-input v-model="selectStatusValue.des" style="width: 60%"></el-input>-->
<!--                  </el-form-item>-->
<!--                  <el-form-item>-->
<!--                    <el-button type="primary" style="margin-left: 350px" @click="updateStatus" plain>确定</el-button>-->
<!--                    <el-button type="danger" @click="deleteStatus" plain>删除</el-button>-->
<!--                  </el-form-item>-->
<!--                </el-form>-->
<!--                <el-form style="margin-top: 15px" v-show="isstatusaddapi">-->
<!--                  <el-form-item label="名称" label-width="120px">-->
<!--                    <el-input v-model="status.name" style="width: 60%"></el-input>-->
<!--                  </el-form-item>-->
<!--                  <el-form-item label="激活" label-width="120px">-->
<!--                    <el-switch-->
<!--                        v-model="status.active"-->
<!--                        active-color="#13ce66"-->
<!--                        inactive-color="#ff4949"-->
<!--                        active-value=1-->
<!--                        inactive-value=0-->
<!--                    >-->
<!--                    </el-switch>-->
<!--                  </el-form-item>-->
<!--                  <el-form-item label="描述" label-width="120px">-->
<!--                    <el-input v-model="status.des" style="width: 60%"></el-input>-->
<!--                  </el-form-item>-->
<!--                  <el-form-item>-->
<!--                    <el-button style="margin-left: 400px" @click="createStatus">确定</el-button>-->
<!--                  </el-form-item>-->
<!--                </el-form>-->
<!--              </el-tab-pane>-->
              <el-tab-pane label="验证" name="checkingedit">
                <el-form>
                  <el-form-item label="选择校验对象" label-width="120px">
                    <template>
                      <el-cascader
                          v-model="checkingValue"
                          :options="checkingOptions"
                          @visible-change="checkingChange"
                          placeholder="请选择校验对象"
                      ></el-cascader>
                    </template>
                  </el-form-item>
                  <el-form-item label="选校验择文件" label-width="120px">
                    <el-upload
                        class=""
                        :headers="headers"
                        ref="upload"
                        :action="checkingUploadUrl"
                        accept=".xsd"
                        :show-file-list="true"
                        :data="checkingUploadParam"
                        :on-success="checkingSuccess"
                        :auto-upload="false">
                      <el-button slot="trigger" class="btn-bo" style="padding: 12px 24px;margin-bottom: 0px;">
                        <svg class="icon" aria-hidden="true">
                          <use xlink:href="#icon-daoru"></use>
                        </svg>
                        选择文件
                      </el-button>
                    </el-upload>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" plain style="margin-left: 380px" @click="goChecking">开始校验</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="图片" name="propimage">
                <el-form>
                  <el-form-item label="本地上传" label-width="120px">
                    <el-upload class="" :headers="headers"
                               name="file"
                               ref="upload"
                               :action="uploadimageurl"
                               accept=".jpg,.png"
                               multiple
                               :show-file-list="false"
                               :data="uploadimageparam"
                               :on-success="uploadsuccess"
                               :auto-upload="true">
                      <el-button slot="trigger" size="small" type="primary" plain>选择</el-button>
                    </el-upload>
                  </el-form-item>
                  <el-form-item label="网络地址" label-width="120px">
                    <el-input v-model="netimageurl" style="width: 60%"></el-input>
                    <a href="javascript:void(0)" @click="addnetimage" class="cg">
                      <svg class="icon" aria-hidden="true">
                        <use xlink:href="#icon-add-s"></use>
                      </svg>
                    </a>
                  </el-form-item>
                  <el-form-item label="已选图片" label-width="120px">
                    <ul class="el-upload-list el-upload-list--picture-card">
                      <li v-for="item in nodeimagelist" class="el-upload-list__item is-success">
                        <img :src="imageurlformat(item)" alt="" class="el-upload-list__item-thumbnail">
                        <label class="el-upload-list__item-status-label">
                          <i class="el-icon-upload-success el-icon-check"></i>
                        </label>
                        <i class="el-icon-close" @click="imagehandleRemove(item)"></i>
                        <span class="el-upload-list__item-actions">
										<span class="el-upload-list__item-preview">
										     <i class="el-icon-zoom-in" @click="handlePictureCardPreview(item)"></i>
										</span>
				                        <span class="el-upload-list__item-delete">
				                            <i class="el-icon-delete" @click="imagehandleRemove(item)"></i>
				                        </span>
									</span>
                      </li>
                    </ul>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="富文本" name="richtextedit">
                <div ref="eidtorToolbar" id="eidtorToolbar" class="wange-toolbar"></div>
                <div ref="eidtorContent" id="eidtorContent" class="wangeditor-form"></div>
              </el-tab-pane>

            </el-tabs>
            <div slot="footer" class="dialog-footer">
              <el-button v-show="propactiveName=='propimage'" type="primary" @click="savenodeimage"
                         class="btn-line cur" plain>Save
              </el-button>
              <el-button v-show="propactiveName=='richtextedit'" @click="savenodecontent" type="primary"
                         class="btn-line cur" plain>保存
              </el-button>
              <el-button v-show="propactiveName=='propapi'" @click="savenodecontent" type="primary"
                         class="btn-line cur" plain>保存
              </el-button>
              <el-button v-show="propactiveName=='propedit'&&graphEntity.uuid!=0" type="primary" @click="createnode"
                         plain>
                更新
              </el-button>
              <el-button v-show="propactiveName=='propedit'&&graphEntity.uuid==0" type="primary" @click="createnode"
                         plain>
                创建
              </el-button>
              <el-button @click="resetsubmit" type="danger" plain>取消</el-button>
            </div>
          </el-dialog>
          <el-dialog id="batchcreateform" :title="operatenameformat(operatetype)" :visible.sync="isbatchcreate"
                     width="30%">
            <div v-show="operatetype==1" class="mb-l">添加同级</div>
            <div v-show="operatetype==2" class="mb-l">添加下级</div>
            <div v-show="operatetype==3" class="mb-l">批量添加</div>
            <div v-show="operatetype==4" class="mb-l">段落识别</div>
            <div class="mb-r">
              <div v-show="operatetype!=2" class="mb-m">
                <div class="mb-label">源节点名称</div>
                <div class="mb-con">
                  <el-input v-model="batchcreate.sourcenodename"></el-input>
                  <span v-show="operatetype==3" class="mb-label">（只能添加一个）</span>
                  <span v-show="operatetype==1" class="mb-label">（多个以英文逗号隔开）</span>
                </div>
                <div class="mb-label" v-show="operatetype!==1">关系</div>
                <div class="mb-con" v-show="operatetype!==1">
                  <el-input v-model="batchcreate.relation"></el-input>
                  <div class="mb-label">（只能添加一个）</div>
                </div>
              </div>
              <div v-show="operatetype!=1" class="mb-m">
                <div class="mb-label">子节点名称</div>
                <div class="mb-con">
                  <el-input v-model="batchcreate.targetnodenames"></el-input>
                  <span class="mb-label " v-show="operatetype==3">（多个以英文逗号隔开,可不填）</span>
                  <span class="mb-label " v-show="operatetype==2">（多个以英文逗号隔开）</span>
                </div>
              </div>
              <div class="mb-m tc">
                <el-button v-show="operatetype==1" type="primary" @click="batchcreatesamenode">确定</el-button>
                <el-button v-show="operatetype==2" type="primary" @click="batchcreatechildnode">确定</el-button>
                <el-button v-show="operatetype==3" type="primary" @click="batchcreatenode">确定</el-button>
                <el-button @click="resetsubmit">取消</el-button>
              </div>
            </div>
          </el-dialog>
          <!-- 底部over -->
        </div>
        <!-- 右侧over -->
        <!-- 空白处右键 -->
        <ul class="el-dropdown-menu el-popper blankmenubar" id="blank_menubar" style="display: none;">
          <li class="el-dropdown-menu__item" @click="btnaddsingle">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-jiedian"></use>
            </svg>
            <span class="pl-15">添加节点</span>
          </li>
          <li class="el-dropdown-menu__item" @click="btnquickaddnode">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-add-rd"></use>
            </svg>
            <span class="pl-15">快速添加</span>
          </li>
        </ul>
        <!-- 连线按钮组 -->
        <ul class="el-dropdown-menu el-popper linkmenubar" id="link_menubar" style="display: none;">
          <li class="el-dropdown-menu__item" @click="updatelinkName">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-editor"></use>
            </svg>
            <span class="pl-15">编辑</span>
          </li>
          <li class="el-dropdown-menu__item" @click="deletelink">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-shanchu"></use>
            </svg>
            <span class="pl-15">删除</span>
          </li>
        </ul>
        <!-- 富文本展示 -->
        <div id="richContainer" style="display: none;width: 400px">
          <div class="mind-fj-box" v-show="showImageList.length>0 ||editorcontent!=''">
            <div class="mind-carousel" v-show="showImageList.length>0">
              <el-carousel height="197px" :interval="2000" arrow="always">
                <el-carousel-item v-for="item in showImageList" :key="item.ID">
                  <div class="carous-img">
                    <img :src="item.FileName" alt="">
                  </div>
                </el-carousel-item>
              </el-carousel>
            </div>
            <el-scrollbar v-show="editorcontent!=''" class="mind-fj-p">
              <p v-html="editorcontent"></p>
            </el-scrollbar>
          </div>
        </div>
      </div>
    </el-container>
  </div>
</template>

<script>
import UbmlHeader from "../components/UbmlHeader";
import StateFlow from "../components/StateFlow";
import $ from "jquery";
import * as d3 from 'd3'
import E from 'wangeditor'
import _ from 'underscore'

let protocol = window.location.protocol; //协议
let host = window.location.host; //主机
let reg = /^localhost+/;
let host_l = host.split(":")
let contextRoot = protocol + "//" + host_l[0] + ":8081/";

// let contextRoot = "http://localhost:8081/";
export default {
  name: 'boIndex',
  components: {StateFlow, UbmlHeader},
  data() {
    return {
      userAttrLabel: '',
      userAttrValue: '',
      userAttrLabelVisible: false,
      dynamicValidateForm:{
        userAttrs: [],
      },

      nodesList: [],
      checkingUploadUrl: contextRoot + "importCheckingFile",
      checkingUploadParam: {domainId: ""},
      checkingValue: [],
      checkingOptions: [
        {
          value: 'prop',
          label: '属性',
          children: [
            {
              value: 'shuxing',
              label: '属性'
            }
          ]
        },
        {
          value: 'order',
          label: '命令',
          children: []
        },
        {
          value: 'action',
          label: '动作',
          children: []
        },
        {
          value: 'prop',
          label: '状态',
          children: []
        }
      ],
      processExist: false,
      process: {
        id: '',
        name: '',
        stepNum: 0,
        des: '无',
        domainId: 0,
        nodeId: 0,
        descVisible: false,
        type: ''
      },
      nameSelectList: [],
      tableSelectTypeValue: "",
      processData: [],
      selectFlowValue: "",
      flowList: [
        {name: "指令", type: "success", list: "orderList"},
        {name: "动作", type: "danger", list: "actionList"},
        {name: "状态", type: "warning", list: "statusList"}
      ],
      tagSelectList: [],
      stepNum: 0,
      step: {
        step_num: 1,
        step_title: '添加标题',
        inputVisible: false,
        timeInputVisible: false,
        step_explain: false,
        step_time: '20xx-xx-xx',
        inputValue: '',
        tags: []
      },
      stepNums: [],
      node1: '',
      aasId: 0,
      convertNameList: ["状态编排", "实体和关系"],
      actionAndStatusShow: false,
      graphContainerShow: true,
      dibmr: false,
      contextLinks: [],
      circleCoordinate: [],
      dataSet3: {
        "name": "上下文",
        "value": "root",
        "children": []
      },
      msgEditVisible: false,
      domainList: [],
      contextVisible: false,
      triggerActionList: [],
      causeActionList: [],
      contextList: [],
      contextList0: [],
      causeContextId: "",
      msgTransmit: {
        id: "",
        name: "",
        type: "",
        triggerAction: "",
        causeContext: "",
        causeAction: ""
      },
      context: {
        id: "",
        name: "",
        type: "",
      },
      msgTransmitTable: [],
      word: {
        id: "",
        name: "",
        type: "",
        des: "",
      },
      wordTable: [],
      wordVisible: false,
      isWordTable: false,
      isContextDetail: false,
      wordEditVisible: false,

      selectStatusValue: {},
      statusList: [
        {}
      ],
      isstatusaddapi: false,
      isstatuseditapi: false,
      status: {
        name: '',
        duration: '',
        des: '',
      },

      selectOrderValue: {},
      orderList: [],
      isorderaddapi: false,
      isordereditapi: false,
      order: {
        name: '',
        duration: '',
        des: '',
      },
      msgList: [],
      param: {},
      paramList: [],
      action: {
        name: '',
        type: '',
        des: '',
        cause: '',
        source: '',
      },
      actionList: [],
      typeOptions: [
        {id: 0, command: "Start", detail: "Fast stop in case of safety event"},
        {id: 1, command: "Reset", detail: "Fast stop in case of safety event"},
        {id: 2, command: "Abort(safety)", detail: "Fast stop in case of safety event"},
        {id: 3, command: "Abort(severe)", detail: "Fast stop in case of severe machine fault"},
        {id: 4, command: "Stop", detail: "Process stop: Stop accepting input, run out machine, slow decel"},
        {id: 5, command: "Hold", detail: "Slow decel in case of Inot severe internal error"},
        {id: 6, command: "Suspend(blocker)", detail: "Machine blocker: slow decel when downstream equipment is not available"},
        {id: 7, command: "Suspend(starved)", detail: "Machine starved: slow decel when raw materials missing"},
        {id: 8, command: "Status Event", detail: "Status event"},
      ],
      resOptions: ['200', '400', '404'],
      selectValue_params: '',
      selectValue: {},
      isapi: false,
      isaddapi: false,
      iseditapi: false,
      paramAddVisible: false,
      paramAdd2Visible: false,
      paramEditVisible: false,
      relNode: [],
      sserviceId: 0,
      that: this,
      svg: "null",
      timer: null,
      editor: null,
      simulation: null,
      contextGroup: null,
      linkGroup: null,
      linktextGroup: null,
      nodeGroup: null,
      nodetextGroup: null,
      nodesymbolGroup: null,
      nodebuttonGroup: null,
      nodebuttonAction: '',
      tooltip: null,
      tipsshow: true,
      txx: {},
      tyy: {},
      nodedetail: null,
      pagesizelist: [{size: 100, isactive: true}, {size: 500, isactive: false}, {
        size: 1000,
        isactive: false
      }, {size: 2000, isactive: false}],
      colorList: ["#ff8373", "#f9c62c", "#a5ca34", "#6fce7a", "#70d3bd", "#ea91b0"],
      color5: '#ff4500',
      predefineColors: ['#ff4500', '#ff8c00', '#90ee90', '#00ced1', '#1e90ff', '#c71585'],
      defaultcr: 30,
      activeName: '',
      dataconfigactive: '',
      querywords: '',
      operatetype: 0,
      isedit: false,
      isaddnode: false,
      isaddlink: false,
      isdeletelink: false,
      isbatchcreate: false,
      selectnodeid: null,
      selectnodename: '',
      selectsourcenodeid: 0,
      selecttargetnodeid: 0,
      sourcenodex1: 0,
      sourcenodey1: 0,
      mousex: 0,
      mousey: 0,
      domain: '',
      domainid: 0,
      nodename: '',
      nodeName2: '',
      pagesize: 100,
      cyphertext: '',
      cyphertextshow: false,
      jsonshow: false,
      propactiveName: 'propedit',
      contentactiveName: 'propimage',
      uploadimageurl: contextRoot + "qiniu/upload",
      uploadimageparam: {},
      nodeimagelist: [],
      netimageurl: '',
      dialogimageVisible: false,
      dialogImageUrl: '',
      showImageList: [],
      editorcontent: '',
      selectnode: {
        name: '',
        count: 0
      },
      pageModel: {
        pageIndex: 1,
        pageSize: 10,
        totalCount: 0,
        totalPage: 0,
        nodeList: []
      },
      graph: {
        nodes: [],
        links: []
      },
      graph_false: {
        nodes: [],
        links: []
      },
      graph_totle: [],
      batchcreate: {
        sourcenodename: '',
        targetnodenames: '',
        relation: '',
      },
      graphEntity: {
        uuid: 0,
        name: '',
        type: '',
        modelVersion: '',
        code: '',
        alias: '',
        namespace: '',
        language: '',
        translated: '',
        extendable: '',
        color: '#ff4500',
        r: 30,
        x: "",
        y: ""
      },
      uploadparam: {domainId: ""},
      domainlabels: [],
      dialogFormVisible: false,
      exportFormVisible: false,
      exportJsonVisible: false,
      headers: {},
      uploadurl: contextRoot + "importgraph",
      jsonurl: 'C:\\Users\\shiha\\Desktop',
      dialogNodeNameVisible: false,
      nodes: [],
    }
  },
  filters: {
    labelformat: function (value) {
      var domain = value.substring(1, value.length - 1);
      return domain;
    },
  },
  mounted() {
    var _this = this;
    //csrf
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var str = '{ "' + header + '": "' + token + '"}';
    _this.headers = eval('(' + str + ')');
    setTimeout(() => {
      _this.initgraph();
    }, 1000)
    $(() => {
      $(".blankmenubar").click(() => {
        $('#blank_menubar').hide();
      })
      $(".blankmenubar").mouseleave(() => {
        $('#blank_menubar').hide();
      })
      $(".graphcontainer").bind("contextmenu", (event) => {
        _this.that.svg.selectAll(".buttongroup").classed("circle_opreate", true);
        var left = event.clientX;
        var top = event.clientY;
        document.getElementById('blank_menubar').style.position = 'absolute';
        document.getElementById('blank_menubar').style.left = left + 'px';
        document.getElementById('blank_menubar').style.top = top + 'px';
        $('#blank_menubar').show();
        event.preventDefault();
      });
      $(".graphcontainer").bind("click", (event) => {
        if (event.target.tagName != "circle" && event.target.tagName != "link") {
          d3.select('#nodedetail').style('display', 'none');
        }
        if (!(event.target.id === "jsoncontainer" || $(event.target).parents("#jsoncontainer").length > 0)) {
          _this.that.jsonshow = false;
        }
        var cursor = document.getElementById("graphcontainer").style.cursor;
        if (cursor == 'crosshair') {
          d3.select('.graphcontainer').style("cursor", "");
          _this.that.txx = event.offsetX;
          _this.that.tyy = event.offsetY;
          _this.that.createSingleNode();
        }
        event.preventDefault();
      });

      $(".linkmenubar").bind("mouseleave", (event) => {
        d3.select('#link_menubar').style('display', 'none');
      });
      $("body").bind("mousedown", (event) => {
        if (!(event.target.id === "linkmenubar" || $(event.target).parents("#linkmenubar").length > 0)) {
          $("#linkmenubar").hide();
        }
        if (!(event.target.id === "batchcreateform" || $(event.target).parents("#batchcreateform").length > 0)) {
          this.that.isbatchcreate = false;
        }
        if (!(event.target.id === "richContainer" || $(event.target).parents("#richContainer").length > 0)) {
          $("#richContainer").hide();
        }
        if (!(event.target.id === "nodedetail" || $(event.target).parents("#nodedetail").length > 0)) {
          d3.select('#nodedetail').style('display', 'none');
        }

      });
    })
  },
  created() {
    var _this = this;
    _this.sserviceId = _this.$route.query.sserviceId;
    // console.log(_this.sserviceId);
    _this.getDomainsBySsId();
    _this.getMsg();
    setTimeout(() => {
      _this.putData();
    }, 500)
    _this.getlabels();
  },
  methods: {
    //自定义属性
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //往数据库里加
          var _this = this;
          console.log(_this.dynamicValidateForm.userAttrs);
          var data = {attrList: _this.dynamicValidateForm.userAttrs, domainId: _this.domainid, nodeId: _this.selectnodeid};
          $.ajax({
            type: "POST",
            data: JSON.stringify(data),
            contentType:"application/json",
            url: contextRoot + "updateUserAttr",
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
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    getAllUserAttr(){
      var _this = this;
      var data = {domainId: _this.domainid, nodeId: _this.selectnodeid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getUserAttrList",
        success: function (result) {
          if (result.code == 200) {
            _this.dynamicValidateForm.userAttrs = result.data;

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
    removeUserAttr(item){
      var _this = this;
      var index = this.dynamicValidateForm.userAttrs.indexOf(item)
      var data = {attrId: item.id};
      console.log(data);
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "deleteUserAttr",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            if (index !== -1) {
              _this.dynamicValidateForm.userAttrs.splice(index, 1)
            }
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
    addDomain(label, value) {
      this.dynamicValidateForm.userAttrs.push({
        attrLabel: label,
        attrValue: value,
        id: Date.now().toString()
      });
      this.userAttrLabelVisible = false;
      this.userAttrLabel = '';
      this.userAttrValue = '';
    },
    //输出
    toModel() {
      var _this = this;
      var data = {sserviceId: _this.sserviceId};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "toModel",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            // console.log(result.data);
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
    goChecking() {
      this.$message({
        message: "校验成功",
        type: 'success'
      });
    },
    checkingSuccess() {
      this.$refs.upload.clearFiles();
      this.$message({
        message: "正在导入中,请稍后查看",
        type: 'success'
      });
    },
    checkingChange(value) {
      var _this = this;
      _this.checkingOptions[1].children = [];
      _this.checkingOptions[2].children = [];
      _this.checkingOptions[3].children = [];

      for (let action of _this.actionList) {
        // console.log(action);
        var obj = {value: action.name, label: action.name};
        _this.checkingOptions[2].children.push(obj);
        // console.log(_this.checkingOptions[2].children);
      }
      for (let order of _this.orderList) {
        // console.log(order);
        var obj = {value: order.name, label: order.des};
        _this.checkingOptions[1].children.push(obj);
        // console.log(_this.checkingOptions[1].children);
      }
      for (let status of _this.statusList) {
        // console.log(status);
        var obj = {value: status.name, label: status.name};
        _this.checkingOptions[3].children.push(obj);
        // console.log(_this.checkingOptions[3].children);
      }
      // console.log(value);
    },
    getAllStep() {
      var _this = this;
      var data = {domainId: _this.domainid, nodeId: _this.selectnodeid};
      _this.stepNums = [];
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getStepList",
        success: function (result) {
          if (result.code == 200) {
            let step;
            for (step of result.data) {
              step.tags = [];
            }
            _this.getAllStepTag();
            if (result.data.length != 0) {
              // console.log(result.data);
              _this.stepNums = result.data;
            }
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
    isProcessExist(stepIndex, index, row) {
      var _this = this;
      var processId = row.id;
      $.ajax({
        data: processId,
        type: "POST",
        url: contextRoot + "isProcessExist",
        contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.data === true) {
            _this.$message({
              showClose: true,
              message: "已存在，请勿重复添加",
              type: 'warning'
            });
          } else {
            _this.addProcess(stepIndex, index, row);
          }
        }
      });
    },
    getProcess() {
      var _this = this;
      var data = {domainId: _this.domainid, nodeId: _this.selectnodeid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getProcessList",
        success: function (result) {
          if (result.code == 200) {
            // console.log(result.data);
            let proData = result.data;
            // console.log(proData);
            let proc;
            _this.processData = [];
            for (proc of proData) {
              // console.log(proc.stepNum);
              _this.processData[proc.stepNum] = [proc];
            }
            // console.log(_this.processData);
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
    addProcess(stepIndex, index, row) {
      var _this = this;
      _this.process = {};
      var data = row;
      data.domainId = _this.domainid;
      data.nodeId = _this.selectnodeid;
      data.stepNum = stepIndex + 1;
      data.descVisible = false;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        url: contextRoot + "addProcess",
        contentType: 'application/json;charset=utf-8',
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
            _this.getProcess();
          }
        }
      });
    },
    updateProcess(row) {
      var _this = this;
      console.log(row);
      var data = row;
      data.domainId = _this.domainid;
      data.nodeId = _this.selectnodeid;
      if (row.id) {
        $.ajax({
          data: JSON.stringify(data),
          type: "POST",
          contentType: 'application/json;charset=utf-8',
          url: contextRoot + "updateProcess",
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
      }
    },
    deleteProcess(index, row) {
      var _this = this;
      var data = {processId: row.id};
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        url: contextRoot + "deleteProcess",
        contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.getProcess();
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
    // addTableColumn1(index) {
    //   var _this = this;
    //   console.log(index);
    //   if (_this.processData[index] == null) {
    //     // _this.processData.splice(index, 0, []);
    //     _this.processData[index] = [];
    //     _this.processData[index].push(_this.process);
    //   } else {
    //     _this.processData[index].push(_this.process);
    //   }
    //   console.log(_this.processData);
    // },
    addTableColumn(index) {
      var _this = this;
      // console.log(index);
      if (_this.processData[index] != null) {
        _this.processData[index].push(_this.process);
      } else {
        _this.processData.splice(index, 0, [])
        _this.processData.splice(index + 1, 1);
        _this.processData[index].push(_this.process);
      }

    },
    addNameList(type) {
      var _this = this;
      _this.nameSelectList = [];
      if (type == "动作") {
        _this.nameSelectList = _this.actionList;
      } else if (type == "指令") {
        _this.nameSelectList = _this.orderList;
      } else if (type == "状态") {
        _this.nameSelectList = _this.statusList;
      }
    },
    addTagList(index) {
      var _this = this;
      _this.tagSelectList = _this.stepNums[index].tags;
    },
    getAll(selectnodeid) {
      var _this = this;
      if (selectnodeid != null){
        _this.selectnodeid = selectnodeid;
      }
      _this.getAllAction();
      _this.getAllOrder();
      _this.getAllStatus();
      // _this.getAllStep();
      // _this.getProcess();
    },
    addStep() {
      var _this = this;
      var data = _this.step;
      data.step_num = this.stepNums.length + 1;
      data.domainId = _this.domainid;
      data.nodeId = _this.selectnodeid;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        url: contextRoot + "addStep",
        contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
            _this.getAllStep();
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
    decreaseStep() {
      var _this = this;
      var nodeId = _this.selectnodeid;
      var stepId = _this.stepNums.pop()["id"];
      console.log(stepId);
      var data = {stepId: stepId};
      var stepNum = {nodeId: nodeId, stepNum: _this.stepNums.length + 1}
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        url: contextRoot + "deleteStep",
        contentType: 'application/json;charset=utf-8',
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
      $.ajax({
        data: JSON.stringify(stepNum),
        type: "POST",
        url: contextRoot + "deleteProcessAndTag",
        success: function (result) {
          if (result.code == 200) {
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
    updateStep(stepNum) {
      var _this = this;
      var data = stepNum;
      data.domainId = _this.domainid;
      data.nodeId = _this.selectnodeid;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "updateStep",
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
    descEnter(keyCode, props) {
      if (keyCode === 13) {
        props.row.descVisible = false
      }
    },
    explainEnter(keyCode, index) {
      if (keyCode === 13) {
        this.stepNums[index].step_explain = false
      }
    },
    timeEnter(keyCode, index) {
      if (keyCode === 13) {
        this.stepNums[index].timeInputVisible = false
      }
    },
    contextMenu() {
      console.log(111)
    },
    handleClose(tag, index, index1) {
      var _this = this;
      var data = {stepTagId: tag.tagId};
      console.log(data);
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        url: contextRoot + "deleteStepTag",
        contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.stepNums[index].tags.splice(index1, 1)
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
    showTimeInput(index) {
      this.stepNums[index].timeInputVisible = true
      this.$nextTick(_ => {
        this.$refs['time_input'][0].focus()
      })
    },
    titleInputShow(index) {
      this.stepNums[index].step_explain = true
      this.$nextTick(_ => {
        this.$refs['step_explain'][0].focus()
      })
    },
    descInputShow(props) {
      props.row.descVisible = true
      this.$nextTick(_ => {
        this.$refs['descVisible'][0].focus()
      })
    },
    showInput(index) {
      this.stepNums[index].inputVisible = true
    },
    handleSelectConfirm(index) {
      var _this = this;
      // let inputValue = this.stepNums[index].inputValue
      let inputValue = _this.selectFlowValue.name;
      let tagType = _this.selectFlowValue.type;
      let list = _this.selectFlowValue.list;
      if (inputValue) {
        var data = {name: inputValue, stepNum: index + 1, type: tagType, list: list};
        data.domainId = _this.domainid;
        data.nodeId = _this.selectnodeid;
        $.ajax({
          data: JSON.stringify(data),
          type: "POST",
          url: contextRoot + "addStepTag",
          contentType: 'application/json;charset=utf-8',
          success: function (result) {
            if (result.code == 200) {
              _this.$message({
                showClose: true,
                message: result.msg,
                type: 'success'
              });
              _this.stepNums[index].tags.push({tagId: result.data, tagName: inputValue, list: list, tagType: tagType});
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
      _this.stepNums[index].inputVisible = false
      _this.stepNums[index].inputValue = ''
      _this.selectFlowValue = ''
      // _this.addTagList(index)
    },
    getAllStepTag() {
      var _this = this;
      var data = {domainId: _this.domainid, nodeId: _this.selectnodeid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getStepTag",
        success: function (result) {
          if (result.code == 200) {
            let stepTag;
            for (stepTag of result.data) {
              if (_this.stepNums[stepTag.stepNum - 1].tags == undefined) {
                _this.stepNums[stepTag.stepNum - 1].tags = [];
              }
              _this.stepNums[stepTag.stepNum - 1].tags.push({
                tagId: stepTag.id,
                tagName: stepTag.name,
                list: stepTag.list,
                tagType: stepTag.type
              })
            }
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

    toAas() {
      var _this = this;
      _this.graphContainerShow = !_this.graphContainerShow;
      _this.actionAndStatusShow = !_this.actionAndStatusShow;
      _this.convertNameList.reverse();
      if (_this.selectnodeid != _this.aasId && _this.selectnodeid != null) {
        _this.getAll();
      }
      _this.aasId = _this.selectnodeid;
      // d3.select("body").selectAll(".mind-cen").remove();
    },
    async putData() {
      var _this = this;
      // console.log(_this.contextList);
      for (let item of _this.contextList) {
        // console.log(item);
        _this.domain = item.name;
        var value = "a" + item.id;
        await _this.getnodes();
        var chirdren = _this.nodes;

        _this.dataSet3.children.push({"name": item.name, "value": value, "children": chirdren});

      }
      console.log(_this.msgList);
      for (let msg of _this.msgList) {
        // console.log(msg);
        for (let d of _this.contextList) {
          if (d.name === msg.causeContext) {
            var tar = d.id;
          }
        }
        if (tar != undefined) {
          _this.contextLinks.push({"source_id": "a" + msg.domainid, "target_id": "a" + tar});
        }
      }
      // console.log(_this.contextLinks);

    },
    removeDom() {
      var _this = this;
      d3.select("body").selectAll("circle").remove();
      d3.select("body").selectAll("svg").attr("viewBox", null);
      d3.select("body").selectAll(".text").remove();
      d3.select("body").selectAll(".packageLink").remove();
      this.svg.attr("height", window.screen.height / 1.4);
      _this.dibmr = true;
    },
    getDomain() {
      var _this = this;
      var data = {domainId: _this.domainid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getDomain",
        success: function (result) {
          if (result.code == 200) {
            _this.context = result.data;
            _this.domain = _this.context.name
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
    updateDomain() {
      var _this = this;
      var data = _this.context;
      data.name = _this.domain;
      data.id = _this.domainid;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "updateDomain",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.getDomain();
            _this.getlabels();
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
    updateMsg() {
      var _this = this;
      var data = _this.msgTransmit;
      data.domainId = _this.domainid;
      data.sserviceId = _this.sserviceId;
      for (let item of _this.contextList) {
        if (item.id == _this.msgTransmit.causeContext) {
          data.causeContext = item.name;
        }
      }
      // console.log(data);
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "updateMsg",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.getAllMsg();
            _this.msgEditVisible = false;
            _this.msgTransmit = {};
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
    isMsgEdit(row) {
      var _this = this;
      _this.getDomainsBySsId();
      _this.getAllTriggerActionByDomainId()
      _this.msgTransmit.id = row.id;
      _this.msgTransmit.name = row.name;
      _this.msgTransmit.type = row.type;
      _this.msgTransmit.triggerAction = row.triggerAction;
      _this.msgTransmit.causeContext = row.causeContext;
      _this.msgTransmit.causeAction = row.causeAction;
      _this.msgEditVisible = true;
    },
    deleteMsg(msgId) {
      var _this = this;
      var data = {msgId: msgId};
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: contextRoot + "deleteMsg",
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
    // 获取所有上下文
    getAllDomainsBySsId() {
      var _this = this;
      var data = {sserviceId: _this.sserviceId};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getAllDomainsBySsId",
        success: function (result) {
          if (result.code == 200) {
            _this.contextList0 = result.data;
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
    // 获取除了自身之外的上下文
    getDomainsBySsId() {
      var _this = this;
      var data = {sserviceId: _this.sserviceId, domainId: _this.domainid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getDomainsBySsId",
        success: function (result) {
          if (result.code == 200) {
            _this.contextList = result.data;
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
    getAllTriggerActionByDomainId() {
      var _this = this;
      var data = {domainId: _this.domainid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getAllActionByDomainId",
        success: function (result) {
          if (result.code == 200) {
            _this.triggerActionList = result.data;
            // console.log(_this.triggerActionList);
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
    getAllCauseActionByDomainId(domainid) {
      var _this = this;
      var data = {domainId: domainid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getAllActionByDomainId",
        success: function (result) {
          if (result.code == 200) {
            _this.causeActionList = result.data;
            // console.log(_this.causeActionList);
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
    createMsg() {
      var _this = this;
      var data = _this.msgTransmit;
      data.domainId = _this.domainid;
      data.sserviceId = _this.sserviceId;
      for (let item of _this.contextList) {
        if (item.id == _this.causeContextId) {
          data.causeContext = item.name;
        }
      }

      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        url: contextRoot + "createMsg",
        contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.msgTransmit = {};
            _this.causeContextId = "";
            _this.getAllMsg();
            _this.contextVisible = false;
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
    getMsg() {
      var _this = this;
      var data = {sserviceId: _this.sserviceId};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getMsg",
        success: function (result) {
          if (result.code == 200) {
            _this.msgList = result.data;
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
    getAllMsg() {
      var _this = this;
      var data = {domainId: _this.domainid, sserviceId: _this.sserviceId};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getAllMsg",
        success: function (result) {
          if (result.code == 200) {
            _this.msgTransmitTable = result.data;
            // console.log(_this.msgTransmitTable)
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
    createWord() {
      var _this = this;
      var data = _this.word;
      data.domainId = _this.domainid;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        url: contextRoot + "createWord",
        contentType: 'application/json;charset=utf-8',
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.word = {};
            _this.getAllWord();
            _this.wordVisible = false;
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
    getAllWord() {
      var _this = this;
      var data = {domainId: _this.domainid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getWordList",
        success: function (result) {
          if (result.code == 200) {
            _this.wordTable = result.data;
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
    updateWord() {
      var _this = this;
      var data = _this.word;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "updateWord",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.wordEditVisible = false;
            _this.getAllWord();
            _this.word = {};
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
    isWordEdit(row) {
      var _this = this;
      _this.word.id = row.id;
      _this.word.name = row.name;
      _this.word.type = row.type;
      _this.word.des = row.des;
      _this.wordEditVisible = true;
    },
    deleteWord(wordId) {
      var _this = this;
      var data = {wordId: wordId}
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: contextRoot + "deleteWord",
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

    getAllStatus() {
      var _this = this;
      var data = {domainId: _this.domainid, nodeId: _this.selectnodeid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getStatusList",
        success: function (result) {
          if (result.code == 200) {
            _this.statusList = result.data;
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
    updateStatus() {
      var _this = this;
      var data = _this.selectStatusValue;
      // console.log(data);
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "updateStatus",
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
    deleteStatus() {
      var _this = this;
      var data = _this.selectStatusValue;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "deleteStatus",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.cleanStatus();
            _this.getAllStatus();
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
    createStatus() {
      var _this = this;
      var data = _this.status;
      data.domainId = _this.domainid;
      data.nodeId = _this.selectnodeid;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "createStatus",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.cleanStatus();
            _this.getAllStatus();
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
    isStatusEdit() {
      var _this = this;
      _this.isstatuseditapi = true;
      _this.isstatusaddapi = false;
      // _this.selectValue_params = '';
    },
    isStatusAdd() {
      var _this = this;
      _this.selectStatusValue = '';
      _this.isstatusaddapi = true;
      _this.isstatuseditapi = false
    },
    cleanStatus() {
      //清空order
      var _this = this;
      _this.isstatuseditapi = false;
      _this.isstatusaddapi = false;
      _this.status.name = '';
      _this.status.active = 0;
      _this.status.des = '';
      _this.selectStatusValue = {};
    },

    getAllOrder() {
      var _this = this;
      var data = {domainId: _this.domainid, nodeId: _this.selectnodeid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getOrderList",
        success: function (result) {
          if (result.code == 200) {
            _this.orderList = result.data;
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
    updateOrder() {
      var _this = this;
      var data = _this.selectOrderValue;
      // console.log(data);
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "updateOrder",
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
    deleteOrder() {
      var _this = this;
      var data = _this.selectOrderValue;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "deleteOrder",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.cleanOrder();
            _this.getAllOrder();
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
    createOrder() {
      var _this = this;
      var data = _this.order;
      data.domainId = _this.domainid;
      data.nodeId = _this.selectnodeid;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "createOrder",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.cleanOrder();
            _this.getAllOrder();
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
    isOrderEdit() {
      var _this = this;
      _this.isordereditapi = true;
      _this.isorderaddapi = false;
      // _this.selectValue_params = '';
    },
    isOrderAdd() {
      var _this = this;
      _this.selectOrderValue = '';
      _this.isorderaddapi = true;
      _this.isordereditapi = false
    },
    cleanOrder() {
      //清空order
      var _this = this;
      _this.isordereditapi = false;
      _this.isorderaddapi = false;
      _this.order.name = '';
      _this.order.duration = '';
      _this.order.des = '';
      _this.selectOrderValue = {};
    },

    updateParamByActionId(id) {
      var _this = this;
      _this.paramEditVisible = false;

    },
    createAction() {
      var _this = this;
      // _this.action.params = _this.paramList;
      var data = _this.action;
      data.domainId = _this.domainid;
      data.nodeId = _this.selectnodeid;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "createAction",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.cleanAction();
            _this.getAllAction();
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
    getAllAction() {
      var _this = this;
      var data = {domainId: _this.domainid, nodeId: _this.selectnodeid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getActionList",
        success: function (result) {
          if (result.code == 200) {
            _this.actionList = result.data;
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
    updateAction() {
      var _this = this;
      var data = _this.selectValue;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "updateAction",
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
    deleteAction() {
      var _this = this;
      var data = _this.selectValue;
      $.ajax({
        data: JSON.stringify(data),
        type: "POST",
        contentType: 'application/json;charset=utf-8',
        url: contextRoot + "deleteAction",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              showClose: true,
              message: result.msg,
              type: 'success'
            });
            _this.cleanAction();
            _this.getAllAction();
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
    cleanAction() {
      //清空action
      var _this = this;
      _this.isaddapi = false;
      _this.iseditapi = false;
      _this.action.name = '';
      _this.action.type = '';
      _this.action.des = '';
      _this.action.cause = '';
      _this.action.source = '';
      // _this.param = {};
      // _this.paramList = [];
      _this.selectValue = {};
      // _this.selectValue_params = '';
    },
    deleteRow(index, rows) {
      rows.splice(index, 1);
    },
    isActionEdit() {
      var _this = this;
      _this.iseditapi = true;
      _this.isaddapi = false;
      _this.selectValue_params = '';
    },
    isActionAdd() {
      var _this = this;
      _this.selectValue = '';
      _this.isaddapi = true;
      _this.iseditapi = false
    },

    // async toModel() {
    //   var _this = this;
    //   console.log("hhhhhh");
    //   // console.log(_this.contextList); //上下文
    //   for (let i = 0; i < _this.pageModel.nodeList.length; i++) {
    //     console.log(_this.pageModel.nodeList[i])
    //     await _this.matchdomaingraph_false(_this.pageModel.nodeList[i]);
    //   }
    //   console.log(_this.graph_totle)
    // },
    toJSON() {
      var _this = this;
      var relNode = new Array();
      var data = _this.graphEntity;
      var dataId = data.uuid;
      var links = _this.graph.links;
      //取出和该节点相关的节点信息
      for (let i of links) {
        if (i.sourceid == dataId) {
          var data = {domain: _this.domain, nodeid: +(i.targetid)};
          $.ajax({
            data: data,
            type: "POST",
            async: false,
            url: contextRoot + "getNodeAttrById",
            success: function (result) {
              if (result.code == 200) {
                result.data.relname = i.name
                relNode.push(result.data)
              }
            }
          })
        } else if (i.targetid == dataId) {
          var data = {domain: _this.domain, nodeid: +(i.sourceid)};
          $.ajax({
            data: data,
            type: "POST",
            async: false,
            url: contextRoot + "getNodeAttrById",
            success: function (result) {
              if (result.code == 200) {
                // console.log(result.data);
                relNode.push(result.data)
              }
            }
          })
          // var target = _this.getNodeAttrById(+(i.sourceid));
        }
      }
      _this.graphEntity.ref = relNode;

      // 以下都是: 生成json文件
      var a = document.createElement("a");
      a.download = 'none.json'.replace('none', _this.graphEntity.name);
      var blob = new Blob([JSON.stringify(_this.graphEntity)], {type: "text/json"});
      a.href = window.URL.createObjectURL(blob);
      // 标签 data- 嵌入自定义属性  屏蔽后也可正常下载
      a.dataset.downloadurl = ["text/json", a.download, a.href].join(":");
      // 添加鼠标事件
      let event = new MouseEvent("click", {});
      // 向一个指定的事件目标派发一个事件
      a.dispatchEvent(event);
    },
    showCypher() {
      this.cyphertextshow = !this.cyphertextshow;
    },
    wordTableShow() {
      if (this.graph.nodes.length == 0 && this.graph.links.length == 0) {
        this.$message.error("请先选择上下文或者执行cypher");
        return;
      }
      this.isContextDetail = false;
      this.jsonshow = false;
      this.isWordTable = !this.isWordTable;
    },
    contextDetailShow() {
      if (this.graph.nodes.length == 0 && this.graph.links.length == 0) {
        this.$message.error("请先选择上下文或者执行cypher");
        return;
      }
      this.isWordTable = false;
      this.jsonshow = false;
      this.isContextDetail = !this.isContextDetail;
    },
    cypherjson() {
      if (this.graph.nodes.length == 0 && this.graph.links.length == 0) {
        this.$message.error("请先选择上下文或者执行cypher");
        return;
      }
      this.isWordTable = false;
      this.isContextDetail = false;
      this.jsonshow = !this.jsonshow;
      // var json = this.graph;
      // var options = {
      //   collapsed: false,//收缩所有节点
      //   withQuotes: false//为key添加双引号
      // }
      // $("#json-renderer").JSONView(json, options);
    },
    cypherrun() {
      var _this = this;
      if (_this.cyphertext == "") {
        _this.$message.error("请输入cypher语句");
        return;
      }
      var data = {cypher: _this.cyphertext};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getcypherresult",
        success: function (result) {
          if (result.code == 200) {
            _this.graph.nodes = result.data.node;
            _this.graph.links = result.data.relationship;
            _this.updategraph();
          } else {
            _this.$message.error(result.msg);
          }
        }
      })
    },
    initEditor() {
      var _this = this;
      if (_this.editor != null) return;
      _this.editor = new E(this.$refs.eidtorToolbar, this.$refs.eidtorContent);
      // console.log(_this.editor);
      _this.editor.config.onchange = function (html) {
        _this.editorcontent = html;
      };
      var token = $("meta[name='_csrf']").attr("content");
      var header = $("meta[name='_csrf_header']").attr("content");
      var str = '{ "' + header + '": "' + token + '"}';
      var headers = eval('(' + str + ')');
      _this.editor.config.uploadFileName = 'file';
      _this.editor.config.uploadImgHeaders = headers;
      _this.editor.config.uploadImgServer = contextRoot + "qiniu/upload"; // 上传图片到服务器
      _this.editor.config.uploadImgHooks = {
        // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
        // （但是，服务器端返回的必须是一个 JSON 格式字符串！！！否则会报错）
        customInsert: function (insertImg, res, editor) {
          // 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
          // insertImg 是插入图片的函数，editor 是编辑器对象，result 是服务器端返回的结果
          for (var i = 0; i < res.results.length; i++) {
            var fileitem = res.results[i];
            insertImg(fileitem.url)
          }
        }
      }
      _this.editor.create();
    },
    initNodeContent() {
      var _this = this;
      var data = {domainid: _this.domainid, nodeid: _this.selectnodeid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getnodecontent",
        success: function (result) {
          _this.editor.txt.html("");
          if (result.code == 200) {
            _this.editorcontent = result.data.Content;
            _this.editor.txt.html(result.data.Content)
          }
        }
      })
    },
    initNodeImage() {
      var _this = this;
      var data = {domainid: _this.domainid, nodeid: _this.selectnodeid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getnodeimage",
        success: function (result) {
          if (result.code == 200) {
            for (var i = 0; i < result.data.length; i++) {
              _this.nodeimagelist.push({
                fileurl: result.data[i].FileName,
                imagetype: result.data[i].ImageType
              })
            }
          }
        }
      })
    },
    getNodeDetail(nodeid) {
      var _this = this;
      var data = {domainid: _this.domainid, nodeid: nodeid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getnodedetail",
        success: function (result) {
          if (result.code == 200) {
            _this.editorcontent = result.data.content;
            _this.showImageList = result.data.imagelist;
          }
        }
      })
    },
    savenodeimage() {
      var _this = this;
      var data = {
        domainid: _this.domainid,
        nodeid: _this.selectnodeid,
        imagelist: JSON.stringify(_this.nodeimagelist)
      };
      $.ajax({
        dataType: 'json',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        type: "POST",
        url: contextRoot + "savenodeimage",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              message: '操作成功',
              type: 'success'
            });
          }
        }
      })
    },
    savenodecontent() {
      var _this = this;
      var data = {domainid: _this.domainid, nodeid: _this.selectnodeid, content: _this.editorcontent};
      $.ajax({
        dataType: 'json',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        type: "POST",
        url: contextRoot + "savenodecontent",
        success: function (result) {
          if (result.code == 200) {
            _this.$message({
              message: '操作成功',
              type: 'success'
            });
          }
        }
      })
    },
    handlePictureCardPreview(item) {
      this.dialogImageUrl = this.imageurlformat(item);
      this.dialogimageVisible = true;
    },
    addnetimage() {
      if (this.netimageurl != '') {
        this.nodeimagelist.push({file: this.netimageurl, imagetype: 1});
        this.netimageurl = '';
      }
    },
    imagehandleRemove(url) {
      this.nodeimagelist.splice(this.nodeimagelist.indexOf(url), 1);
    },
    imageurlformat(item) {
      return item.fileurl;
    },
    dbimageurlformat(item) {
      return item.FileName;
    },
    uploadsuccess(res, file) {
      if (res.success == 1) {
        for (var i = 0; i < res.results.length; i++) {
          var fileitem = res.results[i];
          this.nodeimagelist.push({fileurl: fileitem.url});
        }

      } else {
        this.$message.error(res.msg);
      }
    },
    prophandleClick(tab, event) {
      if (tab.name == 'richtextedit') {
        this.editorcontent = '';
        this.initNodeContent();
        this.initEditor();

      }
      if (tab.name == 'propimage') {
        this.nodeimagelist = [];
        this.initNodeImage();
      }
    },
    operatenameformat() {
      if (this.operatetype == 1) {
        return "添加同级";
      } else if (this.operatetype == 2) {
        return "添加下级";
      } else if (this.operatetype == 3) {
        return "批量添加";
      }
    },
    requestFullScreen() {
      var element = document.getElementById("graphcontainerdiv");
      var width = window.screen.width;
      var height = window.screen.height;
      this.svg.attr("width", width);
      this.svg.attr("height", height);
      if (element.requestFullscreen) {
        element.requestFullscreen();
      }
      // FireFox
      else if (element.mozRequestFullScreen) {
        element.mozRequestFullScreen();
      }
      // Chrome等
      else if (element.webkitRequestFullScreen) {
        element.webkitRequestFullScreen();
      }
      // IE11
      else if (element.msRequestFullscreen) {
        element.msRequestFullscreen();
      }
    },
    async getnodes() {
      var _this = this;
      _this.loading = true;
      var data = {
        domain: _this.domain,
      }
      await $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getdomainnodes",
        success: function (result) {
          if (result.code == 200) {
            var graphModel = result.data;
            if (graphModel != null) {
              _this.nodes = graphModel;
            }
          }
        }
      });
    },
    getNode2(domainName){
      var _this = this;
      _this.loading = true;
      var data = {
        domain: domainName,
      }
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getdomainnodes",
        success: function (result) {
          if (result.code == 200) {
            _this.nodesList = result.data;
            }
          }
      });
    },
    // async getdomaingraph_false() {
    //   let _this = this;
    //   let data = {
    //     domain: _this.domain,
    //   }
    //   await $.ajax({
    //     data: data,
    //     type: "POST",
    //     url: contextRoot + "getDomainGraph_false",
    //     success: function (result) {
    //       if (result.code == 200) {
    //         console.log(result.data)
    //         _this.graph_false.nodes = result.data.node;
    //         _this.graph_false.links = result.data.relationship;
    //         _this.graph_totle.push(_this.graph_false);
    //       }
    //     }
    //   });
    // },

    getdomaingraph() {
      var _this = this;
      _this.loading = true;
      var data = {
        domain: _this.domain,
        nodename: _this.nodename,
        pageSize: _this.pagesize
      }
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getdomaingraph",
        success: function (result) {
          if (result.code == 200) {
            var graphModel = result.data;
            if (graphModel != null) {
              _this.graph.nodes = graphModel.node;
              _this.graph.links = graphModel.relationship;
              _this.updategraph();
            }
          }
        }
      });
    },
    getcurrentnodeinfo(node) {
      var _this = this;
      var data = {domain: _this.domain, nodeid: node.uuid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getrelationnodecount",
        success: function (result) {
          if (result.code == 200) {
            _this.selectnode.name = node.name;
            _this.selectnode.count = result.data;
          }
        }
      });
    },
    btnaddsingle() {
      d3.select('.graphcontainer').style("cursor", "crosshair");//进入新增模式，鼠标变成＋
    },
    btndeletelink() {
      this.isdeletelink = true;
      d3.select('.link').attr("class", "link linkdelete"); // 修改鼠标样式为"+"
    },
    getmorenode() {
      var _this = this;
      var data = {domain: _this.domain, nodeid: _this.selectnodeid};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getmorerelationnode",
        success: function (result) {
          if (result.code == 200) {
            var newnodes = result.data.node;
            var newships = result.data.relationship;
            var oldnodescount = _this.graph.nodes.length;
            if (newnodes) {
              newnodes.forEach(function (m) {
                var sobj = _this.graph.nodes.find(function (x) {
                  return x.uuid === m.uuid
                })
                if (typeof (sobj) == "undefined") {
                  _this.graph.nodes.push(m);
                }
              })
            }
            var newnodescount = _this.graph.nodes.length;
            if (newnodescount <= oldnodescount) {
              _this.$message({
                message: '没有更多节点信息',
                type: 'success'
              });
              return;
            }
            newships.forEach(function (m) {
              var sobj = _this.graph.links.find(function (x) {
                return x.uuid === m.uuid
              })
              if (typeof (sobj) == "undefined") {
                _this.graph.links.push(m);
              }
            })
            _this.updategraph();
          }
        },
        error: function (data) {
        }
      });
    },
    btnaddsame() {
      this.operatetype = 1;
      this.isbatchcreate = true;
      this.isedit = false;
    },
    btnquickaddnode() {
      this.isedit = false;
      this.isbatchcreate = true;
      $("#link_menubar").hide();
      this.operatetype = 3;
    },
    deletedomain(id, value) {
      var _this = this;
      _this.$confirm('此操作将删除该标签及其下节点、关系、词汇(不可恢复), 是否继续?', '三思而后行', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function (res) {
        var data = {domainid: id, domain: value};
        $.ajax({
          data: data,
          type: "POST",
          url: contextRoot + "deletedomain",
          success: function (result) {
            if (result.code == 200) {
              _this.getlabels();
              location.reload();
              _this.domain = "";
            } else {
              _this.$message({
                showClose: true,
                message: result.msg,
                type: 'warning'
              });
            }
          }
        });
      }).catch(function () {
        _this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    createdomain(value) {
      var _this = this;
      _this.$prompt('请输入上下文名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then((res) => {
        value = res.value;
        var data = {domain: value, sserviceId: _this.sserviceId};
        _this.$axios.get("/createdomain", {params: {domain: data.domain, sserviceId: data.sserviceId}}
        ).then(
            (response) => {
              _this.getlabels();
              _this.domain = value;
              _this.getdomaingraph();
              _this.removeDom();
            }
        ).catch(
            (error) => {
            }
        )
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    },
    getlabels() {
      var _this = this;
      var data = {sserviceId: _this.sserviceId};
      $.ajax({
        data: data,
        type: "POST",
        //url: contextRoot+"getlabels",
        url: contextRoot + "getgraph",
        success: function (result) {
          if (result.code == 200) {
            // console.log("进入getlables ajax");
            _this.pageModel = result.data;
            _this.pageModel.totalPage = parseInt((result.data.totalCount - 1) / result.data.pageSize) + 1
          }
        }
      });
    },
    getmoredomain() {
      var _this = this;
      _this.pageModel.pageIndex = _this.pageModel.pageIndex + 1
      var data = {pageIndex: _this.pageModel.pageIndex, sserviceId: _this.sserviceId};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "getgraph",
        success: function (result) {
          if (result.code == 200) {
            _this.pageModel.nodeList.push.apply(_this.pageModel.nodeList, result.data.nodeList);
            //_this.pageModel.nodeList.concat(result.data.nodeList);
          }
        }
      });
    },
    initgraph() {
      var _this = this;
      var graphcontainer = d3.select(".graphcontainer");
      _this.width = graphcontainer._groups[0][0].offsetWidth;
      _this.height = window.screen.height / 1.4;//
      //颜色
      var color = d3.scaleLinear()
          .domain([0, 5])
          .range(["hsl(0,0%,100%)", "hsl(224,100%,50%)"])
          .interpolate(d3.interpolateHcl);
      //数据
      var hierarchyData = d3.hierarchy(this.dataSet3).sum(function (d) {
        return d.value;
      });
      var packcreator = d3.pack().size([_this.width, _this.height]).padding(3);
      var packdata = packcreator(hierarchyData)
      var allnodes = packdata.descendants();
      this.root = packdata;
      this.focus = "";
      _this.view = null;

      //svg
      this.svg = graphcontainer.append("svg");
      this.svg.attr("width", _this.width);
      this.svg.attr("height", _this.height);
      this.svg.style("display", "block")
          .attr("viewBox", `-${_this.width / 2} -${_this.height / 2} ${_this.width} ${_this.height}`)
          .style("margin", "0 -14px")
          .style("background", color(0))
          .style("cursor", "pointer");

      this.simulation = d3.forceSimulation()
          .force("link", d3.forceLink().distance(function (d) {
            return Math.floor(Math.random() * (700 - 200)) + 200;
          }).id(function (d) {
            return d.uuid
          }))
          .force("charge", d3.forceManyBody().strength(-400))
          .force("collide", d3.forceCollide())
          .force("center", d3.forceCenter(_this.width / 2, (_this.height - 200) / 2));

      //打包图


      //绘制
      // this.contextGroup = this.svg.append("g").attr("class", "context");
      // var gcircles = this.contextGroup.selectAll("context")
      //     .data(allnodes)
      //     .enter().append("g").attr("transform", d => `translate(${d.x + 1},${d.y + 1})`);
      //
      // gcircles.append("circle").attr("cx", 0).attr("cy", 0).attr("r", function (d) {return d.r;})
      //     .attr("fill", d => d.children ? color(d.depth) : "#ffffff")
      //     .attr("pointer-events", d => !d.children ? "none" : null)
      //     .on("mouseover", function() { d3.select(this).attr("stroke", "#000000"); })
      //     .on("mouseout", function() { d3.select(this).attr("stroke", null); });
      _this.gcircles = this.svg.selectAll('circle')
          .data(allnodes).enter().append('circle')
          .attr('id', function (d) {
            return d.data.value;
          })
          // .attr("transform", d => `translate(${d.x + 1},${d.y + 1})`)
          // .attr("cx", 0).attr("cy", 0)
          .attr('r', function (d, i) {
            return d.r;
          })
          .style('fill', d => d.children ? color(d.depth) : "white")
          .attr("pointer-events", d => !d.children ? "none" : null)
          .on("mouseover", function () {
            d3.select(this).attr("stroke", "#000");
          })
          .on("mouseout", function () {
            d3.select(this).attr("stroke", null);
          })
          .on('click', function (d) {
            if (focus != d) _this.zoom(d), d3.event.stopPropagation();
          });


      //添加文本
      this.label = this.svg.append("g")
          .attr("class", "text")
          .style("font", "15px sans-serif")
          .attr("pointer-events", "none")
          .attr("text-anchor", "middle")
          .selectAll("text")
          .data(allnodes)
          .enter()
          .append("text")
          .style("fill-opacity", d => d.parent === packdata ? 1 : 0)
          .style("display", d => d.parent === packdata ? "inline" : "none")
          .text(d => d.data.name);

      // gcircles.append("text")
      //     .style("font", "15px sans-serif")
      //     .attr("pointer-events", "none")
      //     .attr("text-anchor", "middle")
      //     .attr("x", -18)
      //     .attr("y", 3)
      //     .style("fill-opacity", d => d.parent === packdata ? 1 : 0)
      //     .style("display", d => d.parent === packdata ? "inline" : "none")
      //     .text(d => d.data.name);
      _this.gcircles.append("title").text(function (d) {
        return d.data.name
      });

      //连线
      //存放连线数据中所有处理好后的坐标信息
      let x_t = _this.height / 2;
      let y_t = _this.height / 2 + 5;
      _this.diameter_global = _this.width > _this.height ? _this.height : _this.width;
      _this.links = _this.svg.append('g')
          // .attr('transform', 'translate(-390, -390)')
          .attr('transform', 'translate(' + -x_t + ',' + -y_t + ')')
          .style('stroke', '#ff0000')
          .attr("class", "packageLink")
          .selectAll('line')
          .data(_this.contextLinks)          //连线数据
          .enter().append('line');


      _this.zoomTo([_this.root.x, _this.root.y, _this.root.r * 2]);


      this.linkGroup = this.svg.append("g").attr("class", "line");
      this.linktextGroup = this.svg.append("g").attr("class", "linetext");
      this.nodeGroup = this.svg.append("g").attr("class", "node");
      this.nodetextGroup = this.svg.append("g").attr("class", "nodetext");
      this.nodesymbolGroup = this.svg.append("g").attr("class", "nodesymbol");
      this.nodebuttonGroup = this.svg.append("g").attr("class", "nodebutton");
      this.addmaker();
      this.tooltip = this.svg.append("div").style("opacity", 0);
      this.svg.on('click', function () {
        d3.selectAll(".buttongroup").classed("circle_opreate", true);
      }, 'false');
    },
    zoomTo(v) {
      var _this = this;
      const k = _this.height / v[2];

      this.view = v;

      _this.links
          // .attr('transform', 'translate(-390, -390)')
          .attr("stroke", "#fd6161")
          .attr("stroke-width", "3")
          .attr("stroke-dasharray", "5,5");
      // .attr("stroke-dasharray", "20,10,5,5,5,10");

      // console.log(k, v);
      _this.label.attr("transform", d => `translate(${(d.x - v[0]) * k},${(d.y - v[1]) * k})`);
      _this.gcircles.attr("transform", d => `translate(${(d.x - v[0]) * k},${(d.y - v[1]) * k})`);
      _this.gcircles.attr("r", d => d.r * k);

      _this.refreshLink(_this.links);


    },
    zoom(d) {
      var _this = this;
      console.log('zooming to', d.x, d.y);
      const focus0 = _this.focus;

      _this.focus = d;

      const transition = _this.svg.transition()
          .duration(d3.event.altKey ? 7500 : 750)
          .tween("zoom", d => {
            const i = d3.interpolateZoom(_this.view, [_this.focus.x, _this.focus.y, _this.focus.r * 2]);
            return t => _this.zoomTo(i(t));
          });

      this.label
          .filter(function (d) {
            return d.parent === _this.focus || this.style.display === "inline";
          })
          .transition(transition)
          .style("fill-opacity", d => d.parent === _this.focus ? 1 : 0)
          .on("start", function (d) {
            if (d.parent === _this.focus) this.style.display = "inline";
          })
          .on("end", function (d) {
            if (d.parent !== _this.focus) this.style.display = "none";
          });
    },
    refreshLink(links) {
      var _this = this;

      function getTranslateX(translateText) {
        var start = translateText.indexOf("(");
        var comma = translateText.indexOf(",");
        return parseFloat(translateText.slice(start + 1, comma));
      }

      function getTranslateY(translateText) {
        var comma = translateText.indexOf(",");
        var end = translateText.indexOf(")");
        return parseFloat(translateText.slice(comma + 1, end));
      }

      function getCircleTransform(id) {
        return d3.select("#" + id.replace(/\./g, '\\.')).attr("transform");
        ;
      }

      links.attr("x1", function (d) {
        var test = getCircleTransform(d.source_id);
        return getTranslateX(getCircleTransform(d.source_id)) + _this.diameter_global / 2;
      })
          .attr("y1", function (d) {
            return getTranslateY(getCircleTransform(d.source_id)) + _this.diameter_global / 2;
          })
          .attr("x2", function (d) {
            return getTranslateX(getCircleTransform(d.target_id)) + _this.diameter_global / 2;
          })
          .attr("y2", function (d) {
            return getTranslateY(getCircleTransform(d.target_id)) + _this.diameter_global / 2;
          });
    },
    updategraph() {
      var _this = this;
      var lks = this.graph.links;
      var nodes = this.graph.nodes;
      var links = [];
      // console.log(this.graph.links);
      //由后端传过来的节点坐标，固定节点，由于是字符串，需要转换
      nodes.forEach(function (n) {
        if (typeof (n.fx) == "undefined" || n.fx == "" || n.fx == null) {
          n.fx = null;
        }
        if (typeof (n.fy) == "undefined" || n.fy == "" || n.fy == null) {
          n.fy = null;
        }
        if ((typeof n.fx) == "string") n.fx = parseFloat(n.fx);
        if ((typeof n.fy) == "string") n.fy = parseFloat(n.fy);
      });
      lks.forEach(function (m) {
        var sourceNode = nodes.filter(function (n) {
          return n.uuid === m.sourceid;
        })[0];
        if (typeof (sourceNode) == 'undefined') return;
        var targetNode = nodes.filter(function (n) {
          return n.uuid === m.targetid;
        })[0];
        if (typeof (targetNode) == 'undefined') return;
        links.push({source: sourceNode.uuid, target: targetNode.uuid, lk: m});
      });
      //为每一个节点定制按钮组
      _this.addnodebutton();
      if (links.length > 0) {
        _.each(links, function (link) {
          var same = _.where(links, {
            'source': link.source,
            'target': link.target
          });
          var sameAlt = _.where(links, {
            'source': link.target,
            'target': link.source
          });
          var sameAll = same.concat(sameAlt);
          _.each(sameAll, function (s, i) {
            s.sameIndex = (i + 1);
            s.sameTotal = sameAll.length;
            s.sameTotalHalf = (s.sameTotal / 2);
            s.sameUneven = ((s.sameTotal % 2) !== 0);
            s.sameMiddleLink = ((s.sameUneven === true) && (Math.ceil(s.sameTotalHalf) === s.sameIndex));
            s.sameLowerHalf = (s.sameIndex <= s.sameTotalHalf);
            s.sameArcDirection = 1;
            //s.sameArcDirection = s.sameLowerHalf ? 0 : 1;
            s.sameIndexCorrected = s.sameLowerHalf ? s.sameIndex : (s.sameIndex - Math.ceil(s.sameTotalHalf));
          });
        });
        var maxSame = _.chain(links)
            .sortBy(function (x) {
              return x.sameTotal;
            })
            .last()
            .value().sameTotal;

        _.each(links, function (link) {
          link.maxSameHalf = Math.round(maxSame / 2);
        });
      }
      // 更新连线 links
      var link = _this.linkGroup.selectAll(".line >path").data(links, function (d) {
        return d.uuid;
      });
      link.exit().remove();
      var linkEnter = _this.drawlink(link);
      link = linkEnter.merge(link);
      // 更新连线文字
      _this.linktextGroup.selectAll("text").data(links, function (d) {
        return d.uuid;
      }).exit().remove();//移除多余的text dom
      var linktext = _this.linktextGroup.selectAll("text >textPath").data(links, function (d) {
        return d.uuid;
      });
      linktext.exit().remove();
      var linktextEnter = _this.drawlinktext(linktext);
      linktext = linktextEnter.merge(linktext).text(function (d) {
        return d.lk.name;
      });
      // 更新节点按钮组
      d3.selectAll(".nodebutton >g").remove();
      var nodebutton = _this.nodebuttonGroup.selectAll(".nodebutton").data(nodes, function (d) {
        return d
      });
      nodebutton.exit().remove();
      var nodebuttonEnter = _this.drawnodebutton(nodebutton);
      nodebutton = nodebuttonEnter.merge(nodebutton);
      // 更新节点
      var node = _this.nodeGroup.selectAll("circle").data(nodes, function (d) {
        return d.uuid;
      });
      node.exit().remove();
      var nodeEnter = _this.drawnode(node);
      node = nodeEnter.merge(node).text(function (d) {
        return d.name;
      });
      // 更新节点文字
      var nodetext = _this.nodetextGroup.selectAll("text").data(nodes, function (d) {
        return d.uuid
      });
      nodetext.exit().remove();
      var nodetextEnter = _this.drawnodetext(nodetext);
      nodetext = nodetextEnter.merge(nodetext).text(function (d) {
        return d.name;
      });
      nodetext.append("title")// 为每个节点设置title
          .text(function (d) {
            return d.name;
          });
      // 更新节点标识
      var nodesymbol = _this.nodesymbolGroup.selectAll("path").data(nodes, function (d) {
        return d.uuid;
      });
      nodesymbol.exit().remove();
      var nodesymbolEnter = _this.drawnodesymbol(nodesymbol);
      nodesymbol = nodesymbolEnter.merge(nodesymbol);
      nodesymbol.attr("fill", "#e15500");
      nodesymbol.attr("display", function (d) {
        if (typeof (d.hasfile) != "undefined" && d.hasfile > 0) {
          return "block";
        }
        return "none";
      })
      _this.simulation.nodes(nodes).on("tick", ticked);
      _this.simulation.force("link").links(links);
      _this.simulation.alphaTarget(1).restart();

      function linkArc(d) {
        var dx = (d.target.x - d.source.x),
            dy = (d.target.y - d.source.y),
            dr = Math.sqrt(dx * dx + dy * dy),
            unevenCorrection = (d.sameUneven ? 0 : 0.5);
        var curvature = 2,
            arc = (1.0 / curvature) * ((dr * d.maxSameHalf) / (d.sameIndexCorrected - unevenCorrection));
        if (d.sameMiddleLink) {
          arc = 0;
        }
        var dd = "M" + d.source.x + "," + d.source.y + "A" + arc + "," + arc + " 0 0," + d.sameArcDirection + " " + d.target.x + "," + d.target.y;
        return dd;
      }

      function ticked() {
        link.attr("d", linkArc)
        // 更新节点坐标
        node.attr("cx", function (d) {
          return d.x;
        })
            .attr("cy", function (d) {
              return d.y;
            });
        // 更新节点操作按钮组坐标
        nodebutton.attr("cx", function (d) {
          return d.x;
        })
            .attr("cy", function (d) {
              return d.y;
            });
        nodebutton.attr("transform", function (d) {
          return "translate(" + d.x + "," + d.y + ") scale(1)";
        })

        // 更新文字坐标
        nodetext.attr("x", function (d) {
          return d.x;
        })
            .attr("y", function (d) {
              return d.y;
            });
        // 更新回形针坐标
        nodesymbol.attr("transform", function (d) {
          return "translate(" + (d.x + 8) + "," + (d.y - 30) + ") scale(0.015,0.015)";
        })
      }

      // 鼠标滚轮缩放
      //_this.svg.call(d3.zoom().transform, d3.zoomIdentity);//缩放至初始倍数
      _this.svg.call(d3.zoom().on("zoom", function () {
        d3.select('#link_menubar').style('display', 'none');
        d3.select('#nodedetail').style('display', 'none');
        d3.selectAll('.node').attr("transform", d3.event.transform);
        d3.selectAll('.nodetext').attr("transform", d3.event.transform);
        d3.selectAll('.line').attr("transform", d3.event.transform);
        d3.selectAll('.linetext').attr("transform", d3.event.transform);
        d3.selectAll('.nodesymbol').attr("transform", d3.event.transform);
        d3.selectAll('.nodebutton').attr("transform", d3.event.transform);
        //_this.svg.selectAll("g").attr("transform", d3.event.transform);
      }));
      _this.svg.on("dblclick.zoom", null); // 静止双击缩放
      //按钮组事件
      _this.svg.selectAll(".buttongroup").on("click", function (d, i) {
        if (_this.nodebuttonAction) {
          switch (_this.nodebuttonAction) {
            case "EDIT":
              _this.isedit = true;
              _this.propactiveName = 'propedit';
              _this.txx = d.x;
              _this.tyy = d.y;
              _this.cleanStatus();
              _this.cleanOrder();
              _this.cleanAction();
              _this.getAllAction();
              _this.getAllOrder();
              _this.getAllStatus();
              _this.getAllWord();
              _this.getAllUserAttr();
              break;
            case "MORE":
              _this.getmorenode();
              break;
            case "CHILD":
              _this.operatetype = 2;
              _this.isbatchcreate = true;
              _this.isedit = false;
              break;
            case "LINK":
              _this.isaddlink = true;
              _this.selectsourcenodeid = d.uuid;
              break;
            case "DELETE":
              _this.selectnodeid = d.uuid;
              var out_buttongroup_id = '.out_buttongroup_' + i;
              _this.deletenode(out_buttongroup_id);
              break;
            case "toJson":
              _this.exportJsonVisible = true;
              break;
          }
          // ACTION = '';//重置 ACTION
        }

      });
      //按钮组事件绑定
      _this.svg.selectAll(".action_0").on("click", function (d) {
        _this.nodebuttonAction = 'EDIT';
      });
      _this.svg.selectAll(".action_1").on("click", function (d) {
        _this.nodebuttonAction = 'MORE';
      });
      _this.svg.selectAll(".action_2").on("click", function (d) {
        _this.nodebuttonAction = 'CHILD';
      });
      _this.svg.selectAll(".action_3").on("click", function (d) {
        _this.nodebuttonAction = 'LINK';
      });
      _this.svg.selectAll(".action_4").on("click", function (d) {
        _this.nodebuttonAction = 'DELETE';
      });
      _this.svg.selectAll(".action_5").on("click", function (d) {
        _this.nodebuttonAction = 'toJson';
      });
    },
    createnode() {
      var _this = this;
      var data = _this.graphEntity;
      data.domain = _this.domain;
      $.ajax({
        data: data,
        type: "POST",
        traditional: true,
        url: contextRoot + "createnode",
        success: function (result) {
          if (result.code == 200) {
            if (_this.graphEntity.uuid != 0) {
              for (var i = 0; i < _this.graph.nodes.length; i++) {
                if (_this.graph.nodes[i].uuid == _this.graphEntity.uuid) {
                  var newnode = result.data;
                  newnode.x = _this.txx;
                  newnode.y = _this.tyy;
                  newnode.fx = _this.txx;
                  newnode.fy = _this.tyy;
                  _this.graph.nodes.splice(i, 1, newnode);
                }
              }
            }
            _this.resetentity();
            _this.updategraph();
            _this.isedit = false;
            _this.resetsubmit();
          }
        }
      });
    },
    createSingleNode() {
      var _this = this;
      var data = {name: '', r: 30};
      data.domain = _this.domain;
      $.ajax({
        data: data,
        type: "POST",
        traditional: true,
        url: contextRoot + "createnode",
        success: function (result) {
          if (result.code == 200) {
            d3.select('.graphcontainer').style("cursor", "");
            var newnode = result.data;
            newnode.x = _this.txx;
            newnode.y = _this.tyy;
            newnode.fx = _this.txx;
            newnode.fy = _this.tyy;
            _this.graph.nodes.push(newnode);
            _this.updategraph();
          }
        }
      });
    },
    addmaker() {
      var arrowMarker = this.svg.append("marker")
          .attr("id", "arrow")
          .attr("markerUnits", "strokeWidth")
          .attr("markerWidth", "20")//
          .attr("markerHeight", "20")
          .attr("viewBox", "0 -5 10 10")
          .attr("refX", "22")// 13
          .attr("refY", "0")
          .attr("orient", "auto");
      var arrow_path = "M0,-5L10,0L0,5";// 定义箭头形状
      arrowMarker.append("path").attr("d", arrow_path).attr("fill", "#e23e55");
    },
    addnodebutton(r) {
      //先删除所有为节点自定义的按钮组
      d3.selectAll("svg >defs").remove();
      var nodes = this.graph.nodes;
      var database = [1, 1, 1, 1, 1, 1];
      var pie = d3.pie();
      var piedata = pie(database);
      var nodebutton = this.svg.append("defs");
      nodes.forEach(function (m) {
        var nbtng = nodebutton.append("g")
            .attr("id", "out_circle" + m.uuid);//为每一个节点定制一个按钮组，在画按钮组的时候为其指定该id
        var buttonEnter = nbtng.selectAll(".buttongroup")
            .data(piedata)
            .enter()
            .append("g")
            .attr("class", function (d, i) {
              return "action_" + i;
            });
        var defaultR = 30;
        if (typeof (m.r) == 'undefined') {
          m.r = defaultR;
        }
        var arc = d3.arc()
            .innerRadius(m.r)
            .outerRadius(m.r + 30);
        buttonEnter.append("path")
            .attr("d", function (d) {
              return arc(d)
            })
            .attr("fill", "#D2D5DA")
            .style("opacity", 0.6)
            .attr("stroke", "#f0f0f4")
            .attr("stroke-width", 2);
        buttonEnter.append("text")
            .attr("transform", function (d, i) {
              return "translate(" + arc.centroid(d) + ")";
            })
            .attr("text-anchor", "middle")
            .text(function (d, i) {
              var zi = new Array()
              zi[0] = "编辑";
              zi[1] = "展开";
              zi[2] = "追加";
              zi[3] = "连线";
              zi[4] = "删除";
              zi[5] = "生成Json"
              return zi[i]
            })
            .attr("font-size", 10);
      })
    },
    dragstarted(d) {
      if (!d3.event.active) this.simulation.alphaTarget(0.3).restart();
      d.fx = d.x;
      d.fy = d.y;
      d.fixed = true;
    },
    dragged(d) {
      d.fx = d3.event.x;
      d.fy = d3.event.y;
    },
    dragended(d) {
      if (!d3.event.active) this.simulation.alphaTarget(0);
      d.fx = d3.event.x;
      d.fy = d3.event.y;
      var domain = this.domain;
      var uuid = d.uuid;
      var fx = d.fx;
      var fy = d.fy;
      var ajaxdata = {domain: domain, uuid: uuid, fx: fx, fy: fy};
      $.ajax({
        data: ajaxdata,
        type: "POST",
        url: contextRoot + "updateCorrdOfNode",
        success: function (result) {
          if (result.code == 200) {
          }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
          alert(errorThrown)
        }
      });
    },
    drawnode(node) {
      var _this = this;
      var nodeEnter = node.enter().append("circle");
      nodeEnter.attr("r", function (d) {
        if (typeof (d.r) != "undefined" && d.r != '') {
          return d.r
        }
        return 30;
      });
      nodeEnter.attr("fill", function (d) {
        if (typeof (d.color) != "undefined" && d.color != '') {
          return d.color
        }
        return "#ff4500";
      });
      nodeEnter.style("opacity", 0.8);
      nodeEnter.style("stroke", function (d) {
        if (typeof (d.color) != "undefined" && d.color != '') {
          return d.color
        }
        return "#ff4500";
      });
      nodeEnter.style("stroke-opacity", 0.6);
      nodeEnter.append("title")// 为每个节点设置title
          .text(function (d) {
            return d.name;
          })
      nodeEnter.on("mouseover", function (d, i) {
        _this.nodedetail = d;
        _this.timer = setTimeout(function () {
          d3.select('#richContainer')
              .style('position', 'absolute')
              .style('left', d.x + "px")
              .style('top', d.y + "px")
              .style('display', 'block');
          _this.editorcontent = "";
          _this.showImageList = [];
          _this.getNodeDetail(d.uuid);
        }, 2000);
      });
      nodeEnter.on("mouseout", function (d, i) {
        clearTimeout(_this.timer);
      });
      nodeEnter.on("dblclick", function (d) {
        // _this.updatenodename(d);
        _this.getAllWord();
        _this.dialogNodeNameVisible = true; // 双击更新节点名称
      });
      nodeEnter.on("mouseenter", function (d) {
        var aa = d3.select(this)._groups[0][0];
        if (aa.classList.contains("selected")) return;
        d3.select(this).style("stroke-width", "6");
      });
      nodeEnter.on("mouseleave", function (d) {
        var aa = d3.select(this)._groups[0][0];
        if (aa.classList.contains("selected")) return;
        d3.select(this).style("stroke-width", "2");
      });
      nodeEnter.on("click", function (d, i) {
        d3.select('#nodedetail').style('display', 'block');
        var out_buttongroup_id = '.out_buttongroup_' + i;
        _this.svg.selectAll(".buttongroup").classed("circle_opreate", true);
        _this.svg.selectAll(out_buttongroup_id).classed("circle_opreate", false);
        _this.graphEntity = d;
        _this.selectnodeid = d.uuid;
        _this.selectnodename = d.name;
        _this.nodeName2 = d.name;
        // 添加连线状态
        if (_this.isaddlink) {
          _this.selecttargetnodeid = d.uuid;
          if (_this.selectsourcenodeid == _this.selecttargetnodeid || _this.selectsourcenodeid == 0 || _this.selecttargetnodeid == 0) return;
          _this.createlink(_this.selectsourcenodeid, _this.selecttargetnodeid, "RE")
          _this.selectsourcenodeid = 0;
          _this.selecttargetnodeid = 0;
          d.fixed = false
          d3.event.stopPropagation();
        }
      });
      nodeEnter.call(d3.drag()
          .on("start", _this.dragstarted)
          .on("drag", _this.dragged)
          .on("end", _this.dragended));
      return nodeEnter;
    },
    drawnodetext(nodetext) {
      var _this = this;
      var nodetextenter = nodetext.enter().append("text")
          .style("fill", "#fff")
          .attr("dy", 4)
          .attr("font-family", "微软雅黑")
          .attr("text-anchor", "middle")
          .text(function (d) {
            if (typeof (d.name) == 'undefined') return '';
            var length = d.name.length;
            if (d.name.length > 4) {
              var s = d.name.slice(0, 4) + "...";
              return s;
            }
            return d.name;
          });
      nodetextenter.on("mouseover", function (d, i) {
        _this.timer = setTimeout(function () {
          d3.select('#richContainer')
              .style('position', 'absolute')
              .style('left', d.x + "px")
              .style('top', d.y + "px")
              .style('display', 'block');
          _this.editorcontent = "";
          _this.showImageList = [];
          _this.getNodeDetail(d.uuid);
        }, 3000);
      });

      nodetextenter.on("dblclick", function (d) {
        // _this.updatenodename(d);
        _this.getAllWord();
        _this.dialogNodeNameVisible = true; // 双击更新节点名称
      });
      nodetextenter.on("click", function (d) {
        $('#link_menubar').hide();// 隐藏空白处右键菜单
        _this.graphEntity = d;
        _this.selectnodeid = d.uuid;
        // 更新工具栏节点信息
        _this.getcurrentnodeinfo(d);
        // 添加连线状态
        if (_this.isaddlink) {
          _this.selecttargetnodeid = d.uuid;
          if (_this.selectsourcenodeid == _this.selecttargetnodeid || _this.selectsourcenodeid == 0 || _this.selecttargetnodeid == 0) return;
          _this.createlink(_this.selectsourcenodeid, _this.selecttargetnodeid, "RE")
          _this.selectsourcenodeid = 0;
          _this.selecttargetnodeid = 0;
          d.fixed = false
          d3.event.stopPropagation();
        }
      });

      return nodetextenter;
    },
    drawnodesymbol(nodesymbol) {
      var _this = this;
      var symnol_path = "M566.92736 550.580907c30.907733-34.655573 25.862827-82.445653 25.862827-104.239787 0-108.086613-87.620267-195.805867-195.577173-195.805867-49.015467 0-93.310293 18.752853-127.68256 48.564907l-0.518827-0.484693-4.980053 4.97664c-1.744213 1.64864-3.91168 2.942293-5.59104 4.72064l0.515413 0.484693-134.69696 133.727573L216.439467 534.8352l0 0 137.478827-136.31488c11.605333-10.410667 26.514773-17.298773 43.165013-17.298773 36.051627 0 65.184427 29.197653 65.184427 65.24928 0 14.032213-5.33504 26.125653-12.73856 36.829867l-131.754667 132.594347 0.515413 0.518827c-10.31168 11.578027-17.07008 26.381653-17.07008 43.066027 0 36.082347 29.16352 65.245867 65.184427 65.245867 16.684373 0 31.460693-6.724267 43.035307-17.07008l0.515413 0.512M1010.336427 343.49056c0-180.25472-145.882453-326.331733-325.911893-326.331733-80.704853 0-153.77408 30.22848-210.418347 79.0528l0.484693 0.64512c-12.352853 11.834027-20.241067 28.388693-20.241067 46.916267 0 36.051627 29.16352 65.245867 65.211733 65.245867 15.909547 0 29.876907-6.36928 41.192107-15.844693l0.38912 0.259413c33.624747-28.030293 76.301653-45.58848 123.511467-45.58848 107.99104 0 195.549867 87.6544 195.549867 195.744427 0 59.815253-27.357867 112.71168-69.51936 148.503893l0 0-319.25248 317.928107 0 0c-35.826347 42.2912-88.654507 69.710507-148.340053 69.710507-107.956907 0-195.549867-87.68512-195.549867-195.805867 0-59.753813 27.385173-112.646827 69.515947-148.43904l-92.18048-92.310187c-65.69984 59.559253-107.700907 144.913067-107.700907 240.749227 0 180.28544 145.885867 326.301013 325.915307 326.301013 95.218347 0 180.02944-41.642667 239.581867-106.827093l0.13312 0.129707 321.061547-319.962453-0.126293-0.13312C968.69376 523.615573 1010.336427 438.71232 1010.336427 343.49056L1010.336427 343.49056 1010.336427 343.49056zM1010.336427 343.49056";// 定义回形针形状
      var nodesymbolEnter = nodesymbol.enter().append("path").attr("d", symnol_path);
      nodesymbolEnter.call(d3.drag()
          .on("start", _this.dragstarted)
          .on("drag", _this.dragged)
          .on("end", _this.dragended));
      return nodesymbolEnter;
    },
    drawnodebutton(nodebutton) {
      var _this = this;
      var nodebuttonEnter = nodebutton.enter().append("g").append("use")//  为每个节点组添加一个 use 子元素
          .attr("r", function (d) {
            return d.r;
          })
          .attr("xlink:href", function (d) {
            return "#out_circle" + d.uuid;
          }) //  指定 use 引用的内容
          .attr('class', function (d, i) {
            return 'buttongroup out_buttongroup_' + i;
          })
          .classed("circle_opreate", true);

      return nodebuttonEnter;
    },
    drawlink(link) {
      var _this = this;
      var linkEnter = link.enter().append("path")
          .attr("stroke-width", 1)
          .attr("stroke", "#f68f7d")
          .attr("fill", "none")
          .attr("id", function (d) {
            return "invis_" + d.lk.sourceid + "-" + d.lk.name + "-" + d.lk.targetid;
          })
          .attr("marker-end", "url(#arrow)");// 箭头
      linkEnter.on("dblclick", function (d) {
        _this.selectnodeid = d.lk.uuid;
        if (_this.isdeletelink) {
          _this.deletelink();
        } else {
          _this.updatelinkName();
        }
      });
      linkEnter.on("contextmenu", function (d) {
        var cc = $(this).offset();
        _this.selectnodeid = d.lk.uuid;
        _this.selectlinkname = d.lk.name;
        d3.select('#link_menubar')
            .style('position', 'absolute')
            .style('left', cc.left + "px")
            .style('top', cc.top + "px")
            .style('display', 'block');
        d3.event.preventDefault();// 禁止系统默认右键
        d3.event.stopPropagation();// 禁止空白处右键
      });
      linkEnter.on("mouseenter", function (d) {
        d3.select(this).style("stroke-width", "6").attr("stroke", "#f68f7d").attr("marker-end", "url(#arrow)");
        _this.nodedetail = d.lk;
        d3.select('#nodedetail').style('display', 'block');
      });
      linkEnter.on("mouseleave", function (d) {
        d3.select(this).style("stroke-width", "1").attr("stroke", "#f68f7d").attr("marker-end", "url(#arrow)");
      });
      return linkEnter;
    },
    drawlinktext(link) {
      var _this = this;
      var linktextEnter = link.enter().append('text')
          .style('fill', '#f68f7d')
          .append("textPath")
          .attr("startOffset", "50%")
          .attr("text-anchor", "middle")
          .attr("xlink:href", function (d) {
            return "#invis_" + d.lk.sourceid + "-" + d.lk.name + "-" + d.lk.targetid;
          })
          .style("font-size", 16)
          .text(function (d) {
            if (d.lk.name != '') {
              return d.lk.name;
            }
          });

      linktextEnter.on("mouseover", function (d) {
        _this.selectnodeid = d.lk.uuid;
        _this.selectlinkname = d.lk.name;
        var cc = $(this).offset();
        d3.select('#link_menubar')
            .style('position', 'absolute')
            .style('left', cc.left + "px")
            .style('top', cc.top + "px")
            .style('display', 'block');
      });

      return linktextEnter;
    },
    deletenode(out_buttongroup_id) {
      var _this = this;
      _this.$confirm('此操作将删除该节点及周边关系(不可恢复), 是否继续?', '三思而后行', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        var data = {domain: _this.domain, nodeid: _this.selectnodeid};
        $.ajax({
          data: data,
          type: "POST",
          url: contextRoot + "deletenode",
          success: function (result) {
            if (result.code == 200) {
              _this.svg.selectAll(out_buttongroup_id).remove();
              var rships = result.data;
              // 删除节点对应的关系
              for (var m = 0; m < rships.length; m++) {
                for (var i = 0; i < _this.graph.links.length; i++) {
                  if (_this.graph.links[i].uuid == rships[m].uuid) {
                    _this.graph.links.splice(i, 1);
                    i = i - 1;
                  }
                }
              }
              // 找到对应的节点索引
              var j = -1;
              for (var i = 0; i < _this.graph.nodes.length; i++) {
                if (_this.graph.nodes[i].uuid == _this.selectnodeid) {
                  j = i;
                  break;
                }
              }
              if (j >= 0) {
                _this.selectnodeid = 0;
                _this.graph.nodes.splice(i, 1);// 根据索引删除该节点
                _this.updategraph();
                _this.resetentity();
                _this.$message({
                  type: 'success',
                  message: '操作成功!'
                });
              }

            }
          }
        })
      }).catch(function () {
        _this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    deletelink() {
      var _this = this;
      this.$confirm('此操作将删除该关系(不可恢复), 是否继续?', '三思而后行', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        var data = {domain: _this.domain, shipid: _this.selectnodeid};
        $.ajax({
          data: data,
          type: "POST",
          url: contextRoot + "deletelink",
          success: function (result) {
            if (result.code == 200) {
              var j = -1;
              for (var i = 0; i < _this.graph.links.length; i++) {
                if (_this.graph.links[i].uuid == _this.selectnodeid) {
                  j = i;
                  break;
                }
              }
              if (j >= 0) {
                _this.selectnodeid = 0;
                _this.graph.links.splice(i, 1);
                _this.updategraph();
                _this.isdeletelink = false;
              }
            }
          }
        });
      }).catch(function () {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    createlink(sourceId, targetId, ship) {
      var _this = this;
      var data = {domain: _this.domain, sourceid: sourceId, targetid: targetId, ship: ''};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "createlink",
        success: function (result) {
          if (result.code == 200) {
            var newship = result.data;
            _this.graph.links.push(newship);

            _this.updategraph();
            _this.isaddlink = false;
          }
        }
      });
    },
    updatelinkName() {
      var _this = this;
      _this.$prompt('请输入关系名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: this.selectlinkname
      }).then(function (res) {
        var value = res.value;
        var data = {domain: _this.domain, shipid: _this.selectnodeid, shipname: value};
        $.ajax({
          data: data,
          type: "POST",
          url: contextRoot + "updatelink",
          success: function (result) {
            if (result.code == 200) {
              var newship = result.data;
              _this.graph.links.forEach(function (m) {
                if (m.uuid == newship.uuid) {
                  m.name = newship.name;
                }
              });
              _this.selectnodeid = 0;
              _this.updategraph();
              _this.isaddlink = false;
              _this.selectlinkname = '';
            }
          }
        });
      }).catch(function () {
        _this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    },
    updatenodename() {
      var _this = this;
      var value = _this.graphEntity.name;
      var data = {domain: _this.domain, nodeid: _this.graphEntity.uuid, nodename: value};
      _this.$axios.get("/updatenodename", {
            params: {
              domain: data.domain,
              nodeid: data.nodeid,
              nodename: data.nodename
            }
          }
      ).then(
          (response) => {
            if (_this.graphEntity.uuid != 0) {
              for (var i = 0; i < _this.graph.nodes.length; i++) {
                if (_this.graph.nodes[i].uuid == _this.graphEntity.uuid) {
                  _this.graph.nodes[i].name = value;
                }
              }
            }
            _this.dialogNodeNameVisible = false
            _this.updategraph();
            _this.$message({
              message: '操作成功',
              type: 'success'
            });
          }
      ).catch(
          (error) => {
          }
      )
    },
    resetsubmit() {
      this.isaddnode = false;
      this.isedit = false;
      this.resetentity();
      this.fieldDataList = [];
      this.dataconfigactive = '';
      this.isbatchcreate = false;
      this.selectnodeid = 0;
    },
    resetentity() {
      this.graphEntity = {
        uuid: 0,
        name: '',
        type: '',
        modelVersion: '',
        code: '',
        alias: '',
        namespace: '',
        language: '',
        translated: '',
        extendable: '',
        color: '#ff4500',
        r: 30,
        x: "",
        y: ""
      };
    },
    fillinform(d) {
      this.graphEntity = {
        uuid: d.uuid,
        name: d.name,
        color: d.color,
        r: d.r,
        x: d.x,
        y: d.y
      };
    },
    // matchdomaingraph_false(domain) {
    //   var _this = this;
    //   _this.domain = domain.name;
    //   _this.domainid = domain.id;
    //   _this.getdomaingraph_false();
    //
    // },
    matchdomaingraph(domain, event) {
      this.domain = domain.name;
      this.domainid = domain.id;
      this.getdomaingraph();
    },
    refreshnode(event) {
      $(".ml-a").removeClass("cur");
      $(event.currentTarget).addClass("cur");
      this.nodename = '';
      this.getdomaingraph();
    },
    getFilterDomain(domainList) {
      var array = [];
      for (var i = 0; i < domainList.length; i++) {
        array.push({value: domainList[i].label.substring(1, domainList[i].label.length - 1)});
      }
      return array;
    },
    operateCommand(command) {
      if (command === 'image') {
        this.exportimage();
      }
      if (command === 'import') {
        this.dialogFormVisible = true;
      }
      if (command === 'export') {
        this.exportFormVisible = true;
      }
    },
    exportcsv() {
      var _this = this;
      $.ajax({
        data: {domain: _this.uploadparam.domain},
        type: "POST",
        url: contextRoot + "exportgraph",
        success: function (result) {
          if (result.code == 200) {
            _this.exportFormVisible = false;
            window.open(result.csvurl);
          }
        }
      });
    },
    submitUpload() {
      this.$refs.upload.submit();
      this.dialogFormVisible = false;
    },
    csvsuccess() {
      this.$refs.upload.clearFiles();
      this.uploadparam.domain = "";
      this.$message({
        message: "正在导入中,请稍后查看",
        type: 'success'
      });
    },

    exportimage() {
      /*https://html2canvas.hertzen.com/getting-started  截图js*/
      html2canvas(document.querySelector(".graphcontainer")).then(function (canvas) {
        var a = document.createElement('a');
        a.href = canvas.toDataURL('image/png');  //将画布内的信息导出为png图片数据
        var timestamp = Date.parse(new Date());
        a.download = timestamp;  //设定下载名称
        a.click(); //点击触发下载
      });
    },
    setmatchsize(m, event) {
      for (var i = 0; i < this.pagesizelist.length; i++) {
        this.pagesizelist[i].isactive = false;
        if (this.pagesizelist[i].size == m.size) {
          this.pagesizelist[i].isactive = true;
        }
      }
      this.pagesize = m.size;
      this.getdomaingraph();
    },
    batchcreatenode() {
      var _this = this;
      var data = {
        domain: _this.domain,
        sourcename: _this.batchcreate.sourcenodename,
        targetnames: _this.batchcreate.targetnodenames,
        relation: _this.batchcreate.relation
      };
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "batchcreatenode",
        success: function (result) {
          if (result.code == 200) {
            _this.isbatchcreate = false;
            var newnodes = result.data.nodes;
            var newships = result.data.ships;
            newnodes.forEach(function (m) {
              var sobj = _this.graph.nodes.find(function (x) {
                return x.uuid === m.uuid
              })
              if (typeof (sobj) == "undefined") {
                _this.graph.nodes.push(m);
              }
            })
            newships.forEach(function (m) {
              var sobj = _this.graph.links.find(function (x) {
                return x.uuid === m.uuid
              })
              if (typeof (sobj) == "undefined") {
                _this.graph.links.push(m);
              }
            })
            _this.updategraph();
            _this.batchcreate.sourcenodename = '';
            _this.batchcreate.targetnodenames = '';
            _this.$message({
              message: '操作成功',
              type: 'success'
            });
          }
        }
      });
    },
    batchcreatechildnode() {
      var _this = this;
      var data = {
        domain: _this.domain,
        sourceid: _this.selectnodeid,
        targetnames: _this.batchcreate.targetnodenames,
        relation: _this.batchcreate.relation
      };
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "batchcreatechildnode",
        success: function (result) {
          if (result.code == 200) {
            _this.isbatchcreate = false;
            var newnodes = result.data.nodes;
            var newships = result.data.ships;
            newnodes.forEach(function (m) {
              var sobj = _this.graph.nodes.find(function (x) {
                return x.uuid === m.uuid
              })
              if (typeof (sobj) == "undefined") {
                _this.graph.nodes.push(m);
              }
            })
            newships.forEach(function (m) {
              var sobj = _this.graph.links.find(function (x) {
                return x.uuid === m.uuid
              })
              if (typeof (sobj) == "undefined") {
                _this.graph.links.push(m);
              }
            })
            _this.updategraph();
            _this.batchcreate.sourcenodename = '';
            _this.batchcreate.targetnodenames = '';
            _this.$message({
              message: '操作成功',
              type: 'success'
            });
          }
        }
      });
    },
    batchcreatesamenode() {
      var _this = this;
      var data = {domain: _this.domain, sourcenames: _this.batchcreate.sourcenodename};
      $.ajax({
        data: data,
        type: "POST",
        url: contextRoot + "batchcreatesamenode",
        success: function (result) {
          if (result.code == 200) {
            _this.isbatchcreate = false;
            var newnodes = result.data;
            newnodes.forEach(function (m) {
              var sobj = _this.graph.nodes.find(function (x) {
                return x.uuid === m.uuid
              })
              if (typeof (sobj) == "undefined") {
                _this.graph.nodes.push(m);
              }
            })
            _this.updategraph();
            _this.batchcreate.sourcenodename = '';
            _this.$message({
              message: '操作成功',
              type: 'success'
            });
          }
        }
      });
    }
  }
}

</script>
<style lang="scss">

.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-bottom: 0;
  width: 40%;
}

.step_container {
  width: 1500px;
  margin: 50px auto;

  .step_row_data {
    min-height: 150px;
    width: 100%;
    overflow: hidden;

    .step_column_data {
      float: left;
    }

    .step_column_data:nth-child(1) {
      margin-right: 20px;
      padding-top: 18px;
      width: 150px;
      text-align: right;

      span:hover {
        cursor: pointer;
        color: #6495ed;
      }

      .step_explain {
        width: 150px;
        outline: none;
        border: none;
        border-bottom: 1px solid cornflowerblue;
      }
    }

    .step_column_data:nth-child(2) {
      margin-left: 10px;
      padding-left: 20px;
      padding-top: 15px;
      width: 500px;
      padding-bottom: 30px;
      position: relative;
      min-height: 150px;
      border-left: 2px solid red;

      .step_right {
        margin-left: 30px;
      }

      .step_num {
        width: 40px;
        height: 40px;
        box-sizing: border-box;
        border-radius: 20px;
        text-align: center;
        line-height: 35px;
        background: #faf6f6;
        border: 1px solid lightgray;
        position: absolute;
        top: 15px;
        left: -21px;
      }

      .step_time {
        position: absolute;
        left: -110px;
        top: 45px;

        color: lightgrey;

        span {
          margin-bottom: 0px;
        }

        span:hover {
          cursor: pointer;
          color: cornflowerblue;
        }

        .time_input {
          outline: none;
          border: none;
          border-bottom: 1px solid cornflowerblue;
        }
      }

      /*.step_time:hover{*/
      /*cursor: pointer;*/
      /*color: cornflowerblue;*/
      /*}*/
      span {
        float: left;
        margin-right: 20px;

      }

      button {
        float: left;
        margin-bottom: 15px;
      }

      .button-new-tag {
        line-height: 20px;
        text-align: center;
        height: 40px;
        border-width: 1px;
        border-style: solid;
        border-radius: 4px;
      }

      .input-new-tag {
        float: left;
        margin-bottom: 15px;
      }
    }
  }

  .input-new-tag {
    max-width: 150px;
  }

  .el-input__suffix {
    width: 10px;
  }

  .addTableColumn {
    width: 40px;
    text-align: right;
    margin-top: 20px;

    i {
      font-size: 30px;
      color: #6495ED;
      cursor: pointer;
    }
  }

  .addStep, .decreaseStep {
    width: 196px;
    text-align: right;
    margin-top: 10px;

    i {
      font-size: 30px;
      color: #409EFF;
      cursor: pointer;
    }
  }
}

.container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.link {
  margin: 10px;
}

/*引入*/
.pl-20 {
  padding-left: 20px
}

text {
  cursor: pointer;
  max-width: 25px;
  display: inline-block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  vertical-align: middle;
}

circle {
  cursor: pointer;
}

#graphcontainerdiv {
  background: #fff;
}

.el-color-picker__panel {
  left: 812px !important;
}

.wange-toolbar {
  border: 1px solid #ccc;
}

.wangeditor-form {
  border: 1px solid #ccc;
  height: 350px;
  min-height: 340px
}

.mind-fj-box {
  display: inline-block;
  width: 290px;
  padding: 5px;
  border: 1px solid #e6e6e6;
  box-shadow: 0 0 8px rgba(206, 205, 201, .38);
}

.mind-fj-p {
  color: #666;
  line-height: 24px;
  padding: 5px;
  background: rgba(255, 255, 255, .85);
}

.mind-carousel + .mind-fj-p .el-scrollbar__wrap {
  height: auto;
  max-height: 220px;
  min-height: 0;
}

.carous-img {
  height: 100%;
  background: rgba(0, 0, 0, .10);
  line-height: 197px;
  text-align: center;
}

.carous-img img {
  max-width: 100%;
  max-height: 100%;
  line-height: 197px;
  vertical-align: middle;
}

.circle_opreate {
  display: none;
}

.node_detail {
  position: absolute;
  width: 100%;
  line-height: 35px;
  -webkit-border-radius: 10px;
  -moz-border-radius: 10px;
  border-radius: 10px;
  font-size: 12px;
  padding-bottom: 10px;
  background: rgba(198, 226, 255, 0.2);
  display: none;
}

.node_pd {
  padding: 4px;
  font-size: 13px;
  font-family: -webkit-body;
  font-weight: 600;
}

.operatetips {
  position: absolute;
  right: 10px;
  float: right;
  top: 0;
  width: 220px;
  padding: 30px;
  border: 2px #EE7942 solid;
  border-radius: 4px;
}

.jsoncontainer {
  position: absolute;
  right: 30%;
  float: right;
  top: 0;
  width: 60%;
  height: 60%;
  padding: 30px;
  border: 2px #EE7942 solid;
  border-radius: 4px;
  background: #fff;
}

.cypher_toolbar {
  line-height: 70px;
  height: 85px;
  padding: 0 22px;
  border-bottom: 1px solid #ededed;
}

</style>
