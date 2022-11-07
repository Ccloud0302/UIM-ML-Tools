<template>
  <el-container>
    <el-header>
      <el-row class="state_header">
        <!--                    <el-col :span="5">-->
        <!--                      请选择实体:-->
        <!--                      <el-select v-model="selectnodeid" clearable placeholder="请选择" @change="getAll()"-->
        <!--                                 style="margin-top: 20px"-->
        <!--                                 no-data-text="请选择上下文">-->
        <!--                        <el-option-->
        <!--                            v-for="item in graph.nodes"-->
        <!--                            :key="item.uuid"-->
        <!--                            :label="item.name"-->
        <!--                            :value="item.uuid">-->
        <!--                        </el-option>-->
        <!--                      </el-select>-->
        <!--                    </el-col>-->
        <el-col :span="5">
          请选择模式：
<!--          Select Mode:-->
          <el-select v-model="selectMode" clearable placeholder="请选择"
                     style="margin-top: 20px"
                     no-data-text="请选择实体" @change="initStateGraph()">
            <el-option
                v-for="item in modeList"
                :key="item.id"
                :label="item.name"
                :value="item.name">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <div class="container_warp">
        <div id="stateFlowContainer"></div>
        <RightDrawer class="right_drawer" :drawerType="type" :selectCell="selectCell"
                     :graph="graph" :grid="grid"
                     :commandList="orderList" :eventList="actionList" :nodeId="selectnodeid" :domainId="domainid" :mode="selectMode"
                     @deleteNode="deleteNode"></RightDrawer>
        <div class="operating">
          <div class="btn-group">
            <div class="btn" title="圆形节点" @mousedown="startDrag('Circle',$event)">
              <i class="iconfont icon-circle"></i>
            </div>
            <div class="btn" title="正方形节点" @mousedown="startDrag('Rect',$event)">
              <i class="iconfont icon-square"></i>
            </div>
            <div class="btn" title="条件节点">
              <i class="iconfont icon-square rotate-square" @mousedown="startDrag('polygon',$event)"></i>
            </div>
            <div class="btn" title="边框">
              <i class="iconfont icon-square" @mousedown="startDrag('ParentRect',$event)"></i>
            </div>
            <div class="btn-group_tips" v-if="showTips">
              拖拽生成<br/>资产拓扑图形
            </div>
          </div>
          <div class="btn-group">
            <el-tooltip content="直线箭头" placement="bottom">
              <div :class=" ['btn',currentArrow === 1?'currentArrow':'']" @click="changeEdgeType('normal')">
                <i class="iconfont icon-ai28"></i>
              </div>
            </el-tooltip>
            <el-tooltip content="曲线箭头" placement="bottom">
              <div :class=" ['btn',currentArrow === 2?'currentArrow':'']" @click="changeEdgeType('smooth')">
                <i class="iconfont icon-Down-Right"></i>
              </div>
            </el-tooltip>
            <el-tooltip content="直角箭头" placement="bottom">
              <div :class=" ['btn',currentArrow === 3?'currentArrow':'']" @click="changeEdgeType()">
                <i class="iconfont icon-jiantou"></i>
              </div>
            </el-tooltip>
          </div>
          <div class="btn-group">
            <el-tooltip content="删除" placement="bottom">
              <div class="btn" @click="deleteNode()" style="margin-top: 5px;">
                <i class="iconfont icon-shanchu"></i>
              </div>
            </el-tooltip>
            <el-tooltip content="保存" placement="bottom">
              <div class="btn" @click="saveToJson()" title="保存">
                <i class="iconfont icon-baocun"></i>
              </div>
            </el-tooltip>
            <el-tooltip content="保存PNG" placement="bottom">
              <div class="btn" @click="saveToPNG()" title="保存">
                <i class="iconfont icon-baocuntupian"></i>
              </div>
            </el-tooltip>
          </div>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import RightDrawer from './RightDrawer';
import '@antv/x6-vue-shape'
import {Graph, Shape, Addon, FunctionExt, DataUri} from '@antv/x6'
import insertCss from 'insert-css';
import {startDragToGraph} from '../assets/StateFlow/methods.js'
import $ from "jquery";
// let contextRoot = "http://localhost:8081/";
let protocol = window.location.protocol; //协议
let host = window.location.host; //主机
let reg = /^localhost+/;
let host_l = host.split(":")
let contextRoot = protocol + "//" + host_l[0] + ":8081/";

