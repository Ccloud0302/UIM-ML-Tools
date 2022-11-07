<template>
  <div class="container">
    <el-container style="height: 100%" direction="vertical">
      <UbmlHeader></UbmlHeader>
      <div id="graphcontainer" class="graphcontainer"></div>
    </el-container>
  </div>
</template>

<script>
import UbmlHeader from "../components/UbmlHeader";
import * as d3 from "d3";

export default {
  name: "contextIndex",
  components: {UbmlHeader},
  data() {
    return {
      dataSet3: {
        "name": "上下文",
        "value": 1222,
        "children": [
          {
            "name": "登录上下文1",
            "value": "a123",
            "children": [
              {"name": "login", "value": 100},
              {"name": "logout", "value": 200}
            ]
          },
          {
            "name": "摄相机上下文1",
            "value": "a122",
            "children": [
              {"name": "摄像机", "value": 700},
              {"name": "传感器", "value": 400}
            ]
          },
          {
            "name": "焊点上下文",
            "value": "a124",
            "children": [
              {"name": "机器人", "value": 100},
              {"name": "车架", "value": 200},
              {"name": "车顶", "value": 900},
              {"name": "传送带", "value": 500},
            ]
          },
          {
            "name": "焊点上下文",
            "value": "a125",
            "children": [
              {"name": "机器人", "value": 100},
              {"name": "车架", "value": 200},
              {"name": "车顶", "value": 900},
              {"name": "传送带", "value": 500},
            ]
          },
          {
            "name": "摄相机上下文",
            "value": "a121",
            "children": [
              {"name": "摄像机", "value": 700},
              {"name": "传感器", "value": 400}
            ]
          },


        ]
      },
      dataSet: {
        name: "家用电器",
        detail: [
          {
            name: "电视",
            detail: [
              {name: "曲面", revenue: 200},
              {name: "超薄", revenue: 100},
              {name: "配件", revenue: 50},
            ]
          },
          {
            name: "冰箱",
            detail: [
              {name: "格力", revenue: 200},
              {name: "美的", revenue: 150},
            ]
          }
        ]
      },
      data: {
        "name": "flare",
        "children": [
          {
            "name": "analytics",
            "children": [
              {
                "name": "cluster",
                "children": [
                  {"name": "AgglomerativeCluster", "value": 3938},
                  {"name": "CommunityStructure", "value": 3812},
                  {"name": "HierarchicalCluster", "value": 6714},
                  {"name": "MergeEdge", "value": 743}
                ]
              },
              {
                "name": "graph",
                "children": [
                  {"name": "BetweennessCentrality", "value": 3534},
                  {"name": "LinkDistance", "value": 5731},
                  {"name": "MaxFlowMinCut", "value": 7840},
                  {"name": "ShortestPaths", "value": 5914},
                  {"name": "SpanningTree", "value": 3416}
                ]
              },
              {
                "name": "optimization",
                "children": [
                  {"name": "AspectRatioBanker", "value": 7074}
                ]
              }
            ]
          },
          {
            "name": "animate",
            "children": [
              {"name": "Easing", "value": 17010},
              {"name": "FunctionSequence", "value": 5842},
              {
                "name": "interpolate",
                "children": [
                  {"name": "ArrayInterpolator", "value": 1983},
                  {"name": "ColorInterpolator", "value": 2047},
                  {"name": "DateInterpolator", "value": 1375}
                ]
              },
              {"name": "ISchedulable", "value": 1041},
              {"name": "Parallel", "value": 5176},
              {"name": "Pause", "value": 449},
              {"name": "Scheduler", "value": 5593},
              {"name": "Sequence", "value": 5534}
            ]
          },
          {
            "name": "data",
            "children": [
              {
                "name": "converters",
                "children": [
                  {"name": "Converters", "value": 721},
                  {"name": "DelimitedTextConverter", "value": 4294},
                  {"name": "GraphMLConverter", "value": 9800}
                ]
              },
              {"name": "DataField", "value": 1759}
            ]
          },
          {
            "name": "display",
            "children": [
              {"name": "DirtySprite", "value": 8833},
              {"name": "LineSprite", "value": 1732},
              {"name": "RectSprite", "value": 3623},
              {"name": "TextSprite", "value": 10066}
            ]
          },
          {
            "name": "flex",
            "children": [
              {"name": "FlareVis", "value": 4116}
            ]
          },
          {
            "name": "physics",
            "children": [
              {"name": "DragForce", "value": 1082},
              {"name": "GravityForce", "value": 1336}
            ]
          },
          {
            "name": "query",
            "children": [
              {"name": "AggregateExpression", "value": 1616},
              {"name": "And", "value": 1027},
              {"name": "Arithmetic", "value": 3891},
              {"name": "Match", "value": 3748},
              {"name": "Maximum", "value": 843},
              {
                "name": "methods",
                "children": [
                  {"name": "add", "value": 593},
                  {"name": "and", "value": 330},
                  {"name": "average", "value": 287}
                ]
              },
              {"name": "Minimum", "value": 843},
              {"name": "Not", "value": 1554},
              {"name": "Or", "value": 970}
            ]
          },
          {
            "name": "scale",
            "children": [
              {"name": "IScaleMap", "value": 2105},
              {"name": "ScaleType", "value": 1821},
              {"name": "TimeScale", "value": 833}
            ]
          },
          {
            "name": "util",
            "children": [
              {"name": "Arrays", "value": 8258},
              {"name": "Colors", "value": 1001},
              {"name": "Dates", "value": 8217},
              {"name": "Displays", "value": 1555},
              {"name": "Filter", "value": 2324},
              {"name": "Geometry", "value": 1093},
              {
                "name": "heap",
                "children": [
                  {"name": "FibonacciHeap", "value": 854},
                  {"name": "HeapNode", "value": 1233}
                ]
              },
              {"name": "IEvaluable", "value": 335},
              {"name": "IPredicate", "value": 383},
              {"name": "IValueProxy", "value": 874},
              {
                "name": "math",
                "children": [
                  {"name": "DenseMatrix", "value": 3165},
                  {"name": "IMatrix", "value": 2815},
                  {"name": "SparseMatrix", "value": 3366}
                ]
              },
              {"name": "Maths", "value": 1705},
              {"name": "Orientation", "value": 1486},
              {
                "name": "palette",
                "children": [
                  {"name": "ColorPalette", "value": 6367},
                  {"name": "Palette", "value": 1229},
                  {"name": "ShapePalette", "value": 2059},
                  {"name": "SizePalette", "value": 2291}
                ]
              },
              {"name": "Property", "value": 5559},
              {"name": "Shapes", "value": 1918},
              {"name": "Sort", "value": 6887},
              {"name": "Stats", "value": 6557},
              {"name": "Strings", "value": 2026}
            ]
          },
          {
            "name": "vis",
            "children": [
              {
                "name": "axis",
                "children": [
                  {"name": "Axes", "value": 1302},
                  {"name": "Axis", "value": 2493},
                  {"name": "AxisGridLine", "value": 652},
                  {"name": "AxisLabel", "value": 636},
                  {"name": "CartesianAxes", "value": 6703}
                ]
              },
              {
                "name": "controls",
                "children": [
                  {"name": "AnchorControl", "value": 2138},
                  {"name": "ClickControl", "value": 3824},
                  {"name": "Control", "value": 1353}
                ]
              },
              {
                "name": "data",
                "children": [
                  {"name": "Data", "value": 2544},
                  {"name": "DataList", "value": 1788},
                  {"name": "DataSprite", "value": 1049},
                  {"name": "EdgeSprite", "value": 3301},
                  {"name": "NodeSprite", "value": 1382},
                  {
                    "name": "render",
                    "children": [
                      {"name": "ArrowType", "value": 698},
                      {"name": "EdgeRenderer", "value": 5569},
                      {"name": "IRenderer", "value": 353},
                      {"name": "ShapeRenderer", "value": 2247}
                    ]
                  },
                  {"name": "ScaleBinding", "value": 1275},
                  {"name": "Tree", "value": 7147},
                  {"name": "TreeBuilder", "value": 9930}
                ]
              },
              {
                "name": "events",
                "children": [
                  {"name": "DataEvent", "value": 2313},
                  {"name": "SelectionEvent", "value": 1880},
                  {"name": "TooltipEvent", "value": 1701},
                  {"name": "VisualizationEvent", "value": 1117}
                ]
              },
              {
                "name": "legend",
                "children": [
                  {"name": "Legend", "value": 2859},
                  {"name": "LegendItem", "value": 4614},
                  {"name": "LegendRange", "value": 10530}
                ]
              },
              {
                "name": "operator",
                "children": [
                  {
                    "name": "distortion",
                    "children": [
                      {"name": "BifocalDistortion", "value": 4461},
                      {"name": "Distortion", "value": 6314},
                      {"name": "FisheyeDistortion", "value": 3444}
                    ]
                  },
                  {
                    "name": "encoder",
                    "children": [
                      {"name": "ColorEncoder", "value": 3179},
                      {"name": "Encoder", "value": 4060},
                      {"name": "PropertyEncoder", "value": 4138},
                      {"name": "ShapeEncoder", "value": 1690},
                      {"name": "SizeEncoder", "value": 1830}
                    ]
                  },
                  {
                    "name": "filter",
                    "children": [
                      {"name": "FisheyeTreeFilter", "value": 5219},
                      {"name": "GraphDistanceFilter", "value": 3165},
                      {"name": "VisibilityFilter", "value": 3509}
                    ]
                  },
                  {"name": "IOperator", "value": 1286},
                  {
                    "name": "label",
                    "children": [
                      {"name": "Labeler", "value": 9956},
                      {"name": "RadialLabeler", "value": 3899},
                      {"name": "StackedAreaLabeler", "value": 3202}
                    ]
                  },
                  {
                    "name": "layout",
                    "children": [
                      {"name": "AxisLayout", "value": 6725},
                      {"name": "BundledEdgeRouter", "value": 3727},
                      {"name": "CircleLayout", "value": 9317},
                      {"name": "CirclePackingLayout", "value": 2003},
                      {"name": "DendrogramLayout", "value": 4853}
                    ]
                  },
                  {"name": "Operator", "value": 2490},
                  {"name": "OperatorList", "value": 5248},
                  {"name": "OperatorSequence", "value": 4190},
                  {"name": "OperatorSwitch", "value": 2581},
                  {"name": "SortOperator", "value": 2023}
                ]
              },
              {"name": "Visualization", "value": 16540}
            ]
          }
        ]
      },
      view: null,
      contextLinks: [
        {
          "source_id": "a122",
          "target_id": "a121"
        },
        {
          "source_id": "a122",
          "target_id": "a123"
        },
        {
          "source_id": "a121",
          "target_id": "a123"
        },
        {
          "source_id": "a125",
          "target_id": "a124"
        },
      ],
      circleCoordinate: [],

    }
  },
  mounted() {
    this.init();
  },
  methods: {
    zoomTo(v) {
      var _this = this;
      const k = _this.height / v[2];
      _this.view = v;

      _this.links.attr('transform','translate(-530,-545)')
          .attr("stroke", "#5d7bde")
          .attr("stroke-width", "3")
          .attr("stroke-dasharray", "20,10,5,5,5,10");

      _this.label.attr("transform", d => `translate(${(d.x - v[0]) * k},${(d.y - v[1]) * k})`);
      _this.gcircles.attr("transform", d => `translate(${(d.x - v[0]) * k},${(d.y - v[1]) * k})`);
      _this.gcircles.attr("r", d => d.r * k);

      _this.refreshLink(_this.links);
    },

    zoom(d) {
      var _this = this;
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
    init() {
      var _this = this;
      _this.width = window.screen.width;
      _this.height = window.screen.height;//
      //数据
      var hierarchyData = d3.hierarchy(this.dataSet3).sum(function (d) {
        return d.value;
      }).sort((a, b) => b.value - a.value);

      var packcreator = d3.pack().size([_this.width, _this.height]).padding(3);
      var packdata = packcreator(hierarchyData);
      var allnodes = packdata.descendants();
      this.root = packdata;
      this.focus = "";

      //颜色
      var color = d3.scaleLinear()
          .domain([0, 5])
          .range(["hsl(152,80%,80%)", "hsl(228,30%,40%)"])
          .interpolate(d3.interpolateHcl)

      //绘图
      var graphcontainer = d3.select(".graphcontainer");
      this.svg = graphcontainer.append("svg");
      this.svg
          .attr("viewBox", `-${_this.width / 2} -${_this.height / 2} ${_this.width} ${_this.height}`)
          .style("display", "block")
          .style("margin", "0 -14px")
          .style("background", color(0))
          .style("cursor", "pointer")
          .on("click", () => _this.zoom(_this.root));


      // this.contextGroup = this.svg.append("g").attr("class", "context");
      // this.gcircles = this.contextGroup.selectAll("context")
      //     .data(allnodes)
      //     .enter().append("g").attr("transform", d => `translate(${d.x + 1},${d.y + 1})`)
      // ;
      // this.gcircles.append("circle").attr("cx", 0).attr("cy", 0).attr("r", function (d) {
      //   return d.r;
      // }).attr("fill", d => d.children ? color(d.depth) : "white")
      //     .attr("pointer-events", d => !d.children ? "none" : null)
      //     .on("mouseover", function() { d3.select(this).attr("stroke", "#000"); })
      //     .on("mouseout", function() { d3.select(this).attr("stroke", null); })
      //     .on('click',function(d){if (focus != d) _this.zoom(d), d3.event.stopPropagation();});

      _this.gcircles = this.svg.append("g").selectAll('circle')
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
          .on("click", (d) => focus !== d && (_this.zoom(d), d3.event.stopPropagation()));
      // .on('click',function(d){  if (_this.focus != d)  _this.zoom(d), d3.event.stopPropagation();});


      //添加文本
      _this.label = this.svg.append("g")
          .style("font", "20px sans-serif")
          .attr("pointer-events", "none")
          .attr("text-anchor", "middle")
          .selectAll("text")
          .data(allnodes)
          .enter()
          .append("text")
          .style("fill-opacity", d => d.parent === _this.root ? 1 : 0)
          .style("display", d => d.parent === _this.root ? "inline" : "none")
          .text(d => d.data.name);


      //连线
      //存放连线数据中所有处理好后的坐标信息
      _this.diameter_global = _this.width > _this.height ? _this.height : _this.width;
      _this.links = _this.svg.append('g')
          .style('stroke', '#ff0000')
          .attr("class", "packageLink")
          .selectAll('line')
          .data(_this.contextLinks)          //连线数据
          .enter().append('line');

      _this.zoomTo([_this.root.x, _this.root.y, _this.root.r * 2]);


      // _this.contextLinks.forEach(function (d) {
      //   console.log(d);
      //   //获取两个圆（source和target）的transform属性（包含坐标信息）和半径
      //   var source_transform = d3.select("#" + d.source_id).attr("transform");
      //   var target_transform = d3.select("#" + d.target_id).attr("transform");
      //   var r1 = d3.select("#" + d.source_id).attr("r");
      //   var r2 = d3.select("#" + d.target_id).attr("r");
      //
      //   //求初始情况下的两个圆心坐标
      //   _this.x1 = parseFloat(source_transform.slice(source_transform.indexOf("(") + 1, source_transform.indexOf(",")));
      //   _this.y1 = parseFloat(source_transform.slice(source_transform.indexOf(",") + 1, source_transform.indexOf(")")));
      //   _this.x2 = parseFloat(target_transform.slice(target_transform.indexOf("(") + 1, target_transform.indexOf(",")));
      //   _this.y2 = parseFloat(target_transform.slice(target_transform.indexOf(",") + 1, target_transform.indexOf(")")));
      //   console.log(_this.x1, _this.y1);
      //   console.log(_this.x2, _this.y2);
      //
      //
      //
      //   //求斜率(考虑斜率正无穷问题)
      //   if (_this.x1 !== _this.x2) {
      //     var k = (_this.y2 - _this.y1) / (_this.x2 - _this.x1);
      //   } else {
      //     var k;
      //   }
      //
      //   if (typeof (k) !== "undefined") {
      //     //求偏移量
      //     var x1_offset = Math.sqrt((r1 * r1) / (k * k + 1));
      //     var y1_offset = Math.sqrt((r1 * r1) / (k * k + 1)) * k;
      //     var x2_offset = Math.sqrt((r2 * r2) / (k * k + 1));
      //     var y2_offset = Math.sqrt((r2 * r2) / (k * k + 1)) * k;
      //     if (_this.x1 > _this.x2) {
      //       _this.x1 -= x1_offset;
      //       _this.y1 -= y1_offset;
      //       _this.x2 += x2_offset;
      //       _this.y2 += y2_offset;
      //     } else {
      //       _this.x1 += x1_offset;
      //       _this.y1 += y1_offset;
      //       _this.x2 -= x2_offset;
      //       _this.y2 -= y2_offset;
      //     }
      //   } else {
      //     if (_this.y1 > _this.y2) {
      //       _this.y1 -= r1;
      //       _this.y2 += r2;
      //     } else if (_this.y1 < _this.y2) {
      //       _this.y1 += r1;
      //       _this.y2 -= r2;
      //     }
      //   }
      //   console.log(_this.x1, _this.y1);
      //   console.log(_this.x2, _this.y2);
      //
      //   //存放每个坐标信息
      //   var temp_coordinate = {};
      //   temp_coordinate["id"] = d.source_id + "_" + d.target_id;
      //   temp_coordinate["x1"] = _this.x1;
      //   temp_coordinate["y1"] = _this.y1;
      //   temp_coordinate["x2"] = _this.x2;
      //   temp_coordinate["y2"] = _this.y2;
      //
      //   //添加到数组
      //   _this.circleCoordinate.push(temp_coordinate);
      // })
      //
      // //对连线添加坐标信息，每添加时调用上方的四个方法，向数组中查询这个连线处理后的坐标信息
      // _this.links.attr("x1", function (d) {return _this.getTranslateX1(d.source_id, d.target_id) + _this.diameter_global / 2;})
      //     .attr("y1", function (d) {return _this.getTranslateY1(d.source_id, d.target_id) + _this.diameter_global / 2;})
      //     .attr("x2", function (d) {return _this.getTranslateX2(d.source_id, d.target_id) + _this.diameter_global / 2;})
      //     .attr("y2", function (d) {return _this.getTranslateY2(d.source_id, d.target_id) + _this.diameter_global / 2;})
      //     .attr('transform','translate(-500,-500)');
    },
    getTranslateX1(source_id, target_id) {
      var link_id = source_id + "_" + target_id;
      return this.circleCoordinate.find((n) => n.id === link_id).x1;
    },

    getTranslateY1(source_id, target_id) {
      var link_id = source_id + "_" + target_id;
      return this.circleCoordinate.find((n) => n.id === link_id).y1;
    },

    getTranslateX2(source_id, target_id) {
      var link_id = source_id + "_" + target_id;
      return this.circleCoordinate.find((n) => n.id === link_id).x2;
    },
    getTranslateY2(source_id, target_id) {
      var link_id = source_id + "_" + target_id;
      return this.circleCoordinate.find((n) => n.id === link_id).y2;
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
    }


  }
}
</script>

<style scoped>

</style>