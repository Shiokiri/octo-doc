const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
})
module.exports = {
  devServer: {
    proxy: {
      '/backend': {
        target: 'http://101.43.177.191:8081',  // 后台接口地址
        changeOrigin: true,  //是否跨域
        pathRewrite: (path) => path.replace(/^\/backend/, '') // 重写路径
        
      }
    }
  }
}