export default {
  name: "StateFlow",
  components: {RightDrawer},
  props: {
    selectnodeid: {
      type: String | Number,
    },
    domainid: {
      type: Number,
    },
    orderList: {
      type: Array | Object,
    },
    actionList: {
      type: Array | Object,
    },
    actionAndStatusShow: {
      type: Boolean
    },
  },
  data() {
    return {
      selectMode: "Production",
      modeList: [{id: 0, name: "Production"},
        {id: 1, name: "Maintenance"},
        {id: 2, name: "Manual"}
      ],
      graph: '',
      statePic: {},
      value1: true,
      type: 'grid',
      selectCell: '',
      connectEdgeType: {  //连线方式
        connector: 'normal',
        router: {
          name: ''
        }
      },
      showTips: false,
      currentArrow: 1,
      grid: { // 网格设置
        size: 20,      // 网格大小 10px
        visible: true, // 渲染网格背景
        type: 'mesh',
        args: {
          color: '#D0D0D0',
          thickness: 1, // 网格线宽度/网格点大小
          factor: 10
        }
      },
    }
  },
  mounted() {
    var _this = this;
    console.log("again");
    if (_this.actionAndStatusShow === true){
      if (document.getElementById('stateFlowContainer') === null) {
        const judgeGraphDiv = setInterval(() => {
          if (document.getElementById('stateFlowContainer') !== null) {
            window.clearInterval(judgeGraphDiv);
            this.initStateGraph();
          }
        }, 1000)
      } else {
        this.initStateGraph();
      }
    }
  },
  created() {
    var _this = this;
    // console.log("command");
    // console.log(_this.nodeId);
    // console.log(_this.commandList);
  },
  watch: {
    orderList: {
      handler(curVal, oldVal) {
        console.log(curVal)
      },
      deep: true //深度监听

    },
  },
  methods: {
    initStateGraph() {
      var _this = this;
      _this.graph = new Graph({
        container: document.getElementById('stateFlowContainer'),
        width: 1200,
        height: 1000,
        grid: _this.grid,
        resizing: { // 调整节点宽高
          enabled: true,
          orthogonal: false,
        },
        selecting: {
          enabled: true,
          rubberband: true, // 启用框选
        }, //框选
        snapline: true, //对齐线
        interacting: {
          edgeLabelMovable: true
        },
        connecting: { // 节点连接
          anchor: 'center',
          connectionPoint: 'anchor',
          allowBlank: false,
          snap: true,
          createEdge() {
            return new Shape.Edge({
              attrs: {
                line: {
                  stroke: '#1890ff',
                  strokeWidth: 1,
                  targetMarker: {
                    name: 'classic',
                    size: 8
                  },
                  strokeDasharray: 0, //虚线
                  style: {
                    animation: 'ant-line 30s infinite linear',
                  }
                },
                text:{fontSize: 12}
              },
              label: {
                text: '',
              },
              connector: _this.connectEdgeType.connector,
              router: {
                name: _this.connectEdgeType.router.name || ''
              },
              zIndex: 0
            })
          },
        },
        highlighting: {
          magnetAvailable: {
            name: 'stroke',
            args: {
              padding: 4,
              attrs: {
                strokeWidth: 4,
                stroke: '#6a6c8a'
              }
            }
          }
        },
        panning: {  //画布平移
          enabled: false,
          eventTypes: ['leftMouseDown', 'rightMouseDown', 'mouseWheel']
        },
        embedding: {  //嵌入父节点
          enabled: true,
          findParent({node}) {
            const bbox = node.getBBox()
            return this.getNodes().filter((node) => {
              // 只有 data.parent 为 true 的节点才是父节点
              const data = node.getData()
              if (data && data.parent) {
                const targetBBox = node.getBBox()
                return bbox.isIntersectWithRect(targetBBox)
              } else {
                return false
              }
            })
          }
        },
        // scroller: {  //画布缩放
        //   enabled: true,
        //   pageVisible: false,
        //   pageBreak: false,
        //   pannable: true,
        // },
        // mousewheel: {  //滚轮缩放
        //   enabled: true,
        //   global: true,
        //   modifiers: ['ctrl', 'meta'],
        // },
      });
      insertCss(`
            @keyframes ant-line {
              to {
                  stroke-dashoffset: -1000
              }
            }
          `)

      //加载cells
      // _this.graph.fromJSON(_this.statePic);
      if (_this.selectnodeid && _this.domainid) {
        _this.renderDataFUN(_this.graph);
      }
      _this.graph.history.redo();
      _this.graph.history.undo();

      // 鼠标移入移出节点
      _this.graph.on('node:mouseenter', FunctionExt.debounce(() => {
            const container = document.getElementById('stateFlowContainer')
            const ports = container.querySelectorAll(
                '.x6-port-body'
            )
            _this.showPorts(ports, true)
          }),
          500
      )
      _this.graph.on('node:mouseleave', () => {
        const container = document.getElementById('stateFlowContainer')
        const ports = container.querySelectorAll(
            '.x6-port-body'
        )
        _this.showPorts(ports, false)
      })
      _this.graph.on('blank:click', () => {
        this.type = 'grid'
      })
      _this.graph.on('cell:click', ({cell}) => {
        this.type = cell.isNode() ? 'node' : 'edge'
      })
      _this.graph.on('selection:changed', (args) => {
        args.added.forEach(cell => {
          this.selectCell = cell
          if (cell.isEdge()) {
            cell.isEdge() && cell.attr('line/strokeDasharray', 5) //虚线蚂蚁线
            cell.addTools([
              {
                name: 'vertices',
                args: {
                  padding: 4,
                  attrs: {
                    strokeWidth: 0.1,
                    stroke: '#2d8cf0',
                    fill: '#ffffff',
                  }
                },
              },
            ])
          }
        })
        args.removed.forEach(cell => {
          cell.isEdge() && cell.attr('line/strokeDasharray', 0)  //正常线
          cell.removeTools()
        })
      })
    },
    showPorts(ports, show) {
      for (let i = 0, len = ports.length; i < len; i = i + 1) {
        ports[i].style.visibility = show ? 'visible' : 'hidden'
      }
    },
    // 拖拽生成正方形或者圆形
    startDrag(type, e) {
      startDragToGraph(this.graph, type, e)
    },
    // 删除节点
    deleteNode() {
      const cell = this.graph.getSelectedCells()
      this.graph.removeCells(cell)
      this.type = 'grid'
    },
    // 保存png
    saveToPNG() {
      this.$nextTick(() => {
        this.graph.toPNG((dataUri) => {
          // 下载
          DataUri.downloadDataUri(dataUri, '状态图.png')
        }, {
          backgroundColor: 'white',
          padding: {
            top: 50,
            right: 50,
            bottom: 50,
            left: 50
          },
          quality: 1,
          copyStyles: false
        })
      })
    },
    // 保存json
    saveToJson() {
      var _this = this;
      const json = _this.graph.toJSON();
      const data = JSON.stringify(json);
      var save_data = {graphData: data, domainId: this.domainid, nodeId: this.selectnodeid, mode: this.selectMode}
      $.ajax({
        data: save_data,
        type: "POST",
        url: contextRoot + "addStateFlow",
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
      console.log('保存数据', data);
    },
    //加载cells
    renderDataFUN(graph) {
      var _this = this;
      var data = {domainId: _this.domainid, nodeId: _this.selectnodeid, mode: _this.selectMode};
      $.ajax({
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
              console.log("按照指定的 JSON 数据渲染节点和边", data);
              _this.graph.fromJSON(data);
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
    },
    // 改变边形状
    changeEdgeType(e) {
      if (e === 'normal') {
        this.connectEdgeType = {
          connector: 'normal',
          router: {name: ''}
        }
        this.currentArrow = 1
      } else if (e === 'smooth') {
        this.connectEdgeType = {
          connector: 'smooth',
          router: {name: ''}
        }
        this.currentArrow = 2
      } else {
        this.connectEdgeType = {
          connector: 'normal',
          router: {name: 'manhattan'}
        }
        this.currentArrow = 3
      }
    }
  },

}
</script>

<style lang="less">
@import '../assets/StateFlow/iconfont.css';
@import '../assets/StateFlow/index.less';
.state_header{
  padding-top: 4px;
  padding-bottom: 12px;
  display: flex;
}

</style>
