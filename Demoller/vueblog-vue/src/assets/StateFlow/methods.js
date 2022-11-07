import '@antv/x6-vue-shape';
import { Graph,Shape,Addon,FunctionExt} from '@antv/x6'
// 拖拽生成四边形或者圆形
export const startDragToGraph  = (graph,type,e) =>{
    const node =
    type === 'Rect'
    ? graph.createNode({
      width: 100,
      height: 60,
      zIndex: 10,
      attrs: {
        label: {
          text: '正方形节点',
          fill: '#000000',
          fontSize: 14,
          textWrap: {
            width: -10,
            height: -10,
            ellipsis: true
          }
        },
        body: {
          stroke: '#000000',
          strokeWidth: 1,
          fill: '#EE8B8B'
        }
      },
      data: {
            nodeTranToMode: [],
            des: "",
            parent: false,
          },
      ports: ports
    })
    : type === 'Circle'
    ? graph.createNode({
      shape: 'ellipse',
      width: 100,
      height: 100,
      zIndex: 10,
      attrs: {
        label: {
          text: '圆形节点',
          fill: '#000000',
          fontSize: 14,
          textWrap: {
            width: -20,
            height: -10,
            ellipsis: true
          }
        },
        body: {
          stroke: '#000000',
          strokeWidth: 1,
          fill: '#EE8B8B'
        }
      },
      data: {
        nodeTranToMode: [],
        des: "",
        parent: false,
          },
      ports: ports
    })
    : type === 'ParentRect'
    ? graph.createNode({
              width: 500,
              height: 350,
              zIndex: 0,
              attrs: {
                label: {
                  refY: 130,
                  fill: '#000000',

                  textWrap: {
                    width: -20,
                    height: -10,
                    ellipsis: true
                  }
                },
                body: {
                  stroke: '#000000',
                  strokeWidth: 1,
                  fill: '#fffbe6'
                }
              },
              data: {
                parent: true,
              },
              ports: parentPorts
            })
    : graph.createNode({
      shape: 'polygon',
      x: 40,
      y: 40,
      width: 120,
      height: 120,
      zIndex: 10,
      attrs: {
        label: {
          text: '条件节点',
          fill: '#000000',
          fontSize: 14,
          textWrap: {
            width: -50,
            height: '70%',
            ellipsis: true
          }
        },
        body: {
          fill: '#EE8B8B',
          stroke: '#000000',
          refPoints: '0,10 10,0 20,10 10,20',
          strokeWidth: 1
        }
      },
      data: {
        nodeTranToMode: [],
        des: "",
        parent: false,
      },
      ports: ports
    })
    const dnd = new Addon.Dnd({target:graph})
    dnd.start(node,e)
}
const ports = {
    groups: {
      // 输入链接桩群组定义
      top: {
        position: 'top',
        attrs: {
          circle: {
            r: 4,
            magnet: true,
            stroke: '#2D8CF0',
            strokeWidth: 2,
            fill: '#fff',
          },
        },
      },
      // 输出链接桩群组定义
      bottom: {
        position: 'bottom',
        attrs: {
          circle: {
            r: 4,
            magnet: true,
            stroke: '#2D8CF0',
            strokeWidth: 2,
            fill: '#fff',
          },
        },
      },
      left: {
        position: 'left',
        attrs: {
          circle: {
            r: 4,
            magnet: true,
            stroke: '#2D8CF0',
            strokeWidth: 2,
            fill: '#fff',
          },
        },
      },
      right: {
        position: 'right',
        attrs: {
          circle: {
            r: 4,
            magnet: true,
            stroke: '#2D8CF0',
            strokeWidth: 2,
            fill: '#fff',
          },
        },
      },
    },
    items: [
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
      }
    ],
}

const rectPorts = {
  groups: {
    // 输入链接桩群组定义
    top: {
      position: 'top',
      attrs: {
        circle: {
          r: 4,
          magnet: true,
          stroke: '#2D8CF0',
          strokeWidth: 2,
          fill: '#fff',
        },
      },
    },
    // 输出链接桩群组定义
    bottom: {
      position: 'bottom',
      attrs: {
        circle: {
          r: 4,
          magnet: true,
          stroke: '#2D8CF0',
          strokeWidth: 2,
          fill: '#fff',
        },
      },
    },
    left: {
      position: 'left',
      attrs: {
        circle: {
          r: 4,
          magnet: true,
          stroke: '#2D8CF0',
          strokeWidth: 2,
          fill: '#fff',
        },
      },
    },
    right: {
      position: 'right',
      attrs: {
        circle: {
          r: 4,
          magnet: true,
          stroke: '#2D8CF0',
          strokeWidth: 2,
          fill: '#fff',
        },
      },
    },
  },
  items: [
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
    }
  ],
}

const parentPorts = {
  groups: {
    // 输入链接桩群组定义
    top: {
      position: 'top',
      attrs: {
        circle: {
          r: 4,
          magnet: true,
          stroke: '#2D8CF0',
          strokeWidth: 2,
          fill: '#fff',
        },
      },
    },
    // 输出链接桩群组定义
    bottom: {
      position: 'bottom',
      attrs: {
        circle: {
          r: 4,
          magnet: true,
          stroke: '#2D8CF0',
          strokeWidth: 2,
          fill: '#fff',
        },
      },
    },
    left: {
      position: 'left',
      attrs: {
        circle: {
          r: 4,
          magnet: true,
          stroke: '#2D8CF0',
          strokeWidth: 2,
          fill: '#fff',
        },
      },
    },
    right: {
      position: 'right',
      attrs: {
        circle: {
          r: 4,
          magnet: true,
          stroke: '#2D8CF0',
          strokeWidth: 2,
          fill: '#fff',
        },
      },
    },
  },
  items: [
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
    {
      id: 'port1111',
      group: 'top',
    },
    {
      id: 'port2222',
      group: 'bottom',
    },
    {
      id: 'port3333',
      group: 'left',
    },
    {
      id: 'port4444',
      group: 'right',
    },
  ],
}
