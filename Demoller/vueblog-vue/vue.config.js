// vue.config.js
const path = require('path') // 引入path模块
function resolve (dir) {
    return path.join(__dirname, dir) // path.join(_dirname)设置绝对路径
}

/**
 * @type {import('@vue/cli-service').ProjectOptions}
 */

module.exports = {
    publicPath: '/',
    outputDir: 'dist',
    lintOnSave: true,
    chainWebpack: (config) => {
        config.resolve.alias
            // 第一个参数：别名 第二个参数：路径
            .set('components', resolve('src/components'))
            .set('assets', resolve('src/assets'))
            .set('commonjs', resolve('src/commonjs'))
            .set('views', resolve('src/views'))
            .set('network', resolve('src/network'))
    },
    // chainWebpack: (config) => {
    //     config.module
    //         .rule('')
    //         .test(/mxClient\.js$/)
    //         .use('exports-loader')
    //         .loader('exports-loader?mxClient,mxToolbar,mxConnectionHandler,mxEllipse,mxConnectionConstraint,mxWindow,' +
    //             'mxObjectCodec,mxGraphModel,mxActor,mxPopupMenu,mxShape,mxEventObject,mxGraph,mxPopupMenuHandler,mxPrintPreview,' +
    //             'mxEventSource,mxRectangle,mxVertexHandler,mxMouseEvent,mxGraphView,mxCodecRegistry,mxImage,mxGeometry,' +
    //             'mxRubberband,mxConstraintHandler,mxKeyHandler,mxDragSource,mxGraphModel,mxEvent,mxUtils,mxEvent,mxCodec,mxCell,' +
    //             'mxConstants,mxPoint,mxGraphHandler,mxCylinder,mxCellRenderer,mxEvent,mxUndoManager')
    //         .end();
    //     config.module
    //         .rule("svg")
    //         .exclude.add(resolve("src/icons"))
    //         .end();
    //     config.module
    //         .rule("icons")
    //         .test(/\.svg$/)
    //         .include.add(resolve("src/icons"))
    //         .end()
    //         .use("svg-sprite-loader")
    //         .loader("svg-sprite-loader")
    //         .options({
    //             symbolId: "icon-[name]"
    //         })
    // }
};
